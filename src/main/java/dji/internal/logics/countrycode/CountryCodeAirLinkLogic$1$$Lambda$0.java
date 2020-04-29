package dji.internal.logics.countrycode;

import dji.internal.logics.countrycode.CountryCodeAirLinkLogic;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;

final /* synthetic */ class CountryCodeAirLinkLogic$1$$Lambda$0 implements Runnable {
    private final CountryCodeAirLinkLogic.AnonymousClass1 arg$1;
    private final DJISDKCacheHWAbstraction.InnerCallback arg$2;

    CountryCodeAirLinkLogic$1$$Lambda$0(CountryCodeAirLinkLogic.AnonymousClass1 r1, DJISDKCacheHWAbstraction.InnerCallback innerCallback) {
        this.arg$1 = r1;
        this.arg$2 = innerCallback;
    }

    public void run() {
        this.arg$1.lambda$onFails$0$CountryCodeAirLinkLogic$1(this.arg$2);
    }
}
