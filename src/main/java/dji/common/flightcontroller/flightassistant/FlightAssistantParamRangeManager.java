package dji.common.flightcontroller.flightassistant;

import dji.common.mission.activetrack.QuickShotMode;
import dji.common.product.Model;
import dji.sdksharedlib.DJISDKCache;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.extension.KeyHelper;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.keycatalog.DJISDKCacheKeys;
import dji.sdksharedlib.keycatalog.ProductKeys;
import dji.sdksharedlib.listener.DJIParamAccessListener;
import dji.sdksharedlib.store.DJISDKCacheParamValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FlightAssistantParamRangeManager {
    private static final String TAG = "FlightAssistantParamRangeManager";
    private DJISDKCacheKey mDefaultKey;
    private List<DJIParamAccessListener> mListeners = new ArrayList();
    private DJISDKCacheHWAbstraction.OnValueChangeListener mOnValueChangeListener;
    private QuickShotMode[] mQuickShotActionTypeRange = null;

    public FlightAssistantParamRangeManager(DJISDKCacheHWAbstraction.OnValueChangeListener valueChangeListener, DJISDKCacheKey defaultKey) {
        this.mOnValueChangeListener = valueChangeListener;
        this.mDefaultKey = defaultKey;
        if (this.mOnValueChangeListener != null) {
            triggerUpdateAll();
            addListenersForQuickshotActionTypeRange(defaultKey.clone(ProductKeys.MODEL_NAME));
        }
    }

    public void onDestroy() {
        for (DJIParamAccessListener listener : this.mListeners) {
            DJISDKCache.getInstance().stopListening(listener);
        }
        this.mListeners = null;
        this.mOnValueChangeListener = null;
    }

    private void addListenersForQuickshotActionTypeRange(DJISDKCacheKey... keys) {
        addListener(new FlightAssistantParamRangeManager$$Lambda$0(this), keys);
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$addListenersForQuickshotActionTypeRange$0$FlightAssistantParamRangeManager(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
        updateQuickshotActionTypeRange();
    }

    private void addListener(DJIParamAccessListener listener, DJISDKCacheKey... keys) {
        for (DJISDKCacheKey key : keys) {
            addOneListener(key, listener);
        }
    }

    private void addOneListener(DJISDKCacheKey key, DJIParamAccessListener listener) {
        DJISDKCache.getInstance().startListeningForUpdates(key, listener, false);
        if (!this.mListeners.contains(listener)) {
            this.mListeners.add(listener);
        }
    }

    private void triggerUpdateAll() {
        updateQuickshotActionTypeRange();
    }

    private void updateQuickshotActionTypeRange() {
        QuickShotMode[] list = null;
        Model model = (Model) CacheHelper.getValue(KeyHelper.getProductKey(ProductKeys.MODEL_NAME));
        if (isProdcutConnected()) {
            if (model == Model.MAVIC_AIR || model == Model.MAVIC_2_PRO) {
                list = new QuickShotMode[]{QuickShotMode.CIRCLE, QuickShotMode.DRONIE, QuickShotMode.HELIX, QuickShotMode.ROCKET, QuickShotMode.BOOMERANG, QuickShotMode.ASTEROID};
            } else if (model == Model.MAVIC_2_ZOOM) {
                list = new QuickShotMode[]{QuickShotMode.CIRCLE, QuickShotMode.DRONIE, QuickShotMode.HELIX, QuickShotMode.ROCKET, QuickShotMode.BOOMERANG, QuickShotMode.ASTEROID, QuickShotMode.DOLLY_ZOOM};
            } else if (model == Model.WM160) {
                list = new QuickShotMode[]{QuickShotMode.DRONIE, QuickShotMode.ROCKET, QuickShotMode.CIRCLE, QuickShotMode.HELIX};
            }
            if (list == null) {
                return;
            }
            if (this.mQuickShotActionTypeRange == null || !Arrays.deepEquals(this.mQuickShotActionTypeRange, list)) {
                this.mQuickShotActionTypeRange = list;
                if (this.mOnValueChangeListener != null) {
                    this.mOnValueChangeListener.onNewValue(list, this.mDefaultKey.clone(ProductKeys.QUICK_SHOT_MODE_RANGE));
                }
            }
        }
    }

    private boolean isProdcutConnected() {
        DJISDKCacheParamValue paramValue = DJISDKCache.getInstance().getAvailableValue(KeyHelper.getProductKey(DJISDKCacheKeys.CONNECTION));
        if (paramValue != null) {
            return ((Boolean) paramValue.getData()).booleanValue();
        }
        return false;
    }
}
