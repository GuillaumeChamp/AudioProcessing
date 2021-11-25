package audio;

import javax.sound.sampled.*;
import java.util.Arrays;

/**
 * Class with only statics methods used for Input/Output process
 */
public class AudioIO {
    /**
     * old methods used to check the name of all available mixer
     */
    public static void printAudioMixers(){
        System.out.println("Mixers:");
        Arrays.stream(AudioSystem.getMixerInfo())
                .forEach(e-> System.out.println("- name= \"" + e.getName()
                + "\" description = \"" + e.getDescription() + "by" + e.getVendor() + "\"") );
    }

    /**
     * Recover the info of a mixer
     * @param mixerName name of the mixer
     * @return Info of the mixer
     */
    public static Mixer.Info getMixerInfo(String mixerName){
        return Arrays.stream(AudioSystem.getMixerInfo())
        .filter(e -> e.getName().equalsIgnoreCase(mixerName)).findFirst().get();
    }
    /**
     * This methode return a target data line matching with the name
     * @param mixerName name of the desired mixer
     * @param sampleRate line sample rate
     * @return the a target data line if found or a default one
     */
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

    /**
     * This methode return a source data line matching with the name
     * @param mixerName name of the desired mixer
     * @param sampleRate line sample rate
     * @return the a source data line if found or a default one
     */
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

    /**
     * Start a new Audio thread
     * @param inputMixer name of the In material
     * @param outputMixer name of the Out material
     * @param sampleRate must be under 20 kHz
     * @param frameSize Size of the buffer (a big one allow to do cool effect but make time to be printed)
     * @return
     */
    public static AudioProcessor startAudioProcessing(String inputMixer, String outputMixer, int sampleRate, int frameSize){
        TargetDataLine targetDataLine = obtainAudioInput(inputMixer,sampleRate);
        SourceDataLine sourceDataLine = obtainAudioOutput(outputMixer,sampleRate);
        AudioProcessor audioProcessor = new AudioProcessor(targetDataLine,sourceDataLine,frameSize);
        try {
            assert sourceDataLine != null;
            sourceDataLine.open();
            sourceDataLine.start();
            assert targetDataLine != null;
            targetDataLine.open();
            targetDataLine.start();
            new Thread(audioProcessor).start();
            return audioProcessor;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
