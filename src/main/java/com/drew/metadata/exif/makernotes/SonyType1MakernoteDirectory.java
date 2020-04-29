package com.drew.metadata.exif.makernotes;

import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Directory;
import dji.sdksharedlib.keycatalog.CameraKeys;
import java.util.HashMap;

public class SonyType1MakernoteDirectory extends Directory {
    public static final int TAG_AF_ILLUMINATOR = 45124;
    public static final int TAG_AF_MODE = 45123;
    public static final int TAG_AF_POINT_SELECTED = 8222;
    public static final int TAG_ANTI_BLUR = 45131;
    public static final int TAG_AUTO_PORTRAIT_FRAMED = 8214;
    public static final int TAG_BRIGHTNESS = 8199;
    public static final int TAG_CAMERA_INFO = 16;
    public static final int TAG_CAMERA_SETTINGS = 276;
    public static final int TAG_COLOR_COMPENSATION_FILTER = 45090;
    public static final int TAG_COLOR_MODE = 45097;
    public static final int TAG_COLOR_MODE_SETTING = 45088;
    public static final int TAG_COLOR_TEMPERATURE = 45089;
    public static final int TAG_CONTRAST = 8196;
    public static final int TAG_DISTORTION_CORRECTION = 8211;
    public static final int TAG_DYNAMIC_RANGE_OPTIMISER = 45093;
    public static final int TAG_DYNAMIC_RANGE_OPTIMIZER = 45135;
    public static final int TAG_EXPOSURE_MODE = 45121;
    public static final int TAG_EXTRA_INFO = 278;
    public static final int TAG_FILE_FORMAT = 45056;
    public static final int TAG_FLASH_EXPOSURE_COMP = 260;
    public static final int TAG_FLASH_LEVEL = 45128;
    public static final int TAG_FOCUS_INFO = 32;
    public static final int TAG_FOCUS_MODE = 8219;
    public static final int TAG_FOCUS_MODE_2 = 45122;
    public static final int TAG_FULL_IMAGE_SIZE = 45099;
    public static final int TAG_HDR = 8202;
    public static final int TAG_HIGH_ISO_NOISE_REDUCTION = 8201;
    public static final int TAG_HIGH_ISO_NOISE_REDUCTION_2 = 45136;
    public static final int TAG_IMAGE_QUALITY = 258;
    public static final int TAG_IMAGE_STABILISATION = 45094;
    public static final int TAG_INTELLIGENT_AUTO = 45138;
    public static final int TAG_JPEG_QUALITY = 45127;
    public static final int TAG_LATERAL_CHROMATIC_ABERRATION = 8210;
    public static final int TAG_LENS_ID = 45095;
    public static final int TAG_LENS_SPEC = 45098;
    public static final int TAG_LONG_EXPOSURE_NOISE_REDUCTION = 8200;
    public static final int TAG_LONG_EXPOSURE_NOISE_REDUCTION_OR_FOCUS_MODE = 45134;
    public static final int TAG_MACRO = 45120;
    public static final int TAG_MINOLTA_MAKERNOTE = 45096;
    public static final int TAG_MULTI_BURST_IMAGE_HEIGHT = 4098;
    public static final int TAG_MULTI_BURST_IMAGE_WIDTH = 4097;
    public static final int TAG_MULTI_BURST_MODE = 4096;
    public static final int TAG_MULTI_FRAME_NOISE_REDUCTION = 8203;
    public static final int TAG_NO_PRINT = 65535;
    public static final int TAG_PANORAMA = 4099;
    public static final int TAG_PICTURE_EFFECT = 8206;
    public static final int TAG_PREVIEW_IMAGE = 8193;
    public static final int TAG_PREVIEW_IMAGE_SIZE = 45100;
    public static final int TAG_PRINT_IMAGE_MATCHING_INFO = 3584;
    public static final int TAG_RATING = 8194;
    public static final int TAG_RELEASE_MODE = 45129;
    public static final int TAG_SATURATION = 8197;
    public static final int TAG_SCENE_MODE = 45091;
    public static final int TAG_SEQUENCE_NUMBER = 45130;
    public static final int TAG_SHARPNESS = 8198;
    public static final int TAG_SHOT_INFO = 12288;
    public static final int TAG_SOFT_SKIN_EFFECT = 8207;
    public static final int TAG_SONY_MODEL_ID = 45057;
    public static final int TAG_TELECONVERTER = 261;
    public static final int TAG_VIGNETTING_CORRECTION = 8209;
    public static final int TAG_WB_SHIFT_AMBER_MAGENTA = 8212;
    public static final int TAG_WHITE_BALANCE = 277;
    public static final int TAG_WHITE_BALANCE_2 = 45140;
    public static final int TAG_WHITE_BALANCE_FINE_TUNE = 274;
    public static final int TAG_ZONE_MATCHING = 45092;
    @NotNull
    protected static final HashMap<Integer, String> _tagNameMap = new HashMap<>();

    static {
        _tagNameMap.put(16, "Camera Info");
        _tagNameMap.put(32, "Focus Info");
        _tagNameMap.put(258, "Image Quality");
        _tagNameMap.put(260, "Flash Exposure Compensation");
        _tagNameMap.put(261, "Teleconverter Model");
        _tagNameMap.put(274, "White Balance Fine Tune Value");
        _tagNameMap.put(276, "Camera Settings");
        _tagNameMap.put(277, "White Balance");
        _tagNameMap.put(278, "Extra Info");
        _tagNameMap.put(3584, "Print Image Matching (PIM) Info");
        _tagNameMap.put(4096, "Multi Burst Mode");
        _tagNameMap.put(4097, "Multi Burst Image Width");
        _tagNameMap.put(4098, "Multi Burst Image Height");
        _tagNameMap.put(4099, "Panorama");
        _tagNameMap.put(8193, "Preview Image");
        _tagNameMap.put(8194, "Rating");
        _tagNameMap.put(Integer.valueOf((int) TAG_CONTRAST), CameraKeys.CONTRAST);
        _tagNameMap.put(Integer.valueOf((int) TAG_SATURATION), CameraKeys.SATURATION);
        _tagNameMap.put(Integer.valueOf((int) TAG_SHARPNESS), CameraKeys.SHARPNESS);
        _tagNameMap.put(Integer.valueOf((int) TAG_BRIGHTNESS), "Brightness");
        _tagNameMap.put(Integer.valueOf((int) TAG_LONG_EXPOSURE_NOISE_REDUCTION), "Long Exposure Noise Reduction");
        _tagNameMap.put(Integer.valueOf((int) TAG_HIGH_ISO_NOISE_REDUCTION), "High ISO Noise Reduction");
        _tagNameMap.put(Integer.valueOf((int) TAG_HDR), "HDR");
        _tagNameMap.put(Integer.valueOf((int) TAG_MULTI_FRAME_NOISE_REDUCTION), "Multi Frame Noise Reduction");
        _tagNameMap.put(Integer.valueOf((int) TAG_PICTURE_EFFECT), "Picture Effect");
        _tagNameMap.put(Integer.valueOf((int) TAG_SOFT_SKIN_EFFECT), "Soft Skin Effect");
        _tagNameMap.put(8209, "Vignetting Correction");
        _tagNameMap.put(8210, "Lateral Chromatic Aberration");
        _tagNameMap.put(Integer.valueOf((int) TAG_DISTORTION_CORRECTION), "Distortion Correction");
        _tagNameMap.put(Integer.valueOf((int) TAG_WB_SHIFT_AMBER_MAGENTA), "WB Shift Amber/Magenta");
        _tagNameMap.put(Integer.valueOf((int) TAG_AUTO_PORTRAIT_FRAMED), "Auto Portrait Framing");
        _tagNameMap.put(Integer.valueOf((int) TAG_FOCUS_MODE), "Focus Mode");
        _tagNameMap.put(Integer.valueOf((int) TAG_AF_POINT_SELECTED), "AF Point Selected");
        _tagNameMap.put(12288, "Shot Info");
        _tagNameMap.put(Integer.valueOf((int) TAG_FILE_FORMAT), "File Format");
        _tagNameMap.put(Integer.valueOf((int) TAG_SONY_MODEL_ID), "Sony Model ID");
        _tagNameMap.put(Integer.valueOf((int) TAG_COLOR_MODE_SETTING), "Color Mode Setting");
        _tagNameMap.put(Integer.valueOf((int) TAG_COLOR_TEMPERATURE), "Color Temperature");
        _tagNameMap.put(Integer.valueOf((int) TAG_COLOR_COMPENSATION_FILTER), "Color Compensation Filter");
        _tagNameMap.put(Integer.valueOf((int) TAG_SCENE_MODE), "Scene Mode");
        _tagNameMap.put(Integer.valueOf((int) TAG_ZONE_MATCHING), "Zone Matching");
        _tagNameMap.put(Integer.valueOf((int) TAG_DYNAMIC_RANGE_OPTIMISER), "Dynamic Range Optimizer");
        _tagNameMap.put(Integer.valueOf((int) TAG_IMAGE_STABILISATION), "Image Stabilisation");
        _tagNameMap.put(Integer.valueOf((int) TAG_LENS_ID), "Lens ID");
        _tagNameMap.put(Integer.valueOf((int) TAG_MINOLTA_MAKERNOTE), "Minolta Makernote");
        _tagNameMap.put(Integer.valueOf((int) TAG_COLOR_MODE), "Color Mode");
        _tagNameMap.put(Integer.valueOf((int) TAG_LENS_SPEC), "Lens Spec");
        _tagNameMap.put(Integer.valueOf((int) TAG_FULL_IMAGE_SIZE), "Full Image Size");
        _tagNameMap.put(Integer.valueOf((int) TAG_PREVIEW_IMAGE_SIZE), "Preview Image Size");
        _tagNameMap.put(Integer.valueOf((int) TAG_MACRO), "Macro");
        _tagNameMap.put(Integer.valueOf((int) TAG_EXPOSURE_MODE), "Exposure Mode");
        _tagNameMap.put(Integer.valueOf((int) TAG_FOCUS_MODE_2), "Focus Mode");
        _tagNameMap.put(Integer.valueOf((int) TAG_AF_MODE), "AF Mode");
        _tagNameMap.put(Integer.valueOf((int) TAG_AF_ILLUMINATOR), "AF Illuminator");
        _tagNameMap.put(Integer.valueOf((int) TAG_JPEG_QUALITY), "Quality");
        _tagNameMap.put(Integer.valueOf((int) TAG_FLASH_LEVEL), "Flash Level");
        _tagNameMap.put(Integer.valueOf((int) TAG_RELEASE_MODE), "Release Mode");
        _tagNameMap.put(Integer.valueOf((int) TAG_SEQUENCE_NUMBER), "Sequence Number");
        _tagNameMap.put(Integer.valueOf((int) TAG_ANTI_BLUR), "Anti Blur");
        _tagNameMap.put(Integer.valueOf((int) TAG_LONG_EXPOSURE_NOISE_REDUCTION_OR_FOCUS_MODE), "Long Exposure Noise Reduction");
        _tagNameMap.put(Integer.valueOf((int) TAG_DYNAMIC_RANGE_OPTIMIZER), "Dynamic Range Optimizer");
        _tagNameMap.put(Integer.valueOf((int) TAG_HIGH_ISO_NOISE_REDUCTION_2), "High ISO Noise Reduction");
        _tagNameMap.put(Integer.valueOf((int) TAG_INTELLIGENT_AUTO), "Intelligent Auto");
        _tagNameMap.put(Integer.valueOf((int) TAG_WHITE_BALANCE_2), "White Balance 2");
        _tagNameMap.put(65535, "No Print");
    }

    public SonyType1MakernoteDirectory() {
        setDescriptor(new SonyType1MakernoteDescriptor(this));
    }

    @NotNull
    public String getName() {
        return "Sony Makernote";
    }

    /* access modifiers changed from: protected */
    @NotNull
    public HashMap<Integer, String> getTagNameMap() {
        return _tagNameMap;
    }
}
