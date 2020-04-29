package dji.sdksharedlib.hardware.abstractions.airlink.wifi;

import dji.common.airlink.WiFiFrequencyBand;
import dji.common.error.DJIAirLinkError;
import dji.common.error.DJIError;
import dji.common.util.AirLinkUtils;
import dji.common.util.CallbackUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataWifiGetPassword;
import dji.midware.data.model.P3.DataWifiGetSSID;
import dji.midware.data.model.P3.DataWifiRestart;
import dji.midware.data.model.P3.DataWifiSetPassword;
import dji.midware.data.model.P3.DataWifiSetSSID;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;

@EXClassNullAway
public class DJIWifiLinkSkyAbstraction extends DJIWifiLinkAbstraction {
    public void setFrequencyBand(WiFiFrequencyBand frequency, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            callback.onFails(DJIError.COMMON_UNSUPPORTED);
        }
    }

    public void getFrequencyBand(DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            callback.onFails(DJIError.COMMON_UNSUPPORTED);
        }
    }

    public void setSSID(String ssid, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (AirLinkUtils.verifySSID(ssid)) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
        } else {
            DataWifiSetSSID.getInstance().setSSID(ssid.getBytes()).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.airlink.wifi.DJIWifiLinkSkyAbstraction.AnonymousClass1 */

                public void onSuccess(Object model) {
                    CallbackUtils.onSuccess(callback, (Object) null);
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
                }
            });
        }
    }

    public void getSSID(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            DataWifiGetSSID.getInstance().start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.airlink.wifi.DJIWifiLinkSkyAbstraction.AnonymousClass2 */

                public void onSuccess(Object model) {
                    CallbackUtils.onSuccess(callback, DataWifiGetSSID.getInstance().getSSID());
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
                }
            });
        }
    }

    public void setPassword(String pwd, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (pwd != null && pwd.length() >= 8) {
            DataWifiSetPassword.getInstance().setPassword(pwd.getBytes()).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.airlink.wifi.DJIWifiLinkSkyAbstraction.AnonymousClass3 */

                public void onSuccess(Object model) {
                    if (callback != null) {
                        callback.onSuccess(null);
                    }
                }

                public void onFailure(Ccode ccode) {
                    if (callback != null) {
                        callback.onFails(DJIError.getDJIError(ccode));
                    }
                }
            });
        } else if (callback != null) {
            CallbackUtils.onFailure(callback, DJIAirLinkError.COMMON_PARAM_ILLEGAL);
        }
    }

    public void getPassword(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            DataWifiGetPassword.getInstance().start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.airlink.wifi.DJIWifiLinkSkyAbstraction.AnonymousClass4 */

                public void onSuccess(Object model) {
                    callback.onSuccess(DataWifiGetPassword.getInstance().getPassword());
                }

                public void onFailure(Ccode ccode) {
                    callback.onFails(DJIError.getDJIError(ccode));
                }
            });
        }
    }

    public void reboot(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataWifiRestart.getInstance().start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.airlink.wifi.DJIWifiLinkSkyAbstraction.AnonymousClass5 */

            public void onSuccess(Object model) {
                if (callback != null) {
                    callback.onSuccess(null);
                }
            }

            public void onFailure(Ccode ccode) {
                if (callback != null) {
                    callback.onFails(DJIError.getDJIError(ccode));
                }
            }
        });
    }
}
