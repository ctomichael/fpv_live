package com.drew.metadata.adobe;

import com.drew.lang.annotations.Nullable;
import com.drew.metadata.TagDescriptor;

public class AdobeJpegDescriptor extends TagDescriptor<AdobeJpegDirectory> {
    public AdobeJpegDescriptor(AdobeJpegDirectory directory) {
        super(directory);
    }

    public String getDescription(int tagType) {
        switch (tagType) {
            case 0:
                return getDctEncodeVersionDescription();
            case 1:
            case 2:
            default:
                return super.getDescription(tagType);
            case 3:
                return getColorTransformDescription();
        }
    }

    @Nullable
    private String getDctEncodeVersionDescription() {
        Integer value = ((AdobeJpegDirectory) this._directory).getInteger(0);
        if (value == null) {
            return null;
        }
        return value.intValue() == 100 ? "100" : Integer.toString(value.intValue());
    }

    @Nullable
    private String getColorTransformDescription() {
        return getIndexedDescription(3, "Unknown (RGB or CMYK)", "YCbCr", "YCCK");
    }
}
