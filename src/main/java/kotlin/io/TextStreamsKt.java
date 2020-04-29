package kotlin.io;

import dji.dbox.upgrade.p4.server.DJIUpServerManager;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.internal.InlineOnly;
import kotlin.jvm.JvmName;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import kotlin.text.Charsets;
import org.jetbrains.annotations.NotNull;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000X\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\u001a\u0017\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u0004H\b\u001a\u0017\u0010\u0000\u001a\u00020\u0005*\u00020\u00062\b\b\u0002\u0010\u0003\u001a\u00020\u0004H\b\u001a\u001c\u0010\u0007\u001a\u00020\b*\u00020\u00022\u0006\u0010\t\u001a\u00020\u00062\b\b\u0002\u0010\u0003\u001a\u00020\u0004\u001a\u001e\u0010\n\u001a\u00020\u000b*\u00020\u00022\u0012\u0010\f\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u000b0\r\u001a\u0010\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0010*\u00020\u0001\u001a\n\u0010\u0011\u001a\u00020\u0012*\u00020\u0013\u001a\u0010\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0015*\u00020\u0002\u001a\n\u0010\u0016\u001a\u00020\u000e*\u00020\u0002\u001a\u0017\u0010\u0016\u001a\u00020\u000e*\u00020\u00132\b\b\u0002\u0010\u0017\u001a\u00020\u0018H\b\u001a\r\u0010\u0019\u001a\u00020\u001a*\u00020\u000eH\b\u001a5\u0010\u001b\u001a\u0002H\u001c\"\u0004\b\u0000\u0010\u001c*\u00020\u00022\u0018\u0010\u001d\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0\u0010\u0012\u0004\u0012\u0002H\u001c0\rH\bø\u0001\u0000¢\u0006\u0002\u0010\u001f\u0002\b\n\u0006\b\u0011(\u001e0\u0001¨\u0006 "}, d2 = {"buffered", "Ljava/io/BufferedReader;", "Ljava/io/Reader;", "bufferSize", "", "Ljava/io/BufferedWriter;", "Ljava/io/Writer;", "copyTo", "", DJIUpServerManager.outFiles, "forEachLine", "", "action", "Lkotlin/Function1;", "", "lineSequence", "Lkotlin/sequences/Sequence;", "readBytes", "", "Ljava/net/URL;", "readLines", "", "readText", "charset", "Ljava/nio/charset/Charset;", "reader", "Ljava/io/StringReader;", "useLines", "T", "block", "Requires newer compiler version to be inlined correctly.", "(Ljava/io/Reader;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "kotlin-stdlib"}, k = 2, mv = {1, 1, 15})
@JvmName(name = "TextStreamsKt")
/* compiled from: ReadWrite.kt */
public final class TextStreamsKt {
    static /* synthetic */ BufferedReader buffered$default(Reader $this$buffered, int bufferSize, int i, Object obj) {
        if ((i & 1) != 0) {
            bufferSize = 8192;
        }
        return $this$buffered instanceof BufferedReader ? (BufferedReader) $this$buffered : new BufferedReader($this$buffered, bufferSize);
    }

    @InlineOnly
    private static final BufferedReader buffered(@NotNull Reader $this$buffered, int bufferSize) {
        return $this$buffered instanceof BufferedReader ? (BufferedReader) $this$buffered : new BufferedReader($this$buffered, bufferSize);
    }

    static /* synthetic */ BufferedWriter buffered$default(Writer $this$buffered, int bufferSize, int i, Object obj) {
        if ((i & 1) != 0) {
            bufferSize = 8192;
        }
        return $this$buffered instanceof BufferedWriter ? (BufferedWriter) $this$buffered : new BufferedWriter($this$buffered, bufferSize);
    }

    @InlineOnly
    private static final BufferedWriter buffered(@NotNull Writer $this$buffered, int bufferSize) {
        return $this$buffered instanceof BufferedWriter ? (BufferedWriter) $this$buffered : new BufferedWriter($this$buffered, bufferSize);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x003d, code lost:
        r12 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x003e, code lost:
        kotlin.io.CloseableKt.closeFinally(r10, r11);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0041, code lost:
        throw r12;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final void forEachLine(@org.jetbrains.annotations.NotNull java.io.Reader r14, @org.jetbrains.annotations.NotNull kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> r15) {
        /*
            java.lang.String r10 = "$this$forEachLine"
            kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r14, r10)
            java.lang.String r10 = "action"
            kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r15, r10)
            r5 = r14
            r4 = 0
            r11 = 8192(0x2000, float:1.14794E-41)
            boolean r10 = r5 instanceof java.io.BufferedReader
            if (r10 == 0) goto L_0x0042
            java.io.BufferedReader r5 = (java.io.BufferedReader) r5
            r10 = r5
        L_0x0017:
            java.io.Closeable r10 = (java.io.Closeable) r10
            r11 = 0
            java.lang.Throwable r11 = (java.lang.Throwable) r11
            r0 = r10
            java.io.BufferedReader r0 = (java.io.BufferedReader) r0     // Catch:{ Throwable -> 0x003b }
            r9 = r0
            r1 = 0
            kotlin.sequences.Sequence r8 = lineSequence(r9)     // Catch:{ Throwable -> 0x003b }
            r2 = 0
            r6 = r15
            r3 = 0
            java.util.Iterator r12 = r8.iterator()     // Catch:{ Throwable -> 0x003b }
        L_0x002d:
            boolean r13 = r12.hasNext()     // Catch:{ Throwable -> 0x003b }
            if (r13 == 0) goto L_0x0048
            java.lang.Object r7 = r12.next()     // Catch:{ Throwable -> 0x003b }
            r6.invoke(r7)     // Catch:{ Throwable -> 0x003b }
            goto L_0x002d
        L_0x003b:
            r11 = move-exception
            throw r11     // Catch:{ all -> 0x003d }
        L_0x003d:
            r12 = move-exception
            kotlin.io.CloseableKt.closeFinally(r10, r11)
            throw r12
        L_0x0042:
            java.io.BufferedReader r10 = new java.io.BufferedReader
            r10.<init>(r5, r11)
            goto L_0x0017
        L_0x0048:
            kotlin.Unit r12 = kotlin.Unit.INSTANCE     // Catch:{ Throwable -> 0x003b }
            kotlin.io.CloseableKt.closeFinally(r10, r11)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.io.TextStreamsKt.forEachLine(java.io.Reader, kotlin.jvm.functions.Function1):void");
    }

    @NotNull
    public static final List<String> readLines(@NotNull Reader $this$readLines) {
        Intrinsics.checkParameterIsNotNull($this$readLines, "$this$readLines");
        ArrayList result = new ArrayList();
        forEachLine($this$readLines, new TextStreamsKt$readLines$1(result));
        return result;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0047, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0048, code lost:
        kotlin.jvm.internal.InlineMarker.finallyStart(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x004f, code lost:
        if (kotlin.internal.PlatformImplementationsKt.apiVersionIsAtLeast(1, 1, 0) != false) goto L_0x0051;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0051, code lost:
        kotlin.io.CloseableKt.closeFinally(r4, r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0054, code lost:
        kotlin.jvm.internal.InlineMarker.finallyEnd(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0057, code lost:
        throw r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0058, code lost:
        if (r5 == null) goto L_0x005a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x005a, code lost:
        r4.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
        r4.close();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final <T> T useLines(@org.jetbrains.annotations.NotNull java.io.Reader r9, @org.jetbrains.annotations.NotNull kotlin.jvm.functions.Function1<? super kotlin.sequences.Sequence<java.lang.String>, ? extends T> r10) {
        /*
            r7 = 0
            r8 = 1
            r2 = 0
            java.lang.String r4 = "$this$useLines"
            kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r9, r4)
            java.lang.String r4 = "block"
            kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r10, r4)
            r5 = 8192(0x2000, float:1.14794E-41)
            boolean r4 = r9 instanceof java.io.BufferedReader
            if (r4 == 0) goto L_0x003b
            java.io.BufferedReader r9 = (java.io.BufferedReader) r9
            r4 = r9
        L_0x0018:
            java.io.Closeable r4 = (java.io.Closeable) r4
            r5 = 0
            java.lang.Throwable r5 = (java.lang.Throwable) r5
            r0 = r4
            java.io.BufferedReader r0 = (java.io.BufferedReader) r0     // Catch:{ Throwable -> 0x0045 }
            r3 = r0
            r1 = 0
            kotlin.sequences.Sequence r6 = lineSequence(r3)     // Catch:{ Throwable -> 0x0045 }
            java.lang.Object r6 = r10.invoke(r6)     // Catch:{ Throwable -> 0x0045 }
            kotlin.jvm.internal.InlineMarker.finallyStart(r8)
            boolean r7 = kotlin.internal.PlatformImplementationsKt.apiVersionIsAtLeast(r8, r8, r7)
            if (r7 == 0) goto L_0x0041
            kotlin.io.CloseableKt.closeFinally(r4, r5)
        L_0x0037:
            kotlin.jvm.internal.InlineMarker.finallyEnd(r8)
            return r6
        L_0x003b:
            java.io.BufferedReader r4 = new java.io.BufferedReader
            r4.<init>(r9, r5)
            goto L_0x0018
        L_0x0041:
            r4.close()
            goto L_0x0037
        L_0x0045:
            r5 = move-exception
            throw r5     // Catch:{ all -> 0x0047 }
        L_0x0047:
            r6 = move-exception
            kotlin.jvm.internal.InlineMarker.finallyStart(r8)
            boolean r7 = kotlin.internal.PlatformImplementationsKt.apiVersionIsAtLeast(r8, r8, r7)
            if (r7 == 0) goto L_0x0058
            kotlin.io.CloseableKt.closeFinally(r4, r5)
        L_0x0054:
            kotlin.jvm.internal.InlineMarker.finallyEnd(r8)
            throw r6
        L_0x0058:
            if (r5 != 0) goto L_0x005e
            r4.close()
            goto L_0x0054
        L_0x005e:
            r4.close()     // Catch:{ Throwable -> 0x0063 }
            goto L_0x0054
        L_0x0063:
            r4 = move-exception
            goto L_0x0054
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.io.TextStreamsKt.useLines(java.io.Reader, kotlin.jvm.functions.Function1):java.lang.Object");
    }

    @InlineOnly
    private static final StringReader reader(@NotNull String $this$reader) {
        return new StringReader($this$reader);
    }

    @NotNull
    public static final Sequence<String> lineSequence(@NotNull BufferedReader $this$lineSequence) {
        Intrinsics.checkParameterIsNotNull($this$lineSequence, "$this$lineSequence");
        return SequencesKt.constrainOnce(new LinesSequence($this$lineSequence));
    }

    @NotNull
    public static final String readText(@NotNull Reader $this$readText) {
        Intrinsics.checkParameterIsNotNull($this$readText, "$this$readText");
        StringWriter buffer = new StringWriter();
        copyTo$default($this$readText, buffer, 0, 2, null);
        String stringWriter = buffer.toString();
        Intrinsics.checkExpressionValueIsNotNull(stringWriter, "buffer.toString()");
        return stringWriter;
    }

    public static /* synthetic */ long copyTo$default(Reader reader, Writer writer, int i, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            i = 8192;
        }
        return copyTo(reader, writer, i);
    }

    public static final long copyTo(@NotNull Reader $this$copyTo, @NotNull Writer out, int bufferSize) {
        Intrinsics.checkParameterIsNotNull($this$copyTo, "$this$copyTo");
        Intrinsics.checkParameterIsNotNull(out, DJIUpServerManager.outFiles);
        long charsCopied = 0;
        char[] buffer = new char[bufferSize];
        int chars = $this$copyTo.read(buffer);
        while (chars >= 0) {
            out.write(buffer, 0, chars);
            charsCopied += (long) chars;
            chars = $this$copyTo.read(buffer);
        }
        return charsCopied;
    }

    @InlineOnly
    private static final String readText(@NotNull URL $this$readText, Charset charset) {
        return new String(readBytes($this$readText), charset);
    }

    static /* synthetic */ String readText$default(URL $this$readText, Charset charset, int i, Object obj) {
        if ((i & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        return new String(readBytes($this$readText), charset);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0029, code lost:
        throw r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0025, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0026, code lost:
        kotlin.io.CloseableKt.closeFinally(r3, r4);
     */
    @org.jetbrains.annotations.NotNull
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final byte[] readBytes(@org.jetbrains.annotations.NotNull java.net.URL r6) {
        /*
            java.lang.String r3 = "$this$readBytes"
            kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r6, r3)
            java.io.InputStream r3 = r6.openStream()
            java.io.Closeable r3 = (java.io.Closeable) r3
            r4 = 0
            java.lang.Throwable r4 = (java.lang.Throwable) r4
            r0 = r3
            java.io.InputStream r0 = (java.io.InputStream) r0     // Catch:{ Throwable -> 0x0023 }
            r2 = r0
            r1 = 0
            java.lang.String r5 = "it"
            kotlin.jvm.internal.Intrinsics.checkExpressionValueIsNotNull(r2, r5)     // Catch:{ Throwable -> 0x0023 }
            byte[] r5 = kotlin.io.ByteStreamsKt.readBytes(r2)     // Catch:{ Throwable -> 0x0023 }
            kotlin.io.CloseableKt.closeFinally(r3, r4)
            return r5
        L_0x0023:
            r4 = move-exception
            throw r4     // Catch:{ all -> 0x0025 }
        L_0x0025:
            r5 = move-exception
            kotlin.io.CloseableKt.closeFinally(r3, r4)
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.io.TextStreamsKt.readBytes(java.net.URL):byte[]");
    }
}
