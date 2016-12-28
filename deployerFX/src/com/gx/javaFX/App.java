package com.gx.javaFX;

import javafx.scene.web.WebEngine;
import javafx.stage.Stage;

/**
 * Created by gaoxiang on 2016/12/27.
 */
public class App {
    private Stage stage;
    private WebEngine webEngine;
    public App(Stage stage, WebEngine webEngine){
        this.stage=stage;
        this.webEngine=webEngine;
    }
    public void sayHello(){
        System.out.println("hello");
    }
    public void exit(){
        stage.close();
    }
}
