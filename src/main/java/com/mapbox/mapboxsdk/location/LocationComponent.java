package com.mapbox.mapboxsdk.location;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Looper;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.annotation.StyleRes;
import android.support.annotation.VisibleForTesting;
import android.view.WindowManager;
import com.dji.permission.Permission;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.mapboxsdk.R;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.log.Logger;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.maps.Transform;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

public final class LocationComponent {
    private static final String TAG = "Mbgl-LocationComponent";
    @VisibleForTesting
    @NonNull
    OnCameraTrackingChangedListener cameraTrackingChangedListener;
    @Nullable
    private CompassEngine compassEngine;
    @NonNull
    private CompassListener compassListener;
    private LocationEngineCallback<LocationEngineResult> currentLocationEngineListener;
    @NonNull
    private final MapboxMap.OnDeveloperAnimationListener developerAnimationListener;
    private long fastestInterval;
    @NonNull
    private InternalLocationEngineProvider internalLocationEngineProvider;
    /* access modifiers changed from: private */
    public boolean isComponentInitialized;
    private boolean isComponentStarted;
    /* access modifiers changed from: private */
    public boolean isEnabled;
    private boolean isLayerReady;
    private boolean isListeningToCompass;
    private CameraPosition lastCameraPosition;
    @Nullable
    private Location lastLocation;
    private LocationEngineCallback<LocationEngineResult> lastLocationEngineListener;
    private long lastUpdateTime;
    /* access modifiers changed from: private */
    public LocationAnimatorCoordinator locationAnimatorCoordinator;
    private LocationCameraController locationCameraController;
    @Nullable
    private LocationEngine locationEngine;
    @NonNull
    private LocationEngineRequest locationEngineRequest;
    /* access modifiers changed from: private */
    public LocationLayerController locationLayerController;
    /* access modifiers changed from: private */
    @NonNull
    public final MapboxMap mapboxMap;
    @NonNull
    private MapboxMap.OnCameraIdleListener onCameraIdleListener;
    @NonNull
    private OnCameraMoveInvalidateListener onCameraMoveInvalidateListener;
    /* access modifiers changed from: private */
    @NonNull
    public MapboxMap.OnCameraMoveListener onCameraMoveListener;
    /* access modifiers changed from: private */
    public final CopyOnWriteArrayList<OnCameraTrackingChangedListener> onCameraTrackingChangedListeners;
    /* access modifiers changed from: private */
    public final CopyOnWriteArrayList<OnLocationClickListener> onLocationClickListeners;
    /* access modifiers changed from: private */
    public final CopyOnWriteArrayList<OnLocationLongClickListener> onLocationLongClickListeners;
    @NonNull
    private OnLocationStaleListener onLocationStaleListener;
    /* access modifiers changed from: private */
    public final CopyOnWriteArrayList<OnLocationStaleListener> onLocationStaleListeners;
    @NonNull
    private MapboxMap.OnMapClickListener onMapClickListener;
    @NonNull
    private MapboxMap.OnMapLongClickListener onMapLongClickListener;
    /* access modifiers changed from: private */
    public final CopyOnWriteArrayList<OnRenderModeChangedListener> onRenderModeChangedListeners;
    private LocationComponentOptions options;
    @VisibleForTesting
    @NonNull
    OnRenderModeChangedListener renderModeChangedListener;
    private StaleStateManager staleStateManager;
    private Style style;
    @NonNull
    private final Transform transform;

    public LocationComponent(@NonNull MapboxMap mapboxMap2, @NonNull Transform transform2, @NonNull List<MapboxMap.OnDeveloperAnimationListener> developerAnimationListeners) {
        this.internalLocationEngineProvider = new InternalLocationEngineProvider();
        this.locationEngineRequest = new LocationEngineRequest.Builder(1000).setFastestInterval(1000).setPriority(0).build();
        this.currentLocationEngineListener = new CurrentLocationEngineCallback(this);
        this.lastLocationEngineListener = new LastLocationEngineCallback(this);
        this.onLocationStaleListeners = new CopyOnWriteArrayList<>();
        this.onLocationClickListeners = new CopyOnWriteArrayList<>();
        this.onLocationLongClickListeners = new CopyOnWriteArrayList<>();
        this.onCameraTrackingChangedListeners = new CopyOnWriteArrayList<>();
        this.onRenderModeChangedListeners = new CopyOnWriteArrayList<>();
        this.onCameraMoveListener = new MapboxMap.OnCameraMoveListener() {
            /* class com.mapbox.mapboxsdk.location.LocationComponent.AnonymousClass1 */

            public void onCameraMove() {
                LocationComponent.this.updateLayerOffsets(false);
            }
        };
        this.onCameraIdleListener = new MapboxMap.OnCameraIdleListener() {
            /* class com.mapbox.mapboxsdk.location.LocationComponent.AnonymousClass2 */

            public void onCameraIdle() {
                LocationComponent.this.updateLayerOffsets(false);
            }
        };
        this.onMapClickListener = new MapboxMap.OnMapClickListener() {
            /* class com.mapbox.mapboxsdk.location.LocationComponent.AnonymousClass3 */

            public boolean onMapClick(@NonNull LatLng point) {
                if (LocationComponent.this.onLocationClickListeners.isEmpty() || !LocationComponent.this.locationLayerController.onMapClick(point)) {
                    return false;
                }
                Iterator it2 = LocationComponent.this.onLocationClickListeners.iterator();
                while (it2.hasNext()) {
                    ((OnLocationClickListener) it2.next()).onLocationComponentClick();
                }
                return true;
            }
        };
        this.onMapLongClickListener = new MapboxMap.OnMapLongClickListener() {
            /* class com.mapbox.mapboxsdk.location.LocationComponent.AnonymousClass4 */

            public boolean onMapLongClick(@NonNull LatLng point) {
                if (LocationComponent.this.onLocationLongClickListeners.isEmpty() || !LocationComponent.this.locationLayerController.onMapClick(point)) {
                    return false;
                }
                Iterator it2 = LocationComponent.this.onLocationLongClickListeners.iterator();
                while (it2.hasNext()) {
                    ((OnLocationLongClickListener) it2.next()).onLocationComponentLongClick();
                }
                return true;
            }
        };
        this.onLocationStaleListener = new OnLocationStaleListener() {
            /* class com.mapbox.mapboxsdk.location.LocationComponent.AnonymousClass5 */

            public void onStaleStateChange(boolean isStale) {
                LocationComponent.this.locationLayerController.setLocationsStale(isStale);
                Iterator it2 = LocationComponent.this.onLocationStaleListeners.iterator();
                while (it2.hasNext()) {
                    ((OnLocationStaleListener) it2.next()).onStaleStateChange(isStale);
                }
            }
        };
        this.onCameraMoveInvalidateListener = new OnCameraMoveInvalidateListener() {
            /* class com.mapbox.mapboxsdk.location.LocationComponent.AnonymousClass6 */

            public void onInvalidateCameraMove() {
                LocationComponent.this.onCameraMoveListener.onCameraMove();
            }
        };
        this.compassListener = new CompassListener() {
            /* class com.mapbox.mapboxsdk.location.LocationComponent.AnonymousClass7 */

            public void onCompassChanged(float userHeading) {
                LocationComponent.this.updateCompassHeading(userHeading);
            }

            public void onCompassAccuracyChange(int compassStatus) {
            }
        };
        this.cameraTrackingChangedListener = new OnCameraTrackingChangedListener() {
            /* class com.mapbox.mapboxsdk.location.LocationComponent.AnonymousClass8 */

            public void onCameraTrackingDismissed() {
                Iterator it2 = LocationComponent.this.onCameraTrackingChangedListeners.iterator();
                while (it2.hasNext()) {
                    ((OnCameraTrackingChangedListener) it2.next()).onCameraTrackingDismissed();
                }
            }

            public void onCameraTrackingChanged(int currentMode) {
                LocationComponent.this.locationAnimatorCoordinator.cancelZoomAnimation();
                LocationComponent.this.locationAnimatorCoordinator.cancelTiltAnimation();
                LocationComponent.this.updateAnimatorListenerHolders();
                Iterator it2 = LocationComponent.this.onCameraTrackingChangedListeners.iterator();
                while (it2.hasNext()) {
                    ((OnCameraTrackingChangedListener) it2.next()).onCameraTrackingChanged(currentMode);
                }
            }
        };
        this.renderModeChangedListener = new OnRenderModeChangedListener() {
            /* class com.mapbox.mapboxsdk.location.LocationComponent.AnonymousClass9 */

            public void onRenderModeChanged(int currentMode) {
                LocationComponent.this.updateAnimatorListenerHolders();
                Iterator it2 = LocationComponent.this.onRenderModeChangedListeners.iterator();
                while (it2.hasNext()) {
                    ((OnRenderModeChangedListener) it2.next()).onRenderModeChanged(currentMode);
                }
            }
        };
        this.developerAnimationListener = new MapboxMap.OnDeveloperAnimationListener() {
            /* class com.mapbox.mapboxsdk.location.LocationComponent.AnonymousClass10 */

            public void onDeveloperAnimationStarted() {
                if (LocationComponent.this.isComponentInitialized && LocationComponent.this.isEnabled) {
                    LocationComponent.this.setCameraMode(8);
                }
            }
        };
        this.mapboxMap = mapboxMap2;
        this.transform = transform2;
        developerAnimationListeners.add(this.developerAnimationListener);
    }

    LocationComponent() {
        this.internalLocationEngineProvider = new InternalLocationEngineProvider();
        this.locationEngineRequest = new LocationEngineRequest.Builder(1000).setFastestInterval(1000).setPriority(0).build();
        this.currentLocationEngineListener = new CurrentLocationEngineCallback(this);
        this.lastLocationEngineListener = new LastLocationEngineCallback(this);
        this.onLocationStaleListeners = new CopyOnWriteArrayList<>();
        this.onLocationClickListeners = new CopyOnWriteArrayList<>();
        this.onLocationLongClickListeners = new CopyOnWriteArrayList<>();
        this.onCameraTrackingChangedListeners = new CopyOnWriteArrayList<>();
        this.onRenderModeChangedListeners = new CopyOnWriteArrayList<>();
        this.onCameraMoveListener = new MapboxMap.OnCameraMoveListener() {
            /* class com.mapbox.mapboxsdk.location.LocationComponent.AnonymousClass1 */

            public void onCameraMove() {
                LocationComponent.this.updateLayerOffsets(false);
            }
        };
        this.onCameraIdleListener = new MapboxMap.OnCameraIdleListener() {
            /* class com.mapbox.mapboxsdk.location.LocationComponent.AnonymousClass2 */

            public void onCameraIdle() {
                LocationComponent.this.updateLayerOffsets(false);
            }
        };
        this.onMapClickListener = new MapboxMap.OnMapClickListener() {
            /* class com.mapbox.mapboxsdk.location.LocationComponent.AnonymousClass3 */

            public boolean onMapClick(@NonNull LatLng point) {
                if (LocationComponent.this.onLocationClickListeners.isEmpty() || !LocationComponent.this.locationLayerController.onMapClick(point)) {
                    return false;
                }
                Iterator it2 = LocationComponent.this.onLocationClickListeners.iterator();
                while (it2.hasNext()) {
                    ((OnLocationClickListener) it2.next()).onLocationComponentClick();
                }
                return true;
            }
        };
        this.onMapLongClickListener = new MapboxMap.OnMapLongClickListener() {
            /* class com.mapbox.mapboxsdk.location.LocationComponent.AnonymousClass4 */

            public boolean onMapLongClick(@NonNull LatLng point) {
                if (LocationComponent.this.onLocationLongClickListeners.isEmpty() || !LocationComponent.this.locationLayerController.onMapClick(point)) {
                    return false;
                }
                Iterator it2 = LocationComponent.this.onLocationLongClickListeners.iterator();
                while (it2.hasNext()) {
                    ((OnLocationLongClickListener) it2.next()).onLocationComponentLongClick();
                }
                return true;
            }
        };
        this.onLocationStaleListener = new OnLocationStaleListener() {
            /* class com.mapbox.mapboxsdk.location.LocationComponent.AnonymousClass5 */

            public void onStaleStateChange(boolean isStale) {
                LocationComponent.this.locationLayerController.setLocationsStale(isStale);
                Iterator it2 = LocationComponent.this.onLocationStaleListeners.iterator();
                while (it2.hasNext()) {
                    ((OnLocationStaleListener) it2.next()).onStaleStateChange(isStale);
                }
            }
        };
        this.onCameraMoveInvalidateListener = new OnCameraMoveInvalidateListener() {
            /* class com.mapbox.mapboxsdk.location.LocationComponent.AnonymousClass6 */

            public void onInvalidateCameraMove() {
                LocationComponent.this.onCameraMoveListener.onCameraMove();
            }
        };
        this.compassListener = new CompassListener() {
            /* class com.mapbox.mapboxsdk.location.LocationComponent.AnonymousClass7 */

            public void onCompassChanged(float userHeading) {
                LocationComponent.this.updateCompassHeading(userHeading);
            }

            public void onCompassAccuracyChange(int compassStatus) {
            }
        };
        this.cameraTrackingChangedListener = new OnCameraTrackingChangedListener() {
            /* class com.mapbox.mapboxsdk.location.LocationComponent.AnonymousClass8 */

            public void onCameraTrackingDismissed() {
                Iterator it2 = LocationComponent.this.onCameraTrackingChangedListeners.iterator();
                while (it2.hasNext()) {
                    ((OnCameraTrackingChangedListener) it2.next()).onCameraTrackingDismissed();
                }
            }

            public void onCameraTrackingChanged(int currentMode) {
                LocationComponent.this.locationAnimatorCoordinator.cancelZoomAnimation();
                LocationComponent.this.locationAnimatorCoordinator.cancelTiltAnimation();
                LocationComponent.this.updateAnimatorListenerHolders();
                Iterator it2 = LocationComponent.this.onCameraTrackingChangedListeners.iterator();
                while (it2.hasNext()) {
                    ((OnCameraTrackingChangedListener) it2.next()).onCameraTrackingChanged(currentMode);
                }
            }
        };
        this.renderModeChangedListener = new OnRenderModeChangedListener() {
            /* class com.mapbox.mapboxsdk.location.LocationComponent.AnonymousClass9 */

            public void onRenderModeChanged(int currentMode) {
                LocationComponent.this.updateAnimatorListenerHolders();
                Iterator it2 = LocationComponent.this.onRenderModeChangedListeners.iterator();
                while (it2.hasNext()) {
                    ((OnRenderModeChangedListener) it2.next()).onRenderModeChanged(currentMode);
                }
            }
        };
        this.developerAnimationListener = new MapboxMap.OnDeveloperAnimationListener() {
            /* class com.mapbox.mapboxsdk.location.LocationComponent.AnonymousClass10 */

            public void onDeveloperAnimationStarted() {
                if (LocationComponent.this.isComponentInitialized && LocationComponent.this.isEnabled) {
                    LocationComponent.this.setCameraMode(8);
                }
            }
        };
        this.mapboxMap = null;
        this.transform = null;
    }

    @VisibleForTesting
    LocationComponent(@NonNull MapboxMap mapboxMap2, @NonNull Transform transform2, @NonNull List<MapboxMap.OnDeveloperAnimationListener> developerAnimationListeners, @NonNull LocationEngineCallback<LocationEngineResult> currentListener, @NonNull LocationEngineCallback<LocationEngineResult> lastListener, @NonNull LocationLayerController locationLayerController2, @NonNull LocationCameraController locationCameraController2, @NonNull LocationAnimatorCoordinator locationAnimatorCoordinator2, @NonNull StaleStateManager staleStateManager2, @NonNull CompassEngine compassEngine2, @NonNull InternalLocationEngineProvider internalLocationEngineProvider2) {
        this.internalLocationEngineProvider = new InternalLocationEngineProvider();
        this.locationEngineRequest = new LocationEngineRequest.Builder(1000).setFastestInterval(1000).setPriority(0).build();
        this.currentLocationEngineListener = new CurrentLocationEngineCallback(this);
        this.lastLocationEngineListener = new LastLocationEngineCallback(this);
        this.onLocationStaleListeners = new CopyOnWriteArrayList<>();
        this.onLocationClickListeners = new CopyOnWriteArrayList<>();
        this.onLocationLongClickListeners = new CopyOnWriteArrayList<>();
        this.onCameraTrackingChangedListeners = new CopyOnWriteArrayList<>();
        this.onRenderModeChangedListeners = new CopyOnWriteArrayList<>();
        this.onCameraMoveListener = new MapboxMap.OnCameraMoveListener() {
            /* class com.mapbox.mapboxsdk.location.LocationComponent.AnonymousClass1 */

            public void onCameraMove() {
                LocationComponent.this.updateLayerOffsets(false);
            }
        };
        this.onCameraIdleListener = new MapboxMap.OnCameraIdleListener() {
            /* class com.mapbox.mapboxsdk.location.LocationComponent.AnonymousClass2 */

            public void onCameraIdle() {
                LocationComponent.this.updateLayerOffsets(false);
            }
        };
        this.onMapClickListener = new MapboxMap.OnMapClickListener() {
            /* class com.mapbox.mapboxsdk.location.LocationComponent.AnonymousClass3 */

            public boolean onMapClick(@NonNull LatLng point) {
                if (LocationComponent.this.onLocationClickListeners.isEmpty() || !LocationComponent.this.locationLayerController.onMapClick(point)) {
                    return false;
                }
                Iterator it2 = LocationComponent.this.onLocationClickListeners.iterator();
                while (it2.hasNext()) {
                    ((OnLocationClickListener) it2.next()).onLocationComponentClick();
                }
                return true;
            }
        };
        this.onMapLongClickListener = new MapboxMap.OnMapLongClickListener() {
            /* class com.mapbox.mapboxsdk.location.LocationComponent.AnonymousClass4 */

            public boolean onMapLongClick(@NonNull LatLng point) {
                if (LocationComponent.this.onLocationLongClickListeners.isEmpty() || !LocationComponent.this.locationLayerController.onMapClick(point)) {
                    return false;
                }
                Iterator it2 = LocationComponent.this.onLocationLongClickListeners.iterator();
                while (it2.hasNext()) {
                    ((OnLocationLongClickListener) it2.next()).onLocationComponentLongClick();
                }
                return true;
            }
        };
        this.onLocationStaleListener = new OnLocationStaleListener() {
            /* class com.mapbox.mapboxsdk.location.LocationComponent.AnonymousClass5 */

            public void onStaleStateChange(boolean isStale) {
                LocationComponent.this.locationLayerController.setLocationsStale(isStale);
                Iterator it2 = LocationComponent.this.onLocationStaleListeners.iterator();
                while (it2.hasNext()) {
                    ((OnLocationStaleListener) it2.next()).onStaleStateChange(isStale);
                }
            }
        };
        this.onCameraMoveInvalidateListener = new OnCameraMoveInvalidateListener() {
            /* class com.mapbox.mapboxsdk.location.LocationComponent.AnonymousClass6 */

            public void onInvalidateCameraMove() {
                LocationComponent.this.onCameraMoveListener.onCameraMove();
            }
        };
        this.compassListener = new CompassListener() {
            /* class com.mapbox.mapboxsdk.location.LocationComponent.AnonymousClass7 */

            public void onCompassChanged(float userHeading) {
                LocationComponent.this.updateCompassHeading(userHeading);
            }

            public void onCompassAccuracyChange(int compassStatus) {
            }
        };
        this.cameraTrackingChangedListener = new OnCameraTrackingChangedListener() {
            /* class com.mapbox.mapboxsdk.location.LocationComponent.AnonymousClass8 */

            public void onCameraTrackingDismissed() {
                Iterator it2 = LocationComponent.this.onCameraTrackingChangedListeners.iterator();
                while (it2.hasNext()) {
                    ((OnCameraTrackingChangedListener) it2.next()).onCameraTrackingDismissed();
                }
            }

            public void onCameraTrackingChanged(int currentMode) {
                LocationComponent.this.locationAnimatorCoordinator.cancelZoomAnimation();
                LocationComponent.this.locationAnimatorCoordinator.cancelTiltAnimation();
                LocationComponent.this.updateAnimatorListenerHolders();
                Iterator it2 = LocationComponent.this.onCameraTrackingChangedListeners.iterator();
                while (it2.hasNext()) {
                    ((OnCameraTrackingChangedListener) it2.next()).onCameraTrackingChanged(currentMode);
                }
            }
        };
        this.renderModeChangedListener = new OnRenderModeChangedListener() {
            /* class com.mapbox.mapboxsdk.location.LocationComponent.AnonymousClass9 */

            public void onRenderModeChanged(int currentMode) {
                LocationComponent.this.updateAnimatorListenerHolders();
                Iterator it2 = LocationComponent.this.onRenderModeChangedListeners.iterator();
                while (it2.hasNext()) {
                    ((OnRenderModeChangedListener) it2.next()).onRenderModeChanged(currentMode);
                }
            }
        };
        this.developerAnimationListener = new MapboxMap.OnDeveloperAnimationListener() {
            /* class com.mapbox.mapboxsdk.location.LocationComponent.AnonymousClass10 */

            public void onDeveloperAnimationStarted() {
                if (LocationComponent.this.isComponentInitialized && LocationComponent.this.isEnabled) {
                    LocationComponent.this.setCameraMode(8);
                }
            }
        };
        this.mapboxMap = mapboxMap2;
        this.transform = transform2;
        developerAnimationListeners.add(this.developerAnimationListener);
        this.currentLocationEngineListener = currentListener;
        this.lastLocationEngineListener = lastListener;
        this.locationLayerController = locationLayerController2;
        this.locationCameraController = locationCameraController2;
        this.locationAnimatorCoordinator = locationAnimatorCoordinator2;
        this.staleStateManager = staleStateManager2;
        this.compassEngine = compassEngine2;
        this.internalLocationEngineProvider = internalLocationEngineProvider2;
        this.isComponentInitialized = true;
    }

    @Deprecated
    public void activateLocationComponent(@NonNull Context context, @NonNull Style style2) {
        activateLocationComponent(context, style2, LocationComponentOptions.createFromAttributes(context, R.style.mapbox_LocationComponent));
    }

    @Deprecated
    public void activateLocationComponent(@NonNull Context context, @NonNull Style style2, boolean useDefaultLocationEngine) {
        if (useDefaultLocationEngine) {
            activateLocationComponent(context, style2, R.style.mapbox_LocationComponent);
        } else {
            activateLocationComponent(context, style2, (LocationEngine) null, R.style.mapbox_LocationComponent);
        }
    }

    @Deprecated
    public void activateLocationComponent(@NonNull Context context, @NonNull Style style2, boolean useDefaultLocationEngine, @NonNull LocationEngineRequest locationEngineRequest2) {
        setLocationEngineRequest(locationEngineRequest2);
        if (useDefaultLocationEngine) {
            activateLocationComponent(context, style2, R.style.mapbox_LocationComponent);
        } else {
            activateLocationComponent(context, style2, (LocationEngine) null, R.style.mapbox_LocationComponent);
        }
    }

    @Deprecated
    public void activateLocationComponent(@NonNull Context context, @NonNull Style style2, boolean useDefaultLocationEngine, @NonNull LocationEngineRequest locationEngineRequest2, @NonNull LocationComponentOptions options2) {
        setLocationEngineRequest(locationEngineRequest2);
        if (useDefaultLocationEngine) {
            activateLocationComponent(context, style2, options2);
        } else {
            activateLocationComponent(context, style2, (LocationEngine) null, options2);
        }
    }

    @Deprecated
    public void activateLocationComponent(@NonNull Context context, @NonNull Style style2, @StyleRes int styleRes) {
        activateLocationComponent(context, style2, LocationComponentOptions.createFromAttributes(context, styleRes));
    }

    @Deprecated
    public void activateLocationComponent(@NonNull Context context, @NonNull Style style2, @NonNull LocationComponentOptions options2) {
        initialize(context, style2, options2);
        initializeLocationEngine(context);
        applyStyle(options2);
    }

    @Deprecated
    public void activateLocationComponent(@NonNull Context context, @NonNull Style style2, @Nullable LocationEngine locationEngine2, @StyleRes int styleRes) {
        activateLocationComponent(context, style2, locationEngine2, LocationComponentOptions.createFromAttributes(context, styleRes));
    }

    @Deprecated
    public void activateLocationComponent(@NonNull Context context, @NonNull Style style2, @Nullable LocationEngine locationEngine2, @NonNull LocationEngineRequest locationEngineRequest2, @StyleRes int styleRes) {
        activateLocationComponent(context, style2, locationEngine2, locationEngineRequest2, LocationComponentOptions.createFromAttributes(context, styleRes));
    }

    @Deprecated
    public void activateLocationComponent(@NonNull Context context, @NonNull Style style2, @Nullable LocationEngine locationEngine2) {
        activateLocationComponent(context, style2, locationEngine2, R.style.mapbox_LocationComponent);
    }

    @Deprecated
    public void activateLocationComponent(@NonNull Context context, @NonNull Style style2, @Nullable LocationEngine locationEngine2, @NonNull LocationEngineRequest locationEngineRequest2) {
        activateLocationComponent(context, style2, locationEngine2, locationEngineRequest2, R.style.mapbox_LocationComponent);
    }

    @Deprecated
    public void activateLocationComponent(@NonNull Context context, @NonNull Style style2, @Nullable LocationEngine locationEngine2, @NonNull LocationComponentOptions options2) {
        initialize(context, style2, options2);
        setLocationEngine(locationEngine2);
        applyStyle(options2);
    }

    @Deprecated
    public void activateLocationComponent(@NonNull Context context, @NonNull Style style2, @Nullable LocationEngine locationEngine2, @NonNull LocationEngineRequest locationEngineRequest2, @NonNull LocationComponentOptions options2) {
        initialize(context, style2, options2);
        setLocationEngineRequest(locationEngineRequest2);
        setLocationEngine(locationEngine2);
        applyStyle(options2);
    }

    public void activateLocationComponent(@NonNull LocationComponentActivationOptions activationOptions) {
        LocationComponentOptions options2 = activationOptions.locationComponentOptions();
        if (options2 == null) {
            int styleRes = activationOptions.styleRes();
            if (styleRes == 0) {
                styleRes = R.style.mapbox_LocationComponent;
            }
            options2 = LocationComponentOptions.createFromAttributes(activationOptions.context(), styleRes);
        }
        initialize(activationOptions.context(), activationOptions.style(), options2);
        applyStyle(options2);
        LocationEngineRequest locationEngineRequest2 = activationOptions.locationEngineRequest();
        if (locationEngineRequest2 != null) {
            setLocationEngineRequest(locationEngineRequest2);
        }
        LocationEngine locationEngine2 = activationOptions.locationEngine();
        if (locationEngine2 != null) {
            setLocationEngine(locationEngine2);
        } else if (activationOptions.useDefaultLocationEngine()) {
            initializeLocationEngine(activationOptions.context());
        } else {
            setLocationEngine(null);
        }
    }

    @RequiresPermission(anyOf = {Permission.ACCESS_FINE_LOCATION, Permission.ACCESS_COARSE_LOCATION})
    public void setLocationComponentEnabled(boolean isEnabled2) {
        checkActivationState();
        if (isEnabled2) {
            enableLocationComponent();
        } else {
            disableLocationComponent();
        }
    }

    public boolean isLocationComponentEnabled() {
        checkActivationState();
        return this.isEnabled;
    }

    public void setCameraMode(int cameraMode) {
        setCameraMode(cameraMode, null);
    }

    public void setCameraMode(int cameraMode, @Nullable OnLocationCameraTransitionListener transitionListener) {
        setCameraMode(cameraMode, 750, null, null, null, transitionListener);
    }

    public void setCameraMode(int cameraMode, long transitionDuration, @Nullable Double zoom, @Nullable Double bearing, @Nullable Double tilt, @Nullable OnLocationCameraTransitionListener transitionListener) {
        checkActivationState();
        this.locationCameraController.setCameraMode(cameraMode, this.lastLocation, transitionDuration, zoom, bearing, tilt, new CameraTransitionListener(transitionListener));
        updateCompassListenerState(true);
    }

    private class CameraTransitionListener implements OnLocationCameraTransitionListener {
        private final OnLocationCameraTransitionListener externalListener;

        private CameraTransitionListener(OnLocationCameraTransitionListener externalListener2) {
            this.externalListener = externalListener2;
        }

        public void onLocationCameraTransitionFinished(int cameraMode) {
            if (this.externalListener != null) {
                this.externalListener.onLocationCameraTransitionFinished(cameraMode);
            }
            reset(cameraMode);
        }

        public void onLocationCameraTransitionCanceled(int cameraMode) {
            if (this.externalListener != null) {
                this.externalListener.onLocationCameraTransitionCanceled(cameraMode);
            }
            reset(cameraMode);
        }

        private void reset(int cameraMode) {
            LocationComponent.this.locationAnimatorCoordinator.resetAllCameraAnimations(LocationComponent.this.mapboxMap.getCameraPosition(), cameraMode == 36);
        }
    }

    public int getCameraMode() {
        checkActivationState();
        return this.locationCameraController.getCameraMode();
    }

    public void setRenderMode(int renderMode) {
        checkActivationState();
        this.locationLayerController.setRenderMode(renderMode);
        updateLayerOffsets(true);
        updateCompassListenerState(true);
    }

    public int getRenderMode() {
        checkActivationState();
        return this.locationLayerController.getRenderMode();
    }

    public LocationComponentOptions getLocationComponentOptions() {
        checkActivationState();
        return this.options;
    }

    public void applyStyle(@NonNull Context context, @StyleRes int styleRes) {
        checkActivationState();
        applyStyle(LocationComponentOptions.createFromAttributes(context, styleRes));
    }

    public void applyStyle(@NonNull LocationComponentOptions options2) {
        checkActivationState();
        this.options = options2;
        if (this.mapboxMap.getStyle() != null) {
            this.locationLayerController.applyStyle(options2);
            this.locationCameraController.initializeOptions(options2);
            this.staleStateManager.setEnabled(options2.enableStaleState());
            this.staleStateManager.setDelayTime(options2.staleStateTimeout());
            this.locationAnimatorCoordinator.setTrackingAnimationDurationMultiplier(options2.trackingAnimationDurationMultiplier());
            this.locationAnimatorCoordinator.setCompassAnimationEnabled(options2.compassAnimationEnabled());
            this.locationAnimatorCoordinator.setAccuracyAnimationEnabled(options2.accuracyAnimationEnabled());
            updateMapWithOptions(options2);
        }
    }

    public void zoomWhileTracking(double zoomLevel, long animationDuration, @Nullable MapboxMap.CancelableCallback callback) {
        checkActivationState();
        if (this.isLayerReady) {
            if (getCameraMode() == 8) {
                Logger.e(TAG, String.format("%s%s", "LocationComponent#zoomWhileTracking method can only be used", " when a camera mode other than CameraMode#NONE is engaged."));
            } else if (this.locationCameraController.isTransitioning()) {
                Logger.e(TAG, "LocationComponent#zoomWhileTracking method call is ignored because the camera mode is transitioning");
            } else {
                this.locationAnimatorCoordinator.feedNewZoomLevel(zoomLevel, this.mapboxMap.getCameraPosition(), animationDuration, callback);
            }
        }
    }

    public void zoomWhileTracking(double zoomLevel, long animationDuration) {
        checkActivationState();
        zoomWhileTracking(zoomLevel, animationDuration, null);
    }

    public void zoomWhileTracking(double zoomLevel) {
        checkActivationState();
        zoomWhileTracking(zoomLevel, 750, null);
    }

    public void cancelZoomWhileTrackingAnimation() {
        checkActivationState();
        this.locationAnimatorCoordinator.cancelZoomAnimation();
    }

    public void tiltWhileTracking(double tilt, long animationDuration, @Nullable MapboxMap.CancelableCallback callback) {
        checkActivationState();
        if (this.isLayerReady) {
            if (getCameraMode() == 8) {
                Logger.e(TAG, String.format("%s%s", "LocationComponent#tiltWhileTracking method can only be used", " when a camera mode other than CameraMode#NONE is engaged."));
            } else if (this.locationCameraController.isTransitioning()) {
                Logger.e(TAG, "LocationComponent#tiltWhileTracking method call is ignored because the camera mode is transitioning");
            } else {
                this.locationAnimatorCoordinator.feedNewTilt(tilt, this.mapboxMap.getCameraPosition(), animationDuration, callback);
            }
        }
    }

    public void tiltWhileTracking(double tilt, long animationDuration) {
        checkActivationState();
        tiltWhileTracking(tilt, animationDuration, null);
    }

    public void tiltWhileTracking(double tilt) {
        checkActivationState();
        tiltWhileTracking(tilt, 1250, null);
    }

    public void cancelTiltWhileTrackingAnimation() {
        checkActivationState();
        this.locationAnimatorCoordinator.cancelTiltAnimation();
    }

    public void forceLocationUpdate(@Nullable Location location) {
        checkActivationState();
        updateLocation(location, false);
    }

    public void setMaxAnimationFps(int maxAnimationFps) {
        checkActivationState();
        this.locationAnimatorCoordinator.setMaxAnimationFps(maxAnimationFps);
    }

    @SuppressLint({"MissingPermission"})
    public void setLocationEngine(@Nullable LocationEngine locationEngine2) {
        checkActivationState();
        if (this.locationEngine != null) {
            this.locationEngine.removeLocationUpdates(this.currentLocationEngineListener);
            this.locationEngine = null;
        }
        if (locationEngine2 != null) {
            this.fastestInterval = this.locationEngineRequest.getFastestInterval();
            this.locationEngine = locationEngine2;
            if (this.isLayerReady && this.isEnabled) {
                setLastLocation();
                locationEngine2.requestLocationUpdates(this.locationEngineRequest, this.currentLocationEngineListener, Looper.getMainLooper());
                return;
            }
            return;
        }
        this.fastestInterval = 0;
    }

    public void setLocationEngineRequest(@NonNull LocationEngineRequest locationEngineRequest2) {
        checkActivationState();
        this.locationEngineRequest = locationEngineRequest2;
        setLocationEngine(this.locationEngine);
    }

    @NonNull
    public LocationEngineRequest getLocationEngineRequest() {
        checkActivationState();
        return this.locationEngineRequest;
    }

    @Nullable
    public LocationEngine getLocationEngine() {
        checkActivationState();
        return this.locationEngine;
    }

    public void setCompassEngine(@Nullable CompassEngine compassEngine2) {
        checkActivationState();
        if (this.compassEngine != null) {
            updateCompassListenerState(false);
        }
        this.compassEngine = compassEngine2;
        updateCompassListenerState(true);
    }

    @Nullable
    public CompassEngine getCompassEngine() {
        checkActivationState();
        return this.compassEngine;
    }

    @Nullable
    public Location getLastKnownLocation() {
        checkActivationState();
        return this.lastLocation;
    }

    public void addOnLocationClickListener(@NonNull OnLocationClickListener listener) {
        this.onLocationClickListeners.add(listener);
    }

    public void removeOnLocationClickListener(@NonNull OnLocationClickListener listener) {
        this.onLocationClickListeners.remove(listener);
    }

    public void addOnLocationLongClickListener(@NonNull OnLocationLongClickListener listener) {
        this.onLocationLongClickListeners.add(listener);
    }

    public void removeOnLocationLongClickListener(@NonNull OnLocationLongClickListener listener) {
        this.onLocationLongClickListeners.remove(listener);
    }

    public void addOnCameraTrackingChangedListener(@NonNull OnCameraTrackingChangedListener listener) {
        this.onCameraTrackingChangedListeners.add(listener);
    }

    public void removeOnCameraTrackingChangedListener(@NonNull OnCameraTrackingChangedListener listener) {
        this.onCameraTrackingChangedListeners.remove(listener);
    }

    public void addOnRenderModeChangedListener(@NonNull OnRenderModeChangedListener listener) {
        this.onRenderModeChangedListeners.add(listener);
    }

    public void removeRenderModeChangedListener(@NonNull OnRenderModeChangedListener listener) {
        this.onRenderModeChangedListeners.remove(listener);
    }

    public void addOnLocationStaleListener(@NonNull OnLocationStaleListener listener) {
        this.onLocationStaleListeners.add(listener);
    }

    public void removeOnLocationStaleListener(@NonNull OnLocationStaleListener listener) {
        this.onLocationStaleListeners.remove(listener);
    }

    public void onStart() {
        this.isComponentStarted = true;
        onLocationLayerStart();
    }

    public void onStop() {
        onLocationLayerStop();
        this.isComponentStarted = false;
    }

    public void onDestroy() {
    }

    public void onStartLoadingMap() {
        onLocationLayerStop();
    }

    public void onFinishLoadingStyle() {
        if (this.isComponentInitialized) {
            this.style = this.mapboxMap.getStyle();
            this.locationLayerController.initializeComponents(this.style, this.options);
            this.locationCameraController.initializeOptions(this.options);
            onLocationLayerStart();
        }
    }

    @SuppressLint({"MissingPermission"})
    private void onLocationLayerStart() {
        if (this.isComponentInitialized && this.isComponentStarted && this.mapboxMap.getStyle() != null) {
            if (!this.isLayerReady) {
                this.isLayerReady = true;
                this.mapboxMap.addOnCameraMoveListener(this.onCameraMoveListener);
                this.mapboxMap.addOnCameraIdleListener(this.onCameraIdleListener);
                if (this.options.enableStaleState()) {
                    this.staleStateManager.onStart();
                }
            }
            if (this.isEnabled) {
                if (this.locationEngine != null) {
                    try {
                        this.locationEngine.requestLocationUpdates(this.locationEngineRequest, this.currentLocationEngineListener, Looper.getMainLooper());
                    } catch (SecurityException se) {
                        Logger.e(TAG, "Unable to request location updates", se);
                    }
                }
                setCameraMode(this.locationCameraController.getCameraMode());
                setLastLocation();
                updateCompassListenerState(true);
                setLastCompassHeading();
            }
        }
    }

    private void onLocationLayerStop() {
        if (this.isComponentInitialized && this.isLayerReady && this.isComponentStarted) {
            this.isLayerReady = false;
            this.staleStateManager.onStop();
            if (this.compassEngine != null) {
                updateCompassListenerState(false);
            }
            this.locationAnimatorCoordinator.cancelAllAnimations();
            if (this.locationEngine != null) {
                this.locationEngine.removeLocationUpdates(this.currentLocationEngineListener);
            }
            this.mapboxMap.removeOnCameraMoveListener(this.onCameraMoveListener);
            this.mapboxMap.removeOnCameraIdleListener(this.onCameraIdleListener);
        }
    }

    private void initialize(@NonNull Context context, @NonNull Style style2, @NonNull LocationComponentOptions options2) {
        if (!this.isComponentInitialized) {
            this.isComponentInitialized = true;
            if (!style2.isFullyLoaded()) {
                throw new IllegalStateException("Style is invalid, provide the most recently loaded one.");
            }
            this.style = style2;
            this.options = options2;
            this.mapboxMap.addOnMapClickListener(this.onMapClickListener);
            this.mapboxMap.addOnMapLongClickListener(this.onMapLongClickListener);
            Style style3 = style2;
            this.locationLayerController = new LocationLayerController(this.mapboxMap, style3, new LayerSourceProvider(), new LayerFeatureProvider(), new LayerBitmapProvider(context), options2, this.renderModeChangedListener);
            this.locationCameraController = new LocationCameraController(context, this.mapboxMap, this.transform, this.cameraTrackingChangedListener, options2, this.onCameraMoveInvalidateListener);
            this.locationAnimatorCoordinator = new LocationAnimatorCoordinator(this.mapboxMap.getProjection(), MapboxAnimatorSetProvider.getInstance(), MapboxAnimatorProvider.getInstance());
            this.locationAnimatorCoordinator.setTrackingAnimationDurationMultiplier(options2.trackingAnimationDurationMultiplier());
            WindowManager windowManager = (WindowManager) context.getSystemService("window");
            SensorManager sensorManager = (SensorManager) context.getSystemService("sensor");
            if (!(windowManager == null || sensorManager == null)) {
                this.compassEngine = new LocationComponentCompassEngine(windowManager, sensorManager);
            }
            this.staleStateManager = new StaleStateManager(this.onLocationStaleListener, options2);
            updateMapWithOptions(options2);
            setRenderMode(18);
            setCameraMode(8);
            onLocationLayerStart();
        }
    }

    private void initializeLocationEngine(@NonNull Context context) {
        if (this.locationEngine != null) {
            this.locationEngine.removeLocationUpdates(this.currentLocationEngineListener);
        }
        setLocationEngine(this.internalLocationEngineProvider.getBestLocationEngine(context, false));
    }

    private void updateCompassListenerState(boolean canListen) {
        if (this.compassEngine == null) {
            return;
        }
        if (!canListen) {
            removeCompassListener(this.compassEngine);
        } else if (this.isComponentInitialized && this.isComponentStarted && this.isEnabled) {
            if (!this.locationCameraController.isConsumingCompass() && !this.locationLayerController.isConsumingCompass()) {
                removeCompassListener(this.compassEngine);
            } else if (!this.isListeningToCompass) {
                this.isListeningToCompass = true;
                this.compassEngine.addCompassListener(this.compassListener);
            }
        }
    }

    private void removeCompassListener(@NonNull CompassEngine engine) {
        if (this.isListeningToCompass) {
            this.isListeningToCompass = false;
            engine.removeCompassListener(this.compassListener);
        }
    }

    private void enableLocationComponent() {
        this.isEnabled = true;
        onLocationLayerStart();
    }

    private void disableLocationComponent() {
        this.isEnabled = false;
        this.locationLayerController.hide();
        onLocationLayerStop();
    }

    private void updateMapWithOptions(@NonNull LocationComponentOptions options2) {
        int[] padding = options2.padding();
        if (padding != null) {
            this.mapboxMap.setPadding(padding[0], padding[1], padding[2], padding[3]);
        }
    }

    /* access modifiers changed from: private */
    public void updateLocation(@Nullable Location location, boolean fromLastLocation) {
        boolean isGpsNorth;
        if (location != null) {
            if (!this.isLayerReady) {
                this.lastLocation = location;
                return;
            }
            long currentTime = SystemClock.elapsedRealtime();
            if (currentTime - this.lastUpdateTime >= this.fastestInterval) {
                this.lastUpdateTime = currentTime;
                showLocationLayerIfHidden();
                if (!fromLastLocation) {
                    this.staleStateManager.updateLatestLocationTime();
                }
                CameraPosition currentCameraPosition = this.mapboxMap.getCameraPosition();
                if (getCameraMode() == 36) {
                    isGpsNorth = true;
                } else {
                    isGpsNorth = false;
                }
                this.locationAnimatorCoordinator.feedNewLocation(location, currentCameraPosition, isGpsNorth);
                updateAccuracyRadius(location, false);
                this.lastLocation = location;
            }
        }
    }

    private void showLocationLayerIfHidden() {
        boolean isLocationLayerHidden = this.locationLayerController.isHidden();
        if (this.isEnabled && this.isComponentStarted && isLocationLayerHidden) {
            this.locationLayerController.show();
        }
    }

    /* access modifiers changed from: private */
    public void updateCompassHeading(float heading) {
        this.locationAnimatorCoordinator.feedNewCompassBearing(heading, this.mapboxMap.getCameraPosition());
    }

    @SuppressLint({"MissingPermission"})
    private void setLastLocation() {
        if (this.locationEngine != null) {
            this.locationEngine.getLastLocation(this.lastLocationEngineListener);
        } else {
            updateLocation(getLastKnownLocation(), true);
        }
    }

    private void setLastCompassHeading() {
        updateCompassHeading(this.compassEngine != null ? this.compassEngine.getLastHeading() : 0.0f);
    }

    /* access modifiers changed from: private */
    @SuppressLint({"MissingPermission"})
    public void updateLayerOffsets(boolean forceUpdate) {
        CameraPosition position = this.mapboxMap.getCameraPosition();
        if (this.lastCameraPosition == null || forceUpdate) {
            this.lastCameraPosition = position;
            this.locationLayerController.updateForegroundBearing((float) position.bearing);
            this.locationLayerController.updateForegroundOffset(position.tilt);
            updateAccuracyRadius(getLastKnownLocation(), true);
            return;
        }
        if (position.bearing != this.lastCameraPosition.bearing) {
            this.locationLayerController.updateForegroundBearing((float) position.bearing);
        }
        if (position.tilt != this.lastCameraPosition.tilt) {
            this.locationLayerController.updateForegroundOffset(position.tilt);
        }
        if (position.zoom != this.lastCameraPosition.zoom) {
            updateAccuracyRadius(getLastKnownLocation(), true);
        }
        this.lastCameraPosition = position;
    }

    private void updateAccuracyRadius(Location location, boolean noAnimation) {
        this.locationAnimatorCoordinator.feedNewAccuracyRadius(Utils.calculateZoomLevelRadius(this.mapboxMap, location), noAnimation);
    }

    /* access modifiers changed from: private */
    public void updateAnimatorListenerHolders() {
        Set<AnimatorListenerHolder> animationsValueChangeListeners = new HashSet<>();
        animationsValueChangeListeners.addAll(this.locationLayerController.getAnimationListeners());
        animationsValueChangeListeners.addAll(this.locationCameraController.getAnimationListeners());
        this.locationAnimatorCoordinator.updateAnimatorListenerHolders(animationsValueChangeListeners);
        this.locationAnimatorCoordinator.resetAllCameraAnimations(this.mapboxMap.getCameraPosition(), this.locationCameraController.getCameraMode() == 36);
        this.locationAnimatorCoordinator.resetAllLayerAnimations();
    }

    @VisibleForTesting
    static final class CurrentLocationEngineCallback implements LocationEngineCallback<LocationEngineResult> {
        private final WeakReference<LocationComponent> componentWeakReference;

        CurrentLocationEngineCallback(LocationComponent component) {
            this.componentWeakReference = new WeakReference<>(component);
        }

        public void onSuccess(LocationEngineResult result) {
            LocationComponent component = this.componentWeakReference.get();
            if (component != null) {
                component.updateLocation(result.getLastLocation(), false);
            }
        }

        public void onFailure(@NonNull Exception exception) {
            Logger.e(LocationComponent.TAG, "Failed to obtain location update", exception);
        }
    }

    @VisibleForTesting
    static final class LastLocationEngineCallback implements LocationEngineCallback<LocationEngineResult> {
        private final WeakReference<LocationComponent> componentWeakReference;

        LastLocationEngineCallback(LocationComponent component) {
            this.componentWeakReference = new WeakReference<>(component);
        }

        public void onSuccess(LocationEngineResult result) {
            LocationComponent component = this.componentWeakReference.get();
            if (component != null) {
                component.updateLocation(result.getLastLocation(), true);
            }
        }

        public void onFailure(@NonNull Exception exception) {
            Logger.e(LocationComponent.TAG, "Failed to obtain last location update", exception);
        }
    }

    static class InternalLocationEngineProvider {
        InternalLocationEngineProvider() {
        }

        /* access modifiers changed from: package-private */
        public LocationEngine getBestLocationEngine(@NonNull Context context, boolean background) {
            return LocationEngineProvider.getBestLocationEngine(context, background);
        }
    }

    private void checkActivationState() {
        if (!this.isComponentInitialized) {
            throw new LocationComponentNotInitializedException();
        }
    }

    public boolean isLocationComponentActivated() {
        return this.isComponentInitialized;
    }
}
