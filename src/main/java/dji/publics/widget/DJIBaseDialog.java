package dji.publics.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import com.dji.frame.util.V_AppUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.pilot.publics.R;
import dji.publics.widget.dialog.DJIDialog;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class DJIBaseDialog extends Dialog {
    protected static final float DEFAULT_DIM_AMOUNT = 0.4f;
    public int height;
    private Runnable hiddenVirtualBarRunable;
    private boolean isHideSystemBar;
    protected Context mContext;
    private float mDimAmout;
    private Handler mPostHandler;
    private Runnable notTouchModalRunable;
    public int width;

    public DJIBaseDialog(Context context) {
        this(context, R.style.LogDialog);
    }

    public DJIBaseDialog(Context context, boolean isHideSystemBar2) {
        this(context, R.style.LogDialog);
        this.isHideSystemBar = isHideSystemBar2;
    }

    public DJIBaseDialog(Context context, int theme) {
        super(context, theme);
        this.mContext = null;
        this.mPostHandler = new Handler();
        this.isHideSystemBar = true;
        this.mDimAmout = 0.0f;
        this.notTouchModalRunable = new Runnable() {
            /* class dji.publics.widget.DJIBaseDialog.AnonymousClass1 */

            public void run() {
                if ((DJIBaseDialog.this.getContext() instanceof Activity) && ((Activity) DJIBaseDialog.this.getContext()).isFinishing()) {
                    return;
                }
                if ((!(DJIBaseDialog.this.getContext() instanceof Activity) || !((Activity) DJIBaseDialog.this.getContext()).isDestroyed()) && DJIBaseDialog.this.isShowing()) {
                    WindowManager.LayoutParams attrs = DJIBaseDialog.this.getWindow().getAttributes();
                    attrs.flags |= 32;
                    DJIBaseDialog.this.getWindow().setAttributes(attrs);
                }
            }
        };
        this.hiddenVirtualBarRunable = new Runnable() {
            /* class dji.publics.widget.DJIBaseDialog.AnonymousClass2 */

            public void run() {
                if ((DJIBaseDialog.this.getContext() instanceof Activity) && ((Activity) DJIBaseDialog.this.getContext()).isFinishing()) {
                    return;
                }
                if ((!(DJIBaseDialog.this.getContext() instanceof Activity) || !((Activity) DJIBaseDialog.this.getContext()).isDestroyed()) && DJIBaseDialog.this.isShowing()) {
                    WindowManager.LayoutParams attrs = DJIBaseDialog.this.getWindow().getAttributes();
                    attrs.flags &= -9;
                    DJIBaseDialog.this.getWindow().setAttributes(attrs);
                }
            }
        };
        this.mContext = context;
    }

    public DJIBaseDialog(Context context, int theme, boolean isHideSystemBar2) {
        super(context, theme);
        this.mContext = null;
        this.mPostHandler = new Handler();
        this.isHideSystemBar = true;
        this.mDimAmout = 0.0f;
        this.notTouchModalRunable = new Runnable() {
            /* class dji.publics.widget.DJIBaseDialog.AnonymousClass1 */

            public void run() {
                if ((DJIBaseDialog.this.getContext() instanceof Activity) && ((Activity) DJIBaseDialog.this.getContext()).isFinishing()) {
                    return;
                }
                if ((!(DJIBaseDialog.this.getContext() instanceof Activity) || !((Activity) DJIBaseDialog.this.getContext()).isDestroyed()) && DJIBaseDialog.this.isShowing()) {
                    WindowManager.LayoutParams attrs = DJIBaseDialog.this.getWindow().getAttributes();
                    attrs.flags |= 32;
                    DJIBaseDialog.this.getWindow().setAttributes(attrs);
                }
            }
        };
        this.hiddenVirtualBarRunable = new Runnable() {
            /* class dji.publics.widget.DJIBaseDialog.AnonymousClass2 */

            public void run() {
                if ((DJIBaseDialog.this.getContext() instanceof Activity) && ((Activity) DJIBaseDialog.this.getContext()).isFinishing()) {
                    return;
                }
                if ((!(DJIBaseDialog.this.getContext() instanceof Activity) || !((Activity) DJIBaseDialog.this.getContext()).isDestroyed()) && DJIBaseDialog.this.isShowing()) {
                    WindowManager.LayoutParams attrs = DJIBaseDialog.this.getWindow().getAttributes();
                    attrs.flags &= -9;
                    DJIBaseDialog.this.getWindow().setAttributes(attrs);
                }
            }
        };
        this.mContext = context;
        this.isHideSystemBar = isHideSystemBar2;
    }

    public DJIBaseDialog(Context context, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = null;
        this.mPostHandler = new Handler();
        this.isHideSystemBar = true;
        this.mDimAmout = 0.0f;
        this.notTouchModalRunable = new Runnable() {
            /* class dji.publics.widget.DJIBaseDialog.AnonymousClass1 */

            public void run() {
                if ((DJIBaseDialog.this.getContext() instanceof Activity) && ((Activity) DJIBaseDialog.this.getContext()).isFinishing()) {
                    return;
                }
                if ((!(DJIBaseDialog.this.getContext() instanceof Activity) || !((Activity) DJIBaseDialog.this.getContext()).isDestroyed()) && DJIBaseDialog.this.isShowing()) {
                    WindowManager.LayoutParams attrs = DJIBaseDialog.this.getWindow().getAttributes();
                    attrs.flags |= 32;
                    DJIBaseDialog.this.getWindow().setAttributes(attrs);
                }
            }
        };
        this.hiddenVirtualBarRunable = new Runnable() {
            /* class dji.publics.widget.DJIBaseDialog.AnonymousClass2 */

            public void run() {
                if ((DJIBaseDialog.this.getContext() instanceof Activity) && ((Activity) DJIBaseDialog.this.getContext()).isFinishing()) {
                    return;
                }
                if ((!(DJIBaseDialog.this.getContext() instanceof Activity) || !((Activity) DJIBaseDialog.this.getContext()).isDestroyed()) && DJIBaseDialog.this.isShowing()) {
                    WindowManager.LayoutParams attrs = DJIBaseDialog.this.getWindow().getAttributes();
                    attrs.flags &= -9;
                    DJIBaseDialog.this.getWindow().setAttributes(attrs);
                }
            }
        };
        this.mContext = context;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adjustAttrsDefault();
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags |= 8;
        getWindow().setAttributes(attrs);
        super.onStart();
        this.mPostHandler.postDelayed(this.hiddenVirtualBarRunable, 50);
    }

    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        if (this.isHideSystemBar) {
            V_AppUtils.enter(getWindow());
        }
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.isHideSystemBar) {
            EventBus.getDefault().post(V_AppUtils.DJI_SYS_UI_EVENT.HIDE);
        }
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (isShowing() && this.isHideSystemBar) {
            V_AppUtils.enter(getWindow());
        }
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        this.mPostHandler.removeCallbacks(this.notTouchModalRunable);
        this.mPostHandler.removeCallbacks(this.hiddenVirtualBarRunable);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent3MainThread(V_AppUtils.DJI_SYS_UI_EVENT event) {
        if (isShowing() && this.isHideSystemBar) {
            switch (event) {
                case HIDE:
                    V_AppUtils.enter(getWindow());
                    return;
                case HIDE_DELAY:
                default:
                    return;
            }
        }
    }

    public boolean dispatchTouchEvent(MotionEvent event) {
        if (this.isHideSystemBar) {
            V_AppUtils.enter(getWindow());
        }
        if (event.getAction() != 0 || !isOutOfBounds(event) || !handleTouchOutside()) {
            return super.dispatchTouchEvent(event);
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void adjustAttrsDefault() {
    }

    public void adjustAttrs(int width2, int height2, int yOffset, int gravity, boolean cancelable, boolean cancelTouchOutside) {
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        this.width = width2;
        this.height = height2;
        attrs.width = width2;
        attrs.height = height2;
        attrs.y = yOffset;
        attrs.dimAmount = this.mDimAmout;
        attrs.flags &= -3;
        attrs.gravity = gravity;
        getWindow().setAttributes(attrs);
        getWindow().setWindowAnimations(R.style.dialogWindowAnim);
        setCancelable(cancelable);
        setCanceledOnTouchOutside(cancelTouchOutside);
    }

    public void setNoModalDlg() {
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags &= -33;
        attrs.flags &= -9;
        getWindow().setAttributes(attrs);
    }

    public void setBeModalDlg() {
        this.mPostHandler.postDelayed(this.notTouchModalRunable, 500);
    }

    public void setBehindDim(float amount) {
        this.mDimAmout = amount;
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.dimAmount = amount;
        attrs.flags |= 2;
        getWindow().setAttributes(attrs);
    }

    /* access modifiers changed from: protected */
    public boolean handleTouchOutside() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isOutOfBounds(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        int slop = ViewConfiguration.get(this.mContext).getScaledWindowTouchSlop();
        View decorView = getWindow().getDecorView();
        return x < (-slop) || y < (-slop) || x > decorView.getWidth() + slop || y > decorView.getHeight() + slop;
    }

    public void show() {
        if (!DJIDialog.isForbidShowDialog() && !isActivityFinish()) {
            super.show();
        }
    }

    public void dismiss() {
        if (!isActivityFinish()) {
            super.dismiss();
        }
    }

    private boolean isActivityFinish() {
        if (this.mContext instanceof Activity) {
            if (((Activity) this.mContext).isFinishing()) {
                return true;
            }
        } else if (!(this.mContext instanceof Activity) && (this.mContext instanceof ContextWrapper)) {
            Context context = ((ContextWrapper) this.mContext).getBaseContext();
            if ((context instanceof Activity) && ((Activity) context).isFinishing()) {
                return true;
            }
        }
        return false;
    }
}
