package dji.sdksharedlib.hardware.abstractions.camera.zenmuse;

import dji.common.camera.SettingsDefinitions;
import dji.common.error.DJICameraError;
import dji.common.util.CallbackUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.CmdIdCamera;
import dji.midware.data.model.P3.DataBaseCameraSetting;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.camera.DJICameraAbstraction;

@EXClassNullAway
public class DJICameraX4SAbstraction extends DJICameraInspire2Abstraction {
    /* access modifiers changed from: protected */
    public String getDisplayName() {
        return DJICameraAbstraction.DisplayNameX4S;
    }

    /* access modifiers changed from: protected */
    public boolean isShootPhotoRawBurstSupported() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isMechanicalShutterSupported() {
        return true;
    }

    public void setLensFocusMode(SettingsDefinitions.FocusMode focusMode, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (focusMode == SettingsDefinitions.FocusMode.UNKNOWN) {
            CallbackUtils.onFailure(callback, DJICameraError.INVALID_PARAMETERS);
            return;
        }
        DataBaseCameraSetting setter = new DataBaseCameraSetting();
        setter.setReceiverId(getReceiverIdByIndex());
        setter.setCmdId(CmdIdCamera.CmdIdType.SetFocusMode).setValue(focusMode.value()).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraX4SAbstraction.AnonymousClass1 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJICameraError.getDJIError(ccode));
            }
        });
    }
}
