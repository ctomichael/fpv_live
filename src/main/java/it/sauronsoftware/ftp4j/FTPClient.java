package it.sauronsoftware.ftp4j;

import dji.component.accountcenter.IMemberProtocol;
import it.sauronsoftware.ftp4j.connectors.DirectConnector;
import it.sauronsoftware.ftp4j.extrecognizers.DefaultTextualExtensionRecognizer;
import it.sauronsoftware.ftp4j.listparsers.DOSListParser;
import it.sauronsoftware.ftp4j.listparsers.EPLFListParser;
import it.sauronsoftware.ftp4j.listparsers.MLSDListParser;
import it.sauronsoftware.ftp4j.listparsers.NetWareListParser;
import it.sauronsoftware.ftp4j.listparsers.UnixListParser;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.InetAddress;
import java.net.Socket;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;
import javax.net.ssl.SSLSocketFactory;

public class FTPClient {
    private static final DateFormat MDTM_DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");
    public static final int MLSD_ALWAYS = 1;
    public static final int MLSD_IF_SUPPORTED = 0;
    public static final int MLSD_NEVER = 2;
    private static final Pattern PASV_PATTERN = Pattern.compile("\\d{1,3},\\d{1,3},\\d{1,3},\\d{1,3},\\d{1,3},\\d{1,3}");
    private static final Pattern PWD_PATTERN = Pattern.compile("\"/.*\"");
    public static final int SECURITY_FTP = 0;
    public static final int SECURITY_FTPES = 2;
    public static final int SECURITY_FTPS = 1;
    private static final int SEND_AND_RECEIVE_BUFFER_SIZE = 65536;
    public static final int TYPE_AUTO = 0;
    public static final int TYPE_BINARY = 2;
    public static final int TYPE_TEXTUAL = 1;
    private Object abortLock = new Object();
    private boolean aborted = false;
    private boolean authenticated = false;
    /* access modifiers changed from: private */
    public long autoNoopTimeout = 0;
    private AutoNoopTimer autoNoopTimer;
    private String charset = null;
    private FTPCommunicationChannel communication = null;
    private ArrayList communicationListeners = new ArrayList();
    private boolean compressionEnabled = false;
    private boolean connected = false;
    /* access modifiers changed from: private */
    public FTPConnector connector = new DirectConnector();
    private boolean consumeAborCommandReply = false;
    /* access modifiers changed from: private */
    public boolean dataChannelEncrypted = false;
    private InputStream dataTransferInputStream = null;
    private OutputStream dataTransferOutputStream = null;
    /* access modifiers changed from: private */
    public String host = null;
    private ArrayList listParsers = new ArrayList();
    /* access modifiers changed from: private */
    public Object lock = new Object();
    private int mlsdPolicy = 0;
    private boolean mlsdSupported = false;
    private boolean modezEnabled = false;
    private boolean modezSupported = false;
    /* access modifiers changed from: private */
    public long nextAutoNoopTime;
    private boolean ongoingDataTransfer = false;
    private FTPListParser parser = null;
    private boolean passive = true;
    private String password;
    private int port = 0;
    private boolean restSupported = false;
    private int security = 0;
    private SocketProvider socketProvider;
    private SSLSocketFactory sslSocketFactory = ((SSLSocketFactory) SSLSocketFactory.getDefault());
    private FTPTextualExtensionRecognizer textualExtensionRecognizer = DefaultTextualExtensionRecognizer.getInstance();
    private int type = 0;
    private String username;
    private boolean utf8Supported = false;

    public FTPClient() {
        addListParser(new UnixListParser());
        addListParser(new DOSListParser());
        addListParser(new EPLFListParser());
        addListParser(new NetWareListParser());
        addListParser(new MLSDListParser());
    }

    public FTPConnector getConnector() {
        FTPConnector fTPConnector;
        synchronized (this.lock) {
            fTPConnector = this.connector;
        }
        return fTPConnector;
    }

    public void setConnector(FTPConnector connector2) {
        synchronized (this.lock) {
            this.connector = connector2;
        }
    }

    public void setSSLSocketFactory(SSLSocketFactory sslSocketFactory2) {
        synchronized (this.lock) {
            this.sslSocketFactory = sslSocketFactory2;
        }
    }

    public SSLSocketFactory getSSLSocketFactory() {
        SSLSocketFactory sSLSocketFactory;
        synchronized (this.lock) {
            sSLSocketFactory = this.sslSocketFactory;
        }
        return sSLSocketFactory;
    }

    public void setSecurity(int security2) throws IllegalStateException, IllegalArgumentException {
        if (security2 == 0 || security2 == 1 || security2 == 2) {
            synchronized (this.lock) {
                if (this.connected) {
                    throw new IllegalStateException("The security level of the connection can't be changed while the client is connected");
                }
                this.security = security2;
            }
            return;
        }
        throw new IllegalArgumentException("Invalid security");
    }

    public int getSecurity() {
        return this.security;
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{javax.net.ssl.SSLSocketFactory.createSocket(java.net.Socket, java.lang.String, int, boolean):java.net.Socket throws java.io.IOException}
     arg types: [java.net.Socket, java.lang.String, int, int]
     candidates:
      ClspMth{javax.net.SocketFactory.createSocket(java.lang.String, int, java.net.InetAddress, int):java.net.Socket throws java.io.IOException, java.net.UnknownHostException}
      ClspMth{javax.net.SocketFactory.createSocket(java.net.InetAddress, int, java.net.InetAddress, int):java.net.Socket throws java.io.IOException}
      ClspMth{javax.net.ssl.SSLSocketFactory.createSocket(java.net.Socket, java.lang.String, int, boolean):java.net.Socket throws java.io.IOException} */
    /* access modifiers changed from: private */
    public Socket ssl(Socket socket, String host2, int port2) throws IOException {
        return this.sslSocketFactory.createSocket(socket, host2, port2, true);
    }

    public void setPassive(boolean passive2) {
        synchronized (this.lock) {
            this.passive = passive2;
        }
    }

    public void setType(int type2) throws IllegalArgumentException {
        if (type2 == 0 || type2 == 2 || type2 == 1) {
            synchronized (this.lock) {
                this.type = type2;
            }
            return;
        }
        throw new IllegalArgumentException("Invalid type");
    }

    public int getType() {
        int i;
        synchronized (this.lock) {
            i = this.type;
        }
        return i;
    }

    public void setMLSDPolicy(int mlsdPolicy2) throws IllegalArgumentException {
        if (this.type == 0 || this.type == 1 || this.type == 2) {
            synchronized (this.lock) {
                this.mlsdPolicy = mlsdPolicy2;
            }
            return;
        }
        throw new IllegalArgumentException("Invalid MLSD policy");
    }

    public int getMLSDPolicy() {
        int i;
        synchronized (this.lock) {
            i = this.mlsdPolicy;
        }
        return i;
    }

    public String getCharset() {
        String str;
        synchronized (this.lock) {
            str = this.charset;
        }
        return str;
    }

    public void setCharset(String charset2) {
        synchronized (this.lock) {
            this.charset = charset2;
            if (this.connected) {
                try {
                    this.communication.changeCharset(pickCharset());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean isResumeSupported() {
        boolean z;
        synchronized (this.lock) {
            z = this.restSupported;
        }
        return z;
    }

    public boolean isCompressionSupported() {
        return this.modezSupported;
    }

    public void setCompressionEnabled(boolean compressionEnabled2) {
        this.compressionEnabled = compressionEnabled2;
    }

    public boolean isCompressionEnabled() {
        return this.compressionEnabled;
    }

    public FTPTextualExtensionRecognizer getTextualExtensionRecognizer() {
        FTPTextualExtensionRecognizer fTPTextualExtensionRecognizer;
        synchronized (this.lock) {
            fTPTextualExtensionRecognizer = this.textualExtensionRecognizer;
        }
        return fTPTextualExtensionRecognizer;
    }

    public void setTextualExtensionRecognizer(FTPTextualExtensionRecognizer textualExtensionRecognizer2) {
        synchronized (this.lock) {
            this.textualExtensionRecognizer = textualExtensionRecognizer2;
        }
    }

    public boolean isAuthenticated() {
        boolean z;
        synchronized (this.lock) {
            z = this.authenticated;
        }
        return z;
    }

    public boolean isConnected() {
        boolean z;
        synchronized (this.lock) {
            z = this.connected;
        }
        return z;
    }

    public boolean isPassive() {
        boolean z;
        synchronized (this.lock) {
            z = this.passive;
        }
        return z;
    }

    public String getHost() {
        String str;
        synchronized (this.lock) {
            str = this.host;
        }
        return str;
    }

    public int getPort() {
        int i;
        synchronized (this.lock) {
            i = this.port;
        }
        return i;
    }

    public String getPassword() {
        String str;
        synchronized (this.lock) {
            str = this.password;
        }
        return str;
    }

    public String getUsername() {
        String str;
        synchronized (this.lock) {
            str = this.username;
        }
        return str;
    }

    public void setAutoNoopTimeout(long autoNoopTimeout2) {
        synchronized (this.lock) {
            if (this.connected && this.authenticated) {
                stopAutoNoopTimer();
            }
            long oldValue = this.autoNoopTimeout;
            long newValue = autoNoopTimeout2;
            this.autoNoopTimeout = autoNoopTimeout2;
            if (!(oldValue == 0 || newValue == 0 || this.nextAutoNoopTime <= 0)) {
                this.nextAutoNoopTime -= oldValue - newValue;
            }
            if (this.connected && this.authenticated) {
                startAutoNoopTimer();
            }
        }
    }

    public long getAutoNoopTimeout() {
        long j;
        synchronized (this.lock) {
            j = this.autoNoopTimeout;
        }
        return j;
    }

    public void addCommunicationListener(FTPCommunicationListener listener) {
        synchronized (this.lock) {
            this.communicationListeners.add(listener);
            if (this.communication != null) {
                this.communication.addCommunicationListener(listener);
            }
        }
    }

    public void removeCommunicationListener(FTPCommunicationListener listener) {
        synchronized (this.lock) {
            this.communicationListeners.remove(listener);
            if (this.communication != null) {
                this.communication.removeCommunicationListener(listener);
            }
        }
    }

    public FTPCommunicationListener[] getCommunicationListeners() {
        FTPCommunicationListener[] ret;
        synchronized (this.lock) {
            int size = this.communicationListeners.size();
            ret = new FTPCommunicationListener[size];
            for (int i = 0; i < size; i++) {
                ret[i] = (FTPCommunicationListener) this.communicationListeners.get(i);
            }
        }
        return ret;
    }

    public void addListParser(FTPListParser listParser) {
        synchronized (this.lock) {
            this.listParsers.add(listParser);
        }
    }

    public void removeListParser(FTPListParser listParser) {
        synchronized (this.lock) {
            this.listParsers.remove(listParser);
        }
    }

    public FTPListParser[] getListParsers() {
        FTPListParser[] ret;
        synchronized (this.lock) {
            int size = this.listParsers.size();
            ret = new FTPListParser[size];
            for (int i = 0; i < size; i++) {
                ret[i] = (FTPListParser) this.listParsers.get(i);
            }
        }
        return ret;
    }

    public String[] connect(String host2) throws IllegalStateException, IOException, FTPIllegalReplyException, FTPException {
        int def;
        if (this.security == 1) {
            def = 990;
        } else {
            def = 21;
        }
        return connect(host2, def);
    }

    /* JADX WARNING: Unknown top exception splitter block from list: {B:32:0x0079=Splitter:B:32:0x0079, B:46:0x00bb=Splitter:B:46:0x00bb} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String[] connect(java.lang.String r9, int r10) throws java.lang.IllegalStateException, java.io.IOException, it.sauronsoftware.ftp4j.FTPIllegalReplyException, it.sauronsoftware.ftp4j.FTPException {
        /*
            r8 = this;
            r7 = 1
            java.lang.Object r5 = r8.lock
            monitor-enter(r5)
            boolean r4 = r8.connected     // Catch:{ all -> 0x002d }
            if (r4 == 0) goto L_0x0030
            java.lang.IllegalStateException r4 = new java.lang.IllegalStateException     // Catch:{ all -> 0x002d }
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ all -> 0x002d }
            r6.<init>()     // Catch:{ all -> 0x002d }
            java.lang.String r7 = "Client already connected to "
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ all -> 0x002d }
            java.lang.StringBuilder r6 = r6.append(r9)     // Catch:{ all -> 0x002d }
            java.lang.String r7 = " on port "
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ all -> 0x002d }
            java.lang.StringBuilder r6 = r6.append(r10)     // Catch:{ all -> 0x002d }
            java.lang.String r6 = r6.toString()     // Catch:{ all -> 0x002d }
            r4.<init>(r6)     // Catch:{ all -> 0x002d }
            throw r4     // Catch:{ all -> 0x002d }
        L_0x002d:
            r4 = move-exception
            monitor-exit(r5)     // Catch:{ all -> 0x002d }
            throw r4
        L_0x0030:
            r0 = 0
            it.sauronsoftware.ftp4j.SocketProvider r4 = r8.socketProvider     // Catch:{ IOException -> 0x006d }
            if (r4 == 0) goto L_0x003c
            it.sauronsoftware.ftp4j.FTPConnector r4 = r8.connector     // Catch:{ IOException -> 0x006d }
            it.sauronsoftware.ftp4j.SocketProvider r6 = r8.socketProvider     // Catch:{ IOException -> 0x006d }
            r4.setSocketProvider(r6)     // Catch:{ IOException -> 0x006d }
        L_0x003c:
            it.sauronsoftware.ftp4j.FTPConnector r4 = r8.connector     // Catch:{ IOException -> 0x006d }
            java.net.Socket r0 = r4.connectForCommunicationChannel(r9, r10)     // Catch:{ IOException -> 0x006d }
            int r4 = r8.security     // Catch:{ IOException -> 0x006d }
            if (r4 != r7) goto L_0x004a
            java.net.Socket r0 = r8.ssl(r0, r9, r10)     // Catch:{ IOException -> 0x006d }
        L_0x004a:
            it.sauronsoftware.ftp4j.FTPCommunicationChannel r4 = new it.sauronsoftware.ftp4j.FTPCommunicationChannel     // Catch:{ IOException -> 0x006d }
            java.lang.String r6 = r8.pickCharset()     // Catch:{ IOException -> 0x006d }
            r4.<init>(r0, r6)     // Catch:{ IOException -> 0x006d }
            r8.communication = r4     // Catch:{ IOException -> 0x006d }
            java.util.ArrayList r4 = r8.communicationListeners     // Catch:{ IOException -> 0x006d }
            java.util.Iterator r2 = r4.iterator()     // Catch:{ IOException -> 0x006d }
        L_0x005b:
            boolean r4 = r2.hasNext()     // Catch:{ IOException -> 0x006d }
            if (r4 == 0) goto L_0x007a
            it.sauronsoftware.ftp4j.FTPCommunicationChannel r6 = r8.communication     // Catch:{ IOException -> 0x006d }
            java.lang.Object r4 = r2.next()     // Catch:{ IOException -> 0x006d }
            it.sauronsoftware.ftp4j.FTPCommunicationListener r4 = (it.sauronsoftware.ftp4j.FTPCommunicationListener) r4     // Catch:{ IOException -> 0x006d }
            r6.addCommunicationListener(r4)     // Catch:{ IOException -> 0x006d }
            goto L_0x005b
        L_0x006d:
            r1 = move-exception
            throw r1     // Catch:{ all -> 0x006f }
        L_0x006f:
            r4 = move-exception
            boolean r6 = r8.connected     // Catch:{ all -> 0x002d }
            if (r6 != 0) goto L_0x0079
            if (r0 == 0) goto L_0x0079
            r0.close()     // Catch:{ Throwable -> 0x00bf }
        L_0x0079:
            throw r4     // Catch:{ all -> 0x002d }
        L_0x007a:
            it.sauronsoftware.ftp4j.FTPCommunicationChannel r4 = r8.communication     // Catch:{ IOException -> 0x006d }
            it.sauronsoftware.ftp4j.FTPReply r3 = r4.readFTPReply()     // Catch:{ IOException -> 0x006d }
            boolean r4 = r3.isSuccessCode()     // Catch:{ IOException -> 0x006d }
            if (r4 != 0) goto L_0x008c
            it.sauronsoftware.ftp4j.FTPException r4 = new it.sauronsoftware.ftp4j.FTPException     // Catch:{ IOException -> 0x006d }
            r4.<init>(r3)     // Catch:{ IOException -> 0x006d }
            throw r4     // Catch:{ IOException -> 0x006d }
        L_0x008c:
            r4 = 1
            r8.connected = r4     // Catch:{ IOException -> 0x006d }
            r4 = 0
            r8.authenticated = r4     // Catch:{ IOException -> 0x006d }
            r4 = 0
            r8.parser = r4     // Catch:{ IOException -> 0x006d }
            r8.host = r9     // Catch:{ IOException -> 0x006d }
            r8.port = r10     // Catch:{ IOException -> 0x006d }
            r4 = 0
            r8.username = r4     // Catch:{ IOException -> 0x006d }
            r4 = 0
            r8.password = r4     // Catch:{ IOException -> 0x006d }
            r4 = 0
            r8.utf8Supported = r4     // Catch:{ IOException -> 0x006d }
            r4 = 0
            r8.restSupported = r4     // Catch:{ IOException -> 0x006d }
            r4 = 0
            r8.mlsdSupported = r4     // Catch:{ IOException -> 0x006d }
            r4 = 0
            r8.modezSupported = r4     // Catch:{ IOException -> 0x006d }
            r4 = 0
            r8.dataChannelEncrypted = r4     // Catch:{ IOException -> 0x006d }
            java.lang.String[] r4 = r3.getMessages()     // Catch:{ IOException -> 0x006d }
            boolean r6 = r8.connected     // Catch:{ all -> 0x002d }
            if (r6 != 0) goto L_0x00bb
            if (r0 == 0) goto L_0x00bb
            r0.close()     // Catch:{ Throwable -> 0x00bd }
        L_0x00bb:
            monitor-exit(r5)     // Catch:{ all -> 0x002d }
            return r4
        L_0x00bd:
            r6 = move-exception
            goto L_0x00bb
        L_0x00bf:
            r6 = move-exception
            goto L_0x0079
        */
        throw new UnsupportedOperationException("Method not decompiled: it.sauronsoftware.ftp4j.FTPClient.connect(java.lang.String, int):java.lang.String[]");
    }

    public void abortCurrentConnectionAttempt() {
        this.connector.abortConnectForCommunicationChannel();
    }

    public void disconnect(boolean sendQuitCommand) throws IllegalStateException, IOException, FTPIllegalReplyException, FTPException {
        synchronized (this.lock) {
            if (!this.connected) {
                throw new IllegalStateException("Client not connected");
            }
            if (this.authenticated) {
                stopAutoNoopTimer();
            }
            if (sendQuitCommand) {
                this.communication.sendFTPCommand("QUIT");
                FTPReply r = this.communication.readFTPReply();
                if (!r.isSuccessCode()) {
                    throw new FTPException(r);
                }
            }
            this.communication.close();
            this.communication = null;
            this.connected = false;
        }
    }

    public void abruptlyCloseCommunication() {
        if (this.communication != null) {
            this.communication.close();
            this.communication = null;
        }
        this.connected = false;
        stopAutoNoopTimer();
    }

    public void login(String username2, String password2) throws IllegalStateException, IOException, FTPIllegalReplyException, FTPException {
        login(username2, password2, null);
    }

    public void login(String username2, String password2, String account) throws IllegalStateException, IOException, FTPIllegalReplyException, FTPException {
        boolean passwordRequired;
        boolean accountRequired;
        synchronized (this.lock) {
            if (!this.connected) {
                throw new IllegalStateException("Client not connected");
            }
            if (this.security == 2) {
                this.communication.sendFTPCommand("AUTH TLS");
                if (this.communication.readFTPReply().isSuccessCode()) {
                    this.communication.ssl(this.sslSocketFactory);
                } else {
                    this.communication.sendFTPCommand("AUTH SSL");
                    FTPReply r = this.communication.readFTPReply();
                    if (r.isSuccessCode()) {
                        this.communication.ssl(this.sslSocketFactory);
                    } else {
                        throw new FTPException(r.getCode(), "SECURITY_FTPES cannot be applied: the server refused both AUTH TLS and AUTH SSL commands");
                    }
                }
            }
            this.authenticated = false;
            this.communication.sendFTPCommand("USER " + username2);
            FTPReply r2 = this.communication.readFTPReply();
            switch (r2.getCode()) {
                case FTPCodes.USER_LOGGED_IN /*230*/:
                    passwordRequired = false;
                    accountRequired = false;
                    break;
                case FTPCodes.USERNAME_OK /*331*/:
                    passwordRequired = true;
                    accountRequired = false;
                    break;
                case FTPCodes.NEED_ACCOUNT /*332*/:
                    passwordRequired = false;
                    accountRequired = true;
                    break;
                default:
                    throw new FTPException(r2);
            }
            if (passwordRequired) {
                if (password2 == null) {
                    throw new FTPException((int) FTPCodes.USERNAME_OK);
                }
                this.communication.sendFTPCommand("PASS " + password2);
                FTPReply r3 = this.communication.readFTPReply();
                switch (r3.getCode()) {
                    case FTPCodes.USER_LOGGED_IN /*230*/:
                        accountRequired = false;
                        break;
                    case FTPCodes.NEED_ACCOUNT /*332*/:
                        accountRequired = true;
                        break;
                    default:
                        throw new FTPException(r3);
                }
            }
            if (accountRequired) {
                if (account == null) {
                    throw new FTPException((int) FTPCodes.NEED_ACCOUNT);
                }
                this.communication.sendFTPCommand("ACCT " + account);
                FTPReply r4 = this.communication.readFTPReply();
                switch (r4.getCode()) {
                    case FTPCodes.USER_LOGGED_IN /*230*/:
                        break;
                    default:
                        throw new FTPException(r4);
                }
            }
            this.authenticated = true;
            this.username = username2;
            this.password = password2;
        }
        postLoginOperations();
        startAutoNoopTimer();
    }

    private void postLoginOperations() throws IllegalStateException, IOException, FTPIllegalReplyException, FTPException {
        synchronized (this.lock) {
            this.utf8Supported = false;
            this.restSupported = false;
            this.mlsdSupported = false;
            this.modezSupported = false;
            this.dataChannelEncrypted = false;
            this.communication.sendFTPCommand("FEAT");
            FTPReply r = this.communication.readFTPReply();
            if (r.getCode() == 211) {
                String[] lines = r.getMessages();
                for (int i = 1; i < lines.length - 1; i++) {
                    String feat = lines[i].trim().toUpperCase();
                    if ("REST STREAM".equalsIgnoreCase(feat)) {
                        this.restSupported = true;
                    } else if ("UTF8".equalsIgnoreCase(feat)) {
                        this.utf8Supported = true;
                        this.communication.changeCharset("UTF-8");
                    } else if ("MLSD".equalsIgnoreCase(feat)) {
                        this.mlsdSupported = true;
                    } else if ("MODE Z".equalsIgnoreCase(feat) || feat.startsWith("MODE Z ")) {
                        this.modezSupported = true;
                    }
                }
            }
            if (this.utf8Supported) {
                this.communication.sendFTPCommand("OPTS UTF8 ON");
                this.communication.readFTPReply();
            }
            if (this.security == 1 || this.security == 2) {
                this.communication.sendFTPCommand("PBSZ 0");
                this.communication.readFTPReply();
                this.communication.sendFTPCommand("PROT P");
                if (this.communication.readFTPReply().isSuccessCode()) {
                    this.dataChannelEncrypted = true;
                }
            }
        }
    }

    public void logout() throws IllegalStateException, IOException, FTPIllegalReplyException, FTPException {
        synchronized (this.lock) {
            if (!this.connected) {
                throw new IllegalStateException("Client not connected");
            } else if (!this.authenticated) {
                throw new IllegalStateException("Client not authenticated");
            } else {
                this.communication.sendFTPCommand("REIN");
                FTPReply r = this.communication.readFTPReply();
                if (!r.isSuccessCode()) {
                    throw new FTPException(r);
                }
                stopAutoNoopTimer();
                this.authenticated = false;
                this.username = null;
                this.password = null;
            }
        }
    }

    public void noop() throws IllegalStateException, IOException, FTPIllegalReplyException, FTPException {
        synchronized (this.lock) {
            if (!this.connected) {
                throw new IllegalStateException("Client not connected");
            } else if (!this.authenticated) {
                throw new IllegalStateException("Client not authenticated");
            } else {
                try {
                    this.communication.sendFTPCommand("NOOP");
                    FTPReply r = this.communication.readFTPReply();
                    if (!r.isSuccessCode()) {
                        throw new FTPException(r);
                    }
                    touchAutoNoopTimer();
                } catch (Throwable th) {
                    touchAutoNoopTimer();
                    throw th;
                }
            }
        }
    }

    public FTPReply sendCustomCommand(String command) throws IllegalStateException, IOException, FTPIllegalReplyException {
        FTPReply readFTPReply;
        synchronized (this.lock) {
            if (!this.connected) {
                throw new IllegalStateException("Client not connected");
            }
            this.communication.sendFTPCommand(command);
            touchAutoNoopTimer();
            readFTPReply = this.communication.readFTPReply();
        }
        return readFTPReply;
    }

    public FTPReply sendSiteCommand(String command) throws IllegalStateException, IOException, FTPIllegalReplyException {
        FTPReply readFTPReply;
        synchronized (this.lock) {
            if (!this.connected) {
                throw new IllegalStateException("Client not connected");
            }
            this.communication.sendFTPCommand("SITE " + command);
            touchAutoNoopTimer();
            readFTPReply = this.communication.readFTPReply();
        }
        return readFTPReply;
    }

    public void changeAccount(String account) throws IllegalStateException, IOException, FTPIllegalReplyException, FTPException {
        synchronized (this.lock) {
            if (!this.connected) {
                throw new IllegalStateException("Client not connected");
            } else if (!this.authenticated) {
                throw new IllegalStateException("Client not authenticated");
            } else {
                this.communication.sendFTPCommand("ACCT " + account);
                FTPReply r = this.communication.readFTPReply();
                touchAutoNoopTimer();
                if (!r.isSuccessCode()) {
                    throw new FTPException(r);
                }
            }
        }
    }

    public String currentDirectory() throws IllegalStateException, IOException, FTPIllegalReplyException, FTPException {
        String substring;
        synchronized (this.lock) {
            if (!this.connected) {
                throw new IllegalStateException("Client not connected");
            } else if (!this.authenticated) {
                throw new IllegalStateException("Client not authenticated");
            } else {
                this.communication.sendFTPCommand("PWD");
                FTPReply r = this.communication.readFTPReply();
                touchAutoNoopTimer();
                if (!r.isSuccessCode()) {
                    throw new FTPException(r);
                }
                String[] messages = r.getMessages();
                if (messages.length != 1) {
                    throw new FTPIllegalReplyException();
                }
                Matcher m = PWD_PATTERN.matcher(messages[0]);
                if (m.find()) {
                    substring = messages[0].substring(m.start() + 1, m.end() - 1);
                } else {
                    throw new FTPIllegalReplyException();
                }
            }
        }
        return substring;
    }

    public void changeDirectory(String path) throws IllegalStateException, IOException, FTPIllegalReplyException, FTPException {
        synchronized (this.lock) {
            if (!this.connected) {
                throw new IllegalStateException("Client not connected");
            } else if (!this.authenticated) {
                throw new IllegalStateException("Client not authenticated");
            } else {
                this.communication.sendFTPCommand("CWD " + path);
                FTPReply r = this.communication.readFTPReply();
                touchAutoNoopTimer();
                if (!r.isSuccessCode()) {
                    throw new FTPException(r);
                }
            }
        }
    }

    public void changeDirectoryUp() throws IllegalStateException, IOException, FTPIllegalReplyException, FTPException {
        synchronized (this.lock) {
            if (!this.connected) {
                throw new IllegalStateException("Client not connected");
            } else if (!this.authenticated) {
                throw new IllegalStateException("Client not authenticated");
            } else {
                this.communication.sendFTPCommand("CDUP");
                FTPReply r = this.communication.readFTPReply();
                touchAutoNoopTimer();
                if (!r.isSuccessCode()) {
                    throw new FTPException(r);
                }
            }
        }
    }

    public Date modifiedDate(String path) throws IllegalStateException, IOException, FTPIllegalReplyException, FTPException {
        Date parse;
        synchronized (this.lock) {
            if (!this.connected) {
                throw new IllegalStateException("Client not connected");
            } else if (!this.authenticated) {
                throw new IllegalStateException("Client not authenticated");
            } else {
                this.communication.sendFTPCommand("MDTM " + path);
                FTPReply r = this.communication.readFTPReply();
                touchAutoNoopTimer();
                if (!r.isSuccessCode()) {
                    throw new FTPException(r);
                }
                String[] messages = r.getMessages();
                if (messages.length != 1) {
                    throw new FTPIllegalReplyException();
                }
                try {
                    parse = MDTM_DATE_FORMAT.parse(messages[0]);
                } catch (ParseException e) {
                    throw new FTPIllegalReplyException();
                }
            }
        }
        return parse;
    }

    public long fileSize(String path) throws IllegalStateException, IOException, FTPIllegalReplyException, FTPException {
        long parseLong;
        synchronized (this.lock) {
            if (!this.connected) {
                throw new IllegalStateException("Client not connected");
            } else if (!this.authenticated) {
                throw new IllegalStateException("Client not authenticated");
            } else {
                this.communication.sendFTPCommand("TYPE I");
                FTPReply r = this.communication.readFTPReply();
                touchAutoNoopTimer();
                if (!r.isSuccessCode()) {
                    throw new FTPException(r);
                }
                this.communication.sendFTPCommand("SIZE " + path);
                FTPReply r2 = this.communication.readFTPReply();
                touchAutoNoopTimer();
                if (!r2.isSuccessCode()) {
                    throw new FTPException(r2);
                }
                String[] messages = r2.getMessages();
                if (messages.length != 1) {
                    throw new FTPIllegalReplyException();
                }
                try {
                    parseLong = Long.parseLong(messages[0]);
                } catch (Throwable th) {
                    throw new FTPIllegalReplyException();
                }
            }
        }
        return parseLong;
    }

    public void rename(String oldPath, String newPath) throws IllegalStateException, IOException, FTPIllegalReplyException, FTPException {
        synchronized (this.lock) {
            if (!this.connected) {
                throw new IllegalStateException("Client not connected");
            } else if (!this.authenticated) {
                throw new IllegalStateException("Client not authenticated");
            } else {
                this.communication.sendFTPCommand("RNFR " + oldPath);
                FTPReply r = this.communication.readFTPReply();
                touchAutoNoopTimer();
                if (r.getCode() != 350) {
                    throw new FTPException(r);
                }
                this.communication.sendFTPCommand("RNTO " + newPath);
                FTPReply r2 = this.communication.readFTPReply();
                touchAutoNoopTimer();
                if (!r2.isSuccessCode()) {
                    throw new FTPException(r2);
                }
            }
        }
    }

    public void deleteFile(String path) throws IllegalStateException, IOException, FTPIllegalReplyException, FTPException {
        synchronized (this.lock) {
            if (!this.connected) {
                throw new IllegalStateException("Client not connected");
            } else if (!this.authenticated) {
                throw new IllegalStateException("Client not authenticated");
            } else {
                this.communication.sendFTPCommand("DELE " + path);
                FTPReply r = this.communication.readFTPReply();
                touchAutoNoopTimer();
                if (!r.isSuccessCode()) {
                    throw new FTPException(r);
                }
            }
        }
    }

    public void deleteDirectory(String path) throws IllegalStateException, IOException, FTPIllegalReplyException, FTPException {
        synchronized (this.lock) {
            if (!this.connected) {
                throw new IllegalStateException("Client not connected");
            } else if (!this.authenticated) {
                throw new IllegalStateException("Client not authenticated");
            } else {
                this.communication.sendFTPCommand("RMD " + path);
                FTPReply r = this.communication.readFTPReply();
                touchAutoNoopTimer();
                if (!r.isSuccessCode()) {
                    throw new FTPException(r);
                }
            }
        }
    }

    public void createDirectory(String directoryName) throws IllegalStateException, IOException, FTPIllegalReplyException, FTPException {
        synchronized (this.lock) {
            if (!this.connected) {
                throw new IllegalStateException("Client not connected");
            } else if (!this.authenticated) {
                throw new IllegalStateException("Client not authenticated");
            } else {
                this.communication.sendFTPCommand("MKD " + directoryName);
                FTPReply r = this.communication.readFTPReply();
                touchAutoNoopTimer();
                if (!r.isSuccessCode()) {
                    throw new FTPException(r);
                }
            }
        }
    }

    public String[] help() throws IllegalStateException, IOException, FTPIllegalReplyException, FTPException {
        String[] messages;
        synchronized (this.lock) {
            if (!this.connected) {
                throw new IllegalStateException("Client not connected");
            } else if (!this.authenticated) {
                throw new IllegalStateException("Client not authenticated");
            } else {
                this.communication.sendFTPCommand("HELP");
                FTPReply r = this.communication.readFTPReply();
                touchAutoNoopTimer();
                if (!r.isSuccessCode()) {
                    throw new FTPException(r);
                }
                messages = r.getMessages();
            }
        }
        return messages;
    }

    public String[] serverStatus() throws IllegalStateException, IOException, FTPIllegalReplyException, FTPException {
        String[] messages;
        synchronized (this.lock) {
            if (!this.connected) {
                throw new IllegalStateException("Client not connected");
            } else if (!this.authenticated) {
                throw new IllegalStateException("Client not authenticated");
            } else {
                this.communication.sendFTPCommand("STAT");
                FTPReply r = this.communication.readFTPReply();
                touchAutoNoopTimer();
                if (!r.isSuccessCode()) {
                    throw new FTPException(r);
                }
                messages = r.getMessages();
            }
        }
        return messages;
    }

    /* JADX INFO: Multiple debug info for r8v2 java.util.Iterator: [D('i' int), D('i' java.util.Iterator)] */
    public FTPFile[] list(String fileSpec) throws IllegalStateException, IOException, FTPIllegalReplyException, FTPException, FTPDataTransferException, FTPAbortedException, FTPListParseException {
        boolean mlsdCommand;
        boolean wasAborted;
        FTPFile[] ret;
        synchronized (this.lock) {
            if (!this.connected) {
                throw new IllegalStateException("Client not connected");
            } else if (!this.authenticated) {
                throw new IllegalStateException("Client not authenticated");
            } else {
                this.communication.sendFTPCommand("TYPE A");
                FTPReply r = this.communication.readFTPReply();
                touchAutoNoopTimer();
                if (!r.isSuccessCode()) {
                    throw new FTPException(r);
                }
                FTPDataTransferConnectionProvider provider = openDataTransferChannel();
                if (this.mlsdPolicy == 0) {
                    mlsdCommand = this.mlsdSupported;
                } else if (this.mlsdPolicy == 1) {
                    mlsdCommand = true;
                } else {
                    mlsdCommand = false;
                }
                String command = mlsdCommand ? "MLSD" : "LIST";
                if (fileSpec != null && fileSpec.length() > 0) {
                    command = command + " " + fileSpec;
                }
                ArrayList lines = new ArrayList();
                boolean wasAborted2 = false;
                this.communication.sendFTPCommand(command);
                try {
                    Socket dtConnection = provider.openDataTransferConnection();
                    provider.dispose();
                    synchronized (this.abortLock) {
                        this.ongoingDataTransfer = true;
                        this.aborted = false;
                        this.consumeAborCommandReply = false;
                    }
                    NVTASCIIReader dataReader = null;
                    try {
                        this.dataTransferInputStream = dtConnection.getInputStream();
                        if (this.modezEnabled) {
                            this.dataTransferInputStream = new InflaterInputStream(this.dataTransferInputStream);
                        }
                        NVTASCIIReader dataReader2 = new NVTASCIIReader(this.dataTransferInputStream, mlsdCommand ? "UTF-8" : pickCharset());
                        while (true) {
                            try {
                                String line = dataReader2.readLine();
                                if (line == null) {
                                    break;
                                } else if (line.length() > 0) {
                                    lines.add(line);
                                }
                            } catch (IOException e) {
                                e = e;
                                dataReader = dataReader2;
                                try {
                                    synchronized (this.abortLock) {
                                        if (this.aborted) {
                                            throw new FTPAbortedException();
                                        }
                                        throw new FTPDataTransferException("I/O error in data transfer", e);
                                    }
                                } catch (Throwable th) {
                                    th = th;
                                }
                            } catch (Throwable th2) {
                                th = th2;
                                dataReader = dataReader2;
                                if (dataReader != null) {
                                    try {
                                        dataReader.close();
                                    } catch (Throwable th3) {
                                    }
                                }
                                try {
                                    dtConnection.close();
                                } catch (Throwable th4) {
                                }
                                this.dataTransferInputStream = null;
                                synchronized (this.abortLock) {
                                    wasAborted2 = this.aborted;
                                    this.ongoingDataTransfer = false;
                                    this.aborted = false;
                                }
                                throw th;
                            }
                        }
                        if (dataReader2 != null) {
                            try {
                                dataReader2.close();
                            } catch (Throwable th5) {
                            }
                        }
                        try {
                            dtConnection.close();
                        } catch (Throwable th6) {
                        }
                        this.dataTransferInputStream = null;
                        synchronized (this.abortLock) {
                            wasAborted = this.aborted;
                            this.ongoingDataTransfer = false;
                            this.aborted = false;
                        }
                        FTPReply r2 = this.communication.readFTPReply();
                        touchAutoNoopTimer();
                        if (r2.getCode() == 150 || r2.getCode() == 125) {
                            FTPReply r3 = this.communication.readFTPReply();
                            if (wasAborted || r3.getCode() == 226) {
                                if (this.consumeAborCommandReply) {
                                    this.communication.readFTPReply();
                                    this.consumeAborCommandReply = false;
                                }
                                int size = lines.size();
                                String[] list = new String[size];
                                for (int i = 0; i < size; i++) {
                                    list[i] = (String) lines.get(i);
                                }
                                ret = null;
                                if (mlsdCommand) {
                                    ret = new MLSDListParser().parse(list);
                                } else {
                                    if (this.parser != null) {
                                        try {
                                            ret = this.parser.parse(list);
                                        } catch (FTPListParseException e2) {
                                            this.parser = null;
                                        }
                                    }
                                    if (ret == null) {
                                        Iterator i2 = this.listParsers.iterator();
                                        while (i2.hasNext()) {
                                            FTPListParser aux = (FTPListParser) i2.next();
                                            try {
                                                ret = aux.parse(list);
                                                this.parser = aux;
                                                break;
                                            } catch (FTPListParseException e3) {
                                            }
                                        }
                                    }
                                }
                                if (ret == null) {
                                    throw new FTPListParseException();
                                }
                            } else {
                                throw new FTPException(r3);
                            }
                        } else {
                            throw new FTPException(r2);
                        }
                    } catch (IOException e4) {
                        e = e4;
                    }
                } catch (Throwable th7) {
                    FTPReply r4 = this.communication.readFTPReply();
                    touchAutoNoopTimer();
                    if (r4.getCode() == 150 || r4.getCode() == 125) {
                        FTPReply r5 = this.communication.readFTPReply();
                        if (wasAborted2 || r5.getCode() == 226) {
                            if (this.consumeAborCommandReply) {
                                this.communication.readFTPReply();
                                this.consumeAborCommandReply = false;
                            }
                            throw th7;
                        }
                        throw new FTPException(r5);
                    }
                    throw new FTPException(r4);
                }
            }
        }
        return ret;
    }

    public FTPFile[] list() throws IllegalStateException, IOException, FTPIllegalReplyException, FTPException, FTPDataTransferException, FTPAbortedException, FTPListParseException {
        return list(null);
    }

    /* JADX WARNING: Removed duplicated region for block: B:49:0x00c9  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String[] listNames() throws java.lang.IllegalStateException, java.io.IOException, it.sauronsoftware.ftp4j.FTPIllegalReplyException, it.sauronsoftware.ftp4j.FTPException, it.sauronsoftware.ftp4j.FTPDataTransferException, it.sauronsoftware.ftp4j.FTPAbortedException, it.sauronsoftware.ftp4j.FTPListParseException {
        /*
            r18 = this;
            r0 = r18
            java.lang.Object r15 = r0.lock
            monitor-enter(r15)
            r0 = r18
            boolean r14 = r0.connected     // Catch:{ all -> 0x0016 }
            if (r14 != 0) goto L_0x0019
            java.lang.IllegalStateException r14 = new java.lang.IllegalStateException     // Catch:{ all -> 0x0016 }
            java.lang.String r16 = "Client not connected"
            r0 = r16
            r14.<init>(r0)     // Catch:{ all -> 0x0016 }
            throw r14     // Catch:{ all -> 0x0016 }
        L_0x0016:
            r14 = move-exception
            monitor-exit(r15)     // Catch:{ all -> 0x0016 }
            throw r14
        L_0x0019:
            r0 = r18
            boolean r14 = r0.authenticated     // Catch:{ all -> 0x0016 }
            if (r14 != 0) goto L_0x002a
            java.lang.IllegalStateException r14 = new java.lang.IllegalStateException     // Catch:{ all -> 0x0016 }
            java.lang.String r16 = "Client not authenticated"
            r0 = r16
            r14.<init>(r0)     // Catch:{ all -> 0x0016 }
            throw r14     // Catch:{ all -> 0x0016 }
        L_0x002a:
            r0 = r18
            it.sauronsoftware.ftp4j.FTPCommunicationChannel r14 = r0.communication     // Catch:{ all -> 0x0016 }
            java.lang.String r16 = "TYPE A"
            r0 = r16
            r14.sendFTPCommand(r0)     // Catch:{ all -> 0x0016 }
            r0 = r18
            it.sauronsoftware.ftp4j.FTPCommunicationChannel r14 = r0.communication     // Catch:{ all -> 0x0016 }
            it.sauronsoftware.ftp4j.FTPReply r11 = r14.readFTPReply()     // Catch:{ all -> 0x0016 }
            r18.touchAutoNoopTimer()     // Catch:{ all -> 0x0016 }
            boolean r14 = r11.isSuccessCode()     // Catch:{ all -> 0x0016 }
            if (r14 != 0) goto L_0x004d
            it.sauronsoftware.ftp4j.FTPException r14 = new it.sauronsoftware.ftp4j.FTPException     // Catch:{ all -> 0x0016 }
            r14.<init>(r11)     // Catch:{ all -> 0x0016 }
            throw r14     // Catch:{ all -> 0x0016 }
        L_0x004d:
            java.util.ArrayList r8 = new java.util.ArrayList     // Catch:{ all -> 0x0016 }
            r8.<init>()     // Catch:{ all -> 0x0016 }
            r13 = 0
            it.sauronsoftware.ftp4j.FTPDataTransferConnectionProvider r10 = r18.openDataTransferChannel()     // Catch:{ all -> 0x0016 }
            r0 = r18
            it.sauronsoftware.ftp4j.FTPCommunicationChannel r14 = r0.communication     // Catch:{ all -> 0x0016 }
            java.lang.String r16 = "NLST"
            r0 = r16
            r14.sendFTPCommand(r0)     // Catch:{ all -> 0x0016 }
            java.net.Socket r4 = r10.openDataTransferConnection()     // Catch:{ all -> 0x012f }
            r10.dispose()     // Catch:{ all -> 0x0106 }
            r0 = r18
            java.lang.Object r0 = r0.abortLock     // Catch:{ all -> 0x0106 }
            r16 = r0
            monitor-enter(r16)     // Catch:{ all -> 0x0106 }
            r14 = 1
            r0 = r18
            r0.ongoingDataTransfer = r14     // Catch:{ all -> 0x0134 }
            r14 = 0
            r0 = r18
            r0.aborted = r14     // Catch:{ all -> 0x0134 }
            r14 = 0
            r0 = r18
            r0.consumeAborCommandReply = r14     // Catch:{ all -> 0x0134 }
            monitor-exit(r16)     // Catch:{ all -> 0x0134 }
            r2 = 0
            java.io.InputStream r14 = r4.getInputStream()     // Catch:{ IOException -> 0x0217 }
            r0 = r18
            r0.dataTransferInputStream = r14     // Catch:{ IOException -> 0x0217 }
            r0 = r18
            boolean r14 = r0.modezEnabled     // Catch:{ IOException -> 0x0217 }
            if (r14 == 0) goto L_0x00a1
            java.util.zip.InflaterInputStream r14 = new java.util.zip.InflaterInputStream     // Catch:{ IOException -> 0x0217 }
            r0 = r18
            java.io.InputStream r0 = r0.dataTransferInputStream     // Catch:{ IOException -> 0x0217 }
            r16 = r0
            r0 = r16
            r14.<init>(r0)     // Catch:{ IOException -> 0x0217 }
            r0 = r18
            r0.dataTransferInputStream = r14     // Catch:{ IOException -> 0x0217 }
        L_0x00a1:
            it.sauronsoftware.ftp4j.NVTASCIIReader r3 = new it.sauronsoftware.ftp4j.NVTASCIIReader     // Catch:{ IOException -> 0x0217 }
            r0 = r18
            java.io.InputStream r14 = r0.dataTransferInputStream     // Catch:{ IOException -> 0x0217 }
            java.lang.String r16 = r18.pickCharset()     // Catch:{ IOException -> 0x0217 }
            r0 = r16
            r3.<init>(r14, r0)     // Catch:{ IOException -> 0x0217 }
        L_0x00b0:
            java.lang.String r7 = r3.readLine()     // Catch:{ IOException -> 0x00c0, all -> 0x0213 }
            if (r7 == 0) goto L_0x0137
            int r14 = r7.length()     // Catch:{ IOException -> 0x00c0, all -> 0x0213 }
            if (r14 <= 0) goto L_0x00b0
            r8.add(r7)     // Catch:{ IOException -> 0x00c0, all -> 0x0213 }
            goto L_0x00b0
        L_0x00c0:
            r5 = move-exception
            r2 = r3
        L_0x00c2:
            r0 = r18
            java.lang.Object r0 = r0.abortLock     // Catch:{ all -> 0x00d8 }
            r16 = r0
            monitor-enter(r16)     // Catch:{ all -> 0x00d8 }
            r0 = r18
            boolean r14 = r0.aborted     // Catch:{ all -> 0x00d5 }
            if (r14 == 0) goto L_0x017f
            it.sauronsoftware.ftp4j.FTPAbortedException r14 = new it.sauronsoftware.ftp4j.FTPAbortedException     // Catch:{ all -> 0x00d5 }
            r14.<init>()     // Catch:{ all -> 0x00d5 }
            throw r14     // Catch:{ all -> 0x00d5 }
        L_0x00d5:
            r14 = move-exception
            monitor-exit(r16)     // Catch:{ all -> 0x00d5 }
            throw r14     // Catch:{ all -> 0x00d8 }
        L_0x00d8:
            r14 = move-exception
        L_0x00d9:
            if (r2 == 0) goto L_0x00de
            r2.close()     // Catch:{ Throwable -> 0x020d }
        L_0x00de:
            r4.close()     // Catch:{ Throwable -> 0x0210 }
        L_0x00e1:
            r16 = 0
            r0 = r16
            r1 = r18
            r1.dataTransferInputStream = r0     // Catch:{ all -> 0x0106 }
            r0 = r18
            java.lang.Object r0 = r0.abortLock     // Catch:{ all -> 0x0106 }
            r16 = r0
            monitor-enter(r16)     // Catch:{ all -> 0x0106 }
            r0 = r18
            boolean r13 = r0.aborted     // Catch:{ all -> 0x018a }
            r17 = 0
            r0 = r17
            r1 = r18
            r1.ongoingDataTransfer = r0     // Catch:{ all -> 0x018a }
            r17 = 0
            r0 = r17
            r1 = r18
            r1.aborted = r0     // Catch:{ all -> 0x018a }
            monitor-exit(r16)     // Catch:{ all -> 0x018a }
            throw r14     // Catch:{ all -> 0x0106 }
        L_0x0106:
            r14 = move-exception
            r0 = r18
            it.sauronsoftware.ftp4j.FTPCommunicationChannel r0 = r0.communication     // Catch:{ all -> 0x0016 }
            r16 = r0
            it.sauronsoftware.ftp4j.FTPReply r11 = r16.readFTPReply()     // Catch:{ all -> 0x0016 }
            int r16 = r11.getCode()     // Catch:{ all -> 0x0016 }
            r17 = 150(0x96, float:2.1E-43)
            r0 = r16
            r1 = r17
            if (r0 == r1) goto L_0x01cd
            int r16 = r11.getCode()     // Catch:{ all -> 0x0016 }
            r17 = 125(0x7d, float:1.75E-43)
            r0 = r16
            r1 = r17
            if (r0 == r1) goto L_0x01cd
            it.sauronsoftware.ftp4j.FTPException r14 = new it.sauronsoftware.ftp4j.FTPException     // Catch:{ all -> 0x0016 }
            r14.<init>(r11)     // Catch:{ all -> 0x0016 }
            throw r14     // Catch:{ all -> 0x0016 }
        L_0x012f:
            r14 = move-exception
            r10.dispose()     // Catch:{ all -> 0x0106 }
            throw r14     // Catch:{ all -> 0x0106 }
        L_0x0134:
            r14 = move-exception
            monitor-exit(r16)     // Catch:{ all -> 0x0134 }
            throw r14     // Catch:{ all -> 0x0106 }
        L_0x0137:
            if (r3 == 0) goto L_0x013c
            r3.close()     // Catch:{ Throwable -> 0x0207 }
        L_0x013c:
            r4.close()     // Catch:{ Throwable -> 0x020a }
        L_0x013f:
            r14 = 0
            r0 = r18
            r0.dataTransferInputStream = r14     // Catch:{ all -> 0x0106 }
            r0 = r18
            java.lang.Object r0 = r0.abortLock     // Catch:{ all -> 0x0106 }
            r16 = r0
            monitor-enter(r16)     // Catch:{ all -> 0x0106 }
            r0 = r18
            boolean r13 = r0.aborted     // Catch:{ all -> 0x017c }
            r14 = 0
            r0 = r18
            r0.ongoingDataTransfer = r14     // Catch:{ all -> 0x017c }
            r14 = 0
            r0 = r18
            r0.aborted = r14     // Catch:{ all -> 0x017c }
            monitor-exit(r16)     // Catch:{ all -> 0x017c }
            r0 = r18
            it.sauronsoftware.ftp4j.FTPCommunicationChannel r14 = r0.communication     // Catch:{ all -> 0x0016 }
            it.sauronsoftware.ftp4j.FTPReply r11 = r14.readFTPReply()     // Catch:{ all -> 0x0016 }
            int r14 = r11.getCode()     // Catch:{ all -> 0x0016 }
            r16 = 150(0x96, float:2.1E-43)
            r0 = r16
            if (r14 == r0) goto L_0x018d
            int r14 = r11.getCode()     // Catch:{ all -> 0x0016 }
            r16 = 125(0x7d, float:1.75E-43)
            r0 = r16
            if (r14 == r0) goto L_0x018d
            it.sauronsoftware.ftp4j.FTPException r14 = new it.sauronsoftware.ftp4j.FTPException     // Catch:{ all -> 0x0016 }
            r14.<init>(r11)     // Catch:{ all -> 0x0016 }
            throw r14     // Catch:{ all -> 0x0016 }
        L_0x017c:
            r14 = move-exception
            monitor-exit(r16)     // Catch:{ all -> 0x017c }
            throw r14     // Catch:{ all -> 0x0106 }
        L_0x017f:
            it.sauronsoftware.ftp4j.FTPDataTransferException r14 = new it.sauronsoftware.ftp4j.FTPDataTransferException     // Catch:{ all -> 0x00d5 }
            java.lang.String r17 = "I/O error in data transfer"
            r0 = r17
            r14.<init>(r0, r5)     // Catch:{ all -> 0x00d5 }
            throw r14     // Catch:{ all -> 0x00d5 }
        L_0x018a:
            r14 = move-exception
            monitor-exit(r16)     // Catch:{ all -> 0x018a }
            throw r14     // Catch:{ all -> 0x0106 }
        L_0x018d:
            r0 = r18
            it.sauronsoftware.ftp4j.FTPCommunicationChannel r14 = r0.communication     // Catch:{ all -> 0x0016 }
            it.sauronsoftware.ftp4j.FTPReply r11 = r14.readFTPReply()     // Catch:{ all -> 0x0016 }
            if (r13 != 0) goto L_0x01a7
            int r14 = r11.getCode()     // Catch:{ all -> 0x0016 }
            r16 = 226(0xe2, float:3.17E-43)
            r0 = r16
            if (r14 == r0) goto L_0x01a7
            it.sauronsoftware.ftp4j.FTPException r14 = new it.sauronsoftware.ftp4j.FTPException     // Catch:{ all -> 0x0016 }
            r14.<init>(r11)     // Catch:{ all -> 0x0016 }
            throw r14     // Catch:{ all -> 0x0016 }
        L_0x01a7:
            r0 = r18
            boolean r14 = r0.consumeAborCommandReply     // Catch:{ all -> 0x0016 }
            if (r14 == 0) goto L_0x01b9
            r0 = r18
            it.sauronsoftware.ftp4j.FTPCommunicationChannel r14 = r0.communication     // Catch:{ all -> 0x0016 }
            r14.readFTPReply()     // Catch:{ all -> 0x0016 }
            r14 = 0
            r0 = r18
            r0.consumeAborCommandReply = r14     // Catch:{ all -> 0x0016 }
        L_0x01b9:
            int r12 = r8.size()     // Catch:{ all -> 0x0016 }
            java.lang.String[] r9 = new java.lang.String[r12]     // Catch:{ all -> 0x0016 }
            r6 = 0
        L_0x01c0:
            if (r6 >= r12) goto L_0x0205
            java.lang.Object r14 = r8.get(r6)     // Catch:{ all -> 0x0016 }
            java.lang.String r14 = (java.lang.String) r14     // Catch:{ all -> 0x0016 }
            r9[r6] = r14     // Catch:{ all -> 0x0016 }
            int r6 = r6 + 1
            goto L_0x01c0
        L_0x01cd:
            r0 = r18
            it.sauronsoftware.ftp4j.FTPCommunicationChannel r0 = r0.communication     // Catch:{ all -> 0x0016 }
            r16 = r0
            it.sauronsoftware.ftp4j.FTPReply r11 = r16.readFTPReply()     // Catch:{ all -> 0x0016 }
            if (r13 != 0) goto L_0x01eb
            int r16 = r11.getCode()     // Catch:{ all -> 0x0016 }
            r17 = 226(0xe2, float:3.17E-43)
            r0 = r16
            r1 = r17
            if (r0 == r1) goto L_0x01eb
            it.sauronsoftware.ftp4j.FTPException r14 = new it.sauronsoftware.ftp4j.FTPException     // Catch:{ all -> 0x0016 }
            r14.<init>(r11)     // Catch:{ all -> 0x0016 }
            throw r14     // Catch:{ all -> 0x0016 }
        L_0x01eb:
            r0 = r18
            boolean r0 = r0.consumeAborCommandReply     // Catch:{ all -> 0x0016 }
            r16 = r0
            if (r16 == 0) goto L_0x0204
            r0 = r18
            it.sauronsoftware.ftp4j.FTPCommunicationChannel r0 = r0.communication     // Catch:{ all -> 0x0016 }
            r16 = r0
            r16.readFTPReply()     // Catch:{ all -> 0x0016 }
            r16 = 0
            r0 = r16
            r1 = r18
            r1.consumeAborCommandReply = r0     // Catch:{ all -> 0x0016 }
        L_0x0204:
            throw r14     // Catch:{ all -> 0x0016 }
        L_0x0205:
            monitor-exit(r15)     // Catch:{ all -> 0x0016 }
            return r9
        L_0x0207:
            r14 = move-exception
            goto L_0x013c
        L_0x020a:
            r14 = move-exception
            goto L_0x013f
        L_0x020d:
            r16 = move-exception
            goto L_0x00de
        L_0x0210:
            r16 = move-exception
            goto L_0x00e1
        L_0x0213:
            r14 = move-exception
            r2 = r3
            goto L_0x00d9
        L_0x0217:
            r5 = move-exception
            goto L_0x00c2
        */
        throw new UnsupportedOperationException("Method not decompiled: it.sauronsoftware.ftp4j.FTPClient.listNames():java.lang.String[]");
    }

    public void upload(File file) throws IllegalStateException, FileNotFoundException, IOException, FTPIllegalReplyException, FTPException, FTPDataTransferException, FTPAbortedException {
        upload(file, 0, null);
    }

    public void upload(File file, FTPDataTransferListener listener) throws IllegalStateException, FileNotFoundException, IOException, FTPIllegalReplyException, FTPException, FTPDataTransferException, FTPAbortedException {
        upload(file, 0, listener);
    }

    public void upload(File file, long restartAt) throws IllegalStateException, FileNotFoundException, IOException, FTPIllegalReplyException, FTPException, FTPDataTransferException, FTPAbortedException {
        upload(file, restartAt, null);
    }

    public void upload(File file, long restartAt, FTPDataTransferListener listener) throws IllegalStateException, FileNotFoundException, IOException, FTPIllegalReplyException, FTPException, FTPDataTransferException, FTPAbortedException {
        if (!file.exists()) {
            throw new FileNotFoundException(file.getAbsolutePath());
        }
        try {
            InputStream inputStream = new FileInputStream(file);
            try {
                upload(file.getName(), inputStream, restartAt, restartAt, listener);
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Throwable th) {
                    }
                }
            } catch (IllegalStateException e) {
                throw e;
            } catch (IOException e2) {
                throw e2;
            } catch (FTPIllegalReplyException e3) {
                throw e3;
            } catch (FTPException e4) {
                throw e4;
            } catch (FTPDataTransferException e5) {
                throw e5;
            } catch (FTPAbortedException e6) {
                throw e6;
            } catch (Throwable th2) {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Throwable th3) {
                    }
                }
                throw th2;
            }
        } catch (IOException e7) {
            throw new FTPDataTransferException(e7);
        }
    }

    public void upload(String fileName, InputStream inputStream, long restartAt, long streamOffset, FTPDataTransferListener listener) throws IllegalStateException, IOException, FTPIllegalReplyException, FTPException, FTPDataTransferException, FTPAbortedException {
        boolean wasAborted;
        synchronized (this.lock) {
            if (!this.connected) {
                throw new IllegalStateException("Client not connected");
            } else if (!this.authenticated) {
                throw new IllegalStateException("Client not authenticated");
            } else {
                int tp = this.type;
                if (tp == 0) {
                    tp = detectType(fileName);
                }
                if (tp == 1) {
                    this.communication.sendFTPCommand("TYPE A");
                } else if (tp == 2) {
                    this.communication.sendFTPCommand("TYPE I");
                }
                FTPReply r = this.communication.readFTPReply();
                touchAutoNoopTimer();
                if (!r.isSuccessCode()) {
                    throw new FTPException(r);
                }
                FTPDataTransferConnectionProvider provider = openDataTransferChannel();
                if (this.restSupported || restartAt > 0) {
                    try {
                        this.communication.sendFTPCommand("REST " + restartAt);
                        FTPReply r2 = this.communication.readFTPReply();
                        touchAutoNoopTimer();
                        if (r2.getCode() != 350 && ((r2.getCode() != 501 && r2.getCode() != 502) || restartAt > 0)) {
                            throw new FTPException(r2);
                        } else if (1 == 0) {
                            provider.dispose();
                        }
                    } catch (Throwable th) {
                        if (0 == 0) {
                            provider.dispose();
                        }
                        throw th;
                    }
                }
                boolean wasAborted2 = false;
                this.communication.sendFTPCommand("STOR " + fileName);
                try {
                    Socket dtConnection = provider.openDataTransferConnection();
                    provider.dispose();
                    synchronized (this.abortLock) {
                        this.ongoingDataTransfer = true;
                        this.aborted = false;
                        this.consumeAborCommandReply = false;
                    }
                    try {
                        inputStream.skip(streamOffset);
                        this.dataTransferOutputStream = dtConnection.getOutputStream();
                        if (this.modezEnabled) {
                            this.dataTransferOutputStream = new DeflaterOutputStream(this.dataTransferOutputStream);
                        }
                        if (listener != null) {
                            listener.started();
                        }
                        if (tp == 1) {
                            Reader reader = new InputStreamReader(inputStream);
                            Writer writer = new OutputStreamWriter(this.dataTransferOutputStream, pickCharset());
                            char[] buffer = new char[65536];
                            while (true) {
                                int l = reader.read(buffer);
                                if (l == -1) {
                                    break;
                                }
                                writer.write(buffer, 0, l);
                                writer.flush();
                                if (listener != null) {
                                    listener.transferred(l);
                                }
                            }
                        } else if (tp == 2) {
                            byte[] buffer2 = new byte[65536];
                            while (true) {
                                int l2 = inputStream.read(buffer2);
                                if (l2 == -1) {
                                    break;
                                }
                                this.dataTransferOutputStream.write(buffer2, 0, l2);
                                this.dataTransferOutputStream.flush();
                                if (listener != null) {
                                    listener.transferred(l2);
                                }
                            }
                        }
                        if (this.dataTransferOutputStream != null) {
                            try {
                                this.dataTransferOutputStream.close();
                            } catch (Throwable th2) {
                            }
                        }
                        try {
                            dtConnection.close();
                        } catch (Throwable th3) {
                        }
                        this.dataTransferOutputStream = null;
                        synchronized (this.abortLock) {
                            wasAborted = this.aborted;
                            this.ongoingDataTransfer = false;
                            this.aborted = false;
                        }
                        FTPReply r3 = this.communication.readFTPReply();
                        touchAutoNoopTimer();
                        if (r3.getCode() == 150 || r3.getCode() == 125) {
                            FTPReply r4 = this.communication.readFTPReply();
                            if (wasAborted || r4.getCode() == 226) {
                                if (this.consumeAborCommandReply) {
                                    this.communication.readFTPReply();
                                    this.consumeAborCommandReply = false;
                                }
                                if (listener != null) {
                                    listener.completed();
                                }
                            } else {
                                throw new FTPException(r4);
                            }
                        } else {
                            throw new FTPException(r3);
                        }
                    } catch (IOException e) {
                        synchronized (this.abortLock) {
                            if (this.aborted) {
                                if (listener != null) {
                                    listener.aborted();
                                }
                                throw new FTPAbortedException();
                            }
                            if (listener != null) {
                                listener.failed();
                            }
                            throw new FTPDataTransferException("I/O error in data transfer", e);
                        }
                    } catch (Throwable th4) {
                        if (this.dataTransferOutputStream != null) {
                            this.dataTransferOutputStream.close();
                        }
                        dtConnection.close();
                        this.dataTransferOutputStream = null;
                        synchronized (this.abortLock) {
                            wasAborted2 = this.aborted;
                            this.ongoingDataTransfer = false;
                            this.aborted = false;
                            throw th4;
                        }
                    }
                } catch (Throwable th5) {
                }
            }
        }
    }

    public void append(File file) throws IllegalStateException, FileNotFoundException, IOException, FTPIllegalReplyException, FTPException, FTPDataTransferException, FTPAbortedException {
        append(file, null);
    }

    public void append(File file, FTPDataTransferListener listener) throws IllegalStateException, FileNotFoundException, IOException, FTPIllegalReplyException, FTPException, FTPDataTransferException, FTPAbortedException {
        if (!file.exists()) {
            throw new FileNotFoundException(file.getAbsolutePath());
        }
        try {
            InputStream inputStream = new FileInputStream(file);
            try {
                append(file.getName(), inputStream, 0, listener);
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Throwable th) {
                    }
                }
            } catch (IllegalStateException e) {
                throw e;
            } catch (IOException e2) {
                throw e2;
            } catch (FTPIllegalReplyException e3) {
                throw e3;
            } catch (FTPException e4) {
                throw e4;
            } catch (FTPDataTransferException e5) {
                throw e5;
            } catch (FTPAbortedException e6) {
                throw e6;
            } catch (Throwable th2) {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Throwable th3) {
                    }
                }
                throw th2;
            }
        } catch (IOException e7) {
            throw new FTPDataTransferException(e7);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:114:0x0154 A[SYNTHETIC, Splitter:B:114:0x0154] */
    /* JADX WARNING: Removed duplicated region for block: B:122:0x0162 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void append(java.lang.String r16, java.io.InputStream r17, long r18, it.sauronsoftware.ftp4j.FTPDataTransferListener r20) throws java.lang.IllegalStateException, java.io.IOException, it.sauronsoftware.ftp4j.FTPIllegalReplyException, it.sauronsoftware.ftp4j.FTPException, it.sauronsoftware.ftp4j.FTPDataTransferException, it.sauronsoftware.ftp4j.FTPAbortedException {
        /*
            r15 = this;
            java.lang.Object r12 = r15.lock
            monitor-enter(r12)
            boolean r11 = r15.connected     // Catch:{ all -> 0x0010 }
            if (r11 != 0) goto L_0x0013
            java.lang.IllegalStateException r11 = new java.lang.IllegalStateException     // Catch:{ all -> 0x0010 }
            java.lang.String r13 = "Client not connected"
            r11.<init>(r13)     // Catch:{ all -> 0x0010 }
            throw r11     // Catch:{ all -> 0x0010 }
        L_0x0010:
            r11 = move-exception
            monitor-exit(r12)     // Catch:{ all -> 0x0010 }
            throw r11
        L_0x0013:
            boolean r11 = r15.authenticated     // Catch:{ all -> 0x0010 }
            if (r11 != 0) goto L_0x0020
            java.lang.IllegalStateException r11 = new java.lang.IllegalStateException     // Catch:{ all -> 0x0010 }
            java.lang.String r13 = "Client not authenticated"
            r11.<init>(r13)     // Catch:{ all -> 0x0010 }
            throw r11     // Catch:{ all -> 0x0010 }
        L_0x0020:
            int r8 = r15.type     // Catch:{ all -> 0x0010 }
            if (r8 != 0) goto L_0x0028
            int r8 = r15.detectType(r16)     // Catch:{ all -> 0x0010 }
        L_0x0028:
            r11 = 1
            if (r8 != r11) goto L_0x0048
            it.sauronsoftware.ftp4j.FTPCommunicationChannel r11 = r15.communication     // Catch:{ all -> 0x0010 }
            java.lang.String r13 = "TYPE A"
            r11.sendFTPCommand(r13)     // Catch:{ all -> 0x0010 }
        L_0x0033:
            it.sauronsoftware.ftp4j.FTPCommunicationChannel r11 = r15.communication     // Catch:{ all -> 0x0010 }
            it.sauronsoftware.ftp4j.FTPReply r6 = r11.readFTPReply()     // Catch:{ all -> 0x0010 }
            r15.touchAutoNoopTimer()     // Catch:{ all -> 0x0010 }
            boolean r11 = r6.isSuccessCode()     // Catch:{ all -> 0x0010 }
            if (r11 != 0) goto L_0x0054
            it.sauronsoftware.ftp4j.FTPException r11 = new it.sauronsoftware.ftp4j.FTPException     // Catch:{ all -> 0x0010 }
            r11.<init>(r6)     // Catch:{ all -> 0x0010 }
            throw r11     // Catch:{ all -> 0x0010 }
        L_0x0048:
            r11 = 2
            if (r8 != r11) goto L_0x0033
            it.sauronsoftware.ftp4j.FTPCommunicationChannel r11 = r15.communication     // Catch:{ all -> 0x0010 }
            java.lang.String r13 = "TYPE I"
            r11.sendFTPCommand(r13)     // Catch:{ all -> 0x0010 }
            goto L_0x0033
        L_0x0054:
            r9 = 0
            it.sauronsoftware.ftp4j.FTPDataTransferConnectionProvider r5 = r15.openDataTransferChannel()     // Catch:{ all -> 0x0010 }
            it.sauronsoftware.ftp4j.FTPCommunicationChannel r11 = r15.communication     // Catch:{ all -> 0x0010 }
            java.lang.StringBuilder r13 = new java.lang.StringBuilder     // Catch:{ all -> 0x0010 }
            r13.<init>()     // Catch:{ all -> 0x0010 }
            java.lang.String r14 = "APPE "
            java.lang.StringBuilder r13 = r13.append(r14)     // Catch:{ all -> 0x0010 }
            r0 = r16
            java.lang.StringBuilder r13 = r13.append(r0)     // Catch:{ all -> 0x0010 }
            java.lang.String r13 = r13.toString()     // Catch:{ all -> 0x0010 }
            r11.sendFTPCommand(r13)     // Catch:{ all -> 0x0010 }
            java.net.Socket r2 = r5.openDataTransferConnection()     // Catch:{ all -> 0x0125 }
            r5.dispose()     // Catch:{ all -> 0x0105 }
            java.lang.Object r13 = r15.abortLock     // Catch:{ all -> 0x0105 }
            monitor-enter(r13)     // Catch:{ all -> 0x0105 }
            r11 = 1
            r15.ongoingDataTransfer = r11     // Catch:{ all -> 0x012a }
            r11 = 0
            r15.aborted = r11     // Catch:{ all -> 0x012a }
            r11 = 0
            r15.consumeAborCommandReply = r11     // Catch:{ all -> 0x012a }
            monitor-exit(r13)     // Catch:{ all -> 0x012a }
            r17.skip(r18)     // Catch:{ IOException -> 0x00d2 }
            java.io.OutputStream r11 = r2.getOutputStream()     // Catch:{ IOException -> 0x00d2 }
            r15.dataTransferOutputStream = r11     // Catch:{ IOException -> 0x00d2 }
            boolean r11 = r15.modezEnabled     // Catch:{ IOException -> 0x00d2 }
            if (r11 == 0) goto L_0x009e
            java.util.zip.DeflaterOutputStream r11 = new java.util.zip.DeflaterOutputStream     // Catch:{ IOException -> 0x00d2 }
            java.io.OutputStream r13 = r15.dataTransferOutputStream     // Catch:{ IOException -> 0x00d2 }
            r11.<init>(r13)     // Catch:{ IOException -> 0x00d2 }
            r15.dataTransferOutputStream = r11     // Catch:{ IOException -> 0x00d2 }
        L_0x009e:
            if (r20 == 0) goto L_0x00a3
            r20.started()     // Catch:{ IOException -> 0x00d2 }
        L_0x00a3:
            r11 = 1
            if (r8 != r11) goto L_0x012d
            java.io.InputStreamReader r7 = new java.io.InputStreamReader     // Catch:{ IOException -> 0x00d2 }
            r0 = r17
            r7.<init>(r0)     // Catch:{ IOException -> 0x00d2 }
            java.io.OutputStreamWriter r10 = new java.io.OutputStreamWriter     // Catch:{ IOException -> 0x00d2 }
            java.io.OutputStream r11 = r15.dataTransferOutputStream     // Catch:{ IOException -> 0x00d2 }
            java.lang.String r13 = r15.pickCharset()     // Catch:{ IOException -> 0x00d2 }
            r10.<init>(r11, r13)     // Catch:{ IOException -> 0x00d2 }
            r11 = 65536(0x10000, float:9.18355E-41)
            char[] r1 = new char[r11]     // Catch:{ IOException -> 0x00d2 }
        L_0x00bc:
            int r4 = r7.read(r1)     // Catch:{ IOException -> 0x00d2 }
            r11 = -1
            if (r4 == r11) goto L_0x0150
            r11 = 0
            r10.write(r1, r11, r4)     // Catch:{ IOException -> 0x00d2 }
            r10.flush()     // Catch:{ IOException -> 0x00d2 }
            if (r20 == 0) goto L_0x00bc
            r0 = r20
            r0.transferred(r4)     // Catch:{ IOException -> 0x00d2 }
            goto L_0x00bc
        L_0x00d2:
            r3 = move-exception
            java.lang.Object r13 = r15.abortLock     // Catch:{ all -> 0x00e8 }
            monitor-enter(r13)     // Catch:{ all -> 0x00e8 }
            boolean r11 = r15.aborted     // Catch:{ all -> 0x00e5 }
            if (r11 == 0) goto L_0x018d
            if (r20 == 0) goto L_0x00df
            r20.aborted()     // Catch:{ all -> 0x00e5 }
        L_0x00df:
            it.sauronsoftware.ftp4j.FTPAbortedException r11 = new it.sauronsoftware.ftp4j.FTPAbortedException     // Catch:{ all -> 0x00e5 }
            r11.<init>()     // Catch:{ all -> 0x00e5 }
            throw r11     // Catch:{ all -> 0x00e5 }
        L_0x00e5:
            r11 = move-exception
            monitor-exit(r13)     // Catch:{ all -> 0x00e5 }
            throw r11     // Catch:{ all -> 0x00e8 }
        L_0x00e8:
            r11 = move-exception
            java.io.OutputStream r13 = r15.dataTransferOutputStream     // Catch:{ all -> 0x0105 }
            if (r13 == 0) goto L_0x00f2
            java.io.OutputStream r13 = r15.dataTransferOutputStream     // Catch:{ Throwable -> 0x01f0 }
            r13.close()     // Catch:{ Throwable -> 0x01f0 }
        L_0x00f2:
            r2.close()     // Catch:{ Throwable -> 0x01ed }
        L_0x00f5:
            r13 = 0
            r15.dataTransferOutputStream = r13     // Catch:{ all -> 0x0105 }
            java.lang.Object r13 = r15.abortLock     // Catch:{ all -> 0x0105 }
            monitor-enter(r13)     // Catch:{ all -> 0x0105 }
            boolean r9 = r15.aborted     // Catch:{ all -> 0x019b }
            r14 = 0
            r15.ongoingDataTransfer = r14     // Catch:{ all -> 0x019b }
            r14 = 0
            r15.aborted = r14     // Catch:{ all -> 0x019b }
            monitor-exit(r13)     // Catch:{ all -> 0x019b }
            throw r11     // Catch:{ all -> 0x0105 }
        L_0x0105:
            r11 = move-exception
            it.sauronsoftware.ftp4j.FTPCommunicationChannel r13 = r15.communication     // Catch:{ all -> 0x0010 }
            it.sauronsoftware.ftp4j.FTPReply r6 = r13.readFTPReply()     // Catch:{ all -> 0x0010 }
            r15.touchAutoNoopTimer()     // Catch:{ all -> 0x0010 }
            int r13 = r6.getCode()     // Catch:{ all -> 0x0010 }
            r14 = 150(0x96, float:2.1E-43)
            if (r13 == r14) goto L_0x01c7
            int r13 = r6.getCode()     // Catch:{ all -> 0x0010 }
            r14 = 125(0x7d, float:1.75E-43)
            if (r13 == r14) goto L_0x01c7
            it.sauronsoftware.ftp4j.FTPException r11 = new it.sauronsoftware.ftp4j.FTPException     // Catch:{ all -> 0x0010 }
            r11.<init>(r6)     // Catch:{ all -> 0x0010 }
            throw r11     // Catch:{ all -> 0x0010 }
        L_0x0125:
            r11 = move-exception
            r5.dispose()     // Catch:{ all -> 0x0105 }
            throw r11     // Catch:{ all -> 0x0105 }
        L_0x012a:
            r11 = move-exception
            monitor-exit(r13)     // Catch:{ all -> 0x012a }
            throw r11     // Catch:{ all -> 0x0105 }
        L_0x012d:
            r11 = 2
            if (r8 != r11) goto L_0x0150
            r11 = 65536(0x10000, float:9.18355E-41)
            byte[] r1 = new byte[r11]     // Catch:{ IOException -> 0x00d2 }
        L_0x0134:
            r0 = r17
            int r4 = r0.read(r1)     // Catch:{ IOException -> 0x00d2 }
            r11 = -1
            if (r4 == r11) goto L_0x0150
            java.io.OutputStream r11 = r15.dataTransferOutputStream     // Catch:{ IOException -> 0x00d2 }
            r13 = 0
            r11.write(r1, r13, r4)     // Catch:{ IOException -> 0x00d2 }
            java.io.OutputStream r11 = r15.dataTransferOutputStream     // Catch:{ IOException -> 0x00d2 }
            r11.flush()     // Catch:{ IOException -> 0x00d2 }
            if (r20 == 0) goto L_0x0134
            r0 = r20
            r0.transferred(r4)     // Catch:{ IOException -> 0x00d2 }
            goto L_0x0134
        L_0x0150:
            java.io.OutputStream r11 = r15.dataTransferOutputStream     // Catch:{ all -> 0x0105 }
            if (r11 == 0) goto L_0x0159
            java.io.OutputStream r11 = r15.dataTransferOutputStream     // Catch:{ Throwable -> 0x01f3 }
            r11.close()     // Catch:{ Throwable -> 0x01f3 }
        L_0x0159:
            r2.close()     // Catch:{ Throwable -> 0x01ea }
        L_0x015c:
            r11 = 0
            r15.dataTransferOutputStream = r11     // Catch:{ all -> 0x0105 }
            java.lang.Object r13 = r15.abortLock     // Catch:{ all -> 0x0105 }
            monitor-enter(r13)     // Catch:{ all -> 0x0105 }
            boolean r9 = r15.aborted     // Catch:{ all -> 0x018a }
            r11 = 0
            r15.ongoingDataTransfer = r11     // Catch:{ all -> 0x018a }
            r11 = 0
            r15.aborted = r11     // Catch:{ all -> 0x018a }
            monitor-exit(r13)     // Catch:{ all -> 0x018a }
            it.sauronsoftware.ftp4j.FTPCommunicationChannel r11 = r15.communication     // Catch:{ all -> 0x0010 }
            it.sauronsoftware.ftp4j.FTPReply r6 = r11.readFTPReply()     // Catch:{ all -> 0x0010 }
            r15.touchAutoNoopTimer()     // Catch:{ all -> 0x0010 }
            int r11 = r6.getCode()     // Catch:{ all -> 0x0010 }
            r13 = 150(0x96, float:2.1E-43)
            if (r11 == r13) goto L_0x019e
            int r11 = r6.getCode()     // Catch:{ all -> 0x0010 }
            r13 = 125(0x7d, float:1.75E-43)
            if (r11 == r13) goto L_0x019e
            it.sauronsoftware.ftp4j.FTPException r11 = new it.sauronsoftware.ftp4j.FTPException     // Catch:{ all -> 0x0010 }
            r11.<init>(r6)     // Catch:{ all -> 0x0010 }
            throw r11     // Catch:{ all -> 0x0010 }
        L_0x018a:
            r11 = move-exception
            monitor-exit(r13)     // Catch:{ all -> 0x018a }
            throw r11     // Catch:{ all -> 0x0105 }
        L_0x018d:
            if (r20 == 0) goto L_0x0192
            r20.failed()     // Catch:{ all -> 0x00e5 }
        L_0x0192:
            it.sauronsoftware.ftp4j.FTPDataTransferException r11 = new it.sauronsoftware.ftp4j.FTPDataTransferException     // Catch:{ all -> 0x00e5 }
            java.lang.String r14 = "I/O error in data transfer"
            r11.<init>(r14, r3)     // Catch:{ all -> 0x00e5 }
            throw r11     // Catch:{ all -> 0x00e5 }
        L_0x019b:
            r11 = move-exception
            monitor-exit(r13)     // Catch:{ all -> 0x019b }
            throw r11     // Catch:{ all -> 0x0105 }
        L_0x019e:
            it.sauronsoftware.ftp4j.FTPCommunicationChannel r11 = r15.communication     // Catch:{ all -> 0x0010 }
            it.sauronsoftware.ftp4j.FTPReply r6 = r11.readFTPReply()     // Catch:{ all -> 0x0010 }
            if (r9 != 0) goto L_0x01b4
            int r11 = r6.getCode()     // Catch:{ all -> 0x0010 }
            r13 = 226(0xe2, float:3.17E-43)
            if (r11 == r13) goto L_0x01b4
            it.sauronsoftware.ftp4j.FTPException r11 = new it.sauronsoftware.ftp4j.FTPException     // Catch:{ all -> 0x0010 }
            r11.<init>(r6)     // Catch:{ all -> 0x0010 }
            throw r11     // Catch:{ all -> 0x0010 }
        L_0x01b4:
            boolean r11 = r15.consumeAborCommandReply     // Catch:{ all -> 0x0010 }
            if (r11 == 0) goto L_0x01c0
            it.sauronsoftware.ftp4j.FTPCommunicationChannel r11 = r15.communication     // Catch:{ all -> 0x0010 }
            r11.readFTPReply()     // Catch:{ all -> 0x0010 }
            r11 = 0
            r15.consumeAborCommandReply = r11     // Catch:{ all -> 0x0010 }
        L_0x01c0:
            if (r20 == 0) goto L_0x01c5
            r20.completed()     // Catch:{ all -> 0x0010 }
        L_0x01c5:
            monitor-exit(r12)     // Catch:{ all -> 0x0010 }
            return
        L_0x01c7:
            it.sauronsoftware.ftp4j.FTPCommunicationChannel r13 = r15.communication     // Catch:{ all -> 0x0010 }
            it.sauronsoftware.ftp4j.FTPReply r6 = r13.readFTPReply()     // Catch:{ all -> 0x0010 }
            if (r9 != 0) goto L_0x01dd
            int r13 = r6.getCode()     // Catch:{ all -> 0x0010 }
            r14 = 226(0xe2, float:3.17E-43)
            if (r13 == r14) goto L_0x01dd
            it.sauronsoftware.ftp4j.FTPException r11 = new it.sauronsoftware.ftp4j.FTPException     // Catch:{ all -> 0x0010 }
            r11.<init>(r6)     // Catch:{ all -> 0x0010 }
            throw r11     // Catch:{ all -> 0x0010 }
        L_0x01dd:
            boolean r13 = r15.consumeAborCommandReply     // Catch:{ all -> 0x0010 }
            if (r13 == 0) goto L_0x01e9
            it.sauronsoftware.ftp4j.FTPCommunicationChannel r13 = r15.communication     // Catch:{ all -> 0x0010 }
            r13.readFTPReply()     // Catch:{ all -> 0x0010 }
            r13 = 0
            r15.consumeAborCommandReply = r13     // Catch:{ all -> 0x0010 }
        L_0x01e9:
            throw r11     // Catch:{ all -> 0x0010 }
        L_0x01ea:
            r11 = move-exception
            goto L_0x015c
        L_0x01ed:
            r13 = move-exception
            goto L_0x00f5
        L_0x01f0:
            r13 = move-exception
            goto L_0x00f2
        L_0x01f3:
            r11 = move-exception
            goto L_0x0159
        */
        throw new UnsupportedOperationException("Method not decompiled: it.sauronsoftware.ftp4j.FTPClient.append(java.lang.String, java.io.InputStream, long, it.sauronsoftware.ftp4j.FTPDataTransferListener):void");
    }

    public void download(String remoteFileName, File localFile) throws IllegalStateException, FileNotFoundException, IOException, FTPIllegalReplyException, FTPException, FTPDataTransferException, FTPAbortedException {
        download(remoteFileName, localFile, 0, (FTPDataTransferListener) null);
    }

    public void download(String remoteFileName, File localFile, FTPDataTransferListener listener) throws IllegalStateException, FileNotFoundException, IOException, FTPIllegalReplyException, FTPException, FTPDataTransferException, FTPAbortedException {
        download(remoteFileName, localFile, 0, listener);
    }

    public void download(String remoteFileName, File localFile, long restartAt) throws IllegalStateException, FileNotFoundException, IOException, FTPIllegalReplyException, FTPException, FTPDataTransferException, FTPAbortedException {
        download(remoteFileName, localFile, restartAt, (FTPDataTransferListener) null);
    }

    public void download(String remoteFileName, File localFile, long restartAt, FTPDataTransferListener listener) throws IllegalStateException, FileNotFoundException, IOException, FTPIllegalReplyException, FTPException, FTPDataTransferException, FTPAbortedException {
        try {
            OutputStream outputStream = new FileOutputStream(localFile, restartAt > 0);
            try {
                download(remoteFileName, outputStream, restartAt, listener);
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (Throwable th) {
                    }
                }
            } catch (IllegalStateException e) {
                throw e;
            } catch (IOException e2) {
                throw e2;
            } catch (FTPIllegalReplyException e3) {
                throw e3;
            } catch (FTPException e4) {
                throw e4;
            } catch (FTPDataTransferException e5) {
                throw e5;
            } catch (FTPAbortedException e6) {
                throw e6;
            } catch (Throwable th2) {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (Throwable th3) {
                    }
                }
                throw th2;
            }
        } catch (IOException e7) {
            throw new FTPDataTransferException(e7);
        }
    }

    public void download(String fileName, OutputStream outputStream, long restartAt, FTPDataTransferListener listener) throws IllegalStateException, IOException, FTPIllegalReplyException, FTPException, FTPDataTransferException, FTPAbortedException {
        boolean wasAborted;
        synchronized (this.lock) {
            if (!this.connected) {
                throw new IllegalStateException("Client not connected");
            } else if (!this.authenticated) {
                throw new IllegalStateException("Client not authenticated");
            } else {
                int tp = this.type;
                if (tp == 0) {
                    tp = detectType(fileName);
                }
                if (tp == 1) {
                    this.communication.sendFTPCommand("TYPE A");
                } else if (tp == 2) {
                    this.communication.sendFTPCommand("TYPE I");
                }
                FTPReply r = this.communication.readFTPReply();
                touchAutoNoopTimer();
                if (!r.isSuccessCode()) {
                    throw new FTPException(r);
                }
                FTPDataTransferConnectionProvider provider = openDataTransferChannel();
                if (this.restSupported || restartAt > 0) {
                    try {
                        this.communication.sendFTPCommand("REST " + restartAt);
                        FTPReply r2 = this.communication.readFTPReply();
                        touchAutoNoopTimer();
                        if (r2.getCode() != 350 && ((r2.getCode() != 501 && r2.getCode() != 502) || restartAt > 0)) {
                            throw new FTPException(r2);
                        } else if (1 == 0) {
                            provider.dispose();
                        }
                    } catch (Throwable th) {
                        if (0 == 0) {
                            provider.dispose();
                        }
                        throw th;
                    }
                }
                boolean wasAborted2 = false;
                this.communication.sendFTPCommand("RETR " + fileName);
                try {
                    Socket dtConnection = provider.openDataTransferConnection();
                    provider.dispose();
                    synchronized (this.abortLock) {
                        this.ongoingDataTransfer = true;
                        this.aborted = false;
                        this.consumeAborCommandReply = false;
                    }
                    try {
                        this.dataTransferInputStream = dtConnection.getInputStream();
                        if (this.modezEnabled) {
                            this.dataTransferInputStream = new InflaterInputStream(this.dataTransferInputStream);
                        }
                        if (listener != null) {
                            listener.started();
                        }
                        if (tp == 1) {
                            Reader reader = new InputStreamReader(this.dataTransferInputStream, pickCharset());
                            Writer writer = new OutputStreamWriter(outputStream);
                            char[] buffer = new char[65536];
                            while (true) {
                                int l = reader.read(buffer, 0, buffer.length);
                                if (l == -1) {
                                    break;
                                }
                                writer.write(buffer, 0, l);
                                writer.flush();
                                if (listener != null) {
                                    listener.transferred(l);
                                }
                            }
                        } else if (tp == 2) {
                            byte[] buffer2 = new byte[65536];
                            while (true) {
                                int l2 = this.dataTransferInputStream.read(buffer2, 0, buffer2.length);
                                if (l2 == -1) {
                                    break;
                                }
                                outputStream.write(buffer2, 0, l2);
                                if (listener != null) {
                                    listener.transferred(l2);
                                }
                            }
                        }
                        if (this.dataTransferInputStream != null) {
                            try {
                                this.dataTransferInputStream.close();
                            } catch (Throwable th2) {
                            }
                        }
                        try {
                            dtConnection.close();
                        } catch (Throwable th3) {
                        }
                        this.dataTransferInputStream = null;
                        synchronized (this.abortLock) {
                            wasAborted = this.aborted;
                            this.ongoingDataTransfer = false;
                            this.aborted = false;
                        }
                        FTPReply r3 = this.communication.readFTPReply();
                        touchAutoNoopTimer();
                        if (r3.getCode() == 150 || r3.getCode() == 125) {
                            FTPReply r4 = this.communication.readFTPReply();
                            if (wasAborted || r4.getCode() == 226) {
                                if (this.consumeAborCommandReply) {
                                    this.communication.readFTPReply();
                                    this.consumeAborCommandReply = false;
                                }
                                if (listener != null) {
                                    listener.completed();
                                }
                            } else {
                                throw new FTPException(r4);
                            }
                        } else {
                            throw new FTPException(r3);
                        }
                    } catch (IOException e) {
                        synchronized (this.abortLock) {
                            if (this.aborted) {
                                if (listener != null) {
                                    listener.aborted();
                                }
                                throw new FTPAbortedException();
                            }
                            if (listener != null) {
                                listener.failed();
                            }
                            throw new FTPDataTransferException("I/O error in data transfer", e);
                        }
                    } catch (Throwable th4) {
                        if (this.dataTransferInputStream != null) {
                            this.dataTransferInputStream.close();
                        }
                        dtConnection.close();
                        this.dataTransferInputStream = null;
                        synchronized (this.abortLock) {
                            wasAborted2 = this.aborted;
                            this.ongoingDataTransfer = false;
                            this.aborted = false;
                            throw th4;
                        }
                    }
                } catch (Throwable th5) {
                }
            }
        }
    }

    private int detectType(String fileName) throws IOException, FTPIllegalReplyException, FTPException {
        int start = fileName.lastIndexOf(46) + 1;
        int stop = fileName.length();
        if (start <= 0 || start >= stop - 1) {
            return 2;
        }
        if (this.textualExtensionRecognizer.isTextualExt(fileName.substring(start, stop).toLowerCase())) {
            return 1;
        }
        return 2;
    }

    private FTPDataTransferConnectionProvider openDataTransferChannel() throws IOException, FTPIllegalReplyException, FTPException, FTPDataTransferException {
        if (!this.modezSupported || !this.compressionEnabled) {
            if (this.modezEnabled) {
                this.communication.sendFTPCommand("MODE S");
                FTPReply r = this.communication.readFTPReply();
                touchAutoNoopTimer();
                if (r.isSuccessCode()) {
                    this.modezEnabled = false;
                }
            }
        } else if (!this.modezEnabled) {
            this.communication.sendFTPCommand("MODE Z");
            FTPReply r2 = this.communication.readFTPReply();
            touchAutoNoopTimer();
            if (r2.isSuccessCode()) {
                this.modezEnabled = true;
            }
        }
        if (this.passive) {
            return openPassiveDataTransferChannel();
        }
        return openActiveDataTransferChannel();
    }

    private FTPDataTransferConnectionProvider openActiveDataTransferChannel() throws IOException, FTPIllegalReplyException, FTPException, FTPDataTransferException {
        FTPDataTransferServer server = new FTPDataTransferServer() {
            /* class it.sauronsoftware.ftp4j.FTPClient.AnonymousClass1 */

            public Socket openDataTransferConnection() throws FTPDataTransferException {
                Socket socket = super.openDataTransferConnection();
                if (!FTPClient.this.dataChannelEncrypted) {
                    return socket;
                }
                try {
                    return FTPClient.this.ssl(socket, socket.getInetAddress().getHostName(), socket.getPort());
                } catch (IOException e) {
                    socket.close();
                } catch (Throwable th) {
                }
                throw new FTPDataTransferException(e);
            }
        };
        int port2 = server.getPort();
        int[] addr = pickLocalAddress();
        this.communication.sendFTPCommand("PORT " + addr[0] + "," + addr[1] + "," + addr[2] + "," + addr[3] + "," + (port2 >>> 8) + "," + (port2 & 255));
        FTPReply r = this.communication.readFTPReply();
        touchAutoNoopTimer();
        if (r.isSuccessCode()) {
            return server;
        }
        server.dispose();
        try {
            server.openDataTransferConnection().close();
        } catch (Throwable th) {
        }
        throw new FTPException(r);
    }

    private FTPDataTransferConnectionProvider openPassiveDataTransferChannel() throws IOException, FTPIllegalReplyException, FTPException, FTPDataTransferException {
        this.communication.sendFTPCommand("PASV");
        FTPReply r = this.communication.readFTPReply();
        touchAutoNoopTimer();
        if (!r.isSuccessCode()) {
            throw new FTPException(r);
        }
        String addressAndPort = null;
        String[] messages = r.getMessages();
        int i = 0;
        while (true) {
            if (i >= messages.length) {
                break;
            }
            Matcher m = PASV_PATTERN.matcher(messages[i]);
            if (m.find()) {
                addressAndPort = messages[i].substring(m.start(), m.end());
                break;
            }
            i++;
        }
        if (addressAndPort == null) {
            throw new FTPIllegalReplyException();
        }
        StringTokenizer stringTokenizer = new StringTokenizer(addressAndPort, ",");
        int b1 = Integer.parseInt(stringTokenizer.nextToken());
        int b2 = Integer.parseInt(stringTokenizer.nextToken());
        int b3 = Integer.parseInt(stringTokenizer.nextToken());
        int b4 = Integer.parseInt(stringTokenizer.nextToken());
        int p1 = Integer.parseInt(stringTokenizer.nextToken());
        final String pasvHost = b1 + "." + b2 + "." + b3 + "." + b4;
        final int pasvPort = (p1 << 8) | Integer.parseInt(stringTokenizer.nextToken());
        return new FTPDataTransferConnectionProvider() {
            /* class it.sauronsoftware.ftp4j.FTPClient.AnonymousClass2 */

            public Socket openDataTransferConnection() throws FTPDataTransferException {
                try {
                    String selectedHost = FTPClient.this.connector.getUseSuggestedAddressForDataConnections() ? pasvHost : FTPClient.this.host;
                    Socket dtConnection = FTPClient.this.connector.connectForDataTransferChannel(selectedHost, pasvPort);
                    if (FTPClient.this.dataChannelEncrypted) {
                        return FTPClient.this.ssl(dtConnection, selectedHost, pasvPort);
                    }
                    return dtConnection;
                } catch (IOException e) {
                    throw new FTPDataTransferException("Cannot connect to the remote server", e);
                }
            }

            public void dispose() {
            }
        };
    }

    public void abortCurrentDataTransfer(boolean sendAborCommand) throws IOException, FTPIllegalReplyException {
        synchronized (this.abortLock) {
            if (this.ongoingDataTransfer && !this.aborted) {
                if (sendAborCommand) {
                    this.communication.sendFTPCommand("ABOR");
                    touchAutoNoopTimer();
                    this.consumeAborCommandReply = true;
                }
                if (this.dataTransferInputStream != null) {
                    try {
                        this.dataTransferInputStream.close();
                    } catch (Throwable th) {
                    }
                }
                if (this.dataTransferOutputStream != null) {
                    try {
                        this.dataTransferOutputStream.close();
                    } catch (Throwable th2) {
                    }
                }
                this.aborted = true;
            }
        }
    }

    private String pickCharset() {
        if (this.charset != null) {
            return this.charset;
        }
        if (this.utf8Supported) {
            return "UTF-8";
        }
        return System.getProperty("file.encoding");
    }

    private int[] pickLocalAddress() throws IOException {
        int[] ret = pickForcedLocalAddress();
        if (ret == null) {
            return pickAutoDetectedLocalAddress();
        }
        return ret;
    }

    private int[] pickForcedLocalAddress() {
        int[] ret = null;
        String aux = System.getProperty(FTPKeys.ACTIVE_DT_HOST_ADDRESS);
        if (aux != null) {
            boolean valid = false;
            StringTokenizer st = new StringTokenizer(aux, ".");
            if (st.countTokens() == 4) {
                valid = true;
                int[] arr = new int[4];
                int i = 0;
                while (true) {
                    if (i >= 4) {
                        break;
                    }
                    try {
                        arr[i] = Integer.parseInt(st.nextToken());
                    } catch (NumberFormatException e) {
                        arr[i] = -1;
                    }
                    if (arr[i] < 0 || arr[i] > 255) {
                        valid = false;
                    } else {
                        i++;
                    }
                }
                if (valid) {
                    ret = arr;
                }
            }
            if (!valid) {
                System.err.println("WARNING: invalid value \"" + aux + "\" for the " + FTPKeys.ACTIVE_DT_HOST_ADDRESS + " system property. The value should be in the x.x.x.x form.");
            }
        }
        return ret;
    }

    private int[] pickAutoDetectedLocalAddress() throws IOException {
        byte[] addr = InetAddress.getLocalHost().getAddress();
        return new int[]{addr[0] & 255, addr[1] & 255, addr[2] & 255, addr[3] & 255};
    }

    public String toString() {
        String stringBuffer;
        synchronized (this.lock) {
            StringBuffer buffer = new StringBuffer();
            buffer.append(getClass().getName());
            buffer.append(" [connected=");
            buffer.append(this.connected);
            if (this.connected) {
                buffer.append(", host=");
                buffer.append(this.host);
                buffer.append(", port=");
                buffer.append(this.port);
            }
            buffer.append(", connector=");
            buffer.append(this.connector);
            buffer.append(", security=");
            switch (this.security) {
                case 0:
                    buffer.append("SECURITY_FTP");
                    break;
                case 1:
                    buffer.append("SECURITY_FTPS");
                    break;
                case 2:
                    buffer.append("SECURITY_FTPES");
                    break;
            }
            buffer.append(", authenticated=");
            buffer.append(this.authenticated);
            if (this.authenticated) {
                buffer.append(", username=");
                buffer.append(this.username);
                buffer.append(", password=");
                StringBuffer buffer2 = new StringBuffer();
                for (int i = 0; i < this.password.length(); i++) {
                    buffer2.append('*');
                }
                buffer.append(buffer2);
                buffer.append(", restSupported=");
                buffer.append(this.restSupported);
                buffer.append(", utf8supported=");
                buffer.append(this.utf8Supported);
                buffer.append(", mlsdSupported=");
                buffer.append(this.mlsdSupported);
                buffer.append(", mode=modezSupported");
                buffer.append(this.modezSupported);
                buffer.append(", mode=modezEnabled");
                buffer.append(this.modezEnabled);
            }
            buffer.append(", transfer mode=");
            buffer.append(this.passive ? "passive" : "active");
            buffer.append(", transfer type=");
            switch (this.type) {
                case 0:
                    buffer.append("TYPE_AUTO");
                    break;
                case 1:
                    buffer.append("TYPE_TEXTUAL");
                    break;
                case 2:
                    buffer.append("TYPE_BINARY");
                    break;
            }
            buffer.append(", textualExtensionRecognizer=");
            buffer.append(this.textualExtensionRecognizer);
            FTPListParser[] listParsers2 = getListParsers();
            if (listParsers2.length > 0) {
                buffer.append(", listParsers=");
                for (int i2 = 0; i2 < listParsers2.length; i2++) {
                    if (i2 > 0) {
                        buffer.append(", ");
                    }
                    buffer.append(listParsers2[i2]);
                }
            }
            FTPCommunicationListener[] communicationListeners2 = getCommunicationListeners();
            if (communicationListeners2.length > 0) {
                buffer.append(", communicationListeners=");
                for (int i3 = 0; i3 < communicationListeners2.length; i3++) {
                    if (i3 > 0) {
                        buffer.append(", ");
                    }
                    buffer.append(communicationListeners2[i3]);
                }
            }
            buffer.append(", autoNoopTimeout=");
            buffer.append(this.autoNoopTimeout);
            buffer.append(IMemberProtocol.STRING_SEPERATOR_RIGHT);
            stringBuffer = buffer.toString();
        }
        return stringBuffer;
    }

    private void startAutoNoopTimer() {
        if (this.autoNoopTimeout > 0) {
            this.autoNoopTimer = new AutoNoopTimer();
            this.autoNoopTimer.start();
        }
    }

    private void stopAutoNoopTimer() {
        if (this.autoNoopTimer != null) {
            this.autoNoopTimer.interrupt();
            this.autoNoopTimer = null;
        }
    }

    private void touchAutoNoopTimer() {
        if (this.autoNoopTimer != null) {
            this.nextAutoNoopTime = System.currentTimeMillis() + this.autoNoopTimeout;
        }
    }

    private class AutoNoopTimer extends Thread {
        private AutoNoopTimer() {
        }

        public void run() {
            synchronized (FTPClient.this.lock) {
                if (FTPClient.this.nextAutoNoopTime <= 0 && FTPClient.this.autoNoopTimeout > 0) {
                    long unused = FTPClient.this.nextAutoNoopTime = System.currentTimeMillis() + FTPClient.this.autoNoopTimeout;
                }
                while (!Thread.interrupted() && FTPClient.this.autoNoopTimeout > 0) {
                    long delay = FTPClient.this.nextAutoNoopTime - System.currentTimeMillis();
                    if (delay > 0) {
                        try {
                            FTPClient.this.lock.wait(delay);
                        } catch (InterruptedException e) {
                        }
                    }
                    if (System.currentTimeMillis() >= FTPClient.this.nextAutoNoopTime) {
                        try {
                            FTPClient.this.noop();
                        } catch (Throwable th) {
                        }
                    }
                }
            }
        }
    }

    public void setSocketProvider(SocketProvider provider) {
        this.socketProvider = provider;
    }
}
