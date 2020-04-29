package dji.component.accountcenter.listener;

public interface OnDataResultListener {
    void onFail(int i, int i2, int i3, Object obj);

    void onStart(int i, boolean z, int i2, Object obj);

    void onSuccess(int i, int i2, int i3, Object obj, Object obj2);

    void onUpdate(int i, long j, long j2, int i2, Object obj);
}
