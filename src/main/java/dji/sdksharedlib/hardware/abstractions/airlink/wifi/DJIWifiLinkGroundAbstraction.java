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
import dji.midware.data.model.P3.DataWifiGetWifiFrequency;
import dji.midware.data.model.P3.DataWifiRestart;
import dji.midware.data.model.P3.DataWifiSetPassword;
import dji.midware.data.model.P3.DataWifiSetSSID;
import dji.midware.data.model.P3.DataWifiSetWifiFrequency;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;

@EXClassNullAway
public class DJIWifiLinkGroundAbstraction extends DJIWifiLinkAbstraction {
    public void setFrequencyBand(WiFiFrequencyBand frequency, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        int i = 0;
        if (frequency == null || frequency.equals(WiFiFrequencyBand.UNKNOWN)) {
            CallbackUtils.onFailure(callback, DJIAirLinkError.COMMON_PARAM_ILLEGAL);
            return;
        }
        DataWifiSetWifiFrequency setter = new DataWifiSetWifiFrequency();
        setter.setFromLongan(false);
        if (!frequency.equals(WiFiFrequencyBand.FREQUENCY_BAND_2_DOT_4_GHZ)) {
            i = 1;
        }
        setter.setFrequency(i).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.airlink.wifi.DJIWifiLinkGroundAbstraction.AnonymousClass1 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
            }
        });
    }

    public void getFrequencyBand(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataWifiGetWifiFrequency getter = new DataWifiGetWifiFrequency();
        getter.setFromLongan(false);
        getter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.airlink.wifi.DJIWifiLinkGroundAbstraction.AnonymousClass2 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, WiFiFrequencyBand.find(getter.getResult()));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
            }
        });
    }

    public void setSSID(String ssid, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (AirLinkUtils.verifySSID(ssid)) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
            return;
        }
        DataWifiSetSSID setter = new DataWifiSetSSID();
        setter.setFromLongan(true);
        setter.setSSID(ssid.getBytes()).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.airlink.wifi.DJIWifiLinkGroundAbstraction.AnonymousClass3 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
            }
        });
    }

    public void getSSID(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            final DataWifiGetSSID getter = new DataWifiGetSSID().setFromLongan(true);
            getter.start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.airlink.wifi.DJIWifiLinkGroundAbstraction.AnonymousClass4 */

                public void onSuccess(Object model) {
                    CallbackUtils.onSuccess(callback, getter.getSSID());
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
                }
            });
        }
    }

    public void setPassword(String pwd, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (pwd == null || pwd.length() < 8) {
            CallbackUtils.onFailure(callback, DJIAirLinkError.COMMON_PARAM_ILLEGAL);
            return;
        }
        DataWifiSetPassword setter = new DataWifiSetPassword();
        setter.setFromLongan(true);
        setter.setPassword(pwd.getBytes()).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.airlink.wifi.DJIWifiLinkGroundAbstraction.AnonymousClass5 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
            }
        });
    }

    public void getPassword(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            final DataWifiGetPassword getter = new DataWifiGetPassword().setFromLongan(true);
            getter.start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.airlink.wifi.DJIWifiLinkGroundAbstraction.AnonymousClass6 */

                public void onSuccess(Object model) {
                    CallbackUtils.onSuccess(callback, getter.getPassword());
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
                }
            });
        }
    }

    public void reboot(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataWifiRestart restarter = new DataWifiRestart();
        restarter.setFromLongan(true);
        restarter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.airlink.wifi.DJIWifiLinkGroundAbstraction.AnonymousClass7 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
            }
        });
    }
}
