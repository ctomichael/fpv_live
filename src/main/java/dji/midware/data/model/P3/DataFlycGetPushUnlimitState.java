package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.Pack;
import dji.midware.util.BytesUtil;
import java.util.ArrayList;
import java.util.List;

@Keep
@EXClassNullAway
public class DataFlycGetPushUnlimitState extends DataBase {
    private static DataFlycGetPushUnlimitState instance = null;
    private List<UnlimitArea> mUnlimitAreas = new ArrayList();

    @Keep
    public static class UnlimitArea {
        public double lat;
        public double lng;
        public double radius;
        public long startTime;
        public long stopTime;
        public int type;
    }

    public static synchronized DataFlycGetPushUnlimitState getInstance() {
        DataFlycGetPushUnlimitState dataFlycGetPushUnlimitState;
        synchronized (DataFlycGetPushUnlimitState.class) {
            if (instance == null) {
                instance = new DataFlycGetPushUnlimitState();
            }
            dataFlycGetPushUnlimitState = instance;
        }
        return dataFlycGetPushUnlimitState;
    }

    private DataFlycGetPushUnlimitState() {
        this.isPushLosed = true;
    }

    /* access modifiers changed from: protected */
    public void setPushRecPack(Pack pack) {
        if (pack == null || pack.data == null || BytesUtil.getInt(pack.data[0]) <= 10) {
            super.setPushRecPack(pack);
        } else {
            DataFlycGetPushNewUnlimitState.getInstance().outerSetPushRecPack(pack);
        }
    }

    /* access modifiers changed from: protected */
    public boolean isChanged(byte[] data) {
        return true;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public int isInUnlimitArea() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public int getUnlimitAreasAction() {
        return ((Integer) get(1, 1, Integer.class)).intValue();
    }

    public int getUnlimitAreasSize() {
        return ((Integer) get(2, 1, Integer.class)).intValue();
    }

    public int getUnlimitAreasEnabled() {
        return ((Integer) get(3, 1, Integer.class)).intValue();
    }

    public List<UnlimitArea> getUnlimitAreasList() {
        this.mUnlimitAreas.clear();
        int size = getUnlimitAreasSize();
        for (int i = 0; i < size; i++) {
            UnlimitArea area = new UnlimitArea();
            area.lat = ((double) ((Integer) get((i * 21) + 7, 4, Integer.class)).intValue()) / 1000000.0d;
            area.lng = ((double) ((Integer) get(((i * 21) + 7) + 4, 4, Integer.class)).intValue()) / 1000000.0d;
            area.radius = (double) ((Long) get((i * 21) + 7 + 8, 4, Long.class)).longValue();
            area.type = ((Integer) get((i * 21) + 7 + 12, 1, Integer.class)).intValue();
            area.startTime = ((Long) get((i * 21) + 7 + 13, 4, Long.class)).longValue();
            area.stopTime = ((Long) get((i * 21) + 7 + 17, 4, Long.class)).longValue();
            this.mUnlimitAreas.add(area);
        }
        return this.mUnlimitAreas;
    }
}
