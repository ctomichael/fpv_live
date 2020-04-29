package com.dji.widget2.dialog.builder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.dji.widget2.dialog.DialogAttributes;
import com.dji.widget2.dialog.content.custom.CustomDialog;

class ShadowCustomDialog extends ShadowBase implements CustomDialog {
    private FrameLayout mContentView;
    private DialogAttributes.Theme mTheme;

    ShadowCustomDialog(@NonNull Context context, @NonNull DialogAttributes.Theme theme, @NonNull DialogAttributes.Size size) {
        super(context, theme, size);
    }

    public View generateContentView(@NonNull Context context, @NonNull DialogAttributes.Theme theme) {
        this.mContentView = new FrameLayout(context);
        this.mTheme = theme;
        return this.mContentView;
    }

    public CustomDialog setView(@NonNull View view, @NonNull ViewGroup.LayoutParams layoutParams) {
        if (layoutParams.height == -1) {
            layoutParams.height = getContentMaxHeight();
        }
        this.mContentView.removeAllViews();
        this.mContentView.addView(view, layoutParams);
        return this;
    }
}
