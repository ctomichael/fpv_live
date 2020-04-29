package dji.log;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Keep;
import android.util.Log;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.DeviceType;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

@Keep
@EXClassNullAway
class LogHelper {
    private static final String LOG_FORMAT = "[%1$s][%2$s][%3$s]:%4$s";
    protected static final int MAX_LINES = 100;
    private static final int MSG_ID_UPDATE_LOG = 4096;
    private static final String STR_NULL = "null";
    private static final String STR_UNKNOWN = "Unknown";
    private static LogHelper mIntance;
    /* access modifiers changed from: private */
    public static String mLog = "";
    private HashMap<DeviceType, ArrayList<String>> list = new HashMap<>();
    private volatile boolean mClosed = true;
    private Context mContext = null;
    private volatile boolean mInit = false;
    private LogDialog mLogDialog = null;
    private LogHandler mLogHandler = null;
    private LogHandlerThread mLogThread = null;
    private final LogUIHandler mLogUIHandler = new LogUIHandler(this);

    protected static synchronized LogHelper createIntance(Context context) {
        LogHelper logHelper;
        synchronized (LogHelper.class) {
            if (mIntance == null) {
                mIntance = new LogHelper(context);
            }
            logHelper = mIntance;
        }
        return logHelper;
    }

    protected static synchronized LogHelper getIntance() {
        LogHelper logHelper;
        synchronized (LogHelper.class) {
            logHelper = mIntance;
        }
        return logHelper;
    }

    private LogHelper(Context context) {
        if (DJILog.controller().print) {
            initializeHelper(context);
        }
    }

    private synchronized void initializeHelper(Context context) {
        if (!this.mInit) {
            this.mContext = context.getApplicationContext();
            this.mLogThread = new LogHandlerThread("djilog-1");
            this.mLogThread.start();
            this.mLogHandler = new LogHandler(this, this.mLogThread.getLooper());
            this.mClosed = true;
            this.mInit = true;
            showDialog("<< log dump start now >>");
        }
    }

    private synchronized void finalizeHelper() {
        if (this.mInit) {
            closeLog();
            this.mLogHandler.removeMessages(4096);
            this.mLogHandler = null;
            this.mLogThread.quit();
            this.mLogThread = null;
            this.mLogUIHandler.removeMessages(4096);
            this.mContext = null;
            this.mInit = false;
        }
    }

    /* access modifiers changed from: protected */
    public void updateLog(DeviceType deviceType, String log) {
        if (canDo()) {
            if (deviceType == DeviceType.APP && log == null) {
                log = STR_NULL;
            }
            synchronized (this) {
                if (this.list.containsKey(deviceType)) {
                    ArrayList<String> items = this.list.get(deviceType);
                    if (items.size() == 100) {
                        items.remove(0);
                    }
                    items.add(log);
                    this.list.put(deviceType, items);
                } else {
                    ArrayList<String> items2 = new ArrayList<>();
                    items2.add(log);
                    this.list.put(deviceType, items2);
                }
            }
            this.mLogHandler.obtainMessage(4096).sendToTarget();
        }
    }

    /* access modifiers changed from: protected */
    public void updateLog() {
        this.mLogHandler.obtainMessage(4096).sendToTarget();
    }

    /* access modifiers changed from: protected */
    public void autoHandle() {
        if (canDo()) {
            Log.d("", "click autoHandle " + this.mClosed);
            if (this.mClosed) {
                openLog();
            } else {
                closeLog();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void openLog() {
        if (canDo() && this.mClosed) {
            this.mClosed = false;
            this.mLogUIHandler.obtainMessage(4096).sendToTarget();
        }
    }

    /* access modifiers changed from: protected */
    public void closeLog() {
        if (canDo() && !this.mClosed) {
            this.mClosed = true;
            this.mLogHandler.removeMessages(4096);
            this.mLogUIHandler.removeMessages(4096);
            hideDialog();
        }
    }

    /* access modifiers changed from: private */
    public void showDialog(String log) {
        if (this.mLogDialog == null) {
            this.mLogDialog = new LogDialog(this.mContext);
        }
        if (!this.mClosed) {
            if (this.mLogDialog != null && !this.mLogDialog.isShowing()) {
                this.mLogDialog.show();
                Log.d("", "click show");
            }
            this.mLogDialog.updateLog(log);
        }
    }

    private void hideDialog() {
        Log.d("", "click hideDialog");
        if (this.mLogDialog != null && this.mLogDialog.isShowing()) {
            this.mLogDialog.dismiss();
        }
    }

    /* access modifiers changed from: private */
    public boolean canDo() {
        return this.mInit;
    }

    /* access modifiers changed from: private */
    public void updateLogInternal() {
        if (canDo()) {
            synchronized (this) {
                StringBuilder sb = new StringBuilder();
                Iterator<DeviceType> it2 = this.list.keySet().iterator();
                while (true) {
                    if (!it2.hasNext()) {
                        break;
                    }
                    DeviceType deviceType = it2.next();
                    DeviceType d = this.mLogDialog.getDeviceType();
                    if (d != null && d == deviceType) {
                        ArrayList<String> items = this.list.get(deviceType);
                        int size = items.size();
                        for (int i = 0; i < size; i++) {
                            sb.append((String) items.get(i)).append("\n");
                        }
                    }
                }
                mLog = sb.toString();
            }
            this.mLogUIHandler.obtainMessage(4096).sendToTarget();
        }
    }

    @Keep
    private static class LogUIHandler extends Handler {
        private final WeakReference<LogHelper> mOutCls;

        public LogUIHandler(LogHelper helper) {
            super(Looper.getMainLooper());
            this.mOutCls = new WeakReference<>(helper);
        }

        public void handleMessage(Message msg) {
            LogHelper helper = this.mOutCls.get();
            if (helper != null && helper.canDo()) {
                switch (msg.what) {
                    case 4096:
                        helper.showDialog(LogHelper.mLog);
                        return;
                    default:
                        return;
                }
            }
        }
    }

    @Keep
    private static class LogHandlerThread extends HandlerThread {
        public LogHandlerThread(String name) {
            this(name, 1);
        }

        public LogHandlerThread(String name, int priority) {
            super(name, priority);
        }
    }

    @Keep
    private static class LogHandler extends Handler {
        private final WeakReference<LogHelper> mOutCls;

        public LogHandler(LogHelper helper, Looper looper) {
            super(looper);
            this.mOutCls = new WeakReference<>(helper);
        }

        public void handleMessage(Message msg) {
            LogHelper helper = this.mOutCls.get();
            if (helper != null && helper.canDo()) {
                switch (msg.what) {
                    case 4096:
                        helper.updateLogInternal();
                        return;
                    default:
                        return;
                }
            }
        }
    }
}
