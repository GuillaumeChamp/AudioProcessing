import java.io.IOException;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.animation.AnimationTimer;

public class App extends Application {
    public void start(Stage theStage) {
        theStage.setTitle("Render");
        ArrayList<String> input = new ArrayList<>(); //store the keyboard input
        final long width = 512; //width of the window
        final long height = 512; //height of the window

        Group root = new Group();
        final long startNanoTime = System.nanoTime();

        Canvas canvas = new Canvas(width, height);
        root.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        try {
            SoundLoop microphone = new SoundLoop();

            new AnimationTimer() {
                public void handle(long currentNanoTime) {
                    double t = (currentNanoTime - startNanoTime) / 1000000000.0;
                }
            }.start();
            theStage.show();
        } catch (Exception e) {
            System.out.println("unable to read the microphone input");
        }

    }
}

