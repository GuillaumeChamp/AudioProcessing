import java.util.ArrayList;
import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javax.sound.sampled.LineUnavailableException;

public class App extends Application {
    Group root = new Group();
    SoundLoop microphone;

    public void start(Stage theStage) throws LineUnavailableException {
        theStage.setTitle("Vocoder");
        ArrayList<String> input = new ArrayList<>(); //store the keyboard input
        final long width = 512; //width of the window
        final long height = 512; //height of the window
        microphone = new SoundLoop();
        microphone.run();

        Canvas canvas = new Canvas(width, height);
        root.getChildren().add(canvas);
        DefineButton();
        Scene scene = new Scene(root,width,height);
        theStage.setScene(scene);
        theStage.show();
    }
    private void DefineButton() {
        Button play = new Button("play Raw");
        play.setOnAction(e->{
            if (microphone.active) {
                microphone.stop();
            }
            else microphone.play();
        });
        play.setTranslateX(100);
        play.setTranslateY(100);
        play.setVisible(true);
        play.setVisible(true);
        root.getChildren().add(play);
    }
    //Todo work on the close option
    //speaker.close();
    //microphone.close();
}

