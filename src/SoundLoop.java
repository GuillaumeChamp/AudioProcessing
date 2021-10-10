import javax.sound.sampled.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SoundLoop implements Runnable {
    AudioFormat format = new AudioFormat( 8000.0f,16, 1,true, true);
    //Format propre au micro du PC
    TargetDataLine microphone;
    SourceDataLine speaker;
    long uptime = 1000000;

    public SoundLoop() throws LineUnavailableException {
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        microphone = (TargetDataLine) AudioSystem.getLine(info); //Fix the format to match with one available
        DataLine.Info speakerInfo = new DataLine.Info(SourceDataLine.class, format);
        speaker = (SourceDataLine) AudioSystem.getLine(speakerInfo);
    }


    private void play(){
        int numBytes = 0;
        ByteArrayOutputStream outputData = new ByteArrayOutputStream(); //long list of unlimited data
        int sampleRate = 1024;
        byte[] rawData = new byte[microphone.getBufferSize()/5];

        while (numBytes < uptime){
            int numberOfByteRead = microphone.read(rawData,0,sampleRate);
            numBytes += numberOfByteRead;
            outputData.write(rawData,0,numberOfByteRead);
            speaker.write(rawData,0,numberOfByteRead);
        }
        speaker.drain(); //clear data remain in the speaker buffer
        speaker.close();
        microphone.close();
    }

    @Override
    public void run() {
        try {
            microphone.open(format); //Equivalent to init
            microphone.start();
            speaker.open(format);
            speaker.start();

            this.play();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Unable to read");
        }
    }

    public static void main(String[] args) throws LineUnavailableException {
            new SoundLoop().run();
        }
    }


