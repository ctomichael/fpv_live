package com.drew.metadata.exif.makernotes;

import com.drew.lang.Rational;
import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.TagDescriptor;
import dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction;

public class FujifilmMakernoteDescriptor extends TagDescriptor<FujifilmMakernoteDirectory> {
    public FujifilmMakernoteDescriptor(@NotNull FujifilmMakernoteDirectory directory) {
        super(directory);
    }

    @Nullable
    public String getDescription(int tagType) {
        switch (tagType) {
            case 0:
                return getMakernoteVersionDescription();
            case 4097:
                return getSharpnessDescription();
            case 4098:
                return getWhiteBalanceDescription();
            case 4099:
                return getColorSaturationDescription();
            case 4100:
                return getToneDescription();
            case 4102:
                return getContrastDescription();
            case 4107:
                return getNoiseReductionDescription();
            case 4110:
                return getHighIsoNoiseReductionDescription();
            case 4112:
                return getFlashModeDescription();
            case 4113:
                return getFlashExposureValueDescription();
            case 4128:
                return getMacroDescription();
            case 4129:
                return getFocusModeDescription();
            case 4144:
                return getSlowSyncDescription();
            case 4145:
                return getPictureModeDescription();
            case 4147:
                return getExrAutoDescription();
            case 4148:
                return getExrModeDescription();
            case FujifilmMakernoteDirectory.TAG_AUTO_BRACKETING /*4352*/:
                return getAutoBracketingDescription();
            case FujifilmMakernoteDirectory.TAG_FINE_PIX_COLOR /*4624*/:
                return getFinePixColorDescription();
            case FujifilmMakernoteDirectory.TAG_BLUR_WARNING /*4864*/:
                return getBlurWarningDescription();
            case FujifilmMakernoteDirectory.TAG_FOCUS_WARNING /*4865*/:
                return getFocusWarningDescription();
            case FujifilmMakernoteDirectory.TAG_AUTO_EXPOSURE_WARNING /*4866*/:
                return getAutoExposureWarningDescription();
            case FujifilmMakernoteDirectory.TAG_DYNAMIC_RANGE /*5120*/:
                return getDynamicRangeDescription();
            case FujifilmMakernoteDirectory.TAG_FILM_MODE /*5121*/:
                return getFilmModeDescription();
            case FujifilmMakernoteDirectory.TAG_DYNAMIC_RANGE_SETTING /*5122*/:
                return getDynamicRangeSettingDescription();
            default:
                return super.getDescription(tagType);
        }
    }

    @Nullable
    private String getMakernoteVersionDescription() {
        return getVersionBytesDescription(0, 2);
    }

    @Nullable
    public String getSharpnessDescription() {
        Integer value = ((FujifilmMakernoteDirectory) this._directory).getInteger(4097);
        if (value == null) {
            return null;
        }
        switch (value.intValue()) {
            case 1:
                return "Softest";
            case 2:
                return "Soft";
            case 3:
                return "Normal";
            case 4:
                return "Hard";
            case 5:
                return "Hardest";
            case NikonType2MakernoteDirectory.TAG_ADAPTER /*130*/:
                return "Medium Soft";
            case 132:
                return "Medium Hard";
            case 32768:
                return "Film Simulation";
            case 65535:
                return DJIRCAbstraction.DEFAULT_FIRMWARE_VERSION;
            default:
                return "Unknown (" + value + ")";
        }
    }

    @Nullable
    public String getWhiteBalanceDescription() {
        Integer value = ((FujifilmMakernoteDirectory) this._directory).getInteger(4098);
        if (value == null) {
            return null;
        }
        switch (value.intValue()) {
            case 0:
                return "Auto";
            case 256:
                return "Daylight";
            case 512:
                return "Cloudy";
            case 768:
                return "Daylight Fluorescent";
            case 769:
                return "Day White Fluorescent";
            case 770:
                return "White Fluorescent";
            case 771:
                return "Warm White Fluorescent";
            case 772:
                return "Living Room Warm White Fluorescent";
            case 1024:
                return "Incandescence";
            case 1280:
                return "Flash";
            case 3840:
                return "Custom White Balance";
            case OlympusMakernoteDirectory.TAG_DATA_DUMP_2 /*3841*/:
                return "Custom White Balance 2";
            case 3842:
                return "Custom White Balance 3";
            case 3843:
                return "Custom White Balance 4";
            case 3844:
                return "Custom White Balance 5";
            case 4080:
                return "Kelvin";
            default:
                return "Unknown (" + value + ")";
        }
    }

    @Nullable
    public String getColorSaturationDescription() {
        Integer value = ((FujifilmMakernoteDirectory) this._directory).getInteger(4099);
        if (value == null) {
            return null;
        }
        switch (value.intValue()) {
            case 0:
                return "Normal";
            case 128:
                return "Medium High";
            case 256:
                return "High";
            case 384:
                return "Medium Low";
            case 512:
                return "Low";
            case 768:
                return "None (B&W)";
            case 769:
                return "B&W Green Filter";
            case 770:
                return "B&W Yellow Filter";
            case 771:
                return "B&W Blue Filter";
            case 772:
                return "B&W Sepia";
            case 32768:
                return "Film Simulation";
            default:
                return "Unknown (" + value + ")";
        }
    }

    @Nullable
    public String getToneDescription() {
        Integer value = ((FujifilmMakernoteDirectory) this._directory).getInteger(4100);
        if (value == null) {
            return null;
        }
        switch (value.intValue()) {
            case 0:
                return "Normal";
            case 128:
                return "Medium High";
            case 256:
                return "High";
            case 384:
                return "Medium Low";
            case 512:
                return "Low";
            case 768:
                return "None (B&W)";
            case 32768:
                return "Film Simulation";
            default:
                return "Unknown (" + value + ")";
        }
    }

    @Nullable
    public String getContrastDescription() {
        Integer value = ((FujifilmMakernoteDirectory) this._directory).getInteger(4102);
        if (value == null) {
            return null;
        }
        switch (value.intValue()) {
            case 0:
                return "Normal";
            case 256:
                return "High";
            case 768:
                return "Low";
            default:
                return "Unknown (" + value + ")";
        }
    }

    @Nullable
    public String getNoiseReductionDescription() {
        Integer value = ((FujifilmMakernoteDirectory) this._directory).getInteger(4107);
        if (value == null) {
            return null;
        }
        switch (value.intValue()) {
            case 64:
                return "Low";
            case 128:
                return "Normal";
            case 256:
                return DJIRCAbstraction.DEFAULT_FIRMWARE_VERSION;
            default:
                return "Unknown (" + value + ")";
        }
    }

    @Nullable
    public String getHighIsoNoiseReductionDescription() {
        Integer value = ((FujifilmMakernoteDirectory) this._directory).getInteger(4110);
        if (value == null) {
            return null;
        }
        switch (value.intValue()) {
            case 0:
                return "Normal";
            case 256:
                return "Strong";
            case 512:
                return "Weak";
            default:
                return "Unknown (" + value + ")";
        }
    }

    @Nullable
    public String getFlashModeDescription() {
        return getIndexedDescription(4112, "Auto", "On", "Off", "Red-eye Reduction", "External");
    }

    @Nullable
    public String getFlashExposureValueDescription() {
        Rational value = ((FujifilmMakernoteDirectory) this._directory).getRational(4113);
        if (value == null) {
            return null;
        }
        return value.toSimpleString(false) + " EV (Apex)";
    }

    @Nullable
    public String getMacroDescription() {
        return getIndexedDescription(4128, "Off", "On");
    }

    @Nullable
    public String getFocusModeDescription() {
        return getIndexedDescription(4129, "Auto Focus", "Manual Focus");
    }

    @Nullable
    public String getSlowSyncDescription() {
        return getIndexedDescription(4144, "Off", "On");
    }

    @Nullable
    public String getPictureModeDescription() {
        Integer value = ((FujifilmMakernoteDirectory) this._directory).getInteger(4145);
        if (value == null) {
            return null;
        }
        switch (value.intValue()) {
            case 0:
                return "Auto";
            case 1:
                return "Portrait scene";
            case 2:
                return "Landscape scene";
            case 3:
                return "Macro";
            case 4:
                return "Sports scene";
            case 5:
                return "Night scene";
            case 6:
                return "Program AE";
            case 7:
                return "Natural Light";
            case 8:
                return "Anti-blur";
            case 9:
                return "Beach & Snow";
            case 10:
                return "Sunset";
            case 11:
                return "Museum";
            case 12:
                return "Party";
            case 13:
                return "Flower";
            case 14:
                return "Text";
            case 15:
                return "Natural Light & Flash";
            case 16:
                return "Beach";
            case 17:
                return "Snow";
            case 18:
                return "Fireworks";
            case 19:
                return "Underwater";
            case 20:
                return "Portrait with Skin Correction";
            case 22:
                return "Panorama";
            case 23:
                return "Night (Tripod)";
            case 24:
                return "Pro Low-light";
            case 25:
                return "Pro Focus";
            case 27:
                return "Dog Face Detection";
            case 28:
                return "Cat Face Detection";
            case 256:
                return "Aperture priority AE";
            case 512:
                return "Shutter priority AE";
            case 768:
                return "Manual exposure";
            default:
                return "Unknown (" + value + ")";
        }
    }

    @Nullable
    public String getExrAutoDescription() {
        return getIndexedDescription(4147, "Auto", "Manual");
    }

    @Nullable
    public String getExrModeDescription() {
        Integer value = ((FujifilmMakernoteDirectory) this._directory).getInteger(4148);
        if (value == null) {
            return null;
        }
        switch (value.intValue()) {
            case 256:
                return "HR (High Resolution)";
            case 512:
                return "SN (Signal to Noise Priority)";
            case 768:
                return "DR (Dynamic Range Priority)";
            default:
                return "Unknown (" + value + ")";
        }
    }

    @Nullable
    public String getAutoBracketingDescription() {
        return getIndexedDescription(FujifilmMakernoteDirectory.TAG_AUTO_BRACKETING, "Off", "On", "No Flash & Flash");
    }

    @Nullable
    public String getFinePixColorDescription() {
        Integer value = ((FujifilmMakernoteDirectory) this._directory).getInteger(FujifilmMakernoteDirectory.TAG_FINE_PIX_COLOR);
        if (value == null) {
            return null;
        }
        switch (value.intValue()) {
            case 0:
                return "Standard";
            case 16:
                return "Chrome";
            case 48:
                return "B&W";
            default:
                return "Unknown (" + value + ")";
        }
    }

    @Nullable
    public String getBlurWarningDescription() {
        return getIndexedDescription(FujifilmMakernoteDirectory.TAG_BLUR_WARNING, "No Blur Warning", "Blur warning");
    }

    @Nullable
    public String getFocusWarningDescription() {
        return getIndexedDescription(FujifilmMakernoteDirectory.TAG_FOCUS_WARNING, "Good Focus", "Out Of Focus");
    }

    @Nullable
    public String getAutoExposureWarningDescription() {
        return getIndexedDescription(FujifilmMakernoteDirectory.TAG_AUTO_EXPOSURE_WARNING, "AE Good", "Over Exposed");
    }

    @Nullable
    public String getDynamicRangeDescription() {
        return getIndexedDescription(FujifilmMakernoteDirectory.TAG_DYNAMIC_RANGE, 1, "Standard", null, "Wide");
    }

    @Nullable
    public String getFilmModeDescription() {
        Integer value = ((FujifilmMakernoteDirectory) this._directory).getInteger(FujifilmMakernoteDirectory.TAG_FILM_MODE);
        if (value == null) {
            return null;
        }
        switch (value.intValue()) {
            case 0:
                return "F0/Standard (Provia) ";
            case 256:
                return "F1/Studio Portrait";
            case 272:
                return "F1a/Studio Portrait Enhanced Saturation";
            case 288:
                return "F1b/Studio Portrait Smooth Skin Tone (Astia)";
            case OlympusRawInfoMakernoteDirectory.TagWbRbLevelsDaylightFluor /*304*/:
                return "F1c/Studio Portrait Increased Sharpness";
            case 512:
                return "F2/Fujichrome (Velvia)";
            case 768:
                return "F3/Studio Portrait Ex";
            case 1024:
                return "F4/Velvia";
            case 1280:
                return "Pro Neg. Std";
            case OlympusCameraSettingsMakernoteDirectory.TagWhiteBalanceTemperature /*1281*/:
                return "Pro Neg. Hi";
            default:
                return "Unknown (" + value + ")";
        }
    }

    @Nullable
    public String getDynamicRangeSettingDescription() {
        Integer value = ((FujifilmMakernoteDirectory) this._directory).getInteger(FujifilmMakernoteDirectory.TAG_DYNAMIC_RANGE_SETTING);
        if (value == null) {
            return null;
        }
        switch (value.intValue()) {
            case 0:
                return "Auto (100-400%)";
            case 1:
                return "Manual";
            case 256:
                return "Standard (100%)";
            case 512:
                return "Wide 1 (230%)";
            case 513:
                return "Wide 2 (400%)";
            case 32768:
                return "Film Simulation";
            default:
                return "Unknown (" + value + ")";
        }
    }
}
