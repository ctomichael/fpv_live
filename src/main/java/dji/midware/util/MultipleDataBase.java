package dji.midware.util;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@EXClassNullAway
public class MultipleDataBase implements DJIDataCallBack {
    public final String TAG = "MultipleDataBase";
    private Callback cb;
    private int index = 0;
    private DJIDataSyncListener[] listeners;

    public interface Callback {
        void onFails(int i, Ccode ccode);

        void onSuccess();
    }

    public void onSuccess(Object model) {
        this.index++;
        if (this.index < this.listeners.length) {
            this.listeners[this.index].start(this);
        } else if (this.cb != null) {
            this.cb.onSuccess();
        }
    }

    public void onFailure(Ccode ccode) {
        if (this.cb != null) {
            this.cb.onFails(this.index, ccode);
        }
    }

    public MultipleDataBase(DJIDataSyncListener... listeners2) {
        this.listeners = listeners2;
    }

    public void start(Callback cb2) {
        if (this.listeners != null && this.listeners.length > 0) {
            this.cb = cb2;
            this.listeners[this.index].start(this);
        } else if (cb2 != null) {
            cb2.onFails(-1, null);
        }
    }
}
