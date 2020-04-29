package dji.sdksharedlib.hardware.abstractions.handheldcontroller;

import dji.common.handheld.ZoomState;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.P3.DataCameraSetOpticsZoomMode;
import dji.sdksharedlib.keycatalog.HandheldControllerKeys;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class Mobile2HandheldControllerAbstraction extends MobileHandheldControllerAbstraction {
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, java.lang.String):void
     arg types: [boolean, java.lang.String]
     candidates:
      dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void
      dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, java.lang.String):void */
    public void syncPushDataFromMidware() {
        super.syncPushDataFromMidware();
        notifyValueChangeForKeyPath((Object) false, HandheldControllerKeys.IS_MODE_BUTTON_BEING_PRESSED);
        if (DataCameraSetOpticsZoomMode.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataCameraSetOpticsZoomMode.getInstance());
        }
    }

    /* access modifiers changed from: protected */
    public boolean supportTriggerButton() {
        return false;
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraSetOpticsZoomMode mode) {
        if (mode.isGetted()) {
            if (mode.getZoomType() == 1) {
                if (mode.getZoomDirection() == 0) {
                    notifyValueChangeForKeyPath(ZoomState.ZOOM_IN, HandheldControllerKeys.ZOOM_STATE);
                } else if (mode.getZoomDirection() == 1) {
                    notifyValueChangeForKeyPath(ZoomState.ZOOM_OUT, HandheldControllerKeys.ZOOM_STATE);
                }
            } else if (mode.getZoomType() == 255) {
                notifyValueChangeForKeyPath(ZoomState.IDLE, HandheldControllerKeys.ZOOM_STATE);
            }
        }
    }
}
