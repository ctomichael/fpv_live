package dji.internal.version;

import android.content.Context;
import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.version.component.DJIVersionInspire1X3Component;
import dji.internal.version.component.DJIVersionInspire1X5Component;
import dji.internal.version.component.DJIVersionInspire1X5rComponent;
import dji.internal.version.component.DJIVersionInspire1XTComponent;
import dji.internal.version.component.DJIVersionInspire1Z3Component;
import dji.log.DJILog;
import dji.midware.component.DJIComponentManager;
import dji.midware.util.DJIEventBusUtil;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class DJIVersionCamera {
    private static final String TAG = "DJIVersionCamera";
    private static final boolean showLog = false;
    private DJIVersionBaseComponent baseComponent = null;
    private DJIComponentManager.CameraComponentType cameraComponentType = DJIComponentManager.CameraComponentType.None;
    private Context context = null;

    public void init(Context ctx) {
        this.context = ctx;
        DJIRemoteVersionInfo.getInstance().init(this.context);
        DJIEventBusUtil.register(this);
        updateValue();
    }

    public void uninit() {
        DJIRemoteVersionInfo.getInstance().uninit(this.context);
        if (this.baseComponent != null) {
            this.baseComponent.uninit();
        }
        this.baseComponent = null;
        this.cameraComponentType = null;
        this.context = null;
        DJIEventBusUtil.unRegister(this);
    }

    private void updateValue() {
        DJIComponentManager.CameraComponentType newType = DJIComponentManager.getInstance().getCameraComponentType(0);
        if (newType == DJIComponentManager.CameraComponentType.None || newType == DJIComponentManager.CameraComponentType.Unknow) {
            newType = DJIComponentManager.CameraComponentType.None;
        }
        if (newType != this.cameraComponentType) {
            if (this.baseComponent != null) {
                this.baseComponent.uninit();
                this.baseComponent = null;
            }
            this.cameraComponentType = newType;
            this.baseComponent = createComponentByComponentType(this.cameraComponentType);
            if (this.baseComponent != null) {
                this.baseComponent.init(this.context);
            }
            if (this.baseComponent == null) {
                EventBus.getDefault().post(this);
            }
        }
    }

    public String getVersion() {
        if (this.baseComponent == null) {
            return null;
        }
        return this.baseComponent.getComponentVersion();
    }

    private DJIVersionBaseComponent createComponentByComponentType(DJIComponentManager.CameraComponentType type) {
        if (type == null) {
            return null;
        }
        switch (type) {
            case X3:
                return new DJIVersionInspire1X3Component();
            case X5:
                return new DJIVersionInspire1X5Component();
            case X5R:
                return new DJIVersionInspire1X5rComponent();
            case Z3:
                return new DJIVersionInspire1Z3Component();
            case TAU336:
            case TAU640:
            case XT2:
                return new DJIVersionInspire1XTComponent();
            default:
                return null;
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DJIVersionBaseComponent component) {
        if (this.baseComponent == component) {
            EventBus.getDefault().post(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DJIComponentManager.CameraComponentType type) {
        DJILog.d(TAG, "camera component type change: " + type, new Object[0]);
        updateValue();
    }

    private void log(String log) {
    }

    private void log(String log, boolean show) {
    }
}
