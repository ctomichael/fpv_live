package dji.publics.widget.dialog;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import dji.publics.DJIUI.DJIImageView;
import dji.publics.widget.dialog.DJIDialog;
import dji.publics.widget.util.Utils;

public class DJITopImageDialog extends DJIDialog {
    private boolean mIsTopIcon = false;
    private DJIImageView mTopImageView;

    public DJITopImageDialog(Context context) {
        super(context);
        initView();
    }

    public DJITopImageDialog(Context context, DJIDialogType dialogType, DJIDialog.DJIDialogTheme theme) {
        super(context, dialogType, theme);
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(this.mContext).inflate(R.layout.dlg_top_image_view, (ViewGroup) null);
        this.mTopImageView = (DJIImageView) view.findViewById(R.id.dlg_top_image);
        addTopView(view);
    }

    public void setTopImage(int resId) {
        if (this.mIsTopIcon) {
            this.mIsTopIcon = false;
            adjustViewMargin();
        }
        this.mTopImageView.setImageResource(resId);
    }

    public void setTopImage(Drawable drawable) {
        if (this.mIsTopIcon) {
            this.mIsTopIcon = false;
            adjustViewMargin();
        }
        this.mTopImageView.setImageDrawable(drawable);
    }

    public void setBigIcon(int resId) {
        this.mIsTopIcon = true;
        adjustViewMargin();
        this.mTopImageView.setImageResource(resId);
    }

    public void setBigIcon(Drawable drawable) {
        this.mIsTopIcon = true;
        adjustViewMargin();
        this.mTopImageView.setImageDrawable(drawable);
    }

    private void adjustViewMargin() {
        int i;
        int i2 = 0;
        if (this.mIsTopIcon) {
            this.mTopImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        } else {
            this.mTopImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }
        LinearLayout.LayoutParams topImageViewLP = (LinearLayout.LayoutParams) this.mTopImageView.getLayoutParams();
        if (this.mIsTopIcon) {
            i = Utils.getDimens(this.mContext, R.dimen.new_dialog_margin_vertical);
        } else {
            i = 0;
        }
        topImageViewLP.topMargin = i;
        this.mTopImageView.setLayoutParams(topImageViewLP);
        RelativeLayout.LayoutParams middleLP = (RelativeLayout.LayoutParams) this.mMiddleContentLy.getLayoutParams();
        if (!this.mIsTopIcon) {
            i2 = Utils.getDimens(this.mContext, R.dimen.new_dialog_margin_vertical);
        }
        middleLP.topMargin = i2;
        this.mMiddleContentLy.setLayoutParams(middleLP);
    }
}
