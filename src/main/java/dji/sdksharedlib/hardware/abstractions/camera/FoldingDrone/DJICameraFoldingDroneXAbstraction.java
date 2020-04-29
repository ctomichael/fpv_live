package dji.sdksharedlib.hardware.abstractions.camera.FoldingDrone;

import dji.common.camera.SettingsDefinitions;
import dji.common.error.DJICameraError;
import dji.common.util.CallbackUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.CmdIdCamera;
import dji.midware.data.model.P3.DataBaseCameraSetting;
import dji.midware.data.model.P3.DataCameraGetCameraRotationMode;
import dji.midware.data.model.P3.DataCameraSetCameraRotationMode;
import dji.midware.data.model.P3.DataEyeSetPseudoCameraControl;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.hardware.abstractions.camera.DJICameraAbstraction;
import dji.sdksharedlib.keycatalog.CameraKeys;

@EXClassNullAway
public class DJICameraFoldingDroneXAbstraction extends DJICameraFoldingDroneSAbstraction {
    /* access modifiers changed from: protected */
    public boolean isSlowMotionSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isDigitalZoomScaleSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isAdjustableFocalPointSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isMFSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isAFSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isVideoPlaybackSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isBurstCountSupported(SettingsDefinitions.PhotoBurstCount count) {
        return (count == SettingsDefinitions.PhotoBurstCount.BURST_COUNT_10 || count == SettingsDefinitions.PhotoBurstCount.BURST_COUNT_14) ? false : true;
    }

    /* access modifiers changed from: protected */
    public String getDisplayName() {
        return DJICameraAbstraction.DisplayNameMavicProCamera;
    }

    @Setter(CameraKeys.ORIENTATION)
    public void setOrientation(SettingsDefinitions.Orientation mode, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        ((DataCameraSetCameraRotationMode) DataCameraSetCameraRotationMode.getInstance().setReceiverId(getReceiverIdByIndex(), DataCameraSetCameraRotationMode.class)).setImageOrientationMode(0).setOrientation(DataCameraSetCameraRotationMode.RotationAngleType.find(mode.value())).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.FoldingDrone.DJICameraFoldingDroneXAbstraction.AnonymousClass1 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    @Getter(CameraKeys.ORIENTATION)
    public void getOrientation(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        ((DataCameraGetCameraRotationMode) DataCameraGetCameraRotationMode.getInstance().setReceiverId(getReceiverIdByIndex(), DataCameraGetCameraRotationMode.class)).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.FoldingDrone.DJICameraFoldingDroneXAbstraction.AnonymousClass2 */

            public void onSuccess(Object model) {
                SettingsDefinitions.Orientation orientation;
                switch (AnonymousClass4.$SwitchMap$dji$midware$data$model$P3$DataCameraSetCameraRotationMode$RotationAngleType[DataCameraGetCameraRotationMode.getInstance().getRotationAngleType().ordinal()]) {
                    case 1:
                    case 2:
                        orientation = SettingsDefinitions.Orientation.LANDSCAPE;
                        break;
                    case 3:
                    case 4:
                        orientation = SettingsDefinitions.Orientation.PORTRAIT;
                        break;
                    default:
                        orientation = SettingsDefinitions.Orientation.UNKNOWN;
                        break;
                }
                CallbackUtils.onSuccess(callback, orientation);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    /* renamed from: dji.sdksharedlib.hardware.abstractions.camera.FoldingDrone.DJICameraFoldingDroneXAbstraction$4  reason: invalid class name */
    static /* synthetic */ class AnonymousClass4 {
        static final /* synthetic */ int[] $SwitchMap$dji$midware$data$model$P3$DataCameraSetCameraRotationMode$RotationAngleType = new int[DataCameraSetCameraRotationMode.RotationAngleType.values().length];

        static {
            try {
                $SwitchMap$dji$midware$data$model$P3$DataCameraSetCameraRotationMode$RotationAngleType[DataCameraSetCameraRotationMode.RotationAngleType.Rotate0.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$dji$midware$data$model$P3$DataCameraSetCameraRotationMode$RotationAngleType[DataCameraSetCameraRotationMode.RotationAngleType.Rotate180.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$dji$midware$data$model$P3$DataCameraSetCameraRotationMode$RotationAngleType[DataCameraSetCameraRotationMode.RotationAngleType.Rotate90.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$dji$midware$data$model$P3$DataCameraSetCameraRotationMode$RotationAngleType[DataCameraSetCameraRotationMode.RotationAngleType.Rotate270.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    public void setLensFocusMode(final SettingsDefinitions.FocusMode focusMode, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (focusMode == SettingsDefinitions.FocusMode.UNKNOWN) {
            CallbackUtils.onFailure(callback, DJICameraError.INVALID_PARAMETERS);
            return;
        }
        DataBaseCameraSetting setter = new DataBaseCameraSetting();
        setter.setReceiverId(getReceiverIdByIndex());
        setter.setCmdId(CmdIdCamera.CmdIdType.SetFocusMode).setValue(focusMode.value()).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.FoldingDrone.DJICameraFoldingDroneXAbstraction.AnonymousClass3 */

            public void onSuccess(Object model) {
                if (focusMode == SettingsDefinitions.FocusMode.AFC) {
                    new DataBaseCameraSetting().setCmdId(CmdIdCamera.CmdIdType.SetMetering).setValue(3).start(new DJIDataCallBack() {
                        /* class dji.sdksharedlib.hardware.abstractions.camera.FoldingDrone.DJICameraFoldingDroneXAbstraction.AnonymousClass3.AnonymousClass1 */

                        public void onSuccess(Object model) {
                            CallbackUtils.onSuccess(callback, (Object) null);
                        }

                        public void onFailure(Ccode ccode) {
                            CallbackUtils.onFailure(callback, DJICameraError.getDJIError(ccode));
                        }
                    });
                } else {
                    CallbackUtils.onSuccess(callback, (Object) null);
                }
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJICameraError.getDJIError(ccode));
            }
        });
    }

    /* access modifiers changed from: protected */
    public boolean isNewProcessOfActivation() {
        return true;
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
