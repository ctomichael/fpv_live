package lecho.lib.hellocharts.renderer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.provider.LineChartDataProvider;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.Chart;

public class PreviewLineChartRenderer extends LineChartRenderer {
    private static final int DEFAULT_PREVIEW_STROKE_WIDTH_DP = 2;
    private static final int DEFAULT_PREVIEW_TRANSPARENCY = 64;
    private static final int FULL_ALPHA = 255;
    private Paint previewPaint = new Paint();

    public PreviewLineChartRenderer(Context context, Chart chart, LineChartDataProvider dataProvider) {
        super(context, chart, dataProvider);
        this.previewPaint.setAntiAlias(true);
        this.previewPaint.setColor(-3355444);
        this.previewPaint.setStrokeWidth((float) ChartUtils.dp2px(this.density, 2));
    }

    public void drawUnclipped(Canvas canvas) {
        super.drawUnclipped(canvas);
        Viewport currentViewport = this.computator.getCurrentViewport();
        float left = this.computator.computeRawX(currentViewport.left);
        float top = this.computator.computeRawY(currentViewport.top);
        float right = this.computator.computeRawX(currentViewport.right);
        float bottom = this.computator.computeRawY(currentViewport.bottom);
        this.previewPaint.setAlpha(64);
        this.previewPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(left, top, right, bottom, this.previewPaint);
        this.previewPaint.setStyle(Paint.Style.STROKE);
        this.previewPaint.setAlpha(255);
        canvas.drawRect(left, top, right, bottom, this.previewPaint);
    }

    public int getPreviewColor() {
        return this.previewPaint.getColor();
    }

    public void setPreviewColor(int color) {
        this.previewPaint.setColor(color);
    }
}
