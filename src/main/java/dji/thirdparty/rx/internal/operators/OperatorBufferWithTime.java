package dji.thirdparty.rx.internal.operators;

import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Scheduler;
import dji.thirdparty.rx.Subscriber;
import dji.thirdparty.rx.exceptions.Exceptions;
import dji.thirdparty.rx.functions.Action0;
import dji.thirdparty.rx.observers.SerializedSubscriber;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public final class OperatorBufferWithTime<T> implements Observable.Operator<List<T>, T> {
    final int count;
    final Scheduler scheduler;
    final long timeshift;
    final long timespan;
    final TimeUnit unit;

    public /* bridge */ /* synthetic */ Object call(Object x0) {
        return call((Subscriber) ((Subscriber) x0));
    }

    public OperatorBufferWithTime(long timespan2, long timeshift2, TimeUnit unit2, int count2, Scheduler scheduler2) {
        this.timespan = timespan2;
        this.timeshift = timeshift2;
        this.unit = unit2;
        this.count = count2;
        this.scheduler = scheduler2;
    }

    public Subscriber<? super T> call(Subscriber<? super List<T>> child) {
        Scheduler.Worker inner = this.scheduler.createWorker();
        SerializedSubscriber<List<T>> serialized = new SerializedSubscriber<>(child);
        if (this.timespan == this.timeshift) {
            OperatorBufferWithTime<T>.ExactSubscriber bsub = new ExactSubscriber(serialized, inner);
            bsub.add(inner);
            child.add(bsub);
            bsub.scheduleExact();
            return bsub;
        }
        OperatorBufferWithTime<T>.InexactSubscriber bsub2 = new InexactSubscriber(serialized, inner);
        bsub2.add(inner);
        child.add(bsub2);
        bsub2.startNewChunk();
        bsub2.scheduleChunk();
        return bsub2;
    }

    final class InexactSubscriber extends Subscriber<T> {
        final Subscriber<? super List<T>> child;
        final List<List<T>> chunks = new LinkedList();
        boolean done;
        final Scheduler.Worker inner;

        public InexactSubscriber(Subscriber<? super List<T>> child2, Scheduler.Worker inner2) {
            this.child = child2;
            this.inner = inner2;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:20:0x0038, code lost:
            if (r4 == null) goto L_0x0053;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:21:0x003a, code lost:
            r1 = r4.iterator();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:23:0x0042, code lost:
            if (r1.hasNext() == false) goto L_0x0053;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:24:0x0044, code lost:
            r7.child.onNext((java.util.List) r1.next());
         */
        /* JADX WARNING: Code restructure failed: missing block: B:38:?, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onNext(T r8) {
            /*
                r7 = this;
                r3 = 0
                monitor-enter(r7)
                boolean r5 = r7.done     // Catch:{ all -> 0x0050 }
                if (r5 == 0) goto L_0x0008
                monitor-exit(r7)     // Catch:{ all -> 0x0050 }
            L_0x0007:
                return
            L_0x0008:
                java.util.List<java.util.List<T>> r5 = r7.chunks     // Catch:{ all -> 0x0050 }
                java.util.Iterator r2 = r5.iterator()     // Catch:{ all -> 0x0050 }
                r4 = r3
            L_0x000f:
                boolean r5 = r2.hasNext()     // Catch:{ all -> 0x0055 }
                if (r5 == 0) goto L_0x0037
                java.lang.Object r0 = r2.next()     // Catch:{ all -> 0x0055 }
                java.util.List r0 = (java.util.List) r0     // Catch:{ all -> 0x0055 }
                r0.add(r8)     // Catch:{ all -> 0x0055 }
                int r5 = r0.size()     // Catch:{ all -> 0x0055 }
                dji.thirdparty.rx.internal.operators.OperatorBufferWithTime r6 = dji.thirdparty.rx.internal.operators.OperatorBufferWithTime.this     // Catch:{ all -> 0x0055 }
                int r6 = r6.count     // Catch:{ all -> 0x0055 }
                if (r5 != r6) goto L_0x005a
                r2.remove()     // Catch:{ all -> 0x0055 }
                if (r4 != 0) goto L_0x0058
                java.util.LinkedList r3 = new java.util.LinkedList     // Catch:{ all -> 0x0055 }
                r3.<init>()     // Catch:{ all -> 0x0055 }
            L_0x0032:
                r3.add(r0)     // Catch:{ all -> 0x0050 }
            L_0x0035:
                r4 = r3
                goto L_0x000f
            L_0x0037:
                monitor-exit(r7)     // Catch:{ all -> 0x0055 }
                if (r4 == 0) goto L_0x0053
                java.util.Iterator r1 = r4.iterator()
            L_0x003e:
                boolean r5 = r1.hasNext()
                if (r5 == 0) goto L_0x0053
                java.lang.Object r0 = r1.next()
                java.util.List r0 = (java.util.List) r0
                dji.thirdparty.rx.Subscriber<? super java.util.List<T>> r5 = r7.child
                r5.onNext(r0)
                goto L_0x003e
            L_0x0050:
                r5 = move-exception
            L_0x0051:
                monitor-exit(r7)     // Catch:{ all -> 0x0050 }
                throw r5
            L_0x0053:
                r3 = r4
                goto L_0x0007
            L_0x0055:
                r5 = move-exception
                r3 = r4
                goto L_0x0051
            L_0x0058:
                r3 = r4
                goto L_0x0032
            L_0x005a:
                r3 = r4
                goto L_0x0035
            */
            throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.internal.operators.OperatorBufferWithTime.InexactSubscriber.onNext(java.lang.Object):void");
        }

        public void onError(Throwable e) {
            synchronized (this) {
                if (!this.done) {
                    this.done = true;
                    this.chunks.clear();
                    this.child.onError(e);
                    unsubscribe();
                }
            }
        }

        /* JADX WARNING: Code restructure failed: missing block: B:11:0x001f, code lost:
            if (r1.hasNext() == false) goto L_0x0037;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:12:0x0021, code lost:
            r5.child.onNext(r1.next());
         */
        /* JADX WARNING: Code restructure failed: missing block: B:20:0x0037, code lost:
            r5.child.onCompleted();
            unsubscribe();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:26:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:9:?, code lost:
            r1 = r2.iterator();
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onCompleted() {
            /*
                r5 = this;
                monitor-enter(r5)     // Catch:{ Throwable -> 0x002d }
                boolean r4 = r5.done     // Catch:{ all -> 0x0034 }
                if (r4 == 0) goto L_0x0007
                monitor-exit(r5)     // Catch:{ all -> 0x0034 }
            L_0x0006:
                return
            L_0x0007:
                r4 = 1
                r5.done = r4     // Catch:{ all -> 0x0034 }
                java.util.LinkedList r2 = new java.util.LinkedList     // Catch:{ all -> 0x0034 }
                java.util.List<java.util.List<T>> r4 = r5.chunks     // Catch:{ all -> 0x0034 }
                r2.<init>(r4)     // Catch:{ all -> 0x0034 }
                java.util.List<java.util.List<T>> r4 = r5.chunks     // Catch:{ all -> 0x0034 }
                r4.clear()     // Catch:{ all -> 0x0034 }
                monitor-exit(r5)     // Catch:{ all -> 0x0034 }
                java.util.Iterator r1 = r2.iterator()     // Catch:{ Throwable -> 0x002d }
            L_0x001b:
                boolean r4 = r1.hasNext()     // Catch:{ Throwable -> 0x002d }
                if (r4 == 0) goto L_0x0037
                java.lang.Object r0 = r1.next()     // Catch:{ Throwable -> 0x002d }
                java.util.List r0 = (java.util.List) r0     // Catch:{ Throwable -> 0x002d }
                dji.thirdparty.rx.Subscriber<? super java.util.List<T>> r4 = r5.child     // Catch:{ Throwable -> 0x002d }
                r4.onNext(r0)     // Catch:{ Throwable -> 0x002d }
                goto L_0x001b
            L_0x002d:
                r3 = move-exception
                dji.thirdparty.rx.Subscriber<? super java.util.List<T>> r4 = r5.child
                dji.thirdparty.rx.exceptions.Exceptions.throwOrReport(r3, r4)
                goto L_0x0006
            L_0x0034:
                r4 = move-exception
                monitor-exit(r5)     // Catch:{ all -> 0x0034 }
                throw r4     // Catch:{ Throwable -> 0x002d }
            L_0x0037:
                dji.thirdparty.rx.Subscriber<? super java.util.List<T>> r4 = r5.child
                r4.onCompleted()
                r5.unsubscribe()
                goto L_0x0006
            */
            throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.internal.operators.OperatorBufferWithTime.InexactSubscriber.onCompleted():void");
        }

        /* access modifiers changed from: package-private */
        public void scheduleChunk() {
            this.inner.schedulePeriodically(new Action0() {
                /* class dji.thirdparty.rx.internal.operators.OperatorBufferWithTime.InexactSubscriber.AnonymousClass1 */

                public void call() {
                    InexactSubscriber.this.startNewChunk();
                }
            }, OperatorBufferWithTime.this.timeshift, OperatorBufferWithTime.this.timeshift, OperatorBufferWithTime.this.unit);
        }

        /* access modifiers changed from: package-private */
        public void startNewChunk() {
            final List<T> chunk = new ArrayList<>();
            synchronized (this) {
                if (!this.done) {
                    this.chunks.add(chunk);
                    this.inner.schedule(new Action0() {
                        /* class dji.thirdparty.rx.internal.operators.OperatorBufferWithTime.InexactSubscriber.AnonymousClass2 */

                        public void call() {
                            InexactSubscriber.this.emitChunk(chunk);
                        }
                    }, OperatorBufferWithTime.this.timespan, OperatorBufferWithTime.this.unit);
                }
            }
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Code restructure failed: missing block: B:13:0x0021, code lost:
            if (r1 == false) goto L_?;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:15:?, code lost:
            r5.child.onNext(r6);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:16:0x0029, code lost:
            r3 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:17:0x002a, code lost:
            dji.thirdparty.rx.exceptions.Exceptions.throwOrReport(r3, r5);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:28:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:29:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:30:?, code lost:
            return;
         */
        /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void emitChunk(java.util.List<T> r6) {
            /*
                r5 = this;
                r1 = 0
                monitor-enter(r5)
                boolean r4 = r5.done     // Catch:{ all -> 0x002e }
                if (r4 == 0) goto L_0x0008
                monitor-exit(r5)     // Catch:{ all -> 0x002e }
            L_0x0007:
                return
            L_0x0008:
                java.util.List<java.util.List<T>> r4 = r5.chunks     // Catch:{ all -> 0x002e }
                java.util.Iterator r2 = r4.iterator()     // Catch:{ all -> 0x002e }
            L_0x000e:
                boolean r4 = r2.hasNext()     // Catch:{ all -> 0x002e }
                if (r4 == 0) goto L_0x0020
                java.lang.Object r0 = r2.next()     // Catch:{ all -> 0x002e }
                java.util.List r0 = (java.util.List) r0     // Catch:{ all -> 0x002e }
                if (r0 != r6) goto L_0x000e
                r2.remove()     // Catch:{ all -> 0x002e }
                r1 = 1
            L_0x0020:
                monitor-exit(r5)     // Catch:{ all -> 0x002e }
                if (r1 == 0) goto L_0x0007
                dji.thirdparty.rx.Subscriber<? super java.util.List<T>> r4 = r5.child     // Catch:{ Throwable -> 0x0029 }
                r4.onNext(r6)     // Catch:{ Throwable -> 0x0029 }
                goto L_0x0007
            L_0x0029:
                r3 = move-exception
                dji.thirdparty.rx.exceptions.Exceptions.throwOrReport(r3, r5)
                goto L_0x0007
            L_0x002e:
                r4 = move-exception
                monitor-exit(r5)     // Catch:{ all -> 0x002e }
                throw r4
            */
            throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.internal.operators.OperatorBufferWithTime.InexactSubscriber.emitChunk(java.util.List):void");
        }
    }

    final class ExactSubscriber extends Subscriber<T> {
        final Subscriber<? super List<T>> child;
        List<T> chunk = new ArrayList();
        boolean done;
        final Scheduler.Worker inner;

        public ExactSubscriber(Subscriber<? super List<T>> child2, Scheduler.Worker inner2) {
            this.child = child2;
            this.inner = inner2;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:10:0x0023, code lost:
            if (r0 == null) goto L_?;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:11:0x0025, code lost:
            r3.child.onNext(r0);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:20:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:21:?, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onNext(T r4) {
            /*
                r3 = this;
                r0 = 0
                monitor-enter(r3)
                boolean r1 = r3.done     // Catch:{ all -> 0x002b }
                if (r1 == 0) goto L_0x0008
                monitor-exit(r3)     // Catch:{ all -> 0x002b }
            L_0x0007:
                return
            L_0x0008:
                java.util.List<T> r1 = r3.chunk     // Catch:{ all -> 0x002b }
                r1.add(r4)     // Catch:{ all -> 0x002b }
                java.util.List<T> r1 = r3.chunk     // Catch:{ all -> 0x002b }
                int r1 = r1.size()     // Catch:{ all -> 0x002b }
                dji.thirdparty.rx.internal.operators.OperatorBufferWithTime r2 = dji.thirdparty.rx.internal.operators.OperatorBufferWithTime.this     // Catch:{ all -> 0x002b }
                int r2 = r2.count     // Catch:{ all -> 0x002b }
                if (r1 != r2) goto L_0x0022
                java.util.List<T> r0 = r3.chunk     // Catch:{ all -> 0x002b }
                java.util.ArrayList r1 = new java.util.ArrayList     // Catch:{ all -> 0x002b }
                r1.<init>()     // Catch:{ all -> 0x002b }
                r3.chunk = r1     // Catch:{ all -> 0x002b }
            L_0x0022:
                monitor-exit(r3)     // Catch:{ all -> 0x002b }
                if (r0 == 0) goto L_0x0007
                dji.thirdparty.rx.Subscriber<? super java.util.List<T>> r1 = r3.child
                r1.onNext(r0)
                goto L_0x0007
            L_0x002b:
                r1 = move-exception
                monitor-exit(r3)     // Catch:{ all -> 0x002b }
                throw r1
            */
            throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.internal.operators.OperatorBufferWithTime.ExactSubscriber.onNext(java.lang.Object):void");
        }

        public void onError(Throwable e) {
            synchronized (this) {
                if (!this.done) {
                    this.done = true;
                    this.chunk = null;
                    this.child.onError(e);
                    unsubscribe();
                }
            }
        }

        public void onCompleted() {
            try {
                this.inner.unsubscribe();
                synchronized (this) {
                    if (!this.done) {
                        this.done = true;
                        List<T> toEmit = this.chunk;
                        this.chunk = null;
                        this.child.onNext(toEmit);
                        this.child.onCompleted();
                        unsubscribe();
                    }
                }
            } catch (Throwable t) {
                Exceptions.throwOrReport(t, this.child);
            }
        }

        /* access modifiers changed from: package-private */
        public void scheduleExact() {
            this.inner.schedulePeriodically(new Action0() {
                /* class dji.thirdparty.rx.internal.operators.OperatorBufferWithTime.ExactSubscriber.AnonymousClass1 */

                public void call() {
                    ExactSubscriber.this.emit();
                }
            }, OperatorBufferWithTime.this.timespan, OperatorBufferWithTime.this.timespan, OperatorBufferWithTime.this.unit);
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void emit() {
            /*
                r3 = this;
                monitor-enter(r3)
                boolean r2 = r3.done     // Catch:{ all -> 0x001c }
                if (r2 == 0) goto L_0x0007
                monitor-exit(r3)     // Catch:{ all -> 0x001c }
            L_0x0006:
                return
            L_0x0007:
                java.util.List<T> r1 = r3.chunk     // Catch:{ all -> 0x001c }
                java.util.ArrayList r2 = new java.util.ArrayList     // Catch:{ all -> 0x001c }
                r2.<init>()     // Catch:{ all -> 0x001c }
                r3.chunk = r2     // Catch:{ all -> 0x001c }
                monitor-exit(r3)     // Catch:{ all -> 0x001c }
                dji.thirdparty.rx.Subscriber<? super java.util.List<T>> r2 = r3.child     // Catch:{ Throwable -> 0x0017 }
                r2.onNext(r1)     // Catch:{ Throwable -> 0x0017 }
                goto L_0x0006
            L_0x0017:
                r0 = move-exception
                dji.thirdparty.rx.exceptions.Exceptions.throwOrReport(r0, r3)
                goto L_0x0006
            L_0x001c:
                r2 = move-exception
                monitor-exit(r3)     // Catch:{ all -> 0x001c }
                throw r2
            */
            throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.internal.operators.OperatorBufferWithTime.ExactSubscriber.emit():void");
        }
    }
}
