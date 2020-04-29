package com.billy.cc.core.component;

import android.text.TextUtils;

public abstract class BaseForwardInterceptor implements ICCInterceptor {
    /* access modifiers changed from: protected */
    public abstract String shouldForwardCC(CC cc, String str);

    public CCResult intercept(Chain chain) {
        CC cc = chain.getCC();
        String forwardComponentName = shouldForwardCC(cc, cc.getComponentName());
        if (!TextUtils.isEmpty(forwardComponentName)) {
            cc.forwardTo(forwardComponentName);
        }
        return chain.proceed();
    }
}
