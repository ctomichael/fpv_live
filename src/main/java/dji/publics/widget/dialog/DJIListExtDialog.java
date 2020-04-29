package dji.publics.widget.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import dji.publics.widget.DJIScrollTextView;
import dji.publics.widget.dialog.DJIDialog;
import dji.publics.widget.util.Utils;

public class DJIListExtDialog extends DJIListDialog {
    private CheckBox mCheckBoxView;
    private DJIScrollTextView mHelpTextView;

    public DJIListExtDialog(Context context) {
        super(context);
        initExtView();
    }

    public DJIListExtDialog(Context context, DJIDialogType dialogType, DJIDialog.DJIDialogTheme theme) {
        super(context, dialogType, theme);
        initExtView();
    }

    private void initExtView() {
        View view = LayoutInflater.from(this.mContext).inflate(R.layout.dlg_ext_custom_view, (ViewGroup) null);
        this.mHelpTextView = (DJIScrollTextView) view.findViewById(R.id.dlg_helptext_below);
        this.mCheckBoxView = (CheckBox) view.findViewById(R.id.dlg_checkbox_below);
        addView(view);
    }

    public void setHelpText(CharSequence msg) {
        this.mHelpTextView.setText(msg);
        this.mHelpTextView.setVisibility(0);
        this.mContentCustomMargin.setVisibility(0);
    }

    public void setHelpTextWithUnderLine(boolean showLine) {
        if (showLine) {
            this.mHelpTextView.getPaint().setFlags(8);
            this.mHelpTextView.getPaint().setAntiAlias(true);
            return;
        }
        this.mHelpTextView.getPaint().setFlags(1);
    }

    public void setOnHelpTextClickListener(final DialogInterface.OnClickListener listener) {
        this.mHelpTextView.setOnClickListener(new View.OnClickListener() {
            /* class dji.publics.widget.dialog.DJIListExtDialog.AnonymousClass1 */

            public void onClick(View v) {
                listener.onClick(DJIListExtDialog.this, v.getId());
            }
        });
    }

    public boolean isChecked() {
        return this.mCheckBoxView.isChecked();
    }

    public void setCheckText(CharSequence msg) {
        adjustCheckBoxDrawable();
        initContentColor(this.mCheckBoxView);
        this.mCheckBoxView.setText(msg);
        this.mCheckBoxView.setVisibility(0);
        updateDividerHeight(this.mTitleContentMargin, Utils.getDimens(this.mContext, R.dimen.new_dialog_margin_left));
    }

    public void setOnCheckedChangeListener(final CompoundButton.OnCheckedChangeListener listener) {
        this.mCheckBoxView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            /* class dji.publics.widget.dialog.DJIListExtDialog.AnonymousClass2 */

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                listener.onCheckedChanged(buttonView, isChecked);
            }
        });
    }

    private void adjustCheckBoxDrawable() {
        Drawable drawable = this.mContext.getResources().getDrawable(R.drawable.dlg_multi_checkbox);
        drawable.setBounds(0, 2, Utils.getDimens(this.mContext, R.dimen.checkbox_width), Utils.getDimens(this.mContext, R.dimen.checkbox_width));
        this.mCheckBoxView.setCompoundDrawables(drawable, null, null, null);
    }

    public void dismiss() {
        super.dismiss();
        if (this.mCheckBoxView.getVisibility() == 0 && isChecked()) {
            this.mCheckBoxView.setChecked(false);
        }
    }
}
