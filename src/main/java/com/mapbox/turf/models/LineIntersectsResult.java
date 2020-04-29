package com.mapbox.turf.models;

import android.support.annotation.Nullable;

public class LineIntersectsResult {
    private final Double horizontalIntersection;
    private final boolean onLine1;
    private final boolean onLine2;
    private final Double verticalIntersection;

    private LineIntersectsResult(@Nullable Double horizontalIntersection2, @Nullable Double verticalIntersection2, boolean onLine12, boolean onLine22) {
        this.horizontalIntersection = horizontalIntersection2;
        this.verticalIntersection = verticalIntersection2;
        this.onLine1 = onLine12;
        this.onLine2 = onLine22;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Nullable
    public Double horizontalIntersection() {
        return this.horizontalIntersection;
    }

    @Nullable
    public Double verticalIntersection() {
        return this.verticalIntersection;
    }

    public boolean onLine1() {
        return this.onLine1;
    }

    public boolean onLine2() {
        return this.onLine2;
    }

    public String toString() {
        return "LineIntersectsResult{horizontalIntersection=" + this.horizontalIntersection + ", verticalIntersection=" + this.verticalIntersection + ", onLine1=" + this.onLine1 + ", onLine2=" + this.onLine2 + "}";
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof LineIntersectsResult)) {
            return false;
        }
        LineIntersectsResult that = (LineIntersectsResult) obj;
        if (this.horizontalIntersection != null ? this.horizontalIntersection.equals(that.horizontalIntersection()) : that.horizontalIntersection() == null) {
            if (this.verticalIntersection != null ? this.verticalIntersection.equals(that.verticalIntersection()) : that.verticalIntersection() == null) {
                if (this.onLine1 == that.onLine1() && this.onLine2 == that.onLine2()) {
                    return true;
                }
            }
        }
        return false;
    }

    public int hashCode() {
        int i;
        int i2 = 1231;
        int i3 = 0;
        int hashCode = ((1 * 1000003) ^ (this.horizontalIntersection == null ? 0 : this.horizontalIntersection.hashCode())) * 1000003;
        if (this.verticalIntersection != null) {
            i3 = this.verticalIntersection.hashCode();
        }
        int hashCode2 = (hashCode ^ i3) * 1000003;
        if (this.onLine1) {
            i = 1231;
        } else {
            i = 1237;
        }
        int hashCode3 = (hashCode2 ^ i) * 1000003;
        if (!this.onLine2) {
            i2 = 1237;
        }
        return hashCode3 ^ i2;
    }

    public Builder toBuilder() {
        return new Builder();
    }

    public static class Builder {
        private Double horizontalIntersection;
        private Boolean onLine1;
        private Boolean onLine2;
        private Double verticalIntersection;

        Builder() {
            this.onLine1 = false;
            this.onLine2 = false;
        }

        private Builder(LineIntersectsResult source) {
            this.onLine1 = false;
            this.onLine2 = false;
            this.horizontalIntersection = source.horizontalIntersection();
            this.verticalIntersection = source.verticalIntersection();
            this.onLine1 = Boolean.valueOf(source.onLine1());
            this.onLine2 = Boolean.valueOf(source.onLine2());
        }

        public Builder horizontalIntersection(@Nullable Double horizontalIntersection2) {
            this.horizontalIntersection = horizontalIntersection2;
            return this;
        }

        public Builder verticalIntersection(@Nullable Double verticalIntersection2) {
            this.verticalIntersection = verticalIntersection2;
            return this;
        }

        public Builder onLine1(boolean onLine12) {
            this.onLine1 = Boolean.valueOf(onLine12);
            return this;
        }

        public Builder onLine2(boolean onLine22) {
            this.onLine2 = Boolean.valueOf(onLine22);
            return this;
        }

        public LineIntersectsResult build() {
            String missing = "";
            if (this.onLine1 == null) {
                missing = missing + " onLine1";
            }
            if (this.onLine2 == null) {
                missing = missing + " onLine2";
            }
            if (missing.isEmpty()) {
                return new LineIntersectsResult(this.horizontalIntersection, this.verticalIntersection, this.onLine1.booleanValue(), this.onLine2.booleanValue());
            }
            throw new IllegalStateException("Missing required properties:" + missing);
        }
    }
}
