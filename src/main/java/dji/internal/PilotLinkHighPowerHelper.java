package dji.internal;

import dji.log.DJILog;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.data.model.P3.DataOsdSetSdrAssitantRead;
import dji.midware.data.model.P3.DataOsdSetSdrAssitantWrite;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.util.BackgroundLooper;

public class PilotLinkHighPowerHelper {
    private static final int MAX_ERROR_CNT = 2;
    private static final String STR_AREA_CODE_CHINA = "CN";
    private static final String STR_AREA_CODE_USA = "US";
    private static final int SUPER_POWER_POSITION = 48;
    private boolean mIsOpen;

    private static class Holder {
        /* access modifiers changed from: private */
        public static PilotLinkHighPowerHelper INSTANCE = new PilotLinkHighPowerHelper();

        private Holder() {
        }
    }

    public static PilotLinkHighPowerHelper getInstance() {
        return Holder.INSTANCE;
    }

    private PilotLinkHighPowerHelper() {
        this.mIsOpen = false;
    }

    public void onCreate() {
        this.mIsOpen = false;
    }

    private boolean checkFileDataValid(byte[] rawData) {
        return rawData != null && rawData.length > 48;
    }

    private boolean checkSupportProductType() {
        return isPM420Platform() || isM200Platform();
    }

    public String openHighPowerByCountryCode(String countryCode) {
        if (!isSupportLbHighPowerMode() || !"CN".equalsIgnoreCase(countryCode)) {
            return countryCode;
        }
        return STR_AREA_CODE_USA;
    }

    public boolean openHighPowerByWriteConfig() {
        if (!isSupportSdrHighPowerMode()) {
            return false;
        }
        sendHighPowerCmd(DataOsdSetSdrAssitantRead.SdrDeviceType.Ground, 0);
        sendHighPowerCmd(DataOsdSetSdrAssitantRead.SdrDeviceType.Sky, 0);
        return true;
    }

    /* access modifiers changed from: private */
    public void sendHighPowerCmd(final DataOsdSetSdrAssitantRead.SdrDeviceType deviceType, final int count) {
        if (count <= 2) {
            new DataOsdSetSdrAssitantWrite().setSdrCpuType(DataOsdSetSdrAssitantRead.SdrCpuType.CP_A7).setSdrDataType(DataOsdSetSdrAssitantRead.SdrDataType.Byte_Data).setSdrDeviceType(deviceType).setAddress(-65435).setWriteValue(1).start(new DJIDataCallBack() {
                /* class dji.internal.PilotLinkHighPowerHelper.AnonymousClass1 */

                public void onSuccess(Object model) {
                }

                public void onFailure(Ccode ccode) {
                    int errCnt = count + 1;
                    DJILog.d("PilotHighPower", deviceType + " err try cnt : " + errCnt + " ccode " + ccode + " " + ccode.relValue(), new Object[0]);
                    if (ServiceManager.getInstance().isConnected()) {
                        BackgroundLooper.postDelayed(new PilotLinkHighPowerHelper$1$$Lambda$0(this, deviceType, errCnt), 5000);
                    }
                }

                /* access modifiers changed from: package-private */
                public final /* synthetic */ void lambda$onFailure$0$PilotLinkHighPowerHelper$1(DataOsdSetSdrAssitantRead.SdrDeviceType deviceType, int errCnt) {
                    PilotLinkHighPowerHelper.this.sendHighPowerCmd(deviceType, errCnt);
                }
            });
        }
    }

    public boolean isSupportLbHighPowerMode() {
        return false;
    }

    public boolean isSupportSdrHighPowerMode() {
        return false;
    }

    private boolean isM200Platform() {
        ProductType productType = DJIProductManager.getInstance().getType();
        return ProductType.M200 == productType || ProductType.M210 == productType || ProductType.M210RTK == productType;
    }

    private boolean isPM420Platform() {
        ProductType productType = DJIProductManager.getInstance().getType();
        return ProductType.PM420 == productType || ProductType.PM420PRO == productType || ProductType.PM420PRO_RTK == productType;
    }

    public void destroy() {
        this.mIsOpen = false;
    }
}
