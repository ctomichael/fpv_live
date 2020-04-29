package com.billy.cc.core.component;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

class GlobalCCInterceptorManager {
    static final CopyOnWriteArrayList<IGlobalCCInterceptor> INTERCEPTORS = new CopyOnWriteArrayList<>();

    GlobalCCInterceptorManager() {
    }

    static void init() {
    }

    /* JADX WARNING: Code restructure failed: missing block: B:29:0x006c, code lost:
        if (com.billy.cc.core.component.CC.DEBUG == false) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x006e, code lost:
        com.billy.cc.core.component.CC.log("register global interceptor success! priority = " + r8.priority() + ", class = " + r0.getName(), new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static void registerGlobalInterceptor(com.billy.cc.core.component.IGlobalCCInterceptor r8) {
        /*
            r7 = 0
            if (r8 != 0) goto L_0x0010
            boolean r3 = com.billy.cc.core.component.CC.DEBUG
            if (r3 == 0) goto L_0x000f
            java.lang.String r3 = "register global interceptor is null!"
            java.lang.Object[] r4 = new java.lang.Object[r7]
            com.billy.cc.core.component.CC.logError(r3, r4)
        L_0x000f:
            return
        L_0x0010:
            java.lang.Class r0 = r8.getClass()
            java.util.concurrent.CopyOnWriteArrayList<com.billy.cc.core.component.IGlobalCCInterceptor> r4 = com.billy.cc.core.component.GlobalCCInterceptorManager.INTERCEPTORS
            monitor-enter(r4)
            r1 = 0
            java.util.concurrent.CopyOnWriteArrayList<com.billy.cc.core.component.IGlobalCCInterceptor> r3 = com.billy.cc.core.component.GlobalCCInterceptorManager.INTERCEPTORS     // Catch:{ all -> 0x0054 }
            java.util.Iterator r3 = r3.iterator()     // Catch:{ all -> 0x0054 }
        L_0x001e:
            boolean r5 = r3.hasNext()     // Catch:{ all -> 0x0054 }
            if (r5 == 0) goto L_0x0064
            java.lang.Object r2 = r3.next()     // Catch:{ all -> 0x0054 }
            com.billy.cc.core.component.IGlobalCCInterceptor r2 = (com.billy.cc.core.component.IGlobalCCInterceptor) r2     // Catch:{ all -> 0x0054 }
            java.lang.Class r5 = r2.getClass()     // Catch:{ all -> 0x0054 }
            if (r5 != r0) goto L_0x0057
            boolean r3 = com.billy.cc.core.component.CC.DEBUG     // Catch:{ all -> 0x0054 }
            if (r3 == 0) goto L_0x0052
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x0054 }
            r3.<init>()     // Catch:{ all -> 0x0054 }
            java.lang.String r5 = "duplicate global interceptor:"
            java.lang.StringBuilder r3 = r3.append(r5)     // Catch:{ all -> 0x0054 }
            java.lang.String r5 = r0.getName()     // Catch:{ all -> 0x0054 }
            java.lang.StringBuilder r3 = r3.append(r5)     // Catch:{ all -> 0x0054 }
            java.lang.String r3 = r3.toString()     // Catch:{ all -> 0x0054 }
            r5 = 0
            java.lang.Object[] r5 = new java.lang.Object[r5]     // Catch:{ all -> 0x0054 }
            com.billy.cc.core.component.CC.logError(r3, r5)     // Catch:{ all -> 0x0054 }
        L_0x0052:
            monitor-exit(r4)     // Catch:{ all -> 0x0054 }
            goto L_0x000f
        L_0x0054:
            r3 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x0054 }
            throw r3
        L_0x0057:
            int r5 = r2.priority()     // Catch:{ all -> 0x0054 }
            int r6 = r8.priority()     // Catch:{ all -> 0x0054 }
            if (r5 <= r6) goto L_0x001e
            int r1 = r1 + 1
            goto L_0x001e
        L_0x0064:
            java.util.concurrent.CopyOnWriteArrayList<com.billy.cc.core.component.IGlobalCCInterceptor> r3 = com.billy.cc.core.component.GlobalCCInterceptorManager.INTERCEPTORS     // Catch:{ all -> 0x0054 }
            r3.add(r1, r8)     // Catch:{ all -> 0x0054 }
            monitor-exit(r4)     // Catch:{ all -> 0x0054 }
            boolean r3 = com.billy.cc.core.component.CC.DEBUG
            if (r3 == 0) goto L_0x000f
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "register global interceptor success! priority = "
            java.lang.StringBuilder r3 = r3.append(r4)
            int r4 = r8.priority()
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r4 = ", class = "
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r4 = r0.getName()
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r3 = r3.toString()
            java.lang.Object[] r4 = new java.lang.Object[r7]
            com.billy.cc.core.component.CC.log(r3, r4)
            goto L_0x000f
        */
        throw new UnsupportedOperationException("Method not decompiled: com.billy.cc.core.component.GlobalCCInterceptorManager.registerGlobalInterceptor(com.billy.cc.core.component.IGlobalCCInterceptor):void");
    }

    static void unregisterGlobalInterceptor(Class<? extends IGlobalCCInterceptor> clazz) {
        synchronized (INTERCEPTORS) {
            Iterator<IGlobalCCInterceptor> it2 = INTERCEPTORS.iterator();
            while (true) {
                if (!it2.hasNext()) {
                    break;
                }
                IGlobalCCInterceptor next = it2.next();
                if (next.getClass() == clazz) {
                    INTERCEPTORS.remove(next);
                    if (CC.DEBUG) {
                        CC.log("unregister global interceptor success! class = " + clazz.getName(), new Object[0]);
                    }
                }
            }
        }
    }
}
