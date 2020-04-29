package dji.sdksharedlib.demo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SeekBar;
import dji.common.camera.SettingsDefinitions;
import dji.fieldAnnotation.EXClassNullAway;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.extension.KeyHelper;
import dji.sdksharedlib.extension.TrackKeyModel;
import dji.sdksharedlib.keycatalog.CameraKeys;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.listener.DJIParamAccessListener;
import dji.sdksharedlib.store.DJISDKCacheParamValue;

@EXClassNullAway
public class EasyTrackKeyDemoView extends View implements DJIParamAccessListener {
    /* access modifiers changed from: private */
    public TrackKeyModel<SettingsDefinitions.ISO> mKeyIso;
    private SeekBar mSeekBar;

    public EasyTrackKeyDemoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mSeekBar = null;
        this.mKeyIso = null;
        this.mKeyIso = new TrackKeyModel<SettingsDefinitions.ISO>(KeyHelper.getCameraKey(0, CameraKeys.ISO), this) {
            /* class dji.sdksharedlib.demo.EasyTrackKeyDemoView.AnonymousClass1 */

            public SettingsDefinitions.ISO getValue() {
                return EasyTrackKeyDemoView.this.transformISO();
            }
        };
    }

    /* access modifiers changed from: private */
    public SettingsDefinitions.ISO transformISO() {
        switch (this.mSeekBar.getProgress()) {
            case 0:
                return SettingsDefinitions.ISO.ISO_100;
            case 1:
                return SettingsDefinitions.ISO.ISO_200;
            default:
                return SettingsDefinitions.ISO.ISO_100;
        }
    }

    private int reverseISO(SettingsDefinitions.ISO iso) {
        if (SettingsDefinitions.ISO.ISO_100 != iso && SettingsDefinitions.ISO.ISO_200 == iso) {
            return 1;
        }
        return 0;
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mSeekBar = null;
        this.mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /* class dji.sdksharedlib.demo.EasyTrackKeyDemoView.AnonymousClass2 */

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                EasyTrackKeyDemoView.this.mKeyIso.startTracking();
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                EasyTrackKeyDemoView.this.mKeyIso.stopTracking(EasyTrackKeyDemoView.this.transformISO());
            }
        });
    }

    public void onValueChange(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
        this.mSeekBar.setProgress(reverseISO((SettingsDefinitions.ISO) CacheHelper.getValue(KeyHelper.getCameraKey(0, CameraKeys.ISO), SettingsDefinitions.ISO.UNKNOWN)));
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mKeyIso.bindData();
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        this.mKeyIso.unbindData();
        super.onDetachedFromWindow();
    }
}
