package com.drew.metadata.icc;

import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Directory;
import java.util.HashMap;

public class IccDirectory extends Directory {
    public static final int TAG_APPLE_MULTI_LANGUAGE_PROFILE_NAME = 1685283693;
    public static final int TAG_CMM_FLAGS = 44;
    public static final int TAG_CMM_TYPE = 4;
    public static final int TAG_COLOR_SPACE = 16;
    public static final int TAG_DEVICE_ATTR = 56;
    public static final int TAG_DEVICE_MAKE = 48;
    public static final int TAG_DEVICE_MODEL = 52;
    public static final int TAG_PLATFORM = 40;
    public static final int TAG_PROFILE_BYTE_COUNT = 0;
    public static final int TAG_PROFILE_CLASS = 12;
    public static final int TAG_PROFILE_CONNECTION_SPACE = 20;
    public static final int TAG_PROFILE_CREATOR = 80;
    public static final int TAG_PROFILE_DATETIME = 24;
    public static final int TAG_PROFILE_VERSION = 8;
    public static final int TAG_RENDERING_INTENT = 64;
    public static final int TAG_SIGNATURE = 36;
    public static final int TAG_TAG_A2B0 = 1093812784;
    public static final int TAG_TAG_A2B1 = 1093812785;
    public static final int TAG_TAG_A2B2 = 1093812786;
    public static final int TAG_TAG_B2A0 = 1110589744;
    public static final int TAG_TAG_B2A1 = 1110589745;
    public static final int TAG_TAG_B2A2 = 1110589746;
    public static final int TAG_TAG_COUNT = 128;
    public static final int TAG_TAG_bTRC = 1649693251;
    public static final int TAG_TAG_bXYZ = 1649957210;
    public static final int TAG_TAG_bfd = 1650877472;
    public static final int TAG_TAG_bkpt = 1651208308;
    public static final int TAG_TAG_calt = 1667329140;
    public static final int TAG_TAG_chad = 1667785060;
    public static final int TAG_TAG_chrm = 1667789421;
    public static final int TAG_TAG_cprt = 1668313716;
    public static final int TAG_TAG_crdi = 1668441193;
    public static final int TAG_TAG_desc = 1684370275;
    public static final int TAG_TAG_devs = 1684371059;
    public static final int TAG_TAG_dmdd = 1684890724;
    public static final int TAG_TAG_dmnd = 1684893284;
    public static final int TAG_TAG_gTRC = 1733579331;
    public static final int TAG_TAG_gXYZ = 1733843290;
    public static final int TAG_TAG_gamt = 1734438260;
    public static final int TAG_TAG_kTRC = 1800688195;
    public static final int TAG_TAG_lumi = 1819635049;
    public static final int TAG_TAG_meas = 1835360627;
    public static final int TAG_TAG_ncl2 = 1852009522;
    public static final int TAG_TAG_ncol = 1852010348;
    public static final int TAG_TAG_pre0 = 1886545200;
    public static final int TAG_TAG_pre1 = 1886545201;
    public static final int TAG_TAG_pre2 = 1886545202;
    public static final int TAG_TAG_ps2i = 1886597737;
    public static final int TAG_TAG_ps2s = 1886597747;
    public static final int TAG_TAG_psd0 = 1886610480;
    public static final int TAG_TAG_psd1 = 1886610481;
    public static final int TAG_TAG_psd2 = 1886610482;
    public static final int TAG_TAG_psd3 = 1886610483;
    public static final int TAG_TAG_pseq = 1886610801;
    public static final int TAG_TAG_rTRC = 1918128707;
    public static final int TAG_TAG_rXYZ = 1918392666;
    public static final int TAG_TAG_resp = 1919251312;
    public static final int TAG_TAG_scrd = 1935897188;
    public static final int TAG_TAG_scrn = 1935897198;
    public static final int TAG_TAG_targ = 1952543335;
    public static final int TAG_TAG_tech = 1952801640;
    public static final int TAG_TAG_view = 1986618743;
    public static final int TAG_TAG_vued = 1987405156;
    public static final int TAG_TAG_wtpt = 2004119668;
    public static final int TAG_XYZ_VALUES = 68;
    @NotNull
    protected static final HashMap<Integer, String> _tagNameMap = new HashMap<>();

    static {
        _tagNameMap.put(0, "Profile Size");
        _tagNameMap.put(4, "CMM Type");
        _tagNameMap.put(8, "Version");
        _tagNameMap.put(12, "Class");
        _tagNameMap.put(16, "Color space");
        _tagNameMap.put(20, "Profile Connection Space");
        _tagNameMap.put(24, "Profile Date/Time");
        _tagNameMap.put(36, "Signature");
        _tagNameMap.put(40, "Primary Platform");
        _tagNameMap.put(44, "CMM Flags");
        _tagNameMap.put(48, "Device manufacturer");
        _tagNameMap.put(52, "Device model");
        _tagNameMap.put(56, "Device attributes");
        _tagNameMap.put(64, "Rendering Intent");
        _tagNameMap.put(68, "XYZ values");
        _tagNameMap.put(80, "Profile Creator");
        _tagNameMap.put(128, "Tag Count");
        _tagNameMap.put(Integer.valueOf((int) TAG_TAG_A2B0), "AToB 0");
        _tagNameMap.put(Integer.valueOf((int) TAG_TAG_A2B1), "AToB 1");
        _tagNameMap.put(Integer.valueOf((int) TAG_TAG_A2B2), "AToB 2");
        _tagNameMap.put(Integer.valueOf((int) TAG_TAG_bXYZ), "Blue Colorant");
        _tagNameMap.put(Integer.valueOf((int) TAG_TAG_bTRC), "Blue TRC");
        _tagNameMap.put(Integer.valueOf((int) TAG_TAG_B2A0), "BToA 0");
        _tagNameMap.put(Integer.valueOf((int) TAG_TAG_B2A1), "BToA 1");
        _tagNameMap.put(Integer.valueOf((int) TAG_TAG_B2A2), "BToA 2");
        _tagNameMap.put(Integer.valueOf((int) TAG_TAG_calt), "Calibration Date/Time");
        _tagNameMap.put(Integer.valueOf((int) TAG_TAG_targ), "Char Target");
        _tagNameMap.put(Integer.valueOf((int) TAG_TAG_chad), "Chromatic Adaptation");
        _tagNameMap.put(Integer.valueOf((int) TAG_TAG_chrm), "Chromaticity");
        _tagNameMap.put(Integer.valueOf((int) TAG_TAG_cprt), "Copyright");
        _tagNameMap.put(Integer.valueOf((int) TAG_TAG_crdi), "CrdInfo");
        _tagNameMap.put(Integer.valueOf((int) TAG_TAG_dmnd), "Device Mfg Description");
        _tagNameMap.put(Integer.valueOf((int) TAG_TAG_dmdd), "Device Model Description");
        _tagNameMap.put(Integer.valueOf((int) TAG_TAG_devs), "Device Settings");
        _tagNameMap.put(Integer.valueOf((int) TAG_TAG_gamt), "Gamut");
        _tagNameMap.put(Integer.valueOf((int) TAG_TAG_kTRC), "Gray TRC");
        _tagNameMap.put(Integer.valueOf((int) TAG_TAG_gXYZ), "Green Colorant");
        _tagNameMap.put(Integer.valueOf((int) TAG_TAG_gTRC), "Green TRC");
        _tagNameMap.put(Integer.valueOf((int) TAG_TAG_lumi), "Luminance");
        _tagNameMap.put(Integer.valueOf((int) TAG_TAG_meas), "Measurement");
        _tagNameMap.put(Integer.valueOf((int) TAG_TAG_bkpt), "Media Black Point");
        _tagNameMap.put(Integer.valueOf((int) TAG_TAG_wtpt), "Media White Point");
        _tagNameMap.put(Integer.valueOf((int) TAG_TAG_ncol), "Named Color");
        _tagNameMap.put(Integer.valueOf((int) TAG_TAG_ncl2), "Named Color 2");
        _tagNameMap.put(Integer.valueOf((int) TAG_TAG_resp), "Output Response");
        _tagNameMap.put(Integer.valueOf((int) TAG_TAG_pre0), "Preview 0");
        _tagNameMap.put(Integer.valueOf((int) TAG_TAG_pre1), "Preview 1");
        _tagNameMap.put(Integer.valueOf((int) TAG_TAG_pre2), "Preview 2");
        _tagNameMap.put(Integer.valueOf((int) TAG_TAG_desc), "Profile Description");
        _tagNameMap.put(Integer.valueOf((int) TAG_TAG_pseq), "Profile Sequence Description");
        _tagNameMap.put(Integer.valueOf((int) TAG_TAG_psd0), "Ps2 CRD 0");
        _tagNameMap.put(Integer.valueOf((int) TAG_TAG_psd1), "Ps2 CRD 1");
        _tagNameMap.put(Integer.valueOf((int) TAG_TAG_psd2), "Ps2 CRD 2");
        _tagNameMap.put(Integer.valueOf((int) TAG_TAG_psd3), "Ps2 CRD 3");
        _tagNameMap.put(Integer.valueOf((int) TAG_TAG_ps2s), "Ps2 CSA");
        _tagNameMap.put(Integer.valueOf((int) TAG_TAG_ps2i), "Ps2 Rendering Intent");
        _tagNameMap.put(Integer.valueOf((int) TAG_TAG_rXYZ), "Red Colorant");
        _tagNameMap.put(Integer.valueOf((int) TAG_TAG_rTRC), "Red TRC");
        _tagNameMap.put(Integer.valueOf((int) TAG_TAG_scrd), "Screening Desc");
        _tagNameMap.put(Integer.valueOf((int) TAG_TAG_scrn), "Screening");
        _tagNameMap.put(Integer.valueOf((int) TAG_TAG_tech), "Technology");
        _tagNameMap.put(Integer.valueOf((int) TAG_TAG_bfd), "Ucrbg");
        _tagNameMap.put(Integer.valueOf((int) TAG_TAG_vued), "Viewing Conditions Description");
        _tagNameMap.put(Integer.valueOf((int) TAG_TAG_view), "Viewing Conditions");
        _tagNameMap.put(Integer.valueOf((int) TAG_APPLE_MULTI_LANGUAGE_PROFILE_NAME), "Apple Multi-language Profile Name");
    }

    public IccDirectory() {
        setDescriptor(new IccDescriptor(this));
    }

    @NotNull
    public String getName() {
        return "ICC Profile";
    }

    /* access modifiers changed from: protected */
    @NotNull
    public HashMap<Integer, String> getTagNameMap() {
        return _tagNameMap;
    }
}
