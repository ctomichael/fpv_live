package com.dji.scan.zxing;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public final class IntentResult {
    private final String barcodeImagePath;
    private final String contents;
    private final String errorCorrectionLevel;
    private final String formatName;
    private final Integer orientation;
    private final byte[] rawBytes;

    IntentResult() {
        this(null, null, null, null, null, null);
    }

    IntentResult(String contents2, String formatName2, byte[] rawBytes2, Integer orientation2, String errorCorrectionLevel2, String barcodeImagePath2) {
        this.contents = contents2;
        this.formatName = formatName2;
        this.rawBytes = rawBytes2;
        this.orientation = orientation2;
        this.errorCorrectionLevel = errorCorrectionLevel2;
        this.barcodeImagePath = barcodeImagePath2;
    }

    public String getContents() {
        return this.contents;
    }

    public String getFormatName() {
        return this.formatName;
    }

    public byte[] getRawBytes() {
        return this.rawBytes;
    }

    public Integer getOrientation() {
        return this.orientation;
    }

    public String getErrorCorrectionLevel() {
        return this.errorCorrectionLevel;
    }

    public String getBarcodeImagePath() {
        return this.barcodeImagePath;
    }

    public String toString() {
        return "Format: " + this.formatName + 10 + "Contents: " + this.contents + 10 + "Raw bytes: (" + (this.rawBytes == null ? 0 : this.rawBytes.length) + " bytes)\nOrientation: " + this.orientation + 10 + "EC level: " + this.errorCorrectionLevel + 10 + "Barcode image: " + this.barcodeImagePath + 10;
    }
}
