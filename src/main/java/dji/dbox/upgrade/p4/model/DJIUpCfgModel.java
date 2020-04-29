package dji.dbox.upgrade.p4.model;

import android.support.annotation.Keep;
import dji.dbox.upgrade.p4.constants.DJIUpConstants;
import dji.fieldAnnotation.EXClassNullAway;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Keep
@EXClassNullAway
public class DJIUpCfgModel {
    private static final int SECURITY_INVALID = -1;
    public int antirollback;
    public String antirollbackExt;
    public String deviceId;
    public int enforce;
    public String enforceExt;
    public String enforceTime;
    public String expire;
    private SimpleDateFormat fmt;
    public String from;
    public boolean isTimeoutCase = false;
    public ArrayList<DJIUpModule> modules;
    public String releaseVersion;
    public int totalSize;

    @Keep
    public enum DJIFirmwareGroup {
        AC,
        RC,
        GL,
        TB
    }

    private synchronized SimpleDateFormat getDateFormat() {
        if (this.fmt == null) {
            this.fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.CHINA);
        }
        return this.fmt;
    }

    public long getEnforceTimestamp() {
        if (this.enforceTime == null) {
            return -1;
        }
        try {
            return getDateFormat().parse(this.enforceTime).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }

    private int getValueForCC(String s) {
        String countryCodeMem;
        synchronized (DJIUpConstants.mSyncCountryCodeMem) {
            countryCodeMem = DJIUpConstants.mCountryCodeMem;
        }
        Matcher m = Pattern.compile(countryCodeMem + ":(\\d+)").matcher(s);
        boolean isFind = m.find();
        int count = m.groupCount();
        if (isFind && count > 0) {
            try {
                return Integer.parseInt(m.group().substring(countryCodeMem.length() + 1));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    public boolean isEnforce(int curEnforce, String curEnforceExt) {
        String countryCodeMem;
        boolean result;
        synchronized (DJIUpConstants.mSyncCountryCodeMem) {
            countryCodeMem = DJIUpConstants.mCountryCodeMem;
        }
        int finalCurEnforce = getSecurityValue(countryCodeMem, curEnforce, curEnforceExt);
        int finalEnforce = getSecurityValue(countryCodeMem, this.enforce, this.enforceExt);
        if (finalEnforce > finalCurEnforce) {
            result = true;
        } else {
            result = false;
        }
        if (result) {
            DJIUpConstants.LOGD("security", String.format(Locale.getDefault(), "check isEnforce %1$s %2$s,%3$s,%4$d,%5$s,%6$d,%7$s,%8$d,%9$d", this.deviceId, this.releaseVersion, countryCodeMem, Integer.valueOf(curEnforce), curEnforceExt, Integer.valueOf(this.enforce), this.enforceExt, Integer.valueOf(finalCurEnforce), Integer.valueOf(finalEnforce)));
        }
        return result;
    }

    public boolean isCanAntirollback(int curAntirollback, String curAntirollbackExt) {
        String countryCodeMem;
        boolean result;
        synchronized (DJIUpConstants.mSyncCountryCodeMem) {
            countryCodeMem = DJIUpConstants.mCountryCodeMem;
        }
        int finalCurAntirollback = getSecurityValue(countryCodeMem, curAntirollback, curAntirollbackExt);
        int finalAntirollback = getSecurityValue(countryCodeMem, this.antirollback, this.antirollbackExt);
        if (finalAntirollback >= finalCurAntirollback) {
            result = true;
        } else {
            result = false;
        }
        if (!result) {
            DJIUpConstants.LOGD("security", String.format(Locale.getDefault(), "check isCanAntirollback %1$s %2$s,%3$s,%4$d,%5$s,%6$d,%7$s,%8$d,%9$d", this.deviceId, this.releaseVersion, countryCodeMem, Integer.valueOf(curAntirollback), curAntirollbackExt, Integer.valueOf(this.antirollback), this.antirollbackExt, Integer.valueOf(finalCurAntirollback), Integer.valueOf(finalAntirollback)));
        }
        return result;
    }

    private int getSecurityValue(String countryCodeMem, int value, String valueExt) {
        int finalValue = -1;
        if (valueExt != null && valueExt.contains(countryCodeMem)) {
            finalValue = getValueForCC(valueExt);
        }
        if (finalValue == -1) {
            return Math.max(0, value);
        }
        return finalValue;
    }

    @Keep
    public static class DJIUpModule {
        public String downpath;
        public DJIFirmwareGroup group;
        public String id;
        public boolean isDownloading = false;
        public String md5;
        public int modelId;
        public int modelType;
        public String name;
        public int size;
        public String type;
        public String version;

        public void setId(String id2) {
            this.id = id2;
            this.modelType = Integer.parseInt(id2.substring(0, 2));
            this.modelId = Integer.parseInt(id2.substring(2));
        }

        public DJIFirmwareGroup translateGroup(String group2) {
            if (group2 != null) {
                DJIFirmwareGroup[] values = DJIFirmwareGroup.values();
                for (DJIFirmwareGroup eGroup : values) {
                    if (eGroup.toString().equalsIgnoreCase(group2)) {
                        return eGroup;
                    }
                }
            }
            return DJIFirmwareGroup.AC;
        }
    }

    public static DJIUpCfgModel makeNullDeviceCfg(String productId) {
        DJIUpConstants.LOGD("", "getCfgCallBack makeNullDeviceCfg");
        DJIUpCfgModel cfgModel = new DJIUpCfgModel();
        cfgModel.antirollback = 0;
        cfgModel.enforce = 0;
        cfgModel.deviceId = productId;
        cfgModel.expire = "00-00-00";
        cfgModel.from = "00-00-00";
        cfgModel.releaseVersion = "00.00.0000";
        cfgModel.modules = new ArrayList<>();
        return cfgModel;
    }

    public boolean isNull() {
        return this.releaseVersion == null || this.releaseVersion.equals("00.00.0000");
    }

    public static boolean isNull(String releaseVersion2) {
        return releaseVersion2 == null || releaseVersion2.equals("00.00.0000");
    }
}
