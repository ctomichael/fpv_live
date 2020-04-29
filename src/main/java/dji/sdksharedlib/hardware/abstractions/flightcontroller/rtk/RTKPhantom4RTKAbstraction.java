package dji.sdksharedlib.hardware.abstractions.flightcontroller.rtk;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import dji.common.error.DJIError;
import dji.common.error.DJIFlightControllerError;
import dji.common.flightcontroller.LocationCoordinate3D;
import dji.common.flightcontroller.PositioningSolution;
import dji.common.flightcontroller.RTKState;
import dji.common.flightcontroller.rtk.BaseStationBatteryState;
import dji.common.flightcontroller.rtk.DataSource;
import dji.common.flightcontroller.rtk.ReferenceStationSource;
import dji.common.model.LocationCoordinate2D;
import dji.common.util.CallbackUtils;
import dji.common.util.LocationUtils;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.manager.P3.DJIFlycParamInfoManager;
import dji.midware.data.model.P3.DataCenterRTKPushStatus;
import dji.midware.data.model.P3.DataFlycGetParams;
import dji.midware.data.model.P3.DataFlycGetPushRTKLocationData;
import dji.midware.data.model.P3.DataFlycSetParams;
import dji.midware.data.model.P3.DataRTKGetMultiParam;
import dji.midware.data.model.P3.DataRTKGetReferenceStationSource;
import dji.midware.data.model.P3.DataRTKSetMultiParam;
import dji.midware.data.model.P3.DataRTKSetType;
import dji.midware.data.model.P3.DataSmartBatteryGetPushDynamicData;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.keycatalog.RTKKeys;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class RTKPhantom4RTKAbstraction extends RTKAbstraction {
    private static final String DIVIDER = ",";
    private final int POSITIONING_SOLUTION_FIXED_MIN = 49;
    private final int POSITIONING_SOLUTION_FLOAT_MIN = 17;
    private final int POSITIONING_SOLUTION_NONE_MIN = 0;
    private final int POSITIONING_SOLUTION_SINGLE_POINT = 16;
    private final int POSITIONING_SOLUTION_UNKNOWN_MIN = 51;

    public void syncPushDataFromMidware() {
        super.syncPushDataFromMidware();
        onEvent3BackgroundThread(DataSmartBatteryGetPushDynamicData.getInstance());
        onEvent3BackgroundThread(DataRTKGetReferenceStationSource.getInstance());
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataFlycGetPushRTKLocationData event) {
        if (event.isGetted()) {
            super.onEvent3BackgroundThread(event);
            notifyValueChangeForKeyPath(new LocationCoordinate3D(event.getLatitude(), event.getLongitude(), event.getHeight()), convertKeyToPath(RTKKeys.RTK_FUSION_MOBILE_LOCATION));
            notifyValueChangeForKeyPath(Float.valueOf(((float) event.getHeading()) * 0.1f), convertKeyToPath(RTKKeys.RTK_FUSION_MOBILE_HEADING));
            notifyValueChangeForKeyPath(Boolean.valueOf(event.isRTKHealthy()), convertKeyToPath(RTKKeys.IS_RTK_FUSION_DATA_USABLE));
            notifyValueChangeForKeyPath(Integer.valueOf(event.getUTCSeconds()), convertKeyToPath(RTKKeys.RTK_FUSION_SEC));
            notifyValueChangeForKeyPath(Integer.valueOf(event.getUTCNanoSeconds()), convertKeyToPath(RTKKeys.RTK_FUSION_NANO_SEC));
            notifyValueChangeForKeyPath(Integer.valueOf(event.getDistanceToHomeDataSource()), convertKeyToPath(RTKKeys.RTK_FUSION_AIRCRAFT_TO_HOME_DATA_SOURCE));
            notifyValueChangeForKeyPath(Boolean.valueOf(event.hasSetRTKTakeOffHeight()), convertKeyToPath(RTKKeys.RTK_FUSION_HAS_SET_TAKE_OFF_ALTITUDE));
            notifyValueChangeForKeyPath(Integer.valueOf(event.getHomePointDataSource()), convertKeyToPath(RTKKeys.RTK_FUSION_HOME_LOCATION_DATA_SOURCE));
            notifyValueChangeForKeyPath(new LocationCoordinate2D(event.getHomePointLatitude(), event.getHomePointLongitude()), convertKeyToPath(RTKKeys.RTK_FUSION_HOME_LOCATION));
            notifyValueChangeForKeyPath(Float.valueOf(event.getTakeOffAltitude()), convertKeyToPath(RTKKeys.RTK_FUSION_TAKE_OFF_ALTITUDE));
            notifyValueChangeForKeyPath(Float.valueOf(event.getAircraftDistanceToHome()), convertKeyToPath(RTKKeys.RTK_FUSION_AIRCRAFT_TO_HOME_DISTANCE));
            notifyValueChangeForKeyPath(Boolean.valueOf(event.isRTKConnected()), convertKeyToPath(RTKKeys.IS_RTK_CONNECTED));
            notifyValueChangeForKeyPath(Integer.valueOf(event.getGPSCount()), convertKeyToPath(RTKKeys.RTK_FUSION_GPS_COUNT));
        }
    }

    /* access modifiers changed from: protected */
    public RTKState.Builder buildRTKPushInfo() {
        DataFlycGetPushRTKLocationData pushInfo = DataFlycGetPushRTKLocationData.getInstance();
        return super.buildRTKPushInfo().msFusionLocation(new LocationCoordinate2D(pushInfo.getLatitude(), pushInfo.getLongitude())).isRTKBeingUsed(true).msFusionAltitude(pushInfo.getHeight()).msFusionHeading(((float) pushInfo.getHeading()) * 0.1f).distanceToHomeDataSource(DataSource.find(pushInfo.getDistanceToHomeDataSource())).hasSetTakeOffAltitude(pushInfo.hasSetRTKTakeOffHeight()).homePointDataSource(DataSource.find(pushInfo.getHomePointDataSource())).homePointLocation(new LocationCoordinate2D(pushInfo.getHomePointLatitude(), pushInfo.getHomePointLongitude())).takeOffAltitude(pushInfo.getTakeOffAltitude()).distanceToHome(pushInfo.getAircraftDistanceToHome()).gpsCount(pushInfo.getGPSCount());
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataSmartBatteryGetPushDynamicData batteryGetPushDynamicData) {
        if (batteryGetPushDynamicData.isGetted() && batteryGetPushDynamicData.isSentByBaseStation()) {
            notifyValueChangeForKeyPath(new BaseStationBatteryState.Builder().voltage(batteryGetPushDynamicData.getVoltage()).capacityPercent(batteryGetPushDynamicData.getRelativeCapacityPercentage()).current(batteryGetPushDynamicData.getCurrent()).temperature((int) (((double) batteryGetPushDynamicData.getTemperature()) / 10.0d)).build(), RTKKeys.RTK_BASE_STATION_BATTERY_STATE);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataRTKGetReferenceStationSource source) {
        if (source.isGetted()) {
            notifyValueChangeForKeyPath(ReferenceStationSource.find(source.getSource()), RTKKeys.RTK_REFERENCE_STATION_SOURCE);
        }
    }

    public void setRTKEnabled(boolean enabled, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        int i = 1;
        DataFlycSetParams setParams = new DataFlycSetParams();
        setParams.setIndexs("g_config.miss_rtk.use_rtk_data_0");
        Number[] numberArr = new Number[1];
        if (!enabled) {
            i = 0;
        }
        numberArr[0] = Integer.valueOf(i);
        setParams.setValues(numberArr);
        setParams.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.rtk.RTKPhantom4RTKAbstraction.AnonymousClass1 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIFlightControllerError.getDJIError(ccode));
            }
        });
    }

    @Getter(RTKKeys.RTK_ENABLED)
    public void getRTKEnabled(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        new DataFlycGetParams().setInfos(new String[]{"g_config.miss_rtk.use_rtk_data_0"}).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.rtk.RTKPhantom4RTKAbstraction.AnonymousClass2 */

            public void onSuccess(Object model) {
                boolean checked = true;
                if (DJIFlycParamInfoManager.read("g_config.miss_rtk.use_rtk_data_0").value.intValue() != 1) {
                    checked = false;
                }
                CallbackUtils.onSuccess(callback, Boolean.valueOf(checked));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIFlightControllerError.getDJIError(ccode));
            }
        });
    }

    @Setter(RTKKeys.RTK_REFERENCE_STATION_SOURCE)
    public void setRTKReferenceStationSource(@NonNull ReferenceStationSource referenceStationSource, @Nullable final DJISDKCacheHWAbstraction.InnerCallback callback) {
        new DataRTKSetType().setData(referenceStationSource.value).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.rtk.RTKPhantom4RTKAbstraction.AnonymousClass3 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIFlightControllerError.getDJIError(ccode));
            }
        });
    }

    @Getter(RTKKeys.RTK_WIFI_CHANNEL)
    public void getRTKWiFiChannel(@NonNull final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataRTKGetMultiParam getter = new DataRTKGetMultiParam();
        getter.setParamId(DataRTKGetMultiParam.RtkGetParam.WIFI_CHANNEL).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.rtk.RTKPhantom4RTKAbstraction.AnonymousClass4 */

            public void onSuccess(Object model) {
                if (getter.getResult()) {
                    CallbackUtils.onSuccess(callback, Integer.valueOf(getter.getWifiChannel()));
                } else {
                    CallbackUtils.onFailure(callback, DJIFlightControllerError.COMMON_EXECUTION_FAILED);
                }
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIFlightControllerError.getDJIError(ccode));
            }
        });
    }

    @Setter(RTKKeys.RTK_DEVICE_NAME)
    public void setRTKDeviceName(@NonNull String name, @Nullable final DJISDKCacheHWAbstraction.InnerCallback callback) {
        new DataRTKSetMultiParam().setDeviceName(name).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.rtk.RTKPhantom4RTKAbstraction.AnonymousClass5 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(null);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIFlightControllerError.getDJIError(ccode));
            }
        });
    }

    @Getter(RTKKeys.RTK_DEVICE_NAME)
    public void getRTKDeviceName(@NonNull final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataRTKGetMultiParam getter = new DataRTKGetMultiParam();
        getter.setParamId(DataRTKGetMultiParam.RtkGetParam.DEVICE_NAME).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.rtk.RTKPhantom4RTKAbstraction.AnonymousClass6 */

            public void onSuccess(Object model) {
                if (getter.getResult()) {
                    CallbackUtils.onSuccess(callback, getter.getDeviceName());
                } else {
                    CallbackUtils.onFailure(callback, DJIFlightControllerError.COMMON_EXECUTION_FAILED);
                }
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIFlightControllerError.getDJIError(ccode));
            }
        });
    }

    @Setter(RTKKeys.RTK_DEVICE_PASSWORD)
    public void setRTKDevicePassword(@NonNull String password, @Nullable final DJISDKCacheHWAbstraction.InnerCallback callback) {
        new DataRTKSetMultiParam().setDevicePassword(password).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.rtk.RTKPhantom4RTKAbstraction.AnonymousClass7 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(null);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIFlightControllerError.getDJIError(ccode));
            }
        });
    }

    @Getter(RTKKeys.RTK_DEVICE_PASSWORD)
    public void getRTKDevicePassword(@NonNull final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataRTKGetMultiParam getter = new DataRTKGetMultiParam();
        getter.setParamId(DataRTKGetMultiParam.RtkGetParam.DEVICE_PWD).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.rtk.RTKPhantom4RTKAbstraction.AnonymousClass8 */

            public void onSuccess(Object model) {
                if (getter.getResult()) {
                    CallbackUtils.onSuccess(callback, getter.getDevicePassword());
                } else {
                    CallbackUtils.onFailure(callback, DJIFlightControllerError.COMMON_EXECUTION_FAILED);
                }
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIFlightControllerError.getDJIError(ccode));
            }
        });
    }

    @Setter(RTKKeys.RTK_WIFI_PASSWORD)
    public void setRTKWiFiPassword(@NonNull String password, @Nullable final DJISDKCacheHWAbstraction.InnerCallback callback) {
        new DataRTKSetMultiParam().setWifiPassword(password).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.rtk.RTKPhantom4RTKAbstraction.AnonymousClass9 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(null);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIFlightControllerError.getDJIError(ccode));
            }
        });
    }

    @Getter(RTKKeys.RTK_WIFI_PASSWORD)
    public void getRTKWiFiPassword(@NonNull final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataRTKGetMultiParam getter = new DataRTKGetMultiParam();
        getter.setParamId(DataRTKGetMultiParam.RtkGetParam.WIFI_PWD).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.rtk.RTKPhantom4RTKAbstraction.AnonymousClass10 */

            public void onSuccess(Object model) {
                if (getter.getResult()) {
                    CallbackUtils.onSuccess(callback, getter.getWifiPassword());
                } else {
                    CallbackUtils.onFailure(callback, DJIFlightControllerError.COMMON_EXECUTION_FAILED);
                }
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIFlightControllerError.getDJIError(ccode));
            }
        });
    }

    @Setter(RTKKeys.RTK_BASE_STATION_REFERENCING_POSITION)
    public void setRTKBaseStationCoordinate(@NonNull LocationCoordinate3D coordinate, @Nullable final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataCenterRTKPushStatus status = DataCenterRTKPushStatus.getInstance();
        if (!LocationUtils.checkValidGPSCoordinate(status.getGroundLat(), status.getGroundLng()) || LocationUtils.gps2m(coordinate.getLatitude(), coordinate.getLongitude(), status.getGroundLat(), status.getGroundLng()) > 50.0d) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
            return;
        }
        final DataRTKSetMultiParam setter = new DataRTKSetMultiParam();
        setter.setBaseStationLocation(truncate(coordinate.getLongitude()) + DIVIDER + truncate(coordinate.getLatitude()) + DIVIDER + truncate((double) coordinate.getAltitude())).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.rtk.RTKPhantom4RTKAbstraction.AnonymousClass11 */

            public void onSuccess(Object model) {
                if (setter.getResult() == 0) {
                    CallbackUtils.onSuccess(null);
                } else {
                    CallbackUtils.onFailure(callback, DJIFlightControllerError.COMMON_EXECUTION_FAILED);
                }
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIFlightControllerError.getDJIError(ccode));
            }
        });
    }

    public static String truncate(double value) {
        DecimalFormat formater = new DecimalFormat();
        formater.setMaximumFractionDigits(9);
        formater.setGroupingSize(0);
        formater.setRoundingMode(RoundingMode.FLOOR);
        return formater.format(value);
    }

    @Getter(RTKKeys.RTK_BASE_STATION_REFERENCING_POSITION)
    public void getRTKBaseStationCoordinate(@NonNull final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataRTKGetMultiParam getter = new DataRTKGetMultiParam();
        getter.setParamId(DataRTKGetMultiParam.RtkGetParam.BS_GPS_LOCATION).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.rtk.RTKPhantom4RTKAbstraction.AnonymousClass12 */

            public void onSuccess(Object model) {
                if (getter.getResult()) {
                    String[] coordinate = getter.getBaseStationLocation().split(RTKPhantom4RTKAbstraction.DIVIDER);
                    CallbackUtils.onSuccess(callback, new LocationCoordinate3D(Double.parseDouble(coordinate[1]), Double.parseDouble(coordinate[0]), Float.parseFloat(coordinate[2])));
                    return;
                }
                CallbackUtils.onFailure(callback, DJIFlightControllerError.COMMON_EXECUTION_FAILED);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIFlightControllerError.getDJIError(ccode));
            }
        });
    }

    @Setter(RTKKeys.RTK_CORS_LOGIN_ACCOUNT)
    public void setRTKCorsLoginAccount(@NonNull String account, @Nullable final DJISDKCacheHWAbstraction.InnerCallback callback) {
        new DataRTKSetMultiParam().setCorsLoginAccount(account).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.rtk.RTKPhantom4RTKAbstraction.AnonymousClass13 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(null);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIFlightControllerError.getDJIError(ccode));
            }
        });
    }

    @Getter(RTKKeys.RTK_CORS_LOGIN_ACCOUNT)
    public void getRTKCorsLoginAccount(@NonNull final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataRTKGetMultiParam getter = new DataRTKGetMultiParam();
        getter.setParamId(DataRTKGetMultiParam.RtkGetParam.CORS_LOGIN_ACCOUNT).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.rtk.RTKPhantom4RTKAbstraction.AnonymousClass14 */

            public void onSuccess(Object model) {
                if (getter.getResult()) {
                    CallbackUtils.onSuccess(callback, getter.getCorsLoginAccount());
                } else {
                    CallbackUtils.onFailure(callback, DJIFlightControllerError.COMMON_EXECUTION_FAILED);
                }
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIFlightControllerError.getDJIError(ccode));
            }
        });
    }

    @Setter(RTKKeys.RTK_CORS_LOGIN_PASSWORD)
    public void setRTKCorsLoginPassword(@NonNull String password, @Nullable final DJISDKCacheHWAbstraction.InnerCallback callback) {
        new DataRTKSetMultiParam().setCorsLoginPassword(password).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.rtk.RTKPhantom4RTKAbstraction.AnonymousClass15 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(null);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIFlightControllerError.getDJIError(ccode));
            }
        });
    }

    @Getter(RTKKeys.RTK_CORS_LOGIN_PASSWORD)
    public void getRTKCorsLoginPassword(@NonNull final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataRTKGetMultiParam getter = new DataRTKGetMultiParam();
        getter.setParamId(DataRTKGetMultiParam.RtkGetParam.CORS_LOGIN_PWD).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.rtk.RTKPhantom4RTKAbstraction.AnonymousClass16 */

            public void onSuccess(Object model) {
                if (getter.getResult()) {
                    CallbackUtils.onSuccess(callback, getter.getCorsLoginPassword());
                } else {
                    CallbackUtils.onFailure(callback, DJIFlightControllerError.COMMON_EXECUTION_FAILED);
                }
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIFlightControllerError.getDJIError(ccode));
            }
        });
    }

    /* access modifiers changed from: protected */
    public PositioningSolution getPositioningSolution(int status) {
        if (status >= 0 && status < 16) {
            return PositioningSolution.NONE;
        }
        if (status == 16) {
            return PositioningSolution.SINGLE_POINT;
        }
        if (17 <= status && status < 49) {
            return PositioningSolution.FLOAT;
        }
        if (49 > status || status >= 51) {
            return PositioningSolution.UNKNOWN;
        }
        return PositioningSolution.FIXED_POINT;
    }
}
