package dji.component.areacode;

public interface DJIAreaCodeCallback {
    void onFailure(int i);

    void onSuccess(String str, AreaCodeStrategy areaCodeStrategy);
}
