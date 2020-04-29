package com.dji.widget2.dialog.builder;

import android.content.Context;
import android.support.annotation.NonNull;
import com.dji.widget2.dialog.DialogAttributes;
import com.dji.widget2.dialog.content.custom.CustomDialog;
import com.dji.widget2.dialog.content.standard.interfaces.LoadingDialog;
import com.dji.widget2.dialog.content.standard.interfaces.MessageDialog;
import com.dji.widget2.dialog.content.standard.interfaces.ProgressDialog;
import com.dji.widget2.dialog.content.standard.interfaces.SelectableListDialog;

public class DJIDialogV2Builder {
    @NonNull
    public static MessageDialog createMessageDialog(@NonNull Context context, @NonNull DialogAttributes.Theme theme, @NonNull DialogAttributes.Size size) {
        return new ShadowMessageDialog(context, theme, size);
    }

    @NonNull
    public static LoadingDialog createLoadingDialog(@NonNull Context context, @NonNull DialogAttributes.Theme theme, @NonNull DialogAttributes.Size size) {
        return new ShadowLoadingDialog(context, theme, size);
    }

    @NonNull
    public static SelectableListDialog createSelectableListDialog(@NonNull Context context, @NonNull DialogAttributes.Theme theme, @NonNull DialogAttributes.Size size) {
        return new ShadowSelectableListDialog(context, theme, size);
    }

    @NonNull
    public static ProgressDialog createProgressDialog(@NonNull Context context, @NonNull DialogAttributes.Theme theme, DialogAttributes.Size size) {
        return new ShadowProgressDialog(context, theme, size);
    }

    @NonNull
    public static CustomDialog createCustomDialog(@NonNull Context context, @NonNull DialogAttributes.Theme theme, @NonNull DialogAttributes.Size size) {
        return new ShadowCustomDialog(context, theme, size);
    }

    private DJIDialogV2Builder() {
    }
}
