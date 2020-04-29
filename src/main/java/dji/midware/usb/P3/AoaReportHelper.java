package dji.midware.usb.P3;

import android.os.Handler;
import android.os.Message;
import dji.log.DJILog;
import dji.log.DJILogHelper;
import dji.log.DJILogUtils;
import dji.midware.component.DJIComponentManager;
import dji.midware.data.manager.P3.DataCameraEvent;
import dji.midware.util.FileRecorder;
import dji.midware.util.MidwareBackgroundLooper;
import java.io.File;
import java.util.Locale;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class AoaReportHelper {
    private static int BLOCK_TIMEOUT = 3000;
    /* access modifiers changed from: private */
    public static int MSG_TIMEOUT = 1126;
    private static final String TAG = "AoaReportHelper";
    public static final String TAG_CONNECT_DEBUG = "ConnectDebug";
    private AoaReportListener aoaReportListener;
    private DataCameraEvent cameraEvent;
    private long cmdByteCount;
    private String fileDir;
    private FileRecorder fileRecorder;
    private Handler handler;
    private boolean isSaveAoaData;
    private long lastCameraEventUpdateTime;
    private long lastT;

    public interface AoaReportListener {
        void onReadBlock();

        void onReadError();

        void onWriteError();
    }

    private AoaReportHelper() {
        this.handler = new Handler(MidwareBackgroundLooper.getLooper()) {
            /* class dji.midware.usb.P3.AoaReportHelper.AnonymousClass1 */

            public void handleMessage(Message msg) {
                if (msg.what == AoaReportHelper.MSG_TIMEOUT) {
                }
            }
        };
        this.lastT = 0;
        this.cmdByteCount = 0;
        this.cameraEvent = null;
        this.lastCameraEventUpdateTime = -1;
        this.fileRecorder = null;
        this.fileDir = DJILogHelper.getInstance().getLogParentDir() + "WM230Cmd" + File.separator;
        this.isSaveAoaData = false;
        EventBus.getDefault().register(this);
    }

    public static AoaReportHelper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final AoaReportHelper INSTANCE = new AoaReportHelper();

        private SingletonHolder() {
        }
    }

    public void setAoaReportListener(AoaReportListener aoaReportListener2) {
        this.aoaReportListener = aoaReportListener2;
    }

    public void readError() {
        if (this.aoaReportListener != null) {
            this.aoaReportListener.onReadError();
        }
        DJILog.saveAsync(TAG, System.currentTimeMillis() + " readError");
    }

    public void writeError() {
        if (this.aoaReportListener != null) {
            this.aoaReportListener.onWriteError();
        }
        DJILog.saveAsync(TAG, System.currentTimeMillis() + " writeError");
    }

    public void startRead() {
        this.handler.sendEmptyMessageDelayed(MSG_TIMEOUT, (long) BLOCK_TIMEOUT);
    }

    public void endRead() {
        this.handler.removeMessages(MSG_TIMEOUT);
    }

    private void readBlock() {
        if (this.aoaReportListener != null) {
            this.aoaReportListener.onReadBlock();
        }
        DJILog.saveAsync(TAG, System.currentTimeMillis() + " readBlock");
    }

    public void reciveCmdChanel(byte[] buffer, int offset, int len) {
        saveCmdRate(len);
        detectBugHappen();
    }

    private void saveCmdRate(int len) {
        this.cmdByteCount += (long) len;
        if (System.currentTimeMillis() - this.lastT > 2000) {
            float rate = (((float) this.cmdByteCount) * 0.5f) / 1024.0f;
            DJILog.saveConnectDebug(System.currentTimeMillis() + String.format(Locale.US, "cmd rate %.2f KB\n", Float.valueOf(rate)));
            this.lastT = System.currentTimeMillis();
            this.cmdByteCount = 0;
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraEvent event) {
        this.cameraEvent = event;
        this.lastCameraEventUpdateTime = System.currentTimeMillis();
    }

    private void detectBugHappen() {
        if (this.cameraEvent != null && this.lastCameraEventUpdateTime > 0) {
            if (DJIComponentManager.getInstance().getLastPlatformType() != DJIComponentManager.PlatformType.WM230 && DJIComponentManager.getInstance().getLastPlatformType() != DJIComponentManager.PlatformType.WM240) {
                return;
            }
            if (this.cameraEvent != DataCameraEvent.ConnectLose || System.currentTimeMillis() - this.lastCameraEventUpdateTime <= 1000) {
                this.isSaveAoaData = false;
            } else {
                this.isSaveAoaData = true;
            }
        }
    }

    public void reciveAoaData(byte[] buffer, int offset, int len) {
        if (this.isSaveAoaData) {
            File dir = new File(this.fileDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            if (this.fileRecorder == null) {
                this.fileRecorder = new FileRecorder(this.fileDir + DJILogUtils.formatNow("yyyy-MM-dd+HH-mm-ss"));
            }
            this.fileRecorder.onRecvData(buffer, offset, len);
        } else if (this.fileRecorder != null) {
            this.fileRecorder.release();
            this.fileRecorder = null;
        }
    }
}
