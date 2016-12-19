package com.gx.javaDeployer;

import java.io.File;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * 监听器,针对ui的各种事件回调
 */
public class Listener {

	private UIComponent ui;
	public Listener(UIComponent ui){
		this.ui=ui;
	}
	//软件启动
	public void onLaunch() throws Exception {
		List<String> ignoreList=Arrays.asList(Util.readFile(Data.localIgnoreFile).split(","));
		//获取diff文件
		Local local=new Local();
		List<String> diffList=local.exec1(Data.localSvnCommand);
		//过滤diff文件
		Iterator<String> it=diffList.iterator();
		while(it.hasNext()){
			String s=it.next();
			if(s.matches("^src/main/filters.*$")||s.matches("^src/test/.*$")){//filter目录或者test目录不显示
				it.remove();
			}
		}

		//publish ignore fileName
		final Object[][] data=new Object[diffList.size()][2];
		for(int i=0,start=0,end=diffList.size()-1;i<diffList.size()&&start<=end;i++){
			boolean use=!ignoreList.contains(diffList.get(i));
			if(use){
				data[start][0]=true;
				data[start][1]=diffList.get(i);
				start++;
			}else{
				data[end][0]=false;
				data[end][1]=diffList.get(i);
				end--;
			}
		}
		ui.data=data;
	}
	public void onStart() throws Exception {
		Local local=new Local(ui);
		ui.setTextAsync("创建新的war包...");
		local.exec(Data.localMavenCommand);
		ui.setTextAsync("连接服务器...");
		Remote remote=new Remote(Data.host, Data.port, Data.username, Data.password,ui);
		ui.setTextAsync("正在传输文件...");
		remote.upload(Data.remoteTempDir, new File(Data.localWarFile));
		ui.setTextAsync("开始执行服务器脚本...");
		remote.shell(Data.remoteShell);
		remote.closeSession();
	}
	public void onRadioChange(int radioType) throws Exception{
		if(radioType==1){
			ui.setTextAsync("点击'开始部署'");
			ui.showArea();
		}else{
			ui.setTextAsync("分析差异文件 ...");
			ui.appendText("分析差异文件 ...");
			new Thread(new Runnable() {
				public void run() {
					try {
						while(ui.data==null){
							Thread.sleep(100);
						}
						ui.showTable();
						ui.setTextAsync("勾选要部署的文件后点击'开始部署'");
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}).start();


		}
	}

	public void onSelectedStart() throws Exception{
		//显示ui
		ui.setTextAsync("获取部署文件...");
		ui.showArea();
		//计算要部署的文件
		final StringBuffer ignore=new StringBuffer();
		boolean restart=false;
		List<BuildFile> publishList=new ArrayList<BuildFile>();
		ui.appendText("目标文件:");
		for(int i=0;i<ui.data.length;i++){
			Boolean publish=(Boolean) ui.data[i][0];
			String file=(String) ui.data[i][1];
			if(publish){
				BuildFile buildFile=getBuildFile(file);
				if(buildFile.isRestart()){restart=true;}//如果需要重启,记录标记
				publishList.add(buildFile);
				ui.appendText(buildFile.getResource()+" ====>> "+buildFile.getBuild());
			}else{
				ignore.append(",");
				ignore.append(file);
			}
		}
		//保存要忽略的文件
		new Thread(new Runnable() {
			public void run() {
				Util.writeFile(Data.localIgnoreFile, ignore.toString().replaceFirst(",", ""));
				ui.appendText("已经记录忽略的文件到ignore.txt ...");
			}
		}).start();
		if(restart){//如果需要重启服务器,才编译,否则直接部署
			ui.setTextAsync("正在编译目标文件...");
			ui.appendText("正在编译目标文件...");
			Local local=new Local(ui);
			local.exec(Data.localMavenCommand);
			ui.appendText("完成编译目标文件...");
		}

		ui.appendText("");
		ui.setTextAsync("连接服务器...");
		Remote remote=new Remote(Data.host, Data.port, Data.username, Data.password,ui);
		if(restart){//如果需要重启服务器
			ui.appendText("停止服务器...");
			remote.shell(Data.remoteShutdownShell);
		}
		ui.setTextAsync("准备上传目标文件...");
		ui.appendText("");
		for(int i=0;i<publishList.size();i++){
			ui.appendText("正在传输第"+(i+1)+"个文件...\n"+publishList.get(i).getLocalPath()+" ====>> "+publishList.get(i).getRemotePath());
			remote.upload(publishList.get(i).getRemotePath(),new File(publishList.get(i).getLocalPath()) );
			ui.appendText("");
		}

		if(restart){//如果需要重启服务器
			ui.appendText("传输完毕...准备启动服务器");
			remote.shell(Data.remoteStartupShell);
		}
		ui.appendText("部署完成...");
		ui.setTextAsync("部署完成...");
		remote.closeSession();
	}

	private BuildFile getBuildFile(String resourceFile){
		BuildFile buildFile=new BuildFile();
		buildFile.setResource(resourceFile);
		if(resourceFile.matches("^src/main/(java|resources)/.*$")){//classes目录下  "^.+\\.(properties|xml|java|p12)$"
			buildFile.setBuild(resourceFile.replaceAll("src/main/(java|resources)/","/WEB-INF/classes/").replace(".java", ".class"));
			buildFile.setLocalPath( Data.localBuildHome+buildFile.getBuild());
			buildFile.setRestart(true);
		}else if(resourceFile.matches("^src/main/webapp/.*$")){//webapp目录下,静态资源和jsp
			buildFile.setBuild(resourceFile.replace("src/main/webapp/","/"));
			buildFile.setLocalPath( Data.localAppHome+"/"+resourceFile);
			buildFile.setRestart(false);
		}else{
			buildFile.setBuild("/"+resourceFile);
			buildFile.setLocalPath( Data.localAppHome+"/"+resourceFile);
			buildFile.setRestart(false);
		}
		buildFile.setRemotePath(Data.remoteAppHome+buildFile.getBuild().replaceAll("/[0-9a-zA-z]+\\.[0-9a-zA-z]+$", ""));
		return buildFile;
	}
	private class BuildFile{
		private String resource;
		private String build;
		private String localPath;
		private String remotePath;
		private boolean restart;
		public String getResource() {
			return resource;
		}
		public void setResource(String resource) {
			this.resource = resource;
		}
		public String getBuild() {
			return build;
		}
		public void setBuild(String build) {
			this.build = build;
		}
		public String getLocalPath() {
			return localPath;
		}
		public void setLocalPath(String localPath) {
			this.localPath = localPath;
		}
		public String getRemotePath() {
			return remotePath;
		}
		public void setRemotePath(String remotePath) {
			this.remotePath = remotePath;
		}
		public boolean isRestart() {
			return restart;
		}
		public void setRestart(boolean restart) {
			this.restart = restart;
		}
		@Override
		public String toString() {
			return "BuildFile [resource=" + resource + ", build=" + build + ", localPath="
					+ localPath + ", remotePath=" + remotePath + ", restart=" + restart + "]";
		}

	}
	public static void main(String[] args) {
		System.out.println("src/main/filters/dev/jdbc.java".matches("^.+\\.(properties|xml|java|p12)$"));
	}
}