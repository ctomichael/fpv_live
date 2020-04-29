package com.billy.cc.core.component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Chain {
    private final CC cc;
    private int index;
    private final List<ICCInterceptor> interceptors = new ArrayList();

    Chain(CC cc2) {
        this.cc = cc2;
        this.index = 0;
    }

    /* access modifiers changed from: package-private */
    public void addInterceptors(Collection<? extends ICCInterceptor> interceptors2) {
        if (interceptors2 != null && !interceptors2.isEmpty()) {
            this.interceptors.addAll(interceptors2);
        }
    }

    /* access modifiers changed from: package-private */
    public void addInterceptor(ICCInterceptor interceptor) {
        if (interceptor != null) {
            this.interceptors.add(interceptor);
        }
    }

    public CCResult proceed() {
        CCResult result;
        if (this.index >= this.interceptors.size()) {
            return CCResult.defaultNullResult();
        }
        List<ICCInterceptor> list = this.interceptors;
        int i = this.index;
        this.index = i + 1;
        ICCInterceptor interceptor = list.get(i);
        if (interceptor == null) {
            return proceed();
        }
        String name = interceptor.getClass().getName();
        String callId = this.cc.getCallId();
        if (this.cc.isFinished()) {
            result = this.cc.getResult();
        } else {
            if (CC.VERBOSE_LOG) {
                CC.verboseLog(callId, "start interceptor:" + name + ", cc:" + this.cc, new Object[0]);
            }
            try {
                result = interceptor.intercept(this);
            } catch (Throwable e) {
                result = CCResult.defaultExceptionResult(e);
            }
            if (CC.VERBOSE_LOG) {
                CC.verboseLog(callId, "end interceptor:" + name + ".CCResult:" + result, new Object[0]);
            }
        }
        if (result == null) {
            result = CCResult.defaultNullResult();
        }
        this.cc.setResult(result);
        return result;
    }

    public CC getCC() {
        return this.cc;
    }
}
