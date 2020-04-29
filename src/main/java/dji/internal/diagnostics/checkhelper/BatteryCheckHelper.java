package dji.internal.diagnostics.checkhelper;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.TextUtils;
import com.dji.frame.util.V_AppUtils;
import com.dji.frame.util.V_JsonUtil;
import dji.log.DJILog;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.model.P3.DataFlycSetBatteryValidStste;
import dji.midware.natives.SDKRelativeJNI;
import dji.midware.util.ContextUtil;
import dji.midware.util.DjiSharedPreferencesManager;
import dji.midware.util.NetworkUtils;
import dji.sdksharedlib.DJISDKCache;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.keycatalog.DJISDKCacheKeys;
import dji.sdksharedlib.keycatalog.ProductKeys;
import dji.sdksharedlib.listener.DJIParamAccessListener;
import dji.sdksharedlib.store.DJISDKCacheParamValue;
import dji.thirdparty.afinal.http.AjaxCallBack;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class BatteryCheckHelper {
    private static final String GET_BAN_BATTERY_URL = SDKRelativeJNI.native_getBatteryBanSnListUrl();
    /* access modifiers changed from: private */
    public static final String SHARE_PREFERENCE_KEY = SDKRelativeJNI.native_getBatteryValidatingSPKey();
    private static final int VALIDATE_BATTERY = 0;
    private static BatteryCheckHelper instance;
    /* access modifiers changed from: private */
    public BanSN banSN;
    /* access modifiers changed from: private */
    public CountDownLatch countDownLatch;
    private String currentSN;
    /* access modifiers changed from: private */
    public boolean isDebugging = false;
    /* access modifiers changed from: private */
    public boolean isUpdateSuccess;
    /* access modifiers changed from: private */
    public List<DJIBatteryCheckListener> listenerList;
    private DJIParamAccessListener productChangeListener = new DJIParamAccessListener() {
        /* class dji.internal.diagnostics.checkhelper.BatteryCheckHelper.AnonymousClass1 */

        public void onValueChange(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
            BatteryCheckHelper.getInstance().validateBattery();
        }
    };
    private String pushSN;
    private HandlerThread validatingHT = new HandlerThread("battery validating thread");
    private Handler validatingHandler;

    public static class BanSN {
        public List<String> invalid_battery_sn_list;
        public int status;
    }

    public interface DJIBatteryCheckListener {
        void onBatteryBanListUpdate(BanSN banSN);

        void onInvalidBatteryFound();
    }

    public void setDebugging(boolean isDebugging2) {
        this.isDebugging = isDebugging2;
        if (!isDebugging2) {
            DataFlycSetBatteryValidStste.getInstance().setIsBatteryValid(true).start();
        }
    }

    public static synchronized BatteryCheckHelper getInstance() {
        BatteryCheckHelper batteryCheckHelper;
        synchronized (BatteryCheckHelper.class) {
            if (instance == null) {
                instance = new BatteryCheckHelper();
            }
            batteryCheckHelper = instance;
        }
        return batteryCheckHelper;
    }

    private void updateSN() {
        this.currentSN = (String) CacheHelper.getBattery(DJISDKCacheKeys.SERIAL_NUMBER);
    }

    /* access modifiers changed from: private */
    public String getCurrentSN() {
        if (TextUtils.isEmpty(this.currentSN)) {
            updateSN();
        }
        return this.currentSN;
    }

    private BatteryCheckHelper() {
        this.validatingHT.start();
        this.validatingHandler = new Handler(this.validatingHT.getLooper(), new Handler.Callback() {
            /* class dji.internal.diagnostics.checkhelper.BatteryCheckHelper.AnonymousClass2 */

            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        try {
                            boolean unused = BatteryCheckHelper.this.updateBanBatteryList(10);
                            if (BatteryCheckHelper.this.checkIsBatteryValid()) {
                                return true;
                            }
                            synchronized (BatteryCheckHelper.class) {
                                for (DJIBatteryCheckListener djiBatteryCheckListener : BatteryCheckHelper.this.listenerList) {
                                    djiBatteryCheckListener.onInvalidBatteryFound();
                                }
                            }
                            return true;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            return true;
                        }
                    default:
                        return true;
                }
            }
        });
        this.listenerList = new LinkedList();
        updateBanBatteryListLocally();
    }

    public void destroy() {
        this.validatingHandler.removeCallbacksAndMessages(null);
    }

    public boolean addListener(DJIBatteryCheckListener listener) {
        boolean add;
        synchronized (BatteryCheckHelper.class) {
            add = this.listenerList.add(listener);
        }
        return add;
    }

    public boolean removeListener(DJIBatteryCheckListener listener) {
        boolean remove;
        synchronized (BatteryCheckHelper.class) {
            remove = this.listenerList.remove(listener);
        }
        return remove;
    }

    public void clearListener() {
        synchronized (BatteryCheckHelper.class) {
            this.listenerList.clear();
        }
    }

    private void noticeListeners() {
        if (this.listenerList != null && this.listenerList.size() > 0) {
            synchronized (BatteryCheckHelper.class) {
                for (DJIBatteryCheckListener djiBatteryCheckListener : this.listenerList) {
                    djiBatteryCheckListener.onBatteryBanListUpdate(this.banSN);
                }
            }
        }
    }

    public boolean checkIsBatteryValid() {
        ProductType productType = DJIProductManager.getInstance().getType();
        if ((productType == ProductType.P34K || productType == ProductType.litchiS || productType == ProductType.litchiX || productType == ProductType.litchiC) && this.banSN != null && this.banSN.invalid_battery_sn_list != null && this.banSN.invalid_battery_sn_list.contains(getCurrentSN())) {
            return false;
        }
        return true;
    }

    private synchronized boolean updateBanBatteryListFromServer(int retryNum) throws InterruptedException {
        boolean z = false;
        synchronized (this) {
            if (NetworkUtils.isOnline()) {
                this.isUpdateSuccess = false;
                int i = 0;
                while (true) {
                    if (i >= retryNum) {
                        break;
                    }
                    this.countDownLatch = new CountDownLatch(1);
                    V_AppUtils.getFinalHttp().get(GET_BAN_BATTERY_URL, new AjaxCallBack<String>() {
                        /* class dji.internal.diagnostics.checkhelper.BatteryCheckHelper.AnonymousClass3 */

                        public void onStart(boolean isResume) {
                        }

                        public void onLoading(long count, long current) {
                        }

                        /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
                         method: dji.log.DJILog.d(java.lang.String, java.lang.String, boolean, boolean):void
                         arg types: [java.lang.String, java.lang.String, int, int]
                         candidates:
                          dji.log.DJILog.d(java.lang.String, java.lang.String, java.lang.Throwable, java.lang.Object[]):void
                          dji.log.DJILog.d(java.lang.String, java.lang.String, boolean, boolean):void */
                        public void onSuccess(String t) {
                            if (t != null) {
                                BanSN tempBanSN = (BanSN) V_JsonUtil.toBean(t, BanSN.class);
                                DJILog.d("BatteryCheck", "battery list json string 1: " + t, true, true);
                                if (tempBanSN != null && tempBanSN.status == 0) {
                                    if (BatteryCheckHelper.this.isDebugging) {
                                        tempBanSN.invalid_battery_sn_list.add(BatteryCheckHelper.this.getCurrentSN());
                                    }
                                    DJILog.d("BatteryCheck", "battery SN object: " + tempBanSN, true, true);
                                    DJILog.d("BatteryCheck", "battery SN list: " + (tempBanSN.invalid_battery_sn_list == null ? "Null" : tempBanSN.invalid_battery_sn_list), true, true);
                                    BanSN unused = BatteryCheckHelper.this.banSN = tempBanSN;
                                    DjiSharedPreferencesManager.putString(ContextUtil.getContext(), BatteryCheckHelper.SHARE_PREFERENCE_KEY, t);
                                    boolean unused2 = BatteryCheckHelper.this.isUpdateSuccess = true;
                                }
                                BatteryCheckHelper.this.countDownLatch.countDown();
                            }
                        }

                        public void onFailure(Throwable t, int errorNo, String strMsg) {
                            BatteryCheckHelper.this.countDownLatch.countDown();
                        }
                    });
                    this.countDownLatch.await(10, TimeUnit.SECONDS);
                    if (this.isUpdateSuccess) {
                        noticeListeners();
                        z = true;
                        break;
                    }
                    if (i < retryNum - 1) {
                        Thread.sleep(500);
                    }
                    i++;
                }
            }
        }
        return z;
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.log.DJILog.d(java.lang.String, java.lang.String, boolean, boolean):void
     arg types: [java.lang.String, java.lang.String, int, int]
     candidates:
      dji.log.DJILog.d(java.lang.String, java.lang.String, java.lang.Throwable, java.lang.Object[]):void
      dji.log.DJILog.d(java.lang.String, java.lang.String, boolean, boolean):void */
    private synchronized boolean updateBanBatteryListLocally() {
        boolean z = true;
        synchronized (this) {
            String t = DjiSharedPreferencesManager.getString(ContextUtil.getContext(), SHARE_PREFERENCE_KEY, null);
            if (t != null) {
                BanSN tempBanSN = (BanSN) V_JsonUtil.toBean(t, BanSN.class);
                DJILog.d("BatteryCheck", "battery list json string from SP: " + t, true, true);
                if (tempBanSN != null && tempBanSN.status == 0) {
                    if (this.isDebugging) {
                        tempBanSN.invalid_battery_sn_list.add(getCurrentSN());
                    }
                    DJILog.d("BatteryCheck", "battery SN object from SP: " + tempBanSN, true, true);
                    this.banSN = tempBanSN;
                    noticeListeners();
                }
            }
            z = false;
        }
        return z;
    }

    /* access modifiers changed from: private */
    public synchronized boolean updateBanBatteryList(int retryNum) throws InterruptedException {
        return updateBanBatteryListFromServer(retryNum) || updateBanBatteryListLocally();
    }

    /* access modifiers changed from: private */
    public void validateBattery() {
        if (CacheHelper.toBool(CacheHelper.getProduct(DJISDKCacheKeys.CONNECTION)) && !this.validatingHandler.hasMessages(0)) {
            this.validatingHandler.sendEmptyMessage(0);
        }
    }

    public void startListenToConnectionChange() {
        DJISDKCache.getInstance().startListeningForUpdates(new DJISDKCacheKey.Builder().component(ProductKeys.COMPONENT_KEY).paramKey(ProductKeys.MODEL_NAME).build(), this.productChangeListener, false);
    }

    public void stopListenToConnectionChange() {
        DJISDKCache.getInstance().stopListeningOnKey(new DJISDKCacheKey.Builder().component(ProductKeys.COMPONENT_KEY).paramKey(ProductKeys.MODEL_NAME).build(), this.productChangeListener);
    }
}
