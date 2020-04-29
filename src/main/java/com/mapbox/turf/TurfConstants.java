package com.mapbox.turf;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class TurfConstants {
    public static final String UNIT_CENTIMETERS = "centimeters";
    public static final String UNIT_CENTIMETRES = "centimetres";
    public static final String UNIT_DEFAULT = "kilometers";
    public static final String UNIT_DEGREES = "degrees";
    public static final String UNIT_FEET = "feet";
    public static final String UNIT_INCHES = "inches";
    public static final String UNIT_KILOMETERS = "kilometers";
    public static final String UNIT_KILOMETRES = "kilometres";
    public static final String UNIT_METERS = "meters";
    public static final String UNIT_METRES = "metres";
    public static final String UNIT_MILES = "miles";
    public static final String UNIT_NAUTICAL_MILES = "nauticalmiles";
    public static final String UNIT_RADIANS = "radians";
    public static final String UNIT_YARDS = "yards";

    @Retention(RetentionPolicy.SOURCE)
    public @interface TurfUnitCriteria {
    }

    private TurfConstants() {
    }
}
