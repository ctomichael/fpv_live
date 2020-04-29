package dji.dbox.upgrade.p4.statemachine;

import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import com.dji.frame.util.MD5;
import com.lmax.disruptor.TimeoutException;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import dji.component.accountcenter.IMemberProtocol;
import dji.dbox.upgrade.p4.constants.DJIUpConstants;
import dji.dbox.upgrade.p4.constants.DJIUpDeviceType;
import dji.dbox.upgrade.p4.model.DJIUpStatus;
import dji.dbox.upgrade.p4.utils.DJIUpStatusHelper;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.Dpad.DpadProductType;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.Dpad.DpadProductManager;
import dji.midware.data.manager.P3.DataBaseProcessor;
import dji.midware.data.model.P3.DataCameraGetPushUpgradeStatus;
import dji.midware.data.model.P3.DataCommonGetPushUpgradeStatus;
import dji.midware.data.model.P3.DataCommonGetVersion;
import dji.midware.data.model.P3.DataCommonRequestReceiveData;
import dji.midware.data.model.P3.DataCommonRequestUpgrade;
import dji.midware.data.model.P3.DataCommonTranslateComplete;
import dji.midware.data.model.P3.DataCommonTranslateData;
import dji.midware.data.model.P3.DataOsdGetPushSdrLinkMode;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.link.DJILinkDaemonService;
import dji.midware.link.DJILinkModeController;
import dji.midware.link.DJILinkType;
import dji.midware.media.DJIVideoDecoder;
import dji.midware.natives.UDT;
import dji.midware.sockets.P3.SwUdpService;
import dji.midware.transfer.FileTransferFirmware;
import dji.midware.transfer.FirmTransferListener;
import dji.midware.util.BytesUtil;
import dji.midware.util.RepeatDataBase;
import dji.midware.wifi.DJIMultiNetworkMgr;
import dji.publics.DJIObject.DJIObject;
import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

@EXClassNullAway
public class DJIUpUploadManager extends DJIObject implements SwUdpService.SwUdpConnectListener {
    private static final int MAX_REQUEST_REVERSE_TIMES = 4;
    private static final long TIMEOUT_STOP_SPECIAL_SEND_CHANNEL = 3000;
    /* access modifiers changed from: private */
    public final String TAG = getClass().getSimpleName();
    private RandomAccessFile accessFile;
    /* access modifiers changed from: private */
    public int comTimes = 0;
    private byte[] dataBuffer = new byte[2048];
    /* access modifiers changed from: private */
    public DataCommonRequestUpgrade.DJIUpgradeFileMethod fileMethod;
    /* access modifiers changed from: private */
    public long fileOffset = 0;
    private String filePath = "";
    /* access modifiers changed from: private */
    public long fileTotalSize = 0;
    /* access modifiers changed from: private */
    public DataCommonGetVersion getVersion = new DataCommonGetVersion();
    private boolean isDebugMode = true;
    private byte[] md5;
    private FileTransferFirmware multiFileStrategy;
    /* access modifiers changed from: private */
    public int packIndex = 0;
    /* access modifiers changed from: private */
    public int packNonSequenceNum = 0;
    private int packPoint = 4000;
    private int packSleep = 2;
    private int packTimes = 0;
    /* access modifiers changed from: private */
    public int packUnitLength = 0;
    private DataCommonGetPushUpgradeStatus pushUpgradeStatus = new DataCommonGetPushUpgradeStatus();
    private DataCommonRequestReceiveData requestReceiveData = DataCommonRequestReceiveData.getInstance().setReceiveType(DeviceType.DM368).setReceiveId(1);
    /* access modifiers changed from: private */
    public int requestReverseTimes;
    /* access modifiers changed from: private */
    public DataCommonRequestUpgrade requestUpgrade = DataCommonRequestUpgrade.getInstance().setReceiveType(DeviceType.DM368).setReceiveId(1);
    /* access modifiers changed from: private */
    public int retryFtpTimes = 0;
    /* access modifiers changed from: private */
    public long startTime;
    /* access modifiers changed from: private */
    public DJIUpgradeStateMachine stateMachine;
    DJIUpStatus status;
    private final String testPath = (Environment.getExternalStorageDirectory().getPath() + "/DJI/p4_08.bin");
    /* access modifiers changed from: private */
    public DataCommonRequestUpgrade.DJIUpgradeTranMethod tranMethod;
    private DataCommonTranslateComplete translateComplete = DataCommonTranslateComplete.getInstance().setReceiveType(DeviceType.DM368).setReceiveId(1);
    /* access modifiers changed from: private */
    public DataCommonTranslateData translateData = DataCommonTranslateData.getInstance().setReceiveType(DeviceType.DM368).setReceiveId(1);

    public interface LinkModeCallBack {
        void onFailed(Ccode ccode);

        void onNoNeed();

        void onSuccess(Object obj);
    }

    static /* synthetic */ int access$1508(DJIUpUploadManager x0) {
        int i = x0.requestReverseTimes;
        x0.requestReverseTimes = i + 1;
        return i;
    }

    static /* synthetic */ int access$2308(DJIUpUploadManager x0) {
        int i = x0.packNonSequenceNum;
        x0.packNonSequenceNum = i + 1;
        return i;
    }

    static /* synthetic */ int access$2708(DJIUpUploadManager x0) {
        int i = x0.comTimes;
        x0.comTimes = i + 1;
        return i;
    }

    public DJIUpUploadManager(DJIUpgradeStateMachine stateMachine2) {
        this.stateMachine = stateMachine2;
    }

    public boolean init(boolean isResetSteps) {
        this.status = DJIUpStatusHelper.getUpgradingStatus();
        DJIUpConstants.LOGE(this.TAG, "upload deviceType=" + this.status.getDeviceType());
        this.requestUpgrade.setReceiveType(this.status.getDeviceType());
        this.requestUpgrade.setReceiveId(this.status.getDeviceId());
        this.requestReceiveData.setReceiveType(this.status.getDeviceType());
        this.requestReceiveData.setReceiveId(this.status.getDeviceId());
        this.translateComplete.setReceiveType(this.status.getDeviceType());
        this.translateComplete.setReceiveId(this.status.getDeviceId());
        if (isResetSteps) {
            resetSteps();
        }
        reset();
        if (this.status.isUseMultiFile()) {
            DJIUpConstants.LOGD(this.TAG, "DJIUpUploadManager init isUseMultiFile");
            return init(DJIUpTarManager.getUpgradeFiles());
        } else if (DJIUpStatusHelper.isUpgradeFromSDCard()) {
            String sdTarPath = this.status.getLatestElement().sdTarPath;
            DJIUpConstants.LOGD(this.TAG, "DJIUpUploadManager init isUseTar path=" + sdTarPath);
            init(sdTarPath);
            return true;
        } else {
            DJIUpConstants.LOGD(this.TAG, "DJIUpUploadManager init isUseTar");
            return init(DJIUpTarManager.getTarFilePath());
        }
    }

    /* access modifiers changed from: private */
    public void pushStatus(DataCameraGetPushUpgradeStatus.UpgradeStep step) {
        if (DJIUpStatusHelper.isNeedAppPushUpgradeStatus()) {
            log("app push status " + step);
            this.pushUpgradeStatus.pushStatus(step, DeviceType.OSD.value(), 0);
        }
    }

    private boolean init(List<String> filesPath) {
        this.multiFileStrategy = new FileTransferFirmware(this.status.getDeviceType(), this.status.getDeviceId());
        this.multiFileStrategy.setFileTransferStrategies(DJIUpDeviceType.getStrategy(this.status.getProductId()));
        boolean success = this.multiFileStrategy.initFirmwareList(filesPath, new FirmTransferListener() {
            /* class dji.dbox.upgrade.p4.statemachine.DJIUpUploadManager.AnonymousClass1 */

            public void initFirmware(int index, File file) {
                DJIUpUploadManager.this.log("init firmware file=" + file.getName() + " exists=" + file.exists() + " size=" + (((float) file.length()) / 1024.0f) + "kb index=" + index);
            }

            public void onUploadStart(int count, int length) {
                DJIUpUploadManager.this.log("upload multi files start count=" + count + " length=" + length + "MB");
                DJIUpUploadManager.this.pushStatus(DataCameraGetPushUpgradeStatus.UpgradeStep.Progress);
                DataBaseProcessor.getInstance().startSpecialSendChannel();
            }

            public void onUploadSuccess(int index) {
                DJIUpUploadManager.this.log("upload multi files success index =" + index);
            }

            public void onUploadRate(float rate) {
            }

            public void onUploadProgress(int progress) {
                DJIUpUploadManager.this.pushStatus(DataCameraGetPushUpgradeStatus.UpgradeStep.Progress);
                DJIUpUploadManager.this.stateMachine.uploadNextPack(false);
            }

            public void onUploadComplete(int seconds, int speed, float lossRate, float exceptionAckRate) {
                DJIUpUploadManager.this.log("upload multi files Complete time:" + seconds + "s speed:" + speed + "kb/s lossRate=" + lossRate + "% exceptionAckRate=" + exceptionAckRate + "%");
                try {
                    DataBaseProcessor.getInstance().stopSpecialSendChannel(DJIUpUploadManager.TIMEOUT_STOP_SPECIAL_SEND_CHANNEL);
                    DJIUpUploadManager.this.log("stop special send channel success");
                } catch (TimeoutException e) {
                    DJIUpUploadManager.this.log("stop special send channel timeout: " + e.getMessage());
                } finally {
                    DJIUpUploadManager.this.log("stop special send channel finally");
                    DJIUpUploadManager.this.tryResumeLinkMode(new LinkModeCallBack() {
                        /* class dji.dbox.upgrade.p4.statemachine.DJIUpUploadManager.AnonymousClass1.C00011 */

                        public void onSuccess(Object model) {
                            DJIUpUploadManager.this.translateComplete();
                        }

                        public void onFailed(Ccode ccode) {
                            DJIUpUploadManager.this.stateMachine.uploadFailQuit(ccode);
                            DJIUpUploadManager.this.pushStatus(DataCameraGetPushUpgradeStatus.UpgradeStep.End);
                        }

                        public void onNoNeed() {
                            DJIUpUploadManager.this.translateComplete();
                        }
                    });
                }
            }

            public void onUploadFailed(String info, Ccode code) {
                DJIUpUploadManager.this.log("upload multi files failed info=" + info + " code=" + code);
                try {
                    DataBaseProcessor.getInstance().stopSpecialSendChannel(DJIUpUploadManager.TIMEOUT_STOP_SPECIAL_SEND_CHANNEL);
                    DJIUpUploadManager.this.log("stop special send channel success");
                    DJIUpUploadManager.this.log("stop special send channel finally");
                    if (code == null) {
                        DJIUpUploadManager.this.stateMachine.uploadFailEnter();
                    } else {
                        DJIUpUploadManager.this.stateMachine.uploadFailTraning();
                    }
                    DJIUpUploadManager.this.pushStatus(DataCameraGetPushUpgradeStatus.UpgradeStep.End);
                    DJIUpUploadManager.this.tryResumeLinkMode(new LinkModeCallBack() {
                        /* class dji.dbox.upgrade.p4.statemachine.DJIUpUploadManager.AnonymousClass1.AnonymousClass2 */

                        public void onSuccess(Object model) {
                        }

                        public void onFailed(Ccode ccode) {
                        }

                        public void onNoNeed() {
                        }
                    });
                } catch (TimeoutException e) {
                    DJIUpUploadManager.this.log("stop special send channel timeout: " + e.getMessage());
                    DJIUpUploadManager.this.log("stop special send channel finally");
                    if (code == null) {
                        DJIUpUploadManager.this.stateMachine.uploadFailEnter();
                    } else {
                        DJIUpUploadManager.this.stateMachine.uploadFailTraning();
                    }
                    DJIUpUploadManager.this.pushStatus(DataCameraGetPushUpgradeStatus.UpgradeStep.End);
                    DJIUpUploadManager.this.tryResumeLinkMode(new LinkModeCallBack() {
                        /* class dji.dbox.upgrade.p4.statemachine.DJIUpUploadManager.AnonymousClass1.AnonymousClass2 */

                        public void onSuccess(Object model) {
                        }

                        public void onFailed(Ccode ccode) {
                        }

                        public void onNoNeed() {
                        }
                    });
                } catch (Throwable th) {
                    DJIUpUploadManager.this.log("stop special send channel finally");
                    if (code == null) {
                        DJIUpUploadManager.this.stateMachine.uploadFailEnter();
                    } else {
                        DJIUpUploadManager.this.stateMachine.uploadFailTraning();
                    }
                    DJIUpUploadManager.this.pushStatus(DataCameraGetPushUpgradeStatus.UpgradeStep.End);
                    DJIUpUploadManager.this.tryResumeLinkMode(new LinkModeCallBack() {
                        /* class dji.dbox.upgrade.p4.statemachine.DJIUpUploadManager.AnonymousClass1.AnonymousClass2 */

                        public void onSuccess(Object model) {
                        }

                        public void onFailed(Ccode ccode) {
                        }

                        public void onNoNeed() {
                        }
                    });
                    throw th;
                }
            }
        });
        if (success) {
            this.fileTotalSize = this.multiFileStrategy.getTotalLength();
        }
        return success;
    }

    private boolean init(String filePath2) {
        this.translateData.setReceiveType(this.status.getDeviceType());
        this.translateData.setReceiveId(this.status.getDeviceId());
        this.filePath = filePath2;
        if (this.isDebugMode && filePath2 == null) {
            filePath2 = this.testPath;
        }
        if (new File(filePath2).exists()) {
            this.md5 = MD5.getMD5(new File(filePath2));
            log("md5=" + BytesUtil.byte2hex(this.md5));
            try {
                this.accessFile = new RandomAccessFile(filePath2, "r");
                this.fileTotalSize = this.accessFile.length();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                this.stateMachine.uploadFailInit();
                return false;
            }
        } else {
            DJIUpConstants.LOGE(this.TAG, "tar 文件不存在");
            this.stateMachine.uploadFailInit();
            return false;
        }
    }

    private void resetSteps() {
    }

    private void reset() {
        this.packIndex = 0;
        this.packNonSequenceNum = 0;
        this.packTimes = 0;
        this.startTime = 0;
        this.fileTotalSize = 0;
        this.fileOffset = 0;
        if (this.accessFile != null) {
            try {
                this.accessFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.multiFileStrategy = null;
    }

    /* access modifiers changed from: private */
    public void log(String msg) {
        DJIUpConstants.LOGE(this.TAG, msg);
    }

    public void start() {
        if (this.fileTotalSize > 0) {
            requestUpgrade();
        }
    }

    private void requestUpgrade() {
        this.getVersion.setDeviceType(DeviceType.WHO);
        this.getVersion.start(new DJIDataCallBack() {
            /* class dji.dbox.upgrade.p4.statemachine.DJIUpUploadManager.AnonymousClass2 */

            public void onSuccess(Object model) {
                DJIUpConstants.LOGE(DJIUpUploadManager.this.TAG, "requestUpgrade whoamI " + DJIUpUploadManager.this.getVersion.getWhoamI());
                DJIUpConstants.LOGE(DJIUpUploadManager.this.TAG, "requestUpgrade " + DJIUpUploadManager.this.requestUpgrade.getmReceiveType());
                DJIUpUploadManager.this.requestUpgradeAnyWay();
            }

            public void onFailure(Ccode ccode) {
                DJIUpConstants.LOGE(DJIUpUploadManager.this.TAG, "uploadFailEnter when whoamI onFailure " + ccode + " " + ccode.relValue());
                DJIUpUploadManager.this.requestUpgradeAnyWay();
            }
        });
    }

    /* access modifiers changed from: private */
    public void requestUpgradeAnyWay() {
        new RepeatDataBase(this.requestUpgrade, 3, DJIVideoDecoder.connectLosedelay, new DJIDataCallBack() {
            /* class dji.dbox.upgrade.p4.statemachine.DJIUpUploadManager.AnonymousClass3 */

            public void onSuccess(Object model) {
                DataCommonRequestUpgrade.DJIUpgradeTranMethod mtranMethod = DJIUpUploadManager.this.requestUpgrade.getTranMethodEntry();
                DataCommonRequestUpgrade.DJIUpgradeFileMethod mfileMethod = DJIUpUploadManager.this.requestUpgrade.getTranFileEntry();
                if (!mtranMethod.isSupportV1 && !mtranMethod.isSupportFTP) {
                    DJIUpConstants.LOGE(DJIUpUploadManager.this.TAG, "isSupportV1 && isSupportFTP false");
                    DJIUpUploadManager.this.stateMachine.uploadFailEnter();
                } else if (!mfileMethod.isSupportBigPackage && DJIUpUploadManager.this.status.getProductId().isUseTar()) {
                    DJIUpConstants.LOGE(DJIUpUploadManager.this.TAG, "isSupportBigPackage false");
                    DJIUpUploadManager.this.stateMachine.uploadFailEnter();
                } else if (mfileMethod.isSupportMultiFile || !DJIUpUploadManager.this.status.getProductId().isUseMultiFile()) {
                    DataCommonRequestUpgrade.DJIUpgradeFileMethod unused = DJIUpUploadManager.this.fileMethod = (DataCommonRequestUpgrade.DJIUpgradeFileMethod) mfileMethod.clone();
                    DataCommonRequestUpgrade.DJIUpgradeTranMethod unused2 = DJIUpUploadManager.this.tranMethod = (DataCommonRequestUpgrade.DJIUpgradeTranMethod) mtranMethod.clone();
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    DJIUpUploadManager.this.requestReceiveData();
                } else {
                    DJIUpConstants.LOGE(DJIUpUploadManager.this.TAG, "isSupportMultiFile false for wm230");
                    DJIUpUploadManager.this.stateMachine.uploadFailEnter();
                }
            }

            public void onFailure(Ccode ccode) {
                DJIUpConstants.LOGE(DJIUpUploadManager.this.TAG, "uploadFailEnter onFailure " + ccode + " " + ccode.relValue());
                DJIUpUploadManager.this.stateMachine.uploadFailEnter();
            }
        }).start();
    }

    /* access modifiers changed from: private */
    public void requestReceiveData() {
        this.fileMethod.reset();
        if (this.status.isUseMultiFile()) {
            this.fileMethod.isSupportMultiFile = true;
        } else {
            this.fileMethod.isSupportBigPackage = true;
        }
        boolean isSupportV1 = this.tranMethod.isSupportV1;
        boolean isSupportFTP = this.tranMethod.isSupportFTP;
        DJIUpConstants.LOGE(this.TAG, "requestReceiveData isSupportV1=" + isSupportV1 + " isSupportFTP=" + isSupportFTP);
        this.tranMethod.reset();
        if (isSupportFTP && this.status.getProductId() == DJIUpDeviceType.wm100ac && DJILinkDaemonService.getInstance().getLinkType() == DJILinkType.WIFI) {
            this.tranMethod.isSupportFTP = true;
        } else {
            this.tranMethod.isSupportV1 = true;
        }
        this.requestReceiveData.setDataLength(this.fileTotalSize);
        this.requestReceiveData.setTranMethod(this.tranMethod);
        this.requestReceiveData.setFileMethod(this.fileMethod);
        new RepeatDataBase(this.requestReceiveData, 3, DJIVideoDecoder.connectLosedelay, new DJIDataCallBack() {
            /* class dji.dbox.upgrade.p4.statemachine.DJIUpUploadManager.AnonymousClass4 */

            public void onSuccess(Object model) {
                if (DJIUpUploadManager.this.tranMethod.isSupportFTP) {
                    int unused = DJIUpUploadManager.this.retryFtpTimes = 0;
                    DJIUpUploadManager.this.sendWithFtp();
                    return;
                }
                DJIUpUploadManager.this.sendWithV1();
            }

            public void onFailure(Ccode ccode) {
                DJIUpConstants.LOGE(DJIUpUploadManager.this.TAG, "uploadFailPreTran onFailure " + ccode);
                DJIUpUploadManager.this.stateMachine.uploadFailPreTran();
            }
        }).start();
    }

    /* access modifiers changed from: private */
    public void sendWithV1() {
        this.requestReverseTimes = 0;
        this.packPoint = 4000;
        if (DpadProductManager.getInstance().getProductType() == DpadProductType.PomatoSdr) {
            this.packPoint = 200;
        } else if (DJIUpDeviceType.wm335.equals(this.status.getProductId()) && !DpadProductManager.getInstance().isDpad()) {
            this.packPoint = 1000;
            this.packSleep = -1;
        } else if (DJIUpDeviceType.rc010.equals(this.status.getProductId())) {
            this.packPoint = 1;
        } else if (DJIUpDeviceType.rm500.equals(this.status.getProductId())) {
            this.packPoint = 250;
            this.packSleep = 4;
        }
        DJIUpConstants.LOGE(this.TAG, "sendWithV1 packPoint=" + this.packPoint + " packSleep=" + this.packSleep);
        if (!this.status.getProductId().equals(DJIUpDeviceType.rc002) || !SwUdpService.getInstance().isRcConnect()) {
            tryRequestReverseLink();
            return;
        }
        DJIUpConstants.LOGE(this.TAG, "sw reverse start");
        SwUdpService.getInstance().setConnectListener(this);
        DJIUpConstants.LOGE(this.TAG, "sw reverse result = " + SwUdpService.getInstance().enReverseSw());
        this.packPoint = 200;
    }

    /* access modifiers changed from: private */
    public void tryRequestReverseLink() {
        final DJIUpDeviceType product = this.status.getProductId();
        if (DJIUpDeviceType.isNeedRequestReverse(product)) {
            DJIUpConstants.LOGD(this.TAG, "product=" + product + " reverse link mode");
            DJILinkModeController.getInstance().setReceiver(DeviceType.DM368_G.value(), 1).requestReverse(new DJILinkModeController.LinkModeSwitchCallBack() {
                /* class dji.dbox.upgrade.p4.statemachine.DJIUpUploadManager.AnonymousClass5 */

                public void onSuccess(Object model) {
                    DJIUpConstants.LOGD(DJIUpUploadManager.this.TAG, "product=" + product + " reverse link mode success");
                    DJIUpUploadManager.this.stateMachine.uploadNextPack(false);
                    DJIUpUploadManager.this.startSendFirmData();
                }

                public void onFailure(Ccode ccode) {
                    DJIUpConstants.LOGD(DJIUpUploadManager.this.TAG, "product=" + product + " reverse link mode failed ccode = " + ccode);
                    if (ccode != Ccode.UPDATE_WAIT_FINISH || DJIUpUploadManager.this.requestReverseTimes > 4) {
                        DJIUpUploadManager.this.stateMachine.uploadFailQuit(ccode);
                        return;
                    }
                    DJIUpUploadManager.access$1508(DJIUpUploadManager.this);
                    SystemClock.sleep(5000);
                    DJIUpConstants.LOGD(DJIUpUploadManager.this.TAG, "request reverse waiting for finish times=" + DJIUpUploadManager.this.requestReverseTimes);
                    DJIUpUploadManager.this.tryRequestReverseLink();
                }

                /* access modifiers changed from: protected */
                public void onLinkModeChanging(DataOsdGetPushSdrLinkMode.LinkMode current, DataOsdGetPushSdrLinkMode.LinkMode except) {
                    DJIUpConstants.LOGD(DJIUpUploadManager.this.TAG, "product=" + product + " reverse link mode changing current=" + current + " except=" + except);
                }

                /* access modifiers changed from: protected */
                public void onLinkModeSwitchTimeout(int times) {
                    DJIUpConstants.LOGD(DJIUpUploadManager.this.TAG, "product=" + product + " reverse link mode remain times" + times);
                }

                /* access modifiers changed from: protected */
                public void onLinkModeSwitchException() {
                    DJIUpConstants.LOGD(DJIUpUploadManager.this.TAG, "product=" + product + " reverse link mode exception");
                }
            });
            return;
        }
        DJIUpConstants.LOGD(this.TAG, "product=" + product + " no need reverse link mode");
        startSendFirmData();
    }

    /* access modifiers changed from: private */
    public void tryResumeLinkMode(final LinkModeCallBack callBack) {
        resumeSw();
        final DJIUpDeviceType product = this.status.getProductId();
        if (DJIUpDeviceType.isNeedRequestReverse(product)) {
            DJIUpConstants.LOGD(this.TAG, "product=" + product + " normal link mode");
            DJILinkModeController.getInstance().requestResume(new DJILinkModeController.LinkModeSwitchCallBack() {
                /* class dji.dbox.upgrade.p4.statemachine.DJIUpUploadManager.AnonymousClass6 */

                public void onSuccess(Object model) {
                    DJIUpConstants.LOGD(DJIUpUploadManager.this.TAG, "product=" + product + " normal link mode success");
                    if (callBack != null) {
                        callBack.onSuccess(model);
                    }
                }

                public void onFailure(Ccode ccode) {
                    DJIUpConstants.LOGD(DJIUpUploadManager.this.TAG, "product=" + product + " normal link mode failed ccode=" + ccode);
                    if (callBack != null) {
                        callBack.onFailed(ccode);
                    }
                }

                /* access modifiers changed from: protected */
                public void onLinkModeChanging(DataOsdGetPushSdrLinkMode.LinkMode current, DataOsdGetPushSdrLinkMode.LinkMode except) {
                    DJIUpConstants.LOGD(DJIUpUploadManager.this.TAG, "product=" + product + " normal link mode changing current=" + current + " except=" + except);
                }

                /* access modifiers changed from: protected */
                public void onLinkModeSwitchTimeout(int times) {
                    DJIUpConstants.LOGD(DJIUpUploadManager.this.TAG, "product=" + product + " normal link mode timeout remain times=" + times);
                }

                /* access modifiers changed from: protected */
                public void onLinkModeSwitchException() {
                    DJIUpConstants.LOGD(DJIUpUploadManager.this.TAG, "product=" + product + " normal link mode exception");
                }
            });
            return;
        }
        DJIUpConstants.LOGD(this.TAG, "product=" + product + " no need normal link mode");
        if (callBack != null) {
            callBack.onNoNeed();
        }
    }

    /* access modifiers changed from: private */
    public void startSendFirmData() {
        this.stateMachine.tryCancelProgressTimer();
        this.comTimes = 0;
        if (this.fileMethod.isSupportMultiFile) {
            sendMultiWithV1();
        } else {
            sendWithV1Next();
        }
    }

    private void sendMultiWithV1() {
        this.multiFileStrategy.startUpload();
    }

    private void sendWithV1Next() {
        this.packUnitLength = this.requestReceiveData.getReceiveDataLength();
        DJIUpConstants.LOGE(this.TAG, "packUnitLength = " + this.packUnitLength);
        this.startTime = System.currentTimeMillis();
        uploadNextPack();
    }

    public void onConnect() {
        DJIUpConstants.LOGE(this.TAG, "sw after reverse isConnected = " + UDT.SwUdpIsConnected());
        startSendFirmData();
    }

    public void onDisconnect() {
    }

    private void resumeSw() {
        if (this.status.getProductId().equals(DJIUpDeviceType.rc002) && SwUdpService.getInstance().isRcConnect()) {
            SwUdpService.getInstance().removeConnectListener();
            DJIUpConstants.LOGE(this.TAG, "sw resume start");
            DJIUpConstants.LOGE(this.TAG, "sw resume result = " + SwUdpService.getInstance().dnReverseSw());
        }
    }

    /* access modifiers changed from: private */
    public boolean canLog() {
        if (this.packPoint <= 1) {
            return this.packPoint == 1 && this.packIndex % 100 == 0;
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public void uploadNextPack() {
        if (!this.tranMethod.isSupportFTP && this.multiFileStrategy == null) {
            try {
                long start = (long) (this.packIndex * this.packUnitLength);
                this.accessFile.seek(start);
                int size = this.accessFile.read(this.dataBuffer, 0, this.packUnitLength);
                if (size < 0) {
                    tryResumeLinkMode(null);
                    this.stateMachine.uploadFailTraning();
                    return;
                }
                this.translateData.setData(this.dataBuffer, size);
                this.translateData.setSequence(this.packIndex);
                this.packIndex++;
                this.packTimes++;
                this.fileOffset = ((long) size) + start;
                if (isUploadTotalFile() || this.packPoint == 1 || (this.packIndex > 0 && this.packIndex % this.packPoint == 1)) {
                    if (canLog()) {
                        log("uploadNextPack startDownload packIndex=" + this.packIndex);
                    }
                    this.translateData.start(new DJIDataCallBack() {
                        /* class dji.dbox.upgrade.p4.statemachine.DJIUpUploadManager.AnonymousClass7 */

                        public void onSuccess(Object model) {
                            if (DJIUpUploadManager.this.canLog()) {
                                DJIUpUploadManager.this.log("uploadonSuccess packIndex=" + DJIUpUploadManager.this.packIndex);
                            }
                            if (DJIUpUploadManager.this.isUploadTotalFile()) {
                                float totalPackNum = (float) ((int) (DJIUpUploadManager.this.fileTotalSize / ((long) DJIUpUploadManager.this.packUnitLength)));
                                int seconds = (int) ((System.currentTimeMillis() - DJIUpUploadManager.this.startTime) / 1000);
                                DJIUpUploadManager.this.log("upload tar file Complete time:" + seconds + "s speed:" + ((int) (DJIUpUploadManager.this.fileTotalSize / ((long) (seconds * 1024)))) + "kb/s lossRate=" + ((totalPackNum == 0.0f || DJIUpUploadManager.this.packNonSequenceNum == 0) ? 0 : (int) ((((float) DJIUpUploadManager.this.packNonSequenceNum) / totalPackNum) * 100.0f)) + "%");
                                DJIUpUploadManager.this.tryResumeLinkMode(null);
                                DJIUpUploadManager.this.translateComplete();
                                return;
                            }
                            DJIUpUploadManager.this.stateMachine.uploadNextPack(true);
                        }

                        public void onFailure(Ccode ccode) {
                            switch (ccode) {
                                case FM_NONSEQUENCE:
                                    DJIUpUploadManager.this.log("packIndex=" + (DJIUpUploadManager.this.packIndex + 1));
                                    DJIUpUploadManager.access$2308(DJIUpUploadManager.this);
                                    int unused = DJIUpUploadManager.this.packIndex = DJIUpUploadManager.this.translateData.getSequence() + 1;
                                    DJIUpUploadManager.this.stateMachine.uploadNextPack(true);
                                    break;
                                case TIMEOUT:
                                    int unused2 = DJIUpUploadManager.this.packIndex = DJIUpUploadManager.this.packIndex - 1;
                                    DJIUpUploadManager.this.stateMachine.uploadNextPack(true);
                                    break;
                                default:
                                    DJIUpUploadManager.this.tryResumeLinkMode(null);
                                    DJIUpUploadManager.this.stateMachine.uploadFailTraning();
                                    break;
                            }
                            DJIUpUploadManager.this.log("uploadFailTraning onFailure " + ccode + " packIndex=" + (DJIUpUploadManager.this.packIndex + 1) + " " + ccode + " " + ccode.relValue());
                        }
                    });
                    return;
                }
                if (this.packSleep > 0) {
                    try {
                        Thread.sleep((long) this.packSleep);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                this.translateData.start();
                if (this.packTimes % 100 == 0) {
                    Log.e(this.TAG, "packIndex=" + this.packIndex + " fileOffset=" + this.fileOffset + " fileTotalSize=" + this.fileTotalSize);
                    this.stateMachine.uploadNextPack(true);
                    return;
                }
                uploadNextPack();
            } catch (IOException e2) {
                e2.printStackTrace();
                log("uploadNextPack IOException=" + e2.getMessage());
            }
        }
    }

    /* access modifiers changed from: private */
    public boolean isUploadTotalFile() {
        return this.fileOffset >= this.fileTotalSize;
    }

    /* access modifiers changed from: package-private */
    public void sendWithFtp() {
        String ip = this.requestReceiveData.getFtpIP();
        int port = this.requestReceiveData.getFtpPort();
        String dir = this.requestReceiveData.getDir();
        DJIUpConstants.LOGE(this.TAG, "upload sendWithFtp ip=" + ip + " port=" + port);
        DJIUpConstants.LOGE(this.TAG, "upload sendWithFtp dir=" + dir);
        String[] ss = dir.split(IMemberProtocol.PARAM_SEPERATOR);
        String toFileName = ss[ss.length - 1];
        String toPath = dir.replace(toFileName, "");
        File file = new File(this.filePath);
        File localBin = new File(file.getParent() + IMemberProtocol.PARAM_SEPERATOR + toFileName);
        file.renameTo(localBin);
        final FTPClient client = new FTPClient();
        client.setSocketProvider(DJIMultiNetworkMgr.getInstance());
        try {
            client.connect(ip, port);
            client.login("anonymous", "");
            if ("".equals(toPath)) {
                toPath = IMemberProtocol.PARAM_SEPERATOR;
            }
            client.changeDirectory(toPath);
            client.getConnector().setConnectionTimeout(BaseImageDownloader.DEFAULT_HTTP_READ_TIMEOUT);
            client.upload(localBin, new FTPDataTransferListener() {
                /* class dji.dbox.upgrade.p4.statemachine.DJIUpUploadManager.AnonymousClass8 */

                public void started() {
                    DJIUpConstants.LOGE(DJIUpUploadManager.this.TAG, "upload sendWithFtp started");
                }

                public void transferred(int i) {
                    long unused = DJIUpUploadManager.this.fileOffset = DJIUpUploadManager.this.fileOffset + ((long) i);
                    if (DJIUpUploadManager.this.fileOffset % 1048576 == 0 || DJIUpUploadManager.this.fileOffset >= DJIUpUploadManager.this.fileTotalSize) {
                        DJIUpUploadManager.this.stateMachine.uploadNextPack(false);
                    }
                }

                public void completed() {
                    DJIUpConstants.LOGE(DJIUpUploadManager.this.TAG, "upload sendWithFtp completed");
                    DJIUpUploadManager.this.reCoverTarFile();
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    DJIUpUploadManager.this.translateComplete();
                    try {
                        if (client.isConnected()) {
                            client.disconnect(true);
                        }
                    } catch (FTPException | FTPIllegalReplyException | IOException e2) {
                        e2.printStackTrace();
                    }
                }

                public void aborted() {
                    DJIUpConstants.LOGE(DJIUpUploadManager.this.TAG, "upload sendWithFtp aborted");
                }

                public void failed() {
                    DJIUpConstants.LOGE(DJIUpUploadManager.this.TAG, "upload sendWithFtp failed");
                }
            });
        } catch (FTPAbortedException | FTPDataTransferException | FTPException | FTPIllegalReplyException | IOException e) {
            DJIUpConstants.LOGE(this.TAG, "upload sendWithFtp Exception " + e.getMessage() + " " + e);
            retryFtpConnect(client);
        }
    }

    /* access modifiers changed from: private */
    public void reCoverTarFile() {
        File fileTo = new File(this.filePath);
        File fileFrom = new File(fileTo.getParent() + "/dji_system.bin");
        if (fileFrom.exists()) {
            fileFrom.renameTo(fileTo);
        }
    }

    private void uploadFailTraning() {
        reCoverTarFile();
        this.stateMachine.uploadFailTraning();
    }

    private void retryFtpConnect(FTPClient client) {
        this.retryFtpTimes++;
        DJIUpConstants.LOGE(this.TAG, "upload retryFtpConnect " + this.retryFtpTimes);
        try {
            if (client.isConnected()) {
                client.disconnect(true);
            }
        } catch (FTPException | FTPIllegalReplyException | IOException e) {
            e.printStackTrace();
        }
        if (this.retryFtpTimes > 20) {
            uploadFailTraning();
            return;
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e2) {
            e2.printStackTrace();
        }
        sendWithFtp();
    }

    /* access modifiers changed from: private */
    public void translateComplete() {
        reset();
        if (DJIUpEnvironmentChecker.checkMotorUp()) {
            this.stateMachine.uploadFailQuit(Ccode.USER_CANCEL);
            pushStatus(DataCameraGetPushUpgradeStatus.UpgradeStep.End);
            return;
        }
        this.translateComplete.setMD5(this.md5).setTimeOut(10000).start(new DJIDataCallBack() {
            /* class dji.dbox.upgrade.p4.statemachine.DJIUpUploadManager.AnonymousClass9 */

            public void onSuccess(Object model) {
                DJIUpUploadManager.this.stateMachine.uploadComplete();
            }

            public void onFailure(Ccode ccode) {
                DJIUpUploadManager.this.log("uploadFailQuit onFailure " + ccode);
                if (ccode == Ccode.UPDATE_WAIT_FINISH) {
                    DJIUpUploadManager.this.stateMachine.uploadComplete();
                    return;
                }
                DJIUpUploadManager.access$2708(DJIUpUploadManager.this);
                if (DJIUpUploadManager.this.comTimes <= 2) {
                    DJIUpUploadManager.this.translateComplete();
                    return;
                }
                DJIUpUploadManager.this.stateMachine.uploadFailQuit(ccode);
                DJIUpUploadManager.this.pushStatus(DataCameraGetPushUpgradeStatus.UpgradeStep.End);
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onCreate() {
    }

    public void onDestroy() {
        this.multiFileStrategy = null;
    }

    public int getProgress() {
        if (this.multiFileStrategy != null) {
            return this.multiFileStrategy.getTotalProgress();
        }
        return (int) ((((float) this.fileOffset) * 100.0f) / ((float) this.fileTotalSize));
    }
}
