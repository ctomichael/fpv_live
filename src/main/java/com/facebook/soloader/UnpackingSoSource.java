package com.facebook.soloader;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;
import dji.component.accountcenter.IMemberProtocol;
import java.io.Closeable;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;

public abstract class UnpackingSoSource extends DirectorySoSource {
    private static final String DEPS_FILE_NAME = "dso_deps";
    private static final String LOCK_FILE_NAME = "dso_lock";
    private static final String MANIFEST_FILE_NAME = "dso_manifest";
    private static final byte MANIFEST_VERSION = 1;
    private static final byte STATE_CLEAN = 1;
    private static final byte STATE_DIRTY = 0;
    private static final String STATE_FILE_NAME = "dso_state";
    private static final String TAG = "fb-UnpackingSoSource";
    @Nullable
    private String[] mAbis;
    protected final Context mContext;
    @Nullable
    protected String mCorruptedLib;
    private final Map<String, Object> mLibsBeingLoaded = new HashMap();

    /* access modifiers changed from: protected */
    public abstract Unpacker makeUnpacker() throws IOException;

    protected UnpackingSoSource(Context context, String name) {
        super(getSoStorePath(context, name), 1);
        this.mContext = context;
    }

    protected UnpackingSoSource(Context context, File storePath) {
        super(storePath, 1);
        this.mContext = context;
    }

    public static File getSoStorePath(Context context, String name) {
        return new File(context.getApplicationInfo().dataDir + IMemberProtocol.PARAM_SEPERATOR + name);
    }

    public String[] getSoSourceAbis() {
        if (this.mAbis == null) {
            return super.getSoSourceAbis();
        }
        return this.mAbis;
    }

    public void setSoSourceAbis(String[] abis) {
        this.mAbis = abis;
    }

    public static class Dso {
        public final String hash;
        public final String name;

        public Dso(String name2, String hash2) {
            this.name = name2;
            this.hash = hash2;
        }
    }

    public static final class DsoManifest {
        public final Dso[] dsos;

        public DsoManifest(Dso[] dsos2) {
            this.dsos = dsos2;
        }

        static final DsoManifest read(DataInput xdi) throws IOException {
            if (xdi.readByte() != 1) {
                throw new RuntimeException("wrong dso manifest version");
            }
            int nrDso = xdi.readInt();
            if (nrDso < 0) {
                throw new RuntimeException("illegal number of shared libraries");
            }
            Dso[] dsos2 = new Dso[nrDso];
            for (int i = 0; i < nrDso; i++) {
                dsos2[i] = new Dso(xdi.readUTF(), xdi.readUTF());
            }
            return new DsoManifest(dsos2);
        }

        public final void write(DataOutput xdo) throws IOException {
            xdo.writeByte(1);
            xdo.writeInt(this.dsos.length);
            for (int i = 0; i < this.dsos.length; i++) {
                xdo.writeUTF(this.dsos[i].name);
                xdo.writeUTF(this.dsos[i].hash);
            }
        }
    }

    protected static final class InputDso implements Closeable {
        public final InputStream content;
        public final Dso dso;

        public InputDso(Dso dso2, InputStream content2) {
            this.dso = dso2;
            this.content = content2;
        }

        public void close() throws IOException {
            this.content.close();
        }
    }

    protected static abstract class InputDsoIterator implements Closeable {
        public abstract boolean hasNext();

        public abstract InputDso next() throws IOException;

        protected InputDsoIterator() {
        }

        public void close() throws IOException {
        }
    }

    protected static abstract class Unpacker implements Closeable {
        /* access modifiers changed from: protected */
        public abstract DsoManifest getDsoManifest() throws IOException;

        /* access modifiers changed from: protected */
        public abstract InputDsoIterator openDsoIterator() throws IOException;

        protected Unpacker() {
        }

        public void close() throws IOException {
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0032, code lost:
        r2 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0033, code lost:
        r3 = r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0045, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0046, code lost:
        r2 = r1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void writeState(java.io.File r6, byte r7) throws java.io.IOException {
        /*
            java.io.RandomAccessFile r0 = new java.io.RandomAccessFile
            java.lang.String r1 = "rw"
            r0.<init>(r6, r1)
            r3 = 0
            r4 = 0
            r0.seek(r4)     // Catch:{ Throwable -> 0x0030, all -> 0x0045 }
            r0.write(r7)     // Catch:{ Throwable -> 0x0030, all -> 0x0045 }
            long r4 = r0.getFilePointer()     // Catch:{ Throwable -> 0x0030, all -> 0x0045 }
            r0.setLength(r4)     // Catch:{ Throwable -> 0x0030, all -> 0x0045 }
            java.io.FileDescriptor r1 = r0.getFD()     // Catch:{ Throwable -> 0x0030, all -> 0x0045 }
            r1.sync()     // Catch:{ Throwable -> 0x0030, all -> 0x0045 }
            if (r0 == 0) goto L_0x0026
            if (r3 == 0) goto L_0x002c
            r0.close()     // Catch:{ Throwable -> 0x0027 }
        L_0x0026:
            return
        L_0x0027:
            r1 = move-exception
            r3.addSuppressed(r1)
            goto L_0x0026
        L_0x002c:
            r0.close()
            goto L_0x0026
        L_0x0030:
            r1 = move-exception
            throw r1     // Catch:{ all -> 0x0032 }
        L_0x0032:
            r2 = move-exception
            r3 = r1
        L_0x0034:
            if (r0 == 0) goto L_0x003b
            if (r3 == 0) goto L_0x0041
            r0.close()     // Catch:{ Throwable -> 0x003c }
        L_0x003b:
            throw r2
        L_0x003c:
            r1 = move-exception
            r3.addSuppressed(r1)
            goto L_0x003b
        L_0x0041:
            r0.close()
            goto L_0x003b
        L_0x0045:
            r1 = move-exception
            r2 = r1
            goto L_0x0034
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.soloader.UnpackingSoSource.writeState(java.io.File, byte):void");
    }

    private void deleteUnmentionedFiles(Dso[] dsos) throws IOException {
        String[] existingFiles = this.soDirectory.list();
        if (existingFiles == null) {
            throw new IOException("unable to list directory " + this.soDirectory);
        }
        for (String fileName : existingFiles) {
            if (!fileName.equals(STATE_FILE_NAME) && !fileName.equals(LOCK_FILE_NAME) && !fileName.equals(DEPS_FILE_NAME) && !fileName.equals(MANIFEST_FILE_NAME)) {
                boolean found = false;
                int j = 0;
                while (!found && j < dsos.length) {
                    if (dsos[j].name.equals(fileName)) {
                        found = true;
                    }
                    j++;
                }
                if (!found) {
                    File fileNameToDelete = new File(this.soDirectory, fileName);
                    Log.v(TAG, "deleting unaccounted-for file " + fileNameToDelete);
                    SysUtil.dumbDeleteRecursive(fileNameToDelete);
                }
            }
        }
    }

    private void extractDso(InputDso iDso, byte[] ioBuffer) throws IOException {
        RandomAccessFile dsoFile;
        Log.i(TAG, "extracting DSO " + iDso.dso.name);
        if (!this.soDirectory.setWritable(true, true)) {
            throw new IOException("cannot make directory writable for us: " + this.soDirectory);
        }
        File dsoFileName = new File(this.soDirectory, iDso.dso.name);
        try {
            dsoFile = new RandomAccessFile(dsoFileName, "rw");
        } catch (IOException ex) {
            Log.w(TAG, "error overwriting " + dsoFileName + " trying to delete and start over", ex);
            SysUtil.dumbDeleteRecursive(dsoFileName);
            dsoFile = new RandomAccessFile(dsoFileName, "rw");
        }
        try {
            int sizeHint = iDso.content.available();
            if (sizeHint > 1) {
                SysUtil.fallocateIfSupported(dsoFile.getFD(), (long) sizeHint);
            }
            SysUtil.copyBytes(dsoFile, iDso.content, Integer.MAX_VALUE, ioBuffer);
            dsoFile.setLength(dsoFile.getFilePointer());
            if (!dsoFileName.setExecutable(true, false)) {
                throw new IOException("cannot make file executable: " + dsoFileName);
            }
            dsoFile.close();
        } catch (IOException e) {
            SysUtil.dumbDeleteRecursive(dsoFileName);
            throw e;
        } catch (Throwable th) {
            dsoFile.close();
            throw th;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:39:0x00a8, code lost:
        r9 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x00ae, code lost:
        if (r11 != null) goto L_0x00b0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:?, code lost:
        r6.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x00b8, code lost:
        r9 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x00b9, code lost:
        r10 = r9;
        r11 = r12;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x00be, code lost:
        r10 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x00bf, code lost:
        r11 = r9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:64:0x00c8, code lost:
        r9 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:0x00c9, code lost:
        r11.addSuppressed(r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:76:0x0104, code lost:
        r9 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:77:0x0105, code lost:
        r11.addSuppressed(r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:78:0x0109, code lost:
        r6.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:79:0x010d, code lost:
        r9 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:81:0x0110, code lost:
        r9 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:82:0x0111, code lost:
        r10 = r9;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x0058 A[Catch:{ Throwable -> 0x00a8, all -> 0x00b8 }] */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x00ae  */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x00b8 A[ExcHandler: all (r9v4 'th' java.lang.Throwable A[CUSTOM_DECLARE]), Splitter:B:6:0x0040] */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x00d3  */
    /* JADX WARNING: Removed duplicated region for block: B:6:0x0040 A[SYNTHETIC, Splitter:B:6:0x0040] */
    /* JADX WARNING: Removed duplicated region for block: B:83:0x0113  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void regenerate(byte r14, com.facebook.soloader.UnpackingSoSource.DsoManifest r15, com.facebook.soloader.UnpackingSoSource.InputDsoIterator r16) throws java.io.IOException {
        /*
            r13 = this;
            java.lang.String r9 = "fb-UnpackingSoSource"
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            r10.<init>()
            java.lang.String r11 = "regenerating DSO store "
            java.lang.StringBuilder r10 = r10.append(r11)
            java.lang.Class r11 = r13.getClass()
            java.lang.String r11 = r11.getName()
            java.lang.StringBuilder r10 = r10.append(r11)
            java.lang.String r10 = r10.toString()
            android.util.Log.v(r9, r10)
            java.io.File r7 = new java.io.File
            java.io.File r9 = r13.soDirectory
            java.lang.String r10 = "dso_manifest"
            r7.<init>(r9, r10)
            java.io.RandomAccessFile r6 = new java.io.RandomAccessFile
            java.lang.String r9 = "rw"
            r6.<init>(r7, r9)
            r12 = 0
            r1 = 0
            r9 = 1
            if (r14 != r9) goto L_0x0094
            com.facebook.soloader.UnpackingSoSource$DsoManifest r1 = com.facebook.soloader.UnpackingSoSource.DsoManifest.read(r6)     // Catch:{ Exception -> 0x008a }
            r2 = r1
        L_0x003e:
            if (r2 != 0) goto L_0x0113
            com.facebook.soloader.UnpackingSoSource$DsoManifest r1 = new com.facebook.soloader.UnpackingSoSource$DsoManifest     // Catch:{ Throwable -> 0x010d, all -> 0x00b8 }
            r9 = 0
            com.facebook.soloader.UnpackingSoSource$Dso[] r9 = new com.facebook.soloader.UnpackingSoSource.Dso[r9]     // Catch:{ Throwable -> 0x010d, all -> 0x00b8 }
            r1.<init>(r9)     // Catch:{ Throwable -> 0x010d, all -> 0x00b8 }
        L_0x0048:
            com.facebook.soloader.UnpackingSoSource$Dso[] r9 = r15.dsos     // Catch:{ Throwable -> 0x00a8, all -> 0x00b8 }
            r13.deleteUnmentionedFiles(r9)     // Catch:{ Throwable -> 0x00a8, all -> 0x00b8 }
            r9 = 32768(0x8000, float:4.5918E-41)
            byte[] r5 = new byte[r9]     // Catch:{ Throwable -> 0x00a8, all -> 0x00b8 }
        L_0x0052:
            boolean r9 = r16.hasNext()     // Catch:{ Throwable -> 0x00a8, all -> 0x00b8 }
            if (r9 == 0) goto L_0x00d1
            com.facebook.soloader.UnpackingSoSource$InputDso r4 = r16.next()     // Catch:{ Throwable -> 0x00a8, all -> 0x00b8 }
            r11 = 0
            r8 = 1
            r3 = 0
        L_0x005f:
            if (r8 == 0) goto L_0x0096
            com.facebook.soloader.UnpackingSoSource$Dso[] r9 = r1.dsos     // Catch:{ Throwable -> 0x00bc, all -> 0x0110 }
            int r9 = r9.length     // Catch:{ Throwable -> 0x00bc, all -> 0x0110 }
            if (r3 >= r9) goto L_0x0096
            com.facebook.soloader.UnpackingSoSource$Dso[] r9 = r1.dsos     // Catch:{ Throwable -> 0x00bc, all -> 0x0110 }
            r9 = r9[r3]     // Catch:{ Throwable -> 0x00bc, all -> 0x0110 }
            java.lang.String r9 = r9.name     // Catch:{ Throwable -> 0x00bc, all -> 0x0110 }
            com.facebook.soloader.UnpackingSoSource$Dso r10 = r4.dso     // Catch:{ Throwable -> 0x00bc, all -> 0x0110 }
            java.lang.String r10 = r10.name     // Catch:{ Throwable -> 0x00bc, all -> 0x0110 }
            boolean r9 = r9.equals(r10)     // Catch:{ Throwable -> 0x00bc, all -> 0x0110 }
            if (r9 == 0) goto L_0x0087
            com.facebook.soloader.UnpackingSoSource$Dso[] r9 = r1.dsos     // Catch:{ Throwable -> 0x00bc, all -> 0x0110 }
            r9 = r9[r3]     // Catch:{ Throwable -> 0x00bc, all -> 0x0110 }
            java.lang.String r9 = r9.hash     // Catch:{ Throwable -> 0x00bc, all -> 0x0110 }
            com.facebook.soloader.UnpackingSoSource$Dso r10 = r4.dso     // Catch:{ Throwable -> 0x00bc, all -> 0x0110 }
            java.lang.String r10 = r10.hash     // Catch:{ Throwable -> 0x00bc, all -> 0x0110 }
            boolean r9 = r9.equals(r10)     // Catch:{ Throwable -> 0x00bc, all -> 0x0110 }
            if (r9 == 0) goto L_0x0087
            r8 = 0
        L_0x0087:
            int r3 = r3 + 1
            goto L_0x005f
        L_0x008a:
            r0 = move-exception
            java.lang.String r9 = "fb-UnpackingSoSource"
            java.lang.String r10 = "error reading existing DSO manifest"
            android.util.Log.i(r9, r10, r0)     // Catch:{ Throwable -> 0x00a8, all -> 0x00b8 }
        L_0x0094:
            r2 = r1
            goto L_0x003e
        L_0x0096:
            if (r8 == 0) goto L_0x009b
            r13.extractDso(r4, r5)     // Catch:{ Throwable -> 0x00bc, all -> 0x0110 }
        L_0x009b:
            if (r4 == 0) goto L_0x0052
            if (r11 == 0) goto L_0x00b4
            r4.close()     // Catch:{ Throwable -> 0x00a3, all -> 0x00b8 }
            goto L_0x0052
        L_0x00a3:
            r9 = move-exception
            r11.addSuppressed(r9)     // Catch:{ Throwable -> 0x00a8, all -> 0x00b8 }
            goto L_0x0052
        L_0x00a8:
            r9 = move-exception
        L_0x00a9:
            throw r9     // Catch:{ all -> 0x00aa }
        L_0x00aa:
            r10 = move-exception
            r11 = r9
        L_0x00ac:
            if (r6 == 0) goto L_0x00b3
            if (r11 == 0) goto L_0x0109
            r6.close()     // Catch:{ Throwable -> 0x0104 }
        L_0x00b3:
            throw r10
        L_0x00b4:
            r4.close()     // Catch:{ Throwable -> 0x00a8, all -> 0x00b8 }
            goto L_0x0052
        L_0x00b8:
            r9 = move-exception
            r10 = r9
            r11 = r12
            goto L_0x00ac
        L_0x00bc:
            r9 = move-exception
            throw r9     // Catch:{ all -> 0x00be }
        L_0x00be:
            r10 = move-exception
            r11 = r9
        L_0x00c0:
            if (r4 == 0) goto L_0x00c7
            if (r11 == 0) goto L_0x00cd
            r4.close()     // Catch:{ Throwable -> 0x00c8, all -> 0x00b8 }
        L_0x00c7:
            throw r10     // Catch:{ Throwable -> 0x00a8, all -> 0x00b8 }
        L_0x00c8:
            r9 = move-exception
            r11.addSuppressed(r9)     // Catch:{ Throwable -> 0x00a8, all -> 0x00b8 }
            goto L_0x00c7
        L_0x00cd:
            r4.close()     // Catch:{ Throwable -> 0x00a8, all -> 0x00b8 }
            goto L_0x00c7
        L_0x00d1:
            if (r6 == 0) goto L_0x00d8
            if (r12 == 0) goto L_0x0100
            r6.close()     // Catch:{ Throwable -> 0x00fb }
        L_0x00d8:
            java.lang.String r9 = "fb-UnpackingSoSource"
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            r10.<init>()
            java.lang.String r11 = "Finished regenerating DSO store "
            java.lang.StringBuilder r10 = r10.append(r11)
            java.lang.Class r11 = r13.getClass()
            java.lang.String r11 = r11.getName()
            java.lang.StringBuilder r10 = r10.append(r11)
            java.lang.String r10 = r10.toString()
            android.util.Log.v(r9, r10)
            return
        L_0x00fb:
            r9 = move-exception
            r12.addSuppressed(r9)
            goto L_0x00d8
        L_0x0100:
            r6.close()
            goto L_0x00d8
        L_0x0104:
            r9 = move-exception
            r11.addSuppressed(r9)
            goto L_0x00b3
        L_0x0109:
            r6.close()
            goto L_0x00b3
        L_0x010d:
            r9 = move-exception
            r1 = r2
            goto L_0x00a9
        L_0x0110:
            r9 = move-exception
            r10 = r9
            goto L_0x00c0
        L_0x0113:
            r1 = r2
            goto L_0x0048
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.soloader.UnpackingSoSource.regenerate(byte, com.facebook.soloader.UnpackingSoSource$DsoManifest, com.facebook.soloader.UnpackingSoSource$InputDsoIterator):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:100:0x012d, code lost:
        r13.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:104:0x0136, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:105:0x0137, code lost:
        r5 = r3;
        r8 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:107:0x013f, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:108:0x0140, code lost:
        r8.addSuppressed(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:109:0x0144, code lost:
        r16.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:121:0x0198, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:122:0x0199, code lost:
        r5 = r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x00d4, code lost:
        r14 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x00e4, code lost:
        r5 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x00e5, code lost:
        r8 = r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:69:0x0102, code lost:
        if (r8 != null) goto L_0x0104;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:71:?, code lost:
        r16.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:77:0x010a, code lost:
        r5 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:78:0x010b, code lost:
        r8 = r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:86:0x0118, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:87:0x0119, code lost:
        r5 = r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:91:0x011d, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:92:0x011e, code lost:
        if (r13 != null) goto L_0x0120;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:93:0x0120, code lost:
        if (r5 != null) goto L_0x0122;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:95:?, code lost:
        r13.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:97:?, code lost:
        throw r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:98:0x0126, code lost:
        r18 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:99:0x0127, code lost:
        r5.addSuppressed(r18);
     */
    /* JADX WARNING: Removed duplicated region for block: B:104:0x0136 A[Catch:{ all -> 0x0136, all -> 0x010a }, ExcHandler: all (r3v7 'th' java.lang.Throwable A[CUSTOM_DECLARE, Catch:{ all -> 0x0136, all -> 0x010a }])] */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x0102  */
    /* JADX WARNING: Removed duplicated region for block: B:86:0x0118 A[ExcHandler: all (r3v21 'th' java.lang.Throwable A[CUSTOM_DECLARE])] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean refreshLocked(com.facebook.soloader.FileLocker r21, int r22, byte[] r23) throws java.io.IOException {
        /*
            r20 = this;
            java.io.File r7 = new java.io.File
            r0 = r20
            java.io.File r3 = r0.soDirectory
            java.lang.String r5 = "dso_state"
            r7.<init>(r3, r5)
            java.io.RandomAccessFile r15 = new java.io.RandomAccessFile
            java.lang.String r3 = "rw"
            r15.<init>(r7, r3)
            r8 = 0
            byte r14 = r15.readByte()     // Catch:{ EOFException -> 0x00d3, Throwable -> 0x00e2, all -> 0x0198 }
            r3 = 1
            if (r14 == r3) goto L_0x004a
            java.lang.String r3 = "fb-UnpackingSoSource"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ EOFException -> 0x00d3, Throwable -> 0x00e2, all -> 0x0198 }
            r5.<init>()     // Catch:{ EOFException -> 0x00d3, Throwable -> 0x00e2, all -> 0x0198 }
            java.lang.String r17 = "dso store "
            r0 = r17
            java.lang.StringBuilder r5 = r5.append(r0)     // Catch:{ EOFException -> 0x00d3, Throwable -> 0x00e2, all -> 0x0198 }
            r0 = r20
            java.io.File r0 = r0.soDirectory     // Catch:{ EOFException -> 0x00d3, Throwable -> 0x00e2, all -> 0x0198 }
            r17 = r0
            r0 = r17
            java.lang.StringBuilder r5 = r5.append(r0)     // Catch:{ EOFException -> 0x00d3, Throwable -> 0x00e2, all -> 0x0198 }
            java.lang.String r17 = " regeneration interrupted: wiping clean"
            r0 = r17
            java.lang.StringBuilder r5 = r5.append(r0)     // Catch:{ EOFException -> 0x00d3, Throwable -> 0x00e2, all -> 0x0198 }
            java.lang.String r5 = r5.toString()     // Catch:{ EOFException -> 0x00d3, Throwable -> 0x00e2, all -> 0x0198 }
            android.util.Log.v(r3, r5)     // Catch:{ EOFException -> 0x00d3, Throwable -> 0x00e2, all -> 0x0198 }
            r14 = 0
        L_0x004a:
            if (r15 == 0) goto L_0x0051
            if (r8 == 0) goto L_0x00dd
            r15.close()     // Catch:{ Throwable -> 0x00d7 }
        L_0x0051:
            java.io.File r4 = new java.io.File
            r0 = r20
            java.io.File r3 = r0.soDirectory
            java.lang.String r5 = "dso_deps"
            r4.<init>(r3, r5)
            r10 = 0
            java.io.RandomAccessFile r9 = new java.io.RandomAccessFile
            java.lang.String r3 = "rw"
            r9.<init>(r4, r3)
            r17 = 0
            long r18 = r9.length()     // Catch:{ Throwable -> 0x0108, all -> 0x0136 }
            r0 = r18
            int r3 = (int) r0     // Catch:{ Throwable -> 0x0108, all -> 0x0136 }
            byte[] r12 = new byte[r3]     // Catch:{ Throwable -> 0x0108, all -> 0x0136 }
            int r3 = r9.read(r12)     // Catch:{ Throwable -> 0x0108, all -> 0x0136 }
            int r5 = r12.length     // Catch:{ Throwable -> 0x0108, all -> 0x0136 }
            if (r3 == r5) goto L_0x0082
            java.lang.String r3 = "fb-UnpackingSoSource"
            java.lang.String r5 = "short read of so store deps file: marking unclean"
            android.util.Log.v(r3, r5)     // Catch:{ Throwable -> 0x0108, all -> 0x0136 }
            r14 = 0
        L_0x0082:
            r0 = r23
            boolean r3 = java.util.Arrays.equals(r12, r0)     // Catch:{ Throwable -> 0x0108, all -> 0x0136 }
            if (r3 != 0) goto L_0x0094
            java.lang.String r3 = "fb-UnpackingSoSource"
            java.lang.String r5 = "deps mismatch on deps store: regenerating"
            android.util.Log.v(r3, r5)     // Catch:{ Throwable -> 0x0108, all -> 0x0136 }
            r14 = 0
        L_0x0094:
            if (r14 == 0) goto L_0x009a
            r3 = r22 & 2
            if (r3 == 0) goto L_0x00c8
        L_0x009a:
            java.lang.String r3 = "fb-UnpackingSoSource"
            java.lang.String r5 = "so store dirty: regenerating"
            android.util.Log.v(r3, r5)     // Catch:{ Throwable -> 0x0108, all -> 0x0136 }
            r3 = 0
            writeState(r7, r3)     // Catch:{ Throwable -> 0x0108, all -> 0x0136 }
            com.facebook.soloader.UnpackingSoSource$Unpacker r16 = r20.makeUnpacker()     // Catch:{ Throwable -> 0x0108, all -> 0x0136 }
            r8 = 0
            com.facebook.soloader.UnpackingSoSource$DsoManifest r10 = r16.getDsoManifest()     // Catch:{ Throwable -> 0x00fc, all -> 0x0118 }
            com.facebook.soloader.UnpackingSoSource$InputDsoIterator r13 = r16.openDsoIterator()     // Catch:{ Throwable -> 0x00fc, all -> 0x0118 }
            r5 = 0
            r0 = r20
            r0.regenerate(r14, r10, r13)     // Catch:{ Throwable -> 0x011b }
            if (r13 == 0) goto L_0x00c1
            if (r5 == 0) goto L_0x0114
            r13.close()     // Catch:{ Throwable -> 0x00f7, all -> 0x0118 }
        L_0x00c1:
            if (r16 == 0) goto L_0x00c8
            if (r8 == 0) goto L_0x013b
            r16.close()     // Catch:{ Throwable -> 0x0131, all -> 0x0136 }
        L_0x00c8:
            if (r9 == 0) goto L_0x00cf
            if (r17 == 0) goto L_0x014f
            r9.close()     // Catch:{ Throwable -> 0x0148 }
        L_0x00cf:
            if (r10 != 0) goto L_0x015d
            r3 = 0
        L_0x00d2:
            return r3
        L_0x00d3:
            r11 = move-exception
            r14 = 0
            goto L_0x004a
        L_0x00d7:
            r3 = move-exception
            r8.addSuppressed(r3)
            goto L_0x0051
        L_0x00dd:
            r15.close()
            goto L_0x0051
        L_0x00e2:
            r3 = move-exception
            throw r3     // Catch:{ all -> 0x00e4 }
        L_0x00e4:
            r5 = move-exception
            r8 = r3
        L_0x00e6:
            if (r15 == 0) goto L_0x00ed
            if (r8 == 0) goto L_0x00f3
            r15.close()     // Catch:{ Throwable -> 0x00ee }
        L_0x00ed:
            throw r5
        L_0x00ee:
            r3 = move-exception
            r8.addSuppressed(r3)
            goto L_0x00ed
        L_0x00f3:
            r15.close()
            goto L_0x00ed
        L_0x00f7:
            r3 = move-exception
            r5.addSuppressed(r3)     // Catch:{ Throwable -> 0x00fc, all -> 0x0118 }
            goto L_0x00c1
        L_0x00fc:
            r3 = move-exception
            throw r3     // Catch:{ all -> 0x00fe }
        L_0x00fe:
            r5 = move-exception
            r8 = r3
        L_0x0100:
            if (r16 == 0) goto L_0x0107
            if (r8 == 0) goto L_0x0144
            r16.close()     // Catch:{ Throwable -> 0x013f, all -> 0x0136 }
        L_0x0107:
            throw r5     // Catch:{ Throwable -> 0x0108, all -> 0x0136 }
        L_0x0108:
            r3 = move-exception
            throw r3     // Catch:{ all -> 0x010a }
        L_0x010a:
            r5 = move-exception
            r8 = r3
        L_0x010c:
            if (r9 == 0) goto L_0x0113
            if (r8 == 0) goto L_0x0159
            r9.close()     // Catch:{ Throwable -> 0x0154 }
        L_0x0113:
            throw r5
        L_0x0114:
            r13.close()     // Catch:{ Throwable -> 0x00fc, all -> 0x0118 }
            goto L_0x00c1
        L_0x0118:
            r3 = move-exception
            r5 = r3
            goto L_0x0100
        L_0x011b:
            r5 = move-exception
            throw r5     // Catch:{ all -> 0x011d }
        L_0x011d:
            r3 = move-exception
            if (r13 == 0) goto L_0x0125
            if (r5 == 0) goto L_0x012d
            r13.close()     // Catch:{ Throwable -> 0x0126, all -> 0x0118 }
        L_0x0125:
            throw r3     // Catch:{ Throwable -> 0x00fc, all -> 0x0118 }
        L_0x0126:
            r18 = move-exception
            r0 = r18
            r5.addSuppressed(r0)     // Catch:{ Throwable -> 0x00fc, all -> 0x0118 }
            goto L_0x0125
        L_0x012d:
            r13.close()     // Catch:{ Throwable -> 0x00fc, all -> 0x0118 }
            goto L_0x0125
        L_0x0131:
            r3 = move-exception
            r8.addSuppressed(r3)     // Catch:{ Throwable -> 0x0108, all -> 0x0136 }
            goto L_0x00c8
        L_0x0136:
            r3 = move-exception
            r5 = r3
            r8 = r17
            goto L_0x010c
        L_0x013b:
            r16.close()     // Catch:{ Throwable -> 0x0108, all -> 0x0136 }
            goto L_0x00c8
        L_0x013f:
            r3 = move-exception
            r8.addSuppressed(r3)     // Catch:{ Throwable -> 0x0108, all -> 0x0136 }
            goto L_0x0107
        L_0x0144:
            r16.close()     // Catch:{ Throwable -> 0x0108, all -> 0x0136 }
            goto L_0x0107
        L_0x0148:
            r3 = move-exception
            r0 = r17
            r0.addSuppressed(r3)
            goto L_0x00cf
        L_0x014f:
            r9.close()
            goto L_0x00cf
        L_0x0154:
            r3 = move-exception
            r8.addSuppressed(r3)
            goto L_0x0113
        L_0x0159:
            r9.close()
            goto L_0x0113
        L_0x015d:
            r6 = r10
            com.facebook.soloader.UnpackingSoSource$1 r2 = new com.facebook.soloader.UnpackingSoSource$1
            r3 = r20
            r5 = r23
            r8 = r21
            r2.<init>(r4, r5, r6, r7, r8)
            r3 = r22 & 1
            if (r3 == 0) goto L_0x0194
            java.lang.Thread r3 = new java.lang.Thread
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r8 = "SoSync:"
            java.lang.StringBuilder r5 = r5.append(r8)
            r0 = r20
            java.io.File r8 = r0.soDirectory
            java.lang.String r8 = r8.getName()
            java.lang.StringBuilder r5 = r5.append(r8)
            java.lang.String r5 = r5.toString()
            r3.<init>(r2, r5)
            r3.start()
        L_0x0191:
            r3 = 1
            goto L_0x00d2
        L_0x0194:
            r2.run()
            goto L_0x0191
        L_0x0198:
            r3 = move-exception
            r5 = r3
            goto L_0x00e6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.soloader.UnpackingSoSource.refreshLocked(com.facebook.soloader.FileLocker, int, byte[]):boolean");
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0046, code lost:
        r6 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0047, code lost:
        r7 = r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0059, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x005a, code lost:
        r6 = r5;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public byte[] getDepsBlock() throws java.io.IOException {
        /*
            r8 = this;
            android.os.Parcel r3 = android.os.Parcel.obtain()
            com.facebook.soloader.UnpackingSoSource$Unpacker r4 = r8.makeUnpacker()
            r7 = 0
            com.facebook.soloader.UnpackingSoSource$DsoManifest r5 = r4.getDsoManifest()     // Catch:{ Throwable -> 0x0044, all -> 0x0059 }
            com.facebook.soloader.UnpackingSoSource$Dso[] r1 = r5.dsos     // Catch:{ Throwable -> 0x0044, all -> 0x0059 }
            r5 = 1
            r3.writeByte(r5)     // Catch:{ Throwable -> 0x0044, all -> 0x0059 }
            int r5 = r1.length     // Catch:{ Throwable -> 0x0044, all -> 0x0059 }
            r3.writeInt(r5)     // Catch:{ Throwable -> 0x0044, all -> 0x0059 }
            r2 = 0
        L_0x0018:
            int r5 = r1.length     // Catch:{ Throwable -> 0x0044, all -> 0x0059 }
            if (r2 >= r5) goto L_0x002c
            r5 = r1[r2]     // Catch:{ Throwable -> 0x0044, all -> 0x0059 }
            java.lang.String r5 = r5.name     // Catch:{ Throwable -> 0x0044, all -> 0x0059 }
            r3.writeString(r5)     // Catch:{ Throwable -> 0x0044, all -> 0x0059 }
            r5 = r1[r2]     // Catch:{ Throwable -> 0x0044, all -> 0x0059 }
            java.lang.String r5 = r5.hash     // Catch:{ Throwable -> 0x0044, all -> 0x0059 }
            r3.writeString(r5)     // Catch:{ Throwable -> 0x0044, all -> 0x0059 }
            int r2 = r2 + 1
            goto L_0x0018
        L_0x002c:
            if (r4 == 0) goto L_0x0033
            if (r7 == 0) goto L_0x0040
            r4.close()     // Catch:{ Throwable -> 0x003b }
        L_0x0033:
            byte[] r0 = r3.marshall()
            r3.recycle()
            return r0
        L_0x003b:
            r5 = move-exception
            r7.addSuppressed(r5)
            goto L_0x0033
        L_0x0040:
            r4.close()
            goto L_0x0033
        L_0x0044:
            r5 = move-exception
            throw r5     // Catch:{ all -> 0x0046 }
        L_0x0046:
            r6 = move-exception
            r7 = r5
        L_0x0048:
            if (r4 == 0) goto L_0x004f
            if (r7 == 0) goto L_0x0055
            r4.close()     // Catch:{ Throwable -> 0x0050 }
        L_0x004f:
            throw r6
        L_0x0050:
            r5 = move-exception
            r7.addSuppressed(r5)
            goto L_0x004f
        L_0x0055:
            r4.close()
            goto L_0x004f
        L_0x0059:
            r5 = move-exception
            r6 = r5
            goto L_0x0048
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.soloader.UnpackingSoSource.getDepsBlock():byte[]");
    }

    /* access modifiers changed from: protected */
    public void prepare(int flags) throws IOException {
        SysUtil.mkdirOrThrow(this.soDirectory);
        FileLocker lock = FileLocker.lock(new File(this.soDirectory, LOCK_FILE_NAME));
        try {
            Log.v(TAG, "locked dso store " + this.soDirectory);
            if (refreshLocked(lock, flags, getDepsBlock())) {
                lock = null;
            } else {
                Log.i(TAG, "dso store is up-to-date: " + this.soDirectory);
            }
            if (lock == null) {
                Log.v(TAG, "not releasing dso store lock for " + this.soDirectory + " (syncer thread started)");
            }
        } finally {
            if (lock != null) {
                Log.v(TAG, "releasing dso store lock for " + this.soDirectory);
                lock.close();
            } else {
                Log.v(TAG, "not releasing dso store lock for " + this.soDirectory + " (syncer thread started)");
            }
        }
    }

    private Object getLibraryLock(String soName) {
        Object lock;
        synchronized (this.mLibsBeingLoaded) {
            lock = this.mLibsBeingLoaded.get(soName);
            if (lock == null) {
                lock = new Object();
                this.mLibsBeingLoaded.put(soName, lock);
            }
        }
        return lock;
    }

    /* access modifiers changed from: protected */
    public synchronized void prepare(String soName) throws IOException {
        synchronized (getLibraryLock(soName)) {
            this.mCorruptedLib = soName;
            prepare(2);
        }
    }

    public int loadLibrary(String soName, int loadFlags, StrictMode.ThreadPolicy threadPolicy) throws IOException {
        int loadLibraryFrom;
        synchronized (getLibraryLock(soName)) {
            loadLibraryFrom = loadLibraryFrom(soName, loadFlags, this.soDirectory, threadPolicy);
        }
        return loadLibraryFrom;
    }
}
