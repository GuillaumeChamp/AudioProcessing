import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javax.sound.sampled.LineUnavailableException;

public class App extends Application {
    Group root = new Group();
    SoundLoop microphone;
    Canvas canvas;
    final long width = 500;
    final long height = 800;

    public void start(Stage theStage) throws LineUnavailableException {
        theStage.setTitle("Vocoder");

        microphone = new SoundLoop();
        microphone.run();

        canvas = new Canvas(width, height);
        root.getChildren().add(canvas);
        DefineButton();
        Scene scene = new Scene(root,width,height);

        new AnimationTimer() {
            public void handle(long currentNanoTime) {
                Draw();
            }
        }.start();
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
        play.setTranslateY(400);
        play.setTranslateX(250);
        play.setVisible(true);
        play.setVisible(true);
        root.getChildren().add(play);

        Button button= new Button("echo");
        button.setOnAction(e-> microphone.addEcho());
        button.setTranslateY(200);
        button.setTranslateX(200);
        root.getChildren().add(button);

        button= new Button("distortion");
        button.setOnAction(e-> microphone.addDistortion());
        button.setTranslateY(200);
        button.setTranslateX(300);
        root.getChildren().add(button);
    }
    //Todo work on the close option
    private void Close(){
        microphone.Close();
    }
    private void Draw(){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.drawImage(new Image("JavaLogo.png",width,height,true,true),50,0);
        //Todo : change picture to allow override of the previous state
        gc.setFill(Color.BLACK);
        gc.fillText("Echo : " + microphone.isEcho(),400,700);
        gc.fillText("Distortion : " + microphone.isDistortion(),400,710);
    }
}

