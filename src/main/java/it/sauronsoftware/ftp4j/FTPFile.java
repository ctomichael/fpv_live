package it.sauronsoftware.ftp4j;

import com.billy.cc.core.component.CCUtil;
import dji.component.accountcenter.IMemberProtocol;
import java.util.Date;

public class FTPFile {
    public static final int TYPE_DIRECTORY = 1;
    public static final int TYPE_FILE = 0;
    public static final int TYPE_LINK = 2;
    private String link = null;
    private Date modifiedDate = null;
    private String name = null;
    private long size = -1;
    private int type;

    public Date getModifiedDate() {
        return this.modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate2) {
        this.modifiedDate = modifiedDate2;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type2) {
        this.type = type2;
    }

    public long getSize() {
        return this.size;
    }

    public void setSize(long size2) {
        this.size = size2;
    }

    public String getLink() {
        return this.link;
    }

    public void setLink(String link2) {
        this.link = link2;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName());
        buffer.append(" [name=");
        buffer.append(this.name);
        buffer.append(", type=");
        if (this.type == 0) {
            buffer.append("FILE");
        } else if (this.type == 1) {
            buffer.append("DIRECTORY");
        } else if (this.type == 2) {
            buffer.append("LINK");
            buffer.append(", link=");
            buffer.append(this.link);
        } else {
            buffer.append(CCUtil.PROCESS_UNKNOWN);
        }
        buffer.append(", size=");
        buffer.append(this.size);
        buffer.append(", modifiedDate=");
        buffer.append(this.modifiedDate);
        buffer.append(IMemberProtocol.STRING_SEPERATOR_RIGHT);
        return buffer.toString();
    }
}
