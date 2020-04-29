package dji.thirdparty.retrofit2;

import dji.thirdparty.okhttp3.Request;
import dji.thirdparty.retrofit2.CallAdapter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.concurrent.Executor;

final class ExecutorCallAdapterFactory extends CallAdapter.Factory {
    final Executor callbackExecutor;

    ExecutorCallAdapterFactory(Executor callbackExecutor2) {
        this.callbackExecutor = callbackExecutor2;
    }

    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        if (getRawType(returnType) != Call.class) {
            return null;
        }
        final Type responseType = Utils.getCallResponseType(returnType);
        return new CallAdapter<Object, Call<?>>() {
            /* class dji.thirdparty.retrofit2.ExecutorCallAdapterFactory.AnonymousClass1 */

            public Type responseType() {
                return responseType;
            }

            public Call<Object> adapt(Call<Object> call) {
                return new ExecutorCallbackCall(ExecutorCallAdapterFactory.this.callbackExecutor, call);
            }
        };
    }

    static final class ExecutorCallbackCall<T> implements Call<T> {
        final Executor callbackExecutor;
        final Call<T> delegate;

        ExecutorCallbackCall(Executor callbackExecutor2, Call<T> delegate2) {
            this.callbackExecutor = callbackExecutor2;
            this.delegate = delegate2;
        }

        public void enqueue(final Callback<T> callback) {
            if (callback == null) {
                throw new NullPointerException("callback == null");
            }
            this.delegate.enqueue(new Callback<T>() {
                /* class dji.thirdparty.retrofit2.ExecutorCallAdapterFactory.ExecutorCallbackCall.AnonymousClass1 */

                public void onResponse(Call<T> call, final Response<T> response) {
                    ExecutorCallbackCall.this.callbackExecutor.execute(new Runnable() {
                        /* class dji.thirdparty.retrofit2.ExecutorCallAdapterFactory.ExecutorCallbackCall.AnonymousClass1.AnonymousClass1 */

                        public void run() {
                            if (ExecutorCallbackCall.this.delegate.isCanceled()) {
                                callback.onFailure(ExecutorCallbackCall.this, new IOException("Canceled"));
                            } else {
                                callback.onResponse(ExecutorCallbackCall.this, response);
                            }
                        }
                    });
                }

                public void onFailure(Call<T> call, final Throwable t) {
                    ExecutorCallbackCall.this.callbackExecutor.execute(new Runnable() {
                        /* class dji.thirdparty.retrofit2.ExecutorCallAdapterFactory.ExecutorCallbackCall.AnonymousClass1.AnonymousClass2 */

                        public void run() {
                            callback.onFailure(ExecutorCallbackCall.this, t);
                        }
                    });
                }
            });
        }

        public boolean isExecuted() {
            return this.delegate.isExecuted();
        }

        public Response<T> execute() throws IOException {
            return this.delegate.execute();
        }

        public void cancel() {
            this.delegate.cancel();
        }

        public boolean isCanceled() {
            return this.delegate.isCanceled();
        }

        public Call<T> clone() {
            return new ExecutorCallbackCall(this.callbackExecutor, this.delegate.clone());
        }

        public Request request() {
            return this.delegate.request();
        }
    }
}
