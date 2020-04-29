package com.dji.component.fpv.widget.subline;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import com.dji.component.fpv.widget.preview.R;
import java.util.ArrayList;
import java.util.List;

public class SubLineMainView extends View {
    private boolean isPreAndroidP;
    private int mHeight;
    private Paint mPaint;
    private Paint mSecondPaint;
    private List<SubLineTypeInfo> mSubLineTypeInfoList;
    private int mWidth;

    public SubLineMainView(Context context) {
        this(context, null);
    }

    public SubLineMainView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SubLineMainView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SubLineMainView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mSubLineTypeInfoList = new ArrayList();
        this.mWidth = 0;
        this.mHeight = 0;
        this.isPreAndroidP = false;
        if (!isInEditMode()) {
            if (Build.VERSION.SDK_INT < 28) {
                this.isPreAndroidP = true;
            }
            if (this.isPreAndroidP) {
                initPreP();
            } else {
                init();
            }
        }
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{android.graphics.Paint.setShadowLayer(float, float, float, int):void}
     arg types: [int, int, int, int]
     candidates:
      ClspMth{android.graphics.Paint.setShadowLayer(float, float, float, long):void}
      ClspMth{android.graphics.Paint.setShadowLayer(float, float, float, int):void} */
    @TargetApi(28)
    private void init() {
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setColor(getContext().getResources().getColor(R.color.white_70));
        this.mPaint.setStrokeWidth(1.5f * getResources().getDisplayMetrics().density);
        this.mPaint.setShadowLayer(3.0f, 0.0f, 1.0f, ContextCompat.getColor(getContext(), R.color.black_50));
    }

    private void initPreP() {
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setColor(getContext().getResources().getColor(R.color.white_70));
        this.mPaint.setStrokeWidth(1.5f * getResources().getDisplayMetrics().density);
        this.mSecondPaint = new Paint();
        this.mSecondPaint.setAntiAlias(true);
        this.mSecondPaint.setStyle(Paint.Style.STROKE);
        this.mSecondPaint.setColor(ContextCompat.getColor(getContext(), R.color.black_50));
        this.mSecondPaint.setStrokeWidth(0.5f * getResources().getDisplayMetrics().density);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isInEditMode()) {
            int width = getWidth();
            int height = getHeight();
            if (!(this.mWidth == width && this.mHeight == height)) {
                this.mWidth = width;
                this.mHeight = height;
                for (int i = 0; i < this.mSubLineTypeInfoList.size(); i++) {
                    this.mSubLineTypeInfoList.get(i).refreshLinePoints(width, height);
                }
            }
            if (this.isPreAndroidP) {
                onDrawPreP(canvas);
            } else {
                onDrawCurrent(canvas);
            }
        }
    }

    private void onDrawPreP(Canvas canvas) {
        for (SubLineTypeInfo lineTypeInfo : this.mSubLineTypeInfoList) {
            if (lineTypeInfo.isCanDraw()) {
                canvas.drawLines(lineTypeInfo.getLinePoints(), this.mPaint);
                canvas.drawLines(lineTypeInfo.getLinePoints(), this.mSecondPaint);
            }
        }
    }

    @TargetApi(28)
    private void onDrawCurrent(Canvas canvas) {
        for (SubLineTypeInfo lineTypeInfo : this.mSubLineTypeInfoList) {
            if (lineTypeInfo.isCanDraw()) {
                canvas.drawLines(lineTypeInfo.getLinePoints(), this.mPaint);
            }
        }
    }

    public void setSubLineDrawType(List<SubLineTypeInfo> lineLists) {
        this.mSubLineTypeInfoList.clear();
        this.mSubLineTypeInfoList.addAll(lineLists);
        updateContentDescription(lineLists);
        postInvalidate();
    }

    private void updateContentDescription(List<SubLineTypeInfo> lineLists) {
        StringBuilder sb = new StringBuilder();
        for (SubLineTypeInfo info : lineLists) {
            if (info.isCanDraw()) {
                sb.append(info.getKey());
                sb.append(";");
            }
        }
        setContentDescription(sb.toString());
    }
}
