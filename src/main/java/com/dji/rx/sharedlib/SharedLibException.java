package com.dji.rx.sharedlib;

import dji.common.error.DJIError;
import java.io.IOException;

public class SharedLibException extends IOException {
    private DJIError mError;

    public SharedLibException(DJIError error) {
        this.mError = error;
    }

    public DJIError getError() {
        return this.mError;
    }

    public String toString() {
        return "SharedLibException{mError=" + this.mError + '}';
    }
}
