package dji.midware.usb.P3;

import android.os.Process;
import android.os.SystemClock;
import android.util.Log;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import dji.midware.aoabridge.AoaController;
import dji.midware.data.manager.P3.DJIServiceInterface;
import dji.midware.parser.plugins.DJIPluginRingBufferParser;
import dji.midware.parser.plugins.DisruptorAoaBufferParser;
import dji.midware.util.BytesUtil;
import dji.midware.util.save.StreamDelaySaver;
import dji.midware.util.save.StreamSaver;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.concurrent.ThreadFactory;

public class AoaRawDataReceiver {
    public static boolean IS_ENNABLE_PARSE_FOR_INNER = true;
    private static final boolean IS_PRINT_RATE = true;
    private static final boolean IS_SAVE_DATA = false;
    public static boolean PRINT_DATA_LENGTH = false;
    public static final String TAG = "Hybrid_Recieve_Thread";
    ThreadFactory disruptorThreadFactory = new ThreadFactory() {
        /* class dji.midware.usb.P3.AoaRawDataReceiver.AnonymousClass1 */

        public Thread newThread(Runnable r) {
            Thread ret = new Thread(r, "recv-disruptor");
            ret.setPriority(9);
            return ret;
        }
    };
    /* access modifiers changed from: private */
    public boolean isPauseService = false;
    /* access modifiers changed from: private */
    public volatile boolean isRunning;
    /* access modifiers changed from: private */
    public InputStream mAoaInputStream;
    /* access modifiers changed from: private */
    public int mIOErrorCount = 0;
    /* access modifiers changed from: private */
    public DJIServiceInterface mLinkService;
    /* access modifiers changed from: private */
    public OnParseEndListener mOnParseEndListener;
    private Runnable mParseRawDataRunnable = new Runnable() {
        /* class dji.midware.usb.P3.AoaRawDataReceiver.AnonymousClass2 */

        public void run() {
            boolean z = true;
            Process.setThreadPriority(-1);
            boolean unused = AoaRawDataReceiver.this.isRunning = true;
            StringBuilder append = new StringBuilder().append("RecvOsdRunnable ");
            if (!AoaRawDataReceiver.this.mLinkService.isConnected() || !AoaRawDataReceiver.this.isRunning) {
                z = false;
            }
            AoaLogUtil.printAndSave(append.append(z).toString());
            byte[] buffer = new byte[16384];
            long last_log_time = -1;
            AoaRawDataReceiver.this.initDisruptor();
            while (AoaRawDataReceiver.this.mLinkService.isConnected() && AoaRawDataReceiver.this.isRunning && AoaRawDataReceiver.this.mAoaInputStream != null) {
                if (UsbAccessoryService.STREAM_DEBUG) {
                    if (last_log_time == -1) {
                        last_log_time = SystemClock.uptimeMillis();
                    }
                    if (SystemClock.uptimeMillis() - last_log_time > 1000) {
                        last_log_time = SystemClock.uptimeMillis();
                    }
                }
                if (AoaRawDataReceiver.this.isPauseService || !AoaRawDataReceiver.IS_ENNABLE_PARSE_FOR_INNER) {
                    try {
                        Thread.sleep(0, 200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        int length = AoaRawDataReceiver.this.mAoaInputStream.read(buffer);
                        int unused2 = AoaRawDataReceiver.this.mIOErrorCount = 0;
                        if (length > 0) {
                            AoaRawDataReceiver.this.handleLogLogic(buffer, length);
                            AoaRawDataReceiver.this.mRingBufferParser.parse(buffer, 0, length);
                            AoaReportHelper.getInstance().reciveAoaData(buffer, 0, length);
                        } else if (length < 0) {
                            if (AoaRawDataReceiver.PRINT_DATA_LENGTH) {
                                AoaLogUtil.printAndSave("osdEndpoint recv err, length: " + length);
                            }
                        }
                    } catch (IOException e2) {
                        Log.d("socketclose", "client close : " + System.currentTimeMillis());
                        AoaRawDataReceiver.access$508(AoaRawDataReceiver.this);
                        if (AoaRawDataReceiver.this.mIOErrorCount % 10 == 0) {
                            boolean unused3 = AoaRawDataReceiver.this.isRunning = false;
                            InputStream unused4 = AoaRawDataReceiver.this.mAoaInputStream = null;
                            if (AoaRawDataReceiver.this.mOnParseEndListener != null) {
                                AoaRawDataReceiver.this.mOnParseEndListener.onReceiverEnd();
                            }
                            AoaLogUtil.printAndSave("连续收包出现10次异常，底层连接不行 RecvOsdRunnable IOException " + e2.getMessage());
                        }
                    }
                }
            }
            AoaRawDataReceiver.this.mRingBufferParser.shutDown();
            Thread unused5 = AoaRawDataReceiver.this.mReceiverThread = null;
            if (AoaRawDataReceiver.this.mRecvDisruptor != null) {
                AoaRawDataReceiver.this.mRecvDisruptor.shutdown();
            }
            AoaLogUtil.printAndSave("recvBufferThread.end");
        }
    };
    /* access modifiers changed from: private */
    public Thread mReceiverThread;
    /* access modifiers changed from: private */
    public Disruptor<RecvBufferEvent> mRecvDisruptor;
    /* access modifiers changed from: private */
    public DJIPluginRingBufferParser mRingBufferParser;
    private File mTestDataFile = new File("/sdcard/aoa_recv.bin");
    private FileOutputStream mTestDataFileOutputStream;

    public interface OnParseEndListener {
        void onReceiverEnd();
    }

    static /* synthetic */ int access$508(AoaRawDataReceiver x0) {
        int i = x0.mIOErrorCount;
        x0.mIOErrorCount = i + 1;
        return i;
    }

    public AoaRawDataReceiver(DJIPluginRingBufferParser parser, DJIServiceInterface linkService, OnParseEndListener endListener) {
        this.mRingBufferParser = parser;
        this.mLinkService = linkService;
        this.mOnParseEndListener = endListener;
    }

    public void startParse(InputStream aoaInputStream) {
        this.mAoaInputStream = aoaInputStream;
        this.mReceiverThread = new Thread(this.mParseRawDataRunnable, "recvBufferThread");
        this.mReceiverThread.setPriority(9);
        this.mReceiverThread.start();
    }

    public void stopParse() {
        this.isRunning = false;
        this.mIOErrorCount = 0;
    }

    public void setPauseService(boolean pauseService) {
        this.isPauseService = pauseService;
    }

    /* access modifiers changed from: private */
    public void initDisruptor() {
        if (this.mRingBufferParser instanceof DisruptorAoaBufferParser) {
            this.mRecvDisruptor = new Disruptor<>(new RecvBufferEventFactory(), 16, this.disruptorThreadFactory, ProducerType.SINGLE, new YieldingWaitStrategy());
            ((DisruptorAoaBufferParser) this.mRingBufferParser).setRingBuffer(this.mRecvDisruptor.getRingBuffer());
            if (this.mRingBufferParser instanceof EventHandler) {
                this.mRecvDisruptor.handleEventsWith((EventHandler) this.mRingBufferParser);
            }
            this.mRecvDisruptor.start();
        }
    }

    /* access modifiers changed from: private */
    public void handleLogLogic(byte[] buffer, int length) {
        if (AoaController.get().isEnable() && AoaController.get().isProxy()) {
            AoaController.get().sendByte(buffer, length);
        }
        AoaLogUtil.printRate(length);
        if (StreamDelaySaver.IS_SAVE_PACKET_DELAY) {
            try {
                StreamDelaySaver.getInstance().packetDelayFile.append((CharSequence) String.format(Locale.US, "   [HybridReceived] word 0: %X word 1: %X word 2: %X size=%d time=%d \n", Integer.valueOf(BytesUtil.getInt(buffer, 0)), Integer.valueOf(BytesUtil.getInt(buffer, 4)), Integer.valueOf(BytesUtil.getInt(buffer, 8)), Integer.valueOf(length), Long.valueOf(System.currentTimeMillis())));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (StreamSaver.SAVE_usbHybridDataStream_Open) {
            StreamSaver.getInstance(StreamSaver.Save_usbHybridDataStream_Name).write(buffer, 0, length);
        }
    }
}
