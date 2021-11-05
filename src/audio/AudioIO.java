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
        System.out.println(mixerName);
        if (mixer!=null)
            for (Line.Info s: mixer.getTargetLineInfo()) {
                System.out.println(s);
                //format are hide in mixer.targetLineInfo[0].formats
        }
        try {
            return (TargetDataLine) mixer.getLine(info);
        }catch (Exception e){
            try {
                System.out.println("buffer input par défaut");
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
        System.out.println(mixerName);
        if (mixer!=null)
            for (Line.Info s: mixer.getSourceLineInfo()) {
                System.out.println(s.toString());
        }
        try {
            return  (SourceDataLine) mixer.getLine(speakerInfo);
        }catch (Exception e){
            try {
                System.out.println("buffer output par défaut, utiliser un format adapté");
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

    public boolean swap(){
        if (audioProcessor.isThreadRunning()) stopAudioProcessing();
        else audioProcessor.run();
        return audioProcessor.isThreadRunning();
    }

    public void startAudioProcessing(String inputMixer, String outputMixer, int sampleRate, int frameSize){
        TargetDataLine targetDataLine = obtainAudioInput(inputMixer,sampleRate);
        SourceDataLine sourceDataLine = obtainAudioOutput(outputMixer,sampleRate);
        audioProcessor= new AudioProcessor(targetDataLine,sourceDataLine,frameSize);
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

    public void stopAudioProcessing(){
        audioProcessor.terminateAudioThread();
    }


    public static void  main(String[] args){
        printAudioMixers();
        AudioIO io = new AudioIO();
        io.startAudioProcessing("Réseau de microphones (Technolo","Périphérique audio principal",44000,1024);
    }
}
