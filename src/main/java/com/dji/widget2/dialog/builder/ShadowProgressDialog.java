package com.dji.widget2.dialog.builder;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import com.dji.widget2.R;
import com.dji.widget2.dialog.DialogAttributes;
import com.dji.widget2.dialog.content.standard.interfaces.BaseDialog;
import com.dji.widget2.dialog.content.standard.interfaces.ProgressDialog;
import com.dji.widget2.dialog.content.standard.view.ProgressView;
import org.jetbrains.annotations.NotNull;

public class ShadowProgressDialog extends ShadowBase implements ProgressDialog {
    private ProgressView mProgressView;

    public /* bridge */ /* synthetic */ void dismiss() {
        super.dismiss();
    }

    @Nullable
    public /* bridge */ /* synthetic */ Object getTag() {
        return super.getTag();
    }

    public /* bridge */ /* synthetic */ boolean isShowing() {
        return super.isShowing();
    }

    @NotNull
    public /* bridge */ /* synthetic */ BaseDialog setCancelable(boolean z) {
        return super.setCancelable(z);
    }

    @NonNull
    public /* bridge */ /* synthetic */ BaseDialog setCanceledTouchOutside(boolean z) {
        return super.setCanceledTouchOutside(z);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.dji.widget2.dialog.builder.ShadowBase.setNegativeButton(int, android.view.View$OnClickListener):com.dji.widget2.dialog.content.standard.interfaces.BaseDialog
     arg types: [int, android.view.View$OnClickListener]
     candidates:
      com.dji.widget2.dialog.builder.ShadowProgressDialog.setNegativeButton(java.lang.CharSequence, android.view.View$OnClickListener):com.dji.widget2.dialog.content.standard.interfaces.BaseDialog
      com.dji.widget2.dialog.builder.ShadowBase.setNegativeButton(int, android.view.View$OnClickListener):com.dji.widget2.dialog.action.DialogAction
      com.dji.widget2.dialog.builder.ShadowBase.setNegativeButton(java.lang.CharSequence, android.view.View$OnClickListener):com.dji.widget2.dialog.action.DialogAction
      com.dji.widget2.dialog.builder.ShadowBase.setNegativeButton(java.lang.CharSequence, android.view.View$OnClickListener):com.dji.widget2.dialog.content.standard.interfaces.BaseDialog
      com.dji.widget2.dialog.action.DialogAction.setNegativeButton(int, android.view.View$OnClickListener):com.dji.widget2.dialog.action.DialogAction
      com.dji.widget2.dialog.action.DialogAction.setNegativeButton(java.lang.CharSequence, android.view.View$OnClickListener):com.dji.widget2.dialog.action.DialogAction
      com.dji.widget2.dialog.action.DialogAction.setNegativeButton(int, android.view.View$OnClickListener):com.dji.widget2.dialog.action.DialogAction
      com.dji.widget2.dialog.action.DialogAction.setNegativeButton(java.lang.CharSequence, android.view.View$OnClickListener):com.dji.widget2.dialog.action.DialogAction
      com.dji.widget2.dialog.builder.ShadowBase.setNegativeButton(int, android.view.View$OnClickListener):com.dji.widget2.dialog.content.standard.interfaces.BaseDialog */
    @NonNull
    public /* bridge */ /* synthetic */ BaseDialog setNegativeButton(int i, @Nullable View.OnClickListener onClickListener) {
        return super.setNegativeButton(i, onClickListener);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.dji.widget2.dialog.builder.ShadowBase.setNegativeButton(java.lang.CharSequence, android.view.View$OnClickListener):com.dji.widget2.dialog.content.standard.interfaces.BaseDialog
     arg types: [java.lang.CharSequence, android.view.View$OnClickListener]
     candidates:
      com.dji.widget2.dialog.builder.ShadowProgressDialog.setNegativeButton(int, android.view.View$OnClickListener):com.dji.widget2.dialog.content.standard.interfaces.BaseDialog
      com.dji.widget2.dialog.builder.ShadowBase.setNegativeButton(int, android.view.View$OnClickListener):com.dji.widget2.dialog.action.DialogAction
      com.dji.widget2.dialog.builder.ShadowBase.setNegativeButton(java.lang.CharSequence, android.view.View$OnClickListener):com.dji.widget2.dialog.action.DialogAction
      com.dji.widget2.dialog.builder.ShadowBase.setNegativeButton(int, android.view.View$OnClickListener):com.dji.widget2.dialog.content.standard.interfaces.BaseDialog
      com.dji.widget2.dialog.action.DialogAction.setNegativeButton(int, android.view.View$OnClickListener):com.dji.widget2.dialog.action.DialogAction
      com.dji.widget2.dialog.action.DialogAction.setNegativeButton(java.lang.CharSequence, android.view.View$OnClickListener):com.dji.widget2.dialog.action.DialogAction
      com.dji.widget2.dialog.action.DialogAction.setNegativeButton(int, android.view.View$OnClickListener):com.dji.widget2.dialog.action.DialogAction
      com.dji.widget2.dialog.action.DialogAction.setNegativeButton(java.lang.CharSequence, android.view.View$OnClickListener):com.dji.widget2.dialog.action.DialogAction
      com.dji.widget2.dialog.builder.ShadowBase.setNegativeButton(java.lang.CharSequence, android.view.View$OnClickListener):com.dji.widget2.dialog.content.standard.interfaces.BaseDialog */
    @NonNull
    public /* bridge */ /* synthetic */ BaseDialog setNegativeButton(@NonNull CharSequence charSequence, @Nullable View.OnClickListener onClickListener) {
        return super.setNegativeButton(charSequence, onClickListener);
    }

    @NonNull
    public /* bridge */ /* synthetic */ BaseDialog setNegativeButtonClickListener(@Nullable View.OnClickListener onClickListener) {
        return super.setNegativeButtonClickListener(onClickListener);
    }

    @NonNull
    public /* bridge */ /* synthetic */ BaseDialog setNegativeButtonColor(int i) {
        return super.setNegativeButtonColor(i);
    }

    @NonNull
    public /* bridge */ /* synthetic */ BaseDialog setNegativeButtonEnable(boolean z) {
        return super.setNegativeButtonEnable(z);
    }

    @NonNull
    public /* bridge */ /* synthetic */ BaseDialog setNegativeButtonText(int i) {
        return super.setNegativeButtonText(i);
    }

    @NonNull
    public /* bridge */ /* synthetic */ BaseDialog setNegativeButtonText(@NonNull CharSequence charSequence) {
        return super.setNegativeButtonText(charSequence);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.dji.widget2.dialog.builder.ShadowBase.setNeutralButton(int, android.view.View$OnClickListener):com.dji.widget2.dialog.content.standard.interfaces.BaseDialog
     arg types: [int, android.view.View$OnClickListener]
     candidates:
      com.dji.widget2.dialog.builder.ShadowProgressDialog.setNeutralButton(java.lang.CharSequence, android.view.View$OnClickListener):com.dji.widget2.dialog.content.standard.interfaces.BaseDialog
      com.dji.widget2.dialog.builder.ShadowBase.setNeutralButton(int, android.view.View$OnClickListener):com.dji.widget2.dialog.action.DialogAction
      com.dji.widget2.dialog.builder.ShadowBase.setNeutralButton(java.lang.CharSequence, android.view.View$OnClickListener):com.dji.widget2.dialog.action.DialogAction
      com.dji.widget2.dialog.builder.ShadowBase.setNeutralButton(java.lang.CharSequence, android.view.View$OnClickListener):com.dji.widget2.dialog.content.standard.interfaces.BaseDialog
      com.dji.widget2.dialog.action.DialogAction.setNeutralButton(int, android.view.View$OnClickListener):com.dji.widget2.dialog.action.DialogAction
      com.dji.widget2.dialog.action.DialogAction.setNeutralButton(java.lang.CharSequence, android.view.View$OnClickListener):com.dji.widget2.dialog.action.DialogAction
      com.dji.widget2.dialog.action.DialogAction.setNeutralButton(int, android.view.View$OnClickListener):com.dji.widget2.dialog.action.DialogAction
      com.dji.widget2.dialog.action.DialogAction.setNeutralButton(java.lang.CharSequence, android.view.View$OnClickListener):com.dji.widget2.dialog.action.DialogAction
      com.dji.widget2.dialog.builder.ShadowBase.setNeutralButton(int, android.view.View$OnClickListener):com.dji.widget2.dialog.content.standard.interfaces.BaseDialog */
    @NonNull
    public /* bridge */ /* synthetic */ BaseDialog setNeutralButton(int i, @NonNull View.OnClickListener onClickListener) {
        return super.setNeutralButton(i, onClickListener);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.dji.widget2.dialog.builder.ShadowBase.setNeutralButton(java.lang.CharSequence, android.view.View$OnClickListener):com.dji.widget2.dialog.content.standard.interfaces.BaseDialog
     arg types: [java.lang.CharSequence, android.view.View$OnClickListener]
     candidates:
      com.dji.widget2.dialog.builder.ShadowProgressDialog.setNeutralButton(int, android.view.View$OnClickListener):com.dji.widget2.dialog.content.standard.interfaces.BaseDialog
      com.dji.widget2.dialog.builder.ShadowBase.setNeutralButton(int, android.view.View$OnClickListener):com.dji.widget2.dialog.action.DialogAction
      com.dji.widget2.dialog.builder.ShadowBase.setNeutralButton(java.lang.CharSequence, android.view.View$OnClickListener):com.dji.widget2.dialog.action.DialogAction
      com.dji.widget2.dialog.builder.ShadowBase.setNeutralButton(int, android.view.View$OnClickListener):com.dji.widget2.dialog.content.standard.interfaces.BaseDialog
      com.dji.widget2.dialog.action.DialogAction.setNeutralButton(int, android.view.View$OnClickListener):com.dji.widget2.dialog.action.DialogAction
      com.dji.widget2.dialog.action.DialogAction.setNeutralButton(java.lang.CharSequence, android.view.View$OnClickListener):com.dji.widget2.dialog.action.DialogAction
      com.dji.widget2.dialog.action.DialogAction.setNeutralButton(int, android.view.View$OnClickListener):com.dji.widget2.dialog.action.DialogAction
      com.dji.widget2.dialog.action.DialogAction.setNeutralButton(java.lang.CharSequence, android.view.View$OnClickListener):com.dji.widget2.dialog.action.DialogAction
      com.dji.widget2.dialog.builder.ShadowBase.setNeutralButton(java.lang.CharSequence, android.view.View$OnClickListener):com.dji.widget2.dialog.content.standard.interfaces.BaseDialog */
    @NonNull
    public /* bridge */ /* synthetic */ BaseDialog setNeutralButton(@NonNull CharSequence charSequence, @NonNull View.OnClickListener onClickListener) {
        return super.setNeutralButton(charSequence, onClickListener);
    }

    @NonNull
    public /* bridge */ /* synthetic */ BaseDialog setNeutralButtonClickListener(@Nullable View.OnClickListener onClickListener) {
        return super.setNeutralButtonClickListener(onClickListener);
    }

    @NonNull
    public /* bridge */ /* synthetic */ BaseDialog setNeutralButtonColor(int i) {
        return super.setNeutralButtonColor(i);
    }

    @NonNull
    public /* bridge */ /* synthetic */ BaseDialog setNeutralButtonEnable(boolean z) {
        return super.setNeutralButtonEnable(z);
    }

    @NonNull
    public /* bridge */ /* synthetic */ BaseDialog setNeutralButtonText(int i) {
        return super.setNeutralButtonText(i);
    }

    @NonNull
    public /* bridge */ /* synthetic */ BaseDialog setNeutralButtonText(@NonNull CharSequence charSequence) {
        return super.setNeutralButtonText(charSequence);
    }

    @NonNull
    public /* bridge */ /* synthetic */ BaseDialog setOnCancelListener(@Nullable DialogInterface.OnCancelListener onCancelListener) {
        return super.setOnCancelListener(onCancelListener);
    }

    @NonNull
    public /* bridge */ /* synthetic */ BaseDialog setOnDismissListener(@Nullable DialogInterface.OnDismissListener onDismissListener) {
        return super.setOnDismissListener(onDismissListener);
    }

    @NonNull
    public /* bridge */ /* synthetic */ BaseDialog setOnShowListener(@Nullable DialogInterface.OnShowListener onShowListener) {
        return super.setOnShowListener(onShowListener);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.dji.widget2.dialog.builder.ShadowBase.setPositiveButton(int, android.view.View$OnClickListener):com.dji.widget2.dialog.content.standard.interfaces.BaseDialog
     arg types: [int, android.view.View$OnClickListener]
     candidates:
      com.dji.widget2.dialog.builder.ShadowProgressDialog.setPositiveButton(java.lang.CharSequence, android.view.View$OnClickListener):com.dji.widget2.dialog.content.standard.interfaces.BaseDialog
      com.dji.widget2.dialog.builder.ShadowBase.setPositiveButton(int, android.view.View$OnClickListener):com.dji.widget2.dialog.action.DialogAction
      com.dji.widget2.dialog.builder.ShadowBase.setPositiveButton(java.lang.CharSequence, android.view.View$OnClickListener):com.dji.widget2.dialog.action.DialogAction
      com.dji.widget2.dialog.builder.ShadowBase.setPositiveButton(java.lang.CharSequence, android.view.View$OnClickListener):com.dji.widget2.dialog.content.standard.interfaces.BaseDialog
      com.dji.widget2.dialog.action.DialogAction.setPositiveButton(int, android.view.View$OnClickListener):com.dji.widget2.dialog.action.DialogAction
      com.dji.widget2.dialog.action.DialogAction.setPositiveButton(java.lang.CharSequence, android.view.View$OnClickListener):com.dji.widget2.dialog.action.DialogAction
      com.dji.widget2.dialog.action.DialogAction.setPositiveButton(int, android.view.View$OnClickListener):com.dji.widget2.dialog.action.DialogAction
      com.dji.widget2.dialog.action.DialogAction.setPositiveButton(java.lang.CharSequence, android.view.View$OnClickListener):com.dji.widget2.dialog.action.DialogAction
      com.dji.widget2.dialog.builder.ShadowBase.setPositiveButton(int, android.view.View$OnClickListener):com.dji.widget2.dialog.content.standard.interfaces.BaseDialog */
    @NonNull
    public /* bridge */ /* synthetic */ BaseDialog setPositiveButton(int i, @NonNull View.OnClickListener onClickListener) {
        return super.setPositiveButton(i, onClickListener);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.dji.widget2.dialog.builder.ShadowBase.setPositiveButton(java.lang.CharSequence, android.view.View$OnClickListener):com.dji.widget2.dialog.content.standard.interfaces.BaseDialog
     arg types: [java.lang.CharSequence, android.view.View$OnClickListener]
     candidates:
      com.dji.widget2.dialog.builder.ShadowProgressDialog.setPositiveButton(int, android.view.View$OnClickListener):com.dji.widget2.dialog.content.standard.interfaces.BaseDialog
      com.dji.widget2.dialog.builder.ShadowBase.setPositiveButton(int, android.view.View$OnClickListener):com.dji.widget2.dialog.action.DialogAction
      com.dji.widget2.dialog.builder.ShadowBase.setPositiveButton(java.lang.CharSequence, android.view.View$OnClickListener):com.dji.widget2.dialog.action.DialogAction
      com.dji.widget2.dialog.builder.ShadowBase.setPositiveButton(int, android.view.View$OnClickListener):com.dji.widget2.dialog.content.standard.interfaces.BaseDialog
      com.dji.widget2.dialog.action.DialogAction.setPositiveButton(int, android.view.View$OnClickListener):com.dji.widget2.dialog.action.DialogAction
      com.dji.widget2.dialog.action.DialogAction.setPositiveButton(java.lang.CharSequence, android.view.View$OnClickListener):com.dji.widget2.dialog.action.DialogAction
      com.dji.widget2.dialog.action.DialogAction.setPositiveButton(int, android.view.View$OnClickListener):com.dji.widget2.dialog.action.DialogAction
      com.dji.widget2.dialog.action.DialogAction.setPositiveButton(java.lang.CharSequence, android.view.View$OnClickListener):com.dji.widget2.dialog.action.DialogAction
      com.dji.widget2.dialog.builder.ShadowBase.setPositiveButton(java.lang.CharSequence, android.view.View$OnClickListener):com.dji.widget2.dialog.content.standard.interfaces.BaseDialog */
    @NonNull
    public /* bridge */ /* synthetic */ BaseDialog setPositiveButton(@NonNull CharSequence charSequence, @NonNull View.OnClickListener onClickListener) {
        return super.setPositiveButton(charSequence, onClickListener);
    }

    @NonNull
    public /* bridge */ /* synthetic */ BaseDialog setPositiveButtonClickListener(@Nullable View.OnClickListener onClickListener) {
        return super.setPositiveButtonClickListener(onClickListener);
    }

    @NonNull
    public /* bridge */ /* synthetic */ BaseDialog setPositiveButtonColor(int i) {
        return super.setPositiveButtonColor(i);
    }

    @NonNull
    public /* bridge */ /* synthetic */ BaseDialog setPositiveButtonEnable(boolean z) {
        return super.setPositiveButtonEnable(z);
    }

    @NonNull
    public /* bridge */ /* synthetic */ BaseDialog setPositiveButtonText(int i) {
        return super.setPositiveButtonText(i);
    }

    @NonNull
    public /* bridge */ /* synthetic */ BaseDialog setPositiveButtonText(@NonNull CharSequence charSequence) {
        return super.setPositiveButtonText(charSequence);
    }

    public /* bridge */ /* synthetic */ void setTag(@Nullable Object obj) {
        super.setTag(obj);
    }

    public /* bridge */ /* synthetic */ void show() {
        super.show();
    }

    ShadowProgressDialog(@NonNull Context context, @NonNull DialogAttributes.Theme theme, @NonNull DialogAttributes.Size size) {
        super(context, theme, size);
    }

    /* access modifiers changed from: package-private */
    public View generateContentView(@NonNull Context context, @NonNull DialogAttributes.Theme theme) {
        this.mProgressView = (ProgressView) View.inflate(context, R.layout.app_layout_dialog_progress, null);
        this.mProgressView.setTheme(theme);
        return this.mProgressView;
    }

    @NonNull
    public ProgressDialog setProgressPercent(float percent) {
        this.mProgressView.setProgress(percent);
        return this;
    }

    @NonNull
    public ProgressDialog setProgressColor(int color) {
        this.mProgressView.setProgressColor(color);
        return this;
    }

    @NonNull
    public ProgressDialog setProgressBackgroundColor(int color) {
        this.mProgressView.setProgressBackgroundColor(color);
        return this;
    }

    @NonNull
    public ProgressDialog setProgressText(@NonNull CharSequence text) {
        this.mProgressView.setProgressText(text);
        return this;
    }

    @NonNull
    public ProgressDialog setProgressTextColor(@ColorInt int color) {
        this.mProgressView.setProgressTextColor(color);
        return this;
    }

    @NonNull
    public ProgressDialog setShowProgressText(boolean isShown) {
        this.mProgressView.setShowProgressText(isShown);
        return this;
    }

    @NonNull
    public ProgressDialog setAutoUpdateProgressText(boolean isAutoUpdate) {
        this.mProgressView.setAutoUpdateProgressText(isAutoUpdate);
        return this;
    }

    @NonNull
    public ProgressDialog setMainText(int resId) {
        this.mProgressView.setMainText(resId);
        return this;
    }

    @NonNull
    public ProgressDialog setMainText(@NonNull CharSequence text) {
        this.mProgressView.setMainText(text);
        return this;
    }

    @NonNull
    public ProgressDialog setSubText(@NonNull CharSequence text) {
        this.mProgressView.setSubText(text);
        return this;
    }

    @NonNull
    public ProgressDialog setSubText(int resId) {
        this.mProgressView.setSubText(resId);
        return this;
    }
}
