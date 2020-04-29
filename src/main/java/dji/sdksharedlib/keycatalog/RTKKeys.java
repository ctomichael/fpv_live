package dji.sdksharedlib.keycatalog;

import dji.common.error.DJIError;
import dji.common.flightcontroller.LocationCoordinate3D;
import dji.common.flightcontroller.PositioningSolution;
import dji.common.flightcontroller.RTKState;
import dji.common.flightcontroller.rtk.BaseStationBatteryState;
import dji.common.flightcontroller.rtk.NetworkServiceState;
import dji.common.flightcontroller.rtk.RTKConnectType;
import dji.common.flightcontroller.rtk.ReferenceStationSource;
import dji.common.model.LocationCoordinate2D;
import dji.fieldAnnotation.EXClassNullAway;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheUpdateType;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.rtk.RTKAbstraction;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.rtk.RTKPM420RTKAbstraction;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.rtk.RTKPhantom4RTKAbstraction;
import dji.sdksharedlib.keycatalog.extension.Key;

@EXClassNullAway
public class RTKKeys extends DJISDKCacheKeys {
    public static final String COMPONENT_KEY = "RTK";
    @Key(accessType = 4, includedAbstractions = {RTKAbstraction.class, RTKPhantom4RTKAbstraction.class}, type = Boolean.class)
    public static final String IS_RTK_CONNECTED = "IsRTKConnected";
    @Key(accessType = 4, type = Boolean.class)
    public static final String IS_RTK_FUSION_DATA_USABLE = "IsRTKFusionDataUsable";
    @Key(accessType = 4, includedAbstractions = {RTKAbstraction.class}, type = PositioningSolution.class)
    public static final String POSITIONING_SOLUTION = "RTKStatus";
    @Key(accessType = 8, includedAbstractions = {RTKPhantom4RTKAbstraction.class}, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String RTK_ACTIVATE = "RtkActivate";
    @Key(accessType = 4, includedAbstractions = {RTKAbstraction.class}, type = Float.class)
    public static final String RTK_AIR_ALTITUDE = "RTKAirAltitude";
    @Key(accessType = 4, includedAbstractions = {RTKAbstraction.class}, type = Double.class)
    public static final String RTK_AIR_LATITUDE = "RTKAirLatitude";
    @Key(accessType = 4, includedAbstractions = {RTKAbstraction.class}, type = Double.class)
    public static final String RTK_AIR_LONGITUDE = "RTKAirLongitude";
    @Key(accessType = 4, type = BaseStationBatteryState.class)
    public static final String RTK_BASE_STATION_BATTERY_STATE = "RTKBaseStationBatteryState";
    @Key(accessType = 3, includedAbstractions = {RTKPhantom4RTKAbstraction.class}, type = LocationCoordinate3D.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String RTK_BASE_STATION_REFERENCING_POSITION = "RTKBaseStationReferencingPosition";
    @Key(accessType = 1, includedAbstractions = {RTKPhantom4RTKAbstraction.class}, type = Integer.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String RTK_BASE_STATION_SDR_SIGNAL_STRENGTH = "RtkBaseStationSdrSignalStrength";
    @Key(accessType = 4, includedAbstractions = {RTKPM420RTKAbstraction.class}, type = RTKConnectType.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String RTK_CONNECT_TYPE = "RtkConnectType";
    @Key(accessType = 3, includedAbstractions = {RTKPhantom4RTKAbstraction.class}, type = String.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String RTK_CORS_LOGIN_ACCOUNT = "RtkCorsLoginAccount";
    @Key(accessType = 3, includedAbstractions = {RTKPhantom4RTKAbstraction.class}, type = String.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String RTK_CORS_LOGIN_PASSWORD = "RtkCorsLoginPassword";
    @Key(accessType = 3, includedAbstractions = {RTKPhantom4RTKAbstraction.class}, type = String.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String RTK_CORS_SERVER_ADDRESS = "RtkCorsServerAddress";
    @Key(accessType = 3, includedAbstractions = {RTKPhantom4RTKAbstraction.class}, type = String.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String RTK_CORS_SERVER_PORT = "RtkCorsServerPort";
    @Key(accessType = 3, includedAbstractions = {RTKPhantom4RTKAbstraction.class}, type = String.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String RTK_DEVICE_NAME = "RtkDeviceName";
    @Key(accessType = 3, includedAbstractions = {RTKPhantom4RTKAbstraction.class}, type = String.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String RTK_DEVICE_PASSWORD = "RtkDevicePassword";
    @Key(accessType = 4, includedAbstractions = {RTKAbstraction.class}, type = Float.class)
    public static final String RTK_DIRECT_ANGLE = "RTKDirectAngle";
    @Key(accessType = 3, includedAbstractions = {RTKAbstraction.class, RTKPhantom4RTKAbstraction.class}, type = Boolean.class)
    public static final String RTK_ENABLED = "RTKEnabled";
    @Key(accessType = 4, includedAbstractions = {RTKAbstraction.class}, type = DJIError.class)
    public static final String RTK_ERROR = "RTKError";
    @Key(accessType = 4, includedAbstractions = {RTKPhantom4RTKAbstraction.class}, type = Integer.class)
    public static final String RTK_FUSION_AIRCRAFT_TO_HOME_DATA_SOURCE = "RTKFusionAircraftToHomeDataSource";
    @Key(accessType = 4, includedAbstractions = {RTKPhantom4RTKAbstraction.class}, type = Float.class)
    public static final String RTK_FUSION_AIRCRAFT_TO_HOME_DISTANCE = "RTKFusionAircraftToHomeDistance";
    @Key(accessType = 4, includedAbstractions = {RTKPhantom4RTKAbstraction.class}, type = Integer.class)
    public static final String RTK_FUSION_GPS_COUNT = "RTKFusionGPSCount";
    @Key(accessType = 4, includedAbstractions = {RTKPhantom4RTKAbstraction.class}, type = Boolean.class)
    public static final String RTK_FUSION_HAS_SET_TAKE_OFF_ALTITUDE = "RTKFusionHasSetTakeOffAltitude";
    @Key(accessType = 4, includedAbstractions = {RTKPhantom4RTKAbstraction.class}, type = LocationCoordinate2D.class)
    public static final String RTK_FUSION_HOME_LOCATION = "RTKFusionHomeLocation";
    @Key(accessType = 4, includedAbstractions = {RTKPhantom4RTKAbstraction.class}, type = Integer.class)
    public static final String RTK_FUSION_HOME_LOCATION_DATA_SOURCE = "RTKFusionHomeLocationDataSource";
    @Key(accessType = 4, type = Float.class)
    public static final String RTK_FUSION_MOBILE_HEADING = "RTKFusionHeading";
    @Key(accessType = 4, type = LocationCoordinate3D.class)
    public static final String RTK_FUSION_MOBILE_LOCATION = "RTKFusionLocation";
    @Key(accessType = 4, includedAbstractions = {RTKPhantom4RTKAbstraction.class}, type = Integer.class)
    public static final String RTK_FUSION_NANO_SEC = "RTKFusionNanoSeconds";
    @Key(accessType = 4, includedAbstractions = {RTKPhantom4RTKAbstraction.class}, type = Integer.class)
    public static final String RTK_FUSION_SEC = "RTKFusionSeconds";
    @Key(accessType = 4, includedAbstractions = {RTKPhantom4RTKAbstraction.class}, type = Float.class)
    public static final String RTK_FUSION_TAKE_OFF_ALTITUDE = "RTKFusionTakeOffAltitude";
    @Key(accessType = 4, includedAbstractions = {RTKAbstraction.class}, type = Float.class)
    public static final String RTK_GROUND_ALTITUDE = "RTKGroundAltitude";
    @Key(accessType = 4, includedAbstractions = {RTKAbstraction.class}, type = Integer.class)
    public static final String RTK_GROUND_BEIDOU_COUNT = "RTKGroundBeidoutCount";
    @Key(accessType = 4, includedAbstractions = {RTKAbstraction.class}, type = Boolean.class)
    public static final String RTK_GROUND_BEIDOU_COUNT_IS_ON = "RTKGroundBeidoutCountIsOn";
    @Key(accessType = 4, includedAbstractions = {RTKAbstraction.class}, type = Integer.class)
    public static final String RTK_GROUND_GLONASS_COUNT = "RTKGroundGlonassCount";
    @Key(accessType = 4, includedAbstractions = {RTKAbstraction.class}, type = Boolean.class)
    public static final String RTK_GROUND_GLONASS_COUNT_IS_ON = "RTKGroundGlonassCountIsOn";
    @Key(accessType = 4, includedAbstractions = {RTKAbstraction.class}, type = Integer.class)
    public static final String RTK_GROUND_GPS_COUNT = "RTKGroundGPSCount";
    @Key(accessType = 4, includedAbstractions = {RTKAbstraction.class}, type = Boolean.class)
    public static final String RTK_GROUND_GPS_COUNT_IS_ON = "RTKGroundGPSCountIsOn";
    @Key(accessType = 4, includedAbstractions = {RTKAbstraction.class}, type = Double.class)
    public static final String RTK_GROUND_LATITUDE = "RTKGroundLatitude";
    @Key(accessType = 4, includedAbstractions = {RTKAbstraction.class}, type = Double.class)
    public static final String RTK_GROUND_LONGITUDE = "RTKGroundLongitude";
    @Key(accessType = 4, includedAbstractions = {RTKAbstraction.class}, type = Boolean.class)
    public static final String RTK_IS_HEADING_VALID = "RTKDirectEnabled";
    @Key(accessType = 4, includedAbstractions = {RTKAbstraction.class}, type = Integer.class)
    public static final String RTK_MAIN_BEIDOU_COUNT = "RTKMainBeidouCount";
    @Key(accessType = 4, includedAbstractions = {RTKAbstraction.class}, type = Boolean.class)
    public static final String RTK_MAIN_BEIDOU_COUNT_IS_ON = "RTKMainBeidouCountIsOn";
    @Key(accessType = 4, includedAbstractions = {RTKAbstraction.class}, type = Integer.class)
    public static final String RTK_MAIN_GLONASS_COUNT = "RTKMainGlonassCount";
    @Key(accessType = 4, includedAbstractions = {RTKAbstraction.class}, type = Boolean.class)
    public static final String RTK_MAIN_GLONASS_COUNT_IS_ON = "RTKMainGlonassCountIsOn";
    @Key(accessType = 4, includedAbstractions = {RTKAbstraction.class}, type = Integer.class)
    public static final String RTK_MAIN_GPS_COUNT = "RTKMainGPSCount";
    @Key(accessType = 4, includedAbstractions = {RTKAbstraction.class}, type = Boolean.class)
    public static final String RTK_MAIN_GPS_COUNT_IS_ON = "RTKMainGPSCountIsOn";
    @Key(accessType = 4, includedAbstractions = {RTKPhantom4RTKAbstraction.class}, type = NetworkServiceState.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String RTK_NETWORK_SERVICE_STATE = "RtkNetworkServiceState";
    @Key(accessType = 6, includedAbstractions = {RTKPhantom4RTKAbstraction.class}, type = ReferenceStationSource.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String RTK_REFERENCE_STATION_SOURCE = "RtkReferenceStationSource";
    @Key(accessType = 4, includedAbstractions = {RTKAbstraction.class}, type = Boolean.class)
    public static final String RTK_SATELLITE_BEIDOU_COUNT_IS_ON = "RTKSatelliteBeidouCountIsOn";
    @Key(accessType = 4, includedAbstractions = {RTKAbstraction.class}, type = Integer.class)
    public static final String RTK_SATELLITE_GLONASS_COUNT = "RTKSatelliteGlonassCount";
    @Key(accessType = 4, includedAbstractions = {RTKAbstraction.class}, type = Boolean.class)
    public static final String RTK_SATELLITE_GLONASS_COUNT_IS_ON = "RTKSatelliteGlonassCountIsOn";
    @Key(accessType = 4, includedAbstractions = {RTKAbstraction.class}, type = Integer.class)
    public static final String RTK_SATELLITE_GPS_COUNT = "RTKSatelliteGPSCount";
    @Key(accessType = 4, includedAbstractions = {RTKAbstraction.class}, type = Boolean.class)
    public static final String RTK_SATELLITE_GPS_COUNT_IS_ON = "RTKSatelliteGPSCountIsOn";
    @Key(accessType = 4, includedAbstractions = {RTKAbstraction.class, RTKPhantom4RTKAbstraction.class}, type = RTKState.class)
    public static final String RTK_STATE = "RTKState";
    @Key(accessType = 1, includedAbstractions = {RTKPhantom4RTKAbstraction.class}, type = Integer.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String RTK_WIFI_CHANNEL = "RtkWiFiChannel";
    @Key(accessType = 3, includedAbstractions = {RTKPhantom4RTKAbstraction.class}, type = String.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String RTK_WIFI_PASSWORD = "RtkWiFiPassword";

    public RTKKeys(String name) {
        super(name);
    }

    /* access modifiers changed from: protected */
    public String getDefaultAbstractionName() {
        return COMPONENT_KEY;
    }
}
