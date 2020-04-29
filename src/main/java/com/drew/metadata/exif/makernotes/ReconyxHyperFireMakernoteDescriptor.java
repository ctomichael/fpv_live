package com.drew.metadata.exif.makernotes;

import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.StringValue;
import com.drew.metadata.TagDescriptor;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ReconyxHyperFireMakernoteDescriptor extends TagDescriptor<ReconyxHyperFireMakernoteDirectory> {
    public ReconyxHyperFireMakernoteDescriptor(@NotNull ReconyxHyperFireMakernoteDirectory directory) {
        super(directory);
    }

    @Nullable
    public String getDescription(int tagType) {
        switch (tagType) {
            case 0:
                return String.format("%d", ((ReconyxHyperFireMakernoteDirectory) this._directory).getInteger(tagType));
            case 2:
                return ((ReconyxHyperFireMakernoteDirectory) this._directory).getString(tagType);
            case 12:
                return ((ReconyxHyperFireMakernoteDirectory) this._directory).getString(tagType);
            case 14:
                int[] sequence = ((ReconyxHyperFireMakernoteDirectory) this._directory).getIntArray(tagType);
                if (sequence == null) {
                    return null;
                }
                return String.format("%d/%d", Integer.valueOf(sequence[0]), Integer.valueOf(sequence[1]));
            case 18:
                return String.format("%d", ((ReconyxHyperFireMakernoteDirectory) this._directory).getInteger(tagType));
            case 22:
                String date = ((ReconyxHyperFireMakernoteDirectory) this._directory).getString(tagType);
                try {
                    DateFormat parser = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
                    return parser.format(parser.parse(date));
                } catch (ParseException e) {
                    return null;
                }
            case 36:
                return getIndexedDescription(tagType, "New", "Waxing Crescent", "First Quarter", "Waxing Gibbous", "Full", "Waning Gibbous", "Last Quarter", "Waning Crescent");
            case 38:
            case 40:
                return String.format("%d", ((ReconyxHyperFireMakernoteDirectory) this._directory).getInteger(tagType));
            case 42:
                StringValue svalue = ((ReconyxHyperFireMakernoteDirectory) this._directory).getStringValue(tagType);
                if (svalue != null) {
                    return svalue.toString();
                }
                return null;
            case 72:
            case 74:
            case 76:
            case 78:
                return String.format("%d", ((ReconyxHyperFireMakernoteDirectory) this._directory).getInteger(tagType));
            case 80:
                return getIndexedDescription(tagType, "Off", "On");
            case 82:
                return String.format("%d", ((ReconyxHyperFireMakernoteDirectory) this._directory).getInteger(tagType));
            case 84:
                Double value = ((ReconyxHyperFireMakernoteDirectory) this._directory).getDoubleObject(tagType);
                return value == null ? null : new DecimalFormat("0.000").format(value);
            case 86:
                return ((ReconyxHyperFireMakernoteDirectory) this._directory).getString(tagType);
            default:
                return super.getDescription(tagType);
        }
    }
}
