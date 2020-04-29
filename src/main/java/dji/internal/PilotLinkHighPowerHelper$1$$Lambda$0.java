package dji.internal;

import dji.internal.PilotLinkHighPowerHelper;
import dji.midware.data.model.P3.DataOsdSetSdrAssitantRead;

final /* synthetic */ class PilotLinkHighPowerHelper$1$$Lambda$0 implements Runnable {
    private final PilotLinkHighPowerHelper.AnonymousClass1 arg$1;
    private final DataOsdSetSdrAssitantRead.SdrDeviceType arg$2;
    private final int arg$3;

    PilotLinkHighPowerHelper$1$$Lambda$0(PilotLinkHighPowerHelper.AnonymousClass1 r1, DataOsdSetSdrAssitantRead.SdrDeviceType sdrDeviceType, int i) {
        this.arg$1 = r1;
        this.arg$2 = sdrDeviceType;
        this.arg$3 = i;
    }

    public void run() {
        this.arg$1.lambda$onFailure$0$PilotLinkHighPowerHelper$1(this.arg$2, this.arg$3);
    }
}
