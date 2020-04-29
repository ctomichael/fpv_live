package com.dji.component.fpv.widget.preview;

import android.content.Context;
import android.support.annotation.NonNull;
import com.dji.lifecycle.AbstractLifecycleLogic;
import com.dji.lifecycle.ILifecycle;
import com.dji.lifecycle.core.LifecycleEvent;
import com.dji.lifecycle.core.annotation.LogicConfig;
import com.dji.lifecycle.core.annotation.OnLifecycleEvent;
import dji.midware.media.newframing.DJIVideoHevcControl;

@LogicConfig(end = LifecycleEvent.ON_DRONE_DISCONNECT, start = LifecycleEvent.ON_DRONE_CONNECT)
public class DJIVideoDroneListener extends AbstractLifecycleLogic {
    public DJIVideoDroneListener(@NonNull Context appContext, @NonNull ILifecycle lifecycle) {
        super(appContext, lifecycle);
    }

    @OnLifecycleEvent(LifecycleEvent.ON_DRONE_CONNECT)
    public void initVideoDecoderControl() {
        DJIVideoHevcControl.getInstance().initHevcControl();
        DJIVideoHevcControl.getInstance().sendChangeVideoTransferFormatToAir();
    }

    @OnLifecycleEvent(LifecycleEvent.ON_DRONE_DISCONNECT)
    public void releaseVideoDecoderControl() {
        DJIVideoHevcControl.getInstance().releaseHevcControl();
    }
}
