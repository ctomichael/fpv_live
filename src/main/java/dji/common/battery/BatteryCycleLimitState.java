package dji.common.battery;

import java.util.Objects;

public class BatteryCycleLimitState {
    private final int mCycleLimit;
    private final BatteryCycleLimitLevel mLimitLevel;

    public BatteryCycleLimitState(BatteryCycleLimitLevel limitLevel, int cycleLimit) {
        this.mLimitLevel = limitLevel;
        this.mCycleLimit = cycleLimit;
    }

    public BatteryCycleLimitLevel getLimitLevel() {
        return this.mLimitLevel;
    }

    public int getCycleLimit() {
        return this.mCycleLimit;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BatteryCycleLimitState)) {
            return false;
        }
        BatteryCycleLimitState that = (BatteryCycleLimitState) o;
        if (this.mCycleLimit == that.mCycleLimit && this.mLimitLevel == that.mLimitLevel) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(this.mLimitLevel, Integer.valueOf(this.mCycleLimit));
    }
}
