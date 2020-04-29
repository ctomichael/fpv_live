package com.facebook.soloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import kotlin.UShort;
import org.bouncycastle.asn1.cmc.BodyPartID;

public final class MinElf {
    public static final int DT_NEEDED = 1;
    public static final int DT_NULL = 0;
    public static final int DT_STRTAB = 5;
    public static final int ELF_MAGIC = 1179403647;
    public static final int PN_XNUM = 65535;
    public static final int PT_DYNAMIC = 2;
    public static final int PT_LOAD = 1;

    public static String[] extract_DT_NEEDED(File elfFile) throws IOException {
        FileInputStream is = new FileInputStream(elfFile);
        try {
            return extract_DT_NEEDED(is.getChannel());
        } finally {
            is.close();
        }
    }

    public static String[] extract_DT_NEEDED(FileChannel fc) throws IOException {
        long e_phoff;
        long e_phnum;
        int e_phentsize;
        long d_tag;
        long d_tag2;
        long d_val;
        long p_type;
        long p_vaddr;
        long p_memsz;
        long p_offset;
        long p_type2;
        long p_offset2;
        long e_shoff;
        long sh_info;
        ByteBuffer bb = ByteBuffer.allocate(8);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        if (getu32(fc, bb, 0) != 1179403647) {
            throw new ElfError("file is not ELF");
        }
        boolean is32 = getu8(fc, bb, 4) == 1;
        if (getu8(fc, bb, 5) == 2) {
            bb.order(ByteOrder.BIG_ENDIAN);
        }
        if (is32) {
            e_phoff = getu32(fc, bb, 28);
        } else {
            e_phoff = get64(fc, bb, 32);
        }
        if (is32) {
            e_phnum = (long) getu16(fc, bb, 44);
        } else {
            e_phnum = (long) getu16(fc, bb, 56);
        }
        if (is32) {
            e_phentsize = getu16(fc, bb, 42);
        } else {
            e_phentsize = getu16(fc, bb, 54);
        }
        if (e_phnum == 65535) {
            if (is32) {
                e_shoff = getu32(fc, bb, 32);
            } else {
                e_shoff = get64(fc, bb, 40);
            }
            if (is32) {
                sh_info = getu32(fc, bb, 28 + e_shoff);
            } else {
                sh_info = getu32(fc, bb, 44 + e_shoff);
            }
            e_phnum = sh_info;
        }
        long dynStart = 0;
        long phdr = e_phoff;
        long i = 0;
        while (true) {
            if (i >= e_phnum) {
                break;
            }
            if (is32) {
                p_type2 = getu32(fc, bb, 0 + phdr);
            } else {
                p_type2 = getu32(fc, bb, 0 + phdr);
            }
            if (p_type2 == 2) {
                if (is32) {
                    p_offset2 = getu32(fc, bb, 4 + phdr);
                } else {
                    p_offset2 = get64(fc, bb, 8 + phdr);
                }
                dynStart = p_offset2;
            } else {
                phdr += (long) e_phentsize;
                i++;
            }
        }
        if (dynStart == 0) {
            throw new ElfError("ELF file does not contain dynamic linking information");
        }
        int nr_DT_NEEDED = 0;
        long dyn = dynStart;
        long ptr_DT_STRTAB = 0;
        do {
            if (is32) {
                d_tag = getu32(fc, bb, 0 + dyn);
            } else {
                d_tag = get64(fc, bb, 0 + dyn);
            }
            if (d_tag == 1) {
                if (nr_DT_NEEDED == Integer.MAX_VALUE) {
                    throw new ElfError("malformed DT_NEEDED section");
                }
                nr_DT_NEEDED++;
            } else if (d_tag == 5) {
                if (is32) {
                    ptr_DT_STRTAB = getu32(fc, bb, 4 + dyn);
                } else {
                    ptr_DT_STRTAB = get64(fc, bb, 8 + dyn);
                }
            }
            dyn += is32 ? 8 : 16;
        } while (d_tag != 0);
        if (ptr_DT_STRTAB == 0) {
            throw new ElfError("Dynamic section string-table not found");
        }
        long off_DT_STRTAB = 0;
        long phdr2 = e_phoff;
        int i2 = 0;
        while (true) {
            if (((long) i2) >= e_phnum) {
                break;
            }
            if (is32) {
                p_type = getu32(fc, bb, 0 + phdr2);
            } else {
                p_type = getu32(fc, bb, 0 + phdr2);
            }
            if (p_type == 1) {
                if (is32) {
                    p_vaddr = getu32(fc, bb, 8 + phdr2);
                } else {
                    p_vaddr = get64(fc, bb, 16 + phdr2);
                }
                if (is32) {
                    p_memsz = getu32(fc, bb, 20 + phdr2);
                } else {
                    p_memsz = get64(fc, bb, 40 + phdr2);
                }
                if (p_vaddr <= ptr_DT_STRTAB && ptr_DT_STRTAB < p_vaddr + p_memsz) {
                    if (is32) {
                        p_offset = getu32(fc, bb, 4 + phdr2);
                    } else {
                        p_offset = get64(fc, bb, 8 + phdr2);
                    }
                    off_DT_STRTAB = p_offset + (ptr_DT_STRTAB - p_vaddr);
                }
            }
            phdr2 += (long) e_phentsize;
            i2++;
        }
        if (off_DT_STRTAB == 0) {
            throw new ElfError("did not find file offset of DT_STRTAB table");
        }
        String[] needed = new String[nr_DT_NEEDED];
        int nr_DT_NEEDED2 = 0;
        long dyn2 = dynStart;
        do {
            if (is32) {
                d_tag2 = getu32(fc, bb, 0 + dyn2);
            } else {
                d_tag2 = get64(fc, bb, 0 + dyn2);
            }
            if (d_tag2 == 1) {
                if (is32) {
                    d_val = getu32(fc, bb, 4 + dyn2);
                } else {
                    d_val = get64(fc, bb, 8 + dyn2);
                }
                needed[nr_DT_NEEDED2] = getSz(fc, bb, off_DT_STRTAB + d_val);
                if (nr_DT_NEEDED2 == Integer.MAX_VALUE) {
                    throw new ElfError("malformed DT_NEEDED section");
                }
                nr_DT_NEEDED2++;
            }
            dyn2 += is32 ? 8 : 16;
        } while (d_tag2 != 0);
        if (nr_DT_NEEDED2 == needed.length) {
            return needed;
        }
        throw new ElfError("malformed DT_NEEDED section");
    }

    private static String getSz(FileChannel fc, ByteBuffer bb, long offset) throws IOException {
        StringBuilder sb = new StringBuilder();
        while (true) {
            long offset2 = offset + 1;
            short b = getu8(fc, bb, offset);
            if (b == 0) {
                return sb.toString();
            }
            sb.append((char) b);
            offset = offset2;
        }
    }

    private static void read(FileChannel fc, ByteBuffer bb, int sz, long offset) throws IOException {
        int numBytesRead;
        bb.position(0);
        bb.limit(sz);
        while (bb.remaining() > 0 && (numBytesRead = fc.read(bb, offset)) != -1) {
            offset += (long) numBytesRead;
        }
        if (bb.remaining() > 0) {
            throw new ElfError("ELF file truncated");
        }
        bb.position(0);
    }

    private static long get64(FileChannel fc, ByteBuffer bb, long offset) throws IOException {
        read(fc, bb, 8, offset);
        return bb.getLong();
    }

    private static long getu32(FileChannel fc, ByteBuffer bb, long offset) throws IOException {
        read(fc, bb, 4, offset);
        return ((long) bb.getInt()) & BodyPartID.bodyIdMax;
    }

    private static int getu16(FileChannel fc, ByteBuffer bb, long offset) throws IOException {
        read(fc, bb, 2, offset);
        return bb.getShort() & UShort.MAX_VALUE;
    }

    private static short getu8(FileChannel fc, ByteBuffer bb, long offset) throws IOException {
        read(fc, bb, 1, offset);
        return (short) (bb.get() & 255);
    }

    private static class ElfError extends RuntimeException {
        ElfError(String why) {
            super(why);
        }
    }
}
