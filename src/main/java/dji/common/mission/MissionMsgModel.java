package dji.common.mission;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class MissionMsgModel {

    public static class DownloadedWPSummaryEvent {
        private byte[] data;

        public byte[] getData() {
            return this.data;
        }

        public void setData(byte[] data2) {
            this.data = data2;
        }
    }

    public static class DownloadedWPDetailedEvent {
        private byte[] data;

        public byte[] getData() {
            return this.data;
        }

        public void setData(byte[] data2) {
            this.data = data2;
        }
    }
}
