package com.mapbox.mapboxsdk.attribution;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class AttributionLayout {
    @Nullable
    private PointF anchorPoint;
    @Nullable
    private Bitmap logo;
    private boolean shortText;

    public AttributionLayout(@Nullable Bitmap logo2, @Nullable PointF anchorPoint2, boolean shortText2) {
        this.logo = logo2;
        this.anchorPoint = anchorPoint2;
        this.shortText = shortText2;
    }

    @Nullable
    public Bitmap getLogo() {
        return this.logo;
    }

    @Nullable
    public PointF getAnchorPoint() {
        return this.anchorPoint;
    }

    public boolean isShortText() {
        return this.shortText;
    }

    public boolean equals(@Nullable Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AttributionLayout that = (AttributionLayout) o;
        if (this.logo == null ? that.logo != null : !this.logo.equals(that.logo)) {
            return false;
        }
        if (this.anchorPoint != null) {
            return this.anchorPoint.equals(that.anchorPoint);
        }
        if (that.anchorPoint != null) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result;
        int i = 0;
        if (this.logo != null) {
            result = this.logo.hashCode();
        } else {
            result = 0;
        }
        int i2 = result * 31;
        if (this.anchorPoint != null) {
            i = this.anchorPoint.hashCode();
        }
        return i2 + i;
    }

    @NonNull
    public String toString() {
        return "AttributionLayout{logo=" + this.logo + ", anchorPoint=" + this.anchorPoint + '}';
    }
}
