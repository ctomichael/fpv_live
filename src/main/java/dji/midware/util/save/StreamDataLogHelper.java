package dji.midware.util.save;

import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.log.GlobalConfig;
import dji.midware.util.save.StreamDataObserver;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

@EXClassNullAway
public class StreamDataLogHelper implements StreamDataObserver.IStreamDataObserverGlobalListener {
    private static boolean IS_OPEN = true;
    private int cachingCount = 0;
    private List<String> curNameList;
    private DateFormat dateFormat;
    private boolean hasData = false;
    private int interval = 2;
    private boolean isLastLineEmpty = false;
    private int lastLineStart = 0;
    private StringBuffer mSb = new StringBuffer();
    private boolean needLogNameList = false;
    private List<String> processingNameList;
    private int skipCount = 0;
    private Object writeDataLock = new Object();
    private int writeNum = 30;

    public void onByteRatesUpdate(Map<String, Map<String, Long>> rst) {
        synchronized (this.writeDataLock) {
            if (this.skipCount == this.interval - 1) {
                this.skipCount = 0;
                if (!rst.isEmpty()) {
                    this.hasData = false;
                    this.lastLineStart = this.mSb.length();
                    this.mSb.append("\n").append(getTimeString());
                    List<Map.Entry<String, Map<String, Long>>> streamDataRst = new ArrayList<>(rst.entrySet());
                    this.processingNameList = new ArrayList(streamDataRst.size());
                    for (Map.Entry<String, Map<String, Long>> dataEntry : streamDataRst) {
                        if (dataEntry.getValue() != null) {
                            this.processingNameList.add(dataEntry.getKey());
                            this.mSb.append("|").append(this.processingNameList.lastIndexOf(dataEntry.getKey()));
                            if (((Map) dataEntry.getValue()).get(StreamDataObserver.PARAM_BYTE_RATE) != null) {
                                this.hasData = true;
                                this.mSb.append("B").append((int) (((float) ((Long) ((Map) dataEntry.getValue()).get(StreamDataObserver.PARAM_BYTE_RATE)).longValue()) / 1024.0f));
                            }
                            if (((Map) dataEntry.getValue()).get("frame_rate") != null) {
                                this.hasData = true;
                                this.mSb.append("F").append(((Number) ((Map) dataEntry.getValue()).get("frame_rate")).intValue());
                            }
                            if (((Map) dataEntry.getValue()).get(StreamDataObserver.PARAM_KEYFRAME_NUM) != null) {
                                this.hasData = true;
                                this.mSb.append("I").append(((Number) ((Map) dataEntry.getValue()).get(StreamDataObserver.PARAM_KEYFRAME_NUM)).intValue());
                            }
                            if ((((String) dataEntry.getKey()).contains("DJIVideoDataRecever") && ((Map) dataEntry.getValue()).get("width") != null) || ((Map) dataEntry.getValue()).get("height") != null) {
                                this.hasData = true;
                                this.mSb.append("R" + ((Number) ((Map) dataEntry.getValue()).get("width")).intValue() + "" + ((Number) ((Map) dataEntry.getValue()).get("height")).intValue());
                            }
                        }
                    }
                    if (!this.hasData) {
                        this.mSb.delete(this.lastLineStart, this.mSb.length());
                        if (!this.isLastLineEmpty) {
                            this.mSb.append("\n").append("No data...");
                        }
                        this.isLastLineEmpty = true;
                    } else {
                        this.isLastLineEmpty = false;
                    }
                    if (!isListEqual(this.processingNameList, this.curNameList)) {
                        this.curNameList = this.processingNameList;
                        this.needLogNameList = true;
                    }
                    if (this.cachingCount >= this.writeNum - 1) {
                        this.cachingCount = 0;
                        if (this.needLogNameList) {
                            logList(this.curNameList);
                            this.needLogNameList = false;
                        }
                        if (this.mSb.length() > 0) {
                            log2File(this.mSb.toString());
                            this.mSb.delete(0, this.mSb.length());
                        }
                    } else {
                        this.cachingCount++;
                    }
                }
            } else {
                this.skipCount++;
            }
        }
    }

    private boolean isListEqual(List list1, List list2) {
        if (list1 == null && list2 == null) {
            return true;
        }
        if (list1 == null || list2 == null) {
            return false;
        }
        return Arrays.equals(list1.toArray(), list2.toArray());
    }

    private void logList(List list) {
        if (list != null) {
            StringBuffer sb = new StringBuffer();
            int i = 0;
            for (Object obj : list) {
                sb.append("\n").append(i).append(":\t").append(obj);
                i++;
            }
            log2File(sb.toString());
        }
    }

    private void log2File(String log) {
        DJILog.saveLog(log, "LiveStreaming");
        if (GlobalConfig.VIDEO_LOG_DEBUGGABLE) {
            DJILog.d("StreamDataLogHelper", log, new Object[0]);
        }
    }

    private String getTimeString() {
        if (this.dateFormat == null) {
            this.dateFormat = new SimpleDateFormat("MMddHHmmss");
        }
        return this.dateFormat.format(new Date());
    }

    public void start() {
        if (IS_OPEN) {
            StreamDataObserver.addListener(this);
        }
    }

    public void stop() {
        if (IS_OPEN) {
            StreamDataObserver.removeListener(this);
            synchronized (this.writeDataLock) {
                if (this.mSb.length() > 0) {
                    if (this.needLogNameList) {
                        logList(this.curNameList);
                        this.needLogNameList = false;
                    }
                    log2File(this.mSb.toString());
                }
            }
        }
    }
}
