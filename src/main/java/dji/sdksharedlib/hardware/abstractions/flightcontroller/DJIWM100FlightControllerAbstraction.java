package dji.sdksharedlib.hardware.abstractions.flightcontroller;

import dji.common.error.DJIError;
import dji.common.error.DJIFlightControllerError;
import dji.common.util.CallbackUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.logic.manager.DJIUSBWifiSwitchManager;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.manager.P3.DJIFlycParamInfoManager;
import dji.midware.data.model.P3.DataFlycGetParams;
import dji.midware.data.model.P3.DataFlycSetParams;
import dji.midware.data.params.P3.ParamCfgName;
import dji.midware.data.params.P3.ParamInfo;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistantAbstraction;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistantSparkAbstraction;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.merge.ParamInfoCallBack;
import dji.sdksharedlib.keycatalog.FlightControllerKeys;

@EXClassNullAway
public class DJIWM100FlightControllerAbstraction extends FlightControllerFoldingDroneAbstraction {
    private static final int AIRPORT_WARNING_MAX_HEIGHT = 120;
    private static final int RC_MODE_MAX_FLIGHT_HEIGHT = 500;
    private static final int RC_MODE_MAX_FLIGHT_RADIUS = 8000;
    private static final int RC_MODE_MIN_FLIGHT_HEIGHT = 20;
    private static final int WM100_MAX_WIFI_MODE_MAX_FLIGHT_HEIGHT = 50;
    private static final int WM100_MAX_WIFI_MODE_MAX_FLIGHT_RADIUS = 100;

    /* access modifiers changed from: protected */
    public IntelligentFlightAssistantAbstraction newIntelligentFlightAssistantIfSupport() {
        return new IntelligentFlightAssistantSparkAbstraction();
    }

    /* access modifiers changed from: protected */
    public void initFlightControllerSupportParameter() {
        super.initFlightControllerSupportParameter();
        this.imuCount = 1;
        this.compassCount = 1;
        notifyValueChangeForKeyPath(Integer.valueOf(this.imuCount), convertKeyToPath(FlightControllerKeys.IMU_COUNT));
        notifyValueChangeForKeyPath(Integer.valueOf(this.compassCount), convertKeyToPath(FlightControllerKeys.COMPASS_COUNT));
    }

    @Setter(FlightControllerKeys.MAX_FLIGHT_HEIGHT)
    public void setMaxFlightHeight(int maxHeight, DJISDKCacheHWAbstraction.InnerCallback callback) {
        boolean isNeedMaxFlightHeight = ((Boolean) CacheHelper.getFlightController(FlightControllerKeys.NEED_LIMIT_FLIGHT_HEIGHT)).booleanValue();
        if (maxHeight <= 120 || !isNeedMaxFlightHeight) {
            if (DJIUSBWifiSwitchManager.getInstance().isRcWifiConnected(null)) {
                if (maxHeight < 20 || maxHeight > 500) {
                    CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
                    return;
                }
            } else if (maxHeight < 20 || maxHeight > 50) {
                CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
                return;
            }
            DataFlycSetParams setParams = new DataFlycSetParams();
            setParams.setIndexs(ParamCfgName.GCONFIG_LIMIT_HEIGHT);
            setParams.setValues(Integer.valueOf(maxHeight));
            setParams.start(CallbackUtils.defaultCB(callback));
            return;
        }
        CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
    }

    @Getter(FlightControllerKeys.MAX_FLIGHT_HEIGHT)
    public void getMaxFlightHeight(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        new DataFlycGetParams().setInfos(new String[]{ParamCfgName.GCONFIG_LIMIT_HEIGHT}).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.DJIWM100FlightControllerAbstraction.AnonymousClass1 */

            public void onSuccess(Object model) {
                int maxFlightHeight = DJIFlycParamInfoManager.read(ParamCfgName.GCONFIG_LIMIT_HEIGHT).value.intValue();
                if (!DJIUSBWifiSwitchManager.getInstance().isRcWifiConnected(null) && maxFlightHeight > 50) {
                    maxFlightHeight = 50;
                }
                CallbackUtils.onSuccess(callback, Integer.valueOf(maxFlightHeight));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIFlightControllerError.getDJIError(ccode));
            }
        });
    }

    @Setter(FlightControllerKeys.MAX_FLIGHT_RADIUS)
    public void setMaxFlightRadius(int maxRadius, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (maxRadius < 15 || maxRadius > 8000) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
            return;
        }
        if (DJIUSBWifiSwitchManager.getInstance().isRcWifiConnected(null) || DJIUSBWifiSwitchManager.getInstance().isProductAoaConnected(null)) {
            if (maxRadius > 8000) {
                CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
                return;
            }
        } else if (maxRadius > 100) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
            return;
        }
        DataFlycSetParams setParams = new DataFlycSetParams();
        setParams.setIndexs(ParamCfgName.GCONFIG_FLY_LIMIT);
        setParams.setValues(Integer.valueOf(maxRadius));
        setParams.start(CallbackUtils.defaultCB(callback));
    }

    @Getter(FlightControllerKeys.MAX_FLIGHT_RADIUS)
    public void getMaxFlightRadius(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        new DataFlycGetParams().setInfos(new String[]{ParamCfgName.GCONFIG_FLY_LIMIT}).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.DJIWM100FlightControllerAbstraction.AnonymousClass2 */

            public void onSuccess(Object model) {
                int maxFlightRadius = DJIFlycParamInfoManager.read(ParamCfgName.GCONFIG_FLY_LIMIT).value.intValue();
                if (!DJIUSBWifiSwitchManager.getInstance().isRcWifiConnected(null) && maxFlightRadius > 100) {
                    maxFlightRadius = 100;
                }
                CallbackUtils.onSuccess(callback, Integer.valueOf(maxFlightRadius));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIFlightControllerError.getDJIError(ccode));
            }
        });
    }

    @Setter(FlightControllerKeys.GO_HOME_HEIGHT_IN_METERS)
    public void setGoHomeAltitude(int altitude, DJISDKCacheHWAbstraction.InnerCallback callback) {
        boolean isNeedMaxFlightHeight = ((Boolean) CacheHelper.getFlightController(FlightControllerKeys.NEED_LIMIT_FLIGHT_HEIGHT)).booleanValue();
        DJILog.d("HeightLimit", "isNeedMaxFlightHeight" + isNeedMaxFlightHeight, new Object[0]);
        if (altitude > 120 && isNeedMaxFlightHeight) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
        } else if (altitude < 20) {
            CallbackUtils.onFailure(callback, DJIFlightControllerError.GO_HOME_ALTITUDE_TOO_LOW);
        } else {
            if (DJIUSBWifiSwitchManager.getInstance().isRcWifiConnected(null)) {
                if (altitude > 500) {
                    CallbackUtils.onFailure(callback, DJIFlightControllerError.GO_HOME_ALTITUDE_TOO_HIGH);
                    return;
                }
            } else if (altitude > 50) {
                CallbackUtils.onFailure(callback, DJIFlightControllerError.GO_HOME_ALTITUDE_TOO_HIGH);
                return;
            }
            final String[] indexs = {ParamCfgName.GCONFIG_GH_ALTITUDE};
            final ParamInfo heightInfo = DJIFlycParamInfoManager.read(ParamCfgName.GCONFIG_GH_ALTITUDE);
            final int i = altitude;
            final DJISDKCacheHWAbstraction.InnerCallback innerCallback = callback;
            getMaxFlightHeight(new DJISDKCacheHWAbstraction.InnerCallback() {
                /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.DJIWM100FlightControllerAbstraction.AnonymousClass3 */

                public void onSuccess(Object o) {
                    if (o instanceof Integer) {
                        if (i > ((Integer) o).intValue()) {
                            CallbackUtils.onFailure(innerCallback, DJIFlightControllerError.GO_HOME_ALTITUDE_HIGHER_THAN_MAX_FLIGHT_HEIGHT);
                        } else {
                            DataFlycGetParams.getInstance().setInfos(indexs).start(new DJIDataCallBack() {
                                /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.DJIWM100FlightControllerAbstraction.AnonymousClass3.AnonymousClass1 */

                                public void onSuccess(Object model) {
                                    if (heightInfo.isCorrect(Integer.valueOf(i))) {
                                        new DataFlycSetParams().setInfo(heightInfo.name, Integer.valueOf(i)).start(CallbackUtils.defaultCB(innerCallback, DJIFlightControllerError.class));
                                    } else {
                                        CallbackUtils.onFailure(innerCallback, DJIFlightControllerError.INVALID_PARAMETER);
                                    }
                                }

                                public void onFailure(Ccode ccode) {
                                    CallbackUtils.onFailure(innerCallback, DJIFlightControllerError.getDJIError(ccode));
                                }
                            });
                        }
                    }
                }

                public void onFails(DJIError error) {
                    CallbackUtils.onFailure(innerCallback, error);
                }
            });
        }
    }

    @Getter(FlightControllerKeys.GO_HOME_HEIGHT_IN_METERS)
    public void getGoHomeAltitude(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        this.mergeGetFlycParamInfo.getInfo(ParamCfgName.GCONFIG_GH_ALTITUDE, new ParamInfoCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.DJIWM100FlightControllerAbstraction.AnonymousClass4 */

            public void onSuccess(ParamInfo info) {
                int maxGoHomeHeight = info.value.intValue();
                if (!DJIUSBWifiSwitchManager.getInstance().isRcWifiConnected(null) && maxGoHomeHeight > 50) {
                    maxGoHomeHeight = 50;
                }
                CallbackUtils.onSuccess(callback, Integer.valueOf(maxGoHomeHeight));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIFlightControllerError.getDJIError(ccode));
            }
        });
    }
}
