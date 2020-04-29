package dji.midware.media;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import dji.log.DJILog;
import dji.publics.DJIExecutor;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SaveLogHelper {
    private static final int MSG_LOG = 0;
    private static final int MSG_SAVE = 1;
    private static final String TAG = "SaveLogHelper";
    private DateFormat dateFormat;
    /* access modifiers changed from: private */
    public Handler handler;
    private Handler.Callback handlerCallback;
    /* access modifiers changed from: private */
    public long interval;
    /* access modifiers changed from: private */
    public String name;
    /* access modifiers changed from: private */
    public StringBuffer stringBuffer;

    public SaveLogHelper(String name2, long interval2, Looper looper) {
        this.stringBuffer = new StringBuffer();
        this.handlerCallback = new Handler.Callback() {
            /* class dji.midware.media.SaveLogHelper.AnonymousClass1 */

            public boolean handleMessage(Message message) {
                switch (message.what) {
                    case 0:
                        SaveLogHelper.this.stringBuffer.append("\n").append(message.obj);
                        if (!SaveLogHelper.this.handler.hasMessages(1)) {
                            SaveLogHelper.this.handler.sendEmptyMessageDelayed(1, SaveLogHelper.this.interval);
                            break;
                        }
                        break;
                    case 1:
                        DJILog.saveLog(SaveLogHelper.this.stringBuffer.toString(), SaveLogHelper.this.name);
                        SaveLogHelper.this.stringBuffer.delete(0, SaveLogHelper.this.stringBuffer.length());
                        break;
                }
                return true;
            }
        };
        if (looper != null) {
            this.handler = new Handler(looper, this.handlerCallback);
        } else {
            this.handler = new Handler(DJIExecutor.getLooper(), this.handlerCallback);
        }
        if (name2 == null || "".equals(name2)) {
            this.name = TAG;
        } else {
            this.name = name2;
        }
        this.interval = interval2;
    }

    public SaveLogHelper(String name2, long interval2) {
        this(name2, interval2, null);
    }

    private String getTimeString() {
        if (this.dateFormat == null) {
            this.dateFormat = new SimpleDateFormat("HHmmss");
        }
        return this.dateFormat.format(new Date());
    }

    public void log(String log) {
        if (log != null && !"".equals(log)) {
            this.handler.obtainMessage(0, getTimeString() + ":" + log).sendToTarget();
        }
    }
}
