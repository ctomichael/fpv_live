package com.drew.metadata.exif.makernotes;

import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.TagDescriptor;
import java.text.DecimalFormat;
import java.util.HashMap;

public class OlympusEquipmentMakernoteDescriptor extends TagDescriptor<OlympusEquipmentMakernoteDirectory> {
    private static final HashMap<String, String> _olympusExtenderTypes = new HashMap<>();
    private static final HashMap<String, String> _olympusLensTypes = new HashMap<>();

    public OlympusEquipmentMakernoteDescriptor(@NotNull OlympusEquipmentMakernoteDirectory directory) {
        super(directory);
    }

    @Nullable
    public String getDescription(int tagType) {
        switch (tagType) {
            case 0:
                return getEquipmentVersionDescription();
            case 256:
                return getCameraType2Description();
            case 259:
                return getFocalPlaneDiagonalDescription();
            case 260:
                return getBodyFirmwareVersionDescription();
            case 513:
                return getLensTypeDescription();
            case 516:
                return getLensFirmwareVersionDescription();
            case 517:
                return getMaxApertureAtMinFocalDescription();
            case 518:
                return getMaxApertureAtMaxFocalDescription();
            case 522:
                return getMaxApertureDescription();
            case 523:
                return getLensPropertiesDescription();
            case 769:
                return getExtenderDescription();
            case 4096:
                return getFlashTypeDescription();
            case 4097:
                return getFlashModelDescription();
            default:
                return super.getDescription(tagType);
        }
    }

    @Nullable
    public String getEquipmentVersionDescription() {
        return getVersionBytesDescription(0, 4);
    }

    @Nullable
    public String getCameraType2Description() {
        String cameratype = ((OlympusEquipmentMakernoteDirectory) this._directory).getString(256);
        if (cameratype == null) {
            return null;
        }
        return OlympusMakernoteDirectory.OlympusCameraTypes.containsKey(cameratype) ? OlympusMakernoteDirectory.OlympusCameraTypes.get(cameratype) : cameratype;
    }

    @Nullable
    public String getFocalPlaneDiagonalDescription() {
        return ((OlympusEquipmentMakernoteDirectory) this._directory).getString(259) + " mm";
    }

    @Nullable
    public String getBodyFirmwareVersionDescription() {
        Integer value = ((OlympusEquipmentMakernoteDirectory) this._directory).getInteger(260);
        if (value == null) {
            return null;
        }
        String hex = String.format("%04X", value);
        return String.format("%s.%s", hex.substring(0, hex.length() - 3), hex.substring(hex.length() - 3));
    }

    @Nullable
    public String getLensTypeDescription() {
        String str = ((OlympusEquipmentMakernoteDirectory) this._directory).getString(513);
        if (str == null) {
            return null;
        }
        String[] values = str.split(" ");
        if (values.length < 6) {
            return null;
        }
        try {
            int num1 = Integer.parseInt(values[0]);
            int num2 = Integer.parseInt(values[2]);
            int num3 = Integer.parseInt(values[3]);
            return _olympusLensTypes.get(String.format("%X %02X %02X", Integer.valueOf(num1), Integer.valueOf(num2), Integer.valueOf(num3)));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Nullable
    public String getLensFirmwareVersionDescription() {
        Integer value = ((OlympusEquipmentMakernoteDirectory) this._directory).getInteger(516);
        if (value == null) {
            return null;
        }
        String hex = String.format("%04X", value);
        return String.format("%s.%s", hex.substring(0, hex.length() - 3), hex.substring(hex.length() - 3));
    }

    @Nullable
    public String getMaxApertureAtMinFocalDescription() {
        Integer value = ((OlympusEquipmentMakernoteDirectory) this._directory).getInteger(517);
        if (value == null) {
            return null;
        }
        return new DecimalFormat("0.#").format(CalcMaxAperture(value.intValue()));
    }

    @Nullable
    public String getMaxApertureAtMaxFocalDescription() {
        Integer value = ((OlympusEquipmentMakernoteDirectory) this._directory).getInteger(518);
        if (value == null) {
            return null;
        }
        return new DecimalFormat("0.#").format(CalcMaxAperture(value.intValue()));
    }

    @Nullable
    public String getMaxApertureDescription() {
        Integer value = ((OlympusEquipmentMakernoteDirectory) this._directory).getInteger(522);
        if (value == null) {
            return null;
        }
        return new DecimalFormat("0.#").format(CalcMaxAperture(value.intValue()));
    }

    private static double CalcMaxAperture(int value) {
        return Math.pow(Math.sqrt(2.0d), ((double) value) / 256.0d);
    }

    @Nullable
    public String getLensPropertiesDescription() {
        Integer value = ((OlympusEquipmentMakernoteDirectory) this._directory).getInteger(523);
        if (value == null) {
            return null;
        }
        return String.format("0x%04X", value);
    }

    @Nullable
    public String getExtenderDescription() {
        String str = ((OlympusEquipmentMakernoteDirectory) this._directory).getString(769);
        if (str == null) {
            return null;
        }
        String[] values = str.split(" ");
        if (values.length < 6) {
            return null;
        }
        try {
            return _olympusExtenderTypes.get(String.format("%X %02X", Integer.valueOf(Integer.parseInt(values[0])), Integer.valueOf(Integer.parseInt(values[2]))));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Nullable
    public String getFlashTypeDescription() {
        return getIndexedDescription(4096, "None", null, "Simple E-System", "E-System");
    }

    @Nullable
    public String getFlashModelDescription() {
        return getIndexedDescription(4097, "None", "FL-20", "FL-50", "RF-11", "TF-22", "FL-36", "FL-50R", "FL-36R");
    }

    static {
        _olympusLensTypes.put("0 00 00", "None");
        _olympusLensTypes.put("0 01 00", "Olympus Zuiko Digital ED 50mm F2.0 Macro");
        _olympusLensTypes.put("0 01 01", "Olympus Zuiko Digital 40-150mm F3.5-4.5");
        _olympusLensTypes.put("0 01 10", "Olympus M.Zuiko Digital ED 14-42mm F3.5-5.6");
        _olympusLensTypes.put("0 02 00", "Olympus Zuiko Digital ED 150mm F2.0");
        _olympusLensTypes.put("0 02 10", "Olympus M.Zuiko Digital 17mm F2.8 Pancake");
        _olympusLensTypes.put("0 03 00", "Olympus Zuiko Digital ED 300mm F2.8");
        _olympusLensTypes.put("0 03 10", "Olympus M.Zuiko Digital ED 14-150mm F4.0-5.6 [II]");
        _olympusLensTypes.put("0 04 10", "Olympus M.Zuiko Digital ED 9-18mm F4.0-5.6");
        _olympusLensTypes.put("0 05 00", "Olympus Zuiko Digital 14-54mm F2.8-3.5");
        _olympusLensTypes.put("0 05 01", "Olympus Zuiko Digital Pro ED 90-250mm F2.8");
        _olympusLensTypes.put("0 05 10", "Olympus M.Zuiko Digital ED 14-42mm F3.5-5.6 L");
        _olympusLensTypes.put("0 06 00", "Olympus Zuiko Digital ED 50-200mm F2.8-3.5");
        _olympusLensTypes.put("0 06 01", "Olympus Zuiko Digital ED 8mm F3.5 Fisheye");
        _olympusLensTypes.put("0 06 10", "Olympus M.Zuiko Digital ED 40-150mm F4.0-5.6");
        _olympusLensTypes.put("0 07 00", "Olympus Zuiko Digital 11-22mm F2.8-3.5");
        _olympusLensTypes.put("0 07 01", "Olympus Zuiko Digital 18-180mm F3.5-6.3");
        _olympusLensTypes.put("0 07 10", "Olympus M.Zuiko Digital ED 12mm F2.0");
        _olympusLensTypes.put("0 08 01", "Olympus Zuiko Digital 70-300mm F4.0-5.6");
        _olympusLensTypes.put("0 08 10", "Olympus M.Zuiko Digital ED 75-300mm F4.8-6.7");
        _olympusLensTypes.put("0 09 10", "Olympus M.Zuiko Digital 14-42mm F3.5-5.6 II");
        _olympusLensTypes.put("0 10 01", "Kenko Tokina Reflex 300mm F6.3 MF Macro");
        _olympusLensTypes.put("0 10 10", "Olympus M.Zuiko Digital ED 12-50mm F3.5-6.3 EZ");
        _olympusLensTypes.put("0 11 10", "Olympus M.Zuiko Digital 45mm F1.8");
        _olympusLensTypes.put("0 12 10", "Olympus M.Zuiko Digital ED 60mm F2.8 Macro");
        _olympusLensTypes.put("0 13 10", "Olympus M.Zuiko Digital 14-42mm F3.5-5.6 II R");
        _olympusLensTypes.put("0 14 10", "Olympus M.Zuiko Digital ED 40-150mm F4.0-5.6 R");
        _olympusLensTypes.put("0 15 00", "Olympus Zuiko Digital ED 7-14mm F4.0");
        _olympusLensTypes.put("0 15 10", "Olympus M.Zuiko Digital ED 75mm F1.8");
        _olympusLensTypes.put("0 16 10", "Olympus M.Zuiko Digital 17mm F1.8");
        _olympusLensTypes.put("0 17 00", "Olympus Zuiko Digital Pro ED 35-100mm F2.0");
        _olympusLensTypes.put("0 18 00", "Olympus Zuiko Digital 14-45mm F3.5-5.6");
        _olympusLensTypes.put("0 18 10", "Olympus M.Zuiko Digital ED 75-300mm F4.8-6.7 II");
        _olympusLensTypes.put("0 19 10", "Olympus M.Zuiko Digital ED 12-40mm F2.8 Pro");
        _olympusLensTypes.put("0 20 00", "Olympus Zuiko Digital 35mm F3.5 Macro");
        _olympusLensTypes.put("0 20 10", "Olympus M.Zuiko Digital ED 40-150mm F2.8 Pro");
        _olympusLensTypes.put("0 21 10", "Olympus M.Zuiko Digital ED 14-42mm F3.5-5.6 EZ");
        _olympusLensTypes.put("0 22 00", "Olympus Zuiko Digital 17.5-45mm F3.5-5.6");
        _olympusLensTypes.put("0 22 10", "Olympus M.Zuiko Digital 25mm F1.8");
        _olympusLensTypes.put("0 23 00", "Olympus Zuiko Digital ED 14-42mm F3.5-5.6");
        _olympusLensTypes.put("0 23 10", "Olympus M.Zuiko Digital ED 7-14mm F2.8 Pro");
        _olympusLensTypes.put("0 24 00", "Olympus Zuiko Digital ED 40-150mm F4.0-5.6");
        _olympusLensTypes.put("0 24 10", "Olympus M.Zuiko Digital ED 300mm F4.0 IS Pro");
        _olympusLensTypes.put("0 25 10", "Olympus M.Zuiko Digital ED 8mm F1.8 Fisheye Pro");
        _olympusLensTypes.put("0 30 00", "Olympus Zuiko Digital ED 50-200mm F2.8-3.5 SWD");
        _olympusLensTypes.put("0 31 00", "Olympus Zuiko Digital ED 12-60mm F2.8-4.0 SWD");
        _olympusLensTypes.put("0 32 00", "Olympus Zuiko Digital ED 14-35mm F2.0 SWD");
        _olympusLensTypes.put("0 33 00", "Olympus Zuiko Digital 25mm F2.8");
        _olympusLensTypes.put("0 34 00", "Olympus Zuiko Digital ED 9-18mm F4.0-5.6");
        _olympusLensTypes.put("0 35 00", "Olympus Zuiko Digital 14-54mm F2.8-3.5 II");
        _olympusLensTypes.put("1 01 00", "Sigma 18-50mm F3.5-5.6 DC");
        _olympusLensTypes.put("1 01 10", "Sigma 30mm F2.8 EX DN");
        _olympusLensTypes.put("1 02 00", "Sigma 55-200mm F4.0-5.6 DC");
        _olympusLensTypes.put("1 02 10", "Sigma 19mm F2.8 EX DN");
        _olympusLensTypes.put("1 03 00", "Sigma 18-125mm F3.5-5.6 DC");
        _olympusLensTypes.put("1 03 10", "Sigma 30mm F2.8 DN | A");
        _olympusLensTypes.put("1 04 00", "Sigma 18-125mm F3.5-5.6 DC");
        _olympusLensTypes.put("1 04 10", "Sigma 19mm F2.8 DN | A");
        _olympusLensTypes.put("1 05 00", "Sigma 30mm F1.4 EX DC HSM");
        _olympusLensTypes.put("1 05 10", "Sigma 60mm F2.8 DN | A");
        _olympusLensTypes.put("1 06 00", "Sigma APO 50-500mm F4.0-6.3 EX DG HSM");
        _olympusLensTypes.put("1 07 00", "Sigma Macro 105mm F2.8 EX DG");
        _olympusLensTypes.put("1 08 00", "Sigma APO Macro 150mm F2.8 EX DG HSM");
        _olympusLensTypes.put("1 09 00", "Sigma 18-50mm F2.8 EX DC Macro");
        _olympusLensTypes.put("1 10 00", "Sigma 24mm F1.8 EX DG Aspherical Macro");
        _olympusLensTypes.put("1 11 00", "Sigma APO 135-400mm F4.5-5.6 DG");
        _olympusLensTypes.put("1 12 00", "Sigma APO 300-800mm F5.6 EX DG HSM");
        _olympusLensTypes.put("1 13 00", "Sigma 30mm F1.4 EX DC HSM");
        _olympusLensTypes.put("1 14 00", "Sigma APO 50-500mm F4.0-6.3 EX DG HSM");
        _olympusLensTypes.put("1 15 00", "Sigma 10-20mm F4.0-5.6 EX DC HSM");
        _olympusLensTypes.put("1 16 00", "Sigma APO 70-200mm F2.8 II EX DG Macro HSM");
        _olympusLensTypes.put("1 17 00", "Sigma 50mm F1.4 EX DG HSM");
        _olympusLensTypes.put("2 01 00", "Leica D Vario Elmarit 14-50mm F2.8-3.5 Asph.");
        _olympusLensTypes.put("2 01 10", "Lumix G Vario 14-45mm F3.5-5.6 Asph. Mega OIS");
        _olympusLensTypes.put("2 02 00", "Leica D Summilux 25mm F1.4 Asph.");
        _olympusLensTypes.put("2 02 10", "Lumix G Vario 45-200mm F4.0-5.6 Mega OIS");
        _olympusLensTypes.put("2 03 00", "Leica D Vario Elmar 14-50mm F3.8-5.6 Asph. Mega OIS");
        _olympusLensTypes.put("2 03 01", "Leica D Vario Elmar 14-50mm F3.8-5.6 Asph.");
        _olympusLensTypes.put("2 03 10", "Lumix G Vario HD 14-140mm F4.0-5.8 Asph. Mega OIS");
        _olympusLensTypes.put("2 04 00", "Leica D Vario Elmar 14-150mm F3.5-5.6");
        _olympusLensTypes.put("2 04 10", "Lumix G Vario 7-14mm F4.0 Asph.");
        _olympusLensTypes.put("2 05 10", "Lumix G 20mm F1.7 Asph.");
        _olympusLensTypes.put("2 06 10", "Leica DG Macro-Elmarit 45mm F2.8 Asph. Mega OIS");
        _olympusLensTypes.put("2 07 10", "Lumix G Vario 14-42mm F3.5-5.6 Asph. Mega OIS");
        _olympusLensTypes.put("2 08 10", "Lumix G Fisheye 8mm F3.5");
        _olympusLensTypes.put("2 09 10", "Lumix G Vario 100-300mm F4.0-5.6 Mega OIS");
        _olympusLensTypes.put("2 10 10", "Lumix G 14mm F2.5 Asph.");
        _olympusLensTypes.put("2 11 10", "Lumix G 12.5mm F12 3D");
        _olympusLensTypes.put("2 12 10", "Leica DG Summilux 25mm F1.4 Asph.");
        _olympusLensTypes.put("2 13 10", "Lumix G X Vario PZ 45-175mm F4.0-5.6 Asph. Power OIS");
        _olympusLensTypes.put("2 14 10", "Lumix G X Vario PZ 14-42mm F3.5-5.6 Asph. Power OIS");
        _olympusLensTypes.put("2 15 10", "Lumix G X Vario 12-35mm F2.8 Asph. Power OIS");
        _olympusLensTypes.put("2 16 10", "Lumix G Vario 45-150mm F4.0-5.6 Asph. Mega OIS");
        _olympusLensTypes.put("2 17 10", "Lumix G X Vario 35-100mm F2.8 Power OIS");
        _olympusLensTypes.put("2 18 10", "Lumix G Vario 14-42mm F3.5-5.6 II Asph. Mega OIS");
        _olympusLensTypes.put("2 19 10", "Lumix G Vario 14-140mm F3.5-5.6 Asph. Power OIS");
        _olympusLensTypes.put("2 20 10", "Lumix G Vario 12-32mm F3.5-5.6 Asph. Mega OIS");
        _olympusLensTypes.put("2 21 10", "Leica DG Nocticron 42.5mm F1.2 Asph. Power OIS");
        _olympusLensTypes.put("2 22 10", "Leica DG Summilux 15mm F1.7 Asph.");
        _olympusLensTypes.put("2 24 10", "Lumix G Macro 30mm F2.8 Asph. Mega OIS");
        _olympusLensTypes.put("2 25 10", "Lumix G 42.5mm F1.7 Asph. Power OIS");
        _olympusLensTypes.put("3 01 00", "Leica D Vario Elmarit 14-50mm F2.8-3.5 Asph.");
        _olympusLensTypes.put("3 02 00", "Leica D Summilux 25mm F1.4 Asph.");
        _olympusLensTypes.put("5 01 10", "Tamron 14-150mm F3.5-5.8 Di III");
        _olympusExtenderTypes.put("0 00", "None");
        _olympusExtenderTypes.put("0 04", "Olympus Zuiko Digital EC-14 1.4x Teleconverter");
        _olympusExtenderTypes.put("0 08", "Olympus EX-25 Extension Tube");
        _olympusExtenderTypes.put("0 10", "Olympus Zuiko Digital EC-20 2.0x Teleconverter");
    }
}
