package audio.effect;

import audio.AudioSignal;

public class Echo implements Effec{
    int delay = 200;

    @Override
    public void setValue(int v) {
        delay=v;
    }

    @Override
    public void apply(AudioSignal as) {
        double[] NewBuffer = new double[as.getSampleBuffer().length];
        double[] OlbBuffer = as.getSampleBuffer();
        for (int i= 0; i<as.getSampleBuffer().length;i++){
            NewBuffer[i] = OlbBuffer[i];
            if (i>delay) NewBuffer[i] =+ OlbBuffer[i-delay];
            if (i>2*delay) NewBuffer[i] =+ 0.8*OlbBuffer[i-2*delay];
            if (i>3*delay) NewBuffer[i] =+ 0.4*OlbBuffer[i-3*delay];
        }
        as.setSampleBuffer(NewBuffer);
    }
}
