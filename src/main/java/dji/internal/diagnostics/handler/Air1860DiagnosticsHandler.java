package dji.internal.diagnostics.handler;

import android.support.annotation.VisibleForTesting;
import dji.diagnostics.model.DJIDiagnostics;
import dji.diagnostics.model.DJIDiagnosticsError;
import dji.diagnostics.model.DJIDiagnosticsType;
import dji.internal.diagnostics.DJIDiagnosticsImpl;
import dji.internal.diagnostics.DiagnosticsBaseHandler;
import dji.internal.diagnostics.DiagnosticsHandlerUpdateObserver;
import dji.internal.diagnostics.handler.util.DiagnosticsModelBase;
import dji.internal.diagnostics.handler.util.DiagnosticsWirelessModel;
import dji.internal.diagnostics.handler.util.UpdateInterface;
import dji.internal.logics.CommonUtil;
import dji.logic.manager.DJIUSBWifiSwitchManager;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.model.P3.Data1860GetPushCheckStatus;
import dji.midware.data.model.P3.DataOsdGetPushCheckStatusV2;
import dji.midware.data.model.P3.DataOsdGetPushSignalQuality;
import dji.midware.data.model.P3.DataOsdGetPushWirelessState;
import dji.midware.data.model.P3.DataWifiGetPushElecSignal;
import dji.midware.data.model.P3.DataWifiGetPushSignal;
import dji.sdksharedlib.DJISDKCache;
import dji.sdksharedlib.extension.KeyHelper;
import dji.sdksharedlib.keycatalog.airlink.AirLinkKeys;
import dji.sdksharedlib.store.DJISDKCacheParamValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class Air1860DiagnosticsHandler extends DiagnosticsBaseHandler implements UpdateInterface {
    private static final DJIDiagnosticsType TYPE = DJIDiagnosticsType.AIR1860;
    private Integer errorCode;
    private boolean hasEncodeException;
    private boolean hasEncodeExceptionWithDetachableCamera;
    private boolean isEncoderUpgrade;
    private boolean isKernelBoardOverHeatProtect;
    private boolean isKernelBoardOverHeatSeriousProtect;
    private boolean isLowRCSignal;
    private boolean isLowRadioSignal;
    private boolean isNoSignal;
    private boolean isRCDisturbHigh;
    private boolean isRCDisturbLow;
    private boolean isRCDisturbMid;
    private boolean isStrongRCRadioSignalNoise;
    private boolean isStrongRadioSignalNoise;
    private boolean isWiFiMagneticInterferenceHigh;
    private boolean last1860CpldI2CAbnormal;
    private boolean last1860HasException;
    private boolean last1860SPIAbnormal;
    private boolean lastEncoderUpgrade;
    private boolean lastKernelBoardOverHeatProtect;
    private boolean lastKernelBoardOverHeatSeriousProtect;
    private boolean lastLowRCSignal;
    private boolean lastLowRadioSignal;
    private boolean lastNoSignal;
    private boolean lastRCDisturbHigh;
    private boolean lastRCDisturbLow;
    private boolean lastRCDisturbMid;
    private boolean lastStrongRCRadioSignalNoise;
    private boolean lastStrongRadioSignalNoise;
    private boolean lastWiFiMagneticInterferenceHigh;
    private List<DiagnosticsModelBase<DataOsdGetPushWirelessState.SdrWirelessState>> mWifiStateModelList;
    private boolean needRestartException;
    private boolean needReturnException;

    /* access modifiers changed from: package-private */
    public final /* bridge */ /* synthetic */ void bridge$lambda$0$Air1860DiagnosticsHandler() {
        notifyChange();
    }

    public Air1860DiagnosticsHandler(DiagnosticsHandlerUpdateObserver observer) {
        super(observer);
        initModel();
    }

    @VisibleForTesting
    public Air1860DiagnosticsHandler() {
        initModel();
    }

    public void onStatusUpdate() {
        postToDiagnosticBackgroudThread(new Air1860DiagnosticsHandler$$Lambda$0(this));
    }

    private void initModel() {
        this.mWifiStateModelList = new ArrayList();
        List<DiagnosticsModelBase<DataOsdGetPushWirelessState.SdrWirelessState>> list = this.mWifiStateModelList;
        DataOsdGetPushWirelessState.SdrWirelessState sdrWirelessState = DataOsdGetPushWirelessState.SdrWirelessState.STRONG_DISTURBANCE;
        sdrWirelessState.getClass();
        DataOsdGetPushWirelessState.SdrWirelessState sdrWirelessState2 = DataOsdGetPushWirelessState.SdrWirelessState.VIDEO_DISTURBANCE;
        sdrWirelessState2.getClass();
        DataOsdGetPushWirelessState.SdrWirelessState sdrWirelessState3 = DataOsdGetPushWirelessState.SdrWirelessState.RC_DISTURBANCE;
        sdrWirelessState3.getClass();
        DataOsdGetPushWirelessState.SdrWirelessState sdrWirelessState4 = DataOsdGetPushWirelessState.SdrWirelessState.LOW_SIGNAL_POWER;
        sdrWirelessState4.getClass();
        list.addAll(Arrays.asList(new DiagnosticsWirelessModel(DJIDiagnosticsError.AirLink.TAKE_OFF_INTERFERENCE, Air1860DiagnosticsHandler$$Lambda$1.get$Lambda(sdrWirelessState), this), new DiagnosticsWirelessModel(DJIDiagnosticsError.AirLink.RC_STRONG_INTERFERENCE, Air1860DiagnosticsHandler$$Lambda$2.get$Lambda(sdrWirelessState2), this), new DiagnosticsWirelessModel(DJIDiagnosticsError.AirLink.AIRCRAFT_SIDE_STRONG_INTERFERENCE, Air1860DiagnosticsHandler$$Lambda$3.get$Lambda(sdrWirelessState3), this), new DiagnosticsWirelessModel(DJIDiagnosticsError.AirLink.WEAK_SIGNAL_FOR_INTERFERENCE, Air1860DiagnosticsHandler$$Lambda$4.get$Lambda(sdrWirelessState4), this)));
    }

    /* access modifiers changed from: protected */
    public void onRegister() {
        if (Data1860GetPushCheckStatus.getInstance().isGetted()) {
            onEvent3BackgroundThread(Data1860GetPushCheckStatus.getInstance());
        }
        if (DataOsdGetPushSignalQuality.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataOsdGetPushSignalQuality.getInstance());
        }
        if (DataWifiGetPushSignal.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataWifiGetPushSignal.getInstance());
        }
        if (DataOsdGetPushCheckStatusV2.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataOsdGetPushCheckStatusV2.getInstance());
        }
        if (DataWifiGetPushElecSignal.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataWifiGetPushElecSignal.getInstance());
        }
        if (DataOsdGetPushWirelessState.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataOsdGetPushWirelessState.getInstance());
        }
    }

    /* access modifiers changed from: protected */
    public void onUnregister() {
    }

    public Set<DJIDiagnostics> getDiagnosisList() {
        Set<DJIDiagnostics> diagnosisList = new HashSet<>();
        if (this.hasEncodeExceptionWithDetachableCamera) {
            diagnosisList.add(new DJIDiagnosticsImpl(TYPE, (int) DJIDiagnosticsError.AirLink.ERROR_WITH_DETACHABLE_CAMERA, this.errorCode));
        }
        if (this.needReturnException) {
            diagnosisList.add(new DJIDiagnosticsImpl(TYPE, DJIDiagnosticsError.AirLink.ERROR_NEED_RETURN));
        }
        if (this.needRestartException) {
            diagnosisList.add(new DJIDiagnosticsImpl(TYPE, DJIDiagnosticsError.AirLink.ERROR_NEED_RESTART));
        }
        if (this.isNoSignal) {
            diagnosisList.add(new DJIDiagnosticsImpl(TYPE, DJIDiagnosticsError.AirLink.NO_SIGNAL));
        }
        if (this.isLowRCSignal) {
            diagnosisList.add(new DJIDiagnosticsImpl(TYPE, DJIDiagnosticsError.AirLink.LOW_RC_SIGNAL));
        }
        if (this.isStrongRCRadioSignalNoise) {
            diagnosisList.add(new DJIDiagnosticsImpl(TYPE, DJIDiagnosticsError.AirLink.STRONG_RC_RADIO_SIGNAL_NOISE));
        }
        if (this.isLowRadioSignal) {
            diagnosisList.add(new DJIDiagnosticsImpl(TYPE, DJIDiagnosticsError.AirLink.LOW_RADIO_SIGNAL));
        }
        if (this.isStrongRadioSignalNoise) {
            diagnosisList.add(new DJIDiagnosticsImpl(TYPE, DJIDiagnosticsError.AirLink.STRONG_RADIO_SIGNAL_NOISE));
        }
        if (this.isRCDisturbMid) {
            diagnosisList.add(new DJIDiagnosticsImpl(TYPE, DJIDiagnosticsError.AirLink.RC_DISTURB_MID));
        }
        if (this.isRCDisturbHigh) {
            diagnosisList.add(new DJIDiagnosticsImpl(TYPE, DJIDiagnosticsError.AirLink.RC_DISTURB_HIGH));
        }
        if (this.isRCDisturbLow) {
            diagnosisList.add(new DJIDiagnosticsImpl(TYPE, DJIDiagnosticsError.AirLink.RC_DISTURB_LOW));
        }
        if (this.hasEncodeException) {
            diagnosisList.add(new DJIDiagnosticsImpl(TYPE, (int) DJIDiagnosticsError.AirLink.ENCODER_ERROR, this.errorCode));
        }
        if (this.isEncoderUpgrade) {
            diagnosisList.add(new DJIDiagnosticsImpl(TYPE, DJIDiagnosticsError.AirLink.ENCODER_UPGRADE));
        }
        if (this.isKernelBoardOverHeatProtect) {
            diagnosisList.add(new DJIDiagnosticsImpl(TYPE, DJIDiagnosticsError.AirLink.KERNEL_BOARD_OVERHEAT_PROTECT));
        }
        if (this.isKernelBoardOverHeatSeriousProtect) {
            diagnosisList.add(new DJIDiagnosticsImpl(TYPE, DJIDiagnosticsError.AirLink.KERNEL_BOARD_OVERHEAT_SERIOUS_PROTECT));
        }
        if (this.isWiFiMagneticInterferenceHigh) {
            diagnosisList.add(new DJIDiagnosticsImpl(TYPE, DJIDiagnosticsError.AirLink.WIFI_MAGNETIC_INTERFERENCE_HIGH));
        }
        for (DiagnosticsModelBase<DataOsdGetPushWirelessState.SdrWirelessState> model : this.mWifiStateModelList) {
            DJIDiagnosticsType dJIDiagnosticsType = TYPE;
            diagnosisList.getClass();
            model.doIfError(dJIDiagnosticsType, Air1860DiagnosticsHandler$$Lambda$5.get$Lambda(diagnosisList));
        }
        return diagnosisList;
    }

    public void reset(int componentIndex) {
        this.last1860HasException = false;
        this.last1860CpldI2CAbnormal = false;
        this.last1860SPIAbnormal = false;
        this.hasEncodeExceptionWithDetachableCamera = false;
        this.hasEncodeException = false;
        this.needReturnException = false;
        this.needRestartException = false;
        this.errorCode = null;
        this.isNoSignal = false;
        this.lastNoSignal = false;
        this.isLowRCSignal = false;
        this.lastLowRCSignal = false;
        this.isStrongRCRadioSignalNoise = false;
        this.lastStrongRCRadioSignalNoise = false;
        this.isLowRadioSignal = false;
        this.lastLowRadioSignal = false;
        this.isStrongRadioSignalNoise = false;
        this.lastStrongRadioSignalNoise = false;
        this.isRCDisturbMid = false;
        this.lastRCDisturbMid = false;
        this.isRCDisturbHigh = false;
        this.lastRCDisturbHigh = false;
        this.isRCDisturbLow = false;
        this.lastRCDisturbLow = false;
        this.isEncoderUpgrade = false;
        this.lastEncoderUpgrade = false;
        this.isKernelBoardOverHeatProtect = false;
        this.lastKernelBoardOverHeatProtect = false;
        this.isKernelBoardOverHeatSeriousProtect = false;
        for (DiagnosticsModelBase<DataOsdGetPushWirelessState.SdrWirelessState> model : this.mWifiStateModelList) {
            model.reset();
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataOsdGetPushSignalQuality status) {
        if (status.isGetted()) {
            postToDiagnosticBackgroudThread(new Air1860DiagnosticsHandler$$Lambda$6(this, status));
        }
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$onEvent3BackgroundThread$0$Air1860DiagnosticsHandler(DataOsdGetPushSignalQuality status) {
        boolean isNoSignal2 = getIsNoSignal();
        updateOsdSignal(status);
        if (!compareBooleans(new boolean[]{isNoSignal2, this.isLowRCSignal, this.isStrongRCRadioSignalNoise, this.isLowRadioSignal, this.isStrongRadioSignalNoise}, new boolean[]{this.lastNoSignal, this.lastLowRCSignal, this.lastStrongRCRadioSignalNoise, this.lastLowRadioSignal, this.lastStrongRadioSignalNoise})) {
            this.isNoSignal = isNoSignal2;
            notifyChange();
        }
        this.lastNoSignal = isNoSignal2;
        this.lastLowRCSignal = this.isLowRCSignal;
        this.lastStrongRCRadioSignalNoise = this.isStrongRCRadioSignalNoise;
        this.lastLowRadioSignal = this.isLowRadioSignal;
        this.lastStrongRadioSignalNoise = this.isStrongRadioSignalNoise;
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataWifiGetPushSignal status) {
        if (status.isGetted()) {
            postToDiagnosticBackgroudThread(new Air1860DiagnosticsHandler$$Lambda$7(this));
        }
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$onEvent3BackgroundThread$1$Air1860DiagnosticsHandler() {
        boolean isNoSignal2 = getIsNoSignal();
        if (!compareBooleans(new boolean[]{isNoSignal2}, new boolean[]{this.lastNoSignal})) {
            this.isNoSignal = isNoSignal2;
            notifyChange();
        }
        this.lastNoSignal = isNoSignal2;
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataOsdGetPushWirelessState state) {
        if (state.isGetted()) {
            postToDiagnosticBackgroudThread(new Air1860DiagnosticsHandler$$Lambda$8(this, state));
        }
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$onEvent3BackgroundThread$2$Air1860DiagnosticsHandler(DataOsdGetPushWirelessState state) {
        boolean changed = false;
        for (DiagnosticsModelBase<DataOsdGetPushWirelessState.SdrWirelessState> model : this.mWifiStateModelList) {
            changed |= model.statusApply(state.getEventCode());
        }
        if (changed) {
            notifyChange();
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataOsdGetPushCheckStatusV2 status) {
        if (status.isGetted()) {
            postToDiagnosticBackgroudThread(new Air1860DiagnosticsHandler$$Lambda$9(this, status));
        }
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$onEvent3BackgroundThread$3$Air1860DiagnosticsHandler(DataOsdGetPushCheckStatusV2 status) {
        boolean isRCDisturbMid2 = status.isRCRadioQualityMedium();
        boolean isRCDisturbHigh2 = status.isRCRadioQualityHigh();
        boolean isRCDisturbLow2 = status.isRCRadioQualityLow();
        if (!compareBooleans(new boolean[]{isRCDisturbMid2, isRCDisturbHigh2, isRCDisturbLow2}, new boolean[]{this.lastRCDisturbMid, this.lastRCDisturbHigh, this.lastRCDisturbLow})) {
            this.isRCDisturbMid = isRCDisturbMid2;
            this.isRCDisturbHigh = isRCDisturbHigh2;
            this.isRCDisturbLow = isRCDisturbLow2;
            notifyChange();
        }
        this.lastRCDisturbMid = isRCDisturbMid2;
        this.lastRCDisturbHigh = isRCDisturbHigh2;
        this.lastRCDisturbLow = isRCDisturbLow2;
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(Data1860GetPushCheckStatus status) {
        if (status.isGetted()) {
            postToDiagnosticBackgroudThread(new Air1860DiagnosticsHandler$$Lambda$10(this, status));
        }
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$onEvent3BackgroundThread$4$Air1860DiagnosticsHandler(Data1860GetPushCheckStatus status) {
        boolean isKernelBoardOverHeatProtect2;
        boolean isKernelBoardOverHeatSeriousProtect2;
        boolean hasExp = status.hasException(false);
        boolean cpldAbnormal = status.isCPLDI2CAbnormal();
        boolean spiAbnormal = status.isVisualSPIAbnormal();
        boolean isCameraDisconnected = status.isCameraDisconnected();
        boolean isEncoderUpgrade2 = status.isSystemUpgradeAbnormal();
        if (status.getThermalZone() == 1) {
            isKernelBoardOverHeatProtect2 = true;
        } else {
            isKernelBoardOverHeatProtect2 = false;
        }
        if (status.getThermalZone() == 2) {
            isKernelBoardOverHeatSeriousProtect2 = true;
        } else {
            isKernelBoardOverHeatSeriousProtect2 = false;
        }
        handleException(hasExp, cpldAbnormal, spiAbnormal, isCameraDisconnected, isEncoderUpgrade2, isKernelBoardOverHeatProtect2, isKernelBoardOverHeatSeriousProtect2, status.getStatus());
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataWifiGetPushElecSignal status) {
        if (status.isGetted()) {
            postToDiagnosticBackgroudThread(new Air1860DiagnosticsHandler$$Lambda$11(this, status));
        }
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$onEvent3BackgroundThread$5$Air1860DiagnosticsHandler(DataWifiGetPushElecSignal status) {
        boolean isWiFiMagneticInterferenceHigh2;
        if (status.getSignalStatus() == DataWifiGetPushElecSignal.SIGNAL_STATUS.Poor) {
            isWiFiMagneticInterferenceHigh2 = true;
        } else {
            isWiFiMagneticInterferenceHigh2 = false;
        }
        if (!compareBooleans(new boolean[]{isWiFiMagneticInterferenceHigh2}, new boolean[]{this.lastWiFiMagneticInterferenceHigh})) {
            this.isWiFiMagneticInterferenceHigh = isWiFiMagneticInterferenceHigh2;
            notifyChange();
        }
        this.lastWiFiMagneticInterferenceHigh = isWiFiMagneticInterferenceHigh2;
    }

    public void handleException(boolean hasExp, boolean cpldAbnormal, boolean spiAbnormal, boolean isCameraDisconnected, boolean isEncoderUpgrade2, boolean isKernelBoardOverHeatProtect2, boolean isKernelBoardOverHeatSeriousProtect2, int errorCode2) {
        if (!(this.last1860HasException == hasExp && this.last1860CpldI2CAbnormal == cpldAbnormal && this.last1860SPIAbnormal == spiAbnormal && this.lastEncoderUpgrade == isEncoderUpgrade2 && this.lastKernelBoardOverHeatProtect == isKernelBoardOverHeatProtect2 && this.lastKernelBoardOverHeatSeriousProtect == isKernelBoardOverHeatSeriousProtect2)) {
            reset1860CheckStatus();
            if (hasExp) {
                this.errorCode = Integer.valueOf(errorCode2);
                if (!isCameraDisconnected || !isDetachableCamera(DJIProductManager.getInstance().getType())) {
                    this.hasEncodeException = true;
                } else {
                    this.hasEncodeExceptionWithDetachableCamera = true;
                }
                if (cpldAbnormal || spiAbnormal) {
                    this.needReturnException = true;
                } else {
                    this.needRestartException = true;
                }
                this.isEncoderUpgrade = isEncoderUpgrade2;
                this.isKernelBoardOverHeatProtect = isKernelBoardOverHeatProtect2;
                this.isKernelBoardOverHeatSeriousProtect = isKernelBoardOverHeatSeriousProtect2;
                notifyChange();
            }
        }
        this.last1860HasException = hasExp;
        this.last1860CpldI2CAbnormal = cpldAbnormal;
        this.last1860SPIAbnormal = spiAbnormal;
        this.lastEncoderUpgrade = isEncoderUpgrade2;
        this.lastKernelBoardOverHeatProtect = isKernelBoardOverHeatProtect2;
        this.lastKernelBoardOverHeatSeriousProtect = isKernelBoardOverHeatSeriousProtect2;
    }

    private boolean isDetachableCamera(ProductType productType) {
        if (!ProductType.isValidType(productType)) {
            return false;
        }
        if (ProductType.Orange == productType || ProductType.Orange2 == productType || ProductType.BigBanana == productType || ProductType.PM820 == productType || ProductType.OrangeRAW == productType || ProductType.OrangeCV600 == productType || ProductType.PM820PRO == productType || ProductType.M200 == productType || ProductType.M210 == productType || ProductType.M210RTK == productType) {
            return true;
        }
        return false;
    }

    private void reset1860CheckStatus() {
        this.hasEncodeExceptionWithDetachableCamera = false;
        this.hasEncodeException = false;
        this.needRestartException = false;
        this.needReturnException = false;
        this.lastEncoderUpgrade = false;
        this.lastKernelBoardOverHeatProtect = false;
        this.lastKernelBoardOverHeatSeriousProtect = false;
    }

    private void updateOsdSignal(DataOsdGetPushSignalQuality signal) {
        DJISDKCacheParamValue downSignalQuality;
        int upSignalQuality = signal.getUpSignalQuality();
        if (upSignalQuality >= 50 || upSignalQuality == 0) {
            this.isLowRCSignal = false;
        } else {
            this.isLowRCSignal = true;
        }
        updateSdrRcSignal(upSignalQuality);
        if (!CommonUtil.isWifiProduct(null)) {
            int radioSignal = 0;
            if (CommonUtil.isSdrProducts(null)) {
                downSignalQuality = DJISDKCache.getInstance().getAvailableValue(KeyHelper.getOcuSyncLinkKey(AirLinkKeys.DOWNLINK_SIGNAL_QUALITY));
            } else {
                downSignalQuality = DJISDKCache.getInstance().getAvailableValue(KeyHelper.getLightbridgeLinkKey(AirLinkKeys.DOWNLINK_SIGNAL_QUALITY));
            }
            if (downSignalQuality != null) {
                radioSignal = ((Integer) downSignalQuality.getData()).intValue();
            }
            if (radioSignal >= 50 || radioSignal == 0) {
                this.isLowRadioSignal = false;
            } else {
                this.isLowRadioSignal = true;
            }
            updateSdrRadioSignal(radioSignal);
        }
        if (DJIUSBWifiSwitchManager.getInstance().isProductWifiConnected(null)) {
            this.isLowRCSignal = false;
        }
    }

    private void updateSdrRcSignal(int signalVal) {
        this.isLowRCSignal = false;
        this.isStrongRCRadioSignalNoise = false;
        if (CommonUtil.isSdrProducts(null)) {
            if (signalVal == 5 || signalVal == 15) {
                this.isLowRCSignal = true;
            } else if (signalVal == 6 || signalVal == 16) {
                this.isStrongRCRadioSignalNoise = true;
            }
        }
    }

    private void updateSdrRadioSignal(int signalVal) {
        this.isLowRadioSignal = false;
        this.isStrongRadioSignalNoise = false;
        if (CommonUtil.isSdrProducts(null)) {
            if (signalVal == 5 || signalVal == 15) {
                this.isLowRadioSignal = true;
            } else if (signalVal == 6 || signalVal == 16) {
                this.isStrongRadioSignalNoise = true;
            }
        }
    }

    private boolean getIsNoSignal() {
        boolean isError1 = false;
        boolean isError2 = false;
        if (DataOsdGetPushSignalQuality.getInstance().isGetted()) {
            isError1 = DataOsdGetPushSignalQuality.getInstance().getUpSignalQuality() == 0;
        }
        if (DataWifiGetPushSignal.getInstance().isGetted()) {
            if (DataWifiGetPushSignal.getInstance().getSignal() == 0) {
                isError2 = true;
            } else {
                isError2 = false;
            }
        }
        if (isError1 || isError2) {
            return true;
        }
        return false;
    }
}
