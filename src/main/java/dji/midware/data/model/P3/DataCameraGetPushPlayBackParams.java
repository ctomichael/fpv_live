package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataCameraGetPushPlayBackParams extends DataBase {
    private static DataCameraGetPushPlayBackParams instance = null;

    public static synchronized DataCameraGetPushPlayBackParams getInstance() {
        DataCameraGetPushPlayBackParams dataCameraGetPushPlayBackParams;
        synchronized (DataCameraGetPushPlayBackParams.class) {
            if (instance == null) {
                instance = new DataCameraGetPushPlayBackParams();
            }
            dataCameraGetPushPlayBackParams = instance;
        }
        return dataCameraGetPushPlayBackParams;
    }

    public MODE getMode() {
        return getMode(-1);
    }

    public MODE getMode(int index) {
        return MODE.find(((Integer) get(0, 1, Integer.class, index)).intValue());
    }

    public FileType getFileType() {
        return FileType.find(((Integer) get(1, 2, Integer.class)).intValue());
    }

    public FileType[] getFileTypes(int size) {
        int msize = 8;
        int data = ((Integer) get(1, 2, Integer.class)).intValue();
        if (size <= 8) {
            msize = size;
        }
        FileType[] result = new FileType[msize];
        for (int i = 0; i < msize; i++) {
            result[i] = ((1 << i) & data) == 1 ? FileType.VIDEO : FileType.JPEG;
        }
        return result;
    }

    public int getFileNum() {
        return ((Integer) get(3, 1, Integer.class)).intValue();
    }

    public int getTotalNum() {
        return ((Integer) get(4, 2, Integer.class)).intValue();
    }

    public int getIndex() {
        return ((Integer) get(6, 2, Integer.class)).intValue();
    }

    public int getIndexForKumquat() {
        return ((Integer) get(4, 4, Integer.class)).intValue();
    }

    public int getProgress() {
        return ((Integer) get(8, 1, Integer.class)).intValue();
    }

    public int getTotalTime() {
        return ((Integer) get(9, 2, Integer.class)).intValue();
    }

    public int getTotalTimeForKumquat() {
        return ((Integer) get(9, 4, Integer.class)).intValue();
    }

    public int getCurrent() {
        return ((Integer) get(11, 2, Integer.class)).intValue();
    }

    public int getCurrentForKumquat() {
        return ((Integer) get(13, 4, Integer.class)).intValue();
    }

    public int getDeleteChioceNum() {
        return ((Integer) get(13, 2, Integer.class)).intValue();
    }

    public float getZoomSize() {
        return (((float) ((Integer) get(15, 2, Integer.class)).intValue()) * 1.0f) / 100.0f;
    }

    public int getTotalPhotoNum() {
        return ((Integer) get(17, 2, Integer.class)).intValue();
    }

    public int getTotalVideoNum() {
        return ((Integer) get(19, 2, Integer.class)).intValue();
    }

    public int getPhotoHeight() {
        return ((Integer) get(25, 4, Integer.class)).intValue();
    }

    public int getPhotoWidth() {
        return ((Integer) get(21, 4, Integer.class)).intValue();
    }

    public int getCenterX() {
        return ((Integer) get(29, 4, Integer.class)).intValue();
    }

    public int getCenterY() {
        return ((Integer) get(33, 4, Integer.class)).intValue();
    }

    public String getFileName() {
        return "";
    }

    public boolean isCurPageSelected() {
        return ((Integer) get(37, 1, Integer.class)).intValue() == 1;
    }

    public DelFileStatus getDelFileStatus() {
        return DelFileStatus.find(((Integer) get(38, 1, Integer.class)).intValue());
    }

    public boolean isSelectFileValid() {
        return ((Integer) get(39, 1, Integer.class)).intValue() == 0;
    }

    public boolean isSingleDownloaded() {
        return ((Integer) get(40, 1, Integer.class)).intValue() == 1;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    @Keep
    public enum MODE {
        Single(0),
        SingleLarge(1),
        SinglePlay(2),
        SinglePause(3),
        MultipleDel(4),
        Multiple(5),
        Download(6),
        SingleOver(7),
        SingleLoading(8),
        TranscodeError(9),
        OTHER(100);
        
        private int data;

        private MODE(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static MODE find(int b) {
            MODE result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum FileType {
        JPEG(0),
        DNG(1),
        VIDEO(2),
        OTHER(100);
        
        private int data;

        private FileType(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static FileType find(int b) {
            FileType result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum DelFileStatus {
        NORMAL(0),
        DELETING(2),
        COMPLETED(3);
        
        private int data;

        private DelFileStatus(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static DelFileStatus find(int b) {
            DelFileStatus result = NORMAL;
            DelFileStatus[] values = values();
            for (DelFileStatus fs : values) {
                if (fs._equals(b)) {
                    return fs;
                }
            }
            return result;
        }
    }
}
