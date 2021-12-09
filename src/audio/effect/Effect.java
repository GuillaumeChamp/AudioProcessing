package audio.effect;

import audio.AudioSignal;

/**
 * Interface all effect must implement
 */
public interface Effect {
     void setValue(int v);
     void apply(AudioSignal as);
}
