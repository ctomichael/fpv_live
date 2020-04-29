package dji.component.flysafe.listener;

import dji.component.flysafe.model.FlyfrbStepInfo;
import dji.component.flysafe.model.JNIWarnModelWrappers;

public interface OnFlysafeWarnListener {
    void OnChinaAirportStateChanged(JNIWarnModelWrappers.AirportWarningAreaTakeoffState airportWarningAreaTakeoffState);

    void OnTFRStepChanged(FlyfrbStepInfo flyfrbStepInfo);

    void onCDLandWarning(JNIWarnModelWrappers.ShowCDLWarningWrapper showCDLWarningWrapper);

    void onNormalAlert(JNIWarnModelWrappers.ShowWarningWrapper showWarningWrapper);

    void onNormalTip(JNIWarnModelWrappers.ShowTipsWrapper showTipsWrapper);
}
