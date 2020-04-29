package com.dji.widget2.dialog.content.standard.interfaces;

import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

public interface ProgressDialog extends BaseDialog {
    @NonNull
    ProgressDialog setAutoUpdateProgressText(boolean z);

    @NonNull
    ProgressDialog setMainText(@StringRes int i);

    @NonNull
    ProgressDialog setMainText(@NonNull CharSequence charSequence);

    @NonNull
    ProgressDialog setProgressBackgroundColor(@ColorInt int i);

    @NonNull
    ProgressDialog setProgressColor(@ColorInt int i);

    @NonNull
    ProgressDialog setProgressPercent(@FloatRange(from = 0.0d, to = 1.0d) float f);

    @NonNull
    ProgressDialog setProgressText(@NonNull CharSequence charSequence);

    @NonNull
    ProgressDialog setProgressTextColor(@ColorInt int i);

    @NonNull
    ProgressDialog setShowProgressText(boolean z);

    @NonNull
    ProgressDialog setSubText(@StringRes int i);

    @NonNull
    ProgressDialog setSubText(@NonNull CharSequence charSequence);
}
