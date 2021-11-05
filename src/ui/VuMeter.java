package ui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class VuMeter extends Canvas {
    GraphicsContext gc = this.getGraphicsContext2D();

    public VuMeter(){
        super(1000,300);
    }

    public void update(double value){
        gc.setFill(Color.ORANGE);
        if (value<40) gc.setFill(Color.GREEN);
        if (value>60) gc.setFill(Color.RED);
        gc.fillRect(20,100-100*value,40,100*value);
    }
}
