package com.jakewharton.rxbinding2.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.Adapter;
import android.widget.AdapterView;
import com.jakewharton.rxbinding2.InitialValueObservable;
import com.jakewharton.rxbinding2.internal.Functions;
import com.jakewharton.rxbinding2.internal.Preconditions;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import java.util.concurrent.Callable;

public final class RxAdapterView {
    @CheckResult
    @NonNull
    public static <T extends Adapter> InitialValueObservable<Integer> itemSelections(@NonNull AdapterView<T> view) {
        Preconditions.checkNotNull(view, "view == null");
        return new AdapterViewItemSelectionObservable(view);
    }

    @CheckResult
    @NonNull
    public static <T extends Adapter> InitialValueObservable<AdapterViewSelectionEvent> selectionEvents(@NonNull AdapterView<T> view) {
        Preconditions.checkNotNull(view, "view == null");
        return new AdapterViewSelectionObservable(view);
    }

    @CheckResult
    @NonNull
    public static <T extends Adapter> Observable<Integer> itemClicks(@NonNull AdapterView<T> view) {
        Preconditions.checkNotNull(view, "view == null");
        return new AdapterViewItemClickObservable(view);
    }

    @CheckResult
    @NonNull
    public static <T extends Adapter> Observable<AdapterViewItemClickEvent> itemClickEvents(@NonNull AdapterView<T> view) {
        Preconditions.checkNotNull(view, "view == null");
        return new AdapterViewItemClickEventObservable(view);
    }

    @CheckResult
    @NonNull
    public static <T extends Adapter> Observable<Integer> itemLongClicks(@NonNull AdapterView<T> view) {
        Preconditions.checkNotNull(view, "view == null");
        return itemLongClicks(view, Functions.CALLABLE_ALWAYS_TRUE);
    }

    @CheckResult
    @NonNull
    public static <T extends Adapter> Observable<Integer> itemLongClicks(@NonNull AdapterView<T> view, @NonNull Callable<Boolean> handled) {
        Preconditions.checkNotNull(view, "view == null");
        Preconditions.checkNotNull(handled, "handled == null");
        return new AdapterViewItemLongClickObservable(view, handled);
    }

    @CheckResult
    @NonNull
    public static <T extends Adapter> Observable<AdapterViewItemLongClickEvent> itemLongClickEvents(@NonNull AdapterView<T> view) {
        Preconditions.checkNotNull(view, "view == null");
        return itemLongClickEvents(view, Functions.PREDICATE_ALWAYS_TRUE);
    }

    @CheckResult
    @NonNull
    public static <T extends Adapter> Observable<AdapterViewItemLongClickEvent> itemLongClickEvents(@NonNull AdapterView<T> view, @NonNull Predicate<? super AdapterViewItemLongClickEvent> handled) {
        Preconditions.checkNotNull(view, "view == null");
        Preconditions.checkNotNull(handled, "handled == null");
        return new AdapterViewItemLongClickEventObservable(view, handled);
    }

    @CheckResult
    @NonNull
    @Deprecated
    public static <T extends Adapter> Consumer<? super Integer> selection(@NonNull AdapterView<T> view) {
        Preconditions.checkNotNull(view, "view == null");
        view.getClass();
        return RxAdapterView$$Lambda$0.get$Lambda(view);
    }

    private RxAdapterView() {
        throw new AssertionError("No instances.");
    }
}
