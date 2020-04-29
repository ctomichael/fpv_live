package dji.midware.media.record;

import android.location.Location;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.os.Build;
import android.util.Log;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.Dpad.DpadProductManager;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.data.model.P3.DataCameraGetAudio;
import dji.midware.media.DJIAudioEncoder;
import dji.midware.media.DJIAudioRecordWrapper;
import dji.midware.media.DJIVideoDecoder;
import dji.midware.media.DJIVideoUtil;
import dji.midware.media.MediaLogger;
import dji.midware.media.metadata.VideoMetadataManager;
import dji.midware.media.muxer.DJIMuxerInterface;
import dji.midware.media.muxer.MuxerManager;
import dji.midware.media.record.RecorderBase;
import dji.midware.media.transcode.online.OnlineTranscoder;
import dji.midware.util.DJIEventBusUtil;
import dji.midware.util.save.StreamDataObserver;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Locale;
import org.greenrobot.eventbus.EventBus;

@EXClassNullAway
public class RecorderAudioMp4 extends RecorderBase implements RecorderInterface, OnlineTranscoder.OnlineTranscoderListener, DJIAudioEncoder.DJIAudioEncoderListener {
    private static final boolean DEBUG = false;
    private static final int MAX_VIDEO_PTS_INTERVAL = 47619;
    public static String TAG = "RecorderAudioMp4";
    private static final int VIDEO_AUDIO_PTS_DIFF_THRESHOLD = 1000;
    private static RecorderAudioMp4 instance = null;
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
    private DJIMuxerInterface muxer = null;
    private boolean muxerInitialized = false;
    private boolean needRecordAudio = true;
    public int orientation;
    public byte[] pps;
    private String recordDir;
    private volatile RecorderAudioState recorderAudioState = RecorderAudioState.NotInitiated;
    private Object recorderLock = new Object();
    private ServiceManager serviceMangaer;
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
        log2File("audioMp4:" + log);
    }

    public void setRecorderAudioState(RecorderAudioState recorderAudioState2) {
        boolean isNew = recorderAudioState2 != this.recorderAudioState;
        this.recorderAudioState = recorderAudioState2;
        if (isNew) {
            log("setRecorderAudioState: " + recorderAudioState2.name());
            logToFile("setRecorderAudioState: " + recorderAudioState2.name());
            EventBus.getDefault().post(recorderAudioState2);
        }
    }

    public static synchronized RecorderAudioMp4 getInstance() {
        RecorderAudioMp4 recorderAudioMp4;
        synchronized (RecorderAudioMp4.class) {
            if (instance == null) {
                instance = new RecorderAudioMp4();
                DJIEventBusUtil.register(instance);
            }
            recorderAudioMp4 = instance;
        }
        return recorderAudioMp4;
    }

    public RecorderAudioMp4 initVideoInfo(String fileName2, Location location, int orientation2) {
        this.fileName = fileName2;
        this.loc = location;
        this.orientation = orientation2;
        return this;
    }

    public static synchronized void destroy() {
        synchronized (RecorderAudioMp4.class) {
            MediaLogger.show("RecorderAudioMp4 will be destroyed asynchronously");
            if (instance != null) {
                instance.onDestroy();
                instance = null;
            }
        }
    }

    public void setFileDir(String dir) {
        this.recordDir = dir;
    }

    private RecorderAudioMp4() {
        logToFile("create instance: " + this);
    }

    private void log(String s) {
    }

    private void createFile() {
        try {
            MediaLogger.show(TAG, "Android Version is: " + Build.VERSION.SDK_INT);
            this.muxer = MuxerManager.createMuxer(DpadProductManager.getInstance().isRM500() ? MuxerManager.MuxerType.NATIVE : MuxerManager.MuxerType.FFMPEG_NEW);
            this.muxer.init(VideoMetadataManager.getSourceVideoDirectory() + this.mainFileName + ".mp4");
            initExternalSdRecordingHelper();
            MediaLogger.show(TAG, "successfully created muxer");
            if (this.recorderAudioState == RecorderAudioState.NotInitiated) {
                setRecorderAudioState(RecorderAudioState.Standby);
            }
            logToFile("create file: " + getRecordingFilePath());
        } catch (IOException e2) {
            MediaLogger.show(e2);
        }
    }

    private void closeOrDeleteFile() {
        if (this.externalSdRecordingHelper != null) {
            this.externalSdRecordingHelper.pause();
        }
        try {
            if (this.muxer != null) {
                synchronized (this.recorderLock) {
                    this.muxer.stop();
                    this.muxer.release();
                    this.muxer = null;
                }
            }
            MediaLogger.show(TAG, "muxer has been closed");
        } catch (Exception e) {
            MediaLogger.show(TAG, "error when closing muxer. possibly because the beginning frames are filtered");
        }
        if (this.externalSdRecordingHelper != null) {
            try {
                Thread.sleep(5);
                this.externalSdRecordingHelper.resume();
                Thread.sleep(50);
            } catch (InterruptedException e2) {
                Log.e(TAG, "closeOrDeleteFile: ", e2);
            }
        }
        if (this.numFrameWritten >= 10) {
            String mp4FilePath = VideoMetadataManager.getSourceVideoDirectory() + this.mainFileName + ".mp4";
            File mp4File = new File(mp4FilePath);
            if (mp4File.exists()) {
                logToFile("end record " + getRecordingFilePath() + ", frameNum=" + this.numFrameWritten + " mp4Size=" + ((((float) mp4File.length()) / 1024.0f) / 1024.0f) + "MB");
                log("closeOrDeleteFile: num frame written: " + this.numFrameWritten + ", mp4 file: " + mp4FilePath + ", size: " + ((((float) mp4File.length()) / 1024.0f) / 1024.0f) + "MB");
                addToMediaLibrary(mp4FilePath);
            } else {
                logToFile("end record frameNum=" + this.numFrameWritten + ", mp4 not exist");
                log("closeOrDeleteFile: mp4 file not exist");
            }
        } else {
            log("closeOrDeleteFile: frame written too few: " + this.numFrameWritten);
            Log.i(OnlineTranscoder.TAG_Internal, "need to delete the related file because it has fewer frames than the threshold");
            File file = new File(VideoMetadataManager.getSourceVideoDirectory() + this.mainFileName + ".mp4");
            if (!file.exists()) {
                logToFile("end record frameNum=" + this.numFrameWritten + ", no short file.");
            } else if (file.delete()) {
                logToFile("end record frameNum=" + this.numFrameWritten + ", deleted short file successful");
                Log.i(OnlineTranscoder.TAG_Internal, "has deleted mp4 file");
            } else {
                logToFile("end record frameNum=" + this.numFrameWritten + ", deleted short file failed");
                Log.e(OnlineTranscoder.TAG_Internal, "failed to delete the short mp4 file");
            }
            File file2 = new File(VideoMetadataManager.getSourceVideoDirectory() + this.mainFileName + ".h264");
            if (file2.exists()) {
                if (file2.delete()) {
                    Log.i(OnlineTranscoder.TAG_Internal, "has deleted h264 file");
                } else {
                    Log.e(OnlineTranscoder.TAG_Internal, "failed to delete the short h264 file");
                }
            }
            File file3 = new File(VideoMetadataManager.getSourceVideoDirectory() + this.mainFileName + ".info");
            if (file3.exists()) {
                if (file3.delete()) {
                    Log.i(OnlineTranscoder.TAG_Internal, "has deleted the .info file");
                } else {
                    Log.e(OnlineTranscoder.TAG_Internal, "failed to delete the .info file");
                }
            }
        }
        stopExternalSdRecordingHelper();
    }

    /* access modifiers changed from: protected */
    public String getTAG() {
        return TAG;
    }

    /* access modifiers changed from: protected */
    public String getRecordingFilePath() {
        return VideoMetadataManager.getSourceVideoDirectory() + this.mainFileName + ".mp4";
    }

    /* access modifiers changed from: protected */
    public void onStartRecord() {
        if (!checkIsCurrentRecorder()) {
            logToFile("start not cur recorder: " + this);
            return;
        }
        this.needRecordAudio = true;
        this.serviceMangaer = ServiceManager.getInstance();
        initPTSSync();
        this.muxerInitialized = false;
        this.numFrameWritten = 0;
        setMainFileName();
        createFile();
        startRecordVideoInfo();
        synchronized (this.recorderLock) {
            if (this.needRecordAudio) {
                try {
                    this.lastRecvAudioPts = -1;
                    this.videoPtsBase = -1;
                    this.audioPtsBase = -1;
                    this.audioRecordWrapper = DJIAudioRecordWrapper.getInstance();
                    DJIAudioEncoder audioEncoder = DJIAudioEncoder.getInstance();
                    audioEncoder.setListener(this);
                    audioEncoder.startAudioEncoder();
                    if (!this.audioRecordWrapper.addListener(audioEncoder)) {
                        logToFile("cannot start recorder!");
                    } else {
                        logToFile("start recorder successfully");
                    }
                } catch (IOException e) {
                    Log.e(TAG, "onStartRecord: ", e);
                }
            } else {
                setRecorderAudioState(RecorderAudioState.AllTracksAdded);
                this.muxer.start();
            }
        }
        OnlineTranscoder.getInstance().addListener(this);
        if (DataCameraGetAudio.getInstance().isEnable()) {
            MediaLogger.show("OSMO: start to record audio locally");
            startRecordAudio();
        } else {
            MediaLogger.show("No local audio recording.");
        }
        this.isStarted = true;
    }

    private void startRecordAudio() {
    }

    /* access modifiers changed from: protected */
    public void onEndRecord() {
        this.isStarted = false;
        setRecorderAudioState(RecorderAudioState.NotInitiated);
        OnlineTranscoder.getInstance().removeListener(this);
        DJIAudioRecordWrapper.getInstance().removeListener(DJIAudioEncoder.getInstance());
        DJIAudioEncoder.getInstance().destroy();
        this.lastAudioPts = -1;
        this.lastVideoPts = -1;
        endRecordVideoInfo();
        closeOrDeleteFile();
        Log.i(TAG, "onEndRecord() completion");
    }

    private void stopRecordAudio() {
    }

    private void initMuxer() {
        DJIVideoDecoder decoder = this.serviceMangaer.getDecoder();
        if (decoder == null) {
            Log.e(TAG, "failed to init muxer. decoder is null. can't get sps pps");
            logToFile("decoder is null");
            return;
        }
        this.muxerInitialized = true;
        byte[] sps_array = decoder.sps_header;
        byte[] pps_array = decoder.pps_header;
        int mwidth = decoder.outputWidth;
        int mheight = decoder.outputHeight;
        if (sps_array == null || pps_array == null || mwidth == 0 || mheight == 0) {
            logToFile("spsLen=" + (sps_array == null ? "null" : Integer.valueOf(sps_array.length)) + ", ppsLen=" + (pps_array == null ? "null" : Integer.valueOf(sps_array.length)) + ", wid=" + mwidth + ", hei=" + mheight);
            Log.e(TAG, "failed to init muxer. sps or pps is null(sps: , pps: ). width or height is 0(width: " + mwidth + ", height: " + mheight + ")");
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
        }
        Log.i(TAG, "muxer has added a track");
    }

    public synchronized void onFrameInput(ByteBuffer buffer, MediaCodec.BufferInfo info, int indexInOriginStream, int width, int height) {
        long durationPerFrameUs;
        if (checkIsCurrentRecorder()) {
            StreamDataObserver.getInstance(StreamDataObserver.ObservingPoint.RecorderAudioMp4Input).onDataRecv(StreamDataObserver.ObservingContext.FrameRate, 1).onDataRecv(StreamDataObserver.ObservingContext.ByteRate, (long) info.size).onDataRecv(StreamDataObserver.ObservingContext.KeyframeNum, (info.flags & 1) > 0 ? 1 : 0).onDataRecv(StreamDataObserver.ObservingContext.Width, (long) width).onDataRecv(StreamDataObserver.ObservingContext.Height, (long) height);
            synchronized (this.recorderLock) {
                log("onFrameInput: state: " + this.recorderAudioState);
                logDataInput("audioMp4 frame input count=" + this.numFrameWritten, DJIVideoDecoder.connectLosedelay, RecorderBase.LogDataMode.Video);
                if (!this.muxerInitialized) {
                    initMuxer();
                    this.initial_original_pts = info.presentationTimeUs;
                    this.lastVideoPts = -1;
                }
                if (this.recorderAudioState != RecorderAudioState.NotInitiated) {
                    boolean isKeyFrame = (info.flags & 1) == 1;
                    if (isKeyFrame && this.recorderAudioState == RecorderAudioState.AllTracksAdded) {
                        setRecorderAudioState(RecorderAudioState.Recording);
                    }
                    log("onFrameInput: flags: " + info.flags + ", is key frame: " + isKeyFrame + ", recording state: " + this.recorderAudioState + ", last audio pts: " + this.lastAudioPts + ", last video pts: " + this.lastVideoPts);
                    if (!this.needRecordAudio) {
                        this.muxer.writeSampleData(0, buffer, info, 1);
                        this.numFrameWritten++;
                    } else if (this.recorderAudioState == RecorderAudioState.Recording && this.lastAudioPts >= 0) {
                        if (this.videoPtsBase < 0) {
                            this.videoPtsBase = 0;
                        }
                        if (this.lastVideoPts < 0) {
                            durationPerFrameUs = 0;
                        } else {
                            durationPerFrameUs = this.lastVideoPts + DJIVideoUtil.getDurationPerFrameUs();
                        }
                        info.presentationTimeUs = durationPerFrameUs;
                        if (info.presentationTimeUs < this.lastAudioPts - 1000) {
                            info.presentationTimeUs = Math.min(this.lastAudioPts - 1000, this.lastVideoPts + 47619);
                        }
                        syncPTS();
                        log("onFrameInput: write video data, pts: " + info.presentationTimeUs);
                        this.lastVideoPts = info.presentationTimeUs;
                        this.muxer.writeSampleData(this.videoTrackIndex, buffer, info, 1);
                        this.numFrameWritten++;
                    }
                    MediaLogger.i(false, TAG, String.format(Locale.US, "muxer write a frame. num=%d, size=%d, pts=%d, flags=%s (KEY=1 END=4)", Integer.valueOf(this.numFrameWritten), Integer.valueOf(info.size), Long.valueOf(info.presentationTimeUs), Integer.valueOf(info.flags)));
                }
            }
        }
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
        }
    }

    public void onDataEncoded(ByteBuffer buffer, MediaCodec.BufferInfo bufferInfo, long ptsInUs) {
        if (checkIsCurrentRecorder()) {
            synchronized (this.recorderLock) {
                logDataInput("audioMp4 audio input count=" + this.audioSampleWriteCount, DJIVideoDecoder.connectLosedelay, RecorderBase.LogDataMode.Audio);
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
