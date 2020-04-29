package dji.internal.diagnostics.handler.util;

import dji.common.battery.BatterySOPTemperatureLevel;
import dji.diagnostics.model.DJIDiagnosticsError;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.extension.KeyHelper;
import dji.sdksharedlib.keycatalog.BatteryKeys;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.listener.DJIParamAccessListener;
import dji.thirdparty.io.reactivex.Observable;
import dji.thirdparty.io.reactivex.ObservableEmitter;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.utils.Optional;
import java.util.Map;
import java.util.Objects;

public class DiagnosticsMotorFailModel extends DiagnosticsModelBase<DataOsdGetPushCommon> {
    private static final DJISDKCacheKey LOW_TEMP_KEY = KeyHelper.getBatteryKey(BatteryKeys.LOW_TEMPERATURE_LEVEL);
    private DataOsdGetPushCommon.MotorStartFailedCause lastCause;
    private Disposable mLowTempDisposable;
    private Map<DataOsdGetPushCommon.MotorStartFailedCause, Integer> reasonCodeMap;

    public DiagnosticsMotorFailModel(Map<DataOsdGetPushCommon.MotorStartFailedCause, Integer> errorCodeMap, UpdateInterface updater) {
        this.reasonCodeMap = errorCodeMap;
        this.mUpdater = updater;
    }

    /* access modifiers changed from: protected */
    public Integer applyToCode(DataOsdGetPushCommon pushCommon) {
        DataOsdGetPushCommon.MotorStartFailedCause motorFailedCause = pushCommon.isMotorUp() ? null : pushCommon.getMotorStartCauseNoStartAction();
        if (DataOsdGetPushCommon.MotorStartFailedCause.None.equals(motorFailedCause) || motorFailedCause == null) {
            motorFailedCause = pushCommon.getMotorFailedCause();
        }
        if (motorFailedCause == DataOsdGetPushCommon.MotorStartFailedCause.None || motorFailedCause == null) {
            this.lastCause = motorFailedCause;
            return null;
        }
        Integer resultCode = this.reasonCodeMap.get(motorFailedCause);
        if (!Objects.equals(motorFailedCause, this.lastCause)) {
            DiagnosticsLog.logi("diagnostics", "statusApply MotorStartFailedCause " + motorFailedCause + " code=" + resultCode);
        }
        if (resultCode == null) {
            resultCode = Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF);
        }
        this.lastCause = motorFailedCause;
        return resultCode;
    }

    /* access modifiers changed from: protected */
    public boolean onCodeChange(DataOsdGetPushCommon value) {
        if (Objects.equals(getDiagnosticsCode(), Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_TEMPERATURE_LOW))) {
            BatterySOPTemperatureLevel level = (BatterySOPTemperatureLevel) CacheHelper.getValue(LOW_TEMP_KEY);
            DiagnosticsLog.logi("diagnostics", "BatterySOPTemperatureLevel:" + level);
            if (level == null) {
                level = BatterySOPTemperatureLevel.UNKNOWN;
            }
            this.mExtraData = level;
            observeTempLevel();
            if (this.mExtraData == BatterySOPTemperatureLevel.UNKNOWN) {
                return false;
            }
            return true;
        }
        if (Objects.equals(getDiagnosticsCode(), Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_BATTERY_NOT_IN_POSITION))) {
            this.mExtraData = Boolean.valueOf(value.isMotorUp());
        } else {
            disposeTempLevelStream();
            this.mExtraData = this.lastCause;
        }
        return super.onCodeChange((Object) value);
    }

    private void disposeTempLevelStream() {
        if (this.mLowTempDisposable != null) {
            this.mLowTempDisposable.dispose();
        }
    }

    private void observeTempLevel() {
        if (this.mLowTempDisposable == null || this.mLowTempDisposable.isDisposed()) {
            this.mLowTempDisposable = getSharedLibListenerObservable(LOW_TEMP_KEY).map(DiagnosticsMotorFailModel$$Lambda$0.$instance).takeWhile(new DiagnosticsMotorFailModel$$Lambda$1(this)).filter(new DiagnosticsMotorFailModel$$Lambda$2(this)).subscribe(new DiagnosticsMotorFailModel$$Lambda$3(this));
        }
    }

    static final /* synthetic */ BatterySOPTemperatureLevel lambda$observeTempLevel$0$DiagnosticsMotorFailModel(Optional optional) throws Exception {
        return (BatterySOPTemperatureLevel) optional.orElse(BatterySOPTemperatureLevel.UNKNOWN);
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ boolean lambda$observeTempLevel$1$DiagnosticsMotorFailModel(BatterySOPTemperatureLevel level) throws Exception {
        return Objects.equals(getDiagnosticsCode(), Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_TEMPERATURE_LOW));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ boolean lambda$observeTempLevel$2$DiagnosticsMotorFailModel(BatterySOPTemperatureLevel level) throws Exception {
        return !level.equals(this.mExtraData);
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$observeTempLevel$3$DiagnosticsMotorFailModel(BatterySOPTemperatureLevel level) throws Exception {
        this.mExtraData = level;
        this.mUpdater.onStatusUpdate();
    }

    public <T> Observable<Optional<T>> getSharedLibListenerObservable(DJISDKCacheKey cacheKey) {
        return Observable.create(new DiagnosticsMotorFailModel$$Lambda$4(cacheKey));
    }

    static final /* synthetic */ void lambda$getSharedLibListenerObservable$6$DiagnosticsMotorFailModel(DJISDKCacheKey cacheKey, ObservableEmitter emitter) throws Exception {
        DJIParamAccessListener listener = new DiagnosticsMotorFailModel$$Lambda$5(emitter, cacheKey);
        CacheHelper.addListener(listener, cacheKey);
        emitter.setCancellable(new DiagnosticsMotorFailModel$$Lambda$6(listener));
    }

    public void reset() {
        this.lastCause = null;
        disposeTempLevelStream();
        super.reset();
    }
}
