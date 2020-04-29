package com.billy.cc.core.component;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.DeadObjectException;
import android.os.SystemClock;
import android.text.TextUtils;
import com.billy.cc.core.component.remote.IRemoteCCService;
import com.billy.cc.core.component.remote.RemoteConnection;
import dji.pilot.fpv.util.DJIFlurryReport;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class RemoteCCInterceptor extends SubProcessCCInterceptor {
    private static final String INTENT_FILTER_SCHEME = "package";
    private static final int MAX_CONNECT_TIME_DURATION = 1000;
    /* access modifiers changed from: private */
    public static final ConcurrentHashMap<String, IRemoteCCService> REMOTE_CONNECTIONS = new ConcurrentHashMap<>();

    private static class RemoteCCInterceptorHolder {
        /* access modifiers changed from: private */
        public static final RemoteCCInterceptor INSTANCE = new RemoteCCInterceptor();

        private RemoteCCInterceptorHolder() {
        }
    }

    private RemoteCCInterceptor() {
    }

    static RemoteCCInterceptor getInstance() {
        return RemoteCCInterceptorHolder.INSTANCE;
    }

    public CCResult intercept(Chain chain) {
        String processName = getProcessName(chain.getCC().getComponentName());
        if (!TextUtils.isEmpty(processName)) {
            return multiProcessCall(chain, processName, REMOTE_CONNECTIONS);
        }
        return CCResult.error(-5);
    }

    private String getProcessName(String componentName) {
        String processName = null;
        try {
            for (Map.Entry<String, IRemoteCCService> entry : REMOTE_CONNECTIONS.entrySet()) {
                try {
                    processName = ((IRemoteCCService) entry.getValue()).getComponentProcessName(componentName);
                } catch (DeadObjectException e) {
                    String processNameTo = (String) entry.getKey();
                    RemoteCCService.remove(processNameTo);
                    IRemoteCCService service = RemoteCCService.get(processNameTo);
                    if (service == null) {
                        String packageName = processNameTo.split(":")[0];
                        boolean wakeup = RemoteConnection.tryWakeup(packageName);
                        CC.log("wakeup remote app '%s'. success=%b.", packageName, Boolean.valueOf(wakeup));
                        if (wakeup) {
                            service = getMultiProcessService(processNameTo);
                        }
                    }
                    if (service != null) {
                        try {
                            processName = service.getComponentProcessName(componentName);
                            REMOTE_CONNECTIONS.put(processNameTo, service);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
                if (!TextUtils.isEmpty(processName)) {
                    String str = processName;
                    return processName;
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return processName;
    }

    /* access modifiers changed from: package-private */
    public void enableRemoteCC() {
        listenComponentApps();
        connect(RemoteConnection.scanComponentApps());
    }

    private void listenComponentApps() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.PACKAGE_ADDED");
        intentFilter.addAction("android.intent.action.PACKAGE_CHANGED");
        intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
        intentFilter.addAction("android.intent.action.MY_PACKAGE_REPLACED");
        intentFilter.addAction("android.intent.action.PACKAGE_RESTARTED");
        intentFilter.addDataScheme(INTENT_FILTER_SCHEME);
        CC.getApplication().registerReceiver(new BroadcastReceiver() {
            /* class com.billy.cc.core.component.RemoteCCInterceptor.AnonymousClass1 */

            public void onReceive(Context context, Intent intent) {
                String packageName = intent.getDataString();
                if (!TextUtils.isEmpty(packageName)) {
                    if (packageName.startsWith(RemoteCCInterceptor.INTENT_FILTER_SCHEME)) {
                        packageName = packageName.replace("package:", "");
                    }
                    String action = intent.getAction();
                    CC.log("onReceived.....pkg=" + packageName + ", action=" + action, new Object[0]);
                    if ("android.intent.action.PACKAGE_REMOVED".equals(action)) {
                        RemoteCCInterceptor.REMOTE_CONNECTIONS.remove(packageName);
                        return;
                    }
                    CC.log("start to wakeup remote app:%s", packageName);
                    if (RemoteConnection.tryWakeup(packageName)) {
                        ComponentManager.threadPool(new ConnectTask(packageName));
                    }
                }
            }
        }, intentFilter);
    }

    private void connect(List<String> packageNames) {
        if (packageNames != null && !packageNames.isEmpty()) {
            for (String pkg : packageNames) {
                ComponentManager.threadPool(new ConnectTask(pkg));
            }
        }
    }

    class ConnectTask implements Runnable {
        String packageName;

        ConnectTask(String packageName2) {
            this.packageName = packageName2;
        }

        public void run() {
            IRemoteCCService service = RemoteCCInterceptor.this.getMultiProcessService(this.packageName);
            if (service != null) {
                RemoteCCInterceptor.REMOTE_CONNECTIONS.put(this.packageName, service);
            }
        }
    }

    /* access modifiers changed from: protected */
    public IRemoteCCService getMultiProcessService(String packageName) {
        long start = System.currentTimeMillis();
        IRemoteCCService service = null;
        while (System.currentTimeMillis() - start < 1000 && (service = RemoteCCService.get(packageName)) == null) {
            SystemClock.sleep(50);
        }
        Object[] objArr = new Object[3];
        objArr[0] = packageName;
        objArr[1] = service == null ? "failed" : DJIFlurryReport.FlightRecord.V2_EVENT_FLIGHTDATA_SYNCHRONOUS_SUBKEY;
        objArr[2] = Long.valueOf(System.currentTimeMillis() - start);
        CC.log("connect remote app '%s' %s. cost time=%d", objArr);
        return service;
    }
}
