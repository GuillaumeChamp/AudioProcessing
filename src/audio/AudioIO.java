package audio;

import javax.sound.sampled.*;
import java.util.Arrays;

public class AudioIO {
    AudioProcessor audioProcessor;

    public static void printAudioMixers(){
        System.out.println("Mixers:");
        Arrays.stream(AudioSystem.getMixerInfo())
                .forEach(e-> System.out.println("- name= \"" + e.getName()
                + "\" description = \"" + e.getDescription() + "by" + e.getVendor() + "\""));
    }
    public static Mixer.Info getMixerInfo(String mixerName){
        return Arrays.stream(AudioSystem.getMixerInfo())
        .filter(e -> e.getName().equalsIgnoreCase(mixerName)).findFirst().get();
    }
    public static TargetDataLine obtainAudioInput(String mixerName, int sampleRate){
        AudioFormat format = new AudioFormat( sampleRate,16, 1,true, true);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        Mixer mixer = null;
        try {
            mixer = AudioSystem.getMixer(getMixerInfo(mixerName));
        }catch (Exception ignored){
        }
        if (mixer!=null)
            for (Line.Info s: mixer.getTargetLineInfo()) {
                System.out.println(s);
                //format are hide in mixer.targetLineInfo[0].formats
        }
        try {
            assert mixer != null;
            return (TargetDataLine) mixer.getLine(info);
        }catch (Exception e){
            try {
                System.out.println("AudioInput default pick a better one");
                return (TargetDataLine) AudioSystem.getLine(info);
            }catch (Exception ee){
                ee.printStackTrace();
                return null;
            }
        }
    }
    public static SourceDataLine obtainAudioOutput(String mixerName, int sampleRate){
        AudioFormat format = new AudioFormat( sampleRate,16, 1,true, true);
        DataLine.Info speakerInfo = new DataLine.Info(SourceDataLine.class, format);
        Mixer mixer = null;
        try {
            mixer = AudioSystem.getMixer(getMixerInfo(mixerName));
        }catch (Exception ignored){
        }
        if (mixer!=null)
            for (Line.Info s: mixer.getSourceLineInfo()) {
                System.out.println(s.toString());
        }
        try {
            assert mixer != null;
            return  (SourceDataLine) mixer.getLine(speakerInfo);
        }catch (Exception e){
            try {
                System.out.println("AudioOutput Default pick a better one");
                return (SourceDataLine) AudioSystem.getLine(speakerInfo);
            }catch (Exception ee){
                ee.printStackTrace();
                return null;
            }
        }
    }
    public AudioSignal getData(){
        return audioProcessor.getOutputSignal();
    }

    public double getdB(){
        return audioProcessor.getOutputSignal().getDBLevel();
    }

    public boolean swap(){
        if (audioProcessor.isThreadRunning()) audioProcessor.terminateAudioThread();
        else audioProcessor.run();
        return audioProcessor.isThreadRunning();
        //Fixme : use resume instead of run but fix it before
    }

    public void startAudioProcessing(String inputMixer, String outputMixer, int sampleRate, int frameSize){
        TargetDataLine targetDataLine = obtainAudioInput(inputMixer,sampleRate);
        SourceDataLine sourceDataLine = obtainAudioOutput(outputMixer,sampleRate);
        audioProcessor = new AudioProcessor(targetDataLine,sourceDataLine,frameSize);
        try {
            assert sourceDataLine != null;
            sourceDataLine.open();
            sourceDataLine.start();
            assert targetDataLine != null;
            targetDataLine.open();
            targetDataLine.start();
            new Thread(audioProcessor).start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void  main(String[] args){
        AudioIO io = new AudioIO();
        io.startAudioProcessing("Réseau de microphones (Technolo","Périphérique audio principal",44000,1024);
    }
}
