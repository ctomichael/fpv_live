package com.mapbox.mapboxsdk.location;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.log.Logger;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Projection;

public final class Utils {
    private static final String TAG = "Mbgl-com.mapbox.mapboxsdk.location.Utils";

    private Utils() {
    }

    public static float shortestRotation(float heading, float previousHeading) {
        double diff = (double) (previousHeading - heading);
        if (diff > 180.0d) {
            return heading + 360.0f;
        }
        if (diff < -180.0d) {
            return heading - 360.0f;
        }
        return heading;
    }

    public static float normalize(float angle) {
        return ((angle % 360.0f) + 360.0f) % 360.0f;
    }

    static Bitmap generateShadow(Drawable drawable, float elevation) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        try {
            drawable.draw(canvas);
            return Bitmap.createScaledBitmap(bitmap, toEven(((float) width) + elevation), toEven(((float) height) + elevation), false);
        } catch (IllegalArgumentException ex) {
            if (!ex.getMessage().equals("radius must be > 0") || Build.VERSION.SDK_INT >= 21) {
                throw ex;
            }
            Logger.w(TAG, "Location's shadow gradient drawable has a radius <= 0px, resetting to 1px in order to avoid crashing");
            ensureShadowGradientRadius(drawable);
            return generateShadow(drawable, elevation);
        }
    }

    private static void ensureShadowGradientRadius(Drawable drawable) {
        if (drawable instanceof GradientDrawable) {
            ((GradientDrawable) drawable).setGradientRadius(1.0f);
        } else if (drawable instanceof LayerDrawable) {
            LayerDrawable layerDrawable = (LayerDrawable) drawable;
            for (int i = 0; i < layerDrawable.getNumberOfLayers(); i++) {
                Drawable layers = layerDrawable.getDrawable(i);
                if (layers instanceof GradientDrawable) {
                    ((GradientDrawable) layers).setGradientRadius(1.0f);
                }
            }
        }
    }

    static float calculateZoomLevelRadius(@NonNull MapboxMap mapboxMap, @Nullable Location location) {
        if (location == null) {
            return 0.0f;
        }
        return (float) (((double) location.getAccuracy()) * (1.0d / mapboxMap.getProjection().getMetersPerPixelAtLatitude(location.getLatitude())));
    }

    static boolean immediateAnimation(@NonNull Projection projection, @NonNull LatLng current, @NonNull LatLng target) {
        return current.distanceTo(target) / projection.getMetersPerPixelAtLatitude((current.getLatitude() + target.getLatitude()) / 2.0d) > 50000.0d;
    }

    private static int toEven(float value) {
        int i = (int) (0.5f + value);
        if (i % 2 == 1) {
            return i - 1;
        }
        return i;
    }
}
