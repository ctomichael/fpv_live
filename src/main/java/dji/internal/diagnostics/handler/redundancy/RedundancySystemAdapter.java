package dji.internal.diagnostics.handler.redundancy;

import android.content.Context;
import android.util.SparseArray;
import android.util.SparseIntArray;
import com.dji.frame.util.V_JsonUtil;
import dji.diagnostics.model.DJIDiagnosticsError;
import dji.midware.data.model.P3.DataFlycRedundancyStatus;
import dji.sdkcache.R;
import java.util.Iterator;
import java.util.List;

public class RedundancySystemAdapter {
    private static final int DEV_TYPE_ACC = 9;
    private static final int DEV_TYPE_BARO = 11;
    private static final int DEV_TYPE_BAT = 27;
    private static final int DEV_TYPE_ESC = 1;
    private static final int DEV_TYPE_GPS = 7;
    private static final int DEV_TYPE_GYRO = 8;
    private static final int DEV_TYPE_IMU = 6;
    private static final int DEV_TYPE_MAG = 12;
    private static final int DEV_TYPE_RADAR = 15;
    private static final int DEV_TYPE_RC = 21;
    private static final int DEV_TYPE_RTK = 25;
    private static final int DEV_TYPE_SYS = 3;
    private static final int DEV_TYPE_US = 13;
    private static final int DEV_TYPE_VPS = 14;
    private static List<RedundancyLocalInfo> sLocalConfig;
    private static SparseArray<SparseIntArray> sRedundancyErrorCodeMap;

    private static synchronized void initCodeMap() {
        synchronized (RedundancySystemAdapter.class) {
            if (sRedundancyErrorCodeMap == null) {
                sRedundancyErrorCodeMap = new SparseArray<>(30);
                sRedundancyErrorCodeMap.put(6, getImuCodeMap());
                sRedundancyErrorCodeMap.put(9, getAccelerationCodeMap());
                sRedundancyErrorCodeMap.put(8, getGyroCodeMap());
                sRedundancyErrorCodeMap.put(12, getCompassCodeMap());
                sRedundancyErrorCodeMap.put(7, getGpsCodeMap());
                sRedundancyErrorCodeMap.put(11, getBarometerCodeMap());
                sRedundancyErrorCodeMap.put(25, getRtkCodeMap());
                sRedundancyErrorCodeMap.put(14, getVpsCodeMap());
                sRedundancyErrorCodeMap.put(13, getUltrasonicCodeMap());
                sRedundancyErrorCodeMap.put(15, getRadarCodeMap());
                sRedundancyErrorCodeMap.put(1, getEscCodeMap());
                sRedundancyErrorCodeMap.put(3, getSystemCodeMap());
                sRedundancyErrorCodeMap.put(27, getBatteryCodeMap());
                sRedundancyErrorCodeMap.put(21, getRemoterControllerCodeMap());
            }
        }
    }

    private static synchronized void loadLocalInfo(Context context) {
        synchronized (RedundancySystemAdapter.class) {
            if (sLocalConfig == null) {
                sLocalConfig = ((RedundancyErrorCodeDescJson) V_JsonUtil.toBean(InputStream2String(context.getResources().openRawResource(R.raw.redunredundancy_error_code_desc_new), "utf-8"), RedundancyErrorCodeDescJson.class)).getErr_code();
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:5:0x000c, code lost:
        if (r9.equals("") != false) goto L_0x000e;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String InputStream2String(java.io.InputStream r8, java.lang.String r9) {
        /*
            java.lang.String r4 = ""
            if (r9 == 0) goto L_0x000e
            java.lang.String r6 = ""
            boolean r6 = r9.equals(r6)     // Catch:{ UnsupportedEncodingException -> 0x0031, IOException -> 0x003e }
            if (r6 == 0) goto L_0x0011
        L_0x000e:
            java.lang.String r9 = "utf-8"
        L_0x0011:
            java.io.BufferedReader r2 = new java.io.BufferedReader     // Catch:{ UnsupportedEncodingException -> 0x0031, IOException -> 0x003e }
            java.io.InputStreamReader r6 = new java.io.InputStreamReader     // Catch:{ UnsupportedEncodingException -> 0x0031, IOException -> 0x003e }
            r6.<init>(r8, r9)     // Catch:{ UnsupportedEncodingException -> 0x0031, IOException -> 0x003e }
            r2.<init>(r6)     // Catch:{ UnsupportedEncodingException -> 0x0031, IOException -> 0x003e }
            java.lang.StringBuffer r3 = new java.lang.StringBuffer     // Catch:{ UnsupportedEncodingException -> 0x0031, IOException -> 0x003e }
            r3.<init>()     // Catch:{ UnsupportedEncodingException -> 0x0031, IOException -> 0x003e }
        L_0x0020:
            java.lang.String r4 = r2.readLine()     // Catch:{ UnsupportedEncodingException -> 0x0031, IOException -> 0x003e }
            if (r4 == 0) goto L_0x0038
            java.lang.StringBuffer r6 = r3.append(r4)     // Catch:{ UnsupportedEncodingException -> 0x0031, IOException -> 0x003e }
            java.lang.String r7 = "\n"
            r6.append(r7)     // Catch:{ UnsupportedEncodingException -> 0x0031, IOException -> 0x003e }
            goto L_0x0020
        L_0x0031:
            r1 = move-exception
            r1.printStackTrace()
        L_0x0035:
            r5 = r4
            r6 = r4
        L_0x0037:
            return r6
        L_0x0038:
            java.lang.String r6 = r3.toString()     // Catch:{ UnsupportedEncodingException -> 0x0031, IOException -> 0x003e }
            r5 = r4
            goto L_0x0037
        L_0x003e:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x0035
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.internal.diagnostics.handler.redundancy.RedundancySystemAdapter.InputStream2String(java.io.InputStream, java.lang.String):java.lang.String");
    }

    private static SparseIntArray getImuCodeMap() {
        SparseIntArray sparseArray = new SparseIntArray();
        sparseArray.put(1, DJIDiagnosticsError.FlightController.REDUNDANCY_IMU_DISCONNECTED);
        sparseArray.put(2, DJIDiagnosticsError.FlightController.REDUNDANCY_IMU_VERSION_ERROR);
        sparseArray.put(4, DJIDiagnosticsError.FlightController.REDUNDANCY_IMU_INIT_FAILED);
        sparseArray.put(5, DJIDiagnosticsError.FlightController.REDUNDANCY_IMU_INITIALIZING);
        sparseArray.put(7, DJIDiagnosticsError.FlightController.REDUNDANCY_IMU_ALTITUDE_SUDDEN_CHANGE);
        sparseArray.put(10, DJIDiagnosticsError.FlightController.REDUNDANCY_IMU_NOT_CONVERGED);
        sparseArray.put(13, DJIDiagnosticsError.FlightController.REDUNDANCY_IMU_ILLEGAL_DATA);
        sparseArray.put(16, DJIDiagnosticsError.FlightController.REDUNDANCY_IMU_ALTITUDE_BIAS_TOO_LARGE);
        sparseArray.put(19, DJIDiagnosticsError.FlightController.REDUNDANCY_IMU_STATIC_ACCELERATION_TOO_LARGE);
        sparseArray.put(22, DJIDiagnosticsError.FlightController.REDUNDANCY_IMU_STATIC_ATTITUDE_BIAS_TOO_LARGE);
        sparseArray.put(25, DJIDiagnosticsError.FlightController.REDUNDANCY_IMU_STATIC_ALTITUDE_FLOATING);
        sparseArray.put(28, DJIDiagnosticsError.FlightController.REDUNDANCY_IMU_INCONSISTENT_ANGULAR_VELOCITY);
        sparseArray.put(31, DJIDiagnosticsError.FlightController.REDUNDANCY_IMU_INCONSISTENT_ACCELERATION);
        sparseArray.put(34, DJIDiagnosticsError.FlightController.REDUNDANCY_IMU_INCONSISTENT_VERTICAL_VELOCITY);
        sparseArray.put(37, DJIDiagnosticsError.FlightController.REDUNDANCY_IMU_INCONSISTENT_ALTITUDE);
        sparseArray.put(40, DJIDiagnosticsError.FlightController.REDUNDANCY_IMU_INCONSISTENT_ATTITUDE);
        sparseArray.put(43, DJIDiagnosticsError.FlightController.REDUNDANCY_IMU_INCONSISTENT_COURSE);
        sparseArray.put(47, DJIDiagnosticsError.FlightController.REDUNDANCY_IMU_INCONSISTENT_HORIZONTAL_VELOCITY);
        sparseArray.put(50, DJIDiagnosticsError.FlightController.REDUNDANCY_IMU_INCONSISTENT_LOCATION);
        sparseArray.put(53, DJIDiagnosticsError.FlightController.REDUNDANCY_IMU_LARGE_BIAS_CAMPASS_COURSE);
        sparseArray.put(56, DJIDiagnosticsError.FlightController.REDUNDANCY_IMU_LARGE_BIAS_GPS_COURSE);
        sparseArray.put(59, DJIDiagnosticsError.FlightController.REDUNDANCY_IMU_LARGE_BIAS_HORIZONTAL_VELOCITY);
        sparseArray.put(62, DJIDiagnosticsError.FlightController.REDUNDANCY_IMU_GYRO_BIAS_ESTIMATION_ERROR);
        sparseArray.put(65, DJIDiagnosticsError.FlightController.REDUNDANCY_IMU_BIG_AND_SMALL_FILTERS_NOT_MATCH_HORIZONTAL_ATTITUDE);
        sparseArray.put(68, DJIDiagnosticsError.FlightController.REDUNDANCY_IMU_BIG_AND_SMALL_FILTERS_NOT_MATCH_VERTICAL_VELOCITY);
        sparseArray.put(71, DJIDiagnosticsError.FlightController.REDUNDANCY_IMU_BIG_AND_SMALL_FILTERS_NOT_MATCH_COURSE);
        sparseArray.put(74, DJIDiagnosticsError.FlightController.REDUNDANCY_IMU_SMALL_FILTERS_NOT_MATCH_HORIZONTAL_ATTITUDE);
        sparseArray.put(77, DJIDiagnosticsError.FlightController.REDUNDANCY_IMU_SMALL_FILTERS_NOT_MATCH_VERTICAL_VELOCITY);
        sparseArray.put(80, DJIDiagnosticsError.FlightController.REDUNDANCY_IMU_SMALL_FILTERS_NOT_MATCH_COURSE);
        return sparseArray;
    }

    private static SparseIntArray getAccelerationCodeMap() {
        SparseIntArray sparseArray = new SparseIntArray();
        sparseArray.put(1, DJIDiagnosticsError.FlightController.REDUNDANCY_ACC_ILLEGAL_FLOAT);
        sparseArray.put(4, DJIDiagnosticsError.FlightController.REDUNDANCY_ACC_DATA_STUCK);
        sparseArray.put(7, DJIDiagnosticsError.FlightController.REDUNDANCY_ACC_DISCONNECTED);
        sparseArray.put(10, DJIDiagnosticsError.FlightController.REDUNDANCY_ACC_MEASUREMENT_INCONSISTENT);
        sparseArray.put(13, DJIDiagnosticsError.FlightController.REDUNDANCY_ACC_OUT_OF_RANGE);
        sparseArray.put(16, DJIDiagnosticsError.FlightController.REDUNDANCY_ACC_INNER_PARAM_ERROR);
        sparseArray.put(19, DJIDiagnosticsError.FlightController.REDUNDANCY_ACC_NOISE_ERROR);
        sparseArray.put(22, DJIDiagnosticsError.FlightController.REDUNDANCY_ACC_LARGE_BIAS);
        sparseArray.put(28, DJIDiagnosticsError.FlightController.REDUNDANCY_ACC_CALIBRATION_FAILED);
        sparseArray.put(31, DJIDiagnosticsError.FlightController.REDUNDANCY_ACC_OFFSET_MISSING);
        sparseArray.put(34, DJIDiagnosticsError.FlightController.REDUNDANCY_ACC_OFFSET_ERROR);
        sparseArray.put(37, DJIDiagnosticsError.FlightController.REDUNDANCY_ACC_WARMING_UP_FAILED);
        sparseArray.put(39, DJIDiagnosticsError.FlightController.REDUNDANCY_ACC_TEMPERATURE_GOING_TO_HIGH_LEVEL);
        sparseArray.put(42, DJIDiagnosticsError.FlightController.REDUNDANCY_ACC_TEMPERATURE_TOO_HIGH);
        sparseArray.put(45, DJIDiagnosticsError.FlightController.REDUNDANCY_ACC_TEMPERATURE_TOO_LOW);
        sparseArray.put(48, DJIDiagnosticsError.FlightController.REDUNDANCY_ACC_WARMING_UP);
        sparseArray.put(51, DJIDiagnosticsError.FlightController.REDUNDANCY_ACC_DATA_SYNC_ERROR);
        sparseArray.put(54, DJIDiagnosticsError.FlightController.REDUNDANCY_ACC_SUPER_CALIBRATION_MISSING);
        sparseArray.put(57, DJIDiagnosticsError.FlightController.REDUNDANCY_ACC_BASE_CALIBRATION_MISSING);
        sparseArray.put(60, DJIDiagnosticsError.FlightController.REDUNDANCY_ACC_HORIZONTAL_CALIBRATION_MISSING);
        sparseArray.put(63, DJIDiagnosticsError.FlightController.REDUNDANCY_ACC_STANDARD_VERSION_UP_TO_DATE);
        sparseArray.put(67, DJIDiagnosticsError.FlightController.REDUNDANCY_ACC_INSTALL_MATRIX_ERROR);
        sparseArray.put(70, DJIDiagnosticsError.FlightController.REDUNDANCY_ACC_INNER_OFFSET_ERROR);
        sparseArray.put(73, DJIDiagnosticsError.FlightController.REDUNDANCY_ACC_CALIBRATION_FAILED_TEMPERATURE_OUT_OF_RANGE);
        sparseArray.put(76, DJIDiagnosticsError.FlightController.REDUNDANCY_ACC_CALIBRATION_FAILED_HIGH_TEMPERATURE);
        sparseArray.put(79, DJIDiagnosticsError.FlightController.REDUNDANCY_ACC_CALIBRATION_FAILED_LAST_UNCOMPLETED);
        sparseArray.put(82, DJIDiagnosticsError.FlightController.REDUNDANCY_ACC_CALIBRATION_FAILED_TEMPERATURE_NOT_READY);
        sparseArray.put(85, DJIDiagnosticsError.FlightController.REDUNDANCY_ACC_FLASHING_OPERATION_ERROR);
        sparseArray.put(88, DJIDiagnosticsError.FlightController.REDUNDANCY_ACC_CALIBRATION_FAILED_AUTO_DIRECTION_ERROR);
        sparseArray.put(91, DJIDiagnosticsError.FlightController.REDUNDANCY_ACC_BIG_SHOCK);
        return sparseArray;
    }

    private static SparseIntArray getGyroCodeMap() {
        SparseIntArray sparseArray = new SparseIntArray();
        sparseArray.put(1, DJIDiagnosticsError.FlightController.REDUNDANCY_GYRO_ILLEGAL_FLOAT);
        sparseArray.put(4, DJIDiagnosticsError.FlightController.REDUNDANCY_GYRO_DATA_STUCK);
        sparseArray.put(7, DJIDiagnosticsError.FlightController.REDUNDANCY_GYRO_DISCONNECTED);
        sparseArray.put(10, DJIDiagnosticsError.FlightController.REDUNDANCY_GYRO_MEASUREMENT_INCONSISTENT);
        sparseArray.put(13, DJIDiagnosticsError.FlightController.REDUNDANCY_GYRO_OUT_OF_RANGE);
        sparseArray.put(16, DJIDiagnosticsError.FlightController.REDUNDANCY_GYRO_INNER_PARAM_ERROR);
        sparseArray.put(19, DJIDiagnosticsError.FlightController.REDUNDANCY_GYRO_NOISE_ERROR);
        sparseArray.put(22, DJIDiagnosticsError.FlightController.REDUNDANCY_GYRO_LARGE_BIAS);
        sparseArray.put(28, DJIDiagnosticsError.FlightController.REDUNDANCY_GYRO_CALIBRATION_FAILED);
        sparseArray.put(37, DJIDiagnosticsError.FlightController.REDUNDANCY_GYRO_WARMING_UP_FAILED);
        sparseArray.put(39, DJIDiagnosticsError.FlightController.REDUNDANCY_GYRO_TEMPERATURE_GOING_TO_HIGH_LEVEL);
        sparseArray.put(42, DJIDiagnosticsError.FlightController.REDUNDANCY_GYRO_TEMPERATURE_TOO_HIGH);
        sparseArray.put(45, DJIDiagnosticsError.FlightController.REDUNDANCY_GYRO_TEMPERATURE_TOO_LOW);
        sparseArray.put(48, DJIDiagnosticsError.FlightController.REDUNDANCY_GYRO_WARMING_UP);
        sparseArray.put(51, DJIDiagnosticsError.FlightController.REDUNDANCY_GYRO_DATA_SYNC_ERROR);
        sparseArray.put(54, DJIDiagnosticsError.FlightController.REDUNDANCY_GYRO_SUPER_CALIBRATION_MISSING);
        sparseArray.put(57, DJIDiagnosticsError.FlightController.REDUNDANCY_GYRO_BASE_CALIBRATION_MISSING);
        sparseArray.put(60, DJIDiagnosticsError.FlightController.REDUNDANCY_GYRO_HORIZONTAL_CALIBRATION_MISSING);
        sparseArray.put(63, DJIDiagnosticsError.FlightController.REDUNDANCY_GYRO_STANDARD_VERSION_UP_TO_DATE);
        sparseArray.put(67, DJIDiagnosticsError.FlightController.REDUNDANCY_GYRO_INSTALL_MATRIX_ERROR);
        sparseArray.put(73, DJIDiagnosticsError.FlightController.REDUNDANCY_GYRO_CALIBRATION_FAILED_TEMPERATURE_OUT_OF_RANGE);
        sparseArray.put(76, DJIDiagnosticsError.FlightController.REDUNDANCY_GYRO_CALIBRATION_FAILED_HIGH_TEMPERATURE);
        sparseArray.put(79, DJIDiagnosticsError.FlightController.REDUNDANCY_GYRO_CALIBRATION_FAILED_LAST_UNCOMPLETED);
        sparseArray.put(82, DJIDiagnosticsError.FlightController.REDUNDANCY_GYRO_CALIBRATION_FAILED_TEMPERATURE_NOT_READY);
        sparseArray.put(85, DJIDiagnosticsError.FlightController.REDUNDANCY_GYRO_FLASHING_OPERATION_ERROR);
        sparseArray.put(88, DJIDiagnosticsError.FlightController.REDUNDANCY_GYRO_CALIBRATION_FAILED_AUTO_DIRECTION_ERROR);
        sparseArray.put(91, DJIDiagnosticsError.FlightController.REDUNDANCY_GYRO_BIG_SHOCK);
        return sparseArray;
    }

    private static SparseIntArray getCompassCodeMap() {
        SparseIntArray sparseArray = new SparseIntArray();
        sparseArray.put(1, DJIDiagnosticsError.FlightController.REDUNDANCY_COMPASS_DISCONNECTED);
        sparseArray.put(4, DJIDiagnosticsError.FlightController.REDUNDANCY_COMPASS_DATA_STUCK);
        sparseArray.put(7, DJIDiagnosticsError.FlightController.REDUNDANCY_COMPASS_NOISE_ERROR);
        sparseArray.put(10, DJIDiagnosticsError.FlightController.REDUNDANCY_COMPASS_DATA_JUMP_ERROR);
        sparseArray.put(13, DJIDiagnosticsError.FlightController.REDUNDANCY_COMPASS_OUT_OF_RANGE);
        sparseArray.put(16, DJIDiagnosticsError.FlightController.REDUNDANCY_COMPASS_DATA_INCONSISTENT);
        sparseArray.put(19, DJIDiagnosticsError.FlightController.REDUNDANCY_COMPASS_CALIBRATION_PARAM_MISSING);
        sparseArray.put(22, DJIDiagnosticsError.FlightController.REDUNDANCY_COMPASS_COMMUNICATION_ERROR);
        sparseArray.put(25, DJIDiagnosticsError.FlightController.REDUNDANCY_COMPASS_BIAS_TOO_LARGE);
        sparseArray.put(28, DJIDiagnosticsError.FlightController.REDUNDANCY_COMPASS_SCALE_ERROR);
        sparseArray.put(31, DJIDiagnosticsError.FlightController.REDUNDANCY_COMPASS_NON_ORTHOGONAL_BIAS_TO_LARGE);
        sparseArray.put(34, DJIDiagnosticsError.FlightController.REDUNDANCY_COMPASS_INSTALL_DIRECTION_ERROR);
        sparseArray.put(37, DJIDiagnosticsError.FlightController.REDUNDANCY_COMPASS_INSTALL_MATRIX_ERROR);
        sparseArray.put(40, DJIDiagnosticsError.FlightController.REDUNDANCY_COMPASS_DISTURB_FROM_GROUND);
        sparseArray.put(43, DJIDiagnosticsError.FlightController.REDUNDANCY_COMPASS_DISTURB_FORM_INCLINATION);
        sparseArray.put(46, DJIDiagnosticsError.FlightController.REDUNDANCY_COMPASS_CALIBRATION_FAILED);
        return sparseArray;
    }

    private static SparseIntArray getGpsCodeMap() {
        SparseIntArray sparseArray = new SparseIntArray();
        sparseArray.put(1, DJIDiagnosticsError.FlightController.REDUNDANCY_GPS_ILLEGAL_FLOAT);
        sparseArray.put(4, DJIDiagnosticsError.FlightController.REDUNDANCY_GPS_DISCONNECTED);
        sparseArray.put(7, DJIDiagnosticsError.FlightController.REDUNDANCY_GPS_OUT_OF_RANGE);
        sparseArray.put(10, DJIDiagnosticsError.FlightController.REDUNDANCY_GPS_SUDDEN_CHANGE);
        sparseArray.put(13, DJIDiagnosticsError.FlightController.REDUNDANCY_GPS_VELOCITY_INCONSISTENT);
        sparseArray.put(16, DJIDiagnosticsError.FlightController.REDUNDANCY_GPS_LOCATION_INCONSISTENT);
        sparseArray.put(19, DJIDiagnosticsError.FlightController.REDUNDANCY_GPS_OFFSET_MISSING);
        sparseArray.put(22, DJIDiagnosticsError.FlightController.REDUNDANCY_GPS_OFFSET_ERROR);
        sparseArray.put(25, DJIDiagnosticsError.FlightController.REDUNDANCY_GPS_VERSION_NOT_MATCH);
        sparseArray.put(28, DJIDiagnosticsError.FlightController.REDUNDANCY_GPS_UPDATE_FREQUENCY_INCONSISTENT);
        sparseArray.put(31, DJIDiagnosticsError.FlightController.REDUNDANCY_GPS_SEARCH_SATELLITE_ERROR);
        sparseArray.put(34, DJIDiagnosticsError.FlightController.REDUNDANCY_GPS_HDOP_INCONSISTENT);
        return sparseArray;
    }

    private static SparseIntArray getBarometerCodeMap() {
        SparseIntArray sparseArray = new SparseIntArray();
        sparseArray.put(1, DJIDiagnosticsError.FlightController.REDUNDANCY_BAROMETER_ILLEGAL_FLOAT);
        sparseArray.put(4, DJIDiagnosticsError.FlightController.REDUNDANCY_BAROMETER_DATA_STUCK);
        sparseArray.put(7, DJIDiagnosticsError.FlightController.REDUNDANCY_BAROMETER_DISCONNECTED);
        sparseArray.put(10, DJIDiagnosticsError.FlightController.REDUNDANCY_BAROMETER_OUT_OF_RANGE);
        sparseArray.put(13, DJIDiagnosticsError.FlightController.REDUNDANCY_BAROMETER_MEASUREMENT_INCONSISTENT);
        sparseArray.put(16, DJIDiagnosticsError.FlightController.REDUNDANCY_BAROMETER_SUDDEN_CHANGE);
        sparseArray.put(19, DJIDiagnosticsError.FlightController.REDUNDANCY_BAROMETER_DATA_FLOATING);
        sparseArray.put(22, DJIDiagnosticsError.FlightController.REDUNDANCY_BAROMETER_TEMPERATURE_ERROR);
        sparseArray.put(25, DJIDiagnosticsError.FlightController.REDUNDANCY_BAROMETER_NOISE_ERROR);
        return sparseArray;
    }

    private static SparseIntArray getRtkCodeMap() {
        SparseIntArray sparseArray = new SparseIntArray();
        sparseArray.put(1, DJIDiagnosticsError.FlightController.REDUNDANCY_RTK_DATA_INVALID);
        sparseArray.put(4, DJIDiagnosticsError.FlightController.REDUNDANCY_RTK_DATA_ERROR);
        sparseArray.put(7, DJIDiagnosticsError.FlightController.REDUNDANCY_RTK_POSITION_JUMP);
        sparseArray.put(10, DJIDiagnosticsError.FlightController.REDUNDANCY_RTK_SPEED_JUMP);
        sparseArray.put(13, DJIDiagnosticsError.FlightController.REDUNDANCY_RTK_NOT_CONNECT);
        sparseArray.put(16, DJIDiagnosticsError.FlightController.REDUNDANCY_RTK_OFFSET_EMPTY);
        sparseArray.put(19, DJIDiagnosticsError.FlightController.REDUNDANCY_RTK_OFFSET_ERROR);
        sparseArray.put(22, DJIDiagnosticsError.FlightController.REDUNDANCY_RTK_VERSION_NOT_MATCH);
        sparseArray.put(25, DJIDiagnosticsError.FlightController.REDUNDANCY_RTK_BASE_STATION_COMMUNICATION_ERROR);
        sparseArray.put(28, DJIDiagnosticsError.FlightController.REDUNDANCY_RTK_MEASURE_ERROR);
        sparseArray.put(31, DJIDiagnosticsError.FlightController.REDUNDANCY_RTK_DIRECTION_MEASURE_ERROR);
        sparseArray.put(34, DJIDiagnosticsError.FlightController.REDUNDANCY_RTK_FREQUENCY_ERROR);
        sparseArray.put(37, DJIDiagnosticsError.FlightController.REDUNDANCY_BASE_STATION_SIGNAL_ERROR);
        sparseArray.put(40, DJIDiagnosticsError.FlightController.REDUNDANCY_RTK_MAIN_ANTENNA_SIGNAL_ERROR);
        sparseArray.put(43, DJIDiagnosticsError.FlightController.REDUNDANCY_RTK_SUB_ANTENNA_SIGNAL_ERROR);
        sparseArray.put(46, DJIDiagnosticsError.FlightController.REDUNDANCY_RTK_BOARD_COMMUNICATION_ERROR);
        return sparseArray;
    }

    private static SparseIntArray getVpsCodeMap() {
        SparseIntArray sparseArray = new SparseIntArray();
        sparseArray.put(1, DJIDiagnosticsError.FlightController.REDUNDANCY_VPS_INVALID_ERROR);
        sparseArray.put(4, DJIDiagnosticsError.FlightController.REDUNDANCY_VPS_DATA_ERROR);
        sparseArray.put(7, DJIDiagnosticsError.FlightController.REDUNDANCY_VPS_NOT_CONNECT);
        sparseArray.put(10, DJIDiagnosticsError.FlightController.REDUNDANCY_VPS_INSTALL_ERROR);
        sparseArray.put(13, DJIDiagnosticsError.FlightController.REDUNDANCY_VPS_VERSION_NOT_MATCH);
        sparseArray.put(16, DJIDiagnosticsError.FlightController.REDUNDANCY_VPS_COMMUNICATION_ERROR);
        sparseArray.put(19, DJIDiagnosticsError.FlightController.REDUNDANCY_VPS_SPEED_JUMP);
        sparseArray.put(22, DJIDiagnosticsError.FlightController.REDUNDANCY_VPS_POSITION_JUMP);
        return sparseArray;
    }

    private static SparseIntArray getUltrasonicCodeMap() {
        SparseIntArray sparseArray = new SparseIntArray();
        sparseArray.put(1, DJIDiagnosticsError.FlightController.REDUNDANCY_ULTRASONIC_INVALID_DATA);
        sparseArray.put(4, DJIDiagnosticsError.FlightController.REDUNDANCY_ULTRASONIC_DATA_ERROR);
        sparseArray.put(7, DJIDiagnosticsError.FlightController.REDUNDANCY_ULTRASONIC_NOT_CONNECT);
        sparseArray.put(10, DJIDiagnosticsError.FlightController.REDUNDANCY_ULTRASONIC_INSTALL_ERROR);
        sparseArray.put(13, DJIDiagnosticsError.FlightController.REDUNDANCY_ULTRASONIC_VERSION_NOT_MATCH);
        sparseArray.put(16, DJIDiagnosticsError.FlightController.REDUNDANCY_ULTRASONIC_COMMUNICATION_ERROR);
        return sparseArray;
    }

    private static SparseIntArray getRadarCodeMap() {
        SparseIntArray sparseArray = new SparseIntArray();
        sparseArray.put(1, DJIDiagnosticsError.FlightController.REDUNDANCY_RADAR_INVALID_DATA);
        sparseArray.put(4, DJIDiagnosticsError.FlightController.REDUNDANCY_RADAR_DATA_ERROR);
        sparseArray.put(7, DJIDiagnosticsError.FlightController.REDUNDANCY_RADAR_NOT_CONNECT);
        sparseArray.put(10, DJIDiagnosticsError.FlightController.REDUNDANCY_RADAR_INSTALL_ERROR);
        sparseArray.put(13, DJIDiagnosticsError.FlightController.REDUNDANCY_RADAR_VERSION_NOT_MATCH);
        sparseArray.put(16, DJIDiagnosticsError.FlightController.REDUNDANCY_RADAR_COMMUNICATION_ERROR);
        return sparseArray;
    }

    private static SparseIntArray getEscCodeMap() {
        SparseIntArray sparseArray = new SparseIntArray();
        sparseArray.put(1, DJIDiagnosticsError.FlightController.REDUNDANCY_FLYC_ESC_ERROR);
        return sparseArray;
    }

    private static SparseIntArray getSystemCodeMap() {
        SparseIntArray sparseArray = new SparseIntArray();
        sparseArray.put(1, DJIDiagnosticsError.FlightController.REDUNDANCY_SYSTEM_BE_LOCKED);
        sparseArray.put(2, DJIDiagnosticsError.FlightController.REDUNDANCY_SYSTEM_SERIAL_NUM_ERROR);
        sparseArray.put(3, DJIDiagnosticsError.FlightController.REDUNDANCY_SYSTEM_SD_CARD_ERROR);
        sparseArray.put(4, DJIDiagnosticsError.FlightController.REDUNDANCY_SYSTEM_NOT_ACTIVE);
        sparseArray.put(5, DJIDiagnosticsError.FlightController.REDUNDANCY_SYSTEM_TOO_LIGHT);
        return sparseArray;
    }

    private static SparseIntArray getBatteryCodeMap() {
        SparseIntArray sparseArray = new SparseIntArray();
        sparseArray.put(1, DJIDiagnosticsError.FlightController.REDUNDANCY_BATTERY_CHIP_ERROR);
        sparseArray.put(2, DJIDiagnosticsError.FlightController.REDUNDANCY_BATTERY_COMMUNICATION_ERROR_NEED_FACTORY);
        sparseArray.put(3, DJIDiagnosticsError.FlightController.REDUNDANCY_BATTERY_VOLTAGE_LOW);
        sparseArray.put(4, DJIDiagnosticsError.FlightController.REDUNDANCY_BATTERY_NUM_LESS);
        sparseArray.put(5, DJIDiagnosticsError.FlightController.REDUNDANCY_BATTERY_CERTIFICATION_ERROR);
        sparseArray.put(6, DJIDiagnosticsError.FlightController.REDUNDANCY_BATTERY_COMMUNICATION_ERROR_NEED_RESTART);
        sparseArray.put(7, DJIDiagnosticsError.FlightController.REDUNDANCY_BATTERY_VOLTAGE_DIFFERENCE_LARGE);
        sparseArray.put(8, DJIDiagnosticsError.FlightController.REDUNDANCY_BATTERY_VOLTAGE_DIFFERENCE_TOO_LARGE);
        return sparseArray;
    }

    private static SparseIntArray getRemoterControllerCodeMap() {
        SparseIntArray sparseArray = new SparseIntArray();
        sparseArray.put(1, DJIDiagnosticsError.FlightController.REDUNDANCY_RC_NEED_CALIBRATION);
        sparseArray.put(2, DJIDiagnosticsError.FlightController.REDUNDANCY_RC_CALIBRATION_PARAM_ERROR);
        sparseArray.put(3, DJIDiagnosticsError.FlightController.REDUNDANCY_RC_CHANNEL_MAP_ERROR);
        sparseArray.put(4, DJIDiagnosticsError.FlightController.REDUNDANCY_RC_CENTER_POSITION_BIAS_OUT_OF_SCOPE);
        sparseArray.put(5, DJIDiagnosticsError.FlightController.REDUNDANCY_RC_CALIBRATION_UNCOMPLETE);
        return sparseArray;
    }

    public static RedundancyErrorInfo getRedundancyErrorInfo(Context context, DataFlycRedundancyStatus.IMUStatus imuStatus) {
        boolean z = false;
        if (sRedundancyErrorCodeMap == null) {
            initCodeMap();
        }
        if (sLocalConfig == null) {
            loadLocalInfo(context);
        }
        int devType = imuStatus.devType;
        int errType = imuStatus.devErrCode;
        SparseIntArray errToCodeMap = sRedundancyErrorCodeMap.get(devType);
        RedundancyErrorInfo result = new RedundancyErrorInfo(errToCodeMap == null ? 0 : errToCodeMap.get(errType), imuStatus.imuIndex);
        RedundancyLocalInfo theLocalInfo = null;
        Iterator<RedundancyLocalInfo> it2 = sLocalConfig.iterator();
        while (true) {
            if (!it2.hasNext()) {
                break;
            }
            RedundancyLocalInfo localInfo = it2.next();
            if (devType == localInfo.getDev_type() && errType == localInfo.getErr_type()) {
                theLocalInfo = localInfo;
                break;
            }
        }
        if (theLocalInfo != null) {
            result.setReason(getReasonString(context, theLocalInfo, imuStatus.isRealInAir));
            result.setSolution(getSolutionString(context, theLocalInfo, imuStatus.isRealInAir));
            if (theLocalInfo.getUsr_show_enable() == 1) {
                z = true;
            }
            result.setUsrShowEnable(z);
        }
        return result;
    }

    private static String getSolutionString(Context context, RedundancyLocalInfo theLocalInfo, boolean isRealInAir) {
        if (isRealInAir) {
            return theLocalInfo.getIn_air_tips_id();
        }
        return theLocalInfo.getGround_tips_id();
    }

    private static String getReasonString(Context context, RedundancyLocalInfo theLocalInfo, boolean isRealInAir) {
        if (isRealInAir) {
            return theLocalInfo.getIn_air_tips_id();
        }
        return theLocalInfo.getGround_tips_id();
    }
}
