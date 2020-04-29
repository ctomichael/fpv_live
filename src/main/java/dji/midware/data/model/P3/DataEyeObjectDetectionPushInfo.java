package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;
import java.util.ArrayList;
import java.util.List;

@Keep
@EXClassNullAway
public class DataEyeObjectDetectionPushInfo extends DataBase {
    private static DataEyeObjectDetectionPushInfo instance;

    @Keep
    public static class ArrowInfo {
        public int alpha;
        public float[] orientation;
        public int type;

        public ArrowInfo(float[] orientation2, int type2, int alpha2) {
            this.orientation = orientation2;
            this.type = type2;
            this.alpha = alpha2;
        }
    }

    private DataEyeObjectDetectionPushInfo() {
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public static DataEyeObjectDetectionPushInfo getInstance() {
        if (instance == null) {
            instance = new DataEyeObjectDetectionPushInfo();
        }
        return instance;
    }

    public int getVersion() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public int getArrorNum() {
        return ((Integer) get(1, 1, Integer.class)).intValue();
    }

    public boolean isLastArrowObstacle() {
        return ((Integer) get(2, 1, Integer.class)).intValue() == 1;
    }

    public List<ArrowInfo> getArrowInfoList() {
        int arrowNum = getArrorNum();
        ArrayList<ArrowInfo> arrowInfoList = new ArrayList<>(arrowNum);
        int index = 3;
        for (int i = 0; i < arrowNum; i++) {
            float[] arrowData = new float[7];
            for (int j = 0; j < arrowData.length; j++) {
                arrowData[j] = ((Float) get(index, 4, Float.class)).floatValue();
                index += 4;
            }
            int index2 = index + 1;
            index = index2 + 1;
            arrowInfoList.add(new ArrowInfo(arrowData, ((Integer) get(index, 1, Integer.class)).intValue(), ((Integer) get(index2, 1, Integer.class)).intValue()));
        }
        return arrowInfoList;
    }
}
