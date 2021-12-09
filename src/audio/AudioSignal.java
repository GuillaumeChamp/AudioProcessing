package audio;

import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

/**
 * Class use to store audio data with methode to record, copy and play
 */
public class AudioSignal {
    private double[] sampleBuffer;
    private double dBlevel;

    /**
     * Main constructor
     * @param frameSize size of sample buffer
     */
    public AudioSignal(int frameSize){
        sampleBuffer = new double[frameSize];
    }

    /**
     * Copy function
     * @param other the signal you want copy
     */
    public void setFrom(AudioSignal other){
        this.sampleBuffer = other.sampleBuffer;
        this.dBlevel= other.dBlevel;
    }

    /**
     * Kind of copy function, use for the effects
     * @param sampleBuffer modified buffer to write
     */
    public void setSampleBuffer(double[] sampleBuffer) {
        this.sampleBuffer = sampleBuffer;
    }

    /**
     * Getter function used for the vuMeter
     * @return
     */
    public double getDBLevel(){
        return dBlevel;
    }
    /**
     * Getter function used for the effects
     * @return
     */
    public double[] getSampleBuffer() {
        return sampleBuffer;
    }

    /**
     * Override the sample buffer and write the value of the in signal
     * @param audioInput signal of an input device
     * @return true if no error (to match as a kernel function)
     */
    public boolean recordFrom(TargetDataLine audioInput){
        byte[] byteBuffer = new byte[sampleBuffer.length*2];
        if (audioInput.read(byteBuffer,0,byteBuffer.length)==1) return false;
        for (int i=0; i<sampleBuffer.length;i++)
            sampleBuffer[i] = ((byteBuffer[2*i]<<8) + byteBuffer[2*i+1])/ 32768.0;
        updatedB();
        return true;
    }
    /**
     * Write the content of the sample buffer in the device buffer
     * @param audioOutput signal of the output device
     * @return true if no error (to match as a kernel function)
     */
    public boolean playTo(SourceDataLine audioOutput){
        byte[] byteBuffer;
        byteBuffer = unsampled(sampleBuffer);
        return audioOutput.write(byteBuffer, 0, byteBuffer.length) != 1;
    }

    /**
     * Sub function of playTo. Unsampled the signal
     * @param data sample buffer
     * @return Byte buffer
     */
    private byte[] unsampled(double[] data){
        byte[] byteBuffer = new byte[data.length*2];
        for (int i=0; i<sampleBuffer.length;i++){
            byteBuffer[2*i] = (byte) ((sampleBuffer[i]*32768)/256);
            byteBuffer[2*i+1] = (byte) ((sampleBuffer[i]*32768)%256);
        }
        return byteBuffer;
    }

    /**
     * Sub function of recordFrom. Update the dBlevel
     */
    private void updatedB(){
        dBlevel = 0;
        for (double l : sampleBuffer) dBlevel =+ Math.log10(Math.abs(l/sampleBuffer.length));
    }
}
