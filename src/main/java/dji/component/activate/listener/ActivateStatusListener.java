package dji.component.activate.listener;

import dji.component.activate.model.ActivateStatusEvent;

public interface ActivateStatusListener {
    void onActivateStageChanged(boolean z);

    void onDeviceActivateStatusChanged(ActivateStatusEvent activateStatusEvent);

    void onRealNameStateChange(boolean z);
}
