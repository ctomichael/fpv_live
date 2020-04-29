package com.dji.widget2.dialog.content.standard.interfaces;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

public interface LoadingDialog extends BaseDialog {
    @NonNull
    LoadingDialog setContent(@StringRes int i);

    @NonNull
    LoadingDialog setContent(@NonNull CharSequence charSequence);
}
