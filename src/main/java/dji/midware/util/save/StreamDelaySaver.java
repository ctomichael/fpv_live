package dji.midware.util.save;

import android.os.Environment;
import dji.fieldAnnotation.EXClassNullAway;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

@EXClassNullAway
public class StreamDelaySaver {
    public static boolean IS_SAVE_PACKET_DELAY = false;
    private static StreamDelaySaver instance;
    public BufferedWriter frameDelayFile = null;
    public BufferedWriter packetDelayFile = null;

    public static StreamDelaySaver getInstance() {
        if (instance == null) {
            instance = new StreamDelaySaver();
        }
        return instance;
    }

    private StreamDelaySaver() {
        if (this.frameDelayFile == null) {
            try {
                this.frameDelayFile = new BufferedWriter(new FileWriter(Environment.getExternalStorageDirectory().getPath() + "/dji_streamdelay_frame.txt"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (this.packetDelayFile == null) {
            try {
                this.packetDelayFile = new BufferedWriter(new FileWriter(Environment.getExternalStorageDirectory().getPath() + "/dji_streamdelay_packet.txt"));
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }
}
