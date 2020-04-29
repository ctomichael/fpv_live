package dji.midware.media.HD;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.media.HD.HDConversion;
import dji.midware.media.MediaLogger;
import dji.midware.media.metadata.VideoMetadataManager;
import dji.midware.media.metadata.VideoRecordInfo;
import dji.midware.media.transcode.offline.TranscoderInterface;
import dji.midware.media.transcode.offline.TranscoderListener;
import java.util.Vector;

@EXClassNullAway
public class HDConvTest implements TranscoderInterface {
    TranscoderListener listener;

    public void stop() {
    }

    public class TestFileExistCallBack implements HDConversion.CallBackGetHD {
        public int fileID;

        public TestFileExistCallBack() {
        }

        public void onStart() {
        }

        public void onFail(Exception e) {
        }

        public void onProgress(int progress) {
            MediaLogger.show("File " + this.fileID + " " + progress);
        }

        public void onSuccess() {
        }
    }

    public void start(String inputFileName, String outputFileName, TranscoderListener listener2) {
        this.listener = listener2;
        try {
            new VideoRecordInfo().load(inputFileName.replace(".h264", ".info"));
            Vector<VideoRecordInfo> vecSegment = new Vector<>();
            VideoRecordInfo seg1 = new VideoRecordInfo();
            seg1.setStartTimeMsec(0);
            seg1.setEndTimeMsec(10000);
            VideoRecordInfo seg2 = new VideoRecordInfo();
            seg2.setStartTimeMsec(15000);
            seg2.setEndTimeMsec(25000);
            HDConversion.getInstance().getHDSegments(vecSegment, VideoMetadataManager.getSourceVideoDirectory(), new HDConversion.CallBackGetHD() {
                /* class dji.midware.media.HD.HDConvTest.AnonymousClass1 */

                public void onSuccess() {
                    HDConvTest.this.listener.onSuccess();
                }

                public void onStart() {
                    HDConvTest.this.listener.onStart();
                }

                public void onProgress(int progress) {
                    HDConvTest.this.listener.onProgress(progress);
                }

                public void onFail(Exception e) {
                    HDConvTest.this.listener.onFailure(e);
                }
            });
        } catch (Exception e1) {
            MediaLogger.show(e1);
            listener2.onFailure(e1);
        }
    }

    public void onDestroy() {
    }

    public void rebindListener(TranscoderListener listener2) {
    }

    public String getInputFileName() {
        return null;
    }

    public int getCurProgress() {
        return 0;
    }

    public boolean isTranscoding() {
        return false;
    }
}
