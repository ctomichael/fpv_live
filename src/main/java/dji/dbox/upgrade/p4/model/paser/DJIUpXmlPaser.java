package dji.dbox.upgrade.p4.model.paser;

import android.util.Xml;
import dji.component.motorlock.model.LockKey;
import dji.dbox.upgrade.p4.constants.DJIUpConstants;
import dji.dbox.upgrade.p4.model.DJIUpCfgModel;
import dji.fieldAnnotation.EXClassNullAway;
import dji.publics.LogReport.base.Fields;
import dji.publics.protocol.ResponseBase;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;

@EXClassNullAway
public class DJIUpXmlPaser {
    private static final String TAG = "DJIUpXmlParser";

    public static DJIUpCfgModel getCfgModel(File file) throws FileNotFoundException {
        InputStream inStream = new FileInputStream(file);
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(inStream, "UTF-8");
            DJIUpCfgModel cfgModel = null;
            ArrayList<DJIUpCfgModel.DJIUpModule> modules = null;
            for (int eventType = parser.getEventType(); eventType != 1; eventType = parser.next()) {
                switch (eventType) {
                    case 0:
                        modules = new ArrayList<>();
                        break;
                    case 2:
                        String name = parser.getName();
                        if (!name.equalsIgnoreCase("device")) {
                            if (cfgModel != null) {
                                if (!name.equalsIgnoreCase(LockKey.FIRMWARE_UPGRADE_LOCK)) {
                                    if (!name.equalsIgnoreCase("release")) {
                                        if (name.equalsIgnoreCase("module")) {
                                            if (cfgModel.modules == null) {
                                                cfgModel.modules = modules;
                                            }
                                            DJIUpCfgModel.DJIUpModule module = new DJIUpCfgModel.DJIUpModule();
                                            module.setId(parser.getAttributeValue(null, ResponseBase.STRING_ID));
                                            module.version = parser.getAttributeValue(null, "version");
                                            module.type = parser.getAttributeValue(null, "type");
                                            module.md5 = parser.getAttributeValue(null, ResponseBase.STRING_MD5);
                                            module.size = Integer.parseInt(parser.getAttributeValue(null, "size"));
                                            module.group = module.translateGroup(parser.getAttributeValue(null, "group"));
                                            module.name = parser.nextText();
                                            if (!module.group.equals(DJIUpCfgModel.DJIFirmwareGroup.TB) && modules != null) {
                                                cfgModel.totalSize += module.size;
                                                modules.add(module);
                                                break;
                                            }
                                        } else {
                                            break;
                                        }
                                    } else {
                                        cfgModel.releaseVersion = parser.getAttributeValue(null, "version");
                                        try {
                                            cfgModel.antirollback = Integer.parseInt(parser.getAttributeValue(null, "antirollback"));
                                        } catch (NumberFormatException e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            cfgModel.antirollbackExt = parser.getAttributeValue(null, "antirollback_ext");
                                        } catch (NumberFormatException e2) {
                                            e2.printStackTrace();
                                        }
                                        try {
                                            cfgModel.enforce = Integer.parseInt(parser.getAttributeValue(null, "enforce"));
                                        } catch (NumberFormatException e3) {
                                            e3.printStackTrace();
                                        }
                                        try {
                                            cfgModel.enforceExt = parser.getAttributeValue(null, "enforce_ext");
                                        } catch (NumberFormatException e4) {
                                            e4.printStackTrace();
                                        }
                                        try {
                                            cfgModel.enforceTime = parser.getAttributeValue(null, "enforce_time");
                                        } catch (NumberFormatException e5) {
                                            e5.printStackTrace();
                                        }
                                        cfgModel.from = parser.getAttributeValue(null, Fields.Dgo_quiz.FROM);
                                        cfgModel.expire = parser.getAttributeValue(null, "expire");
                                        break;
                                    }
                                } else {
                                    break;
                                }
                            } else {
                                break;
                            }
                        } else {
                            cfgModel = new DJIUpCfgModel();
                            cfgModel.deviceId = parser.getAttributeValue(null, ResponseBase.STRING_ID);
                            break;
                        }
                }
            }
            inStream.close();
            return cfgModel;
        } catch (Exception e6) {
            e6.printStackTrace();
            DJIUpConstants.LOGE(TAG, "DJIUpXmlParser e=" + e6.getMessage());
            return null;
        }
    }
}
