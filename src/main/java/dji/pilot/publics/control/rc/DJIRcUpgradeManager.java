package dji.pilot.publics.control.rc;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.media.session.PlaybackStateCompat;
import com.dji.frame.util.V_AppUtils;
import com.dji.frame.util.V_DiskUtil;
import com.dji.frame.util.V_StringUtils;
import dji.apppublic.reflect.AppPubInjectManager;
import dji.component.accountcenter.IMemberProtocol;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.Dpad.DpadProductManager;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.manager.P3.DataEvent;
import dji.midware.data.model.P3.DataCommonGetDeviceStatus;
import dji.midware.data.model.P3.DataCommonGetVersion;
import dji.midware.data.model.P3.DataCommonRestartDevice;
import dji.midware.data.model.P3.DataOsdSetUpgradeTip;
import dji.midware.interfaces.DJIDataCallBack;
import dji.pilot.fpv.model.IEventObjects;
import dji.pilot.publics.R;
import dji.pilot.publics.control.rc.DJIRcPackageParser;
import dji.pilot.publics.control.rc.DJIRcUpgradeModel;
import dji.pilot.publics.model.DJIUpgradePackListModel;
import dji.pilot.publics.rollback.UpgradeStatusChange;
import dji.pilot.publics.util.DJIPublicUtils;
import dji.pilot.usercenter.util.FileUtil;
import dji.thirdparty.afinal.FinalDb;
import dji.thirdparty.afinal.FinalHttp;
import dji.thirdparty.afinal.http.AjaxCallBack;
import dji.thirdparty.afinal.http.HttpHandler;
import java.io.File;
import java.io.FilenameFilter;
import java.io.RandomAccessFile;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class DJIRcUpgradeManager {
    private static final boolean CONNLOSE_RESET = true;
    private static final String DEBUG_FILE = "RC_FW_DEBUG";
    public static final boolean DEBUG_TEST = false;
    private static final int DELAY_FPGA_VERSION = 10000;
    public static final int DELAY_RESTART_RC = 5;
    private static final int DELAY_RESULT_TIP = 50;
    private static final long DELAY_UPDATE_PROGRESS = 200;
    private static final long DELAY_UPGRADE_NEXT = 2600;
    private static final int DELAY_UPGRADE_START_TIP = 200;
    private static final int DELAY_UPGRADE_TIP = 5000;
    private static final DeviceType[] DEVICES_RC_2014 = {DeviceType.TRANSFORM_G, DeviceType.FPGA_G, DeviceType.OSD};
    private static final DeviceType[] DEVICES_RC_368 = {DeviceType.DM368_G, DeviceType.TRANSFORM_G, DeviceType.FPGA_G, DeviceType.OSD};
    private static final String EMPTY_VERSION = "";
    private static final String FW_SUFFIX = ".bin";
    private static final String LITCHIS_FW_PREFIX = "P3XS_FW_RC_V";
    private static final String LITCHIX_FW_PREFIX = "P3XS_FW_RC_V";
    public static final int MAX_PROGRESS = 200;
    private static final int MSG_ID_CHECK_OSDSTATUS = 20480;
    private static final int MSG_ID_CHECK_UPGRADE = 4098;
    private static final int MSG_ID_FPGA_VERSION = 39321;
    private static final int MSG_ID_GETVERSION = 24576;
    private static final int MSG_ID_RESTART_RC = 4096;
    private static final int MSG_ID_UPDATE_PROGRESS = 4352;
    private static final int MSG_ID_UPGRADE_NEXT = 4097;
    private static final int MSG_ID_UPGRADE_TIP = 8192;
    private static final String ORANGE_FW_PREFIX = "WM610_FW_RC_V";
    private static final String PACKAGE_DIR = "Package/";
    private static final String RC_FW_PREFIX = "RC_FW_V";
    private static final String RESULT_TXT = "result.txt";
    private static final int RETRY_TIMES = 100;
    public static final int STATUS_CANTAPPUPGRADE = 257;
    public static final int STATUS_CHECKUPGRADE = 514;
    public static final int STATUS_CONN = 513;
    public static final int STATUS_DISCONN = 512;
    public static final int STATUS_DLALREADY = 261;
    public static final int STATUS_DLFAILED = 260;
    public static final int STATUS_DOWNLOADING = 259;
    public static final int STATUS_NEEDUPGRADE = 258;
    public static final int STATUS_NONE = 256;
    public static final int STATUS_UGFAILED = 264;
    public static final int STATUS_UGFINISH = 263;
    public static final int STATUS_UPGRADING = 262;
    public static final boolean SUPPORT_RCONE = true;
    private static final String TAG = DJIRcUpgradeManager.class.getSimpleName();
    public static final String TEST_VERSION = "1.0";
    private static final int TIMES_RESULT_TIP = 3;
    private static final int TIMES_UPDATE_PROGRESS = 30;
    public static final boolean WRITE_LOG = true;
    private Context mAppCxt;
    private long mBeforeUpgradeSize;
    /* access modifiers changed from: private */
    public DJIDLPackageInfo mCurDLPackage;
    private DJIRcUpgradeModel mCurUpgradeModel;
    private String mCurrentVersion;
    private final ArrayList<DJIDLPackageInfo> mDLPackageList;
    private final ArrayList<DJIDLPackageInfo> mDevicePackages;
    private DeviceType[] mDeviceRcs;
    /* access modifiers changed from: private */
    public final HashMap<DJIDLPackageInfo, HttpHandler<File>> mDownloadHandlers;
    private OnEventListener mEventListener;
    /* access modifiers changed from: private */
    public OnDLPackagedListener mExterDLListener;
    private OnUpgradeListener mExterUpgradeListener;
    /* access modifiers changed from: private */
    public FinalDb mFinalDb;
    private FinalHttp mFinalHttp;
    private long mFpgaDataLength;
    /* access modifiers changed from: private */
    public UIHandler mHandler;
    private volatile boolean mInit;
    private OnDLPackagedListener mInnerDLListener;
    private String mLastestVersion;
    private final ArrayList<ModelCheckStatus> mModelUpgradeList;
    private int mOsdLoaderMode;
    private int mOtherStatus;
    private String mPackageDir;
    private DJIRcPackageParser.ParseResult mPackageParseResult;
    private RandomAccessFile mPackageRAF;
    private DJIUpgradePackListModel.DJIUpgradePack mRbUpgradePack;
    /* access modifiers changed from: private */
    public int mRcStatus;
    private boolean mSupportAppUpgrade;
    private DJIRcUpgradeModel.OnModelUpgradeListener mUpgradeListener;
    private DJIUpgradePackListModel.DJIUpgradePack mUpgradePack;
    /* access modifiers changed from: private */
    public int mUpgradeProgess;
    private final DataOsdSetUpgradeTip mUpgradeTipSetter;
    /* access modifiers changed from: private */
    public long mUpgradeTotalSize;
    /* access modifiers changed from: private */
    public long mUpgradedSize;
    private boolean mbGetAllModule;

    public interface OnDLPackagedListener {
        void onFail(DJIDLPackageInfo dJIDLPackageInfo, int i, String str);

        void onStart(DJIDLPackageInfo dJIDLPackageInfo, boolean z);

        void onSuccess(DJIDLPackageInfo dJIDLPackageInfo, int i);

        void onUpdate(DJIDLPackageInfo dJIDLPackageInfo, long j, long j2);
    }

    public interface OnEventListener {
        void onUpgradePackageError(int i, int i2);

        void onUpgradeStatusChanged(int i);
    }

    public interface OnUpgradeListener {
        void onFail(DJIRcUpgradeManager dJIRcUpgradeManager, Ccode ccode, int i, int i2);

        void onStart(DJIRcUpgradeManager dJIRcUpgradeManager, int i);

        void onSuccess(DJIRcUpgradeManager dJIRcUpgradeManager, int i);

        void onUpdate(DJIRcUpgradeManager dJIRcUpgradeManager, int i, int i2);
    }

    public static DJIRcUpgradeManager getInstance() {
        return SingletonHolder.mInstance;
    }

    public synchronized void initializeManager(Context context) {
        if (!this.mInit) {
            this.mInit = true;
            this.mRcStatus = 256;
            this.mAppCxt = context.getApplicationContext();
            EventBus.getDefault().register(this);
            this.mPackageDir = V_DiskUtil.getExternalCacheDirPath(this.mAppCxt, PACKAGE_DIR);
            FileUtil.mkdirs(this.mPackageDir);
            this.mFinalDb = V_AppUtils.getFinalDb(this.mAppCxt);
            try {
                List<DJIDLPackageInfo> list = this.mFinalDb.findAllByWhere(DJIDLPackageInfo.class, "mType=" + String.valueOf(0));
                if (list != null && !list.isEmpty()) {
                    int size = list.size();
                    for (int i = 0; i < size; i++) {
                        DJIDLPackageInfo tmp = (DJIDLPackageInfo) list.get(i);
                        checkDLPackageStatus(tmp, true);
                        this.mDLPackageList.add(tmp);
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    private void checkDLPackageStatus(DJIDLPackageInfo packInfo, boolean init) {
        if (init) {
            packInfo.mDLStatus = 1;
        }
        File file = new File(packInfo.mAbsPath);
        if (0 == packInfo.mPackageSize) {
            packInfo.mDLSize = 0;
        } else if (FileUtil.isFile(file)) {
            packInfo.mDLSize = file.length();
            if (file.length() >= packInfo.mPackageSize) {
                packInfo.mDLStatus = 3;
            }
        } else {
            packInfo.mDLSize = 0;
        }
    }

    private void stopAllDLPackage() {
        if (!this.mDownloadHandlers.isEmpty()) {
            for (DJIDLPackageInfo dJIDLPackageInfo : this.mDownloadHandlers.keySet()) {
                HttpHandler<File> httpHandler = this.mDownloadHandlers.get(dJIDLPackageInfo);
                if (httpHandler != null) {
                    httpHandler.stop();
                }
            }
            this.mDownloadHandlers.clear();
        }
    }

    @Subscribe(priority = 100, threadMode = ThreadMode.MAIN)
    public void onEvent3MainThread(DataEvent event) {
        DJILogHelper.getInstance().LOGD(TAG, "Data " + event, false, true);
        if (event == DataEvent.ConnectLose) {
            if (this.mRcStatus != 262 || this.mCurUpgradeModel == null) {
                this.mOtherStatus = 512;
                this.mUpgradeProgess = 0;
                this.mUpgradedSize = 0;
                this.mUpgradeTotalSize = 0;
                this.mModelUpgradeList.clear();
                if (this.mCurUpgradeModel != null) {
                    this.mCurUpgradeModel.disconnect();
                    this.mCurUpgradeModel = null;
                }
                closeFile();
                if (this.mRcStatus == 262) {
                    this.mRcStatus = 264;
                    if (this.mExterUpgradeListener != null) {
                        this.mExterUpgradeListener.onFail(this, Ccode.NOCONNECT, 101, 0);
                        return;
                    }
                    return;
                }
                return;
            }
            this.mCurUpgradeModel.setWaitForConn(true);
        } else if (event != DataEvent.ConnectOK) {
        } else {
            if (this.mRcStatus == 262 && this.mCurUpgradeModel != null) {
                this.mCurUpgradeModel.setWaitForConn(false);
            } else if (this.mOtherStatus == 512) {
                this.mOtherStatus = 513;
            }
        }
    }

    public void setNotWaitMode() {
        if (this.mCurUpgradeModel != null) {
            this.mCurUpgradeModel.setWaitForConn(false);
            this.mCurUpgradeModel.clear();
        }
        if (this.mHandler != null) {
            this.mHandler.removeMessages(8192);
        }
    }

    @Subscribe(priority = 100, threadMode = ThreadMode.MAIN)
    public void onEvent3MainThread(DJIProductManager.DJIProductRcEvent rcEvent) {
        if (!canUpgradeByApp()) {
            int beforeRcStatus = this.mRcStatus;
            this.mRcStatus = 256;
            if (beforeRcStatus != this.mRcStatus && this.mEventListener != null) {
                this.mEventListener.onUpgradeStatusChanged(this.mRcStatus);
            }
        }
    }

    private String getUrlInner(boolean get) {
        if (get) {
            if (this.mRbUpgradePack != null) {
                this.mUpgradePack = this.mRbUpgradePack;
            } else {
                this.mUpgradePack = AppPubInjectManager.getAppPubToP3Injectable().getUpgradePack(getProductType(true));
            }
        }
        if (this.mUpgradePack != null) {
            return this.mUpgradePack.rc1url;
        }
        return null;
    }

    private String getLastestVersionInner(boolean get) {
        String version = "";
        if (this.mRbUpgradePack != null) {
            this.mUpgradePack = this.mRbUpgradePack;
        } else if (get) {
            this.mUpgradePack = AppPubInjectManager.getAppPubToP3Injectable().getUpgradePack(getProductType(true));
        }
        if (this.mUpgradePack != null) {
            if (!DJIPublicUtils.isEmpty(this.mUpgradePack.rcversion)) {
                version = this.mUpgradePack.rcversion;
            } else {
                version = this.mUpgradePack.version;
            }
        }
        if (DJIPublicUtils.isEmpty(version)) {
            return "";
        }
        return version;
    }

    public boolean setUpgradePack(DJIUpgradePackListModel.DJIUpgradePack pack) {
        if (pack != null && 262 != this.mRcStatus && this.mSupportAppUpgrade) {
            this.mRbUpgradePack = pack;
            int beforeRcStatus = this.mRcStatus;
            this.mUpgradePack = pack;
            this.mRcStatus = 258;
            this.mLastestVersion = getLastestVersionInner(false);
            DJIDLPackageInfo packInfo = findDLPackage(generatePackageName(this.mLastestVersion));
            if (packInfo != null) {
                this.mCurDLPackage = packInfo;
                checkDLPackageStatus(packInfo, false);
                if (packInfo.mDLStatus == 3) {
                    if (this.mUpgradeListener == null) {
                        this.mRcStatus = 261;
                    } else if (this.mRcStatus != 264) {
                        this.mRcStatus = 261;
                    }
                } else if (this.mUpgradeListener == null) {
                    this.mRcStatus = 259;
                } else if (this.mRcStatus != 260) {
                    this.mRcStatus = 259;
                }
            } else {
                this.mCurDLPackage = null;
                this.mRcStatus = 258;
            }
            reloadDevicePackages();
            if (!(beforeRcStatus == this.mRcStatus || this.mEventListener == null)) {
                this.mEventListener.onUpgradeStatusChanged(this.mRcStatus);
            }
        } else if (262 != this.mRcStatus) {
            this.mRbUpgradePack = null;
            handleRcChanged();
        }
        return true;
    }

    private void handleRcChanged() {
        boolean z;
        int beforeRcStatus = this.mRcStatus;
        if (AppPubInjectManager.getAppPubToP3Injectable().isGetted(formatModule(DeviceType.DM368_G.value(), 0))) {
            this.mDeviceRcs = DEVICES_RC_368;
            if (AppPubInjectManager.getAppPubToP3Injectable().getLoaderByte(formatModule(DeviceType.DM368_G.value(), 0), 1).intValue() > 1) {
                z = true;
            } else {
                z = false;
            }
            this.mSupportAppUpgrade = z;
        } else {
            this.mDeviceRcs = DEVICES_RC_2014;
            this.mSupportAppUpgrade = true;
        }
        if (!this.mSupportAppUpgrade) {
            this.mCurDLPackage = null;
            this.mRcStatus = 257;
        } else if (this.mRbUpgradePack == null) {
            if (AppPubInjectManager.getAppPubToP3Injectable().getNeedUpgradeDevices().contains("rc")) {
                this.mCurDLPackage = null;
                this.mRcStatus = 256;
            } else {
                this.mRcStatus = 258;
                this.mCurrentVersion = AppPubInjectManager.getAppPubToP3Injectable().getRcVersionName();
                if (DJIPublicUtils.isEmpty(this.mCurrentVersion)) {
                    this.mCurrentVersion = "";
                }
                this.mLastestVersion = getLastestVersionInner(true);
                DJIDLPackageInfo packInfo = findDLPackage(generatePackageName(this.mLastestVersion));
                if (packInfo != null) {
                    this.mCurDLPackage = packInfo;
                    checkDLPackageStatus(packInfo, false);
                    if (packInfo.mDLStatus == 3) {
                        if (this.mUpgradeListener == null) {
                            this.mRcStatus = 261;
                        } else if (this.mRcStatus != 264) {
                            this.mRcStatus = 261;
                        }
                    } else if (this.mUpgradeListener == null) {
                        this.mRcStatus = 259;
                    } else if (this.mRcStatus != 260) {
                        this.mRcStatus = 259;
                    }
                } else {
                    this.mCurDLPackage = null;
                    this.mRcStatus = 258;
                }
                reloadDevicePackages();
            }
        }
        DJILogHelper.getInstance().LOGD(TAG, "RcStatus[" + this.mRcStatus + IMemberProtocol.STRING_SEPERATOR_RIGHT, false, true);
        if (beforeRcStatus != this.mRcStatus && this.mEventListener != null) {
            this.mEventListener.onUpgradeStatusChanged(this.mRcStatus);
        }
    }

    @Subscribe(priority = 100, threadMode = ThreadMode.MAIN)
    public void onEvent3MainThread(IEventObjects.DJIUpgradeStatus status) {
        DJILogHelper.getInstance().LOGD(TAG, "upgrade status[" + status + "]type[" + DJIProductManager.getInstance().getRcType() + IMemberProtocol.STRING_SEPERATOR_RIGHT, false, true);
        if (!canUpgradeByApp()) {
            this.mRcStatus = 256;
            return;
        }
        this.mOtherStatus = 514;
        if (this.mRcStatus == 262) {
            return;
        }
        if (IEventObjects.DJIUpgradeStatus.NO == status) {
            this.mRcStatus = 256;
        } else {
            handleRcChanged();
        }
    }

    public boolean canUpgradeByApp() {
        return canUpgradeByApp(getProductType());
    }

    public static boolean canUpgradeByApp(ProductType type) {
        return !DpadProductManager.getInstance().isPomato() && (type == ProductType.Orange || type == ProductType.litchiS || type == ProductType.litchiX || type == ProductType.N1 || type == ProductType.Grape2 || type == ProductType.A2 || type == ProductType.Tomato || type == ProductType.Pomato || type == ProductType.BigBanana || type == ProductType.OrangeRAW || type == ProductType.Olives || type == ProductType.Potato || type == ProductType.OrangeCV600);
    }

    private void upgradeInner() {
        if (this.mRcStatus != 262) {
            return;
        }
        if (!checkUpgradeModel()) {
            FileUtil.delFile(getPackagePath(generatePackageName(this.mLastestVersion)));
            this.mRcStatus = 258;
            if (this.mEventListener != null) {
                this.mEventListener.onUpgradePackageError(this.mRcStatus, 0);
            }
        } else if (this.mModelUpgradeList.isEmpty()) {
            notifyUpgradeFail(null, Ccode.UNDEFINED, 101, 0);
        } else {
            openFile();
            upgreadeNext();
        }
    }

    private void reloadDevicePackages() {
        this.mDevicePackages.clear();
        int size = this.mDLPackageList.size();
        for (int i = 0; i < size; i++) {
            DJIDLPackageInfo tmp = this.mDLPackageList.get(i);
            if (tmp.mProductId == ProductType.None.value()) {
                this.mDevicePackages.add(tmp);
            }
        }
    }

    private void removeAllDLPackageByProduct() {
        int i = 0;
        while (this.mDLPackageList.size() > 0) {
            DJIDLPackageInfo tmp = this.mDLPackageList.get(i);
            this.mDLPackageList.remove(i);
            this.mFinalDb.deleteByWhere(DJIDLPackageInfo.class, "mAbsPath='" + tmp.mAbsPath + "'");
            FileUtil.delFile(tmp.mAbsPath);
            i = (i - 1) + 1;
        }
    }

    private void deleteOtherPackage() {
        final String finalPrefix = getProductFwPrefix();
        File[] files = new File(this.mPackageDir).listFiles(new FilenameFilter() {
            /* class dji.pilot.publics.control.rc.DJIRcUpgradeManager.AnonymousClass1 */

            public boolean accept(File dir, String filename) {
                return filename.startsWith(finalPrefix);
            }
        });
        if (files != null && files.length > 0) {
            for (File file : files) {
                FileUtil.delFile(file);
            }
        }
    }

    private void removeLowVersionPackages() {
        int i = 0;
        while (i < this.mDLPackageList.size()) {
            DJIDLPackageInfo tmp = this.mDLPackageList.get(i);
            if (tmp.mProductId == ProductType.None.value() && (tmp.mVersion == null || !tmp.mVersion.equals(this.mLastestVersion))) {
                this.mDLPackageList.remove(i);
                this.mFinalDb.delete(tmp);
                FileUtil.delFile(tmp.mAbsPath);
                i--;
            }
            i++;
        }
    }

    /* access modifiers changed from: private */
    public DJIDLPackageInfo findDLPackage(String fileName) {
        int size = this.mDLPackageList.size();
        for (int i = 0; i < size; i++) {
            DJIDLPackageInfo tmp = this.mDLPackageList.get(i);
            if (fileName.equals(tmp.mFileName)) {
                return tmp;
            }
        }
        return null;
    }

    private DJIDLPackageInfo removeDLPackage(String fileName) {
        int size = this.mDLPackageList.size();
        for (int i = 0; i < size; i++) {
            DJIDLPackageInfo tmp = this.mDLPackageList.get(i);
            if (fileName.equals(tmp.mFileName)) {
                DJIDLPackageInfo packInfo = tmp;
                this.mDLPackageList.remove(i);
                this.mFinalDb.delete(tmp);
                FileUtil.delFile(this.mCurDLPackage.mAbsPath);
                return packInfo;
            }
        }
        return null;
    }

    private DJIDLPackageInfo addDLPackage(boolean add, String fileName) {
        DJIDLPackageInfo packInfo = new DJIDLPackageInfo();
        packInfo.mAbsPath = getPackagePath(fileName);
        packInfo.mFileName = fileName;
        packInfo.mDLUrl = getUrlInner(false);
        packInfo.mDLSize = 0;
        packInfo.mDLStatus = 0;
        packInfo.mPackageSize = 0;
        packInfo.mProductId = ProductType.None.value();
        packInfo.mRealProductId = getProductType().value();
        packInfo.mVersion = this.mLastestVersion;
        packInfo.mType = 0;
        this.mDLPackageList.add(packInfo);
        this.mFinalDb.save(packInfo);
        return packInfo;
    }

    private void notifyUpgradeStart() {
        this.mRcStatus = 262;
        EventBus.getDefault().post(UpgradeStatusChange.Change);
        if (this.mExterUpgradeListener != null) {
            this.mExterUpgradeListener.onStart(this, 0);
        }
    }

    /* access modifiers changed from: private */
    public void notifyUpgradeUpdate(int progress, int count) {
        if (this.mExterUpgradeListener != null) {
            this.mExterUpgradeListener.onUpdate(this, progress, count);
        }
    }

    /* access modifiers changed from: private */
    public void notifyUpgradeFail(DJIRcUpgradeModel model, Ccode err, int errCode, int arg) {
        String message;
        EventBus.getDefault().post(UpgradeStatusChange.Change);
        this.mCurUpgradeModel = null;
        closeFile();
        if (this.mRcStatus == 262) {
            String device = model != null ? model.getDeviceType().toString() : "null";
            if (arg == 0) {
                message = "device[" + device + "]err[" + err + IMemberProtocol.STRING_SEPERATOR_RIGHT;
            } else {
                message = "device[" + device + "]err[" + err + "]reason[" + this.mAppCxt.getString(arg) + IMemberProtocol.STRING_SEPERATOR_RIGHT;
            }
            writeResult("upgrade failed " + V_StringUtils.getDateTime() + "reason " + message + "\r\n");
            for (int i = 0; i < this.mDeviceRcs.length; i++) {
                regetDeviceVersion(this.mDeviceRcs[i], true);
            }
            notifyUpgradeTip(DataOsdSetUpgradeTip.UPGRADETIP.FAIL, 3, 50, false);
            this.mRcStatus = 264;
            if (this.mExterUpgradeListener != null) {
                this.mExterUpgradeListener.onFail(this, err, errCode, arg);
            }
            EventBus.getDefault().post(UpgradeStatusChange.Change);
        }
    }

    private void notifyUpgradeSuccess(int arg) {
        EventBus.getDefault().post(UpgradeStatusChange.Change);
        this.mCurUpgradeModel = null;
        closeFile();
        if (this.mRcStatus == 262) {
            writeResult("upgrade success in time[" + V_StringUtils.getDateTime() + "]\r\n");
            notifyUpgradeTip(DataOsdSetUpgradeTip.UPGRADETIP.SUCCESS, 3, 50, false);
            this.mRcStatus = 263;
            if (this.mExterUpgradeListener != null) {
                this.mExterUpgradeListener.onSuccess(this, arg);
            }
            EventBus.getDefault().post(UpgradeStatusChange.Change);
        }
    }

    private void writeResult(String msg) {
        FileUtil.writeFile(getPackagePath(RESULT_TXT), msg, true);
    }

    private void openFile() {
        closeFile();
        try {
            this.mPackageRAF = new RandomAccessFile(new File(getPackagePath(generatePackageName(this.mLastestVersion))), "r");
            if (this.mPackageRAF == null) {
                try {
                    this.mPackageRAF = new RandomAccessFile(new File(getPackagePath(this.mLastestVersion)), "r");
                } catch (Exception e) {
                }
            }
        } catch (Exception e2) {
            if (this.mPackageRAF == null) {
                try {
                    this.mPackageRAF = new RandomAccessFile(new File(getPackagePath(this.mLastestVersion)), "r");
                } catch (Exception e3) {
                }
            }
        } catch (Throwable th) {
            if (this.mPackageRAF == null) {
                try {
                    this.mPackageRAF = new RandomAccessFile(new File(getPackagePath(this.mLastestVersion)), "r");
                } catch (Exception e4) {
                }
            }
            throw th;
        }
    }

    private void closeFile() {
        if (this.mPackageRAF != null) {
            try {
                this.mPackageRAF.close();
            } catch (Exception e) {
            }
            this.mPackageRAF = null;
        }
    }

    private void resetUpgradeSize() {
        this.mUpgradeTotalSize = 0;
        this.mUpgradedSize = 0;
        this.mUpgradeProgess = 0;
    }

    private boolean containsFpgaModel() {
        boolean hasCalFpga = false;
        int size = this.mModelUpgradeList.size();
        for (int i = 0; i < size; i++) {
            if (this.mModelUpgradeList.get(i).mDeviceType == DeviceType.FPGA_G) {
                hasCalFpga = true;
            }
        }
        return hasCalFpga;
    }

    /* access modifiers changed from: private */
    public void upgradeModelSuccess(int arg) {
        if (!this.mModelUpgradeList.isEmpty()) {
            this.mModelUpgradeList.remove(0);
        }
        if (arg == 2 && !containsFpgaModel()) {
            notifyUpgradeTip(DataOsdSetUpgradeTip.UPGRADETIP.START, -1, 200, false);
            this.mHandler.sendEmptyMessageDelayed(MSG_ID_FPGA_VERSION, 10000);
        } else if (!this.mModelUpgradeList.isEmpty()) {
            notifyUpgradeTip(DataOsdSetUpgradeTip.UPGRADETIP.START, -1, 200, false);
            this.mHandler.sendEmptyMessageDelayed(4097, DELAY_UPGRADE_NEXT);
        } else if (this.mbGetAllModule) {
            notifyUpgradeSuccess(0);
        } else {
            notifyUpgradeFail(null, Ccode.FIRM_MATCH_WRONG, 106, R.string.rcupgrade_fail_notdetect);
        }
    }

    /* access modifiers changed from: private */
    public boolean isDeviceGetted(DeviceType device, String key) {
        if (DeviceType.FPGA_G != device) {
            return AppPubInjectManager.getAppPubToP3Injectable().isGetted(key);
        }
        if (!AppPubInjectManager.getAppPubToP3Injectable().isGetted(key)) {
            return false;
        }
        String loader = AppPubInjectManager.getAppPubToP3Injectable().getLoaderFromConfig(key);
        DJILogHelper.getInstance().LOGD(TAG, "==== FPGA Loader[" + loader + IMemberProtocol.STRING_SEPERATOR_RIGHT, false, true);
        if (DJIPublicUtils.isEmpty(loader)) {
            return false;
        }
        try {
            return Long.parseLong(loader.replace(".", "")) != 0;
        } catch (Exception e) {
            return false;
        }
    }

    /* access modifiers changed from: private */
    public void recheckUpgrade() {
        boolean next = true;
        if (this.mPackageParseResult != null && !this.mPackageParseResult.mFirmwareInfos.isEmpty()) {
            String key = formatModule(DeviceType.FPGA_G.value(), 0);
            String curVersionStr = AppPubInjectManager.getAppPubToP3Injectable().getVersion(key);
            if (DJIPublicUtils.isEmpty(curVersionStr) || !isDeviceGetted(DeviceType.FPGA_G, key)) {
                this.mbGetAllModule = false;
            } else {
                List<DJIRcPackageParser.FirmwareInfo> firmwares = findFirmwares(DeviceType.FPGA_G, this.mPackageParseResult.mFirmwareInfos);
                if (firmwares == null || firmwares.isEmpty()) {
                    next = false;
                } else {
                    int size = firmwares.size();
                    for (int j = 0; j < size; j++) {
                        DJIRcPackageParser.FirmwareInfo firmware = firmwares.get(j);
                        String module = formatModule(firmware.mDeviceId, firmware.mModuleId);
                        long firmVersion = firmware.mVersion;
                        long firmwareVersion = DJIPublicUtils.formatVersion(curVersionStr);
                        DJILogHelper.getInstance().LOGD(TAG, "====FPGA[" + firmwareVersion + IMemberProtocol.STRING_SEPERATOR_RIGHT + firmVersion + IMemberProtocol.STRING_SEPERATOR_RIGHT, false, true);
                        if (0 == 0 && this.mRbUpgradePack == null && firmwareVersion >= firmVersion) {
                            next = false;
                        } else {
                            ModelCheckStatus model = new ModelCheckStatus();
                            model.mLastestVersion = String.valueOf(firmware.mVersion);
                            model.mDeviceType = DeviceType.FPGA_G;
                            model.mModel = module;
                            model.mModuleId = firmware.mModuleId;
                            model.mNeedUpgrade = true;
                            model.mFirmwareInfo = firmware;
                            this.mModelUpgradeList.add(model);
                        }
                    }
                }
            }
        }
        if (next) {
            upgreadeNext();
            return;
        }
        this.mBeforeUpgradeSize = this.mUpgradedSize;
        updateProgress();
    }

    /* access modifiers changed from: private */
    public void updateProgress() {
        this.mUpgradedSize += (long) ((int) (this.mFpgaDataLength / 30));
        if (this.mUpgradedSize > this.mBeforeUpgradeSize + this.mFpgaDataLength) {
            this.mUpgradedSize = this.mBeforeUpgradeSize + this.mFpgaDataLength;
        }
        int rProgress = (int) ((this.mUpgradedSize * 200) / this.mUpgradeTotalSize);
        if (this.mUpgradeProgess != rProgress) {
            this.mUpgradeProgess = rProgress;
            notifyUpgradeUpdate(rProgress, 200);
        }
        if (this.mUpgradedSize >= this.mBeforeUpgradeSize + this.mFpgaDataLength) {
            upgreadeNext();
        } else {
            this.mHandler.sendEmptyMessageDelayed(4352, 200);
        }
    }

    /* access modifiers changed from: private */
    public void regetDeviceVersion(DeviceType device, boolean force) {
        handleGetDeviceVersion(0, 0, AppPubInjectManager.getAppPubToP3Injectable().getFromKey(formatModule(device.value(), 0)), force);
    }

    /* access modifiers changed from: private */
    public void handleGetDeviceVersion(final int arg1, int arg2, final DataCommonGetVersion getter, boolean force) {
        if (arg1 >= 5) {
            return;
        }
        if (force) {
            getter.startForce(new DJIDataCallBack() {
                /* class dji.pilot.publics.control.rc.DJIRcUpgradeManager.AnonymousClass2 */

                public void onSuccess(Object model) {
                    DeviceType device = getter.getDeviceType();
                    if (DeviceType.FPGA_G == device && !DJIRcUpgradeManager.this.isDeviceGetted(device, DJIRcUpgradeManager.this.formatModule(device.value(), 0))) {
                        DJIRcUpgradeManager.this.mHandler.obtainMessage(24576, arg1 + 1, 0, getter).sendToTarget();
                    }
                }

                public void onFailure(Ccode ccode) {
                    DJIRcUpgradeManager.this.mHandler.obtainMessage(24576, arg1 + 1, 0, getter).sendToTarget();
                }
            });
        } else {
            getter.start(new DJIDataCallBack() {
                /* class dji.pilot.publics.control.rc.DJIRcUpgradeManager.AnonymousClass3 */

                public void onSuccess(Object model) {
                    DeviceType device = getter.getDeviceType();
                    if (DeviceType.FPGA_G == device && !DJIRcUpgradeManager.this.isDeviceGetted(device, DJIRcUpgradeManager.this.formatModule(device.value(), 0))) {
                        DJIRcUpgradeManager.this.mHandler.obtainMessage(24576, arg1 + 1, 0, getter).sendToTarget();
                    }
                }

                public void onFailure(Ccode ccode) {
                    DJIRcUpgradeManager.this.mHandler.obtainMessage(24576, arg1 + 1, 0, getter).sendToTarget();
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void upgreadeNext() {
        if (!this.mModelUpgradeList.isEmpty()) {
            ModelCheckStatus model = this.mModelUpgradeList.get(0);
            notifyUpgradeTip(DataOsdSetUpgradeTip.UPGRADETIP.START, -1, 200, false);
            this.mCurUpgradeModel = new DJIRcUpgradeModel(model.mDeviceType, model.mFirmwareInfo, this.mPackageRAF, this.mUpgradeListener);
            this.mCurUpgradeModel.setNeedRestart(model.mRestart);
            this.mCurUpgradeModel.startUpgrade();
            DJILogHelper.getInstance().LOGD(TAG, "upgreadeNext device[" + this.mCurUpgradeModel.getDeviceType() + IMemberProtocol.STRING_SEPERATOR_RIGHT, true, true);
        } else if (this.mbGetAllModule) {
            notifyUpgradeSuccess(0);
        } else {
            notifyUpgradeFail(null, Ccode.FIRM_MATCH_WRONG, 106, R.string.rcupgrade_fail_notdetect);
        }
    }

    private List<DJIRcPackageParser.FirmwareInfo> findFirmwares(DeviceType type, List<DJIRcPackageParser.FirmwareInfo> firmwares) {
        DJIRcPackageParser.FirmwareInfo tmp;
        List<DJIRcPackageParser.FirmwareInfo> rets = new ArrayList<>();
        int moduleId = -1;
        if (type == DeviceType.TRANSFORM_G) {
            moduleId = AppPubInjectManager.getAppPubToP3Injectable().getLoaderByte(formatModule(type.value(), 0), 2).intValue();
        } else if (type == DeviceType.FPGA_G) {
            moduleId = AppPubInjectManager.getAppPubToP3Injectable().getLoaderByte(formatModule(type.value(), 0), 2).intValue();
        } else if (type == DeviceType.OSD) {
            moduleId = AppPubInjectManager.getAppPubToP3Injectable().getLoaderByte(formatModule(type.value(), 0), 2).intValue();
        } else if (type == DeviceType.DM368_G) {
            moduleId = AppPubInjectManager.getAppPubToP3Injectable().getLoaderByte(formatModule(type.value(), 0), 2).intValue();
        }
        int i = 0;
        int size = firmwares.size();
        while (true) {
            if (i >= size) {
                break;
            }
            tmp = firmwares.get(i);
            if (!(tmp.mDeviceId == type.value() && (moduleId == -1 || moduleId == tmp.mModuleId))) {
                i++;
            }
        }
        rets.add(tmp);
        return rets;
    }

    /* access modifiers changed from: private */
    public String formatModule(int deviceId, int moduleId) {
        return String.format(Locale.US, "%02d%02d", Integer.valueOf(deviceId), Integer.valueOf(moduleId));
    }

    private String getProductName() {
        return "C1";
    }

    /* JADX WARNING: Removed duplicated region for block: B:35:0x008d A[SYNTHETIC, Splitter:B:35:0x008d] */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x0096 A[SYNTHETIC, Splitter:B:40:0x0096] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static boolean checkFirmware(java.lang.String r16, dji.pilot.publics.control.rc.DJIRcPackageParser.FirmwareInfo r17, dji.midware.data.config.P3.DeviceType r18) {
        /*
            r11 = 0
            r8 = 0
            java.io.RandomAccessFile r9 = new java.io.RandomAccessFile     // Catch:{ Exception -> 0x008a, all -> 0x0093 }
            java.io.File r12 = new java.io.File     // Catch:{ Exception -> 0x008a, all -> 0x0093 }
            r0 = r16
            r12.<init>(r0)     // Catch:{ Exception -> 0x008a, all -> 0x0093 }
            java.lang.String r13 = "r"
            r9.<init>(r12, r13)     // Catch:{ Exception -> 0x008a, all -> 0x0093 }
            long r12 = r9.length()     // Catch:{ Exception -> 0x009f, all -> 0x009c }
            r0 = r17
            int r14 = r0.mFileOffset     // Catch:{ Exception -> 0x009f, all -> 0x009c }
            r0 = r17
            int r15 = r0.mDataLength     // Catch:{ Exception -> 0x009f, all -> 0x009c }
            int r14 = r14 + r15
            long r14 = (long) r14     // Catch:{ Exception -> 0x009f, all -> 0x009c }
            int r12 = (r12 > r14 ? 1 : (r12 == r14 ? 0 : -1))
            if (r12 < 0) goto L_0x002a
            dji.midware.data.config.P3.DeviceType r12 = dji.midware.data.config.P3.DeviceType.FPGA_G     // Catch:{ Exception -> 0x009f, all -> 0x009c }
            r0 = r18
            if (r0 != r12) goto L_0x0031
            r11 = 1
        L_0x002a:
            if (r9 == 0) goto L_0x00a2
            r9.close()     // Catch:{ Exception -> 0x0087 }
            r8 = r9
        L_0x0030:
            return r11
        L_0x0031:
            r3 = 4096(0x1000, float:5.74E-42)
            r12 = 4096(0x1000, float:5.74E-42)
            byte[] r4 = new byte[r12]     // Catch:{ Exception -> 0x009f, all -> 0x009c }
            r0 = r17
            int r12 = r0.mFileOffset     // Catch:{ Exception -> 0x009f, all -> 0x009c }
            long r12 = (long) r12     // Catch:{ Exception -> 0x009f, all -> 0x009c }
            r9.seek(r12)     // Catch:{ Exception -> 0x009f, all -> 0x009c }
            r7 = 0
            r0 = r17
            int r12 = r0.mDataLength     // Catch:{ Exception -> 0x009f, all -> 0x009c }
            r13 = 4096(0x1000, float:5.74E-42)
            if (r12 <= r13) goto L_0x0072
            r10 = 4096(0x1000, float:5.74E-42)
        L_0x004a:
            r2 = 0
            java.lang.String r12 = "MD5"
            java.security.MessageDigest r6 = java.security.MessageDigest.getInstance(r12)     // Catch:{ Exception -> 0x009f, all -> 0x009c }
        L_0x0052:
            r12 = 0
            int r7 = r9.read(r4, r12, r10)     // Catch:{ Exception -> 0x009f, all -> 0x009c }
            r12 = -1
            if (r7 == r12) goto L_0x0065
            r12 = 0
            r6.update(r4, r12, r7)     // Catch:{ Exception -> 0x009f, all -> 0x009c }
            int r2 = r2 + r7
            r0 = r17
            int r12 = r0.mDataLength     // Catch:{ Exception -> 0x009f, all -> 0x009c }
            if (r2 < r12) goto L_0x0077
        L_0x0065:
            byte[] r5 = r6.digest()     // Catch:{ Exception -> 0x009f, all -> 0x009c }
            r0 = r17
            byte[] r12 = r0.mMD5     // Catch:{ Exception -> 0x009f, all -> 0x009c }
            boolean r11 = dji.thirdparty.afinal.core.Arrays.equals(r12, r5)     // Catch:{ Exception -> 0x009f, all -> 0x009c }
            goto L_0x002a
        L_0x0072:
            r0 = r17
            int r10 = r0.mDataLength     // Catch:{ Exception -> 0x009f, all -> 0x009c }
            goto L_0x004a
        L_0x0077:
            r0 = r17
            int r12 = r0.mDataLength     // Catch:{ Exception -> 0x009f, all -> 0x009c }
            int r12 = r12 - r2
            r13 = 4096(0x1000, float:5.74E-42)
            if (r12 >= r13) goto L_0x0052
            r0 = r17
            int r12 = r0.mDataLength     // Catch:{ Exception -> 0x009f, all -> 0x009c }
            int r10 = r12 - r2
            goto L_0x0052
        L_0x0087:
            r12 = move-exception
            r8 = r9
            goto L_0x0030
        L_0x008a:
            r12 = move-exception
        L_0x008b:
            if (r8 == 0) goto L_0x0030
            r8.close()     // Catch:{ Exception -> 0x0091 }
            goto L_0x0030
        L_0x0091:
            r12 = move-exception
            goto L_0x0030
        L_0x0093:
            r12 = move-exception
        L_0x0094:
            if (r8 == 0) goto L_0x0099
            r8.close()     // Catch:{ Exception -> 0x009a }
        L_0x0099:
            throw r12
        L_0x009a:
            r13 = move-exception
            goto L_0x0099
        L_0x009c:
            r12 = move-exception
            r8 = r9
            goto L_0x0094
        L_0x009f:
            r12 = move-exception
            r8 = r9
            goto L_0x008b
        L_0x00a2:
            r8 = r9
            goto L_0x0030
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.pilot.publics.control.rc.DJIRcUpgradeManager.checkFirmware(java.lang.String, dji.pilot.publics.control.rc.DJIRcPackageParser$FirmwareInfo, dji.midware.data.config.P3.DeviceType):boolean");
    }

    private boolean checkUpgradeModel() {
        String key;
        boolean ret = false;
        this.mModelUpgradeList.clear();
        String absPath = getPackagePath(generatePackageName(this.mLastestVersion));
        this.mPackageParseResult = DJIRcPackageParser.parsePackageHead(absPath, getProductName());
        if (this.mPackageParseResult != null && !this.mPackageParseResult.mFirmwareInfos.isEmpty()) {
            ret = true;
            int length = this.mDeviceRcs.length;
            for (int i = 0; i < length && ret; i++) {
                List<DJIRcPackageParser.FirmwareInfo> firmwares = findFirmwares(this.mDeviceRcs[i], this.mPackageParseResult.mFirmwareInfos);
                if (firmwares != null && !firmwares.isEmpty()) {
                    int size = firmwares.size();
                    for (int j = 0; j < size; j++) {
                        DJIRcPackageParser.FirmwareInfo firmware = firmwares.get(j);
                        if (DeviceType.TRANSFORM_G == this.mDeviceRcs[i]) {
                            key = formatModule(firmware.mDeviceId, 0);
                        } else if (DeviceType.FPGA_G == this.mDeviceRcs[i]) {
                            key = formatModule(firmware.mDeviceId, 0);
                            this.mFpgaDataLength = (long) firmware.mDataLength;
                        } else {
                            key = formatModule(firmware.mDeviceId, 0);
                        }
                        String module = formatModule(firmware.mDeviceId, firmware.mModuleId);
                        boolean needAdd = false;
                        String curVersionStr = AppPubInjectManager.getAppPubToP3Injectable().getVersion(key);
                        boolean isGetted = isDeviceGetted(this.mDeviceRcs[i], key);
                        if (DJIPublicUtils.isEmpty(curVersionStr) || !isGetted) {
                            needAdd = true;
                            if (!(this.mOsdLoaderMode == 1 && DeviceType.FPGA_G == this.mDeviceRcs[i])) {
                                this.mbGetAllModule = false;
                            }
                        } else {
                            long firmVersion = firmware.mVersion;
                            long firmwareVersion = DJIPublicUtils.formatVersion(curVersionStr);
                            DJILogHelper.getInstance().LOGD(TAG, "Firm[" + firmVersion + "]cur[" + firmwareVersion + IMemberProtocol.STRING_SEPERATOR_RIGHT, false, true);
                            if (!(0 == 0 && this.mRbUpgradePack == null && firmwareVersion >= firmVersion) && (ret = checkFirmware(absPath, firmware, this.mDeviceRcs[i]))) {
                                ModelCheckStatus modelCheckStatus = new ModelCheckStatus();
                                modelCheckStatus.mLastestVersion = String.valueOf(firmware.mVersion);
                                modelCheckStatus.mDeviceType = this.mDeviceRcs[i];
                                modelCheckStatus.mModel = module;
                                modelCheckStatus.mModuleId = firmware.mModuleId;
                                modelCheckStatus.mNeedUpgrade = true;
                                modelCheckStatus.mFirmwareInfo = firmware;
                                if (DeviceType.OSD == this.mDeviceRcs[i] && this.mOsdLoaderMode == 1) {
                                    modelCheckStatus.mRestart = true;
                                    this.mModelUpgradeList.add(0, modelCheckStatus);
                                } else {
                                    this.mModelUpgradeList.add(modelCheckStatus);
                                }
                                needAdd = true;
                            }
                        }
                        if (needAdd) {
                            this.mUpgradeTotalSize += (long) firmware.mDataLength;
                        }
                    }
                }
            }
        }
        String msg = "Type[" + this.mPackageParseResult.mHeadInfo.mProductName + IMemberProtocol.STRING_SEPERATOR_RIGHT;
        int length2 = this.mModelUpgradeList.size();
        String msg2 = (msg + "size[" + length2 + IMemberProtocol.STRING_SEPERATOR_RIGHT) + "model[";
        for (int i2 = 0; i2 < length2; i2++) {
            msg2 = msg2 + this.mModelUpgradeList.get(i2).mDeviceType + ";" + this.mModelUpgradeList.get(i2).mModuleId + ";;";
        }
        DJILogHelper.getInstance().LOGD(TAG, "===" + (msg2 + IMemberProtocol.STRING_SEPERATOR_RIGHT) + IMemberProtocol.STRING_SEPERATOR_RIGHT, true, true);
        return ret;
    }

    private ProductType getProductType(boolean checkLoader) {
        ProductType pType = DJIProductManager.getInstance().getRcType();
        if (!checkLoader || !needCheckLoader(pType)) {
            return pType;
        }
        String key = formatModule(DeviceType.OSD.value(), 0);
        if (!AppPubInjectManager.getAppPubToP3Injectable().isGetted(key) || 50397447 >= DJIPublicUtils.formatVersion(AppPubInjectManager.getAppPubToP3Injectable().getLoaderFromConfig(key))) {
            return pType;
        }
        return ProductType.Pomato;
    }

    private boolean needCheckLoader(ProductType rcType) {
        return rcType != ProductType.Potato;
    }

    private ProductType getProductType() {
        return getProductType(false);
    }

    private String getProductFwPrefix() {
        return RC_FW_PREFIX;
    }

    private String generatePackageName(String version) {
        return getProductFwPrefix() + version + FW_SUFFIX;
    }

    private String getPackagePath(String fileName) {
        return this.mPackageDir + fileName;
    }

    /* access modifiers changed from: private */
    public void restartRc(final int retryTimes, final int delay) {
        if (retryTimes < 100) {
            new DataCommonRestartDevice().setReceiveType(DeviceType.RC).setRestartType(0).setDelay(delay * 1000).start(new DJIDataCallBack() {
                /* class dji.pilot.publics.control.rc.DJIRcUpgradeManager.AnonymousClass4 */

                public void onSuccess(Object model) {
                }

                public void onFailure(Ccode ccode) {
                    DJIRcUpgradeManager.this.mHandler.obtainMessage(4096, retryTimes, delay, ccode).sendToTarget();
                }
            });
        }
    }

    private void checkOsdStatus(final int retryTimes) {
        if (retryTimes < 5) {
            final DataCommonGetDeviceStatus getter = new DataCommonGetDeviceStatus();
            getter.setReceiveType(DeviceType.OSD).setReceiveId(0).setVersioin(0, 0).start(new DJIDataCallBack() {
                /* class dji.pilot.publics.control.rc.DJIRcUpgradeManager.AnonymousClass5 */

                public void onSuccess(Object model) {
                    DJIRcUpgradeManager.this.mHandler.obtainMessage(20480, 0, getter.getMode()).sendToTarget();
                }

                public void onFailure(Ccode ccode) {
                    DJIRcUpgradeManager.this.mHandler.obtainMessage(20480, 1, retryTimes, ccode).sendToTarget();
                }
            });
            return;
        }
        this.mOsdLoaderMode = 0;
        upgradeInner();
    }

    /* access modifiers changed from: private */
    public void handleCheckOsdStatus(int result, int arg2, Ccode code) {
        DJILogHelper.getInstance().LOGD(TAG, "Osd1765 Result[" + result + "]Status[" + arg2 + IMemberProtocol.STRING_SEPERATOR_RIGHT, true, true);
        if (result == 0) {
            this.mOsdLoaderMode = arg2;
            upgradeInner();
            return;
        }
        checkOsdStatus(arg2 + 1);
    }

    /* access modifiers changed from: private */
    public void notifyUpgradeTip(DataOsdSetUpgradeTip.UPGRADETIP tip, int times, int delay, boolean autoDelay) {
        this.mHandler.removeMessages(8192);
        if (tip == DataOsdSetUpgradeTip.UPGRADETIP.START && autoDelay) {
            delay = 5000;
        }
        this.mUpgradeTipSetter.setUpgradeTip(tip).start((DJIDataCallBack) null);
        int arg1 = times == -1 ? -1 : times - 1;
        if (arg1 == -1 || arg1 > 0) {
            this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(8192, arg1, delay, tip), (long) delay);
        }
    }

    private DJIRcUpgradeManager() {
        this.mDeviceRcs = DEVICES_RC_368;
        this.mAppCxt = null;
        this.mInit = false;
        this.mPackageDir = null;
        this.mOtherStatus = 512;
        this.mRcStatus = 256;
        this.mCurrentVersion = "";
        this.mLastestVersion = "";
        this.mUpgradePack = null;
        this.mModelUpgradeList = new ArrayList<>();
        this.mPackageParseResult = null;
        this.mUpgradeListener = null;
        this.mUpgradeTotalSize = 0;
        this.mUpgradedSize = 0;
        this.mUpgradeProgess = 0;
        this.mFpgaDataLength = 0;
        this.mbGetAllModule = true;
        this.mBeforeUpgradeSize = 0;
        this.mPackageRAF = null;
        this.mCurUpgradeModel = null;
        this.mInnerDLListener = null;
        this.mExterDLListener = null;
        this.mExterUpgradeListener = null;
        this.mEventListener = null;
        this.mFinalDb = null;
        this.mDLPackageList = new ArrayList<>();
        this.mDevicePackages = new ArrayList<>();
        this.mDownloadHandlers = new HashMap<>();
        this.mCurDLPackage = null;
        this.mFinalHttp = null;
        this.mHandler = null;
        this.mUpgradeTipSetter = new DataOsdSetUpgradeTip();
        this.mSupportAppUpgrade = true;
        this.mOsdLoaderMode = 0;
        this.mRbUpgradePack = null;
        this.mHandler = new UIHandler(this);
        this.mUpgradeListener = new DJIRcUpgradeModel.OnModelUpgradeListener() {
            /* class dji.pilot.publics.control.rc.DJIRcUpgradeManager.AnonymousClass6 */

            public void onUpdate(DJIRcUpgradeModel model, int progress, int count) {
                long unused = DJIRcUpgradeManager.this.mUpgradedSize = DJIRcUpgradeManager.this.mUpgradedSize + ((long) progress);
                int rProgress = (int) ((DJIRcUpgradeManager.this.mUpgradedSize * 200) / DJIRcUpgradeManager.this.mUpgradeTotalSize);
                if (DJIRcUpgradeManager.this.mUpgradeProgess != rProgress) {
                    int unused2 = DJIRcUpgradeManager.this.mUpgradeProgess = rProgress;
                    DJIRcUpgradeManager.this.notifyUpgradeUpdate(rProgress, 200);
                }
            }

            public void onSuccess(DJIRcUpgradeModel model, int arg) {
                DJIRcUpgradeManager.this.upgradeModelSuccess(arg);
            }

            public void onStart(DJIRcUpgradeModel model, int arg) {
                int rProgress = (int) ((DJIRcUpgradeManager.this.mUpgradedSize * 200) / DJIRcUpgradeManager.this.mUpgradeTotalSize);
                if (DJIRcUpgradeManager.this.mUpgradeProgess != rProgress) {
                    int unused = DJIRcUpgradeManager.this.mUpgradeProgess = rProgress;
                    DJIRcUpgradeManager.this.notifyUpgradeUpdate(rProgress, 200);
                }
            }

            public void onFail(DJIRcUpgradeModel model, Ccode errCode, int err, int arg) {
                DJIRcUpgradeManager.this.notifyUpgradeFail(model, errCode, err, arg);
            }
        };
        this.mInnerDLListener = new OnDLPackagedListener() {
            /* class dji.pilot.publics.control.rc.DJIRcUpgradeManager.AnonymousClass7 */

            public void onUpdate(DJIDLPackageInfo packInfo, long current, long count) {
                if (DJIRcUpgradeManager.this.findDLPackage(packInfo.mFileName) != null) {
                    DJIRcUpgradeManager.this.mFinalDb.update(packInfo, "mAbsPath='" + packInfo.mAbsPath + "'");
                    if (DJIRcUpgradeManager.this.mCurDLPackage == packInfo && DJIRcUpgradeManager.this.mExterDLListener != null) {
                        DJIRcUpgradeManager.this.mExterDLListener.onUpdate(packInfo, (long) (count != 0 ? (int) ((current * 200) / count) : 0), 200);
                    }
                }
            }

            public void onSuccess(DJIDLPackageInfo packInfo, int arg) {
                if (DJIRcUpgradeManager.this.findDLPackage(packInfo.mFileName) != null) {
                    DJIRcUpgradeManager.this.mFinalDb.update(packInfo, "mAbsPath='" + packInfo.mAbsPath + "'");
                    DJIRcUpgradeManager.this.mDownloadHandlers.remove(packInfo);
                    if (DJIRcUpgradeManager.this.mCurDLPackage == packInfo) {
                        int unused = DJIRcUpgradeManager.this.mRcStatus = 261;
                        if (DJIRcUpgradeManager.this.mExterDLListener != null) {
                            DJIRcUpgradeManager.this.mExterDLListener.onSuccess(packInfo, arg);
                        }
                    }
                }
            }

            public void onStart(DJIDLPackageInfo packInfo, boolean isResume) {
                if (DJIRcUpgradeManager.this.findDLPackage(packInfo.mFileName) != null) {
                    DJIRcUpgradeManager.this.mFinalDb.update(packInfo, "mAbsPath='" + packInfo.mAbsPath + "'");
                    if (DJIRcUpgradeManager.this.mCurDLPackage == packInfo && DJIRcUpgradeManager.this.mExterDLListener != null) {
                        DJIRcUpgradeManager.this.mExterDLListener.onStart(packInfo, isResume);
                    }
                }
            }

            public void onFail(DJIDLPackageInfo packInfo, int errorNo, String errMsg) {
                if (DJIRcUpgradeManager.this.findDLPackage(packInfo.mFileName) != null) {
                    DJIRcUpgradeManager.this.mFinalDb.update(packInfo, "mAbsPath='" + packInfo.mAbsPath + "'");
                    HttpHandler<File> httpHandler = (HttpHandler) DJIRcUpgradeManager.this.mDownloadHandlers.remove(packInfo);
                    if (DJIRcUpgradeManager.this.mCurDLPackage == packInfo && httpHandler != null && !httpHandler.isStop()) {
                        int unused = DJIRcUpgradeManager.this.mRcStatus = 260;
                        if (DJIRcUpgradeManager.this.mExterDLListener != null) {
                            DJIRcUpgradeManager.this.mExterDLListener.onFail(packInfo, errorNo, errMsg);
                        }
                    }
                }
            }
        };
    }

    private static final class SingletonHolder {
        /* access modifiers changed from: private */
        public static final DJIRcUpgradeManager mInstance = new DJIRcUpgradeManager();

        private SingletonHolder() {
        }
    }

    private static final class ModelCheckStatus {
        public int mArg;
        public String mCurrentVersion;
        public DeviceType mDeviceType;
        public DJIRcPackageParser.FirmwareInfo mFirmwareInfo;
        public String mLastestVersion;
        public String mModel;
        public int mModuleId;
        public boolean mNeedUpgrade;
        public boolean mRestart;

        private ModelCheckStatus() {
            this.mDeviceType = DeviceType.OTHER;
            this.mModuleId = 0;
            this.mModel = null;
            this.mNeedUpgrade = false;
            this.mCurrentVersion = null;
            this.mLastestVersion = null;
            this.mFirmwareInfo = null;
            this.mArg = 0;
            this.mRestart = false;
        }
    }

    private static final class DownCallBack extends AjaxCallBack<File> {
        private static final int INTERVAL_RATE = 1000;
        private OnDLPackagedListener mDLListener = null;
        private DJIDLPackageInfo mDLPackage = null;
        private int mDlRate = 0;
        private long mLastDlSize = 0;
        private long mLastDlTime = 0;

        private DownCallBack(DJIDLPackageInfo dlPackage, OnDLPackagedListener listener) {
            this.mDLPackage = dlPackage;
            this.mDLListener = listener;
        }

        public void onStart(boolean isResume) {
            this.mDLPackage.mDLStatus = 2;
            this.mDLListener.onStart(this.mDLPackage, isResume);
            this.mLastDlTime = 0;
        }

        public void onLoading(long count, long current) {
            if (!(this.mDLPackage.mPackageSize == count || count == 0)) {
                this.mDLPackage.mPackageSize = count;
            }
            long now = System.currentTimeMillis();
            if (now - this.mLastDlTime >= 1000) {
                int dlSize = (int) (current - this.mLastDlSize);
                if (dlSize >= 0) {
                    this.mDlRate = (int) (((((long) dlSize) / (now - this.mLastDlTime)) * 10000) / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID);
                } else {
                    this.mDlRate = 0;
                }
                this.mLastDlTime = now;
                this.mLastDlSize = current;
            }
            this.mDLPackage.mDLSize = current;
            if (this.mDLPackage.mDLSize > count && count != 0) {
                this.mDLPackage.mDLSize = count;
            }
            this.mDLListener.onUpdate(this.mDLPackage, current, count);
        }

        public void onSuccess(File t) {
            this.mDlRate = 0;
            this.mDLPackage.mDLStatus = 3;
            this.mDLListener.onSuccess(this.mDLPackage, 0);
        }

        public void onFailure(Throwable t, int errorNo, String strMsg) {
            this.mDlRate = 0;
            this.mDLPackage.mDLStatus = 4;
            this.mDLListener.onFail(this.mDLPackage, errorNo, strMsg);
        }
    }

    private static final class UIHandler extends Handler {
        private final WeakReference<DJIRcUpgradeManager> mOutCls;

        public UIHandler(DJIRcUpgradeManager manager) {
            super(Looper.getMainLooper());
            this.mOutCls = new WeakReference<>(manager);
        }

        public void handleMessage(Message msg) {
            DJIRcUpgradeManager manager = this.mOutCls.get();
            if (manager != null) {
                switch (msg.what) {
                    case 4096:
                        if (manager.mRcStatus == 263) {
                            manager.restartRc(msg.arg1 + 1, msg.arg2);
                            return;
                        }
                        return;
                    case 4097:
                        manager.upgreadeNext();
                        return;
                    case 4098:
                        manager.recheckUpgrade();
                        return;
                    case 4352:
                        manager.updateProgress();
                        return;
                    case 8192:
                        if (msg.obj instanceof DataOsdSetUpgradeTip.UPGRADETIP) {
                            manager.notifyUpgradeTip((DataOsdSetUpgradeTip.UPGRADETIP) msg.obj, msg.arg1, msg.arg2, true);
                            return;
                        }
                        return;
                    case 20480:
                        manager.handleCheckOsdStatus(msg.arg1, msg.arg2, msg.obj instanceof Ccode ? (Ccode) msg.obj : Ccode.UNDEFINED);
                        return;
                    case 24576:
                        if (msg.obj instanceof DataCommonGetVersion) {
                            manager.handleGetDeviceVersion(msg.arg1, msg.arg2, (DataCommonGetVersion) msg.obj, false);
                            return;
                        }
                        return;
                    case DJIRcUpgradeManager.MSG_ID_FPGA_VERSION /*39321*/:
                        manager.regetDeviceVersion(DeviceType.FPGA_G, true);
                        sendEmptyMessageDelayed(4098, 10000);
                        return;
                    default:
                        return;
                }
            }
        }
    }
}
