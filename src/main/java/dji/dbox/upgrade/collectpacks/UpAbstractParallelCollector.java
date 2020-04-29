package dji.dbox.upgrade.collectpacks;

import android.content.Context;
import android.text.TextUtils;
import com.dji.frame.util.V_JsonUtil;
import com.google.gson.reflect.TypeToken;
import dji.dbox.upgrade.p4.constants.DJIUpConstants;
import dji.dbox.upgrade.p4.constants.DJIUpDeviceType;
import dji.dbox.upgrade.p4.model.DJIUpCfgModel;
import dji.dbox.upgrade.p4.model.DJIUpListElement;
import dji.dbox.upgrade.p4.model.DJIUpStatus;
import dji.dbox.upgrade.p4.model.DJIUpUrlModel;
import dji.dbox.upgrade.p4.model.paser.DJIUpXmlPaser;
import dji.dbox.upgrade.p4.server.AsyncAjaxCallback;
import dji.dbox.upgrade.p4.server.DJIUpGetServerCfgManager;
import dji.dbox.upgrade.p4.utils.DJIUpStatusHelper;
import dji.dbox.upgrade.p4.utils.DJIUpgradeBaseUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.broadcastReceivers.DJINetWorkReceiver;
import dji.midware.natives.UpgradeVerify;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@EXClassNullAway
abstract class UpAbstractParallelCollector extends UpBaseCollector {
    protected String TAG = getClass().getSimpleName();
    private boolean cfgSetted;
    private final Context context;
    private Timer daemonTimer;
    protected ArrayList<DJIUpCfgModel.DJIFirmwareGroup> groupList = new ArrayList<>();
    private boolean isAlive = true;
    /* access modifiers changed from: private */
    public List<DJIUpListElement> list;
    /* access modifiers changed from: private */
    public boolean serverCfgGetted = false;
    /* access modifiers changed from: private */
    public int serverCfgIndex = 0;
    /* access modifiers changed from: private */
    public boolean serverCfgSetted = false;

    UpAbstractParallelCollector(Context context2, DJIUpDeviceType productId) {
        super(productId);
        this.context = context2;
    }

    /* access modifiers changed from: protected */
    public void setCfgModel(DJIUpCfgModel model) {
        this.status.setCfgModel(model);
        this.cfgSetted = model != null;
    }

    public ArrayList<DJIUpCfgModel.DJIFirmwareGroup> getGroupList() {
        return this.groupList;
    }

    public void startCollect() {
        resetStatus();
        startDeamonTimer();
    }

    /* access modifiers changed from: protected */
    public void resetStatus() {
        this.status.setCfgModel(null);
        this.serverCfgSetted = false;
        this.serverCfgGetted = false;
        this.serverCfgIndex = 0;
        this.list = null;
        this.isAlive = true;
    }

    /* access modifiers changed from: protected */
    public boolean isAllSetted() {
        return this.cfgSetted && this.serverCfgSetted;
    }

    private void startDeamonTimer() {
        DJIUpConstants.LOGD(this.TAG, "***************************************" + DJIUpStatusHelper.getCurEvent() + "******************************************");
        startGetDeviceCfg();
        getServerCFG();
        DJIUpConstants.LOGD(this.TAG, "***************************************" + DJIUpStatusHelper.getCurEvent() + "******************************************");
        if (this.daemonTimer == null) {
            this.daemonTimer = new Timer("UpAbstractParallelCollector");
            this.daemonTimer.schedule(new TimerTask() {
                /* class dji.dbox.upgrade.collectpacks.UpAbstractParallelCollector.AnonymousClass1 */

                public void run() {
                    if (UpAbstractParallelCollector.this.isAllSetted()) {
                        DJIUpConstants.LOGD(UpAbstractParallelCollector.this.TAG, "daemonTimer serverCfgGetted=" + UpAbstractParallelCollector.this.serverCfgGetted);
                        DJIUpConstants.LOGD(UpAbstractParallelCollector.this.TAG, "isAllSetted()=" + UpAbstractParallelCollector.this.isAllSetted());
                        if (UpAbstractParallelCollector.this.list != null) {
                            Collections.sort(UpAbstractParallelCollector.this.list, new Comparator<DJIUpListElement>() {
                                /* class dji.dbox.upgrade.collectpacks.UpAbstractParallelCollector.AnonymousClass1.C00021 */

                                public int compare(DJIUpListElement lhs, DJIUpListElement rhs) {
                                    return DJIUpgradeBaseUtils.compareFirVer(rhs.product_version, lhs.product_version);
                                }
                            });
                            UpAbstractParallelCollector.this.status.setVerList(UpAbstractParallelCollector.this.list);
                        }
                        UpAbstractParallelCollector.this.getStatus(UpAbstractParallelCollector.this.status);
                        UpAbstractParallelCollector.this.initFirmwareGroup();
                    }
                }
            }, 0, 1000);
        }
    }

    /* access modifiers changed from: private */
    public void getStatus(DJIUpStatus status) {
        boolean z;
        boolean z2 = true;
        DJIUpCfgModel cfgModel = status.getCfgModel();
        DJIUpConstants.LOGD(this.TAG, "getStatus start cfgModel=" + status.getProductId());
        if (this.serverCfgGetted && cfgModel != null) {
            DJIUpCfgModel latestCfgModel = null;
            DJIUpListElement latestElement = null;
            for (DJIUpListElement element : this.list) {
                if (element.cfgModel != null) {
                    element.isAllow = element.cfgModel.isCanAntirollback(cfgModel.antirollback, cfgModel.antirollbackExt);
                    element.isUpEnforce = element.cfgModel.isEnforce(cfgModel.enforce, cfgModel.enforceExt);
                    if (element.isAllow || element.isUpEnforce) {
                        if (latestCfgModel == null) {
                            latestElement = element;
                            latestCfgModel = element.cfgModel;
                        } else if (DJIUpgradeBaseUtils.compareFirVer(element.cfgModel.releaseVersion, latestCfgModel.releaseVersion) == 1) {
                            latestElement = element;
                            latestCfgModel = element.cfgModel;
                        }
                    }
                }
            }
            status.setLatestElement(latestElement);
            if (latestElement == null || latestCfgModel == null) {
                DJIUpConstants.LOGD(this.TAG, "daemonTimer latestCfgModel=null cfgModel=" + cfgModel);
                cancelTimer();
                return;
            }
            if (cfgModel.isNull()) {
                status.setNeedUpgrade(true);
                status.setNeedLock(false);
            } else {
                int result = DJIUpgradeBaseUtils.compareFirVer(latestCfgModel.releaseVersion, cfgModel.releaseVersion);
                if (result == 1) {
                    z = true;
                } else {
                    z = false;
                }
                status.setNeedUpgrade(z);
                boolean isNeedLock = latestElement.isUpEnforce;
                if (isNeedLock) {
                    long enforceTime = latestCfgModel.getEnforceTimestamp();
                    DJIUpConstants.LOGD(this.TAG, "daemonTimer time=" + enforceTime + " time=" + System.currentTimeMillis());
                    if (enforceTime != 0) {
                        isNeedLock = enforceTime <= System.currentTimeMillis();
                    }
                }
                if (result != 1 || !isNeedLock) {
                    z2 = false;
                }
                status.setNeedLock(z2);
            }
            DJIUpConstants.LOGD(this.TAG, status.getProductId() + " latestCfgModel=" + latestCfgModel.releaseVersion + " cfgModel=" + cfgModel.releaseVersion);
            DJIUpConstants.LOGD(this.TAG, status.getProductId() + " isNeedUpgrade " + status.isNeedUpgrade() + " isNeedLock " + status.isNeedLock());
        }
        cancelTimer();
        DJIUpConstants.LOGD(this.TAG, "getStatus end cfgModel=" + status.getProductId());
    }

    private void cancelTimer() {
        this.collectorListener.onStrategyCollectListOver();
        if (this.daemonTimer != null) {
            this.daemonTimer.cancel();
            this.daemonTimer = null;
        }
    }

    /* access modifiers changed from: protected */
    public void startGetDeviceCfg() {
    }

    private void getServerCFG() {
        boolean z = true;
        if (this.list != null) {
            this.list.clear();
        }
        if (DJINetWorkReceiver.getNetWorkStatusNoPing(this.context)) {
            DJIUpConstants.LOGD(this.TAG, "getServerCFG network is ok");
            if (this.serverManager.isUrlGetted()) {
                getServerList();
            } else {
                getUrlList();
            }
        } else {
            DJIUpConstants.LOGD(this.TAG, "getServerCFG  start to parseLocalList");
            this.list = DJIUpGetServerCfgManager.parseLocalList(this.context, this.serverManager.getCfgTarget(), this.productId.getProductId());
            this.serverCfgSetted = true;
            if (this.list == null) {
                z = false;
            }
            this.serverCfgGetted = z;
        }
    }

    private void getUrlList() {
        if (this.isAlive) {
            if (!DJINetWorkReceiver.getNetWorkStatusNoPing(this.context)) {
                this.serverCfgSetted = true;
                DJIUpConstants.LOGD(this.TAG, "getUrlList -- onFailure no network");
                return;
            }
            try {
                this.serverManager.getUrlList(new AsyncAjaxCallback<String>() {
                    /* class dji.dbox.upgrade.collectpacks.UpAbstractParallelCollector.AnonymousClass2 */

                    public void asyncOnStart(boolean b) {
                    }

                    public void asyncOnLoading(long count, long current) {
                    }

                    public void asyncOnSuccess(String t) {
                        DJIUpUrlModel urlModel = (DJIUpUrlModel) V_JsonUtil.toBean(t, DJIUpUrlModel.class);
                        if (urlModel == null || urlModel.apis == null || urlModel.apis.allfile == null) {
                            boolean unused = UpAbstractParallelCollector.this.serverCfgSetted = true;
                            DJIUpConstants.LOGD(UpAbstractParallelCollector.this.TAG, "getUrlList -- urlModel null");
                            return;
                        }
                        DJIUpConstants.LOGD(UpAbstractParallelCollector.this.TAG, "getUrlList -- onSuccess productId=" + UpAbstractParallelCollector.this.productId);
                        UpAbstractParallelCollector.this.serverManager.setUrlModel(urlModel);
                        UpAbstractParallelCollector.this.getServerList();
                    }

                    public void asyncOnFailure(Throwable t, int errorNo, String strMsg) {
                        boolean unused = UpAbstractParallelCollector.this.serverCfgSetted = true;
                        DJIUpConstants.LOGD(UpAbstractParallelCollector.this.TAG, "getUrlList -- onFailure " + strMsg);
                    }
                });
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                DJIUpConstants.LOGD(this.TAG, "getUrlList --" + e.getMessage());
                this.serverCfgSetted = true;
            }
        }
    }

    /* access modifiers changed from: private */
    public void getServerList() {
        if (this.isAlive) {
            try {
                this.serverManager.getList(new AsyncAjaxCallback<String>() {
                    /* class dji.dbox.upgrade.collectpacks.UpAbstractParallelCollector.AnonymousClass3 */

                    public void asyncOnStart(boolean b) {
                    }

                    public void asyncOnLoading(long count, long current) {
                    }

                    public void asyncOnSuccess(String t) {
                        try {
                            List unused = UpAbstractParallelCollector.this.list = V_JsonUtil.getList(t, new TypeToken<List<DJIUpListElement>>() {
                                /* class dji.dbox.upgrade.collectpacks.UpAbstractParallelCollector.AnonymousClass3.AnonymousClass1 */
                            });
                            DJIUpConstants.LOGD(UpAbstractParallelCollector.this.TAG, "getServerList -- onSuccess productId=" + UpAbstractParallelCollector.this.productId + " size=" + UpAbstractParallelCollector.this.list.size());
                            if (UpAbstractParallelCollector.this.list.size() > 0) {
                                UpAbstractParallelCollector.this.filterElements();
                                int unused2 = UpAbstractParallelCollector.this.serverCfgIndex = 0;
                                UpAbstractParallelCollector.this.nextCfg();
                            } else {
                                boolean unused3 = UpAbstractParallelCollector.this.serverCfgSetted = true;
                            }
                            if (UpAbstractParallelCollector.this.serverManager != null) {
                                UpAbstractParallelCollector.this.serverManager.saveServerList(t);
                            }
                        } catch (Exception e) {
                            DJIUpConstants.LOGD(UpAbstractParallelCollector.this.TAG, "getServerList-Exception -- " + t);
                            boolean unused4 = UpAbstractParallelCollector.this.serverCfgSetted = true;
                        }
                    }

                    public void asyncOnFailure(Throwable t, int errorNo, String strMsg) {
                        boolean unused = UpAbstractParallelCollector.this.serverCfgSetted = true;
                        DJIUpConstants.LOGD(UpAbstractParallelCollector.this.TAG, "getServerCFG -- onFailure " + strMsg);
                    }
                });
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                DJIUpConstants.LOGD(this.TAG, "getServerCFG --" + e.getMessage());
                this.serverCfgSetted = true;
            }
        }
    }

    /* access modifiers changed from: private */
    public void filterElements() {
        int size = this.list.size();
        int i = 0;
        while (i < size) {
            DJIUpListElement element = this.list.get(i);
            if (element.isDeprecated()) {
                this.list.remove(element);
                size--;
                i--;
            }
            i++;
        }
    }

    /* access modifiers changed from: private */
    public void nextCfg() {
        if (this.isAlive) {
            if (this.list == null) {
                this.serverCfgGetted = false;
                this.serverCfgSetted = false;
            } else if (this.serverCfgIndex < this.list.size()) {
                getCfg(this.list.get(this.serverCfgIndex));
                this.serverCfgIndex++;
            } else {
                this.serverCfgGetted = true;
                this.serverCfgSetted = true;
            }
        }
    }

    private void getCfg(final DJIUpListElement djiUpListElement) {
        try {
            this.serverManager.getCfg(new AsyncAjaxCallback<File>() {
                /* class dji.dbox.upgrade.collectpacks.UpAbstractParallelCollector.AnonymousClass4 */

                public void asyncOnStart(boolean b) {
                }

                public void asyncOnLoading(long count, long current) {
                }

                public void asyncOnSuccess(File t) {
                    boolean isOK;
                    String path = t.getAbsolutePath();
                    String cfgSuffix = UpAbstractParallelCollector.this.productId.getCfgSuffix();
                    if (TextUtils.isEmpty(path) || TextUtils.isEmpty(cfgSuffix) || !path.contains(cfgSuffix)) {
                        boolean unused = UpAbstractParallelCollector.this.serverCfgSetted = true;
                        return;
                    }
                    String outPath = path.replace(cfgSuffix, DJIUpDeviceType.MODE_PARSER_SUFFIX);
                    if (UpAbstractParallelCollector.this.productId.isTlvMode()) {
                        isOK = UpgradeVerify.native_parserTlv(path, outPath);
                    } else {
                        isOK = UpgradeVerify.native_verifyCfg(path, outPath, UpAbstractParallelCollector.this.status.isNeedVerify());
                    }
                    if (isOK) {
                        try {
                            File file = new File(outPath);
                            djiUpListElement.cfgModel = DJIUpXmlPaser.getCfgModel(file);
                            if (!file.delete()) {
                                DJIUpConstants.LOGD(UpAbstractParallelCollector.this.TAG, "getCfg from server onSuccess " + djiUpListElement.product_version + " delete failed!!");
                            }
                            UpAbstractParallelCollector.this.nextCfg();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    } else {
                        DJIUpConstants.LOGD(UpAbstractParallelCollector.this.TAG, "getCfg from server verify failure->" + cfgSuffix);
                        boolean unused2 = UpAbstractParallelCollector.this.serverCfgSetted = true;
                    }
                }

                public void asyncOnFailure(Throwable t, int errorNo, String strMsg) {
                    DJIUpConstants.LOGD(UpAbstractParallelCollector.this.TAG, "getCfg from server --onFailure " + strMsg);
                    UpAbstractParallelCollector.this.nextCfg();
                }
            }, djiUpListElement.product_version);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            DJIUpConstants.LOGD(this.TAG, "getCfg from server --onFailure " + e.getMessage());
            nextCfg();
        }
    }

    public void stop(boolean needQuit) {
        this.isAlive = false;
        if (this.daemonTimer != null) {
            this.daemonTimer.cancel();
            this.daemonTimer = null;
        }
    }
}
