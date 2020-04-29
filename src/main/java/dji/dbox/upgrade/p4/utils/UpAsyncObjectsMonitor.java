package dji.dbox.upgrade.p4.utils;

import dji.fieldAnnotation.EXClassNullAway;
import java.util.concurrent.atomic.AtomicInteger;

@EXClassNullAway
public class UpAsyncObjectsMonitor {
    protected AtomicInteger liveSize = new AtomicInteger(0);
    /* access modifiers changed from: private */
    public UpAsyncObjMonitorListener monitorListener;
    int objectSize = 0;
    private final UpAsyncObject[] objects;

    public interface UpAsyncObjMonitorListener {
        void onMonitorOver();

        void onMonitorStart();
    }

    public UpAsyncObjectsMonitor(UpAsyncObject... objects2) {
        this.objects = objects2;
        this.objectSize = objects2.length;
        for (int i = 0; i < this.objectSize; i++) {
            objects2[i].setUpAsyncObjMonitorListener(new UpAsyncObjMonitorListener() {
                /* class dji.dbox.upgrade.p4.utils.UpAsyncObjectsMonitor.AnonymousClass1 */

                public void onMonitorStart() {
                    UpAsyncObjectsMonitor.this.liveSize.incrementAndGet();
                }

                public void onMonitorOver() {
                    if (UpAsyncObjectsMonitor.this.liveSize.decrementAndGet() <= 0) {
                        UpAsyncObjectsMonitor.this.monitorListener.onMonitorOver();
                    }
                }
            });
            objects2[i].startMonitor();
        }
    }

    public void setUpAsyncObjMonitorListener(UpAsyncObjMonitorListener monitorListener2) {
        this.monitorListener = monitorListener2;
    }

    public static class UpAsyncObject {
        boolean isStarted = false;
        private UpAsyncObjMonitorListener listener;

        public void startMonitor() {
            if (!this.isStarted) {
                this.isStarted = true;
                this.listener.onMonitorStart();
            }
        }

        public void stopMonitor() {
            if (this.isStarted) {
                this.isStarted = false;
                this.listener.onMonitorOver();
            }
        }

        public void setUpAsyncObjMonitorListener(UpAsyncObjMonitorListener listener2) {
            this.listener = listener2;
        }
    }
}
