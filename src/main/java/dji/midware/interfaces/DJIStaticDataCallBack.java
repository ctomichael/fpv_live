package dji.midware.interfaces;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import java.lang.ref.WeakReference;

@EXClassNullAway
public abstract class DJIStaticDataCallBack<T> implements DJIDataCallBack {
    protected final WeakReference<T> mWeakReference;

    public DJIStaticDataCallBack(T weakReference) {
        this.mWeakReference = new WeakReference<>(weakReference);
    }

    public void onSuccess(Object model) {
        if (this.mWeakReference.get() == null) {
        }
    }

    public void onFailure(Ccode ccode) {
        if (this.mWeakReference.get() == null) {
        }
    }
}
