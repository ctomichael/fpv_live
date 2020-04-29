package com.jakewharton.rxbinding2.view;

import android.view.MenuItem;
import com.jakewharton.rxbinding2.internal.Preconditions;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;
import io.reactivex.functions.Predicate;

final class MenuItemActionViewEventObservable extends Observable<MenuItemActionViewEvent> {
    private final Predicate<? super MenuItemActionViewEvent> handled;
    private final MenuItem menuItem;

    MenuItemActionViewEventObservable(MenuItem menuItem2, Predicate<? super MenuItemActionViewEvent> handled2) {
        this.menuItem = menuItem2;
        this.handled = handled2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super MenuItemActionViewEvent> observer) {
        if (Preconditions.checkMainThread(observer)) {
            Listener listener = new Listener(this.menuItem, this.handled, observer);
            observer.onSubscribe(listener);
            this.menuItem.setOnActionExpandListener(listener);
        }
    }

    static final class Listener extends MainThreadDisposable implements MenuItem.OnActionExpandListener {
        private final Predicate<? super MenuItemActionViewEvent> handled;
        private final MenuItem menuItem;
        private final Observer<? super MenuItemActionViewEvent> observer;

        Listener(MenuItem menuItem2, Predicate<? super MenuItemActionViewEvent> handled2, Observer<? super MenuItemActionViewEvent> observer2) {
            this.menuItem = menuItem2;
            this.handled = handled2;
            this.observer = observer2;
        }

        public boolean onMenuItemActionExpand(MenuItem item) {
            return onEvent(MenuItemActionViewExpandEvent.create(item));
        }

        public boolean onMenuItemActionCollapse(MenuItem item) {
            return onEvent(MenuItemActionViewCollapseEvent.create(item));
        }

        private boolean onEvent(MenuItemActionViewEvent event) {
            if (!isDisposed()) {
                try {
                    if (this.handled.test(event)) {
                        this.observer.onNext(event);
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
            this.menuItem.setOnActionExpandListener(null);
        }
    }
}
