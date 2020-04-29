package dji.internal.network;

import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.logics.realname.BindingStateResponse;
import dji.internal.logics.realname.DeleteBindResponse;
import dji.thirdparty.okhttp3.MultipartBody;
import dji.thirdparty.okhttp3.ResponseBody;
import dji.thirdparty.retrofit2.Call;
import dji.thirdparty.retrofit2.http.Body;
import dji.thirdparty.retrofit2.http.GET;
import dji.thirdparty.retrofit2.http.Multipart;
import dji.thirdparty.retrofit2.http.POST;
import dji.thirdparty.retrofit2.http.Part;
import dji.thirdparty.retrofit2.http.Query;
import java.util.List;

@EXClassNullAway
public interface RemoteClient {
    public static final String EVENT_SUB_URI = "events";
    public static final String FLAG_SUB_URI = "flags";
    public static final String LOG_SUB_URI = "upload";
    public static final String bind_state_SUB_URI = "bind_service";
    public static final String delete_bind_SUB_URI = "test_delete_bind_info";

    @GET(delete_bind_SUB_URI)
    Call<DeleteBindResponse> deleteBindInfo(@Query("main_sn") String str);

    @GET("bind_service")
    Call<BindingStateResponse> getBindingState(@Query("main_sn") String str, @Query("mcc") int i, @Query("cc") String str2);

    @GET(FLAG_SUB_URI)
    Call<DJIFeatureFlags> getFeatureFlags(@Query("appKey") String str, @Query("installId") String str2, @Query("displayName") String str3, @Query("platform") String str4, @Query("sdkVersion") String str5, @Query("locale") String str6);

    @POST(EVENT_SUB_URI)
    Call<ResponseBody> postAnalyticsEvents(@Body List<DJIAnalyticsEvent> list);

    @POST(LOG_SUB_URI)
    @Multipart
    Call<ResponseBody> uploadLogZipFile(@Part MultipartBody.Part part, @Query("type") String str, @Query("installId") String str2, @Query("hash") String str3);
}
