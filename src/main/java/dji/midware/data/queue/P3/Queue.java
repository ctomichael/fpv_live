package dji.midware.data.queue.P3;

import android.util.SparseArray;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.packages.P3.Pack;
import dji.midware.data.queue.P3.QueueBase;
import java.util.ArrayList;

@EXClassNullAway
@Deprecated
public class Queue extends QueueBase {
    private int MAX_LENGTH;
    private SparseArray<QueueBase.Msg> list;
    private ArrayList<Integer> listKey;

    public Queue() {
        this.list = null;
        this.listKey = null;
        this.MAX_LENGTH = 100;
        this.list = new SparseArray<>();
        this.listKey = new ArrayList<>();
    }

    public void destroy() {
        this.list = null;
        this.listKey = null;
    }

    public synchronized QueueBase.Msg addMsg(Pack pack) {
        QueueBase.Msg item;
        item = new QueueBase.Msg();
        if (this.list.size() > this.MAX_LENGTH) {
            removeLast();
        }
        int key = getKey(pack);
        if (this.list.indexOfKey(key) >= 0) {
            this.list.put(key, item);
            this.listKey.remove(Integer.valueOf(key));
            this.listKey.add(Integer.valueOf(key));
        } else {
            this.list.append(key, item);
            this.listKey.add(Integer.valueOf(key));
        }
        return item;
    }

    private void removeLast() {
        if (this.listKey != null && this.list != null) {
            int i = 0;
            while (i < 40) {
                try {
                    this.list.remove(this.listKey.get(0).intValue());
                    this.listKey.remove(0);
                    i++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            this.list.size();
        }
    }

    public synchronized void setMsg(Pack pack) {
        QueueBase.Msg item = this.list.get(getKey(pack));
        if (item != null) {
            item.pack = pack;
            item.isResponse = true;
            try {
                synchronized (item) {
                    item.notifyAll();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return;
    }

    private int getKey(Pack pack) {
        return (pack.cmdId << 16) | pack.seq;
    }
}
