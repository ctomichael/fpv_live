package dji.dbox.upgrade.collectpacks;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
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
import dji.dbox.upgrade.p4.utils.DJIUpGlassUtil;
import dji.dbox.upgrade.p4.utils.DJIUpStatusHelper;
import dji.dbox.upgrade.p4.utils.DJIUpgradeBaseUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.broadcastReceivers.DJINetWorkReceiver;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.data.model.P3.DataCommonGetVersion;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.natives.UpgradeVerify;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@EXClassNullAway
abstract class UpAbstractCollector extends UpBaseCollector {
    protected String TAG = getClass().getSimpleName();
    private boolean cfgSetted = false;
    private final Context context;
    private Timer daemonTimer;
    private boolean deviceVerSetted = false;
    private int getVerSucSize = 0;
    /* access modifiers changed from: private */
    public ArrayList<DataCommonGetVersion> getVersions = new ArrayList<>();
    protected String groupKey = "";
    protected ArrayList<DJIUpCfgModel.DJIFirmwareGroup> groupList = new ArrayList<>();
    private Handler handler;
    private HandlerThread handlerThread;
    private boolean isAlive = true;
    /* access modifiers changed from: private */
    public List<DJIUpListElement> list;
    /* access modifiers changed from: private */
    public boolean serverCfgGetted = false;
    /* access modifiers changed from: private */
    public int serverCfgIndex = 0;
    /* access modifiers changed from: private */
    public boolean serverCfgSetted = false;
    /* access modifiers changed from: private */
    public int verIndex = 0;

    static /* synthetic */ int access$008(UpAbstractCollector x0) {
        int i = x0.verIndex;
        x0.verIndex = i + 1;
        return i;
    }

    static /* synthetic */ int access$508(UpAbstractCollector x0) {
        int i = x0.getVerSucSize;
        x0.getVerSucSize = i + 1;
        return i;
    }

    public UpAbstractCollector(Context context2, DJIUpDeviceType productId) {
        super(productId);
        this.context = context2;
        this.handlerThread = new HandlerThread("UpAbstractCollector");
        this.handlerThread.start();
    }

    /* access modifiers changed from: protected */
    public void setCfgModel(DJIUpCfgModel model) {
        this.status.setCfgModel(model);
        this.cfgSetted = true;
    }

    public void initFirmwareGroup() {
        if (DJIUpStatusHelper.isConnectRC()) {
            if (ServiceManager.getInstance().isRemoteOK()) {
                this.groupList.add(DJIUpCfgModel.DJIFirmwareGroup.RC);
                this.groupList.add(DJIUpCfgModel.DJIFirmwareGroup.AC);
                if (DJIUpGlassUtil.isGlassConnected()) {
                    this.groupList.add(DJIUpCfgModel.DJIFirmwareGroup.GL);
                }
                this.groupKey = "ALL";
            } else {
                this.groupList.add(DJIUpCfgModel.DJIFirmwareGroup.RC);
                this.groupKey = "RC";
            }
        } else if (DJIUpStatusHelper.isConnectMC()) {
            this.groupList.add(DJIUpCfgModel.DJIFirmwareGroup.AC);
            this.groupKey = "";
        }
        DJIUpConstants.LOGD("", "initFirmwareGroup groupKey=" + this.groupKey);
    }

    public ArrayList<DJIUpCfgModel.DJIFirmwareGroup> getGroupList() {
        return this.groupList;
    }

    private void initModules() {
        if (this.getVersions.size() <= 0) {
            Iterator<DJIUpCfgModel.DJIUpModule> it2 = this.status.getCfgModel().modules.iterator();
            while (it2.hasNext()) {
                DJIUpCfgModel.DJIUpModule upModule = it2.next();
                if (this.groupList.contains(upModule.group) && upModule.group != DJIUpCfgModel.DJIFirmwareGroup.GL) {
                    String module = upModule.id;
                    int deviceType = Integer.parseInt(module.substring(0, 2));
                    int deviceId = Integer.parseInt(module.substring(2));
                    DataCommonGetVersion getVersion = new DataCommonGetVersion();
                    getVersion.setDeviceType(DeviceType.find(deviceType));
                    getVersion.setDeviceModel(deviceId);
                    this.getVersions.add(getVersion);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onCollectVersionOver() {
    }

    public void startCollect() {
        resetStatus();
        initFirmwareGroup();
        startDeamonTimer();
    }

    /* access modifiers changed from: protected */
    public void resetStatus() {
        this.status.setCfgModel(null);
        this.cfgSetted = false;
        this.serverCfgSetted = false;
        this.deviceVerSetted = false;
        this.serverCfgGetted = false;
        this.getVerSucSize = 0;
        this.serverCfgIndex = 0;
        this.verIndex = 0;
        this.list = null;
        this.isAlive = true;
        if (this.handler == null) {
            this.handler = new Handler(this.handlerThread.getLooper(), new Handler.Callback() {
                /* class dji.dbox.upgrade.collectpacks.UpAbstractCollector.AnonymousClass1 */

                public boolean handleMessage(Message msg) {
                    switch (msg.what) {
                        case 0:
                            UpAbstractCollector.access$008(UpAbstractCollector.this);
                            if (UpAbstractCollector.this.verIndex >= UpAbstractCollector.this.getVersions.size() || !ServiceManager.getInstance().isConnected()) {
                                return false;
                            }
                            UpAbstractCollector.this.getDeviceVers();
                            return false;
                        default:
                            return false;
                    }
                }
            });
        }
    }

    /* access modifiers changed from: protected */
    public boolean isAllSetted() {
        return this.cfgSetted && this.serverCfgSetted && this.deviceVerSetted;
    }

    private void startDeamonTimer() {
        boolean isNeedGetVers = isNeedGetVers();
        DJIUpConstants.LOGD(this.TAG, "***************************************" + DJIUpStatusHelper.getCurEvent() + "******************************************");
        startGetDeviceCfg();
        getServerCFG();
        DJIUpConstants.LOGD(this.TAG, "***************************************" + DJIUpStatusHelper.getCurEvent() + "******************************************");
        if (this.daemonTimer == null) {
            this.daemonTimer = new Timer("UpAbstractCollector");
            this.daemonTimer.schedule(new TimerTask() {
                /* class dji.dbox.upgrade.collectpacks.UpAbstractCollector.AnonymousClass2 */

                public void run() {
                    boolean z;
                    boolean z2;
                    boolean z3 = false;
                    if (UpAbstractCollector.this.isAllSetted()) {
                        if (UpAbstractCollector.this.list != null) {
                            Collections.sort(UpAbstractCollector.this.list, new Comparator<DJIUpListElement>() {
                                /* class dji.dbox.upgrade.collectpacks.UpAbstractCollector.AnonymousClass2.AnonymousClass1 */

                                public int compare(DJIUpListElement lhs, DJIUpListElement rhs) {
                                    return DJIUpgradeBaseUtils.compareFirVer(rhs.product_version, lhs.product_version);
                                }
                            });
                            UpAbstractCollector.this.status.setVerList(UpAbstractCollector.this.list);
                        }
                        UpAbstractCollector.this.status.setNeedLockGetted(false);
                        DJIUpCfgModel cfgModel = UpAbstractCollector.this.status.getCfgModel();
                        String str = UpAbstractCollector.this.TAG;
                        StringBuilder append = new StringBuilder().append("daemonTimer serverCfgGetted=").append(UpAbstractCollector.this.serverCfgGetted).append(" deviceCfgGetted=");
                        if (cfgModel != null) {
                            z = true;
                        } else {
                            z = false;
                        }
                        DJIUpConstants.LOGD(str, append.append(z).toString());
                        DJIUpConstants.LOGD(UpAbstractCollector.this.TAG, "isAllSetted()=" + UpAbstractCollector.this.isAllSetted());
                        if (UpAbstractCollector.this.serverCfgGetted && cfgModel != null) {
                            DJIUpCfgModel latestCfgModel = null;
                            DJIUpListElement latestElement = null;
                            for (DJIUpListElement element : UpAbstractCollector.this.list) {
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
                            UpAbstractCollector.this.status.setLatestElement(latestElement);
                            if (latestElement == null || latestCfgModel == null) {
                                DJIUpConstants.LOGD(UpAbstractCollector.this.TAG, "daemonTimer latestCfgModel=null cfgModel=" + cfgModel);
                                UpAbstractCollector.this.cancelTimer();
                                return;
                            }
                            if (cfgModel.isNull()) {
                                UpAbstractCollector.this.status.setNeedUpgrade(true);
                                UpAbstractCollector.this.status.setNeedLock(false);
                            } else {
                                int result = DJIUpgradeBaseUtils.compareFirVer(latestCfgModel.releaseVersion, cfgModel.releaseVersion);
                                DJIUpStatus dJIUpStatus = UpAbstractCollector.this.status;
                                if (result == 1) {
                                    z2 = true;
                                } else {
                                    z2 = false;
                                }
                                dJIUpStatus.setNeedUpgrade(z2);
                                boolean isNeedLock = latestCfgModel.isEnforce(cfgModel.enforce, cfgModel.enforceExt);
                                DJIUpConstants.LOGD(UpAbstractCollector.this.TAG, "up daemonTimer isNeedLock=" + isNeedLock);
                                if (isNeedLock) {
                                    long enforceTime = latestCfgModel.getEnforceTimestamp();
                                    DJIUpConstants.LOGD(UpAbstractCollector.this.TAG, "up daemonTimer time=" + enforceTime + " time=" + System.currentTimeMillis());
                                    if (enforceTime >= 0) {
                                        isNeedLock = enforceTime <= System.currentTimeMillis();
                                    }
                                }
                                DJIUpStatus dJIUpStatus2 = UpAbstractCollector.this.status;
                                if (result == 1 && isNeedLock) {
                                    z3 = true;
                                }
                                dJIUpStatus2.setNeedLock(z3);
                                UpAbstractCollector.this.status.setNeedLockGetted(true);
                            }
                            DJIUpConstants.LOGD(UpAbstractCollector.this.TAG, UpAbstractCollector.this.status.getProductId() + " latestCfgModel=" + latestCfgModel.releaseVersion + " cfgModel=" + cfgModel.releaseVersion);
                            DJIUpConstants.LOGD(UpAbstractCollector.this.TAG, UpAbstractCollector.this.status.getProductId() + " isNeedUpgrade " + UpAbstractCollector.this.status.isNeedUpgrade() + " isNeedLock " + UpAbstractCollector.this.status.isNeedLock());
                        }
                        UpAbstractCollector.this.cancelTimer();
                        if (cfgModel == null) {
                            DJIUpConstants.LOGD(UpAbstractCollector.this.TAG, "deviceCfgGetted=null");
                        }
                    }
                }
            }, 0, 1000);
        }
    }

    /* access modifiers changed from: private */
    public void cancelTimer() {
        if (ServiceManager.getInstance().isConnected() && ((!ServiceManager.getInstance().isRemoteOK() && this.productId != DJIUpDeviceType.wm335) || DJIUpStatusHelper.isNeedShieldUpgrade())) {
            DJIUpConstants.LOGD(this.TAG, "status.getProductId() = " + this.status.getProductId() + " isRemoteOK false, set isNeedUpgrade " + false);
            this.status.setNeedUpgrade(false);
        }
        this.collectorListener.onStrategyCollectListOver();
        if (this.daemonTimer != null) {
            this.daemonTimer.cancel();
            this.daemonTimer = null;
        }
    }

    /* access modifiers changed from: protected */
    public void startGetDeviceCfg() {
    }

    private int compareVers() {
        int result = 0;
        DJIUpCfgModel cfgModel = this.status.getCfgModel();
        Iterator<DataCommonGetVersion> it2 = this.getVersions.iterator();
        while (it2.hasNext()) {
            DataCommonGetVersion getVersion = it2.next();
            if (getVersion.isGetted()) {
                String ver = getVersion.getFirmVer(".");
                int modelType = getVersion.getDeviceType().value();
                int modelId = getVersion.getModelId();
                if (cfgModel.modules.size() == 0) {
                    return 1;
                }
                Iterator<DJIUpCfgModel.DJIUpModule> it3 = cfgModel.modules.iterator();
                while (it3.hasNext()) {
                    DJIUpCfgModel.DJIUpModule module = it3.next();
                    if (module.modelType == modelType && module.modelId == modelId) {
                        int cresult = DJIUpgradeBaseUtils.compareFirVer(ver, module.version);
                        if (cresult > 0) {
                            return 1;
                        }
                        if (cresult < 0) {
                            result = -1;
                        }
                    }
                }
                continue;
            }
        }
        return result;
    }

    private boolean isNeedGetVers() {
        return false;
    }

    /* access modifiers changed from: protected */
    public void getDeviceVers() {
        if (isNeedGetVers()) {
            initModules();
            if (this.getVersions.size() <= 0) {
                this.deviceVerSetted = true;
            } else if (this.verIndex >= this.getVersions.size()) {
                this.deviceVerSetted = true;
            } else {
                final DataCommonGetVersion getVersion = this.getVersions.get(this.verIndex);
                getVersion.start(new DJIDataCallBack() {
                    /* class dji.dbox.upgrade.collectpacks.UpAbstractCollector.AnonymousClass3 */

                    public void onSuccess(Object model) {
                        DJIUpConstants.LOGD(UpAbstractCollector.this.TAG, "getVers verIndex=" + UpAbstractCollector.this.verIndex + " " + getVersion.getDeviceType() + " " + getVersion.getModelId() + " suc");
                        UpAbstractCollector.access$508(UpAbstractCollector.this);
                        UpAbstractCollector.this.next();
                    }

                    public void onFailure(Ccode ccode) {
                        DJIUpConstants.LOGD(UpAbstractCollector.this.TAG, "getVers " + getVersion.getDeviceType() + " " + getVersion.getModelId() + " " + ccode);
                        UpAbstractCollector.this.next();
                    }
                }, 500, 2);
            }
        } else {
            this.deviceVerSetted = true;
        }
    }

    /* access modifiers changed from: private */
    public void next() {
        if (this.verIndex == this.getVersions.size() - 1) {
            DJIUpConstants.LOGD(this.TAG, "getVers over getVerSucSize=" + this.getVerSucSize);
            this.deviceVerSetted = true;
            this.verIndex = 0;
        } else if (this.handler != null) {
            this.handler.sendEmptyMessageDelayed(0, 50);
        }
    }

    private void getServerCFG() {
        boolean z = true;
        if (this.list != null) {
            this.list.clear();
        }
        if (!DJINetWorkReceiver.getNetWorkStatusNoPing(this.context)) {
            this.list = DJIUpGetServerCfgManager.parseLocalList(this.context, this.serverManager.getCfgTarget(), this.productId.getProductId());
            this.serverCfgSetted = true;
            if (this.list == null) {
                z = false;
            }
            this.serverCfgGetted = z;
        } else if (this.serverManager.isUrlGetted()) {
            getServerList();
        } else {
            getUrlList();
        }
    }

    private void getUrlList() {
        if (this.isAlive) {
            if (!DJINetWorkReceiver.getNetWorkStatusNoPing(this.context)) {
                this.serverCfgSetted = true;
                DJIUpConstants.LOGD(this.TAG, "getUrlList -- onFailure getNetWorkStatusNoPing no network");
                return;
            }
            try {
                this.serverManager.getUrlList(new AsyncAjaxCallback<String>() {
                    /* class dji.dbox.upgrade.collectpacks.UpAbstractCollector.AnonymousClass4 */

                    public void asyncOnStart(boolean b) {
                    }

                    public void asyncOnLoading(long count, long current) {
                    }

                    public void asyncOnSuccess(String t) {
                        DJIUpUrlModel urlModel = (DJIUpUrlModel) V_JsonUtil.toBean(t, DJIUpUrlModel.class);
                        if (urlModel == null || urlModel.apis == null || urlModel.apis.allfile == null) {
                            boolean unused = UpAbstractCollector.this.serverCfgSetted = true;
                            DJIUpConstants.LOGD(UpAbstractCollector.this.TAG, "getUrlList -- urlModel null");
                            return;
                        }
                        DJIUpConstants.LOGD(UpAbstractCollector.this.TAG, "getUrlList -- onSuccess productId=" + UpAbstractCollector.this.productId);
                        UpAbstractCollector.this.serverManager.setUrlModel(urlModel);
                        UpAbstractCollector.this.getServerList();
                    }

                    public void asyncOnFailure(Throwable t, int errorNo, String strMsg) {
                        boolean unused = UpAbstractCollector.this.serverCfgSetted = true;
                        DJIUpConstants.LOGD(UpAbstractCollector.this.TAG, "getUrlList 2 -- onFailure " + strMsg);
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
                    /* class dji.dbox.upgrade.collectpacks.UpAbstractCollector.AnonymousClass5 */

                    public void asyncOnStart(boolean b) {
                    }

                    public void asyncOnLoading(long count, long current) {
                    }

                    public void asyncOnSuccess(String t) {
                        try {
                            List unused = UpAbstractCollector.this.list = V_JsonUtil.getList(t, new TypeToken<List<DJIUpListElement>>() {
                                /* class dji.dbox.upgrade.collectpacks.UpAbstractCollector.AnonymousClass5.AnonymousClass1 */
                            });
                            DJIUpConstants.LOGD(UpAbstractCollector.this.TAG, "getServerList -- onSuccess productId=" + UpAbstractCollector.this.productId + " size=" + UpAbstractCollector.this.list.size());
                            if (UpAbstractCollector.this.list.size() > 0) {
                                UpAbstractCollector.this.filterElements();
                                int unused2 = UpAbstractCollector.this.serverCfgIndex = 0;
                                UpAbstractCollector.this.nextCfg();
                            } else {
                                boolean unused3 = UpAbstractCollector.this.serverCfgSetted = true;
                            }
                            if (UpAbstractCollector.this.serverManager != null) {
                                UpAbstractCollector.this.serverManager.saveServerList(t);
                            }
                        } catch (Exception e) {
                            DJIUpConstants.LOGD(UpAbstractCollector.this.TAG, "getServerList-Exception -- " + t);
                            boolean unused4 = UpAbstractCollector.this.serverCfgSetted = true;
                        }
                    }

                    public void asyncOnFailure(Throwable t, int errorNo, String strMsg) {
                        boolean unused = UpAbstractCollector.this.serverCfgSetted = true;
                        DJIUpConstants.LOGD(UpAbstractCollector.this.TAG, "getServerCFG -- onFailure " + strMsg);
                    }
                });
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                DJIUpConstants.LOGD(this.TAG, "getServerCFG --" + e.getMessage());
                this.serverCfgSetted = true;
            }
        }
    }

    /* access modifiers changed from: protected */
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
                /* class dji.dbox.upgrade.collectpacks.UpAbstractCollector.AnonymousClass6 */

                public void asyncOnStart(boolean b) {
                }

                public void asyncOnLoading(long count, long current) {
                }

                public void asyncOnSuccess(File t) {
                    boolean isOK;
                    String path = t.getAbsolutePath();
                    String cfgSuffix = UpAbstractCollector.this.productId.getCfgSuffix();
                    if (TextUtils.isEmpty(path) || TextUtils.isEmpty(cfgSuffix) || !path.contains(cfgSuffix)) {
                        boolean unused = UpAbstractCollector.this.serverCfgSetted = true;
                        return;
                    }
                    String outPath = path.replace(cfgSuffix, DJIUpDeviceType.MODE_PARSER_SUFFIX);
                    if (UpAbstractCollector.this.productId.isTlvMode()) {
                        isOK = UpgradeVerify.native_parserTlv(path, outPath);
                    } else {
                        isOK = UpgradeVerify.native_verifyCfg(path, outPath, UpAbstractCollector.this.status.isNeedVerify());
                    }
                    if (isOK) {
                        try {
                            File file = new File(outPath);
                            djiUpListElement.cfgModel = DJIUpXmlPaser.getCfgModel(file);
                            if (!file.delete()) {
                                DJIUpConstants.LOGD(UpAbstractCollector.this.TAG, "getCfg from server onSuccess " + djiUpListElement.product_version + " delete failed!!");
                            }
                            UpAbstractCollector.this.nextCfg();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    } else {
                        DJIUpConstants.LOGD(UpAbstractCollector.this.TAG, "getCfg from server verify failure->" + cfgSuffix);
                        boolean unused2 = UpAbstractCollector.this.serverCfgSetted = true;
                    }
                }

                public void asyncOnFailure(Throwable t, int errorNo, String strMsg) {
                    DJIUpConstants.LOGD(UpAbstractCollector.this.TAG, "getCfg from server --onFailure " + strMsg);
                    UpAbstractCollector.this.nextCfg();
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
        if (this.handler != null) {
            this.handler.removeCallbacksAndMessages(null);
            this.handler = null;
        }
        if (needQuit) {
            this.handlerThread.quit();
        }
    }
}
