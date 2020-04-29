package dji.midware.data.model.P3;

import dji.midware.data.manager.P3.DataBase;

public class DataModule4GGetPushSignal extends DataBase {
    public static final int OTHER_TYPE = 2;
    public static final int ZTE_TYPE = 1;

    public static DataModule4GGetPushSignal getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        /* access modifiers changed from: private */
        public static DataModule4GGetPushSignal INSTANCE = new DataModule4GGetPushSignal();

        private Holder() {
        }
    }

    public int getVersion() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public DataModule4GCardType getCardType() {
        return DataModule4GCardType.find(((Integer) get(1, 1, Integer.class)).intValue());
    }

    public NetworkType getNetworkType() {
        int networkType = ((Integer) get(2, 1, Integer.class)).intValue();
        if (getCardType() == DataModule4GCardType.ZTE) {
            return NetworkType.find(1, networkType);
        }
        return NetworkType.find(2, networkType);
    }

    public int getSignal() {
        return ((Integer) get(3, 1, Integer.class)).intValue();
    }

    public int getRsrp() {
        return ((Integer) get(4, 1, Integer.class)).intValue();
    }

    public int getRsrq() {
        return ((Integer) get(5, 1, Integer.class)).intValue();
    }

    public long getCellId() {
        return ((Long) get(6, 4, Long.class)).longValue();
    }

    public int getBand() {
        return ((Integer) get(10, 1, Integer.class)).intValue();
    }

    public int getSnr() {
        return ((Integer) get(11, 1, Integer.class)).intValue();
    }

    public int getRSSI() {
        return ((Integer) get(12, 1, Integer.class)).intValue();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public enum NetworkType {
        NO_SERVICE(1, 0, ""),
        LIMIT_SERVICE(1, 1, "limit"),
        GSM_ZTE(1, 2, "2G"),
        GRPS_ZTE(1, 3, "2G"),
        EDGE_ZTE(1, 4, "2G"),
        UMTS_ZTE(1, 5, "3G"),
        HSDPA_ZTE(1, 6, "3G"),
        HSUPA_ZTE(1, 7, "3G"),
        HSUPA_PLUS_ZTE(1, 8, "3G"),
        HSPA_PLUS_ZTE(1, 9, "3G"),
        DC_ZTE(1, 10, "3G"),
        DC_HSPA_ZTE(1, 11, "3G"),
        DC_HSPA_PLUS_ZTE(1, 12, "3G"),
        LTE_ZTE(1, 13, "4G"),
        ILLEGAL_SERVICE_ZTE(1, 14, ""),
        GSM(2, 0, "2G"),
        GSMCompact(2, 1, "2G"),
        UTRAN(2, 2, "3G"),
        GSMw_EGPRS(2, 3, "3G"),
        UTRANw_HSDPA(2, 4, "3G"),
        UTRANw_HSUPA(2, 5, "3G"),
        UTRANw_HSDUPA(2, 6, "3G"),
        E_UTRAN(2, 7, "4G"),
        UTRAN_HSPA_PLUS(2, 8, "4G"),
        UNKNOWN(2, 65535, "");
        
        String mName;
        int mType;
        int mValue;

        private NetworkType(int type, int value, String name) {
            this.mType = type;
            this.mValue = value;
            this.mName = name;
        }

        public int value() {
            return this.mValue;
        }

        public static NetworkType find(int type, int value) {
            NetworkType target = UNKNOWN;
            NetworkType[] values = values();
            for (NetworkType network : values) {
                if (network.mValue == value && network.mType == type) {
                    return network;
                }
            }
            return target;
        }

        public String toString() {
            return this.mName;
        }
    }
}
