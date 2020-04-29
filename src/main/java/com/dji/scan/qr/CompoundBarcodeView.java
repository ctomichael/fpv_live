package com.dji.scan.qr;

import android.content.Context;
import android.support.annotation.Keep;
import android.util.AttributeSet;
import dji.fieldAnnotation.EXClassNullAway;

@Keep
@EXClassNullAway
public class CompoundBarcodeView extends DecoratedBarcodeView {
    public CompoundBarcodeView(Context context) {
        super(context);
    }

    public CompoundBarcodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CompoundBarcodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
