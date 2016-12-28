package com.gx.javaFX;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import netscape.javascript.JSObject;
import org.w3c.dom.Document;

import java.io.IOException;

public class UIComponent extends Application {
    private static final String resPath=UIComponent.class.getResource("resources")+"/";
    private static final String PAGE_INDEX =resPath+"index.html";
    private static final String CSS=resPath+"css/layout.css";
    private static final Double SIZE_WIDTH=1366.0;
    private static final Double SIZE_HEIGHT=768.0;
    private static WebEngine webEngine;
    private static MouseEvent drugEvent;
    private static Stage stage;
    @Override
    public void start(final Stage stage){
        UIComponent.stage =stage;
        stage.initStyle(StageStyle.TRANSPARENT);

        BorderPane border = new BorderPane();
        border.setTop(addGridPane());


        WebView webView=new WebView();
        border.setCenter(webView);
        webEngine=webView.getEngine();
        webEngine.load(PAGE_INDEX);
        Scene scene = new Scene(border, SIZE_WIDTH, SIZE_HEIGHT);
        scene.getStylesheets().add(CSS);
        stage.setScene(scene);
        webEngine.documentProperty().addListener(new ChangeListener<Document>() {
            @Override
            public void changed(ObservableValue<? extends Document> observableValue,
                                Document document, Document newDoc) {
                if (newDoc != null) {
                    JSObject win = (JSObject) webEngine.executeScript("window");
                    win.setMember("java", new App(stage,webEngine));
                    webEngine.documentProperty().removeListener(this);
                    //webEngine.executeScript("test('app');");
                }
            }
        });
        stage.show();
    }
    public GridPane addGridPane(){

        //col2 rightBox
        Button exitBtn = new Button();
        exitBtn.getStyleClass().add("btn");
        exitBtn.setId("btn-close");
        exitBtn.setTooltip(new Tooltip("退出"));
        exitBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Platform.exit();
            }
        });

        Button setBtn = new Button();
        setBtn.getStyleClass().add("btn");
        setBtn.setId("btn-set");
        setBtn.setTooltip(new Tooltip("设置"));
        setBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                webEngine.executeScript("setPage()");
            }
        });

        Button githubBtn = new Button();
        githubBtn.getStyleClass().add("btn");
        githubBtn.setId("btn-github");
        githubBtn.setTooltip(new Tooltip("打开github"));
        githubBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    Runtime.getRuntime().exec("cmd /c start https://github.com/tomatoTed/javaDeployer");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        HBox rightBox = new HBox();
        rightBox.setId("rightBox");
        rightBox.getChildren().addAll(githubBtn,setBtn,exitBtn);

        //col1 label
        Label label=new Label("  远程部署工具 v1.0");
        label.setId("title");

        //grid col1,col2分别是50%
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setMinWidth(SIZE_WIDTH);
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setHalignment(HPos.LEFT);
        column1.setPercentWidth(50);
        gridPane.getColumnConstraints().add(column1);

        ColumnConstraints column2 = new ColumnConstraints();
        column2.setHalignment(HPos.RIGHT);
        column2.setPercentWidth(50);
        gridPane.getColumnConstraints().add(column2);
        gridPane.addRow(0,label,rightBox);
        gridPane.setId("row");
        gridPane.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                drugEvent =event;
            }
        });
        gridPane.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stage.setX(event.getScreenX() - drugEvent.getSceneX());
                stage.setY(event.getScreenY() - drugEvent.getSceneY());
            }
        });
        return gridPane;
    }
    public static void main(String[] args) {
        launch(args);
    }
}
