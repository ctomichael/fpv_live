package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;
import java.util.ArrayList;
import java.util.List;

@Keep
@EXClassNullAway
public class DataADSBGetPushUnlockInfo extends DataBase {
    private static DataADSBGetPushUnlockInfo mInstance = null;
    private List<Integer> mUnlockAreaIds = new ArrayList();
    private boolean mUnlockAreasChanged = false;

    public static DataADSBGetPushUnlockInfo getInstance() {
        if (mInstance == null) {
            mInstance = new DataADSBGetPushUnlockInfo();
        }
        return mInstance;
    }

    public boolean isUnlockAreasChanged() {
        return this.mUnlockAreasChanged;
    }

    public List<Integer> getUnlockAreaIds() {
        return this.mUnlockAreaIds;
    }

    /* access modifiers changed from: protected */
    public void post() {
        this.mUnlockAreasChanged = true;
        int num = ((Integer) get(0, 1, Integer.class)).intValue();
        this.mUnlockAreaIds.clear();
        for (int i = 0; i < num; i++) {
            this.mUnlockAreaIds.add(get((i * 4) + 1, 4, Integer.class));
        }
        super.post();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
