package com.drew.metadata.exif.makernotes;

import com.drew.lang.Rational;
import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.TagDescriptor;
import org.bouncycastle.asn1.cmc.BodyPartID;

public class OlympusFocusInfoMakernoteDescriptor extends TagDescriptor<OlympusFocusInfoMakernoteDirectory> {
    public OlympusFocusInfoMakernoteDescriptor(@NotNull OlympusFocusInfoMakernoteDirectory directory) {
        super(directory);
    }

    @Nullable
    public String getDescription(int tagType) {
        switch (tagType) {
            case 0:
                return getFocusInfoVersionDescription();
            case 521:
                return getAutoFocusDescription();
            case 773:
                return getFocusDistanceDescription();
            case OlympusFocusInfoMakernoteDirectory.TagAfPoint /*776*/:
                return getAfPointDescription();
            case 4609:
                return getExternalFlashDescription();
            case OlympusFocusInfoMakernoteDirectory.TagExternalFlashBounce /*4612*/:
                return getExternalFlashBounceDescription();
            case OlympusFocusInfoMakernoteDirectory.TagExternalFlashZoom /*4613*/:
                return getExternalFlashZoomDescription();
            case OlympusFocusInfoMakernoteDirectory.TagManualFlash /*4617*/:
                return getManualFlashDescription();
            case OlympusFocusInfoMakernoteDirectory.TagMacroLed /*4618*/:
                return getMacroLedDescription();
            case OlympusFocusInfoMakernoteDirectory.TagSensorTemperature /*5376*/:
                return getSensorTemperatureDescription();
            case OlympusFocusInfoMakernoteDirectory.TagImageStabilization /*5632*/:
                return getImageStabilizationDescription();
            default:
                return super.getDescription(tagType);
        }
    }

    @Nullable
    public String getFocusInfoVersionDescription() {
        return getVersionBytesDescription(0, 4);
    }

    @Nullable
    public String getAutoFocusDescription() {
        return getIndexedDescription(521, "Off", "On");
    }

    @Nullable
    public String getFocusDistanceDescription() {
        Rational value = ((OlympusFocusInfoMakernoteDirectory) this._directory).getRational(773);
        if (value == null || value.getNumerator() == BodyPartID.bodyIdMax || value.getNumerator() == 0) {
            return "inf";
        }
        return (((double) value.getNumerator()) / 1000.0d) + " m";
    }

    @Nullable
    public String getAfPointDescription() {
        Integer value = ((OlympusFocusInfoMakernoteDirectory) this._directory).getInteger(OlympusFocusInfoMakernoteDirectory.TagAfPoint);
        if (value == null) {
            return null;
        }
        return value.toString();
    }

    @Nullable
    public String getExternalFlashDescription() {
        int[] values = ((OlympusFocusInfoMakernoteDirectory) this._directory).getIntArray(4609);
        if (values == null || values.length < 2) {
            return null;
        }
        String join = String.format("%d %d", Short.valueOf((short) values[0]), Short.valueOf((short) values[1]));
        if (join.equals("0 0")) {
            return "Off";
        }
        if (join.equals("1 0")) {
            return "On";
        }
        return "Unknown (" + join + ")";
    }

    @Nullable
    public String getExternalFlashBounceDescription() {
        return getIndexedDescription(OlympusFocusInfoMakernoteDirectory.TagExternalFlashBounce, "Bounce or Off", "Direct");
    }

    @Nullable
    public String getExternalFlashZoomDescription() {
        int[] values = ((OlympusFocusInfoMakernoteDirectory) this._directory).getIntArray(OlympusFocusInfoMakernoteDirectory.TagExternalFlashZoom);
        if (values == null) {
            Integer value = ((OlympusFocusInfoMakernoteDirectory) this._directory).getInteger(OlympusFocusInfoMakernoteDirectory.TagExternalFlashZoom);
            if (value == null) {
                return null;
            }
            values = new int[]{value.intValue()};
        }
        if (values.length == 0) {
            return null;
        }
        String join = String.format("%d", Short.valueOf((short) values[0]));
        if (values.length > 1) {
            join = join + " " + String.format("%d", Short.valueOf((short) values[1]));
        }
        if (join.equals("0")) {
            return "Off";
        }
        if (join.equals("1")) {
            return "On";
        }
        if (join.equals("0 0")) {
            return "Off";
        }
        if (join.equals("1 0")) {
            return "On";
        }
        return "Unknown (" + join + ")";
    }

    @Nullable
    public String getManualFlashDescription() {
        int[] values = ((OlympusFocusInfoMakernoteDirectory) this._directory).getIntArray(OlympusFocusInfoMakernoteDirectory.TagManualFlash);
        if (values == null) {
            return null;
        }
        if (((short) values[0]) == 0) {
            return "Off";
        }
        if (((short) values[1]) == 1) {
            return "Full";
        }
        return "On (1/" + ((int) ((short) values[1])) + " strength)";
    }

    @Nullable
    public String getMacroLedDescription() {
        return getIndexedDescription(OlympusFocusInfoMakernoteDirectory.TagMacroLed, "Off", "On");
    }

    @Nullable
    public String getSensorTemperatureDescription() {
        return ((OlympusFocusInfoMakernoteDirectory) this._directory).getString(OlympusFocusInfoMakernoteDirectory.TagSensorTemperature);
    }

    @Nullable
    public String getImageStabilizationDescription() {
        byte[] values = ((OlympusFocusInfoMakernoteDirectory) this._directory).getByteArray(OlympusFocusInfoMakernoteDirectory.TagImageStabilization);
        if (values == null) {
            return null;
        }
        if ((values[0] | values[1] | values[2] | values[3]) == 0) {
            return "Off";
        }
        return "On, " + ((values[43] & 1) > 0 ? "Mode 1" : "Mode 2");
    }
}
