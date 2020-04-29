package dji.pilot.publics.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import dji.fieldAnnotation.EXClassNullAway;
import dji.pilot.publics.R;
import org.greenrobot.eventbus.EventBus;

@EXClassNullAway
public class DJIInstructionHelper {
    /* access modifiers changed from: private */
    public boolean isNewBeeInstruction = true;
    private boolean isShowBlackCover = false;
    /* access modifiers changed from: private */
    public RelativeLayout mBlackCoverLayer;
    private final Context mContext;
    private int mGravity = 5;
    private View mInstructionView = null;
    /* access modifiers changed from: private */
    public View.OnClickListener mListener;
    private int mMaxWidth = 0;
    private int mOffset = 0;
    private final ViewGroup mRootView;
    private int mScreenH = Integer.MAX_VALUE;
    private int mScreenW = Integer.MAX_VALUE;
    private String mTipStr = "";
    private View mTipView = null;
    private ImageView mTipViewImg = null;
    private TextView mTipViewTv = null;
    private final Rect mViewRect = new Rect();

    public enum EventOfIntroduction {
        NORMAL_NEXT_STEP,
        AUTO_TAKE_OFF_SUCCESS,
        JOYSTICK_COMPLETE,
        RETURN_HOME_SUCCESS
    }

    public DJIInstructionHelper(Activity context) {
        this.mContext = context;
        this.mRootView = (ViewGroup) context.getWindow().getDecorView();
        initDefaults(context);
    }

    public void hideBlackCover() {
        if (this.mBlackCoverLayer != null) {
            this.mRootView.removeView(this.mBlackCoverLayer);
            this.mBlackCoverLayer = null;
        }
    }

    public DJIInstructionHelper setShowBlackCover(boolean showBlackCover) {
        this.isShowBlackCover = showBlackCover;
        return this;
    }

    public void setNewBeeInstruction(boolean isNewBeeInstruction2) {
        this.isNewBeeInstruction = isNewBeeInstruction2;
    }

    private void initBlackCoverAdded() {
        if (this.mBlackCoverLayer == null) {
            this.mBlackCoverLayer = new RelativeLayout(this.mContext);
            this.mBlackCoverLayer.setBackgroundColor(this.mContext.getResources().getColor(R.color.black_40P));
            this.mBlackCoverLayer.setOnClickListener(new View.OnClickListener() {
                /* class dji.pilot.publics.util.DJIInstructionHelper.AnonymousClass1 */

                public void onClick(View v) {
                    if (DJIInstructionHelper.this.mListener != null) {
                        DJIInstructionHelper.this.mListener.onClick(DJIInstructionHelper.this.mBlackCoverLayer);
                    }
                    if (DJIInstructionHelper.this.isNewBeeInstruction) {
                        EventBus.getDefault().post(EventOfIntroduction.NORMAL_NEXT_STEP);
                    }
                }
            });
            this.mBlackCoverLayer.setContentDescription("this is an instruction cover");
            this.mRootView.addView(this.mBlackCoverLayer);
        }
    }

    public DJIInstructionHelper setScreenParam(int screenWidth, int screenHeight) {
        this.mScreenW = screenWidth;
        this.mScreenH = screenHeight;
        return this;
    }

    public DJIInstructionHelper setTipStr(String tip) {
        this.mTipStr = tip;
        return this;
    }

    public DJIInstructionHelper setTipParam(View instructionView, int gravity) {
        this.mInstructionView = instructionView;
        this.mGravity = gravity;
        return this;
    }

    public DJIInstructionHelper setOtherParams(int maxWidth, int offset) {
        this.mMaxWidth = maxWidth;
        this.mOffset = offset;
        return this;
    }

    public DJIInstructionHelper setOffset(int offset) {
        this.mOffset = offset;
        return this;
    }

    public DJIInstructionHelper setMaxWidth(int maxWidth) {
        this.mMaxWidth = maxWidth;
        return this;
    }

    public DJIInstructionHelper setClickListener(View.OnClickListener listener) {
        this.mListener = listener;
        return this;
    }

    public void removeTip() {
        if (this.mTipView != null) {
            this.mRootView.removeView(this.mTipView);
            this.mTipView = null;
        }
    }

    public void showTip() {
        int layoutResId;
        if (this.mTipView == null && this.mInstructionView != null && this.mInstructionView.getGlobalVisibleRect(this.mViewRect) && (layoutResId = getLayout(this.mGravity)) != 0) {
            if (this.isShowBlackCover) {
                initBlackCoverAdded();
            }
            View view = LayoutInflater.from(this.mContext).inflate(layoutResId, (ViewGroup) null);
            TextView txtView = (TextView) view.findViewById(R.id.instruction_view_tv);
            ImageView imgView = (ImageView) view.findViewById(R.id.instruction_view_img);
            txtView.setMaxWidth(this.mMaxWidth);
            txtView.setText(this.mTipStr);
            this.mRootView.addView(view);
            this.mTipViewTv = txtView;
            this.mTipViewImg = imgView;
            this.mTipView = view;
            this.mTipViewTv.measure(View.MeasureSpec.makeMeasureSpec(this.mMaxWidth, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(0, 0));
            locationTipView(view, txtView, imgView, this.mViewRect);
        }
    }

    public void showTip(float x, float y) {
        if (this.mTipView == null) {
            if (this.isShowBlackCover) {
                initBlackCoverAdded();
            }
            View view = LayoutInflater.from(this.mContext).inflate(R.layout.tutorial_bottom_view, (ViewGroup) null);
            TextView txtView = (TextView) view.findViewById(R.id.instruction_view_tv);
            view.findViewById(R.id.instruction_view_img).setVisibility(8);
            txtView.setMaxWidth(this.mMaxWidth);
            txtView.setText(this.mTipStr);
            this.mRootView.addView(view);
            this.mTipViewTv = txtView;
            this.mTipView = view;
            txtView.measure(View.MeasureSpec.makeMeasureSpec(this.mMaxWidth, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(0, 0));
            int width = txtView.getMeasuredWidth();
            int height = txtView.getMeasuredHeight();
            view.setX(adaptAxis(x - ((float) (width / 2)), 0.0f, (float) (this.mScreenW - width)));
            view.setY(adaptAxis(y, 0.0f, (float) (this.mScreenH - height)));
        }
    }

    private float adaptAxis(float value, float min, float max) {
        if (value < min) {
            return min;
        }
        if (value > max) {
            return max;
        }
        return value;
    }

    private void locationTipView(View view, View txtView, View imgView, Rect rect) {
        switch (this.mGravity) {
            case 3:
                locationToLeft(view, txtView, imgView, rect);
                return;
            case 5:
                locationToRight(view, txtView, imgView, rect);
                return;
            case 48:
                locationToTop(view, txtView, imgView, rect);
                return;
            case 80:
                locationToBottom(view, txtView, imgView, rect);
                return;
            default:
                locationToRight(view, txtView, imgView, rect);
                return;
        }
    }

    private void locationToLeft(View view, View txtView, View imgView, Rect rect) {
        int txtWidth = txtView.getMeasuredWidth();
        int txtHeight = txtView.getMeasuredHeight();
        Drawable imgBg = imgView.getBackground();
        int imgW = imgBg.getIntrinsicWidth();
        int imgH = imgBg.getIntrinsicHeight();
        float totalW = (float) (txtWidth + imgW);
        float totalH = (float) Math.max(txtHeight, imgH);
        float x = adaptAxis((((float) rect.left) - totalW) - ((float) this.mOffset), 0.0f, (((float) this.mScreenW) - totalW) - ((float) this.mOffset));
        float y = adaptAxis(((float) rect.centerY()) - (totalH / 2.0f), 0.0f, ((float) this.mScreenH) - totalH);
        view.setX(x);
        view.setY(y);
        ViewGroup.MarginLayoutParams param = (ViewGroup.MarginLayoutParams) imgView.getLayoutParams();
        param.topMargin = (int) ((((float) rect.centerY()) - y) - ((float) (imgH / 2)));
        imgView.setLayoutParams(param);
    }

    private void locationToRight(View view, View txtView, View imgView, Rect rect) {
        int txtWidth = txtView.getMeasuredWidth();
        int txtHeight = txtView.getMeasuredHeight();
        Drawable imgBg = imgView.getBackground();
        int imgW = imgBg.getIntrinsicWidth();
        int imgH = imgBg.getIntrinsicHeight();
        float totalH = (float) Math.max(txtHeight, imgH);
        float x = adaptAxis((float) (rect.right + this.mOffset), 0.0f, ((float) this.mScreenW) - ((float) (txtWidth + imgW)));
        float y = adaptAxis(((float) rect.centerY()) - (totalH / 2.0f), 0.0f, ((float) this.mScreenH) - totalH);
        view.setX(x);
        view.setY(y);
        ViewGroup.MarginLayoutParams param = (ViewGroup.MarginLayoutParams) imgView.getLayoutParams();
        param.topMargin = (int) ((((float) rect.centerY()) - y) - ((float) (imgH / 2)));
        imgView.setLayoutParams(param);
    }

    private void locationToTop(View view, View txtView, View imgView, Rect rect) {
        int txtWidth = txtView.getMeasuredWidth();
        int txtHeight = txtView.getMeasuredHeight();
        Drawable imgBg = imgView.getBackground();
        int imgW = imgBg.getIntrinsicWidth();
        float totalW = (float) txtWidth;
        float totalH = (float) (txtHeight + imgBg.getIntrinsicHeight());
        float y = adaptAxis((((float) rect.top) - totalH) - ((float) this.mOffset), 0.0f, ((float) this.mScreenH) - totalH);
        float x = adaptAxis(((float) rect.centerX()) - (totalW / 2.0f), 0.0f, ((float) this.mScreenW) - totalW);
        view.setX(x);
        view.setY(y);
        ViewGroup.MarginLayoutParams param = (ViewGroup.MarginLayoutParams) imgView.getLayoutParams();
        param.leftMargin = (int) ((((float) rect.centerX()) - x) - ((float) (imgW / 2)));
        imgView.setLayoutParams(param);
    }

    private void locationToBottom(View view, View txtView, View imgView, Rect rect) {
        int txtWidth = txtView.getMeasuredWidth();
        int txtHeight = txtView.getMeasuredHeight();
        Drawable imgBg = imgView.getBackground();
        int imgW = imgBg.getIntrinsicWidth();
        float totalW = (float) txtWidth;
        float y = adaptAxis((float) (rect.bottom + this.mOffset), 0.0f, ((float) this.mScreenH) - ((float) (txtHeight + imgBg.getIntrinsicHeight())));
        float x = adaptAxis(((float) rect.centerX()) - (totalW / 2.0f), 0.0f, ((float) this.mScreenW) - totalW);
        view.setX(x);
        view.setY(y);
        ViewGroup.MarginLayoutParams param = (ViewGroup.MarginLayoutParams) imgView.getLayoutParams();
        param.leftMargin = (int) ((((float) rect.centerX()) - x) - ((float) (imgW / 2)));
        imgView.setLayoutParams(param);
    }

    private void initDefaults(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        this.mMaxWidth = (int) TypedValue.applyDimension(1, 200.0f, metrics);
        this.mOffset = (int) TypedValue.applyDimension(1, 4.0f, metrics);
    }

    private int getLayout(int gravity) {
        switch (gravity) {
            case 3:
                return R.layout.tutorial_left_view;
            case 5:
                return R.layout.tutorial_right_view;
            case 48:
                return R.layout.tutorial_top_view;
            case 80:
                return R.layout.tutorial_bottom_view;
            default:
                return 0;
        }
    }

    public static class Builder {
        private Activity activity;
        private int gravity;
        private int height;
        private String str;
        private View view;
        private int width;

        public Builder activity(Activity activity2) {
            this.activity = activity2;
            return this;
        }

        public Builder width(int width2) {
            this.width = width2;
            return this;
        }

        public Builder height(int height2) {
            this.height = height2;
            return this;
        }

        public Builder str(String str2) {
            this.str = str2;
            return this;
        }

        public Builder view(View view2) {
            this.view = view2;
            return this;
        }

        public Builder gravity(int gravity2) {
            this.gravity = gravity2;
            return this;
        }

        public DJIInstructionHelper build() {
            DJIInstructionHelper helper = new DJIInstructionHelper(this.activity);
            helper.setScreenParam(this.width, this.height);
            helper.setTipStr(this.str);
            helper.setTipParam(this.view, this.gravity);
            return helper;
        }
    }
}
