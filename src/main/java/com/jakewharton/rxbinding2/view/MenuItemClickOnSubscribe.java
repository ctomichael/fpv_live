package com.jakewharton.rxbinding2.view;

import android.view.MenuItem;
import com.jakewharton.rxbinding2.internal.Notification;
import com.jakewharton.rxbinding2.internal.Preconditions;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;
import io.reactivex.functions.Predicate;

final class MenuItemClickOnSubscribe extends Observable<Object> {
    private final Predicate<? super MenuItem> handled;
    private final MenuItem menuItem;

    MenuItemClickOnSubscribe(MenuItem menuItem2, Predicate<? super MenuItem> handled2) {
        this.menuItem = menuItem2;
        this.handled = handled2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super Object> observer) {
        if (Preconditions.checkMainThread(observer)) {
            Listener listener = new Listener(this.menuItem, this.handled, observer);
            observer.onSubscribe(listener);
            this.menuItem.setOnMenuItemClickListener(listener);
        }
    }

    static final class Listener extends MainThreadDisposable implements MenuItem.OnMenuItemClickListener {
        private final Predicate<? super MenuItem> handled;
        private final MenuItem menuItem;
        private final Observer<? super Object> observer;

        Listener(MenuItem menuItem2, Predicate<? super MenuItem> handled2, Observer<? super Object> observer2) {
            this.menuItem = menuItem2;
            this.handled = handled2;
            this.observer = observer2;
        }

        public boolean onMenuItemClick(MenuItem item) {
            if (!isDisposed()) {
                try {
                    if (this.handled.test(this.menuItem)) {
                        this.observer.onNext(Notification.INSTANCE);
                        return true;
                    }
                } catch (Exception e) {
                    this.observer.onError(e);
                    dispose();
                }
            }
            return false;
        }

        /* access modifiers changed from: protected */
        public void onDispose() {
            this.menuItem.setOnMenuItemClickListener(null);
        }
    }
}
