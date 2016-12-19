package com.gx.javaDeployer;



public class Main {


	public static void main(String[] args) throws Exception {
		final UIComponent ui=new UIComponent();
		ui.setListener(new Listener(ui));
		if(!"test".equals(Util.getValueFromPom("properties/war.type"))){
			ui.appendText("pom类型不是test");
		}
	}
}
