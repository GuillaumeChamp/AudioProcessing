package ui;

import audio.AudioSignal;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

public class SignalView extends LineChart<Number,Number> {
    public SignalView(Axis<Number> axis, Axis<Number> axis1) {
        super(axis, axis1);
    }
    // Fixme : make it better
    public void updateData(AudioSignal signal){
        this.getData().removeAll(this.getData());
        XYChart.Series series = new XYChart.Series();
        int i = 0;
        for(double d : signal.getSampleBuffer()) {
            series.getData().add(new XYChart.Data<>(i, d));
            i++;
        }
        this.getData().add(series);
    }
}
