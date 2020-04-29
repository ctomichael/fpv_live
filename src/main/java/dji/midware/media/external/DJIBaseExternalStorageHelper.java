package dji.midware.media.external;

import android.support.v4.provider.DocumentFile;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.util.FileStreamCopyController;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

public abstract class DJIBaseExternalStorageHelper {
    private static final String TAG = "DJIBaseExternalStorageHelper";
    protected FileStreamCopyController copyController;
    protected DocumentFile externalFileDf;
    protected OutputStream externalFileStream;
    protected String filePath;

    protected DJIBaseExternalStorageHelper(String filePath2) {
        this.filePath = filePath2;
        this.copyController = new FileStreamCopyController(filePath2);
    }

    public DJIBaseExternalStorageHelper(String filePath2, DocumentFile documentFile) {
        this(filePath2);
        this.externalFileDf = documentFile;
        try {
            this.externalFileStream = ServiceManager.getContext().getContentResolver().openOutputStream(documentFile.getUri());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        if (this.copyController != null) {
            this.copyController.start(this.externalFileStream);
        }
    }

    public void pause() {
        if (this.copyController != null) {
            this.copyController.pause();
        }
    }

    public void resume() {
        if (this.copyController != null) {
            this.copyController.resume();
        }
    }

    public void forceStop() {
        if (this.copyController != null) {
            this.copyController.forceStop();
        }
    }

    public void stop(boolean needDeleteSrcFile) {
        if (this.externalFileDf != null) {
            try {
                if (this.copyController != null) {
                    this.copyController.stop(this.externalFileDf, getCheckLen(), needDeleteSrcFile);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /* access modifiers changed from: protected */
    public int getCheckLen() throws IOException {
        return 0;
    }
}
