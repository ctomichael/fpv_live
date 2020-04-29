package kotlin.io;

import com.drew.metadata.exif.makernotes.PanasonicMakernoteDirectory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000<\n\u0000\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\f\u001a(\u0010\t\u001a\u00020\u00022\b\b\u0002\u0010\n\u001a\u00020\u00012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u00012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u0002\u001a(\u0010\r\u001a\u00020\u00022\b\b\u0002\u0010\n\u001a\u00020\u00012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u00012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u0002\u001a8\u0010\u000e\u001a\u00020\u000f*\u00020\u00022\u0006\u0010\u0010\u001a\u00020\u00022\b\b\u0002\u0010\u0011\u001a\u00020\u000f2\u001a\b\u0002\u0010\u0012\u001a\u0014\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00150\u0013\u001a&\u0010\u0016\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0010\u001a\u00020\u00022\b\b\u0002\u0010\u0011\u001a\u00020\u000f2\b\b\u0002\u0010\u0017\u001a\u00020\u0018\u001a\n\u0010\u0019\u001a\u00020\u000f*\u00020\u0002\u001a\u0012\u0010\u001a\u001a\u00020\u000f*\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u0002\u001a\u0012\u0010\u001a\u001a\u00020\u000f*\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u0001\u001a\n\u0010\u001c\u001a\u00020\u0002*\u00020\u0002\u001a\u001d\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00020\u001d*\b\u0012\u0004\u0012\u00020\u00020\u001dH\u0002¢\u0006\u0002\b\u001e\u001a\u0011\u0010\u001c\u001a\u00020\u001f*\u00020\u001fH\u0002¢\u0006\u0002\b\u001e\u001a\u0012\u0010 \u001a\u00020\u0002*\u00020\u00022\u0006\u0010!\u001a\u00020\u0002\u001a\u0014\u0010\"\u001a\u0004\u0018\u00010\u0002*\u00020\u00022\u0006\u0010!\u001a\u00020\u0002\u001a\u0012\u0010#\u001a\u00020\u0002*\u00020\u00022\u0006\u0010!\u001a\u00020\u0002\u001a\u0012\u0010$\u001a\u00020\u0002*\u00020\u00022\u0006\u0010%\u001a\u00020\u0002\u001a\u0012\u0010$\u001a\u00020\u0002*\u00020\u00022\u0006\u0010%\u001a\u00020\u0001\u001a\u0012\u0010&\u001a\u00020\u0002*\u00020\u00022\u0006\u0010%\u001a\u00020\u0002\u001a\u0012\u0010&\u001a\u00020\u0002*\u00020\u00022\u0006\u0010%\u001a\u00020\u0001\u001a\u0012\u0010'\u001a\u00020\u000f*\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u0002\u001a\u0012\u0010'\u001a\u00020\u000f*\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u0001\u001a\u0012\u0010(\u001a\u00020\u0001*\u00020\u00022\u0006\u0010!\u001a\u00020\u0002\u001a\u001b\u0010)\u001a\u0004\u0018\u00010\u0001*\u00020\u00022\u0006\u0010!\u001a\u00020\u0002H\u0002¢\u0006\u0002\b*\"\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00028F¢\u0006\u0006\u001a\u0004\b\u0003\u0010\u0004\"\u0015\u0010\u0005\u001a\u00020\u0001*\u00020\u00028F¢\u0006\u0006\u001a\u0004\b\u0006\u0010\u0004\"\u0015\u0010\u0007\u001a\u00020\u0001*\u00020\u00028F¢\u0006\u0006\u001a\u0004\b\b\u0010\u0004¨\u0006+"}, d2 = {"extension", "", "Ljava/io/File;", "getExtension", "(Ljava/io/File;)Ljava/lang/String;", "invariantSeparatorsPath", "getInvariantSeparatorsPath", "nameWithoutExtension", "getNameWithoutExtension", "createTempDir", "prefix", "suffix", "directory", "createTempFile", "copyRecursively", "", "target", "overwrite", "onError", "Lkotlin/Function2;", "Ljava/io/IOException;", "Lkotlin/io/OnErrorAction;", "copyTo", "bufferSize", "", "deleteRecursively", "endsWith", "other", "normalize", "", "normalize$FilesKt__UtilsKt", "Lkotlin/io/FilePathComponents;", "relativeTo", "base", "relativeToOrNull", "relativeToOrSelf", "resolve", "relative", "resolveSibling", "startsWith", "toRelativeString", "toRelativeStringOrNull", "toRelativeStringOrNull$FilesKt__UtilsKt", "kotlin-stdlib"}, k = 5, mv = {1, 1, 15}, xi = 1, xs = "kotlin/io/FilesKt")
/* compiled from: Utils.kt */
class FilesKt__UtilsKt extends FilesKt__FileTreeWalkKt {
    public static /* synthetic */ File createTempDir$default(String str, String str2, File file, int i, Object obj) {
        if ((i & 1) != 0) {
            str = "tmp";
        }
        return FilesKt.createTempDir(str, (i & 2) != 0 ? null : str2, (i & 4) != 0 ? null : file);
    }

    @NotNull
    public static final File createTempDir(@NotNull String prefix, @Nullable String suffix, @Nullable File directory) {
        Intrinsics.checkParameterIsNotNull(prefix, "prefix");
        File dir = File.createTempFile(prefix, suffix, directory);
        dir.delete();
        if (dir.mkdir()) {
            Intrinsics.checkExpressionValueIsNotNull(dir, "dir");
            return dir;
        }
        throw new IOException("Unable to create temporary directory " + dir + '.');
    }

    public static /* synthetic */ File createTempFile$default(String str, String str2, File file, int i, Object obj) {
        if ((i & 1) != 0) {
            str = "tmp";
        }
        return FilesKt.createTempFile(str, (i & 2) != 0 ? null : str2, (i & 4) != 0 ? null : file);
    }

    @NotNull
    public static final File createTempFile(@NotNull String prefix, @Nullable String suffix, @Nullable File directory) {
        Intrinsics.checkParameterIsNotNull(prefix, "prefix");
        File createTempFile = File.createTempFile(prefix, suffix, directory);
        Intrinsics.checkExpressionValueIsNotNull(createTempFile, "File.createTempFile(prefix, suffix, directory)");
        return createTempFile;
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: kotlin.text.StringsKt__StringsKt.substringAfterLast(java.lang.String, char, java.lang.String):java.lang.String
     arg types: [java.lang.String, int, java.lang.String]
     candidates:
      kotlin.text.StringsKt__StringsKt.substringAfterLast(java.lang.String, java.lang.String, java.lang.String):java.lang.String
      kotlin.text.StringsKt__StringsKt.substringAfterLast(java.lang.String, char, java.lang.String):java.lang.String */
    @NotNull
    public static final String getExtension(@NotNull File $this$extension) {
        Intrinsics.checkParameterIsNotNull($this$extension, "$this$extension");
        String name = $this$extension.getName();
        Intrinsics.checkExpressionValueIsNotNull(name, "name");
        return StringsKt.substringAfterLast(name, '.', "");
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: kotlin.text.StringsKt__StringsJVMKt.replace$default(java.lang.String, char, char, boolean, int, java.lang.Object):java.lang.String
     arg types: [java.lang.String, char, int, int, int, ?[OBJECT, ARRAY]]
     candidates:
      kotlin.text.StringsKt__StringsJVMKt.replace$default(java.lang.String, java.lang.String, java.lang.String, boolean, int, java.lang.Object):java.lang.String
      kotlin.text.StringsKt__StringsJVMKt.replace$default(java.lang.String, char, char, boolean, int, java.lang.Object):java.lang.String */
    @NotNull
    public static final String getInvariantSeparatorsPath(@NotNull File $this$invariantSeparatorsPath) {
        Intrinsics.checkParameterIsNotNull($this$invariantSeparatorsPath, "$this$invariantSeparatorsPath");
        if (File.separatorChar != '/') {
            String path = $this$invariantSeparatorsPath.getPath();
            Intrinsics.checkExpressionValueIsNotNull(path, "path");
            return StringsKt.replace$default(path, File.separatorChar, '/', false, 4, (Object) null);
        }
        String path2 = $this$invariantSeparatorsPath.getPath();
        Intrinsics.checkExpressionValueIsNotNull(path2, "path");
        return path2;
    }

    @NotNull
    public static final String getNameWithoutExtension(@NotNull File $this$nameWithoutExtension) {
        Intrinsics.checkParameterIsNotNull($this$nameWithoutExtension, "$this$nameWithoutExtension");
        String name = $this$nameWithoutExtension.getName();
        Intrinsics.checkExpressionValueIsNotNull(name, "name");
        return StringsKt.substringBeforeLast$default(name, ".", (String) null, 2, (Object) null);
    }

    @NotNull
    public static final String toRelativeString(@NotNull File $this$toRelativeString, @NotNull File base) {
        Intrinsics.checkParameterIsNotNull($this$toRelativeString, "$this$toRelativeString");
        Intrinsics.checkParameterIsNotNull(base, "base");
        String relativeStringOrNull$FilesKt__UtilsKt = toRelativeStringOrNull$FilesKt__UtilsKt($this$toRelativeString, base);
        if (relativeStringOrNull$FilesKt__UtilsKt != null) {
            return relativeStringOrNull$FilesKt__UtilsKt;
        }
        throw new IllegalArgumentException("this and base files have different roots: " + $this$toRelativeString + " and " + base + '.');
    }

    @NotNull
    public static final File relativeTo(@NotNull File $this$relativeTo, @NotNull File base) {
        Intrinsics.checkParameterIsNotNull($this$relativeTo, "$this$relativeTo");
        Intrinsics.checkParameterIsNotNull(base, "base");
        return new File(FilesKt.toRelativeString($this$relativeTo, base));
    }

    @NotNull
    public static final File relativeToOrSelf(@NotNull File $this$relativeToOrSelf, @NotNull File base) {
        Intrinsics.checkParameterIsNotNull($this$relativeToOrSelf, "$this$relativeToOrSelf");
        Intrinsics.checkParameterIsNotNull(base, "base");
        String p1 = toRelativeStringOrNull$FilesKt__UtilsKt($this$relativeToOrSelf, base);
        return p1 != null ? new File(p1) : $this$relativeToOrSelf;
    }

    @Nullable
    public static final File relativeToOrNull(@NotNull File $this$relativeToOrNull, @NotNull File base) {
        Intrinsics.checkParameterIsNotNull($this$relativeToOrNull, "$this$relativeToOrNull");
        Intrinsics.checkParameterIsNotNull(base, "base");
        String p1 = toRelativeStringOrNull$FilesKt__UtilsKt($this$relativeToOrNull, base);
        if (p1 != null) {
            return new File(p1);
        }
        return null;
    }

    private static final String toRelativeStringOrNull$FilesKt__UtilsKt(@NotNull File $this$toRelativeStringOrNull, File base) {
        FilePathComponents thisComponents = normalize$FilesKt__UtilsKt(FilesKt.toComponents($this$toRelativeStringOrNull));
        FilePathComponents baseComponents = normalize$FilesKt__UtilsKt(FilesKt.toComponents(base));
        if (!Intrinsics.areEqual(thisComponents.getRoot(), baseComponents.getRoot())) {
            return null;
        }
        int baseCount = baseComponents.getSize();
        int thisCount = thisComponents.getSize();
        int i = 0;
        int maxSameCount = Math.min(thisCount, baseCount);
        while (i < maxSameCount && Intrinsics.areEqual(thisComponents.getSegments().get(i), baseComponents.getSegments().get(i))) {
            i++;
        }
        int sameCount = i;
        StringBuilder res = new StringBuilder();
        int i2 = baseCount - 1;
        if (i2 >= sameCount) {
            while (!Intrinsics.areEqual(baseComponents.getSegments().get(i2).getName(), "..")) {
                res.append("..");
                if (i2 != sameCount) {
                    res.append(File.separatorChar);
                }
                if (i2 != sameCount) {
                    i2--;
                }
            }
            return null;
        }
        if (sameCount < thisCount) {
            if (sameCount < baseCount) {
                res.append(File.separatorChar);
            }
            String str = File.separator;
            Intrinsics.checkExpressionValueIsNotNull(str, "File.separator");
            CollectionsKt.joinTo$default(CollectionsKt.drop(thisComponents.getSegments(), sameCount), res, str, null, null, 0, null, null, PanasonicMakernoteDirectory.TAG_CLEAR_RETOUCH, null);
        }
        return res.toString();
    }

    public static /* synthetic */ File copyTo$default(File file, File file2, boolean z, int i, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            z = false;
        }
        if ((i2 & 4) != 0) {
            i = 8192;
        }
        return FilesKt.copyTo(file, file2, z, i);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:35:0x008f, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:?, code lost:
        kotlin.io.CloseableKt.closeFinally(r2, r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x0093, code lost:
        throw r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x0096, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x0097, code lost:
        r4 = r3;
        r5 = r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x0099, code lost:
        kotlin.io.CloseableKt.closeFinally(r1, r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x009c, code lost:
        throw r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x009d, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x009e, code lost:
        r4 = r2;
        r5 = r3;
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    @org.jetbrains.annotations.NotNull
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final java.io.File copyTo(@org.jetbrains.annotations.NotNull java.io.File r12, @org.jetbrains.annotations.NotNull java.io.File r13, boolean r14, int r15) {
        /*
            r11 = 1
            r3 = 0
            java.lang.String r1 = "$this$copyTo"
            kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r12, r1)
            java.lang.String r1 = "target"
            kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r13, r1)
            boolean r1 = r12.exists()
            if (r1 != 0) goto L_0x0022
            kotlin.io.NoSuchFileException r1 = new kotlin.io.NoSuchFileException
            java.lang.String r4 = "The source file doesn't exist."
            r5 = 2
            r2 = r12
            r6 = r3
            r1.<init>(r2, r3, r4, r5, r6)
            java.lang.Throwable r1 = (java.lang.Throwable) r1
            throw r1
        L_0x0022:
            boolean r1 = r13.exists()
            if (r1 == 0) goto L_0x003f
            if (r14 != 0) goto L_0x0037
        L_0x002a:
            if (r11 == 0) goto L_0x003f
            kotlin.io.FileAlreadyExistsException r1 = new kotlin.io.FileAlreadyExistsException
            java.lang.String r2 = "The destination file already exists."
            r1.<init>(r12, r13, r2)
            java.lang.Throwable r1 = (java.lang.Throwable) r1
            throw r1
        L_0x0037:
            boolean r1 = r13.delete()
            if (r1 == 0) goto L_0x002a
            r11 = 0
            goto L_0x002a
        L_0x003f:
            boolean r1 = r12.isDirectory()
            if (r1 == 0) goto L_0x0056
            boolean r1 = r13.mkdirs()
            if (r1 != 0) goto L_0x008c
            kotlin.io.FileSystemException r1 = new kotlin.io.FileSystemException
            java.lang.String r2 = "Failed to create target directory."
            r1.<init>(r12, r13, r2)
            java.lang.Throwable r1 = (java.lang.Throwable) r1
            throw r1
        L_0x0056:
            java.io.File r1 = r13.getParentFile()
            if (r1 == 0) goto L_0x005f
            r1.mkdirs()
        L_0x005f:
            java.io.FileInputStream r1 = new java.io.FileInputStream
            r1.<init>(r12)
            java.io.Closeable r1 = (java.io.Closeable) r1
            java.lang.Throwable r3 = (java.lang.Throwable) r3
            r0 = r1
            java.io.FileInputStream r0 = (java.io.FileInputStream) r0     // Catch:{ Throwable -> 0x0094, all -> 0x009d }
            r9 = r0
            r7 = 0
            java.io.FileOutputStream r2 = new java.io.FileOutputStream     // Catch:{ Throwable -> 0x0094, all -> 0x009d }
            r2.<init>(r13)     // Catch:{ Throwable -> 0x0094, all -> 0x009d }
            java.io.Closeable r2 = (java.io.Closeable) r2     // Catch:{ Throwable -> 0x0094, all -> 0x009d }
            r4 = 0
            java.lang.Throwable r4 = (java.lang.Throwable) r4     // Catch:{ Throwable -> 0x0094, all -> 0x009d }
            r0 = r2
            java.io.FileOutputStream r0 = (java.io.FileOutputStream) r0     // Catch:{ Throwable -> 0x008d }
            r10 = r0
            r8 = 0
            java.io.InputStream r9 = (java.io.InputStream) r9     // Catch:{ Throwable -> 0x008d }
            java.io.OutputStream r10 = (java.io.OutputStream) r10     // Catch:{ Throwable -> 0x008d }
            kotlin.io.ByteStreamsKt.copyTo(r9, r10, r15)     // Catch:{ Throwable -> 0x008d }
            kotlin.io.CloseableKt.closeFinally(r2, r4)     // Catch:{ Throwable -> 0x0094, all -> 0x009d }
            kotlin.io.CloseableKt.closeFinally(r1, r3)
        L_0x008c:
            return r13
        L_0x008d:
            r4 = move-exception
            throw r4     // Catch:{ all -> 0x008f }
        L_0x008f:
            r5 = move-exception
            kotlin.io.CloseableKt.closeFinally(r2, r4)     // Catch:{ Throwable -> 0x0094, all -> 0x009d }
            throw r5     // Catch:{ Throwable -> 0x0094, all -> 0x009d }
        L_0x0094:
            r2 = move-exception
            throw r2     // Catch:{ all -> 0x0096 }
        L_0x0096:
            r3 = move-exception
            r4 = r3
            r5 = r2
        L_0x0099:
            kotlin.io.CloseableKt.closeFinally(r1, r5)
            throw r4
        L_0x009d:
            r2 = move-exception
            r4 = r2
            r5 = r3
            goto L_0x0099
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.io.FilesKt__UtilsKt.copyTo(java.io.File, java.io.File, boolean, int):java.io.File");
    }

    public static /* synthetic */ boolean copyRecursively$default(File file, File file2, boolean z, Function2 function2, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return FilesKt.copyRecursively(file, file2, z, (i & 4) != 0 ? FilesKt__UtilsKt$copyRecursively$1.INSTANCE : function2);
    }

    public static final boolean copyRecursively(@NotNull File $this$copyRecursively, @NotNull File target, boolean overwrite, @NotNull Function2<? super File, ? super IOException, ? extends OnErrorAction> onError) {
        boolean stillExists;
        Intrinsics.checkParameterIsNotNull($this$copyRecursively, "$this$copyRecursively");
        Intrinsics.checkParameterIsNotNull(target, "target");
        Intrinsics.checkParameterIsNotNull(onError, "onError");
        if (!$this$copyRecursively.exists()) {
            if (((OnErrorAction) onError.invoke($this$copyRecursively, new NoSuchFileException($this$copyRecursively, null, "The source file doesn't exist.", 2, null))) != OnErrorAction.TERMINATE) {
                return true;
            }
            return false;
        }
        try {
            Iterator<File> it2 = FilesKt.walkTopDown($this$copyRecursively).onFail(new FilesKt__UtilsKt$copyRecursively$2(onError)).iterator();
            while (it2.hasNext()) {
                File src = it2.next();
                if (!src.exists()) {
                    if (((OnErrorAction) onError.invoke(src, new NoSuchFileException(src, null, "The source file doesn't exist.", 2, null))) == OnErrorAction.TERMINATE) {
                        return false;
                    }
                } else {
                    File dstFile = new File(target, FilesKt.toRelativeString(src, $this$copyRecursively));
                    if (dstFile.exists() && (!src.isDirectory() || !dstFile.isDirectory())) {
                        if (!overwrite) {
                            stillExists = true;
                        } else if (dstFile.isDirectory()) {
                            stillExists = !FilesKt.deleteRecursively(dstFile);
                        } else {
                            stillExists = !dstFile.delete();
                        }
                        if (stillExists) {
                            if (((OnErrorAction) onError.invoke(dstFile, new FileAlreadyExistsException(src, dstFile, "The destination file already exists."))) == OnErrorAction.TERMINATE) {
                                return false;
                            }
                        }
                    }
                    if (src.isDirectory()) {
                        dstFile.mkdirs();
                    } else if (FilesKt.copyTo$default(src, dstFile, overwrite, 0, 4, null).length() != src.length()) {
                        if (((OnErrorAction) onError.invoke(src, new IOException("Source file wasn't copied completely, length of destination file differs."))) == OnErrorAction.TERMINATE) {
                            return false;
                        }
                    } else {
                        continue;
                    }
                }
            }
            return true;
        } catch (TerminateException e) {
            return false;
        }
    }

    public static final boolean deleteRecursively(@NotNull File $this$deleteRecursively) {
        Intrinsics.checkParameterIsNotNull($this$deleteRecursively, "$this$deleteRecursively");
        boolean accumulator$iv = true;
        for (File file : FilesKt.walkBottomUp($this$deleteRecursively)) {
            accumulator$iv = (file.delete() || !file.exists()) && accumulator$iv;
        }
        return accumulator$iv;
    }

    public static final boolean startsWith(@NotNull File $this$startsWith, @NotNull File other) {
        Intrinsics.checkParameterIsNotNull($this$startsWith, "$this$startsWith");
        Intrinsics.checkParameterIsNotNull(other, "other");
        FilePathComponents components = FilesKt.toComponents($this$startsWith);
        FilePathComponents otherComponents = FilesKt.toComponents(other);
        if (!(!Intrinsics.areEqual(components.getRoot(), otherComponents.getRoot())) && components.getSize() >= otherComponents.getSize()) {
            return components.getSegments().subList(0, otherComponents.getSize()).equals(otherComponents.getSegments());
        }
        return false;
    }

    public static final boolean startsWith(@NotNull File $this$startsWith, @NotNull String other) {
        Intrinsics.checkParameterIsNotNull($this$startsWith, "$this$startsWith");
        Intrinsics.checkParameterIsNotNull(other, "other");
        return FilesKt.startsWith($this$startsWith, new File(other));
    }

    public static final boolean endsWith(@NotNull File $this$endsWith, @NotNull File other) {
        Intrinsics.checkParameterIsNotNull($this$endsWith, "$this$endsWith");
        Intrinsics.checkParameterIsNotNull(other, "other");
        FilePathComponents components = FilesKt.toComponents($this$endsWith);
        FilePathComponents otherComponents = FilesKt.toComponents(other);
        if (otherComponents.isRooted()) {
            return Intrinsics.areEqual($this$endsWith, other);
        }
        int shift = components.getSize() - otherComponents.getSize();
        if (shift < 0) {
            return false;
        }
        return components.getSegments().subList(shift, components.getSize()).equals(otherComponents.getSegments());
    }

    public static final boolean endsWith(@NotNull File $this$endsWith, @NotNull String other) {
        Intrinsics.checkParameterIsNotNull($this$endsWith, "$this$endsWith");
        Intrinsics.checkParameterIsNotNull(other, "other");
        return FilesKt.endsWith($this$endsWith, new File(other));
    }

    @NotNull
    public static final File normalize(@NotNull File $this$normalize) {
        Intrinsics.checkParameterIsNotNull($this$normalize, "$this$normalize");
        FilePathComponents $this$with = FilesKt.toComponents($this$normalize);
        File root = $this$with.getRoot();
        String str = File.separator;
        Intrinsics.checkExpressionValueIsNotNull(str, "File.separator");
        return FilesKt.resolve(root, CollectionsKt.joinToString$default(normalize$FilesKt__UtilsKt($this$with.getSegments()), str, null, null, 0, null, null, 62, null));
    }

    private static final FilePathComponents normalize$FilesKt__UtilsKt(@NotNull FilePathComponents $this$normalize) {
        return new FilePathComponents($this$normalize.getRoot(), normalize$FilesKt__UtilsKt($this$normalize.getSegments()));
    }

    private static final List<File> normalize$FilesKt__UtilsKt(@NotNull List<? extends File> $this$normalize) {
        List list = new ArrayList($this$normalize.size());
        for (File file : $this$normalize) {
            String name = file.getName();
            if (name != null) {
                switch (name.hashCode()) {
                    case 46:
                        if (name.equals(".")) {
                            continue;
                        }
                        break;
                    case 1472:
                        if (name.equals("..")) {
                            if (list.isEmpty() || !(!Intrinsics.areEqual(((File) CollectionsKt.last(list)).getName(), ".."))) {
                                list.add(file);
                            } else {
                                list.remove(list.size() - 1);
                                continue;
                            }
                        }
                        break;
                }
            }
            list.add(file);
        }
        return list;
    }

    @NotNull
    public static final File resolve(@NotNull File $this$resolve, @NotNull File relative) {
        Intrinsics.checkParameterIsNotNull($this$resolve, "$this$resolve");
        Intrinsics.checkParameterIsNotNull(relative, "relative");
        if (FilesKt.isRooted(relative)) {
            return relative;
        }
        String baseName = $this$resolve.toString();
        Intrinsics.checkExpressionValueIsNotNull(baseName, "this.toString()");
        return ((baseName.length() == 0) || StringsKt.endsWith$default(baseName, File.separatorChar, false, 2, null)) ? new File(baseName + relative) : new File(baseName + File.separatorChar + relative);
    }

    @NotNull
    public static final File resolve(@NotNull File $this$resolve, @NotNull String relative) {
        Intrinsics.checkParameterIsNotNull($this$resolve, "$this$resolve");
        Intrinsics.checkParameterIsNotNull(relative, "relative");
        return FilesKt.resolve($this$resolve, new File(relative));
    }

    @NotNull
    public static final File resolveSibling(@NotNull File $this$resolveSibling, @NotNull File relative) {
        Intrinsics.checkParameterIsNotNull($this$resolveSibling, "$this$resolveSibling");
        Intrinsics.checkParameterIsNotNull(relative, "relative");
        FilePathComponents components = FilesKt.toComponents($this$resolveSibling);
        return FilesKt.resolve(FilesKt.resolve(components.getRoot(), components.getSize() == 0 ? new File("..") : components.subPath(0, components.getSize() - 1)), relative);
    }

    @NotNull
    public static final File resolveSibling(@NotNull File $this$resolveSibling, @NotNull String relative) {
        Intrinsics.checkParameterIsNotNull($this$resolveSibling, "$this$resolveSibling");
        Intrinsics.checkParameterIsNotNull(relative, "relative");
        return FilesKt.resolveSibling($this$resolveSibling, new File(relative));
    }
}
