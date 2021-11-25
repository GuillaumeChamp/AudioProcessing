package audio.effect;

import audio.AudioSignal;

public interface Effec {
     void setValue(int v);
     void apply(AudioSignal as);
}
