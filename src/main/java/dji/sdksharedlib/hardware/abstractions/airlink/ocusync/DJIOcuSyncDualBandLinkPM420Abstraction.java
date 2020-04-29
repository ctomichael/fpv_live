package dji.sdksharedlib.hardware.abstractions.airlink.ocusync;

import dji.common.OcuSyncPIPPosition;
import dji.common.OcuSyncSecondaryVideoFormat;
import dji.common.airlink.OcuSyncSecondaryVideoDisplayMode;
import dji.common.airlink.OcuSyncSecondaryVideoOutputPort;
import dji.common.airlink.OcuSyncUnit;
import dji.common.camera.SettingsDefinitions;
import dji.common.error.DJIAirLinkError;
import dji.common.error.DJIError;
import dji.common.util.CallbackUtils;
import dji.midware.MidWare;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.model.P3.DataCameraGetPushStateInfo;
import dji.midware.data.model.P3.DataDm368CameraVideoStreams;
import dji.midware.data.model.P3.DataDm368GetGParams;
import dji.midware.data.model.P3.DataDm368PushLiveVideoStatus;
import dji.midware.data.model.P3.DataDm368SetGParams;
import dji.midware.data.model.P3.DataRcGetMaster;
import dji.midware.data.model.P3.DataRcSetMaster;
import dji.midware.data.model.P3.DataSingleSetMainCameraBandwidthPercent;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.usb.P3.SdrLteVideoController;
import dji.midware.util.DjiSharedPreferencesManager;
import dji.midware.util.DoubleCameraSupportUtil;
import dji.sdksharedlib.extension.KeyHelper;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.keycatalog.airlink.LteLinkKeys;
import dji.sdksharedlib.keycatalog.airlink.OcuSyncLinkKeys;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class DJIOcuSyncDualBandLinkPM420Abstraction extends DJIOcuSyncDualBandLinkAbstraction {
    @Setter("SecondaryVideoOutputEnabled")
    public void setSecondaryVideoOutputEnabled(boolean enable, DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataDm368SetGParams setter = new DataDm368SetGParams();
        setter.set(DataDm368SetGParams.CmdId.SetOutputEnable, enable ? 1 : 0);
        setter.start(CallbackUtils.defaultCB(callback, DJIAirLinkError.class));
    }

    @Getter("SecondaryVideoOutputEnabled")
    public void getSecondaryVideoOutputEnabled(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataDm368GetGParams getter = DataDm368GetGParams.getInstance();
        getter.setType(true).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.airlink.ocusync.DJIOcuSyncDualBandLinkPM420Abstraction.AnonymousClass1 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, Boolean.valueOf(getter.getOutputEnable()));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    @Setter("SecondaryVideoOutputPort")
    public void setSecondaryVideoOutputPort(OcuSyncSecondaryVideoOutputPort port, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (port != null && !port.equals(OcuSyncSecondaryVideoOutputPort.Unknown)) {
            setter(DataDm368SetGParams.CmdId.SetOutputDevice, port.value(), callback);
        } else if (callback != null) {
            callback.onFails(DJIError.COMMON_PARAM_ILLEGAL);
        }
    }

    @Getter("SecondaryVideoOutputPort")
    public void getSecondaryVideoOutputPort(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            DataDm368GetGParams.getInstance().setType(true).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.airlink.ocusync.DJIOcuSyncDualBandLinkPM420Abstraction.AnonymousClass2 */

                public void onSuccess(Object model) {
                    callback.onSuccess(OcuSyncSecondaryVideoOutputPort.find(DataDm368GetGParams.getInstance().getOutputDevice()));
                }

                public void onFailure(Ccode ccode) {
                    callback.onFails(DJIAirLinkError.getDJIError(ccode));
                }
            });
        }
    }

    @Setter("SecondaryVideoDisplayMode")
    public void setSecondaryVideoDisplayMode(OcuSyncSecondaryVideoDisplayMode outputMode, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (outputMode != null && !outputMode.equals(OcuSyncSecondaryVideoDisplayMode.UNKNOWN)) {
            setter(DataDm368SetGParams.CmdId.SetOutputMode, outputMode.value(), callback);
        } else if (callback != null) {
            callback.onFails(DJIError.COMMON_PARAM_ILLEGAL);
        }
    }

    @Getter("SecondaryVideoDisplayMode")
    public void getSecondaryVideoDisplayMode(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            DataDm368GetGParams.getInstance().setType(true).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.airlink.ocusync.DJIOcuSyncDualBandLinkPM420Abstraction.AnonymousClass3 */

                public void onSuccess(Object model) {
                    callback.onSuccess(OcuSyncSecondaryVideoDisplayMode.find(DataDm368GetGParams.getInstance().getOutputMode()));
                }

                public void onFailure(Ccode ccode) {
                    callback.onFails(DJIAirLinkError.getDJIError(ccode));
                }
            });
        }
    }

    @Setter("SecondaryVideoOSDEnabled")
    public void setDisplayOSDEnabled(boolean isDisplay, DJISDKCacheHWAbstraction.InnerCallback callback) {
        setter(DataDm368SetGParams.CmdId.ShowOsd, isDisplay ? 1 : 0, callback);
    }

    @Getter("SecondaryVideoOSDEnabled")
    public void getDisplayOSDEnabled(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            DataDm368GetGParams.getInstance().setType(true).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.airlink.ocusync.DJIOcuSyncDualBandLinkPM420Abstraction.AnonymousClass4 */

                public void onSuccess(Object model) {
                    callback.onSuccess(Boolean.valueOf(DataDm368GetGParams.getInstance().getIsShowOsd()));
                }

                public void onFailure(Ccode ccode) {
                    callback.onFails(DJIAirLinkError.getDJIError(ccode));
                }
            });
        }
    }

    @Setter("SecondaryVideoOSDTopMargin")
    public void setOSDTopMargin(int margin, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (margin >= 0 && margin <= 50) {
            setter(DataDm368SetGParams.CmdId.SetOsdTop, margin, callback);
        } else if (callback != null) {
            callback.onFails(DJIError.COMMON_PARAM_ILLEGAL);
        }
    }

    @Getter("SecondaryVideoOSDTopMargin")
    public void getOSDTopMargin(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            DataDm368GetGParams.getInstance().setType(true).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.airlink.ocusync.DJIOcuSyncDualBandLinkPM420Abstraction.AnonymousClass5 */

                public void onSuccess(Object model) {
                    callback.onSuccess(Integer.valueOf(DataDm368GetGParams.getInstance().getOsdMarginTop()));
                }

                public void onFailure(Ccode ccode) {
                    callback.onFails(DJIAirLinkError.getDJIError(ccode));
                }
            });
        }
    }

    @Setter("SecondaryVideoOSDLeftMargin")
    public void setOSDLeftMargin(int margin, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (margin >= 0 && margin <= 50) {
            setter(DataDm368SetGParams.CmdId.SetOsdLeft, margin, callback);
        } else if (callback != null) {
            callback.onSuccess(DJIError.COMMON_PARAM_ILLEGAL);
        }
    }

    @Getter("SecondaryVideoOSDLeftMargin")
    public void getOSDLeftMargin(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            DataDm368GetGParams.getInstance().setType(true).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.airlink.ocusync.DJIOcuSyncDualBandLinkPM420Abstraction.AnonymousClass6 */

                public void onSuccess(Object model) {
                    callback.onSuccess(Integer.valueOf(DataDm368GetGParams.getInstance().getOsdMarginLeft()));
                }

                public void onFailure(Ccode ccode) {
                    callback.onFails(DJIAirLinkError.getDJIError(ccode));
                }
            });
        }
    }

    @Setter("SecondaryVideoOSDBottomMargin")
    public void setOSDBottomMargin(int margin, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (margin >= 0 && margin <= 50) {
            setter(DataDm368SetGParams.CmdId.SetOsdBottom, margin, callback);
        } else if (callback != null) {
            callback.onFails(DJIError.COMMON_PARAM_ILLEGAL);
        }
    }

    @Getter("SecondaryVideoOSDBottomMargin")
    public void getOSDBottomMargin(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            DataDm368GetGParams.getInstance().setType(true).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.airlink.ocusync.DJIOcuSyncDualBandLinkPM420Abstraction.AnonymousClass7 */

                public void onSuccess(Object model) {
                    callback.onSuccess(Integer.valueOf(DataDm368GetGParams.getInstance().getOsdMarginBottom()));
                }

                public void onFailure(Ccode ccode) {
                    callback.onFails(DJIAirLinkError.getDJIError(ccode));
                }
            });
        }
    }

    @Setter("SecondaryVideoOSDRightMargin")
    public void setOSDRightMargin(int margin, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (margin >= 0 && margin <= 50) {
            setter(DataDm368SetGParams.CmdId.SetOsdRight, margin, callback);
        } else if (callback != null) {
            callback.onFails(DJIError.COMMON_PARAM_ILLEGAL);
        }
    }

    @Getter("SecondaryVideoOSDRightMargin")
    public void getOSDRightMargin(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            DataDm368GetGParams.getInstance().setType(true).setType(true).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.airlink.ocusync.DJIOcuSyncDualBandLinkPM420Abstraction.AnonymousClass8 */

                public void onSuccess(Object model) {
                    callback.onSuccess(Integer.valueOf(DataDm368GetGParams.getInstance().getOsdMarginRight()));
                }

                public void onFailure(Ccode ccode) {
                    callback.onFails(DJIAirLinkError.getDJIError(ccode));
                }
            });
        }
    }

    @Setter("SecondaryVideoOSDUnits")
    public void setOSDUnit(OcuSyncUnit unit, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (unit != null && !unit.equals(OcuSyncUnit.UNKNOWN)) {
            setter(DataDm368SetGParams.CmdId.ShowUnit, unit.value(), callback);
        } else if (callback != null) {
            callback.onFails(DJIError.COMMON_PARAM_ILLEGAL);
        }
    }

    @Getter("SecondaryVideoOSDUnits")
    public void getOSDUnit(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            DataDm368GetGParams.getInstance().setType(true).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.airlink.ocusync.DJIOcuSyncDualBandLinkPM420Abstraction.AnonymousClass9 */

                public void onSuccess(Object model) {
                    callback.onSuccess(DataDm368GetGParams.getInstance().getUnit() ? OcuSyncUnit.METRIC : OcuSyncUnit.IMPERIAL);
                }

                public void onFailure(Ccode ccode) {
                    callback.onFails(DJIAirLinkError.getDJIError(ccode));
                }
            });
        }
    }

    @Setter("HDMIOutputFormat")
    public void setHDMIOutputFormat(OcuSyncSecondaryVideoFormat outputFormat, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (outputFormat != null && !outputFormat.equals(OcuSyncSecondaryVideoFormat.UNKNOWN)) {
            setter(DataDm368SetGParams.CmdId.SetHDMIFormat, outputFormat.value(), callback);
        } else if (callback != null) {
            callback.onFails(DJIError.COMMON_PARAM_ILLEGAL);
        }
    }

    @Getter("HDMIOutputFormat")
    public void getHDMIOutputFormat(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            DataDm368GetGParams.getInstance().setType(true).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.airlink.ocusync.DJIOcuSyncDualBandLinkPM420Abstraction.AnonymousClass10 */

                public void onSuccess(Object model) {
                    callback.onSuccess(OcuSyncSecondaryVideoFormat.find(DataDm368GetGParams.getInstance().getHDMIFormat()));
                }

                public void onFailure(Ccode ccode) {
                    callback.onFails(DJIAirLinkError.getDJIError(ccode));
                }
            });
        }
    }

    @Setter("SDIOutputFormat")
    public void setSDIOutputFormat(OcuSyncSecondaryVideoFormat outputFormat, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (outputFormat != null && !outputFormat.equals(OcuSyncSecondaryVideoFormat.UNKNOWN)) {
            setter(DataDm368SetGParams.CmdId.SetSDIFormat, outputFormat.value(), callback);
        } else if (callback != null) {
            callback.onFails(DJIError.COMMON_PARAM_ILLEGAL);
        }
    }

    @Getter("SDIOutputFormat")
    public void getSDIOutputFormat(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            DataDm368GetGParams.getInstance().setType(true).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.airlink.ocusync.DJIOcuSyncDualBandLinkPM420Abstraction.AnonymousClass11 */

                public void onSuccess(Object model) {
                    callback.onSuccess(OcuSyncSecondaryVideoFormat.find(DataDm368GetGParams.getInstance().getSDIFormat()));
                }

                public void onFailure(Ccode ccode) {
                    callback.onFails(DJIAirLinkError.getDJIError(ccode));
                }
            });
        }
    }

    @Setter("PIPPosition")
    public void setPIPPosition(OcuSyncPIPPosition position, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (position != null && !position.equals(OcuSyncPIPPosition.UNKNOWN)) {
            setter(DataDm368SetGParams.CmdId.SetOutputLoc, position.value(), callback);
        } else if (callback != null) {
            callback.onFails(DJIError.COMMON_PARAM_ILLEGAL);
        }
    }

    @Getter("PIPPosition")
    public void getPIPPosition(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            DataDm368GetGParams.getInstance().start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.airlink.ocusync.DJIOcuSyncDualBandLinkPM420Abstraction.AnonymousClass12 */

                public void onSuccess(Object model) {
                    callback.onSuccess(OcuSyncPIPPosition.find(DataDm368GetGParams.getInstance().getOutputLocation()));
                }

                public void onFailure(Ccode ccode) {
                    callback.onFails(DJIAirLinkError.getDJIError(ccode));
                }
            });
        }
    }

    /* access modifiers changed from: protected */
    public void setter(DataDm368SetGParams.CmdId id, int value, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataDm368SetGParams setter = new DataDm368SetGParams();
        setter.set(id, value);
        setter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.airlink.ocusync.DJIOcuSyncDualBandLinkPM420Abstraction.AnonymousClass13 */

            public void onSuccess(Object model) {
                if (callback != null) {
                    callback.onSuccess(null);
                }
            }

            public void onFailure(Ccode ccode) {
                if (callback != null) {
                    callback.onFails(DJIAirLinkError.getDJIError(ccode));
                }
            }
        });
    }

    @Setter(OcuSyncLinkKeys.VIDEO_STREAM_CAMERAS)
    public void setVideoStreamCameras(SettingsDefinitions.CameraType[] cameras, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataCameraGetPushStateInfo.CameraType mainCam = DataCameraGetPushStateInfo.CameraType.OTHER;
        DataCameraGetPushStateInfo.CameraType secondaryCam = DataCameraGetPushStateInfo.CameraType.OTHER;
        if (cameras[0] != null) {
            mainCam = DataCameraGetPushStateInfo.CameraType.find(cameras[0].value());
        }
        if (cameras[1] != null) {
            secondaryCam = DataCameraGetPushStateInfo.CameraType.find(cameras[1].value());
        }
        if (mainCam != secondaryCam || mainCam == DataCameraGetPushStateInfo.CameraType.OTHER) {
            final DataCameraGetPushStateInfo.CameraType[] camTypes = {mainCam, secondaryCam};
            DataRcGetMaster.getInstance().start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.airlink.ocusync.DJIOcuSyncDualBandLinkPM420Abstraction.AnonymousClass14 */

                public void onSuccess(Object model) {
                    DataRcSetMaster.MODE mode = DataRcGetMaster.getInstance().getMode();
                    if (mode == DataRcSetMaster.MODE.OTHER) {
                        CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
                        return;
                    }
                    DataCameraGetPushStateInfo.CameraType videoMain = camTypes[0];
                    DataCameraGetPushStateInfo.CameraType videoSec = camTypes[1];
                    boolean isMainCamOpen = true;
                    boolean isSecondaryOpen = true;
                    if (videoMain == DataCameraGetPushStateInfo.CameraType.OTHER) {
                        isMainCamOpen = false;
                        if (SdrLteVideoController.getInstance().getMainVideoCamera() != videoSec) {
                            videoMain = SdrLteVideoController.getInstance().getMainVideoCamera();
                        } else if (SdrLteVideoController.getInstance().getSecondaryVideoCamera() != videoSec) {
                            videoMain = SdrLteVideoController.getInstance().getSecondaryVideoCamera();
                        }
                    }
                    if (videoSec == DataCameraGetPushStateInfo.CameraType.OTHER) {
                        isSecondaryOpen = false;
                        if (SdrLteVideoController.getInstance().getMainVideoCamera() != videoMain) {
                            videoSec = SdrLteVideoController.getInstance().getMainVideoCamera();
                        } else if (SdrLteVideoController.getInstance().getSecondaryVideoCamera() != videoMain) {
                            videoSec = SdrLteVideoController.getInstance().getSecondaryVideoCamera();
                        }
                    }
                    DataDm368CameraVideoStreams.getInstance().setRcMode(mode).isGet(false).setMainCamera(videoMain).isMainCameraOpen(isMainCamOpen).setSecondaryCamera(videoSec).isSecondaryCameraOpen(isSecondaryOpen).start(new DJIDataCallBack() {
                        /* class dji.sdksharedlib.hardware.abstractions.airlink.ocusync.DJIOcuSyncDualBandLinkPM420Abstraction.AnonymousClass14.AnonymousClass1 */

                        /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
                         method: dji.sdksharedlib.hardware.abstractions.airlink.ocusync.DJIOcuSyncDualBandLinkPM420Abstraction.access$200(dji.sdksharedlib.hardware.abstractions.airlink.ocusync.DJIOcuSyncDualBandLinkPM420Abstraction, java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void
                         arg types: [dji.sdksharedlib.hardware.abstractions.airlink.ocusync.DJIOcuSyncDualBandLinkPM420Abstraction, dji.common.camera.SettingsDefinitions$CameraType[], dji.sdksharedlib.keycatalog.DJISDKCacheKey]
                         candidates:
                          dji.sdksharedlib.hardware.abstractions.airlink.ocusync.DJIOcuSyncLinkAbstraction.access$200(dji.sdksharedlib.hardware.abstractions.airlink.ocusync.DJIOcuSyncLinkAbstraction, java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void
                          dji.sdksharedlib.hardware.abstractions.airlink.ocusync.DJIOcuSyncDualBandLinkPM420Abstraction.access$200(dji.sdksharedlib.hardware.abstractions.airlink.ocusync.DJIOcuSyncDualBandLinkPM420Abstraction, java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void */
                        /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
                         method: dji.sdksharedlib.hardware.abstractions.airlink.ocusync.DJIOcuSyncDualBandLinkPM420Abstraction.access$300(dji.sdksharedlib.hardware.abstractions.airlink.ocusync.DJIOcuSyncDualBandLinkPM420Abstraction, java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void
                         arg types: [dji.sdksharedlib.hardware.abstractions.airlink.ocusync.DJIOcuSyncDualBandLinkPM420Abstraction, dji.common.camera.SettingsDefinitions$CameraType, dji.sdksharedlib.keycatalog.DJISDKCacheKey]
                         candidates:
                          dji.sdksharedlib.hardware.abstractions.airlink.ocusync.DJIOcuSyncDualBandLinkAbstraction.access$300(dji.sdksharedlib.hardware.abstractions.airlink.ocusync.DJIOcuSyncDualBandLinkAbstraction, java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void
                          dji.sdksharedlib.hardware.abstractions.airlink.ocusync.DJIOcuSyncDualBandLinkPM420Abstraction.access$300(dji.sdksharedlib.hardware.abstractions.airlink.ocusync.DJIOcuSyncDualBandLinkPM420Abstraction, java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void */
                        public void onSuccess(Object model) {
                            SdrLteVideoController.getInstance().updateCameraStreams(DataDm368CameraVideoStreams.getInstance());
                            SettingsDefinitions.CameraType mainVideoCamera = DJIOcuSyncDualBandLinkPM420Abstraction.this.getSettingsCameraType(SdrLteVideoController.getInstance().getMainVideoCamera());
                            SettingsDefinitions.CameraType secondaryCamera = DJIOcuSyncDualBandLinkPM420Abstraction.this.getSettingsCameraType(SdrLteVideoController.getInstance().getSecondaryVideoCamera());
                            DJIOcuSyncDualBandLinkPM420Abstraction.this.notifyValueChangeForKeyPath((Object) new SettingsDefinitions.CameraType[]{mainVideoCamera, secondaryCamera}, DJIOcuSyncDualBandLinkPM420Abstraction.this.convertKeyToPath(OcuSyncLinkKeys.VIDEO_STREAM_CAMERAS));
                            DJIOcuSyncDualBandLinkPM420Abstraction.this.notifyValueChangeForKeyPath((Object) DJIOcuSyncDualBandLinkPM420Abstraction.this.getSettingsCameraType(SdrLteVideoController.getInstance().getLteVideoCamera()), KeyHelper.getLteLinkKey(LteLinkKeys.MODULE_LTE_VIDEO_CAMERA));
                            CallbackUtils.onSuccess(callback, (Object) null);
                        }

                        public void onFailure(Ccode ccode) {
                            CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
                        }
                    });
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
                }
            });
            return;
        }
        CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
    }

    @Getter(OcuSyncLinkKeys.VIDEO_STREAM_CAMERAS)
    public void getVideoCameras(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataRcGetMaster.getInstance().start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.airlink.ocusync.DJIOcuSyncDualBandLinkPM420Abstraction.AnonymousClass15 */

            public void onSuccess(Object model) {
                DataRcSetMaster.MODE mode = DataRcGetMaster.getInstance().getMode();
                if (mode == DataRcSetMaster.MODE.OTHER) {
                    CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
                    return;
                }
                DataCameraGetPushStateInfo.CameraType cam1Type = SdrLteVideoController.getInstance().getMainVideoCamera();
                DataDm368CameraVideoStreams.getInstance().setRcMode(mode).isGet(true).setMainCamera(cam1Type).setSecondaryCamera(SdrLteVideoController.getInstance().getSecondaryVideoCamera()).start(new DJIDataCallBack() {
                    /* class dji.sdksharedlib.hardware.abstractions.airlink.ocusync.DJIOcuSyncDualBandLinkPM420Abstraction.AnonymousClass15.AnonymousClass1 */

                    public void onSuccess(Object model) {
                        SdrLteVideoController.getInstance().updateCameraStreams(DataDm368CameraVideoStreams.getInstance());
                        SettingsDefinitions.CameraType mainVideoCamera = DJIOcuSyncDualBandLinkPM420Abstraction.this.getSettingsCameraType(SdrLteVideoController.getInstance().getMainVideoCamera());
                        SettingsDefinitions.CameraType secondaryVideoCamera = DJIOcuSyncDualBandLinkPM420Abstraction.this.getSettingsCameraType(SdrLteVideoController.getInstance().getSecondaryVideoCamera());
                        DJIOcuSyncDualBandLinkPM420Abstraction.this.notifyValueChangeForKeyPath(DJIOcuSyncDualBandLinkPM420Abstraction.this.getSettingsCameraType(SdrLteVideoController.getInstance().getLteVideoCamera()), KeyHelper.getLteLinkKey(LteLinkKeys.MODULE_LTE_VIDEO_CAMERA));
                        CallbackUtils.onSuccess(callback, new SettingsDefinitions.CameraType[]{mainVideoCamera, secondaryVideoCamera});
                    }

                    public void onFailure(Ccode ccode) {
                        CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
                    }
                });
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
            }
        });
    }

    /* access modifiers changed from: private */
    public SettingsDefinitions.CameraType getSettingsCameraType(DataCameraGetPushStateInfo.CameraType cameraType) {
        return cameraType == null ? SettingsDefinitions.CameraType.OTHER : SettingsDefinitions.CameraType.find(cameraType.value());
    }

    @Setter("BandwidthAllocationForMainCamera")
    public void setBandwidthAllocationForLeftCamera(float percent, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (percent >= 0.0f && percent <= 1.0f) {
            final int tenths = Math.round(10.0f * percent);
            setCamerasBandWidth(tenths, new DJISDKCacheHWAbstraction.InnerCallback() {
                /* class dji.sdksharedlib.hardware.abstractions.airlink.ocusync.DJIOcuSyncDualBandLinkPM420Abstraction.AnonymousClass16 */

                public void onSuccess(Object o) {
                    if (callback != null) {
                        callback.onSuccess(o);
                    }
                    if (MidWare.context.get() != null) {
                        DjiSharedPreferencesManager.putInt(MidWare.context.get(), DoubleCameraSupportUtil.USER_SET_MAIN_CAMERA_BANDWIDTH_PERCENT, tenths);
                    }
                }

                public void onFails(DJIError error) {
                    if (callback != null) {
                        callback.onFails(error);
                    }
                }
            });
        } else if (callback != null) {
            callback.onFails(DJIAirLinkError.COMMON_PARAM_ILLEGAL);
        }
    }

    private void setCamerasBandWidth(int tenths, DJISDKCacheHWAbstraction.InnerCallback callback) {
        ((DataSingleSetMainCameraBandwidthPercent) new DataSingleSetMainCameraBandwidthPercent().setReceiverType(DeviceType.DM368.value()).setReceiverId(6, DataSingleSetMainCameraBandwidthPercent.class)).setPercent(tenths).start(CallbackUtils.defaultCB(callback));
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataDm368PushLiveVideoStatus status) {
        SdrLteVideoController.getInstance().updateCameraStreams(status);
        SettingsDefinitions.CameraType mainVideoCamera = getSettingsCameraType(SdrLteVideoController.getInstance().getMainVideoCamera());
        SettingsDefinitions.CameraType secondaryVideoCamera = getSettingsCameraType(SdrLteVideoController.getInstance().getSecondaryVideoCamera());
        SettingsDefinitions.CameraType camera4G = getSettingsCameraType(SdrLteVideoController.getInstance().getLteVideoCamera());
        notifyValueChangeForKeyPath(new SettingsDefinitions.CameraType[]{mainVideoCamera, secondaryVideoCamera}, convertKeyToPath(OcuSyncLinkKeys.VIDEO_STREAM_CAMERAS));
        notifyValueChangeForKeyPath(camera4G, KeyHelper.getLteLinkKey(LteLinkKeys.MODULE_LTE_VIDEO_CAMERA));
    }
}
