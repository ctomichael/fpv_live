package com.jakewharton.rxbinding2.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.TextView;
import com.jakewharton.rxbinding2.InitialValueObservable;
import com.jakewharton.rxbinding2.internal.Functions;
import com.jakewharton.rxbinding2.internal.Preconditions;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

public final class RxTextView {
    @CheckResult
    @NonNull
    public static Observable<Integer> editorActions(@NonNull TextView view) {
        Preconditions.checkNotNull(view, "view == null");
        return editorActions(view, Functions.PREDICATE_ALWAYS_TRUE);
    }

    @CheckResult
    @NonNull
    public static Observable<Integer> editorActions(@NonNull TextView view, @NonNull Predicate<? super Integer> handled) {
        Preconditions.checkNotNull(view, "view == null");
        Preconditions.checkNotNull(handled, "handled == null");
        return new TextViewEditorActionObservable(view, handled);
    }

    @CheckResult
    @NonNull
    public static Observable<TextViewEditorActionEvent> editorActionEvents(@NonNull TextView view) {
        Preconditions.checkNotNull(view, "view == null");
        return editorActionEvents(view, Functions.PREDICATE_ALWAYS_TRUE);
    }

    @CheckResult
    @NonNull
    public static Observable<TextViewEditorActionEvent> editorActionEvents(@NonNull TextView view, @NonNull Predicate<? super TextViewEditorActionEvent> handled) {
        Preconditions.checkNotNull(view, "view == null");
        Preconditions.checkNotNull(handled, "handled == null");
        return new TextViewEditorActionEventObservable(view, handled);
    }

    @CheckResult
    @NonNull
    public static InitialValueObservable<CharSequence> textChanges(@NonNull TextView view) {
        Preconditions.checkNotNull(view, "view == null");
        return new TextViewTextObservable(view);
    }

    @CheckResult
    @NonNull
    public static InitialValueObservable<TextViewTextChangeEvent> textChangeEvents(@NonNull TextView view) {
        Preconditions.checkNotNull(view, "view == null");
        return new TextViewTextChangeEventObservable(view);
    }

    @CheckResult
    @NonNull
    public static InitialValueObservable<TextViewBeforeTextChangeEvent> beforeTextChangeEvents(@NonNull TextView view) {
        Preconditions.checkNotNull(view, "view == null");
        return new TextViewBeforeTextChangeEventObservable(view);
    }

    @CheckResult
    @NonNull
    public static InitialValueObservable<TextViewAfterTextChangeEvent> afterTextChangeEvents(@NonNull TextView view) {
        Preconditions.checkNotNull(view, "view == null");
        return new TextViewAfterTextChangeEventObservable(view);
    }

    @CheckResult
    @NonNull
    @Deprecated
    public static Consumer<? super CharSequence> text(@NonNull TextView view) {
        Preconditions.checkNotNull(view, "view == null");
        view.getClass();
        return RxTextView$$Lambda$0.get$Lambda(view);
    }

    @CheckResult
    @NonNull
    @Deprecated
    public static Consumer<? super Integer> textRes(@NonNull TextView view) {
        Preconditions.checkNotNull(view, "view == null");
        view.getClass();
        return RxTextView$$Lambda$1.get$Lambda(view);
    }

    @CheckResult
    @NonNull
    @Deprecated
    public static Consumer<? super CharSequence> error(@NonNull TextView view) {
        Preconditions.checkNotNull(view, "view == null");
        view.getClass();
        return RxTextView$$Lambda$2.get$Lambda(view);
    }

    @CheckResult
    @NonNull
    @Deprecated
    public static Consumer<? super Integer> errorRes(@NonNull TextView view) {
        Preconditions.checkNotNull(view, "view == null");
        return new RxTextView$$Lambda$3(view);
    }

    @CheckResult
    @NonNull
    @Deprecated
    public static Consumer<? super CharSequence> hint(@NonNull TextView view) {
        Preconditions.checkNotNull(view, "view == null");
        view.getClass();
        return RxTextView$$Lambda$4.get$Lambda(view);
    }

    @CheckResult
    @NonNull
    @Deprecated
    public static Consumer<? super Integer> hintRes(@NonNull TextView view) {
        Preconditions.checkNotNull(view, "view == null");
        view.getClass();
        return RxTextView$$Lambda$5.get$Lambda(view);
    }

    @CheckResult
    @NonNull
    @Deprecated
    public static Consumer<? super Integer> color(@NonNull TextView view) {
        Preconditions.checkNotNull(view, "view == null");
        view.getClass();
        return RxTextView$$Lambda$6.get$Lambda(view);
    }

    private RxTextView() {
        throw new AssertionError("No instances.");
    }
}
