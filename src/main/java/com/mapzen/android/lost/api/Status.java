package com.mapzen.android.lost.api;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.IntentSender;
import android.os.Parcel;
import android.os.Parcelable;
import com.dji.scan.zxing.Intents;
import com.mapzen.android.lost.internal.DialogDisplayer;

public class Status implements Result, Parcelable {
    public static final int CANCELLED = 16;
    public static final Parcelable.Creator<Status> CREATOR = new Parcelable.Creator<Status>() {
        /* class com.mapzen.android.lost.api.Status.AnonymousClass1 */

        public Status createFromParcel(Parcel in2) {
            return new Status(in2);
        }

        public Status[] newArray(int size) {
            return new Status[size];
        }
    };
    public static final int INTERNAL_ERROR = 8;
    public static final int INTERRUPTED = 14;
    public static final int RESOLUTION_REQUIRED = 6;
    public static final int SETTINGS_CHANGE_UNAVAILABLE = 8502;
    public static final int SUCCESS = 0;
    public static final int TIMEOUT = 15;
    private final DialogDisplayer dialogDisplayer;
    private final PendingIntent pendingIntent;
    private final int statusCode;
    private final String statusMessage;

    public Status(int statusCode2) {
        this(statusCode2, null, null);
    }

    public Status(int statusCode2, DialogDisplayer dialogDisplayer2) {
        this(statusCode2, dialogDisplayer2, null);
    }

    public Status(int statusCode2, DialogDisplayer dialogDisplayer2, PendingIntent pendingIntent2) {
        String statusMessage2;
        switch (statusCode2) {
            case 0:
                statusMessage2 = "SUCCESS";
                break;
            case 6:
                statusMessage2 = "RESOLUTION_REQUIRED";
                break;
            case 8:
                statusMessage2 = "INTERNAL_ERROR";
                break;
            case 14:
                statusMessage2 = "INTERRUPTED";
                break;
            case 15:
                statusMessage2 = Intents.Scan.TIMEOUT;
                break;
            case 16:
                statusMessage2 = "CANCELLED";
                break;
            case 8502:
                statusMessage2 = "SETTINGS_CHANGE_UNAVAILABLE";
                break;
            default:
                statusMessage2 = "UNKNOWN STATUS";
                break;
        }
        this.statusCode = statusCode2;
        this.statusMessage = statusMessage2;
        this.pendingIntent = pendingIntent2;
        this.dialogDisplayer = dialogDisplayer2;
    }

    protected Status(Parcel in2) {
        this.statusCode = in2.readInt();
        this.statusMessage = in2.readString();
        this.pendingIntent = (PendingIntent) in2.readParcelable(PendingIntent.class.getClassLoader());
        this.dialogDisplayer = (DialogDisplayer) in2.readParcelable(DialogDisplayer.class.getClassLoader());
    }

    public void startResolutionForResult(Activity activity, int requestCode) throws IntentSender.SendIntentException {
        if (hasResolution()) {
            this.dialogDisplayer.displayDialog(activity, requestCode, this.pendingIntent);
        }
    }

    public String getStatusMessage() {
        return this.statusMessage;
    }

    public boolean hasResolution() {
        return (this.pendingIntent == null || this.dialogDisplayer == null) ? false : true;
    }

    public boolean isSuccess() {
        return this.statusCode == 0;
    }

    public boolean isCanceled() {
        return this.statusCode == 16;
    }

    public boolean isInterrupted() {
        return this.statusCode == 14;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public PendingIntent getResolution() {
        return this.pendingIntent;
    }

    public Status getStatus() {
        return this;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.statusCode);
        parcel.writeString(this.statusMessage);
        parcel.writeParcelable(this.pendingIntent, i);
        parcel.writeParcelable(this.dialogDisplayer, i);
    }
}
