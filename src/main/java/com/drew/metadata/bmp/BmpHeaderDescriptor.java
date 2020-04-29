package com.drew.metadata.bmp;

import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.TagDescriptor;
import dji.thirdparty.sanselan.ImageInfo;

public class BmpHeaderDescriptor extends TagDescriptor<BmpHeaderDirectory> {
    public BmpHeaderDescriptor(@NotNull BmpHeaderDirectory directory) {
        super(directory);
    }

    public String getDescription(int tagType) {
        switch (tagType) {
            case 5:
                return getCompressionDescription();
            default:
                return super.getDescription(tagType);
        }
    }

    @Nullable
    public String getCompressionDescription() {
        try {
            Integer value = ((BmpHeaderDirectory) this._directory).getInteger(5);
            if (value == null) {
                return null;
            }
            Integer headerSize = ((BmpHeaderDirectory) this._directory).getInteger(-1);
            if (headerSize == null) {
                return null;
            }
            switch (value.intValue()) {
                case 0:
                    return "None";
                case 1:
                    return "RLE 8-bit/pixel";
                case 2:
                    return "RLE 4-bit/pixel";
                case 3:
                    return headerSize.intValue() == 64 ? "Bit field" : "Huffman 1D";
                case 4:
                    return headerSize.intValue() == 64 ? ImageInfo.COMPRESSION_ALGORITHM_JPEG : "RLE-24";
                case 5:
                    return "PNG";
                case 6:
                    return "Bit field";
                default:
                    return super.getDescription(5);
            }
        } catch (Exception e) {
            return null;
        }
    }
}
