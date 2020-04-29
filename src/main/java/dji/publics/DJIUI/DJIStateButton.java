package dji.publics.DJIUI;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

public class DJIStateButton extends Button {
    private float mStateAlpha = 0.3f;

    public DJIStateButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setStateAlpha(float stateAlpha) {
        this.mStateAlpha = stateAlpha;
    }

    /* access modifiers changed from: protected */
    public void drawableStateChanged() {
        super.drawableStateChanged();
        if (isPressed() || isFocused() || isSelected() || isHovered()) {
        }
    }
}
