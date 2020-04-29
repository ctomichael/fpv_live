package dji.midware.media.record;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.media.MediaRecorder;
import android.os.Build;
import android.util.Log;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.Dpad.DpadProductManager;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.data.model.P3.DataCameraGetAudio;
import dji.midware.media.DJIVideoDecoder;
import dji.midware.media.DJIVideoUtil;
import dji.midware.media.MediaLogger;
import dji.midware.media.metadata.VideoMetadataManager;
import dji.midware.media.muxer.DJIMuxerInterface;
import dji.midware.media.muxer.MuxerManager;
import dji.midware.media.record.RecorderBase;
import dji.midware.media.transcode.online.OnlineTranscoder;
import dji.midware.util.save.StreamDataObserver;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Locale;
import org.greenrobot.eventbus.EventBus;

@EXClassNullAway
public class RecorderMp4 extends RecorderBase implements RecorderInterface, OnlineTranscoder.OnlineTranscoderListener {
    private static final boolean DEBUG = false;
    public static String TAG = "RecorderMp4";
    private static RecorderMp4 instance = null;
    /* access modifiers changed from: private */
    public static MediaRecorder recorder;
    private long initial_original_pts = -1;
    private long lastVideoPts = -1;
    private DJIMuxerInterface muxer = null;
    private boolean muxerInitialized = false;
    private ServiceManager serviceMangaer;

    public static synchronized RecorderMp4 getInstance() {
        RecorderMp4 recorderMp4;
        synchronized (RecorderMp4.class) {
            if (instance == null) {
                instance = new RecorderMp4();
                if (!EventBus.getDefault().isRegistered(instance)) {
                    EventBus.getDefault().register(instance);
                }
            }
            recorderMp4 = instance;
        }
        return recorderMp4;
    }

    private void logToFile(String log) {
        log2File("mp4:" + log);
    }

    public static synchronized void destroy() {
        synchronized (RecorderMp4.class) {
            MediaLogger.show("RecorderMp4 will be destroyed asynchronously");
            if (instance != null) {
                instance.onDestroy();
                instance = null;
            }
        }
    }

    private RecorderMp4() {
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

    private void closeOrDeleteFile() {
        if (this.externalSdRecordingHelper != null) {
            this.externalSdRecordingHelper.pause();
        }
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
    public String getRecordingFilePath() {
        return VideoMetadataManager.getSourceVideoDirectory() + this.mainFileName + ".mp4";
    }

    /* access modifiers changed from: protected */
    public String getTAG() {
        return TAG;
    }

    /* access modifiers changed from: protected */
    public void onStartRecord() {
        if (!checkIsCurrentRecorder()) {
            logToFile("start not cur recorder: " + this);
            return;
        }
        this.serviceMangaer = ServiceManager.getInstance();
        initPTSSync();
        this.muxerInitialized = false;
        this.numFrameWritten = 0;
        setMainFileName();
        createFile();
        startRecordVideoInfo();
        OnlineTranscoder.getInstance().addListener(this);
        if (DataCameraGetAudio.getInstance().isEnable()) {
            MediaLogger.show("OSMO: start to record audio locally");
            startRecordAudio();
            return;
        }
        MediaLogger.show("No local audio recording.");
    }

    private void startRecordAudio() {
        try {
            recorder = new MediaRecorder();
            if (recorder != null) {
                recorder.setOnErrorListener(new MediaRecorder.OnErrorListener() {
                    /* class dji.midware.media.record.RecorderMp4.AnonymousClass1 */

                    public void onError(MediaRecorder mr, int what, int extra) {
                        MediaLogger.e(RecorderMp4.TAG, "MeidaRecorder error: what=" + what + " extra=" + extra);
                        MediaRecorder unused = RecorderMp4.recorder = null;
                    }
                });
                recorder.setAudioSource(1);
                recorder.setOutputFormat(2);
                recorder.setAudioEncoder(3);
                recorder.setAudioChannels(2);
                recorder.setAudioSamplingRate(44100);
                recorder.setOutputFile(VideoMetadataManager.getSourceVideoDirectory() + this.mainFileName + ".m4a");
                recorder.prepare();
                recorder.start();
            }
        } catch (Exception e) {
            MediaLogger.e(TAG, e);
            recorder = null;
        }
    }

    /* access modifiers changed from: protected */
    public void onEndRecord() {
        if (recorder != null) {
            stopRecordAudio();
        }
        OnlineTranscoder.getInstance().removeListener(this);
        endRecordVideoInfo();
        closeOrDeleteFile();
        Log.i(TAG, "onEndRecord() completion");
    }

    private void stopRecordAudio() {
        try {
            if (recorder != null) {
                recorder.stop();
            }
        } catch (Exception e) {
            MediaLogger.e(TAG, e);
        }
        try {
            if (recorder != null) {
                recorder.release();
            }
        } catch (Exception e2) {
            MediaLogger.e(TAG, e2);
        } finally {
            recorder = null;
        }
    }

    public synchronized void onFrameInput(ByteBuffer buffer, MediaCodec.BufferInfo info, int indexInOriginStream, int width, int height) {
        if (checkIsCurrentRecorder()) {
            StreamDataObserver.getInstance(StreamDataObserver.ObservingPoint.RecorderMp4Input).onDataRecv(StreamDataObserver.ObservingContext.FrameRate, 1).onDataRecv(StreamDataObserver.ObservingContext.ByteRate, (long) info.size).onDataRecv(StreamDataObserver.ObservingContext.KeyframeNum, (info.flags & 1) > 0 ? 1 : 0).onDataRecv(StreamDataObserver.ObservingContext.Width, (long) width).onDataRecv(StreamDataObserver.ObservingContext.Height, (long) height);
            logDataInput("mp4 frame input count=" + this.numFrameWritten, DJIVideoDecoder.connectLosedelay, RecorderBase.LogDataMode.Video);
            if (!this.muxerInitialized) {
                if (this.serviceMangaer.getDecoder() == null) {
                    Log.e(TAG, "failed to init muxer. decoder is null. can't get sps pps");
                    logToFile("decoder is null");
                } else {
                    byte[] sps_array = this.serviceMangaer.getDecoder().sps_header;
                    byte[] pps_array = this.serviceMangaer.getDecoder().pps_header;
                    int mwidth = this.serviceMangaer.getDecoder().outputWidth;
                    int mheight = this.serviceMangaer.getDecoder().outputHeight;
                    if (sps_array == null || pps_array == null || mwidth == 0 || mheight == 0) {
                        Log.e(TAG, "failed to init muxer. sps or pps is null. width or height is 0");
                        logToFile("spsLen=" + (sps_array == null ? "null" : Integer.valueOf(sps_array.length)) + ", ppsLen=" + (pps_array == null ? "null" : Integer.valueOf(sps_array.length)) + ", wid=" + mwidth + ", hei=" + mheight);
                    } else {
                        if (this.videoRecordInfo != null) {
                            this.videoRecordInfo.setFrameJumpped(indexInOriginStream);
                        }
                        MediaFormat format = MediaFormat.createVideoFormat(DJIVideoUtil.VIDEO_ENCODING_FORMAT[0], mwidth, mheight);
                        format.setInteger("frame-rate", 30);
                        format.setByteBuffer("csd-0", ByteBuffer.wrap(sps_array));
                        format.setByteBuffer("csd-1", ByteBuffer.wrap(pps_array));
                        this.muxer.addTrack(format);
                        this.muxer.start();
                        Log.i(TAG, "muxer has added a track");
                        this.initial_original_pts = info.presentationTimeUs;
                        this.lastVideoPts = -1;
                        this.muxerInitialized = true;
                        logToFile("muxer inited");
                    }
                }
            }
            info.presentationTimeUs = this.lastVideoPts < 0 ? 0 : this.lastVideoPts + DJIVideoUtil.getDurationPerFrameUs();
            syncPTS();
            this.lastVideoPts = info.presentationTimeUs;
            this.muxer.writeSampleData(0, buffer, info, 1);
            this.numFrameWritten++;
            MediaLogger.i(false, TAG, String.format(Locale.US, "muxer write a frame. num=%d, size=%d, pts=%d, flags=%s (KEY=1 END=4)", Integer.valueOf(this.numFrameWritten), Integer.valueOf(info.size), Long.valueOf(info.presentationTimeUs), Integer.valueOf(info.flags)));
        }
    }
}
