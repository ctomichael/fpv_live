package dji.thirdparty.rx.internal.operators;

import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Scheduler;
import dji.thirdparty.rx.Subscriber;
import dji.thirdparty.rx.functions.Action0;
import dji.thirdparty.rx.observers.SerializedSubscriber;
import dji.thirdparty.rx.subscriptions.SerialSubscription;
import java.util.concurrent.TimeUnit;
import kotlin.jvm.internal.LongCompanionObject;

public final class OperatorDebounceWithTime<T> implements Observable.Operator<T, T> {
    final Scheduler scheduler;
    final long timeout;
    final TimeUnit unit;

    public /* bridge */ /* synthetic */ Object call(Object x0) {
        return call((Subscriber) ((Subscriber) x0));
    }

    public OperatorDebounceWithTime(long timeout2, TimeUnit unit2, Scheduler scheduler2) {
        this.timeout = timeout2;
        this.unit = unit2;
        this.scheduler = scheduler2;
    }

    public Subscriber<? super T> call(Subscriber<? super T> child) {
        final Scheduler.Worker worker = this.scheduler.createWorker();
        final SerializedSubscriber<T> s = new SerializedSubscriber<>(child);
        final SerialSubscription ssub = new SerialSubscription();
        s.add(worker);
        s.add(ssub);
        return new Subscriber<T>(child) {
            /* class dji.thirdparty.rx.internal.operators.OperatorDebounceWithTime.AnonymousClass1 */
            final Subscriber<?> self = this;
            final DebounceState<T> state = new DebounceState<>();

            public void onStart() {
                request(LongCompanionObject.MAX_VALUE);
            }

            public void onNext(T t) {
                final int index = this.state.next(t);
                ssub.set(worker.schedule(new Action0() {
                    /* class dji.thirdparty.rx.internal.operators.OperatorDebounceWithTime.AnonymousClass1.AnonymousClass1 */

                    public void call() {
                        AnonymousClass1.this.state.emit(index, s, AnonymousClass1.this.self);
                    }
                }, OperatorDebounceWithTime.this.timeout, OperatorDebounceWithTime.this.unit));
            }

            public void onError(Throwable e) {
                s.onError(e);
                unsubscribe();
                this.state.clear();
            }

            public void onCompleted() {
                this.state.emitAndComplete(s, this);
            }
        };
    }

    static final class DebounceState<T> {
        boolean emitting;
        boolean hasValue;
        int index;
        boolean terminate;
        T value;

        DebounceState() {
        }

        public synchronized int next(T value2) {
            int i;
            this.value = value2;
            this.hasValue = true;
            i = this.index + 1;
            this.index = i;
            return i;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:12:?, code lost:
            r5.onNext(r1);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:13:0x001e, code lost:
            monitor-enter(r3);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:16:0x0021, code lost:
            if (r3.terminate != false) goto L_0x0033;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:17:0x0023, code lost:
            r3.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:18:0x0026, code lost:
            monitor-exit(r3);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:27:0x002e, code lost:
            r0 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:28:0x002f, code lost:
            dji.thirdparty.rx.exceptions.Exceptions.throwOrReport(r0, r6, r1);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:30:?, code lost:
            monitor-exit(r3);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:31:0x0034, code lost:
            r5.onCompleted();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:35:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:36:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:37:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:38:?, code lost:
            return;
         */
        /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void emit(int r4, dji.thirdparty.rx.Subscriber<T> r5, dji.thirdparty.rx.Subscriber<?> r6) {
            /*
                r3 = this;
                monitor-enter(r3)
                boolean r2 = r3.emitting     // Catch:{ all -> 0x002b }
                if (r2 != 0) goto L_0x000d
                boolean r2 = r3.hasValue     // Catch:{ all -> 0x002b }
                if (r2 == 0) goto L_0x000d
                int r2 = r3.index     // Catch:{ all -> 0x002b }
                if (r4 == r2) goto L_0x000f
            L_0x000d:
                monitor-exit(r3)     // Catch:{ all -> 0x002b }
            L_0x000e:
                return
            L_0x000f:
                T r1 = r3.value     // Catch:{ all -> 0x002b }
                r2 = 0
                r3.value = r2     // Catch:{ all -> 0x002b }
                r2 = 0
                r3.hasValue = r2     // Catch:{ all -> 0x002b }
                r2 = 1
                r3.emitting = r2     // Catch:{ all -> 0x002b }
                monitor-exit(r3)     // Catch:{ all -> 0x002b }
                r5.onNext(r1)     // Catch:{ Throwable -> 0x002e }
                monitor-enter(r3)
                boolean r2 = r3.terminate     // Catch:{ all -> 0x0028 }
                if (r2 != 0) goto L_0x0033
                r2 = 0
                r3.emitting = r2     // Catch:{ all -> 0x0028 }
                monitor-exit(r3)     // Catch:{ all -> 0x0028 }
                goto L_0x000e
            L_0x0028:
                r2 = move-exception
                monitor-exit(r3)     // Catch:{ all -> 0x0028 }
                throw r2
            L_0x002b:
                r2 = move-exception
                monitor-exit(r3)     // Catch:{ all -> 0x002b }
                throw r2
            L_0x002e:
                r0 = move-exception
                dji.thirdparty.rx.exceptions.Exceptions.throwOrReport(r0, r6, r1)
                goto L_0x000e
            L_0x0033:
                monitor-exit(r3)     // Catch:{ all -> 0x0028 }
                r5.onCompleted()
                goto L_0x000e
            */
            throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.internal.operators.OperatorDebounceWithTime.DebounceState.emit(int, dji.thirdparty.rx.Subscriber, dji.thirdparty.rx.Subscriber):void");
        }

        /* JADX WARNING: Code restructure failed: missing block: B:10:?, code lost:
            r5.onNext(r2);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:11:0x001d, code lost:
            r5.onCompleted();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:16:0x0024, code lost:
            r0 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:17:0x0025, code lost:
            dji.thirdparty.rx.exceptions.Exceptions.throwOrReport(r0, r6, r2);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:22:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:23:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:8:0x0018, code lost:
            if (r1 == false) goto L_0x001d;
         */
        /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void emitAndComplete(dji.thirdparty.rx.Subscriber<T> r5, dji.thirdparty.rx.Subscriber<?> r6) {
            /*
                r4 = this;
                monitor-enter(r4)
                boolean r3 = r4.emitting     // Catch:{ all -> 0x0021 }
                if (r3 == 0) goto L_0x000a
                r3 = 1
                r4.terminate = r3     // Catch:{ all -> 0x0021 }
                monitor-exit(r4)     // Catch:{ all -> 0x0021 }
            L_0x0009:
                return
            L_0x000a:
                T r2 = r4.value     // Catch:{ all -> 0x0021 }
                boolean r1 = r4.hasValue     // Catch:{ all -> 0x0021 }
                r3 = 0
                r4.value = r3     // Catch:{ all -> 0x0021 }
                r3 = 0
                r4.hasValue = r3     // Catch:{ all -> 0x0021 }
                r3 = 1
                r4.emitting = r3     // Catch:{ all -> 0x0021 }
                monitor-exit(r4)     // Catch:{ all -> 0x0021 }
                if (r1 == 0) goto L_0x001d
                r5.onNext(r2)     // Catch:{ Throwable -> 0x0024 }
            L_0x001d:
                r5.onCompleted()
                goto L_0x0009
            L_0x0021:
                r3 = move-exception
                monitor-exit(r4)     // Catch:{ all -> 0x0021 }
                throw r3
            L_0x0024:
                r0 = move-exception
                dji.thirdparty.rx.exceptions.Exceptions.throwOrReport(r0, r6, r2)
                goto L_0x0009
            */
            throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.internal.operators.OperatorDebounceWithTime.DebounceState.emitAndComplete(dji.thirdparty.rx.Subscriber, dji.thirdparty.rx.Subscriber):void");
        }

        public synchronized void clear() {
            this.index++;
            this.value = null;
            this.hasValue = false;
        }
    }
}
