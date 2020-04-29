package com.jakewharton.rxbinding2.view;

import android.support.annotation.NonNull;
import android.view.MenuItem;

public abstract class MenuItemActionViewEvent {
    @NonNull
    public abstract MenuItem menuItem();

    MenuItemActionViewEvent() {
    }
}
