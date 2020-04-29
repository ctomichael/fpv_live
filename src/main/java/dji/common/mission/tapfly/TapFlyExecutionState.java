package dji.common.mission.tapfly;

import android.graphics.PointF;
import dji.common.util.DirectionUtils;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class TapFlyExecutionState {
    private final BypassDirection bypassDirection;
    private final Vector direction;
    private final PointF imageLocation;
    private final float relativeHeading;
    private final float speed;

    private TapFlyExecutionState(Vector direction2, BypassDirection bypassingState, float relativeHeading2, float speed2) {
        this.direction = direction2;
        this.bypassDirection = bypassingState;
        this.relativeHeading = relativeHeading2;
        this.imageLocation = getTapFlyPointFromTapFlyDirection(direction2);
        this.speed = speed2;
    }

    public Vector getDirection() {
        return this.direction;
    }

    public BypassDirection getBypassDirection() {
        return this.bypassDirection;
    }

    public PointF getImageLocation() {
        return this.imageLocation;
    }

    public float getRelativeHeading() {
        return this.relativeHeading;
    }

    public float getSpeed() {
        return this.speed;
    }

    private PointF getTapFlyPointFromTapFlyDirection(Vector direction2) {
        float[] pointCoordinate = DirectionUtils.calculateCurrMovingDir(new float[]{direction2.getX(), direction2.getY(), direction2.getZ()});
        return new PointF(pointCoordinate[0], pointCoordinate[1]);
    }

    public static class Builder {
        private BypassDirection bypassDirection;
        private Vector direction;
        private PointF imageLocation;
        private float relativeHeading;
        private float speed;

        public Builder direction(Vector direction2) {
            this.direction = direction2;
            return this;
        }

        public Builder bypassDirection(BypassDirection bypassDirection2) {
            this.bypassDirection = bypassDirection2;
            return this;
        }

        public Builder relativeHeading(float relativeHeading2) {
            this.relativeHeading = relativeHeading2;
            return this;
        }

        public Builder speed(float speed2) {
            this.speed = speed2;
            return this;
        }

        public Builder imageLocation(PointF imageLocation2) {
            this.imageLocation = imageLocation2;
            return this;
        }

        public TapFlyExecutionState build() {
            if (this.bypassDirection == null) {
                this.bypassDirection = BypassDirection.NONE;
            }
            return new TapFlyExecutionState(this.direction, this.bypassDirection, this.relativeHeading, this.speed);
        }
    }

    public int hashCode() {
        int result;
        int i;
        int i2 = 0;
        if (this.direction != null) {
            result = this.direction.hashCode();
        } else {
            result = 0;
        }
        int i3 = result * 31;
        if (this.bypassDirection != null) {
            i = this.bypassDirection.hashCode();
        } else {
            i = 0;
        }
        int floatToIntBits = (((((i3 + i) * 31) + Float.floatToIntBits(this.relativeHeading)) * 31) + Float.floatToIntBits(this.speed)) * 31;
        if (this.imageLocation != null) {
            i2 = this.imageLocation.hashCode();
        }
        return floatToIntBits + i2;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        boolean isEqual = false;
        if (obj instanceof TapFlyExecutionState) {
            TapFlyExecutionState object = (TapFlyExecutionState) obj;
            if (object.direction != null ? object.direction.equals(this.direction) : object.direction == this.direction) {
                if (object.imageLocation != null ? object.imageLocation.equals(this.imageLocation) : object.imageLocation == this.imageLocation) {
                    if (object.bypassDirection == this.bypassDirection && object.relativeHeading == this.relativeHeading && object.speed == this.speed) {
                        isEqual = true;
                    }
                }
            }
            isEqual = false;
        }
        return isEqual;
    }
}
