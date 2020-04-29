package dji.internal.mock;

import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.mock.abstractions.MockBatteryAbstraction;
import dji.internal.mock.abstractions.MockCameraAbstraction;
import dji.internal.mock.abstractions.MockFlightControllerAbstraction;
import dji.internal.mock.abstractions.MockGimbalAbstraction;
import dji.internal.mock.abstractions.MockLB1AirLinkAbstraction;
import dji.internal.mock.abstractions.MockProductAbstraction;
import dji.internal.mock.abstractions.MockRemoteControllerAbstraction;
import dji.internal.mock.abstractions.MockWifiLinkFoldingDroneAbstraction;
import dji.log.DJILog;
import dji.sdksharedlib.hardware.DJISDKCacheHWAbstractionLayer;
import dji.sdksharedlib.hardware.abstractions.airlink.DJIAirLinkAbstraction;
import dji.sdksharedlib.hardware.extension.DJISDKCacheAutoGetterVerifier;
import dji.sdksharedlib.keycatalog.BatteryKeys;
import dji.sdksharedlib.keycatalog.CameraKeys;
import dji.sdksharedlib.keycatalog.FlightControllerKeys;
import dji.sdksharedlib.keycatalog.GimbalKeys;
import dji.sdksharedlib.keycatalog.ProductKeys;
import dji.sdksharedlib.keycatalog.RemoteControllerKeys;
import dji.sdksharedlib.keycatalog.airlink.AirLinkKeys;
import dji.sdksharedlib.listener.DJISDKCacheListenerLayer;
import dji.sdksharedlib.store.DJISDKCacheStoreLayer;
import java.util.concurrent.ConcurrentHashMap;

@EXClassNullAway
public class MockDJISDKCacheHWAbstractionLayer extends DJISDKCacheHWAbstractionLayer {
    public void init(DJISDKCacheStoreLayer storeLayer, DJISDKCacheListenerLayer listenerLayer) {
        DJILog.d(this.TAG, "init in MockDJISDKCacheHWAbstractionLayer", new Object[0]);
        this.storeLayer = storeLayer;
        this.hwAbstractionMap = new ConcurrentHashMap();
        initProductAbstraction();
        this.autoGetterVerifier = new DJISDKCacheAutoGetterVerifier();
        this.autoGetterVerifier.init(this, listenerLayer);
    }

    /* access modifiers changed from: protected */
    public void initProductAbstraction() {
        removeAbstraction(ProductKeys.COMPONENT_KEY);
        addAbstraction(1, ProductKeys.COMPONENT_KEY, MockProductAbstraction.class);
    }

    /* access modifiers changed from: protected */
    public void addCameraAbstraction() {
        addAbstraction(1, CameraKeys.COMPONENT_KEY, MockCameraAbstraction.class);
    }

    /* access modifiers changed from: protected */
    public void addBatteryAbstraction() {
        removeAbstraction(BatteryKeys.COMPONENT_KEY);
        addAbstraction(1, BatteryKeys.COMPONENT_KEY, MockBatteryAbstraction.class);
    }

    /* access modifiers changed from: protected */
    public void addFlightController() {
        removeAbstraction(FlightControllerKeys.COMPONENT_KEY);
        addAbstraction(1, FlightControllerKeys.COMPONENT_KEY, MockFlightControllerAbstraction.class);
    }

    /* access modifiers changed from: protected */
    public void addGimbalAbstraction() {
        removeAbstraction(GimbalKeys.COMPONENT_KEY);
        addAbstraction(1, GimbalKeys.COMPONENT_KEY, MockGimbalAbstraction.class);
    }

    /* access modifiers changed from: protected */
    public void addAirLinkAbstraction() {
        DJILog.d("Mock AirLink", "addAirLinkAbstraction", new Object[0]);
        removeAbstraction(AirLinkKeys.COMPONENT_KEY);
        try {
            addAirLinkAbstraction(new DJIAirLinkAbstraction(new MockWifiLinkFoldingDroneAbstraction(), new MockLB1AirLinkAbstraction()));
            DJILog.d("Mock AirLink", "Done adding abstraction", new Object[0]);
        } catch (Exception e) {
            DJILog.d("Mock AirLink", "DJISDKCacheHWAbstractionLayer addAbstraction Exception  : AirLink" + DJILog.exceptionToString(e), new Object[0]);
        }
    }

    /* access modifiers changed from: protected */
    public void addRcAbstraction() {
        DJILog.d("Mock AirLink", "addRcAbstractionn", new Object[0]);
        removeAbstraction(RemoteControllerKeys.COMPONENT_KEY);
        addAbstraction(1, RemoteControllerKeys.COMPONENT_KEY, MockRemoteControllerAbstraction.class);
    }

    public void notifyFakeComponentChanged() {
        notifyComponentChanged();
    }

    public void mockRemoveAbstraction(String hwName) {
        removeAbstraction(hwName);
    }
}
