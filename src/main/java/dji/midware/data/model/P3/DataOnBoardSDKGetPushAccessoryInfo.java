package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.midware.data.manager.P3.DataBase;
import java.util.concurrent.atomic.AtomicBoolean;

@Keep
public class DataOnBoardSDKGetPushAccessoryInfo extends DataBase {
    private static final int BEACON = 2;
    private static final int SEARCHLIGHT = 1;
    private static final int SPEAKER = 3;
    private AtomicBoolean isBeaconConnected = new AtomicBoolean(false);
    private AtomicBoolean isSearchlightConnected = new AtomicBoolean(false);
    private AtomicBoolean isSpeakerConnected = new AtomicBoolean(false);

    public static DataOnBoardSDKGetPushAccessoryInfo getInstance() {
        return SingletonHolder.instance;
    }

    @Keep
    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final DataOnBoardSDKGetPushAccessoryInfo instance = new DataOnBoardSDKGetPushAccessoryInfo();

        private SingletonHolder() {
        }
    }

    /* access modifiers changed from: protected */
    public boolean isChanged(byte[] data) {
        return true;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public boolean isAccessoryConnected() {
        return ((Integer) get(0, 1, Integer.class)).intValue() > 0;
    }

    public boolean isSearchlightConnected() {
        return this.isSearchlightConnected.get();
    }

    public boolean isNavigationLEDConnected() {
        return this.isBeaconConnected.get();
    }

    public boolean isSpeakerConnected() {
        return this.isSpeakerConnected.get();
    }

    /* access modifiers changed from: protected */
    public void setPushRecData(byte[] data) {
        clearCacheData();
        if (data != null && data.length > 1) {
            int detailInfoLength = data.length - 1;
            byte[] detailInfo = new byte[detailInfoLength];
            System.arraycopy(data, 1, detailInfo, 0, detailInfoLength);
            int offset = 0;
            while (offset < detailInfoLength) {
                byte b = detailInfo[offset];
                int offset2 = offset + 1;
                byte b2 = detailInfo[offset2];
                offset = offset2 + 1;
                if (b2 > 0) {
                    byte[] bodyData = new byte[b2];
                    System.arraycopy(detailInfo, offset, bodyData, 0, b2);
                    offset += b2;
                    switch (b) {
                        case 1:
                            this.isSearchlightConnected.compareAndSet(false, true);
                            DataOnBoardSDKGetPushSearchlightInfo.getInstance().setPushRecData(bodyData);
                            continue;
                        case 2:
                            this.isBeaconConnected.compareAndSet(false, true);
                            DataOnBoardSDKGetPushBeaconInfo.getInstance().setPushRecData(bodyData);
                            continue;
                        case 3:
                            this.isSpeakerConnected.compareAndSet(false, true);
                            DataOnBoardSDKGetPushSpeakerInfo.getInstance().setPushRecData(bodyData);
                            continue;
                    }
                }
            }
        }
        super.setPushRecData(data);
    }

    private void clearCacheData() {
        this.isSearchlightConnected.compareAndSet(true, false);
        this.isBeaconConnected.compareAndSet(true, false);
        this.isSpeakerConnected.compareAndSet(true, false);
    }
}
