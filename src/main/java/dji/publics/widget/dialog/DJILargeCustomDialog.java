package dji.publics.widget.dialog;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import dji.publics.DJIUI.DJIImageView;
import dji.publics.DJIUI.DJITextView;
import dji.publics.widget.DJIScrollTextView;
import dji.publics.widget.dialog.DJIDialog;

public class DJILargeCustomDialog extends DJIDialog {
    private CheckBox mCheckBoxView;
    private DJIScrollTextView mDescTv;
    private DJITextView mHighlightTv;
    private DJIImageView mImageBottom;
    private DJIImageView mImageTop;

    public DJILargeCustomDialog(Context context) {
        super(context);
        initCustomView();
    }

    public DJILargeCustomDialog(Context context, DJIDialogType dialogType, DJIDialog.DJIDialogTheme theme) {
        super(context, dialogType, theme);
        initCustomView();
    }

    private void initCustomView() {
        View view = LayoutInflater.from(this.mContext).inflate(R.layout.dlg_large_custom_view, (ViewGroup) null);
        this.mImageTop = (DJIImageView) view.findViewById(R.id.dlg_imageview_top);
        this.mImageBottom = (DJIImageView) view.findViewById(R.id.dlg_imageview_bottom);
        this.mDescTv = (DJIScrollTextView) view.findViewById(R.id.dlg_desc_tv);
        this.mHighlightTv = (DJITextView) view.findViewById(R.id.dlg_highlight_tv);
        this.mCheckBoxView = (CheckBox) view.findViewById(R.id.dlg_checkbox_text);
        setCustomView(view);
    }

    public void setImageTop(int resId) {
        this.mImageTop.setImageResource(resId);
        this.mImageTop.setVisibility(0);
    }

    public void setImageTop(Drawable drawable) {
        this.mImageTop.setImageDrawable(drawable);
        this.mImageTop.setVisibility(0);
    }

    public void hideTopImage() {
        if (this.mImageTop.getVisibility() == 0) {
            this.mImageTop.setVisibility(8);
        }
    }

    public void setImageBottom(int resId) {
        this.mImageBottom.setImageResource(resId);
        this.mImageBottom.setVisibility(0);
    }

    public void setImageBottom(Drawable drawable) {
        this.mImageBottom.setImageDrawable(drawable);
        this.mImageBottom.setVisibility(0);
    }

    public void hideBottomImage() {
        if (this.mImageBottom.getVisibility() == 0) {
            this.mImageBottom.setVisibility(8);
        }
    }

    public void setDescText(int textId) {
        initContentColor(this.mDescTv);
        this.mDescTv.setText(textId);
        this.mDescTv.setVisibility(0);
    }

    public void setDescText(CharSequence msg) {
        initContentColor(this.mDescTv);
        this.mDescTv.setText(msg);
        this.mDescTv.setVisibility(0);
    }

    public void hideDescText() {
        if (this.mDescTv.getVisibility() == 0) {
            this.mDescTv.setVisibility(8);
        }
    }

    public void setDescTextGravity(int gravity) {
        this.mDescTv.setGravity(gravity);
    }

    public void setHighlightText(CharSequence msg) {
        this.mHighlightTv.setText(msg);
        this.mHighlightTv.setVisibility(0);
    }

    public void hideHighlightText() {
        if (this.mHighlightTv.getVisibility() == 0) {
            this.mHighlightTv.setVisibility(8);
        }
    }

    public boolean isChecked() {
        return this.mCheckBoxView.isChecked();
    }

    public void setCheckText(CharSequence msg) {
        initContentColor(this.mCheckBoxView);
        this.mCheckBoxView.setText(msg);
        this.mCheckBoxView.setVisibility(0);
        if (this.mViewContainer.getVisibility() != 0) {
            this.mViewContainer.setVisibility(0);
        }
        if (this.mContentCustomMargin.getVisibility() == 8) {
            this.mContentCustomMargin.setVisibility(0);
        }
    }

    public void setCheckText(int textId) {
        setCheckText(getContext().getString(textId));
    }

    public void setOnCheckedChangeListener(final CompoundButton.OnCheckedChangeListener listener) {
        this.mCheckBoxView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            /* class dji.publics.widget.dialog.DJILargeCustomDialog.AnonymousClass1 */

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                listener.onCheckedChanged(buttonView, isChecked);
            }
        });
    }

    public void hideCheckText() {
        if (this.mCheckBoxView.getVisibility() == 0) {
            this.mCheckBoxView.setVisibility(8);
            this.mCheckBoxView.setChecked(false);
        }
    }

    public void dismiss() {
        super.dismiss();
        if (this.mCheckBoxView.getVisibility() == 0 && isChecked()) {
            this.mCheckBoxView.setChecked(false);
        }
    }
}
