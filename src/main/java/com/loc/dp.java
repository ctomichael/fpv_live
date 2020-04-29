package com.loc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import java.util.ArrayList;

final class dp extends BroadcastReceiver {
    dp() {
    }

    public final void onReceive(Context context, Intent intent) {
        try {
            if (!isInitialStickyBroadcast() && intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
                String a = Cdo.a();
                if (a != "None_Network" && !a.equalsIgnoreCase(Cdo.c)) {
                    dk.a("[BroadcastReceiver.onReceive] - Network state changed");
                    dg.a();
                    ArrayList d = dg.d();
                    dg.a();
                    dg.c();
                    if (Cdo.a && dc.a != null) {
                        dk.a("[BroadcastReceiver.onReceive] - refresh host");
                        dc.a.a(d);
                    }
                }
                String unused = Cdo.c = a;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
