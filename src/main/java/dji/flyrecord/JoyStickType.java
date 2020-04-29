package dji.flyrecord;

import com.squareup.wire.FieldEncoding;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.ProtoReader;
import com.squareup.wire.ProtoWriter;
import com.squareup.wire.WireField;
import com.squareup.wire.internal.Internal;
import java.io.IOException;
import okio.ByteString;

public final class JoyStickType extends Message<JoyStickType, Builder> {
    public static final ProtoAdapter<JoyStickType> ADAPTER = new ProtoAdapter_JoyStickType();
    public static final Double DEFAULT_BOTTOM = Double.valueOf(0.0d);
    public static final Double DEFAULT_LEFT = Double.valueOf(0.0d);
    public static final Double DEFAULT_RIGHT = Double.valueOf(0.0d);
    public static final Double DEFAULT_TOP = Double.valueOf(0.0d);
    private static final long serialVersionUID = 0;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#DOUBLE", tag = 3)
    public final Double bottom;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#DOUBLE", tag = 1)
    public final Double left;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#DOUBLE", tag = 2)
    public final Double right;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#DOUBLE", tag = 4)
    public final Double top;

    public JoyStickType(Double left2, Double right2, Double bottom2, Double top2) {
        this(left2, right2, bottom2, top2, ByteString.EMPTY);
    }

    public JoyStickType(Double left2, Double right2, Double bottom2, Double top2, ByteString unknownFields) {
        super(ADAPTER, unknownFields);
        this.left = left2;
        this.right = right2;
        this.bottom = bottom2;
        this.top = top2;
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        builder.left = this.left;
        builder.right = this.right;
        builder.bottom = this.bottom;
        builder.top = this.top;
        builder.addUnknownFields(unknownFields());
        return builder;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof JoyStickType)) {
            return false;
        }
        JoyStickType o = (JoyStickType) other;
        if (!unknownFields().equals(o.unknownFields()) || !Internal.equals(this.left, o.left) || !Internal.equals(this.right, o.right) || !Internal.equals(this.bottom, o.bottom) || !Internal.equals(this.top, o.top)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int i;
        int i2;
        int i3 = 0;
        int result = this.hashCode;
        if (result != 0) {
            return result;
        }
        int hashCode = ((unknownFields().hashCode() * 37) + (this.left != null ? this.left.hashCode() : 0)) * 37;
        if (this.right != null) {
            i = this.right.hashCode();
        } else {
            i = 0;
        }
        int i4 = (hashCode + i) * 37;
        if (this.bottom != null) {
            i2 = this.bottom.hashCode();
        } else {
            i2 = 0;
        }
        int i5 = (i4 + i2) * 37;
        if (this.top != null) {
            i3 = this.top.hashCode();
        }
        int result2 = i5 + i3;
        this.hashCode = result2;
        return result2;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.left != null) {
            builder.append(", left=").append(this.left);
        }
        if (this.right != null) {
            builder.append(", right=").append(this.right);
        }
        if (this.bottom != null) {
            builder.append(", bottom=").append(this.bottom);
        }
        if (this.top != null) {
            builder.append(", top=").append(this.top);
        }
        return builder.replace(0, 2, "JoyStickType{").append('}').toString();
    }

    public static final class Builder extends Message.Builder<JoyStickType, Builder> {
        public Double bottom;
        public Double left;
        public Double right;
        public Double top;

        public Builder left(Double left2) {
            this.left = left2;
            return this;
        }

        public Builder right(Double right2) {
            this.right = right2;
            return this;
        }

        public Builder bottom(Double bottom2) {
            this.bottom = bottom2;
            return this;
        }

        public Builder top(Double top2) {
            this.top = top2;
            return this;
        }

        public JoyStickType build() {
            return new JoyStickType(this.left, this.right, this.bottom, this.top, super.buildUnknownFields());
        }
    }

    private static final class ProtoAdapter_JoyStickType extends ProtoAdapter<JoyStickType> {
        ProtoAdapter_JoyStickType() {
            super(FieldEncoding.LENGTH_DELIMITED, JoyStickType.class);
        }

        public int encodedSize(JoyStickType value) {
            int i;
            int i2;
            int i3 = 0;
            int encodedSizeWithTag = value.left != null ? ProtoAdapter.DOUBLE.encodedSizeWithTag(1, value.left) : 0;
            if (value.right != null) {
                i = ProtoAdapter.DOUBLE.encodedSizeWithTag(2, value.right);
            } else {
                i = 0;
            }
            int i4 = i + encodedSizeWithTag;
            if (value.bottom != null) {
                i2 = ProtoAdapter.DOUBLE.encodedSizeWithTag(3, value.bottom);
            } else {
                i2 = 0;
            }
            int i5 = i2 + i4;
            if (value.top != null) {
                i3 = ProtoAdapter.DOUBLE.encodedSizeWithTag(4, value.top);
            }
            return i5 + i3 + value.unknownFields().size();
        }

        public void encode(ProtoWriter writer, JoyStickType value) throws IOException {
            if (value.left != null) {
                ProtoAdapter.DOUBLE.encodeWithTag(writer, 1, value.left);
            }
            if (value.right != null) {
                ProtoAdapter.DOUBLE.encodeWithTag(writer, 2, value.right);
            }
            if (value.bottom != null) {
                ProtoAdapter.DOUBLE.encodeWithTag(writer, 3, value.bottom);
            }
            if (value.top != null) {
                ProtoAdapter.DOUBLE.encodeWithTag(writer, 4, value.top);
            }
            writer.writeBytes(value.unknownFields());
        }

        public JoyStickType decode(ProtoReader reader) throws IOException {
            Builder builder = new Builder();
            long token = reader.beginMessage();
            while (true) {
                int tag = reader.nextTag();
                if (tag != -1) {
                    switch (tag) {
                        case 1:
                            builder.left(ProtoAdapter.DOUBLE.decode(reader));
                            break;
                        case 2:
                            builder.right(ProtoAdapter.DOUBLE.decode(reader));
                            break;
                        case 3:
                            builder.bottom(ProtoAdapter.DOUBLE.decode(reader));
                            break;
                        case 4:
                            builder.top(ProtoAdapter.DOUBLE.decode(reader));
                            break;
                        default:
                            FieldEncoding fieldEncoding = reader.peekFieldEncoding();
                            builder.addUnknownField(tag, fieldEncoding, fieldEncoding.rawProtoAdapter().decode(reader));
                            break;
                    }
                } else {
                    reader.endMessage(token);
                    return builder.build();
                }
            }
        }

        public JoyStickType redact(JoyStickType value) {
            Builder builder = value.newBuilder();
            builder.clearUnknownFields();
            return builder.build();
        }
    }
}
