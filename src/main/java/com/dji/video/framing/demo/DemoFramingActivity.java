package com.dji.video.framing.demo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import com.dji.video.framing.R;
import com.dji.video.framing.internal.parser.IFrameParser;
import com.dji.video.framing.utils.BytesUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

public class DemoFramingActivity extends Activity {
    private static final String TAG = "[DECODER_INPUT]";
    IFrameParser parser;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_framing);
        InputStream tmpInputStream = null;
        try {
            tmpInputStream = new FileInputStream(new File(IFrameParser.MOCK_MAVIC_VIDEO_FILE));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.parser = IFrameParser.CREATOR.create(false);
        this.parser.setFrameParserListener(new IFrameParser.OnFrameParserListener() {
            /* class com.dji.video.framing.demo.DemoFramingActivity.AnonymousClass1 */

            public void onRecvData(byte[] videoBuffer, int size, boolean isKeyFrame, int newWidth, int newHeight, int frameNum, int spsPos, int spsLen, int ppsPos, int ppsLen, int fps, boolean ishevcmode) {
                Log.i(DemoFramingActivity.TAG, String.format(Locale.US, " word 0: %d word 1: %d word 2: %d size=%d  w=%d, h=%d, isKeyFrame=%b, time=%d \n", Integer.valueOf(BytesUtil.getInt(videoBuffer, 0)), Integer.valueOf(BytesUtil.getInt(videoBuffer, 4)), Integer.valueOf(BytesUtil.getInt(videoBuffer, 8)), Integer.valueOf(size), Integer.valueOf(newWidth), Integer.valueOf(newHeight), Boolean.valueOf(isKeyFrame), Long.valueOf(System.currentTimeMillis())));
            }
        });
        new Thread(new MockDataRunnable(tmpInputStream) {
            /* class com.dji.video.framing.demo.DemoFramingActivity.AnonymousClass2 */

            public void onDataComing(byte[] data, int offset, int len) {
                DemoFramingActivity.this.parser.feedData(data, offset, len);
            }

            public void onDestroy() {
                DemoFramingActivity.this.parser.stop();
            }
        }).start();
    }

    public static abstract class MockDataRunnable implements Runnable {
        private boolean isStop = false;
        private InputStream sourceStream;

        public abstract void onDataComing(byte[] bArr, int i, int i2);

        public abstract void onDestroy();

        public MockDataRunnable(InputStream sourceStream2) {
            this.sourceStream = sourceStream2;
        }

        public void stop() {
            this.isStop = true;
        }

        public void run() {
            byte[] buffer = new byte[16384];
            while (!this.isStop && this.sourceStream != null) {
                try {
                    int length = this.sourceStream.read(buffer);
                    if (length > 0) {
                        onDataComing(buffer, 0, length);
                    } else {
                        this.isStop = true;
                        Log.e(DemoFramingActivity.TAG, "stop: inputStream.read() get no more data.");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                if (this.sourceStream != null) {
                    this.sourceStream.close();
                }
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            onDestroy();
        }
    }
}
