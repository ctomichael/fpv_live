package dji.midware.media.newframing;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

final /* synthetic */ class DJIVideoHevcControl$$Lambda$2 implements ObservableOnSubscribe {
    private final DJIVideoHevcControl arg$1;

    DJIVideoHevcControl$$Lambda$2(DJIVideoHevcControl dJIVideoHevcControl) {
        this.arg$1 = dJIVideoHevcControl;
    }

    public void subscribe(ObservableEmitter observableEmitter) {
        this.arg$1.lambda$getSendChangeVideoTransferFormatToAirSingleObservable$1$DJIVideoHevcControl(observableEmitter);
    }
}
