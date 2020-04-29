package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;

public interface ObservablePublishClassic<T> {
    ObservableSource<T> publishSource();
}
