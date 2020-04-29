package dji.thirdparty.retrofit2;

import dji.thirdparty.retrofit2.CallAdapter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

final class DefaultCallAdapterFactory extends CallAdapter.Factory {
    static final CallAdapter.Factory INSTANCE = new DefaultCallAdapterFactory();

    DefaultCallAdapterFactory() {
    }

    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        if (getRawType(returnType) != Call.class) {
            return null;
        }
        final Type responseType = Utils.getCallResponseType(returnType);
        return new CallAdapter<Object, Call<?>>() {
            /* class dji.thirdparty.retrofit2.DefaultCallAdapterFactory.AnonymousClass1 */

            public Type responseType() {
                return responseType;
            }

            public Call<Object> adapt(Call<Object> call) {
                return call;
            }
        };
    }
}
