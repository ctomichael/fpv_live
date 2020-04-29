package dji.publics.DJIUI;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import dji.frame.widget.R;

public class DJIOriLayout extends DJIRelativeLayout {
    private static boolean isAllowSetTypeByLayout = true;
    private static DJIDeviceType mdeviceType = DJIDeviceType.Phone;

    public enum DJIDeviceType {
        Phone,
        Pad,
        DJI5_5
    }

    public static void setDeviceType(DJIDeviceType deviceType) {
        mdeviceType = deviceType;
        isAllowSetTypeByLayout = false;
    }

    public static DJIDeviceType getDeviceType() {
        return mdeviceType;
    }

    public static void setOrientation(Activity activity, int requestedOrientation) {
        if (activity.getRequestedOrientation() != requestedOrientation) {
            activity.setRequestedOrientation(requestedOrientation);
        }
    }

    public static void setOrientationByDevice(Activity activity) {
        int requestedOrientation;
        if (mdeviceType == DJIDeviceType.Phone) {
            requestedOrientation = 7;
        } else {
            requestedOrientation = 6;
        }
        if (activity.getRequestedOrientation() != requestedOrientation) {
            activity.setRequestedOrientation(requestedOrientation);
        }
    }

    public DJIOriLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            TypedArray ar = context.obtainStyledAttributes(attrs, R.styleable.DJIDeviceType);
            int faceValue = ar.getInt(R.styleable.DJIDeviceType_djiDeviceType, 0);
            ar.recycle();
            if (!isAllowSetTypeByLayout) {
                return;
            }
            if (faceValue == 0) {
                mdeviceType = DJIDeviceType.Phone;
            } else {
                mdeviceType = DJIDeviceType.Pad;
            }
        }
    }
}
