package dji.component.djicare;

public interface CareResultCallBack {
    void onFailure(Throwable th, int i, String str);

    void onSuccess(String str);

    void onTaskFailure();
}
