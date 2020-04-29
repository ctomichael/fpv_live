package dji.dbox.upgrade.p4.statemachine;

import dji.dbox.upgrade.collectpacks.UpBaseCollector;
import dji.dbox.upgrade.p4.constants.DJIUpConstants;
import dji.dbox.upgrade.p4.model.DJIUpCfgModel;
import dji.dbox.upgrade.p4.model.DJIUpListElement;
import dji.dbox.upgrade.p4.model.DJIUpStatus;
import dji.dbox.upgrade.p4.server.DJIUpServerManager;
import dji.dbox.upgrade.p4.utils.DJIUpStatusHelper;
import dji.fieldAnnotation.EXClassNullAway;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@EXClassNullAway
public class DJIUpTarManager {
    private static final String TAG = "DJIUpTarManager";

    private static ArrayList<String> pickModules(DJIUpListElement element, UpBaseCollector collector) {
        collector.initFirmwareGroup();
        ArrayList<String> fileNames = new ArrayList<>();
        Iterator<DJIUpCfgModel.DJIUpModule> it2 = element.cfgModel.modules.iterator();
        while (it2.hasNext()) {
            DJIUpCfgModel.DJIUpModule module = it2.next();
            DJIUpConstants.LOGD(TAG, "pickModules name=" + module.name + " getGroupList=" + collector.getGroupList() + " group=" + module.group);
            if (collector.getGroupList().contains(module.group)) {
                fileNames.add(module.name);
            }
        }
        return fileNames;
    }

    public static void start(DJIUpServerManager.TarCallBack callBack) {
        DJIUpStatus status = DJIUpStatusHelper.getUpgradingStatus();
        DJIUpServerManager serverManager = status.getServerManager();
        UpBaseCollector collector = status.getCollector();
        DJIUpListElement element = status.getChoiceElement();
        if (element == null) {
            DJIUpConstants.LOGD(TAG, "choiceElement element==null");
            callBack.onFailed();
            return;
        }
        DJIUpConstants.LOGD(TAG, "choiceElement code=" + status.getChoiceElement().product_version);
        serverManager.tarPackage(element.cfgModel.releaseVersion, pickModules(element, collector), callBack);
    }

    public static List<String> getUpgradeFiles() {
        DJIUpStatus status = DJIUpStatusHelper.getUpgradingStatus();
        DJIUpServerManager serverManager = status.getServerManager();
        UpBaseCollector collector = status.getCollector();
        DJIUpListElement element = status.getChoiceElement();
        DJIUpConstants.LOGD(TAG, "getUpgradeFiles deviceType=" + status.getProductId());
        if (element == null) {
            DJIUpConstants.LOGD(TAG, "choiceElement element==null");
            return null;
        }
        DJIUpConstants.LOGD(TAG, "choiceElement code=" + status.getChoiceElement().product_version);
        return serverManager.getUpgradeFiles(element.cfgModel.releaseVersion, pickModules(element, collector));
    }

    public static String getTarFilePath() {
        DJIUpServerManager serverManager;
        DJIUpStatus status = DJIUpStatusHelper.getUpgradingStatus();
        return (status == null || status.getServerManager() == null || (serverManager = status.getServerManager()) == null) ? "" : serverManager.getTarFilePath();
    }

    public static String getRollbackTarFilePath() {
        DJIUpServerManager serverManager;
        DJIUpStatus status = DJIUpStatusHelper.getRollBackStatus();
        return (status == null || status.getServerManager() == null || (serverManager = status.getServerManager()) == null) ? "" : serverManager.getTarFilePath();
    }

    public static boolean isTarExsit() {
        return new File(getTarFilePath()).exists();
    }

    public static boolean isRollbackTarExsit() {
        return new File(getRollbackTarFilePath()).exists();
    }
}
