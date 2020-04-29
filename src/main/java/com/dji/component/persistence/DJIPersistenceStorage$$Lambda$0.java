package com.dji.component.persistence;

final /* synthetic */ class DJIPersistenceStorage$$Lambda$0 implements Runnable {
    private final DJIPersistenceDataListener arg$1;
    private final String arg$2;
    private final String arg$3;

    DJIPersistenceStorage$$Lambda$0(DJIPersistenceDataListener dJIPersistenceDataListener, String str, String str2) {
        this.arg$1 = dJIPersistenceDataListener;
        this.arg$2 = str;
        this.arg$3 = str2;
    }

    public void run() {
        this.arg$1.onValueUpdate(this.arg$2, this.arg$3);
    }
}
