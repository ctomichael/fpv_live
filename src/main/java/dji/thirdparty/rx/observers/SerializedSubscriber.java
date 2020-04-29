package dji.thirdparty.rx.observers;

import dji.thirdparty.rx.Observer;
import dji.thirdparty.rx.Subscriber;

public class SerializedSubscriber<T> extends Subscriber<T> {
    private final Observer<T> s;

    public SerializedSubscriber(Subscriber<? super T> s2) {
        this(s2, true);
    }

    public SerializedSubscriber(Subscriber<? super T> s2, boolean shareSubscriptions) {
        super(s2, shareSubscriptions);
        this.s = new SerializedObserver(s2);
    }

    public void onCompleted() {
        this.s.onCompleted();
    }

    public void onError(Throwable e) {
        this.s.onError(e);
    }

    public void onNext(T t) {
        this.s.onNext(t);
    }
}
