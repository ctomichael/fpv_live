package dji.midware.util.save;

import android.os.Environment;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

public class StreamDebugSaver {
    private static final ThreadLocal<SimpleDateFormat> DATE_FORMAT = new ThreadLocal<>();
    public static boolean SAVE_VIDEO_MAIN = false;
    public static final String SAVE_VIDEO_MAIN_NAME = "dji_video_debug_main";
    public static boolean SAVE_VIDEO_SECONDARY = false;
    public static final String SAVE_VIDEO_SECONDARY_NAME = "dji_video_debug_second";
    private static final HashMap<String, StreamDebugSaver> instanceSet = new HashMap<>(2);
    public boolean isOpen;
    private long mStartTime;
    private String mVideoName;
    private FileOutputStream videoStreamFrameFile = null;

    public static StreamDebugSaver getInstance(String name) {
        StreamDebugSaver saver = instanceSet.get(name);
        if (saver != null) {
            return saver;
        }
        StreamDebugSaver saver2 = new StreamDebugSaver(name);
        instanceSet.put(name, saver2);
        return saver2;
    }

    public void open(boolean isOpen2) {
        if (SAVE_VIDEO_MAIN_NAME.equals(this.mVideoName)) {
            SAVE_VIDEO_MAIN = isOpen2;
        } else if (SAVE_VIDEO_SECONDARY_NAME.equals(this.mVideoName)) {
            SAVE_VIDEO_SECONDARY = isOpen2;
        }
        this.isOpen = isOpen2;
        this.mStartTime = System.currentTimeMillis();
        closeOutputStream();
    }

    public FileOutputStream getOutputStream() throws IOException {
        if (this.videoStreamFrameFile == null) {
            File dir = new File(Environment.getExternalStorageDirectory().getPath() + "/dji_video_test");
            if (!dir.exists()) {
                dir.mkdir();
            }
            if (DATE_FORMAT.get() == null) {
                DATE_FORMAT.set(new SimpleDateFormat("_MM-dd.HH_mm_ss"));
            }
            File logFile = new File(String.format(Environment.getExternalStorageDirectory().getPath() + "/dji_video_test/" + this.mVideoName + DATE_FORMAT.get().format(Long.valueOf(this.mStartTime)), new Object[0]));
            logFile.createNewFile();
            this.videoStreamFrameFile = new FileOutputStream(logFile);
        }
        return this.videoStreamFrameFile;
    }

    public void closeOutputStream() {
        if (this.videoStreamFrameFile != null) {
            try {
                this.videoStreamFrameFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.videoStreamFrameFile = null;
    }

    public StreamDebugSaver(String name) {
        this.mVideoName = name;
    }

    public void write(byte[] data, int offset, int size) {
        if (this.isOpen) {
            try {
                FileOutputStream output = getOutputStream();
                if (output != null) {
                    output.write(data, offset, size);
                    output.flush();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }
}
