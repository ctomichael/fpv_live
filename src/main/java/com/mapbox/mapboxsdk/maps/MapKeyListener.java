package com.mapbox.mapboxsdk.maps;

import android.graphics.PointF;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

final class MapKeyListener {
    /* access modifiers changed from: private */
    @Nullable
    public TrackballLongPressTimeOut currentTrackballLongPressTimeOut;
    /* access modifiers changed from: private */
    public final MapGestureDetector mapGestureDetector;
    private final Transform transform;
    /* access modifiers changed from: private */
    public final UiSettings uiSettings;

    MapKeyListener(Transform transform2, UiSettings uiSettings2, MapGestureDetector mapGestureDetector2) {
        this.transform = transform2;
        this.uiSettings = uiSettings2;
        this.mapGestureDetector = mapGestureDetector2;
    }

    /* access modifiers changed from: package-private */
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        double scrollDist = event.getRepeatCount() >= 5 ? 50.0d : 10.0d;
        switch (keyCode) {
            case 19:
                if (!this.uiSettings.isScrollGesturesEnabled()) {
                    return false;
                }
                this.transform.cancelTransitions();
                this.transform.moveBy(0.0d, scrollDist, 0);
                return true;
            case 20:
                if (!this.uiSettings.isScrollGesturesEnabled()) {
                    return false;
                }
                this.transform.cancelTransitions();
                this.transform.moveBy(0.0d, -scrollDist, 0);
                return true;
            case 21:
                if (!this.uiSettings.isScrollGesturesEnabled()) {
                    return false;
                }
                this.transform.cancelTransitions();
                this.transform.moveBy(scrollDist, 0.0d, 0);
                return true;
            case 22:
                if (!this.uiSettings.isScrollGesturesEnabled()) {
                    return false;
                }
                this.transform.cancelTransitions();
                this.transform.moveBy(-scrollDist, 0.0d, 0);
                return true;
            case 23:
            case 66:
                event.startTracking();
                return true;
            default:
                return false;
        }
    }

    /* access modifiers changed from: package-private */
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case 23:
            case 66:
                if (!this.uiSettings.isZoomGesturesEnabled()) {
                    return false;
                }
                this.mapGestureDetector.zoomOutAnimated(new PointF(this.uiSettings.getWidth() / 2.0f, this.uiSettings.getHeight() / 2.0f), true);
                return true;
            default:
                return false;
        }
    }

    /* access modifiers changed from: package-private */
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (event.isCanceled()) {
            return false;
        }
        switch (keyCode) {
            case 23:
            case 66:
                if (!this.uiSettings.isZoomGesturesEnabled()) {
                    return false;
                }
                this.mapGestureDetector.zoomInAnimated(new PointF(this.uiSettings.getWidth() / 2.0f, this.uiSettings.getHeight() / 2.0f), true);
                return true;
            default:
                return false;
        }
    }

    /* access modifiers changed from: package-private */
    public boolean onTrackballEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case 0:
                if (this.currentTrackballLongPressTimeOut != null) {
                    this.currentTrackballLongPressTimeOut.cancel();
                    this.currentTrackballLongPressTimeOut = null;
                }
                this.currentTrackballLongPressTimeOut = new TrackballLongPressTimeOut();
                new Handler(Looper.getMainLooper()).postDelayed(this.currentTrackballLongPressTimeOut, (long) ViewConfiguration.getLongPressTimeout());
                return true;
            case 1:
                if (!this.uiSettings.isZoomGesturesEnabled()) {
                    return false;
                }
                if (this.currentTrackballLongPressTimeOut != null) {
                    this.mapGestureDetector.zoomInAnimated(new PointF(this.uiSettings.getWidth() / 2.0f, this.uiSettings.getHeight() / 2.0f), true);
                }
                return true;
            case 2:
                if (!this.uiSettings.isScrollGesturesEnabled()) {
                    return false;
                }
                this.transform.cancelTransitions();
                this.transform.moveBy(((double) event.getX()) * -10.0d, ((double) event.getY()) * -10.0d, 0);
                return true;
            case 3:
                if (this.currentTrackballLongPressTimeOut != null) {
                    this.currentTrackballLongPressTimeOut.cancel();
                    this.currentTrackballLongPressTimeOut = null;
                }
                return true;
            default:
                return false;
        }
    }

    private class TrackballLongPressTimeOut implements Runnable {
        private boolean cancelled = false;

        TrackballLongPressTimeOut() {
        }

        public void cancel() {
            this.cancelled = true;
        }

        public void run() {
            if (!this.cancelled) {
                MapKeyListener.this.mapGestureDetector.zoomOutAnimated(new PointF(MapKeyListener.this.uiSettings.getWidth() / 2.0f, MapKeyListener.this.uiSettings.getHeight() / 2.0f), true);
                TrackballLongPressTimeOut unused = MapKeyListener.this.currentTrackballLongPressTimeOut = null;
            }
        }
    }
}
