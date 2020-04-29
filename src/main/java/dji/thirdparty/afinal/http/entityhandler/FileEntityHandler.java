package dji.thirdparty.afinal.http.entityhandler;

import android.text.TextUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpEntity;

public class FileEntityHandler {
    private boolean mStop = false;

    public boolean isStop() {
        return this.mStop;
    }

    public void setStop(boolean stop) {
        this.mStop = stop;
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.io.FileOutputStream.<init>(java.lang.String, boolean):void throws java.io.FileNotFoundException}
     arg types: [java.lang.String, int]
     candidates:
      ClspMth{java.io.FileOutputStream.<init>(java.io.File, boolean):void throws java.io.FileNotFoundException}
      ClspMth{java.io.FileOutputStream.<init>(java.lang.String, boolean):void throws java.io.FileNotFoundException} */
    public Object handleEntity(HttpEntity entity, EntityCallBack callback, String target, boolean isResume, boolean checkContentLength) throws IOException {
        FileOutputStream os;
        int readLen;
        if (TextUtils.isEmpty(target) || target.trim().length() == 0) {
            return null;
        }
        File targetFile = new File(target);
        if (!targetFile.getParentFile().exists()) {
            targetFile.getParentFile().mkdirs();
        }
        if (!targetFile.exists()) {
            targetFile.createNewFile();
        }
        if (this.mStop) {
            return targetFile;
        }
        long current = 0;
        if (isResume) {
            current = targetFile.length();
            os = new FileOutputStream(target, true);
        } else {
            os = new FileOutputStream(target);
        }
        if (this.mStop) {
            os.close();
            return targetFile;
        }
        long length = entity.getContentLength();
        if (!checkContentLength || length >= 0) {
            InputStream input = entity.getContent();
            long count = length + current;
            if ((!checkContentLength || current < count) && !this.mStop) {
                byte[] buffer = new byte[1024];
                while (!this.mStop && ((!checkContentLength || current < count) && (readLen = input.read(buffer, 0, 1024)) > 0)) {
                    os.write(buffer, 0, readLen);
                    current += (long) readLen;
                    callback.callBack(count, current, false);
                }
                os.close();
                input.close();
                callback.callBack(count, current, true);
                if (!this.mStop || current >= count) {
                    return targetFile;
                }
                throw new IOException("user stop download thread");
            }
            os.close();
            input.close();
            return targetFile;
        }
        os.close();
        return "getContentLength " + length;
    }
}
