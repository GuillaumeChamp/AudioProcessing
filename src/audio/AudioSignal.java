package audio;

import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public class AudioSignal {
    private double[] sampleBuffer;
    private double dBlevel;

    public AudioSignal(int frameSize){
        sampleBuffer = new double[frameSize];
    }

    public void setFrom(AudioSignal other){

    }
    public boolean recordFrom(TargetDataLine audioInput){
        byte[] byteBuffer = new byte[sampleBuffer.length*2];
        if (audioInput.read(byteBuffer,0,byteBuffer.length)==1) return false;
        for (int i=0; i<sampleBuffer.length;i++)
            sampleBuffer[i] = ((byteBuffer[2*i]<<8) + byteBuffer[2*i+1])/ 32768.0;
        //TODO update dB
        return true;
    }
    public boolean playTo(SourceDataLine audioOutput){
        byte[] byteBuffer = new byte[sampleBuffer.length*2];
        if (audioOutput.write(byteBuffer,0,byteBuffer.length)==1) return false;
        for (int i=0; i<sampleBuffer.length;i++) {
            byteBuffer[2*i] = ((Byte) sampleBuffer[i]);
            byteBuffer[2*i+1] = ;
        }
        audioOutput.write(byteBuffer,0,byteBuffer.length);
        return true;
    }
}
