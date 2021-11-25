package ui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class VuMeter extends Canvas {
    GraphicsContext gc = this.getGraphicsContext2D();

    public VuMeter(){
        super(150,550);
    }

    public void update(double value){
        int OrangeLevel = -15;
        int RedLevel = -5;
        int MindB = -40;
        int factor = 10;
        gc.clearRect(0,0,150,550);
        gc.setFill(Color.GREEN);
        if (value>OrangeLevel) gc.fillRect(10,200,40,300);
        else gc.fillRect(10,200+300-factor*(value-MindB),40,factor*(value-MindB));
        gc.setFill(Color.ORANGE);
        if (value>RedLevel) gc.fillRect(10,100,40,100);
        else gc.fillRect(10,100+100-factor*(value-OrangeLevel),40,factor*(value-OrangeLevel));
        gc.setFill(Color.RED);
        gc.fillRect(10,50+50-factor*(value-RedLevel),40,factor*(value-RedLevel));
    }
}
