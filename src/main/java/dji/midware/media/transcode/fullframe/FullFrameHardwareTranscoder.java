package dji.midware.media.transcode.fullframe;

import android.media.MediaCodec;
import android.os.Build;
import com.dji.video.framing.DJIVideoCodecInnerManager;
import com.dji.video.framing.internal.decoder.DJIVideoDecoder;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.midware.data.manager.P3.DJIVideoEvent;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.media.DJIVideoHardwareEncoder;
import dji.midware.util.DJIEventBusUtil;
import dji.midware.util.DjiSharedPreferencesManager;
import dji.midware.util.save.StreamDataObserver;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class FullFrameHardwareTranscoder implements DJIVideoHardwareEncoder.VideoHardwareEncoderListener {
    private static final String ENCODER_SURFACE_NAME = "full_frame_hardware_transcoder_surface_name";
    public static final String RESOLUTION_MODE_KEY = "resolution_mode_key";
    private static final String TAG = "FullFrameHardware";
    private static volatile FullFrameHardwareTranscoder instance;
    private int bitRate = 2097152;
    private FullFrameTransCodeCameraIndex cameraIndex = FullFrameTransCodeCameraIndex.Primary;
    private DJIVideoDecoder currentDecoder;
    private Object encoderLock = new Object();
    private int frameIntervalCache = 1;
    private DJIVideoHardwareEncoder hardwareEncoder;
    public int keyFrameRate = 30;
    private List<FullFrameTranscoderListener> listenerList = new LinkedList();
    private FullFrameTranscodeState transcodeState = FullFrameTranscodeState.Standby;

    public interface FullFrameTranscoderListener {
        void onFrameInput(ByteBuffer byteBuffer, MediaCodec.BufferInfo bufferInfo, int i, int i2, boolean z);

        void onFrameInput(byte[] bArr, MediaCodec.BufferInfo bufferInfo, int i, int i2, boolean z);
    }

    public enum FullFrameTranscodeState {
        Standby,
        WaitForInput,
        Encoding
    }

    public enum FullFrameTransCodeCameraIndex {
        Primary(0),
        Secondary(1),
        FPV(2),
        UNKNOWN(255);
        
        private final int value;

        private FullFrameTransCodeCameraIndex(int value2) {
            this.value = value2;
        }

        public int getValue() {
            return this.value;
        }

        private boolean _equals(int b) {
            return this.value == b;
        }

        public static FullFrameTransCodeCameraIndex find(int value2) {
            FullFrameTransCodeCameraIndex result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(value2)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    private DJIVideoDecoder getDecoder() {
        return DJIVideoCodecInnerManager.getInstance().getDJIVideoDecoderInterface().getDJIVideoDecoder();
    }

    private boolean isDecoderOK() {
        DJIVideoDecoder decoder = getDecoder();
        return decoder != null && decoder.isDecoderOK();
    }

    public void setTranscodeState(FullFrameTranscodeState state) {
        this.transcodeState = state;
    }

    public FullFrameTranscodeState getTranscodeState() {
        return this.transcodeState;
    }

    public enum ResolutionMode {
        SameWithDecoder(0),
        Fix720p(1);
        
        private int value;

        private ResolutionMode(int value2) {
            this.value = value2;
        }

        public int getValue() {
            return this.value;
        }

        public static ResolutionMode find(int value2) {
            ResolutionMode rst = SameWithDecoder;
            for (int i = 0; i < values().length; i++) {
                if (values()[i].getValue() == value2) {
                    return values()[i];
                }
            }
            return rst;
        }
    }

    public ResolutionMode getResolutionMode() {
        int spValue = DjiSharedPreferencesManager.getInt(ServiceManager.getContext(), "resolution_mode_key", -1);
        if (spValue >= 0) {
            return ResolutionMode.find(spValue);
        }
        ResolutionMode defaultMode = getDefaultResolutionMode();
        setResolutionMode(defaultMode);
        return defaultMode;
    }

    public void setResolutionMode(ResolutionMode resolutionMode) {
        DjiSharedPreferencesManager.putInt(ServiceManager.getContext(), "resolution_mode_key", resolutionMode.getValue());
    }

    public ResolutionMode getDefaultResolutionMode() {
        if (Build.VERSION.SDK_INT <= 23) {
            return ResolutionMode.Fix720p;
        }
        return ResolutionMode.SameWithDecoder;
    }

    public void addListener(FullFrameTranscoderListener listener) {
        if (listener != null) {
            synchronized (this.listenerList) {
                if (!this.listenerList.contains(listener)) {
                    this.listenerList.add(listener);
                }
            }
            synchronized (this.encoderLock) {
                if (this.transcodeState == FullFrameTranscodeState.Standby) {
                    if (!isDecoderOK()) {
                        setTranscodeState(FullFrameTranscodeState.WaitForInput);
                    } else if (startEncode()) {
                        setTranscodeState(FullFrameTranscodeState.Encoding);
                    } else {
                        setTranscodeState(FullFrameTranscodeState.WaitForInput);
                    }
                }
            }
        }
    }

    public void removeListener(FullFrameTranscoderListener listener) {
        synchronized (this.listenerList) {
            this.listenerList.remove(listener);
        }
        synchronized (this.encoderLock) {
            if (this.listenerList.isEmpty()) {
                if (this.transcodeState == FullFrameTranscodeState.Encoding) {
                    stopEncode();
                }
                setTranscodeState(FullFrameTranscodeState.Standby);
            }
        }
    }

    private void invokeListeners(ByteBuffer buffer, MediaCodec.BufferInfo info, int width, int height, boolean isKeyFrame) {
        if (!this.listenerList.isEmpty()) {
            synchronized (this.listenerList) {
                for (FullFrameTranscoderListener listener : this.listenerList) {
                    if (listener != null) {
                        listener.onFrameInput(buffer, info, width, height, isKeyFrame);
                    }
                }
            }
        }
    }

    private void invokeListeners(byte[] data, MediaCodec.BufferInfo info, int width, int height, boolean isKeyFrame) {
        if (!this.listenerList.isEmpty()) {
            synchronized (this.listenerList) {
                for (FullFrameTranscoderListener listener : this.listenerList) {
                    if (listener != null) {
                        listener.onFrameInput(data, info, width, height, isKeyFrame);
                    }
                }
            }
        }
    }

    public void setBitRate(int bitRate2) {
        boolean needRestart = bitRate2 != this.bitRate;
        this.bitRate = bitRate2;
        if (needRestart && this.transcodeState == FullFrameTranscodeState.Encoding) {
            restartEncode();
        }
    }

    public int getBitRate() {
        return this.bitRate;
    }

    private FullFrameHardwareTranscoder() {
    }

    public static FullFrameHardwareTranscoder getInstance() {
        if (instance == null) {
            synchronized (FullFrameHardwareTranscoder.class) {
                if (instance == null) {
                    instance = new FullFrameHardwareTranscoder();
                    DJIEventBusUtil.register(instance);
                }
            }
        }
        return instance;
    }

    public int getKeyFrameRate() {
        return this.keyFrameRate;
    }

    public void setKeyFrameRate(int keyFrameRate2) {
        boolean needRestart = keyFrameRate2 != this.keyFrameRate;
        this.keyFrameRate = keyFrameRate2;
        if (needRestart && this.transcodeState == FullFrameTranscodeState.Encoding) {
            restartEncode();
        }
    }

    public int getFrameInterval() {
        return this.frameIntervalCache;
    }

    public void setFrameInterval(int frameInterval) {
        this.frameIntervalCache = frameInterval;
    }

    public void setCurrentCameraIndex(int index) {
        boolean needRestart = index != this.cameraIndex.getValue();
        if (needRestart && this.transcodeState == FullFrameTranscodeState.Encoding) {
            stopEncode();
            this.cameraIndex = FullFrameTransCodeCameraIndex.find(index);
            startEncode();
        } else if (needRestart) {
            this.cameraIndex = FullFrameTransCodeCameraIndex.find(index);
        }
    }

    public FullFrameTransCodeCameraIndex getCurrentCameraIndex() {
        return this.cameraIndex;
    }

    public byte[] getSps() {
        if (this.hardwareEncoder == null) {
            return null;
        }
        return this.hardwareEncoder.sps;
    }

    public byte[] getPps() {
        if (this.hardwareEncoder == null) {
            return null;
        }
        return this.hardwareEncoder.pps;
    }

    public int[] getEncodeWidthHeight(DJIVideoDecoder decoder) {
        if (decoder == null) {
            return null;
        }
        int decodeHeight = decoder.getVideoHeight();
        int decodeWidth = decoder.getVideoWidth();
        int encodeWidth = decodeWidth;
        int encodeHeight = decodeHeight;
        switch (getResolutionMode()) {
            case Fix720p:
                if (encodeHeight > 800) {
                    if (decodeWidth == 1088) {
                        decodeWidth = 1080;
                    }
                    if (decodeHeight == 1088) {
                        decodeHeight = 1080;
                    }
                    encodeHeight = 720;
                    encodeWidth = (720 * decodeWidth) / decodeHeight;
                    break;
                }
                break;
        }
        return new int[]{encodeWidth, encodeHeight};
    }

    private boolean startEncode() {
        this.currentDecoder = getDecoder();
        if (this.currentDecoder == null) {
            DJILog.e(TAG, "currentDecoder is null return!", new Object[0]);
            return false;
        }
        int[] encodeWidthHeight = getEncodeWidthHeight(this.currentDecoder);
        int encodeWidth = encodeWidthHeight[0];
        int encodeHeight = encodeWidthHeight[1];
        this.hardwareEncoder = new DJIVideoHardwareEncoder();
        DJILog.d(TAG, "startEncode:width=" + encodeWidth + ", height=" + encodeHeight + ", bitrate=" + this.bitRate, new Object[0]);
        this.hardwareEncoder.start(encodeWidth, encodeHeight, this.keyFrameRate, this.bitRate);
        this.currentDecoder.setAsyncRenderSurface(ENCODER_SURFACE_NAME, this.hardwareEncoder.getInputSurface(), encodeWidth, encodeHeight, this.frameIntervalCache);
        this.hardwareEncoder.addListener(this);
        return true;
    }

    private void stopEncode() {
        if (this.hardwareEncoder != null) {
            DJILog.d(TAG, "stopEncode", new Object[0]);
            if (this.currentDecoder != null) {
                this.currentDecoder.setAsyncRenderSurface(ENCODER_SURFACE_NAME, null, 0, 0, 0);
            }
            this.hardwareEncoder.removeListener(this);
            this.hardwareEncoder.stop();
        }
    }

    private void restartEncode() {
        synchronized (this.encoderLock) {
            stopEncode();
            startEncode();
        }
    }

    public void onEncodeData(byte[] data, MediaCodec.BufferInfo bufferInfo, int width, int height, boolean isKeyFrame) {
        long j = 1;
        StreamDataObserver onDataRecv = StreamDataObserver.getInstance(StreamDataObserver.ObservingPoint.FullFrameTranscodrEncData).onDataRecv(StreamDataObserver.ObservingContext.FrameRate, 1).onDataRecv(StreamDataObserver.ObservingContext.ByteRate, (long) (bufferInfo.size - bufferInfo.offset));
        StreamDataObserver.ObservingContext observingContext = StreamDataObserver.ObservingContext.KeyframeNum;
        if (!isKeyFrame) {
            j = 0;
        }
        onDataRecv.onDataRecv(observingContext, j).onDataRecv(StreamDataObserver.ObservingContext.Width, (long) width).onDataRecv(StreamDataObserver.ObservingContext.Height, (long) height);
        invokeListeners(data, bufferInfo, width, height, isKeyFrame);
    }

    public void onEncodeData(ByteBuffer data, MediaCodec.BufferInfo bufferInfo, int width, int height, boolean isKeyFrame) {
        invokeListeners(data, bufferInfo, width, height, isKeyFrame);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DJIVideoEvent event) {
        if (event == DJIVideoEvent.ConnectOK) {
            synchronized (this.encoderLock) {
                if (this.transcodeState == FullFrameTranscodeState.WaitForInput && startEncode()) {
                    setTranscodeState(FullFrameTranscodeState.Encoding);
                }
            }
        }
    }
}
