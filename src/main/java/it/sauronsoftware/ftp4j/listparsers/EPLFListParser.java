package it.sauronsoftware.ftp4j.listparsers;

import dji.component.accountcenter.IMemberProtocol;
import it.sauronsoftware.ftp4j.FTPFile;
import it.sauronsoftware.ftp4j.FTPListParseException;
import it.sauronsoftware.ftp4j.FTPListParser;
import java.util.Date;
import java.util.StringTokenizer;

public class EPLFListParser implements FTPListParser {
    public FTPFile[] parse(String[] lines) throws FTPListParseException {
        int i;
        int size = lines.length;
        FTPFile[] ret = null;
        for (int i2 = 0; i2 < size; i2++) {
            String l = lines[i2];
            if (l.charAt(0) != '+') {
                throw new FTPListParseException();
            }
            int a = l.indexOf(9);
            if (a == -1) {
                throw new FTPListParseException();
            }
            String facts = l.substring(1, a);
            String name = l.substring(a + 1, l.length());
            Date md = null;
            boolean dir = false;
            long fileSize = 0;
            StringTokenizer stringTokenizer = new StringTokenizer(facts, ",");
            while (stringTokenizer.hasMoreTokens()) {
                String f = stringTokenizer.nextToken();
                int s = f.length();
                if (s > 0) {
                    if (s != 1) {
                        char c = f.charAt(0);
                        String value = f.substring(1, s);
                        if (c == 's') {
                            try {
                                fileSize = Long.parseLong(value);
                            } catch (Throwable th) {
                            }
                        } else if (c == 'm') {
                            try {
                                md = new Date(1000 * Long.parseLong(value));
                            } catch (Throwable th2) {
                            }
                        }
                    } else if (f.equals(IMemberProtocol.PARAM_SEPERATOR)) {
                        dir = true;
                    }
                }
            }
            if (ret == null) {
                ret = new FTPFile[size];
            }
            ret[i2] = new FTPFile();
            ret[i2].setName(name);
            ret[i2].setModifiedDate(md);
            ret[i2].setSize(fileSize);
            FTPFile fTPFile = ret[i2];
            if (dir) {
                i = 1;
            } else {
                i = 0;
            }
            fTPFile.setType(i);
        }
        return ret;
    }

    public static void main(String[] args) throws Throwable {
        FTPFile[] f;
        for (FTPFile fTPFile : new EPLFListParser().parse(new String[]{"+i8388621.29609,m824255902,/,\tdev", "+i8388621.44468,m839956783,r,s10376,\tRFCEPLF"})) {
            System.out.println(fTPFile);
        }
    }
}
