package com.mapbox.mapboxsdk.maps;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Geometry;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.Polygon;
import com.mapbox.mapboxsdk.annotations.Polyline;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.geometry.ProjectedMeters;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.style.expressions.Expression;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.TransitionOptions;
import com.mapbox.mapboxsdk.style.light.Light;
import com.mapbox.mapboxsdk.style.sources.Source;
import java.util.List;

interface NativeMap {
    void addAnnotationIcon(String str, int i, int i2, float f, byte[] bArr);

    void addImages(Image[] imageArr);

    void addLayer(@NonNull Layer layer);

    void addLayerAbove(@NonNull Layer layer, @NonNull String str);

    void addLayerAt(@NonNull Layer layer, @IntRange(from = 0) int i);

    void addLayerBelow(@NonNull Layer layer, @NonNull String str);

    long addMarker(Marker marker);

    @NonNull
    long[] addMarkers(@NonNull List<Marker> list);

    long addPolygon(Polygon polygon);

    @NonNull
    long[] addPolygons(@NonNull List<Polygon> list);

    long addPolyline(Polyline polyline);

    @NonNull
    long[] addPolylines(@NonNull List<Polyline> list);

    void addSnapshotCallback(@NonNull MapboxMap.SnapshotReadyCallback snapshotReadyCallback);

    void addSource(@NonNull Source source);

    void cancelTransitions();

    void cycleDebugOptions();

    void destroy();

    void easeTo(@NonNull LatLng latLng, double d, double d2, double d3, double[] dArr, long j, boolean z);

    void flyTo(@NonNull LatLng latLng, double d, double d2, double d3, double[] dArr, long j);

    double getBearing();

    CameraPosition getCameraForGeometry(@NonNull Geometry geometry, int[] iArr, double d, double d2);

    CameraPosition getCameraForLatLngBounds(@NonNull LatLngBounds latLngBounds, int[] iArr, double d, double d2);

    @NonNull
    CameraPosition getCameraPosition();

    double[] getContentPadding();

    boolean getDebug();

    @NonNull
    RectF getDensityDependantRectangle(RectF rectF);

    Bitmap getImage(String str);

    LatLng getLatLng();

    Layer getLayer(String str);

    @NonNull
    List<Layer> getLayers();

    Light getLight();

    double getMaxZoom();

    double getMetersPerPixelAtLatitude(double d);

    double getMinZoom();

    long getNativePtr();

    double getPitch();

    float getPixelRatio();

    boolean getPrefetchTiles();

    Source getSource(@NonNull String str);

    @NonNull
    List<Source> getSources();

    @NonNull
    String getStyleJson();

    @NonNull
    String getStyleUri();

    double getTopOffsetPixelsForAnnotationSymbol(String str);

    @NonNull
    TransitionOptions getTransitionOptions();

    double getZoom();

    boolean isDestroyed();

    boolean isFullyLoaded();

    void jumpTo(@NonNull LatLng latLng, double d, double d2, double d3, double[] dArr);

    LatLng latLngForPixel(@NonNull PointF pointF);

    LatLng latLngForProjectedMeters(@NonNull ProjectedMeters projectedMeters);

    void moveBy(double d, double d2, long j);

    void onLowMemory();

    @NonNull
    PointF pixelForLatLng(@NonNull LatLng latLng);

    ProjectedMeters projectedMetersForLatLng(@NonNull LatLng latLng);

    @NonNull
    long[] queryPointAnnotations(RectF rectF);

    @NonNull
    List<Feature> queryRenderedFeatures(@NonNull PointF pointF, @Nullable String[] strArr, @Nullable Expression expression);

    @NonNull
    List<Feature> queryRenderedFeatures(@NonNull RectF rectF, @Nullable String[] strArr, @Nullable Expression expression);

    @NonNull
    long[] queryShapeAnnotations(RectF rectF);

    void removeAnnotation(long j);

    void removeAnnotationIcon(String str);

    void removeAnnotations(long[] jArr);

    void removeImage(String str);

    boolean removeLayer(@NonNull Layer layer);

    boolean removeLayer(@NonNull String str);

    boolean removeLayerAt(@IntRange(from = 0) int i);

    boolean removeSource(@NonNull Source source);

    boolean removeSource(@NonNull String str);

    void resetNorth();

    void resetPosition();

    void resetZoom();

    void resizeView(int i, int i2);

    void rotateBy(double d, double d2, double d3, double d4, long j);

    void setApiBaseUrl(String str);

    void setBearing(double d, double d2, double d3, long j);

    void setBearing(double d, long j);

    void setContentPadding(double[] dArr);

    void setDebug(boolean z);

    void setGestureInProgress(boolean z);

    void setLatLng(@NonNull LatLng latLng, long j);

    void setLatLngBounds(@Nullable LatLngBounds latLngBounds);

    void setMaxZoom(double d);

    void setMinZoom(double d);

    void setOnFpsChangedListener(@NonNull MapboxMap.OnFpsChangedListener onFpsChangedListener);

    void setPitch(double d, long j);

    void setPrefetchTiles(boolean z);

    void setReachability(boolean z);

    void setStyleJson(String str);

    void setStyleUri(String str);

    void setTransitionOptions(@NonNull TransitionOptions transitionOptions);

    void setVisibleCoordinateBounds(@NonNull LatLng[] latLngArr, @NonNull RectF rectF, double d, long j);

    void setZoom(double d, @NonNull PointF pointF, long j);

    void updateMarker(@NonNull Marker marker);

    void updatePolygon(@NonNull Polygon polygon);

    void updatePolyline(@NonNull Polyline polyline);
}
