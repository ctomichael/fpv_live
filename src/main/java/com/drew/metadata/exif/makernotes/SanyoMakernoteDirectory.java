package com.drew.metadata.exif.makernotes;

import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Directory;
import java.util.HashMap;

public class SanyoMakernoteDirectory extends Directory {
    public static final int TAG_CAMERA_ID = 521;
    public static final int TAG_COLOR_ADJUSTMENT_MODE = 528;
    public static final int TAG_DATA_DUMP = 3840;
    public static final int TAG_DIGITAL_ZOOM = 516;
    public static final int TAG_DIGITAL_ZOOM_ON = 539;
    public static final int TAG_FLASH_MODE = 549;
    public static final int TAG_FLICKER_REDUCE = 536;
    public static final int TAG_LIGHT_SOURCE_SPECIAL = 541;
    public static final int TAG_MACRO = 514;
    public static final int TAG_MAKERNOTE_OFFSET = 255;
    public static final int TAG_MANUAL_FOCUS_DISTANCE_OR_FACE_INFO = 547;
    public static final int TAG_OPTICAL_ZOOM_ON = 537;
    public static final int TAG_PICT_INFO = 520;
    public static final int TAG_PRINT_IMAGE_MATCHING_INFO = 3584;
    public static final int TAG_QUICK_SHOT = 531;
    public static final int TAG_RECORD_SHUTTER_RELEASE = 535;
    public static final int TAG_RESAVED = 542;
    public static final int TAG_SANYO_QUALITY = 513;
    public static final int TAG_SANYO_THUMBNAIL = 256;
    public static final int TAG_SCENE_SELECT = 543;
    public static final int TAG_SELF_TIMER = 532;
    public static final int TAG_SEQUENCE_SHOT_INTERVAL = 548;
    public static final int TAG_SEQUENTIAL_SHOT = 526;
    public static final int TAG_SOFTWARE_VERSION = 519;
    public static final int TAG_SPECIAL_MODE = 512;
    public static final int TAG_VOICE_MEMO = 534;
    public static final int TAG_WIDE_RANGE = 527;
    @NotNull
    protected static final HashMap<Integer, String> _tagNameMap = new HashMap<>();

    static {
        _tagNameMap.put(255, "Makernote Offset");
        _tagNameMap.put(256, "Sanyo Thumbnail");
        _tagNameMap.put(512, "Special Mode");
        _tagNameMap.put(513, "Sanyo Quality");
        _tagNameMap.put(514, "Macro");
        _tagNameMap.put(516, "Digital Zoom");
        _tagNameMap.put(519, "Software Version");
        _tagNameMap.put(520, "Pict Info");
        _tagNameMap.put(521, "Camera ID");
        _tagNameMap.put(526, "Sequential Shot");
        _tagNameMap.put(527, "Wide Range");
        _tagNameMap.put(528, "Color Adjustment Node");
        _tagNameMap.put(531, "Quick Shot");
        _tagNameMap.put(532, "Self Timer");
        _tagNameMap.put(534, "Voice Memo");
        _tagNameMap.put(Integer.valueOf((int) TAG_RECORD_SHUTTER_RELEASE), "Record Shutter Release");
        _tagNameMap.put(Integer.valueOf((int) TAG_FLICKER_REDUCE), "Flicker Reduce");
        _tagNameMap.put(537, "Optical Zoom On");
        _tagNameMap.put(539, "Digital Zoom On");
        _tagNameMap.put(Integer.valueOf((int) TAG_LIGHT_SOURCE_SPECIAL), "Light Source Special");
        _tagNameMap.put(542, "Resaved");
        _tagNameMap.put(Integer.valueOf((int) TAG_SCENE_SELECT), "Scene Select");
        _tagNameMap.put(547, "Manual Focus Distance or Face Info");
        _tagNameMap.put(Integer.valueOf((int) TAG_SEQUENCE_SHOT_INTERVAL), "Sequence Shot Interval");
        _tagNameMap.put(549, "Flash Mode");
        _tagNameMap.put(3584, "Print IM");
        _tagNameMap.put(3840, "Data Dump");
    }

    public SanyoMakernoteDirectory() {
        setDescriptor(new SanyoMakernoteDescriptor(this));
    }

    @NotNull
    public String getName() {
        return "Sanyo Makernote";
    }

    /* access modifiers changed from: protected */
    @NotNull
    public HashMap<Integer, String> getTagNameMap() {
        return _tagNameMap;
    }
}
