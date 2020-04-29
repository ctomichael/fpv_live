package com.dji.widget2.dialog.content.custom;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import com.dji.widget2.dialog.content.standard.interfaces.BaseDialog;

public interface CustomDialog extends BaseDialog {
    CustomDialog setView(@NonNull View view, @NonNull ViewGroup.LayoutParams layoutParams);
}
