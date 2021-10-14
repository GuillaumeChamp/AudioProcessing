package audio;

import javax.sound.sampled.*;
import java.util.Arrays;

public class AudioIO {
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
        Mixer mixer = AudioSystem.getMixer(getMixerInfo(mixerName));
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        try {
            return (TargetDataLine) mixer.getLine(info);
        }catch (Exception e){
            try {
                return (TargetDataLine) AudioSystem.getLine(info);
            }catch (Exception ee){
                ee.printStackTrace();
                return null;
            }
        }
    }
    public static SourceDataLine obtainAudioOutput(String mixerName, int sampleRate){
        AudioFormat format = new AudioFormat( sampleRate,16, 1,true, true);
        Mixer mixer = AudioSystem.getMixer(getMixerInfo(mixerName));
        DataLine.Info speakerInfo = new DataLine.Info(SourceDataLine.class, format);
        try {
            return  (SourceDataLine) mixer.getLine(speakerInfo);
        }catch (Exception e){
            try {
                return (SourceDataLine) AudioSystem.getLine(speakerInfo);
            }catch (Exception ee){
                ee.printStackTrace();
                return null;
            }
        }
    }
    public static void  main(String[] args){
        printAudioMixers();
        System.out.println(obtainAudioOutput("Port Haut-parleurs (Realtek High Def",8000).toString());
        System.out.println(obtainAudioInput("Réseau de microphones (Technolo",8000).toString());
    }
}
