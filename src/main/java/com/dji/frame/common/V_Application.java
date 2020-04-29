package com.dji.frame.common;

import android.app.Application;
import com.dji.frame.util.V_AppUtils;
import dji.thirdparty.afinal.FinalBitmap;
import dji.thirdparty.afinal.FinalDb;
import dji.thirdparty.afinal.FinalHttp;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

@Deprecated
public class V_Application extends Application {
    private FinalBitmap finalBitmap;
    private FinalDb finalDb;
    private FinalHttp finalHttp;
    private ArrayList<WeakReference<OnLowMemoryListener>> mLowMemoryListeners = new ArrayList<>();
    private V_SoundPool v_SoundPool;

    public interface OnLowMemoryListener {
        void onLowMemoryReceived();
    }

    public FinalHttp getFinalHttp() {
        if (this.finalHttp == null) {
            this.finalHttp = V_AppUtils.getFinalHttp();
        }
        return this.finalHttp;
    }

    public FinalDb getFinalDb() {
        if (this.finalDb == null) {
            this.finalDb = FinalDb.create(this);
        }
        return this.finalDb;
    }

    public V_SoundPool getV_SoundPool() {
        if (this.v_SoundPool == null) {
            this.v_SoundPool = new V_SoundPool(this);
        }
        return this.v_SoundPool;
    }

    public void registerOnLowMemoryListener(OnLowMemoryListener listener) {
        if (listener != null) {
            this.mLowMemoryListeners.add(new WeakReference(listener));
        }
    }

    public void unregisterOnLowMemoryListener(OnLowMemoryListener listener) {
        if (listener != null) {
            int i = 0;
            while (i < this.mLowMemoryListeners.size()) {
                OnLowMemoryListener l = (OnLowMemoryListener) this.mLowMemoryListeners.get(i).get();
                if (l == null || l == listener) {
                    this.mLowMemoryListeners.remove(i);
                } else {
                    i++;
                }
            }
        }
    }

    public void onLowMemory() {
        super.onLowMemory();
        int i = 0;
        while (i < this.mLowMemoryListeners.size()) {
            OnLowMemoryListener listener = (OnLowMemoryListener) this.mLowMemoryListeners.get(i).get();
            if (listener == null) {
                this.mLowMemoryListeners.remove(i);
            } else {
                listener.onLowMemoryReceived();
                i++;
            }
        }
    }
}
