package dji.common.remotecontroller;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class AircraftStickMapping {
    private boolean isReversed;
    private AircraftStickMappingTarget mappingTarget;

    private AircraftStickMapping(Builder builder) {
        this.mappingTarget = builder.target;
        this.isReversed = builder.isReversed;
    }

    public AircraftStickMappingTarget getMappingTarget() {
        return this.mappingTarget;
    }

    public boolean isReversed() {
        return this.isReversed;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AircraftStickMapping that = (AircraftStickMapping) o;
        if (this.isReversed != that.isReversed) {
            return false;
        }
        if (this.mappingTarget != that.mappingTarget) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result;
        int i = 0;
        if (this.mappingTarget != null) {
            result = this.mappingTarget.hashCode();
        } else {
            result = 0;
        }
        int i2 = result * 31;
        if (this.isReversed) {
            i = 1;
        }
        return i2 + i;
    }

    public static final class Builder {
        /* access modifiers changed from: private */
        public boolean isReversed;
        /* access modifiers changed from: private */
        public AircraftStickMappingTarget target;

        public Builder mappingTarget(AircraftStickMappingTarget target2) {
            this.target = target2;
            return this;
        }

        public Builder isReversed(boolean isReversed2) {
            this.isReversed = isReversed2;
            return this;
        }

        public AircraftStickMapping build() {
            return new AircraftStickMapping(this);
        }
    }
}
