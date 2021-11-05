package audio;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public class AudioProcessor implements Runnable {
    private final AudioSignal inputSignal, outputSignal;
    private final TargetDataLine audioInput;
    private final SourceDataLine audioOutput;
    private boolean isThreadRunning;

    public AudioProcessor(TargetDataLine audioInput, SourceDataLine audioOutput, int frameSize) {
        this.audioInput = audioInput;
        this.audioOutput = audioOutput;
        inputSignal = new AudioSignal(frameSize);
        outputSignal = new AudioSignal(frameSize);
    }

    public boolean isThreadRunning() {
        return isThreadRunning;
    }

    public AudioSignal getOutputSignal() {
        return outputSignal;
    }

    @Override
    public void run() {
        isThreadRunning = true;
        System.out.println("run");
        while (isThreadRunning){
            inputSignal.recordFrom(audioInput);
            outputSignal.setFrom(inputSignal);
            outputSignal.playTo(audioOutput);
        }
    }
    public void terminateAudioThread(){
        isThreadRunning =false;
    }

    public static void main(String[] args){
        TargetDataLine inLine = AudioIO.obtainAudioInput("Réseau de microphones (Technolo",44000);
        SourceDataLine outLine = AudioIO.obtainAudioOutput("Périphérique audio principal",44000);
        AudioProcessor as = new AudioProcessor(inLine,outLine,1024);
        try {
            inLine.open();
            inLine.start();
            outLine.open();
            outLine.start();
            new Thread(as).start();

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
