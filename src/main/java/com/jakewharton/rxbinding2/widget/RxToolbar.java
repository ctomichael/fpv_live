package com.jakewharton.rxbinding2.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.MenuItem;
import android.widget.Toolbar;
import com.jakewharton.rxbinding2.internal.Preconditions;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

@RequiresApi(21)
public final class RxToolbar {
    @CheckResult
    @NonNull
    public static Observable<MenuItem> itemClicks(@NonNull Toolbar view) {
        Preconditions.checkNotNull(view, "view == null");
        return new ToolbarItemClickObservable(view);
    }

    @CheckResult
    @NonNull
    public static Observable<Object> navigationClicks(@NonNull Toolbar view) {
        Preconditions.checkNotNull(view, "view == null");
        return new ToolbarNavigationClickObservable(view);
    }

    @CheckResult
    @NonNull
    @Deprecated
    public static Consumer<? super CharSequence> title(@NonNull Toolbar view) {
        Preconditions.checkNotNull(view, "view == null");
        view.getClass();
        return RxToolbar$$Lambda$0.get$Lambda(view);
    }

    @CheckResult
    @NonNull
    @Deprecated
    public static Consumer<? super Integer> titleRes(@NonNull Toolbar view) {
        Preconditions.checkNotNull(view, "view == null");
        view.getClass();
        return RxToolbar$$Lambda$1.get$Lambda(view);
    }

    @CheckResult
    @NonNull
    @Deprecated
    public static Consumer<? super CharSequence> subtitle(@NonNull Toolbar view) {
        Preconditions.checkNotNull(view, "view == null");
        view.getClass();
        return RxToolbar$$Lambda$2.get$Lambda(view);
    }

    @CheckResult
    @NonNull
    @Deprecated
    public static Consumer<? super Integer> subtitleRes(@NonNull Toolbar view) {
        Preconditions.checkNotNull(view, "view == null");
        view.getClass();
        return RxToolbar$$Lambda$3.get$Lambda(view);
    }

    private RxToolbar() {
        throw new AssertionError("No instances.");
    }
}
