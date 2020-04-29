package com.google.zxing.common;

import java.util.List;

public final class DecoderResult {
    private final List<byte[]> byteSegments;
    private final String ecLevel;
    private Integer erasures;
    private Integer errorsCorrected;
    private int numBits;
    private Object other;
    private final byte[] rawBytes;
    private final int structuredAppendParity;
    private final int structuredAppendSequenceNumber;
    private final String text;

    public DecoderResult(byte[] rawBytes2, String text2, List<byte[]> byteSegments2, String ecLevel2) {
        this(rawBytes2, text2, byteSegments2, ecLevel2, -1, -1);
    }

    public DecoderResult(byte[] rawBytes2, String text2, List<byte[]> byteSegments2, String ecLevel2, int saSequence, int saParity) {
        this.rawBytes = rawBytes2;
        this.numBits = rawBytes2 == null ? 0 : rawBytes2.length * 8;
        this.text = text2;
        this.byteSegments = byteSegments2;
        this.ecLevel = ecLevel2;
        this.structuredAppendParity = saParity;
        this.structuredAppendSequenceNumber = saSequence;
    }

    public byte[] getRawBytes() {
        return this.rawBytes;
    }

    public int getNumBits() {
        return this.numBits;
    }

    public void setNumBits(int numBits2) {
        this.numBits = numBits2;
    }

    public String getText() {
        return this.text;
    }

    public List<byte[]> getByteSegments() {
        return this.byteSegments;
    }

    public String getECLevel() {
        return this.ecLevel;
    }

    public Integer getErrorsCorrected() {
        return this.errorsCorrected;
    }

    public void setErrorsCorrected(Integer errorsCorrected2) {
        this.errorsCorrected = errorsCorrected2;
    }

    public Integer getErasures() {
        return this.erasures;
    }

    public void setErasures(Integer erasures2) {
        this.erasures = erasures2;
    }

    public Object getOther() {
        return this.other;
    }

    public void setOther(Object other2) {
        this.other = other2;
    }

    public boolean hasStructuredAppend() {
        return this.structuredAppendParity >= 0 && this.structuredAppendSequenceNumber >= 0;
    }

    public int getStructuredAppendParity() {
        return this.structuredAppendParity;
    }

    public int getStructuredAppendSequenceNumber() {
        return this.structuredAppendSequenceNumber;
    }
}
