package com.dji.findmydrone.ui;

import android.content.Intent;
import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponent;
import dji.component.findmydrone.IFindMyDroneUIComponent;

public class FindMyDroneUIComponent implements IComponent, IFindMyDroneUIComponent {
    public String getName() {
        return IFindMyDroneUIComponent.COMPONENT_NAME;
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{android.content.Intent.putExtra(java.lang.String, boolean):android.content.Intent}
     arg types: [java.lang.String, int]
     candidates:
      ClspMth{android.content.Intent.putExtra(java.lang.String, int):android.content.Intent}
      ClspMth{android.content.Intent.putExtra(java.lang.String, java.lang.String[]):android.content.Intent}
      ClspMth{android.content.Intent.putExtra(java.lang.String, int[]):android.content.Intent}
      ClspMth{android.content.Intent.putExtra(java.lang.String, double):android.content.Intent}
      ClspMth{android.content.Intent.putExtra(java.lang.String, char):android.content.Intent}
      ClspMth{android.content.Intent.putExtra(java.lang.String, boolean[]):android.content.Intent}
      ClspMth{android.content.Intent.putExtra(java.lang.String, byte):android.content.Intent}
      ClspMth{android.content.Intent.putExtra(java.lang.String, android.os.Bundle):android.content.Intent}
      ClspMth{android.content.Intent.putExtra(java.lang.String, float):android.content.Intent}
      ClspMth{android.content.Intent.putExtra(java.lang.String, java.lang.CharSequence[]):android.content.Intent}
      ClspMth{android.content.Intent.putExtra(java.lang.String, java.lang.CharSequence):android.content.Intent}
      ClspMth{android.content.Intent.putExtra(java.lang.String, long[]):android.content.Intent}
      ClspMth{android.content.Intent.putExtra(java.lang.String, long):android.content.Intent}
      ClspMth{android.content.Intent.putExtra(java.lang.String, short):android.content.Intent}
      ClspMth{android.content.Intent.putExtra(java.lang.String, android.os.Parcelable[]):android.content.Intent}
      ClspMth{android.content.Intent.putExtra(java.lang.String, java.io.Serializable):android.content.Intent}
      ClspMth{android.content.Intent.putExtra(java.lang.String, double[]):android.content.Intent}
      ClspMth{android.content.Intent.putExtra(java.lang.String, android.os.Parcelable):android.content.Intent}
      ClspMth{android.content.Intent.putExtra(java.lang.String, float[]):android.content.Intent}
      ClspMth{android.content.Intent.putExtra(java.lang.String, byte[]):android.content.Intent}
      ClspMth{android.content.Intent.putExtra(java.lang.String, java.lang.String):android.content.Intent}
      ClspMth{android.content.Intent.putExtra(java.lang.String, short[]):android.content.Intent}
      ClspMth{android.content.Intent.putExtra(java.lang.String, char[]):android.content.Intent}
      ClspMth{android.content.Intent.putExtra(java.lang.String, boolean):android.content.Intent} */
    public boolean onCall(CC cc) {
        String actionName = cc.getActionName();
        char c = 65535;
        switch (actionName.hashCode()) {
            case -1226157500:
                if (actionName.equals(IFindMyDroneUIComponent.ACTIVITY_FIND_MY_DRONE)) {
                    c = 0;
                    break;
                }
                break;
            case 635366715:
                if (actionName.equals(IFindMyDroneUIComponent.ACTIVITY_FIND_MY_DRONE_FROM_FPV)) {
                    c = 1;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                cc.getContext().startActivity(new Intent(cc.getContext(), FindMyDroneActivity.class));
                CC.sendCCResult(cc.getCallId(), CCResult.success());
                break;
            case 1:
                Intent findMyDroneWithoutCamera = new Intent(cc.getContext(), FindMyDroneActivity.class);
                findMyDroneWithoutCamera.putExtra(IFindMyDroneUIComponent.ACTIVITY_FIND_MY_DRONE_FROM_FPV, false);
                cc.getContext().startActivity(findMyDroneWithoutCamera);
                CC.sendCCResult(cc.getCallId(), CCResult.success());
                break;
            default:
                CC.sendCCResult(cc.getCallId(), CCResult.error("unsupported action:" + cc.getActionName()));
                break;
        }
        return false;
    }
}
