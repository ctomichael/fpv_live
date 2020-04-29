package dji.publics.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Keep;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import dji.fieldAnnotation.EXClassNullAway;
import dji.pilot.publics.R;
import dji.publics.DJIUI.DJILinearLayout;
import dji.publics.DJIUI.DJITextView;
import java.util.ArrayList;

@Keep
@EXClassNullAway
public class DJIProductTextView extends DJILinearLayout {
    protected DJITextView mDeviceSubTv;
    protected DJITextView mDeviceTv;

    public DJIProductTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        addView(this, R.layout.product_name_view);
        if (!isInEditMode()) {
            this.mDeviceTv = (DJITextView) findViewById(R.id.product_device_tv);
            this.mDeviceSubTv = (DJITextView) findViewById(R.id.product_device_sub_tv);
            switchTypeface();
        }
    }

    private void switchTypeface() {
        this.mDeviceTv.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/fly-product-title.ttf"));
    }

    public void setTextColor(int colorId) {
        if (this.mDeviceTv != null) {
            this.mDeviceTv.setTextColor(colorId);
        }
        if (this.mDeviceSubTv != null) {
            this.mDeviceSubTv.setTextColor(colorId);
        }
    }

    public void setDeviceName(int strId) {
        if (this.mDeviceTv != null) {
            this.mDeviceTv.setText(strId);
        }
    }

    public void setDeviceName(String str) {
        if (this.mDeviceTv != null) {
            this.mDeviceTv.setText(str);
        }
    }

    public void setDeviceSubName(int strId) {
        if (this.mDeviceSubTv != null) {
            this.mDeviceSubTv.setText(strId);
            if (this.mDeviceSubTv.getText().equals("")) {
                this.mDeviceSubTv.go();
            } else {
                this.mDeviceSubTv.show();
            }
        }
    }

    public void setDeviceSubName(String str) {
        if (this.mDeviceSubTv != null) {
            this.mDeviceSubTv.setText(str);
            if (this.mDeviceSubTv.getText().equals("")) {
                this.mDeviceSubTv.go();
            } else {
                this.mDeviceSubTv.show();
            }
        }
    }

    public void setTextSize(int unit, float size) {
        if (this.mDeviceTv != null) {
            this.mDeviceTv.setTextSize(unit, size);
        }
        if (this.mDeviceSubTv != null) {
            this.mDeviceSubTv.setTextSize(unit, size);
        }
    }

    private void adjustTvTextSize(TextView tv, int maxWidth, String text) {
        int avaiWidth = ((maxWidth - tv.getPaddingLeft()) - tv.getPaddingRight()) - 10;
        if (avaiWidth > 0) {
            TextPaint textPaintClone = new TextPaint(tv.getPaint());
            float trySize = textPaintClone.getTextSize();
            while (textPaintClone.measureText(text) > ((float) avaiWidth)) {
                trySize -= 1.0f;
                textPaintClone.setTextSize(trySize);
            }
            tv.setTextSize(0, trySize);
        }
    }

    public void addView(View parent, int layoutId) {
        addView(parent, layoutId, parent.getContext());
    }

    private void addView(View parent, int layoutId, Context context) {
        View v = LayoutInflater.from(context).inflate(layoutId, (ViewGroup) null);
        if (!(parent instanceof LinearLayout) || !(v instanceof LinearLayout)) {
            ((ViewGroup) parent).addView(v);
        } else {
            addView((LinearLayout) parent, (LinearLayout) v);
        }
    }

    private void addView(LinearLayout parent, LinearLayout child) {
        ArrayList<View> views = new ArrayList<>();
        ArrayList<LinearLayout.LayoutParams> params = new ArrayList<>();
        int count = child.getChildCount();
        for (int i = 0; i < count; i++) {
            views.add(child.getChildAt(i));
            params.add((LinearLayout.LayoutParams) child.getChildAt(i).getLayoutParams());
        }
        child.removeAllViews();
        parent.setOrientation(child.getOrientation());
        for (int i2 = 0; i2 < count; i2++) {
            parent.addView((View) views.get(i2), (ViewGroup.LayoutParams) params.get(i2));
        }
    }
}
