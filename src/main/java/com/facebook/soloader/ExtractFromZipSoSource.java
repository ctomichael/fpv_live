package com.facebook.soloader;

import android.content.Context;
import com.facebook.soloader.UnpackingSoSource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.annotation.Nullable;

public class ExtractFromZipSoSource extends UnpackingSoSource {
    protected final File mZipFileName;
    protected final String mZipSearchPattern;

    public ExtractFromZipSoSource(Context context, String name, File zipFileName, String zipSearchPattern) {
        super(context, name);
        this.mZipFileName = zipFileName;
        this.mZipSearchPattern = zipSearchPattern;
    }

    /* access modifiers changed from: protected */
    public UnpackingSoSource.Unpacker makeUnpacker() throws IOException {
        return new ZipUnpacker(this);
    }

    protected class ZipUnpacker extends UnpackingSoSource.Unpacker {
        /* access modifiers changed from: private */
        @Nullable
        public ZipDso[] mDsos;
        private final UnpackingSoSource mSoSource;
        /* access modifiers changed from: private */
        public final ZipFile mZipFile;

        ZipUnpacker(UnpackingSoSource soSource) throws IOException {
            this.mZipFile = new ZipFile(ExtractFromZipSoSource.this.mZipFileName);
            this.mSoSource = soSource;
        }

        /* access modifiers changed from: package-private */
        public final ZipDso[] ensureDsos() {
            if (this.mDsos == null) {
                Set<String> librariesAbiSet = new LinkedHashSet<>();
                HashMap<String, ZipDso> providedLibraries = new HashMap<>();
                Pattern zipSearchPattern = Pattern.compile(ExtractFromZipSoSource.this.mZipSearchPattern);
                String[] supportedAbis = SysUtil.getSupportedAbis();
                Enumeration<? extends ZipEntry> entries = this.mZipFile.entries();
                while (entries.hasMoreElements()) {
                    ZipEntry entry = (ZipEntry) entries.nextElement();
                    Matcher m = zipSearchPattern.matcher(entry.getName());
                    if (m.matches()) {
                        String libraryAbi = m.group(1);
                        String soName = m.group(2);
                        int abiScore = SysUtil.findAbiScore(supportedAbis, libraryAbi);
                        if (abiScore >= 0) {
                            librariesAbiSet.add(libraryAbi);
                            ZipDso so = (ZipDso) providedLibraries.get(soName);
                            if (so == null || abiScore < so.abiScore) {
                                providedLibraries.put(soName, new ZipDso(soName, entry, abiScore));
                            }
                        }
                    }
                }
                this.mSoSource.setSoSourceAbis((String[]) librariesAbiSet.toArray(new String[librariesAbiSet.size()]));
                ZipDso[] dsos = (ZipDso[]) providedLibraries.values().toArray(new ZipDso[providedLibraries.size()]);
                Arrays.sort(dsos);
                int nrFilteredDsos = 0;
                for (int i = 0; i < dsos.length; i++) {
                    ZipDso zd = dsos[i];
                    if (shouldExtract(zd.backingEntry, zd.name)) {
                        nrFilteredDsos++;
                    } else {
                        dsos[i] = null;
                    }
                }
                ZipDso[] filteredDsos = new ZipDso[nrFilteredDsos];
                int j = 0;
                for (ZipDso zd2 : dsos) {
                    if (zd2 != null) {
                        filteredDsos[j] = zd2;
                        j++;
                    }
                }
                this.mDsos = filteredDsos;
            }
            return this.mDsos;
        }

        /* access modifiers changed from: protected */
        public boolean shouldExtract(ZipEntry ze, String soName) {
            return true;
        }

        public void close() throws IOException {
            this.mZipFile.close();
        }

        /* access modifiers changed from: protected */
        public final UnpackingSoSource.DsoManifest getDsoManifest() throws IOException {
            return new UnpackingSoSource.DsoManifest(ensureDsos());
        }

        /* access modifiers changed from: protected */
        public final UnpackingSoSource.InputDsoIterator openDsoIterator() throws IOException {
            return new ZipBackedInputDsoIterator();
        }

        private final class ZipBackedInputDsoIterator extends UnpackingSoSource.InputDsoIterator {
            private int mCurrentDso;

            private ZipBackedInputDsoIterator() {
            }

            public boolean hasNext() {
                ZipUnpacker.this.ensureDsos();
                return this.mCurrentDso < ZipUnpacker.this.mDsos.length;
            }

            public UnpackingSoSource.InputDso next() throws IOException {
                ZipUnpacker.this.ensureDsos();
                ZipDso[] access$100 = ZipUnpacker.this.mDsos;
                int i = this.mCurrentDso;
                this.mCurrentDso = i + 1;
                ZipDso zipDso = access$100[i];
                InputStream is = ZipUnpacker.this.mZipFile.getInputStream(zipDso.backingEntry);
                try {
                    UnpackingSoSource.InputDso ret = new UnpackingSoSource.InputDso(zipDso, is);
                    is = null;
                    return ret;
                } finally {
                    if (is != null) {
                        is.close();
                    }
                }
            }
        }
    }

    private static final class ZipDso extends UnpackingSoSource.Dso implements Comparable {
        final int abiScore;
        final ZipEntry backingEntry;

        ZipDso(String name, ZipEntry backingEntry2, int abiScore2) {
            super(name, makePseudoHash(backingEntry2));
            this.backingEntry = backingEntry2;
            this.abiScore = abiScore2;
        }

        private static String makePseudoHash(ZipEntry ze) {
            return String.format("pseudo-zip-hash-1-%s-%s-%s-%s", ze.getName(), Long.valueOf(ze.getSize()), Long.valueOf(ze.getCompressedSize()), Long.valueOf(ze.getCrc()));
        }

        public int compareTo(Object other) {
            return this.name.compareTo(((ZipDso) other).name);
        }
    }
}
