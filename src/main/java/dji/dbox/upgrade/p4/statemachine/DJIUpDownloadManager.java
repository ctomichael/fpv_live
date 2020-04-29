package dji.dbox.upgrade.p4.statemachine;

import com.dji.frame.util.MD5;
import dji.dbox.upgrade.p4.constants.DJIUpConstants;
import dji.dbox.upgrade.p4.constants.DJIUpDeviceType;
import dji.dbox.upgrade.p4.interfaces.DJIUpDownloadArrayListeners;
import dji.dbox.upgrade.p4.model.DJIUpCfgModel;
import dji.dbox.upgrade.p4.model.DJIUpListElement;
import dji.dbox.upgrade.p4.model.DJIUpStatus;
import dji.dbox.upgrade.p4.server.AsyncAjaxCallback;
import dji.dbox.upgrade.p4.server.DJIUpServerManager;
import dji.dbox.upgrade.p4.utils.DJIUpStatusHelper;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.natives.UpgradeVerify;
import dji.midware.util.BytesUtil;
import dji.thirdparty.afinal.http.HttpHandler;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@EXClassNullAway
class DJIUpDownloadManager {
    /* access modifiers changed from: private */
    public String TAG = getClass().getSimpleName();
    private final DJIUpgradeP4Manager djiUpgradeP4Manager;
    /* access modifiers changed from: private */
    public DJIUpDownloadArrayListeners downloadListeners;
    private ArrayList<String> fileNames;
    /* access modifiers changed from: private */
    public int firmTotalLength = 0;
    private ArrayList<HttpHandler<File>> handlers;
    /* access modifiers changed from: private */
    public boolean isDeleting = false;
    /* access modifiers changed from: private */
    public List<DJIUpStatus> status;
    /* access modifiers changed from: private */
    public volatile AtomicLong totalCurrent = new AtomicLong(0);
    /* access modifiers changed from: private */
    public int totalProgress = 0;
    private int totalSize = 0;
    /* access modifiers changed from: private */
    public int totalSucSize = 0;

    static /* synthetic */ int access$710(DJIUpDownloadManager x0) {
        int i = x0.totalSucSize;
        x0.totalSucSize = i - 1;
        return i;
    }

    public DJIUpDownloadManager(DJIUpgradeP4Manager djiUpgradeP4Manager2) {
        this.djiUpgradeP4Manager = djiUpgradeP4Manager2;
    }

    private void deleteFiles() {
        if (this.isDeleting) {
            new Thread(new Runnable() {
                /* class dji.dbox.upgrade.p4.statemachine.DJIUpDownloadManager.AnonymousClass1 */

                public void run() {
                    boolean unused = DJIUpDownloadManager.this.isDeleting = true;
                    if (DJIUpDownloadManager.this.status != null) {
                        for (DJIUpStatus upStatus : DJIUpDownloadManager.this.status) {
                            upStatus.getServerManager().deleteFiles(upStatus.getCfgModel());
                        }
                    }
                    boolean unused2 = DJIUpDownloadManager.this.isDeleting = false;
                }
            }, "upDownM").start();
        }
    }

    private void download() {
        DJIUpConstants.LOGD(this.TAG, "download startDownload");
        resetDownload();
        for (DJIUpStatus upStatus : this.status) {
            DJIUpCfgModel model = upStatus.getChoiceElement().cfgModel;
            if (model.modules.size() <= 0) {
                this.downloadListeners.onDownloadFail("");
                DJIUpStatusHelper.setIsUpDownloading(false);
                DJIUpConstants.LOGD(this.TAG, "DJIUpCfgModel modules size 0");
                return;
            }
            this.totalSucSize += model.modules.size();
            this.totalSize += model.modules.size();
            this.firmTotalLength += model.totalSize;
        }
        DJIUpConstants.LOGD(this.TAG, "download count=" + this.status.size() + " firmTotalLength=" + ((this.firmTotalLength / 1024) / 1024) + "MB totalSize=" + this.totalSize);
        this.handlers = new ArrayList<>(this.totalSize);
        for (DJIUpStatus upStatus2 : this.status) {
            DJIUpServerManager serverManager = upStatus2.getServerManager();
            DJIUpCfgModel cfgModel = upStatus2.getChoiceElement().cfgModel;
            final DJIUpDeviceType deviceType = upStatus2.getProductId();
            Iterator<DJIUpCfgModel.DJIUpModule> it2 = cfgModel.modules.iterator();
            while (it2.hasNext()) {
                DJIUpCfgModel.DJIUpModule module = it2.next();
                final DJIUpStatus dJIUpStatus = upStatus2;
                final DJIUpCfgModel.DJIUpModule dJIUpModule = module;
                try {
                    this.handlers.add(serverManager.downloadPkg(module.name + DJIUpConstants.UP_TEMP_FILE, new AsyncAjaxCallback<File>() {
                        /* class dji.dbox.upgrade.p4.statemachine.DJIUpDownloadManager.AnonymousClass2 */
                        private long temp = 0;

                        public void asyncOnStart(boolean b) {
                            this.temp = 0;
                        }

                        public void asyncOnLoading(long count, long current) {
                            if (DJIUpStatusHelper.isUpDownloading()) {
                                DJIUpDownloadManager.this.totalCurrent.getAndAdd(current - this.temp);
                                int progress = Math.min((int) ((((float) DJIUpDownloadManager.this.totalCurrent.get()) * 100.0f) / ((float) DJIUpDownloadManager.this.firmTotalLength)), 100);
                                if (progress > DJIUpDownloadManager.this.totalProgress) {
                                    DJIUpDownloadManager.this.downloadListeners.onDownloadProgress(progress);
                                    int unused = DJIUpDownloadManager.this.totalProgress = progress;
                                }
                                this.temp = current;
                            }
                        }

                        public void asyncOnSuccess(File t) {
                            if (DJIUpStatusHelper.isUpDownloading()) {
                                boolean isOK = true;
                                if (dJIUpStatus.isNeedVerify()) {
                                    isOK = UpgradeVerify.native_verifyFile(dJIUpModule.id, t.getAbsolutePath());
                                }
                                boolean checkMd5 = true;
                                if (DJIUpDownloadManager.this.needCheckMd5(deviceType)) {
                                    checkMd5 = BytesUtil.toHexString(MD5.getMD5(t)).equals(dJIUpModule.md5);
                                }
                                boolean isRealFile = checkMd5;
                                if (checkMd5 && t.getName().contains(DJIUpConstants.UP_TEMP_FILE)) {
                                    isRealFile = t.renameTo(new File(t.getAbsolutePath().replace(DJIUpConstants.UP_TEMP_FILE, "")));
                                }
                                if (!isOK || !isRealFile || !checkMd5) {
                                    DJIUpConstants.LOGD(DJIUpDownloadManager.this.TAG, "verify image " + dJIUpModule.id + " failed " + t.getName() + " isOK=" + isOK + " isRealFile=" + isRealFile + " checkMd5=" + checkMd5);
                                    DJIUpDownloadManager.this.downloadListeners.onDownloadFail("");
                                    t.delete();
                                    DJIUpDownloadManager.this.stopDownload();
                                    return;
                                }
                                DJIUpDownloadManager.access$710(DJIUpDownloadManager.this);
                                if (DJIUpDownloadManager.this.totalSucSize == 0) {
                                    DJIUpDownloadManager.this.downloadComplete();
                                }
                            }
                        }

                        public void asyncOnFailure(Throwable t, int errorNo, String strMsg) {
                            if (DJIUpStatusHelper.isUpDownloading()) {
                                DJIUpConstants.LOGD(DJIUpDownloadManager.this.TAG, "downloadImage onFailure 1 " + dJIUpModule.name + " " + strMsg);
                                DJIUpDownloadManager.this.stopDownload();
                                DJIUpDownloadManager.this.downloadListeners.onDownloadFail("");
                            }
                        }
                    }, cfgModel.releaseVersion, module.version, module.id, (long) module.size, module.type));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    stopDownload();
                    this.downloadListeners.onDownloadFail("");
                    return;
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public boolean needCheckMd5(DJIUpDeviceType deviceType) {
        return deviceType == DJIUpDeviceType.wm240 || deviceType == DJIUpDeviceType.rc240 || deviceType == DJIUpDeviceType.rc010;
    }

    /* access modifiers changed from: private */
    public void downloadComplete() {
        this.downloadListeners.onDownloadComplete();
        if (!ServiceManager.getInstance().isConnected() || isOffline()) {
            DJIUpStatusHelper.setIsUpDownloading(false);
        }
    }

    private boolean isOffline() {
        if (this.status != null) {
            for (DJIUpStatus upStatus : this.status) {
                if (upStatus.getProductId().isOffline()) {
                    return true;
                }
            }
        }
        return false;
    }

    /* access modifiers changed from: private */
    public void stopDownload() {
        if (this.handlers != null) {
            Iterator<HttpHandler<File>> it2 = this.handlers.iterator();
            while (it2.hasNext()) {
                HttpHandler<File> handler = it2.next();
                if (handler != null && !handler.isStop()) {
                    handler.stop();
                }
            }
        }
        DJIUpStatusHelper.setIsUpDownloading(false);
        resetDownload();
    }

    private void resetDownload() {
        this.firmTotalLength = 0;
        this.totalSize = 0;
        this.totalSucSize = 0;
        this.totalProgress = 0;
        this.totalCurrent.set(0);
        if (this.handlers != null) {
            this.handlers.clear();
        }
    }

    public void start(DJIUpDownloadArrayListeners downloadListeners2, boolean isRollback) {
        this.downloadListeners = downloadListeners2;
        this.downloadListeners.onDownloadStart();
        DJIUpStatusHelper.setIsUpDownloading(true);
        if (this.status == null) {
            this.status = new ArrayList();
        } else {
            this.status.clear();
        }
        List<DJIUpStatus> needDownStatus = new ArrayList<>();
        if (isRollback) {
            needDownStatus.add(DJIUpStatusHelper.getRollBackStatus());
        } else {
            needDownStatus.addAll(DJIUpStatusHelper.getMainPageStatusTogether());
        }
        for (DJIUpStatus upStatus : needDownStatus) {
            if (DJIUpStatusHelper.isNeedUpgrade(upStatus) || isRollback) {
                DJIUpListElement element = upStatus.getChoiceElement();
                if (element == null) {
                    DJIUpConstants.LOGD(this.TAG, upStatus.getProductId() + " element is null");
                    this.downloadListeners.onDownloadFail("");
                    DJIUpStatusHelper.setIsUpDownloading(false);
                    return;
                }
                DJIUpConstants.LOGD(this.TAG, upStatus.getProductId() + " choiceElement=" + element + " version=" + element.product_version);
                this.status.add(upStatus);
            }
        }
        boolean isFromSDCard = true;
        Iterator<DJIUpStatus> it2 = this.status.iterator();
        while (true) {
            if (it2.hasNext()) {
                if (!it2.next().getChoiceElement().isFromSDCard) {
                    isFromSDCard = false;
                    break;
                }
            } else {
                break;
            }
        }
        if (isFromSDCard) {
            DJIUpStatusHelper.setIsUpDownloading(false);
            downloadComplete();
            return;
        }
        clearDownloadedFiles();
        download();
    }

    private void clearDownloadedFiles() {
    }
}
