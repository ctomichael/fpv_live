package com.mapbox.mapboxsdk.maps;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Handler;
import android.support.annotation.IntRange;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Geometry;
import com.mapbox.mapboxsdk.LibraryLoader;
import com.mapbox.mapboxsdk.MapStrictMode;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.Polygon;
import com.mapbox.mapboxsdk.annotations.Polyline;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.exceptions.CalledFromWorkerThreadException;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.geometry.ProjectedMeters;
import com.mapbox.mapboxsdk.log.Logger;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.renderer.MapRenderer;
import com.mapbox.mapboxsdk.storage.FileSource;
import com.mapbox.mapboxsdk.style.expressions.Expression;
import com.mapbox.mapboxsdk.style.layers.CannotAddLayerException;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.TransitionOptions;
import com.mapbox.mapboxsdk.style.light.Light;
import com.mapbox.mapboxsdk.style.sources.CannotAddSourceException;
import com.mapbox.mapboxsdk.style.sources.Source;
import com.mapbox.mapboxsdk.utils.BitmapUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

final class NativeMapView implements NativeMap {
    private static final String TAG = "Mbgl-NativeMapView";
    private boolean destroyed;
    private double[] edgeInsets;
    private final FileSource fileSource;
    /* access modifiers changed from: private */
    public final MapRenderer mapRenderer;
    @Keep
    private long nativePtr;
    private final float pixelRatio;
    private MapboxMap.SnapshotReadyCallback snapshotReadyCallback;
    @Nullable
    private StateCallback stateCallback;
    @NonNull
    private final Thread thread;
    @Nullable
    private ViewCallback viewCallback;

    interface StateCallback extends StyleCallback {
        void onCameraDidChange(boolean z);

        void onCameraIsChanging();

        void onCameraWillChange(boolean z);

        boolean onCanRemoveUnusedStyleImage(String str);

        void onDidBecomeIdle();

        void onDidFailLoadingMap(String str);

        void onDidFinishLoadingMap();

        void onDidFinishRenderingFrame(boolean z);

        void onDidFinishRenderingMap(boolean z);

        void onSourceChanged(String str);

        void onStyleImageMissing(String str);

        void onWillStartRenderingFrame();

        void onWillStartRenderingMap();
    }

    interface StyleCallback {
        void onDidFinishLoadingStyle();

        void onWillStartLoadingMap();
    }

    public interface ViewCallback {
        @Nullable
        Bitmap getViewContent();
    }

    @Keep
    private native void nativeAddAnnotationIcon(String str, int i, int i2, float f, byte[] bArr);

    @Keep
    private native void nativeAddImage(String str, Bitmap bitmap, float f, boolean z);

    @Keep
    private native void nativeAddImages(Image[] imageArr);

    @Keep
    private native void nativeAddLayer(long j, String str) throws CannotAddLayerException;

    @Keep
    private native void nativeAddLayerAbove(long j, String str) throws CannotAddLayerException;

    @Keep
    private native void nativeAddLayerAt(long j, int i) throws CannotAddLayerException;

    @Keep
    @NonNull
    private native long[] nativeAddMarkers(Marker[] markerArr);

    @Keep
    @NonNull
    private native long[] nativeAddPolygons(Polygon[] polygonArr);

    @Keep
    @NonNull
    private native long[] nativeAddPolylines(Polyline[] polylineArr);

    @Keep
    private native void nativeAddSource(Source source, long j) throws CannotAddSourceException;

    @Keep
    private native void nativeCancelTransitions();

    @Keep
    private native void nativeCycleDebugOptions();

    @Keep
    private native void nativeDestroy();

    @Keep
    private native void nativeEaseTo(double d, double d2, double d3, long j, double d4, double d5, double[] dArr, boolean z);

    @Keep
    private native void nativeFlyTo(double d, double d2, double d3, long j, double d4, double d5, double[] dArr);

    @Keep
    private native double nativeGetBearing();

    @Keep
    @NonNull
    private native CameraPosition nativeGetCameraForGeometry(Geometry geometry, double d, double d2, double d3, double d4, double d5, double d6);

    @Keep
    @NonNull
    private native CameraPosition nativeGetCameraForLatLngBounds(LatLngBounds latLngBounds, double d, double d2, double d3, double d4, double d5, double d6);

    @Keep
    @NonNull
    private native CameraPosition nativeGetCameraPosition();

    @Keep
    private native boolean nativeGetDebug();

    @Keep
    @NonNull
    private native Bitmap nativeGetImage(String str);

    @Keep
    @NonNull
    private native LatLng nativeGetLatLng();

    @Keep
    @NonNull
    private native Layer nativeGetLayer(String str);

    @Keep
    @NonNull
    private native Layer[] nativeGetLayers();

    @Keep
    @NonNull
    private native Light nativeGetLight();

    @Keep
    private native double nativeGetMaxZoom();

    @Keep
    private native double nativeGetMetersPerPixelAtLatitude(double d, double d2);

    @Keep
    private native double nativeGetMinZoom();

    @Keep
    private native double nativeGetPitch();

    @Keep
    private native boolean nativeGetPrefetchTiles();

    @Keep
    @NonNull
    private native Source nativeGetSource(String str);

    @Keep
    @NonNull
    private native Source[] nativeGetSources();

    @Keep
    @NonNull
    private native String nativeGetStyleJson();

    @Keep
    @NonNull
    private native String nativeGetStyleUrl();

    @Keep
    private native double nativeGetTopOffsetPixelsForAnnotationSymbol(String str);

    @Keep
    private native long nativeGetTransitionDelay();

    @Keep
    private native long nativeGetTransitionDuration();

    @Keep
    @NonNull
    private native TransitionOptions nativeGetTransitionOptions();

    @Keep
    private native double nativeGetZoom();

    @Keep
    private native void nativeInitialize(NativeMapView nativeMapView, FileSource fileSource2, MapRenderer mapRenderer2, float f, boolean z);

    @Keep
    private native boolean nativeIsFullyLoaded();

    @Keep
    private native void nativeJumpTo(double d, double d2, double d3, double d4, double d5, double[] dArr);

    @Keep
    @NonNull
    private native LatLng nativeLatLngForPixel(float f, float f2);

    @Keep
    @NonNull
    private native LatLng nativeLatLngForProjectedMeters(double d, double d2);

    @Keep
    private native void nativeMoveBy(double d, double d2, long j);

    @Keep
    private native void nativeOnLowMemory();

    @Keep
    @NonNull
    private native PointF nativePixelForLatLng(double d, double d2);

    @Keep
    @NonNull
    private native ProjectedMeters nativeProjectedMetersForLatLng(double d, double d2);

    @Keep
    @NonNull
    private native long[] nativeQueryPointAnnotations(RectF rectF);

    @Keep
    @NonNull
    private native Feature[] nativeQueryRenderedFeaturesForBox(float f, float f2, float f3, float f4, String[] strArr, Object[] objArr);

    @Keep
    @NonNull
    private native Feature[] nativeQueryRenderedFeaturesForPoint(float f, float f2, String[] strArr, Object[] objArr);

    @Keep
    @NonNull
    private native long[] nativeQueryShapeAnnotations(RectF rectF);

    @Keep
    private native void nativeRemoveAnnotationIcon(String str);

    @Keep
    private native void nativeRemoveAnnotations(long[] jArr);

    @Keep
    private native void nativeRemoveImage(String str);

    @Keep
    private native boolean nativeRemoveLayer(long j);

    @Keep
    private native boolean nativeRemoveLayerAt(int i);

    @Keep
    private native boolean nativeRemoveSource(Source source, long j);

    @Keep
    private native void nativeResetNorth();

    @Keep
    private native void nativeResetPosition();

    @Keep
    private native void nativeResetZoom();

    @Keep
    private native void nativeResizeView(int i, int i2);

    @Keep
    private native void nativeRotateBy(double d, double d2, double d3, double d4, long j);

    @Keep
    private native void nativeSetBearing(double d, long j);

    @Keep
    private native void nativeSetBearingXY(double d, double d2, double d3, long j);

    @Keep
    private native void nativeSetDebug(boolean z);

    @Keep
    private native void nativeSetGestureInProgress(boolean z);

    @Keep
    private native void nativeSetLatLng(double d, double d2, double[] dArr, long j);

    @Keep
    private native void nativeSetLatLngBounds(LatLngBounds latLngBounds);

    @Keep
    private native void nativeSetMaxZoom(double d);

    @Keep
    private native void nativeSetMinZoom(double d);

    @Keep
    private native void nativeSetPitch(double d, long j);

    @Keep
    private native void nativeSetPrefetchTiles(boolean z);

    @Keep
    private native void nativeSetReachability(boolean z);

    @Keep
    private native void nativeSetStyleJson(String str);

    @Keep
    private native void nativeSetStyleUrl(String str);

    @Keep
    private native void nativeSetTransitionDelay(long j);

    @Keep
    private native void nativeSetTransitionDuration(long j);

    @Keep
    private native void nativeSetTransitionOptions(TransitionOptions transitionOptions);

    @Keep
    private native void nativeSetVisibleCoordinateBounds(LatLng[] latLngArr, RectF rectF, double d, long j);

    @Keep
    private native void nativeSetZoom(double d, double d2, double d3, long j);

    @Keep
    private native void nativeTakeSnapshot();

    @Keep
    private native void nativeUpdateMarker(long j, double d, double d2, String str);

    @Keep
    private native void nativeUpdatePolygon(long j, Polygon polygon);

    @Keep
    private native void nativeUpdatePolyline(long j, Polyline polyline);

    static {
        LibraryLoader.load();
    }

    public NativeMapView(@NonNull Context context, boolean crossSourceCollisions, ViewCallback viewCallback2, StateCallback stateCallback2, MapRenderer mapRenderer2) {
        this(context, context.getResources().getDisplayMetrics().density, crossSourceCollisions, viewCallback2, stateCallback2, mapRenderer2);
    }

    public NativeMapView(Context context, float pixelRatio2, boolean crossSourceCollisions, ViewCallback viewCallback2, StateCallback stateCallback2, MapRenderer mapRenderer2) {
        this.destroyed = false;
        this.nativePtr = 0;
        this.mapRenderer = mapRenderer2;
        this.viewCallback = viewCallback2;
        this.fileSource = FileSource.getInstance(context);
        this.pixelRatio = pixelRatio2;
        this.thread = Thread.currentThread();
        this.stateCallback = stateCallback2;
        nativeInitialize(this, this.fileSource, mapRenderer2, pixelRatio2, crossSourceCollisions);
    }

    private boolean checkState(String callingMethod) {
        if (this.thread != Thread.currentThread()) {
            throw new CalledFromWorkerThreadException(String.format("Map interactions should happen on the UI thread. Method invoked from wrong thread is %s.", callingMethod));
        }
        if (this.destroyed && !TextUtils.isEmpty(callingMethod)) {
            String message = String.format("You're calling `%s` after the `MapView` was destroyed, were you invoking it after `onDestroy()`?", callingMethod);
            Logger.e(TAG, message);
            MapStrictMode.strictModeViolation(message);
        }
        return this.destroyed;
    }

    public void destroy() {
        this.destroyed = true;
        this.viewCallback = null;
        nativeDestroy();
    }

    public void resizeView(int width, int height) {
        if (!checkState("resizeView")) {
            int width2 = (int) Math.ceil((double) (((float) width) / this.pixelRatio));
            int height2 = (int) Math.ceil((double) (((float) height) / this.pixelRatio));
            if (width2 < 0) {
                throw new IllegalArgumentException("width cannot be negative.");
            } else if (height2 < 0) {
                throw new IllegalArgumentException("height cannot be negative.");
            } else {
                if (width2 > 65535) {
                    Logger.e(TAG, String.format("Device returned an out of range width size, capping value at 65535 instead of %s", Integer.valueOf(width2)));
                    width2 = 65535;
                }
                if (height2 > 65535) {
                    Logger.e(TAG, String.format("Device returned an out of range height size, capping value at 65535 instead of %s", Integer.valueOf(height2)));
                    height2 = 65535;
                }
                nativeResizeView(width2, height2);
            }
        }
    }

    public void setStyleUri(String url) {
        if (!checkState("setStyleUri")) {
            nativeSetStyleUrl(url);
        }
    }

    @NonNull
    public String getStyleUri() {
        if (checkState("getStyleUri")) {
            return "";
        }
        return nativeGetStyleUrl();
    }

    public void setStyleJson(String newStyleJson) {
        if (!checkState("setStyleJson")) {
            nativeSetStyleJson(newStyleJson);
        }
    }

    @NonNull
    public String getStyleJson() {
        if (checkState("getStyleJson")) {
            return "";
        }
        return nativeGetStyleJson();
    }

    public void setLatLngBounds(LatLngBounds latLngBounds) {
        if (!checkState("setLatLngBounds")) {
            nativeSetLatLngBounds(latLngBounds);
        }
    }

    public void cancelTransitions() {
        if (!checkState("cancelTransitions")) {
            nativeCancelTransitions();
        }
    }

    public void setGestureInProgress(boolean inProgress) {
        if (!checkState("setGestureInProgress")) {
            nativeSetGestureInProgress(inProgress);
        }
    }

    public void moveBy(double dx, double dy, long duration) {
        if (!checkState("moveBy")) {
            nativeMoveBy(dx / ((double) this.pixelRatio), dy / ((double) this.pixelRatio), duration);
        }
    }

    public void setLatLng(@NonNull LatLng latLng, long duration) {
        if (!checkState("setLatLng")) {
            nativeSetLatLng(latLng.getLatitude(), latLng.getLongitude(), getAnimationPaddingAndClearCachedInsets(null), duration);
        }
    }

    public LatLng getLatLng() {
        if (checkState("")) {
            return new LatLng();
        }
        return nativeGetLatLng();
    }

    public CameraPosition getCameraForLatLngBounds(LatLngBounds bounds, int[] padding, double bearing, double tilt) {
        if (checkState("getCameraForLatLngBounds")) {
            return null;
        }
        return nativeGetCameraForLatLngBounds(bounds, (double) (((float) padding[1]) / this.pixelRatio), (double) (((float) padding[0]) / this.pixelRatio), (double) (((float) padding[3]) / this.pixelRatio), (double) (((float) padding[2]) / this.pixelRatio), bearing, tilt);
    }

    public CameraPosition getCameraForGeometry(Geometry geometry, int[] padding, double bearing, double tilt) {
        if (checkState("getCameraForGeometry")) {
            return null;
        }
        return nativeGetCameraForGeometry(geometry, (double) (((float) padding[1]) / this.pixelRatio), (double) (((float) padding[0]) / this.pixelRatio), (double) (((float) padding[3]) / this.pixelRatio), (double) (((float) padding[2]) / this.pixelRatio), bearing, tilt);
    }

    public void resetPosition() {
        if (!checkState("resetPosition")) {
            nativeResetPosition();
        }
    }

    public double getPitch() {
        if (checkState("getPitch")) {
            return 0.0d;
        }
        return nativeGetPitch();
    }

    public void setPitch(double pitch, long duration) {
        if (!checkState("setPitch")) {
            nativeSetPitch(pitch, duration);
        }
    }

    public void setZoom(double zoom, @NonNull PointF focalPoint, long duration) {
        if (!checkState("setZoom")) {
            nativeSetZoom(zoom, (double) (focalPoint.x / this.pixelRatio), (double) (focalPoint.y / this.pixelRatio), duration);
        }
    }

    public double getZoom() {
        if (checkState("getZoom")) {
            return 0.0d;
        }
        return nativeGetZoom();
    }

    public void resetZoom() {
        if (!checkState("resetZoom")) {
            nativeResetZoom();
        }
    }

    public void setMinZoom(double zoom) {
        if (!checkState("setMinZoom")) {
            nativeSetMinZoom(zoom);
        }
    }

    public double getMinZoom() {
        if (checkState("getMinZoom")) {
            return 0.0d;
        }
        return nativeGetMinZoom();
    }

    public void setMaxZoom(double zoom) {
        if (!checkState("setMaxZoom")) {
            nativeSetMaxZoom(zoom);
        }
    }

    public double getMaxZoom() {
        if (checkState("getMaxZoom")) {
            return 0.0d;
        }
        return nativeGetMaxZoom();
    }

    public void rotateBy(double sx, double sy, double ex, double ey, long duration) {
        if (!checkState("rotateBy")) {
            nativeRotateBy(sx / ((double) this.pixelRatio), sy / ((double) this.pixelRatio), ex, ey, duration);
        }
    }

    public void setContentPadding(double[] padding) {
        if (!checkState("setContentPadding")) {
            this.edgeInsets = padding;
        }
    }

    public double[] getContentPadding() {
        if (checkState("getContentPadding")) {
            return new double[]{0.0d, 0.0d, 0.0d, 0.0d};
        }
        return this.edgeInsets != null ? this.edgeInsets : getCameraPosition().padding;
    }

    public void setBearing(double degrees, long duration) {
        if (!checkState("setBearing")) {
            nativeSetBearing(degrees, duration);
        }
    }

    public void setBearing(double degrees, double fx, double fy, long duration) {
        if (!checkState("setBearing")) {
            nativeSetBearingXY(degrees, fx / ((double) this.pixelRatio), fy / ((double) this.pixelRatio), duration);
        }
    }

    public double getBearing() {
        if (checkState("getBearing")) {
            return 0.0d;
        }
        return nativeGetBearing();
    }

    public void resetNorth() {
        if (!checkState("resetNorth")) {
            nativeResetNorth();
        }
    }

    public long addMarker(Marker marker) {
        if (checkState("addMarker")) {
            return 0;
        }
        return nativeAddMarkers(new Marker[]{marker})[0];
    }

    @NonNull
    public long[] addMarkers(@NonNull List<Marker> markers) {
        if (checkState("addMarkers")) {
            return new long[0];
        }
        return nativeAddMarkers((Marker[]) markers.toArray(new Marker[markers.size()]));
    }

    public long addPolyline(Polyline polyline) {
        if (checkState("addPolyline")) {
            return 0;
        }
        return nativeAddPolylines(new Polyline[]{polyline})[0];
    }

    @NonNull
    public long[] addPolylines(@NonNull List<Polyline> polylines) {
        if (checkState("addPolylines")) {
            return new long[0];
        }
        return nativeAddPolylines((Polyline[]) polylines.toArray(new Polyline[polylines.size()]));
    }

    public long addPolygon(Polygon polygon) {
        if (checkState("addPolygon")) {
            return 0;
        }
        return nativeAddPolygons(new Polygon[]{polygon})[0];
    }

    @NonNull
    public long[] addPolygons(@NonNull List<Polygon> polygons) {
        if (checkState("addPolygons")) {
            return new long[0];
        }
        return nativeAddPolygons((Polygon[]) polygons.toArray(new Polygon[polygons.size()]));
    }

    public void updateMarker(@NonNull Marker marker) {
        if (!checkState("updateMarker")) {
            LatLng position = marker.getPosition();
            nativeUpdateMarker(marker.getId(), position.getLatitude(), position.getLongitude(), marker.getIcon().getId());
        }
    }

    public void updatePolygon(@NonNull Polygon polygon) {
        if (!checkState("updatePolygon")) {
            nativeUpdatePolygon(polygon.getId(), polygon);
        }
    }

    public void updatePolyline(@NonNull Polyline polyline) {
        if (!checkState("updatePolyline")) {
            nativeUpdatePolyline(polyline.getId(), polyline);
        }
    }

    public void removeAnnotation(long id) {
        if (!checkState("removeAnnotation")) {
            removeAnnotations(new long[]{id});
        }
    }

    public void removeAnnotations(long[] ids) {
        if (!checkState("removeAnnotations")) {
            nativeRemoveAnnotations(ids);
        }
    }

    @NonNull
    public long[] queryPointAnnotations(RectF rect) {
        if (checkState("queryPointAnnotations")) {
            return new long[0];
        }
        return nativeQueryPointAnnotations(rect);
    }

    @NonNull
    public long[] queryShapeAnnotations(RectF rectF) {
        if (checkState("queryShapeAnnotations")) {
            return new long[0];
        }
        return nativeQueryShapeAnnotations(rectF);
    }

    public void addAnnotationIcon(String symbol, int width, int height, float scale, byte[] pixels) {
        if (!checkState("addAnnotationIcon")) {
            nativeAddAnnotationIcon(symbol, width, height, scale, pixels);
        }
    }

    public void removeAnnotationIcon(String symbol) {
        if (!checkState("removeAnnotationIcon")) {
            nativeRemoveAnnotationIcon(symbol);
        }
    }

    public void setVisibleCoordinateBounds(LatLng[] coordinates, RectF padding, double direction, long duration) {
        if (!checkState("setVisibleCoordinateBounds")) {
            nativeSetVisibleCoordinateBounds(coordinates, padding, direction, duration);
        }
    }

    public void onLowMemory() {
        if (!checkState("onLowMemory")) {
            nativeOnLowMemory();
        }
    }

    public void setDebug(boolean debug) {
        if (!checkState("setDebug")) {
            nativeSetDebug(debug);
        }
    }

    public void cycleDebugOptions() {
        if (!checkState("cycleDebugOptions")) {
            nativeCycleDebugOptions();
        }
    }

    public boolean getDebug() {
        if (checkState("getDebug")) {
            return false;
        }
        return nativeGetDebug();
    }

    public boolean isFullyLoaded() {
        if (checkState("isFullyLoaded")) {
            return false;
        }
        return nativeIsFullyLoaded();
    }

    public void setReachability(boolean status) {
        if (!checkState("setReachability")) {
            nativeSetReachability(status);
        }
    }

    public double getMetersPerPixelAtLatitude(double lat) {
        if (checkState("getMetersPerPixelAtLatitude")) {
            return 0.0d;
        }
        return nativeGetMetersPerPixelAtLatitude(lat, getZoom()) / ((double) this.pixelRatio);
    }

    public ProjectedMeters projectedMetersForLatLng(@NonNull LatLng latLng) {
        if (checkState("projectedMetersForLatLng")) {
            return null;
        }
        return nativeProjectedMetersForLatLng(latLng.getLatitude(), latLng.getLongitude());
    }

    public LatLng latLngForProjectedMeters(@NonNull ProjectedMeters projectedMeters) {
        if (checkState("latLngForProjectedMeters")) {
            return new LatLng();
        }
        return nativeLatLngForProjectedMeters(projectedMeters.getNorthing(), projectedMeters.getEasting());
    }

    @NonNull
    public PointF pixelForLatLng(@NonNull LatLng latLng) {
        if (checkState("pixelForLatLng")) {
            return new PointF();
        }
        PointF pointF = nativePixelForLatLng(latLng.getLatitude(), latLng.getLongitude());
        pointF.set(pointF.x * this.pixelRatio, pointF.y * this.pixelRatio);
        return pointF;
    }

    public LatLng latLngForPixel(@NonNull PointF pixel) {
        if (checkState("latLngForPixel")) {
            return new LatLng();
        }
        return nativeLatLngForPixel(pixel.x / this.pixelRatio, pixel.y / this.pixelRatio);
    }

    public double getTopOffsetPixelsForAnnotationSymbol(String symbolName) {
        if (checkState("getTopOffsetPixelsForAnnotationSymbol")) {
            return 0.0d;
        }
        return nativeGetTopOffsetPixelsForAnnotationSymbol(symbolName);
    }

    public void jumpTo(@NonNull LatLng center, double zoom, double pitch, double angle, double[] padding) {
        if (!checkState("jumpTo")) {
            nativeJumpTo(angle, center.getLatitude(), center.getLongitude(), pitch, zoom, getAnimationPaddingAndClearCachedInsets(padding));
        }
    }

    public void easeTo(@NonNull LatLng center, double zoom, double angle, double pitch, double[] padding, long duration, boolean easingInterpolator) {
        if (!checkState("easeTo")) {
            nativeEaseTo(angle, center.getLatitude(), center.getLongitude(), duration, pitch, zoom, getAnimationPaddingAndClearCachedInsets(padding), easingInterpolator);
        }
    }

    public void flyTo(@NonNull LatLng center, double zoom, double angle, double pitch, double[] padding, long duration) {
        if (!checkState("flyTo")) {
            nativeFlyTo(angle, center.getLatitude(), center.getLongitude(), duration, pitch, zoom, getAnimationPaddingAndClearCachedInsets(padding));
        }
    }

    @NonNull
    public CameraPosition getCameraPosition() {
        if (checkState("getCameraValues")) {
            return new CameraPosition.Builder().build();
        }
        if (this.edgeInsets != null) {
            return new CameraPosition.Builder(nativeGetCameraPosition()).padding(this.edgeInsets).build();
        }
        return nativeGetCameraPosition();
    }

    public void setPrefetchTiles(boolean enable) {
        if (!checkState("setPrefetchTiles")) {
            nativeSetPrefetchTiles(enable);
        }
    }

    public boolean getPrefetchTiles() {
        if (checkState("getPrefetchTiles")) {
            return false;
        }
        return nativeGetPrefetchTiles();
    }

    public void setTransitionOptions(@NonNull TransitionOptions transitionOptions) {
        nativeSetTransitionOptions(transitionOptions);
    }

    @NonNull
    public TransitionOptions getTransitionOptions() {
        return nativeGetTransitionOptions();
    }

    @NonNull
    public List<Layer> getLayers() {
        if (checkState("getLayers")) {
            return new ArrayList();
        }
        return Arrays.asList(nativeGetLayers());
    }

    public Layer getLayer(String layerId) {
        if (checkState("getLayer")) {
            return null;
        }
        return nativeGetLayer(layerId);
    }

    public void addLayer(@NonNull Layer layer) {
        if (!checkState("addLayer")) {
            nativeAddLayer(layer.getNativePtr(), null);
        }
    }

    public void addLayerBelow(@NonNull Layer layer, @NonNull String below) {
        if (!checkState("addLayerBelow")) {
            nativeAddLayer(layer.getNativePtr(), below);
        }
    }

    public void addLayerAbove(@NonNull Layer layer, @NonNull String above) {
        if (!checkState("addLayerAbove")) {
            nativeAddLayerAbove(layer.getNativePtr(), above);
        }
    }

    public void addLayerAt(@NonNull Layer layer, @IntRange(from = 0) int index) {
        if (!checkState("addLayerAt")) {
            nativeAddLayerAt(layer.getNativePtr(), index);
        }
    }

    public boolean removeLayer(@NonNull String layerId) {
        Layer layer;
        if (!checkState("removeLayer") && (layer = getLayer(layerId)) != null) {
            return removeLayer(layer);
        }
        return false;
    }

    public boolean removeLayer(@NonNull Layer layer) {
        if (checkState("removeLayer")) {
            return false;
        }
        return nativeRemoveLayer(layer.getNativePtr());
    }

    public boolean removeLayerAt(@IntRange(from = 0) int index) {
        if (checkState("removeLayerAt")) {
            return false;
        }
        return nativeRemoveLayerAt(index);
    }

    @NonNull
    public List<Source> getSources() {
        if (checkState("getSources")) {
            return new ArrayList();
        }
        return Arrays.asList(nativeGetSources());
    }

    public Source getSource(@NonNull String sourceId) {
        if (checkState("getSource")) {
            return null;
        }
        return nativeGetSource(sourceId);
    }

    public void addSource(@NonNull Source source) {
        if (!checkState("addSource")) {
            nativeAddSource(source, source.getNativePtr());
        }
    }

    public boolean removeSource(@NonNull String sourceId) {
        Source source;
        if (!checkState("removeSource") && (source = getSource(sourceId)) != null) {
            return removeSource(source);
        }
        return false;
    }

    public boolean removeSource(@NonNull Source source) {
        if (checkState("removeSource")) {
            return false;
        }
        return nativeRemoveSource(source, source.getNativePtr());
    }

    public void addImages(@NonNull Image[] images) {
        if (!checkState("addImages")) {
            nativeAddImages(images);
        }
    }

    public void removeImage(String name) {
        if (!checkState("removeImage")) {
            nativeRemoveImage(name);
        }
    }

    public Bitmap getImage(String name) {
        if (checkState("getImage")) {
            return null;
        }
        return nativeGetImage(name);
    }

    @NonNull
    public List<Feature> queryRenderedFeatures(@NonNull PointF coordinates, @Nullable String[] layerIds, @Nullable Expression filter) {
        if (checkState("queryRenderedFeatures")) {
            return new ArrayList();
        }
        Feature[] features = nativeQueryRenderedFeaturesForPoint(coordinates.x / this.pixelRatio, coordinates.y / this.pixelRatio, layerIds, filter != null ? filter.toArray() : null);
        return features != null ? Arrays.asList(features) : new ArrayList();
    }

    @NonNull
    public List<Feature> queryRenderedFeatures(@NonNull RectF coordinates, @Nullable String[] layerIds, @Nullable Expression filter) {
        if (checkState("queryRenderedFeatures")) {
            return new ArrayList();
        }
        Feature[] features = nativeQueryRenderedFeaturesForBox(coordinates.left / this.pixelRatio, coordinates.top / this.pixelRatio, coordinates.right / this.pixelRatio, coordinates.bottom / this.pixelRatio, layerIds, filter != null ? filter.toArray() : null);
        return features != null ? Arrays.asList(features) : new ArrayList();
    }

    public void setApiBaseUrl(String baseUrl) {
        if (!checkState("setApiBaseUrl")) {
            this.fileSource.setApiBaseUrl(baseUrl);
        }
    }

    public Light getLight() {
        if (checkState("getLight")) {
            return null;
        }
        return nativeGetLight();
    }

    public float getPixelRatio() {
        return this.pixelRatio;
    }

    @NonNull
    public RectF getDensityDependantRectangle(RectF rectangle) {
        return new RectF(rectangle.left / this.pixelRatio, rectangle.top / this.pixelRatio, rectangle.right / this.pixelRatio, rectangle.bottom / this.pixelRatio);
    }

    @Keep
    private void onCameraWillChange(boolean animated) {
        if (this.stateCallback != null) {
            this.stateCallback.onCameraWillChange(animated);
        }
    }

    @Keep
    private void onCameraIsChanging() {
        if (this.stateCallback != null) {
            this.stateCallback.onCameraIsChanging();
        }
    }

    @Keep
    private void onCameraDidChange(boolean animated) {
        if (this.stateCallback != null) {
            this.stateCallback.onCameraDidChange(animated);
        }
    }

    @Keep
    private void onWillStartLoadingMap() {
        if (this.stateCallback != null) {
            this.stateCallback.onWillStartLoadingMap();
        }
    }

    @Keep
    private void onDidFinishLoadingMap() {
        if (this.stateCallback != null) {
            this.stateCallback.onDidFinishLoadingMap();
        }
    }

    @Keep
    private void onDidFailLoadingMap(String error) {
        if (this.stateCallback != null) {
            this.stateCallback.onDidFailLoadingMap(error);
        }
    }

    @Keep
    private void onWillStartRenderingFrame() {
        if (this.stateCallback != null) {
            this.stateCallback.onWillStartRenderingFrame();
        }
    }

    @Keep
    private void onDidFinishRenderingFrame(boolean fully) {
        if (this.stateCallback != null) {
            this.stateCallback.onDidFinishRenderingFrame(fully);
        }
    }

    @Keep
    private void onWillStartRenderingMap() {
        if (this.stateCallback != null) {
            this.stateCallback.onWillStartRenderingMap();
        }
    }

    @Keep
    private void onDidFinishRenderingMap(boolean fully) {
        if (this.stateCallback != null) {
            this.stateCallback.onDidFinishRenderingMap(fully);
        }
    }

    @Keep
    private void onDidBecomeIdle() {
        if (this.stateCallback != null) {
            this.stateCallback.onDidBecomeIdle();
        }
    }

    @Keep
    private void onDidFinishLoadingStyle() {
        if (this.stateCallback != null) {
            this.stateCallback.onDidFinishLoadingStyle();
        }
    }

    @Keep
    private void onSourceChanged(String sourceId) {
        if (this.stateCallback != null) {
            this.stateCallback.onSourceChanged(sourceId);
        }
    }

    @Keep
    private void onStyleImageMissing(String imageId) {
        if (this.stateCallback != null) {
            this.stateCallback.onStyleImageMissing(imageId);
        }
    }

    @Keep
    private boolean onCanRemoveUnusedStyleImage(String imageId) {
        if (this.stateCallback != null) {
            return this.stateCallback.onCanRemoveUnusedStyleImage(imageId);
        }
        return true;
    }

    /* access modifiers changed from: protected */
    @Keep
    public void onSnapshotReady(@Nullable Bitmap mapContent) {
        if (!checkState("OnSnapshotReady")) {
            try {
                if (this.snapshotReadyCallback != null && mapContent != null) {
                    if (this.viewCallback == null) {
                        this.snapshotReadyCallback.onSnapshotReady(mapContent);
                        return;
                    }
                    Bitmap viewContent = this.viewCallback.getViewContent();
                    if (viewContent != null) {
                        this.snapshotReadyCallback.onSnapshotReady(BitmapUtils.mergeBitmap(mapContent, viewContent));
                    }
                }
            } catch (Throwable err) {
                Logger.e(TAG, "Exception in onSnapshotReady", err);
                throw err;
            }
        }
    }

    public long getNativePtr() {
        return this.nativePtr;
    }

    public void addSnapshotCallback(@NonNull MapboxMap.SnapshotReadyCallback callback) {
        if (!checkState("addSnapshotCallback")) {
            this.snapshotReadyCallback = callback;
            nativeTakeSnapshot();
        }
    }

    public void setOnFpsChangedListener(@Nullable final MapboxMap.OnFpsChangedListener listener) {
        final Handler handler = new Handler();
        this.mapRenderer.queueEvent(new Runnable() {
            /* class com.mapbox.mapboxsdk.maps.NativeMapView.AnonymousClass1 */

            public void run() {
                if (listener != null) {
                    NativeMapView.this.mapRenderer.setOnFpsChangedListener(new MapboxMap.OnFpsChangedListener() {
                        /* class com.mapbox.mapboxsdk.maps.NativeMapView.AnonymousClass1.AnonymousClass1 */

                        public void onFpsChanged(final double fps) {
                            handler.post(new Runnable() {
                                /* class com.mapbox.mapboxsdk.maps.NativeMapView.AnonymousClass1.AnonymousClass1.AnonymousClass1 */

                                public void run() {
                                    listener.onFpsChanged(fps);
                                }
                            });
                        }
                    });
                } else {
                    NativeMapView.this.mapRenderer.setOnFpsChangedListener(null);
                }
            }
        });
    }

    public boolean isDestroyed() {
        return this.destroyed;
    }

    private double[] getAnimationPaddingAndClearCachedInsets(double[] providedPadding) {
        if (providedPadding == null) {
            providedPadding = this.edgeInsets;
        }
        this.edgeInsets = null;
        if (providedPadding == null) {
            return null;
        }
        return new double[]{providedPadding[1] / ((double) this.pixelRatio), providedPadding[0] / ((double) this.pixelRatio), providedPadding[3] / ((double) this.pixelRatio), providedPadding[2] / ((double) this.pixelRatio)};
    }
}
