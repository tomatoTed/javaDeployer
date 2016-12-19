package com.gx.javaDeployer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class Local {
	private Runtime runtime;
	private UIComponent ui;

	public Local(UIComponent ui) {
		this.runtime =Runtime.getRuntime();
		this.ui=ui;
	}
	public Local() {
		this.runtime =Runtime.getRuntime();
	}

	public void exec(final String command) throws IOException, InterruptedException{
		if(ui!=null){
			ui.appendText(">> "+command+"\n");
		}
		Process p=runtime.exec("powershell /c "+command);
		InputStream is=p.getInputStream();
		InputStreamReader isReader=new InputStreamReader(is);
	    BufferedReader bufferReader = new BufferedReader(isReader);
	    String line = null ;
        while ((line=bufferReader.readLine())!=null) {
        	if(ui!=null){
        		ui.appendText(line);
    		}
			if (line.contains("Final Memory")) {
				break;
			}
        }
        is.close();
        p.destroy();
	}

	public List<String> exec1(String command) throws IOException, InterruptedException{
		if(ui!=null)ui.appendText(">> "+command+"\n");
		Process p=runtime.exec("powershell /c "+command);
		InputStream is=p.getInputStream();
		InputStreamReader isReader=new InputStreamReader(is);
	    BufferedReader bufferReader = new BufferedReader(isReader);
	    String line = null ;
	    List<String> fileList=new ArrayList<String>();
        while ((line=bufferReader.readLine())!=null) {
        	if(ui!=null)ui.appendText(line);
        	 line=line.replace("Index: ", "");
        	 if(line.matches(".+\\.[0-9a-zA-Z-]+$")){
        		 fileList.add(line);
        	 }
        }
        is.close();
        p.destroy();
        return fileList;
	}
	public static void main(String[] args) {
		System.out.println("src/main/webapp/WEB-INF/jsp/crossmind/qamgr/userList.jsp".matches(".+\\.[0-9a-zA-Z-]+$"));
		System.out.println("src/main/java/cn/com/crossmind/web/sysmgr/utils/UserUtils.java".matches(".+\\.[0-9a-zA-Z-]+$"));
	}
}
