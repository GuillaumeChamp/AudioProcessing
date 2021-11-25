package ui;

import audio.AudioSignal;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

public class SignalView extends LineChart<Number,Number> {
    XYChart.Series series = new XYChart.Series();
    public SignalView(Axis<Number> axis, Axis<Number> axis1) {
        super(axis, axis1);
        series.setName("Data");
        this.getData().add(series);
    }

    public void updateData(AudioSignal signal){
        series.getData().clear();
        int i = 0;
        for(double d : signal.getSampleBuffer()) {
            series.getData().add(new XYChart.Data<>(i, d));
            i++;
        }
    }
}
