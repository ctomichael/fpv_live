package dji.midware.media.mp4;

import dji.fieldAnnotation.EXClassNullAway;
import java.text.SimpleDateFormat;

@EXClassNullAway
public class MP4Info {
    private static final String TAG = MP4Info.class.getName();
    private final int SAMPLE_DELTA = 1000;
    public int chunk_entry_count;
    public int[] chunk_offset;
    public int create_time;
    public int duration;
    public int duration_time;
    public int fps;
    public int height;
    public int media_length;
    public int modification_time;
    public int[] pos_iframe;
    public byte[] pps;
    public int[] sample_offset;
    public int[] sample_size;
    public byte[] sps;
    public int time_scale;
    public int width;

    public MP4Info(MP4MovieBox moov) {
        this.height = moov.trak.tkhd.height;
        this.width = moov.trak.tkhd.width;
        this.time_scale = moov.mvhd.time_scale;
        this.duration = moov.mvhd.duration;
        this.create_time = moov.mvhd.create_time;
        this.modification_time = moov.mvhd.modification_time;
        this.sample_size = moov.trak.mdia.minf.stbl.stsz.sample_size_list;
        this.chunk_offset = moov.trak.mdia.minf.stbl.stco.chunk_offset_list;
        this.chunk_entry_count = moov.trak.mdia.minf.stbl.stsc.entry_count;
        this.sample_offset = calculateSampleOffset();
        this.pps = moov.trak.mdia.minf.stbl.stsd.avc1.avcC.pps;
        this.sps = moov.trak.mdia.minf.stbl.stsd.avc1.avcC.sps;
        this.pos_iframe = moov.trak.mdia.minf.stbl.stss.iframe_sample_number;
        this.duration_time = this.duration / this.time_scale;
        this.fps = this.time_scale / 1000;
        this.media_length = this.sample_size.length;
    }

    private int[] calculateSampleOffset() {
        int[] sample_offset2 = new int[this.sample_size.length];
        int offset = this.chunk_offset[0];
        for (int i = 0; i < sample_offset2.length; i++) {
            sample_offset2[i] = offset;
            offset += this.sample_size[i];
        }
        return sample_offset2;
    }

    public String toString() {
        String str = "MP4 Info\nheight: " + this.height + "\nwidth: " + this.width + "\ntime_scale: " + this.time_scale + "\nduration: " + this.duration + "\nduration_time: " + this.duration_time + "\ncreate_time: " + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(Integer.valueOf(this.create_time)) + "\nmodification_time: " + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(Integer.valueOf(this.modification_time)) + "\npps: " + MP4BytesUtil.byte2hex(this.pps) + "\nsps: " + MP4BytesUtil.byte2hex(this.sps) + "\nfps: " + this.fps + "\nchunk_offset length: " + this.chunk_offset.length + "\nsample_size length: " + this.sample_size.length + "\nchunk_entry_count: " + this.chunk_entry_count;
        for (int i = 0; i < this.pos_iframe.length; i++) {
            str = str + "\npos_iframe[" + i + "]: " + this.pos_iframe[i];
        }
        return str;
    }
}
