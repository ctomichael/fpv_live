package dji.internal.version;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.version.component.DJIVersionInspire2RcComponent;
import dji.internal.version.component.DJIVersionInspireOneRcComponent;
import dji.internal.version.component.DJIVersionLB2RcComponent;
import dji.internal.version.component.DJIVersionMavicRcComponent;
import dji.internal.version.component.DJIVersionP3xRcComponent;
import dji.internal.version.component.DJIVersionP4ARcComponent;
import dji.internal.version.component.DJIVersionP4PRcComponent;
import dji.internal.version.component.DJIVersionPM420RcComponent;
import dji.internal.version.component.DJIVersionPhantom4RTKRcComponent;
import dji.internal.version.component.DJIVersionWM240RcComponent;
import dji.internal.version.component.DJIVersionWifiGroundRcComponent;
import dji.midware.component.DJIComponentManager;
import dji.midware.data.model.P3.DataCommonGetPushUpgradeStatus;
import dji.midware.util.DJIEventBusUtil;
import dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import kotlin.jvm.internal.LongCompanionObject;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class DJIVersionRC {
    private static final String TAG = "DJIVersionRC";
    private static final boolean showLog = true;
    private DJIVersionBaseComponent baseComponent = null;
    private Context context = null;
    private final List<RcVersionListener> mListeners = new ArrayList();
    private final Map<RcVersionListener, Disposable> mTimeoutMap = new HashMap();
    private DJIComponentManager.RcComponentType rcComponentType = DJIComponentManager.RcComponentType.None;

    public interface RcVersionListenerOnce extends RcVersionListener {
    }

    public interface RcVersionListener {
        void onUpdate(String str);
    }

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
        this.rcComponentType = null;
        this.context = null;
        DJIEventBusUtil.unRegister(this);
    }

    private void updateValue() {
        DJIComponentManager.RcComponentType newType = DJIComponentManager.getInstance().getRcComponentType();
        if (newType == DJIComponentManager.RcComponentType.None || newType == DJIComponentManager.RcComponentType.Unknow) {
            newType = DJIComponentManager.RcComponentType.None;
        }
        if (newType != this.rcComponentType) {
            if (this.baseComponent != null) {
                this.baseComponent.uninit();
                this.baseComponent = null;
            }
            this.rcComponentType = newType;
            this.baseComponent = createComponentByComponentType(this.rcComponentType);
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

    public void getVersion(@NonNull RcVersionListenerOnce listener) {
        getVersion(listener, LongCompanionObject.MAX_VALUE);
    }

    public void getVersion(@NonNull RcVersionListenerOnce listener, long timeoutSeconds) {
        if (this.baseComponent == null) {
            listener.onUpdate(null);
            return;
        }
        String componentVersion = this.baseComponent.getComponentVersion();
        if (!TextUtils.isEmpty(componentVersion)) {
            listener.onUpdate(componentVersion);
            return;
        }
        this.mListeners.add(listener);
        if (LongCompanionObject.MAX_VALUE != timeoutSeconds) {
            this.mTimeoutMap.put(listener, Schedulers.computation().scheduleDirect(new DJIVersionRC$$Lambda$0(this, listener), timeoutSeconds, TimeUnit.SECONDS));
        }
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$getVersion$0$DJIVersionRC(@NonNull RcVersionListenerOnce listener) {
        if (this.mListeners.contains(listener)) {
            listener.onUpdate(DJIRCAbstraction.DEFAULT_FIRMWARE_VERSION);
            this.mListeners.remove(listener);
        }
    }

    private DJIVersionBaseComponent createComponentByComponentType(DJIComponentManager.RcComponentType type) {
        if (type == null) {
            return null;
        }
        switch (type) {
            case P3P4:
            case P4PSdr:
                if (DJIComponentManager.getInstance().getPlatformType() == DJIComponentManager.PlatformType.P4P) {
                    return new DJIVersionP4PRcComponent();
                }
                return new DJIVersionP3xRcComponent();
            case P4P:
                return new DJIVersionP4PRcComponent();
            case P4A:
                return new DJIVersionP4ARcComponent();
            case Inspire:
                return new DJIVersionInspireOneRcComponent();
            case LB2:
                return new DJIVersionLB2RcComponent();
            case Inspire2:
            case Cendence:
                return new DJIVersionInspire2RcComponent();
            case CendenceSDR:
                return new DJIVersionPM420RcComponent();
            case FoldingDrone:
                return new DJIVersionMavicRcComponent();
            case Spark:
            case WM230:
            case WM160:
                return new DJIVersionWifiGroundRcComponent();
            case WM240:
            case WM245:
                return new DJIVersionWM240RcComponent();
            case P4RTK:
                return new DJIVersionPhantom4RTKRcComponent();
            default:
                return null;
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DJIVersionBaseComponent component) {
        if (this.baseComponent == component) {
            EventBus.getDefault().post(this);
            String componentVersion = component.getComponentVersion();
            if (!this.mListeners.isEmpty()) {
                Iterator it2 = new ArrayList(this.mListeners).iterator();
                while (it2.hasNext()) {
                    RcVersionListener listener = (RcVersionListener) it2.next();
                    listener.onUpdate(componentVersion);
                    if (listener instanceof RcVersionListenerOnce) {
                        removeListener(listener);
                    }
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DJIComponentManager.RcComponentType type) {
        updateValue();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCommonGetPushUpgradeStatus upgradeStatus) {
        if (upgradeStatus.getDescList().mUpgradeStep == DataCommonGetPushUpgradeStatus.DJIUpgradeStep.Complete) {
            updateValue();
            VersionController.logD(TAG, "remote controller receive upgrade complete event");
        }
    }

    public RcVersionListener addListener(RcVersionListener listener) {
        if (listener == null) {
            return null;
        }
        this.mListeners.add(listener);
        return listener;
    }

    public void removeListener(RcVersionListener listener) {
        if (listener != null) {
            Disposable disposable = this.mTimeoutMap.get(listener);
            if (disposable != null) {
                disposable.dispose();
                this.mTimeoutMap.remove(listener);
            }
            this.mListeners.remove(listener);
        }
    }
}
