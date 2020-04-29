package dji.log;

import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import dji.pilot.fpv.util.DJIFlurryReport;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DJILogUtils {
    private static final Pattern ANONYMOUS_CLASS = Pattern.compile("(\\$\\d+)+$");
    private static final int CALL_STACK_INDEX = 3;
    public static final String FORMAT_1 = "yyyy-MM-dd";
    public static final String FORMAT_2 = "yyyy-MM-dd HH:mm:ss";
    private static final String JSON_ARRAY_PREFIX = "[";
    private static final int JSON_INDENT = 2;
    private static final String JSON_INVALID = "Invalid Json";
    private static final String JSON_OBJECT_PREFIX = "{";
    private static final int MAX_TAG_LENGTH = 23;
    private static final String XMl_INVALID = "Invalid Xml";

    public static String formatNow() {
        return format(Calendar.getInstance().getTime(), FORMAT_1);
    }

    public static String formatNow(String format) {
        return format(Calendar.getInstance().getTime(), format);
    }

    public static String format(Date date) {
        return format(date, FORMAT_1);
    }

    public static String format(long date) {
        return format(new Date(date), FORMAT_1);
    }

    public static String format(long date, String format) {
        return format(new Date(date), format);
    }

    public static String format(Date date, String format) {
        return obtainDateFormat(format).format(Long.valueOf(date.getTime()));
    }

    public static Date parse(String dateStr) {
        return parse(dateStr, FORMAT_1);
    }

    public static Date parse(String dateStr, String format) {
        try {
            return obtainDateFormat(format).parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static SimpleDateFormat obtainDateFormat(String format) {
        return new SimpleDateFormat(format, Locale.getDefault());
    }

    static String formatJson(String json) {
        if (TextUtils.isEmpty(json)) {
            return JSON_INVALID;
        }
        try {
            String json2 = json.trim();
            if (json2.startsWith(JSON_OBJECT_PREFIX)) {
                return new JSONObject(json2).toString(2);
            }
            if (json2.startsWith("[")) {
                return new JSONArray(json2).toString(2);
            }
            return JSON_INVALID;
        } catch (JSONException e) {
            return "Invalid Json | " + e.toString();
        }
    }

    static String formatXml(String xml) {
        if (TextUtils.isEmpty(xml)) {
            return XMl_INVALID;
        }
        try {
            Source xmlInput = new StreamSource(new StringReader(xml));
            StreamResult xmlOutput = new StreamResult(new StringWriter());
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty("indent", DJIFlurryReport.GroundStation.V2_GS_YES_VAL);
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(xmlInput, xmlOutput);
            return xmlOutput.getWriter().toString().replaceFirst(">", ">\n");
        } catch (TransformerException e) {
            return XMl_INVALID;
        }
    }

    static String formatObject(Object object) {
        if (object == null) {
            return "null";
        }
        if (!object.getClass().isArray()) {
            return object.toString();
        }
        if (object instanceof boolean[]) {
            return Arrays.toString((boolean[]) ((boolean[]) object));
        }
        if (object instanceof byte[]) {
            return Arrays.toString((byte[]) ((byte[]) object));
        }
        if (object instanceof char[]) {
            return Arrays.toString((char[]) ((char[]) object));
        }
        if (object instanceof short[]) {
            TextUtils.concat(new CharSequence[0]);
            return Arrays.toString((short[]) ((short[]) object));
        } else if (object instanceof int[]) {
            return Arrays.toString((int[]) ((int[]) object));
        } else {
            if (object instanceof long[]) {
                return Arrays.toString((long[]) ((long[]) object));
            }
            if (object instanceof float[]) {
                return Arrays.toString((float[]) ((float[]) object));
            }
            if (object instanceof double[]) {
                return Arrays.toString((double[]) ((double[]) object));
            }
            if (object instanceof Object[]) {
                return Arrays.deepToString((Object[]) object);
            }
            return object.toString();
        }
    }

    public static String getCurrentStack() {
        return getCurrentStack(2, 100);
    }

    public static String getCurrentStack(int count) {
        return getCurrentStack(2, count);
    }

    public static String getThreadStack(Thread thread) {
        if (thread == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        StackTraceElement[] stackTrace = thread.getStackTrace();
        boolean isFirst = true;
        for (StackTraceElement st : stackTrace) {
            if (isFirst) {
                isFirst = false;
            } else {
                builder.append("\t").append(st.toString()).append("\n");
            }
        }
        return builder.toString();
    }

    static String getCurrentStack(int start, int count) {
        StringBuilder builder = new StringBuilder();
        StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        if (start >= stackTrace.length || start < 0 || count <= 0) {
            return "empty stack!!!";
        }
        int count2 = Math.min(count, stackTrace.length - start);
        builder.append("┌─────────────────────────────────────────────────────────────────────────────────────────────────\n");
        for (int i = start; i < stackTrace.length; i++) {
            builder.append("│ ").append(stackTrace[i].toString()).append("\n");
            if ((i - start) + 1 >= count2) {
                break;
            }
        }
        builder.append(DJILogConstant.FORMAT_CONSOLE_BOTTOM);
        return builder.toString();
    }

    public static String exceptionToString(Throwable e) {
        if (e == null) {
            return null;
        }
        try {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            return "\r\n" + sw.toString() + "\r\n";
        } catch (Exception e2) {
            return "bad getErrorInfoFromException";
        }
    }

    static String obtainStackElementTag() {
        return obtainStackElementTag(3);
    }

    static String obtainStackElementTag(int callStackIndex) {
        StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        if (stackTrace.length <= callStackIndex) {
            return "DJIGo";
        }
        String tag = stackTrace[callStackIndex].getClassName();
        Matcher m = ANONYMOUS_CLASS.matcher(tag);
        if (m.find()) {
            tag = m.replaceAll("");
        }
        String tag2 = tag.substring(tag.lastIndexOf(46) + 1);
        return (tag2.length() <= 23 || Build.VERSION.SDK_INT >= 24) ? tag2 : tag2.substring(0, 23);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.io.PrintWriter.<init>(java.io.Writer, boolean):void}
     arg types: [java.io.StringWriter, int]
     candidates:
      ClspMth{java.io.PrintWriter.<init>(java.io.File, java.lang.String):void throws java.io.FileNotFoundException, java.io.UnsupportedEncodingException}
      ClspMth{java.io.PrintWriter.<init>(java.io.OutputStream, boolean):void}
      ClspMth{java.io.PrintWriter.<init>(java.lang.String, java.lang.String):void throws java.io.FileNotFoundException, java.io.UnsupportedEncodingException}
      ClspMth{java.io.PrintWriter.<init>(java.io.Writer, boolean):void} */
    static String getStackTraceString(Throwable t) {
        StringWriter sw = new StringWriter(256);
        PrintWriter pw = new PrintWriter((Writer) sw, false);
        t.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }

    static String formatMessage(@NonNull String message, @NonNull Object... args) {
        try {
            if (args.length > 0) {
                return String.format(message, args);
            }
            return message;
        } catch (Exception e) {
            return message;
        }
    }
}
