package dji.midware.media.record;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.os.Build;
import android.util.Log;
import com.dji.video.framing.DJIVideoCodecInnerManager;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.media.DJIVideoDecoder;
import dji.midware.media.DJIVideoHardwareEncoder;
import dji.midware.media.DJIVideoUtil;
import dji.midware.media.MediaLogger;
import dji.midware.media.metadata.VideoMetadataManager;
import dji.midware.media.muxer.DJIMuxerInterface;
import dji.midware.media.muxer.MuxerManager;
import dji.midware.media.record.RecorderBase;
import dji.midware.media.transcode.fullframe.FullFrameHardwareTranscoder;
import dji.midware.media.transcode.online.OnlineTranscoder;
import dji.midware.util.DJIEventBusUtil;
import dji.midware.util.save.StreamDataObserver;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Locale;

@EXClassNullAway
public class RecorderSpeedAdjust extends RecorderBase implements RecorderInterface, DJIVideoHardwareEncoder.VideoHardwareEncoderListener {
    private static final int BITRATE_BASE = ((int) (10 * RecorderManager.MB));
    private static final boolean DEBUG = false;
    private static final String ENCODER_SURFACE_NAME = "recorder_speed_adjust";
    private static final int KEY_FRAME_INTERVAL = 30;
    public static String TAG = "RecorderSpeedAdjust";
    private static RecorderSpeedAdjust instance = null;
    private DJIVideoDecoder decoder;
    private DJIVideoHardwareEncoder encoder;
    private int encodingInterval = 2;
    private long initialOriginalTime = -1;
    private long initial_original_pts = -1;
    private long lastInputTime = -1;
    private long last_original_pts = -1;
    private long last_written_pts = -1;
    private DJIMuxerInterface muxer = null;
    private boolean muxerInitialized = false;
    private ServiceManager serviceMangaer;

    public static synchronized RecorderSpeedAdjust getInstance() {
        RecorderSpeedAdjust recorderSpeedAdjust;
        synchronized (RecorderSpeedAdjust.class) {
            if (instance == null) {
                instance = new RecorderSpeedAdjust();
                DJIEventBusUtil.register(instance);
            }
            recorderSpeedAdjust = instance;
        }
        return recorderSpeedAdjust;
    }

    private void logToFile(String log) {
        log2File("speedAdjust:" + log);
    }

    public static synchronized void destroy() {
        synchronized (RecorderSpeedAdjust.class) {
            MediaLogger.show("RecorderSpeedAdjust will be destroyed asynchronously");
            if (instance != null) {
                instance.onDestroy();
                instance = null;
            }
        }
    }

    public int getEncodingInterval() {
        return this.encodingInterval;
    }

    public void setEncodingInterval(int encodingInterval2) {
        this.encodingInterval = encodingInterval2;
    }

    private RecorderSpeedAdjust() {
        Log.i(TAG, "An instance is created");
    }

    private void createFile() {
        try {
            MediaLogger.show(TAG, "Android Version is: " + Build.VERSION.SDK_INT);
            this.muxer = MuxerManager.createMuxer(MuxerManager.MuxerType.FFMPEG_NEW);
            this.muxer.init(VideoMetadataManager.getSourceVideoDirectory() + this.mainFileName + ".mp4");
            initExternalSdRecordingHelper();
            MediaLogger.show(TAG, "successfully created muxer");
            logToFile("create file: " + getRecordingFilePath());
        } catch (IOException e2) {
            MediaLogger.show(e2);
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
            }
            MediaLogger.show(TAG, "muxer has been closed");
        } catch (Exception e) {
            MediaLogger.show(TAG, "error when closing muxer. possibly because the beginning frames are filtered");
        }
        if (this.numFrameWritten >= 30) {
            String mp4FilePath = VideoMetadataManager.getSourceVideoDirectory() + this.mainFileName + ".mp4";
            File mp4File = new File(mp4FilePath);
            if (mp4File.exists()) {
                logToFile("end record " + getRecordingFilePath() + ", frameNum=" + this.numFrameWritten + " mp4Size=" + ((((float) mp4File.length()) / 1024.0f) / 1024.0f) + "MB");
                addToMediaLibrary(mp4FilePath);
            } else {
                logToFile("end record frameNum=" + this.numFrameWritten + ", mp4 not exist");
            }
        } else {
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
    public int[] getRecordWidthHeight() {
        return FullFrameHardwareTranscoder.getInstance().getEncodeWidthHeight(DJIVideoCodecInnerManager.getInstance().getDJIVideoDecoderInterface().getDJIVideoDecoder());
    }

    /* access modifiers changed from: protected */
    public void onStartRecord() {
        this.serviceMangaer = ServiceManager.getInstance();
        this.decoder = this.serviceMangaer.getDecoder();
        int[] encodeWidthHeight = getRecordWidthHeight();
        if (this.decoder == null || encodeWidthHeight == null) {
            logToFile("get width height failed!");
            return;
        }
        initPTSSync();
        this.muxerInitialized = false;
        this.numFrameWritten = 0;
        setMainFileName();
        createFile();
        startRecordVideoInfo();
        this.encoder = new DJIVideoHardwareEncoder();
        this.encoder.start(encodeWidthHeight[0], encodeWidthHeight[1], 30, BITRATE_BASE / this.encodingInterval);
        this.encoder.addListener(this);
        this.decoder.setAsyncRenderSurface(ENCODER_SURFACE_NAME, this.encoder.getInputSurface(), encodeWidthHeight[0], encodeWidthHeight[1], this.encodingInterval);
    }

    /* access modifiers changed from: protected */
    public void onEndRecord() {
        if (this.encoder != null) {
            this.encoder.removeListener(this);
            this.encoder.stop();
            if (this.decoder != null) {
                this.decoder.setAsyncRenderSurface(ENCODER_SURFACE_NAME, null, 0, 0, 0);
            }
        }
        this.encoder = null;
        endRecordVideoInfo();
        closeOrDeleteFile();
        new File(VideoMetadataManager.getSourceVideoDirectory() + this.mainFileName + ".mp4");
        Log.i(TAG, "onEndRecord() completion");
    }

    public synchronized void onEncodeData(ByteBuffer buffer, MediaCodec.BufferInfo info, int width, int height, boolean isKeyFrame) {
        StreamDataObserver.getInstance(StreamDataObserver.ObservingPoint.RecorderSpeedAdjustInput).onDataRecv(StreamDataObserver.ObservingContext.FrameRate, 1).onDataRecv(StreamDataObserver.ObservingContext.ByteRate, (long) info.size).onDataRecv(StreamDataObserver.ObservingContext.KeyframeNum, (info.flags & 1) > 0 ? 1 : 0).onDataRecv(StreamDataObserver.ObservingContext.Width, (long) width).onDataRecv(StreamDataObserver.ObservingContext.Height, (long) height);
        logDataInput("speed adjust frame input count=" + this.numFrameWritten, DJIVideoDecoder.connectLosedelay, RecorderBase.LogDataMode.Video);
        long time = System.currentTimeMillis();
        if (!this.muxerInitialized) {
            byte[] sps_array = this.encoder == null ? null : this.encoder.sps;
            byte[] pps_array = this.encoder == null ? null : this.encoder.pps;
            int mwidth = width;
            int mheight = height;
            if (sps_array == null || pps_array == null || mwidth == 0 || mheight == 0) {
                logToFile("spsLen=" + (sps_array == null ? "null" : Integer.valueOf(sps_array.length)) + ", ppsLen=" + (pps_array == null ? "null" : Integer.valueOf(sps_array.length)) + ", wid=" + mwidth + ", hei=" + mheight);
                Log.e(TAG, "failed to init muxer. sps or pps is null. width or height is 0");
            } else {
                if (this.videoRecordInfo != null) {
                }
                MediaFormat format = MediaFormat.createVideoFormat(DJIVideoUtil.VIDEO_ENCODING_FORMAT[0], mwidth, mheight);
                format.setInteger("frame-rate", 30);
                format.setByteBuffer("csd-0", ByteBuffer.wrap(sps_array));
                format.setByteBuffer("csd-1", ByteBuffer.wrap(pps_array));
                this.muxer.addTrack(format);
                this.muxer.start();
                this.initial_original_pts = 0;
                this.initialOriginalTime = time;
                this.lastInputTime = 0;
                this.last_written_pts = 0;
                this.muxerInitialized = true;
                logToFile("muxer inited");
            }
        }
        if (this.last_written_pts >= 0) {
            info.presentationTimeUs = this.last_written_pts + DJIVideoUtil.getDurationPerFrameUs();
        }
        this.lastInputTime = time;
        this.last_written_pts = info.presentationTimeUs;
        DJILogHelper.getInstance().LOGD(TAG, "onFrameInput: pts: " + info.presentationTimeUs);
        this.muxer.writeSampleData(0, buffer, info, 1);
        this.numFrameWritten++;
        MediaLogger.i(false, TAG, String.format(Locale.US, "muxer write a frame. num=%d, size=%d, pts=%d, flags=%s (KEY=1 END=4)", Integer.valueOf(this.numFrameWritten), Integer.valueOf(info.size), Long.valueOf(info.presentationTimeUs), Integer.valueOf(info.flags)));
    }

    public void onEncodeData(byte[] data, MediaCodec.BufferInfo info, int width, int height, boolean isKeyFrame) {
    }

    public synchronized void setQuickMovieType(int type) {
        if (this.videoRecordInfoSetter != null) {
            this.videoRecordInfoSetter.setQuickMovieType(type);
        }
    }
}
