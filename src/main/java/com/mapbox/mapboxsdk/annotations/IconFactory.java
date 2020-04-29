package com.mapbox.mapboxsdk.annotations;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import com.mapbox.mapboxsdk.MapStrictMode;
import com.mapbox.mapboxsdk.R;
import com.mapbox.mapboxsdk.exceptions.TooManyIconsException;
import com.mapbox.mapboxsdk.utils.BitmapUtils;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@Deprecated
public final class IconFactory {
    private static final String ICON_ID_PREFIX = "com.mapbox.icons.icon_";
    @SuppressLint({"StaticFieldLeak"})
    private static IconFactory instance;
    private Context context;
    private Icon defaultMarker;
    private int nextId = 0;
    private BitmapFactory.Options options;

    public static synchronized IconFactory getInstance(@NonNull Context context2) {
        IconFactory iconFactory;
        synchronized (IconFactory.class) {
            if (instance == null) {
                instance = new IconFactory(context2.getApplicationContext());
            }
            iconFactory = instance;
        }
        return iconFactory;
    }

    private IconFactory(@NonNull Context context2) {
        this.context = context2;
        DisplayMetrics realMetrics = null;
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context2.getSystemService("window");
        if (Build.VERSION.SDK_INT >= 17) {
            realMetrics = new DisplayMetrics();
            wm.getDefaultDisplay().getRealMetrics(realMetrics);
        }
        wm.getDefaultDisplay().getMetrics(metrics);
        this.options = new BitmapFactory.Options();
        this.options.inScaled = true;
        this.options.inDensity = 160;
        this.options.inTargetDensity = metrics.densityDpi;
        if (realMetrics != null) {
            this.options.inScreenDensity = realMetrics.densityDpi;
        }
    }

    public Icon fromBitmap(@NonNull Bitmap bitmap) {
        if (this.nextId < 0) {
            throw new TooManyIconsException();
        }
        StringBuilder append = new StringBuilder().append(ICON_ID_PREFIX);
        int i = this.nextId + 1;
        this.nextId = i;
        return new Icon(append.append(i).toString(), bitmap);
    }

    public Icon fromResource(@DrawableRes int resourceId) {
        Drawable drawable = BitmapUtils.getDrawableFromRes(this.context, resourceId);
        if (drawable instanceof BitmapDrawable) {
            return fromBitmap(((BitmapDrawable) drawable).getBitmap());
        }
        throw new IllegalArgumentException("Failed to decode image. The resource provided must be a Bitmap.");
    }

    public Icon defaultMarker() {
        if (this.defaultMarker == null) {
            this.defaultMarker = fromResource(R.drawable.mapbox_marker_icon_default);
        }
        return this.defaultMarker;
    }

    private Icon fromInputStream(@NonNull InputStream is) {
        return fromBitmap(BitmapFactory.decodeStream(is, null, this.options));
    }

    public Icon fromAsset(@NonNull String assetName) {
        try {
            return fromInputStream(this.context.getAssets().open(assetName));
        } catch (IOException ioException) {
            MapStrictMode.strictModeViolation(ioException);
            return null;
        }
    }

    public Icon fromPath(@NonNull String absolutePath) {
        return fromBitmap(BitmapFactory.decodeFile(absolutePath, this.options));
    }

    public Icon fromFile(@NonNull String fileName) {
        try {
            return fromInputStream(this.context.openFileInput(fileName));
        } catch (FileNotFoundException fileNotFoundException) {
            MapStrictMode.strictModeViolation(fileNotFoundException);
            return null;
        }
    }

    public static Icon recreate(@NonNull String iconId, @NonNull Bitmap bitmap) {
        return new Icon(iconId, bitmap);
    }
}
