package dji.component.areacode;

import android.support.annotation.Nullable;

public interface IAreaCodeService {
    public static final String COMPONENT_NAME = "AreaCodeService";

    void addAreaCodeChangeListener(IAreaCodeChangeListener iAreaCodeChangeListener);

    void destroy();

    void getAreaCodeAsync(DJIAreaCodeCallback dJIAreaCodeCallback);

    @Nullable
    DJIAreaCodeEvent getAreaCodeEvent();

    String getAreaCodeFromLocal();

    String getLastCity();

    String getLastProvince();

    void initGpsModule();

    boolean isChina();

    boolean isHongKong();

    boolean isMacau();

    void removeAreaCodeChangeListener(IAreaCodeChangeListener iAreaCodeChangeListener);

    void setACConfig(String str, boolean z);

    void updateAreaCode(AreaCodeStrategy areaCodeStrategy, String str);
}
