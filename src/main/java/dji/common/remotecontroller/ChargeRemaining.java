package dji.common.remotecontroller;

import android.support.annotation.NonNull;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class ChargeRemaining {
    private int remainingChargeInPercent;
    private int remainingChargeInmAh;

    public interface Callback {
        void onUpdate(@NonNull ChargeRemaining chargeRemaining);
    }

    public ChargeRemaining(int remainingChargeInMH, int remainingChargeInPercent2) {
        this.remainingChargeInPercent = remainingChargeInPercent2;
        this.remainingChargeInmAh = remainingChargeInMH;
    }

    public int getRemainingChargeInmAh() {
        return this.remainingChargeInmAh;
    }

    public int getRemainingChargeInPercent() {
        return this.remainingChargeInPercent;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChargeRemaining that = (ChargeRemaining) o;
        if (this.remainingChargeInmAh != that.remainingChargeInmAh) {
            return false;
        }
        if (this.remainingChargeInPercent != that.remainingChargeInPercent) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return (this.remainingChargeInmAh * 31) + this.remainingChargeInPercent;
    }
}
