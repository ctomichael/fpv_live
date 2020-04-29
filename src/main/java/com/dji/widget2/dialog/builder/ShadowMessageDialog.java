package com.dji.widget2.dialog.builder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import com.dji.widget2.R;
import com.dji.widget2.dialog.DialogAttributes;
import com.dji.widget2.dialog.content.standard.interfaces.MessageDialog;
import com.dji.widget2.dialog.content.standard.view.MessageView;

class ShadowMessageDialog extends ShadowBase implements MessageDialog {
    private MessageView mMessageView;

    ShadowMessageDialog(@NonNull Context context, @NonNull DialogAttributes.Theme theme, @NonNull DialogAttributes.Size size) {
        super(context, theme, size);
    }

    public View generateContentView(@NonNull Context context, @NonNull DialogAttributes.Theme theme) {
        this.mMessageView = (MessageView) View.inflate(context, R.layout.app_layout_dialog_message, null);
        this.mMessageView.setTheme(theme);
        return this.mMessageView;
    }

    @NonNull
    public MessageDialog setMainText(@NonNull CharSequence text) {
        this.mMessageView.setMainText(text);
        return this;
    }

    @NonNull
    public MessageDialog setMainText(int resId) {
        this.mMessageView.setMainText(resId);
        return this;
    }

    @NonNull
    public MessageDialog setSubText(@NonNull CharSequence text) {
        this.mMessageView.setSubText(text);
        return this;
    }

    @NonNull
    public MessageDialog setSubTextGravity(int gravity) {
        this.mMessageView.setSubTextGravity(gravity);
        return this;
    }

    @NonNull
    public MessageDialog setSubText(int resId) {
        this.mMessageView.setSubText(resId);
        return this;
    }

    @NonNull
    public MessageDialog setIcon(int resId) {
        this.mMessageView.setIcon(resId);
        return this;
    }
}
