package in.srain.cube.views.ptr;

import in.srain.cube.views.ptr.indicator.PtrIndicator;

public interface PtrUIHandler {
    void onUIPositionChange(PtrFrameLayout ptrFrameLayout, boolean z, byte b, PtrIndicator ptrIndicator);

    void onUIRefreshBegin(PtrFrameLayout ptrFrameLayout);

    void onUIRefreshComplete(PtrFrameLayout ptrFrameLayout);

    void onUIRefreshPrepare(PtrFrameLayout ptrFrameLayout);

    void onUIReset(PtrFrameLayout ptrFrameLayout);

    public static class PtrDefaultUIHandler implements PtrUIHandler {
        public void onUIReset(PtrFrameLayout frame) {
        }

        public void onUIRefreshPrepare(PtrFrameLayout frame) {
        }

        public void onUIRefreshBegin(PtrFrameLayout frame) {
        }

        public void onUIRefreshComplete(PtrFrameLayout frame) {
        }

        public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
        }
    }
}
