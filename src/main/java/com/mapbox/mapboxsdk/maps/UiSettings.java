package com.mapbox.mapboxsdk.maps;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.support.annotation.UiThread;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.mapbox.mapboxsdk.R;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.constants.MapboxConstants;
import com.mapbox.mapboxsdk.maps.widgets.CompassView;
import com.mapbox.mapboxsdk.utils.BitmapUtils;
import com.mapbox.mapboxsdk.utils.ColorUtils;

public final class UiSettings {
    private AttributionDialogManager attributionDialogManager;
    private final int[] attributionsMargins = new int[4];
    @NonNull
    private final ImageView attributionsView;
    private final int[] compassMargins = new int[4];
    @NonNull
    private final CompassView compassView;
    private boolean deselectMarkersOnTap = true;
    private boolean disableRotateWhenScaling = true;
    private boolean doubleTapGesturesEnabled = true;
    private boolean flingVelocityAnimationEnabled = true;
    @NonNull
    private final FocalPointChangeListener focalPointChangeListener;
    private boolean increaseRotateThresholdWhenScaling = true;
    private boolean increaseScaleThresholdWhenRotating = true;
    private final int[] logoMargins = new int[4];
    @NonNull
    private final View logoView;
    private final float pixelRatio;
    @NonNull
    private final Projection projection;
    private boolean quickZoomGesturesEnabled = true;
    private boolean rotateGesturesEnabled = true;
    private boolean rotateVelocityAnimationEnabled = true;
    private boolean scaleVelocityAnimationEnabled = true;
    private boolean scrollGesturesEnabled = true;
    private boolean tiltGesturesEnabled = true;
    @Nullable
    private PointF userProvidedFocalPoint;
    private boolean zoomGesturesEnabled = true;
    private float zoomRate = 1.0f;

    UiSettings(@NonNull Projection projection2, @NonNull FocalPointChangeListener listener, @NonNull CompassView compassView2, @NonNull ImageView attributionsView2, @NonNull View logoView2, float pixelRatio2) {
        this.projection = projection2;
        this.focalPointChangeListener = listener;
        this.compassView = compassView2;
        this.attributionsView = attributionsView2;
        this.logoView = logoView2;
        this.pixelRatio = pixelRatio2;
    }

    /* access modifiers changed from: package-private */
    public void initialise(@NonNull Context context, @NonNull MapboxMapOptions options) {
        Resources resources = context.getResources();
        initialiseGestures(options);
        initialiseCompass(options, resources);
        initialiseLogo(options, resources);
        initialiseAttribution(context, options);
    }

    /* access modifiers changed from: package-private */
    public void onSaveInstanceState(@NonNull Bundle outState) {
        saveGestures(outState);
        saveCompass(outState);
        saveLogo(outState);
        saveAttribution(outState);
        saveDeselectMarkersOnTap(outState);
        saveFocalPoint(outState);
    }

    /* access modifiers changed from: package-private */
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        restoreGestures(savedInstanceState);
        restoreCompass(savedInstanceState);
        restoreLogo(savedInstanceState);
        restoreAttribution(savedInstanceState);
        restoreDeselectMarkersOnTap(savedInstanceState);
        restoreFocalPoint(savedInstanceState);
    }

    private void initialiseGestures(MapboxMapOptions options) {
        setZoomGesturesEnabled(options.getZoomGesturesEnabled());
        setScrollGesturesEnabled(options.getScrollGesturesEnabled());
        setRotateGesturesEnabled(options.getRotateGesturesEnabled());
        setTiltGesturesEnabled(options.getTiltGesturesEnabled());
        setDoubleTapGesturesEnabled(options.getDoubleTapGesturesEnabled());
        setQuickZoomGesturesEnabled(options.getQuickZoomGesturesEnabled());
    }

    private void saveGestures(Bundle outState) {
        outState.putBoolean(MapboxConstants.STATE_ZOOM_ENABLED, isZoomGesturesEnabled());
        outState.putBoolean(MapboxConstants.STATE_SCROLL_ENABLED, isScrollGesturesEnabled());
        outState.putBoolean(MapboxConstants.STATE_ROTATE_ENABLED, isRotateGesturesEnabled());
        outState.putBoolean(MapboxConstants.STATE_TILT_ENABLED, isTiltGesturesEnabled());
        outState.putBoolean(MapboxConstants.STATE_DOUBLE_TAP_ENABLED, isDoubleTapGesturesEnabled());
        outState.putBoolean(MapboxConstants.STATE_SCALE_ANIMATION_ENABLED, isScaleVelocityAnimationEnabled());
        outState.putBoolean(MapboxConstants.STATE_ROTATE_ANIMATION_ENABLED, isRotateVelocityAnimationEnabled());
        outState.putBoolean(MapboxConstants.STATE_FLING_ANIMATION_ENABLED, isFlingVelocityAnimationEnabled());
        outState.putBoolean(MapboxConstants.STATE_INCREASE_ROTATE_THRESHOLD, isIncreaseRotateThresholdWhenScaling());
        outState.putBoolean(MapboxConstants.STATE_DISABLE_ROTATE_WHEN_SCALING, isDisableRotateWhenScaling());
        outState.putBoolean(MapboxConstants.STATE_INCREASE_SCALE_THRESHOLD, isIncreaseScaleThresholdWhenRotating());
        outState.putBoolean(MapboxConstants.STATE_QUICK_ZOOM_ENABLED, isQuickZoomGesturesEnabled());
        outState.putFloat(MapboxConstants.STATE_ZOOM_RATE, getZoomRate());
    }

    private void restoreGestures(Bundle savedInstanceState) {
        setZoomGesturesEnabled(savedInstanceState.getBoolean(MapboxConstants.STATE_ZOOM_ENABLED));
        setScrollGesturesEnabled(savedInstanceState.getBoolean(MapboxConstants.STATE_SCROLL_ENABLED));
        setRotateGesturesEnabled(savedInstanceState.getBoolean(MapboxConstants.STATE_ROTATE_ENABLED));
        setTiltGesturesEnabled(savedInstanceState.getBoolean(MapboxConstants.STATE_TILT_ENABLED));
        setDoubleTapGesturesEnabled(savedInstanceState.getBoolean(MapboxConstants.STATE_DOUBLE_TAP_ENABLED));
        setScaleVelocityAnimationEnabled(savedInstanceState.getBoolean(MapboxConstants.STATE_SCALE_ANIMATION_ENABLED));
        setRotateVelocityAnimationEnabled(savedInstanceState.getBoolean(MapboxConstants.STATE_ROTATE_ANIMATION_ENABLED));
        setFlingVelocityAnimationEnabled(savedInstanceState.getBoolean(MapboxConstants.STATE_FLING_ANIMATION_ENABLED));
        setIncreaseRotateThresholdWhenScaling(savedInstanceState.getBoolean(MapboxConstants.STATE_INCREASE_ROTATE_THRESHOLD));
        setDisableRotateWhenScaling(savedInstanceState.getBoolean(MapboxConstants.STATE_DISABLE_ROTATE_WHEN_SCALING));
        setIncreaseScaleThresholdWhenRotating(savedInstanceState.getBoolean(MapboxConstants.STATE_INCREASE_SCALE_THRESHOLD));
        setQuickZoomGesturesEnabled(savedInstanceState.getBoolean(MapboxConstants.STATE_QUICK_ZOOM_ENABLED));
        setZoomRate(savedInstanceState.getFloat(MapboxConstants.STATE_ZOOM_RATE, 1.0f));
    }

    private void initialiseCompass(MapboxMapOptions options, @NonNull Resources resources) {
        setCompassEnabled(options.getCompassEnabled());
        setCompassGravity(options.getCompassGravity());
        int[] compassMargins2 = options.getCompassMargins();
        if (compassMargins2 != null) {
            setCompassMargins(compassMargins2[0], compassMargins2[1], compassMargins2[2], compassMargins2[3]);
        } else {
            int tenDp = (int) resources.getDimension(R.dimen.mapbox_four_dp);
            setCompassMargins(tenDp, tenDp, tenDp, tenDp);
        }
        setCompassFadeFacingNorth(options.getCompassFadeFacingNorth());
        if (options.getCompassImage() == null) {
            options.compassImage(ResourcesCompat.getDrawable(resources, R.drawable.mapbox_compass_icon, null));
        }
        setCompassImage(options.getCompassImage());
    }

    private void saveCompass(Bundle outState) {
        outState.putBoolean(MapboxConstants.STATE_COMPASS_ENABLED, isCompassEnabled());
        outState.putInt(MapboxConstants.STATE_COMPASS_GRAVITY, getCompassGravity());
        outState.putInt(MapboxConstants.STATE_COMPASS_MARGIN_LEFT, getCompassMarginLeft());
        outState.putInt(MapboxConstants.STATE_COMPASS_MARGIN_TOP, getCompassMarginTop());
        outState.putInt(MapboxConstants.STATE_COMPASS_MARGIN_BOTTOM, getCompassMarginBottom());
        outState.putInt(MapboxConstants.STATE_COMPASS_MARGIN_RIGHT, getCompassMarginRight());
        outState.putBoolean(MapboxConstants.STATE_COMPASS_FADE_WHEN_FACING_NORTH, isCompassFadeWhenFacingNorth());
        outState.putByteArray(MapboxConstants.STATE_COMPASS_IMAGE_BITMAP, BitmapUtils.getByteArrayFromDrawable(getCompassImage()));
    }

    private void restoreCompass(Bundle savedInstanceState) {
        setCompassEnabled(savedInstanceState.getBoolean(MapboxConstants.STATE_COMPASS_ENABLED));
        setCompassGravity(savedInstanceState.getInt(MapboxConstants.STATE_COMPASS_GRAVITY));
        setCompassMargins(savedInstanceState.getInt(MapboxConstants.STATE_COMPASS_MARGIN_LEFT), savedInstanceState.getInt(MapboxConstants.STATE_COMPASS_MARGIN_TOP), savedInstanceState.getInt(MapboxConstants.STATE_COMPASS_MARGIN_RIGHT), savedInstanceState.getInt(MapboxConstants.STATE_COMPASS_MARGIN_BOTTOM));
        setCompassFadeFacingNorth(savedInstanceState.getBoolean(MapboxConstants.STATE_COMPASS_FADE_WHEN_FACING_NORTH));
        setCompassImage(BitmapUtils.getDrawableFromByteArray(this.compassView.getContext(), savedInstanceState.getByteArray(MapboxConstants.STATE_COMPASS_IMAGE_BITMAP)));
    }

    private void initialiseLogo(MapboxMapOptions options, @NonNull Resources resources) {
        setLogoEnabled(options.getLogoEnabled());
        setLogoGravity(options.getLogoGravity());
        setLogoMargins(resources, options.getLogoMargins());
    }

    private void setLogoMargins(@NonNull Resources resources, @Nullable int[] logoMargins2) {
        if (logoMargins2 != null) {
            setLogoMargins(logoMargins2[0], logoMargins2[1], logoMargins2[2], logoMargins2[3]);
            return;
        }
        int fourDp = (int) resources.getDimension(R.dimen.mapbox_four_dp);
        setLogoMargins(fourDp, fourDp, fourDp, fourDp);
    }

    private void saveLogo(Bundle outState) {
        outState.putInt(MapboxConstants.STATE_LOGO_GRAVITY, getLogoGravity());
        outState.putInt(MapboxConstants.STATE_LOGO_MARGIN_LEFT, getLogoMarginLeft());
        outState.putInt(MapboxConstants.STATE_LOGO_MARGIN_TOP, getLogoMarginTop());
        outState.putInt(MapboxConstants.STATE_LOGO_MARGIN_RIGHT, getLogoMarginRight());
        outState.putInt(MapboxConstants.STATE_LOGO_MARGIN_BOTTOM, getLogoMarginBottom());
        outState.putBoolean(MapboxConstants.STATE_LOGO_ENABLED, isLogoEnabled());
    }

    private void restoreLogo(Bundle savedInstanceState) {
        setLogoEnabled(savedInstanceState.getBoolean(MapboxConstants.STATE_LOGO_ENABLED));
        setLogoGravity(savedInstanceState.getInt(MapboxConstants.STATE_LOGO_GRAVITY));
        setLogoMargins(savedInstanceState.getInt(MapboxConstants.STATE_LOGO_MARGIN_LEFT), savedInstanceState.getInt(MapboxConstants.STATE_LOGO_MARGIN_TOP), savedInstanceState.getInt(MapboxConstants.STATE_LOGO_MARGIN_RIGHT), savedInstanceState.getInt(MapboxConstants.STATE_LOGO_MARGIN_BOTTOM));
    }

    private void initialiseAttribution(@NonNull Context context, MapboxMapOptions options) {
        setAttributionEnabled(options.getAttributionEnabled());
        setAttributionGravity(options.getAttributionGravity());
        setAttributionMargins(context, options.getAttributionMargins());
        int attributionTintColor = options.getAttributionTintColor();
        if (attributionTintColor == -1) {
            attributionTintColor = ColorUtils.getPrimaryColor(context);
        }
        setAttributionTintColor(attributionTintColor);
    }

    private void setAttributionMargins(@NonNull Context context, @Nullable int[] attributionMargins) {
        if (attributionMargins != null) {
            setAttributionMargins(attributionMargins[0], attributionMargins[1], attributionMargins[2], attributionMargins[3]);
            return;
        }
        Resources resources = context.getResources();
        int margin = (int) resources.getDimension(R.dimen.mapbox_four_dp);
        setAttributionMargins((int) resources.getDimension(R.dimen.mapbox_ninety_two_dp), margin, margin, margin);
    }

    private void saveAttribution(Bundle outState) {
        outState.putInt(MapboxConstants.STATE_ATTRIBUTION_GRAVITY, getAttributionGravity());
        outState.putInt(MapboxConstants.STATE_ATTRIBUTION_MARGIN_LEFT, getAttributionMarginLeft());
        outState.putInt(MapboxConstants.STATE_ATTRIBUTION_MARGIN_TOP, getAttributionMarginTop());
        outState.putInt(MapboxConstants.STATE_ATTRIBUTION_MARGIN_RIGHT, getAttributionMarginRight());
        outState.putInt(MapboxConstants.STATE_ATTRIBUTION_MARGIN_BOTTOM, getAttributionMarginBottom());
        outState.putBoolean(MapboxConstants.STATE_ATTRIBUTION_ENABLED, isAttributionEnabled());
    }

    private void restoreAttribution(Bundle savedInstanceState) {
        setAttributionEnabled(savedInstanceState.getBoolean(MapboxConstants.STATE_ATTRIBUTION_ENABLED));
        setAttributionGravity(savedInstanceState.getInt(MapboxConstants.STATE_ATTRIBUTION_GRAVITY));
        setAttributionMargins(savedInstanceState.getInt(MapboxConstants.STATE_ATTRIBUTION_MARGIN_LEFT), savedInstanceState.getInt(MapboxConstants.STATE_ATTRIBUTION_MARGIN_TOP), savedInstanceState.getInt(MapboxConstants.STATE_ATTRIBUTION_MARGIN_RIGHT), savedInstanceState.getInt(MapboxConstants.STATE_ATTRIBUTION_MARGIN_BOTTOM));
    }

    public void setCompassEnabled(boolean compassEnabled) {
        this.compassView.setEnabled(compassEnabled);
    }

    public boolean isCompassEnabled() {
        return this.compassView.isEnabled();
    }

    @UiThread
    public void setCompassGravity(int gravity) {
        setWidgetGravity(this.compassView, gravity);
    }

    public void setCompassFadeFacingNorth(boolean compassFadeFacingNorth) {
        this.compassView.fadeCompassViewFacingNorth(compassFadeFacingNorth);
    }

    public void setCompassImage(@NonNull Drawable compass) {
        this.compassView.setCompassImage(compass);
    }

    public boolean isCompassFadeWhenFacingNorth() {
        return this.compassView.isFadeCompassViewFacingNorth();
    }

    public int getCompassGravity() {
        return ((FrameLayout.LayoutParams) this.compassView.getLayoutParams()).gravity;
    }

    @UiThread
    public void setCompassMargins(@Px int left, @Px int top, @Px int right, @Px int bottom) {
        setWidgetMargins(this.compassView, this.compassMargins, left, top, right, bottom);
    }

    @Px
    public int getCompassMarginLeft() {
        return this.compassMargins[0];
    }

    @Px
    public int getCompassMarginTop() {
        return this.compassMargins[1];
    }

    @Px
    public int getCompassMarginRight() {
        return this.compassMargins[2];
    }

    @Px
    public int getCompassMarginBottom() {
        return this.compassMargins[3];
    }

    @NonNull
    public Drawable getCompassImage() {
        return this.compassView.getCompassImage();
    }

    /* access modifiers changed from: package-private */
    public void update(@NonNull CameraPosition cameraPosition) {
        this.compassView.update(-cameraPosition.bearing);
    }

    public void setLogoEnabled(boolean enabled) {
        this.logoView.setVisibility(enabled ? 0 : 8);
    }

    public boolean isLogoEnabled() {
        return this.logoView.getVisibility() == 0;
    }

    public void setLogoGravity(int gravity) {
        setWidgetGravity(this.logoView, gravity);
    }

    public int getLogoGravity() {
        return ((FrameLayout.LayoutParams) this.logoView.getLayoutParams()).gravity;
    }

    public void setLogoMargins(@Px int left, @Px int top, @Px int right, @Px int bottom) {
        setWidgetMargins(this.logoView, this.logoMargins, left, top, right, bottom);
    }

    @Px
    public int getLogoMarginLeft() {
        return this.logoMargins[0];
    }

    @Px
    public int getLogoMarginTop() {
        return this.logoMargins[1];
    }

    @Px
    public int getLogoMarginRight() {
        return this.logoMargins[2];
    }

    @Px
    public int getLogoMarginBottom() {
        return this.logoMargins[3];
    }

    public void setAttributionEnabled(boolean enabled) {
        this.attributionsView.setVisibility(enabled ? 0 : 8);
    }

    public boolean isAttributionEnabled() {
        return this.attributionsView.getVisibility() == 0;
    }

    public void setAttributionDialogManager(@NonNull AttributionDialogManager attributionDialogManager2) {
        this.attributionDialogManager = attributionDialogManager2;
    }

    @Nullable
    public AttributionDialogManager getAttributionDialogManager() {
        return this.attributionDialogManager;
    }

    public void setAttributionGravity(int gravity) {
        setWidgetGravity(this.attributionsView, gravity);
    }

    public int getAttributionGravity() {
        return ((FrameLayout.LayoutParams) this.attributionsView.getLayoutParams()).gravity;
    }

    public void setAttributionMargins(@Px int left, @Px int top, @Px int right, @Px int bottom) {
        setWidgetMargins(this.attributionsView, this.attributionsMargins, left, top, right, bottom);
    }

    public void setAttributionTintColor(@ColorInt int tintColor) {
        if (Color.alpha(tintColor) == 0) {
            ColorUtils.setTintList(this.attributionsView, ContextCompat.getColor(this.attributionsView.getContext(), R.color.mapbox_blue));
        } else {
            ColorUtils.setTintList(this.attributionsView, tintColor);
        }
    }

    @Px
    public int getAttributionMarginLeft() {
        return this.attributionsMargins[0];
    }

    @Px
    public int getAttributionMarginTop() {
        return this.attributionsMargins[1];
    }

    @Px
    public int getAttributionMarginRight() {
        return this.attributionsMargins[2];
    }

    @Px
    public int getAttributionMarginBottom() {
        return this.attributionsMargins[3];
    }

    public void setRotateGesturesEnabled(boolean rotateGesturesEnabled2) {
        this.rotateGesturesEnabled = rotateGesturesEnabled2;
    }

    public boolean isRotateGesturesEnabled() {
        return this.rotateGesturesEnabled;
    }

    public void setTiltGesturesEnabled(boolean tiltGesturesEnabled2) {
        this.tiltGesturesEnabled = tiltGesturesEnabled2;
    }

    public boolean isTiltGesturesEnabled() {
        return this.tiltGesturesEnabled;
    }

    public void setZoomGesturesEnabled(boolean zoomGesturesEnabled2) {
        this.zoomGesturesEnabled = zoomGesturesEnabled2;
    }

    public boolean isZoomGesturesEnabled() {
        return this.zoomGesturesEnabled;
    }

    public void setDoubleTapGesturesEnabled(boolean doubleTapGesturesEnabled2) {
        this.doubleTapGesturesEnabled = doubleTapGesturesEnabled2;
    }

    public boolean isDoubleTapGesturesEnabled() {
        return this.doubleTapGesturesEnabled;
    }

    public boolean isQuickZoomGesturesEnabled() {
        return this.quickZoomGesturesEnabled;
    }

    public void setQuickZoomGesturesEnabled(boolean quickZoomGesturesEnabled2) {
        this.quickZoomGesturesEnabled = quickZoomGesturesEnabled2;
    }

    public float getZoomRate() {
        return this.zoomRate;
    }

    public void setZoomRate(@FloatRange(from = 0.0d) float zoomRate2) {
        this.zoomRate = zoomRate2;
    }

    private void restoreDeselectMarkersOnTap(Bundle savedInstanceState) {
        setDeselectMarkersOnTap(savedInstanceState.getBoolean(MapboxConstants.STATE_DESELECT_MARKER_ON_TAP));
    }

    private void saveDeselectMarkersOnTap(Bundle outState) {
        outState.putBoolean(MapboxConstants.STATE_DESELECT_MARKER_ON_TAP, isDeselectMarkersOnTap());
    }

    public boolean isDeselectMarkersOnTap() {
        return this.deselectMarkersOnTap;
    }

    public void setDeselectMarkersOnTap(boolean deselectMarkersOnTap2) {
        this.deselectMarkersOnTap = deselectMarkersOnTap2;
    }

    public void setScrollGesturesEnabled(boolean scrollGesturesEnabled2) {
        this.scrollGesturesEnabled = scrollGesturesEnabled2;
    }

    public boolean isScrollGesturesEnabled() {
        return this.scrollGesturesEnabled;
    }

    public boolean isScaleVelocityAnimationEnabled() {
        return this.scaleVelocityAnimationEnabled;
    }

    public void setScaleVelocityAnimationEnabled(boolean scaleVelocityAnimationEnabled2) {
        this.scaleVelocityAnimationEnabled = scaleVelocityAnimationEnabled2;
    }

    public boolean isRotateVelocityAnimationEnabled() {
        return this.rotateVelocityAnimationEnabled;
    }

    public void setRotateVelocityAnimationEnabled(boolean rotateVelocityAnimationEnabled2) {
        this.rotateVelocityAnimationEnabled = rotateVelocityAnimationEnabled2;
    }

    public boolean isFlingVelocityAnimationEnabled() {
        return this.flingVelocityAnimationEnabled;
    }

    public void setFlingVelocityAnimationEnabled(boolean flingVelocityAnimationEnabled2) {
        this.flingVelocityAnimationEnabled = flingVelocityAnimationEnabled2;
    }

    public void setAllVelocityAnimationsEnabled(boolean allVelocityAnimationsEnabled) {
        setScaleVelocityAnimationEnabled(allVelocityAnimationsEnabled);
        setRotateVelocityAnimationEnabled(allVelocityAnimationsEnabled);
        setFlingVelocityAnimationEnabled(allVelocityAnimationsEnabled);
    }

    @Deprecated
    public boolean isIncreaseRotateThresholdWhenScaling() {
        return this.increaseRotateThresholdWhenScaling;
    }

    @Deprecated
    public void setIncreaseRotateThresholdWhenScaling(boolean increaseRotateThresholdWhenScaling2) {
        this.increaseRotateThresholdWhenScaling = increaseRotateThresholdWhenScaling2;
    }

    public boolean isDisableRotateWhenScaling() {
        return this.disableRotateWhenScaling;
    }

    public void setDisableRotateWhenScaling(boolean disableRotateWhenScaling2) {
        this.disableRotateWhenScaling = disableRotateWhenScaling2;
    }

    public boolean isIncreaseScaleThresholdWhenRotating() {
        return this.increaseScaleThresholdWhenRotating;
    }

    public void setIncreaseScaleThresholdWhenRotating(boolean increaseScaleThresholdWhenRotating2) {
        this.increaseScaleThresholdWhenRotating = increaseScaleThresholdWhenRotating2;
    }

    public void setAllGesturesEnabled(boolean enabled) {
        setScrollGesturesEnabled(enabled);
        setRotateGesturesEnabled(enabled);
        setTiltGesturesEnabled(enabled);
        setZoomGesturesEnabled(enabled);
        setDoubleTapGesturesEnabled(enabled);
        setQuickZoomGesturesEnabled(enabled);
    }

    public boolean areAllGesturesEnabled() {
        return this.rotateGesturesEnabled && this.tiltGesturesEnabled && this.zoomGesturesEnabled && this.scrollGesturesEnabled && this.doubleTapGesturesEnabled && this.quickZoomGesturesEnabled;
    }

    private void saveFocalPoint(Bundle outState) {
        outState.putParcelable(MapboxConstants.STATE_USER_FOCAL_POINT, getFocalPoint());
    }

    private void restoreFocalPoint(Bundle savedInstanceState) {
        PointF pointF = (PointF) savedInstanceState.getParcelable(MapboxConstants.STATE_USER_FOCAL_POINT);
        if (pointF != null) {
            setFocalPoint(pointF);
        }
    }

    public void setFocalPoint(@Nullable PointF focalPoint) {
        this.userProvidedFocalPoint = focalPoint;
        this.focalPointChangeListener.onFocalPointChanged(focalPoint);
    }

    @Nullable
    public PointF getFocalPoint() {
        return this.userProvidedFocalPoint;
    }

    public float getHeight() {
        return this.projection.getHeight();
    }

    public float getWidth() {
        return this.projection.getWidth();
    }

    /* access modifiers changed from: package-private */
    public float getPixelRatio() {
        return this.pixelRatio;
    }

    public void invalidate() {
        setLogoMargins(getLogoMarginLeft(), getLogoMarginTop(), getLogoMarginRight(), getLogoMarginBottom());
        setCompassEnabled(isCompassEnabled());
        setCompassMargins(getCompassMarginLeft(), getCompassMarginTop(), getCompassMarginRight(), getCompassMarginBottom());
        setAttributionMargins(getAttributionMarginLeft(), getAttributionMarginTop(), getAttributionMarginRight(), getAttributionMarginBottom());
    }

    private void setWidgetGravity(@NonNull View view, int gravity) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
        layoutParams.gravity = gravity;
        view.setLayoutParams(layoutParams);
    }

    private void setWidgetMargins(@NonNull View view, int[] initMargins, int left, int top, int right, int bottom) {
        initMargins[0] = left;
        initMargins[1] = top;
        initMargins[2] = right;
        initMargins[3] = bottom;
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
        layoutParams.setMargins(left, top, right, bottom);
        if (Build.VERSION.SDK_INT >= 17) {
            layoutParams.setMarginStart(left);
            layoutParams.setMarginEnd(right);
        }
        view.setLayoutParams(layoutParams);
    }
}
