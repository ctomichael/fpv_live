package com.mapzen.android.lost.internal;

import android.app.Activity;
import android.app.PendingIntent;
import android.os.Parcelable;

public interface DialogDisplayer extends Parcelable {
    void displayDialog(Activity activity, int i, PendingIntent pendingIntent);
}
