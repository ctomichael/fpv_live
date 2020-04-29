package dji.thirdparty.io.reactivex.internal.operators.completable;

import dji.thirdparty.io.reactivex.Completable;
import dji.thirdparty.io.reactivex.CompletableObserver;
import dji.thirdparty.io.reactivex.CompletableSource;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.disposables.Disposables;
import dji.thirdparty.io.reactivex.exceptions.CompositeException;
import dji.thirdparty.io.reactivex.exceptions.Exceptions;
import dji.thirdparty.io.reactivex.functions.Action;
import dji.thirdparty.io.reactivex.functions.Consumer;
import dji.thirdparty.io.reactivex.internal.disposables.EmptyDisposable;
import dji.thirdparty.io.reactivex.plugins.RxJavaPlugins;

public final class CompletablePeek extends Completable {
    final Action onAfterTerminate;
    final Action onComplete;
    final Action onDispose;
    final Consumer<? super Throwable> onError;
    final Consumer<? super Disposable> onSubscribe;
    final Action onTerminate;
    final CompletableSource source;

    public CompletablePeek(CompletableSource source2, Consumer<? super Disposable> onSubscribe2, Consumer<? super Throwable> onError2, Action onComplete2, Action onTerminate2, Action onAfterTerminate2, Action onDispose2) {
        this.source = source2;
        this.onSubscribe = onSubscribe2;
        this.onError = onError2;
        this.onComplete = onComplete2;
        this.onTerminate = onTerminate2;
        this.onAfterTerminate = onAfterTerminate2;
        this.onDispose = onDispose2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(final CompletableObserver s) {
        this.source.subscribe(new CompletableObserver() {
            /* class dji.thirdparty.io.reactivex.internal.operators.completable.CompletablePeek.AnonymousClass1 */

            public void onComplete() {
                try {
                    CompletablePeek.this.onComplete.run();
                    CompletablePeek.this.onTerminate.run();
                    s.onComplete();
                    doAfter();
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    s.onError(e);
                }
            }

            public void onError(Throwable e) {
                try {
                    CompletablePeek.this.onError.accept(e);
                    CompletablePeek.this.onTerminate.run();
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    e = new CompositeException(e, ex);
                }
                s.onError(e);
                doAfter();
            }

            public void onSubscribe(final Disposable d) {
                try {
                    CompletablePeek.this.onSubscribe.accept(d);
                    s.onSubscribe(Disposables.fromRunnable(new Runnable() {
                        /* class dji.thirdparty.io.reactivex.internal.operators.completable.CompletablePeek.AnonymousClass1.AnonymousClass1 */

                        public void run() {
                            try {
                                CompletablePeek.this.onDispose.run();
                            } catch (Throwable e) {
                                Exceptions.throwIfFatal(e);
                                RxJavaPlugins.onError(e);
                            }
                            d.dispose();
                        }
                    }));
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    d.dispose();
                    EmptyDisposable.error(ex, s);
                }
            }

            /* access modifiers changed from: package-private */
            public void doAfter() {
                try {
                    CompletablePeek.this.onAfterTerminate.run();
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    RxJavaPlugins.onError(ex);
                }
            }
        });
    }
}
