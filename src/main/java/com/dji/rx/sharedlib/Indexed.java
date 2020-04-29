package com.dji.rx.sharedlib;

import dji.component.accountcenter.IMemberProtocol;
import io.reactivex.annotations.NonNull;
import io.reactivex.internal.functions.ObjectHelper;

public final class Indexed<T> {
    final long index;
    final T value;

    public Indexed(@NonNull T value2, long index2) {
        this.value = value2;
        this.index = index2;
    }

    @NonNull
    public T value() {
        return this.value;
    }

    public long index() {
        return this.index;
    }

    public boolean isFirst() {
        return this.index == 0;
    }

    public boolean equals(Object other) {
        if (!(other instanceof Indexed)) {
            return false;
        }
        Indexed o = (Indexed) other;
        if (!ObjectHelper.equals(this.value, o.value) || this.index != o.index) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return ((this.value != null ? this.value.hashCode() : 0) * 31) + ((int) ((this.index >>> 31) ^ this.index));
    }

    public String toString() {
        return "Indexed[index=" + this.index + ", value=" + ((Object) this.value) + IMemberProtocol.STRING_SEPERATOR_RIGHT;
    }
}
