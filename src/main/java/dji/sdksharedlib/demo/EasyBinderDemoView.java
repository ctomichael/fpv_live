package dji.sdksharedlib.demo;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import dji.fieldAnnotation.EXClassNullAway;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.extension.KeyHelper;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.keycatalog.FlightControllerKeys;
import dji.sdksharedlib.listener.DJIParamAccessListener;
import dji.sdksharedlib.store.DJISDKCacheParamValue;

@EXClassNullAway
public class EasyBinderDemoView extends View implements DJIParamAccessListener {
    private volatile boolean isBinded;
    private DJISDKCacheKey mKeyAreMotorUp;

    public EasyBinderDemoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.isBinded = false;
        this.mKeyAreMotorUp = null;
        this.mKeyAreMotorUp = KeyHelper.getFlightControllerKey(FlightControllerKeys.ARE_MOTOR_ON);
    }

    public void onValueChange(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
        if (this.mKeyAreMotorUp.equals(key)) {
            updateViewByMotorUp();
        }
    }

    private void updateViewByMotorUp() {
        boolean bool = CacheHelper.toBool(CacheHelper.getValue(this.mKeyAreMotorUp), false);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        bindData();
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        unbindData();
        super.onDetachedFromWindow();
    }

    public void bindData() {
        if (!this.isBinded) {
            this.isBinded = true;
            CacheHelper.addListener(this, this.mKeyAreMotorUp);
        }
    }

    public void unbindData() {
        if (this.isBinded) {
            CacheHelper.removeListener(this);
            this.isBinded = false;
        }
    }
}
