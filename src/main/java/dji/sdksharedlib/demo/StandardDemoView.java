package dji.sdksharedlib.demo;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import dji.fieldAnnotation.EXClassNullAway;
import dji.sdksharedlib.DJISDKCache;
import dji.sdksharedlib.extension.KeyHelper;
import dji.sdksharedlib.keycatalog.CameraKeys;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.keycatalog.ProductKeys;
import dji.sdksharedlib.listener.DJIParamAccessListener;
import dji.sdksharedlib.store.DJISDKCacheParamValue;

@EXClassNullAway
public class StandardDemoView extends LinearLayout implements DJIParamAccessListener {
    DJISDKCacheKey keyPathISO = null;
    DJISDKCacheKey keyPathModel = null;

    public StandardDemoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        DJISDKCacheKey.Builder builder = new DJISDKCacheKey.Builder();
        builder.component(ProductKeys.COMPONENT_KEY);
        builder.paramKey(ProductKeys.MODEL_NAME);
        this.keyPathModel = builder.build();
        DJISDKCacheKey.Builder builder2 = new DJISDKCacheKey.Builder();
        builder2.component(CameraKeys.COMPONENT_KEY);
        builder2.paramKey(CameraKeys.ISO);
        this.keyPathISO = KeyHelper.getCameraKey(CameraKeys.ISO);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        DJISDKCache.getInstance().startListeningForUpdates(this.keyPathModel, this, true);
        DJISDKCache.getInstance().startListeningForUpdates(this.keyPathISO, this, true);
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        DJISDKCache.getInstance().stopListening(this);
    }

    public void onValueChange(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
        if (!key.equals(this.keyPathModel) && key.equals(this.keyPathISO)) {
        }
    }
}
