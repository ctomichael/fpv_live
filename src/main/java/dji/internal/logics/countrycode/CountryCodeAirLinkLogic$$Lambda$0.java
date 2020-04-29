package dji.internal.logics.countrycode;

import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.listener.DJIParamAccessListener;
import dji.sdksharedlib.store.DJISDKCacheParamValue;

final /* synthetic */ class CountryCodeAirLinkLogic$$Lambda$0 implements DJIParamAccessListener {
    private final CountryCodeAirLinkLogic arg$1;

    CountryCodeAirLinkLogic$$Lambda$0(CountryCodeAirLinkLogic countryCodeAirLinkLogic) {
        this.arg$1 = countryCodeAirLinkLogic;
    }

    public void onValueChange(DJISDKCacheKey dJISDKCacheKey, DJISDKCacheParamValue dJISDKCacheParamValue, DJISDKCacheParamValue dJISDKCacheParamValue2) {
        this.arg$1.lambda$new$0$CountryCodeAirLinkLogic(dJISDKCacheKey, dJISDKCacheParamValue, dJISDKCacheParamValue2);
    }
}
