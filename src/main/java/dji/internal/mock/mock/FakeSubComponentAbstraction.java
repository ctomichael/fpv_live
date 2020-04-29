package dji.internal.mock.mock;

import dji.common.error.DJIError;
import dji.fieldAnnotation.EXClassNullAway;
import dji.sdksharedlib.hardware.abstractions.Action;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.DJISubComponentHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.Setter;

@EXClassNullAway
public class FakeSubComponentAbstraction extends DJISubComponentHWAbstraction {
    private static final String TAG = "FakeComponentAbstraction";

    public static class FakeStructParam {
    }

    /* access modifiers changed from: protected */
    public void initializeComponentCharacteristics() {
        addCharacteristics(FakeSubComponentKeys.class, FakeSubComponentAbstraction.class);
    }

    @Getter(FakeSubComponentKeys.FAKE_SUB_VALUE)
    public void getFakeSubValue(DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback == null) {
            return;
        }
        if (this.defaultKeyPath != null) {
            callback.onSuccess(Integer.valueOf(this.defaultKeyPath.getSubComponentIndex()));
        } else {
            callback.onSuccess(-1);
        }
    }

    @Setter(FakeSubComponentKeys.FAKE_SUB_VALUE)
    public void setFakeSubValue(int value, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback == null) {
            return;
        }
        if (this.defaultKeyPath == null) {
            callback.onSuccess(null);
        } else if (this.defaultKeyPath.getSubComponentIndex() == value) {
            callback.onSuccess(null);
        } else {
            callback.onFails(DJIError.COMMON_PARAM_ILLEGAL);
        }
    }

    @Action("FakeAction")
    public void fakeAction(DJISDKCacheHWAbstraction.InnerCallback callback, int param1, FakeStructParam structParam, boolean param3) {
        if (!param3) {
            callback.onFails(DJIError.COMMON_UNDEFINED);
        } else if (param1 == this.defaultKeyPath.getSubComponentIndex()) {
            callback.onSuccess(null);
        } else {
            callback.onFails(DJIError.COMMON_PARAM_ILLEGAL);
        }
    }

    @Action(FakeSubComponentKeys.FAKE_ACTION_NO_PARAM)
    public void fakeActionNoParam(DJISDKCacheHWAbstraction.InnerCallback callback) {
        callback.onSuccess(null);
    }
}
