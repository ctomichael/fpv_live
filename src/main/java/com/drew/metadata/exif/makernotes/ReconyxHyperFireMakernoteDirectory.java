package com.drew.metadata.exif.makernotes;

import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Directory;
import dji.sdksharedlib.keycatalog.CameraKeys;
import java.util.HashMap;

public class ReconyxHyperFireMakernoteDirectory extends Directory {
    public static final int MAKERNOTE_VERSION = 61697;
    public static final int TAG_AMBIENT_TEMPERATURE = 40;
    public static final int TAG_AMBIENT_TEMPERATURE_FAHRENHEIT = 38;
    public static final int TAG_BATTERY_VOLTAGE = 84;
    public static final int TAG_BRIGHTNESS = 74;
    public static final int TAG_CONTRAST = 72;
    public static final int TAG_DATE_TIME_ORIGINAL = 22;
    public static final int TAG_EVENT_NUMBER = 18;
    public static final int TAG_FIRMWARE_VERSION = 2;
    public static final int TAG_INFRARED_ILLUMINATOR = 80;
    public static final int TAG_MAKERNOTE_VERSION = 0;
    public static final int TAG_MOON_PHASE = 36;
    public static final int TAG_MOTION_SENSITIVITY = 82;
    public static final int TAG_SATURATION = 78;
    public static final int TAG_SEQUENCE = 14;
    public static final int TAG_SERIAL_NUMBER = 42;
    public static final int TAG_SHARPNESS = 76;
    public static final int TAG_TRIGGER_MODE = 12;
    public static final int TAG_USER_LABEL = 86;
    @NotNull
    protected static final HashMap<Integer, String> _tagNameMap = new HashMap<>();

    static {
        _tagNameMap.put(0, "Makernote Version");
        _tagNameMap.put(2, "Firmware Version");
        _tagNameMap.put(12, "Trigger Mode");
        _tagNameMap.put(14, "Sequence");
        _tagNameMap.put(18, "Event Number");
        _tagNameMap.put(22, "Date/Time Original");
        _tagNameMap.put(36, "Moon Phase");
        _tagNameMap.put(38, "Ambient Temperature Fahrenheit");
        _tagNameMap.put(40, "Ambient Temperature");
        _tagNameMap.put(42, "Serial Number");
        _tagNameMap.put(72, CameraKeys.CONTRAST);
        _tagNameMap.put(74, "Brightness");
        _tagNameMap.put(76, CameraKeys.SHARPNESS);
        _tagNameMap.put(78, CameraKeys.SATURATION);
        _tagNameMap.put(80, "Infrared Illuminator");
        _tagNameMap.put(82, "Motion Sensitivity");
        _tagNameMap.put(84, "Battery Voltage");
        _tagNameMap.put(86, "User Label");
    }

    public ReconyxHyperFireMakernoteDirectory() {
        setDescriptor(new ReconyxHyperFireMakernoteDescriptor(this));
    }

    @NotNull
    public String getName() {
        return "Reconyx HyperFire Makernote";
    }

    /* access modifiers changed from: protected */
    @NotNull
    public HashMap<Integer, String> getTagNameMap() {
        return _tagNameMap;
    }
}
