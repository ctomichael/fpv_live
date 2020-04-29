package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.midware.data.manager.P3.DataBase;
import java.util.ArrayList;

@Keep
public class DataADSBGetPushAvoidanceAction extends DataBase {
    private static final int dimension = 3;

    @Keep
    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final DataADSBGetPushAvoidanceAction instance = new DataADSBGetPushAvoidanceAction();

        private SingletonHolder() {
        }
    }

    public static DataADSBGetPushAvoidanceAction getInstance() {
        return SingletonHolder.instance;
    }

    public boolean isConnectAdsb() {
        return (((Integer) get(0, 1, Integer.class)).intValue() & 1) == 1;
    }

    public int getActionCountDown() {
        return (((Integer) get(0, 1, Integer.class)).intValue() >> 1) & 31;
    }

    public int getActionMode() {
        return (((Integer) get(0, 1, Integer.class)).intValue() >> 6) & 3;
    }

    public ArrayList<Integer> getEscapeVelocity() {
        ArrayList<Integer> velocityList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            velocityList.add(Integer.valueOf(((Integer) get((i * 2) + 1, 2, Integer.class)).intValue()));
        }
        return velocityList;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
