package android.support.v4.os;

import android.os.Build;

public final class CancellationSignal {
    private boolean mCancelInProgress;
    private Object mCancellationSignalObj;
    private boolean mIsCanceled;
    private OnCancelListener mOnCancelListener;

    public interface OnCancelListener {
        void onCancel();
    }

    public boolean isCanceled() {
        boolean z;
        synchronized (this) {
            z = this.mIsCanceled;
        }
        return z;
    }

    public void throwIfCanceled() {
        if (isCanceled()) {
            throw new OperationCanceledException();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0017, code lost:
        if (r1 == null) goto L_0x0024;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x001d, code lost:
        if (android.os.Build.VERSION.SDK_INT < 16) goto L_0x0024;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x001f, code lost:
        ((android.os.CancellationSignal) r1).cancel();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0024, code lost:
        monitor-enter(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:?, code lost:
        r4.mCancelInProgress = false;
        notifyAll();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x002b, code lost:
        monitor-exit(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0033, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0034, code lost:
        monitor-enter(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:?, code lost:
        r4.mCancelInProgress = false;
        notifyAll();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x003c, code lost:
        throw r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0012, code lost:
        if (r0 == null) goto L_0x0017;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:?, code lost:
        r0.onCancel();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void cancel() {
        /*
            r4 = this;
            monitor-enter(r4)
            boolean r2 = r4.mIsCanceled     // Catch:{ all -> 0x0030 }
            if (r2 == 0) goto L_0x0007
            monitor-exit(r4)     // Catch:{ all -> 0x0030 }
        L_0x0006:
            return
        L_0x0007:
            r2 = 1
            r4.mIsCanceled = r2     // Catch:{ all -> 0x0030 }
            r2 = 1
            r4.mCancelInProgress = r2     // Catch:{ all -> 0x0030 }
            android.support.v4.os.CancellationSignal$OnCancelListener r0 = r4.mOnCancelListener     // Catch:{ all -> 0x0030 }
            java.lang.Object r1 = r4.mCancellationSignalObj     // Catch:{ all -> 0x0030 }
            monitor-exit(r4)     // Catch:{ all -> 0x0030 }
            if (r0 == 0) goto L_0x0017
            r0.onCancel()     // Catch:{ all -> 0x0033 }
        L_0x0017:
            if (r1 == 0) goto L_0x0024
            int r2 = android.os.Build.VERSION.SDK_INT     // Catch:{ all -> 0x0033 }
            r3 = 16
            if (r2 < r3) goto L_0x0024
            android.os.CancellationSignal r1 = (android.os.CancellationSignal) r1     // Catch:{ all -> 0x0033 }
            r1.cancel()     // Catch:{ all -> 0x0033 }
        L_0x0024:
            monitor-enter(r4)
            r2 = 0
            r4.mCancelInProgress = r2     // Catch:{ all -> 0x002d }
            r4.notifyAll()     // Catch:{ all -> 0x002d }
            monitor-exit(r4)     // Catch:{ all -> 0x002d }
            goto L_0x0006
        L_0x002d:
            r2 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x002d }
            throw r2
        L_0x0030:
            r2 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x0030 }
            throw r2
        L_0x0033:
            r2 = move-exception
            monitor-enter(r4)
            r3 = 0
            r4.mCancelInProgress = r3     // Catch:{ all -> 0x003d }
            r4.notifyAll()     // Catch:{ all -> 0x003d }
            monitor-exit(r4)     // Catch:{ all -> 0x003d }
            throw r2
        L_0x003d:
            r2 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x003d }
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.os.CancellationSignal.cancel():void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setOnCancelListener(android.support.v4.os.CancellationSignal.OnCancelListener r2) {
        /*
            r1 = this;
            monitor-enter(r1)
            r1.waitForCancelFinishedLocked()     // Catch:{ all -> 0x0014 }
            android.support.v4.os.CancellationSignal$OnCancelListener r0 = r1.mOnCancelListener     // Catch:{ all -> 0x0014 }
            if (r0 != r2) goto L_0x000a
            monitor-exit(r1)     // Catch:{ all -> 0x0014 }
        L_0x0009:
            return
        L_0x000a:
            r1.mOnCancelListener = r2     // Catch:{ all -> 0x0014 }
            boolean r0 = r1.mIsCanceled     // Catch:{ all -> 0x0014 }
            if (r0 == 0) goto L_0x0012
            if (r2 != 0) goto L_0x0017
        L_0x0012:
            monitor-exit(r1)     // Catch:{ all -> 0x0014 }
            goto L_0x0009
        L_0x0014:
            r0 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x0014 }
            throw r0
        L_0x0017:
            monitor-exit(r1)     // Catch:{ all -> 0x0014 }
            r2.onCancel()
            goto L_0x0009
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.os.CancellationSignal.setOnCancelListener(android.support.v4.os.CancellationSignal$OnCancelListener):void");
    }

    public Object getCancellationSignalObject() {
        Object obj;
        if (Build.VERSION.SDK_INT < 16) {
            return null;
        }
        synchronized (this) {
            if (this.mCancellationSignalObj == null) {
                this.mCancellationSignalObj = new android.os.CancellationSignal();
                if (this.mIsCanceled) {
                    ((android.os.CancellationSignal) this.mCancellationSignalObj).cancel();
                }
            }
            obj = this.mCancellationSignalObj;
        }
        return obj;
    }

    private void waitForCancelFinishedLocked() {
        while (this.mCancelInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
    }
}
