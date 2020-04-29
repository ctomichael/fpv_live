package com.dji.permission;

import android.content.Context;
import com.dji.permission.checker.StandardChecker;
import java.util.ArrayList;
import java.util.List;

public class PermissionHelper {
    private static final StandardChecker CHECKER = new StandardChecker();

    public static boolean checkPermissions(Context context, String... permissions) {
        return CHECKER.hasPermission(context, permissions);
    }

    public static List<String> transformText(Context context, List<String> permissions) {
        List<String> textList = new ArrayList<>();
        for (String permission : permissions) {
            char c = 65535;
            switch (permission.hashCode()) {
                case -2062386608:
                    if (permission.equals(Permission.READ_SMS)) {
                        c = 18;
                        break;
                    }
                    break;
                case -1928411001:
                    if (permission.equals(Permission.READ_CALENDAR)) {
                        c = 0;
                        break;
                    }
                    break;
                case -1921431796:
                    if (permission.equals(Permission.READ_CALL_LOG)) {
                        c = 11;
                        break;
                    }
                    break;
                case -1888586689:
                    if (permission.equals(Permission.ACCESS_FINE_LOCATION)) {
                        c = 6;
                        break;
                    }
                    break;
                case -1479758289:
                    if (permission.equals(Permission.RECEIVE_WAP_PUSH)) {
                        c = 19;
                        break;
                    }
                    break;
                case -1238066820:
                    if (permission.equals(Permission.BODY_SENSORS)) {
                        c = 15;
                        break;
                    }
                    break;
                case -895679497:
                    if (permission.equals(Permission.RECEIVE_MMS)) {
                        c = 20;
                        break;
                    }
                    break;
                case -895673731:
                    if (permission.equals(Permission.RECEIVE_SMS)) {
                        c = 17;
                        break;
                    }
                    break;
                case -406040016:
                    if (permission.equals(Permission.READ_EXTERNAL_STORAGE)) {
                        c = 21;
                        break;
                    }
                    break;
                case -63024214:
                    if (permission.equals(Permission.ACCESS_COARSE_LOCATION)) {
                        c = 7;
                        break;
                    }
                    break;
                case -5573545:
                    if (permission.equals(Permission.READ_PHONE_STATE)) {
                        c = 9;
                        break;
                    }
                    break;
                case 52602690:
                    if (permission.equals(Permission.SEND_SMS)) {
                        c = 16;
                        break;
                    }
                    break;
                case 112197485:
                    if (permission.equals(Permission.CALL_PHONE)) {
                        c = 10;
                        break;
                    }
                    break;
                case 214526995:
                    if (permission.equals(Permission.WRITE_CONTACTS)) {
                        c = 4;
                        break;
                    }
                    break;
                case 463403621:
                    if (permission.equals(Permission.CAMERA)) {
                        c = 2;
                        break;
                    }
                    break;
                case 603653886:
                    if (permission.equals(Permission.WRITE_CALENDAR)) {
                        c = 1;
                        break;
                    }
                    break;
                case 610633091:
                    if (permission.equals(Permission.WRITE_CALL_LOG)) {
                        c = 12;
                        break;
                    }
                    break;
                case 784519842:
                    if (permission.equals(Permission.USE_SIP)) {
                        c = 13;
                        break;
                    }
                    break;
                case 952819282:
                    if (permission.equals(Permission.PROCESS_OUTGOING_CALLS)) {
                        c = 14;
                        break;
                    }
                    break;
                case 1271781903:
                    if (permission.equals(Permission.GET_ACCOUNTS)) {
                        c = 5;
                        break;
                    }
                    break;
                case 1365911975:
                    if (permission.equals(Permission.WRITE_EXTERNAL_STORAGE)) {
                        c = 22;
                        break;
                    }
                    break;
                case 1831139720:
                    if (permission.equals(Permission.RECORD_AUDIO)) {
                        c = 8;
                        break;
                    }
                    break;
                case 1977429404:
                    if (permission.equals(Permission.READ_CONTACTS)) {
                        c = 3;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                case 1:
                    String message = context.getString(R.string.runtime_permissions_name_calendar);
                    if (textList.contains(message)) {
                        break;
                    } else {
                        textList.add(message);
                        break;
                    }
                case 2:
                    String message2 = context.getString(R.string.runtime_permissions_name_camera);
                    if (textList.contains(message2)) {
                        break;
                    } else {
                        textList.add(message2);
                        break;
                    }
                case 3:
                case 4:
                case 5:
                    String message3 = context.getString(R.string.runtime_permissions_name_contacts);
                    if (textList.contains(message3)) {
                        break;
                    } else {
                        textList.add(message3);
                        break;
                    }
                case 6:
                case 7:
                    String message4 = context.getString(R.string.runtime_permissions_name_location);
                    if (textList.contains(message4)) {
                        break;
                    } else {
                        textList.add(message4);
                        break;
                    }
                case 8:
                    String message5 = context.getString(R.string.runtime_permissions_name_microphone);
                    if (textList.contains(message5)) {
                        break;
                    } else {
                        textList.add(message5);
                        break;
                    }
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:
                case 14:
                    String message6 = context.getString(R.string.runtime_permissions_name_phone);
                    if (textList.contains(message6)) {
                        break;
                    } else {
                        textList.add(message6);
                        break;
                    }
                case 15:
                    String message7 = context.getString(R.string.runtime_permissions_name_sensors);
                    if (textList.contains(message7)) {
                        break;
                    } else {
                        textList.add(message7);
                        break;
                    }
                case 16:
                case 17:
                case 18:
                case 19:
                case 20:
                    String message8 = context.getString(R.string.runtime_permissions_name_sms);
                    if (textList.contains(message8)) {
                        break;
                    } else {
                        textList.add(message8);
                        break;
                    }
                case 21:
                case 22:
                    String message9 = context.getString(R.string.runtime_permissions_name_storage);
                    if (textList.contains(message9)) {
                        break;
                    } else {
                        textList.add(message9);
                        break;
                    }
            }
        }
        return textList;
    }
}
