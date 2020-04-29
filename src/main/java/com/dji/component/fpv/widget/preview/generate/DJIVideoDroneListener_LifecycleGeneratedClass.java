package com.dji.component.fpv.widget.preview.generate;

import android.content.Context;
import android.support.annotation.Keep;
import com.dji.component.fpv.widget.preview.DJIVideoDroneListener;
import com.dji.lifecycle.ILifecycle;
import com.dji.lifecycle.LifecycleLogicReceiverHolder;
import com.dji.lifecycle.core.LifecycleEvent;
import com.dji.lifecycle.core.LifecycleLasting;
import com.dji.lifecycle.core.LifecycleReceiver;

@Keep
public class DJIVideoDroneListener_LifecycleGeneratedClass implements LifecycleReceiver {
    private DJIVideoDroneListener mReceiver;

    public DJIVideoDroneListener_LifecycleGeneratedClass(Context appContext, ILifecycle lifecycle) {
        this.mReceiver = new DJIVideoDroneListener(appContext, lifecycle);
    }

    public static void inject() {
        LifecycleLogicReceiverHolder.addLogicReceiver(DJIVideoDroneListener_LifecycleGeneratedClass.class, new LifecycleLasting(LifecycleEvent.ON_DRONE_CONNECT, LifecycleEvent.ON_DRONE_DISCONNECT));
    }

    public void onStateChanged(LifecycleEvent event) {
        switch (event) {
            case ON_DRONE_DISCONNECT:
                this.mReceiver.releaseVideoDecoderControl();
                return;
            case ON_DRONE_CONNECT:
                this.mReceiver.initVideoDecoderControl();
                return;
            default:
                return;
        }
    }

    public Object getReceiver() {
        return this.mReceiver;
    }
}
