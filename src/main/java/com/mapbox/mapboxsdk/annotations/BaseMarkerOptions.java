package com.mapbox.mapboxsdk.annotations;

import android.os.Parcelable;
import com.mapbox.mapboxsdk.annotations.BaseMarkerOptions;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.geometry.LatLng;

@Deprecated
public abstract class BaseMarkerOptions<U extends Marker, T extends BaseMarkerOptions<U, T>> implements Parcelable {
    protected Icon icon;
    protected LatLng position;
    protected String snippet;
    protected String title;

    public abstract U getMarker();

    public abstract T getThis();

    public T position(LatLng position2) {
        this.position = position2;
        return getThis();
    }

    public T snippet(String snippet2) {
        this.snippet = snippet2;
        return getThis();
    }

    public T title(String title2) {
        this.title = title2;
        return getThis();
    }

    public T icon(Icon icon2) {
        this.icon = icon2;
        return getThis();
    }

    public T setIcon(Icon icon2) {
        return icon(icon2);
    }

    public T setPosition(LatLng position2) {
        return position(position2);
    }

    public T setSnippet(String snippet2) {
        return snippet(snippet2);
    }

    public T setTitle(String title2) {
        return title(title2);
    }
}
