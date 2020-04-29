package dji.midware.media;

import android.content.Context;
import android.os.SystemClock;
import android.support.annotation.Keep;
import android.util.Log;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.media.DJIVideoProcessTimeTest;
import dji.midware.media.record.H264FrameListener;
import dji.midware.media.record.MainFrameDataListener;
import dji.midware.media.record.RecorderManager;
import dji.midware.natives.FPVController;
import dji.midware.usb.P3.UsbAccessoryService;
import dji.midware.util.BytesUtil;
import dji.midware.util.save.StreamDataObserver;
import dji.midware.util.save.StreamDelaySaver;
import dji.midware.util.save.StreamSaver;
import dji.midware.util.save.TestUtil;
import dji.midware.util.save.VideoFrameObserver;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Locale;

@Keep
@EXClassNullAway
public class DJIVideoDataRecver {
    private static boolean DEBUG = false;
    private static String TAG = "DJIVideoDataRecver";
    private static DJIVideoDataRecver instance;
    long[] frameIndex = {-1, -1};
    int[] frameNum = {0, 0};
    private boolean isFpvNeedRawData = false;
    private boolean isNeedPacked = true;
    private boolean isNeedRawData = false;
    long jpeg_frame_index = 0;
    long lastT1 = 0;
    private long last_video_frame_coming_time = -1;
    private H264FrameListener listener;
    private DJIVideoDataListener listenerForFPVTrans;
    private DJIVideoDataListener listenerForTrans;
    private Object listenerSync = new Object();
    private FrameRecv mFrameRecv = new FrameRecv();
    private MainFrameDataListener mainFrameDataListener;
    long maxTime = 0;
    int maxsize = 0;
    private boolean needFrameData = false;
    long time = 0;
    private DJIDecoderType type = DJIDecoderType.None;

    public interface DJIVideoDataListener {
        void onVideoRecv(byte[] bArr, int i);
    }

    @Keep
    public static class FrameRecv {
        public int frameNum;
    }

    public static synchronized DJIVideoDataRecver getInstance() {
        DJIVideoDataRecver dJIVideoDataRecver;
        synchronized (DJIVideoDataRecver.class) {
            if (instance == null) {
                instance = new DJIVideoDataRecver();
            }
            dJIVideoDataRecver = instance;
        }
        return dJIVideoDataRecver;
    }

    public DJIVideoDataRecver setVideoDataListener(boolean isNeedPacked2, DJIVideoDataListener listener2) {
        this.isNeedPacked = isNeedPacked2;
        this.listenerForTrans = listener2;
        FPVController.native_setIsNeedPacked(isNeedPacked2);
        return this;
    }

    public DJIVideoDataRecver setFPVVideoDataListener(boolean isNeedPacked2, DJIVideoDataListener listener2) {
        this.isNeedPacked = isNeedPacked2;
        this.listenerForFPVTrans = listener2;
        FPVController.native_setIsNeedPacked(isNeedPacked2);
        return this;
    }

    public DJIVideoDataRecver setDecoderType(DJIDecoderType type2) {
        this.type = type2;
        ServiceManager.getInstance().setDecoderType(type2.value());
        return this;
    }

    public DJIDecoderType getDecoderType() {
        return this.type;
    }

    public DJIVideoDataRecver setNeedVideoDataPacked(boolean isNeedPacked2) {
        this.isNeedPacked = isNeedPacked2;
        ServiceManager.getInstance().setIsNeedPacked(isNeedPacked2);
        return this;
    }

    public DJIVideoDataRecver setNeedRawVideoData(boolean isNeedRawData2) {
        this.isNeedRawData = isNeedRawData2;
        ServiceManager.getInstance().setIsNeedRawData(isNeedRawData2);
        return this;
    }

    public DJIVideoDataRecver setNeedFpvRawVideoData(boolean isNeedRawData2) {
        this.isFpvNeedRawData = isNeedRawData2;
        ServiceManager.getInstance().setIsFpvNeedRawData(isNeedRawData2);
        return this;
    }

    public boolean isNeedRawData() {
        return this.isNeedRawData;
    }

    public boolean isFpvNeedRawData() {
        return this.isFpvNeedRawData;
    }

    public boolean isNeedFrameData() {
        return this.needFrameData;
    }

    public DJIVideoDataRecver setH264FrameListener(boolean isNeedPacked2, H264FrameListener listener2) {
        synchronized (this.listenerSync) {
            this.listener = listener2;
        }
        return this;
    }

    public DJIVideoDataRecver setMainFrameDataListener(MainFrameDataListener listener2) {
        synchronized (this.listenerSync) {
            this.mainFrameDataListener = listener2;
        }
        this.isNeedPacked = true;
        FPVController.native_setIsNeedPacked(this.isNeedPacked);
        if (listener2 != null) {
            this.needFrameData = true;
        } else {
            this.needFrameData = false;
        }
        return this;
    }

    public void onAudioRecv(byte[] videoBuffer, int size, long ptsUs) {
        if (this.isNeedPacked) {
            putAudioBufferToDecoder(videoBuffer, size, ptsUs);
        }
    }

    public void onAudioRecv(byte[] videoBuffer, int offset, int size, long ptsUs) {
        if (this.isNeedPacked) {
            putAudioBufferToDecoder(videoBuffer, offset, size, ptsUs);
        }
    }

    public void onJpegFrameRecv(byte[] jpegBuffer, int offset, int size) {
        this.jpeg_frame_index++;
        if (StreamDelaySaver.IS_SAVE_PACKET_DELAY) {
            try {
                StreamDelaySaver.getInstance().frameDelayFile.append((CharSequence) String.format(Locale.US, "[JPEG_INPUT] word 0: %d word 1: %d word 2: %d size=%d time=%d \n", Integer.valueOf(BytesUtil.getInt(jpegBuffer, 0)), Integer.valueOf(BytesUtil.getInt(jpegBuffer, 4)), Integer.valueOf(BytesUtil.getInt(jpegBuffer, 8)), Integer.valueOf(size), Long.valueOf(System.currentTimeMillis())));
                if (this.jpeg_frame_index % 16 == 0) {
                    StreamDelaySaver.getInstance().frameDelayFile.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        MediaLogger.i(DEBUG, TAG, String.format(Locale.US, "jpeg_frameIndex=%d, offset=%d size=%d", Long.valueOf(this.jpeg_frame_index), Integer.valueOf(offset), Integer.valueOf(size)));
        if (StreamSaver.SAVE_videoDataReceiver_Open) {
            StreamSaver.getInstance(StreamSaver.SAVE_videoDataReceiver_Name).write(jpegBuffer, offset, size);
        }
        DJIVideoDecoder decoder = ServiceManager.getInstance().getDecoder();
        if (decoder != null) {
            decoder.displayJpegFrame(jpegBuffer, offset, size);
        }
    }

    public void onCmdDataRecv(byte[] cmdBuffer, int len, int type2) {
    }

    public void onVideoRecv(byte[] videoBuffer, int size, boolean isRawStream) {
        onVideoRecv(videoBuffer, 0, size, 0, true, -1, 0, -1, 0, 0, 0, isRawStream, 0);
    }

    public void onVideoRecv(byte[] videoBuffer, int size, boolean isRawStream, int index) {
        onVideoRecv(videoBuffer, 0, size, 0, true, -1, 0, -1, 0, 0, 0, isRawStream, index);
    }

    public void onVideoRecv(byte[] videoBuffer, int offset, int size, boolean isRawStream) {
        onVideoRecv(videoBuffer, offset, size, 0, true, -1, 0, -1, 0, 0, 0, isRawStream, 0);
    }

    public void onVideoRecv(byte[] videoBuffer, int size, int frameNum2, boolean isKeyFrame, int spsPos, int spsLen, int ppsPos, int ppsLen, int newWidth, int newHeight, boolean isRawStream) {
        onVideoRecv(videoBuffer, 0, size, frameNum2, isKeyFrame, spsPos, spsLen, ppsPos, ppsLen, newWidth, newHeight, isRawStream, 0);
    }

    public void onVideoRecv(byte[] videoBuffer, int size, int frameNum2, boolean isKeyFrame, int spsPos, int spsLen, int ppsPos, int ppsLen, int newWidth, int newHeight, boolean isRawStream, int sourceIndex) {
        onVideoRecv(videoBuffer, 0, size, frameNum2, isKeyFrame, spsPos, spsLen, ppsPos, ppsLen, newWidth, newHeight, isRawStream, sourceIndex);
    }

    public void onVideoRecv(byte[] videoBuffer, int offset, int size, int frameNum2, boolean isKeyFrame, int spsPos, int spsLen, int ppsPos, int ppsLen, int newWidth, int newHeight, boolean isRawStream) {
        onVideoRecv(videoBuffer, offset, size, frameNum2, isKeyFrame, spsPos, spsLen, ppsPos, ppsLen, newWidth, newHeight, isRawStream, 0);
    }

    public void onVideoRecv(byte[] videoBuffer, int offset, int size, int frameNum2, boolean isKeyFrame, int spsPos, int spsLen, int ppsPos, int ppsLen, int newWidth, int newHeight, boolean isRawStream, int sourceIndex) {
        UsbAccessoryService.VideoStreamSource streamSource = UsbAccessoryService.VideoStreamSource.find(sourceIndex);
        if (sourceIndex > this.frameNum.length) {
            sourceIndex = 0;
        }
        this.frameNum[sourceIndex] = frameNum2;
        DJIVideoProcessTimeTest.getInstance().onVideoDataProcessing(DJIVideoProcessTimeTest.VideoProcessPoint.VideoRecv, videoBuffer, offset + 6);
        if (!isRawStream) {
            if (StreamSaver.videoDebugEnabledUsedInSDK) {
                DJILog.d(StreamSaver.VIDEO_LOG_TAG, " Frame No:" + frameNum2 + " isKeyFrame:" + isKeyFrame + " source:" + sourceIndex, new Object[0]);
            }
            StreamDataObserver.getInstance(StreamDataObserver.ObservingPoint.VideoDataRecverNotRaw, streamSource).onDataRecv(StreamDataObserver.ObservingContext.FrameRate, 1).onDataRecv(StreamDataObserver.ObservingContext.ByteRate, (long) size).onDataRecv(StreamDataObserver.ObservingContext.Width, (long) newWidth).onDataRecv(StreamDataObserver.ObservingContext.Height, (long) newHeight).onDataRecv(StreamDataObserver.ObservingContext.KeyframeNum, isKeyFrame ? 1 : 0).onDataRecv(StreamDataObserver.ObservingContext.SpsppsNum, spsPos >= 0 ? 1 : 0);
            if (streamSource == UsbAccessoryService.VideoStreamSource.Camera) {
                VideoFrameObserver.getInstance().saveTimeStamp(VideoFrameObserver.TimeSavingPoint.VideoDataRecv, videoBuffer, offset, false);
            }
            this.frameIndex[sourceIndex] = this.frameIndex[sourceIndex] + 1;
            long com_pts = DJIVideoUtil.getComprehensivePts(System.currentTimeMillis(), this.frameIndex[sourceIndex], (long) frameNum2);
            if (StreamDelaySaver.IS_SAVE_PACKET_DELAY) {
                try {
                    StreamDelaySaver.getInstance().frameDelayFile.append((CharSequence) String.format(Locale.US, "[DECODER_INPUT] word 0: %d word 1: %d word 2: %d size=%d pts=%d frameNum=%d, frameIndex=%d time=%d \n", Integer.valueOf(BytesUtil.getInt(videoBuffer, 0)), Integer.valueOf(BytesUtil.getInt(videoBuffer, 4)), Integer.valueOf(BytesUtil.getInt(videoBuffer, 8)), Integer.valueOf(size), Long.valueOf(com_pts), Integer.valueOf(frameNum2), this.frameIndex, Long.valueOf(System.currentTimeMillis())));
                    if (frameNum2 % 16 == 0) {
                        StreamDelaySaver.getInstance().frameDelayFile.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            synchronized (this.listenerSync) {
                if (this.listener != null && streamSource == UsbAccessoryService.VideoStreamSource.Camera) {
                    this.listener.onH264FrameInput(videoBuffer, size, com_pts, isKeyFrame);
                } else if (this.mainFrameDataListener != null && streamSource == UsbAccessoryService.VideoStreamSource.Camera) {
                    this.mainFrameDataListener.onFrameInput(videoBuffer, size, frameNum2, isKeyFrame, newWidth, newHeight);
                }
            }
            if (this.isNeedPacked) {
                if (RecorderManager.bufferMode == RecorderManager.BufferMode.GDR_ONLINE && DJIVideoUtil.isDebug(DEBUG)) {
                    Log.i(TAG, String.format(Locale.US, "feed data into decoder: size=%d, frameIndex=%08X", Integer.valueOf(size), Long.valueOf(com_pts)));
                }
                if (TestUtil.TEST_GPIO_DJIVideoDataRecver && streamSource == UsbAccessoryService.VideoStreamSource.Camera) {
                    TestUtil.writeGPIO(videoBuffer);
                }
                putBufferToDecoder(videoBuffer, size, com_pts, frameNum2, isKeyFrame, this.frameIndex[sourceIndex], spsPos, spsLen, ppsPos, ppsLen, newWidth, newHeight, streamSource);
            }
            if (TestUtil.LOAD_STREAM_FROM_FILE) {
                addDelayForLoadingStreamFromFile();
            }
        } else if (streamSource == UsbAccessoryService.VideoStreamSource.Fpv) {
            if (this.listenerForFPVTrans != null) {
                this.listenerForFPVTrans.onVideoRecv(videoBuffer, size);
            }
        } else if (this.listenerForTrans != null) {
            this.listenerForTrans.onVideoRecv(videoBuffer, size);
        }
    }

    private void addDelayForLoadingStreamFromFile() {
        if (this.last_video_frame_coming_time < 0) {
            this.last_video_frame_coming_time = SystemClock.uptimeMillis();
        }
        long durationTime = SystemClock.uptimeMillis() - this.last_video_frame_coming_time;
        long sleepTime = 0;
        if (durationTime >= 0 && durationTime < ((long) 30)) {
            sleepTime = ((long) 30) - durationTime;
        }
        if (sleepTime > 0) {
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                MediaLogger.e(TAG, e);
            }
        }
        this.last_video_frame_coming_time = SystemClock.uptimeMillis();
    }

    public void putAudioBufferToDecoder(byte[] videoBuffer, int size, long ptsUs) {
        putAudioBufferToDecoder(videoBuffer, 0, size, ptsUs);
    }

    public void putAudioBufferToDecoder(byte[] videoBuffer, int offset, int size, long ptsUs) {
        if (ServiceManager.getInstance().getAudioDecoder() != null) {
            ServiceManager.getInstance().getAudioDecoder().onFrameReceive(ByteBuffer.wrap(videoBuffer), offset, size, ptsUs);
        }
    }

    public void putBufferToDecoder(byte[] videoBuffer, int size, long comPts, int frameNum2, boolean isKeyFrame, long frameIndex2, int spsPos, int spsLen, int ppsPos, int ppsLen, int newWidth, int newHeight, UsbAccessoryService.VideoStreamSource source) {
        switch (this.type) {
            case Hardware:
                DJIVideoDecoder decoder = null;
                if (source == UsbAccessoryService.VideoStreamSource.Camera) {
                    decoder = ServiceManager.getInstance().getDecoder();
                } else if (source == UsbAccessoryService.VideoStreamSource.Fpv) {
                    decoder = ServiceManager.getInstance().getSecondaryVideoDecoder();
                }
                if (decoder != null) {
                    if (StreamSaver.SAVE_videoDataReceiver_Open) {
                        StreamSaver.getInstance("dji_video_datareceiver_" + source.name()).write(videoBuffer, 0, size);
                    }
                    decoder.queueInputBuffer(videoBuffer, size, comPts, frameNum2, isKeyFrame, frameIndex2, spsPos, spsLen, ppsPos, ppsLen, newWidth, newHeight);
                    return;
                }
                DJIVideoDecoder.log2FileWithFreqLimit("dec null");
                return;
            case Software:
            default:
                return;
        }
    }

    @Keep
    public enum DJIDecoderType {
        Hardware(0),
        Software(1),
        None(2);
        
        private int data;

        private DJIDecoderType(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static DJIDecoderType find(int b) {
            DJIDecoderType result = None;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & 15) >= 3;
    }
}
