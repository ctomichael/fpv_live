package org.bouncycastle.crypto.tls;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.io.Streams;

public class ServerNameList {
    protected Vector serverNameList;

    public ServerNameList(Vector vector) {
        if (vector == null) {
            throw new IllegalArgumentException("'serverNameList' must not be null");
        }
        this.serverNameList = vector;
    }

    private static short[] checkNameType(short[] sArr, short s) {
        if (!NameType.isValid(s) || Arrays.contains(sArr, s)) {
            return null;
        }
        return Arrays.append(sArr, s);
    }

    public static ServerNameList parse(InputStream inputStream) throws IOException {
        int readUint16 = TlsUtils.readUint16(inputStream);
        if (readUint16 < 1) {
            throw new TlsFatalAlert(50);
        }
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(TlsUtils.readFully(readUint16, inputStream));
        short[] sArr = new short[0];
        Vector vector = new Vector();
        while (byteArrayInputStream.available() > 0) {
            ServerName parse = ServerName.parse(byteArrayInputStream);
            sArr = checkNameType(sArr, parse.getNameType());
            if (sArr == null) {
                throw new TlsFatalAlert(47);
            }
            vector.addElement(parse);
        }
        return new ServerNameList(vector);
    }

    public void encode(OutputStream outputStream) throws IOException {
        int i = 0;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        short[] sArr = new short[0];
        while (true) {
            int i2 = i;
            if (i2 < this.serverNameList.size()) {
                ServerName serverName = (ServerName) this.serverNameList.elementAt(i2);
                sArr = checkNameType(sArr, serverName.getNameType());
                if (sArr == null) {
                    throw new TlsFatalAlert(80);
                }
                serverName.encode(byteArrayOutputStream);
                i = i2 + 1;
            } else {
                TlsUtils.checkUint16(byteArrayOutputStream.size());
                TlsUtils.writeUint16(byteArrayOutputStream.size(), outputStream);
                Streams.writeBufTo(byteArrayOutputStream, outputStream);
                return;
            }
        }
    }

    public Vector getServerNameList() {
        return this.serverNameList;
    }
}
