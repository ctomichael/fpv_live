package dji.publics.logic;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.dji.frame.util.V_AppUtils;
import dji.component.accountcenter.IMemberProtocol;
import dji.fieldAnnotation.EXClassNullAway;
import dji.thirdparty.afinal.FinalHttp;
import dji.thirdparty.afinal.http.AjaxCallBack;
import dji.thirdparty.afinal.http.HttpHandler;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

@EXClassNullAway
public class DJIFileDownloadManager {
    private static final int MSG_ID_FAIL = 4099;
    private static final int MSG_ID_START = 4096;
    private static final int MSG_ID_SUCCESS = 4098;
    private static final int MSG_ID_UPDATE = 4097;
    private Context mAppCxt;
    private DownHandler mDownHandler;
    private final ArrayList<DownInfo> mDownInfos;
    private FinalHttp mFinalHttp;
    private boolean mInit;

    public interface OnDownLoadListener {
        void onFailure(String str, Object obj, String str2, int i);

        void onStart(String str, Object obj, boolean z);

        void onSuccss(String str, String str2, Object obj);

        void onUpdate(String str, Object obj, int i, int i2);
    }

    public static DJIFileDownloadManager getInstance() {
        return SingletonHolder.mInstance;
    }

    public synchronized boolean initializeDM(Context context) {
        if (!this.mInit) {
            this.mAppCxt = context.getApplicationContext();
            this.mFinalHttp = V_AppUtils.getFinalHttp(this.mAppCxt);
            this.mInit = true;
        }
        return this.mInit;
    }

    public synchronized boolean finalizeDM() {
        if (this.mInit) {
        }
        return !this.mInit;
    }

    public void downloadFileForPath(String url, String absPath, boolean isResume, boolean check, Object arg, OnDownLoadListener listener) {
        try {
            DownInfo down = new DownInfo();
            down.mArg = arg;
            down.mDownCB = new DownCallBack(down, this.mDownHandler);
            down.mUrl = url;
            down.mHttpHandler = this.mFinalHttp.downloadCheck(url, absPath, isResume, check, down.mDownCB);
            down.mTargetPath = absPath;
            if (listener != null) {
                down.mListener = new WeakReference<>(listener);
            }
            addDownInfo(down);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public void stopDown(String url) {
        DownInfo down;
        if (url != null && (down = checkHad(url)) != null) {
            down.mHttpHandler.stop();
            removeDownInfo(down);
        }
    }

    private DownInfo checkHad(String url) {
        DownInfo ret = null;
        synchronized (this.mDownInfos) {
            int i = 0;
            while (true) {
                if (i >= this.mDownInfos.size()) {
                    break;
                }
                DownInfo down = this.mDownInfos.get(i);
                if (url.equals(down.mUrl)) {
                    ret = down;
                    break;
                }
                i++;
            }
        }
        return ret;
    }

    private DownInfo addDownInfo(DownInfo down) {
        synchronized (this.mDownInfos) {
            this.mDownInfos.add(down);
        }
        return down;
    }

    private DownInfo removeDownInfo(String url) {
        DownInfo down = null;
        synchronized (this.mDownInfos) {
            int i = 0;
            while (true) {
                if (i >= this.mDownInfos.size()) {
                    break;
                }
                DownInfo tmp = this.mDownInfos.get(i);
                if (url.equals(tmp.mUrl)) {
                    this.mDownInfos.remove(i);
                    down = tmp;
                    break;
                }
                i++;
            }
        }
        return down;
    }

    private DownInfo removeDownInfo(DownInfo down) {
        synchronized (this.mDownInfos) {
            this.mDownInfos.remove(down);
        }
        return down;
    }

    /* access modifiers changed from: private */
    public void handleDownSuccess(DownInfo downInfo) {
        DownInfo down;
        OnDownLoadListener listener;
        if (downInfo != null && (down = checkHad(downInfo.mUrl)) != null) {
            removeDownInfo(down);
            if (down.mListener != null && (listener = down.mListener.get()) != null) {
                listener.onSuccss(downInfo.mUrl, down.mTargetPath, down.mArg);
            }
        }
    }

    /* access modifiers changed from: private */
    public void handleDownFail(DownInfo downInfo, int errCode) {
        DownInfo down;
        OnDownLoadListener listener;
        if (downInfo != null && (down = checkHad(downInfo.mUrl)) != null) {
            removeDownInfo(down);
            if (down.mListener != null && (listener = down.mListener.get()) != null) {
                listener.onFailure(downInfo.mUrl, down.mArg, down.mErrMsg, errCode);
            }
        }
    }

    /* access modifiers changed from: private */
    public void handleDownStart(DownInfo downInfo, boolean isResume) {
        DownInfo down;
        OnDownLoadListener listener;
        if (downInfo != null && (down = checkHad(downInfo.mUrl)) != null && down.mListener != null && (listener = down.mListener.get()) != null) {
            listener.onStart(downInfo.mUrl, down.mArg, isResume);
        }
    }

    /* access modifiers changed from: private */
    public void handleDownUpdate(DownInfo downInfo, int progress, int size) {
        DownInfo down;
        OnDownLoadListener listener;
        if (downInfo != null && (down = checkHad(downInfo.mUrl)) != null && down.mListener != null && (listener = down.mListener.get()) != null) {
            listener.onUpdate(downInfo.mUrl, down.mArg, progress, size);
        }
    }

    private DJIFileDownloadManager() {
        this.mInit = false;
        this.mAppCxt = null;
        this.mFinalHttp = null;
        this.mDownHandler = null;
        this.mDownInfos = new ArrayList<>();
        this.mDownHandler = new DownHandler(this);
    }

    private static final class SingletonHolder {
        public static DJIFileDownloadManager mInstance = new DJIFileDownloadManager();

        private SingletonHolder() {
        }
    }

    private static final class DownInfo {
        public Object mArg;
        public DownCallBack mDownCB;
        public String mErrMsg;
        public HttpHandler<File> mHttpHandler;
        public WeakReference<OnDownLoadListener> mListener;
        public String mTargetPath;
        public String mUrl;

        private DownInfo() {
            this.mUrl = null;
            this.mHttpHandler = null;
            this.mDownCB = null;
            this.mArg = null;
            this.mTargetPath = null;
            this.mErrMsg = "";
            this.mListener = null;
        }

        public boolean equals(Object o) {
            boolean ret = super.equals(o);
            if (ret || !(o instanceof DownInfo)) {
                return ret;
            }
            DownInfo down = (DownInfo) o;
            if (down.mUrl == null || !down.mUrl.equals(this.mUrl)) {
                return ret;
            }
            return true;
        }

        public int hashCode() {
            if (this.mUrl != null) {
                return this.mUrl.hashCode() + 527;
            }
            return 17;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder(48);
            sb.append("url[").append(this.mUrl).append(IMemberProtocol.STRING_SEPERATOR_RIGHT);
            return sb.toString();
        }
    }

    private static final class DownCallBack extends AjaxCallBack<File> {
        public DownInfo mDownInfo = null;
        public Handler mHandler = null;

        public DownCallBack(DownInfo downInfo, Handler h) {
            this.mDownInfo = downInfo;
            this.mHandler = h;
        }

        public void onStart(boolean isResume) {
            this.mHandler.obtainMessage(4096, isResume ? 0 : 1, 0, this.mDownInfo).sendToTarget();
        }

        public void onLoading(long count, long current) {
            if (current > count) {
                current = count;
            }
            this.mHandler.obtainMessage(4097, count == 0 ? 0 : (int) ((100 * current) / count), (int) count, this.mDownInfo).sendToTarget();
        }

        public void onFailure(Throwable t, int errorNo, String strMsg) {
            this.mDownInfo.mErrMsg = strMsg;
            this.mHandler.obtainMessage(4099, errorNo, 0, this.mDownInfo).sendToTarget();
        }

        public void onSuccess(File t) {
            this.mHandler.obtainMessage(4098, 0, 0, this.mDownInfo).sendToTarget();
        }
    }

    private static final class DownHandler extends Handler {
        private final WeakReference<DJIFileDownloadManager> mOutCls;

        public DownHandler(DJIFileDownloadManager fdm) {
            super(Looper.getMainLooper());
            this.mOutCls = new WeakReference<>(fdm);
        }

        public void handleMessage(Message msg) {
            boolean z = true;
            DJIFileDownloadManager fdm = this.mOutCls.get();
            if (fdm != null) {
                switch (msg.what) {
                    case 4096:
                        DownInfo downInfo = (DownInfo) msg.obj;
                        if (msg.arg1 != 1) {
                            z = false;
                        }
                        fdm.handleDownStart(downInfo, z);
                        return;
                    case 4097:
                        fdm.handleDownUpdate((DownInfo) msg.obj, msg.arg1, msg.arg2);
                        return;
                    case 4098:
                        fdm.handleDownSuccess((DownInfo) msg.obj);
                        return;
                    case 4099:
                        fdm.handleDownFail((DownInfo) msg.obj, msg.arg1);
                        return;
                    default:
                        return;
                }
            }
        }
    }
}
