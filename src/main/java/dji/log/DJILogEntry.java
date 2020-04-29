package dji.log;

public class DJILogEntry {
    public long elapsedTime;
    public int level;
    public String msg;
    public String path;
    public String tag;
    public long time;

    public String toString() {
        return "DJILogEntry{time='" + this.time + '\'' + ", level='" + this.level + '\'' + ", tag='" + this.tag + '\'' + ", path='" + this.path + '\'' + ", msg='" + this.msg + '\'' + '}';
    }
}
