package com.dji.scan.qr;

import com.google.zxing.ResultPoint;
import dji.fieldAnnotation.EXClassNullAway;
import java.util.List;

@EXClassNullAway
public interface BarcodeCallback {
    void barcodeResult(BarcodeResult barcodeResult);

    void possibleResultPoints(List<ResultPoint> list);
}
