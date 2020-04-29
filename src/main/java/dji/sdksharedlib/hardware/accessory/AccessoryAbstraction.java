package dji.sdksharedlib.hardware.accessory;

import dji.midware.data.model.P3.DataOnBoardSDKGetPushAccessoryInfo;
import dji.midware.util.BackgroundLooper;
import dji.midware.util.DJIEventBusUtil;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.keycatalog.AccessoryAggregationKeys;
import dji.sdksharedlib.keycatalog.BeaconKeys;
import dji.sdksharedlib.keycatalog.SpeakerKeys;
import dji.sdksharedlib.keycatalog.SpotlightKeys;
import dji.sdksharedlib.store.DJISDKCacheStoreLayer;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class AccessoryAbstraction extends DJISDKCacheHWAbstraction {
    public static final int TIME_OUT_MILLIS = 5000;
    protected Runnable disconnectSubcomponentsRunnable = new Runnable() {
        /* class dji.sdksharedlib.hardware.accessory.AccessoryAbstraction.AnonymousClass1 */

        public void run() {
            if (AccessoryAbstraction.this.shouldUpdateNavigationLEDComponent(false)) {
                AccessoryAbstraction.this.updateNavigationLEDComponent();
            }
            if (AccessoryAbstraction.this.shouldUpdateSearchlightLEDComponent(false)) {
                AccessoryAbstraction.this.updateSearchlightLEDComponent();
            }
            if (AccessoryAbstraction.this.shouldUpdateSpeakerComponent(false)) {
                AccessoryAbstraction.this.updateSpeakerComponent();
            }
        }
    };
    private boolean isNavigationLEDConnected;
    private boolean isSeachlightLEDConnected;
    private boolean isSpeakerConnected;
    protected DJISDKCacheStoreLayer storeLayer;

    public void init(String component, int index, DJISDKCacheStoreLayer storeLayer2, DJISDKCacheHWAbstraction.OnValueChangeListener onValueChangeListener) {
        super.init(component, index, storeLayer2, onValueChangeListener);
        this.storeLayer = storeLayer2;
        DJIEventBusUtil.register(this);
    }

    public void destroy() {
        DJIEventBusUtil.unRegister(this);
        BackgroundLooper.remove(this.disconnectSubcomponentsRunnable);
        super.destroy();
    }

    /* access modifiers changed from: protected */
    public void initializeComponentCharacteristics() {
        addCharacteristics(AccessoryAggregationKeys.class, getClass());
    }

    public void syncPushDataFromMidware() {
        super.syncPushDataFromMidware();
        if (DataOnBoardSDKGetPushAccessoryInfo.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataOnBoardSDKGetPushAccessoryInfo.getInstance());
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataOnBoardSDKGetPushAccessoryInfo info) {
        if (shouldUpdateNavigationLEDComponent(info.isNavigationLEDConnected())) {
            updateNavigationLEDComponent();
        }
        if (shouldUpdateSearchlightLEDComponent(info.isSearchlightConnected())) {
            updateSearchlightLEDComponent();
        }
        if (shouldUpdateSpeakerComponent(info.isSpeakerConnected())) {
            updateSpeakerComponent();
        }
        disconnectSubComponents();
    }

    /* access modifiers changed from: private */
    public boolean shouldUpdateNavigationLEDComponent(boolean isConnected) {
        if (this.isNavigationLEDConnected == isConnected) {
            return false;
        }
        this.isNavigationLEDConnected = isConnected;
        return true;
    }

    /* access modifiers changed from: private */
    public boolean shouldUpdateSearchlightLEDComponent(boolean isConnected) {
        if (this.isSeachlightLEDConnected == isConnected) {
            return false;
        }
        this.isSeachlightLEDConnected = isConnected;
        return true;
    }

    /* access modifiers changed from: private */
    public boolean shouldUpdateSpeakerComponent(boolean isConnected) {
        if (this.isSpeakerConnected == isConnected) {
            return false;
        }
        this.isSpeakerConnected = isConnected;
        return true;
    }

    /* access modifiers changed from: private */
    public void updateNavigationLEDComponent() {
        if (this.isNavigationLEDConnected) {
            BeaconAbstraction navigationLEDAbstraction = new BeaconAbstraction();
            addSubComponents(navigationLEDAbstraction, BeaconKeys.COMPONENT_KEY, 0, this.storeLayer);
            navigationLEDAbstraction.syncPushDataFromMidware();
            return;
        }
        removeSubComponents(BeaconKeys.COMPONENT_KEY, this.storeLayer);
    }

    /* access modifiers changed from: private */
    public void updateSearchlightLEDComponent() {
        if (this.isSeachlightLEDConnected) {
            SpotlightAbstraction searchlightLEDAbstraction = new SpotlightAbstraction();
            addSubComponents(searchlightLEDAbstraction, SpotlightKeys.COMPONENT_KEY, 0, this.storeLayer);
            searchlightLEDAbstraction.syncPushDataFromMidware();
            return;
        }
        removeSubComponents(SpotlightKeys.COMPONENT_KEY, this.storeLayer);
    }

    /* access modifiers changed from: private */
    public void updateSpeakerComponent() {
        if (this.isSpeakerConnected) {
            SpeakerAbstraction speakerAbstraction = new SpeakerAbstraction();
            addSubComponents(speakerAbstraction, SpeakerKeys.COMPONENT_KEY, 0, this.storeLayer);
            speakerAbstraction.syncPushDataFromMidware();
            return;
        }
        removeSubComponents(SpeakerKeys.COMPONENT_KEY, this.storeLayer);
    }

    /* access modifiers changed from: protected */
    public void disconnectSubComponents() {
        BackgroundLooper.remove(this.disconnectSubcomponentsRunnable);
        BackgroundLooper.postDelayed(this.disconnectSubcomponentsRunnable, 5000);
    }
}
