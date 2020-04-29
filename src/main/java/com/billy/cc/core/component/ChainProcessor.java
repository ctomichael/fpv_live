package com.billy.cc.core.component;

import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;

class ChainProcessor implements Callable<CCResult> {
    private final Chain chain;

    ChainProcessor(Chain chain2) {
        this.chain = chain2;
    }

    public CCResult call() throws Exception {
        CCResult result;
        CC cc = this.chain.getCC();
        String callId = cc.getCallId();
        CCMonitor.addMonitorFor(cc);
        try {
            if (CC.VERBOSE_LOG) {
                CC.verboseLog(callId, "process cc at thread:" + Thread.currentThread().getName() + ", pool size=" + ((ThreadPoolExecutor) ComponentManager.CC_THREAD_POOL).getPoolSize(), new Object[0]);
            }
            if (cc.isFinished()) {
                result = cc.getResult();
            } else {
                try {
                    CC.verboseLog(callId, "start interceptor chain", new Object[0]);
                    result = this.chain.proceed();
                    if (CC.VERBOSE_LOG) {
                        CC.verboseLog(callId, "end interceptor chain.CCResult:" + result, new Object[0]);
                    }
                } catch (Exception e) {
                    result = CCResult.defaultExceptionResult(e);
                }
            }
        } catch (Exception e2) {
            result = CCResult.defaultExceptionResult(e2);
        } finally {
            CCMonitor.removeById(callId);
        }
        if (result == null) {
            result = CCResult.defaultNullResult();
        }
        cc.setResult(null);
        performCallback(cc, result);
        return result;
    }

    private static void performCallback(CC cc, CCResult result) {
        IComponentCallback callback = cc.getCallback();
        if (CC.VERBOSE_LOG) {
            CC.verboseLog(cc.getCallId(), "perform callback:" + cc.getCallback() + ", CCResult:" + result, new Object[0]);
        }
        if (callback != null) {
            if (cc.isCallbackOnMainThread()) {
                ComponentManager.mainThread(new CallbackRunnable(callback, cc, result));
                return;
            }
            try {
                callback.onResult(cc, result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static class CallbackRunnable implements Runnable {
        private IComponentCallback callback;
        private final CC cc;
        private CCResult result;

        CallbackRunnable(IComponentCallback callback2, CC cc2, CCResult result2) {
            this.cc = cc2;
            this.callback = callback2;
            this.result = result2;
        }

        public void run() {
            try {
                this.callback.onResult(this.cc, this.result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
