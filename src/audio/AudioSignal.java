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
        this.sampleBuffer = other.getSampleBuffer();
    }
    public double getdBlevel(){
        return dBlevel;
    }

    public double[] getSampleBuffer() {
        return sampleBuffer;
    }

    public boolean recordFrom(TargetDataLine audioInput){
        byte[] byteBuffer = new byte[sampleBuffer.length*2];
        if (audioInput.read(byteBuffer,0,byteBuffer.length)==1) return false;
        for (int i=0; i<sampleBuffer.length;i++)
            sampleBuffer[i] = ((byteBuffer[2*i]<<8) + byteBuffer[2*i+1])/ 32768.0;
        updatedB();
        return true;
    }
    public boolean playTo(SourceDataLine audioOutput){
        byte[] byteBuffer;
        byteBuffer = unsampled(sampleBuffer);
        return audioOutput.write(byteBuffer, 0, byteBuffer.length) != 1;
    }
    private byte[] unsampled(double[] data){
        byte[] byteBuffer = new byte[data.length*2];
        for (int i=0; i<sampleBuffer.length;i++){
            byteBuffer[2*i] = (byte) ((sampleBuffer[i]*32768)/256);
            byteBuffer[2*i+1] = (byte) ((sampleBuffer[i]*32768)%256);
        }
        return byteBuffer;
    }
    private void updatedB(){
        dBlevel = 0;
        for (double l : sampleBuffer) dBlevel = l/sampleBuffer.length;
    }
}
