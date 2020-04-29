package com.dji.component.fpv.base;

import java.util.concurrent.Callable;

final /* synthetic */ class BulletinBoard$$Lambda$0 implements Callable {
    private final BulletinBoard arg$1;
    private final String arg$2;
    private final Object arg$3;

    BulletinBoard$$Lambda$0(BulletinBoard bulletinBoard, String str, Object obj) {
        this.arg$1 = bulletinBoard;
        this.arg$2 = str;
        this.arg$3 = obj;
    }

    public Object call() {
        return this.arg$1.lambda$getObservable$0$BulletinBoard(this.arg$2, this.arg$3);
    }
}
