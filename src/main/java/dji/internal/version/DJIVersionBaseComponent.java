package dji.internal.version;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import com.dji.frame.util.V_JsonUtil;
import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.version.DJIDeviceVersionRequest;
import dji.internal.version.DJIModelUpgradePackList;
import dji.internal.version.component.DJIVersionP3cComponent;
import dji.log.DJILog;
import dji.log.DJILogHelper;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.util.BackgroundLooper;
import dji.midware.util.DJIEventBusUtil;
import dji.midware.util.DjiSharedPreferencesManager;
import dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction;
import dji.sdksharedlib.util.Util;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public abstract class DJIVersionBaseComponent {
    private static final int DEFAULT_EVENT = 1;
    private static final String TAG = "DJIVersionBaseComponent";
    private static final boolean showLog = false;
    private String componentVersion = null;
    /* access modifiers changed from: private */
    public ArrayList<DJIDeviceVersion> djiDeviceVersionList = null;
    private String djiDeviceVersionListSaveKey = null;
    private Runnable getVersionRunnable = null;
    private Handler handler = null;
    /* access modifiers changed from: private */
    public boolean isGettingVersion = false;

    public static class DJIDeviceVersionList {
        public ArrayList<DJIDeviceVersion> list;
    }

    /* access modifiers changed from: protected */
    public abstract String getComponentName();

    /* access modifiers changed from: protected */
    public abstract ArrayList<DJIModelUpgradePackList.DJIUpgradePack> getDJIUpgradePackList(DJIModelUpgradePackList dJIModelUpgradePackList);

    /* access modifiers changed from: protected */
    public abstract String[] getFirmwareList();

    /* access modifiers changed from: protected */
    public abstract String getVersion(DJIModelUpgradePackList.DJIUpgradePack dJIUpgradePack);

    public void init(Context ctx) {
        String simpleName = getClass().getSimpleName();
        log(getComponentName() + " init", false);
        this.djiDeviceVersionListSaveKey = "upgrade_component_" + getComponentName();
        DJIEventBusUtil.register(this);
        updateComponentVersion();
        this.handler = new Handler(BackgroundLooper.getLooper(), new Handler.Callback() {
            /* class dji.internal.version.DJIVersionBaseComponent.AnonymousClass1 */

            public boolean handleMessage(Message msg) {
                if (msg.what != 1) {
                    return false;
                }
                new Thread("versionComp") {
                    /* class dji.internal.version.DJIVersionBaseComponent.AnonymousClass1.AnonymousClass1 */

                    public void run() {
                        DJIVersionBaseComponent.this.startGetVersion();
                    }
                }.start();
                return false;
            }
        });
        this.getVersionRunnable = new Runnable() {
            /* class dji.internal.version.DJIVersionBaseComponent.AnonymousClass2 */

            public void run() {
                DJIVersionBaseComponent.this.startGetVersion();
            }
        };
        requestGetVersionDelay();
    }

    public void uninit() {
        if (this.handler != null) {
            this.handler.removeMessages(1);
            this.handler = null;
        }
        this.getVersionRunnable = null;
        DJIEventBusUtil.unRegister(this);
    }

    public String getComponentVersion() {
        return this.componentVersion;
    }

    public String getDJIDeviceVersionListStr() {
        ArrayList<DJIDeviceVersion> list = getLocalDJIDeviceVersionList();
        if (list == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        builder.append("---------------local---------------\n");
        Iterator<DJIDeviceVersion> it2 = list.iterator();
        while (it2.hasNext()) {
            builder.append(it2.next().toString() + "\n");
        }
        builder.append("------------------------------\n");
        return builder.toString();
    }

    public void testStartGetVersion() {
        startGetVersion();
    }

    /* access modifiers changed from: protected */
    public void requestGetVersionDelay() {
        if (this.handler != null) {
            this.handler.removeMessages(1);
            this.handler.sendEmptyMessageDelayed(1, 3000);
        }
    }

    /* access modifiers changed from: protected */
    public boolean isCfgNeedVerify() {
        return true;
    }

    /* access modifiers changed from: protected */
    public DeviceType getDeviceType() {
        return DeviceType.DM368;
    }

    /* access modifiers changed from: protected */
    public void startGetVersion() {
        log(TAG + getComponentName() + " startGetVersion 1", false);
        if (!this.isGettingVersion) {
            log("startGetVersion 2");
            log(TAG + getComponentName() + " startGetVersion 3", false);
            this.isGettingVersion = true;
            new DJIDeviceVersionRequest(getFirmwareList(), new DJIDeviceVersionRequest.ResultCallBack() {
                /* class dji.internal.version.DJIVersionBaseComponent.AnonymousClass3 */

                public void onResultCallBack(boolean ret, ArrayList<DJIDeviceVersion> DJIDeviceVersionList) {
                    DJIVersionBaseComponent.this.log(DJIVersionBaseComponent.TAG + DJIVersionBaseComponent.this.getComponentName() + " startGetVersion 4", false);
                    boolean unused = DJIVersionBaseComponent.this.isGettingVersion = false;
                    ArrayList unused2 = DJIVersionBaseComponent.this.djiDeviceVersionList = DJIDeviceVersionList;
                    DJIVersionBaseComponent.this.saveToLocalDJIDeviceVersionList(DJIVersionBaseComponent.this.djiDeviceVersionList);
                    DJIVersionBaseComponent.this.log(DJIVersionBaseComponent.this.getDJIDeviceVersionListStr(), false);
                    DJIVersionBaseComponent.this.updateComponentVersion();
                }
            });
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DJIRemoteVersionInfo info) {
        updateComponentVersion();
    }

    /* access modifiers changed from: private */
    public void updateComponentVersion() {
        ArrayList<DJIDeviceVersion> curList = getLocalDJIDeviceVersionList();
        DJIModelUpgradePackList releaseModel = DJIRemoteVersionInfo.getInstance().getUpgradeConfigReleaseModel();
        DJIModelUpgradePackList brModel = DJIRemoteVersionInfo.getInstance().getUpgradeConfigBrModel();
        log(TAG + getComponentName() + " updateComponentVersion releaseModel : " + releaseModel, false);
        log(TAG + getComponentName() + " updateComponentVersion brModel : " + brModel, false);
        if (curList != null && releaseModel != null && brModel != null) {
            ArrayList<DJIModelUpgradePackList.DJIUpgradePack> releaseList = getDJIUpgradePackList(releaseModel);
            ArrayList<DJIModelUpgradePackList.DJIUpgradePack> brList = getDJIUpgradePackList(brModel);
            String componentVersion2 = null;
            String nearComponentVersion = null;
            log(getComponentName() + " updateComponentVersion 2", false);
            if (releaseList != null) {
                log(getComponentName() + " updateComponentVersion 2 size : " + releaseList.size(), false);
            }
            if (releaseList != null && releaseList.size() > 0) {
                int i = releaseList.size() - 1;
                while (true) {
                    if (i < 0) {
                        break;
                    }
                    String version = getVersion(releaseList.get(i));
                    if (version == null) {
                        log(TAG + getComponentName() + " ignore 1 index=" + i);
                    } else if (releaseList.get(i).android_ignore == 1) {
                        log(TAG + getComponentName() + " ignore 2 index=" + i);
                    } else {
                        log(TAG + getComponentName() + " releaseList version:" + version, false);
                        int ret = checkMatch(releaseList.get(i), curList);
                        log(TAG + getComponentName() + " updateComponentVersion 2 index : " + i + "; rst = " + ret, false);
                        if (ret == 0) {
                            componentVersion2 = version;
                            break;
                        } else if (ret == -1) {
                            nearComponentVersion = version;
                        } else if (ret == 1) {
                            break;
                        }
                    }
                    i--;
                }
            }
            log(getComponentName() + " updateComponentVersion 3, componentVersion: " + componentVersion2, false);
            if (componentVersion2 == null && brList != null && brList.size() > 0) {
                int i2 = brList.size() - 1;
                while (true) {
                    if (i2 < 0) {
                        break;
                    } else if (checkMatch(brList.get(i2), curList) == 0) {
                        componentVersion2 = getVersion(brList.get(i2));
                        break;
                    } else {
                        i2--;
                    }
                }
            }
            log(getComponentName() + " updateComponentVersion 4" + componentVersion2 + "," + nearComponentVersion, false);
            this.componentVersion = componentVersion2;
            if (this.componentVersion == null) {
                if (nearComponentVersion == null) {
                    this.componentVersion = DJIRCAbstraction.DEFAULT_FIRMWARE_VERSION;
                } else {
                    this.componentVersion = nearComponentVersion + "+";
                }
            }
            EventBus.getDefault().post(this);
        }
    }

    private int checkMatch(DJIModelUpgradePackList.DJIUpgradePack pack, ArrayList<DJIDeviceVersion> curList) {
        boolean isP3CA9SE = isP3CA9SE(curList);
        int ret = 0;
        Iterator<DJIDeviceVersion> it2 = curList.iterator();
        while (it2.hasNext()) {
            DJIDeviceVersion f = it2.next();
            if (!(f.version == Long.MIN_VALUE || f.version == 0)) {
                if (f.versionStr == null || !f.versionStr.equals("255.255.255.254")) {
                    try {
                        String fieldStr = "m" + f.firmware;
                        if (isP3CA9SE && f.firmware.equals("0100")) {
                            fieldStr = "m2900";
                        }
                        if (isP3CA9SE && f.firmware.equals("0101")) {
                            fieldStr = "m2901";
                        }
                        String versionStr = (String) DJIModelUpgradePackList.DJIUpgradePack.class.getField(fieldStr).get(pack);
                        if (versionStr != null) {
                            DJIDeviceVersion tmp = new DJIDeviceVersion(f.firmware, versionStr.split("&")[0]);
                            if (tmp.version != f.version) {
                                log(String.format("version:%s, firmware:%s, f %s, tmp %s", "" + getVersion(pack), f.firmware, "" + f.versionStr, "" + versionStr), false);
                            }
                            if (tmp.version > f.version) {
                                log("===== keynote " + String.format("version:%s, firmware:%s, f %s, tmp %s", "" + getVersion(pack), f.firmware, "" + f.versionStr, "" + versionStr), false);
                                return 1;
                            } else if (tmp.version < f.version) {
                                ret = -1;
                            }
                        } else {
                            continue;
                        }
                    } catch (Exception e) {
                        DJILog.e(TAG, DJILog.exceptionToString(e), new Object[0]);
                        log("===== keynote crash" + DJILog.exceptionToString(e), false);
                        return 1;
                    }
                }
            }
        }
        return ret;
    }

    private boolean isP3CA9SE(ArrayList<DJIDeviceVersion> curList) {
        if (!(this instanceof DJIVersionP3cComponent)) {
            return false;
        }
        Iterator<DJIDeviceVersion> it2 = curList.iterator();
        while (it2.hasNext()) {
            DJIDeviceVersion f = it2.next();
            if (f.firmware.equals("0100") && f.versionStr != null && Integer.valueOf(f.versionStr.split("\\.")[0]).intValue() == 2) {
                return true;
            }
        }
        return false;
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.log.DJILog.d(java.lang.String, java.lang.String, boolean, boolean):void
     arg types: [java.lang.String, java.lang.String, int, int]
     candidates:
      dji.log.DJILog.d(java.lang.String, java.lang.String, java.lang.Throwable, java.lang.Object[]):void
      dji.log.DJILog.d(java.lang.String, java.lang.String, boolean, boolean):void */
    private ArrayList<DJIDeviceVersion> getLocalDJIDeviceVersionList() {
        DJIDeviceVersionList DJIDeviceVersionList2 = null;
        String s = null;
        try {
            s = getLocalValue(this.djiDeviceVersionListSaveKey);
        } catch (Exception e) {
            DJILog.d(TAG, "saveToLocalDJIDeviceVersionList error", true, true);
            DJILog.e(TAG, DJILog.exceptionToString(e), new Object[0]);
        }
        if (!TextUtils.isEmpty(s)) {
            DJIDeviceVersionList2 = (DJIDeviceVersionList) V_JsonUtil.toBean(s, DJIDeviceVersionList.class);
        }
        if (DJIDeviceVersionList2 != null) {
            return DJIDeviceVersionList2.list;
        }
        return null;
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.log.DJILog.d(java.lang.String, java.lang.String, boolean, boolean):void
     arg types: [java.lang.String, java.lang.String, int, int]
     candidates:
      dji.log.DJILog.d(java.lang.String, java.lang.String, java.lang.Throwable, java.lang.Object[]):void
      dji.log.DJILog.d(java.lang.String, java.lang.String, boolean, boolean):void */
    /* access modifiers changed from: private */
    public void saveToLocalDJIDeviceVersionList(ArrayList<DJIDeviceVersion> list) {
        DJIDeviceVersionList DJIDeviceVersionList2 = new DJIDeviceVersionList();
        DJIDeviceVersionList2.list = list;
        try {
            setLocalValue(this.djiDeviceVersionListSaveKey, V_JsonUtil.toJson(DJIDeviceVersionList2));
        } catch (Exception e) {
            DJILog.d(TAG, "saveToLocalDJIDeviceVersionList error", true, true);
            DJILog.e(TAG, DJILog.exceptionToString(e), new Object[0]);
        }
    }

    private void log(String log) {
        DJILog.d(TAG, log, new Object[0]);
    }

    /* access modifiers changed from: private */
    public void log(String log, boolean showlog) {
        DJILogHelper.getInstance().LOGD(TAG, log, true, showlog);
    }

    private void setLocalValue(String key, String value) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        DjiSharedPreferencesManager.putString(Util.getApplication().getApplicationContext(), "DJIVersionBaseComponent_" + key, value);
    }

    private String getLocalValue(String key) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return DjiSharedPreferencesManager.getString(Util.getApplication().getApplicationContext(), "DJIVersionBaseComponent_" + key, null);
    }
}
