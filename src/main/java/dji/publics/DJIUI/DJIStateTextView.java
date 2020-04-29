package dji.publics.DJIUI;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class DJIStateTextView extends DJITextView {
    private float mStateAlpha = 0.3f;
    private View mView = null;

    public DJIStateTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setRelativeStateView(View view) {
        this.mView = view;
    }

    public void setRelativeStateView(View view, float stateAlpha) {
        this.mView = view;
        this.mStateAlpha = stateAlpha;
    }

    /* access modifiers changed from: protected */
    public void drawableStateChanged() {
        super.drawableStateChanged();
        if (isPressed() || isFocused() || !isEnabled()) {
            setAlpha(this.mStateAlpha);
            if (this.mView != null) {
                this.mView.setAlpha(this.mStateAlpha);
                return;
            }
            return;
        }
        setAlpha(1.0f);
        if (this.mView != null) {
            this.mView.setAlpha(1.0f);
        }
    }
}
