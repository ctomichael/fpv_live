package dji.midware.util.save;

import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import com.dji.megatronking.stringfog.lib.annotation.StringFogIgnore;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.R;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.usb.P3.UsbAccessoryService;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@EXClassNullAway
@StringFogIgnore
public class StreamDataObserver {
    private static final int MSG_OBSERVING = 0;
    public static final String PARAM_BYTE_RATE = "byte_rate";
    public static final String PARAM_FRAME_RATE = "frame_rate";
    public static final String PARAM_HEIGHT = "height";
    public static final String PARAM_KEYFRAME_NUM = "key_frame_num";
    public static final String PARAM_SPSPPS_NUM = "sps_pps_num";
    public static final String PARAM_TOTAL_BYTE_COUNT = "total_byte_count";
    public static final String PARAM_TOTAL_FRAME_COUNT = "total_frame_count";
    public static final String PARAM_WIDTH = "width";
    private static boolean isRunning = false;
    /* access modifiers changed from: private */
    public static List<IStreamDataObserverGlobalListener> listenerList = new LinkedList();
    private static final Object listenerLock = new Object();
    /* access modifiers changed from: private */
    public static Object mapLock = new Object();
    /* access modifiers changed from: private */
    public static StreamDataObserver[][] observerArray = ((StreamDataObserver[][]) Array.newInstance(StreamDataObserver.class, ObservingPoint.values().length, UsbAccessoryService.VideoStreamSource.values().length));
    /* access modifiers changed from: private */
    public static Map<String, Map<String, Long>> sGlobalRst;
    private static Handler sObservingHandler;
    private static HandlerThread sObservingThread;
    /* access modifiers changed from: private */
    public ObservingPoint observingPoint;
    private long[] paramArr = new long[ObservingContext.values().length];
    /* access modifiers changed from: private */
    public UsbAccessoryService.VideoStreamSource source;

    public interface IStreamDataObserverGlobalListener {
        void onByteRatesUpdate(Map<String, Map<String, Long>> map);
    }

    public enum ObservingPoint {
        AoaGetBody("UsbAccessoryService.onGetBody", R.string.stream_observer_desc_aoa_get_body),
        AoaGetStream("UsbAccessoryService.onGetBody(dataType==22346)", R.string.stream_observer_desc_aoa_get_stream),
        AoaGetStream2("UsbAccessoryService.onGetBody(dataType==22347||22350)", R.string.stream_observer_desc_aoa_get_stream),
        AoaGetStream3("UsbAccessoryService.onGetBody(dataType==22352)", R.string.stream_observer_desc_aoa_get_stream),
        AoaGetStreamDefault("UsbAccessoryService.onGetBody(mode=defaule)", R.string.stream_observer_desc_aoa_get_stream_default),
        AoaGetStreamSingle("UsbAccessoryService.onGetBody(mode=single)", R.string.stream_observer_desc_aoa_get_stream_single),
        AoaGetStreamDual("UsbAccessoryService.onGetBody(mode=dual)", R.string.stream_observer_desc_aoa_get_stream_dual),
        AoaHandleNewMethod("UsbAccessoryService.handleNewMethod", R.string.stream_observer_desc_aoa_handle_new_method),
        AoaHandleOldMethod("UsbAccessoryService.handleOldMethod", R.string.stream_observer_desc_aoa_handle_old_method),
        AoaPutVideoBuffer("UsbAccessoryService.putVideoBuffer(buffer video data)", R.string.stream_observer_desc_aoa_put_video_buffer),
        AoaPutVideoNotBuffer("UsbAccessoryService.putVideoBuffer(not buffer video data)", R.string.stream_observer_desc_aoa_put_video_not_buffer),
        AoaTransVideoDataNeedPack("UsbAccessoryService.toTransVideoData(need packed)", R.string.stream_observer_desc_aoa_trans_video_data_need_pack),
        AoaTransVideoDataNoNeedPack("UsbAccessoryService.toTransVideoData(no need to pack)", R.string.stream_observer_desc_aoa_trans_video_data_no_need_pack),
        UdtRunnable("DJIUdtSocket.runnable.run", R.string.stream_observer_desc_udt_runnable),
        P3cService("P3CLiveStreamService.parse(need pack not raw)", R.string.stream_observer_desc_p3c_service),
        MammothService("MammothStreamServices.parse(stream need pack)", R.string.stream_observer_desc_mammoth_service),
        SwUdpServiceParse("SwUdpService.parse(stream need pack)", R.string.stream_observer_desc_sw_udp_service_parse),
        DPadCmdServiceParse("DPadCmd.parse(cmd pack into PackManager.parse())", R.string.stream_observer_desc_dpad_cmd_service_parse),
        DPadUdpServiceParse("DPadUdpStream.parse(stream need pack)", R.string.stream_observer_desc_dpad_udp_service_parse),
        VideoDataRecverNotRaw("DJIVideoDataRecever.onVideoRecv(not raw stream)", R.string.stream_observer_desc_video_data_recver_not_raw),
        DecoderQueueIn("DJIVideoDecoder.onServerQueuein", R.string.stream_observer_desc_decoder_queue_in),
        DecoderQueueToCodec("DJIVideoDecoder.queueToCodec", R.string.stream_observer_desc_decoder_queue_to_codec),
        GlyuvSurfaceDisplay("GLYUVSurface.display", R.string.stream_observer_desc_glyuv_surface_display),
        OnlineTranscoderFrameInput("OnlineTranscoder.onH264FrameInput", R.string.stream_observer_desc_online_transcoder_frame_input),
        OnlineTranscoderRun("OnlineTranscoder.run", R.string.stream_observer_desc_online_transcoder_run),
        FullFrameTranscodrEncData("FullFrameHardwareTranscoder.onEncodeData", R.string.stream_observer_desc_full_frame_transcodr_enc_data),
        RecorderMp4Input("RecorderMp4.onFrameInput", R.string.stream_observer_desc_recorder_mp4_input),
        RecorderAudioMp4Input("RecorderAudioMp4.onFrameInput", R.string.stream_observer_desc_recorder_audio_mp4_input),
        RecorderFullFrameInput("RecorderFullFrame.onFrameInput", R.string.stream_observer_desc_recorder_full_frame_input),
        RecorderAudioFullFrameInput("RecorderAudioFullFrame.onFrameInput", R.string.stream_observer_desc_recorder_audio_full_frame_input),
        RecorderQuickMovieNormal("RecorderQuickMovie.normalEncoderListener", R.string.stream_observer_desc_recorder_quick_movie_normal),
        RecorderQuickMovieQuick("RecorderQuickMovie.quickMovieEncoderListener", R.string.stream_observer_desc_recorder_quick_movie_quick),
        AudioRecord("DJIAudioRecordWrapper.record", R.string.stream_observer_desc_audio_record),
        RecorderSpeedAdjustInput("RecorderSpeedAdjust.onFrameInput", R.string.stream_observer_desc_recorder_speed_adjust_input),
        SendMessage("DJIServiceInterface.sendMessage", R.string.stream_observer_desc_send_message);
        
        private int descId;
        private String descString;
        private String pos;

        private ObservingPoint(String pos2, int descId2) {
            this.pos = pos2;
            this.descId = descId2;
        }

        public int getDescId() {
            return this.descId;
        }

        public String getDesc() {
            if (this.descString == null) {
                this.descString = ServiceManager.getContext().getString(this.descId);
            }
            return this.descString;
        }

        public String getPos() {
            return this.pos;
        }
    }

    public enum ObservingContext {
        ByteRate(StreamDataObserver.PARAM_BYTE_RATE),
        TotalByteCount(StreamDataObserver.PARAM_TOTAL_BYTE_COUNT),
        FrameRate("frame_rate"),
        TotalFrameCount(StreamDataObserver.PARAM_TOTAL_FRAME_COUNT),
        Width("width"),
        Height("height"),
        KeyframeNum(StreamDataObserver.PARAM_KEYFRAME_NUM),
        SpsppsNum(StreamDataObserver.PARAM_SPSPPS_NUM);
        
        /* access modifiers changed from: private */
        public String desc;

        private ObservingContext(String desc2) {
            this.desc = desc2;
        }

        public String toString() {
            return this.desc;
        }
    }

    public static StreamDataObserver getInstance(ObservingPoint observingPoint2, UsbAccessoryService.VideoStreamSource source2) {
        StreamDataObserver observer = observerArray[observingPoint2.ordinal()][source2.ordinal()];
        if (observer != null) {
            return observer;
        }
        StreamDataObserver observer2 = new StreamDataObserver(observingPoint2, source2);
        observerArray[observingPoint2.ordinal()][source2.ordinal()] = observer2;
        return observer2;
    }

    public static StreamDataObserver getInstance(ObservingPoint observingPoint2) {
        return getInstance(observingPoint2, UsbAccessoryService.VideoStreamSource.Unknown);
    }

    public static void addListener(IStreamDataObserverGlobalListener listener) {
        synchronized (listenerLock) {
            if (listener != null) {
                if (!listenerList.contains(listener)) {
                    listenerList.add(listener);
                    if (!isObserving()) {
                        startObserving();
                    }
                }
            }
        }
    }

    public static void removeListener(IStreamDataObserverGlobalListener listener) {
        synchronized (listenerLock) {
            if (listenerList.contains(listener)) {
                listenerList.remove(listener);
                if (isObserving() && listenerList.isEmpty()) {
                    stopObserving();
                }
            }
        }
    }

    public static Set<String> getKeys() {
        Set<String> rst = new LinkedHashSet<>();
        StreamDataObserver[][] streamDataObserverArr = observerArray;
        for (StreamDataObserver[] observerSubArr : streamDataObserverArr) {
            if (observerSubArr != null) {
                for (StreamDataObserver observer : observerSubArr) {
                    if (observer != null) {
                        rst.add(observer.observingPoint.getPos() + " source:" + observer.source.name());
                    }
                }
            }
        }
        return rst;
    }

    public static Set<String> getDescSet() {
        String sourceDesc = ServiceManager.getContext().getString(R.string.stream_observer_stream_source);
        Set<String> rst = new LinkedHashSet<>();
        StreamDataObserver[][] streamDataObserverArr = observerArray;
        for (StreamDataObserver[] observerSubArr : streamDataObserverArr) {
            if (observerSubArr != null) {
                for (StreamDataObserver observer : observerSubArr) {
                    if (observer != null) {
                        rst.add(observer.observingPoint.getDesc() + " " + sourceDesc + ":" + observer.source.name());
                    }
                }
            }
        }
        return rst;
    }

    /* access modifiers changed from: private */
    public static void invokeGlobalListener(Map<String, Map<String, Long>> rst) {
        synchronized (listenerLock) {
            for (IStreamDataObserverGlobalListener listener : listenerList) {
                listener.onByteRatesUpdate(rst);
            }
        }
    }

    private static void startObserving() {
        if (!(sObservingThread == null && sObservingHandler == null)) {
            stopObserving();
        }
        sObservingThread = new HandlerThread("stream data observing thread");
        sObservingThread.start();
        sObservingHandler = new Handler(sObservingThread.getLooper()) {
            /* class dji.midware.util.save.StreamDataObserver.AnonymousClass1 */

            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        String sourceDesc = ServiceManager.getContext().getString(R.string.stream_observer_stream_source);
                        Map unused = StreamDataObserver.sGlobalRst = new HashMap();
                        synchronized (StreamDataObserver.mapLock) {
                            StreamDataObserver[][] access$200 = StreamDataObserver.observerArray;
                            for (StreamDataObserver[] obsSubArr : access$200) {
                                if (obsSubArr != null) {
                                    for (StreamDataObserver observer : obsSubArr) {
                                        if (observer != null) {
                                            Map<String, Long> rate = observer.onObservingData();
                                            if (rate.size() > 0 && !StreamDataObserver.listenerList.isEmpty()) {
                                                StreamDataObserver.sGlobalRst.put(observer.observingPoint.getDesc() + ", " + sourceDesc + ":" + observer.source.name(), rate);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        StreamDataObserver.invokeGlobalListener(StreamDataObserver.sGlobalRst);
                        sendEmptyMessageDelayed(0, 1000);
                        return;
                    default:
                        return;
                }
            }
        };
        sObservingHandler.sendEmptyMessage(0);
        isRunning = true;
    }

    private static void stopObserving() {
        if (sObservingHandler != null) {
            sObservingHandler.removeCallbacksAndMessages(null);
            sObservingHandler = null;
        }
        isRunning = false;
        if (sObservingThread != null) {
            if (sObservingThread.isAlive()) {
                if (Build.VERSION.SDK_INT >= 18) {
                    sObservingThread.quitSafely();
                } else {
                    sObservingThread.quit();
                }
            }
            sObservingThread = null;
        }
    }

    public static boolean isObserving() {
        return isRunning;
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.util.Arrays.fill(long[], long):void}
     arg types: [long[], int]
     candidates:
      ClspMth{java.util.Arrays.fill(double[], double):void}
      ClspMth{java.util.Arrays.fill(byte[], byte):void}
      ClspMth{java.util.Arrays.fill(boolean[], boolean):void}
      ClspMth{java.util.Arrays.fill(char[], char):void}
      ClspMth{java.util.Arrays.fill(short[], short):void}
      ClspMth{java.util.Arrays.fill(java.lang.Object[], java.lang.Object):void}
      ClspMth{java.util.Arrays.fill(int[], int):void}
      ClspMth{java.util.Arrays.fill(float[], float):void}
      ClspMth{java.util.Arrays.fill(long[], long):void} */
    private StreamDataObserver(ObservingPoint observingPoint2, UsbAccessoryService.VideoStreamSource source2) {
        this.observingPoint = observingPoint2;
        this.source = source2;
        Arrays.fill(this.paramArr, -1L);
    }

    public StreamDataObserver onDataRecv(ObservingContext obsContext, long value) {
        if (isObserving()) {
            if (obsContext == ObservingContext.Width || obsContext == ObservingContext.Height) {
                this.paramArr[obsContext.ordinal()] = value;
            } else if (this.paramArr[obsContext.ordinal()] < 0) {
                this.paramArr[obsContext.ordinal()] = value;
            } else {
                long[] jArr = this.paramArr;
                int ordinal = obsContext.ordinal();
                jArr[ordinal] = jArr[ordinal] + value;
            }
            if (ObservingContext.ByteRate == obsContext) {
                onDataRecv(ObservingContext.TotalByteCount, value);
            }
            if (ObservingContext.FrameRate == obsContext) {
                onDataRecv(ObservingContext.TotalFrameCount, value);
            }
        }
        return this;
    }

    private void resetParamMap() {
        for (int i = 0; i < this.paramArr.length; i++) {
            if (!(i == ObservingContext.TotalByteCount.ordinal() || i == ObservingContext.TotalFrameCount.ordinal())) {
                this.paramArr[i] = -1;
            }
        }
    }

    /* access modifiers changed from: private */
    public Map<String, Long> onObservingData() {
        Map<String, Long> rst = new HashMap<>();
        ObservingContext[] values = ObservingContext.values();
        for (ObservingContext obsCtx : values) {
            long val = this.paramArr[obsCtx.ordinal()];
            if (val >= 0) {
                rst.put(obsCtx.desc, Long.valueOf(val));
            }
        }
        resetParamMap();
        return rst;
    }

    public void destroy() {
        this.paramArr = null;
        synchronized (mapLock) {
            StreamDataObserver[][] streamDataObserverArr = observerArray;
            for (StreamDataObserver[] obsSubArr : streamDataObserverArr) {
                if (obsSubArr != null) {
                    for (int i = 0; i < obsSubArr.length; i++) {
                        obsSubArr[i] = null;
                    }
                }
            }
        }
    }
}
