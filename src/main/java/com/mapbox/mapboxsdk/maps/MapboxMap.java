package com.mapbox.mapboxsdk.maps;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Size;
import android.support.annotation.UiThread;
import android.text.TextUtils;
import android.view.View;
import com.mapbox.android.gestures.AndroidGesturesManager;
import com.mapbox.android.gestures.MoveGestureDetector;
import com.mapbox.android.gestures.RotateGestureDetector;
import com.mapbox.android.gestures.ShoveGestureDetector;
import com.mapbox.android.gestures.StandardScaleGestureDetector;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Geometry;
import com.mapbox.mapboxsdk.MapStrictMode;
import com.mapbox.mapboxsdk.annotations.Annotation;
import com.mapbox.mapboxsdk.annotations.BaseMarkerOptions;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.Polygon;
import com.mapbox.mapboxsdk.annotations.PolygonOptions;
import com.mapbox.mapboxsdk.annotations.Polyline;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdate;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.MapboxConstants;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.log.Logger;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.offline.OfflineRegionDefinition;
import com.mapbox.mapboxsdk.style.expressions.Expression;
import java.util.ArrayList;
import java.util.List;

@UiThread
public final class MapboxMap {
    private static final String TAG = "Mbgl-MapboxMap";
    private AnnotationManager annotationManager;
    private final List<Style.OnStyleLoaded> awaitingStyleGetters = new ArrayList();
    private final CameraChangeDispatcher cameraChangeDispatcher;
    private boolean debugActive;
    private final List<OnDeveloperAnimationListener> developerAnimationStartedListeners;
    private LocationComponent locationComponent;
    private final NativeMap nativeMapView;
    @Nullable
    private OnFpsChangedListener onFpsChangedListener;
    private final OnGesturesManagerInteractionListener onGesturesManagerInteractionListener;
    private final Projection projection;
    @Nullable
    private Style style;
    @Nullable
    private Style.OnStyleLoaded styleLoadedCallback;
    private final Transform transform;
    private final UiSettings uiSettings;

    public interface CancelableCallback {
        void onCancel();

        void onFinish();
    }

    @Deprecated
    public interface InfoWindowAdapter {
        @Nullable
        View getInfoWindow(@NonNull Marker marker);
    }

    public interface OnCameraIdleListener {
        void onCameraIdle();
    }

    public interface OnCameraMoveCanceledListener {
        void onCameraMoveCanceled();
    }

    public interface OnCameraMoveListener {
        void onCameraMove();
    }

    public interface OnCameraMoveStartedListener {
        public static final int REASON_API_ANIMATION = 3;
        public static final int REASON_API_GESTURE = 1;
        public static final int REASON_DEVELOPER_ANIMATION = 2;

        void onCameraMoveStarted(int i);
    }

    public interface OnCompassAnimationListener {
        void onCompassAnimation();

        void onCompassAnimationFinished();
    }

    public interface OnDeveloperAnimationListener {
        void onDeveloperAnimationStarted();
    }

    public interface OnFlingListener {
        void onFling();
    }

    public interface OnFpsChangedListener {
        void onFpsChanged(double d);
    }

    interface OnGesturesManagerInteractionListener {
        void cancelAllVelocityAnimations();

        AndroidGesturesManager getGesturesManager();

        void onAddFlingListener(OnFlingListener onFlingListener);

        void onAddMapClickListener(OnMapClickListener onMapClickListener);

        void onAddMapLongClickListener(OnMapLongClickListener onMapLongClickListener);

        void onAddMoveListener(OnMoveListener onMoveListener);

        void onAddRotateListener(OnRotateListener onRotateListener);

        void onAddScaleListener(OnScaleListener onScaleListener);

        void onAddShoveListener(OnShoveListener onShoveListener);

        void onRemoveFlingListener(OnFlingListener onFlingListener);

        void onRemoveMapClickListener(OnMapClickListener onMapClickListener);

        void onRemoveMapLongClickListener(OnMapLongClickListener onMapLongClickListener);

        void onRemoveMoveListener(OnMoveListener onMoveListener);

        void onRemoveRotateListener(OnRotateListener onRotateListener);

        void onRemoveScaleListener(OnScaleListener onScaleListener);

        void onRemoveShoveListener(OnShoveListener onShoveListener);

        void setGesturesManager(AndroidGesturesManager androidGesturesManager, boolean z, boolean z2);
    }

    public interface OnInfoWindowClickListener {
        boolean onInfoWindowClick(@NonNull Marker marker);
    }

    public interface OnInfoWindowCloseListener {
        void onInfoWindowClose(@NonNull Marker marker);
    }

    public interface OnInfoWindowLongClickListener {
        void onInfoWindowLongClick(@NonNull Marker marker);
    }

    public interface OnMapClickListener {
        boolean onMapClick(@NonNull LatLng latLng);
    }

    public interface OnMapLongClickListener {
        boolean onMapLongClick(@NonNull LatLng latLng);
    }

    @Deprecated
    public interface OnMarkerClickListener {
        boolean onMarkerClick(@NonNull Marker marker);
    }

    public interface OnMoveListener {
        void onMove(@NonNull MoveGestureDetector moveGestureDetector);

        void onMoveBegin(@NonNull MoveGestureDetector moveGestureDetector);

        void onMoveEnd(@NonNull MoveGestureDetector moveGestureDetector);
    }

    @Deprecated
    public interface OnPolygonClickListener {
        void onPolygonClick(@NonNull Polygon polygon);
    }

    @Deprecated
    public interface OnPolylineClickListener {
        void onPolylineClick(@NonNull Polyline polyline);
    }

    public interface OnRotateListener {
        void onRotate(@NonNull RotateGestureDetector rotateGestureDetector);

        void onRotateBegin(@NonNull RotateGestureDetector rotateGestureDetector);

        void onRotateEnd(@NonNull RotateGestureDetector rotateGestureDetector);
    }

    public interface OnScaleListener {
        void onScale(@NonNull StandardScaleGestureDetector standardScaleGestureDetector);

        void onScaleBegin(@NonNull StandardScaleGestureDetector standardScaleGestureDetector);

        void onScaleEnd(@NonNull StandardScaleGestureDetector standardScaleGestureDetector);
    }

    public interface OnShoveListener {
        void onShove(@NonNull ShoveGestureDetector shoveGestureDetector);

        void onShoveBegin(@NonNull ShoveGestureDetector shoveGestureDetector);

        void onShoveEnd(@NonNull ShoveGestureDetector shoveGestureDetector);
    }

    public interface SnapshotReadyCallback {
        void onSnapshotReady(@NonNull Bitmap bitmap);
    }

    MapboxMap(NativeMap map, Transform transform2, UiSettings ui, Projection projection2, OnGesturesManagerInteractionListener listener, CameraChangeDispatcher cameraChangeDispatcher2, List<OnDeveloperAnimationListener> developerAnimationStartedListeners2) {
        this.nativeMapView = map;
        this.uiSettings = ui;
        this.projection = projection2;
        this.transform = transform2;
        this.onGesturesManagerInteractionListener = listener;
        this.cameraChangeDispatcher = cameraChangeDispatcher2;
        this.developerAnimationStartedListeners = developerAnimationStartedListeners2;
    }

    /* access modifiers changed from: package-private */
    public void initialise(@NonNull Context context, @NonNull MapboxMapOptions options) {
        this.transform.initialise(this, options);
        this.uiSettings.initialise(context, options);
        setDebugActive(options.getDebugActive());
        setApiBaseUrl(options);
        setPrefetchesTiles(options);
    }

    public void getStyle(@NonNull Style.OnStyleLoaded onStyleLoaded) {
        if (this.style == null || !this.style.isFullyLoaded()) {
            this.awaitingStyleGetters.add(onStyleLoaded);
        } else {
            onStyleLoaded.onStyleLoaded(this.style);
        }
    }

    @Nullable
    public Style getStyle() {
        if (this.style == null || !this.style.isFullyLoaded()) {
            return null;
        }
        return this.style;
    }

    /* access modifiers changed from: package-private */
    public void onStart() {
        this.locationComponent.onStart();
    }

    /* access modifiers changed from: package-private */
    public void onStop() {
        this.locationComponent.onStop();
    }

    /* access modifiers changed from: package-private */
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(MapboxConstants.STATE_CAMERA_POSITION, this.transform.getCameraPosition());
        outState.putBoolean(MapboxConstants.STATE_DEBUG_ACTIVE, isDebugActive());
        this.uiSettings.onSaveInstanceState(outState);
    }

    /* access modifiers changed from: package-private */
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        CameraPosition cameraPosition = (CameraPosition) savedInstanceState.getParcelable(MapboxConstants.STATE_CAMERA_POSITION);
        this.uiSettings.onRestoreInstanceState(savedInstanceState);
        if (cameraPosition != null) {
            moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder(cameraPosition).build()));
        }
        this.nativeMapView.setDebug(savedInstanceState.getBoolean(MapboxConstants.STATE_DEBUG_ACTIVE));
    }

    /* access modifiers changed from: package-private */
    public void onDestroy() {
        this.locationComponent.onDestroy();
        if (this.style != null) {
            this.style.clear();
        }
        this.cameraChangeDispatcher.onDestroy();
    }

    /* access modifiers changed from: package-private */
    public void onPreMapReady() {
        this.transform.invalidateCameraPosition();
        this.annotationManager.reloadMarkers();
        this.annotationManager.adjustTopOffsetPixels(this);
    }

    /* access modifiers changed from: package-private */
    public void onPostMapReady() {
        this.transform.invalidateCameraPosition();
    }

    /* access modifiers changed from: package-private */
    public void onFinishLoadingStyle() {
        notifyStyleLoaded();
    }

    /* access modifiers changed from: package-private */
    public void onFailLoadingStyle() {
        this.styleLoadedCallback = null;
    }

    /* access modifiers changed from: package-private */
    public void onUpdateRegionChange() {
        this.annotationManager.update();
    }

    /* access modifiers changed from: package-private */
    public void onUpdateFullyRendered() {
        CameraPosition cameraPosition = this.transform.invalidateCameraPosition();
        if (cameraPosition != null) {
            this.uiSettings.update(cameraPosition);
        }
    }

    /* access modifiers changed from: package-private */
    public long getNativeMapPtr() {
        return this.nativeMapView.getNativePtr();
    }

    private void setPrefetchesTiles(@NonNull MapboxMapOptions options) {
        setPrefetchesTiles(options.getPrefetchesTiles());
    }

    public void setPrefetchesTiles(boolean enable) {
        this.nativeMapView.setPrefetchTiles(enable);
    }

    public boolean getPrefetchesTiles() {
        return this.nativeMapView.getPrefetchTiles();
    }

    public void setMinZoomPreference(@FloatRange(from = 0.0d, to = 25.5d) double minZoom) {
        this.transform.setMinZoom(minZoom);
    }

    public double getMinZoomLevel() {
        return this.transform.getMinZoom();
    }

    public void setMaxZoomPreference(@FloatRange(from = 0.0d, to = 25.5d) double maxZoom) {
        this.transform.setMaxZoom(maxZoom);
    }

    public double getMaxZoomLevel() {
        return this.transform.getMaxZoom();
    }

    @NonNull
    public UiSettings getUiSettings() {
        return this.uiSettings;
    }

    @NonNull
    public Projection getProjection() {
        return this.projection;
    }

    public void cancelTransitions() {
        this.transform.cancelTransitions();
    }

    @NonNull
    public final CameraPosition getCameraPosition() {
        return this.transform.getCameraPosition();
    }

    public void setCameraPosition(@NonNull CameraPosition cameraPosition) {
        moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), null);
    }

    public final void moveCamera(@NonNull CameraUpdate update) {
        moveCamera(update, null);
    }

    public final void moveCamera(@NonNull CameraUpdate update, @Nullable CancelableCallback callback) {
        notifyDeveloperAnimationListeners();
        this.transform.moveCamera(this, update, callback);
    }

    public final void easeCamera(@NonNull CameraUpdate update) {
        easeCamera(update, 300);
    }

    public final void easeCamera(@NonNull CameraUpdate update, @Nullable CancelableCallback callback) {
        easeCamera(update, 300, callback);
    }

    public final void easeCamera(@NonNull CameraUpdate update, int durationMs) {
        easeCamera(update, durationMs, (CancelableCallback) null);
    }

    public final void easeCamera(@NonNull CameraUpdate update, int durationMs, @Nullable CancelableCallback callback) {
        easeCamera(update, durationMs, true, callback);
    }

    public final void easeCamera(@NonNull CameraUpdate update, int durationMs, boolean easingInterpolator) {
        easeCamera(update, durationMs, easingInterpolator, null);
    }

    public final void easeCamera(@NonNull CameraUpdate update, int durationMs, boolean easingInterpolator, @Nullable CancelableCallback callback) {
        if (durationMs <= 0) {
            throw new IllegalArgumentException("Null duration passed into easeCamera");
        }
        notifyDeveloperAnimationListeners();
        this.transform.easeCamera(this, update, durationMs, easingInterpolator, callback);
    }

    public final void animateCamera(@NonNull CameraUpdate update) {
        animateCamera(update, 300, null);
    }

    public final void animateCamera(@NonNull CameraUpdate update, @Nullable CancelableCallback callback) {
        animateCamera(update, 300, callback);
    }

    public final void animateCamera(@NonNull CameraUpdate update, int durationMs) {
        animateCamera(update, durationMs, null);
    }

    public final void animateCamera(@NonNull CameraUpdate update, int durationMs, @Nullable CancelableCallback callback) {
        if (durationMs <= 0) {
            throw new IllegalArgumentException("Null duration passed into animateCamera");
        }
        notifyDeveloperAnimationListeners();
        this.transform.animateCamera(this, update, durationMs, callback);
    }

    public void scrollBy(float x, float y) {
        scrollBy(x, y, 0);
    }

    public void scrollBy(float x, float y, long duration) {
        notifyDeveloperAnimationListeners();
        this.nativeMapView.moveBy((double) x, (double) y, duration);
    }

    public void resetNorth() {
        notifyDeveloperAnimationListeners();
        this.transform.resetNorth();
    }

    public void setFocalBearing(double bearing, float focalX, float focalY, long duration) {
        notifyDeveloperAnimationListeners();
        this.transform.setBearing(bearing, focalX, focalY, duration);
    }

    public float getHeight() {
        return this.projection.getHeight();
    }

    public float getWidth() {
        return this.projection.getWidth();
    }

    public void setOfflineRegionDefinition(@NonNull OfflineRegionDefinition definition) {
        setOfflineRegionDefinition(definition, null);
    }

    public void setOfflineRegionDefinition(@NonNull OfflineRegionDefinition definition, @Nullable Style.OnStyleLoaded callback) {
        double minZoom = definition.getMinZoom();
        double maxZoom = definition.getMaxZoom();
        moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(definition.getBounds().getCenter()).zoom(minZoom).build()));
        setMinZoomPreference(minZoom);
        setMaxZoomPreference(maxZoom);
        setStyle(new Style.Builder().fromUri(definition.getStyleURL()), callback);
    }

    public boolean isDebugActive() {
        return this.debugActive;
    }

    public void setDebugActive(boolean debugActive2) {
        this.debugActive = debugActive2;
        this.nativeMapView.setDebug(debugActive2);
    }

    public void cycleDebugOptions() {
        this.nativeMapView.cycleDebugOptions();
        this.debugActive = this.nativeMapView.getDebug();
    }

    private void setApiBaseUrl(@NonNull MapboxMapOptions options) {
        String apiBaseUrl = options.getApiBaseUrl();
        if (!TextUtils.isEmpty(apiBaseUrl)) {
            this.nativeMapView.setApiBaseUrl(apiBaseUrl);
        }
    }

    public void setStyle(String style2) {
        setStyle(style2, (Style.OnStyleLoaded) null);
    }

    public void setStyle(String style2, Style.OnStyleLoaded callback) {
        setStyle(new Style.Builder().fromUri(style2), callback);
    }

    public void setStyle(Style.Builder builder) {
        setStyle(builder, (Style.OnStyleLoaded) null);
    }

    public void setStyle(Style.Builder builder, Style.OnStyleLoaded callback) {
        this.styleLoadedCallback = callback;
        this.locationComponent.onStartLoadingMap();
        if (this.style != null) {
            this.style.clear();
        }
        this.style = builder.build(this.nativeMapView);
        if (!TextUtils.isEmpty(builder.getUri())) {
            this.nativeMapView.setStyleUri(builder.getUri());
        } else if (!TextUtils.isEmpty(builder.getJson())) {
            this.nativeMapView.setStyleJson(builder.getJson());
        } else {
            this.nativeMapView.setStyleJson("{\"version\": 8,\"sources\": {},\"layers\": []}");
        }
    }

    /* access modifiers changed from: package-private */
    public void notifyStyleLoaded() {
        if (!this.nativeMapView.isDestroyed()) {
            if (this.style != null) {
                this.style.onDidFinishLoadingStyle();
                this.locationComponent.onFinishLoadingStyle();
                if (this.styleLoadedCallback != null) {
                    this.styleLoadedCallback.onStyleLoaded(this.style);
                }
                for (Style.OnStyleLoaded styleGetter : this.awaitingStyleGetters) {
                    styleGetter.onStyleLoaded(this.style);
                }
            } else {
                MapStrictMode.strictModeViolation("No style to provide.");
            }
            this.styleLoadedCallback = null;
            this.awaitingStyleGetters.clear();
        }
    }

    @Deprecated
    @NonNull
    public Marker addMarker(@NonNull MarkerOptions markerOptions) {
        return this.annotationManager.addMarker(markerOptions, this);
    }

    @Deprecated
    @NonNull
    public Marker addMarker(@NonNull BaseMarkerOptions markerOptions) {
        return this.annotationManager.addMarker(markerOptions, this);
    }

    @Deprecated
    @NonNull
    public List<Marker> addMarkers(@NonNull List<? extends BaseMarkerOptions> markerOptionsList) {
        return this.annotationManager.addMarkers(markerOptionsList, this);
    }

    @Deprecated
    public void updateMarker(@NonNull Marker updatedMarker) {
        this.annotationManager.updateMarker(updatedMarker, this);
    }

    @Deprecated
    @NonNull
    public Polyline addPolyline(@NonNull PolylineOptions polylineOptions) {
        return this.annotationManager.addPolyline(polylineOptions, this);
    }

    @Deprecated
    @NonNull
    public List<Polyline> addPolylines(@NonNull List<PolylineOptions> polylineOptionsList) {
        return this.annotationManager.addPolylines(polylineOptionsList, this);
    }

    @Deprecated
    public void updatePolyline(@NonNull Polyline polyline) {
        this.annotationManager.updatePolyline(polyline);
    }

    @Deprecated
    @NonNull
    public Polygon addPolygon(@NonNull PolygonOptions polygonOptions) {
        return this.annotationManager.addPolygon(polygonOptions, this);
    }

    @Deprecated
    @NonNull
    public List<Polygon> addPolygons(@NonNull List<PolygonOptions> polygonOptionsList) {
        return this.annotationManager.addPolygons(polygonOptionsList, this);
    }

    @Deprecated
    public void updatePolygon(@NonNull Polygon polygon) {
        this.annotationManager.updatePolygon(polygon);
    }

    @Deprecated
    public void removeMarker(@NonNull Marker marker) {
        this.annotationManager.removeAnnotation(marker);
    }

    @Deprecated
    public void removePolyline(@NonNull Polyline polyline) {
        this.annotationManager.removeAnnotation(polyline);
    }

    @Deprecated
    public void removePolygon(@NonNull Polygon polygon) {
        this.annotationManager.removeAnnotation(polygon);
    }

    @Deprecated
    public void removeAnnotation(@NonNull Annotation annotation) {
        this.annotationManager.removeAnnotation(annotation);
    }

    @Deprecated
    public void removeAnnotation(long id) {
        this.annotationManager.removeAnnotation(id);
    }

    @Deprecated
    public void removeAnnotations(@NonNull List<? extends Annotation> annotationList) {
        this.annotationManager.removeAnnotations(annotationList);
    }

    @Deprecated
    public void removeAnnotations() {
        this.annotationManager.removeAnnotations();
    }

    @Deprecated
    public void clear() {
        this.annotationManager.removeAnnotations();
    }

    @Nullable
    @Deprecated
    public Annotation getAnnotation(long id) {
        return this.annotationManager.getAnnotation(id);
    }

    @Deprecated
    @NonNull
    public List<Annotation> getAnnotations() {
        return this.annotationManager.getAnnotations();
    }

    @Deprecated
    @NonNull
    public List<Marker> getMarkers() {
        return this.annotationManager.getMarkers();
    }

    @Deprecated
    @NonNull
    public List<Polygon> getPolygons() {
        return this.annotationManager.getPolygons();
    }

    @Deprecated
    @NonNull
    public List<Polyline> getPolylines() {
        return this.annotationManager.getPolylines();
    }

    @Deprecated
    public void setOnMarkerClickListener(@Nullable OnMarkerClickListener listener) {
        this.annotationManager.setOnMarkerClickListener(listener);
    }

    @Deprecated
    public void setOnPolygonClickListener(@Nullable OnPolygonClickListener listener) {
        this.annotationManager.setOnPolygonClickListener(listener);
    }

    @Deprecated
    public void setOnPolylineClickListener(@Nullable OnPolylineClickListener listener) {
        this.annotationManager.setOnPolylineClickListener(listener);
    }

    @Deprecated
    public void selectMarker(@NonNull Marker marker) {
        if (marker == null) {
            Logger.w(TAG, "marker was null, so just returning");
        } else {
            this.annotationManager.selectMarker(marker);
        }
    }

    @Deprecated
    public void deselectMarkers() {
        this.annotationManager.deselectMarkers();
    }

    @Deprecated
    public void deselectMarker(@NonNull Marker marker) {
        this.annotationManager.deselectMarker(marker);
    }

    @Deprecated
    @NonNull
    public List<Marker> getSelectedMarkers() {
        return this.annotationManager.getSelectedMarkers();
    }

    @Deprecated
    public void setInfoWindowAdapter(@Nullable InfoWindowAdapter infoWindowAdapter) {
        this.annotationManager.getInfoWindowManager().setInfoWindowAdapter(infoWindowAdapter);
    }

    @Nullable
    @Deprecated
    public InfoWindowAdapter getInfoWindowAdapter() {
        return this.annotationManager.getInfoWindowManager().getInfoWindowAdapter();
    }

    @Deprecated
    public void setAllowConcurrentMultipleOpenInfoWindows(boolean allow) {
        this.annotationManager.getInfoWindowManager().setAllowConcurrentMultipleOpenInfoWindows(allow);
    }

    @Deprecated
    public boolean isAllowConcurrentMultipleOpenInfoWindows() {
        return this.annotationManager.getInfoWindowManager().isAllowConcurrentMultipleOpenInfoWindows();
    }

    public void setLatLngBoundsForCameraTarget(@Nullable LatLngBounds latLngBounds) {
        this.nativeMapView.setLatLngBounds(latLngBounds);
    }

    @Nullable
    public CameraPosition getCameraForLatLngBounds(@NonNull LatLngBounds latLngBounds) {
        return getCameraForLatLngBounds(latLngBounds, new int[]{0, 0, 0, 0});
    }

    @Nullable
    public CameraPosition getCameraForLatLngBounds(@NonNull LatLngBounds latLngBounds, @Size(4) @NonNull int[] padding) {
        return getCameraForLatLngBounds(latLngBounds, padding, this.transform.getRawBearing(), this.transform.getTilt());
    }

    @Nullable
    public CameraPosition getCameraForLatLngBounds(@NonNull LatLngBounds latLngBounds, @FloatRange(from = 0.0d, to = 360.0d) double bearing, @FloatRange(from = 0.0d, to = 60.0d) double tilt) {
        return getCameraForLatLngBounds(latLngBounds, new int[]{0, 0, 0, 0}, bearing, tilt);
    }

    @Nullable
    public CameraPosition getCameraForLatLngBounds(@NonNull LatLngBounds latLngBounds, @Size(4) @NonNull int[] padding, @FloatRange(from = 0.0d, to = 360.0d) double bearing, @FloatRange(from = 0.0d, to = 60.0d) double tilt) {
        return this.nativeMapView.getCameraForLatLngBounds(latLngBounds, padding, bearing, tilt);
    }

    @Nullable
    public CameraPosition getCameraForGeometry(@NonNull Geometry geometry) {
        return getCameraForGeometry(geometry, new int[]{0, 0, 0, 0});
    }

    @Nullable
    public CameraPosition getCameraForGeometry(@NonNull Geometry geometry, @Size(4) @NonNull int[] padding) {
        return getCameraForGeometry(geometry, padding, this.transform.getBearing(), this.transform.getTilt());
    }

    @Nullable
    public CameraPosition getCameraForGeometry(@NonNull Geometry geometry, @FloatRange(from = 0.0d, to = 360.0d) double bearing, @FloatRange(from = 0.0d, to = 60.0d) double tilt) {
        return getCameraForGeometry(geometry, new int[]{0, 0, 0, 0}, bearing, tilt);
    }

    @Nullable
    public CameraPosition getCameraForGeometry(@NonNull Geometry geometry, @Size(4) @NonNull int[] padding, @FloatRange(from = 0.0d, to = 360.0d) double bearing, @FloatRange(from = 0.0d, to = 60.0d) double tilt) {
        return this.nativeMapView.getCameraForGeometry(geometry, padding, bearing, tilt);
    }

    @Deprecated
    public void setPadding(int left, int top, int right, int bottom) {
        this.projection.setContentPadding(new int[]{left, top, right, bottom});
        this.uiSettings.invalidate();
    }

    @Deprecated
    @NonNull
    public int[] getPadding() {
        return this.projection.getContentPadding();
    }

    public void addOnCameraIdleListener(@NonNull OnCameraIdleListener listener) {
        this.cameraChangeDispatcher.addOnCameraIdleListener(listener);
    }

    public void removeOnCameraIdleListener(@NonNull OnCameraIdleListener listener) {
        this.cameraChangeDispatcher.removeOnCameraIdleListener(listener);
    }

    public void addOnCameraMoveCancelListener(@NonNull OnCameraMoveCanceledListener listener) {
        this.cameraChangeDispatcher.addOnCameraMoveCancelListener(listener);
    }

    public void removeOnCameraMoveCancelListener(@NonNull OnCameraMoveCanceledListener listener) {
        this.cameraChangeDispatcher.removeOnCameraMoveCancelListener(listener);
    }

    public void addOnCameraMoveStartedListener(@NonNull OnCameraMoveStartedListener listener) {
        this.cameraChangeDispatcher.addOnCameraMoveStartedListener(listener);
    }

    public void removeOnCameraMoveStartedListener(@NonNull OnCameraMoveStartedListener listener) {
        this.cameraChangeDispatcher.removeOnCameraMoveStartedListener(listener);
    }

    public void addOnCameraMoveListener(@NonNull OnCameraMoveListener listener) {
        this.cameraChangeDispatcher.addOnCameraMoveListener(listener);
    }

    public void removeOnCameraMoveListener(@NonNull OnCameraMoveListener listener) {
        this.cameraChangeDispatcher.removeOnCameraMoveListener(listener);
    }

    public void setOnFpsChangedListener(@Nullable OnFpsChangedListener listener) {
        this.onFpsChangedListener = listener;
        this.nativeMapView.setOnFpsChangedListener(listener);
    }

    /* access modifiers changed from: package-private */
    @Nullable
    public OnFpsChangedListener getOnFpsChangedListener() {
        return this.onFpsChangedListener;
    }

    public void addOnFlingListener(@NonNull OnFlingListener listener) {
        this.onGesturesManagerInteractionListener.onAddFlingListener(listener);
    }

    public void removeOnFlingListener(@NonNull OnFlingListener listener) {
        this.onGesturesManagerInteractionListener.onRemoveFlingListener(listener);
    }

    public void addOnMoveListener(@NonNull OnMoveListener listener) {
        this.onGesturesManagerInteractionListener.onAddMoveListener(listener);
    }

    public void removeOnMoveListener(@NonNull OnMoveListener listener) {
        this.onGesturesManagerInteractionListener.onRemoveMoveListener(listener);
    }

    public void addOnRotateListener(@NonNull OnRotateListener listener) {
        this.onGesturesManagerInteractionListener.onAddRotateListener(listener);
    }

    public void removeOnRotateListener(@NonNull OnRotateListener listener) {
        this.onGesturesManagerInteractionListener.onRemoveRotateListener(listener);
    }

    public void addOnScaleListener(@NonNull OnScaleListener listener) {
        this.onGesturesManagerInteractionListener.onAddScaleListener(listener);
    }

    public void removeOnScaleListener(@NonNull OnScaleListener listener) {
        this.onGesturesManagerInteractionListener.onRemoveScaleListener(listener);
    }

    public void addOnShoveListener(@NonNull OnShoveListener listener) {
        this.onGesturesManagerInteractionListener.onAddShoveListener(listener);
    }

    public void removeOnShoveListener(@NonNull OnShoveListener listener) {
        this.onGesturesManagerInteractionListener.onRemoveShoveListener(listener);
    }

    public void setGesturesManager(@NonNull AndroidGesturesManager androidGesturesManager, boolean attachDefaultListeners, boolean setDefaultMutuallyExclusives) {
        this.onGesturesManagerInteractionListener.setGesturesManager(androidGesturesManager, attachDefaultListeners, setDefaultMutuallyExclusives);
    }

    @NonNull
    public AndroidGesturesManager getGesturesManager() {
        return this.onGesturesManagerInteractionListener.getGesturesManager();
    }

    public void cancelAllVelocityAnimations() {
        this.onGesturesManagerInteractionListener.cancelAllVelocityAnimations();
    }

    public void addOnMapClickListener(@NonNull OnMapClickListener listener) {
        this.onGesturesManagerInteractionListener.onAddMapClickListener(listener);
    }

    public void removeOnMapClickListener(@NonNull OnMapClickListener listener) {
        this.onGesturesManagerInteractionListener.onRemoveMapClickListener(listener);
    }

    public void addOnMapLongClickListener(@NonNull OnMapLongClickListener listener) {
        this.onGesturesManagerInteractionListener.onAddMapLongClickListener(listener);
    }

    public void removeOnMapLongClickListener(@NonNull OnMapLongClickListener listener) {
        this.onGesturesManagerInteractionListener.onRemoveMapLongClickListener(listener);
    }

    public void setOnInfoWindowClickListener(@Nullable OnInfoWindowClickListener listener) {
        this.annotationManager.getInfoWindowManager().setOnInfoWindowClickListener(listener);
    }

    @Nullable
    public OnInfoWindowClickListener getOnInfoWindowClickListener() {
        return this.annotationManager.getInfoWindowManager().getOnInfoWindowClickListener();
    }

    public void setOnInfoWindowLongClickListener(@Nullable OnInfoWindowLongClickListener listener) {
        this.annotationManager.getInfoWindowManager().setOnInfoWindowLongClickListener(listener);
    }

    @Nullable
    public OnInfoWindowLongClickListener getOnInfoWindowLongClickListener() {
        return this.annotationManager.getInfoWindowManager().getOnInfoWindowLongClickListener();
    }

    public void setOnInfoWindowCloseListener(@Nullable OnInfoWindowCloseListener listener) {
        this.annotationManager.getInfoWindowManager().setOnInfoWindowCloseListener(listener);
    }

    @Nullable
    public OnInfoWindowCloseListener getOnInfoWindowCloseListener() {
        return this.annotationManager.getInfoWindowManager().getOnInfoWindowCloseListener();
    }

    public void snapshot(@NonNull SnapshotReadyCallback callback) {
        this.nativeMapView.addSnapshotCallback(callback);
    }

    @NonNull
    public List<Feature> queryRenderedFeatures(@NonNull PointF coordinates, @Nullable String... layerIds) {
        return this.nativeMapView.queryRenderedFeatures(coordinates, layerIds, (Expression) null);
    }

    @NonNull
    public List<Feature> queryRenderedFeatures(@NonNull PointF coordinates, @Nullable Expression filter, @Nullable String... layerIds) {
        return this.nativeMapView.queryRenderedFeatures(coordinates, layerIds, filter);
    }

    @NonNull
    public List<Feature> queryRenderedFeatures(@NonNull RectF coordinates, @Nullable String... layerIds) {
        return this.nativeMapView.queryRenderedFeatures(coordinates, layerIds, (Expression) null);
    }

    @NonNull
    public List<Feature> queryRenderedFeatures(@NonNull RectF coordinates, @Nullable Expression filter, @Nullable String... layerIds) {
        return this.nativeMapView.queryRenderedFeatures(coordinates, layerIds, filter);
    }

    /* access modifiers changed from: package-private */
    public void injectLocationComponent(LocationComponent locationComponent2) {
        this.locationComponent = locationComponent2;
    }

    /* access modifiers changed from: package-private */
    public void injectAnnotationManager(AnnotationManager annotationManager2) {
        this.annotationManager = annotationManager2.bind(this);
    }

    @NonNull
    public LocationComponent getLocationComponent() {
        return this.locationComponent;
    }

    /* access modifiers changed from: package-private */
    @NonNull
    public Transform getTransform() {
        return this.transform;
    }

    private void notifyDeveloperAnimationListeners() {
        for (OnDeveloperAnimationListener listener : this.developerAnimationStartedListeners) {
            listener.onDeveloperAnimationStarted();
        }
    }
}
