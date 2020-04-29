package com.billy.cc.core.component;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import com.billy.cc.core.component.annotation.AllProcess;
import com.billy.cc.core.component.annotation.SubProcess;
import com.dji.findmydrone.ui.FindMyDroneUIComponent;
import dji.AppPublicComponent;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class ComponentManager {
    static final String ACTION_GET_PROCESS_NAME = "getDynamicComponentProcessName";
    static final String ACTION_REGISTER = "registerDynamicComponent";
    static final String ACTION_UNREGISTER = "unregisterDynamicComponent";
    static final ExecutorService CC_THREAD_POOL = new ThreadPoolExecutor(2, Integer.MAX_VALUE, 60, TimeUnit.SECONDS, new SynchronousQueue(), THREAD_FACTORY);
    private static final ConcurrentHashMap<String, IComponent> COMPONENTS = new ConcurrentHashMap<>();
    static final String COMPONENT_DYNAMIC_COMPONENT_OPTION = "internal.cc.dynamicComponentOption";
    /* access modifiers changed from: private */
    public static final ConcurrentHashMap<String, String> COMPONENT_PROCESS_NAMES = new ConcurrentHashMap<>();
    static final String KEY_COMPONENT_NAME = "componentName";
    static final String KEY_PROCESS_NAME = "processName";
    static final Handler MAIN_THREAD_HANDLER = new Handler(Looper.getMainLooper());
    private static final String SUB_PROCESS_SEPARATOR = ":";
    private static final ThreadFactory THREAD_FACTORY = new ThreadFactory() {
        /* class com.billy.cc.core.component.ComponentManager.AnonymousClass1 */

        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName("cc-pool-" + thread.getId());
            return thread;
        }
    };

    ComponentManager() {
    }

    static {
        registerComponent(new DynamicComponentOption());
        registerComponent(new AppPublicComponent());
        registerComponent(new FindMyDroneUIComponent());
    }

    static void init() {
    }

    static void registerComponent(IComponent component) {
        if (component != null) {
            try {
                String name = component.getName();
                if (TextUtils.isEmpty(name)) {
                    CC.logError("component " + component.getClass().getName() + " register with an empty name. abort this component.", new Object[0]);
                    return;
                }
                String processName = getComponentProcessName((Class<? extends IComponent>) component.getClass());
                COMPONENT_PROCESS_NAMES.put(name, processName);
                if (processName.equals(CCUtil.getCurProcessName())) {
                    IComponent oldComponent = COMPONENTS.put(name, component);
                    if (oldComponent != null) {
                        CC.logError("component (" + component.getClass().getName() + ") with name:" + name + " has already exists, replaced:" + oldComponent.getClass().getName(), new Object[0]);
                    } else if (CC.DEBUG) {
                        CC.log("register component success! component name = '" + name + "', class = " + component.getClass().getName(), new Object[0]);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static String getComponentProcessName(Class<? extends IComponent> componentClass) {
        if (IDynamicComponent.class.isAssignableFrom(componentClass)) {
            return CCUtil.getCurProcessName();
        }
        String packageName = CC.getApplication().getPackageName();
        String defaultProcessName = packageName;
        if (((AllProcess) componentClass.getAnnotation(AllProcess.class)) != null) {
            return CCUtil.getCurProcessName();
        }
        SubProcess subProcess = (SubProcess) componentClass.getAnnotation(SubProcess.class);
        if (subProcess == null) {
            return defaultProcessName;
        }
        String processName = subProcess.value();
        if (TextUtils.isEmpty(processName)) {
            return defaultProcessName;
        }
        if (processName.startsWith(SUB_PROCESS_SEPARATOR)) {
            return packageName + processName;
        }
        return processName;
    }

    static void unregisterComponent(IComponent component) {
        if (component != null) {
            String name = component.getName();
            if (hasComponent(name)) {
                COMPONENTS.remove(name);
            }
        }
    }

    static boolean hasComponent(String componentName) {
        return getComponentByName(componentName) != null;
    }

    static CCResult call(CC cc) {
        CCResult ccResult;
        String callId = cc.getCallId();
        Chain chain = new Chain(cc);
        if (!cc.isWithoutGlobalInterceptor()) {
            chain.addInterceptors(GlobalCCInterceptorManager.INTERCEPTORS);
        }
        chain.addInterceptors(cc.getInterceptors());
        chain.addInterceptor(ValidateInterceptor.getInstance());
        ChainProcessor processor = new ChainProcessor(chain);
        if (cc.isAsync()) {
            if (CC.VERBOSE_LOG) {
                CC.verboseLog(callId, "put into thread pool", new Object[0]);
            }
            CC_THREAD_POOL.submit(processor);
            return null;
        }
        try {
            ccResult = processor.call();
        } catch (Exception e) {
            ccResult = CCResult.defaultExceptionResult(e);
        }
        if (!CC.VERBOSE_LOG) {
            return ccResult;
        }
        CC.verboseLog(callId, "cc finished.CCResult:" + ccResult, new Object[0]);
        return ccResult;
    }

    static IComponent getComponentByName(String componentName) {
        return COMPONENTS.get(componentName);
    }

    static void mainThread(Runnable runnable) {
        MAIN_THREAD_HANDLER.post(runnable);
    }

    static void threadPool(Runnable runnable) {
        if (runnable != null) {
            CC_THREAD_POOL.execute(runnable);
        }
    }

    static String getComponentProcessName(String componentName) {
        if (TextUtils.isEmpty(componentName)) {
            return null;
        }
        String processName = COMPONENT_PROCESS_NAMES.get(componentName);
        if (!TextUtils.isEmpty(processName) || CCUtil.isMainProcess()) {
            return processName;
        }
        return (String) CC.obtainBuilder(COMPONENT_DYNAMIC_COMPONENT_OPTION).setActionName(ACTION_GET_PROCESS_NAME).addParam(KEY_COMPONENT_NAME, componentName).build().call().getDataItem(KEY_PROCESS_NAME, null);
    }

    static class DynamicComponentOption implements IComponent {
        DynamicComponentOption() {
        }

        public String getName() {
            return ComponentManager.COMPONENT_DYNAMIC_COMPONENT_OPTION;
        }

        public boolean onCall(CC cc) {
            String actionName = cc.getActionName();
            String componentName = (String) cc.getParamItem(ComponentManager.KEY_COMPONENT_NAME, null);
            String processName = (String) cc.getParamItem(ComponentManager.KEY_PROCESS_NAME, null);
            char c = 65535;
            switch (actionName.hashCode()) {
                case -348417062:
                    if (actionName.equals(ComponentManager.ACTION_UNREGISTER)) {
                        c = 1;
                        break;
                    }
                    break;
                case 1176993857:
                    if (actionName.equals(ComponentManager.ACTION_REGISTER)) {
                        c = 0;
                        break;
                    }
                    break;
                case 1851992582:
                    if (actionName.equals(ComponentManager.ACTION_GET_PROCESS_NAME)) {
                        c = 2;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    ComponentManager.COMPONENT_PROCESS_NAMES.put(componentName, processName);
                    CC.sendCCResult(cc.getCallId(), CCResult.success());
                    break;
                case 1:
                    ComponentManager.COMPONENT_PROCESS_NAMES.remove(componentName);
                    CC.sendCCResult(cc.getCallId(), CCResult.success());
                    break;
                case 2:
                    CC.sendCCResult(cc.getCallId(), CCResult.success(ComponentManager.KEY_PROCESS_NAME, (String) ComponentManager.COMPONENT_PROCESS_NAMES.get(componentName)));
                    break;
                default:
                    CC.sendCCResult(cc.getCallId(), CCResult.error("unsupported action:" + actionName));
                    break;
            }
            return false;
        }
    }
}
