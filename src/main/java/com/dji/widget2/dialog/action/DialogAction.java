package com.dji.widget2.dialog.action;

import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.View;

public interface DialogAction {
    @NonNull
    DialogAction setNegativeButton(@StringRes int i, @Nullable View.OnClickListener onClickListener);

    @NonNull
    DialogAction setNegativeButton(@NonNull CharSequence charSequence, @Nullable View.OnClickListener onClickListener);

    @NonNull
    DialogAction setNegativeButtonClickListener(@Nullable View.OnClickListener onClickListener);

    @NonNull
    DialogAction setNegativeButtonColor(@ColorInt int i);

    @NonNull
    DialogAction setNegativeButtonEnable(boolean z);

    @NonNull
    DialogAction setNegativeButtonText(@StringRes int i);

    @NonNull
    DialogAction setNegativeButtonText(@NonNull CharSequence charSequence);

    @NonNull
    DialogAction setNeutralButton(@StringRes int i, @NonNull View.OnClickListener onClickListener);

    @NonNull
    DialogAction setNeutralButton(@NonNull CharSequence charSequence, @NonNull View.OnClickListener onClickListener);

    @NonNull
    DialogAction setNeutralButtonClickListener(@Nullable View.OnClickListener onClickListener);

    @NonNull
    DialogAction setNeutralButtonColor(@ColorInt int i);

    @NonNull
    DialogAction setNeutralButtonEnable(boolean z);

    @NonNull
    DialogAction setNeutralButtonText(@StringRes int i);

    @NonNull
    DialogAction setNeutralButtonText(@NonNull CharSequence charSequence);

    @NonNull
    DialogAction setPositiveButton(@StringRes int i, @NonNull View.OnClickListener onClickListener);

    @NonNull
    DialogAction setPositiveButton(@NonNull CharSequence charSequence, @NonNull View.OnClickListener onClickListener);

    @NonNull
    DialogAction setPositiveButtonClickListener(@Nullable View.OnClickListener onClickListener);

    @NonNull
    DialogAction setPositiveButtonColor(@ColorInt int i);

    @NonNull
    DialogAction setPositiveButtonEnable(boolean z);

    @NonNull
    DialogAction setPositiveButtonText(@StringRes int i);

    @NonNull
    DialogAction setPositiveButtonText(@NonNull CharSequence charSequence);
}
