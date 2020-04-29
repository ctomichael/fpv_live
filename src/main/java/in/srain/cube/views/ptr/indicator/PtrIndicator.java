package in.srain.cube.views.ptr.indicator;

import android.graphics.PointF;

public class PtrIndicator {
    public static final int POS_START = 0;
    private int mCurrentPos = 0;
    private int mHeaderHeight;
    private boolean mIsUnderTouch = false;
    private int mLastPos = 0;
    private int mOffsetToKeepHeaderWhileLoading = -1;
    protected int mOffsetToRefresh = 0;
    private float mOffsetX;
    private float mOffsetY;
    private int mPressedPos = 0;
    private PointF mPtLastMove = new PointF();
    private float mRatioOfHeaderHeightToRefresh = 1.2f;
    private int mRefreshCompleteY = 0;
    private float mResistance = 1.7f;

    public boolean isUnderTouch() {
        return this.mIsUnderTouch;
    }

    public float getResistance() {
        return this.mResistance;
    }

    public void setResistance(float resistance) {
        this.mResistance = resistance;
    }

    public void onRelease() {
        this.mIsUnderTouch = false;
    }

    public void onUIRefreshComplete() {
        this.mRefreshCompleteY = this.mCurrentPos;
    }

    public boolean goDownCrossFinishPosition() {
        return this.mCurrentPos >= this.mRefreshCompleteY;
    }

    /* access modifiers changed from: protected */
    public void processOnMove(float currentX, float currentY, float offsetX, float offsetY) {
        setOffset(offsetX, offsetY / this.mResistance);
    }

    public void setRatioOfHeaderHeightToRefresh(float ratio) {
        this.mRatioOfHeaderHeightToRefresh = ratio;
        this.mOffsetToRefresh = (int) (((float) this.mHeaderHeight) * ratio);
    }

    public float getRatioOfHeaderToHeightRefresh() {
        return this.mRatioOfHeaderHeightToRefresh;
    }

    public int getOffsetToRefresh() {
        return this.mOffsetToRefresh;
    }

    public void setOffsetToRefresh(int offset) {
        this.mRatioOfHeaderHeightToRefresh = (((float) this.mHeaderHeight) * 1.0f) / ((float) offset);
        this.mOffsetToRefresh = offset;
    }

    public void onPressDown(float x, float y) {
        this.mIsUnderTouch = true;
        this.mPressedPos = this.mCurrentPos;
        this.mPtLastMove.set(x, y);
    }

    public final void onMove(float x, float y) {
        processOnMove(x, y, x - this.mPtLastMove.x, y - this.mPtLastMove.y);
        this.mPtLastMove.set(x, y);
    }

    /* access modifiers changed from: protected */
    public void setOffset(float x, float y) {
        this.mOffsetX = x;
        this.mOffsetY = y;
    }

    public float getOffsetX() {
        return this.mOffsetX;
    }

    public float getOffsetY() {
        return this.mOffsetY;
    }

    public int getLastPosY() {
        return this.mLastPos;
    }

    public int getCurrentPosY() {
        return this.mCurrentPos;
    }

    public final void setCurrentPos(int current) {
        this.mLastPos = this.mCurrentPos;
        this.mCurrentPos = current;
        onUpdatePos(current, this.mLastPos);
    }

    /* access modifiers changed from: protected */
    public void onUpdatePos(int current, int last) {
    }

    public int getHeaderHeight() {
        return this.mHeaderHeight;
    }

    public void setHeaderHeight(int height) {
        this.mHeaderHeight = height;
        updateHeight();
    }

    /* access modifiers changed from: protected */
    public void updateHeight() {
        this.mOffsetToRefresh = (int) (this.mRatioOfHeaderHeightToRefresh * ((float) this.mHeaderHeight));
    }

    public void convertFrom(PtrIndicator ptrSlider) {
        this.mCurrentPos = ptrSlider.mCurrentPos;
        this.mLastPos = ptrSlider.mLastPos;
        this.mHeaderHeight = ptrSlider.mHeaderHeight;
    }

    public boolean hasLeftStartPosition() {
        return this.mCurrentPos > 0;
    }

    public boolean hasJustLeftStartPosition() {
        return this.mLastPos == 0 && hasLeftStartPosition();
    }

    public boolean hasJustBackToStartPosition() {
        return this.mLastPos != 0 && isInStartPosition();
    }

    public boolean isOverOffsetToRefresh() {
        return this.mCurrentPos >= getOffsetToRefresh();
    }

    public boolean hasMovedAfterPressedDown() {
        return this.mCurrentPos != this.mPressedPos;
    }

    public boolean isInStartPosition() {
        return this.mCurrentPos == 0;
    }

    public boolean crossRefreshLineFromTopToBottom() {
        return this.mLastPos < getOffsetToRefresh() && this.mCurrentPos >= getOffsetToRefresh();
    }

    public boolean hasJustReachedHeaderHeightFromTopToBottom() {
        return this.mLastPos < this.mHeaderHeight && this.mCurrentPos >= this.mHeaderHeight;
    }

    public boolean isOverOffsetToKeepHeaderWhileLoading() {
        return this.mCurrentPos > getOffsetToKeepHeaderWhileLoading();
    }

    public void setOffsetToKeepHeaderWhileLoading(int offset) {
        this.mOffsetToKeepHeaderWhileLoading = offset;
    }

    public int getOffsetToKeepHeaderWhileLoading() {
        return this.mOffsetToKeepHeaderWhileLoading >= 0 ? this.mOffsetToKeepHeaderWhileLoading : this.mHeaderHeight;
    }

    public boolean isAlreadyHere(int to) {
        return this.mCurrentPos == to;
    }

    public float getLastPercent() {
        if (this.mHeaderHeight == 0) {
            return 0.0f;
        }
        return (((float) this.mLastPos) * 1.0f) / ((float) this.mHeaderHeight);
    }

    public float getCurrentPercent() {
        if (this.mHeaderHeight == 0) {
            return 0.0f;
        }
        return (((float) this.mCurrentPos) * 1.0f) / ((float) this.mHeaderHeight);
    }

    public boolean willOverTop(int to) {
        return to < 0;
    }

    public String toString() {
        return "PtrIndicator{mOffsetToRefresh=" + this.mOffsetToRefresh + ", mPtLastMove=" + this.mPtLastMove + ", mOffsetX=" + this.mOffsetX + ", mOffsetY=" + this.mOffsetY + ", mCurrentPos=" + this.mCurrentPos + ", mLastPos=" + this.mLastPos + ", mHeaderHeight=" + this.mHeaderHeight + ", mPressedPos=" + this.mPressedPos + ", mRatioOfHeaderHeightToRefresh=" + this.mRatioOfHeaderHeightToRefresh + ", mResistance=" + this.mResistance + ", mIsUnderTouch=" + this.mIsUnderTouch + ", mOffsetToKeepHeaderWhileLoading=" + this.mOffsetToKeepHeaderWhileLoading + ", mRefreshCompleteY=" + this.mRefreshCompleteY + '}';
    }
}
