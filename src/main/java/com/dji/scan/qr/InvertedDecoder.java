package com.dji.scan.qr;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.common.HybridBinarizer;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class InvertedDecoder extends Decoder {
    public InvertedDecoder(Reader reader) {
        super(reader);
    }

    /* access modifiers changed from: protected */
    public BinaryBitmap toBitmap(LuminanceSource source) {
        return new BinaryBitmap(new HybridBinarizer(source.invert()));
    }
}
