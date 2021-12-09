package audio;
import audio.effect.Effec;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

/**
 * A class holding all the processing part and the thread
 */
public class AudioProcessor implements Runnable {
    private final AudioSignal inputSignal, outputSignal;
    private final TargetDataLine audioInput;
    private final SourceDataLine audioOutput;
    private boolean isThreadRunning;
    public static Effec effec=null;

    /**
     * Create a new audio processor
     * @param audioInput input line
     * @param audioOutput output line
     * @param frameSize size of the both frame
     */
    public AudioProcessor(TargetDataLine audioInput, SourceDataLine audioOutput, int frameSize) {
        this.audioInput = audioInput;
        this.audioOutput = audioOutput;
        inputSignal = new AudioSignal(frameSize);
        outputSignal = new AudioSignal(frameSize);
    }

    /**
     * Core of the thread, copy the signal from the input to the output and apply the effect
     */
    @Override
    public void run() {
        isThreadRunning = true;
        try {
            audioInput.open();
            audioInput.start();
            audioOutput.open();
            audioOutput.start();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
            System.out.println("unable to open or start");
        }
        while (isThreadRunning){
            inputSignal.recordFrom(audioInput);
            outputSignal.setFrom(inputSignal);
            if (effec!=null) effec.apply(outputSignal);
            outputSignal.playTo(audioOutput);
        }
    }

    /**
     * End properly the thread to allow to start a new one
     */
    public void terminateAudioThread(){
        isThreadRunning =false;
        audioOutput.stop();
        audioOutput.close();
        audioOutput.stop();
        audioInput.close();
    }

    /**
     * getter function useful to print data
     * @return
     */
    public AudioSignal getInputSignal() {
        return inputSignal;
    }

    /**
     * Test of the class /dead code/
     * only keep for the mark
     * @param args
     */
    public static void main(String[] args){
        TargetDataLine inLine = AudioIO.obtainAudioInput("Réseau de microphones (Technolo",44000);
        SourceDataLine outLine = AudioIO.obtainAudioOutput("Périphérique audio principal",44000);
        AudioProcessor as = new AudioProcessor(inLine,outLine,1024);
        try {
            assert inLine != null;
            inLine.open();
            inLine.start();
            assert outLine != null;
            outLine.open();
            outLine.start();
            new Thread(as).start();

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
