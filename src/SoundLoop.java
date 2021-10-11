import javax.sound.sampled.*;
import java.io.ByteArrayOutputStream;

public class SoundLoop implements Runnable {
    AudioFormat format = new AudioFormat( 8000.0f,16, 1,true, true);
    //Format propre au micro du PC
    TargetDataLine microphone;
    SourceDataLine speaker;
    boolean active = false;

    public SoundLoop() throws LineUnavailableException {
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        microphone = (TargetDataLine) AudioSystem.getLine(info); //Fix the format to match with one available
        DataLine.Info speakerInfo = new DataLine.Info(SourceDataLine.class, format);
        speaker = (SourceDataLine) AudioSystem.getLine(speakerInfo);
    }

    public void stop(){
        //TODO : build a stop option
        active=false;
    }
    public void play(){
        //TODO add pre-factor to be able to use the play methode with different arguments to set effect
        int numBytes = 0;
        ByteArrayOutputStream outputData = new ByteArrayOutputStream(); //long list of unlimited data
        int sampleRate = 1024;
        byte[] rawData = new byte[microphone.getBufferSize()/5];

        while (numBytes<100000){
            int numberOfByteRead = microphone.read(rawData,0,sampleRate);
            numBytes += numberOfByteRead;
            outputData.write(rawData,0,numberOfByteRead);
            speaker.write(rawData,0,numberOfByteRead);
        }
        active=false;
        speaker.drain(); //clear data remain in the speaker buffer
    }

    @Override
    public void run() {
        try {
            microphone.open(format); //Equivalent to init
            microphone.start();
            speaker.open(format);
            speaker.start();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Unable to read");
        }
    }

    public static void main(String[] args) throws LineUnavailableException {
            SoundLoop sound = new SoundLoop();
            sound.run();
            sound.play();

        }
    }


