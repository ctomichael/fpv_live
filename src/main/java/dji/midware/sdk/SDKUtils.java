package dji.midware.sdk;

import android.content.Context;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.MidWare;

@EXClassNullAway
public class SDKUtils {
    public static int getMidwareResourceId(Context context, String name) {
        return context.getResources().getIdentifier(name, "string", context.getPackageName());
    }

    public static String getMidwareString(Context context, int id) {
        return context != null ? context.getResources().getString(id) : "";
    }

    public static String getMidwareString(int resId) {
        if (MidWare.context == null || MidWare.context.get() == null) {
            return "";
        }
        return MidWare.context.get().getResources().getString(resId);
    }

    public static String getMidwareStringByName(Context context, String name) {
        return getMidwareString(context, getMidwareResourceId(context, name));
    }
}
