package com.dji.video.framing.internal.recorder;

import android.media.MediaCodec;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.provider.DocumentFile;
import com.dji.video.framing.VideoLog;
import com.dji.video.framing.internal.decoder.DJIVideoDecoder;
import com.dji.video.framing.internal.muxer.DJIMuxerInterface;
import com.dji.video.framing.internal.muxer.FFMpegNewMuxer;
import com.dji.video.framing.internal.recorder.externalsd.ExternalSdRecordingHelper;
import com.dji.video.framing.utils.BackgroundLooper;
import com.dji.video.framing.utils.DJIVideoUtil;
import dji.component.accountcenter.IMemberProtocol;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public abstract class RecorderBase {
    protected static final float BUFFER_EXPAND_MULTIPLIER = 1.15f;
    private static final int CHECK_DECODER_INTERVAL = 500;
    private static final int CHECK_DECODER_RETRY_NUM = 20;
    protected static final int MAX_FRAME_DURATION = 41667;
    private static final int MIN_INTERVAL_TO_DECODER_INIT = 2000;
    private static final int MSG_CHECK_SPACE = 2;
    private static final int MSG_DESTROY = 3;
    private static final int MSG_START_RECORD = 0;
    private static final int MSG_STOP_RECORD = 1;
    private static final String TAG = "RecorderBase";
    protected long audioSampleWriteCount = 0;
    /* access modifiers changed from: private */
    public int checkDecoderNum = 0;
    DJIVideoDecoder currentDecoder;
    protected RecorderStatus currentStatus = RecorderStatus.Standby;
    protected ExternalSdRecordingHelper externalSdRecordingHelper;
    private long[] lastLogDataTimeArr = new long[LogDataMode.values().length];
    protected String mainFileName = "";
    RecorderManager manager;
    protected DJIMuxerInterface muxer = null;
    public int numFrameWritten;
    protected DocumentFile recordDirDf;
    protected Object recordStatusSwitchSync = new Object();
    private Handler stateHandler = new Handler(BackgroundLooper.getLooper()) {
        /* class com.dji.video.framing.internal.recorder.RecorderBase.AnonymousClass1 */

        public void handleMessage(Message msg) {
            VideoLog.d(RecorderBase.TAG, "handleMessage: what=" + msg.what + " status=" + RecorderBase.this.currentStatus, new Object[0]);
            switch (msg.what) {
                case 0:
                    VideoLog.d(RecorderBase.TAG, "handleMessage: decoder state:" + RecorderBase.this.getCurrentDecoder().getDecoderState(), new Object[0]);
                    removeMessages(0);
                    if (RecorderBase.this.getCurrentDecoder() != null && RecorderBase.this.getCurrentDecoder().isDecoderOK()) {
                        int unused = RecorderBase.this.checkDecoderNum = 0;
                        if (RecorderBase.this.currentStatus != RecorderStatus.Standby) {
                            RecorderBase.this._stopRecord();
                            RecorderBase.this._startRecord();
                            RecorderBase.this.setNewStatus(RecorderStatus.Recording);
                            return;
                        } else if (RecorderBase.this.manager.checkAndReleaseBuffer()) {
                            RecorderBase.this._startRecord();
                            RecorderBase.this.setNewStatus(RecorderStatus.Recording);
                            sendEmptyMessageDelayed(2, (long) RecorderManager.storageSpaceCheckDuration);
                            return;
                        } else if (RecorderBase.this.manager.getCallback() != null && RecorderBase.this.manager.isCurrentRecorder(RecorderBase.this)) {
                            RecorderBase.this.manager.getCallback().onError(0);
                            return;
                        } else {
                            return;
                        }
                    } else if (RecorderBase.this.checkDecoderNum < 20) {
                        sendEmptyMessageDelayed(0, 500);
                        return;
                    } else {
                        return;
                    }
                case 1:
                    removeMessages(0);
                    if (RecorderBase.this.currentStatus == RecorderStatus.Recording) {
                        RecorderBase.this._stopRecord();
                        RecorderBase.this.setNewStatus(RecorderStatus.Standby);
                        return;
                    }
                    return;
                case 2:
                    if (!RecorderBase.this.manager.checkAndReleaseBuffer()) {
                        VideoLog.d("Stop recording due to limit of storage space", new Object[0]);
                        sendEmptyMessage(1);
                        if (RecorderBase.this.manager.getCallback() != null && RecorderBase.this.manager.isCurrentRecorder(RecorderBase.this)) {
                            RecorderBase.this.manager.getCallback().onError(0);
                            return;
                        }
                        return;
                    }
                    sendEmptyMessageDelayed(2, (long) RecorderManager.storageSpaceCheckDuration);
                    return;
                case 3:
                    removeCallbacksAndMessages(null);
                    synchronized (RecorderBase.this.recordStatusSwitchSync) {
                        VideoLog.d("Status=" + RecorderBase.this.currentStatus + " event=ON_DESTROY", new Object[0]);
                        if (RecorderBase.this.currentStatus == RecorderStatus.Recording) {
                            try {
                                RecorderBase.this._stopRecord();
                            } catch (Exception e) {
                                VideoLog.d(RecorderBase.TAG, e);
                            }
                            RecorderBase.this.setNewStatus(RecorderStatus.Standby);
                        }
                    }
                    return;
                default:
                    return;
            }
        }
    };

    public enum RecorderStatus {
        Standby,
        Recording
    }

    public enum LogDataMode {
        Video,
        Audio
    }

    /* access modifiers changed from: protected */
    public abstract String getTAG();

    /* access modifiers changed from: protected */
    public abstract void onEndRecord();

    /* access modifiers changed from: protected */
    public abstract void onStartRecord();

    public static class EventVideoCacheCompletion {
        private final String filePath;

        public EventVideoCacheCompletion(String filePath2) {
            this.filePath = filePath2;
        }

        public String getFilePath() {
            return this.filePath;
        }
    }

    public int getNumFrameWritten() {
        return this.numFrameWritten;
    }

    RecorderBase(RecorderManager manager2, DJIVideoDecoder decoder, String fileName) {
        this.mainFileName = fileName;
        this.manager = manager2;
        this.currentDecoder = decoder;
    }

    public String getFileName() {
        return this.mainFileName;
    }

    public void setFileName(String mainFileName2) {
        this.mainFileName = mainFileName2;
    }

    public DJIVideoDecoder getCurrentDecoder() {
        return this.currentDecoder;
    }

    /* access modifiers changed from: package-private */
    public void startRecord() {
        this.stateHandler.sendEmptyMessage(0);
    }

    /* access modifiers changed from: package-private */
    public void stopRecord() {
        this.stateHandler.sendEmptyMessage(1);
    }

    /* access modifiers changed from: private */
    public void _startRecord() {
        if (!checkIsCurrentRecorder()) {
            log2File("start not cur recorder: " + this);
            return;
        }
        this.numFrameWritten = 0;
        createFile();
        if (this.manager.getCallback() != null && this.manager.isCurrentRecorder(this)) {
            this.manager.getCallback().onFileCreated();
        }
        onStartRecord();
    }

    /* access modifiers changed from: private */
    public void _stopRecord() {
        onEndRecord();
        closeOrDeleteFile();
        if (this.manager.getCallback() != null && this.manager.isCurrentRecorder(this)) {
            this.manager.getCallback().onFileCompleted(getRecordingFilePath(), getTotalDuration());
        }
    }

    private double getTotalDuration() {
        return ((double) this.numFrameWritten) * DJIVideoUtil.getDurationPerFrame(this.currentDecoder);
    }

    /* access modifiers changed from: protected */
    public int[] getRecordWidthHeight() {
        return new int[]{this.currentDecoder.width, this.currentDecoder.height};
    }

    public RecorderStatus getCurrentStatus() {
        return this.currentStatus;
    }

    /* access modifiers changed from: protected */
    public void setNewStatus(RecorderStatus newStatus) {
        if (this.currentStatus != newStatus) {
            this.currentStatus = newStatus;
        }
    }

    private void createFile() {
        try {
            VideoLog.d(TAG, "Android Version is: " + Build.VERSION.SDK_INT, new Object[0]);
            this.muxer = new FFMpegNewMuxer();
            this.muxer.init(new File(this.manager.getRecordDir(), this.mainFileName + ".mp4").getPath());
            initExternalSdRecordingHelper();
            VideoLog.d(TAG, "successfully created muxer", new Object[0]);
            log2File("create file: " + getRecordingFilePath());
        } catch (IOException e2) {
            VideoLog.d(TAG, e2);
        }
    }

    private void closeOrDeleteFile() {
        try {
            if (this.muxer != null) {
                MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
                info.set(0, 0, 0, 4);
                this.muxer.writeSampleData(0, ByteBuffer.allocate(10), info, 1);
                this.muxer.stop();
                this.muxer.release();
                this.muxer = null;
                if (this.manager.getCallback() != null && this.manager.isCurrentRecorder(this)) {
                    this.manager.getCallback().onMuxerStoped();
                }
            }
            VideoLog.d(TAG, "muxer has been closed", new Object[0]);
        } catch (Exception e) {
            VideoLog.d(TAG, "error when closing muxer. possibly because the beginning frames are filtered", new Object[0]);
        }
        if (this.numFrameWritten >= 10) {
            File mp4File = new File(this.manager.getRecordDir() + this.mainFileName + ".mp4");
            if (mp4File.exists()) {
                log2File("end record " + getRecordingFilePath() + ", frameNum=" + this.numFrameWritten + " mp4Size=" + ((((float) mp4File.length()) / 1024.0f) / 1024.0f) + "MB");
            } else {
                log2File("end record frameNum=" + this.numFrameWritten + ", mp4 not exist");
            }
        } else {
            VideoLog.i(TAG, "need to delete the related file because it has fewer frames than the threshold", new Object[0]);
            File file = new File(this.manager.getRecordDir() + this.mainFileName + ".mp4");
            if (!file.exists()) {
                log2File("end record frameNum=" + this.numFrameWritten + ", no short file.");
            } else if (file.delete()) {
                log2File("end record frameNum=" + this.numFrameWritten + ", deleted short file successful");
                VideoLog.i(TAG, "has deleted mp4 file", new Object[0]);
            } else {
                log2File("end record frameNum=" + this.numFrameWritten + ", deleted short file failed");
                VideoLog.e(TAG, "failed to delete the short mp4 file", new Object[0]);
            }
            File file2 = new File(this.manager.getRecordDir() + this.mainFileName + ".h264");
            if (file2.exists()) {
                if (file2.delete()) {
                    VideoLog.i(TAG, "has deleted h264 file", new Object[0]);
                } else {
                    VideoLog.e(TAG, "failed to delete the short h264 file", new Object[0]);
                }
            }
            File file3 = new File(this.manager.getRecordDir() + this.mainFileName + ".info");
            if (file3.exists()) {
                if (file3.delete()) {
                    VideoLog.i(TAG, "has deleted the .info file", new Object[0]);
                } else {
                    VideoLog.e(TAG, "failed to delete the .info file", new Object[0]);
                }
            }
        }
        stopExternalSdRecordingHelper();
    }

    /* access modifiers changed from: protected */
    public void initExternalSdRecordingHelper() {
        if (!ExternalSdRecordingHelper.getVideoCacheExternalStorageEnable() || !ExternalSdRecordingHelper.isExteranSDGranted()) {
            this.externalSdRecordingHelper = null;
            VideoLog.d("", "createFile: external sd not granted", new Object[0]);
            return;
        }
        VideoLog.d("", "createFile: external sd granted", new Object[0]);
        this.recordDirDf = ExternalSdRecordingHelper.getExternalSdRecordingDirDf();
        DocumentFile recordFileDf = ExternalSdRecordingHelper.checkAndCreateFile(this.recordDirDf, "video/mp4", this.mainFileName);
        if (recordFileDf != null) {
            this.externalSdRecordingHelper = new ExternalSdRecordingHelper(this.manager, this.manager.getRecordDir(), recordFileDf);
            this.externalSdRecordingHelper.start();
        } else if (this.manager.getCallback() != null && this.manager.isCurrentRecorder(this)) {
            this.manager.getCallback().onError(1);
        }
    }

    /* access modifiers changed from: protected */
    public String getRecordingFilePath() {
        return this.manager.getRecordDir() + IMemberProtocol.PARAM_SEPERATOR + this.mainFileName + ".mp4";
    }

    /* access modifiers changed from: protected */
    public void stopExternalSdRecordingHelper() {
        if (this.externalSdRecordingHelper != null) {
            this.externalSdRecordingHelper.stop(true);
            this.externalSdRecordingHelper = null;
        }
    }

    /* access modifiers changed from: protected */
    public boolean checkIsCurrentRecorder() {
        return this.manager.isCurrentRecorder(this);
    }

    public String getRecordingFileName() {
        String str;
        synchronized (this.recordStatusSwitchSync) {
            if (RecorderStatus.Recording == this.currentStatus) {
                str = this.mainFileName;
            } else {
                str = null;
            }
        }
        return str;
    }

    public void destroy() {
        this.stateHandler.sendEmptyMessage(3);
    }

    public boolean isRecordingToExternalSd() {
        return this.externalSdRecordingHelper != null;
    }

    public static void log2File(String log) {
        VideoLog.d("Recorder", "log2File: " + log, new Object[0]);
    }

    /* access modifiers changed from: protected */
    public void logDataInput(String log, int minInterval, LogDataMode dataMode) {
        long time = System.currentTimeMillis();
        if (time - this.lastLogDataTimeArr[dataMode.ordinal()] > ((long) minInterval)) {
            log2File(log);
            this.lastLogDataTimeArr[dataMode.ordinal()] = time;
        }
    }
}
