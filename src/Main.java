import audio.AudioIO;
import audio.AudioProcessor;
import audio.effect.Echo;
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
import ui.SignalView;
import ui.VuMeter;

import javax.sound.sampled.AudioSystem;
import java.util.Arrays;

public class Main extends Application {
    SignalView signalView = new SignalView(new NumberAxis(),new NumberAxis(-0.1,0.1,0.0001));
    VuMeter vuMeter = new VuMeter();
    AudioProcessor io;
    boolean running= false;
    String InputMixer,OutputMixer;

    /**
     * Starting the app and the scene with an animation timer
     * @param primaryStage the stage
     */
    public void start(Stage primaryStage) {
        try {
            BorderPane root = new BorderPane();
            root.setTop(createToolbar());
            root.setBottom(createStatus());
            root.setCenter(createMainContent());
            Scene scene = new Scene(root,1500,800);
            primaryStage.setScene(scene);
            primaryStage.setTitle("The JavaFX audio processor");
            new AnimationTimer() {
                public void handle(long currentNanoTime) {
                    UpdateView();
                }
            }.start();
            primaryStage.show();
            }
        catch(Exception e) {e.printStackTrace();}
        }

    /**
     * Create the top of the GUI
      * @return top elements
     */
    private Node createToolbar(){
        Button button = new Button("Stop !");
        Button button1 = new Button("Start");
        button1.setOnAction(e-> {
            io = AudioIO.startAudioProcessing(InputMixer,OutputMixer,22000,2048);
            running = true;
        });
        button.setOnAction(event -> active());
        ComboBox<String> cb = new ComboBox<>();
        Arrays.stream(AudioSystem.getMixerInfo())
            .forEach(e->cb.getItems().add(e.getName()));
        cb.setOnAction(e->
                InputMixer = cb.getValue());
        ComboBox<String> cb1 = new ComboBox<>();
        Arrays.stream(AudioSystem.getMixerInfo())
            .forEach(e->cb1.getItems().add(e.getName()));
        cb1.setOnAction(e->
                OutputMixer = cb1.getValue());
        return new ToolBar(button1,button, new Separator(), new Label("Input Mixer"),cb,new Label("outputMixer"), cb1);
    }

    /**
     * Create the bottom part the the GUI
     * @return the bottom elements
     */
    private Node createStatus(){
        HBox status = new HBox();
        ComboBox<String> cb = new ComboBox<>();
        cb.getItems().addAll("None","Echo");
        cb.setOnAction(e-> {
            if (cb.getValue().equals("Echo")) AudioProcessor.effect = new Echo();
        });
        TextField tx = new TextField(" ");
        tx.setOnAction(e-> AudioProcessor.effect.setValue(Integer.parseInt(tx.getText())));
        status.getChildren().addAll(new Label("Effect"),cb,new Label("Value"),tx);
        return status;
    }

    /**
     * Create the center part of the GUI
     * @return the node holding all elements
     */
    private Node createMainContent(){
        Group g = new Group();
        g.getChildren().add(vuMeter);
        vuMeter.setTranslateX(-100);
        g.getChildren().add(signalView);
        return g;
    }

    /**
     * Update all view if the thread is running
     */
    private void UpdateView(){
        if (running) {
            signalView.updateData(io.getInputSignal());
            vuMeter.update(io.getInputSignal().getDBLevel());
        }
    }

    /**
     * Audio thread manager
     */
    private void active(){
        if (io!=null)   io.terminateAudioThread();
        running=false;
    }
}