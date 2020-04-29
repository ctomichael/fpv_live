package dji.thirdparty.retrofit2;

import dji.thirdparty.okhttp3.Headers;
import dji.thirdparty.okhttp3.MultipartBody;
import dji.thirdparty.okhttp3.RequestBody;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Map;

abstract class ParameterHandler<T> {
    /* access modifiers changed from: package-private */
    public abstract void apply(RequestBuilder requestBuilder, T t) throws IOException;

    ParameterHandler() {
    }

    /* access modifiers changed from: package-private */
    public final ParameterHandler<Iterable<T>> iterable() {
        return new ParameterHandler<Iterable<T>>() {
            /* class dji.thirdparty.retrofit2.ParameterHandler.AnonymousClass1 */

            /* access modifiers changed from: package-private */
            public void apply(RequestBuilder builder, Iterable<T> values) throws IOException {
                if (values != null) {
                    for (T value : values) {
                        ParameterHandler.this.apply(builder, value);
                    }
                }
            }
        };
    }

    /* access modifiers changed from: package-private */
    public final ParameterHandler<Object> array() {
        return new ParameterHandler<Object>() {
            /* class dji.thirdparty.retrofit2.ParameterHandler.AnonymousClass2 */

            /* access modifiers changed from: package-private */
            public void apply(RequestBuilder builder, Object values) throws IOException {
                if (values != null) {
                    int size = Array.getLength(values);
                    for (int i = 0; i < size; i++) {
                        ParameterHandler.this.apply(builder, Array.get(values, i));
                    }
                }
            }
        };
    }

    static final class RelativeUrl extends ParameterHandler<Object> {
        RelativeUrl() {
        }

        /* access modifiers changed from: package-private */
        public void apply(RequestBuilder builder, Object value) {
            builder.setRelativeUrl(value);
        }
    }

    static final class Header<T> extends ParameterHandler<T> {
        private final String name;
        private final Converter<T, String> valueConverter;

        Header(String name2, Converter<T, String> valueConverter2) {
            this.name = (String) Utils.checkNotNull(name2, "name == null");
            this.valueConverter = valueConverter2;
        }

        /* access modifiers changed from: package-private */
        public void apply(RequestBuilder builder, T value) throws IOException {
            if (value != null) {
                builder.addHeader(this.name, this.valueConverter.convert(value));
            }
        }
    }

    static final class Path<T> extends ParameterHandler<T> {
        private final boolean encoded;
        private final String name;
        private final Converter<T, String> valueConverter;

        Path(String name2, Converter<T, String> valueConverter2, boolean encoded2) {
            this.name = (String) Utils.checkNotNull(name2, "name == null");
            this.valueConverter = valueConverter2;
            this.encoded = encoded2;
        }

        /* access modifiers changed from: package-private */
        public void apply(RequestBuilder builder, T value) throws IOException {
            if (value == null) {
                throw new IllegalArgumentException("Path parameter \"" + this.name + "\" value must not be null.");
            }
            builder.addPathParam(this.name, this.valueConverter.convert(value), this.encoded);
        }
    }

    static final class Query<T> extends ParameterHandler<T> {
        private final boolean encoded;
        private final String name;
        private final Converter<T, String> valueConverter;

        Query(String name2, Converter<T, String> valueConverter2, boolean encoded2) {
            this.name = (String) Utils.checkNotNull(name2, "name == null");
            this.valueConverter = valueConverter2;
            this.encoded = encoded2;
        }

        /* access modifiers changed from: package-private */
        public void apply(RequestBuilder builder, T value) throws IOException {
            if (value != null) {
                builder.addQueryParam(this.name, this.valueConverter.convert(value), this.encoded);
            }
        }
    }

    static final class QueryName<T> extends ParameterHandler<T> {
        private final boolean encoded;
        private final Converter<T, String> nameConverter;

        QueryName(Converter<T, String> nameConverter2, boolean encoded2) {
            this.nameConverter = nameConverter2;
            this.encoded = encoded2;
        }

        /* access modifiers changed from: package-private */
        public void apply(RequestBuilder builder, T value) throws IOException {
            if (value != null) {
                builder.addQueryParam(this.nameConverter.convert(value), null, this.encoded);
            }
        }
    }

    static final class QueryMap<T> extends ParameterHandler<Map<String, T>> {
        private final boolean encoded;
        private final Converter<T, String> valueConverter;

        QueryMap(Converter<T, String> valueConverter2, boolean encoded2) {
            this.valueConverter = valueConverter2;
            this.encoded = encoded2;
        }

        /* access modifiers changed from: package-private */
        public void apply(RequestBuilder builder, Map<String, T> value) throws IOException {
            if (value == null) {
                throw new IllegalArgumentException("Query map was null.");
            }
            for (Map.Entry<String, T> entry : value.entrySet()) {
                String entryKey = entry.getKey();
                if (entryKey == null) {
                    throw new IllegalArgumentException("Query map contained null key.");
                }
                T entryValue = entry.getValue();
                if (entryValue == null) {
                    throw new IllegalArgumentException("Query map contained null value for key '" + entryKey + "'.");
                }
                builder.addQueryParam(entryKey, this.valueConverter.convert(entryValue), this.encoded);
            }
        }
    }

    static final class HeaderMap<T> extends ParameterHandler<Map<String, T>> {
        private final Converter<T, String> valueConverter;

        HeaderMap(Converter<T, String> valueConverter2) {
            this.valueConverter = valueConverter2;
        }

        /* access modifiers changed from: package-private */
        public void apply(RequestBuilder builder, Map<String, T> value) throws IOException {
            if (value == null) {
                throw new IllegalArgumentException("Header map was null.");
            }
            for (Map.Entry<String, T> entry : value.entrySet()) {
                String headerName = entry.getKey();
                if (headerName == null) {
                    throw new IllegalArgumentException("Header map contained null key.");
                }
                T headerValue = entry.getValue();
                if (headerValue == null) {
                    throw new IllegalArgumentException("Header map contained null value for key '" + headerName + "'.");
                }
                builder.addHeader(headerName, this.valueConverter.convert(headerValue));
            }
        }
    }

    static final class Field<T> extends ParameterHandler<T> {
        private final boolean encoded;
        private final String name;
        private final Converter<T, String> valueConverter;

        Field(String name2, Converter<T, String> valueConverter2, boolean encoded2) {
            this.name = (String) Utils.checkNotNull(name2, "name == null");
            this.valueConverter = valueConverter2;
            this.encoded = encoded2;
        }

        /* access modifiers changed from: package-private */
        public void apply(RequestBuilder builder, T value) throws IOException {
            if (value != null) {
                builder.addFormField(this.name, this.valueConverter.convert(value), this.encoded);
            }
        }
    }

    static final class FieldMap<T> extends ParameterHandler<Map<String, T>> {
        private final boolean encoded;
        private final Converter<T, String> valueConverter;

        FieldMap(Converter<T, String> valueConverter2, boolean encoded2) {
            this.valueConverter = valueConverter2;
            this.encoded = encoded2;
        }

        /* access modifiers changed from: package-private */
        public void apply(RequestBuilder builder, Map<String, T> value) throws IOException {
            if (value == null) {
                throw new IllegalArgumentException("Field map was null.");
            }
            for (Map.Entry<String, T> entry : value.entrySet()) {
                String entryKey = entry.getKey();
                if (entryKey == null) {
                    throw new IllegalArgumentException("Field map contained null key.");
                }
                T entryValue = entry.getValue();
                if (entryValue == null) {
                    throw new IllegalArgumentException("Field map contained null value for key '" + entryKey + "'.");
                }
                builder.addFormField(entryKey, this.valueConverter.convert(entryValue), this.encoded);
            }
        }
    }

    static final class Part<T> extends ParameterHandler<T> {
        private final Converter<T, RequestBody> converter;
        private final Headers headers;

        Part(Headers headers2, Converter<T, RequestBody> converter2) {
            this.headers = headers2;
            this.converter = converter2;
        }

        /* access modifiers changed from: package-private */
        public void apply(RequestBuilder builder, T value) {
            if (value != null) {
                try {
                    builder.addPart(this.headers, this.converter.convert(value));
                } catch (IOException e) {
                    throw new RuntimeException("Unable to convert " + ((Object) value) + " to RequestBody", e);
                }
            }
        }
    }

    static final class RawPart extends ParameterHandler<MultipartBody.Part> {
        static final RawPart INSTANCE = new RawPart();

        private RawPart() {
        }

        /* access modifiers changed from: package-private */
        public void apply(RequestBuilder builder, MultipartBody.Part value) throws IOException {
            if (value != null) {
                builder.addPart(value);
            }
        }
    }

    static final class PartMap<T> extends ParameterHandler<Map<String, T>> {
        private final String transferEncoding;
        private final Converter<T, RequestBody> valueConverter;

        PartMap(Converter<T, RequestBody> valueConverter2, String transferEncoding2) {
            this.valueConverter = valueConverter2;
            this.transferEncoding = transferEncoding2;
        }

        /* access modifiers changed from: package-private */
        public void apply(RequestBuilder builder, Map<String, T> value) throws IOException {
            if (value == null) {
                throw new IllegalArgumentException("Part map was null.");
            }
            for (Map.Entry<String, T> entry : value.entrySet()) {
                String entryKey = entry.getKey();
                if (entryKey == null) {
                    throw new IllegalArgumentException("Part map contained null key.");
                }
                T entryValue = entry.getValue();
                if (entryValue == null) {
                    throw new IllegalArgumentException("Part map contained null value for key '" + entryKey + "'.");
                }
                builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + entryKey + "\"", "Content-Transfer-Encoding", this.transferEncoding), this.valueConverter.convert(entryValue));
            }
        }
    }

    static final class Body<T> extends ParameterHandler<T> {
        private final Converter<T, RequestBody> converter;

        Body(Converter<T, RequestBody> converter2) {
            this.converter = converter2;
        }

        /* access modifiers changed from: package-private */
        public void apply(RequestBuilder builder, T value) {
            if (value == null) {
                throw new IllegalArgumentException("Body parameter value must not be null.");
            }
            try {
                builder.setBody(this.converter.convert(value));
            } catch (IOException e) {
                throw new RuntimeException("Unable to convert " + ((Object) value) + " to RequestBody", e);
            }
        }
    }
}
