package com.jakewharton.rxbinding2.view;

import android.graphics.drawable.Drawable;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import com.jakewharton.rxbinding2.internal.Functions;
import com.jakewharton.rxbinding2.internal.Preconditions;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

public final class RxMenuItem {
    @CheckResult
    @NonNull
    public static Observable<Object> clicks(@NonNull MenuItem menuItem) {
        Preconditions.checkNotNull(menuItem, "menuItem == null");
        return new MenuItemClickOnSubscribe(menuItem, Functions.PREDICATE_ALWAYS_TRUE);
    }

    @CheckResult
    @NonNull
    public static Observable<Object> clicks(@NonNull MenuItem menuItem, @NonNull Predicate<? super MenuItem> handled) {
        Preconditions.checkNotNull(menuItem, "menuItem == null");
        Preconditions.checkNotNull(handled, "handled == null");
        return new MenuItemClickOnSubscribe(menuItem, handled);
    }

    @CheckResult
    @NonNull
    public static Observable<MenuItemActionViewEvent> actionViewEvents(@NonNull MenuItem menuItem) {
        Preconditions.checkNotNull(menuItem, "menuItem == null");
        return new MenuItemActionViewEventObservable(menuItem, Functions.PREDICATE_ALWAYS_TRUE);
    }

    @CheckResult
    @NonNull
    public static Observable<MenuItemActionViewEvent> actionViewEvents(@NonNull MenuItem menuItem, @NonNull Predicate<? super MenuItemActionViewEvent> handled) {
        Preconditions.checkNotNull(menuItem, "menuItem == null");
        Preconditions.checkNotNull(handled, "handled == null");
        return new MenuItemActionViewEventObservable(menuItem, handled);
    }

    @CheckResult
    @NonNull
    @Deprecated
    public static Consumer<? super Boolean> checked(@NonNull MenuItem menuItem) {
        Preconditions.checkNotNull(menuItem, "menuItem == null");
        menuItem.getClass();
        return RxMenuItem$$Lambda$0.get$Lambda(menuItem);
    }

    @CheckResult
    @NonNull
    @Deprecated
    public static Consumer<? super Boolean> enabled(@NonNull MenuItem menuItem) {
        Preconditions.checkNotNull(menuItem, "menuItem == null");
        menuItem.getClass();
        return RxMenuItem$$Lambda$1.get$Lambda(menuItem);
    }

    @CheckResult
    @NonNull
    @Deprecated
    public static Consumer<? super Drawable> icon(@NonNull MenuItem menuItem) {
        Preconditions.checkNotNull(menuItem, "menuItem == null");
        menuItem.getClass();
        return RxMenuItem$$Lambda$2.get$Lambda(menuItem);
    }

    @CheckResult
    @NonNull
    @Deprecated
    public static Consumer<? super Integer> iconRes(@NonNull MenuItem menuItem) {
        Preconditions.checkNotNull(menuItem, "menuItem == null");
        menuItem.getClass();
        return RxMenuItem$$Lambda$3.get$Lambda(menuItem);
    }

    @CheckResult
    @NonNull
    @Deprecated
    public static Consumer<? super CharSequence> title(@NonNull MenuItem menuItem) {
        Preconditions.checkNotNull(menuItem, "menuItem == null");
        menuItem.getClass();
        return RxMenuItem$$Lambda$4.get$Lambda(menuItem);
    }

    @CheckResult
    @NonNull
    @Deprecated
    public static Consumer<? super Integer> titleRes(@NonNull MenuItem menuItem) {
        Preconditions.checkNotNull(menuItem, "menuItem == null");
        menuItem.getClass();
        return RxMenuItem$$Lambda$5.get$Lambda(menuItem);
    }

    @CheckResult
    @NonNull
    @Deprecated
    public static Consumer<? super Boolean> visible(@NonNull MenuItem menuItem) {
        Preconditions.checkNotNull(menuItem, "menuItem == null");
        menuItem.getClass();
        return RxMenuItem$$Lambda$6.get$Lambda(menuItem);
    }

    private RxMenuItem() {
        throw new AssertionError("No instances.");
    }
}
