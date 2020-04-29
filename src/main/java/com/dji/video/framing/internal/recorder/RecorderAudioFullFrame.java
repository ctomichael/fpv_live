package com.dji.video.framing.internal.recorder;

import android.location.Location;
import android.media.MediaCodec;
import android.media.MediaFormat;
import com.dji.video.framing.VideoLog;
import com.dji.video.framing.internal.audio.DJIAudioEncoder;
import com.dji.video.framing.internal.audio.DJIAudioRecordWrapper;
import com.dji.video.framing.internal.decoder.DJIVideoDecoder;
import com.dji.video.framing.internal.encoder.FullFrameHardwareTranscoder;
import com.dji.video.framing.internal.recorder.RecorderBase;
import com.dji.video.framing.utils.DJIVideoUtil;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class RecorderAudioFullFrame extends RecorderBase implements DJIAudioEncoder.DJIAudioEncoderListener, FullFrameHardwareTranscoder.FullFrameTranscoderListener, DJIAudioRecordWrapper.DJIAudioRecordListenter {
    private static final boolean DEBUG = false;
    public static String TAG = "RecorderAudioFullFrame";
    private static RecorderAudioFullFrame instance = null;
    private DJIAudioEncoder audioEncoder;
    private long audioPtsBase = -1;
    private DJIAudioRecordWrapper audioRecordWrapper;
    private int audioTrackIndex = -1;
    public String fileName;
    private long initial_original_pts = -1;
    private boolean isMuxerStarted;
    private boolean isStarted = false;
    private long lastAudioPts = -1;
    private long lastRecvAudioPts = -1;
    private long lastVideoPts = -1;
    public Location loc;
    private boolean muxerInitialized = false;
    private boolean needRecordAudio = true;
    public int orientation;
    public byte[] pps;
    private String recordDir;
    private volatile RecorderAudioState recorderAudioState = RecorderAudioState.NotInitiated;
    private Object recorderLock = new Object();
    public byte[] sps;
    private File testingFile;
    private long videoPtsBase = -1;
    private int videoTrackIndex = -1;

    public enum RecorderAudioState {
        NotInitiated,
        Standby,
        VideoTrackAdded,
        AudioTrackAdded,
        AllTracksAdded,
        Recording
    }

    public boolean needRecordAudio() {
        return this.needRecordAudio;
    }

    public void setNeedRecordAudio(boolean needRecordAudio2) {
        this.needRecordAudio = needRecordAudio2;
    }

    private void logToFile(String log) {
        log2File("audioFullFrame:" + log);
    }

    public void setRecorderAudioState(RecorderAudioState recorderAudioState2) {
        boolean isNew = recorderAudioState2 != this.recorderAudioState;
        this.recorderAudioState = recorderAudioState2;
        if (isNew) {
            log("setRecorderAudioState: " + recorderAudioState2.name());
            logToFile("setRecorderAudioState: " + recorderAudioState2.name());
        }
    }

    public RecorderAudioFullFrame initVideoInfo(String fileName2, Location location, int orientation2) {
        this.fileName = fileName2;
        this.loc = location;
        this.orientation = orientation2;
        return this;
    }

    public void setFileDir(String dir) {
        this.recordDir = dir;
    }

    public RecorderAudioFullFrame(RecorderManager manager, DJIVideoDecoder decoder, String fileName2) {
        super(manager, decoder, fileName2);
        logToFile("create instance: " + this);
    }

    private void log(String s) {
    }

    /* access modifiers changed from: protected */
    public String getTAG() {
        return TAG;
    }

    /* access modifiers changed from: protected */
    public int[] getRecordWidthHeight() {
        return FullFrameHardwareTranscoder.getInstance().getEncodeWidthHeight(getCurrentDecoder());
    }

    /* access modifiers changed from: protected */
    public void onStartRecord() {
        int i;
        if (this.recorderAudioState == RecorderAudioState.NotInitiated) {
            setRecorderAudioState(RecorderAudioState.Standby);
        }
        this.needRecordAudio = true;
        this.muxerInitialized = false;
        synchronized (this.recorderLock) {
            if (this.needRecordAudio) {
                try {
                    this.lastRecvAudioPts = -1;
                    this.audioPtsBase = -1;
                    this.audioRecordWrapper = DJIAudioRecordWrapper.getInstance();
                    this.audioEncoder = new DJIAudioEncoder();
                    this.audioEncoder.setListener(this);
                    this.audioEncoder.startAudioEncoder();
                    if (!this.audioRecordWrapper.addListener(this)) {
                        log("initMuxer: init audio failed!!!!");
                        logToFile("init audio failed!");
                    } else {
                        logToFile("start audio recorder successfully");
                    }
                } catch (IOException e) {
                    VideoLog.e(TAG, "onStartRecord: ", e, new Object[0]);
                }
            } else {
                setRecorderAudioState(RecorderAudioState.AllTracksAdded);
                this.muxer.start();
                if (this.manager.getCallback() != null && this.manager.isCurrentRecorder(this)) {
                    this.manager.getCallback().onMuxerStarted();
                }
            }
        }
        FullFrameHardwareTranscoder.getInstance().setDecoder(getCurrentDecoder());
        FullFrameHardwareTranscoder.getInstance().setBitRate((int) (10 * RecorderManager.MB));
        FullFrameHardwareTranscoder.getInstance().setKeyFrameRate(30);
        FullFrameHardwareTranscoder instance2 = FullFrameHardwareTranscoder.getInstance();
        if (DJIVideoUtil.getFPS(getCurrentDecoder()) == 60) {
            i = 2;
        } else {
            i = 1;
        }
        instance2.setFrameInterval(i);
        FullFrameHardwareTranscoder.getInstance().addListener(this);
        this.isStarted = true;
    }

    /* access modifiers changed from: protected */
    public void onEndRecord() {
        this.isStarted = false;
        setRecorderAudioState(RecorderAudioState.NotInitiated);
        FullFrameHardwareTranscoder.getInstance().removeListener(this);
        DJIAudioRecordWrapper.getInstance().removeListener(this);
        this.audioEncoder.destroy();
        this.lastAudioPts = -1;
        this.lastVideoPts = -1;
        log("onEndRecord() completion");
    }

    private void initMuxer() {
        Object valueOf;
        Object valueOf2;
        this.muxerInitialized = true;
        byte[] sps_array = FullFrameHardwareTranscoder.getInstance().getSps();
        byte[] pps_array = FullFrameHardwareTranscoder.getInstance().getPps();
        int mwidth = getCurrentDecoder().mOutPutWidth;
        int mheight = getCurrentDecoder().mOutputHeight;
        if (sps_array == null || pps_array == null || mwidth == 0 || mheight == 0) {
            StringBuilder append = new StringBuilder().append("init wrong spsLen=");
            if (sps_array == null) {
                valueOf = "null";
            } else {
                valueOf = Integer.valueOf(sps_array.length);
            }
            StringBuilder append2 = append.append(valueOf).append(", ppsLen=");
            if (pps_array == null) {
                valueOf2 = "null";
            } else {
                valueOf2 = Integer.valueOf(sps_array.length);
            }
            logToFile(append2.append(valueOf2).append(", wid=").append(mwidth).append(", hei=").append(mheight).toString());
            return;
        }
        MediaFormat format = MediaFormat.createVideoFormat(DJIVideoUtil.VIDEO_ENCODING_FORMAT[0], mwidth, mheight);
        format.setInteger("frame-rate", 30);
        format.setByteBuffer("csd-0", ByteBuffer.wrap(sps_array));
        format.setByteBuffer("csd-1", ByteBuffer.wrap(pps_array));
        log("initMuxer: add video track");
        logToFile("muxer add video track");
        this.videoTrackIndex = this.muxer.addTrack(format);
        if (this.recorderAudioState == RecorderAudioState.Standby) {
            setRecorderAudioState(this.needRecordAudio ? RecorderAudioState.VideoTrackAdded : RecorderAudioState.AllTracksAdded);
        } else if (this.recorderAudioState == RecorderAudioState.AudioTrackAdded) {
            setRecorderAudioState(RecorderAudioState.AllTracksAdded);
            this.muxer.start();
            if (this.manager.getCallback() != null && this.manager.isCurrentRecorder(this)) {
                this.manager.getCallback().onMuxerStarted();
            }
        }
        log("muxer has added a track");
    }

    public void onFrameInput(byte[] data, MediaCodec.BufferInfo info, int width, int height, boolean isKeyFrame) {
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.lang.Math.min(long, long):long}
     arg types: [int, long]
     candidates:
      ClspMth{java.lang.Math.min(double, double):double}
      ClspMth{java.lang.Math.min(float, float):float}
      ClspMth{java.lang.Math.min(int, int):int}
      ClspMth{java.lang.Math.min(long, long):long} */
    public synchronized void onFrameInput(ByteBuffer buffer, MediaCodec.BufferInfo info, int width, int height, boolean isKeyFrame) {
        if (checkIsCurrentRecorder()) {
            synchronized (this.recorderLock) {
                log("onFrameInput: state: " + this.recorderAudioState);
                logDataInput("audioFullframe frame input count=" + this.numFrameWritten, dji.midware.media.DJIVideoDecoder.connectLosedelay, RecorderBase.LogDataMode.Video);
                if (!this.muxerInitialized) {
                    initMuxer();
                    this.initial_original_pts = info.presentationTimeUs;
                }
                if (this.recorderAudioState != RecorderAudioState.NotInitiated) {
                    if (isKeyFrame) {
                        if (this.recorderAudioState == RecorderAudioState.AllTracksAdded) {
                            setRecorderAudioState(RecorderAudioState.Recording);
                        }
                    }
                    log("onFrameInput: flags: " + info.flags + ", is key frame: " + isKeyFrame + ", recording state: " + this.recorderAudioState + ", last audio pts: " + this.lastAudioPts + ", last video pts: " + this.lastVideoPts);
                    if (!this.needRecordAudio) {
                        this.muxer.writeSampleData(0, buffer, info, 1);
                        this.numFrameWritten++;
                    } else if (this.recorderAudioState == RecorderAudioState.Recording) {
                        if (this.lastVideoPts < 0) {
                            this.lastVideoPts = 0;
                            info.presentationTimeUs = 0;
                        } else {
                            info.presentationTimeUs = this.lastVideoPts + Math.min(41667L, Math.max(DJIVideoUtil.getDurationPerFrameUs(getCurrentDecoder()), (this.lastAudioPts - this.lastVideoPts) + 1));
                            if (info.presentationTimeUs == this.lastAudioPts) {
                                info.presentationTimeUs++;
                            }
                        }
                        log("onFrameInput: write video data, pts: " + info.presentationTimeUs);
                        this.lastVideoPts = info.presentationTimeUs;
                        this.muxer.writeSampleData(this.videoTrackIndex, buffer, info, 1);
                        this.numFrameWritten++;
                    }
                    VideoLog.i(TAG, String.format("muxer write a frame. num=%d, size=%d, pts=%d, flags=%s (KEY=1 END=4)", Integer.valueOf(this.numFrameWritten), Integer.valueOf(info.size), Long.valueOf(info.presentationTimeUs), Integer.valueOf(info.flags)), new Object[0]);
                }
            }
        }
    }

    public void onAudioDataRead(ByteBuffer buffer, int length) {
        if (this.audioEncoder != null) {
            buffer.position(length);
            buffer.flip();
            this.audioEncoder.encode(buffer, length);
        }
    }

    public void onVolumeRefresh(double volumn) {
    }

    public void onEncoderInit(MediaFormat format) {
        log("onEncoderInit: add audio track");
        logToFile("muxer add audio track");
        this.audioTrackIndex = this.muxer.addTrack(format);
        if (this.recorderAudioState == RecorderAudioState.Standby) {
            setRecorderAudioState(RecorderAudioState.AudioTrackAdded);
        } else if (this.recorderAudioState == RecorderAudioState.VideoTrackAdded) {
            setRecorderAudioState(RecorderAudioState.AllTracksAdded);
            this.muxer.start();
            if (this.manager.getCallback() != null && this.manager.isCurrentRecorder(this)) {
                this.manager.getCallback().onMuxerStarted();
            }
        }
    }

    public void onDataEncoded(ByteBuffer buffer, MediaCodec.BufferInfo bufferInfo, long ptsInUs) {
        if (checkIsCurrentRecorder()) {
            synchronized (this.recorderLock) {
                logDataInput("audioMp4 audio input count=" + this.audioSampleWriteCount, dji.midware.media.DJIVideoDecoder.connectLosedelay, RecorderBase.LogDataMode.Audio);
                log("onDataEncoded: state: " + this.recorderAudioState);
                this.lastRecvAudioPts = ptsInUs;
                if (this.recorderAudioState == RecorderAudioState.Recording && this.audioTrackIndex >= 0) {
                    if (this.audioPtsBase < 0) {
                        this.audioPtsBase = ptsInUs;
                    }
                    long pts = ptsInUs - this.audioPtsBase;
                    log("onDataEncoded: write audio data, pts: " + pts);
                    bufferInfo.presentationTimeUs = pts;
                    this.muxer.writeSampleData(this.audioTrackIndex, buffer, bufferInfo, pts);
                    this.lastAudioPts = pts;
                    this.audioSampleWriteCount++;
                }
            }
        }
    }

    public void onFormatChanged(MediaFormat format) {
    }
}
