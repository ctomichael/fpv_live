package com.facebook.soloader;

import android.content.Context;
import com.facebook.soloader.UnpackingSoSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public final class ExoSoSource extends UnpackingSoSource {
    public ExoSoSource(Context context, String name) {
        super(context, name);
    }

    /* access modifiers changed from: protected */
    public UnpackingSoSource.Unpacker makeUnpacker() throws IOException {
        return new ExoUnpacker(this);
    }

    private final class ExoUnpacker extends UnpackingSoSource.Unpacker {
        /* access modifiers changed from: private */
        public final FileDso[] mDsos;

        /* JADX WARNING: Code restructure failed: missing block: B:24:0x00cc, code lost:
            r20 = th;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:25:0x00cd, code lost:
            r21 = r19;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:35:0x00d9, code lost:
            r20 = th;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:36:0x00da, code lost:
            r21 = r19;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:38:0x00de, code lost:
            if (r21 != null) goto L_0x00e0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:40:?, code lost:
            r10.close();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:50:0x0150, code lost:
            r19 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:51:0x0151, code lost:
            r20 = r19;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:53:0x0158, code lost:
            if (r6 == null) goto L_0x015f;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:54:0x015a, code lost:
            if (0 == 0) goto L_0x0182;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:56:?, code lost:
            r6.close();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:63:0x0172, code lost:
            r19 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:64:0x0173, code lost:
            r0 = null;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:66:?, code lost:
            r0.addSuppressed(r19);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:67:0x017b, code lost:
            r19 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:68:0x017c, code lost:
            r20 = r19;
            r21 = null;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:69:0x0182, code lost:
            r6.close();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:74:0x019a, code lost:
            r19 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:75:0x019b, code lost:
            r21.addSuppressed(r19);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:76:0x01a4, code lost:
            r10.close();
         */
        /* JADX WARNING: Removed duplicated region for block: B:38:0x00de  */
        /* JADX WARNING: Removed duplicated region for block: B:67:0x017b A[Catch:{ all -> 0x017b, all -> 0x00d9 }, ExcHandler: all (r19v18 'th' java.lang.Throwable A[CUSTOM_DECLARE, Catch:{ all -> 0x017b, all -> 0x00d9 }])] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        ExoUnpacker(com.facebook.soloader.UnpackingSoSource r28) throws java.io.IOException {
            /*
                r26 = this;
                r0 = r27
                r1 = r26
                com.facebook.soloader.ExoSoSource.this = r0
                r26.<init>()
                r0 = r27
                android.content.Context r7 = r0.mContext
                java.io.File r8 = new java.io.File
                java.lang.StringBuilder r19 = new java.lang.StringBuilder
                r19.<init>()
                java.lang.String r20 = "/data/local/tmp/exopackage/"
                java.lang.StringBuilder r19 = r19.append(r20)
                java.lang.String r20 = r7.getPackageName()
                java.lang.StringBuilder r19 = r19.append(r20)
                java.lang.String r20 = "/native-libs/"
                java.lang.StringBuilder r19 = r19.append(r20)
                java.lang.String r19 = r19.toString()
                r0 = r19
                r8.<init>(r0)
                java.util.ArrayList r16 = new java.util.ArrayList
                r16.<init>()
                java.util.LinkedHashSet r12 = new java.util.LinkedHashSet
                r12.<init>()
                java.lang.String[] r23 = com.facebook.soloader.SysUtil.getSupportedAbis()
                r0 = r23
                int r0 = r0.length
                r24 = r0
                r19 = 0
                r20 = r19
            L_0x004a:
                r0 = r20
                r1 = r24
                if (r0 >= r1) goto L_0x01a9
                r3 = r23[r20]
                java.io.File r4 = new java.io.File
                r4.<init>(r8, r3)
                boolean r19 = r4.isDirectory()
                if (r19 != 0) goto L_0x0062
            L_0x005d:
                int r19 = r20 + 1
                r20 = r19
                goto L_0x004a
            L_0x0062:
                r12.add(r3)
                java.io.File r14 = new java.io.File
                java.lang.String r19 = "metadata.txt"
                r0 = r19
                r14.<init>(r4, r0)
                boolean r19 = r14.isFile()
                if (r19 == 0) goto L_0x005d
                java.io.FileReader r10 = new java.io.FileReader
                r10.<init>(r14)
                r22 = 0
                java.io.BufferedReader r6 = new java.io.BufferedReader     // Catch:{ Throwable -> 0x00d7, all -> 0x017b }
                r6.<init>(r10)     // Catch:{ Throwable -> 0x00d7, all -> 0x017b }
                r21 = 0
            L_0x0083:
                java.lang.String r13 = r6.readLine()     // Catch:{ Throwable -> 0x00ca, all -> 0x0150 }
                if (r13 == 0) goto L_0x0158
                int r19 = r13.length()     // Catch:{ Throwable -> 0x00ca, all -> 0x0150 }
                if (r19 == 0) goto L_0x0083
                r19 = 32
                r0 = r19
                int r17 = r13.indexOf(r0)     // Catch:{ Throwable -> 0x00ca, all -> 0x0150 }
                r19 = -1
                r0 = r17
                r1 = r19
                if (r0 != r1) goto L_0x00e4
                java.lang.RuntimeException r19 = new java.lang.RuntimeException     // Catch:{ Throwable -> 0x00ca, all -> 0x0150 }
                java.lang.StringBuilder r20 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x00ca, all -> 0x0150 }
                r20.<init>()     // Catch:{ Throwable -> 0x00ca, all -> 0x0150 }
                java.lang.String r23 = "illegal line in exopackage metadata: ["
                r0 = r20
                r1 = r23
                java.lang.StringBuilder r20 = r0.append(r1)     // Catch:{ Throwable -> 0x00ca, all -> 0x0150 }
                r0 = r20
                java.lang.StringBuilder r20 = r0.append(r13)     // Catch:{ Throwable -> 0x00ca, all -> 0x0150 }
                java.lang.String r23 = "]"
                r0 = r20
                r1 = r23
                java.lang.StringBuilder r20 = r0.append(r1)     // Catch:{ Throwable -> 0x00ca, all -> 0x0150 }
                java.lang.String r20 = r20.toString()     // Catch:{ Throwable -> 0x00ca, all -> 0x0150 }
                r19.<init>(r20)     // Catch:{ Throwable -> 0x00ca, all -> 0x0150 }
                throw r19     // Catch:{ Throwable -> 0x00ca, all -> 0x0150 }
            L_0x00ca:
                r19 = move-exception
                throw r19     // Catch:{ all -> 0x00cc }
            L_0x00cc:
                r20 = move-exception
                r21 = r19
            L_0x00cf:
                if (r6 == 0) goto L_0x00d6
                if (r21 == 0) goto L_0x0190
                r6.close()     // Catch:{ Throwable -> 0x0186, all -> 0x017b }
            L_0x00d6:
                throw r20     // Catch:{ Throwable -> 0x00d7, all -> 0x017b }
            L_0x00d7:
                r19 = move-exception
                throw r19     // Catch:{ all -> 0x00d9 }
            L_0x00d9:
                r20 = move-exception
                r21 = r19
            L_0x00dc:
                if (r10 == 0) goto L_0x00e3
                if (r21 == 0) goto L_0x01a4
                r10.close()     // Catch:{ Throwable -> 0x019a }
            L_0x00e3:
                throw r20
            L_0x00e4:
                java.lang.StringBuilder r19 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x00ca, all -> 0x0150 }
                r19.<init>()     // Catch:{ Throwable -> 0x00ca, all -> 0x0150 }
                r25 = 0
                r0 = r25
                r1 = r17
                java.lang.String r25 = r13.substring(r0, r1)     // Catch:{ Throwable -> 0x00ca, all -> 0x0150 }
                r0 = r19
                r1 = r25
                java.lang.StringBuilder r19 = r0.append(r1)     // Catch:{ Throwable -> 0x00ca, all -> 0x0150 }
                java.lang.String r25 = ".so"
                r0 = r19
                r1 = r25
                java.lang.StringBuilder r19 = r0.append(r1)     // Catch:{ Throwable -> 0x00ca, all -> 0x0150 }
                java.lang.String r18 = r19.toString()     // Catch:{ Throwable -> 0x00ca, all -> 0x0150 }
                int r15 = r16.size()     // Catch:{ Throwable -> 0x00ca, all -> 0x0150 }
                r9 = 0
                r11 = 0
            L_0x0110:
                if (r11 >= r15) goto L_0x012b
                r0 = r16
                java.lang.Object r19 = r0.get(r11)     // Catch:{ Throwable -> 0x00ca, all -> 0x0150 }
                com.facebook.soloader.ExoSoSource$FileDso r19 = (com.facebook.soloader.ExoSoSource.FileDso) r19     // Catch:{ Throwable -> 0x00ca, all -> 0x0150 }
                r0 = r19
                java.lang.String r0 = r0.name     // Catch:{ Throwable -> 0x00ca, all -> 0x0150 }
                r19 = r0
                r0 = r19
                r1 = r18
                boolean r19 = r0.equals(r1)     // Catch:{ Throwable -> 0x00ca, all -> 0x0150 }
                if (r19 == 0) goto L_0x0155
                r9 = 1
            L_0x012b:
                if (r9 != 0) goto L_0x0083
                int r19 = r17 + 1
                r0 = r19
                java.lang.String r5 = r13.substring(r0)     // Catch:{ Throwable -> 0x00ca, all -> 0x0150 }
                com.facebook.soloader.ExoSoSource$FileDso r19 = new com.facebook.soloader.ExoSoSource$FileDso     // Catch:{ Throwable -> 0x00ca, all -> 0x0150 }
                java.io.File r25 = new java.io.File     // Catch:{ Throwable -> 0x00ca, all -> 0x0150 }
                r0 = r25
                r0.<init>(r4, r5)     // Catch:{ Throwable -> 0x00ca, all -> 0x0150 }
                r0 = r19
                r1 = r18
                r2 = r25
                r0.<init>(r1, r5, r2)     // Catch:{ Throwable -> 0x00ca, all -> 0x0150 }
                r0 = r16
                r1 = r19
                r0.add(r1)     // Catch:{ Throwable -> 0x00ca, all -> 0x0150 }
                goto L_0x0083
            L_0x0150:
                r19 = move-exception
                r20 = r19
                goto L_0x00cf
            L_0x0155:
                int r11 = r11 + 1
                goto L_0x0110
            L_0x0158:
                if (r6 == 0) goto L_0x015f
                if (r21 == 0) goto L_0x0182
                r6.close()     // Catch:{ Throwable -> 0x0172, all -> 0x017b }
            L_0x015f:
                if (r10 == 0) goto L_0x005d
                if (r22 == 0) goto L_0x0195
                r10.close()     // Catch:{ Throwable -> 0x0168 }
                goto L_0x005d
            L_0x0168:
                r19 = move-exception
                r0 = r22
                r1 = r19
                r0.addSuppressed(r1)
                goto L_0x005d
            L_0x0172:
                r19 = move-exception
                r0 = r21
                r1 = r19
                r0.addSuppressed(r1)     // Catch:{ Throwable -> 0x00d7, all -> 0x017b }
                goto L_0x015f
            L_0x017b:
                r19 = move-exception
                r20 = r19
                r21 = r22
                goto L_0x00dc
            L_0x0182:
                r6.close()     // Catch:{ Throwable -> 0x00d7, all -> 0x017b }
                goto L_0x015f
            L_0x0186:
                r19 = move-exception
                r0 = r21
                r1 = r19
                r0.addSuppressed(r1)     // Catch:{ Throwable -> 0x00d7, all -> 0x017b }
                goto L_0x00d6
            L_0x0190:
                r6.close()     // Catch:{ Throwable -> 0x00d7, all -> 0x017b }
                goto L_0x00d6
            L_0x0195:
                r10.close()
                goto L_0x005d
            L_0x019a:
                r19 = move-exception
                r0 = r21
                r1 = r19
                r0.addSuppressed(r1)
                goto L_0x00e3
            L_0x01a4:
                r10.close()
                goto L_0x00e3
            L_0x01a9:
                int r19 = r12.size()
                r0 = r19
                java.lang.String[] r0 = new java.lang.String[r0]
                r19 = r0
                r0 = r19
                java.lang.Object[] r19 = r12.toArray(r0)
                java.lang.String[] r19 = (java.lang.String[]) r19
                r0 = r28
                r1 = r19
                r0.setSoSourceAbis(r1)
                int r19 = r16.size()
                r0 = r19
                com.facebook.soloader.ExoSoSource$FileDso[] r0 = new com.facebook.soloader.ExoSoSource.FileDso[r0]
                r19 = r0
                r0 = r16
                r1 = r19
                java.lang.Object[] r19 = r0.toArray(r1)
                com.facebook.soloader.ExoSoSource$FileDso[] r19 = (com.facebook.soloader.ExoSoSource.FileDso[]) r19
                r0 = r19
                r1 = r26
                r1.mDsos = r0
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.facebook.soloader.ExoSoSource.ExoUnpacker.<init>(com.facebook.soloader.ExoSoSource, com.facebook.soloader.UnpackingSoSource):void");
        }

        /* access modifiers changed from: protected */
        public UnpackingSoSource.DsoManifest getDsoManifest() throws IOException {
            return new UnpackingSoSource.DsoManifest(this.mDsos);
        }

        /* access modifiers changed from: protected */
        public UnpackingSoSource.InputDsoIterator openDsoIterator() throws IOException {
            return new FileBackedInputDsoIterator();
        }

        private final class FileBackedInputDsoIterator extends UnpackingSoSource.InputDsoIterator {
            private int mCurrentDso;

            private FileBackedInputDsoIterator() {
            }

            public boolean hasNext() {
                return this.mCurrentDso < ExoUnpacker.this.mDsos.length;
            }

            public UnpackingSoSource.InputDso next() throws IOException {
                FileDso[] access$100 = ExoUnpacker.this.mDsos;
                int i = this.mCurrentDso;
                this.mCurrentDso = i + 1;
                FileDso fileDso = access$100[i];
                FileInputStream dsoFile = new FileInputStream(fileDso.backingFile);
                try {
                    UnpackingSoSource.InputDso ret = new UnpackingSoSource.InputDso(fileDso, dsoFile);
                    dsoFile = null;
                    return ret;
                } finally {
                    if (dsoFile != null) {
                        dsoFile.close();
                    }
                }
            }
        }
    }

    private static final class FileDso extends UnpackingSoSource.Dso {
        final File backingFile;

        FileDso(String name, String hash, File backingFile2) {
            super(name, hash);
            this.backingFile = backingFile2;
        }
    }
}
