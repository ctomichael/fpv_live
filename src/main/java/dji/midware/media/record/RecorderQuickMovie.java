package dji.midware.media.record;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.os.Build;
import android.util.Log;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.log.DJILogHelper;
import dji.midware.data.manager.Dpad.DpadProductManager;
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
import dji.pilot.fpv.model.IEventObjects;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Locale;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@EXClassNullAway
public class RecorderQuickMovie extends RecorderBase implements RecorderInterface {
    private static final boolean DEBUG = false;
    private static final String NORMAL_ENCOCER_SURFACE_NAME = "recorder_quick_movie_normal_encoder_surface_name";
    private static final int NORMAL_KEY_FRAME_INTERVAL = 30;
    private static final String QUICK_ENCOCER_SURFACE_NAME = "recorder_quick_movie_quick_encoder_surface_name";
    private static final int QUICK_MOVIE_DEFAULT_BITRATE = 10485760;
    private static final int QUICK_MOVIE_DRAW_INTERVAL = 5;
    private static final int QUICK_MOVIE_END_DURATION_IN_SEC = 3;
    private static final int QUICK_MOVIE_KEY_FRAME_INTERVAL = 5;
    private static final int QUICK_MOVIE_START_DURATION_IN_SEC = 3;
    public static String TAG = "RecorderQuickMovie";
    private static RecorderQuickMovie instance = null;
    /* access modifiers changed from: private */
    public QuickMovieRecorderState curQuickMovieState;
    private long initial_original_pts = -1;
    private long lastWriteCreateTime;
    private long last_original_pts = -1;
    private long last_written_pts = -1;
    /* access modifiers changed from: private */
    public final Object lock = new Object();
    private DJIMuxerInterface muxer = null;
    /* access modifiers changed from: private */
    public boolean muxerInitialized = false;
    private FullFrameHardwareTranscoder.FullFrameTranscoderListener normalEncoderListener = new FullFrameHardwareTranscoder.FullFrameTranscoderListener() {
        /* class dji.midware.media.record.RecorderQuickMovie.AnonymousClass1 */

        public void onFrameInput(ByteBuffer buffer, MediaCodec.BufferInfo info, int width, int height, boolean isKeyFrame) {
        }

        public void onFrameInput(byte[] data, MediaCodec.BufferInfo info, int width, int height, boolean isKeyFrame) {
            StreamDataObserver.getInstance(StreamDataObserver.ObservingPoint.RecorderQuickMovieNormal).onDataRecv(StreamDataObserver.ObservingContext.FrameRate, 1).onDataRecv(StreamDataObserver.ObservingContext.ByteRate, (long) data.length).onDataRecv(StreamDataObserver.ObservingContext.KeyframeNum, (info.flags & 1) > 0 ? 1 : 0).onDataRecv(StreamDataObserver.ObservingContext.Width, (long) width).onDataRecv(StreamDataObserver.ObservingContext.Height, (long) height);
            synchronized (RecorderQuickMovie.this.lock) {
                MediaLogger.d(RecorderQuickMovie.TAG, "onEncodeData: normalEncoderListener: cur state: " + RecorderQuickMovie.this.curQuickMovieState.name() + ", normal queue size: " + RecorderQuickMovie.this.normalFramesQueue.size());
                if (RecorderQuickMovie.this.curQuickMovieState == QuickMovieRecorderState.RecordingStart) {
                    if (!RecorderQuickMovie.this.muxerInitialized) {
                        RecorderQuickMovie.this.initMuxer(width, height);
                    }
                    RecorderQuickMovie.this.writeFrameToMuxer(new QuickMovieFrameInfo(data, info, width, height, isKeyFrame, RecorderQuickMovie.NORMAL_ENCOCER_SURFACE_NAME));
                    if (System.currentTimeMillis() - RecorderQuickMovie.this.startTime > 3000) {
                        DJIVideoDecoder unused = RecorderQuickMovie.this.quickDecoder = RecorderQuickMovie.this.serviceMangaer.getDecoder();
                        if (RecorderQuickMovie.this.quickDecoder != null) {
                            RecorderQuickMovie.this.setCurQuickMovieState(QuickMovieRecorderState.RecordingTransition);
                            DJIVideoHardwareEncoder unused2 = RecorderQuickMovie.this.quickEncoder = new DJIVideoHardwareEncoder();
                            RecorderQuickMovie.this.quickEncoder.start(width, height, 5, RecorderQuickMovie.QUICK_MOVIE_DEFAULT_BITRATE);
                            RecorderQuickMovie.this.quickEncoder.addListener(RecorderQuickMovie.this.quickMovieEncoderListener);
                            RecorderQuickMovie.this.quickDecoder.setAsyncRenderSurface(RecorderQuickMovie.QUICK_ENCOCER_SURFACE_NAME, RecorderQuickMovie.this.quickEncoder.getInputSurface(), width, height, 5);
                        }
                    }
                }
                if (RecorderQuickMovie.this.curQuickMovieState == QuickMovieRecorderState.RecordingTransition) {
                    QuickMovieFrameInfo frameInfo = new QuickMovieFrameInfo(data, info, width, height, isKeyFrame, RecorderQuickMovie.NORMAL_ENCOCER_SURFACE_NAME);
                    if (!RecorderQuickMovie.this.normalFramesQueue.offer(frameInfo)) {
                        RecorderQuickMovie.this.normalFramesQueue.poll();
                        RecorderQuickMovie.this.normalFramesQueue.offer(frameInfo);
                    }
                    if (System.currentTimeMillis() - RecorderQuickMovie.this.startTime >= 6000) {
                        RecorderQuickMovie.this.setCurQuickMovieState(QuickMovieRecorderState.RecordingQuickMovie);
                    }
                }
                if (RecorderQuickMovie.this.curQuickMovieState == QuickMovieRecorderState.RecordingQuickMovie) {
                    QuickMovieFrameInfo frameInfo2 = new QuickMovieFrameInfo(data, info, width, height, isKeyFrame, RecorderQuickMovie.NORMAL_ENCOCER_SURFACE_NAME);
                    if (!RecorderQuickMovie.this.normalFramesQueue.offer(frameInfo2)) {
                        RecorderQuickMovie.this.normalFramesQueue.poll();
                        RecorderQuickMovie.this.normalFramesQueue.offer(frameInfo2);
                    }
                    if (isKeyFrame) {
                        long currentTime = System.currentTimeMillis();
                        while (currentTime - ((QuickMovieFrameInfo) RecorderQuickMovie.this.normalFramesQueue.peek()).createTime > IEventObjects.PopViewItem.DURATION_DISAPPEAR) {
                            RecorderQuickMovie.this.removeGop(RecorderQuickMovie.this.normalFramesQueue);
                        }
                    }
                }
            }
        }
    };
    /* access modifiers changed from: private */
    public BlockingQueue<QuickMovieFrameInfo> normalFramesQueue = new ArrayBlockingQueue(150);
    /* access modifiers changed from: private */
    public DJIVideoDecoder quickDecoder;
    /* access modifiers changed from: private */
    public DJIVideoHardwareEncoder quickEncoder;
    /* access modifiers changed from: private */
    public BlockingQueue<QuickMovieFrameInfo> quickFramesQueue = new ArrayBlockingQueue(30);
    /* access modifiers changed from: private */
    public DJIVideoHardwareEncoder.VideoHardwareEncoderListener quickMovieEncoderListener = new DJIVideoHardwareEncoder.VideoHardwareEncoderListener() {
        /* class dji.midware.media.record.RecorderQuickMovie.AnonymousClass2 */

        public void onEncodeData(byte[] data, MediaCodec.BufferInfo bufferInfo, int width, int height, boolean isKeyFrame) {
            long j = 1;
            StreamDataObserver onDataRecv = StreamDataObserver.getInstance(StreamDataObserver.ObservingPoint.RecorderQuickMovieQuick).onDataRecv(StreamDataObserver.ObservingContext.FrameRate, 1).onDataRecv(StreamDataObserver.ObservingContext.ByteRate, (long) data.length);
            StreamDataObserver.ObservingContext observingContext = StreamDataObserver.ObservingContext.KeyframeNum;
            if ((bufferInfo.flags & 1) <= 0) {
                j = 0;
            }
            onDataRecv.onDataRecv(observingContext, j).onDataRecv(StreamDataObserver.ObservingContext.Width, (long) width).onDataRecv(StreamDataObserver.ObservingContext.Height, (long) height);
            synchronized (RecorderQuickMovie.this.lock) {
                MediaLogger.d(RecorderQuickMovie.TAG, "onEncodeData: quickMovieEncoderListener: cur state: " + RecorderQuickMovie.this.curQuickMovieState.name() + ", quick queue size: " + RecorderQuickMovie.this.quickFramesQueue.size());
                if (RecorderQuickMovie.this.curQuickMovieState == QuickMovieRecorderState.RecordingTransition) {
                    QuickMovieFrameInfo frameInfo = new QuickMovieFrameInfo(data, bufferInfo, width, height, isKeyFrame, RecorderQuickMovie.QUICK_ENCOCER_SURFACE_NAME);
                    if (!RecorderQuickMovie.this.quickFramesQueue.offer(frameInfo)) {
                        RecorderQuickMovie.this.quickFramesQueue.poll();
                        RecorderQuickMovie.this.quickFramesQueue.offer(frameInfo);
                    }
                }
                if (RecorderQuickMovie.this.curQuickMovieState == QuickMovieRecorderState.RecordingQuickMovie) {
                    QuickMovieFrameInfo frameInfo2 = new QuickMovieFrameInfo(data, bufferInfo, width, height, isKeyFrame, RecorderQuickMovie.QUICK_ENCOCER_SURFACE_NAME);
                    while (RecorderQuickMovie.this.quickFramesQueue.peek() != null && RecorderQuickMovie.this.normalFramesQueue.peek() != null && ((QuickMovieFrameInfo) RecorderQuickMovie.this.quickFramesQueue.peek()).createTime < ((QuickMovieFrameInfo) RecorderQuickMovie.this.normalFramesQueue.peek()).createTime) {
                        RecorderQuickMovie.this.writeFrameToMuxer((QuickMovieFrameInfo) RecorderQuickMovie.this.quickFramesQueue.poll());
                    }
                    if (!RecorderQuickMovie.this.quickFramesQueue.offer(frameInfo2)) {
                        RecorderQuickMovie.this.writeFrameToMuxer((QuickMovieFrameInfo) RecorderQuickMovie.this.quickFramesQueue.poll());
                        RecorderQuickMovie.this.quickFramesQueue.offer(frameInfo2);
                    }
                }
            }
        }

        public void onEncodeData(ByteBuffer data, MediaCodec.BufferInfo bufferInfo, int width, int height, boolean isKeyFrame) {
        }
    };
    /* access modifiers changed from: private */
    public ServiceManager serviceMangaer;
    /* access modifiers changed from: private */
    public long startTime = 0;

    public enum QuickMovieRecorderState {
        Standby,
        RecordingStart,
        RecordingTransition,
        RecordingQuickMovie,
        RecordingEnd
    }

    public static class QuickMovieFrameInfo {
        public long createTime;
        public byte[] data;
        public long decoderLatestPts;
        public int flags;
        public int height;
        public boolean isKeyFrame;
        public long originPts;
        public int width;

        public QuickMovieFrameInfo(byte[] data2, MediaCodec.BufferInfo bufferInfo, int width2, int height2, boolean isKeyFrame2, String inputSurfaceKey) {
            this.data = data2;
            this.flags = bufferInfo.flags;
            this.isKeyFrame = isKeyFrame2;
            this.width = width2;
            this.height = height2;
            this.originPts = bufferInfo.presentationTimeUs;
            this.decoderLatestPts = ServiceManager.getInstance().getDecoder() == null ? -1 : ServiceManager.getInstance().getDecoder().latestPTS;
            this.createTime = System.currentTimeMillis();
        }

        public void writeToMuxer(DJIMuxerInterface muxer, long pts) {
            MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
            bufferInfo.flags = this.flags;
            bufferInfo.presentationTimeUs = pts;
            bufferInfo.size = this.data.length;
            bufferInfo.offset = 0;
        }
    }

    /* access modifiers changed from: private */
    public void writeFrameToMuxer(QuickMovieFrameInfo frameInfo) {
        if (frameInfo != null) {
            this.lastWriteCreateTime = frameInfo.createTime;
            MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
            bufferInfo.flags = frameInfo.flags;
            bufferInfo.presentationTimeUs = frameInfo.originPts;
            bufferInfo.size = frameInfo.data.length;
            bufferInfo.offset = 0;
            writeFrameToMuxer(ByteBuffer.wrap(frameInfo.data), bufferInfo, frameInfo.width, frameInfo.height, frameInfo.isKeyFrame);
        }
    }

    private void writeFrameToMuxer(BlockingQueue<QuickMovieFrameInfo> frameInfoQueue) {
        while (frameInfoQueue != null && !frameInfoQueue.isEmpty()) {
            writeFrameToMuxer(frameInfoQueue.poll());
        }
    }

    /* access modifiers changed from: private */
    public void removeGop(BlockingQueue<QuickMovieFrameInfo> frameInfoQueue) {
        boolean isFirstFrame = true;
        while (frameInfoQueue != null && !frameInfoQueue.isEmpty()) {
            if (!frameInfoQueue.peek().isKeyFrame || isFirstFrame) {
                frameInfoQueue.poll();
                isFirstFrame = false;
            } else {
                return;
            }
        }
    }

    public synchronized void setCurQuickMovieState(QuickMovieRecorderState curQuickMovieState2) {
        logToFile("set state:" + curQuickMovieState2);
        this.curQuickMovieState = curQuickMovieState2;
    }

    public static synchronized RecorderQuickMovie getInstance() {
        RecorderQuickMovie recorderQuickMovie;
        synchronized (RecorderQuickMovie.class) {
            if (instance == null) {
                instance = new RecorderQuickMovie();
                DJIEventBusUtil.register(instance);
            }
            recorderQuickMovie = instance;
        }
        return recorderQuickMovie;
    }

    private void logToFile(String log) {
        log2File("Quickshot:" + log);
    }

    public static synchronized void destroy() {
        synchronized (RecorderQuickMovie.class) {
            MediaLogger.show("RecorderFullFrame will be destroyed asynchronously");
            if (instance != null) {
                instance.onDestroy();
                instance = null;
            }
        }
    }

    private RecorderQuickMovie() {
        logToFile("create instance: " + this);
    }

    private void createFile() {
        try {
            MediaLogger.show(TAG, "Android Version is: " + Build.VERSION.SDK_INT);
            this.muxer = MuxerManager.createMuxer(DpadProductManager.getInstance().isRM500() ? MuxerManager.MuxerType.NATIVE : MuxerManager.MuxerType.FFMPEG_NEW);
            this.muxer.init(VideoMetadataManager.getSourceVideoDirectory() + this.mainFileName + ".mp4");
            initExternalSdRecordingHelper();
            MediaLogger.show(TAG, "successfully created muxer");
            logToFile("create file: " + getRecordingFilePath());
        } catch (IOException e2) {
            MediaLogger.show(e2);
        }
    }

    /* access modifiers changed from: private */
    public void initMuxer(int width, int height) {
        if (this.serviceMangaer.getDecoder() == null) {
            Log.e(TAG, "failed to init muxer. decoder is null. can't get sps pps");
            return;
        }
        byte[] sps_array = FullFrameHardwareTranscoder.getInstance().getSps();
        byte[] pps_array = FullFrameHardwareTranscoder.getInstance().getPps();
        if (sps_array == null || pps_array == null || width == 0 || height == 0) {
            Log.e(TAG, "failed to init muxer. sps or pps is null. width or height is 0");
            return;
        }
        if (this.videoRecordInfo != null) {
        }
        MediaFormat format = MediaFormat.createVideoFormat(DJIVideoUtil.VIDEO_ENCODING_FORMAT[0], width, height);
        format.setInteger("frame-rate", 30);
        format.setByteBuffer("csd-0", ByteBuffer.wrap(sps_array));
        format.setByteBuffer("csd-1", ByteBuffer.wrap(pps_array));
        this.muxer.addTrack(format);
        this.muxer.start();
        Log.i(TAG, "muxer has added a track");
        this.initial_original_pts = 0;
        this.startTime = System.currentTimeMillis();
        this.muxerInitialized = true;
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
    public void onStartRecord() {
        this.serviceMangaer = ServiceManager.getInstance();
        initPTSSync();
        this.muxerInitialized = false;
        this.numFrameWritten = 0;
        this.last_written_pts = -1;
        setMainFileName();
        createFile();
        startRecordVideoInfo();
        setCurQuickMovieState(QuickMovieRecorderState.RecordingStart);
        MediaLogger.d(TAG, "onStartRecord: width: " + this.serviceMangaer.getDecoder().width + ", height: " + this.serviceMangaer.getDecoder().height);
        FullFrameHardwareTranscoder.getInstance().setBitRate((int) (10 * RecorderManager.MB));
        FullFrameHardwareTranscoder.getInstance().setKeyFrameRate(DJIVideoUtil.getFPS());
        FullFrameHardwareTranscoder.getInstance().setFrameInterval(DJIVideoUtil.getFPS() == 60 ? 2 : 1);
        FullFrameHardwareTranscoder.getInstance().addListener(this.normalEncoderListener);
    }

    /* access modifiers changed from: protected */
    public void onEndRecord() {
        DJILogHelper.getInstance().LOGD(TAG, "onEndRecord: stack: " + DJILog.getCurrentStack());
        stopEncoders();
        setCurQuickMovieState(QuickMovieRecorderState.Standby);
        endRecordVideoInfo();
        closeOrDeleteFile();
        Log.i(TAG, "onEndRecord() completion");
    }

    private void stopEncoders() {
        FullFrameHardwareTranscoder.getInstance().removeListener(this.normalEncoderListener);
        if (this.quickEncoder != null) {
            this.quickEncoder.stop();
            if (this.quickDecoder != null) {
                this.quickDecoder.setAsyncRenderSurface(QUICK_ENCOCER_SURFACE_NAME, null, 0, 0, 0);
            }
        }
        this.quickEncoder = null;
        if (this.curQuickMovieState == QuickMovieRecorderState.RecordingTransition) {
            setCurQuickMovieState(QuickMovieRecorderState.RecordingEnd);
            writeFrameToMuxer(this.normalFramesQueue);
        }
        if (this.curQuickMovieState == QuickMovieRecorderState.RecordingQuickMovie) {
            setCurQuickMovieState(QuickMovieRecorderState.RecordingEnd);
            while (this.quickFramesQueue.peek() != null && this.normalFramesQueue.peek() != null && this.quickFramesQueue.peek().createTime < this.normalFramesQueue.peek().createTime) {
                writeFrameToMuxer(this.quickFramesQueue.poll());
            }
            writeFrameToMuxer(this.normalFramesQueue);
        }
        this.normalFramesQueue.clear();
        this.quickFramesQueue.clear();
    }

    public synchronized void writeFrameToMuxer(ByteBuffer buffer, MediaCodec.BufferInfo info, int width, int height, boolean isKeyFrame) {
        long j = 0;
        synchronized (this) {
            MediaLogger.d(TAG, "writeFrameToMuxer: width: " + width + ", height: " + height + ", is key: " + isKeyFrame + ", flags: " + info.flags + ", pts: " + info.presentationTimeUs);
            logDataInput("Quickshot frame input count=" + this.numFrameWritten + " state=" + this.curQuickMovieState + " norSize=" + this.normalFramesQueue.size() + " quiSize=" + this.quickFramesQueue.size(), DJIVideoDecoder.connectLosedelay, RecorderBase.LogDataMode.Video);
            if (this.initial_original_pts == 0) {
                this.initial_original_pts = info.presentationTimeUs;
            }
            if (this.last_written_pts >= 0) {
                j = this.last_written_pts + DJIVideoUtil.getDurationPerFrameUs();
            }
            info.presentationTimeUs = j;
            this.last_written_pts = info.presentationTimeUs;
            this.muxer.writeSampleData(0, buffer, info, 1);
            this.numFrameWritten++;
            MediaLogger.i(false, TAG, String.format(Locale.US, "muxer write a frame. num=%d, size=%d, pts=%d, flags=%s (KEY=1 END=4)", Integer.valueOf(this.numFrameWritten), Integer.valueOf(info.size), Long.valueOf(info.presentationTimeUs), Integer.valueOf(info.flags)));
        }
    }

    public synchronized void setQuickMovieType(int type) {
        if (this.videoRecordInfoSetter != null) {
            this.videoRecordInfoSetter.setQuickMovieType(type);
        }
    }
}
