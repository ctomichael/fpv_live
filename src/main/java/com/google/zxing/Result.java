package com.google.zxing;

import java.util.EnumMap;
import java.util.Map;

public final class Result {
    private final BarcodeFormat format;
    private final int numBits;
    private final byte[] rawBytes;
    private Map<ResultMetadataType, Object> resultMetadata;
    private ResultPoint[] resultPoints;
    private final String text;
    private final long timestamp;

    public Result(String text2, byte[] rawBytes2, ResultPoint[] resultPoints2, BarcodeFormat format2) {
        this(text2, rawBytes2, resultPoints2, format2, System.currentTimeMillis());
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public Result(String text2, byte[] rawBytes2, ResultPoint[] resultPoints2, BarcodeFormat format2, long timestamp2) {
        this(text2, rawBytes2, rawBytes2 == null ? 0 : rawBytes2.length * 8, resultPoints2, format2, timestamp2);
    }

    public Result(String text2, byte[] rawBytes2, int numBits2, ResultPoint[] resultPoints2, BarcodeFormat format2, long timestamp2) {
        this.text = text2;
        this.rawBytes = rawBytes2;
        this.numBits = numBits2;
        this.resultPoints = resultPoints2;
        this.format = format2;
        this.resultMetadata = null;
        this.timestamp = timestamp2;
    }

    public String getText() {
        return this.text;
    }

    public byte[] getRawBytes() {
        return this.rawBytes;
    }

    public int getNumBits() {
        return this.numBits;
    }

    public ResultPoint[] getResultPoints() {
        return this.resultPoints;
    }

    public BarcodeFormat getBarcodeFormat() {
        return this.format;
    }

    public Map<ResultMetadataType, Object> getResultMetadata() {
        return this.resultMetadata;
    }

    public void putMetadata(ResultMetadataType type, Object value) {
        if (this.resultMetadata == null) {
            this.resultMetadata = new EnumMap(ResultMetadataType.class);
        }
        this.resultMetadata.put(type, value);
    }

    public void putAllMetadata(Map<ResultMetadataType, Object> metadata) {
        if (metadata == null) {
            return;
        }
        if (this.resultMetadata == null) {
            this.resultMetadata = metadata;
        } else {
            this.resultMetadata.putAll(metadata);
        }
    }

    public void addResultPoints(ResultPoint[] newPoints) {
        ResultPoint[] oldPoints = this.resultPoints;
        if (oldPoints == null) {
            this.resultPoints = newPoints;
        } else if (newPoints != null && newPoints.length > 0) {
            ResultPoint[] allPoints = new ResultPoint[(oldPoints.length + newPoints.length)];
            System.arraycopy(oldPoints, 0, allPoints, 0, oldPoints.length);
            System.arraycopy(newPoints, 0, allPoints, oldPoints.length, newPoints.length);
            this.resultPoints = allPoints;
        }
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public String toString() {
        return this.text;
    }
}
