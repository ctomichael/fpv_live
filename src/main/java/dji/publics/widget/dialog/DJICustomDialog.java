package dji.publics.widget.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import dji.publics.DJIUI.DJILinearLayout;
import dji.publics.DJIUI.DJIRelativeLayout;
import dji.publics.DJIUI.DJITextView;
import dji.publics.widget.DJIScrollTextView;
import dji.publics.widget.DJIThumbSeekBar;
import dji.publics.widget.dialog.DJIDialog;
import dji.publics.widget.util.Utils;

public class DJICustomDialog extends DJIDialog {
    private CheckBox mCheckBoxView;
    private DJIScrollTextView mHelpTextView;
    private OnSeekDoneListener mListener;
    private ProgressBar mProgressBar;
    private DJILinearLayout mProgressRoundLy;
    private DJITextView mProgressRoundTv;
    private DJIThumbSeekBar mSeekBar;
    private DJIRelativeLayout mSeekBarLy;
    private DJITextView mSeekBarTextView;

    public interface OnSeekDoneListener {
        void onSeekBarChecked(DialogInterface dialogInterface, boolean z);
    }

    public DJICustomDialog(Context context) {
        super(context);
        initCustomView();
    }

    public DJICustomDialog(Context context, DJIDialogType dialogType, DJIDialog.DJIDialogTheme theme) {
        super(context, dialogType, theme);
        initCustomView();
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initCustomView() {
        View view = LayoutInflater.from(this.mContext).inflate(R.layout.dlg_custom_view, (ViewGroup) null);
        this.mHelpTextView = (DJIScrollTextView) view.findViewById(R.id.dlg_text_help);
        this.mSeekBar = (DJIThumbSeekBar) view.findViewById(R.id.dlg_seekbar);
        this.mSeekBarTextView = (DJITextView) view.findViewById(R.id.dlg_leftmenu_tv);
        this.mSeekBarLy = (DJIRelativeLayout) view.findViewById(R.id.dlg_leftmenu_cb_ly);
        this.mProgressRoundLy = (DJILinearLayout) view.findViewById(R.id.dlg_progress_round);
        this.mProgressRoundTv = (DJITextView) view.findViewById(R.id.dlg_progress_round_tv);
        this.mCheckBoxView = (CheckBox) view.findViewById(R.id.dlg_checkbox_text);
        this.mProgressBar = (ProgressBar) view.findViewById(R.id.dlg_progress_round_pb_circle);
        if (this.mTheme == DJIDialog.DJIDialogTheme.WHITE) {
            this.mProgressBar.setIndeterminateDrawable(this.mContext.getResources().getDrawable(R.drawable.dlg_progress_round_bg_in_white));
            this.mProgressRoundTv.setTextColor(this.mContext.getResources().getColor(R.color.black));
        }
        setCustomView(view);
    }

    public void setHelpText(CharSequence msg) {
        this.mHelpTextView.setText(msg);
        this.mHelpTextView.setVisibility(0);
        this.mContentCustomMargin.setVisibility(8);
        if (this.mViewContainer.getVisibility() != 0) {
            this.mViewContainer.setVisibility(0);
        }
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
            /* class dji.publics.widget.dialog.DJICustomDialog.AnonymousClass1 */

            public void onClick(View v) {
                listener.onClick(DJICustomDialog.this, v.getId());
            }
        });
    }

    public void hideHelpText() {
        if (this.mHelpTextView.getVisibility() == 0) {
            this.mHelpTextView.setVisibility(8);
        }
        hideCustomDivider();
    }

    public void setProgressText(CharSequence msg) {
        this.mProgressRoundTv.setText(msg);
        this.mProgressRoundLy.setVisibility(0);
        if (this.mViewContainer.getVisibility() != 0) {
            this.mViewContainer.setVisibility(0);
        }
    }

    public void hideProgressText() {
        if (this.mProgressRoundLy.getVisibility() == 0) {
            this.mProgressRoundLy.setVisibility(8);
        }
        hideCustomDivider();
    }

    public boolean isChecked() {
        return this.mCheckBoxView.isChecked();
    }

    public void setCheckText(CharSequence msg) {
        adjustCheckBoxDrawable();
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
            /* class dji.publics.widget.dialog.DJICustomDialog.AnonymousClass2 */

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
        hideCustomDivider();
    }

    private void adjustCheckBoxDrawable() {
        Drawable drawable = this.mContext.getResources().getDrawable(R.drawable.dlg_multi_checkbox);
        drawable.setBounds(0, 2, Utils.getDimens(this.mContext, R.dimen.checkbox_width), Utils.getDimens(this.mContext, R.dimen.checkbox_width));
        this.mCheckBoxView.setCompoundDrawables(drawable, null, null, null);
    }

    public void setSeekBarText(CharSequence msg) {
        this.mSeekBarTextView.setText(msg);
        this.mSeekBar.setProgress(0);
        this.mSeekBar.setPadding(0, 0, 0, 0);
        this.mSeekBarLy.setVisibility(0);
        if (this.mViewContainer.getVisibility() != 0) {
            this.mViewContainer.setVisibility(0);
        }
        if (this.mContentCustomMargin.getVisibility() == 8) {
            this.mContentCustomMargin.setVisibility(0);
        }
    }

    public void hideSeekBar() {
        if (this.mSeekBarLy.getVisibility() == 0) {
            this.mSeekBarLy.setVisibility(8);
        }
        hideCustomDivider();
    }

    /* access modifiers changed from: private */
    public void handleSbStopTrack() {
        if (this.mSeekBar.getProgress() >= 95) {
            this.mSeekBar.setProgress(100);
            if (this.mListener != null) {
                this.mListener.onSeekBarChecked(this, true);
                return;
            }
            return;
        }
        this.mSeekBar.setProgress(0);
        if (this.mListener != null) {
            this.mListener.onSeekBarChecked(this, false);
        }
    }

    public void setOnSeekDoneListener(OnSeekDoneListener listener) {
        this.mListener = listener;
        this.mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /* class dji.publics.widget.dialog.DJICustomDialog.AnonymousClass3 */

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                DJICustomDialog.this.handleSbStopTrack();
            }
        });
    }

    public void dismiss() {
        super.dismiss();
        if (this.mSeekBar.getVisibility() == 0) {
            this.mSeekBar.setProgress(0);
        }
        if (this.mCheckBoxView.getVisibility() == 0 && isChecked()) {
            this.mCheckBoxView.setChecked(false);
        }
    }

    private void hideCustomDivider() {
        if (this.mSeekBarLy.getVisibility() != 0 && this.mCheckBoxView.getVisibility() != 0 && this.mHelpTextView.getVisibility() != 0 && this.mProgressRoundLy.getVisibility() != 0) {
            this.mViewContainer.setVisibility(8);
            this.mContentCustomMargin.setVisibility(8);
        }
    }
}
