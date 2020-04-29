package dji.midware.media.mp4;

import android.util.Log;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class MP4MovieBox extends MP4Box {
    public Movie_Header_Box mvhd = new Movie_Header_Box();
    public Track_Box trak = new Track_Box();
    public User_Data_Box udta = new User_Data_Box();

    public boolean parseData(byte[] data, int offset) {
        this.box_data = data;
        this.start_offset = offset;
        this.box_size = MP4BytesUtil.getInt(data, 0);
        int index = 0 + 4;
        this.box_type = MP4BytesUtil.getStringUTF8(data, index, 4);
        int index2 = index + 4;
        boolean parse_mvhd = false;
        boolean parse_trak = false;
        while (index2 < data.length) {
            int size = MP4BytesUtil.getInt(data, index2);
            int index3 = index2 + 4;
            String type = MP4BytesUtil.getStringUTF8(data, index3, 4);
            byte[] child_data = new byte[size];
            int index4 = (index3 + 4) - 8;
            System.arraycopy(data, index4, child_data, 0, size);
            if (MP4BoxType.mvhd._equals(type)) {
                parse_mvhd = this.mvhd.parseData(child_data, index4);
            } else if (MP4BoxType.udta._equals(type)) {
                boolean parse_udta = this.udta.parseData(child_data, index4);
            } else if (MP4BoxType.trak._equals(type)) {
                parse_trak = this.trak.parseData(child_data, index4);
            }
            index2 = index4 + size;
        }
        if (!parse_mvhd || !parse_trak) {
            return false;
        }
        return true;
    }

    public class Movie_Header_Box extends MP4Box {
        public int create_time;
        public int duration;
        public byte[] flags = new byte[3];
        public final int length = 108;
        public byte[] matrix = new byte[36];
        public int modification_time;
        public int next_track_id;
        public byte[] pre_defined = new byte[24];
        public double rate;
        public byte[] reserved = new byte[10];
        public int time_scale;
        public final MP4BoxType type = MP4BoxType.mvhd;
        public byte version;
        public short volume;

        public Movie_Header_Box() {
        }

        public boolean parseData(byte[] data, int offset) {
            this.start_offset = offset;
            this.box_size = MP4BytesUtil.getInt(data, 0);
            int index = 0 + 4;
            this.box_type = MP4BytesUtil.getStringUTF8(data, index, 4);
            int index2 = index + 4;
            if (this.box_size != 108 || !this.type._equals(this.box_type)) {
                Log.e(TAG, "mvhd parse error");
                return false;
            }
            this.version = data[index2];
            int index3 = index2 + 1;
            System.arraycopy(data, index3, this.flags, 0, 3);
            int index4 = index3 + 3;
            this.create_time = MP4BytesUtil.getInt(data, index4);
            int index5 = index4 + 4;
            this.modification_time = MP4BytesUtil.getInt(data, index5);
            int index6 = index5 + 4;
            this.time_scale = MP4BytesUtil.getInt(data, index6);
            int index7 = index6 + 4;
            this.duration = MP4BytesUtil.getInt(data, index7);
            int index8 = index7 + 4;
            this.rate = (double) MP4BytesUtil.getFloat(data, index8);
            int index9 = index8 + 4;
            this.volume = MP4BytesUtil.getShort(data, index9);
            int index10 = index9 + 2;
            System.arraycopy(data, index10, this.reserved, 0, 10);
            int index11 = index10 + 10;
            System.arraycopy(data, index11, this.matrix, 0, 36);
            int index12 = index11 + 36;
            System.arraycopy(data, index12, this.pre_defined, 0, 24);
            int index13 = index12 + 24;
            this.next_track_id = MP4BytesUtil.getInt(data, index13);
            int index14 = index13 + 4;
            this.box_data = data;
            return true;
        }
    }

    public class User_Data_Box extends MP4Box {
        public final int length = 2048;
        public final MP4BoxType type = MP4BoxType.udta;

        public User_Data_Box() {
        }

        public boolean parseData(byte[] data, int offset) {
            this.start_offset = offset;
            this.box_size = MP4BytesUtil.getInt(data, 0);
            int index = 0 + 4;
            this.box_type = MP4BytesUtil.getStringUTF8(data, index, 4);
            int index2 = index + 4;
            if (this.box_size != 2048 || !this.type._equals(this.box_type)) {
                Log.e(TAG, "udta parse error");
                return false;
            }
            this.box_data = data;
            return true;
        }
    }

    public class Track_Box extends MP4Box {
        public MP4Box edts = new MP4Box(MP4BoxType.edts, 36);
        public Media_Box mdia = new Media_Box();
        public Track_Header_Box tkhd = new Track_Header_Box();
        public final MP4BoxType type = MP4BoxType.trak;

        public Track_Box() {
        }

        public boolean parseData(byte[] data, int offset) {
            this.box_data = data;
            this.start_offset = offset;
            this.box_size = MP4BytesUtil.getInt(data, 0);
            int index = 0 + 4;
            this.box_type = MP4BytesUtil.getStringUTF8(data, index, 4);
            int index2 = index + 4;
            boolean parse_tkhd = false;
            boolean parse_edts = false;
            boolean parse_mdia = false;
            while (index2 < data.length) {
                int size = MP4BytesUtil.getInt(data, index2);
                int index3 = index2 + 4;
                String type2 = MP4BytesUtil.getStringUTF8(data, index3, 4);
                byte[] child_data = new byte[size];
                int index4 = (index3 + 4) - 8;
                System.arraycopy(data, index4, child_data, 0, size);
                if (MP4BoxType.tkhd._equals(type2)) {
                    parse_tkhd = this.tkhd.parseData(child_data, index4);
                } else if (MP4BoxType.edts._equals(type2)) {
                    parse_edts = this.edts.parseData(child_data, index4);
                } else if (MP4BoxType.mdia._equals(type2)) {
                    parse_mdia = this.mdia.parseData(child_data, index4);
                }
                index2 = index4 + size;
            }
            if (parse_tkhd && parse_mdia) {
                return true;
            }
            Log.e(TAG, "trak parse error" + parse_tkhd + parse_edts + parse_mdia);
            return false;
        }

        public class Track_Header_Box extends MP4Box {
            public short alternate_group = 0;
            public int create_time;
            public int duration;
            public byte[] flags = new byte[3];
            public int height;
            public short layer;
            public final int length = 92;
            public byte[] matrix = new byte[36];
            public int modification_time;
            public int reserved;
            public byte[] reserved_2 = new byte[8];
            public short reserved_3;
            public int track_id;
            public final MP4BoxType type = MP4BoxType.tkhd;
            public byte version;
            public short volume;
            public int width;

            public Track_Header_Box() {
            }

            public boolean parseData(byte[] data, int offset) {
                this.start_offset = offset;
                this.box_size = MP4BytesUtil.getInt(data, 0);
                int index = 0 + 4;
                this.box_type = MP4BytesUtil.getStringUTF8(data, index, 4);
                int index2 = index + 4;
                if (this.box_size != 92 || !this.type._equals(this.box_type)) {
                    Log.e(TAG, "tkhd parse error");
                    return false;
                }
                this.version = data[index2];
                int index3 = index2 + 1;
                System.arraycopy(data, index3, this.flags, 0, 3);
                int index4 = index3 + 3;
                this.create_time = MP4BytesUtil.getInt(data, index4);
                int index5 = index4 + 4;
                this.modification_time = MP4BytesUtil.getInt(data, index5);
                int index6 = index5 + 4;
                this.track_id = MP4BytesUtil.getInt(data, index6);
                int index7 = index6 + 4;
                this.reserved = MP4BytesUtil.getInt(data, index7);
                int index8 = index7 + 4;
                this.duration = MP4BytesUtil.getInt(data, index8);
                int index9 = index8 + 4;
                System.arraycopy(data, index9, this.reserved_2, 0, 8);
                int index10 = index9 + 8;
                this.layer = MP4BytesUtil.getShort(data, index10);
                int index11 = index10 + 2;
                this.alternate_group = MP4BytesUtil.getShort(data, index11);
                int index12 = index11 + 2;
                this.volume = MP4BytesUtil.getShort(data, index12);
                int index13 = index12 + 2;
                this.reserved_3 = MP4BytesUtil.getShort(data, index13);
                int index14 = index13 + 2;
                System.arraycopy(data, index14, this.matrix, 0, 36);
                int index15 = index14 + 36;
                this.width = MP4BytesUtil.getInt(data, index15) >> 16;
                int index16 = index15 + 4;
                this.height = MP4BytesUtil.getInt(data, index16) >> 16;
                int index17 = index16 + 4;
                return true;
            }
        }

        public class Media_Box extends MP4Box {
            public MP4Box hdlr = new MP4Box(MP4BoxType.hdlr, 49);
            public MP4Box mdhd = new MP4Box(MP4BoxType.mdhd, 32);
            public Media_Information_Box minf = new Media_Information_Box();
            public final MP4BoxType type = MP4BoxType.mdia;

            public Media_Box() {
            }

            public boolean parseData(byte[] data, int offset) {
                this.box_size = MP4BytesUtil.getInt(data, 0);
                int index = 0 + 4;
                this.box_type = MP4BytesUtil.getStringUTF8(data, index, 4);
                int index2 = index + 4;
                boolean parse_mdhd = false;
                boolean parse_hdlr = false;
                boolean parse_minf = false;
                while (index2 < data.length) {
                    int size = MP4BytesUtil.getInt(data, index2);
                    int index3 = index2 + 4;
                    String type2 = MP4BytesUtil.getStringUTF8(data, index3, 4);
                    byte[] child_data = new byte[size];
                    int index4 = (index3 + 4) - 8;
                    System.arraycopy(data, index4, child_data, 0, size);
                    if (MP4BoxType.mdhd._equals(type2)) {
                        parse_mdhd = this.mdhd.parseData(child_data, index4);
                    } else if (MP4BoxType.hdlr._equals(type2)) {
                        parse_hdlr = this.hdlr.parseData(child_data, index4);
                    } else if (MP4BoxType.minf._equals(type2)) {
                        parse_minf = this.minf.parseData(child_data, index4);
                    }
                    index2 = index4 + size;
                }
                if (parse_mdhd && parse_hdlr && parse_minf) {
                    return true;
                }
                Log.e(TAG, "mdia parse error");
                return false;
            }

            public class Media_Information_Box extends MP4Box {
                public MP4Box dinf = new MP4Box(MP4BoxType.dinf, 36);
                public Sample_Table_Box stbl = new Sample_Table_Box();
                public final MP4BoxType type = MP4BoxType.minf;
                public MP4Box vmhd = new MP4Box(MP4BoxType.vmhd, 20);

                public Media_Information_Box() {
                }

                public boolean parseData(byte[] data, int offset) {
                    this.box_size = MP4BytesUtil.getInt(data, 0);
                    int index = 0 + 4;
                    this.box_type = MP4BytesUtil.getStringUTF8(data, index, 4);
                    int index2 = index + 4;
                    boolean parse_vmhd = false;
                    boolean parse_dinf = false;
                    boolean parse_stbl = false;
                    while (index2 < data.length) {
                        int size = MP4BytesUtil.getInt(data, index2);
                        int index3 = index2 + 4;
                        String type2 = MP4BytesUtil.getStringUTF8(data, index3, 4);
                        byte[] child_data = new byte[size];
                        int index4 = (index3 + 4) - 8;
                        System.arraycopy(data, index4, child_data, 0, size);
                        if (MP4BoxType.vmhd._equals(type2)) {
                            parse_vmhd = this.vmhd.parseData(child_data, size);
                        } else if (MP4BoxType.dinf._equals(type2)) {
                            parse_dinf = this.dinf.parseData(child_data, size);
                        } else if (MP4BoxType.stbl._equals(type2)) {
                            parse_stbl = this.stbl.parseData(child_data, size);
                        }
                        index2 = index4 + size;
                    }
                    if (parse_dinf && parse_stbl) {
                        return true;
                    }
                    Log.e(TAG, "minf parse error" + parse_vmhd + parse_dinf + parse_stbl);
                    return false;
                }

                public class Sample_Table_Box extends MP4Box {
                    public MP4Box ctts = new MP4Box();
                    public Chunk_Offset_Box stco = new Chunk_Offset_Box();
                    public Sample_To_Chunk_Box stsc = new Sample_To_Chunk_Box();
                    public Sample_Description_Box stsd = new Sample_Description_Box();
                    public Sync_Sample_Box stss = new Sync_Sample_Box();
                    public Sample_Size_Box stsz = new Sample_Size_Box();
                    public MP4Box stts = new MP4Box();
                    public final MP4BoxType type = MP4BoxType.stbl;

                    public Sample_Table_Box() {
                    }

                    public boolean parseData(byte[] data, int offset) {
                        this.box_size = MP4BytesUtil.getInt(data, 0);
                        int index = 0 + 4;
                        this.box_type = MP4BytesUtil.getStringUTF8(data, index, 4);
                        int index2 = index + 4;
                        while (index2 < this.box_size) {
                            int size = MP4BytesUtil.getInt(data, index2);
                            int index3 = index2 + 4;
                            String type2 = MP4BytesUtil.getStringUTF8(data, index3, 4);
                            byte[] child_data = new byte[size];
                            int index4 = (index3 + 4) - 8;
                            System.arraycopy(data, index4, child_data, 0, size);
                            if (MP4BoxType.stsd._equals(type2)) {
                                this.stsd.parseData(child_data, index4);
                            } else if (MP4BoxType.stts._equals(type2)) {
                                this.stts.parseData(child_data, index4);
                            } else if (MP4BoxType.ctts._equals(type2)) {
                                this.ctts.parseData(child_data, index4);
                            } else if (MP4BoxType.stsc._equals(type2)) {
                                this.stsc.parseData(child_data, index4);
                            } else if (MP4BoxType.stsz._equals(type2)) {
                                this.stsz.parseData(child_data, index4);
                            } else if (MP4BoxType.stco._equals(type2)) {
                                this.stco.parseData(child_data, index4);
                            } else if (MP4BoxType.stss._equals(type2)) {
                                this.stss.parseData(child_data, index4);
                            }
                            index2 = index4 + size;
                        }
                        return true;
                    }

                    public class Sample_Description_Box extends MP4Box {
                        public AVC_Sample_Entrty avc1 = new AVC_Sample_Entrty();
                        public byte[] flags = new byte[3];
                        public int reserved;
                        public final MP4BoxType type = MP4BoxType.stsd;
                        public byte version;

                        public Sample_Description_Box() {
                        }

                        public boolean parseData(byte[] data, int offset) {
                            this.box_size = MP4BytesUtil.getInt(data, 0);
                            int index = 0 + 4;
                            this.box_type = MP4BytesUtil.getStringUTF8(data, index, 4);
                            int index2 = index + 4;
                            this.version = data[index2];
                            int index3 = index2 + 1;
                            System.arraycopy(data, index3, this.flags, 0, 3);
                            int index4 = index3 + 3;
                            this.reserved = MP4BytesUtil.getInt(data, index4);
                            int index5 = index4 + 4;
                            int size = MP4BytesUtil.getInt(data, index5);
                            int index6 = index5 + 4;
                            String type2 = MP4BytesUtil.getStringUTF8(data, index6, 4);
                            byte[] child_data = new byte[size];
                            int index7 = (index6 + 4) - 8;
                            System.arraycopy(data, index7, child_data, 0, size);
                            if (!MP4BoxType.avc1._equals(type2)) {
                                return false;
                            }
                            this.avc1.parseData(child_data, index7);
                            return true;
                        }

                        public class AVC_Sample_Entrty extends MP4Box {
                            public AVC_Decoder_Configuration_Record avcC = new AVC_Decoder_Configuration_Record();
                            public int height;
                            public int horiz_resolution;
                            public final MP4BoxType type = MP4BoxType.avc1;
                            public int ver_resolution;
                            public int width;

                            public AVC_Sample_Entrty() {
                            }

                            public boolean parseData(byte[] data, int offset) {
                                this.box_size = MP4BytesUtil.getInt(data, 0);
                                int index = 0 + 4;
                                this.box_type = MP4BytesUtil.getStringUTF8(data, index, 4);
                                int index2 = index + 4 + 24;
                                this.width = MP4BytesUtil.getShort(data, index2);
                                int index3 = index2 + 2;
                                this.height = MP4BytesUtil.getShort(data, index3);
                                int index4 = index3 + 2;
                                this.horiz_resolution = MP4BytesUtil.getInt(data, index4);
                                int index5 = index4 + 4;
                                this.ver_resolution = MP4BytesUtil.getInt(data, index5);
                                int index6 = index5 + 4 + 42;
                                int size = MP4BytesUtil.getInt(data, index6);
                                int index7 = index6 + 4;
                                String type2 = MP4BytesUtil.getStringUTF8(data, index7, 4);
                                byte[] child_data = new byte[size];
                                int index8 = (index7 + 4) - 8;
                                System.arraycopy(data, index8, child_data, 0, size);
                                if (!MP4BoxType.avcC._equals(type2)) {
                                    return false;
                                }
                                this.avcC.parseData(child_data, index8);
                                return true;
                            }

                            public class AVC_Decoder_Configuration_Record extends MP4Box {
                                public byte[] pps = new byte[4];
                                public byte[] sps;
                                public final MP4BoxType type = MP4BoxType.avcC;

                                public AVC_Decoder_Configuration_Record() {
                                }

                                public boolean parseData(byte[] data, int offset) {
                                    this.box_size = MP4BytesUtil.getInt(data, 0);
                                    int index = 0 + 4;
                                    this.box_type = MP4BytesUtil.getStringUTF8(data, index, 4);
                                    int sps_length = this.box_size - 23;
                                    this.sps = new byte[sps_length];
                                    System.arraycopy(data, index + 4 + 8, this.sps, 0, sps_length);
                                    int index2 = sps_length + 16 + 3;
                                    System.arraycopy(data, index2, this.pps, 0, 4);
                                    int index3 = index2 + 4;
                                    return true;
                                }
                            }
                        }
                    }

                    public class Sample_To_Chunk_Box extends MP4Box {
                        public int entry_count;
                        public int[] first_chunk;
                        public byte[] flags = new byte[3];
                        public int[] sample_description_index;
                        public int[] sample_per_chunk;
                        public final MP4BoxType type = MP4BoxType.stsc;
                        public byte version;

                        public Sample_To_Chunk_Box() {
                        }

                        public boolean parseData(byte[] data, int offset) {
                            this.box_size = MP4BytesUtil.getInt(data, 0);
                            int index = 0 + 4;
                            this.box_type = MP4BytesUtil.getStringUTF8(data, index, 4);
                            int index2 = index + 4;
                            this.version = data[index2];
                            int index3 = index2 + 1;
                            System.arraycopy(data, index3, this.flags, 0, 3);
                            int index4 = index3 + 3;
                            this.entry_count = MP4BytesUtil.getInt(data, index4);
                            int index5 = index4 + 4;
                            if (this.entry_count <= 0) {
                                return false;
                            }
                            this.first_chunk = new int[this.entry_count];
                            this.sample_per_chunk = new int[this.entry_count];
                            this.sample_description_index = new int[this.entry_count];
                            for (int i = 0; i < this.entry_count; i++) {
                                this.first_chunk[i] = MP4BytesUtil.getInt(data, index5);
                                int index6 = index5 + 4;
                                this.sample_per_chunk[i] = MP4BytesUtil.getInt(data, index6);
                                int index7 = index6 + 4;
                                this.sample_description_index[i] = MP4BytesUtil.getInt(data, index7);
                                index5 = index7 + 4;
                            }
                            return true;
                        }
                    }

                    public class Sample_Size_Box extends MP4Box {
                        public byte[] flags = new byte[3];
                        public int sample_count;
                        public int sample_size;
                        public int[] sample_size_list;
                        public final MP4BoxType type = MP4BoxType.stsz;
                        public byte version;

                        public Sample_Size_Box() {
                        }

                        public boolean parseData(byte[] data, int offset) {
                            this.box_size = MP4BytesUtil.getInt(data, 0);
                            int index = 0 + 4;
                            this.box_type = MP4BytesUtil.getStringUTF8(data, index, 4);
                            int index2 = index + 4;
                            this.version = data[index2];
                            int index3 = index2 + 1;
                            System.arraycopy(data, index3, this.flags, 0, 3);
                            int index4 = index3 + 3;
                            this.sample_size = MP4BytesUtil.getInt(data, index4);
                            int index5 = index4 + 4;
                            this.sample_count = MP4BytesUtil.getInt(data, index5);
                            int index6 = index5 + 4;
                            if (this.sample_count > 0) {
                                this.sample_size_list = new int[this.sample_count];
                                for (int i = 0; i < this.sample_count; i++) {
                                    this.sample_size_list[i] = MP4BytesUtil.getInt(data, index6);
                                    index6 += 4;
                                }
                                return true;
                            }
                            Log.e(TAG, "parse stsz sample count error");
                            return true;
                        }
                    }

                    public class Chunk_Offset_Box extends MP4Box {
                        public int[] chunk_offset_list;
                        public int entry_count;
                        public byte[] flags = new byte[3];
                        public final MP4BoxType type = MP4BoxType.stco;
                        public byte version;

                        public Chunk_Offset_Box() {
                        }

                        public boolean parseData(byte[] data, int offset) {
                            this.box_size = MP4BytesUtil.getInt(data, 0);
                            int index = 0 + 4;
                            this.box_type = MP4BytesUtil.getStringUTF8(data, index, 4);
                            int index2 = index + 4;
                            this.version = data[index2];
                            int index3 = index2 + 1;
                            System.arraycopy(data, index3, this.flags, 0, 3);
                            int index4 = index3 + 3;
                            this.entry_count = MP4BytesUtil.getInt(data, index4);
                            int index5 = index4 + 4;
                            if (this.entry_count > 0) {
                                this.chunk_offset_list = new int[this.entry_count];
                                for (int i = 0; i < this.entry_count; i++) {
                                    this.chunk_offset_list[i] = MP4BytesUtil.getInt(data, index5);
                                    index5 += 4;
                                }
                                return true;
                            }
                            Log.e(TAG, "parse stco entry count error");
                            return true;
                        }
                    }

                    public class Sync_Sample_Box extends MP4Box {
                        public int entry_count;
                        public byte[] flags = new byte[3];
                        public int[] iframe_sample_number;
                        public final MP4BoxType type = MP4BoxType.stss;
                        public byte version;

                        public Sync_Sample_Box() {
                        }

                        public boolean parseData(byte[] data, int offset) {
                            this.box_size = MP4BytesUtil.getInt(data, 0);
                            int index = 0 + 4;
                            this.box_type = MP4BytesUtil.getStringUTF8(data, index, 4);
                            int index2 = index + 4;
                            this.version = data[index2];
                            int index3 = index2 + 1;
                            System.arraycopy(data, index3, this.flags, 0, 3);
                            int index4 = index3 + 3;
                            this.entry_count = MP4BytesUtil.getInt(data, index4);
                            int index5 = index4 + 4;
                            Log.e(TAG, "entry_count" + this.entry_count);
                            if (this.entry_count > 0) {
                                this.iframe_sample_number = new int[this.entry_count];
                                for (int i = 0; i < this.entry_count; i++) {
                                    this.iframe_sample_number[i] = MP4BytesUtil.getInt(data, index5);
                                    index5 += 4;
                                }
                                return true;
                            }
                            Log.e(TAG, "parse stss entry count error");
                            return true;
                        }
                    }
                }
            }
        }
    }
}
