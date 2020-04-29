package com.drew.metadata.exif.makernotes;

import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.TagDescriptor;

public class SigmaMakernoteDescriptor extends TagDescriptor<SigmaMakernoteDirectory> {
    public SigmaMakernoteDescriptor(@NotNull SigmaMakernoteDirectory directory) {
        super(directory);
    }

    public String getDescription(int tagType) {
        switch (tagType) {
            case 8:
                return getExposureModeDescription();
            case 9:
                return getMeteringModeDescription();
            default:
                return super.getDescription(tagType);
        }
    }

    @Nullable
    private String getMeteringModeDescription() {
        String value = ((SigmaMakernoteDirectory) this._directory).getString(9);
        if (value == null || value.length() == 0) {
            return null;
        }
        switch (value.charAt(0)) {
            case '8':
                return "Multi Segment";
            case 'A':
                return "Average";
            case 'C':
                return "Center Weighted Average";
            default:
                return value;
        }
    }

    @Nullable
    private String getExposureModeDescription() {
        String value = ((SigmaMakernoteDirectory) this._directory).getString(8);
        if (value == null || value.length() == 0) {
            return null;
        }
        switch (value.charAt(0)) {
            case 'A':
                return "Aperture Priority AE";
            case 'M':
                return "Manual";
            case 'P':
                return "Program AE";
            case 'S':
                return "Shutter Speed Priority AE";
            default:
                return value;
        }
    }
}
