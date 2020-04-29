package dji.midware.util.save;

import dji.log.DJILogHelper;
import dji.midware.util.save.TimeStampSaver;
import dji.midware.util.save.TimeStampSavingManager;
import java.util.List;

public class TimeStampDataLogger implements TimeStampSavingManager.TimeStampSavingListener {
    private static final String PLACEHOLDER_TIMESTAMP = "             ";
    private static final String PLACEHOLDER_TRACKKEY = "                ";
    private static final String TAG = "TimeStampDataLogger";

    private void log2File(String log) {
        DJILogHelper.getInstance().LOGD("StreamingTimestamp", log, "StreamingTimestamp");
    }

    public void onDataUpdated(List<TimeStampSaver.TimeStampSaverData> dataList) {
        StringBuffer sb = new StringBuffer();
        boolean isEmpty = true;
        int i = 0;
        while (true) {
            boolean endLoop = true;
            for (TimeStampSaver.TimeStampSaverData data : dataList) {
                if (i < data.trackKeys.length) {
                    Long key = data.trackKeys[i];
                    if (key != null) {
                        if (isEmpty) {
                            isEmpty = false;
                            for (TimeStampSaver.TimeStampSaverData saverData : dataList) {
                                sb.append(saverData.savePoint.name()).append("\t").append(PLACEHOLDER_TIMESTAMP).append("|\t");
                            }
                            sb.append("\n");
                            for (TimeStampSaver.TimeStampSaverData timeStampSaverData : dataList) {
                                sb.append("key             ").append("\t");
                                sb.append("time         ").append("\t");
                            }
                            sb.append("\n");
                        }
                        endLoop = false;
                        sb.append(Long.toHexString(key.longValue())).append("\t");
                    } else {
                        sb.append(PLACEHOLDER_TRACKKEY).append("\t");
                    }
                } else {
                    sb.append(PLACEHOLDER_TRACKKEY).append("\t");
                }
                if (i < data.timeStamps.length) {
                    Long key2 = data.timeStamps[i];
                    if (key2 != null) {
                        endLoop = false;
                        sb.append(key2).append("\t");
                    } else {
                        sb.append(PLACEHOLDER_TIMESTAMP).append("\t");
                    }
                } else {
                    sb.append(PLACEHOLDER_TIMESTAMP).append("\t");
                }
            }
            if (endLoop) {
                log2File(sb.toString());
                return;
            } else {
                sb.append("\n");
                i++;
            }
        }
    }
}
