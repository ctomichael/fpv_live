package com.drew.metadata.jpeg;

import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.TagDescriptor;

public class JpegDescriptor extends TagDescriptor<JpegDirectory> {
    public JpegDescriptor(@NotNull JpegDirectory directory) {
        super(directory);
    }

    @Nullable
    public String getDescription(int tagType) {
        switch (tagType) {
            case -3:
                return getImageCompressionTypeDescription();
            case -2:
            case -1:
            case 2:
            case 4:
            case 5:
            default:
                return super.getDescription(tagType);
            case 0:
                return getDataPrecisionDescription();
            case 1:
                return getImageHeightDescription();
            case 3:
                return getImageWidthDescription();
            case 6:
                return getComponentDataDescription(0);
            case 7:
                return getComponentDataDescription(1);
            case 8:
                return getComponentDataDescription(2);
            case 9:
                return getComponentDataDescription(3);
        }
    }

    @Nullable
    public String getImageCompressionTypeDescription() {
        return getIndexedDescription(-3, "Baseline", "Extended sequential, Huffman", "Progressive, Huffman", "Lossless, Huffman", null, "Differential sequential, Huffman", "Differential progressive, Huffman", "Differential lossless, Huffman", "Reserved for JPEG extensions", "Extended sequential, arithmetic", "Progressive, arithmetic", "Lossless, arithmetic", null, "Differential sequential, arithmetic", "Differential progressive, arithmetic", "Differential lossless, arithmetic");
    }

    @Nullable
    public String getImageWidthDescription() {
        String value = ((JpegDirectory) this._directory).getString(3);
        if (value == null) {
            return null;
        }
        return value + " pixels";
    }

    @Nullable
    public String getImageHeightDescription() {
        String value = ((JpegDirectory) this._directory).getString(1);
        if (value == null) {
            return null;
        }
        return value + " pixels";
    }

    @Nullable
    public String getDataPrecisionDescription() {
        String value = ((JpegDirectory) this._directory).getString(0);
        if (value == null) {
            return null;
        }
        return value + " bits";
    }

    @Nullable
    public String getComponentDataDescription(int componentNumber) {
        JpegComponent value = ((JpegDirectory) this._directory).getComponent(componentNumber);
        if (value == null) {
            return null;
        }
        return value.getComponentName() + " component: " + value;
    }
}
