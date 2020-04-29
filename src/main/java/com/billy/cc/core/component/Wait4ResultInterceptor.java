package com.billy.cc.core.component;

class Wait4ResultInterceptor implements ICCInterceptor {

    private static class Wait4ResultInterceptorHolder {
        /* access modifiers changed from: private */
        public static final Wait4ResultInterceptor INSTANCE = new Wait4ResultInterceptor();

        private Wait4ResultInterceptorHolder() {
        }
    }

    private Wait4ResultInterceptor() {
    }

    static Wait4ResultInterceptor getInstance() {
        return Wait4ResultInterceptorHolder.INSTANCE;
    }

    public CCResult intercept(Chain chain) {
        CC cc = chain.getCC();
        cc.wait4Result();
        return cc.getResult();
    }
}
