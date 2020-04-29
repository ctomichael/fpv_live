package com.dji.scan.qr;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.ResultPointCallback;
import com.google.zxing.common.HybridBinarizer;
import dji.fieldAnnotation.EXClassNullAway;
import java.util.ArrayList;
import java.util.List;

@EXClassNullAway
public class Decoder implements ResultPointCallback {
    private List<ResultPoint> possibleResultPoints = new ArrayList();
    private Reader reader;

    public Decoder(Reader reader2) {
        this.reader = reader2;
    }

    /* access modifiers changed from: protected */
    public Reader getReader() {
        return this.reader;
    }

    public Result decode(LuminanceSource source) {
        return decode(toBitmap(source));
    }

    /* access modifiers changed from: protected */
    public BinaryBitmap toBitmap(LuminanceSource source) {
        return new BinaryBitmap(new HybridBinarizer(source));
    }

    /* access modifiers changed from: protected */
    public Result decode(BinaryBitmap bitmap) {
        this.possibleResultPoints.clear();
        try {
            if (this.reader instanceof MultiFormatReader) {
                return ((MultiFormatReader) this.reader).decodeWithState(bitmap);
            }
            Result decode = this.reader.decode(bitmap);
            this.reader.reset();
            return decode;
        } catch (Exception e) {
            return null;
        } finally {
            this.reader.reset();
        }
    }

    public List<ResultPoint> getPossibleResultPoints() {
        return new ArrayList(this.possibleResultPoints);
    }

    public void foundPossibleResultPoint(ResultPoint point) {
        this.possibleResultPoints.add(point);
    }
}
