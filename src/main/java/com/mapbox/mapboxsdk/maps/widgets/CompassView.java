package com.mapbox.mapboxsdk.maps.widgets;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.mapbox.mapboxsdk.maps.MapboxMap;

public final class CompassView extends ImageView implements Runnable {
    private static final long TIME_FADE_ANIMATION = 500;
    public static final long TIME_MAP_NORTH_ANIMATION = 150;
    public static final long TIME_WAIT_IDLE = 500;
    private MapboxMap.OnCompassAnimationListener compassAnimationListener;
    @Nullable
    private ViewPropertyAnimatorCompat fadeAnimator;
    private boolean fadeCompassViewFacingNorth = true;
    private boolean isAnimating = false;
    private float rotation = 0.0f;

    public CompassView(@NonNull Context context) {
        super(context);
        initialize(context);
    }

    public CompassView(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public CompassView(@NonNull Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    private void initialize(Context context) {
        setEnabled(false);
        float screenDensity = context.getResources().getDisplayMetrics().density;
        setLayoutParams(new ViewGroup.LayoutParams((int) (48.0f * screenDensity), (int) (48.0f * screenDensity)));
    }

    public void injectCompassAnimationListener(@NonNull MapboxMap.OnCompassAnimationListener compassAnimationListener2) {
        this.compassAnimationListener = compassAnimationListener2;
    }

    public void isAnimating(boolean isAnimating2) {
        this.isAnimating = isAnimating2;
    }

    public void resetAnimation() {
        if (this.fadeAnimator != null) {
            this.fadeAnimator.cancel();
        }
        this.fadeAnimator = null;
    }

    public boolean isHidden() {
        return this.fadeCompassViewFacingNorth && isFacingNorth();
    }

    public boolean isFacingNorth() {
        return ((double) Math.abs(this.rotation)) >= 359.0d || ((double) Math.abs(this.rotation)) <= 1.0d;
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (!enabled || isHidden()) {
            resetAnimation();
            setAlpha(0.0f);
            setVisibility(4);
            return;
        }
        resetAnimation();
        setAlpha(1.0f);
        setVisibility(0);
        update((double) this.rotation);
    }

    public void update(double bearing) {
        this.rotation = (float) bearing;
        if (isEnabled()) {
            if (!isHidden()) {
                resetAnimation();
                setAlpha(1.0f);
                setVisibility(0);
                notifyCompassAnimationListenerWhenAnimating();
                setRotation(this.rotation);
            } else if (getVisibility() != 4 && this.fadeAnimator == null) {
                postDelayed(this, 500);
            }
        }
    }

    public void fadeCompassViewFacingNorth(boolean compassFadeFacingNorth) {
        this.fadeCompassViewFacingNorth = compassFadeFacingNorth;
    }

    public boolean isFadeCompassViewFacingNorth() {
        return this.fadeCompassViewFacingNorth;
    }

    public void setCompassImage(Drawable compass) {
        setImageDrawable(compass);
    }

    public Drawable getCompassImage() {
        return getDrawable();
    }

    public void run() {
        if (isHidden()) {
            this.compassAnimationListener.onCompassAnimationFinished();
            resetAnimation();
            setLayerType(2, null);
            this.fadeAnimator = ViewCompat.animate(this).alpha(0.0f).setDuration(500);
            this.fadeAnimator.setListener(new ViewPropertyAnimatorListenerAdapter() {
                /* class com.mapbox.mapboxsdk.maps.widgets.CompassView.AnonymousClass1 */

                public void onAnimationEnd(View view) {
                    CompassView.this.setLayerType(0, null);
                    CompassView.this.setVisibility(4);
                    CompassView.this.resetAnimation();
                }
            });
        }
    }

    private void notifyCompassAnimationListenerWhenAnimating() {
        if (this.isAnimating) {
            this.compassAnimationListener.onCompassAnimation();
        }
    }
}
