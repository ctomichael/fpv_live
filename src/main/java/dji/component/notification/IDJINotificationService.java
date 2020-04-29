package dji.component.notification;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.view.ViewGroup;
import dji.component.notification.DJITip;

public interface IDJINotificationService {
    public static final String COMPONENT_NAME = "DJINotificationService";

    DJITip.IPriorityBinder addDJITip(@NonNull DJITip dJITip);

    @UiThread
    void registerDJITipContainer(@NonNull Activity activity, @NonNull ViewGroup.LayoutParams layoutParams);

    @UiThread
    void removeDJITip(@NonNull DJITip dJITip);

    void unregisterDJITipContainer(@NonNull Activity activity);
}
