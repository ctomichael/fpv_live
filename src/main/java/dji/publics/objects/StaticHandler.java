package dji.publics.objects;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import dji.fieldAnnotation.EXClassNullAway;
import java.lang.ref.WeakReference;

@EXClassNullAway
public final class StaticHandler extends Handler {
    private final Handler.Callback mCallBack;
    private final WeakReference<StaticHoler> mHolderWRef;

    @Deprecated
    public interface StaticHoler {
        boolean isFinished();
    }

    public interface StaticHolder extends StaticHoler, Handler.Callback {
    }

    public StaticHandler(StaticHolder holder) {
        this(Looper.getMainLooper(), holder, holder);
    }

    public StaticHandler(Looper looper, StaticHolder holder) {
        this(looper, holder, holder);
    }

    public StaticHandler(StaticHoler holder, Handler.Callback cb) {
        this(Looper.getMainLooper(), holder, cb);
    }

    public StaticHandler(Looper looper, StaticHoler holder, Handler.Callback cb) {
        super(looper);
        this.mHolderWRef = new WeakReference<>(holder);
        this.mCallBack = cb;
    }

    public void dispatchMessage(Message msg) {
        StaticHoler holder = this.mHolderWRef.get();
        if (holder != null && !holder.isFinished()) {
            super.dispatchMessage(msg);
        }
    }

    public void handleMessage(Message msg) {
        if (this.mCallBack != null) {
            this.mCallBack.handleMessage(msg);
        }
    }
}
