package com.mapbox.mapboxsdk.maps;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.content.res.ResourcesCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import com.mapbox.mapboxsdk.R;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.constants.MapboxConstants;
import com.mapbox.mapboxsdk.utils.BitmapUtils;
import com.mapbox.mapboxsdk.utils.FontUtils;
import java.util.Arrays;

public class MapboxMapOptions implements Parcelable {
    public static final Parcelable.Creator<MapboxMapOptions> CREATOR = new Parcelable.Creator<MapboxMapOptions>() {
        /* class com.mapbox.mapboxsdk.maps.MapboxMapOptions.AnonymousClass1 */

        public MapboxMapOptions createFromParcel(@NonNull Parcel in2) {
            return new MapboxMapOptions(in2);
        }

        public MapboxMapOptions[] newArray(int size) {
            return new MapboxMapOptions[size];
        }
    };
    private static final float FOUR_DP = 4.0f;
    private static final int LIGHT_GRAY = -988703;
    private static final float NINETY_TWO_DP = 92.0f;
    private static final int UNDEFINED_COLOR = -1;
    private String apiBaseUri;
    private boolean attributionEnabled;
    private int attributionGravity;
    private int[] attributionMargins;
    @ColorInt
    private int attributionTintColor;
    private CameraPosition cameraPosition;
    private boolean compassEnabled;
    private int compassGravity;
    private Drawable compassImage;
    private int[] compassMargins;
    private boolean crossSourceCollisions;
    private boolean debugActive;
    private boolean doubleTapGesturesEnabled;
    private boolean fadeCompassFacingNorth;
    @ColorInt
    private int foregroundLoadColor;
    private String[] localIdeographFontFamilies;
    private String localIdeographFontFamily;
    private boolean localIdeographFontFamilyEnabled;
    private boolean logoEnabled;
    private int logoGravity;
    private int[] logoMargins;
    private double maxZoom;
    private double minZoom;
    private float pixelRatio;
    private boolean prefetchesTiles;
    private boolean quickZoomGesturesEnabled;
    private boolean rotateGesturesEnabled;
    private boolean scrollGesturesEnabled;
    private boolean textureMode;
    private boolean tiltGesturesEnabled;
    private boolean translucentTextureSurface;
    private boolean zMediaOverlay;
    private boolean zoomGesturesEnabled;

    @Deprecated
    public MapboxMapOptions() {
        this.compassEnabled = true;
        this.fadeCompassFacingNorth = true;
        this.compassGravity = 8388661;
        this.logoEnabled = true;
        this.logoGravity = 8388691;
        this.attributionTintColor = -1;
        this.attributionEnabled = true;
        this.attributionGravity = 8388691;
        this.minZoom = 0.0d;
        this.maxZoom = 25.5d;
        this.rotateGesturesEnabled = true;
        this.scrollGesturesEnabled = true;
        this.tiltGesturesEnabled = true;
        this.zoomGesturesEnabled = true;
        this.doubleTapGesturesEnabled = true;
        this.quickZoomGesturesEnabled = true;
        this.prefetchesTiles = true;
        this.zMediaOverlay = false;
        this.localIdeographFontFamilyEnabled = true;
        this.crossSourceCollisions = true;
    }

    private MapboxMapOptions(Parcel in2) {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        boolean z5;
        boolean z6;
        boolean z7;
        boolean z8;
        boolean z9;
        boolean z10;
        boolean z11;
        boolean z12;
        boolean z13;
        boolean z14;
        boolean z15;
        boolean z16;
        boolean z17 = true;
        this.compassEnabled = true;
        this.fadeCompassFacingNorth = true;
        this.compassGravity = 8388661;
        this.logoEnabled = true;
        this.logoGravity = 8388691;
        this.attributionTintColor = -1;
        this.attributionEnabled = true;
        this.attributionGravity = 8388691;
        this.minZoom = 0.0d;
        this.maxZoom = 25.5d;
        this.rotateGesturesEnabled = true;
        this.scrollGesturesEnabled = true;
        this.tiltGesturesEnabled = true;
        this.zoomGesturesEnabled = true;
        this.doubleTapGesturesEnabled = true;
        this.quickZoomGesturesEnabled = true;
        this.prefetchesTiles = true;
        this.zMediaOverlay = false;
        this.localIdeographFontFamilyEnabled = true;
        this.crossSourceCollisions = true;
        this.cameraPosition = (CameraPosition) in2.readParcelable(CameraPosition.class.getClassLoader());
        if (in2.readByte() != 0) {
            z = true;
        } else {
            z = false;
        }
        this.debugActive = z;
        if (in2.readByte() != 0) {
            z2 = true;
        } else {
            z2 = false;
        }
        this.compassEnabled = z2;
        this.compassGravity = in2.readInt();
        this.compassMargins = in2.createIntArray();
        if (in2.readByte() != 0) {
            z3 = true;
        } else {
            z3 = false;
        }
        this.fadeCompassFacingNorth = z3;
        Bitmap compassBitmap = (Bitmap) in2.readParcelable(getClass().getClassLoader());
        if (compassBitmap != null) {
            this.compassImage = new BitmapDrawable(compassBitmap);
        }
        if (in2.readByte() != 0) {
            z4 = true;
        } else {
            z4 = false;
        }
        this.logoEnabled = z4;
        this.logoGravity = in2.readInt();
        this.logoMargins = in2.createIntArray();
        if (in2.readByte() != 0) {
            z5 = true;
        } else {
            z5 = false;
        }
        this.attributionEnabled = z5;
        this.attributionGravity = in2.readInt();
        this.attributionMargins = in2.createIntArray();
        this.attributionTintColor = in2.readInt();
        this.minZoom = in2.readDouble();
        this.maxZoom = in2.readDouble();
        if (in2.readByte() != 0) {
            z6 = true;
        } else {
            z6 = false;
        }
        this.rotateGesturesEnabled = z6;
        if (in2.readByte() != 0) {
            z7 = true;
        } else {
            z7 = false;
        }
        this.scrollGesturesEnabled = z7;
        if (in2.readByte() != 0) {
            z8 = true;
        } else {
            z8 = false;
        }
        this.tiltGesturesEnabled = z8;
        if (in2.readByte() != 0) {
            z9 = true;
        } else {
            z9 = false;
        }
        this.zoomGesturesEnabled = z9;
        if (in2.readByte() != 0) {
            z10 = true;
        } else {
            z10 = false;
        }
        this.doubleTapGesturesEnabled = z10;
        if (in2.readByte() != 0) {
            z11 = true;
        } else {
            z11 = false;
        }
        this.quickZoomGesturesEnabled = z11;
        this.apiBaseUri = in2.readString();
        if (in2.readByte() != 0) {
            z12 = true;
        } else {
            z12 = false;
        }
        this.textureMode = z12;
        if (in2.readByte() != 0) {
            z13 = true;
        } else {
            z13 = false;
        }
        this.translucentTextureSurface = z13;
        if (in2.readByte() != 0) {
            z14 = true;
        } else {
            z14 = false;
        }
        this.prefetchesTiles = z14;
        if (in2.readByte() != 0) {
            z15 = true;
        } else {
            z15 = false;
        }
        this.zMediaOverlay = z15;
        if (in2.readByte() != 0) {
            z16 = true;
        } else {
            z16 = false;
        }
        this.localIdeographFontFamilyEnabled = z16;
        this.localIdeographFontFamily = in2.readString();
        this.localIdeographFontFamilies = in2.createStringArray();
        this.pixelRatio = in2.readFloat();
        this.foregroundLoadColor = in2.readInt();
        this.crossSourceCollisions = in2.readByte() == 0 ? false : z17;
    }

    @NonNull
    public static MapboxMapOptions createFromAttributes(@NonNull Context context) {
        return createFromAttributes(context, null);
    }

    @NonNull
    public static MapboxMapOptions createFromAttributes(@NonNull Context context, @Nullable AttributeSet attrs) {
        return createFromAttributes(new MapboxMapOptions(), context, context.obtainStyledAttributes(attrs, R.styleable.mapbox_MapView, 0, 0));
    }

    @VisibleForTesting
    static MapboxMapOptions createFromAttributes(@NonNull MapboxMapOptions mapboxMapOptions, @NonNull Context context, @Nullable TypedArray typedArray) {
        float pxlRatio = context.getResources().getDisplayMetrics().density;
        try {
            mapboxMapOptions.camera(new CameraPosition.Builder(typedArray).build());
            mapboxMapOptions.apiBaseUrl(typedArray.getString(R.styleable.mapbox_MapView_mapbox_apiBaseUrl));
            String baseUri = typedArray.getString(R.styleable.mapbox_MapView_mapbox_apiBaseUri);
            if (!TextUtils.isEmpty(baseUri)) {
                mapboxMapOptions.apiBaseUri(baseUri);
            }
            mapboxMapOptions.zoomGesturesEnabled(typedArray.getBoolean(R.styleable.mapbox_MapView_mapbox_uiZoomGestures, true));
            mapboxMapOptions.scrollGesturesEnabled(typedArray.getBoolean(R.styleable.mapbox_MapView_mapbox_uiScrollGestures, true));
            mapboxMapOptions.rotateGesturesEnabled(typedArray.getBoolean(R.styleable.mapbox_MapView_mapbox_uiRotateGestures, true));
            mapboxMapOptions.tiltGesturesEnabled(typedArray.getBoolean(R.styleable.mapbox_MapView_mapbox_uiTiltGestures, true));
            mapboxMapOptions.doubleTapGesturesEnabled(typedArray.getBoolean(R.styleable.mapbox_MapView_mapbox_uiDoubleTapGestures, true));
            mapboxMapOptions.quickZoomGesturesEnabled(typedArray.getBoolean(R.styleable.mapbox_MapView_mapbox_uiQuickZoomGestures, true));
            mapboxMapOptions.maxZoomPreference((double) typedArray.getFloat(R.styleable.mapbox_MapView_mapbox_cameraZoomMax, 25.5f));
            mapboxMapOptions.minZoomPreference((double) typedArray.getFloat(R.styleable.mapbox_MapView_mapbox_cameraZoomMin, 0.0f));
            mapboxMapOptions.compassEnabled(typedArray.getBoolean(R.styleable.mapbox_MapView_mapbox_uiCompass, true));
            mapboxMapOptions.compassGravity(typedArray.getInt(R.styleable.mapbox_MapView_mapbox_uiCompassGravity, 8388661));
            mapboxMapOptions.compassMargins(new int[]{(int) typedArray.getDimension(R.styleable.mapbox_MapView_mapbox_uiCompassMarginLeft, 4.0f * pxlRatio), (int) typedArray.getDimension(R.styleable.mapbox_MapView_mapbox_uiCompassMarginTop, 4.0f * pxlRatio), (int) typedArray.getDimension(R.styleable.mapbox_MapView_mapbox_uiCompassMarginRight, 4.0f * pxlRatio), (int) typedArray.getDimension(R.styleable.mapbox_MapView_mapbox_uiCompassMarginBottom, 4.0f * pxlRatio)});
            mapboxMapOptions.compassFadesWhenFacingNorth(typedArray.getBoolean(R.styleable.mapbox_MapView_mapbox_uiCompassFadeFacingNorth, true));
            Drawable compassDrawable = typedArray.getDrawable(R.styleable.mapbox_MapView_mapbox_uiCompassDrawable);
            if (compassDrawable == null) {
                compassDrawable = ResourcesCompat.getDrawable(context.getResources(), R.drawable.mapbox_compass_icon, null);
            }
            mapboxMapOptions.compassImage(compassDrawable);
            mapboxMapOptions.logoEnabled(typedArray.getBoolean(R.styleable.mapbox_MapView_mapbox_uiLogo, true));
            mapboxMapOptions.logoGravity(typedArray.getInt(R.styleable.mapbox_MapView_mapbox_uiLogoGravity, 8388691));
            mapboxMapOptions.logoMargins(new int[]{(int) typedArray.getDimension(R.styleable.mapbox_MapView_mapbox_uiLogoMarginLeft, 4.0f * pxlRatio), (int) typedArray.getDimension(R.styleable.mapbox_MapView_mapbox_uiLogoMarginTop, 4.0f * pxlRatio), (int) typedArray.getDimension(R.styleable.mapbox_MapView_mapbox_uiLogoMarginRight, 4.0f * pxlRatio), (int) typedArray.getDimension(R.styleable.mapbox_MapView_mapbox_uiLogoMarginBottom, 4.0f * pxlRatio)});
            mapboxMapOptions.attributionTintColor(typedArray.getColor(R.styleable.mapbox_MapView_mapbox_uiAttributionTintColor, -1));
            mapboxMapOptions.attributionEnabled(typedArray.getBoolean(R.styleable.mapbox_MapView_mapbox_uiAttribution, true));
            mapboxMapOptions.attributionGravity(typedArray.getInt(R.styleable.mapbox_MapView_mapbox_uiAttributionGravity, 8388691));
            mapboxMapOptions.attributionMargins(new int[]{(int) typedArray.getDimension(R.styleable.mapbox_MapView_mapbox_uiAttributionMarginLeft, NINETY_TWO_DP * pxlRatio), (int) typedArray.getDimension(R.styleable.mapbox_MapView_mapbox_uiAttributionMarginTop, 4.0f * pxlRatio), (int) typedArray.getDimension(R.styleable.mapbox_MapView_mapbox_uiAttributionMarginRight, 4.0f * pxlRatio), (int) typedArray.getDimension(R.styleable.mapbox_MapView_mapbox_uiAttributionMarginBottom, 4.0f * pxlRatio)});
            mapboxMapOptions.textureMode(typedArray.getBoolean(R.styleable.mapbox_MapView_mapbox_renderTextureMode, false));
            mapboxMapOptions.translucentTextureSurface(typedArray.getBoolean(R.styleable.mapbox_MapView_mapbox_renderTextureTranslucentSurface, false));
            mapboxMapOptions.setPrefetchesTiles(typedArray.getBoolean(R.styleable.mapbox_MapView_mapbox_enableTilePrefetch, true));
            mapboxMapOptions.renderSurfaceOnTop(typedArray.getBoolean(R.styleable.mapbox_MapView_mapbox_enableZMediaOverlay, false));
            mapboxMapOptions.localIdeographFontFamilyEnabled = typedArray.getBoolean(R.styleable.mapbox_MapView_mapbox_localIdeographEnabled, true);
            int localIdeographFontFamiliesResId = typedArray.getResourceId(R.styleable.mapbox_MapView_mapbox_localIdeographFontFamilies, 0);
            if (localIdeographFontFamiliesResId != 0) {
                mapboxMapOptions.localIdeographFontFamily(context.getResources().getStringArray(localIdeographFontFamiliesResId));
            } else {
                String localIdeographFontFamily2 = typedArray.getString(R.styleable.mapbox_MapView_mapbox_localIdeographFontFamily);
                if (localIdeographFontFamily2 == null) {
                    localIdeographFontFamily2 = MapboxConstants.DEFAULT_FONT;
                }
                mapboxMapOptions.localIdeographFontFamily(localIdeographFontFamily2);
            }
            mapboxMapOptions.pixelRatio(typedArray.getFloat(R.styleable.mapbox_MapView_mapbox_pixelRatio, 0.0f));
            mapboxMapOptions.foregroundLoadColor(typedArray.getInt(R.styleable.mapbox_MapView_mapbox_foregroundLoadColor, LIGHT_GRAY));
            mapboxMapOptions.crossSourceCollisions(typedArray.getBoolean(R.styleable.mapbox_MapView_mapbox_cross_source_collisions, true));
            return mapboxMapOptions;
        } finally {
            typedArray.recycle();
        }
    }

    @Deprecated
    @NonNull
    public MapboxMapOptions apiBaseUrl(String apiBaseUrl) {
        this.apiBaseUri = apiBaseUrl;
        return this;
    }

    @NonNull
    public MapboxMapOptions apiBaseUri(String apiBaseUri2) {
        this.apiBaseUri = apiBaseUri2;
        return this;
    }

    @NonNull
    public MapboxMapOptions camera(CameraPosition cameraPosition2) {
        this.cameraPosition = cameraPosition2;
        return this;
    }

    @NonNull
    public MapboxMapOptions debugActive(boolean enabled) {
        this.debugActive = enabled;
        return this;
    }

    @NonNull
    public MapboxMapOptions minZoomPreference(double minZoom2) {
        this.minZoom = minZoom2;
        return this;
    }

    @NonNull
    public MapboxMapOptions maxZoomPreference(double maxZoom2) {
        this.maxZoom = maxZoom2;
        return this;
    }

    @NonNull
    public MapboxMapOptions compassEnabled(boolean enabled) {
        this.compassEnabled = enabled;
        return this;
    }

    @NonNull
    public MapboxMapOptions compassGravity(int gravity) {
        this.compassGravity = gravity;
        return this;
    }

    @NonNull
    public MapboxMapOptions compassMargins(int[] margins) {
        this.compassMargins = margins;
        return this;
    }

    @NonNull
    public MapboxMapOptions compassFadesWhenFacingNorth(boolean compassFadeWhenFacingNorth) {
        this.fadeCompassFacingNorth = compassFadeWhenFacingNorth;
        return this;
    }

    @NonNull
    public MapboxMapOptions compassImage(Drawable compass) {
        this.compassImage = compass;
        return this;
    }

    @NonNull
    public MapboxMapOptions logoEnabled(boolean enabled) {
        this.logoEnabled = enabled;
        return this;
    }

    @NonNull
    public MapboxMapOptions logoGravity(int gravity) {
        this.logoGravity = gravity;
        return this;
    }

    @NonNull
    public MapboxMapOptions logoMargins(int[] margins) {
        this.logoMargins = margins;
        return this;
    }

    @NonNull
    public MapboxMapOptions attributionEnabled(boolean enabled) {
        this.attributionEnabled = enabled;
        return this;
    }

    @NonNull
    public MapboxMapOptions attributionGravity(int gravity) {
        this.attributionGravity = gravity;
        return this;
    }

    @NonNull
    public MapboxMapOptions attributionMargins(int[] margins) {
        this.attributionMargins = margins;
        return this;
    }

    @NonNull
    public MapboxMapOptions attributionTintColor(@ColorInt int color) {
        this.attributionTintColor = color;
        return this;
    }

    @NonNull
    public MapboxMapOptions rotateGesturesEnabled(boolean enabled) {
        this.rotateGesturesEnabled = enabled;
        return this;
    }

    @NonNull
    public MapboxMapOptions scrollGesturesEnabled(boolean enabled) {
        this.scrollGesturesEnabled = enabled;
        return this;
    }

    @NonNull
    public MapboxMapOptions tiltGesturesEnabled(boolean enabled) {
        this.tiltGesturesEnabled = enabled;
        return this;
    }

    @NonNull
    public MapboxMapOptions zoomGesturesEnabled(boolean enabled) {
        this.zoomGesturesEnabled = enabled;
        return this;
    }

    @NonNull
    public MapboxMapOptions doubleTapGesturesEnabled(boolean enabled) {
        this.doubleTapGesturesEnabled = enabled;
        return this;
    }

    @NonNull
    public MapboxMapOptions quickZoomGesturesEnabled(boolean enabled) {
        this.quickZoomGesturesEnabled = enabled;
        return this;
    }

    @NonNull
    public MapboxMapOptions textureMode(boolean textureMode2) {
        this.textureMode = textureMode2;
        return this;
    }

    @NonNull
    public MapboxMapOptions translucentTextureSurface(boolean translucentTextureSurface2) {
        this.translucentTextureSurface = translucentTextureSurface2;
        return this;
    }

    @NonNull
    public MapboxMapOptions foregroundLoadColor(@ColorInt int loadColor) {
        this.foregroundLoadColor = loadColor;
        return this;
    }

    @NonNull
    public MapboxMapOptions setPrefetchesTiles(boolean enable) {
        this.prefetchesTiles = enable;
        return this;
    }

    @NonNull
    public MapboxMapOptions crossSourceCollisions(boolean crossSourceCollisions2) {
        this.crossSourceCollisions = crossSourceCollisions2;
        return this;
    }

    @NonNull
    public MapboxMapOptions localIdeographFontFamilyEnabled(boolean enabled) {
        this.localIdeographFontFamilyEnabled = enabled;
        return this;
    }

    @NonNull
    public MapboxMapOptions localIdeographFontFamily(String fontFamily) {
        this.localIdeographFontFamily = FontUtils.extractValidFont(fontFamily);
        return this;
    }

    @NonNull
    public MapboxMapOptions localIdeographFontFamily(String... fontFamilies) {
        this.localIdeographFontFamily = FontUtils.extractValidFont(fontFamilies);
        return this;
    }

    @NonNull
    public MapboxMapOptions pixelRatio(float pixelRatio2) {
        this.pixelRatio = pixelRatio2;
        return this;
    }

    public boolean getPrefetchesTiles() {
        return this.prefetchesTiles;
    }

    public boolean getCrossSourceCollisions() {
        return this.crossSourceCollisions;
    }

    public void renderSurfaceOnTop(boolean renderOnTop) {
        this.zMediaOverlay = renderOnTop;
    }

    public boolean getRenderSurfaceOnTop() {
        return this.zMediaOverlay;
    }

    @Deprecated
    public String getApiBaseUrl() {
        return this.apiBaseUri;
    }

    public String getApiBaseUri() {
        return this.apiBaseUri;
    }

    public CameraPosition getCamera() {
        return this.cameraPosition;
    }

    public double getMinZoomPreference() {
        return this.minZoom;
    }

    public double getMaxZoomPreference() {
        return this.maxZoom;
    }

    public boolean getCompassEnabled() {
        return this.compassEnabled;
    }

    public int getCompassGravity() {
        return this.compassGravity;
    }

    public int[] getCompassMargins() {
        return this.compassMargins;
    }

    public boolean getCompassFadeFacingNorth() {
        return this.fadeCompassFacingNorth;
    }

    public Drawable getCompassImage() {
        return this.compassImage;
    }

    public boolean getLogoEnabled() {
        return this.logoEnabled;
    }

    public int getLogoGravity() {
        return this.logoGravity;
    }

    public int[] getLogoMargins() {
        return this.logoMargins;
    }

    public boolean getRotateGesturesEnabled() {
        return this.rotateGesturesEnabled;
    }

    public boolean getScrollGesturesEnabled() {
        return this.scrollGesturesEnabled;
    }

    public boolean getTiltGesturesEnabled() {
        return this.tiltGesturesEnabled;
    }

    public boolean getZoomGesturesEnabled() {
        return this.zoomGesturesEnabled;
    }

    public boolean getDoubleTapGesturesEnabled() {
        return this.doubleTapGesturesEnabled;
    }

    public boolean getQuickZoomGesturesEnabled() {
        return this.quickZoomGesturesEnabled;
    }

    public boolean getAttributionEnabled() {
        return this.attributionEnabled;
    }

    public int getAttributionGravity() {
        return this.attributionGravity;
    }

    public int[] getAttributionMargins() {
        return this.attributionMargins;
    }

    @ColorInt
    public int getAttributionTintColor() {
        return this.attributionTintColor;
    }

    public boolean getDebugActive() {
        return this.debugActive;
    }

    public boolean getTextureMode() {
        return this.textureMode;
    }

    public boolean getTranslucentTextureSurface() {
        return this.translucentTextureSurface;
    }

    @ColorInt
    public int getForegroundLoadColor() {
        return this.foregroundLoadColor;
    }

    @Nullable
    public String getLocalIdeographFontFamily() {
        if (this.localIdeographFontFamilyEnabled) {
            return this.localIdeographFontFamily;
        }
        return null;
    }

    public boolean isLocalIdeographFontFamilyEnabled() {
        return this.localIdeographFontFamilyEnabled;
    }

    public float getPixelRatio() {
        return this.pixelRatio;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(@NonNull Parcel dest, int flags) {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        int i11;
        int i12;
        int i13;
        int i14;
        int i15;
        int i16 = 1;
        dest.writeParcelable(this.cameraPosition, flags);
        dest.writeByte((byte) (this.debugActive ? 1 : 0));
        if (this.compassEnabled) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeByte((byte) i);
        dest.writeInt(this.compassGravity);
        dest.writeIntArray(this.compassMargins);
        if (this.fadeCompassFacingNorth) {
            i2 = 1;
        } else {
            i2 = 0;
        }
        dest.writeByte((byte) i2);
        dest.writeParcelable(this.compassImage != null ? BitmapUtils.getBitmapFromDrawable(this.compassImage) : null, flags);
        if (this.logoEnabled) {
            i3 = 1;
        } else {
            i3 = 0;
        }
        dest.writeByte((byte) i3);
        dest.writeInt(this.logoGravity);
        dest.writeIntArray(this.logoMargins);
        if (this.attributionEnabled) {
            i4 = 1;
        } else {
            i4 = 0;
        }
        dest.writeByte((byte) i4);
        dest.writeInt(this.attributionGravity);
        dest.writeIntArray(this.attributionMargins);
        dest.writeInt(this.attributionTintColor);
        dest.writeDouble(this.minZoom);
        dest.writeDouble(this.maxZoom);
        if (this.rotateGesturesEnabled) {
            i5 = 1;
        } else {
            i5 = 0;
        }
        dest.writeByte((byte) i5);
        if (this.scrollGesturesEnabled) {
            i6 = 1;
        } else {
            i6 = 0;
        }
        dest.writeByte((byte) i6);
        if (this.tiltGesturesEnabled) {
            i7 = 1;
        } else {
            i7 = 0;
        }
        dest.writeByte((byte) i7);
        if (this.zoomGesturesEnabled) {
            i8 = 1;
        } else {
            i8 = 0;
        }
        dest.writeByte((byte) i8);
        if (this.doubleTapGesturesEnabled) {
            i9 = 1;
        } else {
            i9 = 0;
        }
        dest.writeByte((byte) i9);
        if (this.quickZoomGesturesEnabled) {
            i10 = 1;
        } else {
            i10 = 0;
        }
        dest.writeByte((byte) i10);
        dest.writeString(this.apiBaseUri);
        if (this.textureMode) {
            i11 = 1;
        } else {
            i11 = 0;
        }
        dest.writeByte((byte) i11);
        if (this.translucentTextureSurface) {
            i12 = 1;
        } else {
            i12 = 0;
        }
        dest.writeByte((byte) i12);
        if (this.prefetchesTiles) {
            i13 = 1;
        } else {
            i13 = 0;
        }
        dest.writeByte((byte) i13);
        if (this.zMediaOverlay) {
            i14 = 1;
        } else {
            i14 = 0;
        }
        dest.writeByte((byte) i14);
        if (this.localIdeographFontFamilyEnabled) {
            i15 = 1;
        } else {
            i15 = 0;
        }
        dest.writeByte((byte) i15);
        dest.writeString(this.localIdeographFontFamily);
        dest.writeStringArray(this.localIdeographFontFamilies);
        dest.writeFloat(this.pixelRatio);
        dest.writeInt(this.foregroundLoadColor);
        if (!this.crossSourceCollisions) {
            i16 = 0;
        }
        dest.writeByte((byte) i16);
    }

    public boolean equals(@Nullable Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MapboxMapOptions options = (MapboxMapOptions) o;
        if (this.debugActive != options.debugActive || this.compassEnabled != options.compassEnabled || this.fadeCompassFacingNorth != options.fadeCompassFacingNorth) {
            return false;
        }
        if (this.compassImage != null) {
            if (!this.compassImage.equals(options.compassImage)) {
                return false;
            }
        } else if (options.compassImage != null) {
            return false;
        }
        if (this.compassGravity != options.compassGravity || this.logoEnabled != options.logoEnabled || this.logoGravity != options.logoGravity || this.attributionTintColor != options.attributionTintColor || this.attributionEnabled != options.attributionEnabled || this.attributionGravity != options.attributionGravity || Double.compare(options.minZoom, this.minZoom) != 0 || Double.compare(options.maxZoom, this.maxZoom) != 0 || this.rotateGesturesEnabled != options.rotateGesturesEnabled || this.scrollGesturesEnabled != options.scrollGesturesEnabled || this.tiltGesturesEnabled != options.tiltGesturesEnabled || this.zoomGesturesEnabled != options.zoomGesturesEnabled || this.doubleTapGesturesEnabled != options.doubleTapGesturesEnabled || this.quickZoomGesturesEnabled != options.quickZoomGesturesEnabled) {
            return false;
        }
        if (this.cameraPosition != null) {
            if (!this.cameraPosition.equals(options.cameraPosition)) {
                return false;
            }
        } else if (options.cameraPosition != null) {
            return false;
        }
        if (!Arrays.equals(this.compassMargins, options.compassMargins) || !Arrays.equals(this.logoMargins, options.logoMargins) || !Arrays.equals(this.attributionMargins, options.attributionMargins)) {
            return false;
        }
        if (this.apiBaseUri != null) {
            if (!this.apiBaseUri.equals(options.apiBaseUri)) {
                return false;
            }
        } else if (options.apiBaseUri != null) {
            return false;
        }
        if (!(this.prefetchesTiles == options.prefetchesTiles && this.zMediaOverlay == options.zMediaOverlay && this.localIdeographFontFamilyEnabled == options.localIdeographFontFamilyEnabled && this.localIdeographFontFamily.equals(options.localIdeographFontFamily) && Arrays.equals(this.localIdeographFontFamilies, options.localIdeographFontFamilies) && this.pixelRatio == options.pixelRatio && this.crossSourceCollisions == options.crossSourceCollisions)) {
        }
        return false;
    }

    public int hashCode() {
        int result;
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        int i11;
        int i12;
        int i13;
        int i14;
        int i15;
        int i16;
        int i17;
        int i18;
        int i19;
        int i20 = 1;
        if (this.cameraPosition != null) {
            result = this.cameraPosition.hashCode();
        } else {
            result = 0;
        }
        int i21 = result * 31;
        if (this.debugActive) {
            i = 1;
        } else {
            i = 0;
        }
        int i22 = (i21 + i) * 31;
        if (this.compassEnabled) {
            i2 = 1;
        } else {
            i2 = 0;
        }
        int i23 = (i22 + i2) * 31;
        if (this.fadeCompassFacingNorth) {
            i3 = 1;
        } else {
            i3 = 0;
        }
        int i24 = (((i23 + i3) * 31) + this.compassGravity) * 31;
        if (this.compassImage != null) {
            i4 = this.compassImage.hashCode();
        } else {
            i4 = 0;
        }
        int hashCode = (((i24 + i4) * 31) + Arrays.hashCode(this.compassMargins)) * 31;
        if (this.logoEnabled) {
            i5 = 1;
        } else {
            i5 = 0;
        }
        int hashCode2 = (((((((hashCode + i5) * 31) + this.logoGravity) * 31) + Arrays.hashCode(this.logoMargins)) * 31) + this.attributionTintColor) * 31;
        if (this.attributionEnabled) {
            i6 = 1;
        } else {
            i6 = 0;
        }
        int result2 = ((((hashCode2 + i6) * 31) + this.attributionGravity) * 31) + Arrays.hashCode(this.attributionMargins);
        long temp = Double.doubleToLongBits(this.minZoom);
        int result3 = (result2 * 31) + ((int) ((temp >>> 32) ^ temp));
        long temp2 = Double.doubleToLongBits(this.maxZoom);
        int i25 = ((result3 * 31) + ((int) ((temp2 >>> 32) ^ temp2))) * 31;
        if (this.rotateGesturesEnabled) {
            i7 = 1;
        } else {
            i7 = 0;
        }
        int i26 = (i25 + i7) * 31;
        if (this.scrollGesturesEnabled) {
            i8 = 1;
        } else {
            i8 = 0;
        }
        int i27 = (i26 + i8) * 31;
        if (this.tiltGesturesEnabled) {
            i9 = 1;
        } else {
            i9 = 0;
        }
        int i28 = (i27 + i9) * 31;
        if (this.zoomGesturesEnabled) {
            i10 = 1;
        } else {
            i10 = 0;
        }
        int i29 = (i28 + i10) * 31;
        if (this.doubleTapGesturesEnabled) {
            i11 = 1;
        } else {
            i11 = 0;
        }
        int i30 = (i29 + i11) * 31;
        if (this.quickZoomGesturesEnabled) {
            i12 = 1;
        } else {
            i12 = 0;
        }
        int i31 = (i30 + i12) * 31;
        if (this.apiBaseUri != null) {
            i13 = this.apiBaseUri.hashCode();
        } else {
            i13 = 0;
        }
        int i32 = (i31 + i13) * 31;
        if (this.textureMode) {
            i14 = 1;
        } else {
            i14 = 0;
        }
        int i33 = (i32 + i14) * 31;
        if (this.translucentTextureSurface) {
            i15 = 1;
        } else {
            i15 = 0;
        }
        int i34 = (i33 + i15) * 31;
        if (this.prefetchesTiles) {
            i16 = 1;
        } else {
            i16 = 0;
        }
        int i35 = (i34 + i16) * 31;
        if (this.zMediaOverlay) {
            i17 = 1;
        } else {
            i17 = 0;
        }
        int i36 = (i35 + i17) * 31;
        if (this.localIdeographFontFamilyEnabled) {
            i18 = 1;
        } else {
            i18 = 0;
        }
        int i37 = (i36 + i18) * 31;
        if (this.localIdeographFontFamily != null) {
            i19 = this.localIdeographFontFamily.hashCode();
        } else {
            i19 = 0;
        }
        int hashCode3 = (((((i37 + i19) * 31) + Arrays.hashCode(this.localIdeographFontFamilies)) * 31) + ((int) this.pixelRatio)) * 31;
        if (!this.crossSourceCollisions) {
            i20 = 0;
        }
        return hashCode3 + i20;
    }
}
