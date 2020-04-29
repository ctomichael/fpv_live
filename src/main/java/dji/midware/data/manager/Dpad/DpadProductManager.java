package dji.midware.data.manager.Dpad;

import android.os.Build;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.midware.data.config.Dpad.DpadProductType;

@EXClassNullAway
public class DpadProductManager {
    private static final String TAG = "DpadProductManager";
    private static int mCnt = 1;
    private static final DpadProductManager mInstance = new DpadProductManager();
    private DpadProductType productType = DpadProductType.None;

    public static DpadProductManager getInstance() {
        return mInstance;
    }

    private DpadProductManager() {
        init();
    }

    public DpadProductType getProductType() {
        return this.productType;
    }

    public boolean isDpad() {
        return this.productType != DpadProductType.None;
    }

    public boolean isPomato() {
        return isPomato(this.productType);
    }

    public boolean useUsbdec() {
        return DpadProductType.Pomato == this.productType;
    }

    public static boolean isPomato(DpadProductType type) {
        if (type == null) {
            return false;
        }
        if (type == DpadProductType.Pomato || type == DpadProductType.PomatoSdr || type == DpadProductType.PomatoRTK) {
            return true;
        }
        return false;
    }

    public boolean useUsbWifiLink() {
        return this.productType == DpadProductType.PomatoSdr || this.productType == DpadProductType.PomatoRTK || isRM500();
    }

    public boolean isCrystalSky() {
        return this.productType == DpadProductType.CrystalSkyA || this.productType == DpadProductType.CrystalSkyB;
    }

    public boolean isCrystalSkyZS600a() {
        return this.productType == DpadProductType.CrystalSkyA;
    }

    public boolean isRcDpad() {
        return this.productType == DpadProductType.Pomato || this.productType == DpadProductType.PomatoSdr || this.productType == DpadProductType.PomatoRTK || this.productType == DpadProductType.Mg1S;
    }

    public boolean isCrystalSkyB() {
        return this.productType == DpadProductType.CrystalSkyB;
    }

    public boolean isRM500() {
        return this.productType == DpadProductType.RM500;
    }

    public boolean supportSystemVideoSmooth() {
        return isCrystalSky() || isRM500();
    }

    public boolean useLowQualityAnim() {
        return isCrystalSky() || isRM500();
    }

    private void init() {
        this.productType = DpadProductType.find(Build.PRODUCT);
        DJILog.i(TAG, "init(), productType = " + this.productType, new Object[0]);
    }

    public int getProductTag() {
        return getProductType().getTag();
    }
}
