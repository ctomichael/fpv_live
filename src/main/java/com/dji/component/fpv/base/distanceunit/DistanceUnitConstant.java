package com.dji.component.fpv.base.distanceunit;

import com.dji.component.fpv.base.util.DJIUnitUtil;
import com.dji.component.persistence.DJIPersistenceStorage;

public class DistanceUnitConstant {
    private static final String DISTANCE_UNIT_KEY = "dji_distance_unit_key";

    public enum DistanceUnit {
        METER(0),
        KILOMETER(1),
        MILE(2);
        
        public int id;

        private DistanceUnit(int i) {
            this.id = i;
        }

        public static DistanceUnit find(int id2) {
            DistanceUnit[] values = values();
            for (DistanceUnit distanceUnit : values) {
                if (distanceUnit.id == id2) {
                    return distanceUnit;
                }
            }
            return METER;
        }
    }

    public static String getDistanceUnitKey() {
        return DISTANCE_UNIT_KEY;
    }

    public static DistanceUnit getDistanceUnit() {
        return DistanceUnit.find(DJIPersistenceStorage.getInt(DISTANCE_UNIT_KEY, DistanceUnit.METER.id));
    }

    public static void setDistanceUnit(DistanceUnit unit) {
        DJIPersistenceStorage.putInt(DISTANCE_UNIT_KEY, unit.id);
    }

    public static String getUnitSpeedStr() {
        DistanceUnit unit = getDistanceUnit();
        if (unit == DistanceUnit.METER) {
            return "m/s";
        }
        if (unit == DistanceUnit.KILOMETER) {
            return "km/h";
        }
        return "mph";
    }

    public static String getUnitDistStr() {
        DistanceUnit unit = getDistanceUnit();
        if (unit == DistanceUnit.METER || unit == DistanceUnit.KILOMETER) {
            return "m";
        }
        return "ft";
    }

    public static float transformLength(float length) {
        if (getDistanceUnit() == DistanceUnit.MILE) {
            return DJIUnitUtil.metricToImperialByLength(length);
        }
        return length;
    }

    public static int transformLength(int length) {
        if (getDistanceUnit() == DistanceUnit.MILE) {
            return (int) DJIUnitUtil.metricToImperialByLength((float) length);
        }
        return length;
    }

    public static float transformSpeed(float speed) {
        DistanceUnit unit = getDistanceUnit();
        if (unit == DistanceUnit.MILE) {
            return DJIUnitUtil.metricToImperialBySpeed(speed);
        }
        if (unit == DistanceUnit.KILOMETER) {
            return DJIUnitUtil.fromMeterPerSecondToKiloMeterPerHour(speed);
        }
        return speed;
    }
}
