package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataSmartBatteryGetPushReArrangement extends DataBase {
    private static DataSmartBatteryGetPushReArrangement mInstance = null;

    @Keep
    public static class ReArrangement {
        public int dstIndex;
        public ReArrangementOption option;
        public int srcIndex;
    }

    @Keep
    public enum ReArrangementOption {
        None,
        PlugOut,
        Switch,
        Move
    }

    public static DataSmartBatteryGetPushReArrangement getInstance() {
        if (mInstance == null) {
            mInstance = new DataSmartBatteryGetPushReArrangement();
        }
        return mInstance;
    }

    public ReArrangement[] getReArrangement() {
        ReArrangement[] res = new ReArrangement[6];
        for (int i = 0; i < 6; i++) {
            res[i] = new ReArrangement();
            switch (((Integer) get(i * 3, 1, Integer.class)).intValue()) {
                case 0:
                    res[i].option = ReArrangementOption.None;
                    break;
                case 1:
                    res[i].option = ReArrangementOption.PlugOut;
                    break;
                case 2:
                    res[i].option = ReArrangementOption.Switch;
                    break;
                case 3:
                    res[i].option = ReArrangementOption.Move;
                    break;
            }
            res[i].srcIndex = ((Integer) get((i * 3) + 1, 1, Integer.class)).intValue();
            res[i].dstIndex = ((Integer) get((i * 3) + 2, 1, Integer.class)).intValue();
        }
        return res;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
