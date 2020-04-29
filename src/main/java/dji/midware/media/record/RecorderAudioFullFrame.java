package dji.midware.media.record;

import android.location.Location;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.os.Build;
import android.util.Log;
import com.dji.video.framing.DJIVideoCodecInnerManager;
import dji.midware.data.manager.Dpad.DpadProductManager;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.media.DJIAudioEncoder;
import dji.midware.media.DJIAudioRecordWrapper;
import dji.midware.media.DJIVideoDecoder;
import dji.midware.media.DJIVideoUtil;
import dji.midware.media.MediaLogger;
import dji.midware.media.metadata.VideoMetadataManager;
import dji.midware.media.muxer.DJIMuxerInterface;
import dji.midware.media.muxer.MuxerManager;
import dji.midware.media.record.RecorderBase;
import dji.midware.media.transcode.fullframe.FullFrameHardwareTranscoder;
import dji.midware.util.save.StreamDataObserver;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import org.greenrobot.eventbus.EventBus;

public class RecorderAudioFullFrame extends RecorderBase implements RecorderInterface, DJIAudioEncoder.DJIAudioEncoderListener, FullFrameHardwareTranscoder.FullFrameTranscoderListener {
    private static final boolean DEBUG = false;
    public static String TAG = "RecorderAudioFullFrame";
    private static RecorderAudioFullFrame instance = null;
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
        log2File("audioFullFrame:" + log);
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

    public static synchronized RecorderAudioFullFrame getInstance() {
        RecorderAudioFullFrame recorderAudioFullFrame;
        synchronized (RecorderAudioFullFrame.class) {
            if (instance == null) {
                instance = new RecorderAudioFullFrame();
                EventBus.getDefault().register(instance);
            }
            recorderAudioFullFrame = instance;
        }
        return recorderAudioFullFrame;
    }

    public RecorderAudioFullFrame initVideoInfo(String fileName2, Location location, int orientation2) {
        this.fileName = fileName2;
        this.loc = location;
        this.orientation = orientation2;
        return this;
    }

    public static synchronized void destroy() {
        synchronized (RecorderAudioFullFrame.class) {
            MediaLogger.show("RecorderAudioFullFrame will be destroyed asynchronously");
            if (instance != null) {
                instance.onDestroy();
                instance = null;
            }
        }
    }

    public void setFileDir(String dir) {
        this.recordDir = dir;
    }

    private RecorderAudioFullFrame() {
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
                log("closeOrDeleteFile: num frame written: " + this.numFrameWritten + ", mp4 file: " + mp4FilePath + ", size: " + ((((float) mp4File.length()) / 1024.0f) / 1024.0f) + "MB");
                logToFile("end record " + getRecordingFilePath() + ", frameNum=" + this.numFrameWritten + " mp4Size=" + ((((float) mp4File.length()) / 1024.0f) / 1024.0f) + "MB");
                addToMediaLibrary(mp4FilePath);
            } else {
                logToFile("end record frameNum=" + this.numFrameWritten + ", mp4 not exist");
                log("closeOrDeleteFile: mp4 file not exist");
            }
        } else {
            log("closeOrDeleteFile: frame written too few: " + this.numFrameWritten);
            File file = new File(VideoMetadataManager.getSourceVideoDirectory() + this.mainFileName + ".mp4");
            if (!file.exists()) {
                logToFile("end record frameNum=" + this.numFrameWritten + ", no short file.");
            } else if (file.delete()) {
                logToFile("end record frameNum=" + this.numFrameWritten + ", deleted short file successful");
                log("has deleted mp4 file");
            } else {
                logToFile("end record frameNum=" + this.numFrameWritten + ", deleted short file failed");
                Log.e(TAG, "failed to delete the short mp4 file");
            }
            File file2 = new File(VideoMetadataManager.getSourceVideoDirectory() + this.mainFileName + ".h264");
            if (file2.exists()) {
                if (file2.delete()) {
                    log("has deleted h264 file");
                } else {
                    Log.e(TAG, "failed to delete the short h264 file");
                }
            }
            File file3 = new File(VideoMetadataManager.getSourceVideoDirectory() + this.mainFileName + ".info");
            if (file3.exists()) {
                if (file3.delete()) {
                    log("has deleted the .info file");
                } else {
                    Log.e(TAG, "failed to delete the .info file");
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
    public int[] getRecordWidthHeight() {
        return FullFrameHardwareTranscoder.getInstance().getEncodeWidthHeight(DJIVideoCodecInnerManager.getInstance().getDJIVideoDecoderInterface().getDJIVideoDecoder());
    }

    /* access modifiers changed from: protected */
    public String getRecordingFilePath() {
        return VideoMetadataManager.getSourceVideoDirectory() + this.mainFileName + ".mp4";
    }

    /* access modifiers changed from: protected */
    public void onStartRecord() {
        int i;
        if (!checkIsCurrentRecorder()) {
            logToFile("fullframeaudio start not cur recorder: " + this);
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
                    this.audioPtsBase = -1;
                    this.audioRecordWrapper = DJIAudioRecordWrapper.getInstance();
                    DJIAudioEncoder audioEncoder = DJIAudioEncoder.getInstance();
                    audioEncoder.setListener(this);
                    audioEncoder.startAudioEncoder();
                    if (!this.audioRecordWrapper.addListener(audioEncoder)) {
                        log("initMuxer: init audio failed!!!!");
                        logToFile("init audio failed!");
                    } else {
                        logToFile("start audio recorder successfully");
                    }
                } catch (IOException e) {
                    Log.e(TAG, "onStartRecord: ", e);
                }
            } else {
                setRecorderAudioState(RecorderAudioState.AllTracksAdded);
                this.muxer.start();
            }
        }
        FullFrameHardwareTranscoder.getInstance().setBitRate((int) (10 * RecorderManager.MB));
        FullFrameHardwareTranscoder.getInstance().setKeyFrameRate(30);
        FullFrameHardwareTranscoder instance2 = FullFrameHardwareTranscoder.getInstance();
        if (DJIVideoUtil.getFPS() == 60) {
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
        DJIAudioRecordWrapper.getInstance().removeListener(DJIAudioEncoder.getInstance());
        DJIAudioEncoder.getInstance().destroy();
        this.lastAudioPts = -1;
        this.lastVideoPts = -1;
        endRecordVideoInfo();
        closeOrDeleteFile();
        Log.i(TAG, "onEndRecord() completion");
    }

    private void initMuxer() {
        Object valueOf;
        Object valueOf2;
        this.muxerInitialized = true;
        byte[] sps_array = FullFrameHardwareTranscoder.getInstance().getSps();
        byte[] pps_array = FullFrameHardwareTranscoder.getInstance().getPps();
        int mwidth = this.serviceMangaer.getDecoder().outputWidth;
        int mheight = this.serviceMangaer.getDecoder().outputHeight;
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
        }
        Log.i(TAG, "muxer has added a track");
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
            StreamDataObserver.getInstance(StreamDataObserver.ObservingPoint.RecorderAudioFullFrameInput).onDataRecv(StreamDataObserver.ObservingContext.FrameRate, 1).onDataRecv(StreamDataObserver.ObservingContext.ByteRate, (long) info.size).onDataRecv(StreamDataObserver.ObservingContext.KeyframeNum, (info.flags & 1) > 0 ? 1 : 0).onDataRecv(StreamDataObserver.ObservingContext.Width, (long) width).onDataRecv(StreamDataObserver.ObservingContext.Height, (long) height);
            synchronized (this.recorderLock) {
                log("onFrameInput: state: " + this.recorderAudioState);
                logDataInput("audioFullframe frame input count=" + this.numFrameWritten, DJIVideoDecoder.connectLosedelay, RecorderBase.LogDataMode.Video);
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
                        long currentTimeMillis = System.currentTimeMillis();
                        if (this.lastVideoPts < 0) {
                            this.lastVideoPts = 0;
                            info.presentationTimeUs = 0;
                        } else {
                            info.presentationTimeUs = this.lastVideoPts + Math.min(41667L, Math.max(DJIVideoUtil.getDurationPerFrameUs(), (this.lastAudioPts - this.lastVideoPts) + 1));
                            if (info.presentationTimeUs == this.lastAudioPts) {
                                info.presentationTimeUs++;
                            }
                        }
                        syncPTS();
                        log("onFrameInput: write video data, pts: " + info.presentationTimeUs);
                        this.lastVideoPts = info.presentationTimeUs;
                        this.muxer.writeSampleData(this.videoTrackIndex, buffer, info, 1);
                        this.numFrameWritten++;
                    }
                    MediaLogger.i(false, TAG, String.format("muxer write a frame. num=%d, size=%d, pts=%d, flags=%s (KEY=1 END=4)", Integer.valueOf(this.numFrameWritten), Integer.valueOf(info.size), Long.valueOf(info.presentationTimeUs), Integer.valueOf(info.flags)));
                }
            }
        }
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
