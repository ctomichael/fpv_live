package dji.midware.util;

import android.util.Log;
import java.io.File;
import java.io.IOException;
import okio.BufferedSink;
import okio.Okio;
import okio.Sink;

public class FileRecorder {
    private BufferedSink bufferedSink;
    private String filePath;
    private Sink sink;

    public FileRecorder(String path) {
        this.filePath = path;
        try {
            File file = new File(this.filePath);
            if (file.exists()) {
                file.delete();
            }
            this.sink = Okio.sink(file);
            this.bufferedSink = Okio.buffer(this.sink);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onRecvData(byte[] data, int offset, int len) {
        try {
            this.bufferedSink.write(data, offset, len);
            this.bufferedSink.flush();
            Log.d("FileRecorder", "onRecvData len : " + len);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void release() {
        if (this.sink != null) {
            try {
                this.bufferedSink.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                this.sink.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            this.sink = null;
            this.bufferedSink = null;
        }
    }
}
