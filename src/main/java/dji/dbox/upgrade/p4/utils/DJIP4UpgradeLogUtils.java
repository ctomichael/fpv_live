package dji.dbox.upgrade.p4.utils;

import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import com.dji.frame.util.MD5;
import com.dji.frame.util.V_AppUtils;
import dji.component.accountcenter.IMemberProtocol;
import dji.dbox.upgrade.p4.constants.DJIUpConstants;
import dji.dbox.upgrade.p4.model.DJIUpListElement;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.model.P3.DataCommonGetCfgFile;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.util.BytesUtil;
import dji.publics.protocol.ResponseBase;
import dji.thirdparty.afinal.FinalHttp;
import dji.thirdparty.afinal.http.AjaxCallBack;
import dji.thirdparty.afinal.http.AjaxParams;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

@EXClassNullAway
public class DJIP4UpgradeLogUtils {
    private static String TAG = "DJIP4UpgradeLogUtils";
    private static DJIP4UpgradeLogUtils instance = null;
    private final Object LOCK = new Object();
    private final String LOG_of_1860_DIR = DJIUpConstants.LOG_of_1860_DIR;
    /* access modifiers changed from: private */
    public long READ_LEN_1 = -1;
    private AjaxParams mAjaxParams = new AjaxParams();
    /* access modifiers changed from: private */
    public byte[] mBuffer = new byte[1024];
    /* access modifiers changed from: private */
    public int mBufferSize = 0;
    /* access modifiers changed from: private */
    public FileOutputStream mCfgFos = null;
    /* access modifiers changed from: private */
    public long mCfgOffset = 0;
    /* access modifiers changed from: private */
    public DJIDownLogListener mDownListener;
    /* access modifiers changed from: private */
    public Queue<DataCommonGetCfgFile> mDownQueue = new LinkedList();
    private FinalHttp mFinalHttp = V_AppUtils.getFinalHttp();
    private DJIDataCallBack mInnerDownCallBack = new DJIDataCallBack() {
        /* class dji.dbox.upgrade.p4.utils.DJIP4UpgradeLogUtils.AnonymousClass1 */

        public void onSuccess(Object model) {
            DataCommonGetCfgFile peek = (DataCommonGetCfgFile) DJIP4UpgradeLogUtils.this.mDownQueue.peek();
            if (peek == null) {
                DJIUpConstants.ELogUp("mInnerDownCallBack-> mDownQueue.peek() == null !!!!!");
                DJIP4UpgradeLogUtils.this.mIsDownSuccessMask.add(false);
                DJIP4UpgradeLogUtils.this.starDownNext();
                return;
            }
            if (DJIP4UpgradeLogUtils.this.mReadLen == DJIP4UpgradeLogUtils.this.READ_LEN_1) {
                long unused = DJIP4UpgradeLogUtils.this.mReadLen = peek.getRelLength();
            }
            int unused2 = DJIP4UpgradeLogUtils.this.mBufferSize = peek.getBuffer(DJIP4UpgradeLogUtils.this.mBuffer);
            long unused3 = DJIP4UpgradeLogUtils.this.mCfgOffset = DJIP4UpgradeLogUtils.this.mCfgOffset + ((long) DJIP4UpgradeLogUtils.this.mBufferSize);
            long unused4 = DJIP4UpgradeLogUtils.this.mRemainLen = peek.getRemainLength();
            try {
                DJIP4UpgradeLogUtils.this.writeToLocal();
                if (DJIP4UpgradeLogUtils.this.mTotalLen == 0) {
                    long unused5 = DJIP4UpgradeLogUtils.this.mTotalLen = DJIP4UpgradeLogUtils.this.mRemainLen;
                    DJIUpConstants.ELogUp(" mTotalLen = " + DJIP4UpgradeLogUtils.this.mTotalLen + "B = " + ((DJIP4UpgradeLogUtils.this.mTotalLen / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) + "MB");
                }
                int progress = ((DJIP4UpgradeLogUtils.this.mTotalLen == 0 ? 100 : Math.round((((float) (DJIP4UpgradeLogUtils.this.mTotalLen - DJIP4UpgradeLogUtils.this.mRemainLen)) * 100.0f) / ((float) DJIP4UpgradeLogUtils.this.mTotalLen))) * 100) / DJIP4UpgradeLogUtils.this.mMaxProgress;
                if (DJIP4UpgradeLogUtils.this.mProgress < progress) {
                    int unused6 = DJIP4UpgradeLogUtils.this.mProgress = progress;
                    DJIUpConstants.ELogUp("mInnerDownCallBack->onSuccess() mProgress=" + DJIP4UpgradeLogUtils.this.mProgress + ",mReadLen=" + DJIP4UpgradeLogUtils.this.mReadLen + " mBufferSize=" + DJIP4UpgradeLogUtils.this.mBufferSize + " mRemainLen=" + DJIP4UpgradeLogUtils.this.mRemainLen);
                    DJIP4UpgradeLogUtils.this.mDownListener.onProgress(progress);
                }
                if (DJIP4UpgradeLogUtils.this.mRemainLen > 0) {
                    DJIP4UpgradeLogUtils.this.starDownLog();
                } else if (DJIP4UpgradeLogUtils.this.mRemainLen == 0) {
                    DJIP4UpgradeLogUtils.this.mIsDownSuccessMask.add(true);
                    DJIP4UpgradeLogUtils.this.starDownNext();
                }
            } catch (IOException e) {
                e.printStackTrace();
                DJIUpConstants.ELogUp("writeToLocal->onFailure, IOException=" + e + ",continue download the next");
                DJIP4UpgradeLogUtils.this.mIsDownSuccessMask.add(false);
                DJIP4UpgradeLogUtils.this.starDownNext();
            }
        }

        public void onFailure(Ccode ccode) {
            DJIUpConstants.ELogUp("mInnerDownCallBack->onFailure, ccode=" + ccode + " may be down part!!!,,continue download the next");
            DJIP4UpgradeLogUtils.this.mIsDownSuccessMask.add(false);
            FileOutputStream unused = DJIP4UpgradeLogUtils.this.mCfgFos = null;
            DJIP4UpgradeLogUtils.this.starDownNext();
        }
    };
    private AjaxCallBack mInnerUpCallBack = new AjaxCallBack() {
        /* class dji.dbox.upgrade.p4.utils.DJIP4UpgradeLogUtils.AnonymousClass2 */

        public void onFailure(Throwable throwable, int i, String s) {
            DJIUpConstants.ELogUp("mInnerUpCallBack, throwable = " + throwable + ",i = " + i + ",s = " + s + ",continue UUUUPload the next");
            DJIP4UpgradeLogUtils.this.starUpNext();
        }

        public void onStart(boolean b) {
        }

        public void onLoading(long count, long current) {
            DJIUpConstants.ELogUp(" current/count = " + current + IMemberProtocol.PARAM_SEPERATOR + count);
        }

        public void onSuccess(Object o) {
            DJIUpConstants.ELogUp("mInnerUpCallBack, onSuccess .o==  " + o);
            DJIP4UpgradeLogUtils.this.starUpNext();
        }
    };
    @Deprecated
    private boolean mIsAuto = false;
    /* access modifiers changed from: private */
    public Queue<Boolean> mIsDownSuccessMask = new LinkedList();
    private Queue<Boolean> mIsUpSuccessMask = new LinkedList();
    /* access modifiers changed from: private */
    public int mMaxProgress = 100;
    /* access modifiers changed from: private */
    public int mProgress = 0;
    /* access modifiers changed from: private */
    public long mReadLen = this.READ_LEN_1;
    /* access modifiers changed from: private */
    public long mRemainLen = 0;
    /* access modifiers changed from: private */
    public long mTotalLen = 0;
    @Deprecated
    private DJIUpLogListener mUpListener;
    private Queue<File> mUpQueue = new LinkedList();

    @Deprecated
    public interface DJIUpLogListener {
        void onFailure(String str);

        void onSuccess();
    }

    public interface DJIDownLogListener {
        void onFailed();

        @Deprecated
        void onProgress(int i);

        void onSuccess();
    }

    public static DJIP4UpgradeLogUtils getInstance() {
        if (instance == null) {
            instance = new DJIP4UpgradeLogUtils();
        }
        return instance;
    }

    private DJIP4UpgradeLogUtils() {
        File dirfFile = new File(this.LOG_of_1860_DIR);
        if (!dirfFile.exists()) {
            dirfFile.mkdirs();
        }
    }

    public void downDeviceLog(DJIDownLogListener listener) {
        this.mDownListener = listener;
        this.mDownQueue.clear();
        this.mIsDownSuccessMask.clear();
        this.mUpQueue.clear();
        this.mIsUpSuccessMask.clear();
        ProductType type = DJIProductManager.getInstance().getType();
        if (type == ProductType.KumquatS || type == ProductType.KumquatX) {
            DataCommonGetCfgFile downItem = new DataCommonGetCfgFile();
            downItem.setReceiveId(1).setType(DataCommonGetCfgFile.DJIUpgradeFileType.LOG).setLength(this.mReadLen).setOffset(this.mCfgOffset).setReceiveType(DeviceType.DM368);
            this.mDownQueue.offer(downItem);
            DataCommonGetCfgFile downItem2 = new DataCommonGetCfgFile();
            downItem2.setReceiveId(1).setType(DataCommonGetCfgFile.DJIUpgradeFileType.LOG).setLength(this.mReadLen).setOffset(this.mCfgOffset).setReceiveType(DeviceType.DM368_G);
            this.mDownQueue.offer(downItem2);
        } else {
            DataCommonGetCfgFile downItem3 = new DataCommonGetCfgFile();
            downItem3.setReceiveId(1).setType(DataCommonGetCfgFile.DJIUpgradeFileType.LOG).setLength(this.mReadLen).setOffset(this.mCfgOffset).setReceiveType(DeviceType.DM368);
            this.mDownQueue.offer(downItem3);
        }
        DJIUpConstants.ELogUp("downDeviceLog Main Entry,ProductName=" + DJIProductManager.getInstance().getType()._name() + ",app will download [" + this.mDownQueue.size() + "] LOG");
        this.mMaxProgress = this.mDownQueue.size() * 100;
        reset();
        starDownLog();
        DJIUpConstants.ELogUp("starDownLog 1st LOG");
    }

    public void upDeviceLog(String sn, String token, String user_id, DJIUpLogListener upListener) {
        this.mUpListener = upListener;
        String product = DJIProductManager.getInstance().getType()._name();
        DJIUpListElement element = DJIUpStatusHelper.getUpgradingStatus().getChoiceElement();
        String firmware_version = element == null ? "" : element.product_version;
        if (TextUtils.isEmpty(token) || TextUtils.isEmpty(sn) || TextUtils.isEmpty(product)) {
            DJIUpConstants.ELogUp("token/sn/product is required!!");
            upListener.onFailure("");
            return;
        }
        this.mAjaxParams.put(IMemberProtocol.STRING_USER_ID, user_id);
        this.mAjaxParams.put("product", product);
        this.mAjaxParams.put("firmware_version", firmware_version);
        this.mAjaxParams.put(ResponseBase.STRING_SN, sn);
        this.mFinalHttp.addHeader("Token", token);
        DJIUpConstants.ELogUp("startUpLog:token=" + token);
        startUpLog();
    }

    private void reset() {
        this.mRemainLen = 0;
        this.mCfgOffset = 0;
        this.mReadLen = this.READ_LEN_1;
        this.mBufferSize = 0;
    }

    /* access modifiers changed from: private */
    public void starDownLog() {
        synchronized (this.LOCK) {
            DataCommonGetCfgFile peek = this.mDownQueue.peek();
            if (peek != null) {
                peek.setLength(this.mReadLen);
                peek.setOffset(this.mCfgOffset);
                peek.start(this.mInnerDownCallBack);
            }
        }
    }

    /* access modifiers changed from: private */
    public void starDownNext() {
        synchronized (this.LOCK) {
            this.mDownQueue.poll();
        }
        if (this.mDownQueue.isEmpty()) {
            int index = 0;
            boolean isExistRcOr1860log = false;
            while (!this.mIsDownSuccessMask.isEmpty()) {
                if (this.mIsDownSuccessMask.poll().booleanValue()) {
                    DJIUpConstants.ELogUp("Down index=" + index + ",Success");
                    isExistRcOr1860log = true;
                } else {
                    DJIUpConstants.ELogUp("Down index=" + index + ",onFailed,but may be down part!!!");
                }
                index++;
            }
            boolean isExistAppLog = false;
            File file = new File(DJIUpConstants.getFileName());
            if (file.exists()) {
                isExistAppLog = true;
                DJIUpConstants.ELogUp("starDownNext finish, today log of app " + file + " exists!, add  mUpQueue!!");
                this.mUpQueue.add(file);
            } else {
                DJIUpConstants.ELogUp("starDownNext finish, but today log of app " + file + "is not exists!,not nessssray add  mUpQueue!!");
            }
            DJIUpConstants.ELogUp("starDownNext->isExistAppLog=" + isExistAppLog + ",isExistRcOr1860log=" + isExistRcOr1860log);
            if (isExistAppLog || isExistRcOr1860log) {
                DJIUpConstants.ELogUp("starDownNext->onSuccess， get all LOG Complete！！");
                if (this.mIsAuto) {
                    startUpLog();
                } else if (this.mDownListener != null) {
                    this.mDownListener.onSuccess();
                }
            } else {
                DJIUpConstants.ELogUp("starDownNext->onFailed， no find any LOG ！！");
                if (this.mDownListener != null) {
                    this.mDownListener.onFailed();
                }
            }
        } else {
            DJIUpConstants.ELogUp("starDownNext，continue download the next");
            reset();
            starDownLog();
        }
    }

    /* access modifiers changed from: private */
    public void writeToLocal() throws IOException {
        if (this.mRemainLen == 0) {
            if (this.mCfgFos != null) {
                this.mCfgFos.write(this.mBuffer, 0, this.mBufferSize);
                this.mCfgFos.flush();
                this.mCfgFos.close();
                this.mCfgFos = null;
            }
        } else if (this.mRemainLen < 0) {
            DJIUpConstants.ELogUp("GetCfgCallBack writeToLocal error");
        } else {
            if (this.mCfgFos == null) {
                this.mUpQueue.add(createDownFile());
                this.mCfgFos = new FileOutputStream(this.mUpQueue.peek());
            }
            this.mCfgFos.write(this.mBuffer, 0, this.mBufferSize);
            this.mCfgFos.flush();
        }
    }

    private File createDownFile() {
        return new File(this.LOG_of_1860_DIR + System.currentTimeMillis() + ".txt");
    }

    private void startUpLog() {
        File file = this.mUpQueue.peek();
        if (file == null) {
            DJIUpConstants.ELogUp("error,file=" + file);
            return;
        }
        DJIUpConstants.ELogUp("startUpLog:" + DJIUpConstants.api_upgrade_log + ":param=[" + this.mAjaxParams.toString() + IMemberProtocol.STRING_SEPERATOR_RIGHT);
        DJIUpConstants.ELogUp("file=" + file);
        try {
            this.mAjaxParams.put(ResponseBase.STRING_MD5, BytesUtil.byte2hexNoSep(MD5.getMD5(file)));
            this.mAjaxParams.put("log", file);
            this.mFinalHttp.post(DJIUpConstants.api_upgrade_log, this.mAjaxParams, this.mInnerUpCallBack);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            startUpLog();
        }
    }

    /* access modifiers changed from: private */
    public void starUpNext() {
        this.mUpQueue.poll();
        if (this.mUpQueue.isEmpty()) {
            int index = 0;
            boolean flg = false;
            while (!this.mIsUpSuccessMask.isEmpty()) {
                if (this.mIsUpSuccessMask.poll().booleanValue()) {
                    DJIUpConstants.ELogUp("upload index=" + index + ",Success");
                    flg = true;
                } else {
                    DJIUpConstants.ELogUp("upload index=" + index + ",onFailed");
                }
                index++;
            }
            if (flg) {
                DJIUpConstants.ELogUp("starUpNext->onSuccess， all LOG UpUpUpUpUpUp Complete！！");
                if (this.mUpListener != null) {
                    this.mUpListener.onSuccess();
                    return;
                }
                return;
            }
            DJIUpConstants.ELogUp("starUpNext->onFailure");
            if (this.mUpListener != null) {
                this.mUpListener.onFailure("");
                return;
            }
            return;
        }
        DJIUpConstants.ELogUp("starUpNext，continue upload the next");
        startUpLog();
    }
}
