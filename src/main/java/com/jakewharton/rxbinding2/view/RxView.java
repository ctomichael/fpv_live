package com.jakewharton.rxbinding2.view;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import com.jakewharton.rxbinding2.InitialValueObservable;
import com.jakewharton.rxbinding2.internal.Functions;
import com.jakewharton.rxbinding2.internal.Preconditions;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import java.util.concurrent.Callable;

public final class RxView {
    @CheckResult
    @NonNull
    public static Observable<Object> attaches(@NonNull View view) {
        Preconditions.checkNotNull(view, "view == null");
        return new ViewAttachesObservable(view, true);
    }

    @CheckResult
    @NonNull
    public static Observable<ViewAttachEvent> attachEvents(@NonNull View view) {
        Preconditions.checkNotNull(view, "view == null");
        return new ViewAttachEventObservable(view);
    }

    @CheckResult
    @NonNull
    public static Observable<Object> detaches(@NonNull View view) {
        Preconditions.checkNotNull(view, "view == null");
        return new ViewAttachesObservable(view, false);
    }

    @CheckResult
    @NonNull
    public static Observable<Object> clicks(@NonNull View view) {
        Preconditions.checkNotNull(view, "view == null");
        return new ViewClickObservable(view);
    }

    @CheckResult
    @NonNull
    public static Observable<DragEvent> drags(@NonNull View view) {
        Preconditions.checkNotNull(view, "view == null");
        return new ViewDragObservable(view, Functions.PREDICATE_ALWAYS_TRUE);
    }

    @CheckResult
    @NonNull
    public static Observable<DragEvent> drags(@NonNull View view, @NonNull Predicate<? super DragEvent> handled) {
        Preconditions.checkNotNull(view, "view == null");
        Preconditions.checkNotNull(handled, "handled == null");
        return new ViewDragObservable(view, handled);
    }

    @RequiresApi(16)
    @CheckResult
    @NonNull
    public static Observable<Object> draws(@NonNull View view) {
        Preconditions.checkNotNull(view, "view == null");
        return new ViewTreeObserverDrawObservable(view);
    }

    @CheckResult
    @NonNull
    public static InitialValueObservable<Boolean> focusChanges(@NonNull View view) {
        Preconditions.checkNotNull(view, "view == null");
        return new ViewFocusChangeObservable(view);
    }

    @CheckResult
    @NonNull
    public static Observable<Object> globalLayouts(@NonNull View view) {
        Preconditions.checkNotNull(view, "view == null");
        return new ViewTreeObserverGlobalLayoutObservable(view);
    }

    @CheckResult
    @NonNull
    public static Observable<MotionEvent> hovers(@NonNull View view) {
        Preconditions.checkNotNull(view, "view == null");
        return new ViewHoverObservable(view, Functions.PREDICATE_ALWAYS_TRUE);
    }

    @CheckResult
    @NonNull
    public static Observable<MotionEvent> hovers(@NonNull View view, @NonNull Predicate<? super MotionEvent> handled) {
        Preconditions.checkNotNull(view, "view == null");
        Preconditions.checkNotNull(handled, "handled == null");
        return new ViewHoverObservable(view, handled);
    }

    @CheckResult
    @NonNull
    public static Observable<Object> layoutChanges(@NonNull View view) {
        Preconditions.checkNotNull(view, "view == null");
        return new ViewLayoutChangeObservable(view);
    }

    @CheckResult
    @NonNull
    public static Observable<ViewLayoutChangeEvent> layoutChangeEvents(@NonNull View view) {
        Preconditions.checkNotNull(view, "view == null");
        return new ViewLayoutChangeEventObservable(view);
    }

    @CheckResult
    @NonNull
    public static Observable<Object> longClicks(@NonNull View view) {
        Preconditions.checkNotNull(view, "view == null");
        return new ViewLongClickObservable(view, Functions.CALLABLE_ALWAYS_TRUE);
    }

    @CheckResult
    @NonNull
    public static Observable<Object> longClicks(@NonNull View view, @NonNull Callable<Boolean> handled) {
        Preconditions.checkNotNull(view, "view == null");
        Preconditions.checkNotNull(handled, "handled == null");
        return new ViewLongClickObservable(view, handled);
    }

    @CheckResult
    @NonNull
    public static Observable<Object> preDraws(@NonNull View view, @NonNull Callable<Boolean> proceedDrawingPass) {
        Preconditions.checkNotNull(view, "view == null");
        Preconditions.checkNotNull(proceedDrawingPass, "proceedDrawingPass == null");
        return new ViewTreeObserverPreDrawObservable(view, proceedDrawingPass);
    }

    @RequiresApi(23)
    @CheckResult
    @NonNull
    public static Observable<ViewScrollChangeEvent> scrollChangeEvents(@NonNull View view) {
        Preconditions.checkNotNull(view, "view == null");
        return new ViewScrollChangeEventObservable(view);
    }

    @CheckResult
    @NonNull
    public static Observable<Integer> systemUiVisibilityChanges(@NonNull View view) {
        Preconditions.checkNotNull(view, "view == null");
        return new ViewSystemUiVisibilityChangeObservable(view);
    }

    @CheckResult
    @NonNull
    public static Observable<MotionEvent> touches(@NonNull View view) {
        Preconditions.checkNotNull(view, "view == null");
        return new ViewTouchObservable(view, Functions.PREDICATE_ALWAYS_TRUE);
    }

    @CheckResult
    @NonNull
    public static Observable<MotionEvent> touches(@NonNull View view, @NonNull Predicate<? super MotionEvent> handled) {
        Preconditions.checkNotNull(view, "view == null");
        Preconditions.checkNotNull(handled, "handled == null");
        return new ViewTouchObservable(view, handled);
    }

    @CheckResult
    @NonNull
    public static Observable<KeyEvent> keys(@NonNull View view) {
        Preconditions.checkNotNull(view, "view == null");
        return new ViewKeyObservable(view, Functions.PREDICATE_ALWAYS_TRUE);
    }

    @CheckResult
    @NonNull
    public static Observable<KeyEvent> keys(@NonNull View view, @NonNull Predicate<? super KeyEvent> handled) {
        Preconditions.checkNotNull(view, "view == null");
        Preconditions.checkNotNull(handled, "handled == null");
        return new ViewKeyObservable(view, handled);
    }

    @CheckResult
    @NonNull
    @Deprecated
    public static Consumer<? super Boolean> activated(@NonNull View view) {
        Preconditions.checkNotNull(view, "view == null");
        view.getClass();
        return RxView$$Lambda$0.get$Lambda(view);
    }

    @CheckResult
    @NonNull
    @Deprecated
    public static Consumer<? super Boolean> clickable(@NonNull View view) {
        Preconditions.checkNotNull(view, "view == null");
        view.getClass();
        return RxView$$Lambda$1.get$Lambda(view);
    }

    @CheckResult
    @NonNull
    @Deprecated
    public static Consumer<? super Boolean> enabled(@NonNull View view) {
        Preconditions.checkNotNull(view, "view == null");
        view.getClass();
        return RxView$$Lambda$2.get$Lambda(view);
    }

    @CheckResult
    @NonNull
    @Deprecated
    public static Consumer<? super Boolean> pressed(@NonNull View view) {
        Preconditions.checkNotNull(view, "view == null");
        view.getClass();
        return RxView$$Lambda$3.get$Lambda(view);
    }

    @CheckResult
    @NonNull
    @Deprecated
    public static Consumer<? super Boolean> selected(@NonNull View view) {
        Preconditions.checkNotNull(view, "view == null");
        view.getClass();
        return RxView$$Lambda$4.get$Lambda(view);
    }

    @CheckResult
    @NonNull
    public static Consumer<? super Boolean> visibility(@NonNull View view) {
        Preconditions.checkNotNull(view, "view == null");
        return visibility(view, 8);
    }

    @CheckResult
    @NonNull
    public static Consumer<? super Boolean> visibility(@NonNull View view, int visibilityWhenFalse) {
        Preconditions.checkNotNull(view, "view == null");
        if (visibilityWhenFalse == 0) {
            throw new IllegalArgumentException("Setting visibility to VISIBLE when false would have no effect.");
        } else if (visibilityWhenFalse == 4 || visibilityWhenFalse == 8) {
            return new RxView$$Lambda$5(view, visibilityWhenFalse);
        } else {
            throw new IllegalArgumentException("Must set visibility to INVISIBLE or GONE when false.");
        }
    }

    static final /* synthetic */ void lambda$visibility$0$RxView(@NonNull View view, int visibilityWhenFalse, Boolean value) throws Exception {
        if (value.booleanValue()) {
            visibilityWhenFalse = 0;
        }
        view.setVisibility(visibilityWhenFalse);
    }

    private RxView() {
        throw new AssertionError("No instances.");
    }
}
