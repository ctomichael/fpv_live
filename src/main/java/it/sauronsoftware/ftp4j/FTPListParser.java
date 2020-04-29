package it.sauronsoftware.ftp4j;

public interface FTPListParser {
    FTPFile[] parse(String[] strArr) throws FTPListParseException;
}
