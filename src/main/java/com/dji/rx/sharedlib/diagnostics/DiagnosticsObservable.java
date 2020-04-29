package com.dji.rx.sharedlib.diagnostics;

import android.support.annotation.NonNull;
import io.reactivex.Observable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class DiagnosticsObservable extends Observable<DiagnosticsEvent> {
    public static DiagnosticsObservable singleCode(int code) {
        return new SingleCodeObservable(code);
    }

    public static DiagnosticsObservable multiCode(@NonNull int... codes) {
        if (codes == null) {
            throw new IllegalArgumentException("DiagnosticsObservable#multiCode方法不能传null参数");
        } else if (codes.length == 0) {
            return EmptyCodeObservable.INSTANCE;
        } else {
            if (codes.length == 1) {
                return singleCode(codes[0]);
            }
            Integer[] integers = new Integer[codes.length];
            for (int i = 0; i < codes.length; i++) {
                integers[i] = Integer.valueOf(codes[i]);
            }
            return new MultiCodeObservable(Arrays.asList(integers));
        }
    }

    public static DiagnosticsObservable multiCode(@NonNull List<Integer> codes) {
        if (codes == null) {
            throw new IllegalArgumentException("DiagnosticsObservable#multiCode方法不能传null参数");
        } else if (codes.size() == 0) {
            return EmptyCodeObservable.INSTANCE;
        } else {
            if (codes.size() == 1) {
                return singleCode(codes.get(0).intValue());
            }
            return new MultiCodeObservable(new ArrayList(codes));
        }
    }
}
