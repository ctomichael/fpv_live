package dji.common.mission.activetrack;

import android.graphics.RectF;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class ActiveTrackMission {
    private static int s_index;
    private ActiveTrackMode mode;
    private QuickShotMode quickShotMode;
    private int targetIndex;
    private RectF targetRect;

    public int hashCode() {
        int result;
        int i;
        int i2 = 0;
        if (this.targetRect != null) {
            result = this.targetRect.hashCode();
        } else {
            result = 0;
        }
        int i3 = ((result * 31) + this.targetIndex) * 31;
        if (this.mode != null) {
            i = this.mode.hashCode();
        } else {
            i = 0;
        }
        int i4 = (i3 + i) * 31;
        if (this.quickShotMode != null) {
            i2 = this.quickShotMode.hashCode();
        }
        return i4 + i2;
    }

    public boolean equals(Object o) {
        boolean z;
        if (o == null || !(o instanceof ActiveTrackMission)) {
            return false;
        }
        if (this == o) {
            return true;
        }
        if (this.targetRect == null || ((ActiveTrackMission) o).targetRect == null) {
            if (this.targetRect == null || ((ActiveTrackMission) o).targetRect == null) {
                return false;
            }
        } else if (!this.targetRect.equals(((ActiveTrackMission) o).targetRect)) {
            return false;
        }
        if (this.targetIndex == ((ActiveTrackMission) o).targetIndex && this.mode == ((ActiveTrackMission) o).mode && this.quickShotMode == ((ActiveTrackMission) o).quickShotMode) {
            z = true;
        } else {
            z = false;
        }
        return z;
    }

    public RectF getTargetRect() {
        return this.targetRect;
    }

    public void setTargetRect(RectF targetRect2) {
        this.targetRect = targetRect2;
    }

    public void setTargetIndex(int targetIndex2) {
        this.targetIndex = targetIndex2;
    }

    public int getTargetIndex() {
        return this.targetIndex;
    }

    public ActiveTrackMode getMode() {
        return this.mode;
    }

    public void setMode(ActiveTrackMode mode2) {
        this.mode = mode2;
    }

    public QuickShotMode getQuickShotMode() {
        return this.quickShotMode;
    }

    public void setQuickShotMode(QuickShotMode quickShotMode2) {
        this.quickShotMode = quickShotMode2;
    }

    public ActiveTrackMission() {
    }

    public ActiveTrackMission(RectF targetRect2, ActiveTrackMode mode2) {
        this.targetRect = targetRect2;
        this.mode = mode2;
        int i = s_index;
        s_index = i + 1;
        this.targetIndex = i;
        if (s_index > 1000) {
            s_index = 0;
        }
    }

    public boolean isValid() {
        if (!isNormalized((double) this.targetRect.centerX()) || !isNormalized((double) this.targetRect.centerY()) || this.targetIndex < 0 || this.targetIndex > 1000 || this.mode == ActiveTrackMode.UNKNOWN) {
            return false;
        }
        return true;
    }

    private boolean isNormalized(double x) {
        return 0.0d <= x && x <= 1.0d;
    }
}
