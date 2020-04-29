package dji.thirdparty.io.reactivex.internal.operators.observable;

import dji.thirdparty.io.reactivex.Observable;
import dji.thirdparty.io.reactivex.Observer;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.functions.Consumer;
import dji.thirdparty.io.reactivex.observables.ConnectableObservable;
import java.util.concurrent.atomic.AtomicInteger;

public final class ObservableAutoConnect<T> extends Observable<T> {
    final AtomicInteger clients = new AtomicInteger();
    final Consumer<? super Disposable> connection;
    final int numberOfObservers;
    final ConnectableObservable<? extends T> source;

    public ObservableAutoConnect(ConnectableObservable<? extends T> source2, int numberOfObservers2, Consumer<? super Disposable> connection2) {
        this.source = source2;
        this.numberOfObservers = numberOfObservers2;
        this.connection = connection2;
    }

    public void subscribeActual(Observer<? super T> child) {
        this.source.subscribe(child);
        if (this.clients.incrementAndGet() == this.numberOfObservers) {
            this.source.connect(this.connection);
        }
    }
}
