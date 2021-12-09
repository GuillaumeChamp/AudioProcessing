package audio.effect;

import audio.AudioSignal;

/**
 * Interface all effect must implement
 */
public interface Effec {
     void setValue(int v);
     void apply(AudioSignal as);
}
