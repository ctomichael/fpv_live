package dji.logic.album.manager.litchis;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.logic.album.manager.DJIAlbumCacheManager;
import dji.logic.album.manager.DJIAlbumInterface;
import dji.logic.album.model.DJIAlbumPullErrorType;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.model.litchis.DataCameraFileSystemAbort;
import dji.midware.data.queue.P3.PackUtil;
import dji.midware.util.DJIEventBusUtil;
import java.util.Timer;
import java.util.TimerTask;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public abstract class DJILoader<E> {
    protected static final String LOG_DIR = "playback";
    protected static final String LOG_TAG = DJILoader.class.getSimpleName();
    protected static final int MSG_FLUSH_DATA = 9;
    protected static final int MSG_RECV_DATA = 7;
    protected static final int MSG_RECV_OVER = 6;
    protected final int MSG_ACK_TIMER = 8;
    protected final int MSG_CHECK = 4;
    protected final int MSG_FAILURE = 1;
    protected final int MSG_PROGRESS = 2;
    protected final int MSG_RATE = 5;
    protected final int MSG_START = 3;
    protected final int MSG_SUCCESS = 0;
    protected final String TAG = getClass().getSimpleName();
    protected volatile int ackTimeoutCount = 0;
    protected DJIAlbumCacheManager cacheManager;
    protected int checkDelay = 1000;
    protected volatile int curSeq = 0;
    protected DeviceType deviceType = DeviceType.CAMERA;
    protected Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        /* class dji.logic.album.manager.litchis.DJILoader.AnonymousClass2 */

        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    DJILoader.this.stop();
                    DJILoader.this.listener.onSuccess(msg.obj);
                    break;
                case 1:
                    DJILoader.this.stop();
                    DJILoader.this.listener.onFailure((DJIAlbumPullErrorType) msg.obj);
                    break;
                case 2:
                    DJILoader.this.countProgress();
                    break;
                case 3:
                    DJILoader.this.listener.onStart();
                    break;
                case 4:
                    DJILoader.this.timeoutNum++;
                    if (DJILoader.this.timeoutNum <= DJILoader.this.timeOutMax) {
                        DJILoader.this.resendMe();
                        break;
                    } else {
                        DJILoader.this.clearTimeout();
                        DJILoader.this.abort(true);
                        DJILoader.this.listener.onFailure(DJIAlbumPullErrorType.TIMEOUT);
                        break;
                    }
                case 5:
                    DJILoader.this.countRate();
                    break;
                case 8:
                    DJILoader.this.ackTimeoutCount++;
                    DJILog.logWriteD("joe-playback", "ackTimeoutCount=" + DJILoader.this.ackTimeoutCount, DJILoader.LOG_DIR, new Object[0]);
                    if (DJILoader.this.ackTimeoutCount <= DJILoader.this.timeOutMax) {
                        DJILoader.this.resending = false;
                        DJILoader.this.resendWithSlidingWindow();
                        break;
                    } else {
                        DJILoader.this.clearAckTimeout();
                        DJILoader.this.abort(true);
                        DJILoader.this.listener.onFailure(DJIAlbumPullErrorType.TIMEOUT);
                        break;
                    }
            }
            return false;
        }
    });
    protected volatile boolean isAlive = false;
    protected boolean isRecvOver = false;
    protected DJIAlbumInterface.DJIAlbumPullListener<E> listener;
    protected Handler mParseHandler;
    private HandlerThread mParseThread;
    protected int nextSeq = 0;
    protected volatile int offset = 0;
    protected long offsetTmp = 0;
    protected Timer progTimer;
    protected int receiverIdInProtocol = -1;
    protected volatile boolean resending = false;
    protected int timeOutMax = 0;
    protected int timeoutNum = 0;

    public abstract void abort(boolean z);

    /* access modifiers changed from: protected */
    public abstract void countProgress();

    /* access modifiers changed from: protected */
    public abstract void countRate();

    public abstract void destroy();

    /* access modifiers changed from: protected */
    public abstract void reSend();

    /* access modifiers changed from: protected */
    public abstract void recvOver();

    public abstract void stop();

    public void setReceiverIdInProtocol(int receiverIdInProtocol2) {
        this.receiverIdInProtocol = receiverIdInProtocol2;
    }

    public int getReceiverIdInProtocol() {
        return this.receiverIdInProtocol;
    }

    public DeviceType getDeviceType() {
        return this.deviceType;
    }

    public void setDeviceType(DeviceType deviceType2) {
        if (deviceType2 != null) {
            this.deviceType = deviceType2;
        }
    }

    /* access modifiers changed from: protected */
    public void resendWithSlidingWindow() {
    }

    /* access modifiers changed from: protected */
    public void flushBuffer() {
    }

    public void start() {
        this.handler.sendEmptyMessage(3);
    }

    /* access modifiers changed from: protected */
    public void startMe() {
        synchronized (this) {
            checkPushStatus();
            if (this.progTimer != null) {
                this.progTimer.cancel();
                this.progTimer = null;
            }
            this.progTimer = new Timer("DJILoader");
            this.progTimer.schedule(new TimerTask() {
                /* class dji.logic.album.manager.litchis.DJILoader.AnonymousClass1 */

                public void run() {
                    DJILoader.this.handler.sendEmptyMessage(5);
                }
            }, 1000, 1000);
        }
    }

    /* access modifiers changed from: protected */
    public void stopMe() {
        synchronized (this) {
            this.isAlive = false;
            this.handler.removeMessages(4);
            if (this.progTimer != null) {
                this.progTimer.cancel();
                this.progTimer = null;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void destroyMe() {
        DJIEventBusUtil.unRegister(this);
        this.mParseThread.quit();
    }

    /* access modifiers changed from: protected */
    public void checkPushStatus() {
        this.timeoutNum = 0;
        freshPushStatus();
    }

    /* access modifiers changed from: protected */
    public void freshPushStatus() {
        this.handler.removeMessages(4);
        this.handler.sendEmptyMessageDelayed(4, (long) this.checkDelay);
    }

    /* access modifiers changed from: protected */
    public void clearTimeout() {
        this.handler.removeMessages(4);
        this.timeoutNum = 0;
    }

    /* access modifiers changed from: protected */
    public void clearAckTimeout() {
        this.handler.removeMessages(8);
        this.ackTimeoutCount = 0;
    }

    public DJILoader() {
        initParseThread();
        this.cacheManager = DJIAlbumCacheManager.getInstance();
        DJIEventBusUtil.register(this);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraFileSystemAbort abort) {
        if (this.isAlive) {
            DJILog.logWriteD(LOG_TAG, "abort session form drone" + abort.getRecvPack().sessionId + " current session id:" + PackUtil.sessionId(), LOG_DIR, new Object[0]);
            this.handler.sendMessage(this.handler.obtainMessage(1, DJIAlbumPullErrorType.SERVER_ABORT));
        }
    }

    /* access modifiers changed from: protected */
    public void resendMe() {
        this.resending = false;
        reSend();
    }

    private void initParseThread() {
        this.mParseThread = new HandlerThread("parse_package");
        this.mParseThread.start();
        this.mParseHandler = new Handler(this.mParseThread.getLooper()) {
            /* class dji.logic.album.manager.litchis.DJILoader.AnonymousClass3 */

            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 6:
                        DJILoader.this.recvOver();
                        return;
                    case 7:
                    case 8:
                    default:
                        return;
                    case 9:
                        DJILoader.this.flushBuffer();
                        return;
                }
            }
        };
    }
}
