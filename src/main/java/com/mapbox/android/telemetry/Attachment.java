package com.mapbox.android.telemetry;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.mapbox.android.telemetry.Event;
import java.util.ArrayList;
import java.util.List;

public class Attachment extends Event implements Parcelable {
    public static final Parcelable.Creator<Attachment> CREATOR = new Parcelable.Creator<Attachment>() {
        /* class com.mapbox.android.telemetry.Attachment.AnonymousClass1 */

        public Attachment createFromParcel(Parcel in2) {
            return new Attachment(in2);
        }

        public Attachment[] newArray(int size) {
            return new Attachment[size];
        }
    };
    private static final String VIS_ATTACHMENT = "vis.attachment";
    @SerializedName("files")
    private List<FileAttachment> attachments;
    @SerializedName("event")
    private final String event;

    Attachment() {
        this.event = VIS_ATTACHMENT;
        this.attachments = new ArrayList();
    }

    protected Attachment(Parcel in2) {
        this.event = in2.readString();
    }

    public List<FileAttachment> getAttachments() {
        return this.attachments;
    }

    public void addAttachment(FileAttachment attachment) {
        this.attachments.add(attachment);
    }

    /* access modifiers changed from: package-private */
    public Event.Type obtainType() {
        return Event.Type.VIS_ATTACHMENT;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.event);
    }
}
