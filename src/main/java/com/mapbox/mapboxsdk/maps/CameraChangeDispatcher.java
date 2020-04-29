package com.mapbox.mapboxsdk.maps;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

class CameraChangeDispatcher implements MapboxMap.OnCameraMoveStartedListener, MapboxMap.OnCameraMoveListener, MapboxMap.OnCameraMoveCanceledListener, MapboxMap.OnCameraIdleListener {
    private static final int IDLE = 3;
    private static final int MOVE = 1;
    private static final int MOVE_CANCELED = 2;
    private static final int MOVE_STARTED = 0;
    private final CameraChangeHandler handler = new CameraChangeHandler(this);
    /* access modifiers changed from: private */
    public boolean idle = true;
    private int moveStartedReason;
    private final CopyOnWriteArrayList<MapboxMap.OnCameraIdleListener> onCameraIdle = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<MapboxMap.OnCameraMoveListener> onCameraMove = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<MapboxMap.OnCameraMoveCanceledListener> onCameraMoveCanceled = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<MapboxMap.OnCameraMoveStartedListener> onCameraMoveStarted = new CopyOnWriteArrayList<>();

    CameraChangeDispatcher() {
    }

    public void onCameraMoveStarted(int reason) {
        this.moveStartedReason = reason;
        this.handler.scheduleMessage(0);
    }

    public void onCameraMove() {
        this.handler.scheduleMessage(1);
    }

    public void onCameraMoveCanceled() {
        this.handler.scheduleMessage(2);
    }

    public void onCameraIdle() {
        this.handler.scheduleMessage(3);
    }

    /* access modifiers changed from: package-private */
    public void addOnCameraIdleListener(@NonNull MapboxMap.OnCameraIdleListener listener) {
        this.onCameraIdle.add(listener);
    }

    /* access modifiers changed from: package-private */
    public void removeOnCameraIdleListener(@NonNull MapboxMap.OnCameraIdleListener listener) {
        if (this.onCameraIdle.contains(listener)) {
            this.onCameraIdle.remove(listener);
        }
    }

    /* access modifiers changed from: package-private */
    public void addOnCameraMoveCancelListener(MapboxMap.OnCameraMoveCanceledListener listener) {
        this.onCameraMoveCanceled.add(listener);
    }

    /* access modifiers changed from: package-private */
    public void removeOnCameraMoveCancelListener(MapboxMap.OnCameraMoveCanceledListener listener) {
        if (this.onCameraMoveCanceled.contains(listener)) {
            this.onCameraMoveCanceled.remove(listener);
        }
    }

    /* access modifiers changed from: package-private */
    public void addOnCameraMoveStartedListener(MapboxMap.OnCameraMoveStartedListener listener) {
        this.onCameraMoveStarted.add(listener);
    }

    /* access modifiers changed from: package-private */
    public void removeOnCameraMoveStartedListener(MapboxMap.OnCameraMoveStartedListener listener) {
        if (this.onCameraMoveStarted.contains(listener)) {
            this.onCameraMoveStarted.remove(listener);
        }
    }

    /* access modifiers changed from: package-private */
    public void addOnCameraMoveListener(MapboxMap.OnCameraMoveListener listener) {
        this.onCameraMove.add(listener);
    }

    /* access modifiers changed from: package-private */
    public void removeOnCameraMoveListener(MapboxMap.OnCameraMoveListener listener) {
        if (this.onCameraMove.contains(listener)) {
            this.onCameraMove.remove(listener);
        }
    }

    /* access modifiers changed from: private */
    public void executeOnCameraMoveStarted() {
        if (this.idle) {
            this.idle = false;
            if (!this.onCameraMoveStarted.isEmpty()) {
                Iterator<MapboxMap.OnCameraMoveStartedListener> it2 = this.onCameraMoveStarted.iterator();
                while (it2.hasNext()) {
                    it2.next().onCameraMoveStarted(this.moveStartedReason);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void executeOnCameraMove() {
        if (!this.onCameraMove.isEmpty() && !this.idle) {
            Iterator<MapboxMap.OnCameraMoveListener> it2 = this.onCameraMove.iterator();
            while (it2.hasNext()) {
                it2.next().onCameraMove();
            }
        }
    }

    /* access modifiers changed from: private */
    public void executeOnCameraMoveCancelled() {
        if (!this.onCameraMoveCanceled.isEmpty() && !this.idle) {
            Iterator<MapboxMap.OnCameraMoveCanceledListener> it2 = this.onCameraMoveCanceled.iterator();
            while (it2.hasNext()) {
                it2.next().onCameraMoveCanceled();
            }
        }
    }

    /* access modifiers changed from: private */
    public void executeOnCameraIdle() {
        if (!this.idle) {
            this.idle = true;
            if (!this.onCameraIdle.isEmpty()) {
                Iterator<MapboxMap.OnCameraIdleListener> it2 = this.onCameraIdle.iterator();
                while (it2.hasNext()) {
                    it2.next().onCameraIdle();
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void onDestroy() {
        this.handler.removeCallbacksAndMessages(null);
        this.onCameraMoveStarted.clear();
        this.onCameraMoveCanceled.clear();
        this.onCameraMove.clear();
        this.onCameraIdle.clear();
    }

    private static class CameraChangeHandler extends Handler {
        private WeakReference<CameraChangeDispatcher> dispatcherWeakReference;

        CameraChangeHandler(CameraChangeDispatcher dispatcher) {
            this.dispatcherWeakReference = new WeakReference<>(dispatcher);
        }

        public void handleMessage(@NonNull Message msg) {
            CameraChangeDispatcher dispatcher = this.dispatcherWeakReference.get();
            if (dispatcher != null) {
                switch (msg.what) {
                    case 0:
                        dispatcher.executeOnCameraMoveStarted();
                        return;
                    case 1:
                        dispatcher.executeOnCameraMove();
                        return;
                    case 2:
                        dispatcher.executeOnCameraMoveCancelled();
                        return;
                    case 3:
                        dispatcher.executeOnCameraIdle();
                        return;
                    default:
                        return;
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void scheduleMessage(int change) {
            CameraChangeDispatcher dispatcher = this.dispatcherWeakReference.get();
            if (dispatcher != null) {
                if (change == 0) {
                    boolean shouldReturn = !dispatcher.idle && (hasMessages(3) || hasMessages(2));
                    removeMessages(3);
                    removeMessages(2);
                    if (shouldReturn) {
                        return;
                    }
                }
                Message message = new Message();
                message.what = change;
                sendMessage(message);
            }
        }
    }
}
