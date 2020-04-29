package dji.common.util;

import dji.common.error.DJIError;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class CommonCallbacks {

    public interface CompletionCallbackWith<T> {
        void onFailure(DJIError dJIError);

        void onSuccess(T t);
    }

    public interface CompletionCallbackWithTwoParam<X, Y> {
        void onFailure(DJIError dJIError);

        void onSuccess(X x, Y y);
    }

    public interface CompletionCallback {
        void onResult(DJIError dJIError);
    }
}
