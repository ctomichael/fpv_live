package dji.proto.djigo_services;

import android.util.Base64;
import android.util.JsonReader;
import com.dji.fieldAnnotation.EXClassNullAway;
import com.dji.proto.DJIProtoUtil;
import dji.proto.djigo_services.reflect.HttpApiInjectManager;
import dji.publics.protocol.ResponseBase;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@EXClassNullAway
public class AbTestResponseWrapper {
    private static final String KEY = "FWIGEoCSCQpxqPm2L123DvM2Ios0YWtl";
    private static final String TEST_KEY = "0pnF9b4E0oICdlNFuXFWFCcQaBsssL2L";
    PostAbfuncsResponse abfuncsResponse;
    private String aesKey;
    private String message;
    private int status;

    public AbTestResponseWrapper(String response) {
        if (HttpApiInjectManager.getHttpApiToP3Injectable().isInnerPackage() || HttpApiInjectManager.getHttpApiToP3Injectable().isDevelopPackage()) {
            this.aesKey = KEY;
        } else {
            this.aesKey = KEY;
        }
        parseResponse(response);
    }

    private void parseResponse(String response) {
        InputStream is = new ByteArrayInputStream(response.getBytes());
        InputStreamReader isr = new InputStreamReader(is);
        JsonReader reader = new JsonReader(isr);
        try {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("status")) {
                    this.status = reader.nextInt();
                } else if (name.equals(ResponseBase.STRING_MESSAGE)) {
                    this.message = reader.nextString();
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();
            try {
                reader.close();
                isr.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e2) {
            e2.printStackTrace();
            try {
                reader.close();
                isr.close();
                is.close();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
        } catch (Throwable th) {
            try {
                reader.close();
                isr.close();
                is.close();
            } catch (IOException e4) {
                e4.printStackTrace();
            }
            throw th;
        }
    }

    private PostAbfuncsResponse getPostAbfuncsResponse() throws IOException {
        byte[] content = DJIProtoUtil.decodeDefaultPackageV1(Base64.decode(this.message, 2));
        if (content != null) {
            return PostAbfuncsResponse.ADAPTER.decode(content);
        }
        return new PostAbfuncsResponse(false, "");
    }

    public Boolean getResult() {
        try {
            if (this.abfuncsResponse == null) {
                return false;
            }
            this.abfuncsResponse = getPostAbfuncsResponse();
            return this.abfuncsResponse.ab_funcs_result;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getDecryptedAesResult() {
        try {
            this.abfuncsResponse = getPostAbfuncsResponse();
            if (this.abfuncsResponse != null) {
                return new String(DJIProtoUtil.decrypt(Base64.decode(this.abfuncsResponse.aes_result, 2), this.aesKey));
            }
            return "";
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
