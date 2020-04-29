package com.jakewharton.rxbinding2.widget;

import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Toolbar;
import com.jakewharton.rxbinding2.internal.Notification;
import com.jakewharton.rxbinding2.internal.Preconditions;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

@RequiresApi(21)
final class ToolbarNavigationClickObservable extends Observable<Object> {
    private final Toolbar view;

    ToolbarNavigationClickObservable(Toolbar view2) {
        this.view = view2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super Object> observer) {
        if (Preconditions.checkMainThread(observer)) {
            Listener listener = new Listener(this.view, observer);
            observer.onSubscribe(listener);
            this.view.setNavigationOnClickListener(listener);
        }
    }

    static final class Listener extends MainThreadDisposable implements View.OnClickListener {
        private final Observer<? super Object> observer;
        private final Toolbar view;

        Listener(Toolbar view2, Observer<? super Object> observer2) {
            this.view = view2;
            this.observer = observer2;
        }

        public void onClick(View v) {
            if (!isDisposed()) {
                this.observer.onNext(Notification.INSTANCE);
            }
        }

        /* access modifiers changed from: protected */
        public void onDispose() {
            this.view.setNavigationOnClickListener(null);
        }
    }
}
