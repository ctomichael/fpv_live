package in.srain.cube.views.ptr;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;
import android.widget.TextView;
import dji.frame.widget.R;
import in.srain.cube.views.ptr.indicator.PtrIndicator;
import java.util.Locale;

public class PtrFrameLayout extends ViewGroup {
    private static final boolean DEBUG_LAYOUT = true;
    private static final byte FLAG_AUTO_REFRESH_AT_ONCE = 1;
    private static final byte FLAG_AUTO_REFRESH_BUT_LATER = 2;
    private static final byte FLAG_ENABLE_NEXT_PTR_AT_ONCE = 4;
    private static final byte FLAG_PIN_CONTENT = 8;
    private static final String LOG_TAG = "ptr-frame";
    private static final byte MASK_AUTO_REFRESH = 3;
    public static final byte PTR_STATUS_COMPLETE = 4;
    public static final byte PTR_STATUS_INIT = 1;
    public static final byte PTR_STATUS_LOADING = 3;
    public static final byte PTR_STATUS_PREPARE = 2;
    private int mContainerId;
    protected View mContent;
    private boolean mDisableWhenHorizontalMove;
    private int mDurationToClose;
    private int mDurationToCloseHeader;
    private int mFlag;
    private boolean mHasSendCancelEvent;
    private int mHeaderHeight;
    private int mHeaderId;
    private View mHeaderView;
    /* access modifiers changed from: private */
    public boolean mIsDebugEnabled;
    private boolean mKeepHeaderWhenRefresh;
    private MotionEvent mLastMoveEvent;
    private int mLoadingMinTime;
    private long mLoadingStartTime;
    private int mPagingTouchSlop;
    private final Runnable mPerformRefreshCompleteDelay;
    private boolean mPreventForHorizontal;
    private PtrHandler mPtrHandler;
    /* access modifiers changed from: private */
    public PtrIndicator mPtrIndicator;
    private PtrUIHandlerHolder mPtrUIHandlerHolder;
    private boolean mPullToRefresh;
    private PtrUIHandlerHook mRefreshCompleteHook;
    private ScrollChecker mScrollChecker;
    private byte mStatus;

    public PtrFrameLayout(Context context) {
        this(context, null);
    }

    public PtrFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PtrFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mStatus = 1;
        this.mIsDebugEnabled = false;
        this.mHeaderId = 0;
        this.mContainerId = 0;
        this.mDurationToClose = 200;
        this.mDurationToCloseHeader = 1000;
        this.mKeepHeaderWhenRefresh = true;
        this.mPullToRefresh = false;
        this.mPtrUIHandlerHolder = PtrUIHandlerHolder.create();
        this.mDisableWhenHorizontalMove = false;
        this.mFlag = 0;
        this.mPreventForHorizontal = false;
        this.mLoadingMinTime = 500;
        this.mLoadingStartTime = 0;
        this.mHasSendCancelEvent = false;
        this.mPerformRefreshCompleteDelay = new Runnable() {
            /* class in.srain.cube.views.ptr.PtrFrameLayout.AnonymousClass1 */

            public void run() {
                PtrFrameLayout.this.performRefreshComplete();
            }
        };
        this.mPtrIndicator = new PtrIndicator();
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.PtrFrameLayout, 0, 0);
        if (arr != null) {
            this.mHeaderId = arr.getResourceId(R.styleable.PtrFrameLayout_ptr_header, this.mHeaderId);
            this.mContainerId = arr.getResourceId(R.styleable.PtrFrameLayout_ptr_content, this.mContainerId);
            this.mPtrIndicator.setResistance(arr.getFloat(R.styleable.PtrFrameLayout_ptr_resistance, this.mPtrIndicator.getResistance()));
            this.mDurationToClose = arr.getInt(R.styleable.PtrFrameLayout_ptr_duration_to_close, this.mDurationToClose);
            this.mDurationToCloseHeader = arr.getInt(R.styleable.PtrFrameLayout_ptr_duration_to_close_header, this.mDurationToCloseHeader);
            this.mPtrIndicator.setRatioOfHeaderHeightToRefresh(arr.getFloat(R.styleable.PtrFrameLayout_ptr_ratio_of_header_height_to_refresh, this.mPtrIndicator.getRatioOfHeaderToHeightRefresh()));
            this.mKeepHeaderWhenRefresh = arr.getBoolean(R.styleable.PtrFrameLayout_ptr_keep_header_when_refresh, this.mKeepHeaderWhenRefresh);
            this.mPullToRefresh = arr.getBoolean(R.styleable.PtrFrameLayout_ptr_pull_to_fresh, this.mPullToRefresh);
            arr.recycle();
        }
        this.mScrollChecker = new ScrollChecker();
        this.mPagingTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        int childCount = getChildCount();
        if (childCount > 2) {
            throw new IllegalStateException("PtrFrameLayout can only contains 2 children");
        }
        if (childCount == 2) {
            if (this.mHeaderId != 0 && this.mHeaderView == null) {
                this.mHeaderView = findViewById(this.mHeaderId);
            }
            if (this.mContainerId != 0 && this.mContent == null) {
                this.mContent = findViewById(this.mContainerId);
            }
            if (this.mContent == null || this.mHeaderView == null) {
                View child1 = getChildAt(0);
                View child2 = getChildAt(1);
                if (child1 instanceof PtrUIHandler) {
                    this.mHeaderView = child1;
                    this.mContent = child2;
                } else if (child2 instanceof PtrUIHandler) {
                    this.mHeaderView = child2;
                    this.mContent = child1;
                } else if (this.mContent == null && this.mHeaderView == null) {
                    this.mHeaderView = child1;
                    this.mContent = child2;
                } else if (this.mHeaderView == null) {
                    if (this.mContent != child1) {
                        child2 = child1;
                    }
                    this.mHeaderView = child2;
                } else {
                    if (this.mHeaderView != child1) {
                        child2 = child1;
                    }
                    this.mContent = child2;
                }
            }
        } else if (childCount == 1) {
            this.mContent = getChildAt(0);
        } else {
            TextView errorView = new TextView(getContext());
            errorView.setClickable(true);
            errorView.setTextColor(-39424);
            errorView.setGravity(17);
            errorView.setTextSize(20.0f);
            errorView.setText("The content view in PtrFrameLayout is empty. Do you forget to specify its id in xml layout file?");
            this.mContent = errorView;
            addView(this.mContent);
        }
        if (this.mHeaderView != null) {
            this.mHeaderView.bringToFront();
        }
        super.onFinishInflate();
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mScrollChecker != null) {
            this.mScrollChecker.destroy();
        }
        if (this.mPerformRefreshCompleteDelay != null) {
            removeCallbacks(this.mPerformRefreshCompleteDelay);
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (isDebug()) {
            PtrCLog.d(LOG_TAG, "onMeasure frame: width: %s, height: %s, padding: %s %s %s %s", Integer.valueOf(getMeasuredHeight()), Integer.valueOf(getMeasuredWidth()), Integer.valueOf(getPaddingLeft()), Integer.valueOf(getPaddingRight()), Integer.valueOf(getPaddingTop()), Integer.valueOf(getPaddingBottom()));
        }
        if (this.mHeaderView != null) {
            measureChildWithMargins(this.mHeaderView, widthMeasureSpec, 0, heightMeasureSpec, 0);
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) this.mHeaderView.getLayoutParams();
            this.mHeaderHeight = this.mHeaderView.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            this.mPtrIndicator.setHeaderHeight(this.mHeaderHeight);
        }
        if (this.mContent != null) {
            measureContentView(this.mContent, widthMeasureSpec, heightMeasureSpec);
            if (isDebug()) {
                ViewGroup.MarginLayoutParams lp2 = (ViewGroup.MarginLayoutParams) this.mContent.getLayoutParams();
                PtrCLog.d(LOG_TAG, "onMeasure content, width: %s, height: %s, margin: %s %s %s %s", Integer.valueOf(getMeasuredWidth()), Integer.valueOf(getMeasuredHeight()), Integer.valueOf(lp2.leftMargin), Integer.valueOf(lp2.topMargin), Integer.valueOf(lp2.rightMargin), Integer.valueOf(lp2.bottomMargin));
                PtrCLog.d(LOG_TAG, "onMeasure, currentPos: %s, lastPos: %s, top: %s", Integer.valueOf(this.mPtrIndicator.getCurrentPosY()), Integer.valueOf(this.mPtrIndicator.getLastPosY()), Integer.valueOf(this.mContent.getTop()));
            }
        }
    }

    private void measureContentView(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
        child.measure(getChildMeasureSpec(parentWidthMeasureSpec, getPaddingLeft() + getPaddingRight() + lp.leftMargin + lp.rightMargin, lp.width), getChildMeasureSpec(parentHeightMeasureSpec, getPaddingTop() + getPaddingBottom() + lp.topMargin, lp.height));
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean flag, int i, int j, int k, int l) {
        layoutChildren();
    }

    private void layoutChildren() {
        int offset = this.mPtrIndicator.getCurrentPosY();
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        if (this.mHeaderView != null) {
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) this.mHeaderView.getLayoutParams();
            int left = paddingLeft + lp.leftMargin;
            int top = -(((this.mHeaderHeight - paddingTop) - lp.topMargin) - offset);
            int right = left + this.mHeaderView.getMeasuredWidth();
            int bottom = top + this.mHeaderView.getMeasuredHeight();
            this.mHeaderView.layout(left, top, right, bottom);
            if (isDebug()) {
                PtrCLog.d(LOG_TAG, "onLayout header: %s %s %s %s", Integer.valueOf(left), Integer.valueOf(top), Integer.valueOf(right), Integer.valueOf(bottom));
            }
        }
        if (this.mContent != null) {
            if (isPinContent()) {
                offset = 0;
            }
            ViewGroup.MarginLayoutParams lp2 = (ViewGroup.MarginLayoutParams) this.mContent.getLayoutParams();
            int left2 = paddingLeft + lp2.leftMargin;
            int top2 = lp2.topMargin + paddingTop + offset;
            int right2 = left2 + this.mContent.getMeasuredWidth();
            int bottom2 = top2 + this.mContent.getMeasuredHeight();
            if (isDebug()) {
                PtrCLog.d(LOG_TAG, "onLayout content: %s %s %s %s", Integer.valueOf(left2), Integer.valueOf(top2), Integer.valueOf(right2), Integer.valueOf(bottom2));
            }
            this.mContent.layout(left2, top2, right2, bottom2);
        }
    }

    private boolean isDebug() {
        return this.mIsDebugEnabled;
    }

    public boolean dispatchTouchEventSupper(MotionEvent e) {
        return super.dispatchTouchEvent(e);
    }

    /* JADX INFO: Can't fix incorrect switch cases order, some code will duplicate */
    public boolean dispatchTouchEvent(MotionEvent e) {
        boolean moveDown;
        boolean moveUp;
        boolean canMoveDown;
        if (!isEnabled() || this.mContent == null || this.mHeaderView == null) {
            return dispatchTouchEventSupper(e);
        }
        switch (e.getAction()) {
            case 0:
                this.mHasSendCancelEvent = false;
                this.mPtrIndicator.onPressDown(e.getX(), e.getY());
                this.mScrollChecker.abortIfWorking();
                this.mPreventForHorizontal = false;
                dispatchTouchEventSupper(e);
                return true;
            case 1:
            case 3:
                this.mPtrIndicator.onRelease();
                if (!this.mPtrIndicator.hasLeftStartPosition()) {
                    return dispatchTouchEventSupper(e);
                }
                if (this.mIsDebugEnabled) {
                    PtrCLog.d(LOG_TAG, "call onRelease when user release");
                }
                onRelease(false);
                if (!this.mPtrIndicator.hasMovedAfterPressedDown()) {
                    return dispatchTouchEventSupper(e);
                }
                sendCancelEvent();
                return true;
            case 2:
                this.mLastMoveEvent = e;
                this.mPtrIndicator.onMove(e.getX(), e.getY());
                float offsetX = this.mPtrIndicator.getOffsetX();
                float offsetY = this.mPtrIndicator.getOffsetY();
                if (this.mDisableWhenHorizontalMove && !this.mPreventForHorizontal && Math.abs(offsetX) > ((float) this.mPagingTouchSlop) && Math.abs(offsetX) > Math.abs(offsetY) && this.mPtrIndicator.isInStartPosition()) {
                    this.mPreventForHorizontal = true;
                }
                if (this.mPreventForHorizontal) {
                    return dispatchTouchEventSupper(e);
                }
                if (offsetY > 0.0f) {
                    moveDown = true;
                } else {
                    moveDown = false;
                }
                if (!moveDown) {
                    moveUp = true;
                } else {
                    moveUp = false;
                }
                boolean canMoveUp = this.mPtrIndicator.hasLeftStartPosition();
                if (this.mIsDebugEnabled) {
                    if (this.mPtrHandler == null || !this.mPtrHandler.checkCanDoRefresh(this, this.mContent, this.mHeaderView)) {
                        canMoveDown = false;
                    } else {
                        canMoveDown = true;
                    }
                    PtrCLog.v(LOG_TAG, "ACTION_MOVE: offsetY:%s, currentPos: %s, moveUp: %s, canMoveUp: %s, moveDown: %s: canMoveDown: %s", Float.valueOf(offsetY), Integer.valueOf(this.mPtrIndicator.getCurrentPosY()), Boolean.valueOf(moveUp), Boolean.valueOf(canMoveUp), Boolean.valueOf(moveDown), Boolean.valueOf(canMoveDown));
                }
                if (moveDown && this.mPtrHandler != null && !this.mPtrHandler.checkCanDoRefresh(this, this.mContent, this.mHeaderView)) {
                    return dispatchTouchEventSupper(e);
                }
                if ((moveUp && canMoveUp) || moveDown) {
                    movePos(offsetY);
                    return true;
                }
                break;
        }
        return dispatchTouchEventSupper(e);
    }

    /* access modifiers changed from: private */
    public void movePos(float deltaY) {
        if (deltaY >= 0.0f || !this.mPtrIndicator.isInStartPosition()) {
            int to = this.mPtrIndicator.getCurrentPosY() + ((int) deltaY);
            if (this.mPtrIndicator.willOverTop(to)) {
                if (this.mIsDebugEnabled) {
                    PtrCLog.e(LOG_TAG, String.format(Locale.US, "over top", new Object[0]));
                }
                to = 0;
            }
            this.mPtrIndicator.setCurrentPos(to);
            updatePos(to - this.mPtrIndicator.getLastPosY());
        } else if (this.mIsDebugEnabled) {
            PtrCLog.e(LOG_TAG, String.format(Locale.US, "has reached the top", new Object[0]));
        }
    }

    private void updatePos(int change) {
        if (change != 0) {
            boolean isUnderTouch = this.mPtrIndicator.isUnderTouch();
            if (isUnderTouch && !this.mHasSendCancelEvent && this.mPtrIndicator.hasMovedAfterPressedDown()) {
                this.mHasSendCancelEvent = true;
                sendCancelEvent();
            }
            if ((this.mPtrIndicator.hasJustLeftStartPosition() && this.mStatus == 1) || (this.mPtrIndicator.goDownCrossFinishPosition() && this.mStatus == 4 && isEnabledNextPtrAtOnce())) {
                this.mStatus = 2;
                this.mPtrUIHandlerHolder.onUIRefreshPrepare(this);
                if (this.mIsDebugEnabled) {
                    PtrCLog.i(LOG_TAG, "PtrUIHandler: onUIRefreshPrepare, mFlag %s", Integer.valueOf(this.mFlag));
                }
            }
            if (this.mPtrIndicator.hasJustBackToStartPosition()) {
                tryToNotifyReset();
                if (isUnderTouch) {
                    sendDownEvent();
                }
            }
            if (this.mStatus == 2) {
                if (isUnderTouch && !isAutoRefresh() && this.mPullToRefresh && this.mPtrIndicator.crossRefreshLineFromTopToBottom()) {
                    tryToPerformRefresh();
                }
                if (performAutoRefreshButLater() && this.mPtrIndicator.hasJustReachedHeaderHeightFromTopToBottom()) {
                    tryToPerformRefresh();
                }
            }
            if (this.mIsDebugEnabled) {
                PtrCLog.v(LOG_TAG, "updatePos: change: %s, current: %s last: %s, top: %s, headerHeight: %s", Integer.valueOf(change), Integer.valueOf(this.mPtrIndicator.getCurrentPosY()), Integer.valueOf(this.mPtrIndicator.getLastPosY()), Integer.valueOf(this.mContent.getTop()), Integer.valueOf(this.mHeaderHeight));
            }
            this.mHeaderView.offsetTopAndBottom(change);
            if (!isPinContent()) {
                this.mContent.offsetTopAndBottom(change);
            }
            invalidate();
            if (this.mPtrUIHandlerHolder.hasHandler()) {
                this.mPtrUIHandlerHolder.onUIPositionChange(this, isUnderTouch, this.mStatus, this.mPtrIndicator);
            }
            onPositionChange(isUnderTouch, this.mStatus, this.mPtrIndicator);
        }
    }

    /* access modifiers changed from: protected */
    public void onPositionChange(boolean isInTouching, byte status, PtrIndicator mPtrIndicator2) {
    }

    public int getHeaderHeight() {
        return this.mHeaderHeight;
    }

    private void onRelease(boolean stayForLoading) {
        tryToPerformRefresh();
        if (this.mStatus == 3) {
            if (!this.mKeepHeaderWhenRefresh) {
                tryScrollBackToTopWhileLoading();
            } else if (this.mPtrIndicator.isOverOffsetToKeepHeaderWhileLoading() && !stayForLoading) {
                this.mScrollChecker.tryToScrollTo(this.mPtrIndicator.getOffsetToKeepHeaderWhileLoading(), this.mDurationToClose);
            }
        } else if (this.mStatus == 4) {
            notifyUIRefreshComplete(false);
        } else {
            tryScrollBackToTopAbortRefresh();
        }
    }

    public void setRefreshCompleteHook(PtrUIHandlerHook hook) {
        this.mRefreshCompleteHook = hook;
        hook.setResumeAction(new Runnable() {
            /* class in.srain.cube.views.ptr.PtrFrameLayout.AnonymousClass2 */

            public void run() {
                if (PtrFrameLayout.this.mIsDebugEnabled) {
                    PtrCLog.d(PtrFrameLayout.LOG_TAG, "mRefreshCompleteHook resume.");
                }
                PtrFrameLayout.this.notifyUIRefreshComplete(true);
            }
        });
    }

    private void tryScrollBackToTop() {
        if (!this.mPtrIndicator.isUnderTouch()) {
            this.mScrollChecker.tryToScrollTo(0, this.mDurationToCloseHeader);
        }
    }

    private void tryScrollBackToTopWhileLoading() {
        tryScrollBackToTop();
    }

    private void tryScrollBackToTopAfterComplete() {
        tryScrollBackToTop();
    }

    private void tryScrollBackToTopAbortRefresh() {
        tryScrollBackToTop();
    }

    private boolean tryToPerformRefresh() {
        if (this.mStatus == 2 && ((this.mPtrIndicator.isOverOffsetToKeepHeaderWhileLoading() && isAutoRefresh()) || this.mPtrIndicator.isOverOffsetToRefresh())) {
            this.mStatus = 3;
            performRefresh();
        }
        return false;
    }

    private void performRefresh() {
        this.mLoadingStartTime = System.currentTimeMillis();
        if (this.mPtrUIHandlerHolder.hasHandler()) {
            this.mPtrUIHandlerHolder.onUIRefreshBegin(this);
            if (this.mIsDebugEnabled) {
                PtrCLog.i(LOG_TAG, "PtrUIHandler: onUIRefreshBegin");
            }
        }
        if (this.mPtrHandler != null) {
            this.mPtrHandler.onRefreshBegin(this);
        }
    }

    private boolean tryToNotifyReset() {
        if ((this.mStatus != 4 && this.mStatus != 2) || !this.mPtrIndicator.isInStartPosition()) {
            return false;
        }
        if (this.mPtrUIHandlerHolder.hasHandler()) {
            this.mPtrUIHandlerHolder.onUIReset(this);
            if (this.mIsDebugEnabled) {
                PtrCLog.i(LOG_TAG, "PtrUIHandler: onUIReset");
            }
        }
        this.mStatus = 1;
        clearFlag();
        return true;
    }

    /* access modifiers changed from: protected */
    public void onPtrScrollAbort() {
        if (this.mPtrIndicator.hasLeftStartPosition() && isAutoRefresh()) {
            if (this.mIsDebugEnabled) {
                PtrCLog.d(LOG_TAG, "call onRelease after scroll abort");
            }
            onRelease(true);
        }
    }

    /* access modifiers changed from: protected */
    public void onPtrScrollFinish() {
        if (this.mPtrIndicator.hasLeftStartPosition() && isAutoRefresh()) {
            if (this.mIsDebugEnabled) {
                PtrCLog.d(LOG_TAG, "call onRelease after scroll finish");
            }
            onRelease(true);
        }
    }

    public boolean isRefreshing() {
        return this.mStatus == 3;
    }

    public final void refreshComplete() {
        if (this.mIsDebugEnabled) {
            PtrCLog.i(LOG_TAG, "refreshComplete");
        }
        if (this.mRefreshCompleteHook != null) {
            this.mRefreshCompleteHook.reset();
        }
        int delay = (int) (((long) this.mLoadingMinTime) - (System.currentTimeMillis() - this.mLoadingStartTime));
        if (delay <= 0) {
            if (this.mIsDebugEnabled) {
                PtrCLog.d(LOG_TAG, "performRefreshComplete at once");
            }
            performRefreshComplete();
            return;
        }
        postDelayed(this.mPerformRefreshCompleteDelay, (long) delay);
        if (this.mIsDebugEnabled) {
            PtrCLog.d(LOG_TAG, "performRefreshComplete after delay: %s", Integer.valueOf(delay));
        }
    }

    /* access modifiers changed from: private */
    public void performRefreshComplete() {
        this.mStatus = 4;
        if (!this.mScrollChecker.mIsRunning || !isAutoRefresh()) {
            notifyUIRefreshComplete(false);
        } else if (this.mIsDebugEnabled) {
            PtrCLog.d(LOG_TAG, "performRefreshComplete do nothing, scrolling: %s, auto refresh: %s", Boolean.valueOf(this.mScrollChecker.mIsRunning), Integer.valueOf(this.mFlag));
        }
    }

    /* access modifiers changed from: private */
    public void notifyUIRefreshComplete(boolean ignoreHook) {
        if (!this.mPtrIndicator.hasLeftStartPosition() || ignoreHook || this.mRefreshCompleteHook == null) {
            if (this.mPtrUIHandlerHolder.hasHandler()) {
                if (this.mIsDebugEnabled) {
                    PtrCLog.i(LOG_TAG, "PtrUIHandler: onUIRefreshComplete");
                }
                this.mPtrUIHandlerHolder.onUIRefreshComplete(this);
            }
            this.mPtrIndicator.onUIRefreshComplete();
            tryScrollBackToTopAfterComplete();
            tryToNotifyReset();
            return;
        }
        if (this.mIsDebugEnabled) {
            PtrCLog.d(LOG_TAG, "notifyUIRefreshComplete mRefreshCompleteHook run.");
        }
        this.mRefreshCompleteHook.takeOver();
    }

    public void autoRefresh() {
        autoRefresh(true, this.mDurationToCloseHeader);
    }

    public void autoRefresh(boolean atOnce) {
        autoRefresh(atOnce, this.mDurationToCloseHeader);
    }

    private void clearFlag() {
        this.mFlag &= -4;
    }

    public void autoRefresh(boolean atOnce, int duration) {
        if (this.mStatus == 1) {
            this.mFlag = (atOnce ? 1 : 2) | this.mFlag;
            this.mStatus = 2;
            if (this.mPtrUIHandlerHolder.hasHandler()) {
                this.mPtrUIHandlerHolder.onUIRefreshPrepare(this);
                if (this.mIsDebugEnabled) {
                    PtrCLog.i(LOG_TAG, "PtrUIHandler: onUIRefreshPrepare, mFlag %s", Integer.valueOf(this.mFlag));
                }
            }
            this.mScrollChecker.tryToScrollTo(this.mPtrIndicator.getOffsetToRefresh(), duration);
            if (atOnce) {
                this.mStatus = 3;
                performRefresh();
            }
        }
    }

    public boolean isAutoRefresh() {
        return (this.mFlag & 3) > 0;
    }

    private boolean performAutoRefreshButLater() {
        return (this.mFlag & 3) == 2;
    }

    public boolean isEnabledNextPtrAtOnce() {
        return (this.mFlag & 4) > 0;
    }

    public void setEnabledNextPtrAtOnce(boolean enable) {
        if (enable) {
            this.mFlag |= 4;
        } else {
            this.mFlag &= -5;
        }
    }

    public boolean isPinContent() {
        return (this.mFlag & 8) > 0;
    }

    public void setPinContent(boolean pinContent) {
        if (pinContent) {
            this.mFlag |= 8;
        } else {
            this.mFlag &= -9;
        }
    }

    public void disableWhenHorizontalMove(boolean disable) {
        this.mDisableWhenHorizontalMove = disable;
    }

    public void setLoadingMinTime(int time) {
        this.mLoadingMinTime = time;
    }

    @Deprecated
    public void setInterceptEventWhileWorking(boolean yes) {
    }

    public View getContentView() {
        return this.mContent;
    }

    public void setPtrHandler(PtrHandler ptrHandler) {
        this.mPtrHandler = ptrHandler;
    }

    public void addPtrUIHandler(PtrUIHandler ptrUIHandler) {
        PtrUIHandlerHolder.addHandler(this.mPtrUIHandlerHolder, ptrUIHandler);
    }

    public void removePtrUIHandler(PtrUIHandler ptrUIHandler) {
        this.mPtrUIHandlerHolder = PtrUIHandlerHolder.removeHandler(this.mPtrUIHandlerHolder, ptrUIHandler);
    }

    public void setPtrIndicator(PtrIndicator slider) {
        if (!(this.mPtrIndicator == null || this.mPtrIndicator == slider)) {
            slider.convertFrom(this.mPtrIndicator);
        }
        this.mPtrIndicator = slider;
    }

    public float getResistance() {
        return this.mPtrIndicator.getResistance();
    }

    public void setResistance(float resistance) {
        this.mPtrIndicator.setResistance(resistance);
    }

    public float getDurationToClose() {
        return (float) this.mDurationToClose;
    }

    public void setDurationToClose(int duration) {
        this.mDurationToClose = duration;
    }

    public long getDurationToCloseHeader() {
        return (long) this.mDurationToCloseHeader;
    }

    public void setDurationToCloseHeader(int duration) {
        this.mDurationToCloseHeader = duration;
    }

    public void setRatioOfHeaderHeightToRefresh(float ratio) {
        this.mPtrIndicator.setRatioOfHeaderHeightToRefresh(ratio);
    }

    public int getOffsetToRefresh() {
        return this.mPtrIndicator.getOffsetToRefresh();
    }

    public void setOffsetToRefresh(int offset) {
        this.mPtrIndicator.setOffsetToRefresh(offset);
    }

    public float getRatioOfHeaderToHeightRefresh() {
        return this.mPtrIndicator.getRatioOfHeaderToHeightRefresh();
    }

    public int getOffsetToKeepHeaderWhileLoading() {
        return this.mPtrIndicator.getOffsetToKeepHeaderWhileLoading();
    }

    public void setOffsetToKeepHeaderWhileLoading(int offset) {
        this.mPtrIndicator.setOffsetToKeepHeaderWhileLoading(offset);
    }

    public boolean isKeepHeaderWhenRefresh() {
        return this.mKeepHeaderWhenRefresh;
    }

    public void setKeepHeaderWhenRefresh(boolean keepOrNot) {
        this.mKeepHeaderWhenRefresh = keepOrNot;
    }

    public boolean isPullToRefresh() {
        return this.mPullToRefresh;
    }

    public void setPullToRefresh(boolean pullToRefresh) {
        this.mPullToRefresh = pullToRefresh;
    }

    public View getHeaderView() {
        return this.mHeaderView;
    }

    public void setHeaderView(View header) {
        if (header != null) {
            if (!(this.mHeaderView == null || this.mHeaderView == header)) {
                removeView(this.mHeaderView);
            }
            if (header.getLayoutParams() == null) {
                header.setLayoutParams(new LayoutParams(-1, -2));
            }
            this.mHeaderView = header;
            addView(header);
        }
    }

    /* access modifiers changed from: protected */
    public boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p != null && (p instanceof LayoutParams);
    }

    /* access modifiers changed from: protected */
    public ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-1, -1);
    }

    /* access modifiers changed from: protected */
    public ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    private void sendCancelEvent() {
        if (this.mIsDebugEnabled) {
            PtrCLog.d(LOG_TAG, "send cancel event");
        }
        if (this.mLastMoveEvent != null) {
            MotionEvent last = this.mLastMoveEvent;
            dispatchTouchEventSupper(MotionEvent.obtain(last.getDownTime(), last.getEventTime() + ((long) ViewConfiguration.getLongPressTimeout()), 3, last.getX(), last.getY(), last.getMetaState()));
        }
    }

    private void sendDownEvent() {
        if (this.mIsDebugEnabled) {
            PtrCLog.d(LOG_TAG, "send down event");
        }
        MotionEvent last = this.mLastMoveEvent;
        dispatchTouchEventSupper(MotionEvent.obtain(last.getDownTime(), last.getEventTime(), 0, last.getX(), last.getY(), last.getMetaState()));
    }

    public static class LayoutParams extends ViewGroup.MarginLayoutParams {
        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }

    class ScrollChecker implements Runnable {
        /* access modifiers changed from: private */
        public boolean mIsRunning = false;
        private int mLastFlingY;
        private Scroller mScroller;
        private int mStart;
        private int mTo;

        public ScrollChecker() {
            this.mScroller = new Scroller(PtrFrameLayout.this.getContext());
        }

        public void run() {
            boolean finish;
            if (!this.mScroller.computeScrollOffset() || this.mScroller.isFinished()) {
                finish = true;
            } else {
                finish = false;
            }
            int curY = this.mScroller.getCurrY();
            int deltaY = curY - this.mLastFlingY;
            if (PtrFrameLayout.this.mIsDebugEnabled && deltaY != 0) {
                PtrCLog.v(PtrFrameLayout.LOG_TAG, "scroll: %s, start: %s, to: %s, currentPos: %s, current :%s, last: %s, delta: %s", Boolean.valueOf(finish), Integer.valueOf(this.mStart), Integer.valueOf(this.mTo), Integer.valueOf(PtrFrameLayout.this.mPtrIndicator.getCurrentPosY()), Integer.valueOf(curY), Integer.valueOf(this.mLastFlingY), Integer.valueOf(deltaY));
            }
            if (!finish) {
                this.mLastFlingY = curY;
                PtrFrameLayout.this.movePos((float) deltaY);
                PtrFrameLayout.this.post(this);
                return;
            }
            finish();
        }

        private void finish() {
            if (PtrFrameLayout.this.mIsDebugEnabled) {
                PtrCLog.v(PtrFrameLayout.LOG_TAG, "finish, currentPos:%s", Integer.valueOf(PtrFrameLayout.this.mPtrIndicator.getCurrentPosY()));
            }
            reset();
            PtrFrameLayout.this.onPtrScrollFinish();
        }

        private void reset() {
            this.mIsRunning = false;
            this.mLastFlingY = 0;
            PtrFrameLayout.this.removeCallbacks(this);
        }

        /* access modifiers changed from: private */
        public void destroy() {
            reset();
            if (!this.mScroller.isFinished()) {
                this.mScroller.forceFinished(true);
            }
        }

        public void abortIfWorking() {
            if (this.mIsRunning) {
                if (!this.mScroller.isFinished()) {
                    this.mScroller.forceFinished(true);
                }
                PtrFrameLayout.this.onPtrScrollAbort();
                reset();
            }
        }

        public void tryToScrollTo(int to, int duration) {
            if (!PtrFrameLayout.this.mPtrIndicator.isAlreadyHere(to)) {
                this.mStart = PtrFrameLayout.this.mPtrIndicator.getCurrentPosY();
                this.mTo = to;
                int distance = to - this.mStart;
                if (PtrFrameLayout.this.mIsDebugEnabled) {
                    PtrCLog.d(PtrFrameLayout.LOG_TAG, "tryToScrollTo: start: %s, distance:%s, to:%s", Integer.valueOf(this.mStart), Integer.valueOf(distance), Integer.valueOf(to));
                }
                PtrFrameLayout.this.removeCallbacks(this);
                this.mLastFlingY = 0;
                if (!this.mScroller.isFinished()) {
                    this.mScroller.forceFinished(true);
                }
                this.mScroller.startScroll(0, 0, 0, distance, duration);
                PtrFrameLayout.this.post(this);
                this.mIsRunning = true;
            }
        }
    }
}
