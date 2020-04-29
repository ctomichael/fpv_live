package dji.thirdparty.rx.internal.operators;

import dji.thirdparty.rx.Notification;
import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Producer;
import dji.thirdparty.rx.Subscriber;
import dji.thirdparty.rx.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicLong;
import kotlin.jvm.internal.LongCompanionObject;

public final class OperatorMaterialize<T> implements Observable.Operator<Notification<T>, T> {
    public /* bridge */ /* synthetic */ Object call(Object x0) {
        return call((Subscriber) ((Subscriber) x0));
    }

    private static final class Holder {
        static final OperatorMaterialize<Object> INSTANCE = new OperatorMaterialize<>();

        private Holder() {
        }
    }

    public static <T> OperatorMaterialize<T> instance() {
        return Holder.INSTANCE;
    }

    OperatorMaterialize() {
    }

    public Subscriber<? super T> call(Subscriber<? super Notification<T>> child) {
        final ParentSubscriber<T> parent = new ParentSubscriber<>(child);
        child.add(parent);
        child.setProducer(new Producer() {
            /* class dji.thirdparty.rx.internal.operators.OperatorMaterialize.AnonymousClass1 */

            public void request(long n) {
                if (n > 0) {
                    parent.requestMore(n);
                }
            }
        });
        return parent;
    }

    private static class ParentSubscriber<T> extends Subscriber<T> {
        private boolean busy = false;
        private final Subscriber<? super Notification<T>> child;
        private boolean missed = false;
        private final AtomicLong requested = new AtomicLong();
        private volatile Notification<T> terminalNotification;

        ParentSubscriber(Subscriber<? super Notification<T>> child2) {
            this.child = child2;
        }

        public void onStart() {
            request(0);
        }

        /* access modifiers changed from: package-private */
        public void requestMore(long n) {
            BackpressureUtils.getAndAddRequest(this.requested, n);
            request(n);
            drain();
        }

        public void onCompleted() {
            this.terminalNotification = Notification.createOnCompleted();
            drain();
        }

        public void onError(Throwable e) {
            this.terminalNotification = Notification.createOnError(e);
            RxJavaPlugins.getInstance().getErrorHandler().handleError(e);
            drain();
        }

        public void onNext(T t) {
            this.child.onNext(Notification.createOnNext(t));
            decrementRequested();
        }

        private void decrementRequested() {
            long r;
            AtomicLong localRequested = this.requested;
            do {
                r = localRequested.get();
                if (r == LongCompanionObject.MAX_VALUE) {
                    return;
                }
            } while (!localRequested.compareAndSet(r, r - 1));
        }

        /* JADX WARNING: Code restructure failed: missing block: B:10:0x0015, code lost:
            r1 = r6.terminalNotification;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:11:0x0017, code lost:
            if (r1 == null) goto L_0x003c;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:13:0x0021, code lost:
            if (r0.get() <= 0) goto L_0x003c;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:14:0x0023, code lost:
            r6.terminalNotification = null;
            r6.child.onNext(r1);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:15:0x0031, code lost:
            if (r6.child.isUnsubscribed() != false) goto L_?;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:16:0x0033, code lost:
            r6.child.onCompleted();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:21:0x003c, code lost:
            monitor-enter(r6);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:24:0x003f, code lost:
            if (r6.missed != false) goto L_0x0049;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:25:0x0041, code lost:
            r6.busy = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:26:0x0044, code lost:
            monitor-exit(r6);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:32:?, code lost:
            monitor-exit(r6);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:40:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:41:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:42:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:43:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:7:0x000b, code lost:
            r0 = r6.requested;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:9:0x0013, code lost:
            if (r6.child.isUnsubscribed() != false) goto L_0x0009;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private void drain() {
            /*
                r6 = this;
                monitor-enter(r6)
                boolean r2 = r6.busy     // Catch:{ all -> 0x0039 }
                if (r2 == 0) goto L_0x000a
                r2 = 1
                r6.missed = r2     // Catch:{ all -> 0x0039 }
                monitor-exit(r6)     // Catch:{ all -> 0x0039 }
            L_0x0009:
                return
            L_0x000a:
                monitor-exit(r6)     // Catch:{ all -> 0x0039 }
                java.util.concurrent.atomic.AtomicLong r0 = r6.requested
            L_0x000d:
                dji.thirdparty.rx.Subscriber<? super dji.thirdparty.rx.Notification<T>> r2 = r6.child
                boolean r2 = r2.isUnsubscribed()
                if (r2 != 0) goto L_0x0009
                dji.thirdparty.rx.Notification<T> r1 = r6.terminalNotification
                if (r1 == 0) goto L_0x003c
                long r2 = r0.get()
                r4 = 0
                int r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
                if (r2 <= 0) goto L_0x003c
                r2 = 0
                r6.terminalNotification = r2
                dji.thirdparty.rx.Subscriber<? super dji.thirdparty.rx.Notification<T>> r2 = r6.child
                r2.onNext(r1)
                dji.thirdparty.rx.Subscriber<? super dji.thirdparty.rx.Notification<T>> r2 = r6.child
                boolean r2 = r2.isUnsubscribed()
                if (r2 != 0) goto L_0x0009
                dji.thirdparty.rx.Subscriber<? super dji.thirdparty.rx.Notification<T>> r2 = r6.child
                r2.onCompleted()
                goto L_0x0009
            L_0x0039:
                r2 = move-exception
                monitor-exit(r6)     // Catch:{ all -> 0x0039 }
                throw r2
            L_0x003c:
                monitor-enter(r6)
                boolean r2 = r6.missed     // Catch:{ all -> 0x0046 }
                if (r2 != 0) goto L_0x0049
                r2 = 0
                r6.busy = r2     // Catch:{ all -> 0x0046 }
                monitor-exit(r6)     // Catch:{ all -> 0x0046 }
                goto L_0x0009
            L_0x0046:
                r2 = move-exception
                monitor-exit(r6)     // Catch:{ all -> 0x0046 }
                throw r2
            L_0x0049:
                monitor-exit(r6)     // Catch:{ all -> 0x0046 }
                goto L_0x000d
            */
            throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.internal.operators.OperatorMaterialize.ParentSubscriber.drain():void");
        }
    }
}
