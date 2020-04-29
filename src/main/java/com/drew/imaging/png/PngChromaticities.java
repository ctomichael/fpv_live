package com.drew.imaging.png;

import com.drew.lang.SequentialByteArrayReader;
import com.drew.lang.annotations.NotNull;
import java.io.IOException;

public class PngChromaticities {
    private final int _blueX;
    private final int _blueY;
    private final int _greenX;
    private final int _greenY;
    private final int _redX;
    private final int _redY;
    private final int _whitePointX;
    private final int _whitePointY;

    public PngChromaticities(@NotNull byte[] bytes) throws PngProcessingException {
        if (bytes.length != 32) {
            throw new PngProcessingException("Invalid number of bytes");
        }
        SequentialByteArrayReader reader = new SequentialByteArrayReader(bytes);
        try {
            this._whitePointX = reader.getInt32();
            this._whitePointY = reader.getInt32();
            this._redX = reader.getInt32();
            this._redY = reader.getInt32();
            this._greenX = reader.getInt32();
            this._greenY = reader.getInt32();
            this._blueX = reader.getInt32();
            this._blueY = reader.getInt32();
        } catch (IOException ex) {
            throw new PngProcessingException(ex);
        }
    }

    public int getWhitePointX() {
        return this._whitePointX;
    }

    public int getWhitePointY() {
        return this._whitePointY;
    }

    public int getRedX() {
        return this._redX;
    }

    public int getRedY() {
        return this._redY;
    }

    public int getGreenX() {
        return this._greenX;
    }

    public int getGreenY() {
        return this._greenY;
    }

    public int getBlueX() {
        return this._blueX;
    }

    public int getBlueY() {
        return this._blueY;
    }
}
