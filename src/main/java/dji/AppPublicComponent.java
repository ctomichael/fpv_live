package dji;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponent;
import dji.component.apppublic.IAppPublic;
import dji.pilot.fpv.control.DJIGenSettingDataManager;

public class AppPublicComponent implements IComponent {
    public String getName() {
        return IAppPublic.COMPONENT_NAME;
    }

    public boolean onCall(CC cc) {
        String actionName = cc.getActionName();
        char c = 65535;
        switch (actionName.hashCode()) {
            case 1157744955:
                if (actionName.equals(IAppPublic.ACTION_CHANGE_HERE_AVAILABILITY)) {
                    c = 3;
                    break;
                }
                break;
            case 1425689018:
                if (actionName.equals(IAppPublic.ACTION_CHANGE_MAPBOX_AVAILABILITY)) {
                    c = 0;
                    break;
                }
                break;
            case 1685442148:
                if (actionName.equals(IAppPublic.ACTION_GET_MAPBOX_AVAILABLE)) {
                    c = 1;
                    break;
                }
                break;
            case 1779769263:
                if (actionName.equals("isHereAvailable")) {
                    c = 2;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                DJIGenSettingDataManager.getInstance().changeMapboxAvailability(((Boolean) cc.getParamItem(IAppPublic.PARAM_KEY_MAPBOX_AVAILABILITY)).booleanValue());
                CC.sendCCResult(cc.getCallId(), CCResult.success());
                break;
            case 1:
                CC.sendCCResult(cc.getCallId(), CCResult.success(IAppPublic.PARAM_KEY_IS_MAPBOX_AVAILABLE, Boolean.valueOf(DJIGenSettingDataManager.getInstance().isMapboxAvailable())));
                break;
            case 2:
                CC.sendCCResult(cc.getCallId(), CCResult.success("isHereAvailable", Boolean.valueOf(DJIGenSettingDataManager.getInstance().isHereAvailable())));
                break;
            case 3:
                DJIGenSettingDataManager.getInstance().changeHereAvailability(((Boolean) cc.getParamItem(IAppPublic.PARAM_KEY_HERE_AVAILABILITY)).booleanValue());
                CC.sendCCResult(cc.getCallId(), CCResult.success());
                break;
            default:
                CC.sendCCResult(cc.getCallId(), CCResult.error("not find action in AppPublicComponent"));
                break;
        }
        return false;
    }
}
