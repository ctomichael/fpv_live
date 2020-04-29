package com.mapbox.mapboxsdk.maps;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import java.util.HashMap;
import java.util.Map;

class IconManager {
    private int highestIconHeight;
    private int highestIconWidth;
    private final Map<Icon, Integer> iconMap = new HashMap();
    private NativeMap nativeMap;

    IconManager(NativeMap nativeMap2) {
        this.nativeMap = nativeMap2;
    }

    /* access modifiers changed from: package-private */
    public Icon loadIconForMarker(@NonNull Marker marker) {
        Icon icon = marker.getIcon();
        if (icon == null) {
            icon = loadDefaultIconForMarker(marker);
        } else {
            updateHighestIconSize(icon);
        }
        addIcon(icon);
        return icon;
    }

    /* access modifiers changed from: package-private */
    public int getTopOffsetPixelsForIcon(@NonNull Icon icon) {
        return (int) (this.nativeMap.getTopOffsetPixelsForAnnotationSymbol(icon.getId()) * ((double) this.nativeMap.getPixelRatio()));
    }

    /* access modifiers changed from: package-private */
    public int getHighestIconWidth() {
        return this.highestIconWidth;
    }

    /* access modifiers changed from: package-private */
    public int getHighestIconHeight() {
        return this.highestIconHeight;
    }

    private Icon loadDefaultIconForMarker(Marker marker) {
        Icon icon = IconFactory.getInstance(Mapbox.getApplicationContext()).defaultMarker();
        Bitmap bitmap = icon.getBitmap();
        updateHighestIconSize(bitmap.getWidth(), bitmap.getHeight() / 2);
        marker.setIcon(icon);
        return icon;
    }

    private void addIcon(@NonNull Icon icon) {
        addIcon(icon, true);
    }

    private void addIcon(@NonNull Icon icon, boolean addIconToMap) {
        if (!this.iconMap.keySet().contains(icon)) {
            this.iconMap.put(icon, 1);
            if (addIconToMap) {
                loadIcon(icon);
                return;
            }
            return;
        }
        this.iconMap.put(icon, Integer.valueOf(this.iconMap.get(icon).intValue() + 1));
    }

    private void updateHighestIconSize(Icon icon) {
        updateHighestIconSize(icon.getBitmap());
    }

    private void updateHighestIconSize(Bitmap bitmap) {
        updateHighestIconSize(bitmap.getWidth(), bitmap.getHeight());
    }

    private void updateHighestIconSize(int width, int height) {
        if (width > this.highestIconWidth) {
            this.highestIconWidth = width;
        }
        if (height > this.highestIconHeight) {
            this.highestIconHeight = height;
        }
    }

    private void loadIcon(Icon icon) {
        Bitmap bitmap = icon.getBitmap();
        this.nativeMap.addAnnotationIcon(icon.getId(), bitmap.getWidth(), bitmap.getHeight(), icon.getScale(), icon.toBytes());
    }

    /* access modifiers changed from: package-private */
    public void reloadIcons() {
        for (Icon icon : this.iconMap.keySet()) {
            loadIcon(icon);
        }
    }

    /* access modifiers changed from: package-private */
    public void ensureIconLoaded(@NonNull Marker marker, @NonNull MapboxMap mapboxMap) {
        Icon icon = marker.getIcon();
        if (icon == null) {
            icon = loadDefaultIconForMarker(marker);
        }
        addIcon(icon);
        setTopOffsetPixels(marker, mapboxMap, icon);
    }

    private void setTopOffsetPixels(Marker marker, @NonNull MapboxMap mapboxMap, @NonNull Icon icon) {
        Marker previousMarker = marker.getId() != -1 ? (Marker) mapboxMap.getAnnotation(marker.getId()) : null;
        if (previousMarker == null || previousMarker.getIcon() == null || previousMarker.getIcon() != marker.getIcon()) {
            marker.setTopOffsetPixels(getTopOffsetPixelsForIcon(icon));
        }
    }

    /* access modifiers changed from: package-private */
    public void iconCleanup(@NonNull Icon icon) {
        Integer refCounter = this.iconMap.get(icon);
        if (refCounter != null) {
            Integer refCounter2 = Integer.valueOf(refCounter.intValue() - 1);
            if (refCounter2.intValue() == 0) {
                remove(icon);
            } else {
                updateIconRefCounter(icon, refCounter2.intValue());
            }
        }
    }

    private void remove(Icon icon) {
        this.nativeMap.removeAnnotationIcon(icon.getId());
        this.iconMap.remove(icon);
    }

    private void updateIconRefCounter(Icon icon, int refCounter) {
        this.iconMap.put(icon, Integer.valueOf(refCounter));
    }
}
