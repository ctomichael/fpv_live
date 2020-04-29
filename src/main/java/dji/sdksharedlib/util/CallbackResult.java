package dji.sdksharedlib.util;

import dji.common.error.DJIError;

public class CallbackResult<T> {
    private final DJIError error;
    private final T value;

    public CallbackResult(T value2, DJIError error2) {
        this.value = value2;
        this.error = error2;
    }

    public T getValue() {
        return this.value;
    }

    public DJIError getError() {
        return this.error;
    }
}
