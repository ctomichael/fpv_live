package dji.internal.version;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import com.dji.api.UpgradeHttpApi;
import com.dji.frame.util.V_AppUtils;
import com.dji.frame.util.V_JsonUtil;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.midware.util.DjiSharedPreferencesManager;
import dji.midware.util.NetworkUtils;
import dji.sdksharedlib.util.Util;
import dji.thirdparty.afinal.FinalHttp;
import dji.thirdparty.afinal.http.AjaxCallBack;
import java.lang.reflect.InvocationTargetException;
import org.greenrobot.eventbus.EventBus;

@EXClassNullAway
public class DJIRemoteVersionInfo extends BroadcastReceiver {
    /* access modifiers changed from: private */
    public static boolean DEBUG = false;
    private static final String TAG = "DJIRemoteVersionInfo";
    private static final String keyForBrList = "br_list";
    private static final String keyForDate = "data";
    private static final String keyForList = "list";
    private FinalHttp finalHttp;
    /* access modifiers changed from: private */
    public boolean isDownloadRemote;
    /* access modifiers changed from: private */
    public String tmpDateStr;
    /* access modifiers changed from: private */
    public String tmpUpgradeConfig;
    /* access modifiers changed from: private */
    public String tmpUpgradeConfigBr;
    /* access modifiers changed from: private */
    public DJIModelUpgradePackList upgradeConfigBrModel;
    private String upgradeConfigBrUrl;
    /* access modifiers changed from: private */
    public DJIModelUpgradePackList upgradeConfigReleaseModel;
    private String upgradeConfigUrl;

    public static DJIRemoteVersionInfo getInstance() {
        return SingletonHolder.instance;
    }

    private static final class SingletonHolder {
        /* access modifiers changed from: private */
        public static final DJIRemoteVersionInfo instance = new DJIRemoteVersionInfo();

        private SingletonHolder() {
        }
    }

    private DJIRemoteVersionInfo() {
        this.upgradeConfigReleaseModel = null;
        this.upgradeConfigBrModel = null;
        this.isDownloadRemote = false;
    }

    public void init(Context ctx) {
        log("UpgradeConfigInfo init");
        DEBUG = !UpgradeHttpApi.isOfficial();
        this.upgradeConfigUrl = UpgradeHttpApi.getUpgradePilotV2Url(null);
        this.upgradeConfigBrUrl = UpgradeHttpApi.getUpgradePilotBrUrl(null);
        this.upgradeConfigReleaseModel = getLocalUpgradeConfig();
        this.upgradeConfigBrModel = getLocalUpgradeConfigBr();
        this.finalHttp = V_AppUtils.getFinalHttp(ctx);
        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        ctx.registerReceiver(this, mIntentFilter);
        getDateFile();
    }

    public void uninit(Context ctx) {
        try {
            ctx.unregisterReceiver(this);
        } catch (Exception e) {
            DJILog.e(TAG, "Receiver not existed!", new Object[0]);
        }
    }

    public DJIModelUpgradePackList getUpgradeConfigReleaseModel() {
        return this.upgradeConfigReleaseModel;
    }

    public DJIModelUpgradePackList getUpgradeConfigBrModel() {
        return this.upgradeConfigBrModel;
    }

    public void onReceive(Context context, Intent intent) {
        log("UpgradeConfigInfo onReceive");
        ConnectivityManager connectMgr = (ConnectivityManager) context.getSystemService("connectivity");
        NetworkInfo mobNetInfo = connectMgr.getNetworkInfo(0);
        NetworkInfo wifiNetInfo = connectMgr.getNetworkInfo(1);
        if ((mobNetInfo != null && mobNetInfo.isConnected()) || (wifiNetInfo != null && wifiNetInfo.isConnected())) {
            getDateFile();
        }
    }

    private void getDateFile() {
        if (NetworkUtils.isOnline()) {
            log("UpgradeConfigInfo getDateFile");
            if (!this.isDownloadRemote) {
                this.isDownloadRemote = true;
                this.finalHttp.get(UpgradeHttpApi.getGetDateUrl(), new AjaxCallBack<String>() {
                    /* class dji.internal.version.DJIRemoteVersionInfo.AnonymousClass1 */

                    public void onSuccess(String t) {
                        DJIModelUpgradeDate curDateModel = DJIRemoteVersionInfo.this.getLocalDate();
                        DJIModelUpgradeDate lastestDateModel = (DJIModelUpgradeDate) V_JsonUtil.toBean(t, DJIModelUpgradeDate.class);
                        if (DJIRemoteVersionInfo.DEBUG || curDateModel == null || lastestDateModel == null || lastestDateModel.data > curDateModel.data) {
                            String unused = DJIRemoteVersionInfo.this.tmpDateStr = t;
                            DJIRemoteVersionInfo.this.getUpgradeConfigFile();
                            return;
                        }
                        boolean unused2 = DJIRemoteVersionInfo.this.isDownloadRemote = false;
                    }

                    public void onStart(boolean isResume) {
                    }

                    public void onLoading(long count, long current) {
                    }

                    public void onFailure(Throwable t, int errorNo, String strMsg) {
                        boolean unused = DJIRemoteVersionInfo.this.isDownloadRemote = false;
                    }
                });
            }
        }
    }

    /* access modifiers changed from: protected */
    public void getUpgradeConfigFile() {
        if (NetworkUtils.isOnline()) {
            this.finalHttp.get(this.upgradeConfigUrl, new AjaxCallBack<String>() {
                /* class dji.internal.version.DJIRemoteVersionInfo.AnonymousClass2 */

                public void onSuccess(String t) {
                    String unused = DJIRemoteVersionInfo.this.tmpUpgradeConfig = t;
                    DJIRemoteVersionInfo.this.getUpgradeBrConfigFile();
                }

                public void onStart(boolean isResume) {
                }

                public void onLoading(long count, long current) {
                }

                public void onFailure(Throwable t, int errorNo, String strMsg) {
                    boolean unused = DJIRemoteVersionInfo.this.isDownloadRemote = false;
                }
            });
        }
    }

    /* access modifiers changed from: protected */
    public void getUpgradeBrConfigFile() {
        if (NetworkUtils.isOnline()) {
            this.finalHttp.get(this.upgradeConfigBrUrl, new AjaxCallBack<String>() {
                /* class dji.internal.version.DJIRemoteVersionInfo.AnonymousClass3 */

                public void onSuccess(String t) {
                    String unused = DJIRemoteVersionInfo.this.tmpUpgradeConfigBr = t;
                    DJIRemoteVersionInfo.this.setLocalValue(DJIRemoteVersionInfo.keyForDate, DJIRemoteVersionInfo.this.tmpDateStr);
                    DJIRemoteVersionInfo.this.setLocalValue(DJIRemoteVersionInfo.keyForList, DJIRemoteVersionInfo.this.tmpUpgradeConfig);
                    DJIRemoteVersionInfo.this.setLocalValue(DJIRemoteVersionInfo.keyForBrList, DJIRemoteVersionInfo.this.tmpUpgradeConfigBr);
                    DJIModelUpgradePackList unused2 = DJIRemoteVersionInfo.this.upgradeConfigReleaseModel = (DJIModelUpgradePackList) V_JsonUtil.toBean(DJIRemoteVersionInfo.this.tmpUpgradeConfig, DJIModelUpgradePackList.class);
                    DJIModelUpgradePackList unused3 = DJIRemoteVersionInfo.this.upgradeConfigBrModel = (DJIModelUpgradePackList) V_JsonUtil.toBean(DJIRemoteVersionInfo.this.tmpUpgradeConfigBr, DJIModelUpgradePackList.class);
                    EventBus.getDefault().post(DJIRemoteVersionInfo.this);
                    DJIRemoteVersionInfo.this.log("UpgradeConfigInfo getFile for net success");
                }

                public void onStart(boolean isResume) {
                }

                public void onLoading(long count, long current) {
                }

                public void onFailure(Throwable t, int errorNo, String strMsg) {
                    boolean unused = DJIRemoteVersionInfo.this.isDownloadRemote = false;
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public DJIModelUpgradeDate getLocalDate() {
        String s = getLocalValue(keyForDate);
        if (!TextUtils.isEmpty(s)) {
            return (DJIModelUpgradeDate) V_JsonUtil.toBean(s, DJIModelUpgradeDate.class);
        }
        return null;
    }

    private DJIModelUpgradePackList getLocalUpgradeConfig() {
        String s = getLocalValue(keyForList);
        if (!TextUtils.isEmpty(s)) {
            return (DJIModelUpgradePackList) V_JsonUtil.toBean(s, DJIModelUpgradePackList.class);
        }
        return null;
    }

    private DJIModelUpgradePackList getLocalUpgradeConfigBr() {
        String s = getLocalValue(keyForBrList);
        if (!TextUtils.isEmpty(s)) {
            return (DJIModelUpgradePackList) V_JsonUtil.toBean(s, DJIModelUpgradePackList.class);
        }
        return null;
    }

    /* access modifiers changed from: private */
    public void log(String log) {
        DJILog.d(TAG, log, new Object[0]);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.log.DJILog.d(java.lang.String, java.lang.String, boolean, boolean):void
     arg types: [java.lang.String, java.lang.String, int, int]
     candidates:
      dji.log.DJILog.d(java.lang.String, java.lang.String, java.lang.Throwable, java.lang.Object[]):void
      dji.log.DJILog.d(java.lang.String, java.lang.String, boolean, boolean):void */
    /* access modifiers changed from: private */
    public void setLocalValue(String key, String value) {
        try {
            DjiSharedPreferencesManager.putString(Util.getApplication().getApplicationContext(), "DJIRemoteVersionInfo_" + key, value);
        } catch (ClassNotFoundException e) {
            DJILog.d(TAG, "saveToLocalDJIDeviceVersionList error", true, true);
        } catch (NoSuchMethodException e2) {
            DJILog.d(TAG, "saveToLocalDJIDeviceVersionList error", true, true);
        } catch (InvocationTargetException e3) {
            DJILog.d(TAG, "saveToLocalDJIDeviceVersionList error", true, true);
        } catch (IllegalAccessException e4) {
            DJILog.d(TAG, "saveToLocalDJIDeviceVersionList error", true, true);
        }
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.log.DJILog.d(java.lang.String, java.lang.String, boolean, boolean):void
     arg types: [java.lang.String, java.lang.String, int, int]
     candidates:
      dji.log.DJILog.d(java.lang.String, java.lang.String, java.lang.Throwable, java.lang.Object[]):void
      dji.log.DJILog.d(java.lang.String, java.lang.String, boolean, boolean):void */
    private String getLocalValue(String key) {
        try {
            return DjiSharedPreferencesManager.getString(Util.getApplication().getApplicationContext(), "DJIRemoteVersionInfo_" + key, null);
        } catch (ClassNotFoundException e) {
            DJILog.d(TAG, "saveToLocalDJIDeviceVersionList error", true, true);
            return null;
        } catch (NoSuchMethodException e2) {
            DJILog.d(TAG, "saveToLocalDJIDeviceVersionList error", true, true);
            return null;
        } catch (InvocationTargetException e3) {
            DJILog.d(TAG, "saveToLocalDJIDeviceVersionList error", true, true);
            return null;
        } catch (IllegalAccessException e4) {
            DJILog.d(TAG, "saveToLocalDJIDeviceVersionList error", true, true);
            return null;
        }
    }
}
