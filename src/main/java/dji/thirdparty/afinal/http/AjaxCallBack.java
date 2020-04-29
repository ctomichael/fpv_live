package dji.thirdparty.afinal.http;

public abstract class AjaxCallBack<T> {
    private boolean progress = true;
    private int rate = 1000;

    public abstract void onFailure(Throwable th, int i, String str);

    public abstract void onLoading(long j, long j2);

    public abstract void onStart(boolean z);

    public abstract void onSuccess(T t);

    public boolean isProgress() {
        return this.progress;
    }

    public int getRate() {
        return this.rate;
    }

    public AjaxCallBack<T> progress(boolean progress2, int rate2) {
        this.progress = progress2;
        this.rate = rate2;
        return this;
    }
}
