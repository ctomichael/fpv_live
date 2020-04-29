package com.drew.metadata.exif.makernotes;

import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Directory;
import java.util.HashMap;

public class OlympusCameraSettingsMakernoteDirectory extends Directory {
    public static final int TagAeLock = 513;
    public static final int TagAfAreas = 772;
    public static final int TagAfFineTune = 774;
    public static final int TagAfFineTuneAdj = 775;
    public static final int TagAfPointSelected = 773;
    public static final int TagAfSearch = 771;
    public static final int TagArtFilter = 1321;
    public static final int TagArtFilterEffect = 1327;
    public static final int TagCameraSettingsVersion = 0;
    public static final int TagColorCreatorEffect = 1330;
    public static final int TagColorSpace = 1287;
    public static final int TagCompressionFactor = 1293;
    public static final int TagContrastSetting = 1285;
    public static final int TagCustomSaturation = 1283;
    public static final int TagDateTimeUtc = 2312;
    public static final int TagDistortionCorrection = 1291;
    public static final int TagDriveMode = 1536;
    public static final int TagExposureMode = 512;
    public static final int TagExposureShift = 515;
    public static final int TagExtendedWBDetect = 2306;
    public static final int TagFlashControlMode = 1028;
    public static final int TagFlashExposureComp = 1025;
    public static final int TagFlashIntensity = 1029;
    public static final int TagFlashMode = 1024;
    public static final int TagFlashRemoteControl = 1027;
    public static final int TagFocusMode = 769;
    public static final int TagFocusProcess = 770;
    public static final int TagGradation = 1295;
    public static final int TagImageQuality2 = 1539;
    public static final int TagImageStabilization = 1540;
    public static final int TagMacroMode = 768;
    public static final int TagMagicFilter = 1324;
    public static final int TagManometerPressure = 2304;
    public static final int TagManometerReading = 2305;
    public static final int TagManualFlashStrength = 1030;
    public static final int TagMeteringMode = 514;
    public static final int TagModifiedSaturation = 1284;
    public static final int TagNdFilter = 516;
    public static final int TagNoiseFilter = 1319;
    public static final int TagNoiseReduction = 1290;
    public static final int TagPanoramaMode = 1537;
    public static final int TagPictureMode = 1312;
    public static final int TagPictureModeBWFilter = 1317;
    public static final int TagPictureModeContrast = 1315;
    public static final int TagPictureModeEffect = 1325;
    public static final int TagPictureModeHue = 1314;
    public static final int TagPictureModeSaturation = 1313;
    public static final int TagPictureModeSharpness = 1316;
    public static final int TagPictureModeTone = 1318;
    public static final int TagPitchAngle = 2308;
    public static final int TagPreviewImageLength = 258;
    public static final int TagPreviewImageStart = 257;
    public static final int TagPreviewImageValid = 256;
    public static final int TagRollAngle = 2307;
    public static final int TagSceneMode = 1289;
    public static final int TagShadingCompensation = 1292;
    public static final int TagSharpnessSetting = 1286;
    public static final int TagStackedImage = 2052;
    public static final int TagToneLevel = 1326;
    public static final int TagWhiteBalance2 = 1280;
    public static final int TagWhiteBalanceBracket = 1282;
    public static final int TagWhiteBalanceTemperature = 1281;
    @NotNull
    protected static final HashMap<Integer, String> _tagNameMap = new HashMap<>();

    static {
        _tagNameMap.put(0, "Camera Settings Version");
        _tagNameMap.put(256, "Preview Image Valid");
        _tagNameMap.put(257, "Preview Image Start");
        _tagNameMap.put(258, "Preview Image Length");
        _tagNameMap.put(512, "Exposure Mode");
        _tagNameMap.put(513, "AE Lock");
        _tagNameMap.put(514, "Metering Mode");
        _tagNameMap.put(515, "Exposure Shift");
        _tagNameMap.put(516, "ND Filter");
        _tagNameMap.put(768, "Macro Mode");
        _tagNameMap.put(769, "Focus Mode");
        _tagNameMap.put(770, "Focus Process");
        _tagNameMap.put(771, "AF Search");
        _tagNameMap.put(772, "AF Areas");
        _tagNameMap.put(773, "AF Point Selected");
        _tagNameMap.put(Integer.valueOf((int) TagAfFineTune), "AF Fine Tune");
        _tagNameMap.put(Integer.valueOf((int) TagAfFineTuneAdj), "AF Fine Tune Adj");
        _tagNameMap.put(1024, "Flash Mode");
        _tagNameMap.put(1025, "Flash Exposure Comp");
        _tagNameMap.put(1027, "Flash Remote Control");
        _tagNameMap.put(1028, "Flash Control Mode");
        _tagNameMap.put(1029, "Flash Intensity");
        _tagNameMap.put(1030, "Manual Flash Strength");
        _tagNameMap.put(1280, "White Balance 2");
        _tagNameMap.put(Integer.valueOf((int) TagWhiteBalanceTemperature), "White Balance Temperature");
        _tagNameMap.put(Integer.valueOf((int) TagWhiteBalanceBracket), "White Balance Bracket");
        _tagNameMap.put(Integer.valueOf((int) TagCustomSaturation), "Custom Saturation");
        _tagNameMap.put(Integer.valueOf((int) TagModifiedSaturation), "Modified Saturation");
        _tagNameMap.put(Integer.valueOf((int) TagContrastSetting), "Contrast Setting");
        _tagNameMap.put(Integer.valueOf((int) TagSharpnessSetting), "Sharpness Setting");
        _tagNameMap.put(Integer.valueOf((int) TagColorSpace), "Color Space");
        _tagNameMap.put(Integer.valueOf((int) TagSceneMode), "Scene Mode");
        _tagNameMap.put(Integer.valueOf((int) TagNoiseReduction), "Noise Reduction");
        _tagNameMap.put(Integer.valueOf((int) TagDistortionCorrection), "Distortion Correction");
        _tagNameMap.put(Integer.valueOf((int) TagShadingCompensation), "Shading Compensation");
        _tagNameMap.put(Integer.valueOf((int) TagCompressionFactor), "Compression Factor");
        _tagNameMap.put(Integer.valueOf((int) TagGradation), "Gradation");
        _tagNameMap.put(Integer.valueOf((int) TagPictureMode), "Picture Mode");
        _tagNameMap.put(Integer.valueOf((int) TagPictureModeSaturation), "Picture Mode Saturation");
        _tagNameMap.put(Integer.valueOf((int) TagPictureModeHue), "Picture Mode Hue");
        _tagNameMap.put(Integer.valueOf((int) TagPictureModeContrast), "Picture Mode Contrast");
        _tagNameMap.put(Integer.valueOf((int) TagPictureModeSharpness), "Picture Mode Sharpness");
        _tagNameMap.put(Integer.valueOf((int) TagPictureModeBWFilter), "Picture Mode BW Filter");
        _tagNameMap.put(Integer.valueOf((int) TagPictureModeTone), "Picture Mode Tone");
        _tagNameMap.put(Integer.valueOf((int) TagNoiseFilter), "Noise Filter");
        _tagNameMap.put(Integer.valueOf((int) TagArtFilter), "Art Filter");
        _tagNameMap.put(Integer.valueOf((int) TagMagicFilter), "Magic Filter");
        _tagNameMap.put(Integer.valueOf((int) TagPictureModeEffect), "Picture Mode Effect");
        _tagNameMap.put(Integer.valueOf((int) TagToneLevel), "Tone Level");
        _tagNameMap.put(Integer.valueOf((int) TagArtFilterEffect), "Art Filter Effect");
        _tagNameMap.put(Integer.valueOf((int) TagColorCreatorEffect), "Color Creator Effect");
        _tagNameMap.put(1536, "Drive Mode");
        _tagNameMap.put(1537, "Panorama Mode");
        _tagNameMap.put(Integer.valueOf((int) TagImageQuality2), "Image Quality 2");
        _tagNameMap.put(Integer.valueOf((int) TagImageStabilization), "Image Stabilization");
        _tagNameMap.put(Integer.valueOf((int) TagStackedImage), "Stacked Image");
        _tagNameMap.put(Integer.valueOf((int) TagManometerPressure), "Manometer Pressure");
        _tagNameMap.put(Integer.valueOf((int) TagManometerReading), "Manometer Reading");
        _tagNameMap.put(Integer.valueOf((int) TagExtendedWBDetect), "Extended WB Detect");
        _tagNameMap.put(Integer.valueOf((int) TagRollAngle), "Roll Angle");
        _tagNameMap.put(Integer.valueOf((int) TagPitchAngle), "Pitch Angle");
        _tagNameMap.put(Integer.valueOf((int) TagDateTimeUtc), "Date Time UTC");
    }

    public OlympusCameraSettingsMakernoteDirectory() {
        setDescriptor(new OlympusCameraSettingsMakernoteDescriptor(this));
    }

    @NotNull
    public String getName() {
        return "Olympus Camera Settings";
    }

    /* access modifiers changed from: protected */
    @NotNull
    public HashMap<Integer, String> getTagNameMap() {
        return _tagNameMap;
    }
}
