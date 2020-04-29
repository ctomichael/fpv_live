package dji.thirdparty.plogger;

import dji.component.accountcenter.IMemberProtocol;
import java.util.Arrays;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

final class LoggerPrinter implements Printer {
    private static final int ASSERT = 7;
    private static final String BOTTOM_BORDER = "╚════════════════════════════════════════════════════════════════════════════════════════";
    private static final char BOTTOM_LEFT_CORNER = '╚';
    private static final int CHUNK_SIZE = 4000;
    private static final int DEBUG = 3;
    private static final String DEFAULT_TAG = "PRETTYLOGGER";
    private static final String DOUBLE_DIVIDER = "════════════════════════════════════════════";
    private static final int ERROR = 6;
    private static final char HORIZONTAL_DOUBLE_LINE = '║';
    private static final int INFO = 4;
    private static final int JSON_INDENT = 2;
    private static final String MIDDLE_BORDER = "╟────────────────────────────────────────────────────────────────────────────────────────";
    private static final char MIDDLE_CORNER = '╟';
    private static final int MIN_STACK_OFFSET = 3;
    private static final String SINGLE_DIVIDER = "────────────────────────────────────────────";
    private static final String TOP_BORDER = "╔════════════════════════════════════════════════════════════════════════════════════════";
    private static final char TOP_LEFT_CORNER = '╔';
    private static final int VERBOSE = 2;
    private static final int WARN = 5;
    private final ThreadLocal<Integer> localMethodCount = new ThreadLocal<>();
    private final ThreadLocal<String> localTag = new ThreadLocal<>();
    private final Settings settings = new Settings();
    private String tag;

    public LoggerPrinter() {
        init(DEFAULT_TAG);
    }

    public Settings init(String tag2) {
        if (tag2 == null) {
            throw new NullPointerException("tag may not be null");
        } else if (tag2.trim().length() == 0) {
            throw new IllegalStateException("tag may not be empty");
        } else {
            this.tag = tag2;
            return this.settings;
        }
    }

    public Settings getSettings() {
        return this.settings;
    }

    public Printer t(String tag2, int methodCount) {
        if (tag2 != null) {
            this.localTag.set(tag2);
        }
        this.localMethodCount.set(Integer.valueOf(methodCount));
        return this;
    }

    public void d(String message, Object... args) {
        log(3, (Throwable) null, message, args);
    }

    public void d(Object object) {
        String message;
        if (object.getClass().isArray()) {
            message = Arrays.deepToString((Object[]) object);
        } else {
            message = object.toString();
        }
        log(3, (Throwable) null, message, new Object[0]);
    }

    public void e(String message, Object... args) {
        e(null, message, args);
    }

    public void e(Throwable throwable, String message, Object... args) {
        log(6, throwable, message, args);
    }

    public void w(String message, Object... args) {
        log(5, (Throwable) null, message, args);
    }

    public void i(String message, Object... args) {
        log(4, (Throwable) null, message, args);
    }

    public void v(String message, Object... args) {
        log(2, (Throwable) null, message, args);
    }

    public void wtf(String message, Object... args) {
        log(7, (Throwable) null, message, args);
    }

    public void json(String json) {
        if (Helper.isEmpty(json)) {
            d("Empty/Null json content");
            return;
        }
        try {
            String json2 = json.trim();
            if (json2.startsWith("{")) {
                d(new JSONObject(json2).toString(2));
            } else if (json2.startsWith(IMemberProtocol.STRING_SEPERATOR_LEFT)) {
                d(new JSONArray(json2).toString(2));
            } else {
                e("Invalid Json", new Object[0]);
            }
        } catch (JSONException e) {
            e("Invalid Json", new Object[0]);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:0x006e A[SYNTHETIC, Splitter:B:20:0x006e] */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0073  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x007a A[SYNTHETIC, Splitter:B:26:0x007a] */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x007f  */
    /* JADX WARNING: Removed duplicated region for block: B:45:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void xml(java.lang.String r12) {
        /*
            r11 = this;
            boolean r8 = dji.thirdparty.plogger.Helper.isEmpty(r12)
            if (r8 == 0) goto L_0x000d
            java.lang.String r8 = "Empty/Null xml content"
            r11.d(r8)
        L_0x000c:
            return
        L_0x000d:
            r1 = 0
            r4 = 0
            java.io.StringReader r2 = new java.io.StringReader     // Catch:{ TransformerException -> 0x0062 }
            r2.<init>(r12)     // Catch:{ TransformerException -> 0x0062 }
            javax.xml.transform.stream.StreamSource r6 = new javax.xml.transform.stream.StreamSource     // Catch:{ TransformerException -> 0x0090, all -> 0x0089 }
            r6.<init>(r2)     // Catch:{ TransformerException -> 0x0090, all -> 0x0089 }
            java.io.StringWriter r5 = new java.io.StringWriter     // Catch:{ TransformerException -> 0x0090, all -> 0x0089 }
            r5.<init>()     // Catch:{ TransformerException -> 0x0090, all -> 0x0089 }
            javax.xml.transform.stream.StreamResult r7 = new javax.xml.transform.stream.StreamResult     // Catch:{ TransformerException -> 0x0093, all -> 0x008c }
            r7.<init>(r5)     // Catch:{ TransformerException -> 0x0093, all -> 0x008c }
            javax.xml.transform.TransformerFactory r8 = javax.xml.transform.TransformerFactory.newInstance()     // Catch:{ TransformerException -> 0x0093, all -> 0x008c }
            javax.xml.transform.Transformer r3 = r8.newTransformer()     // Catch:{ TransformerException -> 0x0093, all -> 0x008c }
            java.lang.String r8 = "indent"
            java.lang.String r9 = "yes"
            r3.setOutputProperty(r8, r9)     // Catch:{ TransformerException -> 0x0093, all -> 0x008c }
            java.lang.String r8 = "{http://xml.apache.org/xslt}indent-amount"
            java.lang.String r9 = "2"
            r3.setOutputProperty(r8, r9)     // Catch:{ TransformerException -> 0x0093, all -> 0x008c }
            r3.transform(r6, r7)     // Catch:{ TransformerException -> 0x0093, all -> 0x008c }
            java.io.Writer r8 = r7.getWriter()     // Catch:{ TransformerException -> 0x0093, all -> 0x008c }
            java.lang.String r8 = r8.toString()     // Catch:{ TransformerException -> 0x0093, all -> 0x008c }
            java.lang.String r9 = ">"
            java.lang.String r10 = ">\n"
            java.lang.String r8 = r8.replaceFirst(r9, r10)     // Catch:{ TransformerException -> 0x0093, all -> 0x008c }
            r11.d(r8)     // Catch:{ TransformerException -> 0x0093, all -> 0x008c }
            if (r5 == 0) goto L_0x005a
            r5.close()     // Catch:{ IOException -> 0x0083 }
        L_0x005a:
            if (r2 == 0) goto L_0x0097
            r2.close()
            r4 = r5
            r1 = r2
            goto L_0x000c
        L_0x0062:
            r0 = move-exception
        L_0x0063:
            java.lang.String r8 = "Invalid xml"
            r9 = 0
            java.lang.Object[] r9 = new java.lang.Object[r9]     // Catch:{ all -> 0x0077 }
            r11.e(r8, r9)     // Catch:{ all -> 0x0077 }
            if (r4 == 0) goto L_0x0071
            r4.close()     // Catch:{ IOException -> 0x0085 }
        L_0x0071:
            if (r1 == 0) goto L_0x000c
            r1.close()
            goto L_0x000c
        L_0x0077:
            r8 = move-exception
        L_0x0078:
            if (r4 == 0) goto L_0x007d
            r4.close()     // Catch:{ IOException -> 0x0087 }
        L_0x007d:
            if (r1 == 0) goto L_0x0082
            r1.close()
        L_0x0082:
            throw r8
        L_0x0083:
            r8 = move-exception
            goto L_0x005a
        L_0x0085:
            r8 = move-exception
            goto L_0x0071
        L_0x0087:
            r9 = move-exception
            goto L_0x007d
        L_0x0089:
            r8 = move-exception
            r1 = r2
            goto L_0x0078
        L_0x008c:
            r8 = move-exception
            r4 = r5
            r1 = r2
            goto L_0x0078
        L_0x0090:
            r0 = move-exception
            r1 = r2
            goto L_0x0063
        L_0x0093:
            r0 = move-exception
            r4 = r5
            r1 = r2
            goto L_0x0063
        L_0x0097:
            r4 = r5
            r1 = r2
            goto L_0x000c
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.plogger.LoggerPrinter.xml(java.lang.String):void");
    }

    public synchronized void log(int priority, String tag2, String message, Throwable throwable) {
        if (this.settings.getLogLevel() != LogLevel.NONE) {
            if (!(throwable == null || message == null)) {
                message = message + " : " + Helper.getStackTraceString(throwable);
            }
            if (throwable != null && message == null) {
                message = Helper.getStackTraceString(throwable);
            }
            if (message == null) {
                message = "No message/exception is set";
            }
            int methodCount = getMethodCount();
            if (Helper.isEmpty(message)) {
                message = "Empty/NULL log message";
            }
            logTopBorder(priority, tag2);
            logHeaderContent(priority, tag2, methodCount);
            byte[] bytes = message.getBytes();
            int length = bytes.length;
            if (length <= CHUNK_SIZE) {
                if (methodCount > 0) {
                    logDivider(priority, tag2);
                }
                logContent(priority, tag2, message);
                logBottomBorder(priority, tag2);
            } else {
                if (methodCount > 0) {
                    logDivider(priority, tag2);
                }
                for (int i = 0; i < length; i += CHUNK_SIZE) {
                    logContent(priority, tag2, new String(bytes, i, Math.min(length - i, (int) CHUNK_SIZE)));
                }
                logBottomBorder(priority, tag2);
            }
        }
    }

    public void resetSettings() {
        this.settings.reset();
    }

    private synchronized void log(int priority, Throwable throwable, String msg, Object... args) {
        if (this.settings.getLogLevel() != LogLevel.NONE) {
            log(priority, getTag(), createMessage(msg, args), throwable);
        }
    }

    private void logTopBorder(int logType, String tag2) {
        logChunk(logType, tag2, TOP_BORDER);
    }

    private void logHeaderContent(int logType, String tag2, int methodCount) {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        if (this.settings.isShowThreadInfo()) {
            logChunk(logType, tag2, "║ Thread: " + Thread.currentThread().getName());
            logDivider(logType, tag2);
        }
        String level = "";
        int stackOffset = getStackOffset(trace) + this.settings.getMethodOffset();
        if (methodCount + stackOffset > trace.length) {
            methodCount = (trace.length - stackOffset) - 1;
        }
        for (int i = methodCount; i > 0; i--) {
            int stackIndex = i + stackOffset;
            if (stackIndex < trace.length) {
                StringBuilder builder = new StringBuilder();
                builder.append("║ ").append(level).append(getSimpleClassName(trace[stackIndex].getClassName())).append(".").append(trace[stackIndex].getMethodName()).append(" ").append(" (").append(trace[stackIndex].getFileName()).append(":").append(trace[stackIndex].getLineNumber()).append(")");
                level = level + "   ";
                logChunk(logType, tag2, builder.toString());
            }
        }
    }

    private void logBottomBorder(int logType, String tag2) {
        logChunk(logType, tag2, BOTTOM_BORDER);
    }

    private void logDivider(int logType, String tag2) {
        logChunk(logType, tag2, MIDDLE_BORDER);
    }

    private void logContent(int logType, String tag2, String chunk) {
        String[] lines = chunk.split(System.getProperty("line.separator"));
        int length = lines.length;
        for (int i = 0; i < length; i++) {
            logChunk(logType, tag2, "║ " + lines[i]);
        }
    }

    private void logChunk(int logType, String tag2, String chunk) {
        String finalTag = formatTag(tag2);
        switch (logType) {
            case 2:
                this.settings.getLogAdapter().v(finalTag, chunk);
                return;
            case 3:
            default:
                this.settings.getLogAdapter().d(finalTag, chunk);
                return;
            case 4:
                this.settings.getLogAdapter().i(finalTag, chunk);
                return;
            case 5:
                this.settings.getLogAdapter().w(finalTag, chunk);
                return;
            case 6:
                this.settings.getLogAdapter().e(finalTag, chunk);
                return;
            case 7:
                this.settings.getLogAdapter().wtf(finalTag, chunk);
                return;
        }
    }

    private String getSimpleClassName(String name) {
        return name.substring(name.lastIndexOf(".") + 1);
    }

    private String formatTag(String tag2) {
        if (Helper.isEmpty(tag2) || Helper.equals(this.tag, tag2)) {
            return this.tag;
        }
        return this.tag + "-" + tag2;
    }

    private String getTag() {
        String tag2 = this.localTag.get();
        if (tag2 == null) {
            return this.tag;
        }
        this.localTag.remove();
        return tag2;
    }

    private String createMessage(String message, Object... args) {
        return (args == null || args.length == 0) ? message : String.format(message, args);
    }

    private int getMethodCount() {
        Integer count = this.localMethodCount.get();
        int result = this.settings.getMethodCount();
        if (count != null) {
            this.localMethodCount.remove();
            result = count.intValue();
        }
        if (result >= 0) {
            return result;
        }
        throw new IllegalStateException("methodCount cannot be negative");
    }

    private int getStackOffset(StackTraceElement[] trace) {
        for (int i = 3; i < trace.length; i++) {
            String name = trace[i].getClassName();
            if (!name.equals(LoggerPrinter.class.getName()) && !name.equals(Logger.class.getName())) {
                return i - 1;
            }
        }
        return -1;
    }
}
