package com.drew.metadata.jfif;

import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.TagDescriptor;

public class JfifDescriptor extends TagDescriptor<JfifDirectory> {
    public JfifDescriptor(@NotNull JfifDirectory directory) {
        super(directory);
    }

    @Nullable
    public String getDescription(int tagType) {
        switch (tagType) {
            case 5:
                return getImageVersionDescription();
            case 6:
            case 9:
            default:
                return super.getDescription(tagType);
            case 7:
                return getImageResUnitsDescription();
            case 8:
                return getImageResXDescription();
            case 10:
                return getImageResYDescription();
        }
    }

    @Nullable
    public String getImageVersionDescription() {
        Integer value = ((JfifDirectory) this._directory).getInteger(5);
        if (value == null) {
            return null;
        }
        return String.format("%d.%d", Integer.valueOf((value.intValue() & 65280) >> 8), Integer.valueOf(value.intValue() & 255));
    }

    @Nullable
    public String getImageResYDescription() {
        Integer value = ((JfifDirectory) this._directory).getInteger(10);
        if (value == null) {
            return null;
        }
        Object[] objArr = new Object[2];
        objArr[0] = value;
        objArr[1] = value.intValue() == 1 ? "" : "s";
        return String.format("%d dot%s", objArr);
    }

    @Nullable
    public String getImageResXDescription() {
        Integer value = ((JfifDirectory) this._directory).getInteger(8);
        if (value == null) {
            return null;
        }
        Object[] objArr = new Object[2];
        objArr[0] = value;
        objArr[1] = value.intValue() == 1 ? "" : "s";
        return String.format("%d dot%s", objArr);
    }

    @Nullable
    public String getImageResUnitsDescription() {
        Integer value = ((JfifDirectory) this._directory).getInteger(7);
        if (value == null) {
            return null;
        }
        switch (value.intValue()) {
            case 0:
                return "none";
            case 1:
                return "inch";
            case 2:
                return "centimetre";
            default:
                return "unit";
        }
    }
}
