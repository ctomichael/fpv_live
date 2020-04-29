package dji.log;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.SystemClock;
import android.text.TextUtils;
import dji.log.DJILog;
import dji.log.DJILogConsoleConfig;
import dji.log.DJILogFileConfig;
import dji.log.impl.SimpleConsoleFormat;
import dji.log.impl.SimpleEncryption;
import dji.log.impl.SimpleFileFormat;
import dji.thirdparty.sanselan.formats.tiff.constants.GPSTagConstants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class DJILogExample {
    private static final String DIR = "DJILogExample";
    private static final String TAG = "DJILogExample";
    private String example;

    private DJILogExample(String example2) {
        this.example = example2;
    }

    public static void example(final Context context) {
        new Thread(new Runnable() {
            /* class dji.log.DJILogExample.AnonymousClass1 */

            public void run() {
                DJILogExample.execute(context);
            }
        }).start();
    }

    /* access modifiers changed from: private */
    public static void method() {
        SystemClock.sleep(5000);
    }

    /* access modifiers changed from: private */
    @SuppressLint({"SdCardPath"})
    public static void execute(Context context) {
        DJILog.setController(DJILog.newController().priority(6).save(false).warn(2));
        DJILog.init(context, new DJILogFileConfig.Builder(context).setLogTimeFormat(DJILogConstant.LOG_INFO_TIME_FORMAT).setEncryption(new SimpleEncryption()).setFileFormat(new SimpleFileFormat()).setPathRoot("/sdcard/DJI/dji.go.v4/").setVersionName("4.3.0").setVersionCode(1000).build(), new DJILogConsoleConfig.Builder(context).setConsoleFormat(new SimpleConsoleFormat()).build());
        DJILog.d("d:use class name as tag", new Object[0]);
        DJILog.d("DJILogExample", "d:assign tag", new Object[0]);
        DJILog.d("DJILogExample", "d:format %d %f %s", 1, Float.valueOf(2.0f), "d");
        DJILog.e("e:use class name as tag", new Object[0]);
        DJILog.e("DJILogExample", "e:assign tag", new Object[0]);
        DJILog.e("DJILogExample", "e:format %d %f %s", 1, Float.valueOf(2.0f), "e");
        DJILog.w("DJILogExample", "w:with throwable", new NullPointerException("there is a null pointer exception"), new Object[0]);
        DJILog.i("DJILogExample", "i:with throwable", new UnsupportedOperationException("the operation is not supported"), new Object[0]);
        DJILog.saveLog("save message");
        DJILog.saveLog("save message", "DJILogExample");
        DJILog.saveExtraLog("save message", "DJILogExample");
        DJILog.logWriteD("DJILogExample", "d & save message", new Object[0]);
        DJILog.logWriteD("DJILogExample", "d & save message in DIR", "DJILogExample", new Object[0]);
        DJILog.logWriteW("DJILogExample", "w & save message", new Object[0]);
        DJILog.logWriteE("DJILogExample", "e & save message in DIR", "DJILogExample", new Object[0]);
        DJILog.logStack();
        DJILog.logStack(2);
        DJILog.logStack("DJILogExample", 4);
        DJILog.saveStack("DJILogExample");
        DJILog.saveStack("DJILogExample", 2);
        DJILog.d("", "tag is empty warn!", new Object[0]);
        DJILog.d("aabbccddeeffgghhiiggkkllmmnnooppqqrrssttuuvvwwxxyyzz", "tag is too long warn", new Object[0]);
        for (int i = 0; i < 101; i++) {
            DJILog.w("DJILogExampleWarn", "%d", Integer.valueOf(i));
        }
        DJILog.countTime("DJILogExample", new DJILog.Callback() {
            /* class dji.log.DJILogExample.AnonymousClass2 */

            public void doMethod() {
                DJILogExample.method();
            }
        });
        DJILogExample exampleA = new DJILogExample(GPSTagConstants.GPS_TAG_GPS_STATUS_VALUE_MEASUREMENT_IN_PROGRESS);
        DJILogExample exampleB = new DJILogExample("B");
        DJILogExample exampleC = new DJILogExample("C");
        DJILogExample exampleD = new DJILogExample("D");
        ArrayList<DJILogExample> exampleList = new ArrayList<>();
        exampleList.add(exampleA);
        exampleList.add(exampleB);
        exampleList.add(exampleC);
        exampleList.add(exampleD);
        Map<String, DJILogExample> exampleMap = new HashMap<>();
        exampleMap.put(exampleA.example, exampleA);
        exampleMap.put(exampleB.example, exampleB);
        exampleMap.put(exampleC.example, exampleC);
        exampleMap.put(exampleD.example, exampleD);
        Set<DJILogExample> exampleSet = new HashSet<>();
        exampleSet.add(exampleA);
        exampleSet.add(exampleB);
        exampleSet.add(exampleC);
        exampleSet.add(exampleD);
        DJILog.object(exampleA);
        DJILog.object("DJILogExample", exampleB);
        DJILog.object(6, "DJILogExample", exampleC);
        DJILog.object(new DJILogExample[]{exampleA, exampleB, exampleC, exampleD});
        DJILog.object(exampleList);
        DJILog.object(exampleMap);
        DJILog.object(exampleSet);
        DJILog.object(new int[]{1, 2, 3, 4});
        DJILog.object(new boolean[]{true, false, false, false});
        DJILog.json("{\"query\":\"Pizza\",\"locations\":[94043,90210]}");
        DJILog.json("DJILogExample", "{\"query\":\"Pizza\",\"locations\":[94043,90210]}");
        DJILog.json(5, "DJILogExample", "{\"query\":\"Pizza\",\"locations\":[94043,90210]}");
        DJILog.json("[{\"query\":\"Pizza\",\"locations\":[94043,90210]},{\"query\":\"Pizza\",\"locations\":[94043,90210]}]");
        DJILog.json("DJILogExample", "[{\"query\":\"Pizza\",\"locations\":[94043,90210]},{\"query\":\"Pizza\",\"locations\":[94043,90210]}]");
        DJILog.json(6, "DJILogExample", "[{\"query\":\"Pizza\",\"locations\":[94043,90210]},{\"query\":\"Pizza\",\"locations\":[94043,90210]}]");
        DJILog.xml("<?xml version=\"1.0\" encoding=\"utf-8\"?><dji><device id=\"wm100\"><firmware formal=\"01.00.0701\"><release version=\"01.00.0701\" antirollback=\"1\" antirollback_ext=\"cn:3\" enforce=\"1\" enforce_ext=\"cn:3\" enforce_time=\"2017-10-31T12:31:43+00:00\" from=\"2017/10/31\" expire=\"2018/10/31\"><module id=\"0805\" version=\"01.01.01.70\" type=\"\" group=\"ac\" size=\"20285920\" md5=\"3aa9622a5ba485a4dbddd82163a40fe2\">wm100_0805_v01.01.01.70_20171020.pro.fw.sig</module></release></firmware></device></dji>");
        DJILog.xml("DJILogExample", "<?xml version=\"1.0\" encoding=\"utf-8\"?><dji><device id=\"wm100\"><firmware formal=\"01.00.0701\"><release version=\"01.00.0701\" antirollback=\"1\" antirollback_ext=\"cn:3\" enforce=\"1\" enforce_ext=\"cn:3\" enforce_time=\"2017-10-31T12:31:43+00:00\" from=\"2017/10/31\" expire=\"2018/10/31\"><module id=\"0805\" version=\"01.01.01.70\" type=\"\" group=\"ac\" size=\"20285920\" md5=\"3aa9622a5ba485a4dbddd82163a40fe2\">wm100_0805_v01.01.01.70_20171020.pro.fw.sig</module></release></firmware></device></dji>");
        DJILog.xml(6, "DJILogExample", "<?xml version=\"1.0\" encoding=\"utf-8\"?><dji><device id=\"wm100\"><firmware formal=\"01.00.0701\"><release version=\"01.00.0701\" antirollback=\"1\" antirollback_ext=\"cn:3\" enforce=\"1\" enforce_ext=\"cn:3\" enforce_time=\"2017-10-31T12:31:43+00:00\" from=\"2017/10/31\" expire=\"2018/10/31\"><module id=\"0805\" version=\"01.01.01.70\" type=\"\" group=\"ac\" size=\"20285920\" md5=\"3aa9622a5ba485a4dbddd82163a40fe2\">wm100_0805_v01.01.01.70_20171020.pro.fw.sig</module></release></firmware></device></dji>");
        DJILog.addInterceptor(new Interceptor() {
            /* class dji.log.DJILogExample.AnonymousClass3 */

            public void interceptor(DJILogEntry entry) {
                entry.msg = TextUtils.concat(entry.msg, "fairy tail").toString();
            }
        });
    }

    public String toString() {
        return "this is an example " + this.example;
    }
}
