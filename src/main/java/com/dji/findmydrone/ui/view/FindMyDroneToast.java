package com.dji.findmydrone.ui.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.dji.relay.R;

public class FindMyDroneToast {
    public static void show(Context context, int resId) {
        show(context, context.getResources().getString(resId));
    }

    public static void show(Context context, String message) {
        View view = LayoutInflater.from(context).inflate((int) R.layout.fmd_toast_view, (ViewGroup) null);
        ((TextView) view.findViewById(R.id.fmd_toast_content_text_view)).setText(message);
        Toast toast = new Toast(context);
        toast.setGravity(17, 0, 0);
        toast.setDuration(0);
        toast.setView(view);
        toast.show();
    }
}
