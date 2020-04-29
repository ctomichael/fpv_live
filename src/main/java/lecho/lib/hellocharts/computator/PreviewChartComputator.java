package lecho.lib.hellocharts.computator;

import lecho.lib.hellocharts.model.Viewport;

public class PreviewChartComputator extends ChartComputator {
    public float computeRawX(float valueX) {
        return ((float) this.contentRectMinusAllMargins.left) + ((valueX - this.maxViewport.left) * (((float) this.contentRectMinusAllMargins.width()) / this.maxViewport.width()));
    }

    public float computeRawY(float valueY) {
        return ((float) this.contentRectMinusAllMargins.bottom) - ((valueY - this.maxViewport.bottom) * (((float) this.contentRectMinusAllMargins.height()) / this.maxViewport.height()));
    }

    public Viewport getVisibleViewport() {
        return this.maxViewport;
    }

    public void setVisibleViewport(Viewport visibleViewport) {
        setMaxViewport(visibleViewport);
    }

    public void constrainViewport(float left, float top, float right, float bottom) {
        super.constrainViewport(left, top, right, bottom);
        this.viewportChangeListener.onViewportChanged(this.currentViewport);
    }
}
