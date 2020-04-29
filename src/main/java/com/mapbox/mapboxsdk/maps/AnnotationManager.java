package com.mapbox.mapboxsdk.maps;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.LongSparseArray;
import android.view.View;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.R;
import com.mapbox.mapboxsdk.annotations.Annotation;
import com.mapbox.mapboxsdk.annotations.BaseMarkerOptions;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.Polygon;
import com.mapbox.mapboxsdk.annotations.PolygonOptions;
import com.mapbox.mapboxsdk.annotations.Polyline;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.log.Logger;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import java.util.ArrayList;
import java.util.List;

class AnnotationManager {
    private static final long NO_ANNOTATION_ID = -1;
    private static final String TAG = "Mbgl-AnnotationManager";
    private Annotations annotations;
    private final LongSparseArray<Annotation> annotationsArray;
    private final IconManager iconManager;
    private final InfoWindowManager infoWindowManager = new InfoWindowManager();
    @NonNull
    private final MapView mapView;
    private MapboxMap mapboxMap;
    private Markers markers;
    @Nullable
    private MapboxMap.OnMarkerClickListener onMarkerClickListener;
    @Nullable
    private MapboxMap.OnPolygonClickListener onPolygonClickListener;
    @Nullable
    private MapboxMap.OnPolylineClickListener onPolylineClickListener;
    private Polygons polygons;
    private Polylines polylines;
    private final List<Marker> selectedMarkers = new ArrayList();
    private ShapeAnnotations shapeAnnotations;

    AnnotationManager(@NonNull MapView mapView2, LongSparseArray<Annotation> annotationsArray2, IconManager iconManager2, Annotations annotations2, Markers markers2, Polygons polygons2, Polylines polylines2, ShapeAnnotations shapeAnnotations2) {
        this.mapView = mapView2;
        this.annotationsArray = annotationsArray2;
        this.iconManager = iconManager2;
        this.annotations = annotations2;
        this.markers = markers2;
        this.polygons = polygons2;
        this.polylines = polylines2;
        this.shapeAnnotations = shapeAnnotations2;
    }

    /* access modifiers changed from: package-private */
    @NonNull
    public AnnotationManager bind(MapboxMap mapboxMap2) {
        this.mapboxMap = mapboxMap2;
        return this;
    }

    /* access modifiers changed from: package-private */
    public void update() {
        this.infoWindowManager.update();
    }

    /* access modifiers changed from: package-private */
    public Annotation getAnnotation(long id) {
        return this.annotations.obtainBy(id);
    }

    /* access modifiers changed from: package-private */
    public List<Annotation> getAnnotations() {
        return this.annotations.obtainAll();
    }

    /* access modifiers changed from: package-private */
    public void removeAnnotation(long id) {
        this.annotations.removeBy(id);
    }

    /* access modifiers changed from: package-private */
    public void removeAnnotation(@NonNull Annotation annotation) {
        if (annotation instanceof Marker) {
            Marker marker = (Marker) annotation;
            marker.hideInfoWindow();
            if (this.selectedMarkers.contains(marker)) {
                this.selectedMarkers.remove(marker);
            }
            this.iconManager.iconCleanup(marker.getIcon());
        }
        this.annotations.removeBy(annotation);
    }

    /* access modifiers changed from: package-private */
    public void removeAnnotations(@NonNull List<? extends Annotation> annotationList) {
        for (Annotation annotation : annotationList) {
            if (annotation instanceof Marker) {
                Marker marker = (Marker) annotation;
                marker.hideInfoWindow();
                if (this.selectedMarkers.contains(marker)) {
                    this.selectedMarkers.remove(marker);
                }
                this.iconManager.iconCleanup(marker.getIcon());
            }
        }
        this.annotations.removeBy(annotationList);
    }

    /* access modifiers changed from: package-private */
    public void removeAnnotations() {
        int count = this.annotationsArray.size();
        long[] ids = new long[count];
        this.selectedMarkers.clear();
        for (int i = 0; i < count; i++) {
            ids[i] = this.annotationsArray.keyAt(i);
            Annotation annotation = this.annotationsArray.get(ids[i]);
            if (annotation instanceof Marker) {
                Marker marker = (Marker) annotation;
                marker.hideInfoWindow();
                this.iconManager.iconCleanup(marker.getIcon());
            }
        }
        this.annotations.removeAll();
    }

    /* access modifiers changed from: package-private */
    public Marker addMarker(@NonNull BaseMarkerOptions markerOptions, @NonNull MapboxMap mapboxMap2) {
        return this.markers.addBy(markerOptions, mapboxMap2);
    }

    /* access modifiers changed from: package-private */
    public List<Marker> addMarkers(@NonNull List<? extends BaseMarkerOptions> markerOptionsList, @NonNull MapboxMap mapboxMap2) {
        return this.markers.addBy(markerOptionsList, mapboxMap2);
    }

    /* access modifiers changed from: package-private */
    public void updateMarker(@NonNull Marker updatedMarker, @NonNull MapboxMap mapboxMap2) {
        if (!isAddedToMap(updatedMarker)) {
            logNonAdded(updatedMarker);
        } else {
            this.markers.update(updatedMarker, mapboxMap2);
        }
    }

    /* access modifiers changed from: package-private */
    public List<Marker> getMarkers() {
        return this.markers.obtainAll();
    }

    /* access modifiers changed from: package-private */
    @NonNull
    public List<Marker> getMarkersInRect(@NonNull RectF rectangle) {
        return this.markers.obtainAllIn(rectangle);
    }

    /* access modifiers changed from: package-private */
    public void reloadMarkers() {
        this.markers.reload();
    }

    /* access modifiers changed from: package-private */
    public Polygon addPolygon(@NonNull PolygonOptions polygonOptions, @NonNull MapboxMap mapboxMap2) {
        return this.polygons.addBy(polygonOptions, mapboxMap2);
    }

    /* access modifiers changed from: package-private */
    public List<Polygon> addPolygons(@NonNull List<PolygonOptions> polygonOptionsList, @NonNull MapboxMap mapboxMap2) {
        return this.polygons.addBy(polygonOptionsList, mapboxMap2);
    }

    /* access modifiers changed from: package-private */
    public void updatePolygon(@NonNull Polygon polygon) {
        if (!isAddedToMap(polygon)) {
            logNonAdded(polygon);
        } else {
            this.polygons.update(polygon);
        }
    }

    /* access modifiers changed from: package-private */
    public List<Polygon> getPolygons() {
        return this.polygons.obtainAll();
    }

    /* access modifiers changed from: package-private */
    public Polyline addPolyline(@NonNull PolylineOptions polylineOptions, @NonNull MapboxMap mapboxMap2) {
        return this.polylines.addBy(polylineOptions, mapboxMap2);
    }

    /* access modifiers changed from: package-private */
    public List<Polyline> addPolylines(@NonNull List<PolylineOptions> polylineOptionsList, @NonNull MapboxMap mapboxMap2) {
        return this.polylines.addBy(polylineOptionsList, mapboxMap2);
    }

    /* access modifiers changed from: package-private */
    public void updatePolyline(@NonNull Polyline polyline) {
        if (!isAddedToMap(polyline)) {
            logNonAdded(polyline);
        } else {
            this.polylines.update(polyline);
        }
    }

    /* access modifiers changed from: package-private */
    public List<Polyline> getPolylines() {
        return this.polylines.obtainAll();
    }

    /* access modifiers changed from: package-private */
    public void setOnMarkerClickListener(@Nullable MapboxMap.OnMarkerClickListener listener) {
        this.onMarkerClickListener = listener;
    }

    /* access modifiers changed from: package-private */
    public void setOnPolygonClickListener(@Nullable MapboxMap.OnPolygonClickListener listener) {
        this.onPolygonClickListener = listener;
    }

    /* access modifiers changed from: package-private */
    public void setOnPolylineClickListener(@Nullable MapboxMap.OnPolylineClickListener listener) {
        this.onPolylineClickListener = listener;
    }

    /* access modifiers changed from: package-private */
    public void selectMarker(@NonNull Marker marker) {
        if (!this.selectedMarkers.contains(marker)) {
            if (!this.infoWindowManager.isAllowConcurrentMultipleOpenInfoWindows()) {
                deselectMarkers();
            }
            if (this.infoWindowManager.isInfoWindowValidForMarker(marker) || this.infoWindowManager.getInfoWindowAdapter() != null) {
                this.infoWindowManager.add(marker.showInfoWindow(this.mapboxMap, this.mapView));
            }
            this.selectedMarkers.add(marker);
        }
    }

    /* access modifiers changed from: package-private */
    public void deselectMarkers() {
        if (!this.selectedMarkers.isEmpty()) {
            for (Marker marker : this.selectedMarkers) {
                if (marker != null && marker.isInfoWindowShown()) {
                    marker.hideInfoWindow();
                }
            }
            this.selectedMarkers.clear();
        }
    }

    /* access modifiers changed from: package-private */
    public void deselectMarker(@NonNull Marker marker) {
        if (this.selectedMarkers.contains(marker)) {
            if (marker.isInfoWindowShown()) {
                marker.hideInfoWindow();
            }
            this.selectedMarkers.remove(marker);
        }
    }

    /* access modifiers changed from: package-private */
    @NonNull
    public List<Marker> getSelectedMarkers() {
        return this.selectedMarkers;
    }

    /* access modifiers changed from: package-private */
    @NonNull
    public InfoWindowManager getInfoWindowManager() {
        return this.infoWindowManager;
    }

    /* access modifiers changed from: package-private */
    public void adjustTopOffsetPixels(@NonNull MapboxMap mapboxMap2) {
        int count = this.annotationsArray.size();
        for (int i = 0; i < count; i++) {
            Annotation annotation = this.annotationsArray.get((long) i);
            if (annotation instanceof Marker) {
                Marker marker = (Marker) annotation;
                marker.setTopOffsetPixels(this.iconManager.getTopOffsetPixelsForIcon(marker.getIcon()));
            }
        }
        for (Marker marker2 : this.selectedMarkers) {
            if (marker2.isInfoWindowShown()) {
                marker2.hideInfoWindow();
                marker2.showInfoWindow(mapboxMap2, this.mapView);
            }
        }
    }

    private boolean isAddedToMap(@Nullable Annotation annotation) {
        return (annotation == null || annotation.getId() == -1 || this.annotationsArray.indexOfKey(annotation.getId()) <= -1) ? false : true;
    }

    private void logNonAdded(@NonNull Annotation annotation) {
        Logger.w(TAG, String.format("Attempting to update non-added %s with value %s", annotation.getClass().getCanonicalName(), annotation));
    }

    /* access modifiers changed from: package-private */
    public boolean onTap(@NonNull PointF tapPoint) {
        long markerId = new MarkerHitResolver(this.mapboxMap).execute(getMarkerHitFromTouchArea(tapPoint));
        if (markerId != -1 && isClickHandledForMarker(markerId)) {
            return true;
        }
        Annotation annotation = new ShapeAnnotationHitResolver(this.shapeAnnotations).execute(getShapeAnnotationHitFromTap(tapPoint));
        if (annotation == null || !handleClickForShapeAnnotation(annotation)) {
            return false;
        }
        return true;
    }

    private ShapeAnnotationHit getShapeAnnotationHitFromTap(PointF tapPoint) {
        float touchTargetSide = Mapbox.getApplicationContext().getResources().getDimension(R.dimen.mapbox_eight_dp);
        return new ShapeAnnotationHit(new RectF(tapPoint.x - touchTargetSide, tapPoint.y - touchTargetSide, tapPoint.x + touchTargetSide, tapPoint.y + touchTargetSide));
    }

    private boolean handleClickForShapeAnnotation(Annotation annotation) {
        if ((annotation instanceof Polygon) && this.onPolygonClickListener != null) {
            this.onPolygonClickListener.onPolygonClick((Polygon) annotation);
            return true;
        } else if (!(annotation instanceof Polyline) || this.onPolylineClickListener == null) {
            return false;
        } else {
            this.onPolylineClickListener.onPolylineClick((Polyline) annotation);
            return true;
        }
    }

    private MarkerHit getMarkerHitFromTouchArea(PointF tapPoint) {
        int touchSurfaceWidth = (int) (((double) this.iconManager.getHighestIconHeight()) * 1.5d);
        int touchSurfaceHeight = (int) (((double) this.iconManager.getHighestIconWidth()) * 1.5d);
        RectF tapRect = new RectF(tapPoint.x - ((float) touchSurfaceWidth), tapPoint.y - ((float) touchSurfaceHeight), tapPoint.x + ((float) touchSurfaceWidth), tapPoint.y + ((float) touchSurfaceHeight));
        return new MarkerHit(tapRect, getMarkersInRect(tapRect));
    }

    private boolean isClickHandledForMarker(long markerId) {
        Marker marker = (Marker) getAnnotation(markerId);
        if (onClickMarker(marker)) {
            return true;
        }
        toggleMarkerSelectionState(marker);
        return true;
    }

    private boolean onClickMarker(@NonNull Marker marker) {
        return this.onMarkerClickListener != null && this.onMarkerClickListener.onMarkerClick(marker);
    }

    private void toggleMarkerSelectionState(@NonNull Marker marker) {
        if (!this.selectedMarkers.contains(marker)) {
            selectMarker(marker);
        } else {
            deselectMarker(marker);
        }
    }

    private static class ShapeAnnotationHitResolver {
        private ShapeAnnotations shapeAnnotations;

        ShapeAnnotationHitResolver(ShapeAnnotations shapeAnnotations2) {
            this.shapeAnnotations = shapeAnnotations2;
        }

        @Nullable
        public Annotation execute(@NonNull ShapeAnnotationHit shapeHit) {
            List<Annotation> annotations = this.shapeAnnotations.obtainAllIn(shapeHit.tapPoint);
            if (annotations.size() > 0) {
                return annotations.get(0);
            }
            return null;
        }
    }

    private static class MarkerHitResolver {
        private Bitmap bitmap;
        private int bitmapHeight;
        private int bitmapWidth;
        private long closestMarkerId = -1;
        @NonNull
        private RectF highestSurfaceIntersection = new RectF();
        @NonNull
        private RectF hitRectMarker = new RectF();
        @NonNull
        private Rect hitRectView = new Rect();
        private PointF markerLocation;
        private final int minimalTouchSize;
        @NonNull
        private final Projection projection;
        @Nullable
        private View view;

        MarkerHitResolver(@NonNull MapboxMap mapboxMap) {
            this.projection = mapboxMap.getProjection();
            this.minimalTouchSize = (int) (32.0f * Mapbox.getApplicationContext().getResources().getDisplayMetrics().density);
        }

        public long execute(@NonNull MarkerHit markerHit) {
            resolveForMarkers(markerHit);
            return this.closestMarkerId;
        }

        private void resolveForMarkers(MarkerHit markerHit) {
            for (Marker marker : markerHit.markers) {
                resolveForMarker(markerHit, marker);
            }
        }

        private void resolveForMarker(@NonNull MarkerHit markerHit, Marker marker) {
            this.markerLocation = this.projection.toScreenLocation(marker.getPosition());
            this.bitmap = marker.getIcon().getBitmap();
            this.bitmapHeight = this.bitmap.getHeight();
            if (this.bitmapHeight < this.minimalTouchSize) {
                this.bitmapHeight = this.minimalTouchSize;
            }
            this.bitmapWidth = this.bitmap.getWidth();
            if (this.bitmapWidth < this.minimalTouchSize) {
                this.bitmapWidth = this.minimalTouchSize;
            }
            this.hitRectMarker.set(0.0f, 0.0f, (float) this.bitmapWidth, (float) this.bitmapHeight);
            this.hitRectMarker.offsetTo(this.markerLocation.x - ((float) (this.bitmapWidth / 2)), this.markerLocation.y - ((float) (this.bitmapHeight / 2)));
            hitTestMarker(markerHit, marker, this.hitRectMarker);
        }

        private void hitTestMarker(MarkerHit markerHit, @NonNull Marker marker, RectF hitRectMarker2) {
            if (hitRectMarker2.contains(markerHit.getTapPointX(), markerHit.getTapPointY())) {
                hitRectMarker2.intersect(markerHit.tapRect);
                if (isRectangleHighestSurfaceIntersection(hitRectMarker2)) {
                    this.highestSurfaceIntersection = new RectF(hitRectMarker2);
                    this.closestMarkerId = marker.getId();
                }
            }
        }

        private boolean isRectangleHighestSurfaceIntersection(RectF rectF) {
            return rectF.width() * rectF.height() > this.highestSurfaceIntersection.width() * this.highestSurfaceIntersection.height();
        }
    }

    private static class ShapeAnnotationHit {
        /* access modifiers changed from: private */
        public final RectF tapPoint;

        ShapeAnnotationHit(RectF tapPoint2) {
            this.tapPoint = tapPoint2;
        }
    }

    private static class MarkerHit {
        /* access modifiers changed from: private */
        public final List<Marker> markers;
        /* access modifiers changed from: private */
        public final RectF tapRect;

        MarkerHit(RectF tapRect2, List<Marker> markers2) {
            this.tapRect = tapRect2;
            this.markers = markers2;
        }

        /* access modifiers changed from: package-private */
        public float getTapPointX() {
            return this.tapRect.centerX();
        }

        /* access modifiers changed from: package-private */
        public float getTapPointY() {
            return this.tapRect.centerY();
        }
    }
}
