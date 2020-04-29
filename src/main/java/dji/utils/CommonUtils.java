package dji.utils;

import android.content.Context;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.view.MotionEvent;
import android.view.View;
import dji.publics.protocol.ResponseBase;

public class CommonUtils {
    private static final int MIN_DELAY_TIME = 680;
    private static long lastClickTime;

    public static boolean isPermisionDenied(Context context, String permission) {
        int permissionResult = ActivityCompat.checkSelfPermission(context, permission);
        if (Build.VERSION.SDK_INT < 23 || permissionResult == 0) {
            return false;
        }
        return true;
    }

    public static final boolean isLocationEnable(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(ResponseBase.STRING_LOCATION);
        boolean networkProvider = locationManager.isProviderEnabled("network");
        boolean gpsProvider = locationManager.isProviderEnabled("gps");
        if (networkProvider || gpsProvider) {
            return true;
        }
        return false;
    }

    public static void setLongClick(Handler handler, View longClickView, long delayMillis, View.OnLongClickListener longClickListener) {
        final Handler handler2 = handler;
        final long j = delayMillis;
        final View.OnLongClickListener onLongClickListener = longClickListener;
        final View view = longClickView;
        longClickView.setOnTouchListener(new View.OnTouchListener() {
            /* class dji.utils.CommonUtils.AnonymousClass1 */
            private int TOUCH_MAX = 50;
            private int mLastMotionX;
            private int mLastMotionY;
            private Runnable r = new Runnable() {
                /* class dji.utils.CommonUtils.AnonymousClass1.AnonymousClass1 */

                public void run() {
                    if (onLongClickListener != null) {
                        onLongClickListener.onLongClick(view);
                    }
                }
            };

            public boolean onTouch(View v, MotionEvent event) {
                int x = (int) event.getX();
                int y = (int) event.getY();
                switch (event.getAction()) {
                    case 0:
                        handler2.removeCallbacks(this.r);
                        this.mLastMotionX = x;
                        this.mLastMotionY = y;
                        handler2.postDelayed(this.r, j);
                        return true;
                    case 1:
                        handler2.removeCallbacks(this.r);
                        return true;
                    case 2:
                        if (Math.abs(this.mLastMotionX - x) <= this.TOUCH_MAX && Math.abs(this.mLastMotionY - y) <= this.TOUCH_MAX) {
                            return true;
                        }
                        handler2.removeCallbacks(this.r);
                        return true;
                    case 3:
                        handler2.removeCallbacks(this.r);
                        return true;
                    default:
                        return true;
                }
            }
        });
    }

    public static boolean isFastClick() {
        boolean flag = true;
        long currentClickTime = System.currentTimeMillis();
        if (currentClickTime - lastClickTime >= 680) {
            flag = false;
        }
        lastClickTime = currentClickTime;
        return flag;
    }
}
