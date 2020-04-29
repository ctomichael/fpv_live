package dji.midware.data.manager.P3;

import android.util.SparseArray;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.queue.P3.Queue;

@EXClassNullAway
@Deprecated
public class DJIQueueManager {
    private static DJIQueueManager instance = null;
    private SparseArray<Queue> queueArray = new SparseArray<>();

    public static synchronized DJIQueueManager getInstance() {
        DJIQueueManager dJIQueueManager;
        synchronized (DJIQueueManager.class) {
            if (instance == null) {
                instance = new DJIQueueManager();
            }
            dJIQueueManager = instance;
        }
        return dJIQueueManager;
    }

    public Queue getQueue(CmdSet cmdSet) {
        if (cmdSet == null) {
            return null;
        }
        if (this.queueArray == null) {
            this.queueArray = new SparseArray<>();
        }
        Queue queue = this.queueArray.get(cmdSet.value());
        if (queue != null) {
            return queue;
        }
        Queue queue2 = new Queue();
        this.queueArray.put(cmdSet.value(), queue2);
        return queue2;
    }

    public Queue getQueue(int cmdSet) {
        return getQueue(CmdSet.find(cmdSet));
    }

    private void destroys() {
        this.queueArray = null;
    }

    public static synchronized void destroy() {
        synchronized (DJIQueueManager.class) {
            if (instance != null) {
                instance.destroys();
                instance = null;
            }
        }
    }
}
