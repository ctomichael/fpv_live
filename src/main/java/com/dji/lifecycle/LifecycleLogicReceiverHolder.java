package com.dji.lifecycle;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.dji.component.fpv.widget.preview.generate.DJIVideoDroneListener_Inject;
import com.dji.lifecycle.core.LifecycleEvent;
import com.dji.lifecycle.core.LifecycleInject;
import com.dji.lifecycle.core.LifecycleLasting;
import com.dji.lifecycle.core.LifecycleReceiver;
import dji.log.DJILog;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class LifecycleLogicReceiverHolder {
    private static final HashMap<Class, LifecycleLasting> MAP_LOGIC_LASTING = new HashMap<>();
    private Context mApplicationContext;
    private ILifecycle mLifecycle;
    private HashMap<Class, LifecycleReceiver> mMapLogicInstance = new HashMap<>();

    public static void addLogicReceiver(@NonNull Class receiverClass, @NonNull LifecycleLasting lasting) {
        MAP_LOGIC_LASTING.put(receiverClass, lasting);
    }

    LifecycleLogicReceiverHolder(@NonNull Context applicationContext, @Nullable ILifecycle lifecycle) {
        this.mApplicationContext = applicationContext;
        this.mLifecycle = lifecycle;
        inject();
    }

    private void inject() {
        injectSingle(new DJIVideoDroneListener_Inject());
    }

    private void injectSingle(LifecycleInject lifecycleInject) {
        lifecycleInject.inject();
    }

    /* access modifiers changed from: package-private */
    public void notifyLifecycleChange(@NonNull LifecycleEvent event) {
        DJILog.logWriteD("Lifecycle", "Event=" + event.name(), "Lifecycle", new Object[0]);
        for (Class clazz : MAP_LOGIC_LASTING.keySet()) {
            LifecycleLasting lasting = MAP_LOGIC_LASTING.get(clazz);
            if (lasting != null && lasting.getStartEvent() == event) {
                if (this.mMapLogicInstance.containsKey(clazz)) {
                    DJILog.logWriteE("Lifecycle", "Duplicate class instance: class=" + clazz + ", event=" + event.name(), new Object[0]);
                } else {
                    Object instance = createLogicInstance(clazz);
                    if (instance == null) {
                        throw new RuntimeException("Create instance fail: class=" + clazz + ", event=" + event.name());
                    }
                    this.mMapLogicInstance.put(clazz, (LifecycleReceiver) instance);
                    DJILog.logWriteD("Lifecycle", "Create instance: class=" + clazz + ", event=" + event.name(), "Lifecycle", new Object[0]);
                }
            }
        }
        for (LifecycleReceiver instance2 : this.mMapLogicInstance.values()) {
            instance2.onStateChanged(event);
        }
        for (Class clazz2 : MAP_LOGIC_LASTING.keySet()) {
            LifecycleLasting lasting2 = MAP_LOGIC_LASTING.get(clazz2);
            if (lasting2 != null && lasting2.getEndEvent() == event) {
                if (!this.mMapLogicInstance.containsKey(clazz2)) {
                    DJILog.logWriteE("Lifecycle", "Class instance not exist: class=" + clazz2 + ", event=" + event.name(), new Object[0]);
                } else {
                    this.mMapLogicInstance.remove(clazz2);
                    DJILog.logWriteD("Lifecycle", "Destroy instance: class=" + clazz2 + ", event=" + event.name(), "Lifecycle", new Object[0]);
                }
            }
        }
    }

    @Nullable
    private <T> T createLogicInstance(@NonNull Class<T> logic) {
        try {
            return logic.getConstructor(Context.class, ILifecycle.class).newInstance(this.mApplicationContext, this.mLifecycle);
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }
}
