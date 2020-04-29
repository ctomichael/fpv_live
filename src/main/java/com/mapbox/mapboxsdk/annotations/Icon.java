package com.mapbox.mapboxsdk.annotations;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.nio.ByteBuffer;

@Deprecated
public class Icon {
    private Bitmap mBitmap;
    private String mId;

    Icon(String id, Bitmap bitmap) {
        this.mId = id;
        this.mBitmap = bitmap;
    }

    public String getId() {
        return this.mId;
    }

    public Bitmap getBitmap() {
        if (!(this.mBitmap == null || this.mBitmap.getConfig() == Bitmap.Config.ARGB_8888)) {
            this.mBitmap = this.mBitmap.copy(Bitmap.Config.ARGB_8888, false);
        }
        return this.mBitmap;
    }

    public float getScale() {
        if (this.mBitmap == null) {
            throw new IllegalStateException("Required to set a Icon before calling getScale");
        }
        float density = (float) this.mBitmap.getDensity();
        if (density == 0.0f) {
            density = 160.0f;
        }
        return density / 160.0f;
    }

    @NonNull
    public byte[] toBytes() {
        if (this.mBitmap == null) {
            throw new IllegalStateException("Required to set a Icon before calling toBytes");
        }
        ByteBuffer buffer = ByteBuffer.allocate(this.mBitmap.getRowBytes() * this.mBitmap.getHeight());
        this.mBitmap.copyPixelsToBuffer(buffer);
        return buffer.array();
    }

    public boolean equals(@Nullable Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Icon icon = (Icon) object;
        if (!this.mBitmap.equals(icon.mBitmap) || !this.mId.equals(icon.mId)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result = 0;
        if (this.mBitmap != null) {
            result = this.mBitmap.hashCode();
        }
        if (this.mId != null) {
            return (result * 31) + this.mId.hashCode();
        }
        return result;
    }
}
