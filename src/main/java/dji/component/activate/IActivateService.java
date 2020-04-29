package dji.component.activate;

import android.support.annotation.Nullable;
import dji.component.activate.listener.ActivateFinishListener;
import dji.component.activate.listener.ActivateStatusListener;
import dji.component.activate.model.ActivateStatusEvent;
import dji.component.activate.model.DJICareModel;
import dji.component.activate.model.RealNameStateModel;
import dji.component.djicare.CareResultCallBack;
import dji.midware.data.config.P3.ProductType;
import io.reactivex.Observable;
import io.reactivex.subjects.Subject;

public interface IActivateService {
    public static final String COMPONENT_NAME = "ActivateService";

    void addActivateFinishListener(ActivateFinishListener activateFinishListener);

    void addActiveStatusListener(ActivateStatusListener activateStatusListener);

    @Nullable
    String getActivateDeviceSN();

    ActivateStatusEvent getActiveStatus();

    Subject<Boolean> getAviatorTipStateSubject();

    void getCanShowDJICare(String str, ProductType productType, CareResultCallBack careResultCallBack);

    String getConnectDeviceSN();

    String getDJICareBuyUrl();

    Observable<DJICareModel> getDJICareChangeObservable();

    @Nullable
    DJICareModel getDJICareModel(String str);

    boolean getIsActivating();

    boolean getIsNeedRealName();

    String getLastConnectDeviceSN();

    Subject<RealNameStateModel> getRealNameTipStateSubject();

    void log(String str, Object... objArr);

    void onActivateFinish();

    void removeActivateFinishListener(ActivateFinishListener activateFinishListener);

    void removeActiveStatusListener(ActivateStatusListener activateStatusListener);

    void setActivateDeviceSN(String str);

    void setActiveStatus(ActivateStatusEvent activateStatusEvent);

    void setIsActivating(boolean z);

    void setIsNeedRealName(boolean z);

    void updateDJICareResultModel(DJICareModel dJICareModel);
}
