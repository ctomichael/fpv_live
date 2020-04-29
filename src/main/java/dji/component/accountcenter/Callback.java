package dji.component.accountcenter;

public interface Callback {
    void onError(int i, Object obj);

    void onSuccess(Object obj);
}
