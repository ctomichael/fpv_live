package dji.dbox.upgrade.p4.utils;

import android.content.Context;
import dji.component.accountcenter.IMemberProtocol;
import dji.dbox.upgrade.p4.constants.DJIUpConstants;
import dji.dbox.upgrade.p4.server.DJIUpServerManager;
import dji.fieldAnnotation.EXClassNullAway;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

@EXClassNullAway
public class DJIUpFilesUtils {
    public static void deleteFiles(Context context, String dirPath, String exludeVersion) {
        File rootDir = new File(dirPath);
        boolean isDirectory = rootDir.isDirectory();
        File[] dirs = rootDir.listFiles();
        if (dirs != null && dirs.length != 0) {
            for (File cdir : dirs) {
                if (cdir.isDirectory()) {
                    deleteVerDirs(new File(cdir.getAbsolutePath() + IMemberProtocol.PARAM_SEPERATOR + DJIUpServerManager.tarFiles + IMemberProtocol.PARAM_SEPERATOR + DJIUpServerManager.outFiles).listFiles(), exludeVersion);
                    deleteVerDirs(new File(cdir.getAbsolutePath() + IMemberProtocol.PARAM_SEPERATOR + DJIUpServerManager.tarFiles).listFiles(), exludeVersion);
                }
            }
        }
    }

    private static void deleteVerDirs(File[] verDirs, String exludeVersion) {
        if (verDirs != null && verDirs.length > 2) {
            ArrayList<File> list = new ArrayList<>(verDirs.length);
            for (File file : verDirs) {
                list.add(file);
            }
            Collections.sort(list, new Comparator<File>() {
                /* class dji.dbox.upgrade.p4.utils.DJIUpFilesUtils.AnonymousClass1 */

                public int compare(File lhs, File rhs) {
                    DJIUpConstants.LOGE("deleteVerDirs", "lhs=" + lhs.getName() + " " + Long.parseLong(lhs.getName().replace(".", "")));
                    DJIUpConstants.LOGE("deleteVerDirs", "rhs=" + rhs.getName() + " " + Long.parseLong(rhs.getName().replace(".", "")));
                    return DJIUpFilesUtils.compareLong(Long.parseLong(rhs.getName().replace(".", "")), Long.parseLong(lhs.getName().replace(".", "")));
                }
            });
            for (int i = 2; i < list.size(); i++) {
                if (!((File) list.get(i)).getName().contains(exludeVersion)) {
                    DeleteRecursive((File) list.get(i));
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public static int compareLong(long lhs, long rhs) {
        if (lhs < rhs) {
            return -1;
        }
        return lhs == rhs ? 0 : 1;
    }

    public static void DeleteRecursive(File dir) {
        if (dir != null) {
            if (dir.isDirectory()) {
                String[] children = dir.list();
                if (children != null) {
                    for (String str : children) {
                        File temp = new File(dir, str);
                        if (temp.isDirectory()) {
                            DeleteRecursive(temp);
                        } else {
                            DJIUpConstants.LOGE("deleteVerDirs", "delete=" + temp.getName() + " " + temp.delete());
                        }
                    }
                } else {
                    return;
                }
            }
            dir.delete();
        }
    }
}
