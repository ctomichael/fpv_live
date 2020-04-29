package dji.publics.widget.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.support.annotation.DimenRes;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import dji.publics.DJIUI.DJIImageView;
import dji.publics.DJIUI.DJILinearLayout;
import dji.publics.DJIUI.DJIRelativeLayout;
import dji.publics.DJIUI.DJITextView;
import dji.publics.DJIUI.DJIView;
import dji.publics.widget.DJIScrollTextView;
import dji.publics.widget.util.CommonEvent;
import dji.publics.widget.util.Utils;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class DJIDialog extends DJINewBaseDialog {
    public static final int TYPE_BACKHOME = 5;
    public static final int TYPE_ICON_MAX = 10;
    public static final int TYPE_NOFLYZONE = 4;
    public static final int TYPE_NOTIFY = 1;
    public static final int TYPE_OKTIPS = 2;
    public static final int TYPE_WARNING = 3;
    private static boolean sForbidShowDialog = false;
    protected DJILinearLayout mBottomBtnLy;
    protected DJIView mBtnDivider;
    private DJIImageView mCloseView;
    protected DJIScrollTextView mContent;
    protected DJIView mContentCustomMargin;
    private int mContentCustomMarginHeight;
    protected DJIView mContentDivider;
    /* access modifiers changed from: private */
    public DJIView mContentSpaceBottom;
    /* access modifiers changed from: private */
    public DJIView mContentSpaceTop;
    private boolean mIsAuto = true;
    private boolean mIsShow = false;
    protected DJITextView mLeftBtn;
    protected DialogInterface.OnClickListener mLeftListener;
    /* access modifiers changed from: private */
    public int mMaxMiddleContentHeight;
    private int mMaxTopCustomHeight;
    protected DJILinearLayout mMiddleContentLy;
    protected DJITextView mRightBtn;
    protected DialogInterface.OnClickListener mRightListener;
    private DJITextView mTitle;
    protected DJIView mTitleContentMargin;
    private int mTitleContentMarginHeight;
    private DJIImageView mTitleIcon;
    private DJILinearLayout mTitleLayout;
    private DJILinearLayout mTopCustomContainer;
    protected DJILinearLayout mViewContainer;

    public enum DJIDialogTheme {
        BLACK,
        WHITE
    }

    public DJIDialog(Context context) {
        super(context, R.style.LogDialog);
        init();
    }

    public DJIDialog(Context context, DJIDialogType dialogType, DJIDialogTheme theme) {
        super(context, dialogType, theme);
        init();
    }

    private void init() {
        setContentView(R.layout.dlg_common_view);
        this.mCloseView = (DJIImageView) findViewById(R.id.dlg_close_btn);
        this.mTitleLayout = (DJILinearLayout) findViewById(R.id.dlg_title_ly);
        this.mTitleIcon = (DJIImageView) findViewById(R.id.dlg_title_icon);
        this.mTitle = (DJITextView) findViewById(R.id.dlg_title_tv);
        this.mContent = (DJIScrollTextView) findViewById(R.id.dlg_content_tv);
        this.mContentDivider = (DJIView) findViewById(R.id.dlg_content_divider_line);
        this.mLeftBtn = (DJITextView) findViewById(R.id.dlg_left_btn);
        this.mRightBtn = (DJITextView) findViewById(R.id.dlg_right_btn);
        this.mBtnDivider = (DJIView) findViewById(R.id.dlg_btn_divider_line);
        this.mBottomBtnLy = (DJILinearLayout) findViewById(R.id.dlg_bottom_btn_ly);
        this.mViewContainer = (DJILinearLayout) findViewById(R.id.dlg_view_container);
        this.mMiddleContentLy = (DJILinearLayout) findViewById(R.id.dlg_top_content_ly);
        this.mTitleContentMargin = (DJIView) findViewById(R.id.dlg_content_title_margin);
        this.mContentCustomMargin = (DJIView) findViewById(R.id.dlg_content_custom_margin);
        this.mTopCustomContainer = (DJILinearLayout) findViewById(R.id.dlg_top_custom_container);
        this.mContentSpaceTop = (DJIView) findViewById(R.id.dlg_content_space_top);
        this.mContentSpaceBottom = (DJIView) findViewById(R.id.dlg_content_space_bottom);
        initThemeBackGround();
        adjustViewType();
    }

    private void initThemeBackGround() {
        DJIRelativeLayout allDlgLy = (DJIRelativeLayout) findViewById(R.id.dlg_all_ly);
        if (this.mTheme == DJIDialogTheme.BLACK) {
            allDlgLy.setBackgroundResource(R.drawable.dlg_stroke_dark_bg);
            this.mContentDivider.setBackgroundResource(R.color.dlg_black_divide_color);
            this.mBtnDivider.setBackgroundResource(R.color.dlg_black_divide_color);
            this.mCloseView.setImageResource(R.drawable.ic_popup_cancle);
            return;
        }
        allDlgLy.setBackgroundResource(R.color.white);
        this.mContentDivider.setBackgroundResource(R.color.dlg_white_divide_color);
        this.mBtnDivider.setBackgroundResource(R.color.dlg_white_divide_color);
        this.mCloseView.setImageResource(R.drawable.dialog_close_black);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent3MainThread(CommonEvent event) {
        if (event == CommonEvent.RcC1Clicked) {
            if (this.mLeftListener != null) {
                this.mPostHandler.post(new Runnable() {
                    /* class dji.publics.widget.dialog.DJIDialog.AnonymousClass1 */

                    public void run() {
                        DJIDialog.this.onClickEvent(DJIDialog.this.mLeftListener);
                    }
                });
            }
        } else if (event == CommonEvent.RcC2Clicked && this.mRightListener != null) {
            this.mPostHandler.post(new Runnable() {
                /* class dji.publics.widget.dialog.DJIDialog.AnonymousClass2 */

                public void run() {
                    DJIDialog.this.onClickEvent(DJIDialog.this.mRightListener);
                }
            });
        }
    }

    private void setTheme(DJIDialogTheme theme) {
        switch (theme) {
            case BLACK:
                this.mContext.setTheme(R.style.DialogBlack);
                return;
            case WHITE:
                this.mContext.setTheme(R.style.DialogWhite);
                return;
            default:
                this.mContext.setTheme(R.style.DialogBlack);
                return;
        }
    }

    private void adjustViewType() {
        switch (this.mDialogType) {
            case SMALL:
                this.mMaxTopCustomHeight = Utils.getDimens(this.mContext, R.dimen.s_90_dp);
                this.mMaxMiddleContentHeight = Utils.getDimens(this.mContext, R.dimen.new_dialog_middle_height_small);
                this.mTitleContentMarginHeight = Utils.getDimens(this.mContext, R.dimen.new_dialog_margin_small);
                this.mContentCustomMarginHeight = this.mTitleContentMarginHeight;
                break;
            case MEDIUM:
                this.mMaxTopCustomHeight = Utils.getDimens(this.mContext, R.dimen.s_120_dp);
                this.mMaxMiddleContentHeight = Utils.getDimens(this.mContext, R.dimen.new_dialog_middle_height_medium);
                if (this.mTheme == DJIDialogTheme.WHITE) {
                    this.mTitleContentMarginHeight = Utils.getDimens(this.mContext, R.dimen.new_dialog_margin_small);
                } else {
                    this.mTitleContentMarginHeight = Utils.getDimens(this.mContext, R.dimen.new_dialog_margin_medium);
                }
                this.mContentCustomMarginHeight = Utils.getDimens(this.mContext, R.dimen.new_dialog_margin_medium);
                break;
            case LARGE:
                this.mMaxTopCustomHeight = Utils.getDimens(this.mContext, R.dimen.s_120_dp);
                this.mMaxMiddleContentHeight = Utils.getDimens(this.mContext, R.dimen.new_dialog_middle_height_large);
                this.mTitleContentMarginHeight = Utils.getDimens(this.mContext, R.dimen.new_dialog_margin_large);
                this.mContentCustomMarginHeight = this.mTitleContentMarginHeight;
                RelativeLayout.LayoutParams lpMiddle = (RelativeLayout.LayoutParams) this.mMiddleContentLy.getLayoutParams();
                lpMiddle.height = this.mMaxMiddleContentHeight;
                this.mMiddleContentLy.setLayoutParams(lpMiddle);
                break;
            default:
                this.mMaxTopCustomHeight = Utils.getDimens(this.mContext, R.dimen.s_120_dp);
                this.mMaxMiddleContentHeight = Utils.getDimens(this.mContext, R.dimen.new_dialog_middle_height_small);
                this.mTitleContentMarginHeight = Utils.getDimens(this.mContext, R.dimen.new_dialog_margin_small);
                this.mContentCustomMarginHeight = this.mTitleContentMarginHeight;
                break;
        }
        this.mTitleContentMargin.setLayoutParams(new LinearLayout.LayoutParams(-1, this.mTitleContentMarginHeight));
        this.mContentCustomMargin.setLayoutParams(new LinearLayout.LayoutParams(-1, this.mContentCustomMarginHeight));
    }

    /* access modifiers changed from: protected */
    public void updateDividerHeight(View view, int height) {
        LinearLayout.LayoutParams lpMiddle = (LinearLayout.LayoutParams) view.getLayoutParams();
        lpMiddle.height = height;
        view.setLayoutParams(lpMiddle);
    }

    /* access modifiers changed from: protected */
    public void setCustomView(View view) {
        addView(view);
    }

    /* access modifiers changed from: protected */
    public void addView(View view) {
        addView(view, -1);
    }

    /* access modifiers changed from: protected */
    public void addView(View view, int index) {
        if (view != null) {
            this.mViewContainer.addView(view, index);
            if (this.mViewContainer.getVisibility() != 0) {
                this.mViewContainer.setVisibility(0);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void addTopView(View view) {
        if (view != null) {
            this.mTopCustomContainer.addView(view);
            if (this.mTopCustomContainer.getVisibility() != 0) {
                this.mTopCustomContainer.setVisibility(0);
            }
        }
    }

    public void setTitle(int titleId) {
        if (TextUtils.isEmpty(this.mContext.getResources().getString(titleId))) {
            hideTitleView();
            return;
        }
        initTitleColor();
        this.mTitle.setText(titleId);
        this.mTitleLayout.setVisibility(0);
        this.mTitleContentMargin.setVisibility(0);
        if (this.mViewContainer.getVisibility() == 0 && this.mContent.getVisibility() == 0) {
            this.mContentCustomMargin.setVisibility(0);
        }
    }

    public void setTitle(CharSequence title) {
        if (TextUtils.isEmpty(title)) {
            hideTitleView();
            return;
        }
        initTitleColor();
        this.mTitle.setText(title);
        this.mTitleLayout.setVisibility(0);
        this.mTitleContentMargin.setVisibility(0);
        if (this.mViewContainer.getVisibility() == 0 && this.mContent.getVisibility() == 0) {
            this.mContentCustomMargin.setVisibility(0);
        }
    }

    private void hideTitleView() {
        this.mTitleLayout.setVisibility(8);
        this.mTitleContentMargin.setVisibility(8);
    }

    public void setTitleSize(float size) {
        this.mTitle.setTextSize(size);
    }

    public void setTitleBold(boolean isBold) {
        this.mTitle.getPaint().setFakeBoldText(isBold);
    }

    private void initTitleColor() {
        if (this.mTheme == DJIDialogTheme.BLACK) {
            this.mTitle.setTextColor(this.mContext.getResources().getColor(R.color.white));
            return;
        }
        this.mTitle.setTextColor(this.mContext.getResources().getColor(R.color.black));
        this.mTitle.getPaint().setFakeBoldText(true);
    }

    public void setTitleIcon(int iconId) {
        this.mTitleIcon.setImageResource(iconId);
        this.mTitleIcon.setVisibility(0);
    }

    public void setIconType(int type) {
        if (1 == type) {
            this.mTitleIcon.setImageResource(R.drawable.ic_popup_notify);
        } else if (2 == type) {
            this.mTitleIcon.setImageResource(R.drawable.ic_popup_oktips);
        } else if (3 == type) {
            this.mTitleIcon.setImageResource(R.drawable.ic_popup_warn);
        } else if (4 == type) {
            this.mTitleIcon.setImageResource(R.drawable.ic_popup_noflyzone);
        } else if (5 == type) {
            this.mTitleIcon.setImageResource(R.drawable.ic_popup_backtohome);
        }
        this.mTitleIcon.setVisibility(0);
    }

    public void setCloseVisibility(boolean show) {
        if (show) {
            this.mCloseView.setOnClickListener(new View.OnClickListener() {
                /* class dji.publics.widget.dialog.DJIDialog.AnonymousClass3 */

                public void onClick(View v) {
                    DJIDialog.this.dismiss();
                }
            });
            this.mCloseView.setVisibility(0);
            return;
        }
        this.mCloseView.setVisibility(8);
    }

    public DJIDialog setContent(int contentId) {
        if (TextUtils.isEmpty(this.mContext.getResources().getString(contentId))) {
            hideContentView();
        } else {
            initContentColor(this.mContent);
            this.mContent.setText(contentId);
            this.mContent.setVisibility(0);
            if (this.mViewContainer.getVisibility() == 0) {
                this.mContentCustomMargin.setVisibility(0);
            }
        }
        return this;
    }

    public DJIDialog setContent(CharSequence content) {
        if (TextUtils.isEmpty(content)) {
            hideContentView();
        } else {
            initContentColor(this.mContent);
            this.mContent.setText(content);
            this.mContent.setVisibility(0);
            if (this.mViewContainer.getVisibility() == 0) {
                this.mContentCustomMargin.setVisibility(0);
            }
        }
        return this;
    }

    private void hideContentView() {
        this.mContent.setVisibility(8);
        this.mContentCustomMargin.setVisibility(8);
    }

    /* access modifiers changed from: protected */
    public void initContentColor(TextView contentTv) {
        if (contentTv != null) {
            if (this.mTheme == DJIDialogTheme.BLACK) {
                contentTv.setTextColor(this.mContext.getResources().getColor(R.color.white));
            } else {
                contentTv.setTextColor(this.mContext.getResources().getColor(R.color.dlg_black_text));
            }
        }
    }

    public void setContentTextGravity(int gravity) {
        this.mContent.setGravity(gravity);
    }

    public void setContentMaxHeight(@DimenRes int dimen) {
        this.mContent.setMaxHeight(Utils.getDimens(this.mContext, dimen));
    }

    public void setMiddleMaxHeight(@DimenRes int dimen) {
        this.mMaxMiddleContentHeight = Utils.getDimens(this.mContext, dimen);
    }

    public DJIDialog setLeftBtn(int textId, DialogInterface.OnClickListener listener) {
        return setLeftBtn(getContext().getString(textId), listener, false);
    }

    public DJIDialog setLeftBtn(String text, DialogInterface.OnClickListener listener) {
        return setLeftBtn(text, listener, false);
    }

    public DJIDialog setLeftBtn(String text, DialogInterface.OnClickListener listener, boolean isHighlight) {
        initBtnTextColor(this.mLeftBtn, isHighlight);
        this.mLeftBtn.setText(text);
        this.mLeftListener = listener;
        this.mLeftBtn.setOnClickListener(new View.OnClickListener() {
            /* class dji.publics.widget.dialog.DJIDialog.AnonymousClass4 */

            public void onClick(View v) {
                DJIDialog.this.onClickEvent(DJIDialog.this.mLeftListener);
            }
        });
        showLeftBtn();
        return this;
    }

    public DJIDialog setLeftBtnText(int textId, boolean isHighlight) {
        initBtnTextColor(this.mLeftBtn, isHighlight);
        this.mLeftBtn.setText(textId);
        return this;
    }

    public DJIDialog setLeftBtnText(CharSequence text) {
        this.mLeftBtn.setText(text);
        return this;
    }

    public DJIDialog setBtnHighlight(boolean isLeft, boolean isHighlight) {
        initBtnTextColor(isLeft ? this.mLeftBtn : this.mRightBtn, isHighlight);
        return this;
    }

    /* access modifiers changed from: protected */
    public void initBtnTextColor(DJITextView tv, boolean isHighlight) {
        if (tv != null) {
            if (this.mTheme == DJIDialogTheme.BLACK) {
                tv.setTextColor(isHighlight ? this.mContext.getResources().getColorStateList(R.color.dlg_btn_highlight_bg) : this.mContext.getResources().getColorStateList(R.color.dlg_btn_text_color));
            } else {
                tv.setTextColor(isHighlight ? this.mContext.getResources().getColorStateList(R.color.dlg_white_btn_highlight_bg) : this.mContext.getResources().getColorStateList(R.color.dlg_white_btn_text_color));
            }
            if (isHighlight) {
                tv.getPaint().setFakeBoldText(true);
            }
        }
    }

    public DJIDialog setRightBtn(int textId, DialogInterface.OnClickListener listener) {
        return setRightBtn(getContext().getString(textId), listener, true);
    }

    public DJIDialog setRightBtn(String text, DialogInterface.OnClickListener listener) {
        return setRightBtn(text, listener, true);
    }

    public DJIDialog setRightBtn(String text, DialogInterface.OnClickListener listener, boolean isHighlight) {
        initBtnTextColor(this.mRightBtn, isHighlight);
        this.mRightBtn.setText(text);
        this.mRightListener = listener;
        this.mRightBtn.setOnClickListener(new View.OnClickListener() {
            /* class dji.publics.widget.dialog.DJIDialog.AnonymousClass5 */

            public void onClick(View v) {
                DJIDialog.this.onClickEvent(DJIDialog.this.mRightListener);
            }
        });
        showRightBtn();
        return this;
    }

    public DJIDialog setRightBtnText(int textId, boolean isHighlight) {
        initBtnTextColor(this.mRightBtn, isHighlight);
        this.mRightBtn.setText(textId);
        return this;
    }

    public DJIDialog showLeftBtn() {
        if (this.mRightBtn.getVisibility() == 0) {
            this.mBtnDivider.setVisibility(0);
        } else {
            this.mContentDivider.setVisibility(0);
            this.mBottomBtnLy.setVisibility(0);
        }
        this.mLeftBtn.setVisibility(0);
        return this;
    }

    public DJIDialog hideLeftBtn() {
        if (this.mLeftBtn.getVisibility() == 0) {
            this.mLeftBtn.setVisibility(8);
            if (this.mRightBtn.getVisibility() == 8) {
                this.mContentDivider.setVisibility(8);
                this.mBottomBtnLy.setVisibility(8);
            } else {
                this.mBtnDivider.setVisibility(8);
            }
        }
        return this;
    }

    public DJIDialog showRightBtn() {
        if (this.mLeftBtn.getVisibility() == 0) {
            this.mBtnDivider.setVisibility(0);
        } else {
            this.mContentDivider.setVisibility(0);
            this.mBottomBtnLy.setVisibility(0);
        }
        this.mRightBtn.setVisibility(0);
        return this;
    }

    public DJIDialog hideRightBtn() {
        if (this.mRightBtn.getVisibility() == 0) {
            this.mRightBtn.setVisibility(8);
            if (this.mLeftBtn.getVisibility() == 8) {
                this.mContentDivider.setVisibility(8);
                this.mBottomBtnLy.setVisibility(8);
            } else {
                this.mBtnDivider.setVisibility(8);
            }
        }
        return this;
    }

    public DJIDialog hideBottomBtn() {
        if (this.mContentDivider.getVisibility() == 0) {
            this.mContentDivider.setVisibility(8);
            this.mBottomBtnLy.setVisibility(8);
        }
        return this;
    }

    public void setDialogAutoDismiss(boolean auto) {
        this.mIsAuto = auto;
    }

    /* access modifiers changed from: private */
    public void onClickEvent(DialogInterface.OnClickListener listener) {
        if (listener != null) {
            listener.onClick(this, 0);
        }
        if (isShowing() && this.mIsAuto) {
            dismiss();
        }
    }

    public void show() {
        if (!isForbidShowDialog() && !isActivityFinish()) {
            if (!this.mIsShow) {
                requestMeasureHeight();
                this.mIsShow = true;
            }
            super.show();
        }
    }

    public void dismiss() {
        if (!isActivityFinish()) {
            super.dismiss();
        }
    }

    private boolean isActivityFinish() {
        if (this.mContext instanceof Activity) {
            if (((Activity) this.mContext).isFinishing()) {
                return true;
            }
        } else if (!(this.mContext instanceof Activity) && (this.mContext instanceof ContextWrapper)) {
            Context context = ((ContextWrapper) this.mContext).getBaseContext();
            if ((context instanceof Activity) && ((Activity) context).isFinishing()) {
                return true;
            }
        }
        return false;
    }

    private void requestMeasureHeight() {
        measureTopHeight();
        measureMiddleHeight();
    }

    private void measureMiddleHeight() {
        if (this.mDialogType == DJIDialogType.LARGE) {
            this.mContentSpaceTop.setVisibility(8);
            this.mContentSpaceBottom.setVisibility(8);
            return;
        }
        this.mMiddleContentLy.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            /* class dji.publics.widget.dialog.DJIDialog.AnonymousClass6 */

            public void onGlobalLayout() {
                DJIDialog.this.mMiddleContentLy.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                if (DJIDialog.this.mMiddleContentLy.getMeasuredHeight() > DJIDialog.this.mMaxMiddleContentHeight - (Utils.getDimens(DJIDialog.this.mContext, R.dimen.s_15_dp) * 2)) {
                    DJIDialog.this.mContentSpaceTop.setVisibility(8);
                    DJIDialog.this.mContentSpaceBottom.setVisibility(8);
                    RelativeLayout.LayoutParams lpMiddle = (RelativeLayout.LayoutParams) DJIDialog.this.mMiddleContentLy.getLayoutParams();
                    lpMiddle.height = DJIDialog.this.mMaxMiddleContentHeight;
                    DJIDialog.this.mMiddleContentLy.setLayoutParams(lpMiddle);
                }
            }
        });
    }

    private void measureTopHeight() {
        if (this.mTopCustomContainer.getVisibility() == 0) {
            this.mContentSpaceTop.setVisibility(8);
            this.mContentSpaceBottom.setVisibility(8);
            this.mTopCustomContainer.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
            int topViewHeight = this.mTopCustomContainer.getMeasuredHeight();
            RelativeLayout.LayoutParams lpTop = (RelativeLayout.LayoutParams) this.mTopCustomContainer.getLayoutParams();
            if (topViewHeight > this.mMaxTopCustomHeight) {
                topViewHeight = this.mMaxTopCustomHeight;
            }
            lpTop.height = topViewHeight;
            this.mTopCustomContainer.setLayoutParams(lpTop);
        }
    }

    public static void setForbidShowDialog(boolean forbidShowDialog) {
        sForbidShowDialog = forbidShowDialog;
    }

    public static boolean isForbidShowDialog() {
        return sForbidShowDialog;
    }
}
