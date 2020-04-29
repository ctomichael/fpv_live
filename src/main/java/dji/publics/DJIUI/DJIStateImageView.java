package dji.publics.DJIUI;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class DJIStateImageView extends DJIImageView {
    private boolean mOnlyDisable = false;
    private float mStateAlpha = 0.3f;
    private View mView = null;

    public DJIStateImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnlyDisable(boolean only) {
        this.mOnlyDisable = only;
    }

    public void setRelativeStateView(View view) {
        this.mView = view;
    }

    public void setStateAlpha(float stateAlpha) {
        this.mStateAlpha = stateAlpha;
    }

    /* access modifiers changed from: protected */
    public void drawableStateChanged() {
        super.drawableStateChanged();
        if ((this.mOnlyDisable || (!isPressed() && !isFocused())) && isEnabled()) {
            setAlpha(1.0f);
            if (this.mView != null) {
                this.mView.setAlpha(1.0f);
                return;
            }
            return;
        }
        setAlpha(this.mStateAlpha);
        if (this.mView != null) {
            this.mView.setAlpha(this.mStateAlpha);
        }
    }
}
