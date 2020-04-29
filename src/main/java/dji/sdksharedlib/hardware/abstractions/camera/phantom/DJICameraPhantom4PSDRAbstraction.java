package dji.sdksharedlib.hardware.abstractions.camera.phantom;

import dji.common.error.DJICameraError;
import dji.common.util.CallbackUtils;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataCameraGetVOutParams;
import dji.midware.data.model.P3.DataCameraSetVOutParams;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.camera.DJICameraAbstraction;

public class DJICameraPhantom4PSDRAbstraction extends DJICameraPhantom4PAbstraction {
    /* access modifiers changed from: protected */
    public String getDisplayName() {
        return DJICameraAbstraction.DisplaynamePhantom4PSDRCamera;
    }

    public void setLiveViewOutputMode(boolean enabled, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        ((DataCameraSetVOutParams) new DataCameraSetVOutParams().setStream(enabled ? 2 : 1).setReceiverId(getReceiverIdByIndex(), DataCameraSetVOutParams.class)).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.phantom.DJICameraPhantom4PSDRAbstraction.AnonymousClass1 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJICameraError.getDJIError(ccode));
            }
        });
    }

    public void getLiveViewHDEnable(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        ((DataCameraGetVOutParams) DataCameraGetVOutParams.getInstance().setReceiverId(getReceiverIdByIndex(), DataCameraGetVOutParams.class)).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.phantom.DJICameraPhantom4PSDRAbstraction.AnonymousClass2 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, Boolean.valueOf(DataCameraGetVOutParams.getInstance().getStream(DJICameraPhantom4PSDRAbstraction.this.getExpectedSenderIdByIndex()) == 2));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJICameraError.getDJIError(ccode));
            }
        });
    }
}
