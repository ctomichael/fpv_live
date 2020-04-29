package com.drew.metadata.photoshop;

import com.drew.lang.ByteArrayReader;
import com.drew.lang.RandomAccessReader;
import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.TagDescriptor;
import java.io.IOException;
import java.text.DecimalFormat;

public class PhotoshopDescriptor extends TagDescriptor<PhotoshopDirectory> {
    public PhotoshopDescriptor(@NotNull PhotoshopDirectory directory) {
        super(directory);
    }

    public String getDescription(int tagType) {
        switch (tagType) {
            case 1002:
            case 1035:
                return getSimpleString(tagType);
            case 1005:
                return getResolutionInfoDescription();
            case 1028:
                return getBinaryDataString(tagType);
            case 1030:
                return getJpegQualityString();
            case 1033:
            case PhotoshopDirectory.TAG_THUMBNAIL /*1036*/:
                return getThumbnailDescription(tagType);
            case 1034:
                return getBooleanString(tagType);
            case 1037:
            case PhotoshopDirectory.TAG_SEED_NUMBER /*1044*/:
            case PhotoshopDirectory.TAG_GLOBAL_ALTITUDE /*1049*/:
            case PhotoshopDirectory.TAG_URL_LIST /*1054*/:
                return get32BitNumberString(tagType);
            case PhotoshopDirectory.TAG_SLICES /*1050*/:
                return getSlicesDescription();
            case PhotoshopDirectory.TAG_VERSION /*1057*/:
                return getVersionDescription();
            case PhotoshopDirectory.TAG_PRINT_SCALE /*1062*/:
                return getPrintScaleDescription();
            case PhotoshopDirectory.TAG_PIXEL_ASPECT_RATIO /*1064*/:
                return getPixelAspectRatioString();
            default:
                return super.getDescription(tagType);
        }
    }

    @Nullable
    public String getJpegQualityString() {
        int q1;
        String quality;
        String format;
        try {
            byte[] b = ((PhotoshopDirectory) this._directory).getByteArray(1030);
            if (b == null) {
                return ((PhotoshopDirectory) this._directory).getString(1030);
            }
            RandomAccessReader reader = new ByteArrayReader(b);
            int q = reader.getUInt16(0);
            int f = reader.getUInt16(2);
            int s = reader.getUInt16(4);
            if (q > 65535 || q < 65533) {
                q1 = q <= 8 ? q + 4 : q;
            } else {
                q1 = q - 65532;
            }
            switch (q) {
                case 0:
                case 65533:
                case 65534:
                case 65535:
                    quality = "Low";
                    break;
                case 1:
                case 2:
                case 3:
                    quality = "Medium";
                    break;
                case 4:
                case 5:
                    quality = "High";
                    break;
                case 6:
                case 7:
                case 8:
                    quality = "Maximum";
                    break;
                default:
                    quality = "Unknown";
                    break;
            }
            switch (f) {
                case 0:
                    format = "Standard";
                    break;
                case 1:
                    format = "Optimised";
                    break;
                case 257:
                    format = "Progressive";
                    break;
                default:
                    format = String.format("Unknown 0x%04X", Integer.valueOf(f));
                    break;
            }
            return String.format("%d (%s), %s format, %s scans", Integer.valueOf(q1), quality, format, (s < 1 || s > 3) ? String.format("Unknown 0x%04X", Integer.valueOf(s)) : String.format("%d", Integer.valueOf(s + 2)));
        } catch (IOException e) {
            return null;
        }
    }

    @Nullable
    public String getPixelAspectRatioString() {
        try {
            byte[] bytes = ((PhotoshopDirectory) this._directory).getByteArray(PhotoshopDirectory.TAG_PIXEL_ASPECT_RATIO);
            if (bytes == null) {
                return null;
            }
            return Double.toString(new ByteArrayReader(bytes).getDouble64(4));
        } catch (Exception e) {
            return null;
        }
    }

    @Nullable
    public String getPrintScaleDescription() {
        try {
            byte[] bytes = ((PhotoshopDirectory) this._directory).getByteArray(PhotoshopDirectory.TAG_PRINT_SCALE);
            if (bytes == null) {
                return null;
            }
            RandomAccessReader reader = new ByteArrayReader(bytes);
            int style = reader.getInt32(0);
            float locX = reader.getFloat32(2);
            float locY = reader.getFloat32(6);
            float scale = reader.getFloat32(10);
            switch (style) {
                case 0:
                    return "Centered, Scale " + scale;
                case 1:
                    return "Size to fit";
                case 2:
                    return String.format("User defined, X:%s Y:%s, Scale:%s", Float.valueOf(locX), Float.valueOf(locY), Float.valueOf(scale));
                default:
                    return String.format("Unknown %04X, X:%s Y:%s, Scale:%s", Integer.valueOf(style), Float.valueOf(locX), Float.valueOf(locY), Float.valueOf(scale));
            }
        } catch (Exception e) {
            return null;
        }
    }

    @Nullable
    public String getResolutionInfoDescription() {
        try {
            byte[] bytes = ((PhotoshopDirectory) this._directory).getByteArray(1005);
            if (bytes == null) {
                return null;
            }
            RandomAccessReader reader = new ByteArrayReader(bytes);
            float resX = reader.getS15Fixed16(0);
            float resY = reader.getS15Fixed16(8);
            DecimalFormat format = new DecimalFormat("0.##");
            return format.format((double) resX) + "x" + format.format((double) resY) + " DPI";
        } catch (Exception e) {
            return null;
        }
    }

    @Nullable
    public String getVersionDescription() {
        try {
            byte[] bytes = ((PhotoshopDirectory) this._directory).getByteArray(PhotoshopDirectory.TAG_VERSION);
            if (bytes == null) {
                return null;
            }
            RandomAccessReader reader = new ByteArrayReader(bytes);
            int ver = reader.getInt32(0);
            int readerLength = reader.getInt32(5);
            int pos = 0 + 4 + 1 + 4;
            String readerStr = reader.getString(9, readerLength * 2, "UTF-16");
            int pos2 = (readerLength * 2) + 9;
            int writerLength = reader.getInt32(pos2);
            int pos3 = pos2 + 4;
            return String.format("%d (%s, %s) %d", Integer.valueOf(ver), readerStr, reader.getString(pos3, writerLength * 2, "UTF-16"), Integer.valueOf(reader.getInt32(pos3 + (writerLength * 2))));
        } catch (IOException e) {
            return null;
        }
    }

    @Nullable
    public String getSlicesDescription() {
        try {
            byte[] bytes = ((PhotoshopDirectory) this._directory).getByteArray(PhotoshopDirectory.TAG_SLICES);
            if (bytes == null) {
                return null;
            }
            RandomAccessReader reader = new ByteArrayReader(bytes);
            int nameLength = reader.getInt32(20);
            return String.format("%s (%d,%d,%d,%d) %d Slices", reader.getString(24, nameLength * 2, "UTF-16"), Integer.valueOf(reader.getInt32(4)), Integer.valueOf(reader.getInt32(8)), Integer.valueOf(reader.getInt32(12)), Integer.valueOf(reader.getInt32(16)), Integer.valueOf(reader.getInt32((nameLength * 2) + 24)));
        } catch (IOException e) {
            return null;
        }
    }

    @Nullable
    public String getThumbnailDescription(int tagType) {
        try {
            byte[] v = ((PhotoshopDirectory) this._directory).getByteArray(tagType);
            if (v == null) {
                return null;
            }
            RandomAccessReader reader = new ByteArrayReader(v);
            int format = reader.getInt32(0);
            int width = reader.getInt32(4);
            int height = reader.getInt32(8);
            int totalSize = reader.getInt32(16);
            int compSize = reader.getInt32(20);
            int bpp = reader.getInt32(24);
            Object[] objArr = new Object[6];
            objArr[0] = format == 1 ? "JpegRGB" : "RawRGB";
            objArr[1] = Integer.valueOf(width);
            objArr[2] = Integer.valueOf(height);
            objArr[3] = Integer.valueOf(totalSize);
            objArr[4] = Integer.valueOf(bpp);
            objArr[5] = Integer.valueOf(compSize);
            return String.format("%s, %dx%d, Decomp %d bytes, %d bpp, %d bytes", objArr);
        } catch (IOException e) {
            return null;
        }
    }

    @Nullable
    private String getBooleanString(int tag) {
        byte[] bytes = ((PhotoshopDirectory) this._directory).getByteArray(tag);
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        return bytes[0] == 0 ? "No" : "Yes";
    }

    @Nullable
    private String get32BitNumberString(int tag) {
        byte[] bytes = ((PhotoshopDirectory) this._directory).getByteArray(tag);
        if (bytes == null) {
            return null;
        }
        try {
            return String.format("%d", Integer.valueOf(new ByteArrayReader(bytes).getInt32(0)));
        } catch (IOException e) {
            return null;
        }
    }

    @Nullable
    private String getSimpleString(int tagType) {
        byte[] bytes = ((PhotoshopDirectory) this._directory).getByteArray(tagType);
        if (bytes == null) {
            return null;
        }
        return new String(bytes);
    }

    @Nullable
    private String getBinaryDataString(int tagType) {
        byte[] bytes = ((PhotoshopDirectory) this._directory).getByteArray(tagType);
        if (bytes == null) {
            return null;
        }
        return String.format("%d bytes binary data", Integer.valueOf(bytes.length));
    }
}
