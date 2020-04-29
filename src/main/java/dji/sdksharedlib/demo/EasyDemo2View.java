package dji.sdksharedlib.demo;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import dji.fieldAnnotation.EXClassNullAway;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.extension.KeyHelper;
import dji.sdksharedlib.keycatalog.CameraKeys;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.keycatalog.ProductKeys;
import dji.sdksharedlib.listener.DJIParamAccessListener;
import dji.sdksharedlib.store.DJISDKCacheParamValue;

@EXClassNullAway
public class EasyDemo2View extends LinearLayout implements DJIParamAccessListener {
    DJISDKCacheKey keyPathISO;
    DJISDKCacheKey keyPathModel;

    public EasyDemo2View(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.keyPathModel = null;
        this.keyPathISO = null;
        this.keyPathModel = KeyHelper.getProductKey(ProductKeys.MODEL_NAME);
        this.keyPathISO = KeyHelper.getCameraKey(CameraKeys.ISO);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        CacheHelper.addListener(this, this.keyPathISO, this.keyPathModel);
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        CacheHelper.removeListener(this);
    }

    public void onValueChange(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
        if (key != this.keyPathModel && key == this.keyPathISO) {
        }
    }
}
