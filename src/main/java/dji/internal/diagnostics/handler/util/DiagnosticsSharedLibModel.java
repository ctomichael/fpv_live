package dji.internal.diagnostics.handler.util;

import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.listener.DJIParamAccessListener;
import dji.sdksharedlib.store.DJISDKCacheParamValue;
import dji.utils.function.BiPredicate;
import dji.utils.function.Function;
import dji.utils.function.Predicate;

public class DiagnosticsSharedLibModel<T> extends DiagnosticsIfModel<T> {
    private DJIParamAccessListener mDJIParamAccessListener;
    private DJISDKCacheKey mSdkKey;

    public DiagnosticsSharedLibModel(int codeIfTrue, DJISDKCacheKey sdkKey, Predicate<T> mCondition) {
        super(codeIfTrue, mCondition);
        this.mSdkKey = sdkKey;
    }

    public DiagnosticsSharedLibModel(int codeIfTrue, DJISDKCacheKey sdkKey, BiPredicate<T, T> mBiCondition) {
        super(codeIfTrue, mBiCondition);
        this.mSdkKey = sdkKey;
    }

    public DiagnosticsSharedLibModel(int codeIfTrue, DJISDKCacheKey sdkKey, Predicate<T> mCondition, Function<T, ?> extraDataFunction) {
        super(codeIfTrue, mCondition, extraDataFunction);
        this.mSdkKey = sdkKey;
    }

    public DiagnosticsSharedLibModel(int codeIfTrue, DJISDKCacheKey sdkKey, BiPredicate<T, T> mBiCondition, Function<T, ?> extraDataFunction) {
        super(codeIfTrue, mBiCondition, extraDataFunction);
        this.mSdkKey = sdkKey;
    }

    public void startSubscribe(UpdateInterface autoUpdater) {
        startSubscribe(autoUpdater, true);
    }

    public void startSubscribe(UpdateInterface autoUpdater, boolean withInitialValue) {
        this.mDJIParamAccessListener = new DiagnosticsSharedLibModel$$Lambda$0(this, autoUpdater);
        if (withInitialValue && statusApply(CacheHelper.getValue(this.mSdkKey))) {
            autoUpdater.onStatusUpdate();
        }
        CacheHelper.addListener(this.mDJIParamAccessListener, this.mSdkKey);
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$startSubscribe$0$DiagnosticsSharedLibModel(UpdateInterface autoUpdater, DJISDKCacheKey key1, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
        if (newValue != null && statusApply(newValue.getData())) {
            autoUpdater.onStatusUpdate();
        }
    }

    public void stopSubscibe() {
        CacheHelper.removeListener(this.mDJIParamAccessListener);
    }
}
