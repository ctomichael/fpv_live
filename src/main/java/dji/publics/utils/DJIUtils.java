package dji.publics.utils;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.provider.MediaStore;
import android.view.Display;
import android.view.WindowManager;
import dji.component.motorlock.model.LockKey;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;
import dji.midware.data.manager.Dpad.DpadProductManager;
import dji.publics.protocol.ResponseBase;
import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

@EXClassNullAway
public class DJIUtils {
    private static Display mDisplay;
    private static String sizeName = "";

    public static class DjiUpgradeStatusModel {
        public ArrayList<DjiFirmwareModel> list;
        public String product;
        public String version;
    }

    public static class DjiFirmwareModel {
        public int compress;
        public String filename;
        public int priority;
    }

    public static int getDimens(Context context, int dimenId) {
        return (int) context.getResources().getDimension(dimenId);
    }

    public static String getSizeName() {
        return sizeName;
    }

    public static void createSizeName(Context context) {
        if (sizeName.equals("")) {
            switch (context.getResources().getConfiguration().screenLayout & 15) {
                case 1:
                    sizeName = "small";
                    break;
                case 2:
                    sizeName = ResponseBase.STRING_NORMAL;
                    break;
                case 3:
                    sizeName = "large";
                    break;
                case 4:
                    sizeName = "xlarge";
                    break;
                default:
                    sizeName = "undefined";
                    break;
            }
            DJILogHelper.getInstance().LOGD("", "sizename=" + sizeName);
        }
    }

    public static void openSystemAlbum(Context context) {
        Intent intent = new Intent("android.intent.action.VIEW", MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        context.startActivity(intent);
    }

    public static int getDisplayRotation(Context ctx) {
        if (mDisplay == null) {
            mDisplay = ((WindowManager) ctx.getSystemService("window")).getDefaultDisplay();
        }
        int rot = mDisplay.getRotation();
        if (DpadProductManager.getInstance().isDpad()) {
            return (rot + 1) % 4;
        }
        return rot;
    }

    public static boolean isUBSConnected(Context context) {
        int plugged = context.registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED")).getIntExtra("plugged", -1);
        if (plugged == 1 || plugged == 2) {
            return true;
        }
        return false;
    }

    public static DjiUpgradeStatusModel getFirmwareUpgradeStatus(Context context, String name) {
        SAXParserFactory f = SAXParserFactory.newInstance();
        XmlHandler h = new XmlHandler();
        try {
            f.newSAXParser().parse(context.openFileInput(name), h);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return h.statusModel;
    }

    static class XmlHandler extends DefaultHandler {
        private String currentEleName;
        private DjiFirmwareModel firmwareModel;
        /* access modifiers changed from: private */
        public DjiUpgradeStatusModel statusModel;

        XmlHandler() {
        }

        public void startDocument() throws SAXException {
            super.startDocument();
            this.statusModel = new DjiUpgradeStatusModel();
            this.statusModel.list = new ArrayList<>();
        }

        public void endDocument() throws SAXException {
            super.endDocument();
        }

        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            super.startElement(uri, localName, qName, attributes);
            if (LockKey.FIRMWARE_UPGRADE_LOCK.equals(localName)) {
                this.firmwareModel = new DjiFirmwareModel();
            } else {
                this.currentEleName = localName;
            }
        }

        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);
            if (LockKey.FIRMWARE_UPGRADE_LOCK.equals(localName)) {
                this.statusModel.list.add(this.firmwareModel);
            } else if (ResponseBase.STRING_FILENAME.equals(localName) || "compress".equals(localName) || "priority".equals(localName) || "product".equals(localName) || "version".equals(localName)) {
                this.currentEleName = "";
            }
        }

        public void characters(char[] ch, int start, int length) throws SAXException {
            super.characters(ch, start, length);
            String str = new String(ch, start, length);
            if (ResponseBase.STRING_FILENAME.equals(this.currentEleName)) {
                this.firmwareModel.filename = str;
            } else if ("compress".equals(this.currentEleName)) {
                this.firmwareModel.compress = Integer.valueOf(str).intValue();
            } else if ("priority".equals(this.currentEleName)) {
                this.firmwareModel.priority = Integer.valueOf(str).intValue();
            } else if ("product".equals(this.currentEleName)) {
                this.statusModel.product = str;
            } else if ("version".equals(this.currentEleName)) {
                this.statusModel.version = str;
            }
        }
    }

    public static void safeClose(Closeable fosfrom) {
        if (fosfrom != null) {
            try {
                fosfrom.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean hasCamera(Context context) {
        if (DpadProductManager.getInstance().isDpad()) {
            return false;
        }
        PackageManager pm = context.getPackageManager();
        if (pm.hasSystemFeature("android.hardware.camera") || pm.hasSystemFeature("android.hardware.camera.front")) {
            return true;
        }
        return false;
    }
}
