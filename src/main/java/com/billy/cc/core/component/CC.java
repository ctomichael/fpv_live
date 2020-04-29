package com.billy.cc.core.component;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import com.billy.android.pools.ObjPool;
import com.billy.cc.core.component.CCMonitor;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.json.JSONObject;

@SuppressLint({"PrivateApi"})
public class CC {
    /* access modifiers changed from: private */
    public static final ObjPool<Builder, String> BUILDER_POOL = new ObjPool<Builder, String>() {
        /* class com.billy.cc.core.component.CC.AnonymousClass1 */

        /* access modifiers changed from: protected */
        public Builder newInstance(String componentName) {
            return new Builder();
        }
    };
    static boolean DEBUG = false;
    private static final long DEFAULT_TIMEOUT = 2000;
    private static boolean REMOTE_CC_ENABLED = false;
    private static final String TAG = "ComponentCaller";
    static boolean VERBOSE_LOG = false;
    private static final String VERBOSE_TAG = "ComponentCaller_VERBOSE";
    private static Application application;
    private static AtomicInteger index = new AtomicInteger(1);
    private static String prefix;
    /* access modifiers changed from: private */
    public String actionName;
    private boolean async;
    /* access modifiers changed from: private */
    public String callId;
    private IComponentCallback callback;
    private boolean callbackOnMainThread;
    WeakReference<Activity> cancelOnDestroyActivity;
    WeakReference<Fragment> cancelOnDestroyFragment;
    private volatile boolean canceled;
    /* access modifiers changed from: private */
    public String componentName;
    /* access modifiers changed from: private */
    public WeakReference<Context> context;
    private final AtomicBoolean finished;
    /* access modifiers changed from: private */
    public final List<ICCInterceptor> interceptors;
    /* access modifiers changed from: private */
    public final Map<String, Object> params;
    private volatile CCResult result;
    /* access modifiers changed from: private */
    public long timeout;
    long timeoutAt;
    private volatile boolean timeoutStatus;
    private final byte[] wait4resultLock;
    private volatile boolean waiting;
    /* access modifiers changed from: private */
    public boolean withoutGlobalInterceptor;

    static {
        Application app = CCUtil.initApplication();
        if (app != null) {
            init(app);
        }
    }

    public static synchronized void init(Application app) {
        synchronized (CC.class) {
            init(app, false, false);
        }
    }

    public static synchronized void init(Application app, boolean initComponents, boolean initGlobalInterceptors) {
        synchronized (CC.class) {
            if (application == null && app != null) {
                application = app;
                if (Build.VERSION.SDK_INT >= 14) {
                    application.registerActivityLifecycleCallbacks(new CCMonitor.ActivityMonitor());
                }
            }
            if (initComponents) {
                ComponentManager.init();
            }
            if (initGlobalInterceptors) {
                GlobalCCInterceptorManager.init();
            }
        }
    }

    private CC(String componentName2) {
        this.wait4resultLock = new byte[0];
        this.params = new HashMap();
        this.interceptors = new ArrayList();
        this.timeout = -1;
        this.finished = new AtomicBoolean(false);
        this.canceled = false;
        this.timeoutStatus = false;
        this.withoutGlobalInterceptor = false;
        this.componentName = componentName2;
    }

    public static Builder obtainBuilder(String componentName2) {
        return BUILDER_POOL.get(componentName2);
    }

    public static Application getApplication() {
        return application;
    }

    public static class Builder implements ObjPool.Resetable, ObjPool.Initable<String> {
        private CC cr;

        private Builder() {
        }

        public Builder setContext(Context context) {
            if (context != null) {
                WeakReference unused = this.cr.context = new WeakReference(context);
            }
            return this;
        }

        public Builder setNoTimeout() {
            return setTimeout(0);
        }

        public Builder setTimeout(long timeout) {
            if (timeout >= 0) {
                long unused = this.cr.timeout = timeout;
            } else {
                CC.logError("Invalid timeout value:" + timeout + ", timeout should >= 0. timeout will be set as default:" + ((long) CC.DEFAULT_TIMEOUT), new Object[0]);
            }
            return this;
        }

        public Builder setActionName(String actionName) {
            String unused = this.cr.actionName = actionName;
            return this;
        }

        public Builder withoutGlobalInterceptor() {
            boolean unused = this.cr.withoutGlobalInterceptor = true;
            return this;
        }

        public Builder setParams(Map<String, Object> params) {
            this.cr.params.clear();
            return addParams(params);
        }

        public Builder addParams(Map<String, Object> params) {
            if (params != null) {
                for (String key : params.keySet()) {
                    addParam(key, params.get(key));
                }
            }
            return this;
        }

        public Builder addParam(String key, Object value) {
            this.cr.params.put(key, value);
            return this;
        }

        public Builder addInterceptor(ICCInterceptor interceptor) {
            if (interceptor != null) {
                this.cr.interceptors.add(interceptor);
            }
            return this;
        }

        public Builder cancelOnDestroyWith(Activity activity) {
            if (activity != null) {
                this.cr.cancelOnDestroyActivity = new WeakReference<>(activity);
            }
            return this;
        }

        public Builder cancelOnDestroyWith(Fragment fragment) {
            if (fragment != null) {
                this.cr.cancelOnDestroyFragment = new WeakReference<>(fragment);
            }
            return this;
        }

        /* access modifiers changed from: package-private */
        public Builder setCallId(String callId) {
            if (!TextUtils.isEmpty(callId)) {
                String unused = this.cr.callId = callId;
            }
            return this;
        }

        public CC build() {
            CC cr2 = this.cr;
            CC.BUILDER_POOL.put(this);
            if (TextUtils.isEmpty(cr2.componentName)) {
                CC.logError("ComponentName is empty:" + cr2.toString(), new Object[0]);
            }
            return cr2;
        }

        public void reset() {
            this.cr = null;
        }

        public void init(String componentName) {
            this.cr = new CC(componentName);
        }
    }

    public String toString() {
        JSONObject json = new JSONObject();
        CCUtil.put(json, "callId", this.callId);
        CCUtil.put(json, "context", getContext());
        CCUtil.put(json, "componentName", this.componentName);
        CCUtil.put(json, "actionName", this.actionName);
        CCUtil.put(json, "timeout", Long.valueOf(this.timeout));
        CCUtil.put(json, "withoutGlobalInterceptor", Boolean.valueOf(this.withoutGlobalInterceptor));
        CCUtil.put(json, "callbackOnMainThread", Boolean.valueOf(this.callbackOnMainThread));
        CCUtil.put(json, "params", CCUtil.convertToJson(this.params));
        CCUtil.put(json, "interceptors", this.interceptors);
        CCUtil.put(json, "callback", getCallback());
        return json.toString();
    }

    public Context getContext() {
        Context context2;
        return (this.context == null || (context2 = this.context.get()) == null) ? application : context2;
    }

    /* access modifiers changed from: package-private */
    public void forwardTo(String componentName2) {
        this.componentName = componentName2;
    }

    public String getActionName() {
        return this.actionName;
    }

    public Map<String, Object> getParams() {
        return this.params;
    }

    public <T> T getParamItem(String key, T defaultValue) {
        T item = getParamItem(key);
        return item == null ? defaultValue : item;
    }

    public <T> T getParamItem(String key) {
        try {
            return this.params.get(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isAsync() {
        return this.async;
    }

    /* access modifiers changed from: package-private */
    public boolean isCallbackOnMainThread() {
        return this.callbackOnMainThread;
    }

    /* access modifiers changed from: package-private */
    public long getTimeout() {
        return this.timeout;
    }

    public String getCallId() {
        return this.callId;
    }

    /* access modifiers changed from: package-private */
    public boolean isCanceled() {
        return this.canceled;
    }

    public boolean isStopped() {
        return this.canceled || this.timeoutStatus;
    }

    /* access modifiers changed from: package-private */
    public boolean isTimeout() {
        return this.timeoutStatus;
    }

    /* access modifiers changed from: package-private */
    public boolean isWithoutGlobalInterceptor() {
        return this.withoutGlobalInterceptor;
    }

    /* access modifiers changed from: package-private */
    public CCResult getResult() {
        return this.result;
    }

    /* access modifiers changed from: package-private */
    public void setResult(CCResult result2) {
        this.finished.set(true);
        this.result = result2;
    }

    /* access modifiers changed from: package-private */
    public void setResult4Waiting(CCResult result2) {
        try {
            synchronized (this.wait4resultLock) {
                if (VERBOSE_LOG) {
                    verboseLog(this.callId, "setResult" + (this.waiting ? "4Waiting" : "") + ". CCResult:" + result2, new Object[0]);
                }
                setResult(result2);
                if (this.waiting) {
                    this.waiting = false;
                    this.wait4resultLock.notifyAll();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: package-private */
    public void wait4Result() {
        synchronized (this.wait4resultLock) {
            if (!isFinished()) {
                try {
                    verboseLog(this.callId, "start waiting for CC.sendCCResult(...)", new Object[0]);
                    this.waiting = true;
                    this.wait4resultLock.wait();
                    verboseLog(this.callId, "end waiting for CC.sendCCResult(...)", new Object[0]);
                } catch (InterruptedException e) {
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public IComponentCallback getCallback() {
        return this.callback;
    }

    /* access modifiers changed from: package-private */
    public void cancelOnDestroy(Object reason) {
        if (!isFinished()) {
            if (VERBOSE_LOG) {
                verboseLog(this.callId, "call cancel on " + reason + " destroyed", new Object[0]);
            }
            cancel();
        }
    }

    /* access modifiers changed from: package-private */
    public void addCancelOnFragmentDestroyIfSet() {
        Fragment fragment;
        FragmentManager manager;
        if (this.cancelOnDestroyFragment != null && (fragment = this.cancelOnDestroyFragment.get()) != null && (manager = fragment.getFragmentManager()) != null) {
            manager.registerFragmentLifecycleCallbacks(new CCMonitor.FragmentMonitor(this), false);
        }
    }

    public String getComponentName() {
        return this.componentName;
    }

    /* access modifiers changed from: package-private */
    public List<ICCInterceptor> getInterceptors() {
        return this.interceptors;
    }

    public String callAsync() {
        return callAsync(null);
    }

    public String callAsync(IComponentCallback callback2) {
        this.callbackOnMainThread = false;
        return processCallAsync(callback2);
    }

    public String callAsyncCallbackOnMainThread(IComponentCallback callback2) {
        this.callbackOnMainThread = true;
        return processCallAsync(callback2);
    }

    private String processCallAsync(IComponentCallback callback2) {
        if (callback2 != null) {
            this.callback = callback2;
        }
        this.async = true;
        if (this.timeout < 0) {
            this.timeout = 0;
        }
        setTimeoutAt();
        this.callId = nextCallId();
        this.canceled = false;
        this.timeoutStatus = false;
        if (VERBOSE_LOG) {
            verboseLog(this.callId, "start to callAsync:" + this, new Object[0]);
        }
        ComponentManager.call(this);
        return this.callId;
    }

    public CCResult call() {
        boolean mainThreadCallWithNoTimeout;
        this.callback = null;
        this.async = false;
        if (this.timeout == 0 && Looper.getMainLooper() == Looper.myLooper()) {
            mainThreadCallWithNoTimeout = true;
        } else {
            mainThreadCallWithNoTimeout = false;
        }
        if (mainThreadCallWithNoTimeout || this.timeout < 0) {
            this.timeout = DEFAULT_TIMEOUT;
        }
        setTimeoutAt();
        this.callId = nextCallId();
        this.canceled = false;
        this.timeoutStatus = false;
        if (VERBOSE_LOG) {
            verboseLog(this.callId, "start to call:" + this, new Object[0]);
        }
        return ComponentManager.call(this);
    }

    private void setTimeoutAt() {
        if (this.timeout > 0) {
            this.timeoutAt = System.currentTimeMillis() + this.timeout;
        } else {
            this.timeoutAt = 0;
        }
    }

    public void cancel() {
        if (markFinished()) {
            this.canceled = true;
            setResult4Waiting(CCResult.error(-8));
            verboseLog(this.callId, "call cancel()", new Object[0]);
            return;
        }
        verboseLog(this.callId, "call cancel(). but this cc is already finished", new Object[0]);
    }

    /* access modifiers changed from: package-private */
    public boolean isFinished() {
        return this.finished.get();
    }

    private boolean markFinished() {
        return this.finished.compareAndSet(false, true);
    }

    public static void cancel(String callId2) {
        verboseLog(callId2, "call CC.cancel()", new Object[0]);
        CC cc = CCMonitor.getById(callId2);
        if (cc != null) {
            cc.cancel();
        }
    }

    static void timeout(String callId2) {
        verboseLog(callId2, "call CC.cancel()", new Object[0]);
        CC cc = CCMonitor.getById(callId2);
        if (cc != null) {
            cc.timeout();
        }
    }

    /* access modifiers changed from: package-private */
    public void timeout() {
        if (markFinished()) {
            this.timeoutStatus = true;
            setResult4Waiting(CCResult.error(-9));
            verboseLog(this.callId, "timeout", new Object[0]);
            return;
        }
        verboseLog(this.callId, "call timeout(). but this cc is already finished", new Object[0]);
    }

    public boolean resultRequired() {
        return !this.async || this.callback != null;
    }

    public static void sendCCResult(String callId2, CCResult ccResult) {
        if (VERBOSE_LOG) {
            verboseLog(callId2, "CCResult received by CC.sendCCResult(...).CCResult:" + ccResult, new Object[0]);
        }
        CC cc = CCMonitor.getById(callId2);
        if (cc == null) {
            log("CCResult received, but cannot found callId:" + callId2, new Object[0]);
        } else if (cc.markFinished()) {
            if (ccResult == null) {
                ccResult = CCResult.defaultNullResult();
                logError("CC.sendCCResult called, But ccResult is null, set it to CCResult.defaultNullResult(). ComponentName=" + cc.getComponentName(), new Object[0]);
            }
            cc.setResult4Waiting(ccResult);
        } else {
            logError("CC.sendCCResult called, But ccResult is null. ComponentName=" + cc.getComponentName(), new Object[0]);
        }
    }

    @Deprecated
    public static void invokeCallback(String callId2, CCResult result2) {
        sendCCResult(callId2, result2);
    }

    public static boolean hasComponent(String componentName2) {
        return ComponentManager.hasComponent(componentName2);
    }

    public static void registerComponent(IDynamicComponent component) {
        if (component != null) {
            ComponentManager.registerComponent(component);
            String curProcessName = CCUtil.getCurProcessName();
            if (!isMainProcess()) {
                obtainBuilder("internal.cc.dynamicComponentOption").setActionName("registerDynamicComponent").addParam("componentName", component.getName()).addParam("processName", curProcessName).build().callAsync();
            }
        }
    }

    public static void unregisterComponent(IDynamicComponent component) {
        if (component != null) {
            ComponentManager.unregisterComponent(component);
            if (!isMainProcess()) {
                obtainBuilder("internal.cc.dynamicComponentOption").setActionName("unregisterDynamicComponent").addParam("componentName", component.getName()).build().callAsync();
            }
        }
    }

    public static void registerGlobalInterceptor(IGlobalCCInterceptor interceptor) {
        GlobalCCInterceptorManager.registerGlobalInterceptor(interceptor);
    }

    public static void unregisterGlobalInterceptor(Class<? extends IGlobalCCInterceptor> clazz) {
        GlobalCCInterceptorManager.unregisterGlobalInterceptor(clazz);
    }

    private String nextCallId() {
        if (!TextUtils.isEmpty(this.callId)) {
            return this.callId;
        }
        if (TextUtils.isEmpty(prefix)) {
            String curProcessName = CCUtil.getCurProcessName();
            if (TextUtils.isEmpty(curProcessName)) {
                return ":::" + index.getAndIncrement();
            }
            prefix = curProcessName + ":";
        }
        return prefix + index.getAndIncrement();
    }

    static void verboseLog(String callId2, String s, Object... args) {
        if (VERBOSE_LOG) {
            Log.i(VERBOSE_TAG, "(" + CCUtil.getCurProcessName() + ")(" + Thread.currentThread().getName() + ")" + callId2 + " >>>> " + format(s, args));
        }
    }

    public static void log(String s, Object... args) {
        if (DEBUG) {
            Log.i(TAG, format(s, args));
        }
    }

    static void logError(String s, Object... args) {
        if (DEBUG) {
            Log.e(TAG, format(s, args));
        }
    }

    private static String format(String s, Object... args) {
        if (args == null) {
            return s;
        }
        try {
            if (args.length > 0) {
                return String.format(s, args);
            }
            return s;
        } catch (Exception e) {
            e.printStackTrace();
            return s;
        }
    }

    public static void enableVerboseLog(boolean enable) {
        VERBOSE_LOG = enable;
    }

    public static void enableDebug(boolean enable) {
        DEBUG = enable;
    }

    public static void enableRemoteCC(boolean enable) {
        REMOTE_CC_ENABLED = enable;
        if (enable && application != null) {
            RemoteCCInterceptor.getInstance().enableRemoteCC();
        }
    }

    public static boolean isRemoteCCEnabled() {
        return REMOTE_CC_ENABLED;
    }

    public static boolean isMainProcess() {
        return CCUtil.isMainProcess();
    }
}
