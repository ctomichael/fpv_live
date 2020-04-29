package dji.publics.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import com.dji.frame.util.V_ActivityUtil;
import com.dji.frame.util.V_AppUtils;
import dji.publics.widget.dialog.DJIDialog;
import dji.publics.widget.util.Utils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class DJINewBaseDialog extends Dialog {
    protected static final float DEFAULT_DIM_AMOUNT = 0.4f;
    public int height;
    private boolean isHideSystemBar;
    protected Context mContext;
    protected DJIDialogType mDialogType;
    private float mDimAmout;
    public Handler mPostHandler;
    protected DJIDialog.DJIDialogTheme mTheme;
    private boolean mTypeToast;
    public int width;

    public DJINewBaseDialog(Context context) {
        this(context, R.style.LogDialog);
        this.mContext = context;
    }

    public DJINewBaseDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = null;
        this.mPostHandler = new Handler(Looper.getMainLooper());
        this.isHideSystemBar = true;
        this.mDimAmout = 0.0f;
        this.mTypeToast = false;
        this.mDialogType = DJIDialogType.SMALL;
        this.mTheme = DJIDialog.DJIDialogTheme.BLACK;
        this.mContext = context;
    }

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public DJINewBaseDialog(android.content.Context r3, dji.publics.widget.dialog.DJIDialogType r4, dji.publics.widget.dialog.DJIDialog.DJIDialogTheme r5) {
        /*
            r2 = this;
            dji.publics.widget.dialog.DJIDialog$DJIDialogTheme r0 = dji.publics.widget.dialog.DJIDialog.DJIDialogTheme.BLACK
            if (r5 != r0) goto L_0x002f
            int r0 = dji.publics.widget.dialog.R.style.LogDialog
        L_0x0006:
            r2.<init>(r3, r0)
            r0 = 0
            r2.mContext = r0
            android.os.Handler r0 = new android.os.Handler
            android.os.Looper r1 = android.os.Looper.getMainLooper()
            r0.<init>(r1)
            r2.mPostHandler = r0
            r0 = 1
            r2.isHideSystemBar = r0
            r0 = 0
            r2.mDimAmout = r0
            r0 = 0
            r2.mTypeToast = r0
            dji.publics.widget.dialog.DJIDialogType r0 = dji.publics.widget.dialog.DJIDialogType.SMALL
            r2.mDialogType = r0
            dji.publics.widget.dialog.DJIDialog$DJIDialogTheme r0 = dji.publics.widget.dialog.DJIDialog.DJIDialogTheme.BLACK
            r2.mTheme = r0
            r2.mContext = r3
            r2.mDialogType = r4
            r2.mTheme = r5
            return
        L_0x002f:
            int r0 = dji.publics.widget.dialog.R.style.LogDialogShowDarkBackground
            goto L_0x0006
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.publics.widget.dialog.DJINewBaseDialog.<init>(android.content.Context, dji.publics.widget.dialog.DJIDialogType, dji.publics.widget.dialog.DJIDialog$DJIDialogTheme):void");
    }

    public DJINewBaseDialog(Context context, boolean isHideSystemBar2) {
        this(context, R.style.LogDialog);
        this.isHideSystemBar = isHideSystemBar2;
    }

    public DJINewBaseDialog(Context context, int theme, boolean isHideSystemBar2) {
        super(context, theme);
        this.mContext = null;
        this.mPostHandler = new Handler(Looper.getMainLooper());
        this.isHideSystemBar = true;
        this.mDimAmout = 0.0f;
        this.mTypeToast = false;
        this.mDialogType = DJIDialogType.SMALL;
        this.mTheme = DJIDialog.DJIDialogTheme.BLACK;
        this.mContext = context;
        this.isHideSystemBar = isHideSystemBar2;
    }

    public void setHideSystemBar(boolean isHideSystemBar2) {
        this.isHideSystemBar = isHideSystemBar2;
        if (getWindow() != null && !isHideSystemBar2) {
            getWindow().getDecorView().setSystemUiVisibility(0);
        }
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
        this.mPostHandler.postDelayed(new Runnable() {
            /* class dji.publics.widget.dialog.DJINewBaseDialog.AnonymousClass1 */

            public void run() {
                if ((!(DJINewBaseDialog.this.getContext() instanceof Activity) || !((Activity) DJINewBaseDialog.this.getContext()).isFinishing()) && DJINewBaseDialog.this.isShowing()) {
                    WindowManager.LayoutParams attrs = DJINewBaseDialog.this.getWindow().getAttributes();
                    attrs.flags &= -9;
                    DJINewBaseDialog.this.getWindow().setAttributes(attrs);
                }
            }
        }, 50);
    }

    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        if (this.isHideSystemBar) {
            V_AppUtils.enter(getWindow());
        }
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    public void onDetachedFromWindow() {
        if (this.isHideSystemBar) {
            EventBus.getDefault().post(V_AppUtils.DJI_SYS_UI_EVENT.HIDE);
        }
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDetachedFromWindow();
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (isShowing() && this.isHideSystemBar) {
            V_AppUtils.enter(getWindow());
        }
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
        switch (this.mDialogType) {
            case SMALL:
                this.width = Utils.getDimens(this.mContext, R.dimen.new_dialog_width_small);
                break;
            case MEDIUM:
                this.width = this.mTheme == DJIDialog.DJIDialogTheme.WHITE ? Utils.getDimens(this.mContext, R.dimen.new_dialog_width_medium_white) : Utils.getDimens(this.mContext, R.dimen.new_dialog_width_medium);
                break;
            case LARGE:
                this.width = Utils.getDimens(this.mContext, R.dimen.new_dialog_width_large);
                break;
        }
        this.width = adjustDialogWidth(this.width);
        adjustAttrs(this.width, this.height, 0, 17);
    }

    private int adjustDialogWidth(int width2) {
        int maxWidth = getScreenWidth() - Utils.getDimens(getContext(), R.dimen.new_dialog_margin_left);
        return width2 > maxWidth ? maxWidth : width2;
    }

    public void adjustAttrs(int width2, int height2, int yOffset, int gravity) {
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        if (V_ActivityUtil.isApkDebugable(this.mContext) && this.mTypeToast) {
            attrs.type = 2005;
        }
        attrs.width = width2;
        attrs.y = yOffset;
        attrs.gravity = gravity;
        getWindow().setAttributes(attrs);
        getWindow().setWindowAnimations(R.style.dialogWindowAnim);
    }

    public void setWindowTypeToast(boolean typeToast) {
        this.mTypeToast = typeToast;
    }

    public void setNoModalDlg() {
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags &= -33;
        attrs.flags &= -9;
        getWindow().setAttributes(attrs);
    }

    public void setBeModalDlg() {
        this.mPostHandler.postDelayed(new Runnable() {
            /* class dji.publics.widget.dialog.DJINewBaseDialog.AnonymousClass2 */

            public void run() {
                if ((!(DJINewBaseDialog.this.getContext() instanceof Activity) || !((Activity) DJINewBaseDialog.this.getContext()).isFinishing()) && DJINewBaseDialog.this.isShowing()) {
                    WindowManager.LayoutParams attrs = DJINewBaseDialog.this.getWindow().getAttributes();
                    attrs.flags |= 32;
                    DJINewBaseDialog.this.getWindow().setAttributes(attrs);
                }
            }
        }, 500);
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

    /* access modifiers changed from: protected */
    public int getScreenWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService("window");
        if (Build.VERSION.SDK_INT < 17) {
            DisplayMetrics metrics = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(metrics);
            if (getContext().getResources().getConfiguration().orientation != 2) {
                return metrics.widthPixels;
            }
            if (metrics.widthPixels > metrics.heightPixels) {
                return metrics.widthPixels;
            }
            return metrics.heightPixels;
        }
        Display display = wm.getDefaultDisplay();
        Point outSize = new Point();
        display.getRealSize(outSize);
        if (getContext().getResources().getConfiguration().orientation == 2) {
            return outSize.x > outSize.y ? outSize.x : outSize.y;
        }
        return outSize.x;
    }
}
