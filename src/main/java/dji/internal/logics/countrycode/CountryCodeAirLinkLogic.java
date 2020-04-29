package dji.internal.logics.countrycode;

import android.content.Context;
import android.text.TextUtils;
import dji.common.error.DJIError;
import dji.common.error.DJISDKCacheError;
import dji.common.util.CallbackUtils;
import dji.component.areacode.AreaCodeStrategy;
import dji.component.areacode.DJIAreaCodeEvent;
import dji.component.areacode.DJIAreaCodeStrategyEvent;
import dji.component.areacode.IAreaCodeChangeListener;
import dji.component.areacode.IAreaCodeService;
import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.logics.CommonUtil;
import dji.midware.component.DJIComponentManager;
import dji.midware.data.manager.P3.DataCameraEvent;
import dji.midware.util.ContextUtil;
import dji.midware.util.DJIEventBusUtil;
import dji.midware.util.DjiSharedPreferencesManager;
import dji.sdksharedlib.DJISDKCache;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.extension.KeyHelper;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.keycatalog.FlightControllerKeys;
import dji.sdksharedlib.listener.DJIParamAccessListener;
import dji.sdksharedlib.listener.DJISetCallback;
import dji.sdksharedlib.store.DJISDKCacheParamValue;
import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Subscription;
import dji.thirdparty.rx.schedulers.Schedulers;
import java.util.concurrent.TimeUnit;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class CountryCodeAirLinkLogic implements IAreaCodeChangeListener {
    private static final String AUTO_SYNC_ENABLED_KEY = "DJIAutoSyncCountryCodeEnabledKey";
    private static final long DELAY_TIME_IN_MILLISECONDS = 500;
    private static final String[] FACTORY_COUNTRY_CODES = {"F2", "FF"};
    private static final String TAG = "CountryCodeAirLinkLogic";
    private static boolean isAutoSync = true;
    private boolean areMotorsOn;
    private IAreaCodeService areaCodeService;
    private Context context;
    private String countryAreaCodeCache;
    private AreaCodeStrategy countryAreaCodeStrategyCache;
    private DJIParamAccessListener motorsListener;
    /* access modifiers changed from: private */
    public String prevSetCountryCode;
    private Subscription timerSubscription;
    private Verifier verifier;

    private static class LazyHolder {
        /* access modifiers changed from: private */
        public static final CountryCodeAirLinkLogic INSTANCE = new CountryCodeAirLinkLogic();

        private LazyHolder() {
        }
    }

    private CountryCodeAirLinkLogic() {
        this.motorsListener = new CountryCodeAirLinkLogic$$Lambda$0(this);
    }

    public static CountryCodeAirLinkLogic getInstance() {
        return LazyHolder.INSTANCE;
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$new$0$CountryCodeAirLinkLogic(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
        if (newValue != null && newValue.getData() != null) {
            this.areMotorsOn = CacheHelper.toBool(newValue.getData());
            checkAndUpdateCountryCodeToAircraftIfNeeded(null);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DJIAreaCodeEvent event) {
        onAreaCodeChanged(event);
    }

    public void onAreaCodeChanged(DJIAreaCodeEvent areaCodeEvent) {
        if (shouldUpdateCountryCode(areaCodeEvent)) {
            checkAndUpdateCountryCodeToAircraftIfNeeded(null);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DJIAreaCodeStrategyEvent event) {
        onAreaCodeStrategyChanged(event);
    }

    public void onAreaCodeStrategyChanged(DJIAreaCodeStrategyEvent strategyEvent) {
        if (shouldUpdateCountryCodeSource(strategyEvent)) {
            checkAndUpdateCountryCodeToAircraftIfNeeded(null);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraEvent event) {
        if (event == DataCameraEvent.ConnectLose) {
            this.prevSetCountryCode = null;
        } else if (event == DataCameraEvent.ConnectOK) {
            checkAndUpdateCountryCodeToAircraftIfNeeded(null);
        }
    }

    public void init(Context context2) {
        this.context = context2;
        this.verifier = new Verifier();
        DJISDKCacheKey motorsKey = KeyHelper.getFlightControllerKey(FlightControllerKeys.ARE_MOTOR_ON);
        DJISDKCache.getInstance().startListeningForUpdates(motorsKey, this.motorsListener, false);
        DJISDKCacheParamValue motorValue = DJISDKCache.getInstance().getAvailableValue(motorsKey);
        if (motorValue != null) {
            this.areMotorsOn = CacheHelper.toBool(motorValue.getData());
        }
        this.areaCodeService.addAreaCodeChangeListener(this);
        this.countryAreaCodeCache = CommonUtil.getAreaCodeEvent().areaCode;
        this.countryAreaCodeStrategyCache = CommonUtil.getAreaCodeEvent().strategy;
        this.prevSetCountryCode = null;
        checkAndUpdateCountryCodeToAircraftIfNeeded(null);
    }

    public void destroy() {
        DJISDKCache.getInstance().stopListening(this.motorsListener);
        this.areaCodeService.removeAreaCodeChangeListener(this);
        stopTimer();
    }

    /* access modifiers changed from: private */
    public synchronized void startTimer(Runnable operation, long timeoutInMilliSeconds) {
        stopTimer();
        this.timerSubscription = Observable.timer(timeoutInMilliSeconds, TimeUnit.MILLISECONDS, Schedulers.computation()).subscribe(new CountryCodeAirLinkLogic$$Lambda$1(operation));
    }

    private synchronized void stopTimer() {
        if (this.timerSubscription != null && !this.timerSubscription.isUnsubscribed()) {
            this.timerSubscription.unsubscribe();
        }
    }

    private void registerEventBus() {
        DJIEventBusUtil.register(this);
    }

    private void unregisterEventBus() {
        DJIEventBusUtil.unRegister(this);
    }

    private boolean shouldUpdateCountryCode(DJIAreaCodeEvent event) {
        String areaCodeTemp = event.areaCode;
        if (this.countryAreaCodeCache.equals(areaCodeTemp)) {
            return false;
        }
        this.countryAreaCodeCache = areaCodeTemp;
        return true;
    }

    private boolean shouldUpdateCountryCodeSource(DJIAreaCodeStrategyEvent event) {
        AreaCodeStrategy areaCodeStrategy = event.strategy;
        if (this.countryAreaCodeStrategyCache.equals(areaCodeStrategy)) {
            return false;
        }
        this.countryAreaCodeStrategyCache = areaCodeStrategy;
        return true;
    }

    public boolean areMotorsOn() {
        return this.areMotorsOn;
    }

    /* access modifiers changed from: private */
    public boolean isTargetDevice() {
        DJIComponentManager.PlatformType type = DJIComponentManager.getInstance().getPlatformType();
        if (type == null) {
            return false;
        }
        if (type == DJIComponentManager.PlatformType.FoldingDrone || type == DJIComponentManager.PlatformType.WM230 || type == DJIComponentManager.PlatformType.Spark || type == DJIComponentManager.PlatformType.P4P || type == DJIComponentManager.PlatformType.P4A || type == DJIComponentManager.PlatformType.Inspire2 || DJIComponentManager.getInstance().isPlatformM200Series(type) || type == DJIComponentManager.PlatformType.WM240 || type == DJIComponentManager.PlatformType.WM245 || type == DJIComponentManager.PlatformType.WM160 || checkIsP4POrIn2SerialDevice()) {
            return true;
        }
        return false;
    }

    private boolean checkIsP4POrIn2SerialDevice() {
        DJIComponentManager.PlatformType type = DJIComponentManager.getInstance().getPlatformType();
        if (type == null) {
            return false;
        }
        if (type == DJIComponentManager.PlatformType.P4P || type == DJIComponentManager.PlatformType.P4A || type == DJIComponentManager.PlatformType.Inspire2 || DJIComponentManager.getInstance().isPlatformM200Series(type)) {
            return true;
        }
        return false;
    }

    private boolean isValidStateForSync() {
        return !this.areMotorsOn && isTargetDevice() && !TextUtils.isEmpty(this.countryAreaCodeCache) && !this.countryAreaCodeCache.equals(this.prevSetCountryCode);
    }

    public synchronized void syncCountryCodeToAircraft(final DJISDKCacheHWAbstraction.InnerCallback callBack) {
        if (!isValidStateForSync()) {
            CallbackUtils.onFailure(callBack);
        } else {
            String countryCodeToSet = this.countryAreaCodeCache;
            if (!(this.countryAreaCodeStrategyCache == AreaCodeStrategy.IP && !checkIsP4POrIn2SerialDevice() && (countryCodeToSet = this.verifier.verifyIPWithCountryCode(this.countryAreaCodeCache, this.context)) == null)) {
                final String finalCountryCodeToSet = countryCodeToSet;
                DJISDKCache.getInstance().setValue(KeyHelper.getAirLinkKey("CountryCode"), finalCountryCodeToSet, new DJISetCallback() {
                    /* class dji.internal.logics.countrycode.CountryCodeAirLinkLogic.AnonymousClass1 */

                    public void onSuccess() {
                        String unused = CountryCodeAirLinkLogic.this.prevSetCountryCode = finalCountryCodeToSet;
                        CallbackUtils.onSuccess(callBack, finalCountryCodeToSet);
                    }

                    public void onFails(DJIError error) {
                        if (error == DJIError.COMMON_TIMEOUT || (error == DJISDKCacheError.INVALID_KEY_FOR_COMPONENT && CountryCodeAirLinkLogic.this.isTargetDevice())) {
                            CountryCodeAirLinkLogic.this.startTimer(new CountryCodeAirLinkLogic$1$$Lambda$0(this, callBack), 500);
                        } else {
                            CallbackUtils.onFailure(callBack);
                        }
                    }

                    /* access modifiers changed from: package-private */
                    public final /* synthetic */ void lambda$onFails$0$CountryCodeAirLinkLogic$1(DJISDKCacheHWAbstraction.InnerCallback callBack) {
                        CountryCodeAirLinkLogic.this.syncCountryCodeToAircraft(callBack);
                    }
                });
            }
        }
    }

    public static synchronized boolean getAutoUpdateCountryCodeEnabled() {
        boolean z;
        synchronized (CountryCodeAirLinkLogic.class) {
            isAutoSync = DjiSharedPreferencesManager.getBoolean(ContextUtil.getContext(), AUTO_SYNC_ENABLED_KEY, true);
            z = isAutoSync;
        }
        return z;
    }

    public static synchronized void setAutoUpdateCountryCodeEnabled(boolean enabled) {
        synchronized (CountryCodeAirLinkLogic.class) {
            isAutoSync = enabled;
            DjiSharedPreferencesManager.putBoolean(ContextUtil.getContext(), AUTO_SYNC_ENABLED_KEY, isAutoSync);
        }
    }

    private static boolean isFactoryCountryCode(String countryCode) {
        if (!TextUtils.isEmpty(countryCode)) {
            return false;
        }
        for (String cc : FACTORY_COUNTRY_CODES) {
            if (countryCode.equals(cc)) {
                return true;
            }
        }
        return false;
    }

    public void checkAndUpdateCountryCodeToAircraftIfNeeded(DJISDKCacheHWAbstraction.InnerCallback callBack) {
        if (!isValidStateForSync()) {
            CallbackUtils.onFailure(callBack);
            return;
        }
        String countryCodeToSet = this.countryAreaCodeCache;
        if (this.countryAreaCodeStrategyCache == AreaCodeStrategy.IP && !checkIsP4POrIn2SerialDevice()) {
            countryCodeToSet = this.verifier.verifyIPWithCountryCode(this.countryAreaCodeCache, this.context);
        }
        if (TextUtils.isEmpty(countryCodeToSet)) {
            return;
        }
        if (getAutoUpdateCountryCodeEnabled() || isFactoryCountryCode(countryCodeToSet)) {
            syncCountryCodeToAircraft(callBack);
        } else {
            EventBus.getDefault().post(new CountryCodeToSetEvent(countryCodeToSet));
        }
    }

    public boolean isUpdateCountryCodeRequired() {
        if (getAutoUpdateCountryCodeEnabled() || !isValidStateForSync()) {
            return false;
        }
        String countryCodeToSet = this.countryAreaCodeCache;
        if (this.countryAreaCodeStrategyCache == AreaCodeStrategy.IP && !checkIsP4POrIn2SerialDevice()) {
            countryCodeToSet = this.verifier.verifyIPWithCountryCode(this.countryAreaCodeCache, this.context);
        }
        if (TextUtils.isEmpty(countryCodeToSet) || isFactoryCountryCode(countryCodeToSet)) {
            return false;
        }
        return true;
    }

    public static final class CountryCodeToSetEvent {
        private final String countryCode;

        CountryCodeToSetEvent(String countryCode2) {
            this.countryCode = countryCode2;
        }

        public String getCountryCode() {
            return this.countryCode;
        }
    }
}
