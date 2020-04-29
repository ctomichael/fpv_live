package com.mapbox.mapboxsdk.location;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.Dimension;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import com.mapbox.mapboxsdk.R;
import java.util.Arrays;

public class LocationComponentOptions implements Parcelable {
    private static final float ACCURACY_ALPHA_DEFAULT = 0.15f;
    public static final Parcelable.Creator<LocationComponentOptions> CREATOR = new Parcelable.Creator<LocationComponentOptions>() {
        /* class com.mapbox.mapboxsdk.location.LocationComponentOptions.AnonymousClass1 */

        public LocationComponentOptions createFromParcel(Parcel in2) {
            boolean z;
            float readFloat = in2.readFloat();
            int readInt = in2.readInt();
            int readInt2 = in2.readInt();
            String readString = in2.readInt() == 0 ? in2.readString() : null;
            int readInt3 = in2.readInt();
            String readString2 = in2.readInt() == 0 ? in2.readString() : null;
            int readInt4 = in2.readInt();
            String readString3 = in2.readInt() == 0 ? in2.readString() : null;
            int readInt5 = in2.readInt();
            String readString4 = in2.readInt() == 0 ? in2.readString() : null;
            int readInt6 = in2.readInt();
            String readString5 = in2.readInt() == 0 ? in2.readString() : null;
            int readInt7 = in2.readInt();
            String readString6 = in2.readInt() == 0 ? in2.readString() : null;
            Integer valueOf = in2.readInt() == 0 ? Integer.valueOf(in2.readInt()) : null;
            Integer valueOf2 = in2.readInt() == 0 ? Integer.valueOf(in2.readInt()) : null;
            Integer valueOf3 = in2.readInt() == 0 ? Integer.valueOf(in2.readInt()) : null;
            Integer valueOf4 = in2.readInt() == 0 ? Integer.valueOf(in2.readInt()) : null;
            Integer valueOf5 = in2.readInt() == 0 ? Integer.valueOf(in2.readInt()) : null;
            float readFloat2 = in2.readFloat();
            boolean z2 = in2.readInt() == 1;
            long readLong = in2.readLong();
            int[] createIntArray = in2.createIntArray();
            float readFloat3 = in2.readFloat();
            float readFloat4 = in2.readFloat();
            boolean z3 = in2.readInt() == 1;
            float readFloat5 = in2.readFloat();
            float readFloat6 = in2.readFloat();
            String readString7 = in2.readString();
            String readString8 = in2.readString();
            float readFloat7 = in2.readFloat();
            boolean z4 = in2.readInt() == 1;
            if (in2.readInt() == 1) {
                z = true;
            } else {
                z = false;
            }
            return new LocationComponentOptions(readFloat, readInt, readInt2, readString, readInt3, readString2, readInt4, readString3, readInt5, readString4, readInt6, readString5, readInt7, readString6, valueOf, valueOf2, valueOf3, valueOf4, valueOf5, readFloat2, z2, readLong, createIntArray, readFloat3, readFloat4, z3, readFloat5, readFloat6, readString7, readString8, readFloat7, z4, z);
        }

        public LocationComponentOptions[] newArray(int size) {
            return new LocationComponentOptions[size];
        }
    };
    private static final float MAX_ZOOM_ICON_SCALE_DEFAULT = 1.0f;
    private static final float MIN_ZOOM_ICON_SCALE_DEFAULT = 0.6f;
    private static final int[] PADDING_DEFAULT = {0, 0, 0, 0};
    private static final long STALE_STATE_DELAY_MS = 30000;
    private static final float TRACKING_ANIMATION_DURATION_MULTIPLIER_DEFAULT = 1.1f;
    private float accuracyAlpha;
    private boolean accuracyAnimationEnabled;
    private int accuracyColor;
    private int backgroundDrawable;
    private int backgroundDrawableStale;
    @Nullable
    private String backgroundName;
    @Nullable
    private String backgroundStaleName;
    @Nullable
    private Integer backgroundStaleTintColor;
    @Nullable
    private Integer backgroundTintColor;
    private int bearingDrawable;
    @Nullable
    private String bearingName;
    @Nullable
    private Integer bearingTintColor;
    private boolean compassAnimationEnabled;
    private float elevation;
    private boolean enableStaleState;
    private int foregroundDrawable;
    private int foregroundDrawableStale;
    @Nullable
    private String foregroundName;
    @Nullable
    private String foregroundStaleName;
    @Nullable
    private Integer foregroundStaleTintColor;
    @Nullable
    private Integer foregroundTintColor;
    private int gpsDrawable;
    @Nullable
    private String gpsName;
    private String layerAbove;
    private String layerBelow;
    private float maxZoomIconScale;
    private float minZoomIconScale;
    @Nullable
    private int[] padding;
    private long staleStateTimeout;
    private float trackingAnimationDurationMultiplier;
    private boolean trackingGesturesManagement;
    private float trackingInitialMoveThreshold;
    private float trackingMultiFingerMoveThreshold;

    public LocationComponentOptions(float accuracyAlpha2, int accuracyColor2, int backgroundDrawableStale2, @Nullable String backgroundStaleName2, int foregroundDrawableStale2, @Nullable String foregroundStaleName2, int gpsDrawable2, @Nullable String gpsName2, int foregroundDrawable2, @Nullable String foregroundName2, int backgroundDrawable2, @Nullable String backgroundName2, int bearingDrawable2, @Nullable String bearingName2, @Nullable Integer bearingTintColor2, @Nullable Integer foregroundTintColor2, @Nullable Integer backgroundTintColor2, @Nullable Integer foregroundStaleTintColor2, @Nullable Integer backgroundStaleTintColor2, float elevation2, boolean enableStaleState2, long staleStateTimeout2, @Nullable int[] padding2, float maxZoomIconScale2, float minZoomIconScale2, boolean trackingGesturesManagement2, float trackingInitialMoveThreshold2, float trackingMultiFingerMoveThreshold2, String layerAbove2, String layerBelow2, float trackingAnimationDurationMultiplier2, boolean compassAnimationEnabled2, boolean accuracyAnimationEnabled2) {
        this.accuracyAlpha = accuracyAlpha2;
        this.accuracyColor = accuracyColor2;
        this.backgroundDrawableStale = backgroundDrawableStale2;
        this.backgroundStaleName = backgroundStaleName2;
        this.foregroundDrawableStale = foregroundDrawableStale2;
        this.foregroundStaleName = foregroundStaleName2;
        this.gpsDrawable = gpsDrawable2;
        this.gpsName = gpsName2;
        this.foregroundDrawable = foregroundDrawable2;
        this.foregroundName = foregroundName2;
        this.backgroundDrawable = backgroundDrawable2;
        this.backgroundName = backgroundName2;
        this.bearingDrawable = bearingDrawable2;
        this.bearingName = bearingName2;
        this.bearingTintColor = bearingTintColor2;
        this.foregroundTintColor = foregroundTintColor2;
        this.backgroundTintColor = backgroundTintColor2;
        this.foregroundStaleTintColor = foregroundStaleTintColor2;
        this.backgroundStaleTintColor = backgroundStaleTintColor2;
        this.elevation = elevation2;
        this.enableStaleState = enableStaleState2;
        this.staleStateTimeout = staleStateTimeout2;
        if (padding2 == null) {
            throw new NullPointerException("Null padding");
        }
        this.padding = padding2;
        this.maxZoomIconScale = maxZoomIconScale2;
        this.minZoomIconScale = minZoomIconScale2;
        this.trackingGesturesManagement = trackingGesturesManagement2;
        this.trackingInitialMoveThreshold = trackingInitialMoveThreshold2;
        this.trackingMultiFingerMoveThreshold = trackingMultiFingerMoveThreshold2;
        this.layerAbove = layerAbove2;
        this.layerBelow = layerBelow2;
        this.trackingAnimationDurationMultiplier = trackingAnimationDurationMultiplier2;
        this.compassAnimationEnabled = compassAnimationEnabled2;
        this.accuracyAnimationEnabled = accuracyAnimationEnabled2;
    }

    @NonNull
    public static LocationComponentOptions createFromAttributes(@NonNull Context context, @StyleRes int styleRes) {
        TypedArray typedArray = context.obtainStyledAttributes(styleRes, R.styleable.mapbox_LocationComponent);
        Builder builder = new Builder().enableStaleState(true).staleStateTimeout(STALE_STATE_DELAY_MS).maxZoomIconScale(1.0f).minZoomIconScale(0.6f).padding(PADDING_DEFAULT);
        builder.foregroundDrawable(typedArray.getResourceId(R.styleable.mapbox_LocationComponent_mapbox_foregroundDrawable, -1));
        if (typedArray.hasValue(R.styleable.mapbox_LocationComponent_mapbox_foregroundTintColor)) {
            builder.foregroundTintColor(Integer.valueOf(typedArray.getColor(R.styleable.mapbox_LocationComponent_mapbox_foregroundTintColor, -1)));
        }
        builder.backgroundDrawable(typedArray.getResourceId(R.styleable.mapbox_LocationComponent_mapbox_backgroundDrawable, -1));
        if (typedArray.hasValue(R.styleable.mapbox_LocationComponent_mapbox_backgroundTintColor)) {
            builder.backgroundTintColor(Integer.valueOf(typedArray.getColor(R.styleable.mapbox_LocationComponent_mapbox_backgroundTintColor, -1)));
        }
        builder.foregroundDrawableStale(typedArray.getResourceId(R.styleable.mapbox_LocationComponent_mapbox_foregroundDrawableStale, -1));
        if (typedArray.hasValue(R.styleable.mapbox_LocationComponent_mapbox_foregroundStaleTintColor)) {
            builder.foregroundStaleTintColor(Integer.valueOf(typedArray.getColor(R.styleable.mapbox_LocationComponent_mapbox_foregroundStaleTintColor, -1)));
        }
        builder.backgroundDrawableStale(typedArray.getResourceId(R.styleable.mapbox_LocationComponent_mapbox_backgroundDrawableStale, -1));
        if (typedArray.hasValue(R.styleable.mapbox_LocationComponent_mapbox_backgroundStaleTintColor)) {
            builder.backgroundStaleTintColor(Integer.valueOf(typedArray.getColor(R.styleable.mapbox_LocationComponent_mapbox_backgroundStaleTintColor, -1)));
        }
        builder.bearingDrawable(typedArray.getResourceId(R.styleable.mapbox_LocationComponent_mapbox_bearingDrawable, -1));
        if (typedArray.hasValue(R.styleable.mapbox_LocationComponent_mapbox_bearingTintColor)) {
            builder.bearingTintColor(Integer.valueOf(typedArray.getColor(R.styleable.mapbox_LocationComponent_mapbox_bearingTintColor, -1)));
        }
        if (typedArray.hasValue(R.styleable.mapbox_LocationComponent_mapbox_enableStaleState)) {
            builder.enableStaleState(typedArray.getBoolean(R.styleable.mapbox_LocationComponent_mapbox_enableStaleState, true));
        }
        if (typedArray.hasValue(R.styleable.mapbox_LocationComponent_mapbox_staleStateTimeout)) {
            builder.staleStateTimeout((long) typedArray.getInteger(R.styleable.mapbox_LocationComponent_mapbox_staleStateTimeout, 30000));
        }
        builder.gpsDrawable(typedArray.getResourceId(R.styleable.mapbox_LocationComponent_mapbox_gpsDrawable, -1));
        float elevation2 = typedArray.getDimension(R.styleable.mapbox_LocationComponent_mapbox_elevation, 0.0f);
        builder.accuracyColor(typedArray.getColor(R.styleable.mapbox_LocationComponent_mapbox_accuracyColor, -1));
        builder.accuracyAlpha(typedArray.getFloat(R.styleable.mapbox_LocationComponent_mapbox_accuracyAlpha, 0.15f));
        builder.elevation(elevation2);
        builder.trackingGesturesManagement(typedArray.getBoolean(R.styleable.mapbox_LocationComponent_mapbox_trackingGesturesManagement, false));
        builder.trackingInitialMoveThreshold(typedArray.getDimension(R.styleable.mapbox_LocationComponent_mapbox_trackingInitialMoveThreshold, context.getResources().getDimension(R.dimen.mapbox_locationComponentTrackingInitialMoveThreshold)));
        builder.trackingMultiFingerMoveThreshold(typedArray.getDimension(R.styleable.mapbox_LocationComponent_mapbox_trackingMultiFingerMoveThreshold, context.getResources().getDimension(R.dimen.mapbox_locationComponentTrackingMultiFingerMoveThreshold)));
        builder.padding(new int[]{typedArray.getInt(R.styleable.mapbox_LocationComponent_mapbox_iconPaddingLeft, 0), typedArray.getInt(R.styleable.mapbox_LocationComponent_mapbox_iconPaddingTop, 0), typedArray.getInt(R.styleable.mapbox_LocationComponent_mapbox_iconPaddingRight, 0), typedArray.getInt(R.styleable.mapbox_LocationComponent_mapbox_iconPaddingBottom, 0)});
        builder.layerAbove(typedArray.getString(R.styleable.mapbox_LocationComponent_mapbox_layer_above));
        builder.layerBelow(typedArray.getString(R.styleable.mapbox_LocationComponent_mapbox_layer_below));
        float minScale = typedArray.getFloat(R.styleable.mapbox_LocationComponent_mapbox_minZoomIconScale, 0.6f);
        float maxScale = typedArray.getFloat(R.styleable.mapbox_LocationComponent_mapbox_maxZoomIconScale, 1.0f);
        builder.minZoomIconScale(minScale);
        builder.maxZoomIconScale(maxScale);
        builder.trackingAnimationDurationMultiplier(typedArray.getFloat(R.styleable.mapbox_LocationComponent_mapbox_trackingAnimationDurationMultiplier, TRACKING_ANIMATION_DURATION_MULTIPLIER_DEFAULT));
        Boolean unused = builder.compassAnimationEnabled = Boolean.valueOf(typedArray.getBoolean(R.styleable.mapbox_LocationComponent_mapbox_compassAnimationEnabled, true));
        Boolean unused2 = builder.accuracyAnimationEnabled = Boolean.valueOf(typedArray.getBoolean(R.styleable.mapbox_LocationComponent_mapbox_accuracyAnimationEnabled, true));
        typedArray.recycle();
        return builder.build();
    }

    @NonNull
    public Builder toBuilder() {
        return new Builder();
    }

    @NonNull
    public static Builder builder(@NonNull Context context) {
        return createFromAttributes(context, R.style.mapbox_LocationComponent).toBuilder();
    }

    public float accuracyAlpha() {
        return this.accuracyAlpha;
    }

    @ColorInt
    public int accuracyColor() {
        return this.accuracyColor;
    }

    @DrawableRes
    public int backgroundDrawableStale() {
        return this.backgroundDrawableStale;
    }

    @Nullable
    public String backgroundStaleName() {
        return this.backgroundStaleName;
    }

    @DrawableRes
    public int foregroundDrawableStale() {
        return this.foregroundDrawableStale;
    }

    @Nullable
    public String foregroundStaleName() {
        return this.foregroundStaleName;
    }

    @DrawableRes
    public int gpsDrawable() {
        return this.gpsDrawable;
    }

    @Nullable
    public String gpsName() {
        return this.gpsName;
    }

    @DrawableRes
    public int foregroundDrawable() {
        return this.foregroundDrawable;
    }

    @Nullable
    public String foregroundName() {
        return this.foregroundName;
    }

    @DrawableRes
    public int backgroundDrawable() {
        return this.backgroundDrawable;
    }

    @Nullable
    public String backgroundName() {
        return this.backgroundName;
    }

    @DrawableRes
    public int bearingDrawable() {
        return this.bearingDrawable;
    }

    @Nullable
    public String bearingName() {
        return this.bearingName;
    }

    @ColorInt
    @Nullable
    public Integer bearingTintColor() {
        return this.bearingTintColor;
    }

    @ColorInt
    @Nullable
    public Integer foregroundTintColor() {
        return this.foregroundTintColor;
    }

    @ColorInt
    @Nullable
    public Integer backgroundTintColor() {
        return this.backgroundTintColor;
    }

    @ColorInt
    @Nullable
    public Integer foregroundStaleTintColor() {
        return this.foregroundStaleTintColor;
    }

    @ColorInt
    @Nullable
    public Integer backgroundStaleTintColor() {
        return this.backgroundStaleTintColor;
    }

    @Dimension
    public float elevation() {
        return this.elevation;
    }

    public boolean enableStaleState() {
        return this.enableStaleState;
    }

    public long staleStateTimeout() {
        return this.staleStateTimeout;
    }

    @Nullable
    public int[] padding() {
        return this.padding;
    }

    public float maxZoomIconScale() {
        return this.maxZoomIconScale;
    }

    public float minZoomIconScale() {
        return this.minZoomIconScale;
    }

    public boolean trackingGesturesManagement() {
        return this.trackingGesturesManagement;
    }

    public float trackingInitialMoveThreshold() {
        return this.trackingInitialMoveThreshold;
    }

    public float trackingMultiFingerMoveThreshold() {
        return this.trackingMultiFingerMoveThreshold;
    }

    public String layerAbove() {
        return this.layerAbove;
    }

    public String layerBelow() {
        return this.layerBelow;
    }

    public float trackingAnimationDurationMultiplier() {
        return this.trackingAnimationDurationMultiplier;
    }

    public boolean compassAnimationEnabled() {
        return this.compassAnimationEnabled;
    }

    public boolean accuracyAnimationEnabled() {
        return this.accuracyAnimationEnabled;
    }

    @NonNull
    public String toString() {
        return "LocationComponentOptions{accuracyAlpha=" + this.accuracyAlpha + ", accuracyColor=" + this.accuracyColor + ", backgroundDrawableStale=" + this.backgroundDrawableStale + ", backgroundStaleName=" + this.backgroundStaleName + ", foregroundDrawableStale=" + this.foregroundDrawableStale + ", foregroundStaleName=" + this.foregroundStaleName + ", gpsDrawable=" + this.gpsDrawable + ", gpsName=" + this.gpsName + ", foregroundDrawable=" + this.foregroundDrawable + ", foregroundName=" + this.foregroundName + ", backgroundDrawable=" + this.backgroundDrawable + ", backgroundName=" + this.backgroundName + ", bearingDrawable=" + this.bearingDrawable + ", bearingName=" + this.bearingName + ", bearingTintColor=" + this.bearingTintColor + ", foregroundTintColor=" + this.foregroundTintColor + ", backgroundTintColor=" + this.backgroundTintColor + ", foregroundStaleTintColor=" + this.foregroundStaleTintColor + ", backgroundStaleTintColor=" + this.backgroundStaleTintColor + ", elevation=" + this.elevation + ", enableStaleState=" + this.enableStaleState + ", staleStateTimeout=" + this.staleStateTimeout + ", padding=" + Arrays.toString(this.padding) + ", maxZoomIconScale=" + this.maxZoomIconScale + ", minZoomIconScale=" + this.minZoomIconScale + ", trackingGesturesManagement=" + this.trackingGesturesManagement + ", trackingInitialMoveThreshold=" + this.trackingInitialMoveThreshold + ", trackingMultiFingerMoveThreshold=" + this.trackingMultiFingerMoveThreshold + ", layerAbove=" + this.layerAbove + "layerBelow=" + this.layerBelow + "trackingAnimationDurationMultiplier=" + this.trackingAnimationDurationMultiplier + "}";
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LocationComponentOptions options = (LocationComponentOptions) o;
        if (Float.compare(options.accuracyAlpha, this.accuracyAlpha) != 0 || this.accuracyColor != options.accuracyColor || this.backgroundDrawableStale != options.backgroundDrawableStale || this.foregroundDrawableStale != options.foregroundDrawableStale || this.gpsDrawable != options.gpsDrawable || this.foregroundDrawable != options.foregroundDrawable || this.backgroundDrawable != options.backgroundDrawable || this.bearingDrawable != options.bearingDrawable || Float.compare(options.elevation, this.elevation) != 0 || this.enableStaleState != options.enableStaleState || this.staleStateTimeout != options.staleStateTimeout || Float.compare(options.maxZoomIconScale, this.maxZoomIconScale) != 0 || Float.compare(options.minZoomIconScale, this.minZoomIconScale) != 0 || this.trackingGesturesManagement != options.trackingGesturesManagement || Float.compare(options.trackingInitialMoveThreshold, this.trackingInitialMoveThreshold) != 0 || Float.compare(options.trackingMultiFingerMoveThreshold, this.trackingMultiFingerMoveThreshold) != 0 || Float.compare(options.trackingAnimationDurationMultiplier, this.trackingAnimationDurationMultiplier) != 0 || this.compassAnimationEnabled != options.compassAnimationEnabled || this.accuracyAnimationEnabled != options.accuracyAnimationEnabled) {
            return false;
        }
        if (this.backgroundStaleName != null) {
            if (!this.backgroundStaleName.equals(options.backgroundStaleName)) {
                return false;
            }
        } else if (options.backgroundStaleName != null) {
            return false;
        }
        if (this.foregroundStaleName != null) {
            if (!this.foregroundStaleName.equals(options.foregroundStaleName)) {
                return false;
            }
        } else if (options.foregroundStaleName != null) {
            return false;
        }
        if (this.gpsName != null) {
            if (!this.gpsName.equals(options.gpsName)) {
                return false;
            }
        } else if (options.gpsName != null) {
            return false;
        }
        if (this.foregroundName != null) {
            if (!this.foregroundName.equals(options.foregroundName)) {
                return false;
            }
        } else if (options.foregroundName != null) {
            return false;
        }
        if (this.backgroundName != null) {
            if (!this.backgroundName.equals(options.backgroundName)) {
                return false;
            }
        } else if (options.backgroundName != null) {
            return false;
        }
        if (this.bearingName != null) {
            if (!this.bearingName.equals(options.bearingName)) {
                return false;
            }
        } else if (options.bearingName != null) {
            return false;
        }
        if (this.bearingTintColor != null) {
            if (!this.bearingTintColor.equals(options.bearingTintColor)) {
                return false;
            }
        } else if (options.bearingTintColor != null) {
            return false;
        }
        if (this.foregroundTintColor != null) {
            if (!this.foregroundTintColor.equals(options.foregroundTintColor)) {
                return false;
            }
        } else if (options.foregroundTintColor != null) {
            return false;
        }
        if (this.backgroundTintColor != null) {
            if (!this.backgroundTintColor.equals(options.backgroundTintColor)) {
                return false;
            }
        } else if (options.backgroundTintColor != null) {
            return false;
        }
        if (this.foregroundStaleTintColor != null) {
            if (!this.foregroundStaleTintColor.equals(options.foregroundStaleTintColor)) {
                return false;
            }
        } else if (options.foregroundStaleTintColor != null) {
            return false;
        }
        if (this.backgroundStaleTintColor != null) {
            if (!this.backgroundStaleTintColor.equals(options.backgroundStaleTintColor)) {
                return false;
            }
        } else if (options.backgroundStaleTintColor != null) {
            return false;
        }
        if (!Arrays.equals(this.padding, options.padding)) {
            return false;
        }
        if (this.layerAbove != null) {
            if (!this.layerAbove.equals(options.layerAbove)) {
                return false;
            }
        } else if (options.layerAbove != null) {
            return false;
        }
        if (this.layerBelow != null) {
            z = this.layerBelow.equals(options.layerBelow);
        } else if (options.layerBelow != null) {
            z = false;
        }
        return z;
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
        int i20;
        int i21;
        int i22;
        int i23 = 1;
        if (this.accuracyAlpha != 0.0f) {
            result = Float.floatToIntBits(this.accuracyAlpha);
        } else {
            result = 0;
        }
        int i24 = ((((result * 31) + this.accuracyColor) * 31) + this.backgroundDrawableStale) * 31;
        if (this.backgroundStaleName != null) {
            i = this.backgroundStaleName.hashCode();
        } else {
            i = 0;
        }
        int i25 = (((i24 + i) * 31) + this.foregroundDrawableStale) * 31;
        if (this.foregroundStaleName != null) {
            i2 = this.foregroundStaleName.hashCode();
        } else {
            i2 = 0;
        }
        int i26 = (((i25 + i2) * 31) + this.gpsDrawable) * 31;
        if (this.gpsName != null) {
            i3 = this.gpsName.hashCode();
        } else {
            i3 = 0;
        }
        int i27 = (((i26 + i3) * 31) + this.foregroundDrawable) * 31;
        if (this.foregroundName != null) {
            i4 = this.foregroundName.hashCode();
        } else {
            i4 = 0;
        }
        int i28 = (((i27 + i4) * 31) + this.backgroundDrawable) * 31;
        if (this.backgroundName != null) {
            i5 = this.backgroundName.hashCode();
        } else {
            i5 = 0;
        }
        int i29 = (((i28 + i5) * 31) + this.bearingDrawable) * 31;
        if (this.bearingName != null) {
            i6 = this.bearingName.hashCode();
        } else {
            i6 = 0;
        }
        int i30 = (i29 + i6) * 31;
        if (this.bearingTintColor != null) {
            i7 = this.bearingTintColor.hashCode();
        } else {
            i7 = 0;
        }
        int i31 = (i30 + i7) * 31;
        if (this.foregroundTintColor != null) {
            i8 = this.foregroundTintColor.hashCode();
        } else {
            i8 = 0;
        }
        int i32 = (i31 + i8) * 31;
        if (this.backgroundTintColor != null) {
            i9 = this.backgroundTintColor.hashCode();
        } else {
            i9 = 0;
        }
        int i33 = (i32 + i9) * 31;
        if (this.foregroundStaleTintColor != null) {
            i10 = this.foregroundStaleTintColor.hashCode();
        } else {
            i10 = 0;
        }
        int i34 = (i33 + i10) * 31;
        if (this.backgroundStaleTintColor != null) {
            i11 = this.backgroundStaleTintColor.hashCode();
        } else {
            i11 = 0;
        }
        int i35 = (i34 + i11) * 31;
        if (this.elevation != 0.0f) {
            i12 = Float.floatToIntBits(this.elevation);
        } else {
            i12 = 0;
        }
        int i36 = (i35 + i12) * 31;
        if (this.enableStaleState) {
            i13 = 1;
        } else {
            i13 = 0;
        }
        int hashCode = (((((i36 + i13) * 31) + ((int) (this.staleStateTimeout ^ (this.staleStateTimeout >>> 32)))) * 31) + Arrays.hashCode(this.padding)) * 31;
        if (this.maxZoomIconScale != 0.0f) {
            i14 = Float.floatToIntBits(this.maxZoomIconScale);
        } else {
            i14 = 0;
        }
        int i37 = (hashCode + i14) * 31;
        if (this.minZoomIconScale != 0.0f) {
            i15 = Float.floatToIntBits(this.minZoomIconScale);
        } else {
            i15 = 0;
        }
        int i38 = (i37 + i15) * 31;
        if (this.trackingGesturesManagement) {
            i16 = 1;
        } else {
            i16 = 0;
        }
        int i39 = (i38 + i16) * 31;
        if (this.trackingInitialMoveThreshold != 0.0f) {
            i17 = Float.floatToIntBits(this.trackingInitialMoveThreshold);
        } else {
            i17 = 0;
        }
        int i40 = (i39 + i17) * 31;
        if (this.trackingMultiFingerMoveThreshold != 0.0f) {
            i18 = Float.floatToIntBits(this.trackingMultiFingerMoveThreshold);
        } else {
            i18 = 0;
        }
        int i41 = (i40 + i18) * 31;
        if (this.layerAbove != null) {
            i19 = this.layerAbove.hashCode();
        } else {
            i19 = 0;
        }
        int i42 = (i41 + i19) * 31;
        if (this.layerBelow != null) {
            i20 = this.layerBelow.hashCode();
        } else {
            i20 = 0;
        }
        int i43 = (i42 + i20) * 31;
        if (this.trackingAnimationDurationMultiplier != 0.0f) {
            i21 = Float.floatToIntBits(this.trackingAnimationDurationMultiplier);
        } else {
            i21 = 0;
        }
        int i44 = (i43 + i21) * 31;
        if (this.compassAnimationEnabled) {
            i22 = 1;
        } else {
            i22 = 0;
        }
        int i45 = (i44 + i22) * 31;
        if (!this.accuracyAnimationEnabled) {
            i23 = 0;
        }
        return i45 + i23;
    }

    public void writeToParcel(@NonNull Parcel dest, int flags) {
        int i;
        int i2;
        int i3;
        int i4 = 1;
        dest.writeFloat(accuracyAlpha());
        dest.writeInt(accuracyColor());
        dest.writeInt(backgroundDrawableStale());
        if (backgroundStaleName() == null) {
            dest.writeInt(1);
        } else {
            dest.writeInt(0);
            dest.writeString(backgroundStaleName());
        }
        dest.writeInt(foregroundDrawableStale());
        if (foregroundStaleName() == null) {
            dest.writeInt(1);
        } else {
            dest.writeInt(0);
            dest.writeString(foregroundStaleName());
        }
        dest.writeInt(gpsDrawable());
        if (gpsName() == null) {
            dest.writeInt(1);
        } else {
            dest.writeInt(0);
            dest.writeString(gpsName());
        }
        dest.writeInt(foregroundDrawable());
        if (foregroundName() == null) {
            dest.writeInt(1);
        } else {
            dest.writeInt(0);
            dest.writeString(foregroundName());
        }
        dest.writeInt(backgroundDrawable());
        if (backgroundName() == null) {
            dest.writeInt(1);
        } else {
            dest.writeInt(0);
            dest.writeString(backgroundName());
        }
        dest.writeInt(bearingDrawable());
        if (bearingName() == null) {
            dest.writeInt(1);
        } else {
            dest.writeInt(0);
            dest.writeString(bearingName());
        }
        if (bearingTintColor() == null) {
            dest.writeInt(1);
        } else {
            dest.writeInt(0);
            dest.writeInt(bearingTintColor().intValue());
        }
        if (foregroundTintColor() == null) {
            dest.writeInt(1);
        } else {
            dest.writeInt(0);
            dest.writeInt(foregroundTintColor().intValue());
        }
        if (backgroundTintColor() == null) {
            dest.writeInt(1);
        } else {
            dest.writeInt(0);
            dest.writeInt(backgroundTintColor().intValue());
        }
        if (foregroundStaleTintColor() == null) {
            dest.writeInt(1);
        } else {
            dest.writeInt(0);
            dest.writeInt(foregroundStaleTintColor().intValue());
        }
        if (backgroundStaleTintColor() == null) {
            dest.writeInt(1);
        } else {
            dest.writeInt(0);
            dest.writeInt(backgroundStaleTintColor().intValue());
        }
        dest.writeFloat(elevation());
        if (enableStaleState()) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeInt(i);
        dest.writeLong(staleStateTimeout());
        dest.writeIntArray(padding());
        dest.writeFloat(maxZoomIconScale());
        dest.writeFloat(minZoomIconScale());
        if (trackingGesturesManagement()) {
            i2 = 1;
        } else {
            i2 = 0;
        }
        dest.writeInt(i2);
        dest.writeFloat(trackingInitialMoveThreshold());
        dest.writeFloat(trackingMultiFingerMoveThreshold());
        dest.writeString(layerAbove());
        dest.writeString(layerBelow());
        dest.writeFloat(this.trackingAnimationDurationMultiplier);
        if (compassAnimationEnabled()) {
            i3 = 1;
        } else {
            i3 = 0;
        }
        dest.writeInt(i3);
        if (!accuracyAnimationEnabled()) {
            i4 = 0;
        }
        dest.writeInt(i4);
    }

    public int describeContents() {
        return 0;
    }

    public static class Builder {
        private Float accuracyAlpha;
        /* access modifiers changed from: private */
        public Boolean accuracyAnimationEnabled;
        private Integer accuracyColor;
        private Integer backgroundDrawable;
        private Integer backgroundDrawableStale;
        @Nullable
        private String backgroundName;
        @Nullable
        private String backgroundStaleName;
        @Nullable
        private Integer backgroundStaleTintColor;
        @Nullable
        private Integer backgroundTintColor;
        private Integer bearingDrawable;
        @Nullable
        private String bearingName;
        @Nullable
        private Integer bearingTintColor;
        /* access modifiers changed from: private */
        public Boolean compassAnimationEnabled;
        private Float elevation;
        private Boolean enableStaleState;
        private Integer foregroundDrawable;
        private Integer foregroundDrawableStale;
        @Nullable
        private String foregroundName;
        @Nullable
        private String foregroundStaleName;
        @Nullable
        private Integer foregroundStaleTintColor;
        @Nullable
        private Integer foregroundTintColor;
        private Integer gpsDrawable;
        @Nullable
        private String gpsName;
        private String layerAbove;
        private String layerBelow;
        private Float maxZoomIconScale;
        private Float minZoomIconScale;
        @Nullable
        private int[] padding;
        private Long staleStateTimeout;
        private Float trackingAnimationDurationMultiplier;
        private Boolean trackingGesturesManagement;
        private Float trackingInitialMoveThreshold;
        private Float trackingMultiFingerMoveThreshold;

        @NonNull
        public LocationComponentOptions build() {
            LocationComponentOptions locationComponentOptions = autoBuild();
            if (locationComponentOptions.accuracyAlpha() < 0.0f || locationComponentOptions.accuracyAlpha() > 1.0f) {
                throw new IllegalArgumentException("Accuracy alpha value must be between 0.0 and 1.0.");
            } else if (locationComponentOptions.elevation() < 0.0f) {
                throw new IllegalArgumentException("Invalid shadow size " + locationComponentOptions.elevation() + ". Must be >= 0");
            } else if (locationComponentOptions.layerAbove() == null || locationComponentOptions.layerBelow() == null) {
                return locationComponentOptions;
            } else {
                throw new IllegalArgumentException("You cannot set both layerAbove and layerBelow options.Choose one or the other.");
            }
        }

        Builder() {
        }

        private Builder(LocationComponentOptions source) {
            this.accuracyAlpha = Float.valueOf(source.accuracyAlpha());
            this.accuracyColor = Integer.valueOf(source.accuracyColor());
            this.backgroundDrawableStale = Integer.valueOf(source.backgroundDrawableStale());
            this.backgroundStaleName = source.backgroundStaleName();
            this.foregroundDrawableStale = Integer.valueOf(source.foregroundDrawableStale());
            this.foregroundStaleName = source.foregroundStaleName();
            this.gpsDrawable = Integer.valueOf(source.gpsDrawable());
            this.gpsName = source.gpsName();
            this.foregroundDrawable = Integer.valueOf(source.foregroundDrawable());
            this.foregroundName = source.foregroundName();
            this.backgroundDrawable = Integer.valueOf(source.backgroundDrawable());
            this.backgroundName = source.backgroundName();
            this.bearingDrawable = Integer.valueOf(source.bearingDrawable());
            this.bearingName = source.bearingName();
            this.bearingTintColor = source.bearingTintColor();
            this.foregroundTintColor = source.foregroundTintColor();
            this.backgroundTintColor = source.backgroundTintColor();
            this.foregroundStaleTintColor = source.foregroundStaleTintColor();
            this.backgroundStaleTintColor = source.backgroundStaleTintColor();
            this.elevation = Float.valueOf(source.elevation());
            this.enableStaleState = Boolean.valueOf(source.enableStaleState());
            this.staleStateTimeout = Long.valueOf(source.staleStateTimeout());
            this.padding = source.padding();
            this.maxZoomIconScale = Float.valueOf(source.maxZoomIconScale());
            this.minZoomIconScale = Float.valueOf(source.minZoomIconScale());
            this.trackingGesturesManagement = Boolean.valueOf(source.trackingGesturesManagement());
            this.trackingInitialMoveThreshold = Float.valueOf(source.trackingInitialMoveThreshold());
            this.trackingMultiFingerMoveThreshold = Float.valueOf(source.trackingMultiFingerMoveThreshold());
            this.layerAbove = source.layerAbove();
            this.layerBelow = source.layerBelow();
            this.trackingAnimationDurationMultiplier = Float.valueOf(source.trackingAnimationDurationMultiplier());
            this.compassAnimationEnabled = Boolean.valueOf(source.compassAnimationEnabled());
            this.accuracyAnimationEnabled = Boolean.valueOf(source.accuracyAnimationEnabled());
        }

        @NonNull
        public Builder accuracyAlpha(float accuracyAlpha2) {
            this.accuracyAlpha = Float.valueOf(accuracyAlpha2);
            return this;
        }

        @NonNull
        public Builder accuracyColor(int accuracyColor2) {
            this.accuracyColor = Integer.valueOf(accuracyColor2);
            return this;
        }

        @NonNull
        public Builder backgroundDrawableStale(int backgroundDrawableStale2) {
            this.backgroundDrawableStale = Integer.valueOf(backgroundDrawableStale2);
            return this;
        }

        @NonNull
        public Builder backgroundStaleName(@Nullable String backgroundStaleName2) {
            this.backgroundStaleName = backgroundStaleName2;
            return this;
        }

        @NonNull
        public Builder foregroundDrawableStale(int foregroundDrawableStale2) {
            this.foregroundDrawableStale = Integer.valueOf(foregroundDrawableStale2);
            return this;
        }

        @NonNull
        public Builder foregroundStaleName(@Nullable String foregroundStaleName2) {
            this.foregroundStaleName = foregroundStaleName2;
            return this;
        }

        @NonNull
        public Builder gpsDrawable(int gpsDrawable2) {
            this.gpsDrawable = Integer.valueOf(gpsDrawable2);
            return this;
        }

        @NonNull
        public Builder gpsName(@Nullable String gpsName2) {
            this.gpsName = gpsName2;
            return this;
        }

        @NonNull
        public Builder foregroundDrawable(int foregroundDrawable2) {
            this.foregroundDrawable = Integer.valueOf(foregroundDrawable2);
            return this;
        }

        @NonNull
        public Builder foregroundName(@Nullable String foregroundName2) {
            this.foregroundName = foregroundName2;
            return this;
        }

        @NonNull
        public Builder backgroundDrawable(int backgroundDrawable2) {
            this.backgroundDrawable = Integer.valueOf(backgroundDrawable2);
            return this;
        }

        @NonNull
        public Builder backgroundName(@Nullable String backgroundName2) {
            this.backgroundName = backgroundName2;
            return this;
        }

        @NonNull
        public Builder bearingDrawable(int bearingDrawable2) {
            this.bearingDrawable = Integer.valueOf(bearingDrawable2);
            return this;
        }

        @NonNull
        public Builder bearingName(@Nullable String bearingName2) {
            this.bearingName = bearingName2;
            return this;
        }

        @NonNull
        public Builder bearingTintColor(@Nullable Integer bearingTintColor2) {
            this.bearingTintColor = bearingTintColor2;
            return this;
        }

        @NonNull
        public Builder foregroundTintColor(@Nullable Integer foregroundTintColor2) {
            this.foregroundTintColor = foregroundTintColor2;
            return this;
        }

        @NonNull
        public Builder backgroundTintColor(@Nullable Integer backgroundTintColor2) {
            this.backgroundTintColor = backgroundTintColor2;
            return this;
        }

        @NonNull
        public Builder foregroundStaleTintColor(@Nullable Integer foregroundStaleTintColor2) {
            this.foregroundStaleTintColor = foregroundStaleTintColor2;
            return this;
        }

        @NonNull
        public Builder backgroundStaleTintColor(@Nullable Integer backgroundStaleTintColor2) {
            this.backgroundStaleTintColor = backgroundStaleTintColor2;
            return this;
        }

        @NonNull
        public Builder elevation(float elevation2) {
            this.elevation = Float.valueOf(elevation2);
            return this;
        }

        @NonNull
        public Builder enableStaleState(boolean enabled) {
            this.enableStaleState = Boolean.valueOf(enabled);
            return this;
        }

        @NonNull
        public Builder staleStateTimeout(long timeout) {
            this.staleStateTimeout = Long.valueOf(timeout);
            return this;
        }

        @Deprecated
        @NonNull
        public Builder padding(@Nullable int[] padding2) {
            if (padding2 == null) {
                throw new NullPointerException("Null padding");
            }
            this.padding = padding2;
            return this;
        }

        @NonNull
        public Builder maxZoomIconScale(float maxZoomIconScale2) {
            this.maxZoomIconScale = Float.valueOf(maxZoomIconScale2);
            return this;
        }

        @NonNull
        public Builder minZoomIconScale(float minZoomIconScale2) {
            this.minZoomIconScale = Float.valueOf(minZoomIconScale2);
            return this;
        }

        @NonNull
        public Builder trackingGesturesManagement(boolean trackingGesturesManagement2) {
            this.trackingGesturesManagement = Boolean.valueOf(trackingGesturesManagement2);
            return this;
        }

        @NonNull
        public Builder trackingInitialMoveThreshold(float moveThreshold) {
            this.trackingInitialMoveThreshold = Float.valueOf(moveThreshold);
            return this;
        }

        @NonNull
        public Builder trackingMultiFingerMoveThreshold(float moveThreshold) {
            this.trackingMultiFingerMoveThreshold = Float.valueOf(moveThreshold);
            return this;
        }

        @NonNull
        public Builder layerAbove(String layerAbove2) {
            this.layerAbove = layerAbove2;
            return this;
        }

        @NonNull
        public Builder layerBelow(String layerBelow2) {
            this.layerBelow = layerBelow2;
            return this;
        }

        @NonNull
        public Builder trackingAnimationDurationMultiplier(float trackingAnimationDurationMultiplier2) {
            this.trackingAnimationDurationMultiplier = Float.valueOf(trackingAnimationDurationMultiplier2);
            return this;
        }

        public Builder compassAnimationEnabled(Boolean compassAnimationEnabled2) {
            this.compassAnimationEnabled = compassAnimationEnabled2;
            return this;
        }

        public Builder accuracyAnimationEnabled(Boolean accuracyAnimationEnabled2) {
            this.accuracyAnimationEnabled = accuracyAnimationEnabled2;
            return this;
        }

        /* access modifiers changed from: package-private */
        @Nullable
        public LocationComponentOptions autoBuild() {
            String missing = "";
            if (this.accuracyAlpha == null) {
                missing = missing + " accuracyAlpha";
            }
            if (this.accuracyColor == null) {
                missing = missing + " accuracyColor";
            }
            if (this.backgroundDrawableStale == null) {
                missing = missing + " backgroundDrawableStale";
            }
            if (this.foregroundDrawableStale == null) {
                missing = missing + " foregroundDrawableStale";
            }
            if (this.gpsDrawable == null) {
                missing = missing + " gpsDrawable";
            }
            if (this.foregroundDrawable == null) {
                missing = missing + " foregroundDrawable";
            }
            if (this.backgroundDrawable == null) {
                missing = missing + " backgroundDrawable";
            }
            if (this.bearingDrawable == null) {
                missing = missing + " bearingDrawable";
            }
            if (this.elevation == null) {
                missing = missing + " elevation";
            }
            if (this.enableStaleState == null) {
                missing = missing + " enableStaleState";
            }
            if (this.staleStateTimeout == null) {
                missing = missing + " staleStateTimeout";
            }
            if (this.padding == null) {
                missing = missing + " padding";
            }
            if (this.maxZoomIconScale == null) {
                missing = missing + " maxZoomIconScale";
            }
            if (this.minZoomIconScale == null) {
                missing = missing + " minZoomIconScale";
            }
            if (this.trackingGesturesManagement == null) {
                missing = missing + " trackingGesturesManagement";
            }
            if (this.trackingInitialMoveThreshold == null) {
                missing = missing + " trackingInitialMoveThreshold";
            }
            if (this.trackingMultiFingerMoveThreshold == null) {
                missing = missing + " trackingMultiFingerMoveThreshold";
            }
            if (this.trackingAnimationDurationMultiplier == null) {
                missing = missing + " trackingAnimationDurationMultiplier";
            }
            if (missing.isEmpty()) {
                return new LocationComponentOptions(this.accuracyAlpha.floatValue(), this.accuracyColor.intValue(), this.backgroundDrawableStale.intValue(), this.backgroundStaleName, this.foregroundDrawableStale.intValue(), this.foregroundStaleName, this.gpsDrawable.intValue(), this.gpsName, this.foregroundDrawable.intValue(), this.foregroundName, this.backgroundDrawable.intValue(), this.backgroundName, this.bearingDrawable.intValue(), this.bearingName, this.bearingTintColor, this.foregroundTintColor, this.backgroundTintColor, this.foregroundStaleTintColor, this.backgroundStaleTintColor, this.elevation.floatValue(), this.enableStaleState.booleanValue(), this.staleStateTimeout.longValue(), this.padding, this.maxZoomIconScale.floatValue(), this.minZoomIconScale.floatValue(), this.trackingGesturesManagement.booleanValue(), this.trackingInitialMoveThreshold.floatValue(), this.trackingMultiFingerMoveThreshold.floatValue(), this.layerAbove, this.layerBelow, this.trackingAnimationDurationMultiplier.floatValue(), this.compassAnimationEnabled.booleanValue(), this.accuracyAnimationEnabled.booleanValue());
            }
            throw new IllegalStateException("Missing required properties:" + missing);
        }
    }
}
