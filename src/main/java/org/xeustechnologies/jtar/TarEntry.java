package org.xeustechnologies.jtar;

import dji.component.accountcenter.IMemberProtocol;
import java.io.File;
import java.util.Date;

public class TarEntry {
    protected File file;
    protected TarHeader header;

    private TarEntry() {
        this.file = null;
        this.header = new TarHeader();
    }

    public TarEntry(File file2, String entryName) {
        this();
        this.file = file2;
        extractTarHeader(entryName);
    }

    public TarEntry(byte[] headerBuf) {
        this();
        parseTarHeader(headerBuf);
    }

    public boolean equals(TarEntry it2) {
        return this.header.name.toString().equals(it2.header.name.toString());
    }

    public boolean isDescendent(TarEntry desc) {
        return desc.header.name.toString().startsWith(this.header.name.toString());
    }

    public TarHeader getHeader() {
        return this.header;
    }

    public String getName() {
        return this.header.name.toString();
    }

    public void setName(String name) {
        this.header.name = new StringBuffer(name);
    }

    public int getUserId() {
        return this.header.userId;
    }

    public void setUserId(int userId) {
        this.header.userId = userId;
    }

    public int getGroupId() {
        return this.header.groupId;
    }

    public void setGroupId(int groupId) {
        this.header.groupId = groupId;
    }

    public String getUserName() {
        return this.header.userName.toString();
    }

    public void setUserName(String userName) {
        this.header.userName = new StringBuffer(userName);
    }

    public String getGroupName() {
        return this.header.groupName.toString();
    }

    public void setGroupName(String groupName) {
        this.header.groupName = new StringBuffer(groupName);
    }

    public void setIds(int userId, int groupId) {
        setUserId(userId);
        setGroupId(groupId);
    }

    public void setModTime(long time) {
        this.header.modTime = time / 1000;
    }

    public void setModTime(Date time) {
        this.header.modTime = time.getTime() / 1000;
    }

    public Date getModTime() {
        return new Date(this.header.modTime * 1000);
    }

    public File getFile() {
        return this.file;
    }

    public long getSize() {
        return this.header.size;
    }

    public void setSize(long size) {
        this.header.size = size;
    }

    public boolean isDirectory() {
        if (this.file != null) {
            return this.file.isDirectory();
        }
        if (this.header == null || (this.header.linkFlag != 53 && !this.header.name.toString().endsWith(IMemberProtocol.PARAM_SEPERATOR))) {
            return false;
        }
        return true;
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.lang.String.replace(char, char):java.lang.String}
     arg types: [char, int]
     candidates:
      ClspMth{java.lang.String.replace(java.lang.CharSequence, java.lang.CharSequence):java.lang.String}
      ClspMth{java.lang.String.replace(char, char):java.lang.String} */
    public void extractTarHeader(String entryName) {
        String name = entryName.replace(File.separatorChar, '/');
        if (name.startsWith(IMemberProtocol.PARAM_SEPERATOR)) {
            name = name.substring(1);
        }
        this.header.linkName = new StringBuffer("");
        this.header.name = new StringBuffer(name);
        if (this.file.isDirectory()) {
            this.header.mode = 16877;
            this.header.linkFlag = TarHeader.LF_DIR;
            if (this.header.name.charAt(this.header.name.length() - 1) != '/') {
                this.header.name.append(IMemberProtocol.PARAM_SEPERATOR);
            }
            this.header.size = 0;
        } else {
            this.header.size = this.file.length();
            this.header.mode = 33188;
            this.header.linkFlag = TarHeader.LF_NORMAL;
        }
        this.header.modTime = this.file.lastModified() / 1000;
        this.header.checkSum = 0;
        this.header.devMajor = 0;
        this.header.devMinor = 0;
    }

    public long computeCheckSum(byte[] buf) {
        long sum = 0;
        for (byte b : buf) {
            sum += (long) (b & 255);
        }
        return sum;
    }

    public void writeEntryHeader(byte[] outbuf) {
        int offset = Octal.getLongOctalBytes(this.header.modTime, outbuf, Octal.getLongOctalBytes(this.header.size, outbuf, Octal.getOctalBytes((long) this.header.groupId, outbuf, Octal.getOctalBytes((long) this.header.userId, outbuf, Octal.getOctalBytes((long) this.header.mode, outbuf, TarHeader.getNameBytes(this.header.name, outbuf, 0, 100), 8), 8), 8), 12), 12);
        int csOffset = offset;
        int c = 0;
        int offset2 = offset;
        while (c < 8) {
            outbuf[offset2] = 32;
            c++;
            offset2++;
        }
        outbuf[offset2] = this.header.linkFlag;
        for (int offset3 = Octal.getOctalBytes((long) this.header.devMinor, outbuf, Octal.getOctalBytes((long) this.header.devMajor, outbuf, TarHeader.getNameBytes(this.header.groupName, outbuf, TarHeader.getNameBytes(this.header.userName, outbuf, TarHeader.getNameBytes(this.header.magic, outbuf, TarHeader.getNameBytes(this.header.linkName, outbuf, offset2 + 1, 100), 8), 32), 32), 8), 8); offset3 < outbuf.length; offset3++) {
            outbuf[offset3] = 0;
        }
        Octal.getCheckSumOctalBytes(computeCheckSum(outbuf), outbuf, csOffset, 8);
    }

    public void parseTarHeader(byte[] bh) {
        this.header.name = TarHeader.parseName(bh, 0, 100);
        int offset = 0 + 100;
        this.header.mode = (int) Octal.parseOctal(bh, offset, 8);
        int offset2 = offset + 8;
        this.header.userId = (int) Octal.parseOctal(bh, offset2, 8);
        int offset3 = offset2 + 8;
        this.header.groupId = (int) Octal.parseOctal(bh, offset3, 8);
        int offset4 = offset3 + 8;
        this.header.size = Octal.parseOctal(bh, offset4, 12);
        int offset5 = offset4 + 12;
        this.header.modTime = Octal.parseOctal(bh, offset5, 12);
        int offset6 = offset5 + 12;
        this.header.checkSum = (int) Octal.parseOctal(bh, offset6, 8);
        int offset7 = offset6 + 8;
        int offset8 = offset7 + 1;
        this.header.linkFlag = bh[offset7];
        this.header.linkName = TarHeader.parseName(bh, offset8, 100);
        int offset9 = offset8 + 100;
        this.header.magic = TarHeader.parseName(bh, offset9, 8);
        int offset10 = offset9 + 8;
        this.header.userName = TarHeader.parseName(bh, offset10, 32);
        int offset11 = offset10 + 32;
        this.header.groupName = TarHeader.parseName(bh, offset11, 32);
        int offset12 = offset11 + 32;
        this.header.devMajor = (int) Octal.parseOctal(bh, offset12, 8);
        this.header.devMinor = (int) Octal.parseOctal(bh, offset12 + 8, 8);
    }
}
