package dji.midware.upgradeComponent;

import android.support.annotation.NonNull;
import java.util.Objects;

public class DJIUpgradeComponent {
    private final DJIUpgradeModelType fcType;
    private final DJIUpgradeModelType rcType;

    public DJIUpgradeComponent(@NonNull DJIUpgradeModelType fcType2, @NonNull DJIUpgradeModelType rcType2) {
        this.fcType = fcType2;
        this.rcType = rcType2;
    }

    @NonNull
    public DJIUpgradeModelType getFcType() {
        return this.fcType;
    }

    @NonNull
    public DJIUpgradeModelType getRcType() {
        return this.rcType;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DJIUpgradeComponent that = (DJIUpgradeComponent) o;
        if (getFcType() == that.getFcType() && getRcType() == that.getRcType()) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(getFcType(), getRcType());
    }
}
