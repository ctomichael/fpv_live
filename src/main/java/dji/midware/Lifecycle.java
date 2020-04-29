package dji.midware;

import android.content.Context;
import android.content.Intent;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class Lifecycle {
    public static final String ACTION_APPLICATION = "com.dji.lifecycle.application";
    public static final String STATUS_CREATE = "create";
    public static final String STATUS_DESTROY = "destroy";

    public static void broadcastCreate(Context ctx, String action) {
        broadcast(ctx, action, STATUS_CREATE);
    }

    public static void broadcastDestroy(Context ctx, String action) {
        broadcast(ctx, action, STATUS_DESTROY);
    }

    public static void broadcast(Context ctx, String action, String status) {
        Intent intent = new Intent();
        intent.setAction(action);
        intent.setPackage(ctx.getPackageName());
        if (status != null) {
            intent.putExtra("status", status);
        }
        ctx.sendBroadcast(intent);
    }
}
