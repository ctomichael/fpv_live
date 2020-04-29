package com.mapbox.mapboxsdk.maps;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v4.util.LongSparseArray;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.mapbox.android.gestures.AndroidGesturesManager;
import com.mapbox.mapboxsdk.MapStrictMode;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.R;
import com.mapbox.mapboxsdk.annotations.Annotation;
import com.mapbox.mapboxsdk.constants.MapboxConstants;
import com.mapbox.mapboxsdk.exceptions.MapboxConfigurationException;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.NativeMapView;
import com.mapbox.mapboxsdk.maps.renderer.MapRenderer;
import com.mapbox.mapboxsdk.maps.renderer.glsurfaceview.GLSurfaceViewMapRenderer;
import com.mapbox.mapboxsdk.maps.renderer.glsurfaceview.MapboxGLSurfaceView;
import com.mapbox.mapboxsdk.maps.renderer.textureview.TextureViewMapRenderer;
import com.mapbox.mapboxsdk.maps.widgets.CompassView;
import com.mapbox.mapboxsdk.net.ConnectivityReceiver;
import com.mapbox.mapboxsdk.storage.FileSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MapView extends FrameLayout implements NativeMapView.ViewCallback {
    private ImageView attrView;
    private AttributionClickListener attributionClickListener;
    /* access modifiers changed from: private */
    public CompassView compassView;
    /* access modifiers changed from: private */
    public boolean destroyed;
    /* access modifiers changed from: private */
    public PointF focalPoint;
    private final InitialRenderCallback initialRenderCallback = new InitialRenderCallback();
    private boolean isStarted;
    private ImageView logoView;
    private final MapCallback mapCallback = new MapCallback();
    private final MapChangeReceiver mapChangeReceiver = new MapChangeReceiver();
    /* access modifiers changed from: private */
    @Nullable
    public MapGestureDetector mapGestureDetector;
    @Nullable
    private MapKeyListener mapKeyListener;
    private MapRenderer mapRenderer;
    /* access modifiers changed from: private */
    @Nullable
    public MapboxMap mapboxMap;
    private MapboxMapOptions mapboxMapOptions;
    @Nullable
    private NativeMap nativeMapView;
    @Nullable
    private Bundle savedInstanceState;

    public interface OnCameraDidChangeListener {
        void onCameraDidChange(boolean z);
    }

    public interface OnCameraIsChangingListener {
        void onCameraIsChanging();
    }

    public interface OnCameraWillChangeListener {
        void onCameraWillChange(boolean z);
    }

    public interface OnCanRemoveUnusedStyleImageListener {
        boolean onCanRemoveUnusedStyleImage(@NonNull String str);
    }

    public interface OnDidBecomeIdleListener {
        void onDidBecomeIdle();
    }

    public interface OnDidFailLoadingMapListener {
        void onDidFailLoadingMap(String str);
    }

    public interface OnDidFinishLoadingMapListener {
        void onDidFinishLoadingMap();
    }

    public interface OnDidFinishLoadingStyleListener {
        void onDidFinishLoadingStyle();
    }

    public interface OnDidFinishRenderingFrameListener {
        void onDidFinishRenderingFrame(boolean z);
    }

    public interface OnDidFinishRenderingMapListener {
        void onDidFinishRenderingMap(boolean z);
    }

    public interface OnSourceChangedListener {
        void onSourceChangedListener(String str);
    }

    public interface OnStyleImageMissingListener {
        void onStyleImageMissing(@NonNull String str);
    }

    public interface OnWillStartLoadingMapListener {
        void onWillStartLoadingMap();
    }

    public interface OnWillStartRenderingFrameListener {
        void onWillStartRenderingFrame();
    }

    public interface OnWillStartRenderingMapListener {
        void onWillStartRenderingMap();
    }

    @UiThread
    public MapView(@NonNull Context context) {
        super(context);
        initialize(context, MapboxMapOptions.createFromAttributes(context));
    }

    @UiThread
    public MapView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(context, MapboxMapOptions.createFromAttributes(context, attrs));
    }

    @UiThread
    public MapView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, MapboxMapOptions.createFromAttributes(context, attrs));
    }

    @UiThread
    public MapView(@NonNull Context context, @Nullable MapboxMapOptions options) {
        super(context);
        initialize(context, options == null ? MapboxMapOptions.createFromAttributes(context) : options);
    }

    /* access modifiers changed from: protected */
    @UiThread
    @CallSuper
    public void initialize(@NonNull Context context, @NonNull MapboxMapOptions options) {
        if (!isInEditMode()) {
            if (!Mapbox.hasInstance()) {
                throw new MapboxConfigurationException();
            }
            setForeground(new ColorDrawable(options.getForegroundLoadColor()));
            this.mapboxMapOptions = options;
            View view = LayoutInflater.from(context).inflate(R.layout.mapbox_mapview_internal, this);
            this.compassView = (CompassView) view.findViewById(R.id.compassView);
            this.attrView = (ImageView) view.findViewById(R.id.attributionView);
            this.attrView.setImageDrawable(BitmapUtils.getDrawableFromRes(getContext(), R.drawable.mapbox_info_bg_selector));
            this.logoView = (ImageView) view.findViewById(R.id.logoView);
            this.logoView.setImageDrawable(BitmapUtils.getDrawableFromRes(getContext(), R.drawable.mapbox_logo_icon));
            setContentDescription(context.getString(R.string.mapbox_mapActionDescription));
            setWillNotDraw(false);
            initialiseDrawingSurface(options);
        }
    }

    /* access modifiers changed from: private */
    public void initialiseMap() {
        Context context = getContext();
        FocalPointInvalidator focalInvalidator = new FocalPointInvalidator();
        focalInvalidator.addListener(createFocalPointChangeListener());
        GesturesManagerInteractionListener gesturesManagerInteractionListener = new GesturesManagerInteractionListener();
        CameraChangeDispatcher cameraDispatcher = new CameraChangeDispatcher();
        Projection proj = new Projection(this.nativeMapView, this);
        UiSettings uiSettings = new UiSettings(proj, focalInvalidator, this.compassView, this.attrView, this.logoView, getPixelRatio());
        LongSparseArray<Annotation> annotationsArray = new LongSparseArray<>();
        IconManager iconManager = new IconManager(this.nativeMapView);
        AnnotationManager annotationManager = new AnnotationManager(this, annotationsArray, iconManager, new AnnotationContainer(this.nativeMapView, annotationsArray), new MarkerContainer(this.nativeMapView, annotationsArray, iconManager), new PolygonContainer(this.nativeMapView, annotationsArray), new PolylineContainer(this.nativeMapView, annotationsArray), new ShapeAnnotationContainer(this.nativeMapView, annotationsArray));
        Transform transform = new Transform(this, this.nativeMapView, cameraDispatcher);
        List<MapboxMap.OnDeveloperAnimationListener> developerAnimationListeners = new ArrayList<>();
        this.mapboxMap = new MapboxMap(this.nativeMapView, transform, uiSettings, proj, gesturesManagerInteractionListener, cameraDispatcher, developerAnimationListeners);
        this.mapboxMap.injectAnnotationManager(annotationManager);
        this.mapGestureDetector = new MapGestureDetector(context, transform, proj, uiSettings, annotationManager, cameraDispatcher);
        this.mapKeyListener = new MapKeyListener(transform, uiSettings, this.mapGestureDetector);
        this.compassView.injectCompassAnimationListener(createCompassAnimationListener(cameraDispatcher));
        this.compassView.setOnClickListener(createCompassClickListener(cameraDispatcher));
        this.mapboxMap.injectLocationComponent(new LocationComponent(this.mapboxMap, transform, developerAnimationListeners));
        ImageView imageView = this.attrView;
        AttributionClickListener attributionClickListener2 = new AttributionClickListener(context, this.mapboxMap);
        this.attributionClickListener = attributionClickListener2;
        imageView.setOnClickListener(attributionClickListener2);
        setClickable(true);
        setLongClickable(true);
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestDisallowInterceptTouchEvent(true);
        this.nativeMapView.setReachability(Mapbox.isConnected().booleanValue());
        if (this.savedInstanceState == null) {
            this.mapboxMap.initialise(context, this.mapboxMapOptions);
        } else {
            this.mapboxMap.onRestoreInstanceState(this.savedInstanceState);
        }
        this.mapCallback.initialised();
    }

    private FocalPointChangeListener createFocalPointChangeListener() {
        return new FocalPointChangeListener() {
            /* class com.mapbox.mapboxsdk.maps.MapView.AnonymousClass1 */

            public void onFocalPointChanged(PointF pointF) {
                PointF unused = MapView.this.focalPoint = pointF;
            }
        };
    }

    private MapboxMap.OnCompassAnimationListener createCompassAnimationListener(@NonNull final CameraChangeDispatcher cameraChangeDispatcher) {
        return new MapboxMap.OnCompassAnimationListener() {
            /* class com.mapbox.mapboxsdk.maps.MapView.AnonymousClass2 */

            public void onCompassAnimation() {
                cameraChangeDispatcher.onCameraMove();
            }

            public void onCompassAnimationFinished() {
                MapView.this.compassView.isAnimating(false);
                cameraChangeDispatcher.onCameraIdle();
            }
        };
    }

    private View.OnClickListener createCompassClickListener(@NonNull final CameraChangeDispatcher cameraChangeDispatcher) {
        return new View.OnClickListener() {
            /* class com.mapbox.mapboxsdk.maps.MapView.AnonymousClass3 */

            public void onClick(View v) {
                if (MapView.this.mapboxMap != null && MapView.this.compassView != null) {
                    if (MapView.this.focalPoint != null) {
                        MapView.this.mapboxMap.setFocalBearing(0.0d, MapView.this.focalPoint.x, MapView.this.focalPoint.y, 150);
                    } else {
                        MapView.this.mapboxMap.setFocalBearing(0.0d, MapView.this.mapboxMap.getWidth() / 2.0f, MapView.this.mapboxMap.getHeight() / 2.0f, 150);
                    }
                    cameraChangeDispatcher.onCameraMoveStarted(3);
                    MapView.this.compassView.isAnimating(true);
                    MapView.this.compassView.postDelayed(MapView.this.compassView, 650);
                }
            }
        };
    }

    @UiThread
    public void onCreate(@Nullable Bundle savedInstanceState2) {
        if (savedInstanceState2 == null) {
            TelemetryDefinition telemetry = Mapbox.getTelemetry();
            if (telemetry != null) {
                telemetry.onAppUserTurnstileEvent();
            }
        } else if (savedInstanceState2.getBoolean(MapboxConstants.STATE_HAS_SAVED_STATE)) {
            this.savedInstanceState = savedInstanceState2;
        }
    }

    private void initialiseDrawingSurface(MapboxMapOptions options) {
        String localFontFamily = options.getLocalIdeographFontFamily();
        if (options.getTextureMode()) {
            TextureView textureView = new TextureView(getContext());
            this.mapRenderer = new TextureViewMapRenderer(getContext(), textureView, localFontFamily, options.getTranslucentTextureSurface()) {
                /* class com.mapbox.mapboxsdk.maps.MapView.AnonymousClass4 */

                /* access modifiers changed from: protected */
                public void onSurfaceCreated(GL10 gl, EGLConfig config) {
                    MapView.this.onSurfaceCreated();
                    super.onSurfaceCreated(gl, config);
                }
            };
            addView(textureView, 0);
        } else {
            MapboxGLSurfaceView glSurfaceView = new MapboxGLSurfaceView(getContext());
            glSurfaceView.setZOrderMediaOverlay(this.mapboxMapOptions.getRenderSurfaceOnTop());
            this.mapRenderer = new GLSurfaceViewMapRenderer(getContext(), glSurfaceView, localFontFamily) {
                /* class com.mapbox.mapboxsdk.maps.MapView.AnonymousClass5 */

                public void onSurfaceCreated(GL10 gl, EGLConfig config) {
                    MapView.this.onSurfaceCreated();
                    super.onSurfaceCreated(gl, config);
                }
            };
            addView(glSurfaceView, 0);
        }
        this.nativeMapView = new NativeMapView(getContext(), getPixelRatio(), this.mapboxMapOptions.getCrossSourceCollisions(), this, this.mapChangeReceiver, this.mapRenderer);
    }

    /* access modifiers changed from: private */
    public void onSurfaceCreated() {
        post(new Runnable() {
            /* class com.mapbox.mapboxsdk.maps.MapView.AnonymousClass6 */

            public void run() {
                if (!MapView.this.destroyed && MapView.this.mapboxMap == null) {
                    MapView.this.initialiseMap();
                    MapView.this.mapboxMap.onStart();
                }
            }
        });
    }

    @UiThread
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (this.mapboxMap != null) {
            outState.putBoolean(MapboxConstants.STATE_HAS_SAVED_STATE, true);
            this.mapboxMap.onSaveInstanceState(outState);
        }
    }

    @UiThread
    public void onStart() {
        if (!this.isStarted) {
            ConnectivityReceiver.instance(getContext()).activate();
            FileSource.getInstance(getContext()).activate();
            this.isStarted = true;
        }
        if (this.mapboxMap != null) {
            this.mapboxMap.onStart();
        }
        if (this.mapRenderer != null) {
            this.mapRenderer.onStart();
        }
    }

    @UiThread
    public void onResume() {
        if (this.mapRenderer != null) {
            this.mapRenderer.onResume();
        }
    }

    @UiThread
    public void onPause() {
        if (this.mapRenderer != null) {
            this.mapRenderer.onPause();
        }
    }

    @UiThread
    public void onStop() {
        if (this.attributionClickListener != null) {
            this.attributionClickListener.onStop();
        }
        if (this.mapboxMap != null) {
            this.mapGestureDetector.cancelAnimators();
            this.mapboxMap.onStop();
        }
        if (this.mapRenderer != null) {
            this.mapRenderer.onStop();
        }
        if (this.isStarted) {
            ConnectivityReceiver.instance(getContext()).deactivate();
            FileSource.getInstance(getContext()).deactivate();
            this.isStarted = false;
        }
    }

    @UiThread
    public void onDestroy() {
        this.destroyed = true;
        this.mapChangeReceiver.clear();
        this.mapCallback.onDestroy();
        this.initialRenderCallback.onDestroy();
        if (this.compassView != null) {
            this.compassView.resetAnimation();
        }
        if (this.mapboxMap != null) {
            this.mapboxMap.onDestroy();
        }
        if (this.nativeMapView != null) {
            this.nativeMapView.destroy();
            this.nativeMapView = null;
        }
        if (this.mapRenderer != null) {
            this.mapRenderer.onDestroy();
        }
    }

    public void setMaximumFps(int maximumFps) {
        if (this.mapRenderer != null) {
            this.mapRenderer.setMaximumFps(maximumFps);
            return;
        }
        throw new IllegalStateException("Calling MapView#setMaximumFps before mapRenderer is created.");
    }

    @UiThread
    public boolean isDestroyed() {
        return this.destroyed;
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (!isGestureDetectorInitialized()) {
            return super.onTouchEvent(event);
        }
        return this.mapGestureDetector.onTouchEvent(event) || super.onTouchEvent(event);
    }

    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        return this.mapKeyListener.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return this.mapKeyListener.onKeyLongPress(keyCode, event) || super.onKeyLongPress(keyCode, event);
    }

    public boolean onKeyUp(int keyCode, @NonNull KeyEvent event) {
        return this.mapKeyListener.onKeyUp(keyCode, event) || super.onKeyUp(keyCode, event);
    }

    public boolean onTrackballEvent(@NonNull MotionEvent event) {
        return this.mapKeyListener.onTrackballEvent(event) || super.onTrackballEvent(event);
    }

    public boolean onGenericMotionEvent(@NonNull MotionEvent event) {
        if (!isGestureDetectorInitialized()) {
            return super.onGenericMotionEvent(event);
        }
        return this.mapGestureDetector.onGenericMotionEvent(event) || super.onGenericMotionEvent(event);
    }

    @UiThread
    public void onLowMemory() {
        if (this.nativeMapView != null && this.mapboxMap != null && !this.destroyed) {
            this.nativeMapView.onLowMemory();
        }
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int width, int height, int oldw, int oldh) {
        if (!isInEditMode() && this.nativeMapView != null) {
            this.nativeMapView.resizeView(width, height);
        }
    }

    private float getPixelRatio() {
        float pixelRatio = this.mapboxMapOptions.getPixelRatio();
        if (pixelRatio == 0.0f) {
            return getResources().getDisplayMetrics().density;
        }
        return pixelRatio;
    }

    @Nullable
    public Bitmap getViewContent() {
        return BitmapUtils.createBitmapFromView(this);
    }

    public void addOnCameraWillChangeListener(@NonNull OnCameraWillChangeListener listener) {
        this.mapChangeReceiver.addOnCameraWillChangeListener(listener);
    }

    public void removeOnCameraWillChangeListener(@NonNull OnCameraWillChangeListener listener) {
        this.mapChangeReceiver.removeOnCameraWillChangeListener(listener);
    }

    public void addOnCameraIsChangingListener(@NonNull OnCameraIsChangingListener listener) {
        this.mapChangeReceiver.addOnCameraIsChangingListener(listener);
    }

    public void removeOnCameraIsChangingListener(@NonNull OnCameraIsChangingListener listener) {
        this.mapChangeReceiver.removeOnCameraIsChangingListener(listener);
    }

    public void addOnCameraDidChangeListener(@NonNull OnCameraDidChangeListener listener) {
        this.mapChangeReceiver.addOnCameraDidChangeListener(listener);
    }

    public void removeOnCameraDidChangeListener(@NonNull OnCameraDidChangeListener listener) {
        this.mapChangeReceiver.removeOnCameraDidChangeListener(listener);
    }

    public void addOnWillStartLoadingMapListener(@NonNull OnWillStartLoadingMapListener listener) {
        this.mapChangeReceiver.addOnWillStartLoadingMapListener(listener);
    }

    public void removeOnWillStartLoadingMapListener(@NonNull OnWillStartLoadingMapListener listener) {
        this.mapChangeReceiver.removeOnWillStartLoadingMapListener(listener);
    }

    public void addOnDidFinishLoadingMapListener(@NonNull OnDidFinishLoadingMapListener listener) {
        this.mapChangeReceiver.addOnDidFinishLoadingMapListener(listener);
    }

    public void removeOnDidFinishLoadingMapListener(@NonNull OnDidFinishLoadingMapListener listener) {
        this.mapChangeReceiver.removeOnDidFinishLoadingMapListener(listener);
    }

    public void addOnDidFailLoadingMapListener(@NonNull OnDidFailLoadingMapListener listener) {
        this.mapChangeReceiver.addOnDidFailLoadingMapListener(listener);
    }

    public void removeOnDidFailLoadingMapListener(@NonNull OnDidFailLoadingMapListener listener) {
        this.mapChangeReceiver.removeOnDidFailLoadingMapListener(listener);
    }

    public void addOnWillStartRenderingFrameListener(@NonNull OnWillStartRenderingFrameListener listener) {
        this.mapChangeReceiver.addOnWillStartRenderingFrameListener(listener);
    }

    public void removeOnWillStartRenderingFrameListener(@NonNull OnWillStartRenderingFrameListener listener) {
        this.mapChangeReceiver.removeOnWillStartRenderingFrameListener(listener);
    }

    public void addOnDidFinishRenderingFrameListener(@NonNull OnDidFinishRenderingFrameListener listener) {
        this.mapChangeReceiver.addOnDidFinishRenderingFrameListener(listener);
    }

    public void removeOnDidFinishRenderingFrameListener(@NonNull OnDidFinishRenderingFrameListener listener) {
        this.mapChangeReceiver.removeOnDidFinishRenderingFrameListener(listener);
    }

    public void addOnWillStartRenderingMapListener(@NonNull OnWillStartRenderingMapListener listener) {
        this.mapChangeReceiver.addOnWillStartRenderingMapListener(listener);
    }

    public void removeOnWillStartRenderingMapListener(@NonNull OnWillStartRenderingMapListener listener) {
        this.mapChangeReceiver.removeOnWillStartRenderingMapListener(listener);
    }

    public void addOnDidFinishRenderingMapListener(@NonNull OnDidFinishRenderingMapListener listener) {
        this.mapChangeReceiver.addOnDidFinishRenderingMapListener(listener);
    }

    public void removeOnDidFinishRenderingMapListener(OnDidFinishRenderingMapListener listener) {
        this.mapChangeReceiver.removeOnDidFinishRenderingMapListener(listener);
    }

    public void addOnDidBecomeIdleListener(@NonNull OnDidBecomeIdleListener listener) {
        this.mapChangeReceiver.addOnDidBecomeIdleListener(listener);
    }

    public void removeOnDidBecomeIdleListener(@NonNull OnDidBecomeIdleListener listener) {
        this.mapChangeReceiver.removeOnDidBecomeIdleListener(listener);
    }

    public void addOnDidFinishLoadingStyleListener(@NonNull OnDidFinishLoadingStyleListener listener) {
        this.mapChangeReceiver.addOnDidFinishLoadingStyleListener(listener);
    }

    public void removeOnDidFinishLoadingStyleListener(@NonNull OnDidFinishLoadingStyleListener listener) {
        this.mapChangeReceiver.removeOnDidFinishLoadingStyleListener(listener);
    }

    public void addOnSourceChangedListener(@NonNull OnSourceChangedListener listener) {
        this.mapChangeReceiver.addOnSourceChangedListener(listener);
    }

    public void removeOnSourceChangedListener(@NonNull OnSourceChangedListener listener) {
        this.mapChangeReceiver.removeOnSourceChangedListener(listener);
    }

    public void addOnStyleImageMissingListener(@NonNull OnStyleImageMissingListener listener) {
        this.mapChangeReceiver.addOnStyleImageMissingListener(listener);
    }

    public void removeOnStyleImageMissingListener(@NonNull OnStyleImageMissingListener listener) {
        this.mapChangeReceiver.removeOnStyleImageMissingListener(listener);
    }

    public void addOnCanRemoveUnusedStyleImageListener(@NonNull OnCanRemoveUnusedStyleImageListener listener) {
        this.mapChangeReceiver.addOnCanRemoveUnusedStyleImageListener(listener);
    }

    public void removeOnCanRemoveUnusedStyleImageListener(@NonNull OnCanRemoveUnusedStyleImageListener listener) {
        this.mapChangeReceiver.removeOnCanRemoveUnusedStyleImageListener(listener);
    }

    @UiThread
    public void getMapAsync(@NonNull OnMapReadyCallback callback) {
        if (this.mapboxMap == null) {
            this.mapCallback.addOnMapReadyCallback(callback);
        } else {
            callback.onMapReady(this.mapboxMap);
        }
    }

    private boolean isGestureDetectorInitialized() {
        return this.mapGestureDetector != null;
    }

    /* access modifiers changed from: package-private */
    @Nullable
    public MapboxMap getMapboxMap() {
        return this.mapboxMap;
    }

    /* access modifiers changed from: package-private */
    public void setMapboxMap(MapboxMap mapboxMap2) {
        this.mapboxMap = mapboxMap2;
    }

    private class FocalPointInvalidator implements FocalPointChangeListener {
        private final List<FocalPointChangeListener> focalPointChangeListeners;

        private FocalPointInvalidator() {
            this.focalPointChangeListeners = new ArrayList();
        }

        /* access modifiers changed from: package-private */
        public void addListener(FocalPointChangeListener focalPointChangeListener) {
            this.focalPointChangeListeners.add(focalPointChangeListener);
        }

        public void onFocalPointChanged(PointF pointF) {
            MapView.this.mapGestureDetector.setFocalPoint(pointF);
            for (FocalPointChangeListener focalPointChangeListener : this.focalPointChangeListeners) {
                focalPointChangeListener.onFocalPointChanged(pointF);
            }
        }
    }

    private class InitialRenderCallback implements OnDidFinishRenderingFrameListener {
        private int renderCount;

        InitialRenderCallback() {
            MapView.this.addOnDidFinishRenderingFrameListener(this);
        }

        public void onDidFinishRenderingFrame(boolean fully) {
            if (MapView.this.mapboxMap != null && MapView.this.mapboxMap.getStyle() != null && MapView.this.mapboxMap.getStyle().isFullyLoaded()) {
                this.renderCount++;
                if (this.renderCount == 3) {
                    MapView.this.setForeground(null);
                    MapView.this.removeOnDidFinishRenderingFrameListener(this);
                }
            }
        }

        /* access modifiers changed from: private */
        public void onDestroy() {
            MapView.this.removeOnDidFinishRenderingFrameListener(this);
        }
    }

    private class GesturesManagerInteractionListener implements MapboxMap.OnGesturesManagerInteractionListener {
        private GesturesManagerInteractionListener() {
        }

        public void onAddMapClickListener(MapboxMap.OnMapClickListener listener) {
            MapView.this.mapGestureDetector.addOnMapClickListener(listener);
        }

        public void onRemoveMapClickListener(MapboxMap.OnMapClickListener listener) {
            MapView.this.mapGestureDetector.removeOnMapClickListener(listener);
        }

        public void onAddMapLongClickListener(MapboxMap.OnMapLongClickListener listener) {
            MapView.this.mapGestureDetector.addOnMapLongClickListener(listener);
        }

        public void onRemoveMapLongClickListener(MapboxMap.OnMapLongClickListener listener) {
            MapView.this.mapGestureDetector.removeOnMapLongClickListener(listener);
        }

        public void onAddFlingListener(MapboxMap.OnFlingListener listener) {
            MapView.this.mapGestureDetector.addOnFlingListener(listener);
        }

        public void onRemoveFlingListener(MapboxMap.OnFlingListener listener) {
            MapView.this.mapGestureDetector.removeOnFlingListener(listener);
        }

        public void onAddMoveListener(MapboxMap.OnMoveListener listener) {
            MapView.this.mapGestureDetector.addOnMoveListener(listener);
        }

        public void onRemoveMoveListener(MapboxMap.OnMoveListener listener) {
            MapView.this.mapGestureDetector.removeOnMoveListener(listener);
        }

        public void onAddRotateListener(MapboxMap.OnRotateListener listener) {
            MapView.this.mapGestureDetector.addOnRotateListener(listener);
        }

        public void onRemoveRotateListener(MapboxMap.OnRotateListener listener) {
            MapView.this.mapGestureDetector.removeOnRotateListener(listener);
        }

        public void onAddScaleListener(MapboxMap.OnScaleListener listener) {
            MapView.this.mapGestureDetector.addOnScaleListener(listener);
        }

        public void onRemoveScaleListener(MapboxMap.OnScaleListener listener) {
            MapView.this.mapGestureDetector.removeOnScaleListener(listener);
        }

        public void onAddShoveListener(MapboxMap.OnShoveListener listener) {
            MapView.this.mapGestureDetector.addShoveListener(listener);
        }

        public void onRemoveShoveListener(MapboxMap.OnShoveListener listener) {
            MapView.this.mapGestureDetector.removeShoveListener(listener);
        }

        public AndroidGesturesManager getGesturesManager() {
            return MapView.this.mapGestureDetector.getGesturesManager();
        }

        public void setGesturesManager(AndroidGesturesManager gesturesManager, boolean attachDefaultListeners, boolean setDefaultMutuallyExclusives) {
            MapView.this.mapGestureDetector.setGesturesManager(MapView.this.getContext(), gesturesManager, attachDefaultListeners, setDefaultMutuallyExclusives);
        }

        public void cancelAllVelocityAnimations() {
            MapView.this.mapGestureDetector.cancelAnimators();
        }
    }

    private class MapCallback implements OnDidFinishLoadingStyleListener, OnDidFinishRenderingFrameListener, OnDidFinishLoadingMapListener, OnCameraIsChangingListener, OnCameraDidChangeListener, OnDidFailLoadingMapListener {
        private final List<OnMapReadyCallback> onMapReadyCallbackList = new ArrayList();

        MapCallback() {
            MapView.this.addOnDidFinishLoadingStyleListener(this);
            MapView.this.addOnDidFinishRenderingFrameListener(this);
            MapView.this.addOnDidFinishLoadingMapListener(this);
            MapView.this.addOnCameraIsChangingListener(this);
            MapView.this.addOnCameraDidChangeListener(this);
            MapView.this.addOnDidFailLoadingMapListener(this);
        }

        /* access modifiers changed from: package-private */
        public void initialised() {
            MapView.this.mapboxMap.onPreMapReady();
            onMapReady();
            MapView.this.mapboxMap.onPostMapReady();
        }

        private void onMapReady() {
            if (this.onMapReadyCallbackList.size() > 0) {
                Iterator<OnMapReadyCallback> iterator = this.onMapReadyCallbackList.iterator();
                while (iterator.hasNext()) {
                    OnMapReadyCallback callback = iterator.next();
                    if (callback != null) {
                        callback.onMapReady(MapView.this.mapboxMap);
                    }
                    iterator.remove();
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void addOnMapReadyCallback(OnMapReadyCallback callback) {
            this.onMapReadyCallbackList.add(callback);
        }

        /* access modifiers changed from: package-private */
        public void onDestroy() {
            this.onMapReadyCallbackList.clear();
            MapView.this.removeOnDidFinishLoadingStyleListener(this);
            MapView.this.removeOnDidFinishRenderingFrameListener(this);
            MapView.this.removeOnDidFinishLoadingMapListener(this);
            MapView.this.removeOnCameraIsChangingListener(this);
            MapView.this.removeOnCameraDidChangeListener(this);
            MapView.this.removeOnDidFailLoadingMapListener(this);
        }

        public void onDidFinishLoadingStyle() {
            if (MapView.this.mapboxMap != null) {
                MapView.this.mapboxMap.onFinishLoadingStyle();
            }
        }

        public void onDidFailLoadingMap(String errorMessage) {
            if (MapView.this.mapboxMap != null) {
                MapView.this.mapboxMap.onFailLoadingStyle();
            }
        }

        public void onDidFinishRenderingFrame(boolean fully) {
            if (MapView.this.mapboxMap != null) {
                MapView.this.mapboxMap.onUpdateFullyRendered();
            }
        }

        public void onDidFinishLoadingMap() {
            if (MapView.this.mapboxMap != null) {
                MapView.this.mapboxMap.onUpdateRegionChange();
            }
        }

        public void onCameraIsChanging() {
            if (MapView.this.mapboxMap != null) {
                MapView.this.mapboxMap.onUpdateRegionChange();
            }
        }

        public void onCameraDidChange(boolean animated) {
            if (MapView.this.mapboxMap != null) {
                MapView.this.mapboxMap.onUpdateRegionChange();
            }
        }
    }

    private static class AttributionClickListener implements View.OnClickListener {
        @NonNull
        private final AttributionDialogManager defaultDialogManager;
        private UiSettings uiSettings;

        private AttributionClickListener(@NonNull Context context, @NonNull MapboxMap mapboxMap) {
            this.defaultDialogManager = new AttributionDialogManager(context, mapboxMap);
            this.uiSettings = mapboxMap.getUiSettings();
        }

        public void onClick(View v) {
            getDialogManager().onClick(v);
        }

        public void onStop() {
            getDialogManager().onStop();
        }

        private AttributionDialogManager getDialogManager() {
            if (this.uiSettings.getAttributionDialogManager() != null) {
                return this.uiSettings.getAttributionDialogManager();
            }
            return this.defaultDialogManager;
        }
    }

    public static void setMapStrictModeEnabled(boolean strictModeEnabled) {
        MapStrictMode.setStrictModeEnabled(strictModeEnabled);
    }
}
