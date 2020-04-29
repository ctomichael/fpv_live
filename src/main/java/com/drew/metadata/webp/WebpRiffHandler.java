package com.drew.metadata.webp;

import com.drew.imaging.riff.RiffHandler;
import com.drew.lang.ByteArrayReader;
import com.drew.lang.RandomAccessReader;
import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifReader;
import com.drew.metadata.icc.IccReader;
import com.drew.metadata.xmp.XmpReader;
import java.io.IOException;

public class WebpRiffHandler implements RiffHandler {
    @NotNull
    private final Metadata _metadata;

    public WebpRiffHandler(@NotNull Metadata metadata) {
        this._metadata = metadata;
    }

    public boolean shouldAcceptRiffIdentifier(@NotNull String identifier) {
        return identifier.equals("WEBP");
    }

    public boolean shouldAcceptChunk(@NotNull String fourCC) {
        return fourCC.equals("VP8X") || fourCC.equals("VP8L") || fourCC.equals("VP8 ") || fourCC.equals("EXIF") || fourCC.equals("ICCP") || fourCC.equals("XMP ");
    }

    public void processChunk(@NotNull String fourCC, @NotNull byte[] payload) {
        if (fourCC.equals("EXIF")) {
            new ExifReader().extract(new ByteArrayReader(payload), this._metadata);
        } else if (fourCC.equals("ICCP")) {
            new IccReader().extract(new ByteArrayReader(payload), this._metadata);
        } else if (fourCC.equals("XMP ")) {
            new XmpReader().extract(payload, this._metadata);
        } else if (fourCC.equals("VP8X") && payload.length == 10) {
            RandomAccessReader reader = new ByteArrayReader(payload);
            reader.setMotorolaByteOrder(false);
            try {
                boolean isAnimation = reader.getBit(1);
                boolean hasAlpha = reader.getBit(4);
                int widthMinusOne = reader.getInt24(4);
                int heightMinusOne = reader.getInt24(7);
                WebpDirectory directory = new WebpDirectory();
                directory.setInt(2, widthMinusOne + 1);
                directory.setInt(1, heightMinusOne + 1);
                directory.setBoolean(3, hasAlpha);
                directory.setBoolean(4, isAnimation);
                this._metadata.addDirectory(directory);
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
        } else if (fourCC.equals("VP8L") && payload.length > 4) {
            RandomAccessReader reader2 = new ByteArrayReader(payload);
            reader2.setMotorolaByteOrder(false);
            try {
                if (reader2.getInt8(0) == 47) {
                    int b1 = reader2.getUInt8(1);
                    int b2 = reader2.getUInt8(2);
                    int heightMinusOne2 = ((reader2.getUInt8(4) & 15) << 10) | (reader2.getUInt8(3) << 2) | ((b2 & 192) >> 6);
                    WebpDirectory directory2 = new WebpDirectory();
                    directory2.setInt(2, (((b2 & 63) << 8) | b1) + 1);
                    directory2.setInt(1, heightMinusOne2 + 1);
                    this._metadata.addDirectory(directory2);
                }
            } catch (IOException e2) {
                e2.printStackTrace(System.err);
            }
        } else if (fourCC.equals("VP8 ") && payload.length > 9) {
            RandomAccessReader reader3 = new ByteArrayReader(payload);
            reader3.setMotorolaByteOrder(false);
            try {
                if (reader3.getUInt8(3) == 157 && reader3.getUInt8(4) == 1 && reader3.getUInt8(5) == 42) {
                    int width = reader3.getUInt16(6);
                    int height = reader3.getUInt16(8);
                    WebpDirectory directory3 = new WebpDirectory();
                    directory3.setInt(2, width);
                    directory3.setInt(1, height);
                    this._metadata.addDirectory(directory3);
                }
            } catch (IOException e3) {
                e3.printStackTrace(System.err);
            }
        }
    }
}
