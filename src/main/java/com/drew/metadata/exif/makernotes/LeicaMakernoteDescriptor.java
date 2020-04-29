package com.drew.metadata.exif.makernotes;

import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.TagDescriptor;

public class LeicaMakernoteDescriptor extends TagDescriptor<LeicaMakernoteDirectory> {
    public LeicaMakernoteDescriptor(@NotNull LeicaMakernoteDirectory directory) {
        super(directory);
    }

    @Nullable
    public String getDescription(int tagType) {
        switch (tagType) {
            case 768:
                return getQualityDescription();
            case 770:
                return getUserProfileDescription();
            case 772:
                return getWhiteBalanceDescription();
            case 785:
                return getExternalSensorBrightnessValueDescription();
            case LeicaMakernoteDirectory.TAG_MEASURED_LV /*786*/:
                return getMeasuredLvDescription();
            case LeicaMakernoteDirectory.TAG_APPROXIMATE_F_NUMBER /*787*/:
                return getApproximateFNumberDescription();
            case LeicaMakernoteDirectory.TAG_CAMERA_TEMPERATURE /*800*/:
                return getCameraTemperatureDescription();
            case LeicaMakernoteDirectory.TAG_WB_RED_LEVEL /*802*/:
            case LeicaMakernoteDirectory.TAG_WB_GREEN_LEVEL /*803*/:
            case LeicaMakernoteDirectory.TAG_WB_BLUE_LEVEL /*804*/:
                return getSimpleRational(tagType);
            default:
                return super.getDescription(tagType);
        }
    }

    @Nullable
    private String getCameraTemperatureDescription() {
        return getFormattedInt(LeicaMakernoteDirectory.TAG_CAMERA_TEMPERATURE, "%d C");
    }

    @Nullable
    private String getApproximateFNumberDescription() {
        return getSimpleRational(LeicaMakernoteDirectory.TAG_APPROXIMATE_F_NUMBER);
    }

    @Nullable
    private String getMeasuredLvDescription() {
        return getSimpleRational(LeicaMakernoteDirectory.TAG_MEASURED_LV);
    }

    @Nullable
    private String getExternalSensorBrightnessValueDescription() {
        return getSimpleRational(785);
    }

    @Nullable
    private String getWhiteBalanceDescription() {
        return getIndexedDescription(772, "Auto or Manual", "Daylight", "Fluorescent", "Tungsten", "Flash", "Cloudy", "Shadow");
    }

    @Nullable
    private String getUserProfileDescription() {
        return getIndexedDescription(768, 1, "User Profile 1", "User Profile 2", "User Profile 3", "User Profile 0 (Dynamic)");
    }

    @Nullable
    private String getQualityDescription() {
        return getIndexedDescription(768, 1, "Fine", "Basic");
    }
}
