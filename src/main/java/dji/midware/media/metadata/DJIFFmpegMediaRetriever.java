package dji.midware.media.metadata;

import dji.component.mediaprovider.DJIMediaStore;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.media.MediaLogger;
import dji.midware.natives.FPVController;
import java.util.HashMap;

@EXClassNullAway
public class DJIFFmpegMediaRetriever {
    private static final String KEY_DURATION = "duration";
    private static final String KEY_FRAMERATE = "framerate";
    private static final String KEY_HEIGHT = "height";
    private static final String KEY_ROTATION = "rotate";
    private static final String KEY_WIDTH = "width";
    private static final String TAG = "DJIFFmpegMediaRetriver";
    private HashMap<String, String> metaData = null;
    private String path;

    public void setDataSource(String path2) {
        this.path = path2;
        MediaLogger.d(TAG, "Retrieving metadata: " + path2);
    }

    public void getMetaData() {
        if (this.metaData == null) {
            this.metaData = FPVController.jni_demuxer_getMetadata(this.path);
            MediaLogger.i(TAG, "metaData=" + this.metaData);
        }
    }

    public int getDurationMs() {
        int re = 0;
        getMetaData();
        if (this.metaData != null) {
            try {
                re = Integer.parseInt(this.metaData.get("duration"));
            } catch (Exception e) {
            }
        }
        MediaLogger.i(TAG, "duration (ms)=" + re);
        return re;
    }

    public int getVideoWidth() {
        int re = 0;
        getMetaData();
        if (this.metaData != null) {
            try {
                re = Integer.parseInt(this.metaData.get("width"));
            } catch (Exception e) {
            }
        }
        MediaLogger.i(TAG, "video width=" + re);
        return re;
    }

    public int getVideoHeight() {
        int re = 0;
        getMetaData();
        if (this.metaData != null) {
            try {
                re = Integer.parseInt(this.metaData.get("height"));
            } catch (Exception e) {
            }
        }
        MediaLogger.i(TAG, "video height=" + re);
        return re;
    }

    public float getRotation() {
        float re = 0.0f;
        getMetaData();
        if (this.metaData != null) {
            if (this.metaData.containsKey(DJIMediaStore.FileColumns.ROTATION)) {
                re = Float.parseFloat(this.metaData.get(DJIMediaStore.FileColumns.ROTATION));
            } else if (this.metaData.containsKey(KEY_ROTATION)) {
                re = Float.parseFloat(this.metaData.get(KEY_ROTATION));
            }
        }
        MediaLogger.i(TAG, "rotate=" + re);
        return re;
    }

    public float getFrameRate() {
        float re = 0.0f;
        getMetaData();
        if (this.metaData != null) {
            try {
                re = Float.parseFloat(this.metaData.get(KEY_FRAMERATE));
            } catch (Exception e) {
                MediaLogger.e(TAG, e);
            }
        }
        MediaLogger.i(TAG, "frame rate=" + re);
        return re;
    }
}
