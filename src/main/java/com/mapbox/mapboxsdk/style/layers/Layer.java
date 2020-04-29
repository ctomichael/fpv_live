package com.mapbox.mapboxsdk.style.layers;

import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.gson.JsonElement;
import com.mapbox.mapboxsdk.LibraryLoader;
import com.mapbox.mapboxsdk.style.expressions.Expression;
import com.mapbox.mapboxsdk.style.types.Formatted;
import com.mapbox.mapboxsdk.utils.ThreadUtils;

public abstract class Layer {
    private static final String TAG = "Mbgl-Layer";
    private boolean detached;
    @Keep
    private boolean invalidated;
    @Keep
    private long nativePtr;

    /* access modifiers changed from: protected */
    @Keep
    public native void finalize() throws Throwable;

    /* access modifiers changed from: protected */
    @Keep
    @Nullable
    public native JsonElement nativeGetFilter();

    /* access modifiers changed from: protected */
    @Keep
    @NonNull
    public native String nativeGetId();

    /* access modifiers changed from: protected */
    @Keep
    public native float nativeGetMaxZoom();

    /* access modifiers changed from: protected */
    @Keep
    public native float nativeGetMinZoom();

    /* access modifiers changed from: protected */
    @Keep
    @NonNull
    public native String nativeGetSourceId();

    /* access modifiers changed from: protected */
    @Keep
    @NonNull
    public native String nativeGetSourceLayer();

    /* access modifiers changed from: protected */
    @Keep
    @NonNull
    public native Object nativeGetVisibility();

    /* access modifiers changed from: protected */
    @Keep
    public native void nativeSetFilter(Object[] objArr);

    /* access modifiers changed from: protected */
    @Keep
    public native void nativeSetLayoutProperty(String str, Object obj);

    /* access modifiers changed from: protected */
    @Keep
    public native void nativeSetMaxZoom(float f);

    /* access modifiers changed from: protected */
    @Keep
    public native void nativeSetMinZoom(float f);

    /* access modifiers changed from: protected */
    @Keep
    public native void nativeSetPaintProperty(String str, Object obj);

    /* access modifiers changed from: protected */
    @Keep
    public native void nativeSetSourceLayer(String str);

    static {
        LibraryLoader.load();
    }

    @Keep
    protected Layer(long nativePtr2) {
        checkThread();
        this.nativePtr = nativePtr2;
    }

    public Layer() {
        checkThread();
    }

    /* access modifiers changed from: protected */
    public void checkThread() {
        ThreadUtils.checkThread(TAG);
    }

    public void setProperties(@NonNull PropertyValue<?>... properties) {
        if (!this.detached) {
            checkThread();
            if (properties.length != 0) {
                for (PropertyValue<?> property : properties) {
                    Object converted = convertValue(property.value);
                    if (property instanceof PaintPropertyValue) {
                        nativeSetPaintProperty(property.name, converted);
                    } else {
                        nativeSetLayoutProperty(property.name, converted);
                    }
                }
            }
        }
    }

    @NonNull
    public String getId() {
        checkThread();
        return nativeGetId();
    }

    @NonNull
    public PropertyValue<String> getVisibility() {
        checkThread();
        return new PaintPropertyValue("visibility", (String) nativeGetVisibility());
    }

    public float getMinZoom() {
        checkThread();
        return nativeGetMinZoom();
    }

    public float getMaxZoom() {
        checkThread();
        return nativeGetMaxZoom();
    }

    public void setMinZoom(float zoom) {
        checkThread();
        nativeSetMinZoom(zoom);
    }

    public void setMaxZoom(float zoom) {
        checkThread();
        nativeSetMaxZoom(zoom);
    }

    public long getNativePtr() {
        return this.nativePtr;
    }

    @Nullable
    private Object convertValue(@Nullable Object value) {
        if (value instanceof Expression) {
            return ((Expression) value).toArray();
        }
        if (value instanceof Formatted) {
            return ((Formatted) value).toArray();
        }
        return value;
    }

    public void setDetached() {
        this.detached = true;
    }

    public boolean isDetached() {
        return this.detached;
    }
}
