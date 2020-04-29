package dji.midware.media.newframing;

import android.content.Context;
import com.dji.video.framing.DJIVideoCodecInnerManager;
import com.dji.video.framing.DJIVideoDecoderInterface;
import com.dji.video.framing.DJIVideoHEVCFomatManager;
import com.dji.video.framing.R;
import com.dji.video.framing.VideoLog;
import com.dji.video.framing.internal.VideoFrame;
import com.dji.video.framing.internal.decoder.DJIVideoDecoder;
import com.dji.video.framing.internal.decoder.ErrorStatusManager;
import com.dji.video.framing.internal.decoder.GdrKeyFrameGenerator;
import com.dji.video.framing.internal.decoder.decoderinterface.IReceiveDataCallback;
import com.dji.video.framing.internal.opengl.surface.SurfaceInterface;
import com.dji.video.framing.internal.parser.VideoStreamParseController;
import com.dji.video.framing.internal.recorder.RecorderManager;
import com.dji.video.framing.utils.BackgroundLooper;
import dji.log.DJILog;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.manager.P3.DJIVideoEvent;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.data.model.P3.DataOldSpecialControl;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.media.metadata.VideoRecordInfoBuild;
import dji.midware.media.newframing.DJIVideoHevcControl;
import java.util.Date;
import kotlin.jvm.internal.LongCompanionObject;
import org.greenrobot.eventbus.EventBus;

public class DJIVideoCodecManager implements DJIVideoDecoderInterface {
    private static final String TAG = "DJIVideoCodecManager";
    private Context mContext;
    /* access modifiers changed from: private */
    public DJIVideoDecoder mDJIVideoDecoder;
    private ErrorStatusManager.FrameCheckerCallback mFrameCheckerCallback = new ErrorStatusManager.FrameCheckerCallback() {
        /* class dji.midware.media.newframing.DJIVideoCodecManager.AnonymousClass6 */

        public boolean supportHevcTransfer() {
            return DJIProductManager.getInstance().getType() == ProductType.WM160;
        }

        public void onErrorStatusChange(boolean isError) {
            DataOldSpecialControl.getInstance().setStreamErrorStatus(isError).start((DJIDataCallBack) null);
        }

        public boolean getErrorStatus() {
            return DataOldSpecialControl.getInstance().isStreamErrorStatus();
        }

        public boolean needCheckFrame() {
            return DJIProductManager.getInstance().getType() == ProductType.WM240 || DJIProductManager.getInstance().getType() == ProductType.WM245;
        }

        public boolean isDemandI() {
            return needCheckFrame() || (DJIProductManager.getInstance().getType() == ProductType.WM160 && DJIVideoHEVCFomatManager.getInstance().isInHevcMode());
        }
    };
    /* access modifiers changed from: private */
    public VideoStreamParseController.FrameDataOutputCallback mFrameDataOutputCallback = new VideoStreamParseController.FrameDataOutputCallback() {
        /* class dji.midware.media.newframing.DJIVideoCodecManager.AnonymousClass3 */

        public boolean onFrameOutput(VideoFrame frame) {
            if (DJIVideoCodecManager.this.mDJIVideoDecoder != null) {
                DJIVideoCodecManager.this.mDJIVideoDecoder.queueInputBuffer(frame);
            }
            return false;
        }
    };
    private DJIVideoHEVCFomatManager.HevcChangeCallback mHevcChangeCallback = new DJIVideoHEVCFomatManager.HevcChangeCallback() {
        /* class dji.midware.media.newframing.DJIVideoCodecManager.AnonymousClass2 */

        public void onHevcModeUnMatched(boolean isHevcMode) {
            VideoLog.w(DJIVideoCodecManager.TAG, "onHevcModeUnMatched() isHevcMode:" + isHevcMode, new Object[0]);
            DJIVideoHevcControl.getInstance().sendChangeVideoTransferFormatToAir();
        }

        public void onHevcModeChanged(boolean isHevcMode) {
            if (DJIVideoCodecManager.this.mDJIVideoDecoder != null) {
                DJILog.w(DJIVideoCodecManager.TAG, "onHevcModeChanged() isHevcMode:" + isHevcMode, new Object[0]);
                DJIVideoCodecManager.this.mVideoStreamParseController.setOutputCallback(null);
                DJIVideoCodecManager.this.mDJIVideoDecoder.setHevcMode(isHevcMode);
                DJIVideoCodecManager.this.mDJIVideoDecoder.setSurface(null, true);
                DJIVideoCodecManager.this.mVideoStreamParseController.setOutputCallback(DJIVideoCodecManager.this.mFrameDataOutputCallback);
            }
        }
    };
    private DJIVideoHevcControl.HevcDisposeStatusListener mHevcDisposeStatusListener = new DJIVideoHevcControl.HevcDisposeStatusListener() {
        /* class dji.midware.media.newframing.DJIVideoCodecManager.AnonymousClass1 */

        public void onHevcControlRelease() {
            if (DJIVideoCodecManager.this.mVideoStreamParseController != null) {
                DJIVideoCodecManager.this.mVideoStreamParseController.resetCheckStatus();
            }
        }
    };
    private GdrKeyFrameGenerator.KeyFrameResCallback mKeyFrameResCallback = new GdrKeyFrameGenerator.KeyFrameResCallback() {
        /* class dji.midware.media.newframing.DJIVideoCodecManager.AnonymousClass5 */

        public int getKeyFrameResID(int width, int height) {
            ProductType productType = DJIProductManager.getInstance().getType();
            switch (productType) {
                case WM160:
                    if (width == 960 && height == 720) {
                        return R.raw.iframe_960x720_wm160;
                    }
                    if (width == 1280 && height == 720) {
                        return R.raw.iframe_1280x720_wm160;
                    }
                case WM230:
                    if (width == 1280 && height == 720) {
                        return R.raw.iframe_1280x720_wm230;
                    }
                    if (width == 1280 && height == 960) {
                        return R.raw.iframe_1280x960_wm230;
                    }
            }
            DJILog.e(DJIVideoCodecManager.TAG, "getKeyFrameResID() warning not support type :" + productType + " width:" + width + " height:" + height, new Object[0]);
            if (width == 1920 && height == 1080) {
                return R.raw.dji1080gdriframe;
            }
            return R.raw.dji1080gdriframe;
        }
    };
    private RecorderManager mRecorderManager;
    private RecorderManager.RecorderManagerCallback mRecorderManagerCallback = new RecorderManager.RecorderManagerCallback() {
        /* class dji.midware.media.newframing.DJIVideoCodecManager.AnonymousClass4 */

        public void onFileCreated() {
            DJILog.d(DJIVideoCodecManager.TAG, "onFileCreated()", new Object[0]);
            VideoRecordInfoBuild unused = DJIVideoCodecManager.this.mVideoRecordInfoSetter = new VideoRecordInfoBuild(new Date(), DJIVideoCodecManager.this.mDJIVideoDecoder.getVideoWidth(), DJIVideoCodecManager.this.mDJIVideoDecoder.getVideoHeight(), null, null);
            if (DJIVideoCodecManager.this.mVideoRecordFilSavedListener != null) {
                DJIVideoCodecManager.this.mVideoRecordFilSavedListener.onRecordingFileStart(DJIVideoCodecManager.this.mVideoRecordInfoSetter.getVideoInfo());
            }
        }

        public void onMuxerStarted() {
            DJILog.d(DJIVideoCodecManager.TAG, "onMuxerStarted() ", new Object[0]);
        }

        public void onMuxerStoped() {
            DJILog.d(DJIVideoCodecManager.TAG, "onMuxerStoped() ", new Object[0]);
        }

        public void onFileCompleted(String filePath, double duration) {
            DJILog.d(DJIVideoCodecManager.TAG, "onFileCompleted() " + filePath, new Object[0]);
            if (DJIVideoCodecManager.this.mVideoRecordFilSavedListener != null) {
                DJIVideoCodecManager.this.mVideoRecordInfoSetter.setFilePath(filePath);
                DJIVideoCodecManager.this.mVideoRecordInfoSetter.setEndTimeMsec((int) duration);
                DJIVideoCodecManager.this.mVideoRecordFilSavedListener.onRecordingFileSaved(DJIVideoCodecManager.this.mVideoRecordInfoSetter.getVideoInfo());
                DJIVideoDecoderInterface.VideoRecordFilSavedListener unused = DJIVideoCodecManager.this.mVideoRecordFilSavedListener = null;
            }
        }

        public void onSpaceReleaseFinish() {
            DJILog.d(DJIVideoCodecManager.TAG, "onSpaceReleaseFinish()", new Object[0]);
        }

        public void onError(int errorCode) {
            DJILog.e(DJIVideoCodecManager.TAG, "onError() " + errorCode, new Object[0]);
            if (DJIVideoCodecManager.this.mVideoRecordFilSavedListener != null) {
                DJIVideoCodecManager.this.mVideoRecordFilSavedListener.onRecordingFileError(errorCode);
                DJIVideoDecoderInterface.VideoRecordFilSavedListener unused = DJIVideoCodecManager.this.mVideoRecordFilSavedListener = null;
            }
        }
    };
    /* access modifiers changed from: private */
    public DJIVideoDecoderInterface.VideoRecordFilSavedListener mVideoRecordFilSavedListener;
    /* access modifiers changed from: private */
    public VideoRecordInfoBuild mVideoRecordInfoSetter;
    /* access modifiers changed from: private */
    public VideoStreamParseController mVideoStreamParseController;

    public void initDJIVideoDecoder(Context context, SurfaceInterface renderManager) {
        initDJIVideoDecoder(context, false, renderManager);
    }

    public void initDJIVideoDecoder(Context context, boolean isLiveStream, SurfaceInterface renderManager) {
        DJILog.d(TAG, "initDJIVideoDecoder()", new Object[0]);
        this.mContext = context;
        DJIVideoHevcControl.getInstance().sendChangeVideoTransferFormatToAir();
        DJIVideoHEVCFomatManager.getInstance().addHevcModeChangeListener(this.mHevcChangeCallback);
        this.mDJIVideoDecoder = new DJIVideoDecoder(this.mContext, renderManager, this.mKeyFrameResCallback, this.mFrameCheckerCallback, DJIVideoHEVCFomatManager.getInstance().isInHevcMode());
        this.mVideoStreamParseController = new VideoStreamParseController(VideoStreamParseController.OutputMode.Directly, 1200, BackgroundLooper.getLooper());
        this.mVideoStreamParseController.setOutputCallback(this.mFrameDataOutputCallback);
        DJIVideoCodecInnerManager.getInstance().setDJIVideoDecoderInterface(this);
        ServiceManager.getInstance().setIsNeedPacked(true);
        this.mRecorderManager = new RecorderManager();
        this.mRecorderManager.setCallback(this.mRecorderManagerCallback);
        DJIVideoHevcControl.getInstance().setHevcDisposeStatusListener(this.mHevcDisposeStatusListener);
    }

    public void setVideoDecoderEventListener(DJIVideoDecoder.VideoDecoderEventListener listener) {
        if (this.mDJIVideoDecoder != null) {
            this.mDJIVideoDecoder.setmVideoDecoderEventListener(listener);
        }
    }

    public void setReceiverDataCallback(IReceiveDataCallback callback) {
        if (this.mDJIVideoDecoder != null) {
            this.mDJIVideoDecoder.setRecvDataCallBack(callback);
        }
    }

    public void setSurface(SurfaceInterface surfaceInterface) {
        if (this.mDJIVideoDecoder != null) {
            if (surfaceInterface != null) {
                surfaceInterface.setVideoDecoder(this.mDJIVideoDecoder);
                DJIVideoHevcControl.getInstance().sendChangeVideoTransferFormatToAir();
                DJIVideoHEVCFomatManager.getInstance().addHevcModeChangeListener(this.mHevcChangeCallback);
            }
            DJIVideoCodecInnerManager.getInstance().setDJIVideoDecoderInterface(this);
            this.mDJIVideoDecoder.setSurface(surfaceInterface);
        }
    }

    public void setPause(boolean isPause) {
        if (this.mDJIVideoDecoder != null) {
            this.mDJIVideoDecoder.setPause(isPause);
        }
    }

    public void release() {
        DJILog.d(TAG, "release()", new Object[0]);
        if (this.mDJIVideoDecoder != null) {
            this.mDJIVideoDecoder.release();
            this.mDJIVideoDecoder.setmVideoDecoderEventListener(null);
            this.mDJIVideoDecoder.setRecvDataCallBack(null);
            this.mDJIVideoDecoder = null;
            this.mVideoStreamParseController.setOutputCallback(null);
            this.mVideoStreamParseController.destroy();
        }
        DJIVideoHevcControl.getInstance().setHevcDisposeStatusListener(null);
        DJIVideoHEVCFomatManager.getInstance().removeHevcModeChangeListener();
        DJIVideoCodecInnerManager.getInstance().setDJIVideoDecoderInterface(null);
        this.mFrameDataOutputCallback = null;
        this.mRecorderManager.destroy();
    }

    public VideoStreamParseController getParseController() {
        return this.mVideoStreamParseController;
    }

    public DJIVideoDecoder getDJIVideoDecoder() {
        return this.mDJIVideoDecoder;
    }

    public Context getContext() {
        return this.mContext;
    }

    public void postStartDecodeEvent() {
        EventBus.getDefault().post(DJIVideoEvent.ConnectOK);
    }

    public void startVideoRecording(String dirPath) {
        this.mRecorderManager.setRecordDir(dirPath);
        this.mRecorderManager.start(RecorderManager.BufferMode.Transcode, this.mDJIVideoDecoder);
    }

    public void stopVideoRecording() {
        this.mRecorderManager.stop();
    }

    public void setRecordingCacheSizeLimit(int sizeLimit) {
        if (sizeLimit > 0) {
            RecorderManager.setMaxBufferSpace(((long) sizeLimit) * RecorderManager.GB);
        } else {
            RecorderManager.setMaxBufferSpace(LongCompanionObject.MAX_VALUE);
        }
    }

    public void setVideoRecordFilSavedListener(DJIVideoDecoderInterface.VideoRecordFilSavedListener videoRecordFilSavedListener) {
        this.mVideoRecordFilSavedListener = videoRecordFilSavedListener;
    }
}
