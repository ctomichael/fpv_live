package dji.midware.natives;

import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.util.Log;
import dji.fieldAnnotation.EXClassNullAway;
import java.nio.ByteBuffer;
import java.util.HashMap;

@Keep
@EXClassNullAway
public class FPVController {
    public static native int jni_audio_filters_frame_in(long j, int i, ByteBuffer byteBuffer, int i2, long j2);

    public static native int jni_audio_filters_frame_out(long j, ByteBuffer byteBuffer, ByteBuffer byteBuffer2);

    public static native long jni_audio_filters_init(String str, int i, int[] iArr, int[] iArr2, int[] iArr3, int i2, int i3, int i4);

    public static native int jni_audio_filters_release(long j);

    public static native HashMap<String, String> jni_demuxer_getMetadata(String str);

    public static native int jni_demuxer_getTrackCount(long j);

    public static native int jni_demuxer_getTrackFormat(long j, int i, ByteBuffer byteBuffer);

    public static native long jni_demuxer_init(String str);

    public static native boolean jni_demuxer_readSample(long j, Object obj, Object obj2, boolean z);

    public static native void jni_demuxer_release(long j);

    public static native boolean jni_demuxer_seekTo(long j, int i, long j2, boolean z);

    public static native int jni_image_convert_I420ToRGB565(Object obj, int i, int i2, int i3, Object obj2, int i4, int i5);

    public static native int jni_image_convert_NV21ToRGB565(Object obj, int i, int i2, Object obj2, int i3, int i4);

    public static native int native_clear();

    public static native long native_getDJIAVPaserHeaderMagic();

    public static native boolean native_getIsLiveStreamAudioMute();

    public static native int native_getLiveStreamAudioBitRate();

    public static native int native_getLiveStreamVideoBitRate();

    public static native int native_getLiveStreamVideoBufSize();

    public static native float native_getLiveStreamVideoFps();

    public static native int native_getQueueSize();

    public static native int native_getSecondaryQueueSize();

    public static native short[] native_getStreamParams();

    public static native int native_h264ParseSliceHeader(byte[] bArr, int i, int[] iArr, int[] iArr2, int[] iArr3, int i2);

    public static native int native_init(Object obj);

    public static native int native_initStreaming(String str, int i, byte[] bArr, int i2);

    public static native boolean native_isStarted(int i);

    public static native boolean native_isX264Enabled();

    public static native int native_mp4MuxerAddAudioTrack(int i, int i2, int i3, int i4, long j, byte[] bArr, int i5);

    public static native int native_mp4MuxerAddVideoTrack(int i, int i2, int i3, byte[] bArr, int i4, long j);

    public static native int native_mp4MuxerInit(int i);

    public static native void native_mp4MuxerSetIsRotated(int i);

    public static native int native_mp4MuxerStart(String str);

    public static native int native_mp4MuxerStop();

    public static native int native_mp4MuxerWrite(int i, Object obj, int i2, int i3, long j, long j2);

    public static native int native_newMuxerAddTrack(long j, @NonNull String[] strArr, @NonNull Object[] objArr);

    public static native int native_newMuxerRelease(long j);

    public static native int native_newMuxerSetLocation(long j, int i, int i2);

    public static native int native_newMuxerSetOrientationHint(long j, int i);

    public static native long native_newMuxerSetup(String str);

    public static native int native_newMuxerStart(long j);

    public static native int native_newMuxerStop(long j);

    public static native int native_newMuxerWriteSampleData(long j, int i, @NonNull ByteBuffer byteBuffer, int i2, int i3, long j2, int i4);

    public static native int native_pauseParseThread(boolean z);

    public static native int native_pauseRecvThread(boolean z);

    public static native int native_putAudioData(short[] sArr, int i);

    public static native int native_putAudioDataLiveStream(short[] sArr, int i);

    public static native int native_putVideoStreamData(byte[] bArr, int i, int i2, int i3, int i4);

    public static native int native_reqCtrlInfo(byte[] bArr);

    public static native int native_sendCtrlInfo(byte[] bArr, int i, int i2);

    public static native int native_setCallObject(Object obj);

    public static native int native_setDataMode(boolean z);

    public static native int native_setDecodeMode(boolean z);

    public static native int native_setDecoderType(int i);

    public static native int native_setFrameRate(int i);

    public static native int native_setIsFixRate(boolean z);

    public static native int native_setIsLiveStreamAudioMute(boolean z);

    public static native int native_setIsNeedPacked(boolean z);

    public static native int native_setIsNeedRawData(boolean z);

    public static native int native_setIsNewRate(boolean z);

    public static native int native_setMSCChannel(int i, int i2, int i3);

    public static native int native_setNeedRemoveAud(int i);

    public static native int native_setOnStreamCB(Object obj, String str);

    public static native int native_setRGBBuffer(byte[] bArr, int i);

    public static native int native_setRealtimeFps(int i);

    public static native int native_setVideoDataRecver(Object obj);

    public static native int native_setVideoPackObject(Object obj);

    public static native int native_startParseThread();

    public static native int native_startRecvThread();

    public static native int native_startRecvThread2(String str, boolean z);

    public static native int native_startStream(boolean z);

    public static native int native_stopParseThread();

    public static native int native_stopRecvThread();

    public static native int native_stopRecvThread2(boolean z);

    public static native int native_stopStream();

    public static native int native_transSerialData(byte[] bArr, int i, int i2);

    public static native int native_transcodeX264Deinit();

    public static native int native_transcodeX264Encode(Object obj, int i, Object obj2);

    public static native int native_transcodeX264Init(Object obj, int i, int i2, int i3, int i4);

    public static native int native_transcodeX264Start();

    public static native int native_transcodeX264Stop();

    public static native int native_transcodeYUVConvert(Object obj, Object obj2, int i, int i2, int i3);

    public static native int native_transferAudioData(byte[] bArr, int i, int i2, byte[] bArr2, int i3);

    public static native int native_transferVideoData(byte[] bArr, int i, byte[] bArr2, int i2, int i3);

    public static native int native_transferVideoDataDirect(byte[] bArr, int i, int i2, byte[] bArr2, int i3);

    public static native int native_unInit();

    public static native int native_unInitStreaming();

    static {
        try {
            Log.d("FPVController", "try to load libdjivideo.so");
            System.loadLibrary("djivideo");
        } catch (UnsatisfiedLinkError e) {
            Log.e("FPVController", "Couldn't load lib");
        }
    }

    public static void loadLibrary() {
    }

    public static boolean native_isStarted() {
        return native_isStarted(0);
    }

    public static synchronized int native_transferVideoData(byte[] pBuff, int len) {
        int native_transferVideoData;
        synchronized (FPVController.class) {
            native_transferVideoData = native_transferVideoData(pBuff, len, null, 0, 0);
        }
        return native_transferVideoData;
    }

    public static synchronized int native_transferVideoData(byte[] pBuff, int len, int sourceIndex) {
        int native_transferVideoData;
        synchronized (FPVController.class) {
            native_transferVideoData = native_transferVideoData(pBuff, len, null, 0, sourceIndex);
        }
        return native_transferVideoData;
    }

    public static synchronized int native_transferVideoData(byte[] pBuff, int len, int type, int sourceIndex) {
        int native_transferVideoData;
        synchronized (FPVController.class) {
            native_transferVideoData = native_transferVideoData(pBuff, len, null, type, sourceIndex);
        }
        return native_transferVideoData;
    }

    public static int native_transferVideoDataDirect(byte[] pBuff, int len, byte[] ptsBuff, int ptsLen) {
        return native_transferVideoDataDirect(pBuff, 0, len, ptsBuff, ptsLen);
    }

    public static int native_transferAudioData(byte[] pBuff, int len, byte[] ptsBuff, int ptsLen) {
        return native_transferAudioData(pBuff, 0, len, ptsBuff, ptsLen);
    }

    @Keep
    public enum ColorPlanar {
        YUV420P(0),
        NV12(1);
        
        private int value;

        private ColorPlanar(int value2) {
            this.value = value2;
        }

        /* access modifiers changed from: package-private */
        public int getValue() {
            return this.value;
        }
    }

    @Keep
    public enum TranscodeResult {
        SUCCESS(0),
        ERR_INVALID_PARAM(-1),
        ERR_NODATA(-2),
        OTHER(-100);
        
        private int value;

        private TranscodeResult(int v) {
            this.value = v;
        }

        public int getValue() {
            return this.value;
        }

        public boolean _equals(int b) {
            return this.value == b;
        }

        public static TranscodeResult find(int b) {
            TranscodeResult result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
