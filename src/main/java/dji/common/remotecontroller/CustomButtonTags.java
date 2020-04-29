package dji.common.remotecontroller;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class CustomButtonTags {
    private short c1ButtonTag;
    private short c2ButtonTag;
    private short c3ButtonTag;

    public static final class Builder {
        /* access modifiers changed from: private */
        public short c1ButtonTag;
        /* access modifiers changed from: private */
        public short c2ButtonTag;
        /* access modifiers changed from: private */
        public short c3ButtonTag;

        public Builder c1ButtonTag(short c1ButtonTag2) {
            this.c1ButtonTag = c1ButtonTag2;
            return this;
        }

        public Builder c2ButtonTag(short c2ButtonTag2) {
            this.c2ButtonTag = c2ButtonTag2;
            return this;
        }

        public Builder c3ButtonTag(short c3ButtonTag2) {
            this.c3ButtonTag = c3ButtonTag2;
            return this;
        }

        public CustomButtonTags build() {
            return new CustomButtonTags(this);
        }
    }

    public short getC1ButtonTag() {
        return this.c1ButtonTag;
    }

    public short getC2ButtonTag() {
        return this.c2ButtonTag;
    }

    public short getC3ButtonTag() {
        return this.c3ButtonTag;
    }

    private CustomButtonTags(Builder builder) {
        this.c1ButtonTag = builder.c1ButtonTag;
        this.c2ButtonTag = builder.c2ButtonTag;
        this.c3ButtonTag = builder.c3ButtonTag;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CustomButtonTags that = (CustomButtonTags) o;
        if (this.c1ButtonTag != that.c1ButtonTag || this.c2ButtonTag != that.c2ButtonTag) {
            return false;
        }
        if (this.c3ButtonTag != that.c3ButtonTag) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return (((this.c1ButtonTag * 31) + this.c2ButtonTag) * 31) + this.c3ButtonTag;
    }
}
