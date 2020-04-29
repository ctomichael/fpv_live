package dji.publics.LogReport.helper;

import android.os.Handler;
import android.os.Message;
import android.os.Process;
import com.dji.video.framing.internal.decoder.DJIVideoDecoder;
import dji.midware.media.record.RecorderInterface;
import dji.midware.media.record.RecorderManager;
import dji.midware.stat.LinuxUtils;
import dji.publics.DJIExecutor;
import dji.publics.LogReport.DJIReportUtil;
import dji.publics.LogReport.base.Event;
import dji.publics.LogReport.base.Fields;
import java.util.HashMap;

public class VideoDecoderReportHelper implements DJIVideoDecoder.VideoDecoderEventListener {
    private static final int FETCH_CPU_USAGE_ELAPSE = 1000;
    private static final int MSG_FETCH_CPU_USAGE = 1;
    private static final int MSG_REPORT = 0;
    private static final int REPORT_INTERVAL = 5000;
    private static final String TAG = "VideoDecoderReportHelpe";
    private static VideoDecoderReportHelper instance;
    private int dropFrameNum = 0;
    private Runnable fetchCpuUsageDelegate = new Runnable() {
        /* class dji.publics.LogReport.helper.VideoDecoderReportHelper.AnonymousClass2 */

        public void run() {
            VideoDecoderReportHelper.this.fetchCpuUsage();
        }
    };
    private LinuxUtils linuxUtils = new LinuxUtils();
    private int pid = Process.myPid();
    private float processCpuUsage;
    private Handler reportHandler = new Handler(DJIExecutor.getLooper()) {
        /* class dji.publics.LogReport.helper.VideoDecoderReportHelper.AnonymousClass1 */

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    VideoDecoderReportHelper.this.report();
                    return;
                default:
                    return;
            }
        }
    };
    private float totalCpuUsage;

    public static VideoDecoderReportHelper getInstance() {
        if (instance == null) {
            instance = new VideoDecoderReportHelper();
        }
        return instance;
    }

    private VideoDecoderReportHelper() {
    }

    public void onCodecInit() {
    }

    public void onFrameDropped() {
        this.dropFrameNum++;
        if (!this.reportHandler.hasMessages(0)) {
            DJIExecutor.getExecutor().execute(this.fetchCpuUsageDelegate);
            this.reportHandler.sendEmptyMessageDelayed(0, 5000);
        }
    }

    /* access modifiers changed from: private */
    public synchronized void fetchCpuUsage() {
        float[] cpuUsage = this.linuxUtils.syncGetTotalAndProcessCpuUsage(this.pid, 1000);
        this.totalCpuUsage = cpuUsage[0];
        this.processCpuUsage = cpuUsage[1];
    }

    /* access modifiers changed from: private */
    public synchronized void report() {
        HashMap<String, String> data = DJIReportUtil.getNormalDeviceData();
        data.put(Fields.Dgo_decoder_event.drop_frame_num, "" + this.dropFrameNum);
        RecorderInterface curRecorder = RecorderManager.getCurrentRecorder();
        data.put(Fields.Dgo_decoder_event.recorder, curRecorder == null ? "null" : curRecorder.getClass().getSimpleName());
        data.put(Fields.Dgo_decoder_event.total_cpu_usage, "" + this.totalCpuUsage);
        data.put(Fields.Dgo_decoder_event.process_cpu_usage, "" + this.processCpuUsage);
        DJIReportUtil.logEvent(Event.Dgo_decoder_event, data);
        DJIVideoDecoder.log2File("report: dropNum=" + this.dropFrameNum + " recorder=" + curRecorder + " totalCpu=" + this.totalCpuUsage + " processCpu=" + this.processCpuUsage);
        this.dropFrameNum = 0;
    }
}
