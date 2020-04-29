package dji.common.flightcontroller;

import com.drew.metadata.exif.makernotes.NikonType2MakernoteDirectory;
import com.drew.metadata.exif.makernotes.PanasonicMakernoteDirectory;
import dji.fieldAnnotation.EXClassNullAway;
import it.sauronsoftware.ftp4j.FTPCodes;
import org.bouncycastle.asn1.eac.CertificateBody;

@EXClassNullAway
public enum MotorStartFailedState {
    NONE(0),
    COMPASS_ERROR(1),
    ASSISTANT_PROTECTED(2),
    DEVICE_LOCKED(3),
    DISTANCE_LIMIT(4),
    IMU_NEED_CALIBRATION(5),
    IMU_SN_ERROR(6),
    IMU_WARNING(7),
    COMPASS_CALIBRATION(8),
    ATTI_ERROR(9),
    NOVICE_PROTECTED(10),
    BATTERY_CELL_ERROR(11),
    BATTERY_COMMUNITE_ERROR(12),
    SERIOUS_LOW_VOLTAGE(13),
    SERIOUS_LOW_POWER(14),
    LOW_VOLTAGE(15),
    LOW_TEMPERATURE(16),
    SMART_LOW_TO_LAND(17),
    BATTERY_NOT_READY(18),
    SIMULATOR_MODE(19),
    PACK_MODE(20),
    ATTITUDE_ABNORMAL(21),
    UNACTIVE_ERROR(22),
    FLY_FORBIDDEN_ERROR(23),
    BIAS_ERROR(24),
    ESC_ERROR(25),
    IMU_INIT_ERROR(26),
    SYSTEM_UPGRADE(27),
    SIMULATOR_STARTED(28),
    IMUING_ERROR(29),
    ATTI_ANGLE_OVER(30),
    GYROSCOPE_ERROR(31),
    ACCELERATOR_ERROR(32),
    COMPASS_FAILED(33),
    BAROMETER_ERROR(34),
    BAROMETER_NEGATIVE(35),
    BIG_COMPASS(36),
    GYROSCOPE_BIAS_BIG(37),
    BIG_ACCELERATOR_BIAS(38),
    COMPASS_NOISE_BIG(39),
    BAROMETER_NOISE_BIG(40),
    INVALID_SN(41),
    FLASH_OPERATING(44),
    GPS_DISCONNECT(45),
    SDCARD_EXCEPTION(47),
    IMU_NOCONNECTION(61),
    RC_CALIBRATION(62),
    RC_CALIBRATION_EXCEPTION(63),
    RC_CALIBRATION_UNFINISHED(64),
    RC_CALIBRATION_EXCEPTION2(65),
    RC_CALIBRATION_EXCEPTION3(66),
    AIRCRAFT_TYPE_MISMATCH(67),
    FOUND_UNFINISHED_MODULE(68),
    CYRO_ABNORMAL(70),
    BARO_ABNORMAL(71),
    COMPASS_ABNORMAL(72),
    GPS_ABNORMAL(73),
    NS_ABNORMAL(74),
    TOPOLOGY_ABNORMAL(75),
    RC_NEED_CALI(76),
    INVALID_FLOAT(77),
    M600_BAT_TOO_LITTLE(78),
    M600_BAT_AUTH_ERR(79),
    M600_BAT_COMM_ERR(80),
    M600_BAT_DIF_VOLT_LARGE_1(81),
    M600_BAT_DIF_VOLT_LARGE_2(82),
    INVALID_VERSION(83),
    GIMBAL_GYRO_ABNORMAL(84),
    GIMBAL_ESC_PITCH_NON_DATA(85),
    GIMBAL_ESC_ROLL_NON_DATA(86),
    GIMBAL_ESC_YAW_NON_DATA(87),
    GIMBAL_FIRM_IS_UPDATING(88),
    GIMBAL_DISORDER(89),
    GIMBAL_PITCH_SHOCK(90),
    GIMBAL_ROLL_SHOCK(91),
    GIMBAL_YAW_SHOCK(92),
    IMU_CALIBRATION_FINISHED(93),
    TAKEOFF_ROLLOVER(94),
    MOTOR_STUCK(95),
    MOTOR_UNBALANCED(96),
    MOTOR_LESS_PADDLE(97),
    MOTOR_START_ERROR(98),
    MOTOR_AUTO_TAKEOFF_FAIL(99),
    BAT_VERSION_ERROR(101),
    RTK_BAD_SIGNAL(102),
    RTK_DEVIATION_ERROR(103),
    ESC_CALIBRATING(112),
    GPS_SIGN_INVALID(113),
    GIMBAL_IS_CALIBRATING(114),
    LOCK_BY_APP(115),
    START_FLY_HEIGHT_ERROR(116),
    ESC_VERSION_NOT_MATCH(117),
    IMU_ORI_NOT_MATCH(118),
    STOP_BY_APP(119),
    COMPASS_IMU_ORI_NOT_MATCH(120),
    ESC_ECHOING(122),
    BATTERY_OVER_TEMPERATURE(123),
    BATTERY_INSTALL_ERROR(PanasonicMakernoteDirectory.TAG_CLEAR_RETOUCH),
    BE_IMPACT(FTPCodes.DATA_CONNECTION_ALREADY_OPEN),
    CRASH(CertificateBody.profileType),
    LOW_VERSION_OF_BATTERY(129),
    VOLTAGE_OF_BATTERY_IS_TOO_HIGH(NikonType2MakernoteDirectory.TAG_ADAPTER),
    BATTERY_EMBED_ERROR(131),
    COOLING_FAN_EXCEPTION(132),
    RC_THROTTLE_IS_NOT_IN_MIDDLE(136),
    REMOTE_USB_CONNECTED(200, 255),
    OTHER(Integer.MAX_VALUE);
    
    private static volatile MotorStartFailedState[] sValues = null;
    private final int maxValue;
    private final int minValue;
    private int relValue;

    private MotorStartFailedState(int value) {
        this(r1, r2, value, value);
        this.relValue = value;
    }

    private MotorStartFailedState(int minValue2, int maxValue2) {
        this.relValue = 0;
        this.minValue = minValue2;
        this.maxValue = maxValue2;
    }

    public int relValue() {
        return this.relValue;
    }

    public boolean _equals(int b) {
        return this.minValue <= b && b <= this.maxValue;
    }

    public static MotorStartFailedState find(int b) {
        if (sValues == null) {
            sValues = values();
        }
        MotorStartFailedState result = OTHER;
        MotorStartFailedState[] values = sValues;
        int i = 0;
        int length = values.length;
        while (true) {
            if (i >= length) {
                break;
            } else if (values[i]._equals(b)) {
                result = values[i];
                break;
            } else {
                i++;
            }
        }
        result.relValue = b;
        return result;
    }
}
