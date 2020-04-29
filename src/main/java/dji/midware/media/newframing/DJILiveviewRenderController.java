package dji.midware.media.newframing;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.data.model.P3.DataCameraGetMode;
import dji.midware.data.model.P3.DataCameraGetPushShotParams;
import dji.midware.data.model.P3.DataCameraGetPushStateInfo;
import dji.midware.media.DJIVideoDecoder;
import dji.midware.util.DJIEventBusUtil;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class DJILiveviewRenderController {
    private static DJILiveviewRenderController instance;
    private boolean needDlogSetByUser = false;

    public enum DlogRenderEvent {
        Open,
        Close
    }

    public static DJILiveviewRenderController getInstance() {
        if (instance == null) {
            instance = new DJILiveviewRenderController();
        }
        return instance;
    }

    public void init() {
        DJIEventBusUtil.register(this);
    }

    public void destroy() {
        DJIEventBusUtil.unRegister(this);
        instance = null;
    }

    private DJIVideoDecoder getDecoder() {
        return ServiceManager.getInstance().getDecoder();
    }

    public boolean setDlogRenderEnable(boolean enable) {
        if (this.needDlogSetByUser == enable) {
            return false;
        }
        this.needDlogSetByUser = enable;
        return _setDlogRenderEnable(enable);
    }

    private boolean _setDlogRenderEnable(boolean enable) {
        DJIVideoDecoder decoder = getDecoder();
        if (decoder != null) {
            return decoder.enableDlogRender(enable);
        }
        return false;
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraGetPushStateInfo pushStateInfo) {
        DataCameraGetMode.MODE cameraMode = pushStateInfo.getMode();
        if (cameraMode != DataCameraGetMode.MODE.TAKEPHOTO && cameraMode != DataCameraGetMode.MODE.RECORD) {
            _setDlogRenderEnable(false);
        } else if (this.needDlogSetByUser) {
            _setDlogRenderEnable(true);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraGetPushShotParams pushShotParams) {
        if (pushShotParams.getDigitalFilter() != 23) {
            _setDlogRenderEnable(false);
        } else if (this.needDlogSetByUser) {
            _setDlogRenderEnable(true);
        }
    }
}
