package dji.publics.DJIUI;

import android.content.Context;
import android.util.AttributeSet;

public class DJIStateLinearLayout extends DJILinearLayout {
    private float mStateAlpha = 0.3f;

    public DJIStateLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setStateAlpha(float stateAlpha) {
        this.mStateAlpha = stateAlpha;
    }

    /* access modifiers changed from: protected */
    public void drawableStateChanged() {
        super.drawableStateChanged();
        if (isPressed() || isFocused() || !isEnabled()) {
            setAlpha(this.mStateAlpha);
        } else {
            setAlpha(1.0f);
        }
    }
}
