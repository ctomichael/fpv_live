package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import android.text.TextUtils;
import dji.midware.data.model.P3.DataOnBoardSDKSetAccessoryCommonParams;
import dji.midware.util.BytesUtil;
import java.util.ArrayList;

@Keep
public class DataOnBoardSDKSetSpeakerParams extends DataOnBoardSDKSetAccessoryCommonParams {
    private static final int MAX_FILE_NAME_LENGTH = 128;
    private ControlType controlType = ControlType.STOP;
    private int fileIndex;
    private byte[] fileNameData;
    private ArrayList<Integer> indexs = new ArrayList<>();
    private PlayMode mode = PlayMode.SINGLE_HEAD;
    private int volume;

    public DataOnBoardSDKSetSpeakerParams() {
        super(DataOnBoardSDKSetAccessoryCommonParams.AccessoryType.SPEAKER);
    }

    public DataOnBoardSDKSetSpeakerParams setParamType(int type) {
        this.paramType = type;
        return this;
    }

    public DataOnBoardSDKSetSpeakerParams setVolume(int volume2) {
        this.volume = volume2;
        return this;
    }

    public DataOnBoardSDKSetSpeakerParams setPlayMode(PlayMode mode2) {
        this.mode = mode2;
        return this;
    }

    public DataOnBoardSDKSetSpeakerParams setControlType(ControlType controlType2) {
        this.controlType = controlType2;
        return this;
    }

    public DataOnBoardSDKSetSpeakerParams setIndexList(ArrayList<Integer> indexList) {
        this.indexs = indexList;
        return this;
    }

    public DataOnBoardSDKSetSpeakerParams setFileIndex(int fileIndex2) {
        this.fileIndex = fileIndex2;
        return this;
    }

    public DataOnBoardSDKSetSpeakerParams setFileName(String fileName) {
        if (!TextUtils.isEmpty(fileName)) {
            this.fileNameData = BytesUtil.getBytesUTF8(fileName + "\u0000");
        }
        return this;
    }

    /* access modifiers changed from: protected */
    public byte[] getParamData() {
        switch (this.paramType) {
            case 1:
            case 240:
                return getVolumeData();
            case 2:
                return getPlayControlData();
            case 3:
                return getPlayModeData();
            case 4:
                return getFileRenameData();
            default:
                return new byte[0];
        }
    }

    private byte[] getVolumeData() {
        return new byte[]{BytesUtil.getByte(this.volume)};
    }

    private byte[] getPlayModeData() {
        return new byte[]{BytesUtil.getByte(this.mode.value())};
    }

    private byte[] getPlayControlData() {
        int indexSize;
        if (this.indexs != null) {
            indexSize = this.indexs.size();
        } else {
            indexSize = 0;
        }
        byte[] data = new byte[((indexSize * 4) + 2)];
        data[0] = (byte) this.controlType.value();
        int index = 0 + 1;
        data[index] = (byte) indexSize;
        int index2 = index + 1;
        if (indexSize > 0) {
            for (int i = 0; i < indexSize; i++) {
                System.arraycopy(BytesUtil.getBytes(this.indexs.get(i).intValue()), 0, data, index2, 4);
                index2 += 4;
            }
        }
        return data;
    }

    private byte[] getFileRenameData() {
        int fileNameLength;
        if (this.fileNameData != null) {
            fileNameLength = this.fileNameData.length;
        } else {
            fileNameLength = 0;
        }
        if (fileNameLength > 128) {
            fileNameLength = 128;
        }
        byte[] data = new byte[(fileNameLength + 5)];
        System.arraycopy(BytesUtil.getBytes(this.fileIndex), 0, data, 0, 4);
        int index = 0 + 4;
        data[index] = BytesUtil.getByte(fileNameLength);
        int index2 = index + 1;
        if (fileNameLength > 0) {
            System.arraycopy(this.fileNameData, 0, data, index2, fileNameLength);
        }
        return data;
    }

    @Keep
    public enum ParamType {
        VOLUME(1),
        PLAY_CONTROL(2),
        PLAY_MODE(3),
        RENAME_FILE(4),
        PLAY_TEST(240);
        
        private final int mValue;

        private ParamType(int value) {
            this.mValue = value;
        }

        public int value() {
            return this.mValue;
        }
    }

    @Keep
    public enum ControlType {
        STOP(0),
        PLAY(1);
        
        private final int mValue;

        private ControlType(int value) {
            this.mValue = value;
        }

        public int value() {
            return this.mValue;
        }
    }

    @Keep
    public enum PlayMode {
        SINGLE_HEAD(0),
        SINGLE_CYCLE(1),
        LIST_ORDER(2),
        LIST_CYCLE(3);
        
        private final int mValue;

        private PlayMode(int value) {
            this.mValue = value;
        }

        public int value() {
            return this.mValue;
        }

        private boolean _equals(int b) {
            return this.mValue == b;
        }

        public static PlayMode find(int b) {
            PlayMode result = SINGLE_HEAD;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
