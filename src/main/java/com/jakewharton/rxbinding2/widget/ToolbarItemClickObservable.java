package com.jakewharton.rxbinding2.widget;

import android.support.annotation.RequiresApi;
import android.view.MenuItem;
import android.widget.Toolbar;
import com.jakewharton.rxbinding2.internal.Preconditions;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

@RequiresApi(21)
final class ToolbarItemClickObservable extends Observable<MenuItem> {
    private final Toolbar view;

    ToolbarItemClickObservable(Toolbar view2) {
        this.view = view2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super MenuItem> observer) {
        if (Preconditions.checkMainThread(observer)) {
            Listener listener = new Listener(this.view, observer);
            observer.onSubscribe(listener);
            this.view.setOnMenuItemClickListener(listener);
        }
    }

    static final class Listener extends MainThreadDisposable implements Toolbar.OnMenuItemClickListener {
        private final Observer<? super MenuItem> observer;
        private final Toolbar view;

        Listener(Toolbar view2, Observer<? super MenuItem> observer2) {
            this.view = view2;
            this.observer = observer2;
        }

        public boolean onMenuItemClick(MenuItem item) {
            if (isDisposed()) {
                return false;
            }
            this.observer.onNext(item);
            return true;
        }

        /* access modifiers changed from: protected */
        public void onDispose() {
            this.view.setOnMenuItemClickListener(null);
        }
    }
}
