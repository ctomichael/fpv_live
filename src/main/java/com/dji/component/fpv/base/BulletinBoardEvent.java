package com.dji.component.fpv.base;

import android.os.Parcel;
import android.os.Parcelable;
import dji.component.accountcenter.IMemberProtocol;
import java.util.Objects;

public final class BulletinBoardEvent implements Parcelable {
    public static final BulletinBoardEvent CLICK_EVENT = new BulletinBoardEvent("clickEvent");
    public static final Parcelable.Creator<BulletinBoardEvent> CREATOR = new Parcelable.Creator<BulletinBoardEvent>() {
        /* class com.dji.component.fpv.base.BulletinBoardEvent.AnonymousClass1 */

        public BulletinBoardEvent createFromParcel(Parcel in2) {
            return new BulletinBoardEvent(in2);
        }

        public BulletinBoardEvent[] newArray(int size) {
            return new BulletinBoardEvent[size];
        }
    };
    public static final BulletinBoardEvent LONG_PRESS_EVENT = new BulletinBoardEvent("longClickEvent");
    public static final BulletinBoardEvent MENU_ITEM_REMOVE_SELF_EVENT = new BulletinBoardEvent("menuItemRemoveSelfEvent");
    public static final BulletinBoardEvent SLIDE_DOWN_EVENT = new BulletinBoardEvent("slideDownEvent");
    private final String description;

    private BulletinBoardEvent(String description2) {
        this.description = description2;
    }

    protected BulletinBoardEvent(Parcel in2) {
        this.description = in2.readString();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BulletinBoardEvent)) {
            return false;
        }
        return Objects.equals(this.description, ((BulletinBoardEvent) o).description);
    }

    public String toString() {
        return "BBEvent[" + this.description + IMemberProtocol.STRING_SEPERATOR_RIGHT;
    }

    public int hashCode() {
        return Objects.hash(this.description);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.description);
    }
}
