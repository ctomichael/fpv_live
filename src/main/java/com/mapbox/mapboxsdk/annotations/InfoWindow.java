package com.mapbox.mapboxsdk.annotations;

import android.content.res.Resources;
import android.graphics.PointF;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.mapbox.mapboxsdk.R;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import java.lang.ref.WeakReference;

@Deprecated
public class InfoWindow {
    private WeakReference<Marker> boundMarker;
    @Nullable
    private final ViewTreeObserver.OnGlobalLayoutListener contentUpdateListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        /* class com.mapbox.mapboxsdk.annotations.InfoWindow.AnonymousClass3 */

        public void onGlobalLayout() {
            View view = InfoWindow.this.view.get();
            if (view != null) {
                if (Build.VERSION.SDK_INT >= 16) {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                float unused = InfoWindow.this.viewHeightOffset = ((float) (-view.getMeasuredHeight())) + InfoWindow.this.markerHeightOffset;
                InfoWindow.this.update();
            }
        }
    };
    private PointF coordinates;
    private boolean isVisible;
    @LayoutRes
    private int layoutRes;
    /* access modifiers changed from: private */
    public WeakReference<MapboxMap> mapboxMap;
    /* access modifiers changed from: private */
    public float markerHeightOffset;
    private float markerWidthOffset;
    protected WeakReference<View> view;
    /* access modifiers changed from: private */
    public float viewHeightOffset;
    private float viewWidthOffset;

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{android.view.LayoutInflater.inflate(int, android.view.ViewGroup, boolean):android.view.View}
     arg types: [int, com.mapbox.mapboxsdk.maps.MapView, int]
     candidates:
      ClspMth{android.view.LayoutInflater.inflate(org.xmlpull.v1.XmlPullParser, android.view.ViewGroup, boolean):android.view.View}
      ClspMth{android.view.LayoutInflater.inflate(int, android.view.ViewGroup, boolean):android.view.View} */
    InfoWindow(MapView mapView, int layoutResId, MapboxMap mapboxMap2) {
        this.layoutRes = layoutResId;
        initialize(LayoutInflater.from(mapView.getContext()).inflate(layoutResId, (ViewGroup) mapView, false), mapboxMap2);
    }

    InfoWindow(@NonNull View view2, MapboxMap mapboxMap2) {
        initialize(view2, mapboxMap2);
    }

    private void initialize(@NonNull View view2, MapboxMap mapboxMap2) {
        this.mapboxMap = new WeakReference<>(mapboxMap2);
        this.isVisible = false;
        this.view = new WeakReference<>(view2);
        view2.setOnClickListener(new View.OnClickListener() {
            /* class com.mapbox.mapboxsdk.annotations.InfoWindow.AnonymousClass1 */

            public void onClick(View v) {
                MapboxMap mapboxMap = (MapboxMap) InfoWindow.this.mapboxMap.get();
                if (mapboxMap != null) {
                    MapboxMap.OnInfoWindowClickListener onInfoWindowClickListener = mapboxMap.getOnInfoWindowClickListener();
                    boolean handledDefaultClick = false;
                    if (onInfoWindowClickListener != null) {
                        handledDefaultClick = onInfoWindowClickListener.onInfoWindowClick(InfoWindow.this.getBoundMarker());
                    }
                    if (!handledDefaultClick) {
                        InfoWindow.this.closeInfoWindow();
                    }
                }
            }
        });
        view2.setOnLongClickListener(new View.OnLongClickListener() {
            /* class com.mapbox.mapboxsdk.annotations.InfoWindow.AnonymousClass2 */

            public boolean onLongClick(View v) {
                MapboxMap.OnInfoWindowLongClickListener listener;
                MapboxMap mapboxMap = (MapboxMap) InfoWindow.this.mapboxMap.get();
                if (mapboxMap == null || (listener = mapboxMap.getOnInfoWindowLongClickListener()) == null) {
                    return true;
                }
                listener.onInfoWindowLongClick(InfoWindow.this.getBoundMarker());
                return true;
            }
        });
    }

    /* access modifiers changed from: private */
    public void closeInfoWindow() {
        MapboxMap mapbox = this.mapboxMap.get();
        Marker marker = this.boundMarker.get();
        if (!(marker == null || mapbox == null)) {
            mapbox.deselectMarker(marker);
        }
        close();
    }

    /* access modifiers changed from: package-private */
    @NonNull
    public InfoWindow open(@NonNull MapView mapView, Marker boundMarker2, @NonNull LatLng position, int offsetX, int offsetY) {
        setBoundMarker(boundMarker2);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(-2, -2);
        MapboxMap mapboxMap2 = this.mapboxMap.get();
        View view2 = this.view.get();
        if (!(view2 == null || mapboxMap2 == null)) {
            view2.measure(0, 0);
            this.markerHeightOffset = (float) offsetY;
            this.markerWidthOffset = (float) (-offsetX);
            this.coordinates = mapboxMap2.getProjection().toScreenLocation(position);
            float x = (this.coordinates.x - ((float) (view2.getMeasuredWidth() / 2))) + ((float) offsetX);
            float y = (this.coordinates.y - ((float) view2.getMeasuredHeight())) + ((float) offsetY);
            if (view2 instanceof BubbleLayout) {
                Resources resources = mapView.getContext().getResources();
                float rightSideInfowWindow = x + ((float) view2.getMeasuredWidth());
                float leftSideInfoWindow = x;
                float mapRight = (float) mapView.getRight();
                float mapLeft = (float) mapView.getLeft();
                float marginHorizontal = resources.getDimension(R.dimen.mapbox_infowindow_margin);
                float tipViewOffset = resources.getDimension(R.dimen.mapbox_infowindow_tipview_width) / 2.0f;
                float tipViewMarginLeft = ((float) (view2.getMeasuredWidth() / 2)) - tipViewOffset;
                boolean outOfBoundsLeft = false;
                boolean outOfBoundsRight = false;
                if (this.coordinates.x >= 0.0f && this.coordinates.x <= ((float) mapView.getWidth()) && this.coordinates.y >= 0.0f && this.coordinates.y <= ((float) mapView.getHeight())) {
                    if (rightSideInfowWindow > mapRight) {
                        outOfBoundsRight = true;
                        x -= rightSideInfowWindow - mapRight;
                        tipViewMarginLeft += (rightSideInfowWindow - mapRight) + tipViewOffset;
                        rightSideInfowWindow = x + ((float) view2.getMeasuredWidth());
                    }
                    if (leftSideInfoWindow < mapLeft) {
                        outOfBoundsLeft = true;
                        x += mapLeft - leftSideInfoWindow;
                        tipViewMarginLeft -= (mapLeft - leftSideInfoWindow) + tipViewOffset;
                        leftSideInfoWindow = x;
                    }
                    if (outOfBoundsRight && mapRight - rightSideInfowWindow < marginHorizontal) {
                        x -= marginHorizontal - (mapRight - rightSideInfowWindow);
                        tipViewMarginLeft += (marginHorizontal - (mapRight - rightSideInfowWindow)) - tipViewOffset;
                        leftSideInfoWindow = x;
                    }
                    if (outOfBoundsLeft && leftSideInfoWindow - mapLeft < marginHorizontal) {
                        x += marginHorizontal - (leftSideInfoWindow - mapLeft);
                        tipViewMarginLeft -= (marginHorizontal - (leftSideInfoWindow - mapLeft)) - tipViewOffset;
                    }
                }
                ((BubbleLayout) view2).setArrowPosition(tipViewMarginLeft);
            }
            view2.setX(x);
            view2.setY(y);
            this.viewWidthOffset = (x - this.coordinates.x) - ((float) offsetX);
            this.viewHeightOffset = (float) ((-view2.getMeasuredHeight()) + offsetY);
            close();
            mapView.addView(view2, lp);
            this.isVisible = true;
        }
        return this;
    }

    /* access modifiers changed from: package-private */
    @NonNull
    public InfoWindow close() {
        MapboxMap mapboxMap2 = this.mapboxMap.get();
        if (this.isVisible && mapboxMap2 != null) {
            this.isVisible = false;
            View view2 = this.view.get();
            if (!(view2 == null || view2.getParent() == null)) {
                ((ViewGroup) view2.getParent()).removeView(view2);
            }
            Marker marker = getBoundMarker();
            MapboxMap.OnInfoWindowCloseListener listener = mapboxMap2.getOnInfoWindowCloseListener();
            if (listener != null) {
                listener.onInfoWindowClose(marker);
            }
            setBoundMarker(null);
        }
        return this;
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{android.view.LayoutInflater.inflate(int, android.view.ViewGroup, boolean):android.view.View}
     arg types: [int, com.mapbox.mapboxsdk.maps.MapView, int]
     candidates:
      ClspMth{android.view.LayoutInflater.inflate(org.xmlpull.v1.XmlPullParser, android.view.ViewGroup, boolean):android.view.View}
      ClspMth{android.view.LayoutInflater.inflate(int, android.view.ViewGroup, boolean):android.view.View} */
    /* access modifiers changed from: package-private */
    public void adaptDefaultMarker(@NonNull Marker overlayItem, MapboxMap mapboxMap2, @NonNull MapView mapView) {
        View view2 = this.view.get();
        if (view2 == null) {
            view2 = LayoutInflater.from(mapView.getContext()).inflate(this.layoutRes, (ViewGroup) mapView, false);
            initialize(view2, mapboxMap2);
        }
        this.mapboxMap = new WeakReference<>(mapboxMap2);
        String title = overlayItem.getTitle();
        TextView titleTextView = (TextView) view2.findViewById(R.id.infowindow_title);
        if (!TextUtils.isEmpty(title)) {
            titleTextView.setText(title);
            titleTextView.setVisibility(0);
        } else {
            titleTextView.setVisibility(8);
        }
        String snippet = overlayItem.getSnippet();
        TextView snippetTextView = (TextView) view2.findViewById(R.id.infowindow_description);
        if (!TextUtils.isEmpty(snippet)) {
            snippetTextView.setText(snippet);
            snippetTextView.setVisibility(0);
            return;
        }
        snippetTextView.setVisibility(8);
    }

    /* access modifiers changed from: package-private */
    @NonNull
    public InfoWindow setBoundMarker(Marker boundMarker2) {
        this.boundMarker = new WeakReference<>(boundMarker2);
        return this;
    }

    /* access modifiers changed from: package-private */
    @Nullable
    public Marker getBoundMarker() {
        if (this.boundMarker == null) {
            return null;
        }
        return this.boundMarker.get();
    }

    public void update() {
        MapboxMap mapboxMap2 = this.mapboxMap.get();
        Marker marker = this.boundMarker.get();
        View view2 = this.view.get();
        if (mapboxMap2 != null && marker != null && view2 != null) {
            this.coordinates = mapboxMap2.getProjection().toScreenLocation(marker.getPosition());
            if (view2 instanceof BubbleLayout) {
                view2.setX((this.coordinates.x + this.viewWidthOffset) - this.markerWidthOffset);
            } else {
                view2.setX((this.coordinates.x - ((float) (view2.getMeasuredWidth() / 2))) - this.markerWidthOffset);
            }
            view2.setY(this.coordinates.y + this.viewHeightOffset);
        }
    }

    /* access modifiers changed from: package-private */
    public void onContentUpdate() {
        View view2 = this.view.get();
        if (view2 != null) {
            ViewTreeObserver viewTreeObserver = view2.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(this.contentUpdateListener);
            }
        }
    }

    @Nullable
    public View getView() {
        if (this.view != null) {
            return this.view.get();
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public boolean isVisible() {
        return this.isVisible;
    }
}
