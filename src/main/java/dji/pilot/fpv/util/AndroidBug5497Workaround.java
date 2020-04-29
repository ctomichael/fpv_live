package dji.pilot.fpv.util;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import dji.fieldAnnotation.EXClassNullAway;
import org.greenrobot.eventbus.EventBus;

@EXClassNullAway
public class AndroidBug5497Workaround {
    private FrameLayout.LayoutParams frameLayoutParams = ((FrameLayout.LayoutParams) this.mChildOfContent.getLayoutParams());
    private View mChildOfContent;
    private int usableHeightPrevious;

    public enum SoftKeyBoardStateEvent {
        HIDDEN,
        SHOWED
    }

    public static void assistActivity(Activity activity) {
        new AndroidBug5497Workaround(activity);
    }

    private AndroidBug5497Workaround(Activity activity) {
        this.mChildOfContent = ((FrameLayout) activity.findViewById(16908290)).getChildAt(0);
        this.mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            /* class dji.pilot.fpv.util.AndroidBug5497Workaround.AnonymousClass1 */

            public void onGlobalLayout() {
                AndroidBug5497Workaround.this.possiblyResizeChildOfContent();
            }
        });
    }

    /* access modifiers changed from: private */
    public void possiblyResizeChildOfContent() {
        int usableHeightNow = computeUsableHeight();
        if (usableHeightNow != this.usableHeightPrevious) {
            int usableHeightSansKeyboard = this.mChildOfContent.getRootView().getHeight();
            int heightDifference = usableHeightSansKeyboard - usableHeightNow;
            if (heightDifference > usableHeightSansKeyboard / 4) {
                this.frameLayoutParams.height = usableHeightSansKeyboard - heightDifference;
                EventBus.getDefault().post(SoftKeyBoardStateEvent.SHOWED);
            } else {
                this.frameLayoutParams.height = usableHeightSansKeyboard;
                EventBus.getDefault().post(SoftKeyBoardStateEvent.HIDDEN);
            }
            this.mChildOfContent.requestLayout();
            this.usableHeightPrevious = usableHeightNow;
        }
    }

    private int computeUsableHeight() {
        Rect r = new Rect();
        this.mChildOfContent.getWindowVisibleDisplayFrame(r);
        return r.bottom - r.top;
    }
}
