package com.dji.component.fpv.widget.preview;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.dji.component.fpv.base.FpvConstant;
import com.dji.video.framing.DJIVideoDecoderInterface;
import com.dji.video.framing.internal.decoder.DJIVideoDecoder;
import com.dji.video.framing.internal.decoder.decoderinterface.IReceiveDataCallback;
import com.dji.video.framing.internal.opengl.surface.SurfaceInterface;
import com.dji.video.framing.utils.DJIVideoUtil;
import dji.common.camera.CameraRecordingState;
import dji.common.mission.activetrack.QuickShotMode;
import dji.component.mediaprovider.DJIMediaProviderUtils;
import dji.component.playback.model.PlaybackFileInfo;
import dji.component.playback.model.PlaybackFileType;
import dji.component.playback.model.video.PlaybackFileResolution;
import dji.component.playback.model.video.PlaybackVideoSubType;
import dji.component.playback.model.video.PlaybackVideoType;
import dji.log.DJILog;
import dji.midware.media.DJIVideoDecoder;
import dji.midware.media.MediaLogger;
import dji.midware.media.metadata.VideoRecordInfo;
import dji.midware.media.newframing.DJIVideoCodecManager;
import dji.publics.LogReport.helper.VideoDecoderReportHelper;
import dji.utils.Optional;
import io.reactivex.disposables.CompositeDisposable;
import java.io.File;
import java.lang.ref.WeakReference;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class DJIVideoDecoderController {
    private static final int MSG_ID_RESTART_DECODER = 1;
    public static boolean OPEN_RESTART_MECHANISM = false;
    private static final int RESTART_DELAY_MS = 5000;
    private static final String TAG = "DJIVideoDecoderController";
    /* access modifiers changed from: private */
    public Context context = null;
    private ViewHandler handler = null;
    /* access modifiers changed from: private */
    public IReceiveDataCallback mCallback = null;
    private CompositeDisposable mCompositeDisposable;
    /* access modifiers changed from: private */
    public DJIVideoCodecManager mDJIVideoCodecManager;
    /* access modifiers changed from: private */
    public DJIVideoRecordModel mDJiVideoRecordModel;
    /* access modifiers changed from: private */
    public SurfaceInterface renderManager = null;

    private static final class ViewHandler extends Handler {
        private final WeakReference<DJIVideoDecoderController> mClsRef;

        public ViewHandler(DJIVideoDecoderController cls) {
            super(Looper.getMainLooper());
            this.mClsRef = new WeakReference<>(cls);
        }

        public void handleMessage(Message msg) {
            DJIVideoDecoderController cls = this.mClsRef.get();
            if (cls != null) {
                switch (msg.what) {
                    case 1:
                        if (cls.mDJIVideoCodecManager != null) {
                            MediaLogger.show(DJIVideoDecoderController.TAG, "\n......Restarting by DJIVideoDecoderController......\n");
                            cls.mDJIVideoCodecManager.setSurface(null);
                            cls.mDJIVideoCodecManager.release();
                            DJIVideoCodecManager unused = cls.mDJIVideoCodecManager = null;
                            DJIVideoCodecManager unused2 = cls.mDJIVideoCodecManager = new DJIVideoCodecManager();
                            cls.mDJIVideoCodecManager.initDJIVideoDecoder(cls.context, cls.renderManager);
                            cls.mDJIVideoCodecManager.setVideoDecoderEventListener(VideoDecoderReportHelper.getInstance());
                            cls.setRecvDataCallBack(cls.mCallback);
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }
        }
    }

    public DJIVideoDecoderController(Context context2, SurfaceInterface renderManager2) {
        this.context = context2;
        this.renderManager = renderManager2;
        if (OPEN_RESTART_MECHANISM) {
            this.handler = new ViewHandler(this);
            EventBus.getDefault().register(this);
        }
        this.mDJIVideoCodecManager = new DJIVideoCodecManager();
        this.mDJIVideoCodecManager.initDJIVideoDecoder(context2, renderManager2);
        this.mDJIVideoCodecManager.setVideoDecoderEventListener(VideoDecoderReportHelper.getInstance());
        DJIVideoUtil.setContext(context2);
        this.mDJiVideoRecordModel = new DJIVideoRecordModel();
        this.mCompositeDisposable = new CompositeDisposable();
        this.mCompositeDisposable.add(this.mDJiVideoRecordModel.getRecordStatusObservable().subscribe(new DJIVideoDecoderController$$Lambda$0(this)));
        this.mCompositeDisposable.add(this.mDJiVideoRecordModel.getPersistenceObservable().subscribe(new DJIVideoDecoderController$$Lambda$1(this)));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$new$0$DJIVideoDecoderController(Optional isRecording) throws Exception {
        if (isRecording.get() == CameraRecordingState.RECORDING) {
            startVideoRecording();
        } else if (isRecording.get() == CameraRecordingState.NOT_RECORDING) {
            stopVideoRecording();
        }
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$new$1$DJIVideoDecoderController(Optional size) throws Exception {
        this.mDJIVideoCodecManager.setRecordingCacheSizeLimit(((Integer) size.get()).intValue());
    }

    private void startVideoRecording() {
        DJILog.d(TAG, "startVideoRecording()", new Object[0]);
        if (this.mDJiVideoRecordModel.canRecordingCache()) {
            File djiPath = new File(DJIMediaProviderUtils.getDJIVideoPath());
            if (!djiPath.exists()) {
                djiPath.mkdir();
            }
            this.mDJIVideoCodecManager.setVideoRecordFilSavedListener(new DJIVideoDecoderInterface.VideoRecordFilSavedListener() {
                /* class com.dji.component.fpv.widget.preview.DJIVideoDecoderController.AnonymousClass1 */
                private QuickShotMode quickShotMode;

                public void onRecordingFileStart(DJIVideoDecoderInterface.IVideoRecordInfo videoRecordInfo) {
                    if (DJIVideoDecoderController.this.mDJiVideoRecordModel.isQSEnable()) {
                        this.quickShotMode = FpvConstant.QuickShot.getQuickShotMode();
                    }
                }

                public void onRecordingFileSaved(DJIVideoDecoderInterface.IVideoRecordInfo videoRecordInfo) {
                    if (videoRecordInfo instanceof VideoRecordInfo) {
                        DJIMediaProviderUtils.addVideoCacheMediaFiles(DJIVideoDecoderController.this.getPlayBackFileInfo((VideoRecordInfo) videoRecordInfo, this.quickShotMode), ((VideoRecordInfo) videoRecordInfo).sourceFilePath);
                    }
                    this.quickShotMode = null;
                }

                public void onRecordingFileError(int error) {
                }
            });
            this.mDJIVideoCodecManager.startVideoRecording(djiPath.getPath());
        }
    }

    private void stopVideoRecording() {
        DJILog.d(TAG, "stopVideoRecording()", new Object[0]);
        this.mDJIVideoCodecManager.stopVideoRecording();
    }

    /* access modifiers changed from: private */
    public PlaybackFileInfo getPlayBackFileInfo(VideoRecordInfo videoRecordInfo, QuickShotMode quickShotMode) {
        PlaybackFileInfo fileInfo = new PlaybackFileInfo();
        fileInfo.fileGuid = videoRecordInfo.uuid_drone.longValue();
        fileInfo.frameRate = videoRecordInfo.fps_local.intValue();
        fileInfo.duration = videoRecordInfo.endTimeMsec.intValue() - videoRecordInfo.startTimeMsec.intValue();
        fileInfo.fileType = PlaybackFileType.find(PlaybackFileType.MP4.value());
        fileInfo.resolution = PlaybackFileResolution.Size_1280_720p;
        if (quickShotMode != null) {
            PlaybackVideoSubType subType = null;
            if (quickShotMode == QuickShotMode.CIRCLE) {
                subType = PlaybackVideoSubType.CIRCLE;
            } else if (quickShotMode == QuickShotMode.DRONIE) {
                subType = PlaybackVideoSubType.DIAGONAL;
            } else if (quickShotMode == QuickShotMode.HELIX) {
                subType = PlaybackVideoSubType.SPIRAL;
            } else if (quickShotMode == QuickShotMode.ROCKET) {
                subType = PlaybackVideoSubType.ROCKY;
            } else if (quickShotMode == QuickShotMode.BOOMERANG) {
                subType = null;
            } else if (quickShotMode == QuickShotMode.ASTEROID) {
                subType = null;
            } else if (quickShotMode == QuickShotMode.DOLLY_ZOOM) {
                subType = PlaybackVideoSubType.DOLLY_ZOOM;
            }
            if (subType != null) {
                fileInfo.videoType = PlaybackVideoType.QUICK_SHOT;
                fileInfo.subVideoType = subType;
            }
        }
        return fileInfo;
    }

    public void setRecvDataCallBack(IReceiveDataCallback callBack) {
        this.mCallback = this.mCallback;
        if (this.mDJIVideoCodecManager != null) {
            this.mDJIVideoCodecManager.setReceiverDataCallback(callBack);
        }
    }

    public DJIVideoDecoder getDecoder() {
        return this.mDJIVideoCodecManager.getDJIVideoDecoder();
    }

    public void resetToManager() {
    }

    public void setSurface(SurfaceInterface renderManager2) {
        if (this.mDJIVideoCodecManager != null) {
            this.mDJIVideoCodecManager.setSurface(renderManager2);
        }
    }

    public void setPause(boolean isPause) {
        if (this.mDJIVideoCodecManager != null) {
            this.mDJIVideoCodecManager.setPause(isPause);
        }
    }

    public void stopVideoDecoder() {
        DJILog.d(TAG, "stopVideoDecoder()", new Object[0]);
        if (this.mDJIVideoCodecManager != null) {
            stopVideoRecording();
            this.mDJIVideoCodecManager.release();
            this.mDJIVideoCodecManager = null;
        }
        if (OPEN_RESTART_MECHANISM) {
            EventBus.getDefault().unregister(this);
        }
    }

    public void destroy() {
        this.context = null;
        this.renderManager = null;
        this.mCompositeDisposable.clear();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent3MainThread(DJIVideoDecoder.VideoDecodeStatus decodeStatus) {
        MediaLogger.show(TAG, "\nReceived decodeStatus = " + decodeStatus + "\n");
        if (decodeStatus == DJIVideoDecoder.VideoDecodeStatus.Healthy) {
            this.handler.removeMessages(1);
        }
        if (decodeStatus == DJIVideoDecoder.VideoDecodeStatus.Unhealthy && !this.handler.hasMessages(1)) {
            this.handler.sendEmptyMessageDelayed(1, 5000);
        }
    }
}
