package com.drew.imaging.png;

import com.drew.lang.annotations.NotNull;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PngChunkType {
    static final /* synthetic */ boolean $assertionsDisabled = (!PngChunkType.class.desiredAssertionStatus());
    public static final PngChunkType IDAT;
    public static final PngChunkType IEND;
    public static final PngChunkType IHDR;
    public static final PngChunkType PLTE;
    private static final Set<String> _identifiersAllowingMultiples = new HashSet(Arrays.asList("IDAT", "sPLT", "iTXt", "tEXt", "zTXt"));
    public static final PngChunkType bKGD;
    public static final PngChunkType cHRM;
    public static final PngChunkType gAMA;
    public static final PngChunkType hIST;
    public static final PngChunkType iCCP;
    public static final PngChunkType iTXt;
    public static final PngChunkType pHYs;
    public static final PngChunkType sBIT;
    public static final PngChunkType sPLT;
    public static final PngChunkType sRGB;
    public static final PngChunkType tEXt;
    public static final PngChunkType tIME;
    public static final PngChunkType tRNS;
    public static final PngChunkType zTXt;
    private final byte[] _bytes;
    private final boolean _multipleAllowed;

    static {
        try {
            IHDR = new PngChunkType("IHDR");
            PLTE = new PngChunkType("PLTE");
            IDAT = new PngChunkType("IDAT", true);
            IEND = new PngChunkType("IEND");
            cHRM = new PngChunkType("cHRM");
            gAMA = new PngChunkType("gAMA");
            iCCP = new PngChunkType("iCCP");
            sBIT = new PngChunkType("sBIT");
            sRGB = new PngChunkType("sRGB");
            bKGD = new PngChunkType("bKGD");
            hIST = new PngChunkType("hIST");
            tRNS = new PngChunkType("tRNS");
            pHYs = new PngChunkType("pHYs");
            sPLT = new PngChunkType("sPLT", true);
            tIME = new PngChunkType("tIME");
            iTXt = new PngChunkType("iTXt", true);
            tEXt = new PngChunkType("tEXt", true);
            zTXt = new PngChunkType("zTXt", true);
        } catch (PngProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public PngChunkType(@NotNull String identifier) throws PngProcessingException {
        this(identifier, false);
    }

    public PngChunkType(@NotNull String identifier, boolean multipleAllowed) throws PngProcessingException {
        this._multipleAllowed = multipleAllowed;
        try {
            byte[] bytes = identifier.getBytes("ASCII");
            validateBytes(bytes);
            this._bytes = bytes;
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("Unable to convert string code to bytes.");
        }
    }

    public PngChunkType(@NotNull byte[] bytes) throws PngProcessingException {
        validateBytes(bytes);
        this._bytes = bytes;
        this._multipleAllowed = _identifiersAllowingMultiples.contains(getIdentifier());
    }

    private static void validateBytes(byte[] bytes) throws PngProcessingException {
        if (bytes.length != 4) {
            throw new PngProcessingException("PNG chunk type identifier must be four bytes in length");
        }
        for (byte b : bytes) {
            if (!isValidByte(b)) {
                throw new PngProcessingException("PNG chunk type identifier may only contain alphabet characters");
            }
        }
    }

    public boolean isCritical() {
        return isUpperCase(this._bytes[0]);
    }

    public boolean isAncillary() {
        return !isCritical();
    }

    public boolean isPrivate() {
        return isUpperCase(this._bytes[1]);
    }

    public boolean isSafeToCopy() {
        return isLowerCase(this._bytes[3]);
    }

    public boolean areMultipleAllowed() {
        return this._multipleAllowed;
    }

    private static boolean isLowerCase(byte b) {
        return (b & 32) != 0;
    }

    private static boolean isUpperCase(byte b) {
        return (b & 32) == 0;
    }

    private static boolean isValidByte(byte b) {
        return (b >= 65 && b <= 90) || (b >= 97 && b <= 122);
    }

    public String getIdentifier() {
        try {
            return new String(this._bytes, "ASCII");
        } catch (UnsupportedEncodingException e) {
            if ($assertionsDisabled) {
                return "Invalid object instance";
            }
            throw new AssertionError();
        }
    }

    public String toString() {
        return getIdentifier();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return Arrays.equals(this._bytes, ((PngChunkType) o)._bytes);
    }

    public int hashCode() {
        return Arrays.hashCode(this._bytes);
    }
}
