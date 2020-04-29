package com.dji.findmydrone.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v4.app.ActivityCompat;
import com.dji.permission.Permission;

final /* synthetic */ class DebugApp$$Lambda$2 implements DialogInterface.OnClickListener {
    private final Activity arg$1;

    DebugApp$$Lambda$2(Activity activity) {
        this.arg$1 = activity;
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        ActivityCompat.requestPermissions(this.arg$1, new String[]{Permission.ACCESS_FINE_LOCATION}, 99);
    }
}
