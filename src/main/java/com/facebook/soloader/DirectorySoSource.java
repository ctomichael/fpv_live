package com.facebook.soloader;

import android.os.StrictMode;
import android.util.Log;
import dji.component.accountcenter.IMemberProtocol;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import javax.annotation.Nullable;

public class DirectorySoSource extends SoSource {
    public static final int ON_LD_LIBRARY_PATH = 2;
    public static final int RESOLVE_DEPENDENCIES = 1;
    protected final int flags;
    protected final File soDirectory;

    public DirectorySoSource(File soDirectory2, int flags2) {
        this.soDirectory = soDirectory2;
        this.flags = flags2;
    }

    public int loadLibrary(String soName, int loadFlags, StrictMode.ThreadPolicy threadPolicy) throws IOException {
        return loadLibraryFrom(soName, loadFlags, this.soDirectory, threadPolicy);
    }

    /* access modifiers changed from: protected */
    public int loadLibraryFrom(String soName, int loadFlags, File libDir, StrictMode.ThreadPolicy threadPolicy) throws IOException {
        File soFile = new File(libDir, soName);
        if (!soFile.exists()) {
            Log.d("SoLoader", soName + " not found on " + libDir.getCanonicalPath());
            return 0;
        }
        Log.d("SoLoader", soName + " found on " + libDir.getCanonicalPath());
        if ((loadFlags & 1) == 0 || (this.flags & 2) == 0) {
            if ((this.flags & 1) != 0) {
                loadDependencies(soFile, loadFlags, threadPolicy);
            } else {
                Log.d("SoLoader", "Not resolving dependencies for " + soName);
            }
            try {
                SoLoader.sSoFileLoader.load(soFile.getAbsolutePath(), loadFlags);
                return 1;
            } catch (UnsatisfiedLinkError e) {
                if (e.getMessage().contains("bad ELF magic")) {
                    Log.d("SoLoader", "Corrupted lib file detected");
                    return 3;
                }
                throw e;
            }
        } else {
            Log.d("SoLoader", soName + " loaded implicitly");
            return 2;
        }
    }

    @Nullable
    public String getLibraryPath(String soName) throws IOException {
        File soFile = new File(this.soDirectory, soName);
        if (soFile.exists()) {
            return soFile.getCanonicalPath();
        }
        return null;
    }

    private void loadDependencies(File soFile, int loadFlags, StrictMode.ThreadPolicy threadPolicy) throws IOException {
        String[] dependencies = getDependencies(soFile);
        Log.d("SoLoader", "Loading lib dependencies: " + Arrays.toString(dependencies));
        for (String dependency : dependencies) {
            if (!dependency.startsWith(IMemberProtocol.PARAM_SEPERATOR)) {
                SoLoader.loadLibraryBySoName(dependency, loadFlags | 1, threadPolicy);
            }
        }
    }

    private static String[] getDependencies(File soFile) throws IOException {
        if (SoLoader.SYSTRACE_LIBRARY_LOADING) {
            Api18TraceUtils.beginTraceSection("SoLoader.getElfDependencies[" + soFile.getName() + IMemberProtocol.STRING_SEPERATOR_RIGHT);
        }
        try {
            return MinElf.extract_DT_NEEDED(soFile);
        } finally {
            if (SoLoader.SYSTRACE_LIBRARY_LOADING) {
                Api18TraceUtils.endSection();
            }
        }
    }

    @Nullable
    public File unpackLibrary(String soName) throws IOException {
        File soFile = new File(this.soDirectory, soName);
        if (soFile.exists()) {
            return soFile;
        }
        return null;
    }

    public void addToLdLibraryPath(Collection<String> paths) {
        paths.add(this.soDirectory.getAbsolutePath());
    }

    public String toString() {
        String path;
        try {
            path = String.valueOf(this.soDirectory.getCanonicalPath());
        } catch (IOException e) {
            path = this.soDirectory.getName();
        }
        return getClass().getName() + "[root = " + path + " flags = " + this.flags + ']';
    }
}
