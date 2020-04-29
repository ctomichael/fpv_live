package dji.internal.diagnostics.handler;

import dji.internal.diagnostics.DJIDiagnosticsImpl;
import dji.utils.function.Consumer;
import dji.utils.function.Consumer$$CC;
import java.util.Set;

final /* synthetic */ class CameraDiagnosticsHandler$$Lambda$3 implements Consumer {
    private final Set arg$1;

    private CameraDiagnosticsHandler$$Lambda$3(Set set) {
        this.arg$1 = set;
    }

    static Consumer get$Lambda(Set set) {
        return new CameraDiagnosticsHandler$$Lambda$3(set);
    }

    public void accept(Object obj) {
        this.arg$1.add((DJIDiagnosticsImpl) obj);
    }

    public Consumer andThen(Consumer consumer) {
        return Consumer$$CC.andThen(this, consumer);
    }
}
