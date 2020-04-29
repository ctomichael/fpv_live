package com.billy.cc.core.component;

import android.os.DeadObjectException;
import android.os.Looper;
import android.os.RemoteException;
import com.billy.cc.core.component.remote.IRemoteCCService;
import com.billy.cc.core.component.remote.IRemoteCallback;
import com.billy.cc.core.component.remote.RemoteCC;
import com.billy.cc.core.component.remote.RemoteCCResult;
import dji.pilot.fpv.util.DJIFlurryReport;
import java.util.concurrent.ConcurrentHashMap;

class SubProcessCCInterceptor implements ICCInterceptor {
    private static final ConcurrentHashMap<String, IRemoteCCService> CONNECTIONS = new ConcurrentHashMap<>();

    private static class SubProcessCCInterceptorHolder {
        /* access modifiers changed from: private */
        public static final SubProcessCCInterceptor INSTANCE = new SubProcessCCInterceptor();

        private SubProcessCCInterceptorHolder() {
        }
    }

    SubProcessCCInterceptor() {
    }

    static SubProcessCCInterceptor getInstance() {
        return SubProcessCCInterceptorHolder.INSTANCE;
    }

    public CCResult intercept(Chain chain) {
        return multiProcessCall(chain, ComponentManager.getComponentProcessName(chain.getCC().getComponentName()), CONNECTIONS);
    }

    /* access modifiers changed from: package-private */
    public CCResult multiProcessCall(Chain chain, String processName, ConcurrentHashMap<String, IRemoteCCService> connectionCache) {
        if (processName == null) {
            return CCResult.error(-5);
        }
        CC cc = chain.getCC();
        ProcessCrossTask task = new ProcessCrossTask(cc, processName, connectionCache, !cc.isAsync() && Looper.getMainLooper() == Looper.myLooper());
        ComponentManager.threadPool(task);
        if (!cc.isFinished()) {
            chain.proceed();
            if (cc.isCanceled()) {
                task.cancel();
            } else if (cc.isTimeout()) {
                task.timeout();
            }
        }
        return cc.getResult();
    }

    /* access modifiers changed from: protected */
    public IRemoteCCService getMultiProcessService(String processName) {
        CC.log("start to get RemoteService from process %s", processName);
        IRemoteCCService service = RemoteCCService.get(processName);
        Object[] objArr = new Object[2];
        objArr[0] = processName;
        objArr[1] = service != null ? DJIFlurryReport.FlightRecord.V2_EVENT_FLIGHTDATA_SYNCHRONOUS_SUBKEY : "failed";
        CC.log("get RemoteService from process %s %s!", objArr);
        return service;
    }

    class ProcessCrossTask implements Runnable {
        /* access modifiers changed from: private */
        public final CC cc;
        private final ConcurrentHashMap<String, IRemoteCCService> connectionCache;
        private final boolean isMainThreadSyncCall;
        /* access modifiers changed from: private */
        public final String processName;
        private IRemoteCCService service;

        ProcessCrossTask(CC cc2, String processName2, ConcurrentHashMap<String, IRemoteCCService> connectionCache2, boolean isMainThreadSyncCall2) {
            this.cc = cc2;
            this.processName = processName2;
            this.connectionCache = connectionCache2;
            this.isMainThreadSyncCall = isMainThreadSyncCall2;
        }

        public void run() {
            call(new RemoteCC(this.cc, this.isMainThreadSyncCall));
        }

        private void call(RemoteCC remoteCC) {
            try {
                this.service = this.connectionCache.get(this.processName);
                if (this.service == null) {
                    this.service = SubProcessCCInterceptor.this.getMultiProcessService(this.processName);
                    if (this.service != null) {
                        this.connectionCache.put(this.processName, this.service);
                    }
                }
                if (this.cc.isFinished()) {
                    CC.verboseLog(this.cc.getCallId(), "cc is finished before call %s process", this.processName);
                } else if (this.service == null) {
                    CC.verboseLog(this.cc.getCallId(), "RemoteService is not found for process: %s", this.processName);
                    setResult(CCResult.error(-5));
                } else {
                    if (CC.VERBOSE_LOG) {
                        CC.verboseLog(this.cc.getCallId(), "start to call process:%s, RemoteCC: %s", this.processName, remoteCC.toString());
                    }
                    this.service.call(remoteCC, new IRemoteCallback.Stub() {
                        /* class com.billy.cc.core.component.SubProcessCCInterceptor.ProcessCrossTask.AnonymousClass1 */

                        public void callback(RemoteCCResult remoteCCResult) throws RemoteException {
                            try {
                                if (CC.VERBOSE_LOG) {
                                    CC.verboseLog(ProcessCrossTask.this.cc.getCallId(), "receive RemoteCCResult from process:%s, RemoteCCResult: %s", ProcessCrossTask.this.processName, remoteCCResult.toString());
                                }
                                ProcessCrossTask.this.setResult(remoteCCResult.toCCResult());
                            } catch (Exception e) {
                                e.printStackTrace();
                                ProcessCrossTask.this.setResult(CCResult.error(-11));
                            }
                        }
                    });
                }
            } catch (DeadObjectException e) {
                this.connectionCache.remove(this.processName);
                call(remoteCC);
            } catch (Exception e2) {
                e2.printStackTrace();
                setResult(CCResult.error(-11));
            }
        }

        /* access modifiers changed from: package-private */
        public void setResult(CCResult result) {
            this.cc.setResult4Waiting(result);
        }

        /* access modifiers changed from: package-private */
        public void cancel() {
            try {
                this.service.cancel(this.cc.getCallId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /* access modifiers changed from: package-private */
        public void timeout() {
            try {
                this.service.timeout(this.cc.getCallId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
