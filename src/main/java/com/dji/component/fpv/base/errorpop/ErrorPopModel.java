package com.dji.component.fpv.base.errorpop;

import java.util.Arrays;
import java.util.Objects;

public class ErrorPopModel {
    public static final String ACTION_ADD = "add";
    public static final String ACTION_REMOVE = "remove";
    private final String mAction;
    private final int mErrorCode;
    private final Object[] mFormatArgs;

    public ErrorPopModel(int errorCode) {
        this.mErrorCode = errorCode;
        this.mAction = ACTION_ADD;
        this.mFormatArgs = null;
    }

    private ErrorPopModel(Builder builder) {
        this.mErrorCode = builder.mErrorCode;
        this.mAction = builder.mAction;
        this.mFormatArgs = builder.mFormatArgs;
    }

    public int getErrorCode() {
        return this.mErrorCode;
    }

    public String getAction() {
        return this.mAction;
    }

    public Object[] getFormatArgs() {
        return this.mFormatArgs;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ErrorPopModel that = (ErrorPopModel) o;
        if (this.mErrorCode != that.mErrorCode || !this.mAction.equals(that.mAction) || !Arrays.equals(this.mFormatArgs, that.mFormatArgs)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return (Objects.hash(Integer.valueOf(this.mErrorCode), this.mAction) * 31) + Arrays.hashCode(this.mFormatArgs);
    }

    public String toString() {
        return "ErrorPopModel{mErrorCode=" + this.mErrorCode + ", mAction='" + this.mAction + '\'' + ", mFormatArgs=" + Arrays.toString(this.mFormatArgs) + '}';
    }

    public static final class Builder {
        /* access modifiers changed from: private */
        public String mAction;
        /* access modifiers changed from: private */
        public final int mErrorCode;
        /* access modifiers changed from: private */
        public Object[] mFormatArgs = null;

        public Builder(int mErrorCode2) {
            this.mErrorCode = mErrorCode2;
        }

        public Builder setAction(String action) {
            this.mAction = action;
            return this;
        }

        public Builder setFormatArgs(Object... formatArgs) {
            this.mFormatArgs = formatArgs;
            return this;
        }

        public ErrorPopModel build() {
            if (this.mAction == null) {
                this.mAction = ErrorPopModel.ACTION_ADD;
            }
            return new ErrorPopModel(this);
        }
    }
}
