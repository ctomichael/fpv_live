package com.dji.widget2.dialog.builder;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import com.dji.widget2.R;
import com.dji.widget2.dialog.DialogAttributes;
import com.dji.widget2.dialog.action.DialogActionView;
import com.dji.widget2.dialog.content.standard.interfaces.BaseDialog;
import org.jetbrains.annotations.NotNull;

abstract class ShadowBase implements BaseDialog {
    private DialogActionView mActionView;
    private Context mContext;
    Dialog mDialog;
    private DialogAttributes.Size mSize;

    /* access modifiers changed from: package-private */
    public abstract View generateContentView(@NonNull Context context, @NonNull DialogAttributes.Theme theme);

    ShadowBase(@NonNull Context context, @NonNull DialogAttributes.Theme theme, @NonNull DialogAttributes.Size size) {
        this.mContext = context;
        this.mSize = size;
        this.mActionView = new DialogActionView(context, null);
        this.mActionView.setTheme(theme);
        LinearLayout container = new LinearLayout(context);
        container.setOrientation(1);
        container.addView(generateContentView(context, theme), new ViewGroup.LayoutParams(-1, -2));
        container.addView(this.mActionView, new ViewGroup.LayoutParams(-1, -2));
        this.mDialog = new Dialog(context, R.style.Dialog2) {
            /* class com.dji.widget2.dialog.builder.ShadowBase.AnonymousClass1 */

            public void show() {
                if (getWindow() != null) {
                    getWindow().getDecorView().setSystemUiVisibility(5638);
                }
                super.show();
            }

            public void dismiss() {
                Window window = getWindow();
                if (window != null && window.getDecorView().getParent() != null) {
                    super.dismiss();
                }
            }
        };
        this.mDialog.addContentView(container, new ViewGroup.LayoutParams(context.getResources().getDimensionPixelSize(size.width), -2));
        configure(container, theme);
    }

    public void setTag(@Nullable Object object) {
        this.mActionView.setTag(object);
    }

    @Nullable
    public Object getTag() {
        return this.mActionView.getTag();
    }

    public boolean isShowing() {
        return this.mDialog.isShowing();
    }

    public void show() {
        this.mDialog.show();
    }

    public void dismiss() {
        this.mDialog.dismiss();
    }

    @NotNull
    public BaseDialog setCancelable(boolean flag) {
        this.mDialog.setCancelable(flag);
        return this;
    }

    @NonNull
    public BaseDialog setCanceledTouchOutside(boolean canceledTouchOutside) {
        this.mDialog.setCanceledOnTouchOutside(canceledTouchOutside);
        return this;
    }

    @NonNull
    public BaseDialog setOnShowListener(@Nullable DialogInterface.OnShowListener listener) {
        this.mDialog.setOnShowListener(listener);
        return this;
    }

    @NonNull
    public BaseDialog setOnDismissListener(@Nullable DialogInterface.OnDismissListener listener) {
        this.mDialog.setOnDismissListener(listener);
        return this;
    }

    @NonNull
    public BaseDialog setOnCancelListener(@Nullable DialogInterface.OnCancelListener listener) {
        this.mDialog.setOnCancelListener(listener);
        return this;
    }

    @NonNull
    public BaseDialog setNegativeButton(int resId, @Nullable View.OnClickListener listener) {
        this.mActionView.setNegativeButton(resId, listener);
        return this;
    }

    @NonNull
    public BaseDialog setNegativeButton(@NonNull CharSequence text, @Nullable View.OnClickListener listener) {
        this.mActionView.setNegativeButton(text, listener);
        return this;
    }

    @NonNull
    public BaseDialog setNegativeButtonClickListener(@Nullable View.OnClickListener listener) {
        this.mActionView.setNegativeButtonClickListener(listener);
        return this;
    }

    @NonNull
    public BaseDialog setNegativeButtonColor(int color) {
        this.mActionView.setNegativeButtonColor(color);
        return this;
    }

    @NonNull
    public BaseDialog setNegativeButtonEnable(boolean enable) {
        this.mActionView.setNegativeButtonEnable(enable);
        return this;
    }

    @NonNull
    public BaseDialog setNegativeButtonText(int resId) {
        this.mActionView.setNegativeButtonText(resId);
        return this;
    }

    @NonNull
    public BaseDialog setNegativeButtonText(@NonNull CharSequence text) {
        this.mActionView.setNegativeButtonText(text);
        return this;
    }

    @NonNull
    public BaseDialog setPositiveButton(int resId, @NonNull View.OnClickListener listener) {
        this.mActionView.setPositiveButton(resId, listener);
        return this;
    }

    @NonNull
    public BaseDialog setPositiveButton(@NonNull CharSequence text, @NonNull View.OnClickListener listener) {
        this.mActionView.setPositiveButton(text, listener);
        return this;
    }

    @NonNull
    public BaseDialog setPositiveButtonClickListener(@Nullable View.OnClickListener listener) {
        this.mActionView.setPositiveButtonClickListener(listener);
        return this;
    }

    @NonNull
    public BaseDialog setPositiveButtonColor(int color) {
        this.mActionView.setPositiveButtonColor(color);
        return this;
    }

    @NonNull
    public BaseDialog setPositiveButtonEnable(boolean enable) {
        this.mActionView.setPositiveButtonEnable(enable);
        return this;
    }

    @NonNull
    public BaseDialog setPositiveButtonText(int resId) {
        this.mActionView.setPositiveButtonText(resId);
        return this;
    }

    @NonNull
    public BaseDialog setPositiveButtonText(@NonNull CharSequence text) {
        this.mActionView.setPositiveButtonText(text);
        return this;
    }

    @NonNull
    public BaseDialog setNeutralButton(int resId, @NonNull View.OnClickListener listener) {
        this.mActionView.setNeutralButton(resId, listener);
        return this;
    }

    @NonNull
    public BaseDialog setNeutralButton(@NonNull CharSequence text, @NonNull View.OnClickListener listener) {
        this.mActionView.setNeutralButton(text, listener);
        return this;
    }

    @NonNull
    public BaseDialog setNeutralButtonClickListener(@Nullable View.OnClickListener listener) {
        this.mActionView.setNeutralButtonClickListener(listener);
        return this;
    }

    @NonNull
    public BaseDialog setNeutralButtonColor(int color) {
        this.mActionView.setNeutralButtonColor(color);
        return this;
    }

    @NonNull
    public BaseDialog setNeutralButtonEnable(boolean enable) {
        this.mActionView.setNeutralButtonEnable(enable);
        return this;
    }

    @NonNull
    public BaseDialog setNeutralButtonText(int resId) {
        this.mActionView.setNeutralButtonText(resId);
        return this;
    }

    @NonNull
    public BaseDialog setNeutralButtonText(@NonNull CharSequence text) {
        this.mActionView.setNeutralButtonText(text);
        return this;
    }

    /* access modifiers changed from: package-private */
    public int getContentMaxHeight() {
        this.mActionView.measure(View.MeasureSpec.makeMeasureSpec(this.mSize.width, 1073741824), View.MeasureSpec.makeMeasureSpec(this.mContext.getResources().getDimensionPixelSize(R.dimen.s_52_dp), 1073741824));
        return this.mActionView.getResources().getDimensionPixelSize(this.mSize.maxHeight) - this.mActionView.getMeasuredHeight();
    }

    private void configure(@NonNull View view, @NonNull DialogAttributes.Theme theme) {
        switch (theme) {
            case LIGHT:
                view.setBackgroundResource(R.color.white);
                view.getBackground().setAlpha(255);
                return;
            case FPV:
                view.setBackgroundResource(R.color.grey_10);
                view.getBackground().setAlpha(229);
                return;
            default:
                return;
        }
    }
}
