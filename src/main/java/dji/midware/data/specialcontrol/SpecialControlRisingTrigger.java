package dji.midware.data.specialcontrol;

import io.reactivex.disposables.Disposable;

public interface SpecialControlRisingTrigger {
    Disposable sendHighLevel();

    Disposable sendLowLevel();
}
