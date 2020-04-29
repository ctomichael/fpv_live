package dji.midware.data.forbid;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;

@Keep
@EXClassNullAway
public class NewNfzDesc {
    public boolean isAuthZone;
    public boolean isDrone;
    public boolean isInAvailableLicense;
    public int mAreaId;
    public float mDistance;
    public boolean mHasElevation;
    public int mHeight;
    public NfzPositionState mPosState = NfzPositionState.OTHER;
    public int mSubId;

    public boolean isInvalid() {
        return this.mAreaId == 0 && this.mSubId == 0 && this.mDistance == 0.0f;
    }

    @Keep
    public enum NfzPositionState {
        OUTSIDE_CAN_ENTER(0),
        OUTSIDE_CAN_NOT_ENTER(1),
        UNDER_LIMIT(2),
        INSIDE_LIMIT(3),
        OTHER(100);
        
        private int data;

        private NfzPositionState(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static NfzPositionState find(int b) {
            NfzPositionState result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
