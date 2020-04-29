package dji.publics.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Keep;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import com.dji.frame.util.V_AppUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.manager.Dpad.DpadProductManager;
import dji.midware.data.model.P3.DataCameraControlUpgrade;
import dji.midware.data.model.P3.DataCameraGetPushUpgradeStatus;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.midware.interfaces.DJIDataCallBack;
import dji.pilot.publics.BuildConfig;
import dji.pilot.publics.util.DpadUIUtil;
import dji.publics.DJIUI.DJIImageView;
import dji.publics.utils.AppUIUtils;
import dji.publics.utils.DJIUtils;
import dji.publics.utils.NavigationBarUtil;
import dji.publics.utils.StatusBarUtil;
import dji.publics.widget.dialog.DJILargeCustomDialog;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@Keep
@EXClassNullAway
public class DJIFragmentActivityNoFullScreen extends Activity implements DpadUIUtil.DpadOrientationHelper {
    private static final int INTERVAL_LOG = 300;
    /* access modifiers changed from: private */
    public static String TAG = "DJIFragmentActivityNoFullScreen";
    private static long mLastTime = 0;
    public static int screenHeight;
    public static float screenRatio;
    public static int screenWidth;
    private Handler handler = new Handler(new Handler.Callback() {
        /* class dji.publics.activity.DJIFragmentActivityNoFullScreen.AnonymousClass2 */

        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                case 3:
                default:
                    return false;
                case 100:
                    DJIFragmentActivityNoFullScreen.this.onEvent3MainThread(DataCameraGetPushUpgradeStatus.getInstance());
                    DJILogHelper.getInstance().LOGD(DJIFragmentActivityNoFullScreen.TAG, "base activity DJIUpgradeNoticeEvent");
                    return false;
            }
        }
    });
    private boolean isMotorUp = false;
    protected boolean isVisible;
    protected ViewGroup mAndroidContentView = null;
    private Context mContext;
    protected BitmapFactory.Options mGuideBmpOption = null;
    protected View.OnClickListener mGuideClickListener = null;
    protected DJIImageView mGuideImg = null;
    protected int mGuideIndex = 0;
    protected View mGuideLy = null;
    protected int[] mGuideResIds = null;
    protected boolean mGuideShowing = false;
    private DataCameraGetPushUpgradeStatus.UpgradeStep mStep;
    private DJILargeCustomDialog rcGimbaldialog;
    private int upgradeRestartTime = 5;
    protected Window window;

    /* access modifiers changed from: protected */
    @TargetApi(19)
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestedDpadOrientation();
        if (DpadProductManager.getInstance().isDpad()) {
            DpadUIUtil.hideStatusBar(this);
            NavigationBarUtil.transparencyBar(this);
            setNavigationBar();
        } else {
            StatusBarUtil.transparencyBar(this);
            NavigationBarUtil.noTransparencyBar(this);
            StatusBarUtil.StatusBarLightMode(this);
        }
        if (BuildConfig.DEBUG) {
            AppUIUtils.add(this);
        }
    }

    /* access modifiers changed from: protected */
    public void setNavigationBar() {
        int flags;
        if (Build.VERSION.SDK_INT >= 19) {
            flags = 2050;
        } else {
            flags = 2;
        }
        getWindow().getDecorView().setSystemUiVisibility(flags);
        DpadUIUtil.setSystemFullScreen(this, false);
    }

    /* access modifiers changed from: protected */
    public void enter(Window window2) {
    }

    @SuppressLint({"NewApi"})
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        this.window = getWindow();
        this.mContext = this;
        DJIUtils.createSizeName(getBaseContext());
        if (screenWidth == 0) {
            if (Build.VERSION.SDK_INT < 17) {
                DisplayMetrics metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);
                screenWidth = metrics.widthPixels;
                screenHeight = metrics.heightPixels;
                if (screenWidth < screenHeight) {
                    int tmp = screenWidth;
                    screenWidth = screenHeight;
                    screenHeight = tmp;
                }
            } else {
                Display display = getWindowManager().getDefaultDisplay();
                Point outSize = new Point();
                display.getRealSize(outSize);
                screenWidth = outSize.x > outSize.y ? outSize.x : outSize.y;
                screenHeight = outSize.x > outSize.y ? outSize.y : outSize.x;
            }
            screenRatio = (((float) screenWidth) * 1.0f) / ((float) screenHeight);
        }
        this.handler.sendEmptyMessageDelayed(100, 1000);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        this.isVisible = true;
        if (DpadProductManager.getInstance().isDpad()) {
            setNavigationBar();
        }
        if (BuildConfig.DEBUG) {
            AppUIUtils.focus(this);
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        this.isVisible = false;
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        DataCameraGetPushUpgradeStatus.getInstance().clear();
    }

    public void finishThis() {
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        if (BuildConfig.DEBUG) {
            AppUIUtils.remove(this);
        }
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == 0) {
            long current = System.currentTimeMillis();
            if (current - mLastTime < 300) {
                DJILogHelper.getInstance().autoHandle();
                Log.d("", "click double");
                mLastTime = 0;
            } else {
                mLastTime = current;
                Log.d("", "click single");
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent3MainThread(V_AppUtils.DJI_SYS_UI_EVENT event) {
        if (this.isVisible) {
            switch (event) {
                case HIDE:
                case HIDE_DELAY:
                default:
                    return;
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataOsdGetPushCommon pushCommon) {
        if (this.isMotorUp != pushCommon.isMotorUp()) {
            this.isMotorUp = pushCommon.isMotorUp();
            this.handler.sendEmptyMessage(300);
        }
        onBackgroundThreadOver(pushCommon);
    }

    /* access modifiers changed from: protected */
    public void onBackgroundThreadOver(DataOsdGetPushCommon pushCommon) {
    }

    private void showUserConfirm() {
        DataCameraControlUpgrade.getInstance().setControlCmd(DataCameraControlUpgrade.ControlCmd.Start).start(new DJIDataCallBack() {
            /* class dji.publics.activity.DJIFragmentActivityNoFullScreen.AnonymousClass1 */

            public void onSuccess(Object model) {
                DJILogHelper.getInstance().LOGD("", "ControlCmd start success", false, true);
            }

            public void onFailure(Ccode ccode) {
                DJILogHelper.getInstance().LOGD("", "ControlCmd start " + ccode, false, true);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent3MainThread(DataCameraGetPushUpgradeStatus upgradeStatus) {
        DataCameraGetPushUpgradeStatus.UpgradeStep step;
        if (this.isVisible && (step = upgradeStatus.getStep()) != this.mStep) {
            this.mStep = step;
            DJILogHelper.getInstance().LOGD(TAG, "upgrade step " + this.mStep, false, true);
            switch (step) {
                case Check:
                case End:
                default:
                    return;
                case Ack:
                    showUserConfirm();
                    return;
            }
        }
    }

    public int getStatusBarH() {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            return getResources().getDimensionPixelSize(Integer.parseInt(c.getField("status_bar_height").get(c.newInstance()).toString()));
        } catch (Exception e1) {
            e1.printStackTrace();
            return 0;
        }
    }

    public void requestedDpadOrientation() {
    }
}
