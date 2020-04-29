package dji.pilot2.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Process;
import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.pilot.publics.R;
import dji.publics.widget.dialog.DJIDialog;
import dji.publics.widget.dialog.DJIDialogBuilder;
import dji.publics.widget.dialog.DJIDialogType;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

@Keep
@EXClassNullAway
public class SystemUtils {
    private static Object[] mArmArchitecture = new Object[3];

    static {
        mArmArchitecture[1] = -1;
    }

    public static Object[] getCpuArchitecture() {
        InputStream is;
        InputStreamReader ir;
        BufferedReader br;
        if (((Integer) mArmArchitecture[1]).intValue() != -1) {
            return mArmArchitecture;
        }
        try {
            is = new FileInputStream("/proc/cpuinfo");
            ir = new InputStreamReader(is);
            br = new BufferedReader(ir);
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                String[] pair = line.split(":");
                if (pair.length == 2) {
                    String key = pair[0].trim();
                    String val = pair[1].trim();
                    if (key.compareTo("Processor") == 0) {
                        String n = "";
                        for (int i = val.indexOf("ARMv") + 4; i < val.length(); i++) {
                            String temp = val.charAt(i) + "";
                            if (!temp.matches("\\d")) {
                                break;
                            }
                            n = n + temp;
                        }
                        mArmArchitecture[0] = "ARM";
                        mArmArchitecture[1] = Integer.valueOf(Integer.parseInt(n));
                    } else if (key.compareToIgnoreCase("Features") == 0) {
                        if (val.contains("neon")) {
                            mArmArchitecture[2] = "neon";
                        }
                    } else if (key.compareToIgnoreCase("model name") == 0) {
                        if (val.contains("Intel")) {
                            mArmArchitecture[0] = "INTEL";
                            mArmArchitecture[2] = "atom";
                        }
                    } else if (key.compareToIgnoreCase("cpu family") == 0) {
                        mArmArchitecture[1] = Integer.valueOf(Integer.parseInt(val));
                    }
                }
            }
            br.close();
            ir.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Throwable th) {
            br.close();
            ir.close();
            is.close();
            throw th;
        }
        return mArmArchitecture;
    }

    public static boolean isIntelCPU() {
        Object[] armArchitecture = getCpuArchitecture();
        if (armArchitecture == null || !(armArchitecture[0] instanceof String) || ((String) armArchitecture[0]).compareToIgnoreCase("intel") != 0) {
            return false;
        }
        return true;
    }

    public static boolean isCanLoadSo() {
        try {
            System.loadLibrary("FRCorkscrew");
            return true;
        } catch (UnsatisfiedLinkError e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean checkSoAndShowDialog(Context context) {
        boolean isIntelCpu = isCanLoadSo();
        if (!isIntelCpu) {
            DJIDialog dialog = DJIDialogBuilder.createDJIDialog(context, DJIDialogType.MEDIUM, DJIDialog.DJIDialogTheme.WHITE);
            dialog.setContent(R.string.not_support_cpu);
            dialog.setLeftBtn(17039379, new DialogInterface.OnClickListener() {
                /* class dji.pilot2.utils.SystemUtils.AnonymousClass1 */

                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Process.killProcess(Process.myPid());
                }
            });
            dialog.show();
        }
        return !isIntelCpu;
    }
}
