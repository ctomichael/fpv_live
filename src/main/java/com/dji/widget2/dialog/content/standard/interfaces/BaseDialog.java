package com.dji.widget2.dialog.content.standard.interfaces;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.dji.widget2.dialog.action.DialogAction;

public interface BaseDialog extends DialogAction {
    void dismiss();

    @Nullable
    Object getTag();

    boolean isShowing();

    @NonNull
    BaseDialog setCancelable(boolean z);

    @NonNull
    BaseDialog setCanceledTouchOutside(boolean z);

    @NonNull
    BaseDialog setOnCancelListener(@Nullable DialogInterface.OnCancelListener onCancelListener);

    @NonNull
    BaseDialog setOnDismissListener(@Nullable DialogInterface.OnDismissListener onDismissListener);

    @NonNull
    BaseDialog setOnShowListener(@Nullable DialogInterface.OnShowListener onShowListener);

    void setTag(@Nullable Object obj);

    void show();
}
