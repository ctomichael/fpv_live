package com.billy.cc.core.component;

import android.os.Looper;

class LocalCCInterceptor implements ICCInterceptor {

    private static class LocalCCInterceptorHolder {
        /* access modifiers changed from: private */
        public static final LocalCCInterceptor INSTANCE = new LocalCCInterceptor();

        private LocalCCInterceptorHolder() {
        }
    }

    private LocalCCInterceptor() {
    }

    static LocalCCInterceptor getInstance() {
        return LocalCCInterceptorHolder.INSTANCE;
    }

    public CCResult intercept(Chain chain) {
        boolean curIsMainThread;
        CC cc = chain.getCC();
        IComponent component = ComponentManager.getComponentByName(cc.getComponentName());
        if (component == null) {
            CC.verboseLog(cc.getCallId(), "component not found in this app. maybe 2 reasons:\n1. CC.enableRemoteCC changed to false\n2. Component named \"%s\" is a IDynamicComponent but now is unregistered", new Object[0]);
            return CCResult.error(-5);
        }
        try {
            String callId = cc.getCallId();
            if (CC.VERBOSE_LOG) {
                CC.verboseLog(callId, "start component:%s, cc: %s", component.getClass().getName(), cc.toString());
            }
            boolean shouldSwitchThread = false;
            LocalCCRunnable runnable = new LocalCCRunnable(cc, component);
            if (component instanceof IMainThread) {
                if (Looper.myLooper() == Looper.getMainLooper()) {
                    curIsMainThread = true;
                } else {
                    curIsMainThread = false;
                }
                Boolean runOnMainThread = ((IMainThread) component).shouldActionRunOnMainThread(cc.getActionName(), cc);
                if (runOnMainThread == null || !(runOnMainThread.booleanValue() ^ curIsMainThread)) {
                    shouldSwitchThread = false;
                } else {
                    shouldSwitchThread = true;
                }
                if (shouldSwitchThread) {
                    runnable.setShouldSwitchThread(true);
                    if (runOnMainThread.booleanValue()) {
                        ComponentManager.mainThread(runnable);
                    } else {
                        ComponentManager.threadPool(runnable);
                    }
                }
            }
            if (!shouldSwitchThread) {
                runnable.run();
            }
            if (!cc.isFinished()) {
                chain.proceed();
            }
            return cc.getResult();
        } catch (Exception e) {
            return CCResult.defaultExceptionResult(e);
        }
    }

    static class LocalCCRunnable implements Runnable {
        private final String callId;
        private CC cc;
        private IComponent component;
        private boolean shouldSwitchThread;

        LocalCCRunnable(CC cc2, IComponent component2) {
            this.cc = cc2;
            this.callId = cc2.getCallId();
            this.component = component2;
        }

        /* access modifiers changed from: package-private */
        public void setShouldSwitchThread(boolean shouldSwitchThread2) {
            this.shouldSwitchThread = shouldSwitchThread2;
        }

        public void run() {
            if (!this.cc.isFinished()) {
                try {
                    boolean callbackDelay = this.component.onCall(this.cc);
                    if (CC.VERBOSE_LOG) {
                        CC.verboseLog(this.callId, this.component.getName() + ":" + this.component.getClass().getName() + ".onCall(cc) return:" + callbackDelay, new Object[0]);
                    }
                    if (!callbackDelay && !this.cc.isFinished()) {
                        CC.logError("component.onCall(cc) return false but CC.sendCCResult(...) not called!\nmaybe: actionName error\nor if-else not call CC.sendCCResult\nor switch-case-default not call CC.sendCCResult\nor try-catch block not call CC.sendCCResult.", new Object[0]);
                        setResult(CCResult.error(-10));
                    }
                } catch (Exception e) {
                    setResult(CCResult.defaultExceptionResult(e));
                }
            }
        }

        private void setResult(CCResult result) {
            if (this.shouldSwitchThread) {
                this.cc.setResult4Waiting(result);
            } else {
                this.cc.setResult(result);
            }
        }
    }
}
