package com.dji.component.fpv.widget.preview;

import com.dji.component.fpv.base.PersistenceObservable;
import com.dji.component.persistence.DJIPersistenceStorage;
import com.dji.rx.sharedlib.SharedLibPushObservable;
import com.dji.video.framing.utils.DJIVideoUtil;
import dji.common.camera.CameraRecordingState;
import dji.common.camera.SettingsDefinitions;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.extension.KeyHelper;
import dji.sdksharedlib.keycatalog.CameraKeys;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.keycatalog.IntelligentFlightAssistantKeys;
import dji.utils.Optional;
import io.reactivex.Observable;

public class DJIVideoRecordModel {
    private SharedLibPushObservable<SettingsDefinitions.CameraMode> mObservableCameraMode = new SharedLibPushObservable<>(KeyHelper.getCameraKey(0, "Mode"), SettingsDefinitions.CameraMode.UNKNOWN);
    private SharedLibPushObservable<CameraRecordingState> mObservableIsRecording = new SharedLibPushObservable<>(KeyHelper.getCameraKey(0, CameraKeys.RECORDING_STATE), CameraRecordingState.UNKNOWN);
    private PersistenceObservable mPersistenceObservable = new PersistenceObservable(DJIVideoUtil.KEY_RECORDING_CACHING_KEY, Integer.class, Integer.valueOf(DJIVideoUtil.VideoCachingSize.GB_2.value()));
    DJISDKCacheKey mQsEnableKey = KeyHelper.getIntelligentFlightAssistantKey(IntelligentFlightAssistantKeys.IS_MULTI_QUICK_SHOT_ENABLED);

    public Observable<Optional<CameraRecordingState>> getRecordStatusObservable() {
        return this.mObservableIsRecording;
    }

    public Observable<Optional<SettingsDefinitions.CameraMode>> getCameraModeObservable() {
        return this.mObservableCameraMode;
    }

    public PersistenceObservable getPersistenceObservable() {
        return this.mPersistenceObservable;
    }

    public boolean canRecordingCache() {
        return DJIPersistenceStorage.getBoolean(DJIVideoUtil.KEY_RECORDING_CACHING_KEY, true);
    }

    public boolean isQSEnable() {
        return ((Boolean) CacheHelper.getValue(this.mQsEnableKey, false)).booleanValue();
    }
}
