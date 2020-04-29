package com.dji.permission.checker;

import android.media.MediaRecorder;
import java.io.File;

class RecordAudioTest implements PermissionTest {
    private MediaRecorder mRecorder;
    private File mTempFile;

    RecordAudioTest() {
        this.mTempFile = null;
        this.mRecorder = null;
        this.mRecorder = new MediaRecorder();
    }

    public boolean test() throws Throwable {
        try {
            this.mTempFile = File.createTempFile("permission", "test");
            this.mRecorder.setAudioSource(1);
            this.mRecorder.setOutputFormat(3);
            this.mRecorder.setAudioEncoder(1);
            this.mRecorder.setOutputFile(this.mTempFile.getAbsolutePath());
            this.mRecorder.prepare();
            this.mRecorder.start();
            return true;
        } finally {
            stop();
        }
    }

    private void stop() {
        if (this.mRecorder != null) {
            try {
                this.mRecorder.stop();
            } catch (Exception e) {
            }
            try {
                this.mRecorder.release();
            } catch (Exception e2) {
            }
        }
        if (this.mTempFile != null && this.mTempFile.exists()) {
            this.mTempFile.delete();
        }
    }
}
