package dji.publics.utils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

public class UIUtils {
    private static long lastClickTime;

    public static void addMergeView(ViewGroup parent, int layoutId) {
        LayoutInflater.from(parent.getContext()).inflate(layoutId, parent);
    }

    public static void addView(ViewGroup parent, int layoutId) {
        LayoutInflater.from(parent.getContext()).inflate(layoutId, parent);
    }

    public static void addView(ViewGroup parent, int layoutId, Context context) {
        LayoutInflater.from(context).inflate(layoutId, parent);
    }

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 >= timeD || timeD >= 1200) {
            lastClickTime = time;
            Log.d("isFastDoubleClick", "false");
            return false;
        }
        Log.d("isFastDoubleClick", "true");
        return true;
    }
}
