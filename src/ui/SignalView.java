package ui;

import audio.AudioSignal;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

public class SignalView extends LineChart<Number,Number> {
    XYChart.Series series = new XYChart.Series();

    /**
     * Main constructor, set the parameters for the char
     * @param axis can be a new, first axis of the chart
     * @param axis1 can be a new, second axis of the chart
     */
    public SignalView(Axis<Number> axis, Axis<Number> axis1) {
        super(axis, axis1);
        this.getYAxis().setAutoRanging(false);

        series.setName("Input in sample");
        this.getData().add(series);
    }

    /**
     * Update the data to represent the signal
     * @param signal signal to print
     */
    public void updateData(AudioSignal signal){
        series.getData().clear();
        int i = 0;
        for(double d : signal.getSampleBuffer()) {
            series.getData().add(new XYChart.Data<>(i, d));
            i++;
        }
    }
}
