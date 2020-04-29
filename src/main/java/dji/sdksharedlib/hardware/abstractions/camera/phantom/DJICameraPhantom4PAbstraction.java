package dji.sdksharedlib.hardware.abstractions.camera.phantom;

import dji.common.camera.SettingsDefinitions;
import dji.common.error.DJICameraError;
import dji.common.util.CallbackUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.CmdIdCamera;
import dji.midware.data.model.P3.DataBaseCameraSetting;
import dji.midware.data.model.P3.DataEyeSetPseudoCameraControl;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.hardware.abstractions.camera.DJICameraAbstraction;
import dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction;
import dji.sdksharedlib.keycatalog.CameraKeys;
import dji.sdksharedlib.keycatalog.extension.InternalKey;

@EXClassNullAway
@InternalKey
public class DJICameraPhantom4PAbstraction extends DJICameraBaseAbstraction {
    /* access modifiers changed from: protected */
    public boolean isMediaDownloadModeSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isVideoPlaybackSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public String getDisplayName() {
        return DJICameraAbstraction.DisplaynamePhantom4ProCamera;
    }

    /* access modifiers changed from: protected */
    public boolean isSlowMotionSupported() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isHDRPhotoSupported() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isMediaDownModeMapValue2() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isAdjustableApertureSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isAdjustableFocalPointSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isMechanicalShutterSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public void initializeComponentCharacteristics() {
        addCharacteristics(CameraKeys.class, getClass());
    }

    /* access modifiers changed from: protected */
    public boolean isNewProcessOfActivation() {
        return true;
    }

    /* access modifiers changed from: protected */
    public int mapCameraModeToProtocolValue(SettingsDefinitions.CameraMode mode) {
        if (SettingsDefinitions.CameraMode.MEDIA_DOWNLOAD == mode) {
            return 2;
        }
        return mode.value();
    }

    /* access modifiers changed from: protected */
    public boolean checkTrueColorDigitalFilterSupported() {
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
            /* class dji.sdksharedlib.hardware.abstractions.camera.phantom.DJICameraPhantom4PAbstraction.AnonymousClass1 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJICameraError.getDJIError(ccode));
            }
        });
    }

    @Setter(CameraKeys.SHOOT_PHOTO_MODE)
    public void setShootPhotoMode(SettingsDefinitions.ShootPhotoMode photoMode, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (isShootPhotoModeSupported(photoMode, callback)) {
            SettingsDefinitions.ShootPhotoMode currentMode = (SettingsDefinitions.ShootPhotoMode) CacheHelper.getCamera(CameraKeys.SHOOT_PHOTO_MODE);
            if (currentMode != null && currentMode == SettingsDefinitions.ShootPhotoMode.PANORAMA) {
                new DataEyeSetPseudoCameraControl().setPseudoCameraCmd(DataEyeSetPseudoCameraControl.PseudoCameraCmd.PSEUDO_CAMERA_CMD_RELEASE).start((DJIDataCallBack) null);
            }
            startSetShootPhotoModeCmd(photoMode, callback);
        }
    }
}
