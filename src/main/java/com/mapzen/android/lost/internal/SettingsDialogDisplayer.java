package com.mapzen.android.lost.internal;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.os.Parcel;
import android.os.Parcelable;

public class SettingsDialogDisplayer implements DialogDisplayer, Parcelable {
    public static final Parcelable.Creator<SettingsDialogDisplayer> CREATOR = new Parcelable.Creator<SettingsDialogDisplayer>() {
        /* class com.mapzen.android.lost.internal.SettingsDialogDisplayer.AnonymousClass1 */

        public SettingsDialogDisplayer createFromParcel(Parcel in2) {
            return new SettingsDialogDisplayer(in2);
        }

        public SettingsDialogDisplayer[] newArray(int size) {
            return new SettingsDialogDisplayer[size];
        }
    };
    private static final String SETTINGS_DIALOG_TAG = "settings-dialog";

    public SettingsDialogDisplayer() {
    }

    protected SettingsDialogDisplayer(Parcel in2) {
    }

    public void displayDialog(final Activity activity, final int requestCode, final PendingIntent pendingIntent) {
        SettingsDialogFragment fragment = new SettingsDialogFragment();
        fragment.setOnClickListener(new DialogInterface.OnClickListener() {
            /* class com.mapzen.android.lost.internal.SettingsDialogDisplayer.AnonymousClass2 */

            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    activity.startIntentSenderForResult(pendingIntent.getIntentSender(), requestCode, null, 0, 0, 0);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        });
        fragment.show(activity.getFragmentManager(), SETTINGS_DIALOG_TAG);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
    }
}
