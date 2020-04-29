package dji.internal.network;

import android.util.Base64;
import com.amap.location.common.model.AmapLoc;
import com.dji.api.ActiveHttpApi;
import com.dji.api.SDKHttpApi;
import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.logics.realname.BindingStateResponse;
import dji.internal.logics.realname.DeleteBindResponse;
import dji.internal.network.BaseRemoteService;
import dji.internal.network.HttpInterceptor;
import dji.log.DJILog;
import dji.thirdparty.afinal.utils.HttpsHelper;
import dji.thirdparty.okhttp3.MediaType;
import dji.thirdparty.okhttp3.MultipartBody;
import dji.thirdparty.okhttp3.OkHttpClient;
import dji.thirdparty.okhttp3.RequestBody;
import dji.thirdparty.okhttp3.ResponseBody;
import dji.thirdparty.retrofit2.Call;
import dji.thirdparty.retrofit2.Callback;
import dji.thirdparty.retrofit2.Response;
import dji.thirdparty.retrofit2.Retrofit;
import dji.thirdparty.retrofit2.converter.gson.GsonConverterFactory;
import java.io.File;
import java.util.List;

@EXClassNullAway
public class RemoteService extends BaseRemoteService {
    private static final String AUTH_HEADER_NAME = "Authorization";
    private static final String BASIC_AUTH = ("Basic " + Base64.encodeToString(String.format("%s:%s", userName, "").getBytes(), 2));
    private static final HttpInterceptor.Level LOGGING = HttpInterceptor.Level.NONE;
    private static final String PASSWORD = "";
    private static final String SDK_LOG_FILE_TYPE = "warrantyLog";
    private static final String TAG = "RemoteService";
    private static String baseUrl = SDKHttpApi.getSDKRemoteServerUrl();
    private static RemoteClient bindStateClient;
    private static String bindURL = ActiveHttpApi.getBindServiceBaseUrl();
    private static RemoteClient client;
    private static RemoteClient deleteBindingClient;
    private static String deleteBindingURL = ActiveHttpApi.getDeleteBindServiceUrl();
    private static RemoteService instance;
    private static String userName = SDKHttpApi.getSDKRemoteServerUserName();

    public static String getBaseUrl() {
        return baseUrl;
    }

    private RemoteService() {
        HttpInterceptor interceptor = new HttpInterceptor();
        interceptor.setLevel(LOGGING);
        interceptor.addHeader(AUTH_HEADER_NAME, BASIC_AUTH);
        client = (RemoteClient) new Retrofit.Builder().client(new OkHttpClient.Builder().addInterceptor(interceptor).sslSocketFactory(HttpsHelper.getDJISSLSocketFactoryForJavax(), HttpsHelper.getTrustManager()).build()).baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build().create(RemoteClient.class);
    }

    public static synchronized RemoteService getInstance() {
        RemoteService remoteService;
        synchronized (RemoteService.class) {
            if (instance == null) {
                instance = new RemoteService();
            }
            remoteService = instance;
        }
        return remoteService;
    }

    public void destroy() {
    }

    public void postAnalyticsEvents(List<DJIAnalyticsEvent> events, final BaseRemoteService.SDKRemoteServiceCallback callback) {
        client.postAnalyticsEvents(events).enqueue(new Callback<ResponseBody>() {
            /* class dji.internal.network.RemoteService.AnonymousClass1 */

            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (callback != null) {
                    callback.onSuccess(response);
                }
            }

            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                DJILog.d(RemoteService.TAG, "Failed to postAnalyticsEvents! " + throwable, new Object[0]);
                if (callback != null) {
                    callback.onFailure();
                }
            }
        });
    }

    public void getFeatureFlags(String appKey, String installID, String displayName, String sdkVersion, String locale, final BaseRemoteService.SDKRemoteServiceCallback callback) {
        client.getFeatureFlags(appKey, installID, displayName, "Android", sdkVersion, locale).enqueue(new Callback<DJIFeatureFlags>() {
            /* class dji.internal.network.RemoteService.AnonymousClass2 */

            public void onResponse(Call<DJIFeatureFlags> call, Response<DJIFeatureFlags> response) {
                if (callback == null) {
                    return;
                }
                if (response == null || !response.isSuccessful()) {
                    callback.onFailure();
                } else {
                    callback.onSuccess(response.body());
                }
            }

            public void onFailure(Call<DJIFeatureFlags> call, Throwable throwable) {
                DJILog.d(RemoteService.TAG, "Failed to getFeatureFlags! " + throwable, new Object[0]);
                if (callback != null) {
                    callback.onFailure();
                }
            }
        });
    }

    public void uploadLogZipFile(String filePath, String installId, String fileHash, final BaseRemoteService.SDKRemoteServiceCallback callback) {
        File file = new File(filePath);
        client.uploadLogZipFile(MultipartBody.Part.createFormData(AmapLoc.TYPE_OFFLINE_CELL, file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file)), SDK_LOG_FILE_TYPE, installId, fileHash).enqueue(new Callback<ResponseBody>() {
            /* class dji.internal.network.RemoteService.AnonymousClass3 */

            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (callback == null) {
                    return;
                }
                if (response == null || !response.isSuccessful()) {
                    callback.onFailure();
                } else {
                    callback.onSuccess(response.body());
                }
            }

            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                DJILog.d("retrofit", "Failed to  upload SDKLOG! " + throwable, new Object[0]);
                if (callback != null) {
                    callback.onFailure();
                }
            }
        });
    }

    public synchronized void getBindingStateFromServer(String sn, int mcc, String countryCode, final BaseRemoteService.SDKRemoteServiceCallback callback) {
        if (bindStateClient == null) {
            HttpInterceptor interceptor = new HttpInterceptor();
            interceptor.setLevel(LOGGING);
            bindStateClient = (RemoteClient) new Retrofit.Builder().client(new OkHttpClient.Builder().addInterceptor(interceptor).sslSocketFactory(HttpsHelper.getDJISSLSocketFactoryForJavax(), HttpsHelper.getTrustManager()).build()).baseUrl(bindURL).addConverterFactory(GsonConverterFactory.create()).build().create(RemoteClient.class);
        }
        bindStateClient.getBindingState(sn, mcc, countryCode).enqueue(new Callback<BindingStateResponse>() {
            /* class dji.internal.network.RemoteService.AnonymousClass4 */

            public void onResponse(Call<BindingStateResponse> call, Response<BindingStateResponse> response) {
                if (callback == null) {
                    return;
                }
                if (response == null || !response.isSuccessful()) {
                    callback.onFailure();
                } else {
                    callback.onSuccess(response.body());
                }
            }

            public void onFailure(Call<BindingStateResponse> call, Throwable throwable) {
                DJILog.d(RemoteService.TAG, "Failed to BindingInfo! " + throwable, new Object[0]);
                if (callback != null) {
                    callback.onFailure();
                }
            }
        });
    }

    public synchronized void deleteBindingInfo(String sn, final BaseRemoteService.SDKRemoteServiceCallback callback) {
        if (deleteBindingClient == null) {
            HttpInterceptor interceptor = new HttpInterceptor();
            interceptor.setLevel(LOGGING);
            deleteBindingClient = (RemoteClient) new Retrofit.Builder().client(new OkHttpClient.Builder().addInterceptor(interceptor).sslSocketFactory(HttpsHelper.getDJISSLSocketFactoryForJavax(), HttpsHelper.getTrustManager()).build()).baseUrl(deleteBindingURL).addConverterFactory(GsonConverterFactory.create()).build().create(RemoteClient.class);
        }
        deleteBindingClient.deleteBindInfo(sn).enqueue(new Callback<DeleteBindResponse>() {
            /* class dji.internal.network.RemoteService.AnonymousClass5 */

            public void onResponse(Call<DeleteBindResponse> call, Response<DeleteBindResponse> response) {
                if (callback == null) {
                    return;
                }
                if (response == null || !response.isSuccessful()) {
                    callback.onFailure();
                } else {
                    callback.onSuccess(response.body());
                }
            }

            public void onFailure(Call<DeleteBindResponse> call, Throwable throwable) {
                DJILog.d(RemoteService.TAG, "Failed to delete BindingInfo! " + throwable, new Object[0]);
                if (callback != null) {
                    callback.onFailure();
                }
            }
        });
    }
}
