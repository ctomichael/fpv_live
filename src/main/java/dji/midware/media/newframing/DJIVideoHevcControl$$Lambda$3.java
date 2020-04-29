package dji.midware.media.newframing;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

final /* synthetic */ class DJIVideoHevcControl$$Lambda$3 implements Function {
    private final Observable arg$1;

    DJIVideoHevcControl$$Lambda$3(Observable observable) {
        this.arg$1 = observable;
    }

    public Object apply(Object obj) {
        return this.arg$1.retry(3).onErrorResumeNext(Observable.empty());
    }
}
