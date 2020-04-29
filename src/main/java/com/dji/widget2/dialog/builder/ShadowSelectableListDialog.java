package com.dji.widget2.dialog.builder;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import com.dji.widget2.R;
import com.dji.widget2.dialog.DialogAttributes;
import com.dji.widget2.dialog.content.standard.interfaces.SelectableListDialog;
import com.dji.widget2.dialog.content.standard.view.SelectableListView;

class ShadowSelectableListDialog extends ShadowBase implements SelectableListDialog {
    private boolean isSingleChoice = false;
    private DialogInterface.OnClickListener mOnItemClickListener;
    private DialogInterface.OnMultiChoiceClickListener mOnMultiChoiceClickListener;
    private SelectableListView mSelectableListView;

    ShadowSelectableListDialog(@NonNull Context context, @NonNull DialogAttributes.Theme theme, @NonNull DialogAttributes.Size size) {
        super(context, theme, size);
    }

    /* access modifiers changed from: package-private */
    public View generateContentView(@NonNull Context context, @NonNull DialogAttributes.Theme theme) {
        this.mSelectableListView = (SelectableListView) View.inflate(context, R.layout.app_layout_dialog_selectable_list, null);
        this.mSelectableListView.setOnItemClickListener(new ShadowSelectableListDialog$$Lambda$0(this));
        this.mSelectableListView.setOnMultiChoiceClickListener(new ShadowSelectableListDialog$$Lambda$1(this));
        this.mSelectableListView.setTheme(theme);
        return this.mSelectableListView;
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$generateContentView$0$ShadowSelectableListDialog(DialogInterface dialog, int which) {
        if (this.mOnItemClickListener != null) {
            this.mOnItemClickListener.onClick(this.mDialog, which);
        }
        if (this.isSingleChoice) {
            dismiss();
        }
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$generateContentView$1$ShadowSelectableListDialog(DialogInterface dialog, int which, boolean isChecked) {
        if (this.mOnMultiChoiceClickListener != null) {
            this.mOnMultiChoiceClickListener.onClick(dialog, which, isChecked);
        }
    }

    @NonNull
    public SelectableListDialog setIcon(int resId) {
        this.mSelectableListView.setIcon(resId);
        return this;
    }

    @NonNull
    public SelectableListDialog setMainText(int resId) {
        this.mSelectableListView.setMainText(resId);
        return this;
    }

    @NonNull
    public SelectableListDialog setMainText(@NonNull CharSequence text) {
        this.mSelectableListView.setMainText(text);
        return this;
    }

    @NonNull
    public SelectableListDialog setContentListGravity(int gravity) {
        this.mSelectableListView.setContentListGravity(gravity);
        return this;
    }

    @NonNull
    public SelectableListDialog setSingleChoiceNAutoDismiss(boolean _isSingleChoice) {
        this.isSingleChoice = _isSingleChoice;
        this.mSelectableListView.setShowCheckbox(!_isSingleChoice);
        return this;
    }

    @NonNull
    public SelectableListDialog setData(@NonNull CharSequence[] textArray, int selectedIndex) {
        this.mSelectableListView.setData(0, textArray, new int[]{selectedIndex});
        return this;
    }

    @NonNull
    public SelectableListDialog setData(@NonNull CharSequence[] textArray, int[] selectedIndexArray) {
        this.mSelectableListView.setData(1, textArray, selectedIndexArray);
        return this;
    }

    public SelectableListDialog setOnItemClickListener(@Nullable DialogInterface.OnClickListener listener) {
        this.mOnItemClickListener = listener;
        return this;
    }

    public SelectableListDialog setOnMultiChoiceListener(@Nullable DialogInterface.OnMultiChoiceClickListener listener) {
        this.mOnMultiChoiceClickListener = listener;
        return this;
    }
}
