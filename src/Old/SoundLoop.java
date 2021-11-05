package Old;

import javax.sound.sampled.*;
import java.io.ByteArrayOutputStream;

public class SoundLoop implements Runnable {
    AudioFormat format = new AudioFormat( 8000.0f,16, 1,true, true);
    //Format propre au micro du PC
    private final TargetDataLine microphone;
    private final SourceDataLine speaker;
    boolean active = false;
    private boolean echo = false;
    private boolean distortion = false;

    public SoundLoop() throws LineUnavailableException {
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        microphone = (TargetDataLine) AudioSystem.getLine(info); //Fix the format to match with one available
        DataLine.Info speakerInfo = new DataLine.Info(SourceDataLine.class, format);
        speaker = (SourceDataLine) AudioSystem.getLine(speakerInfo);
    }
    public void addEcho(){
        /*if (!echo)control.setValue(2);
        else control.setValue(0);
        Disable if reverb not supported
         */
        echo=!echo;
    }

    public boolean isEcho() {
        return echo;
    }

    public boolean isDistortion() {
        return distortion;
    }

    public void addDistortion(){
        distortion=!distortion;
    }

    public void stop(){
        //TODO : build a stop option have a look at IT
        active=false;
    }
    public void play(){
        //TODO add pre-factor to be able to use the play methode with different arguments to set effect
        int numBytes = 0;
        ByteArrayOutputStream outputData = new ByteArrayOutputStream(); //long list of unlimited data
        int sampleRate = 1024;
        byte[] rawData = new byte[microphone.getBufferSize()/5];
        active=true;
        long uptime = 100000;
        while (numBytes<uptime){
            int numberOfByteRead = microphone.read(rawData,0,sampleRate);
            numBytes += numberOfByteRead;
            //Todo : change the rawData by rawData*amp
            //Todo : add a limit option
            outputData.write(rawData,0,numberOfByteRead/2);
            speaker.write(rawData,0,numberOfByteRead);
            if (numBytes>1000 & echo){
                try {
                    speaker.write(rawData, numberOfByteRead/2-1, numberOfByteRead/2);
                }catch (IndexOutOfBoundsException e){
                    e.printStackTrace();
                    System.out.println("Echo : Fix me");
                }
            }
        }
        active=false;
        speaker.drain(); //clear data remain in the speaker buffer
    }

    @Override
    public void run() {
        try {
            microphone.open(format); //Equivalent to init
            /*
            try {
                control = (FloatControl) speaker.getControl(FloatControl.Type.REVERB_SEND);
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("Reverb not supported ");
            }

             */

            microphone.start();
            speaker.open(format);
            speaker.start();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Unable to read");
        }
    }
    public void Close(){
        speaker.drain();
        speaker.close();
        microphone.close();
    }

    public static void main(String[] args) throws LineUnavailableException {
            SoundLoop sound = new SoundLoop();
            sound.run();
            sound.play();
        }
    }


