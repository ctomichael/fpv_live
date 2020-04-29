package com.drew.metadata.exif.makernotes;

import com.amap.location.common.model.AmapLoc;
import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.TagDescriptor;

public class CasioType2MakernoteDescriptor extends TagDescriptor<CasioType2MakernoteDirectory> {
    public CasioType2MakernoteDescriptor(@NotNull CasioType2MakernoteDirectory directory) {
        super(directory);
    }

    @Nullable
    public String getDescription(int tagType) {
        switch (tagType) {
            case 2:
                return getThumbnailDimensionsDescription();
            case 3:
                return getThumbnailSizeDescription();
            case 4:
                return getThumbnailOffsetDescription();
            case 8:
                return getQualityModeDescription();
            case 9:
                return getImageSizeDescription();
            case 13:
                return getFocusMode1Description();
            case 20:
                return getIsoSensitivityDescription();
            case 25:
                return getWhiteBalance1Description();
            case 29:
                return getFocalLengthDescription();
            case 31:
                return getSaturationDescription();
            case 32:
                return getContrastDescription();
            case 33:
                return getSharpnessDescription();
            case 8192:
                return getCasioPreviewThumbnailDescription();
            case 8209:
                return getWhiteBalanceBiasDescription();
            case 8210:
                return getWhiteBalance2Description();
            case 8226:
                return getObjectDistanceDescription();
            case CasioType2MakernoteDirectory.TAG_FLASH_DISTANCE /*8244*/:
                return getFlashDistanceDescription();
            case 12288:
                return getRecordModeDescription();
            case 12289:
                return getSelfTimerDescription();
            case 12290:
                return getQualityDescription();
            case 12291:
                return getFocusMode2Description();
            case CasioType2MakernoteDirectory.TAG_TIME_ZONE /*12294*/:
                return getTimeZoneDescription();
            case CasioType2MakernoteDirectory.TAG_CCD_ISO_SENSITIVITY /*12308*/:
                return getCcdIsoSensitivityDescription();
            case CasioType2MakernoteDirectory.TAG_COLOUR_MODE /*12309*/:
                return getColourModeDescription();
            case CasioType2MakernoteDirectory.TAG_ENHANCEMENT /*12310*/:
                return getEnhancementDescription();
            case CasioType2MakernoteDirectory.TAG_FILTER /*12311*/:
                return getFilterDescription();
            default:
                return super.getDescription(tagType);
        }
    }

    @Nullable
    public String getFilterDescription() {
        return getIndexedDescription(CasioType2MakernoteDirectory.TAG_FILTER, "Off");
    }

    @Nullable
    public String getEnhancementDescription() {
        return getIndexedDescription(CasioType2MakernoteDirectory.TAG_ENHANCEMENT, "Off");
    }

    @Nullable
    public String getColourModeDescription() {
        return getIndexedDescription(CasioType2MakernoteDirectory.TAG_COLOUR_MODE, "Off");
    }

    @Nullable
    public String getCcdIsoSensitivityDescription() {
        return getIndexedDescription(CasioType2MakernoteDirectory.TAG_CCD_ISO_SENSITIVITY, "Off", "On");
    }

    @Nullable
    public String getTimeZoneDescription() {
        return ((CasioType2MakernoteDirectory) this._directory).getString(CasioType2MakernoteDirectory.TAG_TIME_ZONE);
    }

    @Nullable
    public String getFocusMode2Description() {
        Integer value = ((CasioType2MakernoteDirectory) this._directory).getInteger(12291);
        if (value == null) {
            return null;
        }
        switch (value.intValue()) {
            case 1:
                return "Fixation";
            case 6:
                return "Multi-Area Focus";
            default:
                return "Unknown (" + value + ")";
        }
    }

    @Nullable
    public String getQualityDescription() {
        return getIndexedDescription(12290, 3, "Fine");
    }

    @Nullable
    public String getSelfTimerDescription() {
        return getIndexedDescription(12289, 1, "Off");
    }

    @Nullable
    public String getRecordModeDescription() {
        return getIndexedDescription(12288, 2, "Normal");
    }

    @Nullable
    public String getFlashDistanceDescription() {
        return getIndexedDescription(CasioType2MakernoteDirectory.TAG_FLASH_DISTANCE, "Off");
    }

    @Nullable
    public String getObjectDistanceDescription() {
        Integer value = ((CasioType2MakernoteDirectory) this._directory).getInteger(8226);
        if (value == null) {
            return null;
        }
        return Integer.toString(value.intValue()) + " mm";
    }

    @Nullable
    public String getWhiteBalance2Description() {
        Integer value = ((CasioType2MakernoteDirectory) this._directory).getInteger(8210);
        if (value == null) {
            return null;
        }
        switch (value.intValue()) {
            case 0:
                return "Manual";
            case 1:
                return "Auto";
            case 4:
                return "Flash";
            case 12:
                return "Flash";
            default:
                return "Unknown (" + value + ")";
        }
    }

    @Nullable
    public String getWhiteBalanceBiasDescription() {
        return ((CasioType2MakernoteDirectory) this._directory).getString(8209);
    }

    @Nullable
    public String getCasioPreviewThumbnailDescription() {
        byte[] bytes = ((CasioType2MakernoteDirectory) this._directory).getByteArray(8192);
        if (bytes == null) {
            return null;
        }
        return "<" + bytes.length + " bytes of image data>";
    }

    @Nullable
    public String getSharpnessDescription() {
        return getIndexedDescription(33, AmapLoc.RESULT_TYPE_AMAP_INDOOR, "Normal", "+1");
    }

    @Nullable
    public String getContrastDescription() {
        return getIndexedDescription(32, AmapLoc.RESULT_TYPE_AMAP_INDOOR, "Normal", "+1");
    }

    @Nullable
    public String getSaturationDescription() {
        return getIndexedDescription(31, AmapLoc.RESULT_TYPE_AMAP_INDOOR, "Normal", "+1");
    }

    @Nullable
    public String getFocalLengthDescription() {
        Double value = ((CasioType2MakernoteDirectory) this._directory).getDoubleObject(29);
        if (value == null) {
            return null;
        }
        return getFocalLengthDescription(value.doubleValue() / 10.0d);
    }

    @Nullable
    public String getWhiteBalance1Description() {
        return getIndexedDescription(25, "Auto", "Daylight", "Shade", "Tungsten", "Florescent", "Manual");
    }

    @Nullable
    public String getIsoSensitivityDescription() {
        Integer value = ((CasioType2MakernoteDirectory) this._directory).getInteger(20);
        if (value == null) {
            return null;
        }
        switch (value.intValue()) {
            case 3:
                return "50";
            case 4:
                return "64";
            case 5:
            case 7:
            case 8:
            default:
                return "Unknown (" + value + ")";
            case 6:
                return "100";
            case 9:
                return "200";
        }
    }

    @Nullable
    public String getFocusMode1Description() {
        return getIndexedDescription(13, "Normal", "Macro");
    }

    @Nullable
    public String getImageSizeDescription() {
        Integer value = ((CasioType2MakernoteDirectory) this._directory).getInteger(9);
        if (value == null) {
            return null;
        }
        switch (value.intValue()) {
            case 0:
                return "640 x 480 pixels";
            case 4:
                return "1600 x 1200 pixels";
            case 5:
                return "2048 x 1536 pixels";
            case 20:
                return "2288 x 1712 pixels";
            case 21:
                return "2592 x 1944 pixels";
            case 22:
                return "2304 x 1728 pixels";
            case 36:
                return "3008 x 2008 pixels";
            default:
                return "Unknown (" + value + ")";
        }
    }

    @Nullable
    public String getQualityModeDescription() {
        return getIndexedDescription(8, 1, "Fine", "Super Fine");
    }

    @Nullable
    public String getThumbnailOffsetDescription() {
        return ((CasioType2MakernoteDirectory) this._directory).getString(4);
    }

    @Nullable
    public String getThumbnailSizeDescription() {
        Integer value = ((CasioType2MakernoteDirectory) this._directory).getInteger(3);
        if (value == null) {
            return null;
        }
        return Integer.toString(value.intValue()) + " bytes";
    }

    @Nullable
    public String getThumbnailDimensionsDescription() {
        int[] dimensions = ((CasioType2MakernoteDirectory) this._directory).getIntArray(2);
        if (dimensions == null || dimensions.length != 2) {
            return ((CasioType2MakernoteDirectory) this._directory).getString(2);
        }
        return dimensions[0] + " x " + dimensions[1] + " pixels";
    }
}
