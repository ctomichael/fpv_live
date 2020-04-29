package com.drew.lang;

import com.drew.lang.annotations.NotNull;

public class ByteConvert {
    public static int toInt32BigEndian(@NotNull byte[] bytes) {
        return ((bytes[0] << 24) & -16777216) | ((bytes[1] << Tnaf.POW_2_WIDTH) & 16711680) | ((bytes[2] << 8) & 65280) | (bytes[3] & 255);
    }

    public static int toInt32LittleEndian(@NotNull byte[] bytes) {
        return (bytes[0] & 255) | ((bytes[1] << 8) & 65280) | ((bytes[2] << Tnaf.POW_2_WIDTH) & 16711680) | ((bytes[3] << 24) & -16777216);
    }
}
