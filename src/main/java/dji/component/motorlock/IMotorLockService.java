package dji.component.motorlock;

import android.support.annotation.Nullable;
import dji.component.motorlock.model.LockKey;
import java.util.Set;

public interface IMotorLockService {
    public static final String COMPONENT_NAME = "MotorLockService";

    boolean isMotorLocked();

    Set<LockKey> queryAllLock();

    LockKey requestLockMotor(String str);

    void requestUnlockMotor(LockKey lockKey);

    void setLockChangeListener(@Nullable LockChangeListener lockChangeListener);
}
