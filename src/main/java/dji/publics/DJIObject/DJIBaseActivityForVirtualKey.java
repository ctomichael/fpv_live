package dji.publics.DJIObject;

import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.Window;
import com.dji.frame.util.V_AppUtils;
import dji.thirdparty.afinal.FinalActivity;

public class DJIBaseActivityForVirtualKey extends FinalActivity {
    private Handler handler = new Handler(new Handler.Callback() {
        /* class dji.publics.DJIObject.DJIBaseActivityForVirtualKey.AnonymousClass1 */

        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    DJIBaseActivityForVirtualKey.this.enter(DJIBaseActivityForVirtualKey.this.window);
                    return false;
                default:
                    return false;
            }
        }
    });
    protected boolean isVisible;
    protected Window window;

    /* access modifiers changed from: protected */
    public void enter(Window window2) {
        V_AppUtils.enter(window2);
    }

    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        this.window = getWindow();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        enter(this.window);
        this.isVisible = true;
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        this.isVisible = false;
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == 0) {
            enter(this.window);
        }
        return super.dispatchTouchEvent(ev);
    }

    public void onEventMainThread(V_AppUtils.DJI_SYS_UI_EVENT event) {
        if (this.isVisible) {
            switch (event) {
                case HIDE:
                    enter(this.window);
                    return;
                case HIDE_DELAY:
                    this.handler.sendEmptyMessageDelayed(3, 2000);
                    return;
                default:
                    return;
            }
        }
    }
}
