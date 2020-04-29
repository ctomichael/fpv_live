package dji.publics.widget.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.IdRes;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import dji.publics.DJIUI.DJIEditText;
import dji.publics.widget.dialog.DJIDialog;
import dji.publics.widget.util.Utils;

public class DJIEditDialog extends DJIDialog {
    private DJIEditText mEditView;

    public interface OnEditClickListener {
        void onClick(DialogInterface dialogInterface, int i, String str);
    }

    public DJIEditDialog(Context context) {
        super(context);
        initEditView();
    }

    public DJIEditDialog(Context context, DJIDialogType dialogType, DJIDialog.DJIDialogTheme theme) {
        super(context, dialogType, theme);
        initEditView();
    }

    private void initEditView() {
        View view = LayoutInflater.from(this.mContext).inflate(R.layout.dlg_edit_view, (ViewGroup) null);
        this.mEditView = (DJIEditText) view.findViewById(R.id.dlg_editview);
        if (this.mTheme == DJIDialog.DJIDialogTheme.WHITE) {
            this.mEditView.setTextColor(this.mContext.getResources().getColor(R.color.black));
            this.mEditView.setBackgroundDrawable(this.mContext.getResources().getDrawable(R.drawable.dlg_editview_bg_theme_white));
        } else {
            this.mEditView.setTextColor(this.mContext.getResources().getColor(R.color.white));
            this.mEditView.setBackgroundDrawable(this.mContext.getResources().getDrawable(R.drawable.dlg_editview_bg_theme_black));
        }
        setCustomView(view);
        updateDividerHeight(this.mTitleContentMargin, Utils.getDimens(this.mContext, R.dimen.new_dialog_margin_left));
    }

    public void setEditHint(CharSequence msg) {
        this.mEditView.setHint(msg);
    }

    public void setLeftBtnOfEdit(String name, final OnEditClickListener listener) {
        setLeftBtn(name, new DialogInterface.OnClickListener() {
            /* class dji.publics.widget.dialog.DJIEditDialog.AnonymousClass1 */

            public void onClick(DialogInterface dialog, int which) {
                listener.onClick(dialog, which, DJIEditDialog.this.getEditText());
            }
        });
    }

    public void setRightBtnOfEdit(String name, final OnEditClickListener listener) {
        setRightBtn(name, new DialogInterface.OnClickListener() {
            /* class dji.publics.widget.dialog.DJIEditDialog.AnonymousClass2 */

            public void onClick(DialogInterface dialog, int which) {
                listener.onClick(dialog, which, DJIEditDialog.this.getEditText());
            }
        });
    }

    public String getEditText() {
        return this.mEditView.getText().toString();
    }

    public void setEditText(String text) {
        this.mEditView.setText(text);
    }

    public void setEditTextColor(@IdRes int colorId) {
        this.mEditView.setTextColor(getContext().getResources().getColor(colorId));
    }

    public void setEditTextMaxInput(int lengthLimit) {
        this.mEditView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(lengthLimit)});
    }

    public void dismiss() {
        super.dismiss();
        this.mEditView.setText("");
    }
}
