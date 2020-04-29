package dji.midware.data.manager.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;
import dji.midware.data.packages.P3.RecvPack;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;

@Keep
@EXClassNullAway
public class DJIVideoPackManager extends DJIPackManagerBase {
    private static DJIVideoPackManager instance = null;
    private int cttt = 0;
    /* access modifiers changed from: private */
    public long currentSize;
    private File file = new File("/sdcard/DJI/save3s.file");
    /* access modifiers changed from: private */
    public boolean isStart = false;
    private boolean isStartParse = false;
    private OnStartListener onStartListener;
    private Timer progTimer;
    private Runnable runnable = new Runnable() {
        /* class dji.midware.data.manager.P3.DJIVideoPackManager.AnonymousClass1 */

        /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
         method: dji.midware.data.manager.P3.DJIVideoPackManager.access$102(dji.midware.data.manager.P3.DJIVideoPackManager, long):long
         arg types: [dji.midware.data.manager.P3.DJIVideoPackManager, int]
         candidates:
          dji.midware.data.manager.P3.DJIPackManagerBase.access$102(dji.midware.data.manager.P3.DJIPackManagerBase, int):int
          dji.midware.data.manager.P3.DJIVideoPackManager.access$102(dji.midware.data.manager.P3.DJIVideoPackManager, long):long */
        public void run() {
            DJILogHelper.getInstance().LOGD("", "isstart=" + DJIVideoPackManager.this.isStart + " rate=" + DJIVideoPackManager.this.currentSize + "b/s", false, true);
            long unused = DJIVideoPackManager.this.currentSize = 0L;
        }
    };
    private boolean saveFile = false;
    private FileOutputStream stream;
    private long tmpSize;

    public interface OnStartListener {
        void onStart();
    }

    public static synchronized DJIVideoPackManager getInstance() {
        DJIVideoPackManager dJIVideoPackManager;
        synchronized (DJIVideoPackManager.class) {
            if (instance == null) {
                instance = new DJIVideoPackManager();
            }
            dJIVideoPackManager = instance;
        }
        return dJIVideoPackManager;
    }

    private DJIVideoPackManager() {
    }

    public void clearVideoData() {
    }

    /* access modifiers changed from: protected */
    public void handleAirConnection(RecvPack pack) {
    }

    public void setOnStartListener(OnStartListener onStartListener2) {
        this.onStartListener = onStartListener2;
    }

    public void start() {
        clearVideoData();
        ServiceManager.getInstance().setDataMode(true);
        this.isStart = true;
        if (this.progTimer != null) {
            this.progTimer.cancel();
            this.progTimer = null;
        }
    }

    public boolean isStart() {
        return this.isStart;
    }

    public void stop() {
        if (this.progTimer != null) {
            this.progTimer.cancel();
            this.progTimer = null;
        }
        this.isStart = false;
        this.isStartParse = false;
        if (this.stream != null) {
            try {
                this.stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.stream = null;
        }
        ServiceManager.getInstance().setDataMode(false);
    }

    public synchronized void parseData(byte[] videoBuffer, int offset, int count) {
        if (this.isStart) {
            this.currentSize += (long) count;
            if (this.onStartListener != null) {
                this.onStartListener.onStart();
            }
            if (!this.isStartParse) {
                this.isStartParse = true;
                if (this.saveFile) {
                    if (this.file.exists()) {
                        this.file.delete();
                    }
                    try {
                        this.file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (this.stream == null) {
                        try {
                            this.stream = new FileOutputStream(this.file);
                        } catch (FileNotFoundException e2) {
                            e2.printStackTrace();
                        }
                    }
                }
            }
            if (count > 0) {
                if (count == 6) {
                }
                if (this.saveFile && this.stream != null) {
                    try {
                        this.stream.write(videoBuffer, offset, count);
                        this.stream.flush();
                    } catch (IOException e3) {
                        e3.printStackTrace();
                    }
                }
                parse(videoBuffer, offset, count);
            }
        }
    }
}
