package com.facebook.soloader;

import android.content.Context;
import android.os.Parcel;
import android.util.Log;
import com.facebook.soloader.ExtractFromZipSoSource;
import com.facebook.soloader.UnpackingSoSource;
import java.io.File;
import java.io.IOException;
import java.util.zip.ZipEntry;

public class ApkSoSource extends ExtractFromZipSoSource {
    private static final byte APK_SO_SOURCE_SIGNATURE_VERSION = 2;
    private static final byte LIBS_DIR_DOESNT_EXIST = 1;
    private static final byte LIBS_DIR_DONT_CARE = 0;
    private static final byte LIBS_DIR_SNAPSHOT = 2;
    public static final int PREFER_ANDROID_LIBS_DIRECTORY = 1;
    private static final String TAG = "ApkSoSource";
    /* access modifiers changed from: private */
    public final int mFlags;

    public ApkSoSource(Context context, String name, int flags) {
        this(context, new File(context.getApplicationInfo().sourceDir), name, flags);
    }

    public ApkSoSource(Context context, File apkPath, String name, int flags) {
        super(context, name, apkPath, "^lib/([^/]+)/([^/]+\\.so)$");
        this.mFlags = flags;
    }

    /* access modifiers changed from: protected */
    public UnpackingSoSource.Unpacker makeUnpacker() throws IOException {
        return new ApkUnpacker(this);
    }

    protected class ApkUnpacker extends ExtractFromZipSoSource.ZipUnpacker {
        private final int mFlags;
        private File mLibDir;

        ApkUnpacker(ExtractFromZipSoSource soSource) throws IOException {
            super(soSource);
            this.mLibDir = new File(ApkSoSource.this.mContext.getApplicationInfo().nativeLibraryDir);
            this.mFlags = ApkSoSource.this.mFlags;
        }

        /* access modifiers changed from: protected */
        public boolean shouldExtract(ZipEntry ze, String soName) {
            String msg;
            boolean result;
            String zipPath = ze.getName();
            if (soName.equals(ApkSoSource.this.mCorruptedLib)) {
                ApkSoSource.this.mCorruptedLib = null;
                msg = String.format("allowing consideration of corrupted lib %s", soName);
                result = true;
            } else if ((this.mFlags & 1) == 0) {
                msg = "allowing consideration of " + zipPath + ": self-extraction preferred";
                result = true;
            } else {
                File sysLibFile = new File(this.mLibDir, soName);
                if (!sysLibFile.isFile()) {
                    msg = String.format("allowing considering of %s: %s not in system lib dir", zipPath, soName);
                    result = true;
                } else {
                    long sysLibLength = sysLibFile.length();
                    long apkLibLength = ze.getSize();
                    if (sysLibLength != apkLibLength) {
                        msg = String.format("allowing consideration of %s: sysdir file length is %s, but the file is %s bytes long in the APK", sysLibFile, Long.valueOf(sysLibLength), Long.valueOf(apkLibLength));
                        result = true;
                    } else {
                        msg = "not allowing consideration of " + zipPath + ": deferring to libdir";
                        result = false;
                    }
                }
            }
            Log.d(ApkSoSource.TAG, msg);
            return result;
        }
    }

    /* access modifiers changed from: protected */
    public byte[] getDepsBlock() throws IOException {
        byte[] marshall;
        File apkFile = this.mZipFileName.getCanonicalFile();
        Parcel parcel = Parcel.obtain();
        try {
            parcel.writeByte((byte) 2);
            parcel.writeString(apkFile.getPath());
            parcel.writeLong(apkFile.lastModified());
            parcel.writeInt(SysUtil.getAppVersionCode(this.mContext));
            if ((this.mFlags & 1) == 0) {
                parcel.writeByte((byte) 0);
                marshall = parcel.marshall();
            } else {
                String nativeLibraryDir = this.mContext.getApplicationInfo().nativeLibraryDir;
                if (nativeLibraryDir == null) {
                    parcel.writeByte((byte) 1);
                    marshall = parcel.marshall();
                    parcel.recycle();
                } else {
                    File canonicalFile = new File(nativeLibraryDir).getCanonicalFile();
                    if (!canonicalFile.exists()) {
                        parcel.writeByte((byte) 1);
                        marshall = parcel.marshall();
                        parcel.recycle();
                    } else {
                        parcel.writeByte((byte) 2);
                        parcel.writeString(canonicalFile.getPath());
                        parcel.writeLong(canonicalFile.lastModified());
                        marshall = parcel.marshall();
                        parcel.recycle();
                    }
                }
            }
            return marshall;
        } finally {
            parcel.recycle();
        }
    }
}
