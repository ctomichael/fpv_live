package com.dji.scan.qr;

import com.google.zxing.ResultPoint;
import com.google.zxing.ResultPointCallback;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class DecoderResultPointCallback implements ResultPointCallback {
    private Decoder decoder;

    public DecoderResultPointCallback(Decoder decoder2) {
        this.decoder = decoder2;
    }

    public DecoderResultPointCallback() {
    }

    public Decoder getDecoder() {
        return this.decoder;
    }

    public void setDecoder(Decoder decoder2) {
        this.decoder = decoder2;
    }

    public void foundPossibleResultPoint(ResultPoint point) {
        if (this.decoder != null) {
            this.decoder.foundPossibleResultPoint(point);
        }
    }
}
