package dji.publics.widget.dialog;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import dji.publics.DJIUI.DJILinearLayout;
import dji.publics.DJIUI.DJITextView;
import dji.publics.widget.dialog.DJIDialog;
import dji.publics.widget.util.ThreadLocalFormatUtil;
import dji.publics.widget.util.Utils;
import java.lang.ref.WeakReference;
import java.util.Locale;

public class DJIProgressDialog extends DJIDialog {
    private boolean mHasStarted;
    private int mIncrementBy;
    private int mIncrementSecondaryBy;
    private boolean mIndeterminate;
    private Drawable mIndeterminateDrawable;
    private int mMax;
    private ProgressBar mProgress;
    private DJILinearLayout mProgressBottomLy;
    private Drawable mProgressDrawable;
    private String mProgressNumberFormat;
    private DJITextView mProgressPercent;
    private DJITextView mProgressTextLeft;
    private DJITextView mProgressTextRight;
    private int mProgressVal;
    private int mSecondaryProgressVal;
    private ViewUpdateHandler mViewUpdateHandler;

    public DJIProgressDialog(Context context) {
        super(context);
        initDivider();
        initView();
    }

    public DJIProgressDialog(Context context, DJIDialogType dialogType, DJIDialog.DJIDialogTheme theme) {
        super(context, dialogType, theme);
        initDivider();
        initView();
    }

    private void initDivider() {
        updateDividerHeight(this.mTitleContentMargin, Utils.getDimens(this.mContext, R.dimen.new_dialog_margin_left));
    }

    private void initFormats() {
        this.mProgressNumberFormat = "%1d/%2d";
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        this.mViewUpdateHandler = new ViewUpdateHandler(this);
        if (this.mMax > 0) {
            setMax(this.mMax);
        }
        if (this.mProgressVal > 0) {
            setProgress(this.mProgressVal);
        }
        if (this.mSecondaryProgressVal > 0) {
            setSecondaryProgress(this.mSecondaryProgressVal);
        }
        if (this.mIncrementBy > 0) {
            incrementProgressBy(this.mIncrementBy);
        }
        if (this.mIncrementSecondaryBy > 0) {
            incrementSecondaryProgressBy(this.mIncrementSecondaryBy);
        }
        if (this.mProgressDrawable != null) {
            setProgressDrawable(this.mProgressDrawable);
        }
        if (this.mIndeterminateDrawable != null) {
            setIndeterminateDrawable(this.mIndeterminateDrawable);
        }
        setIndeterminate(this.mIndeterminate);
        onProgressChanged();
        super.onCreate(savedInstanceState);
    }

    private void initView() {
        View view = LayoutInflater.from(this.mContext).inflate(R.layout.dlg_progress_view, (ViewGroup) null);
        this.mProgress = (ProgressBar) view.findViewById(R.id.progress_view);
        this.mProgressPercent = (DJITextView) view.findViewById(R.id.progress_percent);
        this.mProgressTextLeft = (DJITextView) view.findViewById(R.id.progress_textleft);
        this.mProgressTextRight = (DJITextView) view.findViewById(R.id.progress_textright);
        this.mProgressBottomLy = (DJILinearLayout) view.findViewById(R.id.progress_bottom_ly);
        setCustomView(view);
    }

    public void onStart() {
        super.onStart();
        this.mHasStarted = true;
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        this.mHasStarted = false;
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mViewUpdateHandler != null) {
            this.mViewUpdateHandler.removeCallbacksAndMessages(null);
        }
    }

    public void setProgressPercent(CharSequence msg) {
        this.mProgressPercent.setText(msg);
        if (this.mProgressPercent.getVisibility() != 0) {
            this.mProgressPercent.setVisibility(0);
        }
    }

    public void setDefaultFormat() {
        initFormats();
    }

    public void setProgressLeftText(CharSequence msg) {
        initContentColor(this.mProgressTextLeft);
        this.mProgressTextLeft.setText(msg);
        if (this.mProgressTextLeft.getVisibility() != 0) {
            this.mProgressTextLeft.setVisibility(0);
            this.mProgressBottomLy.setVisibility(0);
        }
    }

    public void setProgressRightText(CharSequence msg) {
        initContentColor(this.mProgressTextRight);
        this.mProgressTextRight.setText(msg);
        if (this.mProgressTextRight.getVisibility() != 0) {
            this.mProgressTextRight.setVisibility(0);
            this.mProgressBottomLy.setVisibility(0);
        }
    }

    public void setMax(int max) {
        if (this.mProgress != null) {
            this.mProgress.setMax(max);
            onProgressChanged();
            return;
        }
        this.mMax = max;
    }

    public int getMax() {
        if (this.mProgress != null) {
            return this.mProgress.getMax();
        }
        return this.mMax;
    }

    public void setProgress(int value) {
        if (this.mHasStarted) {
            this.mProgress.setProgress(value);
            onProgressChanged();
            return;
        }
        this.mProgressVal = value;
    }

    public void setSecondaryProgress(int secondaryProgress) {
        if (this.mProgress != null) {
            this.mProgress.setSecondaryProgress(secondaryProgress);
            onProgressChanged();
            return;
        }
        this.mSecondaryProgressVal = secondaryProgress;
    }

    public int getProgress() {
        if (this.mProgress != null) {
            return this.mProgress.getProgress();
        }
        return this.mProgressVal;
    }

    public int getSecondaryProgress() {
        if (this.mProgress != null) {
            return this.mProgress.getSecondaryProgress();
        }
        return this.mSecondaryProgressVal;
    }

    public void incrementProgressBy(int diff) {
        if (this.mProgress != null) {
            this.mProgress.incrementProgressBy(diff);
            onProgressChanged();
            return;
        }
        this.mIncrementBy += diff;
    }

    public void incrementSecondaryProgressBy(int diff) {
        if (this.mProgress != null) {
            this.mProgress.incrementSecondaryProgressBy(diff);
            onProgressChanged();
            return;
        }
        this.mIncrementSecondaryBy += diff;
    }

    public void setProgressRed() {
        setProgressDrawable(this.mContext.getResources().getDrawable(R.drawable.dlg_progress_red));
    }

    public void setProgressNormal() {
        setProgressDrawable(this.mContext.getResources().getDrawable(R.drawable.dlg_progress_background));
    }

    public void setProgressDrawable(Drawable d) {
        if (this.mProgress != null) {
            this.mProgress.setProgressDrawable(d);
        } else {
            this.mProgressDrawable = d;
        }
    }

    public void setIndeterminateDrawable(Drawable d) {
        if (this.mProgress != null) {
            this.mProgress.setIndeterminateDrawable(d);
        } else {
            this.mIndeterminateDrawable = d;
        }
    }

    public void setIndeterminate(boolean indeterminate) {
        if (this.mProgress != null) {
            this.mProgress.setIndeterminate(indeterminate);
        } else {
            this.mIndeterminate = indeterminate;
        }
    }

    public boolean isIndeterminate() {
        if (this.mProgress != null) {
            return this.mProgress.isIndeterminate();
        }
        return this.mIndeterminate;
    }

    public void setProgressNumberFormat(String format) {
        this.mProgressNumberFormat = format;
        onProgressChanged();
    }

    private void onProgressChanged() {
        if (this.mViewUpdateHandler != null && !this.mViewUpdateHandler.hasMessages(0)) {
            this.mViewUpdateHandler.sendEmptyMessage(0);
        }
    }

    /* access modifiers changed from: private */
    public void updateProgressView() {
        int progress = this.mProgress.getProgress();
        int max = this.mProgress.getMax();
        if (this.mProgressNumberFormat != null && this.mProgressTextRight.getVisibility() == 0) {
            String format = this.mProgressNumberFormat;
            this.mProgressTextRight.setText(String.format(Locale.US, format, Integer.valueOf(progress), Integer.valueOf(max)));
        }
        if (this.mProgressPercent.getVisibility() == 0) {
            this.mProgressPercent.setText(ThreadLocalFormatUtil.format(((double) progress) / ((double) max)));
        }
    }

    private static class ViewUpdateHandler extends Handler {
        private WeakReference<DJIProgressDialog> mViewReference;

        ViewUpdateHandler(DJIProgressDialog dialog) {
            this.mViewReference = new WeakReference<>(dialog);
        }

        public void handleMessage(Message msg) {
            this.mViewReference.get().updateProgressView();
        }
    }
}
