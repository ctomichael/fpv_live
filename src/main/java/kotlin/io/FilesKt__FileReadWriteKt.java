package kotlin.io;

import com.mapbox.mapboxsdk.style.layers.Property;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.internal.InlineOnly;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;
import org.bouncycastle.i18n.TextBundle;
import org.jetbrains.annotations.NotNull;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000z\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\u001a\u0012\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004\u001a\u001c\u0010\u0005\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\t\u001a!\u0010\n\u001a\u00020\u000b*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\f\u001a\u00020\rH\b\u001a!\u0010\u000e\u001a\u00020\u000f*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\f\u001a\u00020\rH\b\u001aB\u0010\u0010\u001a\u00020\u0001*\u00020\u000226\u0010\u0011\u001a2\u0012\u0013\u0012\u00110\u0004¢\u0006\f\b\u0013\u0012\b\b\u0014\u0012\u0004\b\b(\u0015\u0012\u0013\u0012\u00110\r¢\u0006\f\b\u0013\u0012\b\b\u0014\u0012\u0004\b\b(\u0016\u0012\u0004\u0012\u00020\u00010\u0012\u001aJ\u0010\u0010\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0017\u001a\u00020\r26\u0010\u0011\u001a2\u0012\u0013\u0012\u00110\u0004¢\u0006\f\b\u0013\u0012\b\b\u0014\u0012\u0004\b\b(\u0015\u0012\u0013\u0012\u00110\r¢\u0006\f\b\u0013\u0012\b\b\u0014\u0012\u0004\b\b(\u0016\u0012\u0004\u0012\u00020\u00010\u0012\u001a7\u0010\u0018\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\t2!\u0010\u0011\u001a\u001d\u0012\u0013\u0012\u00110\u0007¢\u0006\f\b\u0013\u0012\b\b\u0014\u0012\u0004\b\b(\u001a\u0012\u0004\u0012\u00020\u00010\u0019\u001a\r\u0010\u001b\u001a\u00020\u001c*\u00020\u0002H\b\u001a\r\u0010\u001d\u001a\u00020\u001e*\u00020\u0002H\b\u001a\u0017\u0010\u001f\u001a\u00020 *\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\tH\b\u001a\n\u0010!\u001a\u00020\u0004*\u00020\u0002\u001a\u001a\u0010\"\u001a\b\u0012\u0004\u0012\u00020\u00070#*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\t\u001a\u0014\u0010$\u001a\u00020\u0007*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\t\u001a\u0017\u0010%\u001a\u00020&*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\tH\b\u001a?\u0010'\u001a\u0002H(\"\u0004\b\u0000\u0010(*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\t2\u0018\u0010)\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00070*\u0012\u0004\u0012\u0002H(0\u0019H\bø\u0001\u0000¢\u0006\u0002\u0010,\u001a\u0012\u0010-\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004\u001a\u001c\u0010.\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\t\u001a\u0017\u0010/\u001a\u000200*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\tH\b\u0002\b\n\u0006\b\u0011(+0\u0001¨\u00061"}, d2 = {"appendBytes", "", "Ljava/io/File;", "array", "", "appendText", TextBundle.TEXT_ENTRY, "", "charset", "Ljava/nio/charset/Charset;", "bufferedReader", "Ljava/io/BufferedReader;", "bufferSize", "", "bufferedWriter", "Ljava/io/BufferedWriter;", "forEachBlock", "action", "Lkotlin/Function2;", "Lkotlin/ParameterName;", "name", "buffer", "bytesRead", "blockSize", "forEachLine", "Lkotlin/Function1;", Property.SYMBOL_PLACEMENT_LINE, "inputStream", "Ljava/io/FileInputStream;", "outputStream", "Ljava/io/FileOutputStream;", "printWriter", "Ljava/io/PrintWriter;", "readBytes", "readLines", "", "readText", "reader", "Ljava/io/InputStreamReader;", "useLines", "T", "block", "Lkotlin/sequences/Sequence;", "Requires newer compiler version to be inlined correctly.", "(Ljava/io/File;Ljava/nio/charset/Charset;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "writeBytes", "writeText", "writer", "Ljava/io/OutputStreamWriter;", "kotlin-stdlib"}, k = 5, mv = {1, 1, 15}, xi = 1, xs = "kotlin/io/FilesKt")
/* compiled from: FileReadWrite.kt */
class FilesKt__FileReadWriteKt extends FilesKt__FilePathComponentsKt {
    static /* synthetic */ InputStreamReader reader$default(File $this$reader, Charset charset, int i, Object obj) {
        if ((i & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        return new InputStreamReader(new FileInputStream($this$reader), charset);
    }

    @InlineOnly
    private static final InputStreamReader reader(@NotNull File $this$reader, Charset charset) {
        return new InputStreamReader(new FileInputStream($this$reader), charset);
    }

    static /* synthetic */ BufferedReader bufferedReader$default(File $this$bufferedReader, Charset charset, int bufferSize, int i, Object obj) {
        if ((i & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        if ((i & 2) != 0) {
            bufferSize = 8192;
        }
        Reader inputStreamReader = new InputStreamReader(new FileInputStream($this$bufferedReader), charset);
        return inputStreamReader instanceof BufferedReader ? (BufferedReader) inputStreamReader : new BufferedReader(inputStreamReader, bufferSize);
    }

    @InlineOnly
    private static final BufferedReader bufferedReader(@NotNull File $this$bufferedReader, Charset charset, int bufferSize) {
        Reader inputStreamReader = new InputStreamReader(new FileInputStream($this$bufferedReader), charset);
        return inputStreamReader instanceof BufferedReader ? (BufferedReader) inputStreamReader : new BufferedReader(inputStreamReader, bufferSize);
    }

    static /* synthetic */ OutputStreamWriter writer$default(File $this$writer, Charset charset, int i, Object obj) {
        if ((i & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        return new OutputStreamWriter(new FileOutputStream($this$writer), charset);
    }

    @InlineOnly
    private static final OutputStreamWriter writer(@NotNull File $this$writer, Charset charset) {
        return new OutputStreamWriter(new FileOutputStream($this$writer), charset);
    }

    static /* synthetic */ BufferedWriter bufferedWriter$default(File $this$bufferedWriter, Charset charset, int bufferSize, int i, Object obj) {
        if ((i & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        if ((i & 2) != 0) {
            bufferSize = 8192;
        }
        Writer outputStreamWriter = new OutputStreamWriter(new FileOutputStream($this$bufferedWriter), charset);
        return outputStreamWriter instanceof BufferedWriter ? (BufferedWriter) outputStreamWriter : new BufferedWriter(outputStreamWriter, bufferSize);
    }

    @InlineOnly
    private static final BufferedWriter bufferedWriter(@NotNull File $this$bufferedWriter, Charset charset, int bufferSize) {
        Writer outputStreamWriter = new OutputStreamWriter(new FileOutputStream($this$bufferedWriter), charset);
        return outputStreamWriter instanceof BufferedWriter ? (BufferedWriter) outputStreamWriter : new BufferedWriter(outputStreamWriter, bufferSize);
    }

    static /* synthetic */ PrintWriter printWriter$default(File $this$printWriter, Charset charset, int i, Object obj) {
        if ((i & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        Writer outputStreamWriter = new OutputStreamWriter(new FileOutputStream($this$printWriter), charset);
        return new PrintWriter(outputStreamWriter instanceof BufferedWriter ? (BufferedWriter) outputStreamWriter : new BufferedWriter(outputStreamWriter, 8192));
    }

    @InlineOnly
    private static final PrintWriter printWriter(@NotNull File $this$printWriter, Charset charset) {
        Writer outputStreamWriter = new OutputStreamWriter(new FileOutputStream($this$printWriter), charset);
        return new PrintWriter(outputStreamWriter instanceof BufferedWriter ? (BufferedWriter) outputStreamWriter : new BufferedWriter(outputStreamWriter, 8192));
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x005b, code lost:
        kotlin.io.CloseableKt.closeFinally(r11, r12);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x005e, code lost:
        throw r13;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x005a, code lost:
        r13 = move-exception;
     */
    @org.jetbrains.annotations.NotNull
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final byte[] readBytes(@org.jetbrains.annotations.NotNull java.io.File r16) {
        /*
            java.lang.String r11 = "$this$readBytes"
            r0 = r16
            kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r0, r11)
            java.io.FileInputStream r11 = new java.io.FileInputStream
            r0 = r16
            r11.<init>(r0)
            java.io.Closeable r11 = (java.io.Closeable) r11
            r12 = 0
            java.lang.Throwable r12 = (java.lang.Throwable) r12
            r0 = r11
            java.io.FileInputStream r0 = (java.io.FileInputStream) r0     // Catch:{ Throwable -> 0x0058 }
            r4 = r0
            r3 = 0
            r5 = 0
            long r6 = r16.length()     // Catch:{ Throwable -> 0x0058 }
            r2 = 0
            r13 = 2147483647(0x7fffffff, float:NaN)
            long r14 = (long) r13     // Catch:{ Throwable -> 0x0058 }
            int r13 = (r6 > r14 ? 1 : (r6 == r14 ? 0 : -1))
            if (r13 <= 0) goto L_0x005f
            java.lang.OutOfMemoryError r13 = new java.lang.OutOfMemoryError     // Catch:{ Throwable -> 0x0058 }
            java.lang.StringBuilder r14 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0058 }
            r14.<init>()     // Catch:{ Throwable -> 0x0058 }
            java.lang.String r15 = "File "
            java.lang.StringBuilder r14 = r14.append(r15)     // Catch:{ Throwable -> 0x0058 }
            r0 = r16
            java.lang.StringBuilder r14 = r14.append(r0)     // Catch:{ Throwable -> 0x0058 }
            java.lang.String r15 = " is too big ("
            java.lang.StringBuilder r14 = r14.append(r15)     // Catch:{ Throwable -> 0x0058 }
            java.lang.StringBuilder r14 = r14.append(r6)     // Catch:{ Throwable -> 0x0058 }
            java.lang.String r15 = " bytes) to fit in memory."
            java.lang.StringBuilder r14 = r14.append(r15)     // Catch:{ Throwable -> 0x0058 }
            java.lang.String r14 = r14.toString()     // Catch:{ Throwable -> 0x0058 }
            r13.<init>(r14)     // Catch:{ Throwable -> 0x0058 }
            java.lang.Throwable r13 = (java.lang.Throwable) r13     // Catch:{ Throwable -> 0x0058 }
            throw r13     // Catch:{ Throwable -> 0x0058 }
        L_0x0058:
            r12 = move-exception
            throw r12     // Catch:{ all -> 0x005a }
        L_0x005a:
            r13 = move-exception
            kotlin.io.CloseableKt.closeFinally(r11, r12)
            throw r13
        L_0x005f:
            int r9 = (int) r6
            byte[] r10 = new byte[r9]     // Catch:{ Throwable -> 0x0058 }
        L_0x0063:
            if (r9 <= 0) goto L_0x006b
            int r8 = r4.read(r10, r5, r9)     // Catch:{ Throwable -> 0x0058 }
            if (r8 >= 0) goto L_0x0072
        L_0x006b:
            if (r9 != 0) goto L_0x0075
        L_0x006d:
            kotlin.io.CloseableKt.closeFinally(r11, r12)
            return r10
        L_0x0072:
            int r9 = r9 - r8
            int r5 = r5 + r8
            goto L_0x0063
        L_0x0075:
            byte[] r10 = java.util.Arrays.copyOf(r10, r5)     // Catch:{ Throwable -> 0x0058 }
            java.lang.String r13 = "java.util.Arrays.copyOf(this, newSize)"
            kotlin.jvm.internal.Intrinsics.checkExpressionValueIsNotNull(r10, r13)     // Catch:{ Throwable -> 0x0058 }
            goto L_0x006d
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.io.FilesKt__FileReadWriteKt.readBytes(java.io.File):byte[]");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x002b, code lost:
        throw r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0027, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0028, code lost:
        kotlin.io.CloseableKt.closeFinally(r3, r4);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final void writeBytes(@org.jetbrains.annotations.NotNull java.io.File r6, @org.jetbrains.annotations.NotNull byte[] r7) {
        /*
            java.lang.String r3 = "$this$writeBytes"
            kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r6, r3)
            java.lang.String r3 = "array"
            kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r7, r3)
            java.io.FileOutputStream r3 = new java.io.FileOutputStream
            r3.<init>(r6)
            java.io.Closeable r3 = (java.io.Closeable) r3
            r4 = 0
            java.lang.Throwable r4 = (java.lang.Throwable) r4
            r0 = r3
            java.io.FileOutputStream r0 = (java.io.FileOutputStream) r0     // Catch:{ Throwable -> 0x0025 }
            r2 = r0
            r1 = 0
            r2.write(r7)     // Catch:{ Throwable -> 0x0025 }
            kotlin.Unit r5 = kotlin.Unit.INSTANCE     // Catch:{ Throwable -> 0x0025 }
            kotlin.io.CloseableKt.closeFinally(r3, r4)
            return
        L_0x0025:
            r4 = move-exception
            throw r4     // Catch:{ all -> 0x0027 }
        L_0x0027:
            r5 = move-exception
            kotlin.io.CloseableKt.closeFinally(r3, r4)
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.io.FilesKt__FileReadWriteKt.writeBytes(java.io.File, byte[]):void");
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.io.FileOutputStream.<init>(java.io.File, boolean):void throws java.io.FileNotFoundException}
     arg types: [java.io.File, int]
     candidates:
      ClspMth{java.io.FileOutputStream.<init>(java.lang.String, boolean):void throws java.io.FileNotFoundException}
      ClspMth{java.io.FileOutputStream.<init>(java.io.File, boolean):void throws java.io.FileNotFoundException} */
    /* JADX WARNING: Code restructure failed: missing block: B:10:0x002c, code lost:
        throw r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0028, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0029, code lost:
        kotlin.io.CloseableKt.closeFinally(r3, r4);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final void appendBytes(@org.jetbrains.annotations.NotNull java.io.File r6, @org.jetbrains.annotations.NotNull byte[] r7) {
        /*
            java.lang.String r3 = "$this$appendBytes"
            kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r6, r3)
            java.lang.String r3 = "array"
            kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r7, r3)
            java.io.FileOutputStream r3 = new java.io.FileOutputStream
            r4 = 1
            r3.<init>(r6, r4)
            java.io.Closeable r3 = (java.io.Closeable) r3
            r4 = 0
            java.lang.Throwable r4 = (java.lang.Throwable) r4
            r0 = r3
            java.io.FileOutputStream r0 = (java.io.FileOutputStream) r0     // Catch:{ Throwable -> 0x0026 }
            r2 = r0
            r1 = 0
            r2.write(r7)     // Catch:{ Throwable -> 0x0026 }
            kotlin.Unit r5 = kotlin.Unit.INSTANCE     // Catch:{ Throwable -> 0x0026 }
            kotlin.io.CloseableKt.closeFinally(r3, r4)
            return
        L_0x0026:
            r4 = move-exception
            throw r4     // Catch:{ all -> 0x0028 }
        L_0x0028:
            r5 = move-exception
            kotlin.io.CloseableKt.closeFinally(r3, r4)
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.io.FilesKt__FileReadWriteKt.appendBytes(java.io.File, byte[]):void");
    }

    @NotNull
    public static final String readText(@NotNull File $this$readText, @NotNull Charset charset) {
        Intrinsics.checkParameterIsNotNull($this$readText, "$this$readText");
        Intrinsics.checkParameterIsNotNull(charset, "charset");
        return new String(FilesKt.readBytes($this$readText), charset);
    }

    public static /* synthetic */ String readText$default(File file, Charset charset, int i, Object obj) {
        if ((i & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        return FilesKt.readText(file, charset);
    }

    public static final void writeText(@NotNull File $this$writeText, @NotNull String text, @NotNull Charset charset) {
        Intrinsics.checkParameterIsNotNull($this$writeText, "$this$writeText");
        Intrinsics.checkParameterIsNotNull(text, TextBundle.TEXT_ENTRY);
        Intrinsics.checkParameterIsNotNull(charset, "charset");
        byte[] bytes = text.getBytes(charset);
        Intrinsics.checkExpressionValueIsNotNull(bytes, "(this as java.lang.String).getBytes(charset)");
        FilesKt.writeBytes($this$writeText, bytes);
    }

    public static /* synthetic */ void writeText$default(File file, String str, Charset charset, int i, Object obj) {
        if ((i & 2) != 0) {
            charset = Charsets.UTF_8;
        }
        FilesKt.writeText(file, str, charset);
    }

    public static final void appendText(@NotNull File $this$appendText, @NotNull String text, @NotNull Charset charset) {
        Intrinsics.checkParameterIsNotNull($this$appendText, "$this$appendText");
        Intrinsics.checkParameterIsNotNull(text, TextBundle.TEXT_ENTRY);
        Intrinsics.checkParameterIsNotNull(charset, "charset");
        byte[] bytes = text.getBytes(charset);
        Intrinsics.checkExpressionValueIsNotNull(bytes, "(this as java.lang.String).getBytes(charset)");
        FilesKt.appendBytes($this$appendText, bytes);
    }

    public static /* synthetic */ void appendText$default(File file, String str, Charset charset, int i, Object obj) {
        if ((i & 2) != 0) {
            charset = Charsets.UTF_8;
        }
        FilesKt.appendText(file, str, charset);
    }

    public static final void forEachBlock(@NotNull File $this$forEachBlock, @NotNull Function2<? super byte[], ? super Integer, Unit> action) {
        Intrinsics.checkParameterIsNotNull($this$forEachBlock, "$this$forEachBlock");
        Intrinsics.checkParameterIsNotNull(action, "action");
        FilesKt.forEachBlock($this$forEachBlock, 4096, action);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x003b, code lost:
        r7 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x003c, code lost:
        kotlin.io.CloseableKt.closeFinally(r5, r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x003f, code lost:
        throw r7;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final void forEachBlock(@org.jetbrains.annotations.NotNull java.io.File r8, int r9, @org.jetbrains.annotations.NotNull kotlin.jvm.functions.Function2<? super byte[], ? super java.lang.Integer, kotlin.Unit> r10) {
        /*
            java.lang.String r5 = "$this$forEachBlock"
            kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r8, r5)
            java.lang.String r5 = "action"
            kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r10, r5)
            r5 = 512(0x200, float:7.175E-43)
            int r5 = kotlin.ranges.RangesKt.coerceAtLeast(r9, r5)
            byte[] r2 = new byte[r5]
            java.io.FileInputStream r5 = new java.io.FileInputStream
            r5.<init>(r8)
            java.io.Closeable r5 = (java.io.Closeable) r5
            r6 = 0
            java.lang.Throwable r6 = (java.lang.Throwable) r6
            r0 = r5
            java.io.FileInputStream r0 = (java.io.FileInputStream) r0     // Catch:{ Throwable -> 0x0039 }
            r3 = r0
            r1 = 0
        L_0x0024:
            int r4 = r3.read(r2)     // Catch:{ Throwable -> 0x0039 }
            if (r4 > 0) goto L_0x0031
            kotlin.Unit r7 = kotlin.Unit.INSTANCE     // Catch:{ Throwable -> 0x0039 }
            kotlin.io.CloseableKt.closeFinally(r5, r6)
            return
        L_0x0031:
            java.lang.Integer r7 = java.lang.Integer.valueOf(r4)     // Catch:{ Throwable -> 0x0039 }
            r10.invoke(r2, r7)     // Catch:{ Throwable -> 0x0039 }
            goto L_0x0024
        L_0x0039:
            r6 = move-exception
            throw r6     // Catch:{ all -> 0x003b }
        L_0x003b:
            r7 = move-exception
            kotlin.io.CloseableKt.closeFinally(r5, r6)
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.io.FilesKt__FileReadWriteKt.forEachBlock(java.io.File, int, kotlin.jvm.functions.Function2):void");
    }

    public static /* synthetic */ void forEachLine$default(File file, Charset charset, Function1 function1, int i, Object obj) {
        if ((i & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        FilesKt.forEachLine(file, charset, function1);
    }

    public static final void forEachLine(@NotNull File $this$forEachLine, @NotNull Charset charset, @NotNull Function1<? super String, Unit> action) {
        Intrinsics.checkParameterIsNotNull($this$forEachLine, "$this$forEachLine");
        Intrinsics.checkParameterIsNotNull(charset, "charset");
        Intrinsics.checkParameterIsNotNull(action, "action");
        TextStreamsKt.forEachLine(new BufferedReader(new InputStreamReader(new FileInputStream($this$forEachLine), charset)), action);
    }

    @InlineOnly
    private static final FileInputStream inputStream(@NotNull File $this$inputStream) {
        return new FileInputStream($this$inputStream);
    }

    @InlineOnly
    private static final FileOutputStream outputStream(@NotNull File $this$outputStream) {
        return new FileOutputStream($this$outputStream);
    }

    public static /* synthetic */ List readLines$default(File file, Charset charset, int i, Object obj) {
        if ((i & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        return FilesKt.readLines(file, charset);
    }

    @NotNull
    public static final List<String> readLines(@NotNull File $this$readLines, @NotNull Charset charset) {
        Intrinsics.checkParameterIsNotNull($this$readLines, "$this$readLines");
        Intrinsics.checkParameterIsNotNull(charset, "charset");
        ArrayList result = new ArrayList();
        FilesKt.forEachLine($this$readLines, charset, new FilesKt__FileReadWriteKt$readLines$1(result));
        return result;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0062, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0063, code lost:
        kotlin.jvm.internal.InlineMarker.finallyStart(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x006a, code lost:
        if (kotlin.internal.PlatformImplementationsKt.apiVersionIsAtLeast(1, 1, 0) != false) goto L_0x006c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x006c, code lost:
        kotlin.io.CloseableKt.closeFinally(r4, r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x006f, code lost:
        kotlin.jvm.internal.InlineMarker.finallyEnd(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0072, code lost:
        throw r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0073, code lost:
        if (r5 == null) goto L_0x0075;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0075, code lost:
        r4.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:?, code lost:
        r4.close();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static /* synthetic */ java.lang.Object useLines$default(java.io.File r9, java.nio.charset.Charset r10, kotlin.jvm.functions.Function1 r11, int r12, java.lang.Object r13) {
        /*
            r7 = 0
            r8 = 1
            r4 = r12 & 1
            if (r4 == 0) goto L_0x0008
            java.nio.charset.Charset r10 = kotlin.text.Charsets.UTF_8
        L_0x0008:
            r2 = 0
            java.lang.String r4 = "$this$useLines"
            kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r9, r4)
            java.lang.String r4 = "charset"
            kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r10, r4)
            java.lang.String r4 = "block"
            kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r11, r4)
            r6 = 8192(0x2000, float:1.14794E-41)
            java.io.FileInputStream r4 = new java.io.FileInputStream
            r4.<init>(r9)
            java.io.InputStream r4 = (java.io.InputStream) r4
            java.io.InputStreamReader r5 = new java.io.InputStreamReader
            r5.<init>(r4, r10)
            r4 = r5
            java.io.Reader r4 = (java.io.Reader) r4
            boolean r5 = r4 instanceof java.io.BufferedReader
            if (r5 == 0) goto L_0x0055
            java.io.BufferedReader r4 = (java.io.BufferedReader) r4
        L_0x0032:
            java.io.Closeable r4 = (java.io.Closeable) r4
            r5 = 0
            java.lang.Throwable r5 = (java.lang.Throwable) r5
            r0 = r4
            java.io.BufferedReader r0 = (java.io.BufferedReader) r0     // Catch:{ Throwable -> 0x0060 }
            r3 = r0
            r1 = 0
            kotlin.sequences.Sequence r6 = kotlin.io.TextStreamsKt.lineSequence(r3)     // Catch:{ Throwable -> 0x0060 }
            java.lang.Object r6 = r11.invoke(r6)     // Catch:{ Throwable -> 0x0060 }
            kotlin.jvm.internal.InlineMarker.finallyStart(r8)
            boolean r7 = kotlin.internal.PlatformImplementationsKt.apiVersionIsAtLeast(r8, r8, r7)
            if (r7 == 0) goto L_0x005c
            kotlin.io.CloseableKt.closeFinally(r4, r5)
        L_0x0051:
            kotlin.jvm.internal.InlineMarker.finallyEnd(r8)
            return r6
        L_0x0055:
            java.io.BufferedReader r5 = new java.io.BufferedReader
            r5.<init>(r4, r6)
            r4 = r5
            goto L_0x0032
        L_0x005c:
            r4.close()
            goto L_0x0051
        L_0x0060:
            r5 = move-exception
            throw r5     // Catch:{ all -> 0x0062 }
        L_0x0062:
            r6 = move-exception
            kotlin.jvm.internal.InlineMarker.finallyStart(r8)
            boolean r7 = kotlin.internal.PlatformImplementationsKt.apiVersionIsAtLeast(r8, r8, r7)
            if (r7 == 0) goto L_0x0073
            kotlin.io.CloseableKt.closeFinally(r4, r5)
        L_0x006f:
            kotlin.jvm.internal.InlineMarker.finallyEnd(r8)
            throw r6
        L_0x0073:
            if (r5 != 0) goto L_0x0079
            r4.close()
            goto L_0x006f
        L_0x0079:
            r4.close()     // Catch:{ Throwable -> 0x007e }
            goto L_0x006f
        L_0x007e:
            r4 = move-exception
            goto L_0x006f
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.io.FilesKt__FileReadWriteKt.useLines$default(java.io.File, java.nio.charset.Charset, kotlin.jvm.functions.Function1, int, java.lang.Object):java.lang.Object");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x005c, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x005d, code lost:
        kotlin.jvm.internal.InlineMarker.finallyStart(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0064, code lost:
        if (kotlin.internal.PlatformImplementationsKt.apiVersionIsAtLeast(1, 1, 0) != false) goto L_0x0066;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0066, code lost:
        kotlin.io.CloseableKt.closeFinally(r4, r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0069, code lost:
        kotlin.jvm.internal.InlineMarker.finallyEnd(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x006c, code lost:
        throw r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x006d, code lost:
        if (r5 == null) goto L_0x006f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x006f, code lost:
        r4.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
        r4.close();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final <T> T useLines(@org.jetbrains.annotations.NotNull java.io.File r9, @org.jetbrains.annotations.NotNull java.nio.charset.Charset r10, @org.jetbrains.annotations.NotNull kotlin.jvm.functions.Function1<? super kotlin.sequences.Sequence<java.lang.String>, ? extends T> r11) {
        /*
            r7 = 0
            r8 = 1
            r2 = 0
            java.lang.String r4 = "$this$useLines"
            kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r9, r4)
            java.lang.String r4 = "charset"
            kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r10, r4)
            java.lang.String r4 = "block"
            kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r11, r4)
            r6 = 8192(0x2000, float:1.14794E-41)
            java.io.FileInputStream r4 = new java.io.FileInputStream
            r4.<init>(r9)
            java.io.InputStream r4 = (java.io.InputStream) r4
            java.io.InputStreamReader r5 = new java.io.InputStreamReader
            r5.<init>(r4, r10)
            r4 = r5
            java.io.Reader r4 = (java.io.Reader) r4
            boolean r5 = r4 instanceof java.io.BufferedReader
            if (r5 == 0) goto L_0x004f
            java.io.BufferedReader r4 = (java.io.BufferedReader) r4
        L_0x002c:
            java.io.Closeable r4 = (java.io.Closeable) r4
            r5 = 0
            java.lang.Throwable r5 = (java.lang.Throwable) r5
            r0 = r4
            java.io.BufferedReader r0 = (java.io.BufferedReader) r0     // Catch:{ Throwable -> 0x005a }
            r3 = r0
            r1 = 0
            kotlin.sequences.Sequence r6 = kotlin.io.TextStreamsKt.lineSequence(r3)     // Catch:{ Throwable -> 0x005a }
            java.lang.Object r6 = r11.invoke(r6)     // Catch:{ Throwable -> 0x005a }
            kotlin.jvm.internal.InlineMarker.finallyStart(r8)
            boolean r7 = kotlin.internal.PlatformImplementationsKt.apiVersionIsAtLeast(r8, r8, r7)
            if (r7 == 0) goto L_0x0056
            kotlin.io.CloseableKt.closeFinally(r4, r5)
        L_0x004b:
            kotlin.jvm.internal.InlineMarker.finallyEnd(r8)
            return r6
        L_0x004f:
            java.io.BufferedReader r5 = new java.io.BufferedReader
            r5.<init>(r4, r6)
            r4 = r5
            goto L_0x002c
        L_0x0056:
            r4.close()
            goto L_0x004b
        L_0x005a:
            r5 = move-exception
            throw r5     // Catch:{ all -> 0x005c }
        L_0x005c:
            r6 = move-exception
            kotlin.jvm.internal.InlineMarker.finallyStart(r8)
            boolean r7 = kotlin.internal.PlatformImplementationsKt.apiVersionIsAtLeast(r8, r8, r7)
            if (r7 == 0) goto L_0x006d
            kotlin.io.CloseableKt.closeFinally(r4, r5)
        L_0x0069:
            kotlin.jvm.internal.InlineMarker.finallyEnd(r8)
            throw r6
        L_0x006d:
            if (r5 != 0) goto L_0x0073
            r4.close()
            goto L_0x0069
        L_0x0073:
            r4.close()     // Catch:{ Throwable -> 0x0078 }
            goto L_0x0069
        L_0x0078:
            r4 = move-exception
            goto L_0x0069
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.io.FilesKt__FileReadWriteKt.useLines(java.io.File, java.nio.charset.Charset, kotlin.jvm.functions.Function1):java.lang.Object");
    }
}
