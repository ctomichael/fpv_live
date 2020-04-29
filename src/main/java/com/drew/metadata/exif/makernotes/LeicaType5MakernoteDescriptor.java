package com.drew.metadata.exif.makernotes;

import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.TagDescriptor;

public class LeicaType5MakernoteDescriptor extends TagDescriptor<LeicaType5MakernoteDirectory> {
    public LeicaType5MakernoteDescriptor(@NotNull LeicaType5MakernoteDirectory directory) {
        super(directory);
    }

    @Nullable
    public String getDescription(int tagType) {
        switch (tagType) {
            case 1037:
                return getExposureModeDescription();
            default:
                return super.getDescription(tagType);
        }
    }

    @Nullable
    public String getExposureModeDescription() {
        byte[] values = ((LeicaType5MakernoteDirectory) this._directory).getByteArray(1037);
        if (values == null || values.length < 4) {
            return null;
        }
        String join = String.format("%d %d %d %d", Byte.valueOf(values[0]), Byte.valueOf(values[1]), Byte.valueOf(values[2]), Byte.valueOf(values[3]));
        if (join.equals("0 0 0 0")) {
            return "Program AE";
        }
        if (join.equals("1 0 0 0")) {
            return "Aperture-priority AE";
        }
        if (join.equals("1 1 0 0")) {
            return "Aperture-priority AE (1)";
        }
        if (join.equals("2 0 0 0")) {
            return "Shutter speed priority AE";
        }
        if (join.equals("3 0 0 0")) {
            return "Manual";
        }
        return String.format("Unknown (%s)", join);
    }
}
