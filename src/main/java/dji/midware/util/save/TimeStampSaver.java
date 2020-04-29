package dji.midware.util.save;

import dji.midware.util.save.TimeStampSavingManager;
import java.util.LinkedList;

public class TimeStampSaver {
    private Object dataLock = new Object();
    private LinkedList<Long> keyList = new LinkedList<>();
    private TimeStampSavingManager.TimeStampSavePoint savePoint = TimeStampSavingManager.TimeStampSavePoint.Unknown;
    private LinkedList<Long> timeStampList = new LinkedList<>();

    public static class TimeStampSaverData {
        public TimeStampSavingManager.TimeStampSavePoint savePoint;
        public Long[] timeStamps;
        public Long[] trackKeys;

        public TimeStampSaverData(TimeStampSavingManager.TimeStampSavePoint savePoint2, Long[] trackKeys2, Long[] timeStamps2) {
            this.savePoint = savePoint2;
            this.trackKeys = trackKeys2;
            this.timeStamps = timeStamps2;
        }
    }

    public TimeStampSaver(TimeStampSavingManager.TimeStampSavePoint savePoint2) {
        this.savePoint = savePoint2;
    }

    public void saveTimeStamp(long trackKey, long timeStampInMs) {
        synchronized (this.dataLock) {
            if (this.keyList.offer(Long.valueOf(trackKey))) {
                if (!this.timeStampList.offer(Long.valueOf(timeStampInMs))) {
                    this.keyList.pollLast();
                }
            }
        }
    }

    public TimeStampSaverData fetchData() {
        TimeStampSaverData timeStampSaverData;
        synchronized (this.dataLock) {
            Long[] trackKeyArr = new Long[this.keyList.size()];
            this.keyList.toArray(trackKeyArr);
            Long[] timestampArr = new Long[this.timeStampList.size()];
            this.timeStampList.toArray(timestampArr);
            this.keyList.clear();
            this.timeStampList.clear();
            timeStampSaverData = new TimeStampSaverData(this.savePoint, trackKeyArr, timestampArr);
        }
        return timeStampSaverData;
    }
}
