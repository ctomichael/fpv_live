package android.support.v4.widget;

import android.support.annotation.NonNull;
import android.widget.ListView;

public class ListViewAutoScrollHelper extends AutoScrollHelper {
    private final ListView mTarget;

    public ListViewAutoScrollHelper(@NonNull ListView target) {
        super(target);
        this.mTarget = target;
    }

    public void scrollTargetBy(int deltaX, int deltaY) {
        ListViewCompat.scrollListBy(this.mTarget, deltaY);
    }

    public boolean canTargetScrollHorizontally(int direction) {
        return false;
    }

    /* JADX WARNING: Removed duplicated region for block: B:7:0x0028 A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean canTargetScrollVertically(int r11) {
        /*
            r10 = this;
            r7 = 0
            android.widget.ListView r6 = r10.mTarget
            int r3 = r6.getCount()
            if (r3 != 0) goto L_0x000a
        L_0x0009:
            return r7
        L_0x000a:
            int r0 = r6.getChildCount()
            int r1 = r6.getFirstVisiblePosition()
            int r4 = r1 + r0
            if (r11 <= 0) goto L_0x002a
            if (r4 < r3) goto L_0x0028
            int r8 = r0 + -1
            android.view.View r5 = r6.getChildAt(r8)
            int r8 = r5.getBottom()
            int r9 = r6.getHeight()
            if (r8 <= r9) goto L_0x0009
        L_0x0028:
            r7 = 1
            goto L_0x0009
        L_0x002a:
            if (r11 >= 0) goto L_0x0009
            if (r1 > 0) goto L_0x0028
            android.view.View r2 = r6.getChildAt(r7)
            int r8 = r2.getTop()
            if (r8 < 0) goto L_0x0028
            goto L_0x0009
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.widget.ListViewAutoScrollHelper.canTargetScrollVertically(int):boolean");
    }
}
