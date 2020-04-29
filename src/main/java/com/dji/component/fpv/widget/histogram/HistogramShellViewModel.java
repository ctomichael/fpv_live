package com.dji.component.fpv.widget.histogram;

import android.support.annotation.VisibleForTesting;
import com.dji.component.fpv.base.AbstractViewModel;
import com.dji.component.fpv.base.BulletinBoardProvider;
import com.dji.rx.sharedlib.SharedLibPushObservable;
import dji.sdksharedlib.extension.KeyHelper;
import dji.sdksharedlib.keycatalog.CameraKeys;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.utils.Optional;
import io.reactivex.Observable;

public class HistogramShellViewModel extends AbstractViewModel {
    @VisibleForTesting
    DJISDKCacheKey mKeyHistogramEnable = KeyHelper.getCameraKey(0, CameraKeys.HISTOGRAM_ENABLED);
    @VisibleForTesting
    DJISDKCacheKey mKeyHistogramLightValue = KeyHelper.getCameraKey(0, CameraKeys.HISTOGRAM_LIGHT_VALUES);
    private SharedLibPushObservable<Boolean> mObservableHistogramEnable = new SharedLibPushObservable<>(this.mKeyHistogramEnable, false);
    private SharedLibPushObservable<short[]> mObservableHistogramLightValue = new SharedLibPushObservable<>(this.mKeyHistogramLightValue);

    public HistogramShellViewModel(BulletinBoardProvider bridge) {
        super(bridge);
    }

    public Observable<Optional<Boolean>> getHistogramEnableObservable() {
        return this.mObservableHistogramEnable;
    }

    public void setObservableHistogramEnable(boolean enable) {
        this.mCacheHelper.setValue(this.mKeyHistogramEnable, Boolean.valueOf(enable), null);
    }

    public Observable<Optional<short[]>> getHistogramLightValueObservable() {
        return this.mObservableHistogramLightValue;
    }

    public void onCreateView() {
    }

    public void onDestroyView() {
    }
}
