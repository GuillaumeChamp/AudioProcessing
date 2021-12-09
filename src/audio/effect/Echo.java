package audio.effect;

import audio.AudioSignal;

/**
 * Only a sample of the possibilities
 */
public class Echo implements Effect {
    int delay = 200;

    /**
     * The value for this effect is the delay (in sample) between the ripple
     * @param v value of the new ripple time
     */
    @Override
    public void setValue(int v) {
        delay=v;
    }

    /**
     * Apply the effect to the audioSignal which can be either the out or the in depending of if you want it to be printed
     * @param as the in or out signal
     */
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
