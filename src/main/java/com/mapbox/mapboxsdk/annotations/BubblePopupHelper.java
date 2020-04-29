package com.mapbox.mapboxsdk.annotations;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.widget.PopupWindow;
import com.mapbox.mapboxsdk.R;

@Deprecated
class BubblePopupHelper {
    BubblePopupHelper() {
    }

    @NonNull
    static PopupWindow create(@NonNull Context context, @NonNull BubbleLayout bubbleLayout) {
        Drawable drawable;
        PopupWindow popupWindow = new PopupWindow(context);
        popupWindow.setContentView(bubbleLayout);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setWidth(-2);
        popupWindow.setHeight(-2);
        popupWindow.setAnimationStyle(16973826);
        if (Build.VERSION.SDK_INT >= 21) {
            drawable = context.getDrawable(R.drawable.mapbox_popup_window_transparent);
        } else {
            drawable = context.getResources().getDrawable(R.drawable.mapbox_popup_window_transparent);
        }
        popupWindow.setBackgroundDrawable(drawable);
        return popupWindow;
    }
}
