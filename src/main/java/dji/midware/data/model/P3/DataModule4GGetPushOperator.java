package dji.midware.data.model.P3;

import dji.midware.data.manager.P3.DataBase;

public class DataModule4GGetPushOperator extends DataBase {
    public static DataModule4GGetPushOperator getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        /* access modifiers changed from: private */
        public static DataModule4GGetPushOperator INSTANCE = new DataModule4GGetPushOperator();

        private Holder() {
        }
    }

    /* access modifiers changed from: protected */
    public boolean isChanged(byte[] data) {
        return true;
    }

    public int getVersion() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public DataModule4GCardType getCardType() {
        return DataModule4GCardType.find(((Integer) get(1, 1, Integer.class)).intValue());
    }

    public int getMCC() {
        return ((Integer) get(2, 2, Integer.class)).intValue();
    }

    public int getMNC() {
        return ((Integer) get(4, 2, Integer.class)).intValue();
    }

    public boolean getSim() {
        return ((Integer) get(6, 1, Integer.class)).intValue() == 1;
    }

    public int getDJIRTT() {
        return ((Integer) get(7, 2, Integer.class)).intValue();
    }

    public long getRxBytes() {
        return ((Long) get(9, 4, Long.class)).longValue();
    }

    public long getTxBytes() {
        return ((Long) get(13, 4, Long.class)).longValue();
    }

    public String getApn() {
        if (!isGetted() || this._recData.length < 17) {
            return null;
        }
        int len = 0;
        int index = 17;
        while (index < this._recData.length && this._recData[index] != 0) {
            index++;
            len++;
        }
        return get(17, len);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
