package com.dji.widget2.dialog.content.standard.interfaces;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

public interface MessageDialog extends BaseDialog {
    @NonNull
    MessageDialog setIcon(@DrawableRes int i);

    @NonNull
    MessageDialog setMainText(@StringRes int i);

    @NonNull
    MessageDialog setMainText(@NonNull CharSequence charSequence);

    @NonNull
    MessageDialog setSubText(@StringRes int i);

    @NonNull
    MessageDialog setSubText(@NonNull CharSequence charSequence);

    @NonNull
    MessageDialog setSubTextGravity(int i);
}
