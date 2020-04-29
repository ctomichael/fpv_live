package com.dji.widget2.dialog.content.standard.interfaces;

import android.content.DialogInterface;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

public interface SelectableListDialog extends BaseDialog {
    @NonNull
    SelectableListDialog setContentListGravity(int i);

    @NonNull
    SelectableListDialog setData(@NonNull CharSequence[] charSequenceArr, int i);

    @NonNull
    SelectableListDialog setData(@NonNull CharSequence[] charSequenceArr, int[] iArr);

    @NonNull
    SelectableListDialog setIcon(@DrawableRes int i);

    @NonNull
    SelectableListDialog setMainText(@StringRes int i);

    @NonNull
    SelectableListDialog setMainText(@NonNull CharSequence charSequence);

    @NonNull
    SelectableListDialog setOnItemClickListener(@Nullable DialogInterface.OnClickListener onClickListener);

    @NonNull
    SelectableListDialog setOnMultiChoiceListener(@Nullable DialogInterface.OnMultiChoiceClickListener onMultiChoiceClickListener);

    @NonNull
    SelectableListDialog setSingleChoiceNAutoDismiss(boolean z);
}
