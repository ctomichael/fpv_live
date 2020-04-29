package com.drew.imaging.png;

import com.drew.lang.SequentialByteArrayReader;
import com.drew.lang.SequentialReader;
import com.drew.lang.annotations.NotNull;
import java.io.IOException;

public class PngHeader {
    private byte _bitsPerSample;
    @NotNull
    private PngColorType _colorType;
    private byte _compressionType;
    private byte _filterMethod;
    private int _imageHeight;
    private int _imageWidth;
    private byte _interlaceMethod;

    public PngHeader(@NotNull byte[] bytes) throws PngProcessingException {
        if (bytes.length != 13) {
            throw new PngProcessingException("PNG header chunk must have 13 data bytes");
        }
        SequentialReader reader = new SequentialByteArrayReader(bytes);
        try {
            this._imageWidth = reader.getInt32();
            this._imageHeight = reader.getInt32();
            this._bitsPerSample = reader.getInt8();
            byte colorTypeNumber = reader.getInt8();
            PngColorType colorType = PngColorType.fromNumericValue(colorTypeNumber);
            if (colorType == null) {
                throw new PngProcessingException("Unexpected PNG color type: " + ((int) colorTypeNumber));
            }
            this._colorType = colorType;
            this._compressionType = reader.getInt8();
            this._filterMethod = reader.getInt8();
            this._interlaceMethod = reader.getInt8();
        } catch (IOException e) {
            throw new PngProcessingException(e);
        }
    }

    public int getImageWidth() {
        return this._imageWidth;
    }

    public int getImageHeight() {
        return this._imageHeight;
    }

    public byte getBitsPerSample() {
        return this._bitsPerSample;
    }

    @NotNull
    public PngColorType getColorType() {
        return this._colorType;
    }

    public byte getCompressionType() {
        return this._compressionType;
    }

    public byte getFilterMethod() {
        return this._filterMethod;
    }

    public byte getInterlaceMethod() {
        return this._interlaceMethod;
    }
}
