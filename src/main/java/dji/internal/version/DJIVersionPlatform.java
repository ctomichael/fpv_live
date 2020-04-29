package dji.internal.version;

import android.content.Context;
import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.version.component.DJIVersionDM368Component;
import dji.internal.version.component.DJIVersionInspire1Component;
import dji.internal.version.component.DJIVersionLB2Component;
import dji.internal.version.component.DJIVersionM100Component;
import dji.internal.version.component.DJIVersionM600Component;
import dji.internal.version.component.DJIVersionM600ProComponent;
import dji.internal.version.component.DJIVersionOSMOMobile2Component;
import dji.internal.version.component.DJIVersionOSMOMobileComponent;
import dji.internal.version.component.DJIVersionOsmoComponent;
import dji.internal.version.component.DJIVersionOsmoPlusComponent;
import dji.internal.version.component.DJIVersionOsmoProComponent;
import dji.internal.version.component.DJIVersionOsmoRawComponent;
import dji.internal.version.component.DJIVersionP3cComponent;
import dji.internal.version.component.DJIVersionP3sComponent;
import dji.internal.version.component.DJIVersionP3xComponent;
import dji.internal.version.component.DJIVersionWM160Component;
import dji.internal.version.component.DJIVersionWM230Component;
import dji.log.DJILog;
import dji.midware.component.DJIComponentManager;
import dji.midware.data.model.P3.DataCommonGetPushUpgradeStatus;
import dji.midware.util.DJIEventBusUtil;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class DJIVersionPlatform {
    private static final String TAG = "DJIVersionPlatform";
    private static final boolean showLog = false;
    private DJIVersionBaseComponent baseComponent = null;
    private Context context = null;
    private DJIComponentManager.PlatformType platformType = DJIComponentManager.PlatformType.None;

    public void init(Context ctx) {
        this.context = ctx;
        DJIRemoteVersionInfo.getInstance().init(this.context);
        DJIEventBusUtil.register(this);
        updateValue();
    }

    public void destroy() {
        DJIRemoteVersionInfo.getInstance().uninit(this.context);
        if (this.baseComponent != null) {
            this.baseComponent.uninit();
        }
        this.baseComponent = null;
        this.platformType = null;
        this.context = null;
        DJIEventBusUtil.unRegister(this);
    }

    private void updateValue() {
        DJIComponentManager.PlatformType newType = DJIComponentManager.getInstance().getPlatformType();
        DJILog.d(TAG, "DJIVersionPlatform updateValue: " + newType, new Object[0]);
        if (newType == DJIComponentManager.PlatformType.None || newType == DJIComponentManager.PlatformType.Unknown) {
            newType = DJIComponentManager.PlatformType.None;
        }
        if (newType != this.platformType) {
            if (this.baseComponent != null) {
                this.baseComponent.uninit();
                this.baseComponent = null;
            }
            this.platformType = newType;
            this.baseComponent = createComponentByComponentType(this.platformType);
            if (this.baseComponent != null) {
                this.baseComponent.init(this.context);
            }
            if (this.baseComponent == null) {
                EventBus.getDefault().post(this);
            } else {
                DJILog.d(TAG, "DJIVersionPlatform : " + this.baseComponent.getComponentVersion(), new Object[0]);
            }
        }
    }

    public String getVersion() {
        if (this.baseComponent == null) {
            return null;
        }
        return this.baseComponent.getComponentVersion();
    }

    private DJIVersionBaseComponent createComponentByComponentType(DJIComponentManager.PlatformType type) {
        if (type == null) {
            return null;
        }
        switch (type) {
            case P3c:
                return new DJIVersionP3cComponent();
            case P3s:
                return new DJIVersionP3sComponent();
            case P3x:
                return new DJIVersionP3xComponent();
            case OSMO:
                switch (DJIComponentManager.getInstance().getCameraComponentType(0)) {
                    case X5:
                        return new DJIVersionOsmoProComponent();
                    case X5R:
                        return new DJIVersionOsmoRawComponent();
                    case Z3:
                        return new DJIVersionOsmoPlusComponent();
                    default:
                        return new DJIVersionOsmoComponent();
                }
            case OSMOMobile:
                if (DJIComponentManager.getInstance().getGimbalComponentType() == DJIComponentManager.GimbalComponentType.OSMO_2) {
                    return new DJIVersionOSMOMobile2Component();
                }
                return new DJIVersionOSMOMobileComponent();
            case M100:
                return new DJIVersionM100Component();
            case M600:
                return new DJIVersionM600Component();
            case M600Pro:
                return new DJIVersionM600ProComponent();
            case Inspire:
                return new DJIVersionInspire1Component();
            case A3:
            case N3:
            case A2:
                if (DJIComponentManager.getInstance().getRcComponentType() == DJIComponentManager.RcComponentType.LB2) {
                    return new DJIVersionLB2Component();
                }
                return null;
            case P4:
            case FoldingDrone:
            case Inspire2:
            case P4P:
            case P4A:
            case M200:
            case M210RTK:
            case M210:
            case PM420:
            case PM420PRO:
            case PM420PRO_RTK:
            case Spark:
            case P4PSDR:
            case P4RTK:
                return new DJIVersionDM368Component();
            case WM230:
            case WM240:
            case WM245:
                return new DJIVersionWM230Component();
            case WM160:
                return new DJIVersionWM160Component();
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
    public void onEvent3BackgroundThread(DJIComponentManager.PlatformType type) {
        updateValue();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCommonGetPushUpgradeStatus upgradeStatus) {
        if (upgradeStatus.getDescList().mUpgradeStep == DataCommonGetPushUpgradeStatus.DJIUpgradeStep.Complete) {
            updateValue();
            VersionController.logD(TAG, "Platform receive upgrade complete event");
        }
    }
}
