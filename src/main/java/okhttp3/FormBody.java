package okhttp3;

import dji.proto.djigo_services.AbTestRequestWrapper;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import okhttp3.internal.Util;
import okio.Buffer;
import okio.BufferedSink;

public final class FormBody extends RequestBody {
    private static final MediaType CONTENT_TYPE = MediaType.get(AbTestRequestWrapper.CONTENT_TYPE);
    private final List<String> encodedNames;
    private final List<String> encodedValues;

    FormBody(List<String> encodedNames2, List<String> encodedValues2) {
        this.encodedNames = Util.immutableList(encodedNames2);
        this.encodedValues = Util.immutableList(encodedValues2);
    }

    public int size() {
        return this.encodedNames.size();
    }

    public String encodedName(int index) {
        return this.encodedNames.get(index);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: okhttp3.HttpUrl.percentDecode(java.lang.String, boolean):java.lang.String
     arg types: [java.lang.String, int]
     candidates:
      okhttp3.HttpUrl.percentDecode(java.util.List<java.lang.String>, boolean):java.util.List<java.lang.String>
      okhttp3.HttpUrl.percentDecode(java.lang.String, boolean):java.lang.String */
    public String name(int index) {
        return HttpUrl.percentDecode(encodedName(index), true);
    }

    public String encodedValue(int index) {
        return this.encodedValues.get(index);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: okhttp3.HttpUrl.percentDecode(java.lang.String, boolean):java.lang.String
     arg types: [java.lang.String, int]
     candidates:
      okhttp3.HttpUrl.percentDecode(java.util.List<java.lang.String>, boolean):java.util.List<java.lang.String>
      okhttp3.HttpUrl.percentDecode(java.lang.String, boolean):java.lang.String */
    public String value(int index) {
        return HttpUrl.percentDecode(encodedValue(index), true);
    }

    public MediaType contentType() {
        return CONTENT_TYPE;
    }

    public long contentLength() {
        return writeOrCountBytes(null, true);
    }

    public void writeTo(BufferedSink sink) throws IOException {
        writeOrCountBytes(sink, false);
    }

    private long writeOrCountBytes(@Nullable BufferedSink sink, boolean countBytes) {
        Buffer buffer;
        if (countBytes) {
            buffer = new Buffer();
        } else {
            buffer = sink.buffer();
        }
        int size = this.encodedNames.size();
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                buffer.writeByte(38);
            }
            buffer.writeUtf8(this.encodedNames.get(i));
            buffer.writeByte(61);
            buffer.writeUtf8(this.encodedValues.get(i));
        }
        if (!countBytes) {
            return 0;
        }
        long byteCount = buffer.size();
        buffer.clear();
        return byteCount;
    }

    public static final class Builder {
        private final Charset charset;
        private final List<String> names;
        private final List<String> values;

        public Builder() {
            this(null);
        }

        public Builder(Charset charset2) {
            this.names = new ArrayList();
            this.values = new ArrayList();
            this.charset = charset2;
        }

        public Builder add(String name, String value) {
            if (name == null) {
                throw new NullPointerException("name == null");
            } else if (value == null) {
                throw new NullPointerException("value == null");
            } else {
                this.names.add(HttpUrl.canonicalize(name, " \"':;<=>@[]^`{}|/\\?#&!$(),~", false, false, true, true, this.charset));
                this.values.add(HttpUrl.canonicalize(value, " \"':;<=>@[]^`{}|/\\?#&!$(),~", false, false, true, true, this.charset));
                return this;
            }
        }

        public Builder addEncoded(String name, String value) {
            if (name == null) {
                throw new NullPointerException("name == null");
            } else if (value == null) {
                throw new NullPointerException("value == null");
            } else {
                this.names.add(HttpUrl.canonicalize(name, " \"':;<=>@[]^`{}|/\\?#&!$(),~", true, false, true, true, this.charset));
                this.values.add(HttpUrl.canonicalize(value, " \"':;<=>@[]^`{}|/\\?#&!$(),~", true, false, true, true, this.charset));
                return this;
            }
        }

        public FormBody build() {
            return new FormBody(this.names, this.values);
        }
    }
}
