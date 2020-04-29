package dji.internal.diagnostics;

import android.support.annotation.VisibleForTesting;
import dji.component.accountcenter.IMemberProtocol;
import dji.diagnostics.model.DJIDiagnostics;
import dji.internal.diagnostics.handler.util.UpdateInterface;
import dji.midware.component.DJIComponentManager;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataOsdGetPushHome;
import dji.midware.interfaces.CmdIdInterface;
import dji.midware.util.BackgroundLooper;
import dji.midware.util.BytesUtil;
import dji.midware.util.DJIEventBusUtil;
import dji.sdksharedlib.DJISDKCache;
import dji.sdksharedlib.extension.KeyHelper;
import dji.sdksharedlib.keycatalog.BatteryKeys;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.keycatalog.DJISDKCacheKeys;
import dji.sdksharedlib.store.DJISDKCacheParamValue;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import org.greenrobot.eventbus.Subscribe;

public abstract class DiagnosticsBaseHandler implements UpdateInterface {
    private DJISDKCacheKey batteryPercentKey1;
    private DJISDKCacheKey batteryPercentKey2;
    private DiagnosticsHandlerUpdateObserver diagnosisHandlerUpdateObserver;
    private DJISDKCacheKey fcsnKey;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    public abstract Set<DJIDiagnostics> getDiagnosisList();

    /* access modifiers changed from: protected */
    public abstract void onRegister();

    /* access modifiers changed from: protected */
    public abstract void onUnregister();

    public abstract void reset(int i);

    @VisibleForTesting
    protected DiagnosticsBaseHandler() {
    }

    public DiagnosticsBaseHandler(DiagnosticsHandlerUpdateObserver observer) {
        this.diagnosisHandlerUpdateObserver = observer;
    }

    public void register() {
        DJIEventBusUtil.register(this);
        onRegister();
    }

    @Subscribe
    public void onEvent3BackgroundThread(DiagnosticsBaseHandler noSense) {
    }

    public void unregister() {
        onUnregister();
        DJIEventBusUtil.unRegister(this);
    }

    /* access modifiers changed from: protected */
    public void notifyChange() {
        if (this.diagnosisHandlerUpdateObserver != null) {
            this.diagnosisHandlerUpdateObserver.onDiagnosticUpdated();
        }
    }

    /* access modifiers changed from: protected */
    public void postToDiagnosticBackgroudThread(Runnable runnable) {
        BackgroundLooper.post(runnable);
    }

    protected static boolean compareBooleans(boolean[] a, boolean[] b) {
        if (a == null || b == null || a.length != b.length) {
            return false;
        }
        for (int i = 0; i < b.length; i++) {
            if (a[i] != b[i]) {
                return false;
            }
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void saveLog(String text, DataBase dataBase) {
        String strDate = this.sdf.format(new Date());
        StringBuilder builder = new StringBuilder();
        builder.append(strDate);
        builder.append(", [tip:").append(text).append(IMemberProtocol.STRING_SEPERATOR_RIGHT);
        builder.append(", logIndex=").append(DataOsdGetPushHome.getInstance().getFlycLogIndex());
        if (dataBase != null) {
            if (dataBase.getPack() != null) {
                builder.append(", [header:");
                builder.append("senderType=").append(Integer.toHexString(dataBase.getPack().senderType));
                builder.append("(").append(DeviceType.find(dataBase.getPack().senderType)).append(")").append(" ");
                builder.append("senderId=").append(Integer.toHexString(dataBase.getPack().senderId)).append(" ");
                builder.append("cmdSet=").append(Integer.toHexString(dataBase.getPack().cmdSet));
                CmdSet cmdSet = CmdSet.find(dataBase.getPack().cmdSet);
                builder.append("(").append(cmdSet).append(")").append(" ");
                builder.append("cmdType=").append(dataBase.getPack().cmdType).append(" ");
                builder.append("cmdId=").append(Integer.toHexString(dataBase.getPack().cmdId));
                builder.append("(").append(getCmdIdName(cmdSet, dataBase.getPack().cmdId)).append(")");
                builder.append(IMemberProtocol.STRING_SEPERATOR_RIGHT);
            }
            builder.append(", [Platform:").append(getPlatformName());
            builder.append(", sn: ").append(getFlightControllerSN());
            builder.append(", battery: ").append(getBatteryPercent());
            builder.append("]\n");
            if (dataBase.getRecData() != null) {
                builder.append("[data:").append(BytesUtil.byte2hex(dataBase.getRecData())).append("]\n");
            }
        }
    }

    private String getCmdIdName(CmdSet cmdSet, int cmdId) {
        CmdIdInterface cmdIdInterface;
        if (cmdSet == null || cmdSet.cmdIdClass() == null || (cmdIdInterface = cmdSet.cmdIdClass().getCmdId(cmdId)) == null) {
            return null;
        }
        return cmdIdInterface.toString();
    }

    private String getFlightControllerSN() {
        DJISDKCacheParamValue value;
        if (this.fcsnKey == null) {
            this.fcsnKey = KeyHelper.getFlightControllerKey(DJISDKCacheKeys.SERIAL_NUMBER);
        }
        if (DJISDKCache.getInstance() == null || (value = DJISDKCache.getInstance().getAvailableValue(this.fcsnKey)) == null || value.getData() == null) {
            return null;
        }
        return (String) value.getData();
    }

    private String getPlatformName() {
        if (DJIComponentManager.getInstance() != null) {
            return DJIComponentManager.getInstance().getPlatformType().toString();
        }
        return "None";
    }

    private String getBatteryPercent() {
        if (this.batteryPercentKey1 == null) {
            this.batteryPercentKey1 = KeyHelper.getBatteryKey(0, BatteryKeys.CHARGE_REMAINING_IN_PERCENT);
        }
        if (this.batteryPercentKey2 == null) {
            this.batteryPercentKey2 = KeyHelper.getBatteryKey(1, BatteryKeys.CHARGE_REMAINING_IN_PERCENT);
        }
        StringBuilder batteryBuilder = new StringBuilder();
        DJISDKCacheParamValue value = DJISDKCache.getInstance().getAvailableValue(this.batteryPercentKey1);
        if (value == null || value.getData() == null) {
            batteryBuilder.append("0%");
        } else {
            batteryBuilder.append((Integer) value.getData()).append("%");
        }
        DJISDKCacheParamValue value2 = DJISDKCache.getInstance().getAvailableValue(this.batteryPercentKey2);
        if (value2 == null || value2.getData() == null) {
            batteryBuilder.append(" 0%");
        } else {
            batteryBuilder.append(" ").append((Integer) value2.getData()).append("%");
        }
        return batteryBuilder.toString();
    }

    public void onStatusUpdate() {
        postToDiagnosticBackgroudThread(new DiagnosticsBaseHandler$$Lambda$0(this));
    }
}
