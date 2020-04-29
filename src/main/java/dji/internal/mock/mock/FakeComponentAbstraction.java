package dji.internal.mock.mock;

import dji.common.error.DJIError;
import dji.fieldAnnotation.EXClassNullAway;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.store.DJISDKCacheStoreLayer;

@EXClassNullAway
public class FakeComponentAbstraction extends DJISDKCacheHWAbstraction {
    private static final String TAG = "FakeComponentAbstraction";

    /* access modifiers changed from: protected */
    public void initializeComponentCharacteristics() {
        addCharacteristics(FakeComponentKeys.class, FakeComponentAbstraction.class);
    }

    /* access modifiers changed from: protected */
    public void initializeSubComponents(DJISDKCacheStoreLayer storeLayer) {
        addSubComponents(new FakeSubComponentAbstraction(), FakeSubComponentKeys.COMPONENT_KEY, 0, storeLayer);
        addSubComponents(new FakeSubComponentAbstraction(), FakeSubComponentKeys.COMPONENT_KEY, 1, storeLayer);
    }

    @Setter(FakeComponentKeys.FAKE_VALUE)
    public void setFakeValue(int realValue, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback == null) {
            return;
        }
        if (realValue > 0) {
            callback.onSuccess(null);
        } else {
            callback.onFails(DJIError.COMMON_PARAM_ILLEGAL);
        }
    }

    @Getter(FakeComponentKeys.FAKE_VALUE)
    public void getFakeValue(DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            callback.onSuccess(20);
        }
    }
}
