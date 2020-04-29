package dji.internal.mock.mock;

import dji.fieldAnnotation.EXClassNullAway;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.Setter;

@EXClassNullAway
public class FakeSubComponent2Abstraction extends DJISDKCacheHWAbstraction {
    private static final String TAG = "FakeComponentAbstraction";

    /* access modifiers changed from: protected */
    public void initializeComponentCharacteristics() {
        addCharacteristics(FakeSubComponentKeys.class, FakeSubComponent2Abstraction.class);
    }

    @Getter(FakeSubComponentKeys.FAKE_PUSH)
    public void getFakeSubValue(DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            callback.onSuccess(100);
        }
    }

    @Setter(FakeSubComponentKeys.FAKE_PUSH)
    public void setFakeSubValue(int value, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            callback.onSuccess(null);
        }
    }
}
