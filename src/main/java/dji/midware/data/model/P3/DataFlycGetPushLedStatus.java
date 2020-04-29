package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;
import java.util.ArrayList;
import java.util.List;

@Keep
@EXClassNullAway
public class DataFlycGetPushLedStatus extends DataBase {
    private static final int MAX_NUM = 20;
    private static final int TIME_TICK = 20;
    private static DataFlycGetPushLedStatus instance = null;

    @Keep
    public static class LedStatus {
        public LED_COLOR mColor = LED_COLOR.OFF;
        public int mInterval = 0;
    }

    public static synchronized DataFlycGetPushLedStatus getInstance() {
        DataFlycGetPushLedStatus dataFlycGetPushLedStatus;
        synchronized (DataFlycGetPushLedStatus.class) {
            if (instance == null) {
                instance = new DataFlycGetPushLedStatus();
            }
            dataFlycGetPushLedStatus = instance;
        }
        return dataFlycGetPushLedStatus;
    }

    public LED_REASON getLedReason() {
        if (this._recData == null) {
            return null;
        }
        return LED_REASON.find(((Integer) get(0, 4, Integer.class)).intValue());
    }

    public List<LedStatus> getLedStatus() {
        int num = ((Integer) get(4, 4, Integer.class)).intValue();
        if (num > 20) {
            num = 20;
        }
        List<LedStatus> leds = null;
        if (num > 0) {
            leds = new ArrayList<>(num);
            for (int i = 0; i < num; i++) {
                LedStatus led = new LedStatus();
                led.mColor = LED_COLOR.find(((Integer) get((i * 4) + 8, 2, Integer.class)).intValue());
                led.mInterval = ((Integer) get((i * 4) + 10, 2, Integer.class)).intValue() * 20;
                leds.add(led);
            }
        }
        return leds;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    @Keep
    public enum LED_REASON {
        SET_HOME(0),
        SET_HOT_POINT(1),
        SET_COURSE_LOCK(2),
        TEST_LED(3),
        USB_IS_VALID(4),
        PACKING_FAIL(5),
        PACKING_NORMAL(6),
        NO_ATTI(7),
        COMPASS_CALI_STEP0(8),
        COMPASS_CALI_STEP1(9),
        COMPASS_CALI_ERROR(10),
        SENSOR_TEMP_NOT_READY(11),
        IMU_OR_GYRO_LOST(12),
        IMU_BAD_ATTI(13),
        SYSTEM_ERROR(14),
        IMU_ERROR(15),
        IMU_NEED_CALI(16),
        COMPASS_OUT_RANGE(17),
        RC_COMPLETELY_LOST(18),
        BATTERY_WARNING(19),
        BATTERY_ERROR(20),
        IMU_WARNING(21),
        SET_FLY_LIMIT(22),
        NORMAL_LED(23),
        FDI_VIBRATE(24),
        CODE_ERROR(25),
        SYSTEM_RECONSTRUCTION(26),
        RECORDER_ERROR(27);
        
        private int data = 0;

        private LED_REASON(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static LED_REASON find(int b) {
            LED_REASON result = NORMAL_LED;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum LED_COLOR {
        OFF(0),
        RED(1),
        GREEN(2),
        BLUE(3),
        YELLOW(4),
        DEEP_RED(5),
        CYAN(6),
        PURPLE(7),
        WHITE(8),
        PURPLE1(9),
        PURPLE2(10),
        PURPLE3(11),
        COLOR_ANY(12),
        COLOR_UNDEFINED(13);
        
        private int data = 0;

        private LED_COLOR(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static LED_COLOR find(int b) {
            LED_COLOR result = OFF;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
