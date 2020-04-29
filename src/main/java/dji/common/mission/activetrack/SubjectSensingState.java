package dji.common.mission.activetrack;

import android.graphics.RectF;

public class SubjectSensingState {
    private final int index;
    private final ActiveTrackTargetState state;
    private final RectF targetRect;
    private final ActiveTrackTargetType targetType;

    public SubjectSensingState(ActiveTrackTargetState state2, ActiveTrackTargetType targetType2, RectF targetRect2, int index2) {
        this.state = state2;
        this.targetType = targetType2;
        this.targetRect = targetRect2;
        this.index = index2;
    }

    public ActiveTrackTargetState getState() {
        return this.state;
    }

    public ActiveTrackTargetType getTargetType() {
        return this.targetType;
    }

    public RectF getTargetRect() {
        return this.targetRect;
    }

    public int getIndex() {
        return this.index;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubjectSensingState)) {
            return false;
        }
        SubjectSensingState that = (SubjectSensingState) o;
        if (getIndex() != that.getIndex() || getState() != that.getState() || getTargetType() != that.getTargetType()) {
            return false;
        }
        if (getTargetRect() != null) {
            z = getTargetRect().equals(that.getTargetRect());
        } else if (that.getTargetRect() != null) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        int result;
        int i;
        int i2 = 0;
        if (getState() != null) {
            result = getState().hashCode();
        } else {
            result = 0;
        }
        int i3 = result * 31;
        if (getTargetType() != null) {
            i = getTargetType().hashCode();
        } else {
            i = 0;
        }
        int i4 = (i3 + i) * 31;
        if (getTargetRect() != null) {
            i2 = getTargetRect().hashCode();
        }
        return ((i4 + i2) * 31) + getIndex();
    }
}
