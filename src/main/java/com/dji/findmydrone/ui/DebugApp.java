package com.dji.findmydrone.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import com.billy.cc.core.component.CC;
import com.dji.component.persistence.DJIPersistenceStorage;
import com.dji.frame.util.V_ActivityUtil;
import com.dji.mapkit.core.Mapkit;
import com.dji.permission.Permission;
import dji.apppublic.reflect.AppPubInjectManager;
import dji.component.appstate.IAppStateService;
import dji.log.Controller;
import dji.log.DJILog;
import dji.log.DJILogConsoleConfig;
import dji.log.DJILogFileConfig;
import dji.midware.data.manager.P3.DJIFlycParamInfoManager;
import dji.midware.media.external.DJIExternalStorageController;
import dji.publics.DJIExecutor;
import dji.sdksharedlib.DJISDKSharedLib;
import dji.service.DJIAppServiceManager;
import dji.service.IDJIService;
import io.reactivex.plugins.RxJavaPlugins;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class DebugApp extends Application implements Application.ActivityLifecycleCallbacks {
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private DebugApp mApplication;

    public void onCreate() {
        super.onCreate();
        this.mApplication = this;
        CC.enableVerboseLog(true);
        initLog();
        DJIPersistenceStorage.initialize(this);
        DJISDKSharedLib.getInstance().init(this);
        DJIFlycParamInfoManager.getInstance();
        AppPubInjectManager.setAppPubToP3Injectable(new FakeInjectManager());
        createAppService();
        initAppService();
        DJIExternalStorageController.getInstance().init(this);
        Mapkit.mapboxAccessToken("pk.eyJ1IjoiZGppbWFwcyIsImEiOiJjamprM3FoYnAwMDBvM3FxanAyNnA3d2QzIn0.uCVES9vGkfNJ3sdVTySyhQ");
        registerActivityLifecycleCallbacks(this);
        innerConfig();
        configRxJavaErrorHandler();
    }

    public void onTerminate() {
        super.onTerminate();
        unregisterActivityLifecycleCallbacks(this);
    }

    private void initLog() {
        Controller controller = DJILog.newController();
        if (V_ActivityUtil.isApkDebugable(this.mApplication)) {
            controller.warn(1).priority(2);
        } else {
            controller.warn(0).priority(6).encrypt(true);
        }
        DJILog.setController(controller);
        DJILog.init(this.mApplication, new DJILogFileConfig.Builder(this.mApplication).setExecutorService(DJIExecutor.getExecutorFor(DJIExecutor.Purpose.IO)).setVersionName("0.0.1").build(), new DJILogConsoleConfig.Builder(this.mApplication).build());
    }

    private void createAppService() {
        add(new FakeMapService());
        add(new FakeAppStateService());
        add(new FakeFlyForbidService());
    }

    private void add(IDJIService service) {
        DJIAppServiceManager.getInstance().register(service.getName(), service);
    }

    private void initAppService() {
        ArrayList<IDJIService> serviceList = new ArrayList<>(DJIAppServiceManager.getInstance().getServices());
        Collections.sort(serviceList, DebugApp$$Lambda$0.$instance);
        Iterator it2 = serviceList.iterator();
        while (it2.hasNext()) {
            ((IDJIService) it2.next()).init(this.mApplication.getApplicationContext());
        }
    }

    static final /* synthetic */ int lambda$initAppService$0$DebugApp(IDJIService o1, IDJIService o2) {
        return o1.priority() - o2.priority();
    }

    public void innerConfig() {
        IAppStateService service = (IAppStateService) DJIAppServiceManager.getInstance().getService(IAppStateService.NAME);
        if (service == null) {
            return;
        }
        if (service.isInnerApp() || service.isDebugApp()) {
            try {
                Method method = Class.forName("com.dji.tools.base.InnerToolsLifeCycle").getDeclaredMethod("onApplicationCreate", new Class[0]);
                if (!method.isAccessible()) {
                    method.setAccessible(true);
                }
                method.invoke(null, new Object[0]);
            } catch (Exception ignored) {
                ignored.printStackTrace();
            }
        }
    }

    public void configRxJavaErrorHandler() {
        if (!RxJavaPlugins.isLockdown()) {
            RxJavaPlugins.setErrorHandler(new DebugApp$$Lambda$1(this));
            RxJavaPlugins.lockdown();
        }
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$configRxJavaErrorHandler$1$DebugApp(Throwable error) throws Exception {
        DJILog.e(getClass().getSimpleName(), "rxJavaErrorHandler", error, new Object[0]);
        throw new RuntimeException("rxJavaErrorHandler capture an error!!!");
    }

    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    public void onActivityStarted(Activity activity) {
    }

    public void onActivityResumed(Activity activity) {
    }

    public void onActivityPaused(Activity activity) {
    }

    public void onActivityStopped(Activity activity) {
    }

    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    public void onActivityDestroyed(Activity activity) {
    }

    public boolean checkLocationPermission(Activity activity) {
        if (ContextCompat.checkSelfPermission(this, Permission.ACCESS_FINE_LOCATION) == 0) {
            return true;
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Permission.ACCESS_FINE_LOCATION)) {
            new AlertDialog.Builder(this).setTitle("Request permission").setMessage("Fine location").setPositiveButton(17039370, new DebugApp$$Lambda$2(activity)).create().show();
            return false;
        }
        ActivityCompat.requestPermissions(activity, new String[]{Permission.ACCESS_FINE_LOCATION}, 99);
        return false;
    }
}
