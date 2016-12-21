package com.gx.javaDeployer;

import java.util.HashMap;
import java.util.Map;

/**
 * 静态数据
 */
public class Data {
	public static final String host=Util.getValueFromProp("host");
	public static final int port=Integer.valueOf(Util.getValueFromProp("port"));
	public static final String username=Util.getValueFromProp("username");
	public static final String password=Util.getValueFromProp("password");

	public static final String localAppDir =Util.getValueFromProp("localAppDir");//本地工程路径
	public static final String localAppName=Util.getValueFromProp("localAppName");//本地工程名
	public static final String remoteAppName=Util.getValueFromProp("remoteAppName");//应用名称(webapps目录下的)
	public static final String remoteTomcatDir=Util.getValueFromProp("remoteTomcatDir");//远程tomcat路径
	public static final String remoteTempDir=Util.getValueFromProp("remoteTempDir");//远程存放war路径

	public static final String localMavenCommand="cd "+ localAppDir +";mvn clean install";//运行本地maven命令
	public static final String localSvnCommand="cd "+ localAppDir +";svn diff|findstr /b Index:";//运行本地svn命令
	public static final String localWarFile= localAppDir +"/target/"+localAppName+".war";//本地待上传war包路径
	public static final String localBuildDir = localAppDir +"/target/"+localAppName;//本地待上传class目录(target目录下)
	public static final String localIgnoreFile= localAppDir +"/.svn/ignore.txt";//本地忽略文件记录表



	public static final String remoteAppDir =remoteTomcatDir+"/webapps/"+remoteAppName;//远程应用目录
	public static final String remoteShell="cd "+remoteTempDir+";sh remote.sh "+remoteTomcatDir+" "+remoteAppName;
	public static final String remoteShutdownShell="cd "+remoteTempDir+";sh shutdown.sh "+remoteTomcatDir;
	public static final String remoteStartupShell="cd "+remoteTempDir+";sh startup.sh "+remoteTomcatDir;
	public static final Map<String,String> codeMap=new HashMap<>();
	static{
		codeMap.put("shutdown server","开始停止服务器");
		codeMap.put("kill server","强行kill服务器");
		codeMap.put("finished shutdown server","完成停止服务器");
		codeMap.put("backup war file","开始备份原来war包");
		codeMap.put("finished remove war file","删除原来war包");
		codeMap.put("finished backup war file","完成备份");
		codeMap.put("exract war file","开始拷贝并解压war包");
		codeMap.put("finished exract war file","完成解压war包");
		codeMap.put("copy jar file","开始拷贝jar包");
		codeMap.put("finished copy jar file","完成拷贝jar包");
		codeMap.put("start server","开始启动服务器");
		codeMap.put("finished start server","完成启动服务器");
		codeMap.put("show log","查看日志");
		codeMap.put("publish success","部署成功");
	}
	public static void main(String[] args) {
		System.out.println(remoteShutdownShell);
	}
	
}
