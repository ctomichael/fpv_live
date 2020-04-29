package dji.proto.djigo_services;

import android.util.Base64;
import com.dji.fieldAnnotation.EXClassNullAway;
import com.dji.proto.DJIProtoUtil;
import dji.proto.djigo_services.PostAbFuncsRequest;

@EXClassNullAway
public class AbTestRequestWrapper {
    public static final String CONTENT_TYPE = "application/x-www-form-urlencoded";
    public static final String FORM_DATA_KEY = "pb_data";
    private PostAbFuncsRequest abRequest;
    private String appUuid;
    private String funcUuid;
    private int productType;
    private byte[] requestData;

    public AbTestRequestWrapper(String appUuid2, String funcUuid2, int productType2) {
        this.appUuid = appUuid2;
        this.funcUuid = funcUuid2;
        this.productType = productType2;
    }

    public byte[] getRequestData() {
        PostAbFuncsRequest.Builder requestBuilder = new PostAbFuncsRequest.Builder();
        requestBuilder.app_uuid(this.appUuid);
        requestBuilder.func_uuid(this.funcUuid);
        requestBuilder.product_type(Integer.valueOf(this.productType));
        this.requestData = DJIProtoUtil.encodeDefaultPackageV1(requestBuilder.build());
        return this.requestData;
    }

    public String getRequestDataBase64() {
        this.requestData = getRequestData();
        return Base64.encodeToString(this.requestData, 2);
    }
}
