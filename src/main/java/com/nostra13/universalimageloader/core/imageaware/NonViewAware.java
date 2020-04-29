package com.nostra13.universalimageloader.core.imageaware;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.ViewScaleType;

public class NonViewAware implements ImageAware {
    protected final ImageSize imageSize;
    protected final String imageUri;
    protected final ViewScaleType scaleType;

    public NonViewAware(ImageSize imageSize2, ViewScaleType scaleType2) {
        this(null, imageSize2, scaleType2);
    }

    public NonViewAware(String imageUri2, ImageSize imageSize2, ViewScaleType scaleType2) {
        if (imageSize2 == null) {
            throw new IllegalArgumentException("imageSize must not be null");
        } else if (scaleType2 == null) {
            throw new IllegalArgumentException("scaleType must not be null");
        } else {
            this.imageUri = imageUri2;
            this.imageSize = imageSize2;
            this.scaleType = scaleType2;
        }
    }

    public int getWidth() {
        return this.imageSize.getWidth();
    }

    public int getHeight() {
        return this.imageSize.getHeight();
    }

    public ViewScaleType getScaleType() {
        return this.scaleType;
    }

    public View getWrappedView() {
        return null;
    }

    public boolean isCollected() {
        return false;
    }

    public int getId() {
        return TextUtils.isEmpty(this.imageUri) ? super.hashCode() : this.imageUri.hashCode();
    }

    public boolean setImageDrawable(Drawable drawable) {
        return true;
    }

    public boolean setImageBitmap(Bitmap bitmap) {
        return true;
    }
}
