package in.srain.cube.views.ptr;

public abstract class PtrUIHandlerHook implements Runnable {
    private static final byte STATUS_IN_HOOK = 1;
    private static final byte STATUS_PREPARE = 0;
    private static final byte STATUS_RESUMED = 2;
    private Runnable mResumeAction;
    private byte mStatus = 0;

    public void takeOver() {
        takeOver(null);
    }

    public void takeOver(Runnable resumeAction) {
        if (resumeAction != null) {
            this.mResumeAction = resumeAction;
        }
        switch (this.mStatus) {
            case 0:
                this.mStatus = 1;
                run();
                return;
            case 1:
            default:
                return;
            case 2:
                resume();
                return;
        }
    }

    public void reset() {
        this.mStatus = 0;
    }

    public void resume() {
        if (this.mResumeAction != null) {
            this.mResumeAction.run();
        }
        this.mStatus = 2;
    }

    public void setResumeAction(Runnable runnable) {
        this.mResumeAction = runnable;
    }
}
