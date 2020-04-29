package dji.midware.data.manager.P3;

import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.midware.data.model.common.ByteObject;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@EXClassNullAway
public class DJIPayloadUsbDataManager {
    private static final String TAG = "DJIPayloadUsbDataManager";
    private static volatile DJIPayloadUsbDataManager instance = null;
    private final long WARN_PERIOD = 2000;
    /* access modifiers changed from: private */
    public PayloadUsbDataListener dataListener;
    private long lastTimeBlocking;
    /* access modifiers changed from: private */
    public BlockingQueue<ByteObject> queue = new LinkedBlockingQueue(1024);
    private Thread thread = new Thread(TAG) {
        /* class dji.midware.data.manager.P3.DJIPayloadUsbDataManager.AnonymousClass1 */

        public void run() {
            while (!isInterrupted()) {
                try {
                    ByteObject byteObject = (ByteObject) DJIPayloadUsbDataManager.this.queue.take();
                    if (DJIPayloadUsbDataManager.this.dataListener != null) {
                        DJIPayloadUsbDataManager.this.dataListener.onDataInput(byteObject.getBytes(), byteObject.getValidLen());
                    }
                    byteObject.recycle();
                } catch (Exception e) {
                    DJILog.e(DJIPayloadUsbDataManager.TAG, "take error", new Object[0]);
                }
            }
        }
    };

    public interface PayloadUsbDataListener {
        void isBlocking();

        void onDataInput(byte[] bArr, int i);
    }

    public static DJIPayloadUsbDataManager getInstance() {
        if (instance == null) {
            synchronized (DJIPayloadUsbDataManager.class) {
                if (instance == null) {
                    instance = new DJIPayloadUsbDataManager();
                }
            }
        }
        return instance;
    }

    public void feedData(ByteObject object) {
        if (this.queue.remainingCapacity() > 0) {
            try {
                this.queue.put(object);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            object.recycle();
            if (this.dataListener != null && System.currentTimeMillis() - this.lastTimeBlocking > 2000) {
                this.dataListener.isBlocking();
                this.lastTimeBlocking = System.currentTimeMillis();
            }
        }
    }

    private DJIPayloadUsbDataManager() {
        this.thread.start();
    }

    public void setDataListener(PayloadUsbDataListener listener) {
        this.dataListener = listener;
    }
}
