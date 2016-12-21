package com.gx.javaDeployer;


public class Main {


	public static void main(String[] args) throws Exception {
		if(args.length>0){//指定配置文件
			Util.setConfigFile(args[0]);
		}
		final UIComponent ui=new UIComponent();
		ui.setListener(new Listener(ui));
		if(!"test".equals(Util.getValueFromPom("properties/war.type"))){
			ui.appendText("警告:pom类型不是test");
		}
	}
}
