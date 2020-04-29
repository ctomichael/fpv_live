package dji.internal.upgrade;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class DJIUpDataModel {
    public long file_size;
    public boolean isDownloaded;
    public String savePath;
    public String sign;
    public int status;
    public boolean update = false;
    public String url;
    public String version;

    public DJIUpDataModel() {
        reset();
    }

    public void reset() {
        this.status = 1;
        this.version = "";
        this.url = "";
        this.update = false;
        this.sign = "";
        this.file_size = 0;
        this.isDownloaded = false;
    }

    public boolean isUpdateNeeded() {
        return (this.status == 0 || this.update) && this.url != null;
    }

    public static boolean isUpdateNeeded(DJIUpDataModel model) {
        return model != null && model.isUpdateNeeded();
    }

    public static boolean isForceUpdateNeeded(DJIUpDataModel model) {
        return model != null && model.status == 0 && model.url != null && model.update;
    }
}
