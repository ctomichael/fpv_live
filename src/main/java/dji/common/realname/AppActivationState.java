package dji.common.realname;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum AppActivationState {
    NOT_SUPPORTED,
    LOGIN_REQUIRED,
    ACTIVATED,
    UNKNOWN;

    public interface AppActivationStateListener {
        void onUpdate(AppActivationState appActivationState);
    }
}
