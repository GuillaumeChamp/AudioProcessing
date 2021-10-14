package audio;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public class AudioProcessor implements Runnable {
    private AudioSignal inputSignal, outputSignal;
    private TargetDataLine audioInput;
    private SourceDataLine audioOutput;
    private boolean isThreadRunning;

    public AudioProcessor(TargetDataLine audioInput, SourceDataLine audioOutput, int frameSize) {
        this.audioInput = audioInput;
        this.audioOutput = audioOutput;
    }

    @Override
    public void run() {
        isThreadRunning = true;
        while (isThreadRunning){
            inputSignal.recordFrom(audioInput);
            outputSignal.playTo(audioOutput);
        }
    }
    public void terminateAudioThread(){
        isThreadRunning =false;
    }

    public static void main(String[] args) {
        TargetDataLine inLine = AudioIO.obtainAudioInput("Default Audio Device",16000);
        SourceDataLine outLine = AudioIO.obtainAudioOutput("Default Audio Device",16000);
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
