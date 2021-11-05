package ui;

import audio.AudioIO;
import audio.AudioProcessor;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;


public class Main extends Application {
    SignalView signalView = new SignalView(new NumberAxis(),new NumberAxis());
    AudioIO io = new AudioIO();
    Boolean running = true;

        public void start(Stage primaryStage) {
        try {
            BorderPane root = new BorderPane();
            root.setTop(createToolbar());
            root.setBottom(createStatusbar());
            root.setCenter(createMainContent());
            Scene scene = new Scene(root,1500,800);
            primaryStage.setScene(scene);
            primaryStage.setTitle("The JavaFX audio processor");
            io.startAudioProcessing("Réseau de microphones (Technolo","Périphérique audio principal",44000,1024);
            new AnimationTimer() {
                public void handle(long currentNanoTime) {
                    Draw();
                }
            }.start();
            primaryStage.show();
            } catch(Exception e) {e.printStackTrace();}
        }

        private Node createToolbar(){
        Button button = new Button("Stop !");
        ToolBar tb = new ToolBar(button, new Label("ceci est un label"), new Separator());
        button.setOnAction(event -> active());
        ComboBox<String> cb = new ComboBox<>();
        cb.getItems().addAll("Item 1", "Item 2", "Item 3");
        tb.getItems().add(cb);
        return tb;
        }

        private Node createStatusbar(){
        HBox statusbar = new HBox();
        statusbar.getChildren().addAll(new Label("Name:"), new TextField(" "));
        return statusbar;
        }
        private Node createMainContent(){
        Group g = new Group();
        g.getChildren().add(signalView);
        return g;
        }
        private void Draw(){
            if (running) signalView.updateData(io.getData());
        }
        private void active(){
            running = io.swap();
        }
}