package org.xeustechnologies.jtar;

public class TarHeader {
    public static final int CHKSUMLEN = 8;
    public static final int DEVLEN = 8;
    public static final int GIDLEN = 8;
    public static final int GNAMELEN = 32;
    public static final String GNU_TMAGIC = "ustar  ";
    public static final byte LF_BLK = 52;
    public static final byte LF_CHR = 51;
    public static final byte LF_CONTIG = 55;
    public static final byte LF_DIR = 53;
    public static final byte LF_FIFO = 54;
    public static final byte LF_LINK = 49;
    public static final byte LF_NORMAL = 48;
    public static final byte LF_OLDNORM = 0;
    public static final byte LF_SYMLINK = 50;
    public static final int MAGICLEN = 8;
    public static final int MODELEN = 8;
    public static final int MODTIMELEN = 12;
    public static final int NAMELEN = 100;
    public static final int SIZELEN = 12;
    public static final String TMAGIC = "ustar";
    public static final int UIDLEN = 8;
    public static final int UNAMELEN = 32;
    public int checkSum;
    public int devMajor;
    public int devMinor;
    public int groupId;
    public StringBuffer groupName;
    public byte linkFlag;
    public StringBuffer linkName = new StringBuffer();
    public StringBuffer magic = new StringBuffer(TMAGIC);
    public long modTime;
    public int mode;
    public StringBuffer name = new StringBuffer();
    public long size;
    public int userId;
    public StringBuffer userName;

    public TarHeader() {
        String user = System.getProperty("user.name", "");
        user = user.length() > 31 ? user.substring(0, 31) : user;
        this.userId = 0;
        this.groupId = 0;
        this.userName = new StringBuffer(user);
        this.groupName = new StringBuffer("");
    }

    public static StringBuffer parseName(byte[] header, int offset, int length) {
        StringBuffer result = new StringBuffer(length);
        int end = offset + length;
        int i = offset;
        while (i < end && header[i] != 0) {
            result.append((char) header[i]);
            i++;
        }
        return result;
    }

    public static int getNameBytes(StringBuffer name2, byte[] buf, int offset, int length) {
        int i = 0;
        while (i < length && i < name2.length()) {
            buf[offset + i] = (byte) name2.charAt(i);
            i++;
        }
        while (i < length) {
            buf[offset + i] = 0;
            i++;
        }
        return offset + length;
    }
}
