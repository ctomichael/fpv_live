package com.jakewharton.rxbinding2.support.v4.view;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import com.jakewharton.rxbinding2.internal.Preconditions;
import com.jakewharton.rxbinding2.view.MenuItemActionViewEvent;
import com.jakewharton.rxbinding2.view.RxMenuItem;
import io.reactivex.Observable;
import io.reactivex.functions.Predicate;

@Deprecated
public final class RxMenuItemCompat {
    @CheckResult
    @NonNull
    @Deprecated
    public static Observable<MenuItemActionViewEvent> actionViewEvents(@NonNull MenuItem menuItem) {
        Preconditions.checkNotNull(menuItem, "menuItem == null");
        return RxMenuItem.actionViewEvents(menuItem);
    }

    @CheckResult
    @NonNull
    @Deprecated
    public static Observable<MenuItemActionViewEvent> actionViewEvents(@NonNull MenuItem menuItem, @NonNull Predicate<? super MenuItemActionViewEvent> handled) {
        Preconditions.checkNotNull(menuItem, "menuItem == null");
        Preconditions.checkNotNull(handled, "handled == null");
        return RxMenuItem.actionViewEvents(menuItem, handled);
    }

    private RxMenuItemCompat() {
        throw new AssertionError("No instances.");
    }
}
