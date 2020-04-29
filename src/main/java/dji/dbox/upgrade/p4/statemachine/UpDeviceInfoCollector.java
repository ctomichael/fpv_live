package dji.dbox.upgrade.p4.statemachine;

import dji.dbox.upgrade.p4.constants.DJIUpConstants;
import dji.dbox.upgrade.p4.constants.DJIUpDeviceType;
import dji.dbox.upgrade.p4.statemachine.DJIUpgradeService;
import dji.dbox.upgrade.p4.utils.DJIUpGlassUtil;
import dji.dbox.upgrade.p4.utils.DJIUpStatusHelper;
import dji.dbox.upgrade.p4.utils.DJIUpStatusOfflineHelper;
import dji.dbox.upgrade.strategy.AbstractStrategy;
import dji.dbox.upgrade.strategy.McDiffRc;
import dji.dbox.upgrade.strategy.McOnly;
import dji.dbox.upgrade.strategy.McRcDiffGlass;
import dji.dbox.upgrade.strategy.McSameRc;
import dji.dbox.upgrade.strategy.RcOnly;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIConnectType;
import dji.midware.data.manager.P3.DJIPackManager;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.data.model.P3.DataCommonGetDeviceInfo;
import dji.midware.data.model.P3.DataCommonGetVersion;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.link.DJILinkDaemonService;
import dji.midware.link.DJILinkType;
import dji.midware.upgradeComponent.DJIUpgradeProductID;
import java.util.ArrayList;
import java.util.Locale;
import org.greenrobot.eventbus.EventBus;

@EXClassNullAway
public class UpDeviceInfoCollector {
    private static final String TAG = "UpDeviceInfoCollector";
    private UpDeviceInfoCollectListener collectListener;
    private DJIUpgradeService.DJIUpP4Event curEvent;
    /* access modifiers changed from: private */
    public DeviceType deviceType = DeviceType.OTHER;
    /* access modifiers changed from: private */
    public ArrayList<DJIUpDeviceType> devices = new ArrayList<>(1);
    /* access modifiers changed from: private */
    public DataCommonGetVersion getDeviceInfo = new DataCommonGetVersion();
    /* access modifiers changed from: private */
    public int getDeviceInfotimeOut = 3;
    /* access modifiers changed from: private */
    public DataCommonGetDeviceInfo getDm368Info = new DataCommonGetDeviceInfo();
    /* access modifiers changed from: private */
    public DataCommonGetVersion getVersion = new DataCommonGetVersion();
    /* access modifiers changed from: private */
    public int whoTimes = 0;

    public interface UpDeviceInfoCollectListener {
        void onCollectOver(Class<? extends AbstractStrategy> cls, DJIUpDeviceType dJIUpDeviceType, DJIUpDeviceType dJIUpDeviceType2);

        void onCollectStart();
    }

    static /* synthetic */ int access$1110(UpDeviceInfoCollector x0) {
        int i = x0.getDeviceInfotimeOut;
        x0.getDeviceInfotimeOut = i - 1;
        return i;
    }

    public void setCollectListener(UpDeviceInfoCollectListener collectListener2) {
        this.collectListener = collectListener2;
    }

    public synchronized void disConnect() {
        this.curEvent = DJIUpgradeService.DJIUpP4Event.Disconnect;
        EventBus.getDefault().post(this.curEvent);
    }

    public synchronized void reset() {
    }

    public synchronized void start() {
        this.deviceType = DeviceType.OTHER;
        DJIUpStatusHelper.setIsAllowRollBack(false);
        DJIUpConstants.LOGE(TAG, "UpDeviceInfoCollector start ");
        whoamI();
    }

    public synchronized void startOffline() {
        this.deviceType = DeviceType.OTHER;
        DJIUpStatusHelper.setIsAllowRollBack(false);
        DJIUpConstants.LOGE(TAG, "UpDeviceInfoCollector startOffline ");
        doneOffline();
    }

    /* access modifiers changed from: private */
    public synchronized void whoamI() {
        this.collectListener.onCollectStart();
        this.whoTimes++;
        this.getVersion.setDeviceType(DeviceType.WHO);
        this.getVersion.start(new DJIDataCallBack() {
            /* class dji.dbox.upgrade.p4.statemachine.UpDeviceInfoCollector.AnonymousClass1 */

            public void onSuccess(Object model) {
                DJIUpConstants.LOGE(UpDeviceInfoCollector.TAG, "whoamI " + UpDeviceInfoCollector.this.getVersion.getWhoamI());
                DeviceType unused = UpDeviceInfoCollector.this.deviceType = UpDeviceInfoCollector.this.getVersion.getWhoamI();
                switch (AnonymousClass4.$SwitchMap$dji$midware$data$config$P3$DeviceType[UpDeviceInfoCollector.this.deviceType.ordinal()]) {
                    case 1:
                        if (DJILinkDaemonService.getInstance().getLinkType() != DJILinkType.WIFI) {
                            DJIPackManager.getInstance().setConnectType(DJIConnectType.MC);
                            break;
                        } else {
                            DJIPackManager.getInstance().setConnectType(DJIConnectType.RC);
                            DeviceType unused2 = UpDeviceInfoCollector.this.deviceType = DeviceType.DM368_G;
                            break;
                        }
                    case 2:
                        DJIPackManager.getInstance().setConnectType(DJIConnectType.RC);
                        break;
                    case 3:
                        DJIPackManager.getInstance().setConnectType(DJIConnectType.RC);
                        break;
                    default:
                        DeviceType unused3 = UpDeviceInfoCollector.this.deviceType = DeviceType.DM368_G;
                        DJIPackManager.getInstance().setConnectType(DJIConnectType.RC);
                        break;
                }
                int unused4 = UpDeviceInfoCollector.this.whoTimes = 0;
                UpDeviceInfoCollector.this.getDeviceInfo();
            }

            public void onFailure(Ccode ccode) {
                DJIUpConstants.LOGE(UpDeviceInfoCollector.TAG, "whoamI " + ccode);
                if (Ccode.TIMEOUT != ccode || UpDeviceInfoCollector.this.whoTimes >= 4) {
                    int unused = UpDeviceInfoCollector.this.whoTimes = 0;
                    if (Ccode.NOCONNECT != ccode) {
                        DeviceType unused2 = UpDeviceInfoCollector.this.deviceType = DeviceType.DM368_G;
                    }
                    UpDeviceInfoCollector.this.getDeviceInfo();
                    return;
                }
                UpDeviceInfoCollector.this.whoamI();
            }
        }, 500, 2);
    }

    /* access modifiers changed from: private */
    public synchronized void getDeviceInfo() {
        this.devices.clear();
        this.getDeviceInfo.setDeviceType(DeviceType.BROADCAST);
        this.getDeviceInfo.setDeviceModel(0);
        this.getDeviceInfo.start(new DJIDataCallBack() {
            /* class dji.dbox.upgrade.p4.statemachine.UpDeviceInfoCollector.AnonymousClass2 */

            public void onSuccess(Object model) {
                DJIUpDeviceType device;
                boolean isNeedAcResult = false;
                String info = UpDeviceInfoCollector.this.getDeviceInfo.getHardwareVer().toLowerCase(Locale.ENGLISH);
                DJIUpConstants.LOGE(UpDeviceInfoCollector.TAG, "getDeviceInfo broadcast " + info);
                if (info.contains(DJIUpgradeProductID.WM330)) {
                    UpDeviceInfoCollector.this.devices.add(DJIUpDeviceType.wm330);
                    DJIProductManager.getInstance().setType(ProductType.Tomato);
                    DJIProductManager.getInstance().setRemoteSeted(true);
                    if (UpDeviceInfoCollector.this.deviceType == DeviceType.DM368) {
                        DJIUpStatusHelper.setIsAllowRollBack(true);
                    }
                } else if (info.contains(DJIUpgradeProductID.WM331)) {
                    UpDeviceInfoCollector.this.devices.add(DJIUpDeviceType.wm331);
                    DJIProductManager.getInstance().setType(ProductType.Pomato);
                    DJIProductManager.getInstance().setRemoteSeted(true);
                    if (UpDeviceInfoCollector.this.deviceType == DeviceType.DM368) {
                        DJIUpStatusHelper.setIsAllowRollBack(true);
                    }
                } else if (info.contains(DJIUpgradeProductID.RC001) || info.contains("de01")) {
                    UpDeviceInfoCollector.this.devices.add(DJIUpDeviceType.rc001);
                    DJIUpStatusHelper.setIsAllowRollBack(true);
                    if (ServiceManager.getInstance().isRemoteOK()) {
                        isNeedAcResult = true;
                        UpDeviceInfoCollector.this.getFromAir();
                    }
                } else if (info.contains(DJIUpgradeProductID.RC003)) {
                    UpDeviceInfoCollector.this.devices.add(DJIUpDeviceType.rc003);
                    DJIUpStatusHelper.setIsAllowRollBack(true);
                    if (ServiceManager.getInstance().isRemoteOK()) {
                        isNeedAcResult = true;
                        UpDeviceInfoCollector.this.getFromAir();
                    }
                } else if (info.contains(DJIUpDeviceType.rm500.getProductId())) {
                    UpDeviceInfoCollector.this.devices.add(DJIUpDeviceType.rm500);
                    DJIUpStatusHelper.setIsAllowRollBack(true);
                    if (ServiceManager.getInstance().isRemoteOK()) {
                        isNeedAcResult = true;
                        UpDeviceInfoCollector.this.getFromAir();
                    }
                } else if (info.contains(DJIUpgradeProductID.WM620)) {
                    UpDeviceInfoCollector.this.devices.add(DJIUpDeviceType.wm620);
                    DJIProductManager.getInstance().setType(ProductType.Orange2);
                    DJIProductManager.getInstance().setRemoteSeted(true);
                    DJIUpStatusHelper.setIsAllowRollBack(true);
                } else if (info.contains(DJIUpgradeProductID.PM410)) {
                    UpDeviceInfoCollector.this.devices.add(DJIUpDeviceType.pm410);
                    DJIProductManager.getInstance().setType(ProductType.M200);
                    DJIProductManager.getInstance().setRemoteSeted(true);
                    DJIUpStatusHelper.setIsAllowRollBack(true);
                } else if (info.contains(DJIUpgradeProductID.WM220)) {
                    UpDeviceInfoCollector.this.devices.add(DJIUpDeviceType.wm220);
                    if (info.contains(DJIUpgradeProductID.P4PSDRACSig)) {
                        DJIProductManager.getInstance().setType(ProductType.KumquatX);
                        DJIProductManager.getInstance().setRemoteSeted(true);
                        DeviceType unused = UpDeviceInfoCollector.this.deviceType = DeviceType.DM368;
                        if (DJILinkDaemonService.getInstance().getLinkType() == DJILinkType.WIFI) {
                            DeviceType unused2 = UpDeviceInfoCollector.this.deviceType = DeviceType.DM368_G;
                        }
                    } else {
                        DJIProductManager.getInstance().setType(ProductType.KumquatX);
                        DeviceType unused3 = UpDeviceInfoCollector.this.deviceType = DeviceType.DM368_G;
                        UpDeviceInfoCollector.this.checkGlassUpgrade();
                    }
                    DJIUpStatusHelper.setIsAllowRollBack(true);
                } else if (info.contains(DJIUpgradeProductID.WM221)) {
                    UpDeviceInfoCollector.this.devices.add(DJIUpDeviceType.wm221);
                    if (info.contains(DJIUpgradeProductID.P4PSDRACSig)) {
                        DJIProductManager.getInstance().setType(ProductType.KumquatS);
                        DJIProductManager.getInstance().setRemoteSeted(true);
                        DeviceType unused4 = UpDeviceInfoCollector.this.deviceType = DeviceType.DM368;
                    } else {
                        DJIProductManager.getInstance().setType(ProductType.KumquatS);
                        DeviceType unused5 = UpDeviceInfoCollector.this.deviceType = DeviceType.DM368_G;
                        UpDeviceInfoCollector.this.checkGlassUpgrade();
                    }
                    DJIUpStatusHelper.setIsAllowRollBack(true);
                } else if (info.contains(DJIUpgradeProductID.RC002)) {
                    UpDeviceInfoCollector.this.devices.add(DJIUpDeviceType.rc002);
                    if (DataOsdGetPushCommon.getInstance().getDroneType() == DataOsdGetPushCommon.DroneType.WM230) {
                        DJIProductManager.getInstance().setType(ProductType.WM230);
                    } else {
                        DJIProductManager.getInstance().setType(ProductType.Mammoth);
                    }
                    DeviceType unused6 = UpDeviceInfoCollector.this.deviceType = DeviceType.WIFI_G;
                    DJIUpStatusHelper.setIsAllowRollBack(true);
                    if (ServiceManager.getInstance().isRemoteOK()) {
                        isNeedAcResult = true;
                        UpDeviceInfoCollector.this.getFromAir();
                    }
                } else if (info.contains(DJIUpgradeProductID.RC230)) {
                    UpDeviceInfoCollector.this.devices.add(DJIUpDeviceType.rc230);
                    DeviceType unused7 = UpDeviceInfoCollector.this.deviceType = DeviceType.WIFI_G;
                    DJIProductManager.getInstance().setType(ProductType.WM230);
                    DJIUpStatusHelper.setIsAllowRollBack(true);
                    if (ServiceManager.getInstance().isRemoteOK()) {
                        isNeedAcResult = true;
                        UpDeviceInfoCollector.this.getFromAir();
                    }
                } else if (info.contains(DJIUpDeviceType.wm240.name())) {
                    if (!info.contains("rc")) {
                        device = DJIUpDeviceType.wm240;
                    } else if (info.endsWith("a")) {
                        device = DJIUpDeviceType.rc240;
                    } else {
                        device = DJIUpDeviceType.rc010;
                    }
                    if (!device.isMc()) {
                        UpDeviceInfoCollector.this.devices.add(device);
                        DeviceType unused8 = UpDeviceInfoCollector.this.deviceType = DeviceType.DM368_G;
                        DJIProductManager.getInstance().setType(ProductType.WM240);
                        DJIUpStatusHelper.setIsAllowRollBack(true);
                        if (ServiceManager.getInstance().isRemoteOK()) {
                            isNeedAcResult = true;
                            UpDeviceInfoCollector.this.getFromAir();
                        }
                    }
                } else if (info.contains(DJIUpgradeProductID.WM332)) {
                    UpDeviceInfoCollector.this.devices.add(DJIUpDeviceType.wm332);
                    DJIProductManager.getInstance().setType(ProductType.Potato);
                    DJIProductManager.getInstance().setRemoteSeted(true);
                    if (UpDeviceInfoCollector.this.deviceType == DeviceType.DM368) {
                        DJIUpStatusHelper.setIsAllowRollBack(true);
                    }
                } else if (info.contains(DJIUpDeviceType.wm335.name())) {
                    UpDeviceInfoCollector.this.devices.add(DJIUpDeviceType.wm335);
                    if (info.contains(DJIUpgradeProductID.P4PSDRACSig)) {
                        DJIProductManager.getInstance().setType(ProductType.PomatoSDR);
                        DJIProductManager.getInstance().setRemoteSeted(true);
                        DeviceType unused9 = UpDeviceInfoCollector.this.deviceType = DeviceType.DM368;
                    } else {
                        DJIProductManager.getInstance().setType(ProductType.PomatoSDR);
                        DeviceType unused10 = UpDeviceInfoCollector.this.deviceType = DeviceType.DM368_G;
                    }
                    DJIUpStatusHelper.setIsAllowRollBack(true);
                } else if (info.contains(DJIUpDeviceType.gl300k.name())) {
                    UpDeviceInfoCollector.this.devices.add(DJIUpDeviceType.wm335);
                    DJIUpStatusHelper.setIsAllowRollBack(true);
                    if (ServiceManager.getInstance().isRemoteOK()) {
                        isNeedAcResult = true;
                        UpDeviceInfoCollector.this.getFromAir();
                    }
                } else if (info.contains(DJIUpDeviceType.wm334.name())) {
                    UpDeviceInfoCollector.this.devices.add(DJIUpDeviceType.wm334);
                    DJIProductManager.getInstance().setType(ProductType.PomatoRTK);
                    DJIProductManager.getInstance().setRemoteSeted(true);
                    DeviceType unused11 = UpDeviceInfoCollector.this.deviceType = DeviceType.DM368;
                    DJIUpStatusHelper.setIsAllowRollBack(false);
                } else if (info.contains(DJIUpDeviceType.ag410.name())) {
                    UpDeviceInfoCollector.this.devices.add(DJIUpDeviceType.ag410);
                    DJIUpStatusHelper.setIsAllowRollBack(false);
                    if (ServiceManager.getInstance().isRemoteOK()) {
                        isNeedAcResult = true;
                        UpDeviceInfoCollector.this.getFromAir();
                    }
                } else {
                    isNeedAcResult = true;
                    UpDeviceInfoCollector.this.getFromAir();
                }
                if (!isNeedAcResult) {
                    UpDeviceInfoCollector.this.collectOver(true);
                }
            }

            public void onFailure(Ccode ccode) {
                if (ccode == Ccode.TIMEOUT || ccode == Ccode.UNDEFINED) {
                    DJIUpConstants.LOGE(UpDeviceInfoCollector.TAG, "getDeviceInfo " + UpDeviceInfoCollector.this.getDeviceInfo.getDeviceType() + " fail " + ccode);
                    UpDeviceInfoCollector.this.getFromAir();
                    return;
                }
                UpDeviceInfoCollector.this.collectOver(false);
            }
        }, 1000, 20);
    }

    /* access modifiers changed from: private */
    public synchronized void getFromAir() {
        this.getDm368Info.setReceiveType(DeviceType.DM368);
        this.getDm368Info.setReceiveId(1);
        this.getDm368Info.start(new DJIDataCallBack() {
            /* class dji.dbox.upgrade.p4.statemachine.UpDeviceInfoCollector.AnonymousClass3 */

            public void onSuccess(Object model) {
                DJIUpDeviceType device = null;
                String info = UpDeviceInfoCollector.this.getDm368Info.getInfo().toLowerCase(Locale.ENGLISH);
                DJIUpConstants.LOGE(UpDeviceInfoCollector.TAG, "getDeviceInfo FromAir broadcast air " + info);
                if (info.contains(DJIUpgradeProductID.WM330)) {
                    device = DJIUpDeviceType.wm330;
                    DJIProductManager.getInstance().setType(ProductType.Tomato);
                    DJIProductManager.getInstance().setRemoteSeted(true);
                    if (UpDeviceInfoCollector.this.deviceType == DeviceType.DM368) {
                        DJIUpStatusHelper.setIsAllowRollBack(true);
                    }
                } else if (info.contains(DJIUpgradeProductID.WM331)) {
                    device = DJIUpDeviceType.wm331;
                    DJIProductManager.getInstance().setType(ProductType.Pomato);
                    DJIProductManager.getInstance().setRemoteSeted(true);
                    if (UpDeviceInfoCollector.this.deviceType == DeviceType.DM368) {
                        DJIUpStatusHelper.setIsAllowRollBack(true);
                    }
                } else if (info.contains(DJIUpgradeProductID.WM620)) {
                    device = DJIUpDeviceType.wm620;
                    DJIProductManager.getInstance().setType(ProductType.Orange2);
                    DJIProductManager.getInstance().setRemoteSeted(true);
                    DJIUpStatusHelper.setIsAllowRollBack(true);
                } else if (info.contains(DJIUpgradeProductID.PM410)) {
                    device = DJIUpDeviceType.pm410;
                    DJIUpStatusHelper.setIsAllowRollBack(true);
                } else if (info.contains(DJIUpgradeProductID.WM220)) {
                    device = DJIUpDeviceType.wm220;
                    if (info.contains(DJIUpgradeProductID.P4PSDRACSig)) {
                        DJIProductManager.getInstance().setType(ProductType.KumquatX);
                        DJIProductManager.getInstance().setRemoteSeted(true);
                    }
                } else if (info.contains(DJIUpgradeProductID.WM221)) {
                    device = DJIUpDeviceType.wm221;
                    if (info.contains(DJIUpgradeProductID.P4PSDRACSig)) {
                        DJIProductManager.getInstance().setType(ProductType.KumquatS);
                        DJIProductManager.getInstance().setRemoteSeted(true);
                    }
                } else if (info.contains(DJIUpgradeProductID.WM332)) {
                    device = DJIUpDeviceType.wm332;
                    DJIProductManager.getInstance().setType(ProductType.Potato);
                    DJIProductManager.getInstance().setRemoteSeted(true);
                    if (UpDeviceInfoCollector.this.deviceType == DeviceType.DM368) {
                        DJIUpStatusHelper.setIsAllowRollBack(true);
                    }
                } else if (info.contains(DJIUpDeviceType.wm335.name())) {
                    if (!UpDeviceInfoCollector.this.devices.contains(DJIUpDeviceType.wm335)) {
                        device = DJIUpDeviceType.wm335;
                    }
                    if (info.contains(DJIUpgradeProductID.P4PSDRACSig)) {
                        DJIProductManager.getInstance().setType(ProductType.PomatoSDR);
                        DJIProductManager.getInstance().setRemoteSeted(true);
                    }
                } else if (info.contains(DJIUpDeviceType.wm334.name())) {
                    device = DJIUpDeviceType.wm334;
                    DJIProductManager.getInstance().setType(ProductType.PomatoRTK);
                    DJIProductManager.getInstance().setRemoteSeted(true);
                    DJIUpStatusHelper.setIsAllowRollBack(false);
                } else if (info.contains("wm100")) {
                    device = DJIUpDeviceType.wm100ac;
                    DJIProductManager.getInstance().setType(ProductType.Mammoth);
                    DJIUpStatusHelper.setIsAllowRollBack(true);
                    DJIProductManager.getInstance().setRemoteSeted(true);
                } else if (info.contains(DJIUpgradeProductID.WM230AC)) {
                    device = DJIUpDeviceType.wm230;
                    DJIProductManager.getInstance().setType(ProductType.WM230);
                    DJIUpStatusHelper.setIsAllowRollBack(true);
                    DJIProductManager.getInstance().setRemoteSeted(true);
                } else if (info.contains(DJIUpDeviceType.wm240.name())) {
                    device = DJIUpDeviceType.wm240;
                    DJIProductManager.getInstance().setType(ProductType.WM240);
                    DJIUpStatusHelper.setIsAllowRollBack(true);
                    DJIProductManager.getInstance().setRemoteSeted(true);
                }
                if (device == null) {
                    UpDeviceInfoCollector.this.collectOver(false);
                    return;
                }
                UpDeviceInfoCollector.this.devices.add(device);
                UpDeviceInfoCollector.this.collectOver(true);
            }

            public void onFailure(Ccode ccode) {
                if (ccode == Ccode.TIMEOUT) {
                    DJIUpConstants.LOGE(UpDeviceInfoCollector.TAG, "getDeviceInfo dm368 " + ccode + " times=" + UpDeviceInfoCollector.this.getDeviceInfotimeOut);
                    if (UpDeviceInfoCollector.this.getDeviceInfotimeOut > 0) {
                        UpDeviceInfoCollector.access$1110(UpDeviceInfoCollector.this);
                        UpDeviceInfoCollector.this.getFromAir();
                        return;
                    }
                    int unused = UpDeviceInfoCollector.this.getDeviceInfotimeOut = 3;
                    UpDeviceInfoCollector.this.collectOver(false);
                    return;
                }
                int unused2 = UpDeviceInfoCollector.this.getDeviceInfotimeOut = 3;
                UpDeviceInfoCollector.this.collectOver(false);
            }
        });
    }

    /* access modifiers changed from: private */
    public void checkGlassUpgrade() {
        DJIUpConstants.LOGE(TAG, "checkGlassUpgrade isGlassConnected = " + DJIUpGlassUtil.isGlassConnected() + " glassType = " + DJIUpGlassUtil.getGlassType());
        if (DJIUpGlassUtil.isUpgradeOwn()) {
            this.devices.add(DJIUpDeviceType.gl811);
        }
    }

    /* access modifiers changed from: private */
    public synchronized void collectOver(boolean b) {
        done();
    }

    private void done() {
        boolean isConnectP4Series;
        DJIUpDeviceType device2 = null;
        if (this.deviceType == DeviceType.DM368 || this.deviceType == DeviceType.DM368_G || this.deviceType == DeviceType.WIFI_G) {
            isConnectP4Series = true;
        } else {
            isConnectP4Series = false;
        }
        if (!isConnectP4Series && !DJIProductManager.getInstance().isRemoteSeted()) {
            this.curEvent = DJIUpgradeService.DJIUpP4Event.ConnectOther;
            DJIUpStatusHelper.setCurEvent(this.curEvent);
        } else if (this.devices.size() == 0) {
            this.curEvent = DJIUpgradeService.DJIUpP4Event.ConnectOther;
            DJIUpStatusHelper.setCurEvent(this.curEvent);
            this.collectListener.onCollectOver(null, null, null);
        } else {
            DataOsdGetPushCommon.DroneType droneType = DataOsdGetPushCommon.getInstance().getDroneType();
            DJIUpConstants.LOGE(TAG, "isConnectP4Series=" + isConnectP4Series + " getDroneType=" + droneType);
            if (!ServiceManager.getInstance().isRemoteOK() || DJIProductManager.getInstance().getType().isFromWifi() || isConnectP4Series || droneType != DataOsdGetPushCommon.DroneType.Unknown) {
                if (!isConnectP4Series && DJIUpStatusHelper.isProductUpFromMC(droneType)) {
                    isConnectP4Series = true;
                }
                DJIUpDeviceType device1 = this.devices.get(0);
                DJIUpStatusHelper.setConnectDeviceType(device1);
                if (this.devices.size() > 1) {
                    device2 = this.devices.get(1);
                }
                DJIUpStatusOfflineHelper.setDevices(device1, device2);
                DJIUpConstants.LOGE(TAG, "doneOnline device1=" + device1);
                DJIUpConstants.LOGE(TAG, "doneOnline device2=" + device2);
                if (device1.isOffline()) {
                    DJIUpStatusOfflineHelper.setLastDeviceSsid();
                }
                Class<? extends AbstractStrategy> strategyCls = null;
                if (this.deviceType == DeviceType.OTHER) {
                    this.curEvent = DJIUpgradeService.DJIUpP4Event.ConnectOther;
                } else if (!isConnectP4Series || this.deviceType != DeviceType.DM368) {
                    if (isConnectP4Series && (this.deviceType == DeviceType.DM368_G || this.deviceType == DeviceType.WIFI_G)) {
                        this.curEvent = DJIUpgradeService.DJIUpP4Event.ConnectP4RC;
                        switch (device1) {
                            case rc002:
                            case rc001:
                            case rc003:
                            case rm500:
                            case rc230:
                            case rc240:
                            case rc010:
                                if (!ServiceManager.getInstance().isRemoteOK()) {
                                    strategyCls = RcOnly.class;
                                    break;
                                } else {
                                    strategyCls = McDiffRc.class;
                                    if (device2 == null) {
                                        if (DJIProductManager.getInstance().isRemoteSeted()) {
                                            switch (DJIProductManager.getInstance().getType()) {
                                                case Orange2:
                                                    device2 = DJIUpDeviceType.wm620;
                                                    break;
                                                case Mammoth:
                                                    device2 = DJIUpDeviceType.wm100ac;
                                                    break;
                                                case WM230:
                                                    device2 = DJIUpDeviceType.wm230;
                                                    break;
                                                case WM240:
                                                    device2 = DJIUpDeviceType.wm240;
                                                    break;
                                                case PomatoRTK:
                                                    device2 = DJIUpDeviceType.wm334;
                                                    break;
                                            }
                                        }
                                        if (device2 == null) {
                                            strategyCls = RcOnly.class;
                                            break;
                                        }
                                    }
                                }
                                break;
                            case wm220:
                            case wm221:
                            case wm335:
                                if (device2 != DJIUpDeviceType.gl811) {
                                    strategyCls = McSameRc.class;
                                    break;
                                } else {
                                    strategyCls = McRcDiffGlass.class;
                                    break;
                                }
                            case wm330:
                            case wm331:
                            case wm332:
                            case wm334:
                                strategyCls = McOnly.class;
                                break;
                            case wm100ac:
                            case wm230:
                            case wm240:
                                strategyCls = McOnly.class;
                                break;
                        }
                    } else {
                        this.curEvent = DJIUpgradeService.DJIUpP4Event.ConnectOther;
                    }
                } else {
                    this.curEvent = DJIUpgradeService.DJIUpP4Event.ConnectP4MC;
                    strategyCls = McOnly.class;
                }
                DJIUpStatusHelper.setCurEvent(this.curEvent);
                if (strategyCls != null) {
                    if (strategyCls == McDiffRc.class && DJIUpDeviceType.isMcUpBefore(device1)) {
                        this.collectListener.onCollectOver(strategyCls, device2, device1);
                    } else if (strategyCls == McRcDiffGlass.class && device2 == DJIUpDeviceType.gl811) {
                        this.collectListener.onCollectOver(strategyCls, device2, device1);
                    } else {
                        this.collectListener.onCollectOver(strategyCls, device1, device2);
                    }
                }
                EventBus.getDefault().post(this.curEvent);
                return;
            }
            this.curEvent = DJIUpgradeService.DJIUpP4Event.ConnectOther;
            DJIUpStatusHelper.setCurEvent(this.curEvent);
        }
    }

    /* renamed from: dji.dbox.upgrade.p4.statemachine.UpDeviceInfoCollector$4  reason: invalid class name */
    static /* synthetic */ class AnonymousClass4 {
        static final /* synthetic */ int[] $SwitchMap$dji$midware$data$config$P3$DeviceType = new int[DeviceType.values().length];

        static {
            $SwitchMap$dji$dbox$upgrade$p4$constants$DJIUpDeviceType = new int[DJIUpDeviceType.values().length];
            try {
                $SwitchMap$dji$dbox$upgrade$p4$constants$DJIUpDeviceType[DJIUpDeviceType.rc002.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$dji$dbox$upgrade$p4$constants$DJIUpDeviceType[DJIUpDeviceType.rc001.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$dji$dbox$upgrade$p4$constants$DJIUpDeviceType[DJIUpDeviceType.rc003.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$dji$dbox$upgrade$p4$constants$DJIUpDeviceType[DJIUpDeviceType.rm500.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$dji$dbox$upgrade$p4$constants$DJIUpDeviceType[DJIUpDeviceType.rc230.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$dji$dbox$upgrade$p4$constants$DJIUpDeviceType[DJIUpDeviceType.rc240.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$dji$dbox$upgrade$p4$constants$DJIUpDeviceType[DJIUpDeviceType.rc010.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$dji$dbox$upgrade$p4$constants$DJIUpDeviceType[DJIUpDeviceType.wm220.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$dji$dbox$upgrade$p4$constants$DJIUpDeviceType[DJIUpDeviceType.wm221.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$dji$dbox$upgrade$p4$constants$DJIUpDeviceType[DJIUpDeviceType.wm335.ordinal()] = 10;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$dji$dbox$upgrade$p4$constants$DJIUpDeviceType[DJIUpDeviceType.wm330.ordinal()] = 11;
            } catch (NoSuchFieldError e11) {
            }
            try {
                $SwitchMap$dji$dbox$upgrade$p4$constants$DJIUpDeviceType[DJIUpDeviceType.wm331.ordinal()] = 12;
            } catch (NoSuchFieldError e12) {
            }
            try {
                $SwitchMap$dji$dbox$upgrade$p4$constants$DJIUpDeviceType[DJIUpDeviceType.wm332.ordinal()] = 13;
            } catch (NoSuchFieldError e13) {
            }
            try {
                $SwitchMap$dji$dbox$upgrade$p4$constants$DJIUpDeviceType[DJIUpDeviceType.wm334.ordinal()] = 14;
            } catch (NoSuchFieldError e14) {
            }
            try {
                $SwitchMap$dji$dbox$upgrade$p4$constants$DJIUpDeviceType[DJIUpDeviceType.wm100ac.ordinal()] = 15;
            } catch (NoSuchFieldError e15) {
            }
            try {
                $SwitchMap$dji$dbox$upgrade$p4$constants$DJIUpDeviceType[DJIUpDeviceType.wm230.ordinal()] = 16;
            } catch (NoSuchFieldError e16) {
            }
            try {
                $SwitchMap$dji$dbox$upgrade$p4$constants$DJIUpDeviceType[DJIUpDeviceType.wm240.ordinal()] = 17;
            } catch (NoSuchFieldError e17) {
            }
            $SwitchMap$dji$midware$data$config$P3$ProductType = new int[ProductType.values().length];
            try {
                $SwitchMap$dji$midware$data$config$P3$ProductType[ProductType.Orange2.ordinal()] = 1;
            } catch (NoSuchFieldError e18) {
            }
            try {
                $SwitchMap$dji$midware$data$config$P3$ProductType[ProductType.Mammoth.ordinal()] = 2;
            } catch (NoSuchFieldError e19) {
            }
            try {
                $SwitchMap$dji$midware$data$config$P3$ProductType[ProductType.WM230.ordinal()] = 3;
            } catch (NoSuchFieldError e20) {
            }
            try {
                $SwitchMap$dji$midware$data$config$P3$ProductType[ProductType.WM240.ordinal()] = 4;
            } catch (NoSuchFieldError e21) {
            }
            try {
                $SwitchMap$dji$midware$data$config$P3$ProductType[ProductType.PomatoRTK.ordinal()] = 5;
            } catch (NoSuchFieldError e22) {
            }
            try {
                $SwitchMap$dji$midware$data$config$P3$DeviceType[DeviceType.DM368.ordinal()] = 1;
            } catch (NoSuchFieldError e23) {
            }
            try {
                $SwitchMap$dji$midware$data$config$P3$DeviceType[DeviceType.WIFI_G.ordinal()] = 2;
            } catch (NoSuchFieldError e24) {
            }
            try {
                $SwitchMap$dji$midware$data$config$P3$DeviceType[DeviceType.DM368_G.ordinal()] = 3;
            } catch (NoSuchFieldError e25) {
            }
        }
    }

    private void doneOffline() {
        DJIUpStatusOfflineHelper.refreshDeviceInfo();
        DJIUpDeviceType device1 = DJIUpStatusOfflineHelper.getDevice1();
        DJIUpDeviceType device2 = DJIUpStatusOfflineHelper.getDevice2();
        DJIUpConstants.LOGE(TAG, "doneOffline device1=" + device1);
        DJIUpConstants.LOGE(TAG, "doneOffline device2=" + device2);
        if (DJIUpDeviceType.isSupportCompareOffline(device1)) {
            DJIUpStatusHelper.setConnectDeviceType(device1);
            Class<? extends AbstractStrategy> strategyCls = null;
            if (!device1.isMc() || device1 == DJIUpDeviceType.wm100ac) {
                this.curEvent = DJIUpgradeService.DJIUpP4Event.ConnectP4RC;
            } else {
                this.curEvent = DJIUpgradeService.DJIUpP4Event.ConnectP4MC;
            }
            switch (device1) {
                case rc002:
                case rc240:
                case rc010:
                    strategyCls = McDiffRc.class;
                    if (device2 == DJIUpDeviceType.unknow) {
                        strategyCls = RcOnly.class;
                        break;
                    }
                    break;
                case wm221:
                    strategyCls = McSameRc.class;
                    break;
                case wm100ac:
                case wm230:
                case wm240:
                    strategyCls = McOnly.class;
                    break;
            }
            DJIUpStatusHelper.setIsAllowRollBack(true);
            DJIUpStatusHelper.setCurEvent(this.curEvent);
            if (strategyCls != null) {
                if (strategyCls != McDiffRc.class || !DJIUpDeviceType.isMcUpBefore(device1)) {
                    this.collectListener.onCollectOver(strategyCls, device1, device2);
                } else {
                    this.collectListener.onCollectOver(strategyCls, device2, device1);
                }
            }
            EventBus.getDefault().post(this.curEvent);
        }
    }
}
