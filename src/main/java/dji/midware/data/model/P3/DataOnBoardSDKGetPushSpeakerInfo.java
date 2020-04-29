package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.midware.data.manager.P3.DataBase;

@Keep
public class DataOnBoardSDKGetPushSpeakerInfo extends DataBase {
    public static DataOnBoardSDKGetPushSpeakerInfo getInstance() {
        return SingletonHolder.instance;
    }

    @Keep
    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final DataOnBoardSDKGetPushSpeakerInfo instance = new DataOnBoardSDKGetPushSpeakerInfo();

        private SingletonHolder() {
        }
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public void setPushRecData(byte[] data) {
        super.setPushRecData(data);
    }

    public int getSpeakerVolume() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public boolean isSpeakerPlaying() {
        return (((Integer) get(1, 1, Integer.class)).intValue() & 3) != 0;
    }

    public int getSpeakerDataSource() {
        return ((Integer) get(1, 1, Integer.class)).intValue() & 3;
    }

    public int getSpeakerPlaybackMode() {
        return (((Integer) get(1, 1, Integer.class)).intValue() >> 2) & 7;
    }

    public int getPlayingSoundID() {
        return ((Integer) get(2, 4, Integer.class)).intValue();
    }
}
