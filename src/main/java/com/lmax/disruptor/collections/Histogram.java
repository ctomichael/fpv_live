package com.lmax.disruptor.collections;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import kotlin.jvm.internal.LongCompanionObject;

@Deprecated
public final class Histogram {
    private final long[] counts;
    private long maxValue = 0;
    private long minValue = LongCompanionObject.MAX_VALUE;
    private final long[] upperBounds;

    public Histogram(long[] upperBounds2) {
        validateBounds(upperBounds2);
        this.upperBounds = Arrays.copyOf(upperBounds2, upperBounds2.length);
        this.counts = new long[upperBounds2.length];
    }

    private void validateBounds(long[] upperBounds2) {
        long lastBound = -1;
        if (upperBounds2.length <= 0) {
            throw new IllegalArgumentException("Must provide at least one interval");
        }
        int length = upperBounds2.length;
        int i = 0;
        while (i < length) {
            long bound = upperBounds2[i];
            if (bound <= 0) {
                throw new IllegalArgumentException("Bounds must be positive values");
            } else if (bound <= lastBound) {
                throw new IllegalArgumentException("bound " + bound + " is not greater than " + lastBound);
            } else {
                lastBound = bound;
                i++;
            }
        }
    }

    public int getSize() {
        return this.upperBounds.length;
    }

    public long getUpperBoundAt(int index) {
        return this.upperBounds[index];
    }

    public long getCountAt(int index) {
        return this.counts[index];
    }

    public boolean addObservation(long value) {
        int low = 0;
        int high = this.upperBounds.length - 1;
        while (low < high) {
            int mid = low + ((high - low) >> 1);
            if (this.upperBounds[mid] < value) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        if (value > this.upperBounds[high]) {
            return false;
        }
        long[] jArr = this.counts;
        jArr[high] = jArr[high] + 1;
        trackRange(value);
        return true;
    }

    private void trackRange(long value) {
        if (value < this.minValue) {
            this.minValue = value;
        }
        if (value > this.maxValue) {
            this.maxValue = value;
        }
    }

    public void addObservations(Histogram histogram) {
        if (this.upperBounds.length != histogram.upperBounds.length) {
            throw new IllegalArgumentException("Histograms must have matching intervals");
        }
        int size = this.upperBounds.length;
        for (int i = 0; i < size; i++) {
            if (this.upperBounds[i] != histogram.upperBounds[i]) {
                throw new IllegalArgumentException("Histograms must have matching intervals");
            }
        }
        int size2 = this.counts.length;
        for (int i2 = 0; i2 < size2; i2++) {
            long[] jArr = this.counts;
            jArr[i2] = jArr[i2] + histogram.counts[i2];
        }
        trackRange(histogram.minValue);
        trackRange(histogram.maxValue);
    }

    public void clear() {
        this.maxValue = 0;
        this.minValue = LongCompanionObject.MAX_VALUE;
        int size = this.counts.length;
        for (int i = 0; i < size; i++) {
            this.counts[i] = 0;
        }
    }

    public long getCount() {
        long count = 0;
        int size = this.counts.length;
        for (int i = 0; i < size; i++) {
            count += this.counts[i];
        }
        return count;
    }

    public long getMin() {
        return this.minValue;
    }

    public long getMax() {
        return this.maxValue;
    }

    public BigDecimal getMean() {
        if (0 == getCount()) {
            return BigDecimal.ZERO;
        }
        long lowerBound = this.counts[0] > 0 ? this.minValue : 0;
        BigDecimal total = BigDecimal.ZERO;
        int size = this.upperBounds.length;
        for (int i = 0; i < size; i++) {
            if (0 != this.counts[i]) {
                total = total.add(new BigDecimal(lowerBound + ((Math.min(this.upperBounds[i], this.maxValue) - lowerBound) / 2)).multiply(new BigDecimal(this.counts[i])));
            }
            lowerBound = Math.max(this.upperBounds[i] + 1, this.minValue);
        }
        return total.divide(new BigDecimal(getCount()), 2, RoundingMode.HALF_UP);
    }

    public long getTwoNinesUpperBound() {
        return getUpperBoundForFactor(0.99d);
    }

    public long getFourNinesUpperBound() {
        return getUpperBoundForFactor(0.9999d);
    }

    public long getUpperBoundForFactor(double factor) {
        if (0.0d >= factor || factor >= 1.0d) {
            throw new IllegalArgumentException("factor must be >= 0.0 and <= 1.0");
        }
        long totalCount = getCount();
        long tailTotal = totalCount - Math.round(((double) totalCount) * factor);
        long tailCount = 0;
        for (int i = this.counts.length - 1; i >= 0; i--) {
            if (0 != this.counts[i]) {
                tailCount += this.counts[i];
                if (tailCount >= tailTotal) {
                    return this.upperBounds[i];
                }
            }
        }
        return 0;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Histogram{");
        sb.append("min=").append(getMin()).append(", ");
        sb.append("max=").append(getMax()).append(", ");
        sb.append("mean=").append(getMean()).append(", ");
        sb.append("99%=").append(getTwoNinesUpperBound()).append(", ");
        sb.append("99.99%=").append(getFourNinesUpperBound()).append(", ");
        sb.append('[');
        int size = this.counts.length;
        for (int i = 0; i < size; i++) {
            sb.append(this.upperBounds[i]).append('=').append(this.counts[i]).append(", ");
        }
        if (this.counts.length > 0) {
            sb.setLength(sb.length() - 2);
        }
        sb.append(']');
        sb.append('}');
        return sb.toString();
    }
}
