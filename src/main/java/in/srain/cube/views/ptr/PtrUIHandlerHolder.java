package in.srain.cube.views.ptr;

import in.srain.cube.views.ptr.indicator.PtrIndicator;

class PtrUIHandlerHolder implements PtrUIHandler {
    private PtrUIHandler mHandler;
    private PtrUIHandlerHolder mNext;

    private boolean contains(PtrUIHandler handler) {
        return this.mHandler != null && this.mHandler == handler;
    }

    private PtrUIHandlerHolder() {
    }

    public boolean hasHandler() {
        return this.mHandler != null;
    }

    private PtrUIHandler getHandler() {
        return this.mHandler;
    }

    public static void addHandler(PtrUIHandlerHolder head, PtrUIHandler handler) {
        if (handler != null && head != null) {
            if (head.mHandler == null) {
                head.mHandler = handler;
                return;
            }
            for (PtrUIHandlerHolder current = head; !current.contains(handler); current = current.mNext) {
                if (current.mNext == null) {
                    PtrUIHandlerHolder newHolder = new PtrUIHandlerHolder();
                    newHolder.mHandler = handler;
                    current.mNext = newHolder;
                    return;
                }
            }
        }
    }

    public static PtrUIHandlerHolder create() {
        return new PtrUIHandlerHolder();
    }

    public static PtrUIHandlerHolder removeHandler(PtrUIHandlerHolder head, PtrUIHandler handler) {
        if (head == null || handler == null || head.mHandler == null) {
            return head;
        }
        PtrUIHandlerHolder current = head;
        PtrUIHandlerHolder pre = null;
        do {
            if (!current.contains(handler)) {
                pre = current;
                current = current.mNext;
                continue;
            } else if (pre == null) {
                head = current.mNext;
                current.mNext = null;
                current = head;
                continue;
            } else {
                pre.mNext = current.mNext;
                current.mNext = null;
                current = pre.mNext;
                continue;
            }
        } while (current != null);
        if (head == null) {
            return new PtrUIHandlerHolder();
        }
        return head;
    }

    public void onUIReset(PtrFrameLayout frame) {
        PtrUIHandlerHolder current = this;
        do {
            PtrUIHandler handler = current.getHandler();
            if (handler != null) {
                handler.onUIReset(frame);
            }
            current = current.mNext;
        } while (current != null);
    }

    public void onUIRefreshPrepare(PtrFrameLayout frame) {
        if (hasHandler()) {
            PtrUIHandlerHolder current = this;
            do {
                PtrUIHandler handler = current.getHandler();
                if (handler != null) {
                    handler.onUIRefreshPrepare(frame);
                }
                current = current.mNext;
            } while (current != null);
        }
    }

    public void onUIRefreshBegin(PtrFrameLayout frame) {
        PtrUIHandlerHolder current = this;
        do {
            PtrUIHandler handler = current.getHandler();
            if (handler != null) {
                handler.onUIRefreshBegin(frame);
            }
            current = current.mNext;
        } while (current != null);
    }

    public void onUIRefreshComplete(PtrFrameLayout frame) {
        PtrUIHandlerHolder current = this;
        do {
            PtrUIHandler handler = current.getHandler();
            if (handler != null) {
                handler.onUIRefreshComplete(frame);
            }
            current = current.mNext;
        } while (current != null);
    }

    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
        PtrUIHandlerHolder current = this;
        do {
            PtrUIHandler handler = current.getHandler();
            if (handler != null) {
                handler.onUIPositionChange(frame, isUnderTouch, status, ptrIndicator);
            }
            current = current.mNext;
        } while (current != null);
    }
}
