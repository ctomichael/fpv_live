package dji.common.remotecontroller;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class GimbalStickMapping {
    private boolean isReversed;
    private GimbalStickMappingTarget mappingTarget;

    private GimbalStickMapping(Builder builder) {
        this.mappingTarget = builder.target;
        this.isReversed = builder.isReversed;
    }

    public GimbalStickMappingTarget getMappingTarget() {
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
        GimbalStickMapping that = (GimbalStickMapping) o;
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
        public GimbalStickMappingTarget target;

        public Builder mappingTarget(GimbalStickMappingTarget target2) {
            this.target = target2;
            return this;
        }

        public Builder isReversed(boolean isReversed2) {
            this.isReversed = isReversed2;
            return this;
        }

        public GimbalStickMapping build() {
            return new GimbalStickMapping(this);
        }
    }
}
