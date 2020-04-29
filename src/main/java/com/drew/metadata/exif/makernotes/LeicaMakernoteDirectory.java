package com.drew.metadata.exif.makernotes;

import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Directory;
import java.util.HashMap;

public class LeicaMakernoteDirectory extends Directory {
    public static final int TAG_APPROXIMATE_F_NUMBER = 787;
    public static final int TAG_CAMERA_TEMPERATURE = 800;
    public static final int TAG_CCD_BOARD_VERSION = 817;
    public static final int TAG_CCD_VERSION = 816;
    public static final int TAG_COLOR_TEMPERATURE = 801;
    public static final int TAG_CONTROLLER_BOARD_VERSION = 818;
    public static final int TAG_EXTERNAL_SENSOR_BRIGHTNESS_VALUE = 785;
    public static final int TAG_IMAGE_ID_NUMBER = 832;
    public static final int TAG_LENS_TYPE = 784;
    public static final int TAG_M16_C_VERSION = 819;
    public static final int TAG_MEASURED_LV = 786;
    public static final int TAG_QUALITY = 768;
    public static final int TAG_SERIAL_NUMBER = 771;
    public static final int TAG_USER_PROFILE = 770;
    public static final int TAG_WB_BLUE_LEVEL = 804;
    public static final int TAG_WB_GREEN_LEVEL = 803;
    public static final int TAG_WB_RED_LEVEL = 802;
    public static final int TAG_WHITE_BALANCE = 772;
    @NotNull
    protected static final HashMap<Integer, String> _tagNameMap = new HashMap<>();

    static {
        _tagNameMap.put(768, "Quality");
        _tagNameMap.put(770, "User Profile");
        _tagNameMap.put(771, "Serial Number");
        _tagNameMap.put(772, "White Balance");
        _tagNameMap.put(784, "Lens Type");
        _tagNameMap.put(785, "External Sensor Brightness Value");
        _tagNameMap.put(Integer.valueOf((int) TAG_MEASURED_LV), "Measured LV");
        _tagNameMap.put(Integer.valueOf((int) TAG_APPROXIMATE_F_NUMBER), "Approximate F Number");
        _tagNameMap.put(Integer.valueOf((int) TAG_CAMERA_TEMPERATURE), "Camera Temperature");
        _tagNameMap.put(Integer.valueOf((int) TAG_COLOR_TEMPERATURE), "Color Temperature");
        _tagNameMap.put(Integer.valueOf((int) TAG_WB_RED_LEVEL), "WB Red Level");
        _tagNameMap.put(Integer.valueOf((int) TAG_WB_GREEN_LEVEL), "WB Green Level");
        _tagNameMap.put(Integer.valueOf((int) TAG_WB_BLUE_LEVEL), "WB Blue Level");
        _tagNameMap.put(Integer.valueOf((int) TAG_CCD_VERSION), "CCD Version");
        _tagNameMap.put(Integer.valueOf((int) TAG_CCD_BOARD_VERSION), "CCD Board Version");
        _tagNameMap.put(Integer.valueOf((int) TAG_CONTROLLER_BOARD_VERSION), "Controller Board Version");
        _tagNameMap.put(Integer.valueOf((int) TAG_M16_C_VERSION), "M16 C Version");
        _tagNameMap.put(Integer.valueOf((int) TAG_IMAGE_ID_NUMBER), "Image ID Number");
    }

    public LeicaMakernoteDirectory() {
        setDescriptor(new LeicaMakernoteDescriptor(this));
    }

    @NotNull
    public String getName() {
        return "Leica Makernote";
    }

    /* access modifiers changed from: protected */
    @NotNull
    public HashMap<Integer, String> getTagNameMap() {
        return _tagNameMap;
    }
}
