package com.mapbox.mapboxsdk.style.sources;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.geometry.LatLngQuad;
import com.mapbox.mapboxsdk.utils.BitmapUtils;
import java.net.URI;
import java.net.URL;

@UiThread
public class ImageSource extends Source {
    /* access modifiers changed from: protected */
    @Keep
    public native void finalize() throws Throwable;

    /* access modifiers changed from: protected */
    @Keep
    public native void initialize(String str, LatLngQuad latLngQuad);

    /* access modifiers changed from: protected */
    @Keep
    @NonNull
    public native String nativeGetUrl();

    /* access modifiers changed from: protected */
    @Keep
    public native void nativeSetCoordinates(LatLngQuad latLngQuad);

    /* access modifiers changed from: protected */
    @Keep
    public native void nativeSetImage(Bitmap bitmap);

    /* access modifiers changed from: protected */
    @Keep
    public native void nativeSetUrl(String str);

    @Keep
    ImageSource(long nativePtr) {
        super(nativePtr);
    }

    @Deprecated
    public ImageSource(String id, LatLngQuad coordinates, @NonNull URL url) {
        initialize(id, coordinates);
        setUrl(url);
    }

    public ImageSource(String id, LatLngQuad coordinates, @NonNull URI uri) {
        initialize(id, coordinates);
        setUri(uri);
    }

    public ImageSource(String id, LatLngQuad coordinates, @NonNull Bitmap bitmap) {
        initialize(id, coordinates);
        setImage(bitmap);
    }

    public ImageSource(String id, LatLngQuad coordinates, @DrawableRes int resourceId) {
        initialize(id, coordinates);
        setImage(resourceId);
    }

    @Deprecated
    public void setUrl(@NonNull URL url) {
        setUrl(url.toExternalForm());
    }

    @Deprecated
    public void setUrl(String url) {
        checkThread();
        nativeSetUrl(url);
    }

    public void setUri(@NonNull URI uri) {
        checkThread();
        nativeSetUrl(uri.toString());
    }

    public void setUri(String uri) {
        checkThread();
        nativeSetUrl(uri);
    }

    public void setImage(@NonNull Bitmap bitmap) {
        checkThread();
        nativeSetImage(bitmap);
    }

    public void setImage(@DrawableRes int resourceId) throws IllegalArgumentException {
        checkThread();
        Drawable drawable = BitmapUtils.getDrawableFromRes(Mapbox.getApplicationContext(), resourceId);
        if (drawable instanceof BitmapDrawable) {
            nativeSetImage(((BitmapDrawable) drawable).getBitmap());
            return;
        }
        throw new IllegalArgumentException("Failed to decode image. The resource provided must be a Bitmap.");
    }

    @Nullable
    @Deprecated
    public String getUrl() {
        checkThread();
        return nativeGetUrl();
    }

    @Nullable
    public String getUri() {
        checkThread();
        return nativeGetUrl();
    }

    public void setCoordinates(LatLngQuad latLngQuad) {
        checkThread();
        nativeSetCoordinates(latLngQuad);
    }
}
