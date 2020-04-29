package in.srain.cube.views.ptr;

import android.view.View;

public interface PtrHandler {
    boolean checkCanDoRefresh(PtrFrameLayout ptrFrameLayout, View view, View view2);

    void onRefreshBegin(PtrFrameLayout ptrFrameLayout);
}
