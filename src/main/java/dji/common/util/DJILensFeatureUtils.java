package dji.common.util;

import android.util.SparseArray;
import com.drew.metadata.exif.makernotes.FujifilmMakernoteDirectory;
import com.drew.metadata.exif.makernotes.OlympusMakernoteDirectory;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class DJILensFeatureUtils {
    private static final String DEFAUL_PRODUCTNAME = "Unknown";
    private static final int MEMBERID_DJI = 8;
    private static final int MEMBERID_JKIMAGING = 7;
    private static final int MEMBERID_KENKO_TOKINA = 6;
    private static final int MEMBERID_OLYMPUS = 0;
    private static final int MEMBERID_PANASONIC2 = 2;
    private static final int MEMBERID_PANASONIC3 = 3;
    private static final int MEMBERID_SIGMA = 1;
    private static final int MEMBERID_TAMRON = 5;
    private static final SparseArray<SparseArray<String>> cameraInfoSpArray = new SparseArray<>();

    private static int generateKey(int modelId, int versionId) {
        return (modelId << 16) & versionId;
    }

    public static String getProductName(int memberId, int modelId, int versionId) {
        SparseArray<String> sp = cameraInfoSpArray.get(modelId);
        if (sp == null) {
            if (8 == memberId) {
                sp = loadDJISp();
            } else if (7 == memberId) {
                sp = loadJKImagingSp();
            } else if (6 == memberId) {
                sp = loadKenkoTokinaSp();
            } else if (5 == memberId) {
                sp = loadTamronSp();
            } else if (3 == memberId) {
                sp = loadPanasonic3Sp();
            } else if (2 == memberId) {
                sp = loadPanasonic2Sp();
            } else if (1 == memberId) {
                sp = loadSigmaSp();
            } else if (memberId == 0) {
                sp = loadOlympusSp();
            }
        }
        if (sp != null) {
            return sp.get(generateKey(modelId, versionId), "Unknown");
        }
        return "Unknown";
    }

    private static SparseArray<String> loadOlympusSp() {
        SparseArray<String> sp = new SparseArray<>();
        sp.put(generateKey(4097, 0), "M.ZUIKO DIGITAL ED 14-42mm F3.5-5.6");
        sp.put(generateKey(OlympusMakernoteDirectory.TAG_SENSOR_TEMPERATURE, 0), "M.ZUIKO DIGITAL ED 12mm F2.0");
        sp.put(generateKey(4113, 0), "M.ZUIKO DIGITAL 45mm F1.8");
        sp.put(generateKey(4118, 0), "M.ZUIKO DIGITAL 17mm F1.8");
        sp.put(generateKey(4129, 0), "M.ZUIKO DIGITAL ED 14-42mm F3.5-5.6 EZ");
        sp.put(generateKey(OlympusMakernoteDirectory.TAG_INTERNAL_FLASH_AE2, 0), "M.ZUIKO DIGITAL 25mm F1.8");
        sp.put(generateKey(4131, 0), "M.ZUIKO DIGITAL ED 7-14mm F2.8 PRO");
        sp.put(generateKey(OlympusMakernoteDirectory.TAG_EXTERNAL_FLASH_G_VALUE, 0), "M.ZUIKO DIGITAL ED 8mm F1.8 Fisheye");
        cameraInfoSpArray.put(0, sp);
        return sp;
    }

    private static SparseArray<String> loadSigmaSp() {
        SparseArray<String> sp = new SparseArray<>();
        sp.put(generateKey(4099, 0), "SIGMA 30mm F2.8 DN");
        sp.put(generateKey(4100, 0), "SIGMA 19mm F2.8 DN");
        sp.put(generateKey(4101, 0), "SIGMA 60mm F2.8 DN");
        cameraInfoSpArray.put(1, sp);
        return sp;
    }

    private static SparseArray<String> loadPanasonic2Sp() {
        SparseArray<String> sp = new SparseArray<>();
        sp.put(generateKey(4101, 4096), "LUMIX G 20mm F1.7");
        sp.put(generateKey(4101, FujifilmMakernoteDirectory.TAG_AUTO_BRACKETING), "LUMIX G 20mm F1.7 II");
        sp.put(generateKey(OlympusMakernoteDirectory.TAG_COLOR_TEMPERATURE_RG, 4096), "LUMIX G VARIO PZ 14-42mm/F3.5-5.6");
        sp.put(generateKey(4131, 4096), "LUMIX G VARIO 35-100mm/F4.0-5.6");
        sp.put(generateKey(OlympusMakernoteDirectory.TAG_INTERNAL_FLASH_TABLE, 4096), "LUMIX G MACRO 30mm/F2.8");
        sp.put(generateKey(OlympusMakernoteDirectory.TAG_EXTERNAL_FLASH_G_VALUE, 4096), "LUMIX G 42.5mm/F1.7");
        sp.put(generateKey(OlympusMakernoteDirectory.TAG_EXTERNAL_FLASH_BOUNCE, 4096), "LUMIX G 25mm/F1.7");
        cameraInfoSpArray.put(2, sp);
        return sp;
    }

    private static SparseArray<String> loadPanasonic3Sp() {
        SparseArray<String> sp = new SparseArray<>();
        sp.put(generateKey(2, 4096), "LEICA D SUMMILUX 25mm F1.4 ASPH");
        cameraInfoSpArray.put(3, sp);
        return sp;
    }

    private static SparseArray<String> loadTamronSp() {
        SparseArray<String> sp = new SparseArray<>();
        sp.put(generateKey(4097, 1), "14-150mm F/3.5-5.8 Di IIII C 001");
        sp.put(generateKey(4098, 1), "14-150mm F/3.5-5.8 Di IIII C 001");
        cameraInfoSpArray.put(5, sp);
        return sp;
    }

    private static SparseArray<String> loadKenkoTokinaSp() {
        SparseArray<String> sp = new SparseArray<>();
        sp.put(generateKey(4097, 0), "Reflex 300mm F6.3 MF Macro");
        cameraInfoSpArray.put(6, sp);
        return sp;
    }

    private static SparseArray<String> loadJKImagingSp() {
        SparseArray<String> sp = new SparseArray<>();
        sp.put(generateKey(4097, 4096), "PIXPRO SZ 12-45/F3.5-6.3 AF");
        sp.put(generateKey(4098, 4096), "PIXPRO SZ 42.5-160/F3.9-5.9 AF");
        cameraInfoSpArray.put(7, sp);
        return sp;
    }

    private static SparseArray<String> loadDJISp() {
        SparseArray<String> sp = new SparseArray<>();
        sp.put(generateKey(4097, 0), "DJI MFT 15mm F1.7 ASPH");
        cameraInfoSpArray.put(8, sp);
        return sp;
    }
}
