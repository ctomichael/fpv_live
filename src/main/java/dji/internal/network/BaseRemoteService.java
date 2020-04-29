package dji.internal.network;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public abstract class BaseRemoteService {

    public interface SDKRemoteServiceCallback {
        void onFailure();

        void onSuccess(Object obj);
    }
}
