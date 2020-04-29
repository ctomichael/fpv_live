package dji.dbox.upgrade.collectpacks;

import android.os.HandlerThread;
import dji.dbox.upgrade.p4.constants.DJIUpConstants;
import dji.dbox.upgrade.p4.constants.DJIUpDeviceType;
import dji.dbox.upgrade.p4.model.DJIUpCfgModel;
import dji.dbox.upgrade.p4.model.DJIUpListElement;
import dji.dbox.upgrade.p4.server.DJIUpGetServerCfgManager;
import dji.dbox.upgrade.p4.utils.DJIUpStatusHelper;
import dji.dbox.upgrade.p4.utils.DJIUpStatusOfflineHelper;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.data.model.P3.DataCommonGetVersion;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@EXClassNullAway
abstract class UpAbstractWifiCollector extends UpBaseCollector {
    protected String TAG = getClass().getSimpleName();
    private boolean cfgSetted = false;
    private Timer daemonTimer;
    private int getVerSucSize = 0;
    private ArrayList<DataCommonGetVersion> getVersions = new ArrayList<>();
    private String groupKey = "";
    private ArrayList<DJIUpCfgModel.DJIFirmwareGroup> groupList = new ArrayList<>();
    private HandlerThread handlerThread = new HandlerThread("UpAbstractCollector");
    private boolean isAlive = true;
    /* access modifiers changed from: private */
    public List<DJIUpListElement> list;
    /* access modifiers changed from: private */
    public boolean serverCfgGetted = false;
    private int serverCfgIndex = 0;
    private boolean serverCfgSetted = false;
    private int verIndex = 0;

    public UpAbstractWifiCollector(DJIUpDeviceType productId) {
        super(productId);
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
                this.groupList.add(DJIUpCfgModel.DJIFirmwareGroup.GL);
                this.groupKey = "ALL";
            } else {
                this.groupList.add(DJIUpCfgModel.DJIFirmwareGroup.RC);
                this.groupList.add(DJIUpCfgModel.DJIFirmwareGroup.AC);
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
        this.serverCfgGetted = false;
        this.getVerSucSize = 0;
        this.serverCfgIndex = 0;
        this.verIndex = 0;
        this.list = null;
        this.isAlive = true;
    }

    /* access modifiers changed from: protected */
    public boolean isAllSetted() {
        return this.cfgSetted && this.serverCfgSetted;
    }

    private void startDeamonTimer() {
        DJIUpConstants.LOGD(this.TAG, "***************************************" + DJIUpStatusHelper.getCurEvent() + "******************************************");
        this.serverManager.setUrlModel(DJIUpStatusOfflineHelper.getUrlModel());
        startGetDeviceCfg();
        getLocalCFG();
        DJIUpConstants.LOGD(this.TAG, "***************************************" + DJIUpStatusHelper.getCurEvent() + "******************************************");
        if (this.daemonTimer == null) {
            this.daemonTimer = new Timer("UpAbstractWifiCollector");
            this.daemonTimer.schedule(new TimerTask() {
                /* class dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector.AnonymousClass1 */

                /* JADX WARNING: Removed duplicated region for block: B:31:0x0193  */
                /* JADX WARNING: Removed duplicated region for block: B:66:0x01a2 A[EDGE_INSN: B:66:0x01a2->B:36:0x01a2 ?: BREAK  , SYNTHETIC] */
                /* Code decompiled incorrectly, please refer to instructions dump. */
                public void run() {
                    /*
                        r14 = this;
                        r10 = 0
                        r9 = 1
                        dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector r8 = dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector.this
                        boolean r8 = r8.isAllSetted()
                        if (r8 == 0) goto L_0x0075
                        dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector r8 = dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector.this
                        java.util.List r8 = r8.list
                        if (r8 == 0) goto L_0x0039
                        dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector r8 = dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector.this
                        java.util.List r8 = r8.list
                        int r8 = r8.size()
                        if (r8 <= 0) goto L_0x0039
                        dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector r8 = dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector.this
                        java.util.List r8 = r8.list
                        dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector$1$1 r11 = new dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector$1$1
                        r11.<init>()
                        java.util.Collections.sort(r8, r11)
                        dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector r8 = dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector.this
                        dji.dbox.upgrade.p4.model.DJIUpStatus r8 = r8.status
                        dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector r11 = dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector.this
                        java.util.List r11 = r11.list
                        r8.setVerList(r11)
                    L_0x0039:
                        dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector r8 = dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector.this
                        dji.dbox.upgrade.p4.model.DJIUpStatus r8 = r8.status
                        r8.setNeedLockGetted(r10)
                        dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector r8 = dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector.this
                        dji.dbox.upgrade.p4.model.DJIUpStatus r8 = r8.status
                        dji.dbox.upgrade.p4.model.DJIUpCfgModel r0 = r8.getCfgModel()
                        if (r0 != 0) goto L_0x0076
                        dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector r8 = dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector.this
                        java.lang.String r8 = r8.TAG
                        java.lang.StringBuilder r9 = new java.lang.StringBuilder
                        r9.<init>()
                        java.lang.String r10 = "daemonTimer "
                        java.lang.StringBuilder r9 = r9.append(r10)
                        dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector r10 = dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector.this
                        dji.dbox.upgrade.p4.constants.DJIUpDeviceType r10 = r10.productId
                        java.lang.StringBuilder r9 = r9.append(r10)
                        java.lang.String r10 = " get device cfg failed!"
                        java.lang.StringBuilder r9 = r9.append(r10)
                        java.lang.String r9 = r9.toString()
                        dji.dbox.upgrade.p4.constants.DJIUpConstants.LOGD(r8, r9)
                        dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector r8 = dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector.this
                        r8.cancelTimer()
                    L_0x0075:
                        return
                    L_0x0076:
                        boolean r8 = r0.isTimeoutCase
                        if (r8 == 0) goto L_0x00a8
                        dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector r8 = dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector.this
                        java.lang.String r8 = r8.TAG
                        java.lang.StringBuilder r9 = new java.lang.StringBuilder
                        r9.<init>()
                        java.lang.String r10 = "daemonTimer serverCfgGetted="
                        java.lang.StringBuilder r9 = r9.append(r10)
                        dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector r10 = dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector.this
                        boolean r10 = r10.serverCfgGetted
                        java.lang.StringBuilder r9 = r9.append(r10)
                        java.lang.String r10 = " deviceCfgGetted isTimeoutCase can not be used!"
                        java.lang.StringBuilder r9 = r9.append(r10)
                        java.lang.String r9 = r9.toString()
                        dji.dbox.upgrade.p4.constants.DJIUpConstants.LOGD(r8, r9)
                        dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector r8 = dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector.this
                        r8.cancelTimer()
                        goto L_0x0075
                    L_0x00a8:
                        dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector r8 = dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector.this
                        java.lang.String r11 = r8.TAG
                        java.lang.StringBuilder r8 = new java.lang.StringBuilder
                        r8.<init>()
                        java.lang.String r12 = "daemonTimer serverCfgGetted="
                        java.lang.StringBuilder r8 = r8.append(r12)
                        dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector r12 = dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector.this
                        boolean r12 = r12.serverCfgGetted
                        java.lang.StringBuilder r8 = r8.append(r12)
                        java.lang.String r12 = " deviceCfgGetted="
                        java.lang.StringBuilder r12 = r8.append(r12)
                        if (r0 == 0) goto L_0x011d
                        r8 = r9
                    L_0x00cc:
                        java.lang.StringBuilder r8 = r12.append(r8)
                        java.lang.String r8 = r8.toString()
                        dji.dbox.upgrade.p4.constants.DJIUpConstants.LOGD(r11, r8)
                        dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector r8 = dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector.this
                        java.lang.String r8 = r8.TAG
                        java.lang.StringBuilder r11 = new java.lang.StringBuilder
                        r11.<init>()
                        java.lang.String r12 = "isAllSetted()="
                        java.lang.StringBuilder r11 = r11.append(r12)
                        dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector r12 = dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector.this
                        boolean r12 = r12.isAllSetted()
                        java.lang.StringBuilder r11 = r11.append(r12)
                        java.lang.String r11 = r11.toString()
                        dji.dbox.upgrade.p4.constants.DJIUpConstants.LOGD(r8, r11)
                        dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector r8 = dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector.this
                        java.util.List r8 = r8.list
                        if (r8 == 0) goto L_0x010c
                        dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector r8 = dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector.this
                        java.util.List r8 = r8.list
                        int r8 = r8.size()
                        if (r8 > 0) goto L_0x011f
                    L_0x010c:
                        dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector r8 = dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector.this
                        java.lang.String r8 = r8.TAG
                        java.lang.String r9 = "list=null"
                        dji.dbox.upgrade.p4.constants.DJIUpConstants.LOGD(r8, r9)
                        dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector r8 = dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector.this
                        r8.cancelTimer()
                        goto L_0x0075
                    L_0x011d:
                        r8 = r10
                        goto L_0x00cc
                    L_0x011f:
                        dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector r8 = dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector.this
                        java.util.List r8 = r8.list
                        java.util.Iterator r8 = r8.iterator()
                    L_0x0129:
                        boolean r11 = r8.hasNext()
                        if (r11 == 0) goto L_0x014e
                        java.lang.Object r1 = r8.next()
                        dji.dbox.upgrade.p4.model.DJIUpListElement r1 = (dji.dbox.upgrade.p4.model.DJIUpListElement) r1
                        dji.dbox.upgrade.p4.model.DJIUpCfgModel r11 = r1.cfgModel
                        int r12 = r0.antirollback
                        java.lang.String r13 = r0.antirollbackExt
                        boolean r11 = r11.isCanAntirollback(r12, r13)
                        r1.isAllow = r11
                        dji.dbox.upgrade.p4.model.DJIUpCfgModel r11 = r1.cfgModel
                        int r12 = r0.enforce
                        java.lang.String r13 = r0.enforceExt
                        boolean r11 = r11.isEnforce(r12, r13)
                        r1.isUpEnforce = r11
                        goto L_0x0129
                    L_0x014e:
                        dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector r8 = dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector.this
                        java.lang.String r8 = r8.TAG
                        java.lang.StringBuilder r11 = new java.lang.StringBuilder
                        r11.<init>()
                        java.lang.String r12 = "serverCfgGetted="
                        java.lang.StringBuilder r11 = r11.append(r12)
                        dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector r12 = dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector.this
                        boolean r12 = r12.serverCfgGetted
                        java.lang.StringBuilder r11 = r11.append(r12)
                        java.lang.String r12 = " cfgModel="
                        java.lang.StringBuilder r11 = r11.append(r12)
                        java.lang.StringBuilder r11 = r11.append(r0)
                        java.lang.String r11 = r11.toString()
                        dji.dbox.upgrade.p4.constants.DJIUpConstants.LOGD(r8, r11)
                        dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector r8 = dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector.this
                        boolean r8 = r8.serverCfgGetted
                        if (r8 == 0) goto L_0x02da
                        r6 = 0
                        dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector r8 = dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector.this
                        java.util.List r8 = r8.list
                        java.util.Iterator r8 = r8.iterator()
                    L_0x018d:
                        boolean r11 = r8.hasNext()
                        if (r11 == 0) goto L_0x01a2
                        java.lang.Object r1 = r8.next()
                        dji.dbox.upgrade.p4.model.DJIUpListElement r1 = (dji.dbox.upgrade.p4.model.DJIUpListElement) r1
                        boolean r11 = r1.isAllow
                        if (r11 != 0) goto L_0x01a1
                        boolean r11 = r1.isUpEnforce
                        if (r11 == 0) goto L_0x018d
                    L_0x01a1:
                        r6 = r1
                    L_0x01a2:
                        if (r6 != 0) goto L_0x01b5
                        dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector r8 = dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector.this
                        java.lang.String r8 = r8.TAG
                        java.lang.String r9 = "daemonTimer latestElement=null"
                        dji.dbox.upgrade.p4.constants.DJIUpConstants.LOGD(r8, r9)
                        dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector r8 = dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector.this
                        r8.cancelTimer()
                        goto L_0x0075
                    L_0x01b5:
                        dji.dbox.upgrade.p4.model.DJIUpCfgModel r5 = r6.cfgModel
                        if (r5 != 0) goto L_0x01db
                        dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector r8 = dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector.this
                        java.lang.String r8 = r8.TAG
                        java.lang.StringBuilder r9 = new java.lang.StringBuilder
                        r9.<init>()
                        java.lang.String r10 = "daemonTimer latestCfgModel==null cfgModel="
                        java.lang.StringBuilder r9 = r9.append(r10)
                        java.lang.StringBuilder r9 = r9.append(r0)
                        java.lang.String r9 = r9.toString()
                        dji.dbox.upgrade.p4.constants.DJIUpConstants.LOGD(r8, r9)
                        dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector r8 = dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector.this
                        r8.cancelTimer()
                        goto L_0x0075
                    L_0x01db:
                        dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector r8 = dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector.this
                        dji.dbox.upgrade.p4.model.DJIUpStatus r8 = r8.status
                        r8.setLatestElement(r6)
                        boolean r8 = r0.isNull()
                        if (r8 == 0) goto L_0x02eb
                        dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector r8 = dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector.this
                        dji.dbox.upgrade.p4.model.DJIUpStatus r8 = r8.status
                        r8.setNeedUpgrade(r9)
                        dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector r8 = dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector.this
                        dji.dbox.upgrade.p4.model.DJIUpStatus r8 = r8.status
                        r8.setNeedLock(r10)
                    L_0x01f6:
                        dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector r8 = dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector.this
                        java.lang.String r8 = r8.TAG
                        java.lang.StringBuilder r9 = new java.lang.StringBuilder
                        r9.<init>()
                        dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector r10 = dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector.this
                        dji.dbox.upgrade.p4.model.DJIUpStatus r10 = r10.status
                        dji.dbox.upgrade.p4.constants.DJIUpDeviceType r10 = r10.getProductId()
                        java.lang.StringBuilder r9 = r9.append(r10)
                        java.lang.String r10 = " latestCfgModel="
                        java.lang.StringBuilder r9 = r9.append(r10)
                        int r10 = r5.antirollback
                        java.lang.StringBuilder r9 = r9.append(r10)
                        java.lang.String r10 = " cfgModel="
                        java.lang.StringBuilder r9 = r9.append(r10)
                        int r10 = r0.antirollback
                        java.lang.StringBuilder r9 = r9.append(r10)
                        java.lang.String r9 = r9.toString()
                        dji.dbox.upgrade.p4.constants.DJIUpConstants.LOGD(r8, r9)
                        dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector r8 = dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector.this
                        java.lang.String r8 = r8.TAG
                        java.lang.StringBuilder r9 = new java.lang.StringBuilder
                        r9.<init>()
                        dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector r10 = dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector.this
                        dji.dbox.upgrade.p4.model.DJIUpStatus r10 = r10.status
                        dji.dbox.upgrade.p4.constants.DJIUpDeviceType r10 = r10.getProductId()
                        java.lang.StringBuilder r9 = r9.append(r10)
                        java.lang.String r10 = " latestCfgModel="
                        java.lang.StringBuilder r9 = r9.append(r10)
                        int r10 = r5.enforce
                        java.lang.StringBuilder r9 = r9.append(r10)
                        java.lang.String r10 = " cfgModel="
                        java.lang.StringBuilder r9 = r9.append(r10)
                        int r10 = r0.enforce
                        java.lang.StringBuilder r9 = r9.append(r10)
                        java.lang.String r9 = r9.toString()
                        dji.dbox.upgrade.p4.constants.DJIUpConstants.LOGD(r8, r9)
                        dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector r8 = dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector.this
                        java.lang.String r8 = r8.TAG
                        java.lang.StringBuilder r9 = new java.lang.StringBuilder
                        r9.<init>()
                        dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector r10 = dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector.this
                        dji.dbox.upgrade.p4.model.DJIUpStatus r10 = r10.status
                        dji.dbox.upgrade.p4.constants.DJIUpDeviceType r10 = r10.getProductId()
                        java.lang.StringBuilder r9 = r9.append(r10)
                        java.lang.String r10 = " latestCfgModel="
                        java.lang.StringBuilder r9 = r9.append(r10)
                        java.lang.String r10 = r5.releaseVersion
                        java.lang.StringBuilder r9 = r9.append(r10)
                        java.lang.String r10 = " cfgModel="
                        java.lang.StringBuilder r9 = r9.append(r10)
                        java.lang.String r10 = r0.releaseVersion
                        java.lang.StringBuilder r9 = r9.append(r10)
                        java.lang.String r9 = r9.toString()
                        dji.dbox.upgrade.p4.constants.DJIUpConstants.LOGD(r8, r9)
                        dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector r8 = dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector.this
                        java.lang.String r8 = r8.TAG
                        java.lang.StringBuilder r9 = new java.lang.StringBuilder
                        r9.<init>()
                        dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector r10 = dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector.this
                        dji.dbox.upgrade.p4.model.DJIUpStatus r10 = r10.status
                        dji.dbox.upgrade.p4.constants.DJIUpDeviceType r10 = r10.getProductId()
                        java.lang.StringBuilder r9 = r9.append(r10)
                        java.lang.String r10 = " isNeedUpgrade "
                        java.lang.StringBuilder r9 = r9.append(r10)
                        dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector r10 = dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector.this
                        dji.dbox.upgrade.p4.model.DJIUpStatus r10 = r10.status
                        boolean r10 = r10.isNeedUpgrade()
                        java.lang.StringBuilder r9 = r9.append(r10)
                        java.lang.String r10 = " isNeedLock "
                        java.lang.StringBuilder r9 = r9.append(r10)
                        dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector r10 = dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector.this
                        dji.dbox.upgrade.p4.model.DJIUpStatus r10 = r10.status
                        boolean r10 = r10.isNeedLock()
                        java.lang.StringBuilder r9 = r9.append(r10)
                        java.lang.String r9 = r9.toString()
                        dji.dbox.upgrade.p4.constants.DJIUpConstants.LOGD(r8, r9)
                    L_0x02da:
                        dji.midware.data.manager.P3.ServiceManager r8 = dji.midware.data.manager.P3.ServiceManager.getInstance()
                        boolean r8 = r8.isRemoteOK()
                        if (r8 != 0) goto L_0x02e4
                    L_0x02e4:
                        dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector r8 = dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector.this
                        r8.cancelTimer()
                        goto L_0x0075
                    L_0x02eb:
                        java.lang.String r8 = r5.releaseVersion
                        java.lang.String r11 = r0.releaseVersion
                        int r7 = dji.dbox.upgrade.p4.utils.DJIUpgradeBaseUtils.compareFirVer(r8, r11)
                        dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector r8 = dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector.this
                        dji.dbox.upgrade.p4.model.DJIUpStatus r11 = r8.status
                        if (r7 != r9) goto L_0x036e
                        r8 = r9
                    L_0x02fa:
                        r11.setNeedUpgrade(r8)
                        boolean r4 = r6.isUpEnforce
                        dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector r8 = dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector.this
                        java.lang.String r8 = r8.TAG
                        java.lang.StringBuilder r11 = new java.lang.StringBuilder
                        r11.<init>()
                        java.lang.String r12 = "up daemonTimer isNeedLock="
                        java.lang.StringBuilder r11 = r11.append(r12)
                        java.lang.StringBuilder r11 = r11.append(r4)
                        java.lang.String r11 = r11.toString()
                        dji.dbox.upgrade.p4.constants.DJIUpConstants.LOGD(r8, r11)
                        if (r4 == 0) goto L_0x0359
                        long r2 = r5.getEnforceTimestamp()
                        dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector r8 = dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector.this
                        java.lang.String r8 = r8.TAG
                        java.lang.StringBuilder r11 = new java.lang.StringBuilder
                        r11.<init>()
                        java.lang.String r12 = "up daemonTimer time="
                        java.lang.StringBuilder r11 = r11.append(r12)
                        java.lang.StringBuilder r11 = r11.append(r2)
                        java.lang.String r12 = " time="
                        java.lang.StringBuilder r11 = r11.append(r12)
                        long r12 = java.lang.System.currentTimeMillis()
                        java.lang.StringBuilder r11 = r11.append(r12)
                        java.lang.String r11 = r11.toString()
                        dji.dbox.upgrade.p4.constants.DJIUpConstants.LOGD(r8, r11)
                        r12 = 0
                        int r8 = (r2 > r12 ? 1 : (r2 == r12 ? 0 : -1))
                        if (r8 < 0) goto L_0x0359
                        long r12 = java.lang.System.currentTimeMillis()
                        int r8 = (r2 > r12 ? 1 : (r2 == r12 ? 0 : -1))
                        if (r8 > 0) goto L_0x0370
                        r4 = r9
                    L_0x0359:
                        dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector r8 = dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector.this
                        dji.dbox.upgrade.p4.model.DJIUpStatus r8 = r8.status
                        if (r7 != r9) goto L_0x0362
                        if (r4 == 0) goto L_0x0362
                        r10 = r9
                    L_0x0362:
                        r8.setNeedLock(r10)
                        dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector r8 = dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector.this
                        dji.dbox.upgrade.p4.model.DJIUpStatus r8 = r8.status
                        r8.setNeedLockGetted(r9)
                        goto L_0x01f6
                    L_0x036e:
                        r8 = r10
                        goto L_0x02fa
                    L_0x0370:
                        r4 = r10
                        goto L_0x0359
                    */
                    throw new UnsupportedOperationException("Method not decompiled: dji.dbox.upgrade.collectpacks.UpAbstractWifiCollector.AnonymousClass1.run():void");
                }
            }, 0, 1000);
        }
    }

    /* access modifiers changed from: private */
    public void cancelTimer() {
        this.collectorListener.onStrategyCollectListOver();
        if (this.daemonTimer != null) {
            this.daemonTimer.cancel();
            this.daemonTimer = null;
        }
    }

    private void getLocalCFG() {
        boolean z = true;
        this.list = DJIUpGetServerCfgManager.parseLocalList(null, this.serverManager.getCfgTarget(), this.status.getProductId() != null ? this.status.getProductId().getProductId() : DJIUpDeviceType.unknow.getProductId());
        this.serverCfgSetted = true;
        if (this.list == null) {
            z = false;
        }
        this.serverCfgGetted = z;
    }

    public void stop(boolean needQuit) {
        this.isAlive = false;
        if (this.daemonTimer != null) {
            this.daemonTimer.cancel();
            this.daemonTimer = null;
        }
        if (needQuit) {
            this.handlerThread.quit();
        }
    }

    /* access modifiers changed from: protected */
    public void startGetDeviceCfg() {
    }
}
