package com.dji.permission.checker;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import com.dji.permission.Permission;
import java.util.List;

public final class StrictChecker implements PermissionChecker {
    public boolean hasPermission(@NonNull Context context, @NonNull String... permissions) {
        if (Build.VERSION.SDK_INT < 21) {
            return true;
        }
        for (String permission : permissions) {
            if (!hasPermission(context, permission)) {
                return false;
            }
        }
        return true;
    }

    public boolean hasPermission(@NonNull Context context, @NonNull List<String> permissions) {
        if (Build.VERSION.SDK_INT < 21) {
            return true;
        }
        for (String permission : permissions) {
            if (!hasPermission(context, permission)) {
                return false;
            }
        }
        return true;
    }

    @RequiresApi(api = 19)
    private boolean hasPermission(Context context, String permission) {
        char c = 65535;
        try {
            switch (permission.hashCode()) {
                case -2062386608:
                    if (permission.equals(Permission.READ_SMS)) {
                        c = 19;
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
                        c = 7;
                        break;
                    }
                    break;
                case -1479758289:
                    if (permission.equals(Permission.RECEIVE_WAP_PUSH)) {
                        c = 20;
                        break;
                    }
                    break;
                case -1238066820:
                    if (permission.equals(Permission.BODY_SENSORS)) {
                        c = 16;
                        break;
                    }
                    break;
                case -895679497:
                    if (permission.equals(Permission.RECEIVE_MMS)) {
                        c = 18;
                        break;
                    }
                    break;
                case -895673731:
                    if (permission.equals(Permission.RECEIVE_SMS)) {
                        c = 21;
                        break;
                    }
                    break;
                case -406040016:
                    if (permission.equals(Permission.READ_EXTERNAL_STORAGE)) {
                        c = 22;
                        break;
                    }
                    break;
                case -63024214:
                    if (permission.equals(Permission.ACCESS_COARSE_LOCATION)) {
                        c = 6;
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
                        c = 17;
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
                        c = 14;
                        break;
                    }
                    break;
                case 952819282:
                    if (permission.equals(Permission.PROCESS_OUTGOING_CALLS)) {
                        c = 15;
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
                        c = 23;
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
                case 2133799037:
                    if (permission.equals(Permission.ADD_VOICEMAIL)) {
                        c = 13;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    return checkReadCalendar(context);
                case 1:
                    return checkWriteCalendar(context);
                case 2:
                    return checkCamera(context);
                case 3:
                    return checkReadContacts(context);
                case 4:
                    return checkWriteContacts(context);
                case 5:
                case 10:
                case 15:
                case 17:
                case 18:
                case 20:
                case 21:
                default:
                    return true;
                case 6:
                case 7:
                    return checkLocation(context);
                case 8:
                    return checkRecordAudio();
                case 9:
                    return checkReadPhoneState(context);
                case 11:
                    return checkReadCallLog(context);
                case 12:
                    return checkWriteCallLog(context);
                case 13:
                    return checkAddVoicemail(context);
                case 14:
                    return checkSip(context);
                case 16:
                    return checkSensors(context);
                case 19:
                    return checkReadSms(context);
                case 22:
                    return checkReadStorage();
                case 23:
                    return checkWriteStorage();
            }
        } catch (Throwable th) {
            return false;
        }
    }

    private static boolean checkReadCalendar(Context context) throws Throwable {
        return new CalendarReadTest(context).test();
    }

    private static boolean checkWriteCalendar(Context context) throws Throwable {
        return new CalendarWriteTest(context).test();
    }

    private static boolean checkCamera(Context context) throws Throwable {
        return new CameraTest(context).test();
    }

    private static boolean checkReadContacts(Context context) throws Throwable {
        return new ContactsReadTest(context).test();
    }

    private static boolean checkWriteContacts(Context context) throws Throwable {
        return new ContactsWriteTest(context.getContentResolver()).test();
    }

    private static boolean checkLocation(Context context) throws Throwable {
        return new LocationTest(context).test();
    }

    private static boolean checkRecordAudio() throws Throwable {
        return new RecordAudioTest().test();
    }

    private static boolean checkReadPhoneState(Context context) throws Throwable {
        return new PhoneStateReadTest(context).test();
    }

    private static boolean checkReadCallLog(Context context) throws Throwable {
        return new CallLogReadTest(context).test();
    }

    private static boolean checkWriteCallLog(Context context) throws Throwable {
        return new CallLogWriteTest(context).test();
    }

    private static boolean checkAddVoicemail(Context context) throws Throwable {
        return new AddVoicemailTest(context).test();
    }

    private static boolean checkSip(Context context) throws Throwable {
        return new SipTest(context).test();
    }

    private static boolean checkSensors(Context context) throws Throwable {
        return new SensorsTest(context).test();
    }

    private static boolean checkReadSms(Context context) throws Throwable {
        return new SmsReadTest(context).test();
    }

    private static boolean checkReadStorage() throws Throwable {
        return new StorageReadTest().test();
    }

    private static boolean checkWriteStorage() throws Throwable {
        return new StorageWriteTest().test();
    }
}
