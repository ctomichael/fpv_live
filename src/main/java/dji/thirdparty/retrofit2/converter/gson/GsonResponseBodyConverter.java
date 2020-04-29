package dji.thirdparty.retrofit2.converter.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import dji.thirdparty.okhttp3.ResponseBody;
import dji.thirdparty.retrofit2.Converter;
import java.io.IOException;

final class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final TypeAdapter<T> adapter;
    private final Gson gson;

    GsonResponseBodyConverter(Gson gson2, TypeAdapter<T> adapter2) {
        this.gson = gson2;
        this.adapter = adapter2;
    }

    public T convert(ResponseBody value) throws IOException {
        try {
            return this.adapter.read(this.gson.newJsonReader(value.charStream()));
        } finally {
            value.close();
        }
    }
}
