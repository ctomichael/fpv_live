package dji.component.motorlock;

import dji.component.motorlock.model.LockKey;

public interface LockChangeListener {
    void onLockKeyChange(LockKey lockKey, boolean z);
}
