package dji.internal.logics;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class Message {
    private final String description;
    private final long flag;
    private final boolean shouldBlink;
    private final String title;
    private final Type type;

    public enum Type {
        OFFLINE(0),
        GOOD(1),
        WARNING(2),
        ERROR(3);
        
        private final int value;

        private Type(int value2) {
            this.value = value2;
        }

        public int getValue() {
            return this.value;
        }
    }

    public Message(@NonNull Type type2, @NonNull String title2, @Nullable String description2) {
        this(type2, false, title2, description2, 0);
    }

    public Message(@NonNull Type type2, boolean shouldBlink2, @NonNull String title2, long flag2) {
        this(type2, shouldBlink2, title2, null, flag2);
    }

    public Message(@NonNull Type type2, boolean shouldBlink2, @NonNull String title2, @Nullable String description2, long flag2) {
        this.type = type2;
        this.title = title2;
        this.description = description2;
        this.shouldBlink = shouldBlink2;
        this.flag = flag2;
    }

    public Type getType() {
        return this.type;
    }

    public String getDescription() {
        return this.description;
    }

    public String getTitle() {
        return this.title;
    }

    public boolean shouldBlink() {
        return this.shouldBlink;
    }

    public long getFlag() {
        return this.flag;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Message msg = (Message) o;
        if (msg.type.getValue() != this.type.getValue() || msg.shouldBlink != this.shouldBlink || msg.flag != this.flag) {
            return false;
        }
        if (!TextUtils.equals(msg.title, this.title) || !TextUtils.equals(msg.description, this.description)) {
            z = false;
        }
        return z;
    }

    public boolean equals(Type type2, String title2, String description2) {
        if (this.type == type2 && TextUtils.equals(title2, this.title) && TextUtils.equals(description2, this.description)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int i;
        int i2;
        int i3 = 0;
        int value = ((this.type.getValue() * 31) + ((int) this.flag)) * 31;
        if (this.shouldBlink) {
            i = 1;
        } else {
            i = 0;
        }
        int i4 = (value + i) * 31;
        if (this.title != null) {
            i2 = this.title.hashCode();
        } else {
            i2 = 0;
        }
        int i5 = (i4 + i2) * 31;
        if (this.description != null) {
            i3 = this.description.hashCode();
        }
        return i5 + i3;
    }
}
