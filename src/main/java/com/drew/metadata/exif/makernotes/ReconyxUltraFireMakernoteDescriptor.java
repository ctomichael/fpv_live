package com.drew.metadata.exif.makernotes;

import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.StringValue;
import com.drew.metadata.TagDescriptor;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ReconyxUltraFireMakernoteDescriptor extends TagDescriptor<ReconyxUltraFireMakernoteDirectory> {
    public ReconyxUltraFireMakernoteDescriptor(@NotNull ReconyxUltraFireMakernoteDirectory directory) {
        super(directory);
    }

    @Nullable
    public String getDescription(int tagType) {
        switch (tagType) {
            case 0:
                return ((ReconyxUltraFireMakernoteDirectory) this._directory).getString(tagType);
            case 10:
                return String.format("0x%08X", ((ReconyxUltraFireMakernoteDirectory) this._directory).getInteger(tagType));
            case 14:
                return String.format("%d", ((ReconyxUltraFireMakernoteDirectory) this._directory).getInteger(tagType));
            case 18:
                return String.format("0x%08X", ((ReconyxUltraFireMakernoteDirectory) this._directory).getInteger(tagType));
            case 22:
                return String.format("%d", ((ReconyxUltraFireMakernoteDirectory) this._directory).getInteger(tagType));
            case 24:
            case 31:
            case 38:
            case 45:
            case 52:
                return ((ReconyxUltraFireMakernoteDirectory) this._directory).getString(tagType);
            case 53:
                int[] sequence = ((ReconyxUltraFireMakernoteDirectory) this._directory).getIntArray(tagType);
                if (sequence == null) {
                    return null;
                }
                return String.format("%d/%d", Integer.valueOf(sequence[0]), Integer.valueOf(sequence[1]));
            case 55:
                return String.format("%d", ((ReconyxUltraFireMakernoteDirectory) this._directory).getInteger(tagType));
            case 59:
                String date = ((ReconyxUltraFireMakernoteDirectory) this._directory).getString(tagType);
                try {
                    DateFormat parser = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
                    return parser.format(parser.parse(date));
                } catch (ParseException e) {
                    return null;
                }
            case 67:
                return getIndexedDescription(tagType, "New", "Waxing Crescent", "First Quarter", "Waxing Gibbous", "Full", "Waning Gibbous", "Last Quarter", "Waning Crescent");
            case 68:
            case 70:
                return String.format("%d", ((ReconyxUltraFireMakernoteDirectory) this._directory).getInteger(tagType));
            case 72:
                return getIndexedDescription(tagType, "Off", "On");
            case 73:
                Double value = ((ReconyxUltraFireMakernoteDirectory) this._directory).getDoubleObject(tagType);
                return value == null ? null : new DecimalFormat("0.000").format(value);
            case 75:
                StringValue svalue = ((ReconyxUltraFireMakernoteDirectory) this._directory).getStringValue(tagType);
                if (svalue != null) {
                    return svalue.toString();
                }
                return null;
            case 80:
                return ((ReconyxUltraFireMakernoteDirectory) this._directory).getString(tagType);
            default:
                return super.getDescription(tagType);
        }
    }
}
