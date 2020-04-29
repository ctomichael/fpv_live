package dji.internal.flyzone;

import android.support.annotation.NonNull;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum FlyZoneCollisionState {
    CLEAR(0),
    COLLISION_WITH_CUSTOM_UNLOCK_ZONE(1),
    COLLISION_WITH_RESTRICTED_ZONE(2),
    COLLISION_WITH_RESTRICTED_ZONE_WHEN_GO_HOME(3),
    COLLISION_WITH_AUTHORIZED_ZONE_HAS_LICENSE(4),
    COLLISION_WITH_AUTHORIZED_ZONE(5),
    COLLISION_WITH_HEIGHT_LIMIT_ZONE(6),
    UNKNOWN(255);
    
    private int data;

    public interface Callback {
        void onUpdate(@NonNull FlyZoneCollisionState flyZoneCollisionState);
    }

    private FlyZoneCollisionState(int _data) {
        this.data = _data;
    }

    public int value() {
        return this.data;
    }

    public boolean _equals(int b) {
        return this.data == b;
    }

    public static FlyZoneCollisionState find(int b) {
        FlyZoneCollisionState result = UNKNOWN;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(b)) {
                return values()[i];
            }
        }
        return result;
    }
}
