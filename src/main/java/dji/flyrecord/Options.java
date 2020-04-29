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

public final class Options extends Message<Options, Builder> {
    public static final ProtoAdapter<Options> ADAPTER = new ProtoAdapter_Options();
    public static final String DEFAULT_COLOR = "";
    public static final String DEFAULT_FILLCOLOR = "";
    public static final Double DEFAULT_FILLOPACITY = Double.valueOf(0.0d);
    public static final Double DEFAULT_OPACITY = Double.valueOf(0.0d);
    public static final Double DEFAULT_WEIGHT = Double.valueOf(0.0d);
    private static final long serialVersionUID = 0;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 2)
    public final String color;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 4)
    public final String fillColor;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#DOUBLE", tag = 5)
    public final Double fillOpacity;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#DOUBLE", tag = 3)
    public final Double opacity;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#DOUBLE", tag = 1)
    public final Double weight;

    public Options(Double weight2, String color2, Double opacity2, String fillColor2, Double fillOpacity2) {
        this(weight2, color2, opacity2, fillColor2, fillOpacity2, ByteString.EMPTY);
    }

    public Options(Double weight2, String color2, Double opacity2, String fillColor2, Double fillOpacity2, ByteString unknownFields) {
        super(ADAPTER, unknownFields);
        this.weight = weight2;
        this.color = color2;
        this.opacity = opacity2;
        this.fillColor = fillColor2;
        this.fillOpacity = fillOpacity2;
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        builder.weight = this.weight;
        builder.color = this.color;
        builder.opacity = this.opacity;
        builder.fillColor = this.fillColor;
        builder.fillOpacity = this.fillOpacity;
        builder.addUnknownFields(unknownFields());
        return builder;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Options)) {
            return false;
        }
        Options o = (Options) other;
        if (!unknownFields().equals(o.unknownFields()) || !Internal.equals(this.weight, o.weight) || !Internal.equals(this.color, o.color) || !Internal.equals(this.opacity, o.opacity) || !Internal.equals(this.fillColor, o.fillColor) || !Internal.equals(this.fillOpacity, o.fillOpacity)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int i;
        int i2;
        int i3;
        int i4 = 0;
        int result = this.hashCode;
        if (result != 0) {
            return result;
        }
        int hashCode = ((unknownFields().hashCode() * 37) + (this.weight != null ? this.weight.hashCode() : 0)) * 37;
        if (this.color != null) {
            i = this.color.hashCode();
        } else {
            i = 0;
        }
        int i5 = (hashCode + i) * 37;
        if (this.opacity != null) {
            i2 = this.opacity.hashCode();
        } else {
            i2 = 0;
        }
        int i6 = (i5 + i2) * 37;
        if (this.fillColor != null) {
            i3 = this.fillColor.hashCode();
        } else {
            i3 = 0;
        }
        int i7 = (i6 + i3) * 37;
        if (this.fillOpacity != null) {
            i4 = this.fillOpacity.hashCode();
        }
        int result2 = i7 + i4;
        this.hashCode = result2;
        return result2;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.weight != null) {
            builder.append(", weight=").append(this.weight);
        }
        if (this.color != null) {
            builder.append(", color=").append(this.color);
        }
        if (this.opacity != null) {
            builder.append(", opacity=").append(this.opacity);
        }
        if (this.fillColor != null) {
            builder.append(", fillColor=").append(this.fillColor);
        }
        if (this.fillOpacity != null) {
            builder.append(", fillOpacity=").append(this.fillOpacity);
        }
        return builder.replace(0, 2, "Options{").append('}').toString();
    }

    public static final class Builder extends Message.Builder<Options, Builder> {
        public String color;
        public String fillColor;
        public Double fillOpacity;
        public Double opacity;
        public Double weight;

        public Builder weight(Double weight2) {
            this.weight = weight2;
            return this;
        }

        public Builder color(String color2) {
            this.color = color2;
            return this;
        }

        public Builder opacity(Double opacity2) {
            this.opacity = opacity2;
            return this;
        }

        public Builder fillColor(String fillColor2) {
            this.fillColor = fillColor2;
            return this;
        }

        public Builder fillOpacity(Double fillOpacity2) {
            this.fillOpacity = fillOpacity2;
            return this;
        }

        public Options build() {
            return new Options(this.weight, this.color, this.opacity, this.fillColor, this.fillOpacity, super.buildUnknownFields());
        }
    }

    private static final class ProtoAdapter_Options extends ProtoAdapter<Options> {
        ProtoAdapter_Options() {
            super(FieldEncoding.LENGTH_DELIMITED, Options.class);
        }

        public int encodedSize(Options value) {
            int i;
            int i2;
            int i3;
            int i4 = 0;
            int encodedSizeWithTag = value.weight != null ? ProtoAdapter.DOUBLE.encodedSizeWithTag(1, value.weight) : 0;
            if (value.color != null) {
                i = ProtoAdapter.STRING.encodedSizeWithTag(2, value.color);
            } else {
                i = 0;
            }
            int i5 = i + encodedSizeWithTag;
            if (value.opacity != null) {
                i2 = ProtoAdapter.DOUBLE.encodedSizeWithTag(3, value.opacity);
            } else {
                i2 = 0;
            }
            int i6 = i5 + i2;
            if (value.fillColor != null) {
                i3 = ProtoAdapter.STRING.encodedSizeWithTag(4, value.fillColor);
            } else {
                i3 = 0;
            }
            int i7 = i3 + i6;
            if (value.fillOpacity != null) {
                i4 = ProtoAdapter.DOUBLE.encodedSizeWithTag(5, value.fillOpacity);
            }
            return i7 + i4 + value.unknownFields().size();
        }

        public void encode(ProtoWriter writer, Options value) throws IOException {
            if (value.weight != null) {
                ProtoAdapter.DOUBLE.encodeWithTag(writer, 1, value.weight);
            }
            if (value.color != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 2, value.color);
            }
            if (value.opacity != null) {
                ProtoAdapter.DOUBLE.encodeWithTag(writer, 3, value.opacity);
            }
            if (value.fillColor != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 4, value.fillColor);
            }
            if (value.fillOpacity != null) {
                ProtoAdapter.DOUBLE.encodeWithTag(writer, 5, value.fillOpacity);
            }
            writer.writeBytes(value.unknownFields());
        }

        public Options decode(ProtoReader reader) throws IOException {
            Builder builder = new Builder();
            long token = reader.beginMessage();
            while (true) {
                int tag = reader.nextTag();
                if (tag != -1) {
                    switch (tag) {
                        case 1:
                            builder.weight(ProtoAdapter.DOUBLE.decode(reader));
                            break;
                        case 2:
                            builder.color(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 3:
                            builder.opacity(ProtoAdapter.DOUBLE.decode(reader));
                            break;
                        case 4:
                            builder.fillColor(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 5:
                            builder.fillOpacity(ProtoAdapter.DOUBLE.decode(reader));
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

        public Options redact(Options value) {
            Builder builder = value.newBuilder();
            builder.clearUnknownFields();
            return builder.build();
        }
    }
}
