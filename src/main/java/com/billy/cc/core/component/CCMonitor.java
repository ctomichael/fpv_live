package com.billy.cc.core.component;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import kotlin.jvm.internal.LongCompanionObject;

class CCMonitor {
    static final ConcurrentHashMap<String, CC> CC_MAP = new ConcurrentHashMap<>();
    /* access modifiers changed from: private */
    public static final byte[] LOCK = new byte[0];
    /* access modifiers changed from: private */
    public static final AtomicBoolean STOPPED = new AtomicBoolean(true);
    /* access modifiers changed from: private */
    public static volatile long minTimeoutAt = LongCompanionObject.MAX_VALUE;

    CCMonitor() {
    }

    static void addMonitorFor(CC cc) {
        if (cc != null) {
            CC_MAP.put(cc.getCallId(), cc);
            cc.addCancelOnFragmentDestroyIfSet();
            long timeoutAt = cc.timeoutAt;
            if (timeoutAt > 0) {
                if (minTimeoutAt > timeoutAt) {
                    minTimeoutAt = timeoutAt;
                    synchronized (LOCK) {
                        LOCK.notifyAll();
                    }
                }
                if (STOPPED.compareAndSet(true, false)) {
                    new TimeoutMonitorThread().start();
                }
            }
            if (CC.VERBOSE_LOG) {
                CC.verboseLog(cc.getCallId(), "totalCC count=" + CC_MAP.size() + ". add monitor for:" + cc, new Object[0]);
            }
        }
    }

    static CC getById(String callId) {
        return CC_MAP.get(callId);
    }

    static void removeById(String callId) {
        CC_MAP.remove(callId);
    }

    private static class TimeoutMonitorThread extends Thread {
        private TimeoutMonitorThread() {
        }

        /*  JADX ERROR: JadxRuntimeException in pass: LoopRegionVisitor
            jadx.core.utils.exceptions.JadxRuntimeException: Incorrect nodes count for selectOther: B:23:0x0063 in [B:19:?, B:23:0x0063, B:25:0x006b, B:36:0x007c, B:37:0x007e, B:28:0x0071]
            	at jadx.core.utils.BlockUtils.selectOther(BlockUtils.java:57)
            	at jadx.core.dex.attributes.nodes.LoopInfo.getPreHeader(LoopInfo.java:76)
            	at jadx.core.dex.regions.loops.ForEachLoop.injectFakeInsns(ForEachLoop.java:29)
            	at jadx.core.dex.visitors.regions.LoopRegionVisitor.checkIterableForEach(LoopRegionVisitor.java:321)
            	at jadx.core.dex.visitors.regions.LoopRegionVisitor.processLoopRegion(LoopRegionVisitor.java:77)
            	at jadx.core.dex.visitors.regions.LoopRegionVisitor.enterRegion(LoopRegionVisitor.java:61)
            	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:63)
            	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:64)
            	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
            	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:64)
            	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:64)
            	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
            	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:64)
            	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:64)
            	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
            	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:64)
            	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:64)
            	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
            	at java.base/java.util.Collections$UnmodifiableCollection.forEach(Collections.java:1085)
            	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:64)
            	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:64)
            	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
            	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:64)
            	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:64)
            	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
            	at java.base/java.util.Collections$UnmodifiableCollection.forEach(Collections.java:1085)
            	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:64)
            	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:64)
            	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
            	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:64)
            	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:64)
            	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
            	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:64)
            	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverse(DepthRegionTraversal.java:19)
            	at jadx.core.dex.visitors.regions.LoopRegionVisitor.visit(LoopRegionVisitor.java:55)
            */
        /* JADX WARNING: CFG modification limit reached, blocks count: 141 */
        public void run() {
            /*
                r14 = this;
                java.util.concurrent.atomic.AtomicBoolean r1 = com.billy.cc.core.component.CCMonitor.STOPPED
                boolean r1 = r1.get()
                if (r1 == 0) goto L_0x000e
            L_0x000a:
                return
            L_0x000b:
                long unused = com.billy.cc.core.component.CCMonitor.minTimeoutAt = r4     // Catch:{ InterruptedException -> 0x0075 }
            L_0x000e:
                java.util.concurrent.ConcurrentHashMap<java.lang.String, com.billy.cc.core.component.CC> r1 = com.billy.cc.core.component.CCMonitor.CC_MAP
                int r1 = r1.size()
                if (r1 > 0) goto L_0x0023
                long r10 = com.billy.cc.core.component.CCMonitor.minTimeoutAt
                r12 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
                int r1 = (r10 > r12 ? 1 : (r10 == r12 ? 0 : -1))
                if (r1 != 0) goto L_0x0080
            L_0x0023:
                long r10 = com.billy.cc.core.component.CCMonitor.minTimeoutAt     // Catch:{ InterruptedException -> 0x0075 }
                long r12 = java.lang.System.currentTimeMillis()     // Catch:{ InterruptedException -> 0x0075 }
                long r2 = r10 - r12
                r10 = 0
                int r1 = (r2 > r10 ? 1 : (r2 == r10 ? 0 : -1))
                if (r1 <= 0) goto L_0x0040
                byte[] r10 = com.billy.cc.core.component.CCMonitor.LOCK     // Catch:{ InterruptedException -> 0x0075 }
                monitor-enter(r10)     // Catch:{ InterruptedException -> 0x0075 }
                byte[] r1 = com.billy.cc.core.component.CCMonitor.LOCK     // Catch:{ all -> 0x0077 }
                r1.wait(r2)     // Catch:{ all -> 0x0077 }
                monitor-exit(r10)     // Catch:{ all -> 0x0077 }
            L_0x0040:
                r4 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
                long r6 = java.lang.System.currentTimeMillis()     // Catch:{ InterruptedException -> 0x0075 }
                java.util.concurrent.ConcurrentHashMap<java.lang.String, com.billy.cc.core.component.CC> r1 = com.billy.cc.core.component.CCMonitor.CC_MAP     // Catch:{ InterruptedException -> 0x0075 }
                java.util.Collection r1 = r1.values()     // Catch:{ InterruptedException -> 0x0075 }
                java.util.Iterator r1 = r1.iterator()     // Catch:{ InterruptedException -> 0x0075 }
            L_0x0053:
                boolean r10 = r1.hasNext()     // Catch:{ InterruptedException -> 0x0075 }
                if (r10 == 0) goto L_0x000b
                java.lang.Object r0 = r1.next()     // Catch:{ InterruptedException -> 0x0075 }
                com.billy.cc.core.component.CC r0 = (com.billy.cc.core.component.CC) r0     // Catch:{ InterruptedException -> 0x0075 }
                boolean r10 = r0.isFinished()     // Catch:{ InterruptedException -> 0x0075 }
                if (r10 != 0) goto L_0x0053
                long r8 = r0.timeoutAt     // Catch:{ InterruptedException -> 0x0075 }
                r10 = 0
                int r10 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
                if (r10 <= 0) goto L_0x0053
                int r10 = (r8 > r6 ? 1 : (r8 == r6 ? 0 : -1))
                if (r10 >= 0) goto L_0x007a
                r0.timeout()     // Catch:{ InterruptedException -> 0x0075 }
                goto L_0x0053
            L_0x0075:
                r1 = move-exception
                goto L_0x000e
            L_0x0077:
                r1 = move-exception
                monitor-exit(r10)     // Catch:{ all -> 0x0077 }
                throw r1     // Catch:{ InterruptedException -> 0x0075 }
            L_0x007a:
                int r10 = (r8 > r4 ? 1 : (r8 == r4 ? 0 : -1))
                if (r10 >= 0) goto L_0x0053
                r4 = r8
                goto L_0x0053
            L_0x0080:
                java.util.concurrent.atomic.AtomicBoolean r1 = com.billy.cc.core.component.CCMonitor.STOPPED
                r10 = 1
                r1.set(r10)
                goto L_0x000a
            */
            throw new UnsupportedOperationException("Method not decompiled: com.billy.cc.core.component.CCMonitor.TimeoutMonitorThread.run():void");
        }
    }

    @TargetApi(14)
    static class ActivityMonitor implements Application.ActivityLifecycleCallbacks {
        ActivityMonitor() {
        }

        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        }

        public void onActivityStarted(Activity activity) {
        }

        public void onActivityResumed(Activity activity) {
        }

        public void onActivityPaused(Activity activity) {
        }

        public void onActivityStopped(Activity activity) {
        }

        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }

        public void onActivityDestroyed(Activity activity) {
            for (CC cc : CCMonitor.CC_MAP.values()) {
                if (!cc.isFinished() && cc.cancelOnDestroyActivity != null && cc.cancelOnDestroyActivity.get() == activity) {
                    cc.cancelOnDestroy(activity);
                }
            }
        }
    }

    static class FragmentMonitor extends FragmentManager.FragmentLifecycleCallbacks {
        WeakReference<CC> reference;

        FragmentMonitor(CC cc) {
            this.reference = new WeakReference<>(cc);
        }

        public void onFragmentDestroyed(FragmentManager fm, Fragment f) {
            CC cc;
            WeakReference<Fragment> fragReference;
            if (this.reference != null && (cc = this.reference.get()) != null && !cc.isFinished() && (fragReference = cc.cancelOnDestroyFragment) != null && f == fragReference.get()) {
                cc.cancelOnDestroy(f);
            }
        }
    }
}
