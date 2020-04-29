package com.mapbox.mapboxsdk.text;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

public class LocalGlyphRasterizer {
    private final Bitmap bitmap = Bitmap.createBitmap(35, 35, Bitmap.Config.ARGB_8888);
    @NonNull
    private final Canvas canvas;
    @NonNull
    private final Paint paint = new Paint();

    LocalGlyphRasterizer() {
        this.paint.setAntiAlias(true);
        this.paint.setTextSize(24.0f);
        this.canvas = new Canvas();
        this.canvas.setBitmap(this.bitmap);
    }

    /* access modifiers changed from: protected */
    @Keep
    @WorkerThread
    public Bitmap drawGlyphBitmap(String fontFamily, boolean bold, char glyphID) {
        int i;
        Paint paint2 = this.paint;
        if (bold) {
            i = 1;
        } else {
            i = 0;
        }
        paint2.setTypeface(Typeface.create(fontFamily, i));
        this.canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        this.canvas.drawText(String.valueOf(glyphID), 0.0f, 20.0f, this.paint);
        return this.bitmap;
    }
}
