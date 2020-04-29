package dji.midware.link;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.model.P3.DataOsdGetPushSdrLinkMode;
import dji.midware.data.model.P3.DataOsdSetSdrLinkMode;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.util.DJIEventBusUtil;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class DJILinkModeController {
    private static final int CHANGE_LINK_MODE_RETRY_TIMES = 3;
    private static final int CHANGE_LINK_MODE_TIMEOUT = 35000;
    private static final int MSG_REQUEST_RESUME = 2;
    private static final int MSG_REQUEST_REVERSE = 1;
    private static final int MSG_REQUEST_SUCCESS = 3;
    private static final int MSG_REQUEST_TIMEOUT = 4;
    /* access modifiers changed from: private */
    public static final String TAG = DJILinkModeController.class.getSimpleName();
    private static DJILinkModeController mInstance = null;
    private LinkModeSwitchCallBack mCallback;
    private DataOsdGetPushSdrLinkMode.LinkMode mCurrentMode = DataOsdGetPushSdrLinkMode.LinkMode.NORMAL;
    private DataOsdGetPushSdrLinkMode.LinkMode mExceptMode = null;
    private LinkRequestHandler mHandler;
    private HandlerThread mHandlerThread = new HandlerThread("thread_link_mode");
    private volatile boolean mIsRequesting = false;
    private volatile boolean mNeedReverse = false;
    private int mReceiverId = 0;
    private int mReceiverType = DeviceType.OSD.value();

    private class LinkRequestHandler extends Handler {
        public LinkRequestHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    DJILinkModeController.this.handleRequest(DataOsdGetPushSdrLinkMode.LinkMode.REVERSE, msg.arg1);
                    return;
                case 2:
                    DJILinkModeController.this.handleRequest(DataOsdGetPushSdrLinkMode.LinkMode.NORMAL, msg.arg1);
                    return;
                case 3:
                    DJILinkModeController.this.handleSuccess();
                    return;
                case 4:
                    int i = msg.arg1 - 1;
                    msg.arg1 = i;
                    DJILinkModeController.this.handleTimeout((DataOsdGetPushSdrLinkMode.LinkMode) msg.obj, i);
                    return;
                default:
                    return;
            }
        }
    }

    public static synchronized DJILinkModeController getInstance() {
        DJILinkModeController dJILinkModeController;
        synchronized (DJILinkModeController.class) {
            if (mInstance == null) {
                mInstance = new DJILinkModeController();
            }
            dJILinkModeController = mInstance;
        }
        return dJILinkModeController;
    }

    public static synchronized void destroy() {
        synchronized (DJILinkModeController.class) {
            if (mInstance != null) {
                mInstance.release();
                mInstance = null;
            }
        }
    }

    private DJILinkModeController() {
        this.mHandlerThread.start();
        this.mHandler = new LinkRequestHandler(this.mHandlerThread.getLooper());
        this.mCurrentMode = DataOsdGetPushSdrLinkMode.getInstance().getLinkMode();
        onEvent3BackgroundThread(DataOsdGetPushSdrLinkMode.getInstance());
        DJIEventBusUtil.register(this);
    }

    public DJILinkModeController setReceiver(int type, int id) {
        this.mReceiverType = type;
        this.mReceiverId = id;
        return this;
    }

    private void release() {
        DJIEventBusUtil.unRegister(this);
        this.mHandler.removeCallbacksAndMessages(null);
        this.mHandlerThread.quit();
    }

    /* access modifiers changed from: private */
    public void handleRequest(final DataOsdGetPushSdrLinkMode.LinkMode linkMode, int retryTimes) {
        ((DataOsdSetSdrLinkMode) DataOsdSetSdrLinkMode.getInstance().setLinkMode(linkMode).setReceiverType(this.mReceiverType).setReceiverId(this.mReceiverId, DataOsdSetSdrLinkMode.class)).start(new DJIDataCallBack() {
            /* class dji.midware.link.DJILinkModeController.AnonymousClass1 */

            public void onSuccess(Object model) {
                DJILogHelper.getInstance().LOGD(DJILinkModeController.TAG, "请求翻转:" + linkMode.name());
            }

            public void onFailure(Ccode ccode) {
                DJILogHelper.getInstance().LOGE(DJILinkModeController.TAG, "请求翻转失败:" + linkMode.name());
            }
        });
        this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(4, retryTimes, 0, linkMode), 35000);
    }

    /* access modifiers changed from: private */
    public void handleTimeout(DataOsdGetPushSdrLinkMode.LinkMode linkMode, int remainTimes) {
        if (remainTimes > 0) {
            if (this.mCallback != null) {
                this.mCallback.onLinkModeSwitchTimeout(remainTimes);
            }
            handleRequest(linkMode, remainTimes);
            return;
        }
        handleFailure();
    }

    /* access modifiers changed from: private */
    public void handleSuccess() {
        DJILogHelper.getInstance().LOGD(TAG, "翻转成功，当前模式为：" + this.mCurrentMode);
        if (this.mCallback != null) {
            this.mCallback.onSuccess(DataOsdSetSdrLinkMode.getInstance());
        }
        if (this.mHandler != null) {
            this.mHandler.removeMessages(4);
        }
        this.mExceptMode = null;
        this.mCallback = null;
        this.mIsRequesting = false;
    }

    private void handleFailure() {
        DJILogHelper.getInstance().LOGD(TAG, "翻转超时，当前模式为：" + this.mCurrentMode);
        this.mIsRequesting = false;
        this.mExceptMode = null;
        if (this.mCallback != null) {
            this.mCallback.onFailure(Ccode.TIMEOUT);
        }
        if (this.mHandler != null) {
            this.mHandler.removeMessages(4);
        }
        this.mCallback = null;
    }

    /* access modifiers changed from: private */
    public void checkLinkState(DataOsdGetPushSdrLinkMode.LinkMode linkMode) {
        if (linkMode == DataOsdGetPushSdrLinkMode.LinkMode.REVERSE && !this.mNeedReverse && !this.mIsRequesting) {
            requestResume(new LinkModeSwitchCallBack() {
                /* class dji.midware.link.DJILinkModeController.AnonymousClass2 */

                public void onSuccess(Object model) {
                    DJILogHelper.getInstance().LOGD(DJILinkModeController.TAG, "状态检查异常，请求恢复正常模式");
                }

                public void onFailure(Ccode ccode) {
                    DJILinkModeController.this.checkLinkState(DataOsdGetPushSdrLinkMode.getInstance().getLinkMode());
                }
            });
        }
    }

    public DataOsdGetPushSdrLinkMode.LinkMode getCurrentMode() {
        return this.mCurrentMode;
    }

    public boolean isReversing() {
        return this.mIsRequesting;
    }

    public synchronized void requestReverse(LinkModeSwitchCallBack callBack) {
        if (this.mIsRequesting) {
            if (callBack != null) {
                callBack.onFailure(Ccode.UPDATE_WAIT_FINISH);
            }
        } else if (this.mCurrentMode != DataOsdGetPushSdrLinkMode.LinkMode.REVERSE) {
            this.mNeedReverse = true;
            this.mIsRequesting = true;
            this.mExceptMode = DataOsdGetPushSdrLinkMode.LinkMode.REVERSE;
            this.mCallback = callBack;
            this.mHandler.sendMessage(this.mHandler.obtainMessage(1, 3, 0));
        } else if (callBack != null) {
            callBack.onSuccess(DataOsdSetSdrLinkMode.getInstance());
        }
    }

    public synchronized void requestResume(LinkModeSwitchCallBack callBack) {
        if (this.mIsRequesting) {
            if (callBack != null) {
                callBack.onFailure(Ccode.UPDATE_WAIT_FINISH);
            }
        } else if (this.mCurrentMode != DataOsdGetPushSdrLinkMode.LinkMode.NORMAL) {
            this.mNeedReverse = false;
            this.mIsRequesting = true;
            this.mExceptMode = DataOsdGetPushSdrLinkMode.LinkMode.NORMAL;
            this.mCallback = callBack;
            this.mHandler.sendMessage(this.mHandler.obtainMessage(2, 3, 0));
        } else if (callBack != null) {
            callBack.onSuccess(DataOsdSetSdrLinkMode.getInstance());
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataOsdGetPushSdrLinkMode mode) {
        DataOsdGetPushSdrLinkMode.LinkMode linkMode = mode.getLinkMode();
        if (this.mCurrentMode != linkMode) {
            this.mCurrentMode = linkMode;
            switch (linkMode) {
                case NORMAL:
                case REVERSE:
                    if (this.mExceptMode == linkMode && this.mIsRequesting) {
                        this.mHandler.removeMessages(4);
                        this.mHandler.sendMessage(this.mHandler.obtainMessage(3));
                        break;
                    }
                case CHANGING:
                    if (this.mIsRequesting) {
                        this.mHandler.removeMessages(4);
                        this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(4), 35000);
                        if (this.mCallback != null) {
                            this.mCallback.onLinkModeChanging(this.mCurrentMode, this.mExceptMode);
                            break;
                        }
                    }
                    break;
                case OTHER:
                    if (this.mCallback != null) {
                        this.mCallback.onLinkModeSwitchException();
                        break;
                    }
                    break;
            }
        }
        checkLinkState(linkMode);
    }

    public static abstract class LinkModeSwitchCallBack implements DJIDataCallBack {
        /* access modifiers changed from: protected */
        public void onLinkModeChanging(DataOsdGetPushSdrLinkMode.LinkMode current, DataOsdGetPushSdrLinkMode.LinkMode except) {
        }

        /* access modifiers changed from: protected */
        public void onLinkModeSwitchTimeout(int times) {
        }

        /* access modifiers changed from: protected */
        public void onLinkModeSwitchException() {
        }
    }
}
