package com.billy.cc.core.component;

import android.text.TextUtils;

class ValidateInterceptor implements ICCInterceptor {

    private static class ValidateInterceptorHolder {
        /* access modifiers changed from: private */
        public static final ValidateInterceptor INSTANCE = new ValidateInterceptor();

        private ValidateInterceptorHolder() {
        }
    }

    private ValidateInterceptor() {
    }

    static ValidateInterceptor getInstance() {
        return ValidateInterceptorHolder.INSTANCE;
    }

    public CCResult intercept(Chain chain) {
        CC cc = chain.getCC();
        String componentName = cc.getComponentName();
        int code = 0;
        Boolean notFoundInCurApp = null;
        if (TextUtils.isEmpty(componentName)) {
            code = -2;
        } else if (cc.getContext() == null) {
            code = -6;
        } else if (!ComponentManager.hasComponent(componentName)) {
            notFoundInCurApp = Boolean.valueOf(TextUtils.isEmpty(ComponentManager.getComponentProcessName(componentName)));
            if (notFoundInCurApp.booleanValue() && !CC.isRemoteCCEnabled()) {
                code = -5;
                CC.verboseLog(cc.getCallId(), "componentName=" + componentName + " is not exists and CC.enableRemoteCC is " + CC.isRemoteCCEnabled(), new Object[0]);
            }
        }
        if (code != 0) {
            return CCResult.error(code);
        }
        if (ComponentManager.hasComponent(componentName)) {
            chain.addInterceptor(LocalCCInterceptor.getInstance());
        } else {
            if (notFoundInCurApp == null) {
                notFoundInCurApp = Boolean.valueOf(TextUtils.isEmpty(ComponentManager.getComponentProcessName(componentName)));
            }
            if (notFoundInCurApp.booleanValue()) {
                chain.addInterceptor(RemoteCCInterceptor.getInstance());
            } else {
                chain.addInterceptor(SubProcessCCInterceptor.getInstance());
            }
        }
        chain.addInterceptor(Wait4ResultInterceptor.getInstance());
        return chain.proceed();
    }
}
