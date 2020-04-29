package com.dji.frame.common;

import android.content.Context;
import android.media.SoundPool;
import com.dji.frame.R;
import java.util.HashMap;
import java.util.Map;

public class V_SoundPool {
    public static int SOUND_ID_CLICK = 2;
    public static int SOUND_ID_DELETE = 3;
    public static int SOUND_ID_MOVE = 1;
    private Boolean isMute = false;
    private Map<Integer, Integer> soundMap = new HashMap();
    private SoundPool sp;

    public V_SoundPool(Context context) {
        loadSources(context);
    }

    private void loadSources(Context context) {
        this.sp = new SoundPool(10, 1, 5);
        this.soundMap.put(Integer.valueOf(SOUND_ID_MOVE), Integer.valueOf(this.sp.load(context, R.raw.effect_tick, 1)));
        this.soundMap.put(Integer.valueOf(SOUND_ID_CLICK), Integer.valueOf(this.sp.load(context, R.raw.effect_tick, 1)));
        this.soundMap.put(Integer.valueOf(SOUND_ID_DELETE), Integer.valueOf(this.sp.load(context, R.raw.effect_tick, 1)));
    }

    public void play(int type) {
        if (this.soundMap.containsKey(Integer.valueOf(type)) && !this.isMute.booleanValue()) {
            this.sp.play(this.soundMap.get(Integer.valueOf(type)).intValue(), 1.0f, 1.0f, 0, 0, 1.0f);
        }
    }

    public void setMute(Boolean isMute2) {
        this.isMute = isMute2;
    }
}
