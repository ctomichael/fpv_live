package com.billy.cc.core.component;

import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.os.RemoteException;
import com.billy.cc.core.component.remote.IRemoteCCService;
import com.billy.cc.core.component.remote.IRemoteCallback;
import com.billy.cc.core.component.remote.RemoteCC;
import com.billy.cc.core.component.remote.RemoteCCResult;
import com.billy.cc.core.component.remote.RemoteCursor;
import com.billy.cc.core.component.remote.RemoteProvider;
import java.util.concurrent.ConcurrentHashMap;

public class RemoteCCService extends IRemoteCCService.Stub {
    private static final ConcurrentHashMap<String, IRemoteCCService> CACHE = new ConcurrentHashMap<>();
    private static final byte[] LOCK = new byte[0];
    private Handler mainThreadHandler;

    private static class RemoteCCServiceHolder {
        /* access modifiers changed from: private */
        public static final RemoteCCService INSTANCE = new RemoteCCService();

        private RemoteCCServiceHolder() {
        }
    }

    private RemoteCCService() {
        this.mainThreadHandler = new Handler(Looper.getMainLooper());
    }

    public static RemoteCCService getInstance() {
        return RemoteCCServiceHolder.INSTANCE;
    }

    public void call(RemoteCC remoteCC, final IRemoteCallback callback) throws RemoteException {
        if (!isInvalidate()) {
            String componentName = remoteCC.getComponentName();
            final String callId = remoteCC.getCallId();
            if (CC.VERBOSE_LOG) {
                CC.verboseLog(callId, "receive call from other process. RemoteCC: %s", remoteCC.toString());
            }
            if (!ComponentManager.hasComponent(componentName)) {
                CC.verboseLog(callId, "There is no component found for name:%s in process:%s", componentName, CCUtil.getCurProcessName());
                doCallback(callback, callId, CCResult.error(-5));
                return;
            }
            final CC cc = CC.obtainBuilder(componentName).setActionName(remoteCC.getActionName()).setParams(remoteCC.getParams()).setCallId(remoteCC.getCallId()).withoutGlobalInterceptor().build();
            if (remoteCC.isMainThreadSyncCall()) {
                this.mainThreadHandler.post(new Runnable() {
                    /* class com.billy.cc.core.component.RemoteCCService.AnonymousClass1 */

                    public void run() {
                        RemoteCCService.doCallback(callback, callId, cc.call());
                    }
                });
            } else {
                cc.callAsync(new IComponentCallback() {
                    /* class com.billy.cc.core.component.RemoteCCService.AnonymousClass2 */

                    public void onResult(CC cc, CCResult result) {
                        RemoteCCService.doCallback(callback, callId, result);
                    }
                });
            }
        }
    }

    private boolean isInvalidate() {
        return !CC.isRemoteCCEnabled() && getCallingUid() != Process.myUid();
    }

    /* access modifiers changed from: private */
    public static void doCallback(IRemoteCallback callback, String callId, CCResult ccResult) {
        RemoteCCResult remoteCCResult;
        try {
            remoteCCResult = new RemoteCCResult(ccResult);
            if (CC.VERBOSE_LOG) {
                CC.verboseLog(callId, "callback to other process. RemoteCCResult: %s", remoteCCResult.toString());
            }
        } catch (Exception e) {
            remoteCCResult = new RemoteCCResult(CCResult.error(-11));
            if (CC.VERBOSE_LOG) {
                CC.verboseLog(callId, "remote CC success. But result can not be converted for IPC. RemoteCCResult: %s", remoteCCResult.toString());
            }
        }
        try {
            callback.callback(remoteCCResult);
        } catch (RemoteException e2) {
            e2.printStackTrace();
            CC.verboseLog(callId, "remote doCallback failed!", new Object[0]);
        }
    }

    public void cancel(String callId) throws RemoteException {
        if (!isInvalidate()) {
            CC.cancel(callId);
        }
    }

    public void timeout(String callId) throws RemoteException {
        if (!isInvalidate()) {
            CC.timeout(callId);
        }
    }

    public String getComponentProcessName(String componentName) throws RemoteException {
        if (isInvalidate()) {
            return null;
        }
        return ComponentManager.getComponentProcessName(componentName);
    }

    private static Uri getDispatcherProviderUri(String processName) {
        return Uri.parse("content://" + processName + "." + RemoteProvider.URI_SUFFIX + "/cc");
    }

    static IRemoteCCService get(String processNameTo) {
        IRemoteCCService service = CACHE.get(processNameTo);
        if (service == null && CC.getApplication() != null) {
            synchronized (LOCK) {
                service = CACHE.get(processNameTo);
                if (service == null && (service = getService(processNameTo)) != null) {
                    CACHE.put(processNameTo, service);
                }
            }
        }
        return service;
    }

    static void remove(String processName) {
        CACHE.remove(processName);
    }

    private static IRemoteCCService getService(String processNameTo) {
        Cursor cursor = null;
        try {
            cursor = CC.getApplication().getContentResolver().query(getDispatcherProviderUri(processNameTo), RemoteProvider.PROJECTION_MAIN, null, null, null);
            if (cursor == null) {
                if (cursor != null) {
                    try {
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
            IRemoteCCService remoteCCService = RemoteCursor.getRemoteCCService(cursor);
            if (cursor == null) {
                return remoteCCService;
            }
            try {
                cursor.close();
                return remoteCCService;
            } catch (Exception e2) {
                e2.printStackTrace();
                return remoteCCService;
            }
        } finally {
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (Exception e3) {
                    e3.printStackTrace();
                }
            }
        }
    }
}
