package dji.component.accountcenter;

public interface OnConvertRequestListener {
    void onFailure(int i, String str);

    void onSuccess(LoginAuthUrlData loginAuthUrlData);
}
