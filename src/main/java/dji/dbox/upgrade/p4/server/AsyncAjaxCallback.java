package dji.dbox.upgrade.p4.server;

import dji.midware.util.BackgroundLooper;
import dji.thirdparty.afinal.http.AjaxCallBack;

public abstract class AsyncAjaxCallback<T> extends AjaxCallBack<T> {
    public abstract void asyncOnFailure(Throwable th, int i, String str);

    public abstract void asyncOnLoading(long j, long j2);

    public abstract void asyncOnStart(boolean z);

    public abstract void asyncOnSuccess(Object obj);

    public void onStart(final boolean b) {
        BackgroundLooper.post(new Runnable() {
            /* class dji.dbox.upgrade.p4.server.AsyncAjaxCallback.AnonymousClass1 */

            public void run() {
                AsyncAjaxCallback.this.asyncOnStart(b);
            }
        });
    }

    public void onLoading(long count, long current) {
        final long j = count;
        final long j2 = current;
        BackgroundLooper.post(new Runnable() {
            /* class dji.dbox.upgrade.p4.server.AsyncAjaxCallback.AnonymousClass2 */

            public void run() {
                AsyncAjaxCallback.this.asyncOnLoading(j, j2);
            }
        });
    }

    public void onSuccess(final T t) {
        BackgroundLooper.post(new Runnable() {
            /* class dji.dbox.upgrade.p4.server.AsyncAjaxCallback.AnonymousClass3 */

            public void run() {
                AsyncAjaxCallback.this.asyncOnSuccess(t);
            }
        });
    }

    public void onFailure(final Throwable throwable, final int i, final String s) {
        BackgroundLooper.post(new Runnable() {
            /* class dji.dbox.upgrade.p4.server.AsyncAjaxCallback.AnonymousClass4 */

            public void run() {
                AsyncAjaxCallback.this.asyncOnFailure(throwable, i, s);
            }
        });
    }
}
