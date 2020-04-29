package dji.publics.utils;

import android.util.Log;
import android.util.SparseArray;
import java.util.HashMap;
import java.util.Map;

public class TimeLog {
    private static int lastIndex = -1;
    private static Map<Integer, String> nameMap = new HashMap();
    private static SparseArray<Long> startTimes = new SparseArray<>(5);

    public static void time(int index) {
        if (lastIndex != -1) {
            endTime(lastIndex);
        }
        startTime(index, null);
        lastIndex = index;
    }

    public static void startTime(int index, String tag) {
        startTimes.put(index, Long.valueOf(System.currentTimeMillis()));
        if (tag != null) {
            nameMap.put(Integer.valueOf(index), tag);
        }
    }

    public static void endTime(int index) {
        if (nameMap.containsKey(Integer.valueOf(index))) {
            Log.d("CostTime", String.format("Tag: %s, index : %d, time : %d", nameMap.get(Integer.valueOf(index)), Integer.valueOf(index), Long.valueOf(System.currentTimeMillis() - startTimes.get(index).longValue())));
            return;
        }
        Log.d("CostTime", String.format("index : %d, time : %d", Integer.valueOf(index), Long.valueOf(System.currentTimeMillis() - startTimes.get(index).longValue())));
    }
}
