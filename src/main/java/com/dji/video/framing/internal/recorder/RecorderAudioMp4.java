package com.dji.video.framing.internal.recorder;

import android.media.MediaCodec;
import android.media.MediaFormat;
import com.dji.video.framing.VideoLog;
import com.dji.video.framing.internal.VideoFrame;
import com.dji.video.framing.internal.audio.DJIAudioEncoder;
import com.dji.video.framing.internal.audio.DJIAudioRecordWrapper;
import com.dji.video.framing.internal.decoder.DJIVideoDecoder;
import com.dji.video.framing.internal.decoder.decoderinterface.IVideoRecordDataListener;
import com.dji.video.framing.internal.recorder.RecorderBase;
import com.dji.video.framing.utils.DJIVideoUtil;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Locale;

public class RecorderAudioMp4 extends RecorderBase implements DJIAudioEncoder.DJIAudioEncoderListener, DJIAudioRecordWrapper.DJIAudioRecordListenter, IVideoRecordDataListener {
    private static final boolean DEBUG = false;
    private static final int MAX_VIDEO_PTS_INTERVAL = 47619;
    public static String TAG = "RecorderAudioMp4";
    private static final int VIDEO_AUDIO_PTS_DIFF_THRESHOLD = 1000;
    private static RecorderAudioMp4 instance = null;
    private DJIAudioEncoder audioEncoder;
    private long audioPtsBase = -1;
    private DJIAudioRecordWrapper audioRecordWrapper;
    private int audioTrackIndex = -1;
    private long initial_original_pts = -1;
    private boolean isMuxerStarted;
    private boolean isStarted = false;
    private long lastAudioPts = -1;
    private long lastRecvAudioPts = -1;
    private long lastVideoPts = -1;
    private boolean muxerInitialized = false;
    private boolean needRecordAudio = true;
    private String recordDir;
    private volatile RecorderAudioState recorderAudioState = RecorderAudioState.NotInitiated;
    private Object recorderLock = new Object();
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
        log2File("audioMp4:" + log);
    }

    public void setRecorderAudioState(RecorderAudioState recorderAudioState2) {
        boolean isNew = recorderAudioState2 != this.recorderAudioState;
        this.recorderAudioState = recorderAudioState2;
        if (isNew) {
            log("setRecorderAudioState: " + recorderAudioState2.name());
            logToFile("setRecorderAudioState: " + recorderAudioState2.name());
        }
    }

    public void setFileDir(String dir) {
        this.recordDir = dir;
    }

    public RecorderAudioMp4(RecorderManager manager, DJIVideoDecoder decoder, String fileName) {
        super(manager, decoder, fileName);
        logToFile("create instance: " + this);
    }

    private void log(String s) {
    }

    /* access modifiers changed from: protected */
    public String getTAG() {
        return TAG;
    }

    /* access modifiers changed from: protected */
    public void onStartRecord() {
        if (this.recorderAudioState == RecorderAudioState.NotInitiated) {
            setRecorderAudioState(RecorderAudioState.Standby);
        }
        this.needRecordAudio = true;
        this.muxerInitialized = false;
        this.numFrameWritten = 0;
        synchronized (this.recorderLock) {
            if (this.needRecordAudio) {
                try {
                    this.lastRecvAudioPts = -1;
                    this.videoPtsBase = -1;
                    this.audioPtsBase = -1;
                    this.audioRecordWrapper = DJIAudioRecordWrapper.getInstance();
                    this.audioEncoder = new DJIAudioEncoder();
                    this.audioEncoder.setListener(this);
                    this.audioEncoder.startAudioEncoder();
                    if (!this.audioRecordWrapper.addListener(this)) {
                        logToFile("cannot start recorder!");
                    } else {
                        logToFile("start recorder successfully");
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
        getCurrentDecoder().addRecordDataListener(this);
        this.isStarted = true;
    }

    /* access modifiers changed from: protected */
    public void onEndRecord() {
        this.isStarted = false;
        setRecorderAudioState(RecorderAudioState.NotInitiated);
        getCurrentDecoder().removeRecordDataListener(this);
        DJIAudioRecordWrapper.getInstance().removeListener(this);
        this.audioEncoder.destroy();
        this.lastAudioPts = -1;
        this.lastVideoPts = -1;
        VideoLog.i(TAG, "onEndRecord() completion", new Object[0]);
    }

    private void initMuxer() {
        DJIVideoDecoder decoder = getCurrentDecoder();
        if (decoder == null) {
            VideoLog.e(TAG, "failed to init muxer. decoder is null. can't get sps pps", new Object[0]);
            logToFile("decoder is null");
            return;
        }
        this.muxerInitialized = true;
        byte[] sps_array = decoder.mSpsHeader;
        byte[] pps_array = decoder.mPpsHeader;
        int mwidth = decoder.mOutPutWidth;
        int mheight = decoder.mOutputHeight;
        if (sps_array == null || pps_array == null || mwidth == 0 || mheight == 0) {
            logToFile("spsLen=" + (sps_array == null ? "null" : Integer.valueOf(sps_array.length)) + ", ppsLen=" + (pps_array == null ? "null" : Integer.valueOf(sps_array.length)) + ", wid=" + mwidth + ", hei=" + mheight);
            VideoLog.e(TAG, "failed to init muxer. sps or pps is null(sps: , pps: ). width or height is 0(width: " + mwidth + ", height: " + mheight + ")", new Object[0]);
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
        VideoLog.i(TAG, "muxer has added a track", new Object[0]);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.lang.Math.min(long, long):long}
     arg types: [int, long]
     candidates:
      ClspMth{java.lang.Math.min(double, double):double}
      ClspMth{java.lang.Math.min(float, float):float}
      ClspMth{java.lang.Math.min(int, int):int}
      ClspMth{java.lang.Math.min(long, long):long} */
    public void onVideoFrameInput(VideoFrame frame) {
        if (checkIsCurrentRecorder()) {
            synchronized (this.recorderLock) {
                log("onFrameInput: state: " + this.recorderAudioState);
                logDataInput("audioMp4 frame input count=" + this.numFrameWritten, dji.midware.media.DJIVideoDecoder.connectLosedelay, RecorderBase.LogDataMode.Video);
                if (!this.muxerInitialized) {
                    initMuxer();
                    this.initial_original_pts = 0;
                    this.lastVideoPts = -1;
                }
                if (this.recorderAudioState != RecorderAudioState.NotInitiated) {
                    if (frame.isKeyFrame && this.recorderAudioState == RecorderAudioState.AllTracksAdded) {
                        setRecorderAudioState(RecorderAudioState.Recording);
                    }
                    ByteBuffer buffer = ByteBuffer.wrap(frame.data);
                    MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
                    info.offset = 0;
                    info.size = frame.size;
                    info.flags = frame.isKeyFrame ? 1 : 0;
                    log("onFrameInput: flags: " + info.flags + ", is key frame: " + frame.isKeyFrame + ", recording state: " + this.recorderAudioState + ", last audio pts: " + this.lastAudioPts + ", last video pts: " + this.lastVideoPts);
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
                    VideoLog.i(TAG, String.format(Locale.US, "muxer write a frame. num=%d, size=%d, pts=%d, flags=%s (KEY=1 END=4)", Integer.valueOf(this.numFrameWritten), Integer.valueOf(info.size), Long.valueOf(info.presentationTimeUs), Integer.valueOf(info.flags)), new Object[0]);
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
        this.audioSampleWriteCount = 0;
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
