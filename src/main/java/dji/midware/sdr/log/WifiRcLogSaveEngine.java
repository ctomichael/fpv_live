package dji.midware.sdr.log;

import android.support.annotation.NonNull;
import android.util.Log;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ThreadFactory;

public class WifiRcLogSaveEngine implements EventHandler<WifiRcLogRawData> {
    private ThreadFactory disruptorThreadFactory = WifiRcLogSaveEngine$$Lambda$0.$instance;
    private boolean isEncryptMode;
    private Disruptor<WifiRcLogRawData> mRecvDisruptor;
    private CommonLogEncryption mSimpleCipher;

    static final /* synthetic */ Thread lambda$new$0$WifiRcLogSaveEngine(Runnable r) {
        Thread ret = new Thread(r, "wifi-rclog-disruptor");
        ret.setPriority(8);
        return ret;
    }

    WifiRcLogSaveEngine(boolean needEncrypt) {
        this.isEncryptMode = needEncrypt;
        this.mSimpleCipher = new CommonLogEncryption();
        this.mRecvDisruptor = new Disruptor<>(new WifiRcLogEventFactory(), 128, this.disruptorThreadFactory, ProducerType.MULTI, new YieldingWaitStrategy());
        this.mRecvDisruptor.handleEventsWith(this);
        this.mRecvDisruptor.start();
    }

    public void onDataReceive(@NonNull FileOutputStream fos, @NonNull byte[] rawData) {
        RingBuffer<WifiRcLogRawData> ringBuffer = this.mRecvDisruptor.getRingBuffer();
        long sequence = ringBuffer.next();
        try {
            ringBuffer.get(sequence).setData(fos, rawData);
        } finally {
            ringBuffer.publish(sequence);
        }
    }

    public void onDestroy() {
        this.mRecvDisruptor.shutdown();
    }

    public void onEvent(WifiRcLogRawData wifiRcLogRawData, long sequence, boolean endOfBatch) throws Exception {
        byte[] toWriteData = wifiRcLogRawData.getData();
        FileOutputStream fos = wifiRcLogRawData.getFos();
        if (toWriteData != null && fos != null) {
            try {
                if (this.isEncryptMode) {
                    toWriteData = this.mSimpleCipher.encrypt(toWriteData);
                }
                fos.write(toWriteData);
                fos.write(10);
            } catch (IOException ioe) {
                Log.e("WifiRcLogSaveEngine", "onEvent fos.write IOException:" + ioe);
                try {
                    fos.flush();
                } catch (Exception e) {
                    Log.e("WifiRcLogSaveEngine", "onEvent fos.flush error:" + e);
                }
                fos.close();
            }
        }
    }

    private final class WifiRcLogEventFactory implements EventFactory<WifiRcLogRawData> {
        private WifiRcLogEventFactory() {
        }

        public WifiRcLogRawData newInstance() {
            return new WifiRcLogRawData();
        }
    }
}
