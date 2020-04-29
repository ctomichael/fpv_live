package com.mapbox.mapboxsdk.maps;

import com.mapbox.mapboxsdk.log.Logger;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.NativeMapView;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

class MapChangeReceiver implements NativeMapView.StateCallback {
    private static final String TAG = "Mbgl-MapChangeReceiver";
    private final List<MapView.OnCameraDidChangeListener> onCameraDidChangeListenerList = new CopyOnWriteArrayList();
    private final List<MapView.OnCameraIsChangingListener> onCameraIsChangingListenerList = new CopyOnWriteArrayList();
    private final List<MapView.OnCameraWillChangeListener> onCameraWillChangeListenerList = new CopyOnWriteArrayList();
    private final List<MapView.OnCanRemoveUnusedStyleImageListener> onCanRemoveUnusedStyleImageListenerList = new CopyOnWriteArrayList();
    private final List<MapView.OnDidBecomeIdleListener> onDidBecomeIdleListenerList = new CopyOnWriteArrayList();
    private final List<MapView.OnDidFailLoadingMapListener> onDidFailLoadingMapListenerList = new CopyOnWriteArrayList();
    private final List<MapView.OnDidFinishLoadingMapListener> onDidFinishLoadingMapListenerList = new CopyOnWriteArrayList();
    private final List<MapView.OnDidFinishLoadingStyleListener> onDidFinishLoadingStyleListenerList = new CopyOnWriteArrayList();
    private final List<MapView.OnDidFinishRenderingFrameListener> onDidFinishRenderingFrameList = new CopyOnWriteArrayList();
    private final List<MapView.OnDidFinishRenderingMapListener> onDidFinishRenderingMapListenerList = new CopyOnWriteArrayList();
    private final List<MapView.OnSourceChangedListener> onSourceChangedListenerList = new CopyOnWriteArrayList();
    private final List<MapView.OnStyleImageMissingListener> onStyleImageMissingListenerList = new CopyOnWriteArrayList();
    private final List<MapView.OnWillStartLoadingMapListener> onWillStartLoadingMapListenerList = new CopyOnWriteArrayList();
    private final List<MapView.OnWillStartRenderingFrameListener> onWillStartRenderingFrameList = new CopyOnWriteArrayList();
    private final List<MapView.OnWillStartRenderingMapListener> onWillStartRenderingMapListenerList = new CopyOnWriteArrayList();

    MapChangeReceiver() {
    }

    public void onCameraWillChange(boolean animated) {
        try {
            if (!this.onCameraWillChangeListenerList.isEmpty()) {
                for (MapView.OnCameraWillChangeListener onCameraWillChangeListener : this.onCameraWillChangeListenerList) {
                    onCameraWillChangeListener.onCameraWillChange(animated);
                }
            }
        } catch (Throwable err) {
            Logger.e(TAG, "Exception in onCameraWillChange", err);
            throw err;
        }
    }

    public void onCameraIsChanging() {
        try {
            if (!this.onCameraIsChangingListenerList.isEmpty()) {
                for (MapView.OnCameraIsChangingListener onCameraIsChangingListener : this.onCameraIsChangingListenerList) {
                    onCameraIsChangingListener.onCameraIsChanging();
                }
            }
        } catch (Throwable err) {
            Logger.e(TAG, "Exception in onCameraIsChanging", err);
            throw err;
        }
    }

    public void onCameraDidChange(boolean animated) {
        try {
            if (!this.onCameraDidChangeListenerList.isEmpty()) {
                for (MapView.OnCameraDidChangeListener onCameraDidChangeListener : this.onCameraDidChangeListenerList) {
                    onCameraDidChangeListener.onCameraDidChange(animated);
                }
            }
        } catch (Throwable err) {
            Logger.e(TAG, "Exception in onCameraDidChange", err);
            throw err;
        }
    }

    public void onWillStartLoadingMap() {
        try {
            if (!this.onWillStartLoadingMapListenerList.isEmpty()) {
                for (MapView.OnWillStartLoadingMapListener onWillStartLoadingMapListener : this.onWillStartLoadingMapListenerList) {
                    onWillStartLoadingMapListener.onWillStartLoadingMap();
                }
            }
        } catch (Throwable err) {
            Logger.e(TAG, "Exception in onWillStartLoadingMap", err);
            throw err;
        }
    }

    public void onDidFinishLoadingMap() {
        try {
            if (!this.onDidFinishLoadingMapListenerList.isEmpty()) {
                for (MapView.OnDidFinishLoadingMapListener onDidFinishLoadingMapListener : this.onDidFinishLoadingMapListenerList) {
                    onDidFinishLoadingMapListener.onDidFinishLoadingMap();
                }
            }
        } catch (Throwable err) {
            Logger.e(TAG, "Exception in onDidFinishLoadingMap", err);
            throw err;
        }
    }

    public void onDidFailLoadingMap(String error) {
        try {
            if (!this.onDidFailLoadingMapListenerList.isEmpty()) {
                for (MapView.OnDidFailLoadingMapListener onDidFailLoadingMapListener : this.onDidFailLoadingMapListenerList) {
                    onDidFailLoadingMapListener.onDidFailLoadingMap(error);
                }
            }
        } catch (Throwable err) {
            Logger.e(TAG, "Exception in onDidFailLoadingMap", err);
            throw err;
        }
    }

    public void onWillStartRenderingFrame() {
        try {
            if (!this.onWillStartRenderingFrameList.isEmpty()) {
                for (MapView.OnWillStartRenderingFrameListener listener : this.onWillStartRenderingFrameList) {
                    listener.onWillStartRenderingFrame();
                }
            }
        } catch (Throwable err) {
            Logger.e(TAG, "Exception in onWillStartRenderingFrame", err);
            throw err;
        }
    }

    public void onDidFinishRenderingFrame(boolean fully) {
        try {
            if (!this.onDidFinishRenderingFrameList.isEmpty()) {
                for (MapView.OnDidFinishRenderingFrameListener listener : this.onDidFinishRenderingFrameList) {
                    listener.onDidFinishRenderingFrame(fully);
                }
            }
        } catch (Throwable err) {
            Logger.e(TAG, "Exception in onDidFinishRenderingFrame", err);
            throw err;
        }
    }

    public void onWillStartRenderingMap() {
        try {
            if (!this.onWillStartRenderingMapListenerList.isEmpty()) {
                for (MapView.OnWillStartRenderingMapListener listener : this.onWillStartRenderingMapListenerList) {
                    listener.onWillStartRenderingMap();
                }
            }
        } catch (Throwable err) {
            Logger.e(TAG, "Exception in onWillStartRenderingMap", err);
            throw err;
        }
    }

    public void onDidFinishRenderingMap(boolean fully) {
        try {
            if (!this.onDidFinishRenderingMapListenerList.isEmpty()) {
                for (MapView.OnDidFinishRenderingMapListener listener : this.onDidFinishRenderingMapListenerList) {
                    listener.onDidFinishRenderingMap(fully);
                }
            }
        } catch (Throwable err) {
            Logger.e(TAG, "Exception in onDidFinishRenderingMap", err);
            throw err;
        }
    }

    public void onDidBecomeIdle() {
        try {
            if (!this.onDidBecomeIdleListenerList.isEmpty()) {
                for (MapView.OnDidBecomeIdleListener listener : this.onDidBecomeIdleListenerList) {
                    listener.onDidBecomeIdle();
                }
            }
        } catch (Throwable err) {
            Logger.e(TAG, "Exception in onDidBecomeIdle", err);
            throw err;
        }
    }

    public void onDidFinishLoadingStyle() {
        try {
            if (!this.onDidFinishLoadingStyleListenerList.isEmpty()) {
                for (MapView.OnDidFinishLoadingStyleListener listener : this.onDidFinishLoadingStyleListenerList) {
                    listener.onDidFinishLoadingStyle();
                }
            }
        } catch (Throwable err) {
            Logger.e(TAG, "Exception in onDidFinishLoadingStyle", err);
            throw err;
        }
    }

    public void onSourceChanged(String sourceId) {
        try {
            if (!this.onSourceChangedListenerList.isEmpty()) {
                for (MapView.OnSourceChangedListener onSourceChangedListener : this.onSourceChangedListenerList) {
                    onSourceChangedListener.onSourceChangedListener(sourceId);
                }
            }
        } catch (Throwable err) {
            Logger.e(TAG, "Exception in onSourceChanged", err);
            throw err;
        }
    }

    public void onStyleImageMissing(String imageId) {
        try {
            if (!this.onStyleImageMissingListenerList.isEmpty()) {
                for (MapView.OnStyleImageMissingListener listener : this.onStyleImageMissingListenerList) {
                    listener.onStyleImageMissing(imageId);
                }
            }
        } catch (Throwable err) {
            Logger.e(TAG, "Exception in onStyleImageMissing", err);
            throw err;
        }
    }

    public boolean onCanRemoveUnusedStyleImage(String imageId) {
        boolean canRemove = true;
        if (!this.onCanRemoveUnusedStyleImageListenerList.isEmpty()) {
            try {
                if (!this.onCanRemoveUnusedStyleImageListenerList.isEmpty()) {
                    canRemove = true;
                    for (MapView.OnCanRemoveUnusedStyleImageListener listener : this.onCanRemoveUnusedStyleImageListenerList) {
                        canRemove &= listener.onCanRemoveUnusedStyleImage(imageId);
                    }
                }
            } catch (Throwable err) {
                Logger.e(TAG, "Exception in onCanRemoveUnusedStyleImage", err);
                throw err;
            }
        }
        return canRemove;
    }

    /* access modifiers changed from: package-private */
    public void addOnCameraWillChangeListener(MapView.OnCameraWillChangeListener listener) {
        this.onCameraWillChangeListenerList.add(listener);
    }

    /* access modifiers changed from: package-private */
    public void removeOnCameraWillChangeListener(MapView.OnCameraWillChangeListener listener) {
        this.onCameraWillChangeListenerList.remove(listener);
    }

    /* access modifiers changed from: package-private */
    public void addOnCameraIsChangingListener(MapView.OnCameraIsChangingListener listener) {
        this.onCameraIsChangingListenerList.add(listener);
    }

    /* access modifiers changed from: package-private */
    public void removeOnCameraIsChangingListener(MapView.OnCameraIsChangingListener listener) {
        this.onCameraIsChangingListenerList.remove(listener);
    }

    /* access modifiers changed from: package-private */
    public void addOnCameraDidChangeListener(MapView.OnCameraDidChangeListener listener) {
        this.onCameraDidChangeListenerList.add(listener);
    }

    /* access modifiers changed from: package-private */
    public void removeOnCameraDidChangeListener(MapView.OnCameraDidChangeListener listener) {
        this.onCameraDidChangeListenerList.remove(listener);
    }

    /* access modifiers changed from: package-private */
    public void addOnWillStartLoadingMapListener(MapView.OnWillStartLoadingMapListener listener) {
        this.onWillStartLoadingMapListenerList.add(listener);
    }

    /* access modifiers changed from: package-private */
    public void removeOnWillStartLoadingMapListener(MapView.OnWillStartLoadingMapListener listener) {
        this.onWillStartLoadingMapListenerList.remove(listener);
    }

    /* access modifiers changed from: package-private */
    public void addOnDidFinishLoadingMapListener(MapView.OnDidFinishLoadingMapListener listener) {
        this.onDidFinishLoadingMapListenerList.add(listener);
    }

    /* access modifiers changed from: package-private */
    public void removeOnDidFinishLoadingMapListener(MapView.OnDidFinishLoadingMapListener listener) {
        this.onDidFinishLoadingMapListenerList.remove(listener);
    }

    /* access modifiers changed from: package-private */
    public void addOnDidFailLoadingMapListener(MapView.OnDidFailLoadingMapListener listener) {
        this.onDidFailLoadingMapListenerList.add(listener);
    }

    /* access modifiers changed from: package-private */
    public void removeOnDidFailLoadingMapListener(MapView.OnDidFailLoadingMapListener listener) {
        this.onDidFailLoadingMapListenerList.remove(listener);
    }

    /* access modifiers changed from: package-private */
    public void addOnWillStartRenderingFrameListener(MapView.OnWillStartRenderingFrameListener listener) {
        this.onWillStartRenderingFrameList.add(listener);
    }

    /* access modifiers changed from: package-private */
    public void removeOnWillStartRenderingFrameListener(MapView.OnWillStartRenderingFrameListener listener) {
        this.onWillStartRenderingFrameList.remove(listener);
    }

    /* access modifiers changed from: package-private */
    public void addOnDidFinishRenderingFrameListener(MapView.OnDidFinishRenderingFrameListener listener) {
        this.onDidFinishRenderingFrameList.add(listener);
    }

    /* access modifiers changed from: package-private */
    public void removeOnDidFinishRenderingFrameListener(MapView.OnDidFinishRenderingFrameListener listener) {
        this.onDidFinishRenderingFrameList.remove(listener);
    }

    /* access modifiers changed from: package-private */
    public void addOnWillStartRenderingMapListener(MapView.OnWillStartRenderingMapListener listener) {
        this.onWillStartRenderingMapListenerList.add(listener);
    }

    /* access modifiers changed from: package-private */
    public void removeOnWillStartRenderingMapListener(MapView.OnWillStartRenderingMapListener listener) {
        this.onWillStartRenderingMapListenerList.remove(listener);
    }

    /* access modifiers changed from: package-private */
    public void addOnDidFinishRenderingMapListener(MapView.OnDidFinishRenderingMapListener listener) {
        this.onDidFinishRenderingMapListenerList.add(listener);
    }

    /* access modifiers changed from: package-private */
    public void removeOnDidFinishRenderingMapListener(MapView.OnDidFinishRenderingMapListener listener) {
        this.onDidFinishRenderingMapListenerList.remove(listener);
    }

    /* access modifiers changed from: package-private */
    public void addOnDidBecomeIdleListener(MapView.OnDidBecomeIdleListener listener) {
        this.onDidBecomeIdleListenerList.add(listener);
    }

    /* access modifiers changed from: package-private */
    public void removeOnDidBecomeIdleListener(MapView.OnDidBecomeIdleListener listener) {
        this.onDidBecomeIdleListenerList.remove(listener);
    }

    /* access modifiers changed from: package-private */
    public void addOnDidFinishLoadingStyleListener(MapView.OnDidFinishLoadingStyleListener listener) {
        this.onDidFinishLoadingStyleListenerList.add(listener);
    }

    /* access modifiers changed from: package-private */
    public void removeOnDidFinishLoadingStyleListener(MapView.OnDidFinishLoadingStyleListener listener) {
        this.onDidFinishLoadingStyleListenerList.remove(listener);
    }

    /* access modifiers changed from: package-private */
    public void addOnSourceChangedListener(MapView.OnSourceChangedListener listener) {
        this.onSourceChangedListenerList.add(listener);
    }

    /* access modifiers changed from: package-private */
    public void removeOnSourceChangedListener(MapView.OnSourceChangedListener listener) {
        this.onSourceChangedListenerList.remove(listener);
    }

    /* access modifiers changed from: package-private */
    public void addOnStyleImageMissingListener(MapView.OnStyleImageMissingListener listener) {
        this.onStyleImageMissingListenerList.add(listener);
    }

    /* access modifiers changed from: package-private */
    public void removeOnStyleImageMissingListener(MapView.OnStyleImageMissingListener listener) {
        this.onStyleImageMissingListenerList.remove(listener);
    }

    /* access modifiers changed from: package-private */
    public void addOnCanRemoveUnusedStyleImageListener(MapView.OnCanRemoveUnusedStyleImageListener listener) {
        this.onCanRemoveUnusedStyleImageListenerList.add(listener);
    }

    /* access modifiers changed from: package-private */
    public void removeOnCanRemoveUnusedStyleImageListener(MapView.OnCanRemoveUnusedStyleImageListener listener) {
        this.onCanRemoveUnusedStyleImageListenerList.remove(listener);
    }

    /* access modifiers changed from: package-private */
    public void clear() {
        this.onCameraWillChangeListenerList.clear();
        this.onCameraIsChangingListenerList.clear();
        this.onCameraDidChangeListenerList.clear();
        this.onWillStartLoadingMapListenerList.clear();
        this.onDidFinishLoadingMapListenerList.clear();
        this.onDidFailLoadingMapListenerList.clear();
        this.onWillStartRenderingFrameList.clear();
        this.onDidFinishRenderingFrameList.clear();
        this.onWillStartRenderingMapListenerList.clear();
        this.onDidFinishRenderingMapListenerList.clear();
        this.onDidBecomeIdleListenerList.clear();
        this.onDidFinishLoadingStyleListenerList.clear();
        this.onSourceChangedListenerList.clear();
        this.onStyleImageMissingListenerList.clear();
        this.onCanRemoveUnusedStyleImageListenerList.clear();
    }
}
