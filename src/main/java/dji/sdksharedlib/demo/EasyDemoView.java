package dji.sdksharedlib.demo;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import dji.common.battery.WarningRecord;
import dji.common.product.Model;
import dji.fieldAnnotation.EXClassNullAway;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.keycatalog.BatteryKeys;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.keycatalog.ProductKeys;
import dji.sdksharedlib.listener.DJIParamAccessListener;
import dji.sdksharedlib.store.DJISDKCacheParamValue;

@EXClassNullAway
public class EasyDemoView extends LinearLayout implements DJIParamAccessListener {
    public EasyDemoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        CacheHelper.addProductListener(this, ProductKeys.MODEL_NAME);
        CacheHelper.addBatteryListener(this, "ChargeRemaining", BatteryKeys.FULL_CHARGE_CAPACITY, BatteryKeys.LATEST_WARNING_RECORD);
        updateView();
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        CacheHelper.removeListener(this);
    }

    private void updateView() {
        Model model = (Model) CacheHelper.getProduct(ProductKeys.MODEL_NAME);
        int[] iArr = (int[]) CacheHelper.getBattery(BatteryKeys.CELL_VOLTAGES);
        WarningRecord warningRecord = (WarningRecord) CacheHelper.getBattery(BatteryKeys.LATEST_WARNING_RECORD);
    }

    public void onValueChange(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
        updateView();
    }
}
