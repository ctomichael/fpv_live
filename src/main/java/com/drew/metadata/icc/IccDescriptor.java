package com.drew.metadata.icc;

import android.support.v4.os.EnvironmentCompat;
import android.support.v4.view.ViewCompat;
import com.drew.lang.ByteArrayReader;
import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.TagDescriptor;
import dji.sdksharedlib.keycatalog.CameraKeys;
import dji.thirdparty.sanselan.formats.tiff.constants.GPSTagConstants;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;

public class IccDescriptor extends TagDescriptor<IccDirectory> {
    private static final int ICC_TAG_TYPE_CURV = 1668641398;
    private static final int ICC_TAG_TYPE_DESC = 1684370275;
    private static final int ICC_TAG_TYPE_MEAS = 1835360627;
    private static final int ICC_TAG_TYPE_MLUC = 1835824483;
    private static final int ICC_TAG_TYPE_SIG = 1936287520;
    private static final int ICC_TAG_TYPE_TEXT = 1952807028;
    private static final int ICC_TAG_TYPE_XYZ_ARRAY = 1482250784;

    public IccDescriptor(@NotNull IccDirectory directory) {
        super(directory);
    }

    public String getDescription(int tagType) {
        switch (tagType) {
            case 8:
                return getProfileVersionDescription();
            case 12:
                return getProfileClassDescription();
            case 40:
                return getPlatformDescription();
            case 64:
                return getRenderingIntentDescription();
            default:
                if (tagType <= 538976288 || tagType >= 2054847098) {
                    return super.getDescription(tagType);
                }
                return getTagDataString(tagType);
        }
    }

    @Nullable
    private String getTagDataString(int tagType) {
        String name;
        String observerString;
        String geometryString;
        String illuminantString;
        try {
            byte[] bytes = ((IccDirectory) this._directory).getByteArray(tagType);
            if (bytes == null) {
                return ((IccDirectory) this._directory).getString(tagType);
            }
            ByteArrayReader byteArrayReader = new ByteArrayReader(bytes);
            int iccTagType = byteArrayReader.getInt32(0);
            switch (iccTagType) {
                case ICC_TAG_TYPE_XYZ_ARRAY /*1482250784*/:
                    StringBuilder res = new StringBuilder();
                    DecimalFormat format = new DecimalFormat("0.####");
                    int count = (bytes.length - 8) / 12;
                    for (int i = 0; i < count; i++) {
                        float x = byteArrayReader.getS15Fixed16((i * 12) + 8);
                        float y = byteArrayReader.getS15Fixed16((i * 12) + 8 + 4);
                        float z = byteArrayReader.getS15Fixed16((i * 12) + 8 + 8);
                        if (i > 0) {
                            res.append(", ");
                        }
                        res.append("(").append(format.format((double) x)).append(", ").append(format.format((double) y)).append(", ").append(format.format((double) z)).append(")");
                    }
                    return res.toString();
                case ICC_TAG_TYPE_CURV /*1668641398*/:
                    int num = byteArrayReader.getInt32(8);
                    StringBuilder res2 = new StringBuilder();
                    for (int i2 = 0; i2 < num; i2++) {
                        if (i2 != 0) {
                            res2.append(", ");
                        }
                        res2.append(formatDoubleAsString(((double) ((float) byteArrayReader.getUInt16((i2 * 2) + 12))) / 65535.0d, 7, false));
                    }
                    return res2.toString();
                case 1684370275:
                    return new String(bytes, 12, byteArrayReader.getInt32(8) - 1);
                case 1835360627:
                    int observerType = byteArrayReader.getInt32(8);
                    float x2 = byteArrayReader.getS15Fixed16(12);
                    float y2 = byteArrayReader.getS15Fixed16(16);
                    float z2 = byteArrayReader.getS15Fixed16(20);
                    int geometryType = byteArrayReader.getInt32(24);
                    float flare = byteArrayReader.getS15Fixed16(28);
                    int illuminantType = byteArrayReader.getInt32(32);
                    switch (observerType) {
                        case 0:
                            observerString = "Unknown";
                            break;
                        case 1:
                            observerString = "1931 2°";
                            break;
                        case 2:
                            observerString = "1964 10°";
                            break;
                        default:
                            observerString = String.format("Unknown %d", Integer.valueOf(observerType));
                            break;
                    }
                    switch (geometryType) {
                        case 0:
                            geometryString = "Unknown";
                            break;
                        case 1:
                            geometryString = "0/45 or 45/0";
                            break;
                        case 2:
                            geometryString = "0/d or d/0";
                            break;
                        default:
                            geometryString = String.format("Unknown %d", Integer.valueOf(observerType));
                            break;
                    }
                    switch (illuminantType) {
                        case 0:
                            illuminantString = EnvironmentCompat.MEDIA_UNKNOWN;
                            break;
                        case 1:
                            illuminantString = "D50";
                            break;
                        case 2:
                            illuminantString = "D65";
                            break;
                        case 3:
                            illuminantString = "D93";
                            break;
                        case 4:
                            illuminantString = "F2";
                            break;
                        case 5:
                            illuminantString = "D55";
                            break;
                        case 6:
                            illuminantString = GPSTagConstants.GPS_TAG_GPS_STATUS_VALUE_MEASUREMENT_IN_PROGRESS;
                            break;
                        case 7:
                            illuminantString = "Equi-Power (E)";
                            break;
                        case 8:
                            illuminantString = "F8";
                            break;
                        default:
                            illuminantString = String.format("Unknown %d", Integer.valueOf(illuminantType));
                            break;
                    }
                    DecimalFormat format2 = new DecimalFormat("0.###");
                    return String.format("%s Observer, Backing (%s, %s, %s), Geometry %s, Flare %d%%, Illuminant %s", observerString, format2.format((double) x2), format2.format((double) y2), format2.format((double) z2), geometryString, Integer.valueOf(Math.round(100.0f * flare)), illuminantString);
                case ICC_TAG_TYPE_MLUC /*1835824483*/:
                    int int1 = byteArrayReader.getInt32(8);
                    StringBuilder res3 = new StringBuilder();
                    res3.append(int1);
                    for (int i3 = 0; i3 < int1; i3++) {
                        String str = IccReader.getStringFromInt32(byteArrayReader.getInt32((i3 * 12) + 16));
                        int len = byteArrayReader.getInt32((i3 * 12) + 16 + 4);
                        int ofs = byteArrayReader.getInt32((i3 * 12) + 16 + 8);
                        try {
                            name = new String(bytes, ofs, len, "UTF-16BE");
                        } catch (UnsupportedEncodingException e) {
                            name = new String(bytes, ofs, len);
                        }
                        res3.append(" ").append(str).append("(").append(name).append(")");
                    }
                    return res3.toString();
                case ICC_TAG_TYPE_SIG /*1936287520*/:
                    return IccReader.getStringFromInt32(byteArrayReader.getInt32(8));
                case ICC_TAG_TYPE_TEXT /*1952807028*/:
                    try {
                        return new String(bytes, 8, (bytes.length - 8) - 1, "ASCII");
                    } catch (UnsupportedEncodingException e2) {
                        return new String(bytes, 8, (bytes.length - 8) - 1);
                    }
                default:
                    return String.format("%s (0x%08X): %d bytes", IccReader.getStringFromInt32(iccTagType), Integer.valueOf(iccTagType), Integer.valueOf(bytes.length));
            }
            return null;
        } catch (IOException e3) {
            return null;
        }
    }

    @NotNull
    public static String formatDoubleAsString(double value, int precision, boolean zeroes) {
        if (precision < 1) {
            return "" + Math.round(value);
        }
        long intPart = Math.abs((long) value);
        long rest = (long) ((int) Math.round((Math.abs(value) - ((double) intPart)) * Math.pow(10.0d, (double) precision)));
        long restKept = rest;
        String res = "";
        for (int i = precision; i > 0; i--) {
            byte cour = (byte) ((int) Math.abs(rest % 10));
            rest /= 10;
            if (res.length() > 0 || zeroes || cour != 0 || i == 1) {
                res = ((int) cour) + res;
            }
        }
        long intPart2 = intPart + rest;
        return ((value > 0.0d ? 1 : (value == 0.0d ? 0 : -1)) < 0 && ((intPart2 > 0 ? 1 : (intPart2 == 0 ? 0 : -1)) != 0 || (restKept > 0 ? 1 : (restKept == 0 ? 0 : -1)) != 0) ? "-" : "") + intPart2 + "." + res;
    }

    @Nullable
    private String getRenderingIntentDescription() {
        return getIndexedDescription(64, "Perceptual", "Media-Relative Colorimetric", CameraKeys.SATURATION, "ICC-Absolute Colorimetric");
    }

    @Nullable
    private String getPlatformDescription() {
        String str = ((IccDirectory) this._directory).getString(40);
        if (str == null) {
            return null;
        }
        try {
            switch (getInt32FromString(str)) {
                case 1095782476:
                    return "Apple Computer, Inc.";
                case 1297303124:
                    return "Microsoft Corporation";
                case 1397180704:
                    return "Silicon Graphics, Inc.";
                case 1398099543:
                    return "Sun Microsystems, Inc.";
                case 1413959252:
                    return "Taligent, Inc.";
                default:
                    return String.format("Unknown (%s)", str);
            }
        } catch (IOException e) {
            return str;
        }
    }

    @Nullable
    private String getProfileClassDescription() {
        String str = ((IccDirectory) this._directory).getString(12);
        if (str == null) {
            return null;
        }
        try {
            switch (getInt32FromString(str)) {
                case 1633842036:
                    return "Abstract";
                case 1818848875:
                    return "DeviceLink";
                case 1835955314:
                    return "Display Device";
                case 1852662636:
                    return "Named Color";
                case 1886549106:
                    return "Output Device";
                case 1935896178:
                    return "Input Device";
                case 1936744803:
                    return "ColorSpace Conversion";
                default:
                    return String.format("Unknown (%s)", str);
            }
        } catch (IOException e) {
            return str;
        }
    }

    @Nullable
    private String getProfileVersionDescription() {
        Integer value = ((IccDirectory) this._directory).getInteger(8);
        if (value == null) {
            return null;
        }
        return String.format("%d.%d.%d", Integer.valueOf((value.intValue() & ViewCompat.MEASURED_STATE_MASK) >> 24), Integer.valueOf((value.intValue() & 15728640) >> 20), Integer.valueOf((value.intValue() & 983040) >> 16));
    }

    private static int getInt32FromString(@NotNull String string) throws IOException {
        return new ByteArrayReader(string.getBytes()).getInt32(0);
    }
}
