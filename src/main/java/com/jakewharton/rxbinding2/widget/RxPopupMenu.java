package com.jakewharton.rxbinding2.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.widget.PopupMenu;
import com.jakewharton.rxbinding2.internal.Preconditions;
import io.reactivex.Observable;

public final class RxPopupMenu {
    @CheckResult
    @NonNull
    public static Observable<MenuItem> itemClicks(@NonNull PopupMenu view) {
        Preconditions.checkNotNull(view, "view == null");
        return new PopupMenuItemClickObservable(view);
    }

    @CheckResult
    @NonNull
    public static Observable<Object> dismisses(@NonNull PopupMenu view) {
        Preconditions.checkNotNull(view, "view == null");
        return new PopupMenuDismissObservable(view);
    }

    private RxPopupMenu() {
        throw new AssertionError("No instances.");
    }
}
