package dji.thirdparty.afinal.http.entityhandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpEntity;

public class StringEntityHandler {
    public Object handleEntity(HttpEntity entity, EntityCallBack callback, String charset) throws IOException {
        if (entity == null) {
            return null;
        }
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        long count = entity.getContentLength();
        long curCount = 0;
        InputStream is = entity.getContent();
        while (true) {
            int len = is.read(buffer);
            if (len == -1) {
                break;
            }
            outStream.write(buffer, 0, len);
            curCount += (long) len;
            if (callback != null) {
                callback.callBack(count, curCount, false);
            }
        }
        if (callback != null) {
            callback.callBack(count, curCount, true);
        }
        byte[] data = outStream.toByteArray();
        outStream.close();
        is.close();
        return new String(data, charset);
    }
}
