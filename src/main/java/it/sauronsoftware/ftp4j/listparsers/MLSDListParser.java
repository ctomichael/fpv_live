package it.sauronsoftware.ftp4j.listparsers;

import com.amap.location.common.model.AmapLoc;
import it.sauronsoftware.ftp4j.FTPFile;
import it.sauronsoftware.ftp4j.FTPListParseException;
import it.sauronsoftware.ftp4j.FTPListParser;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import java.util.StringTokenizer;

public class MLSDListParser implements FTPListParser {
    private static final DateFormat MLSD_DATE_FORMAT_1 = new SimpleDateFormat("yyyyMMddhhmmss.SSS Z");
    private static final DateFormat MLSD_DATE_FORMAT_2 = new SimpleDateFormat("yyyyMMddhhmmss Z");

    public FTPFile[] parse(String[] lines) throws FTPListParseException {
        ArrayList list = new ArrayList();
        for (String str : lines) {
            FTPFile file = parseLine(str);
            if (file != null) {
                list.add(file);
            }
        }
        int size = list.size();
        FTPFile[] ret = new FTPFile[size];
        for (int i = 0; i < size; i++) {
            ret[i] = (FTPFile) list.get(i);
        }
        return ret;
    }

    private FTPFile parseLine(String line) throws FTPListParseException {
        int type;
        ArrayList list = new ArrayList();
        StringTokenizer stringTokenizer = new StringTokenizer(line, ";");
        while (stringTokenizer.hasMoreElements()) {
            String aux = stringTokenizer.nextToken().trim();
            if (aux.length() > 0) {
                list.add(aux);
            }
        }
        if (list.size() == 0) {
            throw new FTPListParseException();
        }
        String name = (String) list.remove(list.size() - 1);
        Properties facts = new Properties();
        Iterator i = list.iterator();
        while (i.hasNext()) {
            String aux2 = (String) i.next();
            int sep = aux2.indexOf(61);
            if (sep == -1) {
                throw new FTPListParseException();
            }
            String key = aux2.substring(0, sep).trim();
            String value = aux2.substring(sep + 1, aux2.length()).trim();
            if (key.length() == 0 || value.length() == 0) {
                throw new FTPListParseException();
            }
            facts.setProperty(key, value);
        }
        String typeString = facts.getProperty("type");
        if (typeString == null) {
            throw new FTPListParseException();
        }
        if (AmapLoc.TYPE_OFFLINE_CELL.equalsIgnoreCase(typeString)) {
            type = 0;
        } else if ("dir".equalsIgnoreCase(typeString)) {
            type = 1;
        } else if (!"cdir".equalsIgnoreCase(typeString) && "pdir".equalsIgnoreCase(typeString)) {
            return null;
        } else {
            return null;
        }
        Date modifiedDate = null;
        String modifyString = facts.getProperty("modify");
        if (modifyString != null) {
            String modifyString2 = modifyString + " +0000";
            try {
                synchronized (MLSD_DATE_FORMAT_1) {
                    modifiedDate = MLSD_DATE_FORMAT_1.parse(modifyString2);
                }
            } catch (ParseException e) {
                try {
                    synchronized (MLSD_DATE_FORMAT_2) {
                        modifiedDate = MLSD_DATE_FORMAT_2.parse(modifyString2);
                    }
                } catch (ParseException e2) {
                }
            }
        }
        long size = 0;
        String sizeString = facts.getProperty("size");
        if (sizeString != null) {
            try {
                size = Long.parseLong(sizeString);
            } catch (NumberFormatException e3) {
            }
            if (size < 0) {
                size = 0;
            }
        }
        FTPFile ret = new FTPFile();
        ret.setType(type);
        ret.setModifiedDate(modifiedDate);
        ret.setSize(size);
        ret.setName(name);
        return ret;
    }
}
