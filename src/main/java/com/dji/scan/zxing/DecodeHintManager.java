package com.dji.scan.zxing;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import com.google.zxing.DecodeHintType;
import dji.fieldAnnotation.EXClassNullAway;
import dji.pilot.fpv.util.DJIFlurryReport;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@EXClassNullAway
public final class DecodeHintManager {
    private static final Pattern COMMA = Pattern.compile(",");
    private static final String TAG = DecodeHintManager.class.getSimpleName();

    private DecodeHintManager() {
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.lang.String.replace(char, char):java.lang.String}
     arg types: [int, int]
     candidates:
      ClspMth{java.lang.String.replace(java.lang.CharSequence, java.lang.CharSequence):java.lang.String}
      ClspMth{java.lang.String.replace(char, char):java.lang.String} */
    private static Map<String, String> splitQuery(String query) {
        String name;
        String text;
        Map<String, String> map = new HashMap<>();
        int pos = 0;
        while (true) {
            if (pos >= query.length()) {
                break;
            } else if (query.charAt(pos) == '&') {
                pos++;
            } else {
                int amp = query.indexOf(38, pos);
                int equ = query.indexOf(61, pos);
                if (amp < 0) {
                    if (equ < 0) {
                        name = Uri.decode(query.substring(pos).replace('+', ' '));
                        text = "";
                    } else {
                        name = Uri.decode(query.substring(pos, equ).replace('+', ' '));
                        text = Uri.decode(query.substring(equ + 1).replace('+', ' '));
                    }
                    if (!map.containsKey(name)) {
                        map.put(name, text);
                    }
                } else if (equ < 0 || equ > amp) {
                    String name2 = Uri.decode(query.substring(pos, amp).replace('+', ' '));
                    if (!map.containsKey(name2)) {
                        map.put(name2, "");
                    }
                    pos = amp + 1;
                } else {
                    String name3 = Uri.decode(query.substring(pos, equ).replace('+', ' '));
                    String text2 = Uri.decode(query.substring(equ + 1, amp).replace('+', ' '));
                    if (!map.containsKey(name3)) {
                        map.put(name3, text2);
                    }
                    pos = amp + 1;
                }
            }
        }
        return map;
    }

    static Map<DecodeHintType, ?> parseDecodeHints(Uri inputUri) {
        String parameterText;
        String query = inputUri.getEncodedQuery();
        if (query == null || query.isEmpty()) {
            return null;
        }
        Map<String, String> parameters = splitQuery(query);
        Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
        DecodeHintType[] values = DecodeHintType.values();
        for (DecodeHintType hintType : values) {
            if (!(hintType == DecodeHintType.CHARACTER_SET || hintType == DecodeHintType.NEED_RESULT_POINT_CALLBACK || hintType == DecodeHintType.POSSIBLE_FORMATS || (parameterText = parameters.get(hintType.name())) == null)) {
                if (hintType.getValueType().equals(Object.class)) {
                    hints.put(hintType, parameterText);
                } else if (hintType.getValueType().equals(Void.class)) {
                    hints.put(hintType, Boolean.TRUE);
                } else if (hintType.getValueType().equals(String.class)) {
                    hints.put(hintType, parameterText);
                } else if (hintType.getValueType().equals(Boolean.class)) {
                    if (parameterText.isEmpty()) {
                        hints.put(hintType, Boolean.TRUE);
                    } else if ("0".equals(parameterText) || "false".equalsIgnoreCase(parameterText) || DJIFlurryReport.GroundStation.V2_GS_NO_VAL.equalsIgnoreCase(parameterText)) {
                        hints.put(hintType, Boolean.FALSE);
                    } else {
                        hints.put(hintType, Boolean.TRUE);
                    }
                } else if (hintType.getValueType().equals(int[].class)) {
                    if (!parameterText.isEmpty() && parameterText.charAt(parameterText.length() - 1) == ',') {
                        parameterText = parameterText.substring(0, parameterText.length() - 1);
                    }
                    String[] values2 = COMMA.split(parameterText);
                    int[] array = new int[values2.length];
                    int i = 0;
                    while (i < values2.length) {
                        try {
                            array[i] = Integer.parseInt(values2[i]);
                            i++;
                        } catch (NumberFormatException e) {
                            Log.w(TAG, "Skipping array of integers hint " + hintType + " due to invalid numeric value: '" + values2[i] + '\'');
                            array = null;
                        }
                    }
                    if (array != null) {
                        hints.put(hintType, array);
                    }
                } else {
                    Log.w(TAG, "Unsupported hint type '" + hintType + "' of type " + hintType.getValueType());
                }
            }
        }
        Log.i(TAG, "Hints from the URI: " + hints);
        return hints;
    }

    public static Map<DecodeHintType, Object> parseDecodeHints(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras == null || extras.isEmpty()) {
            return null;
        }
        Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
        DecodeHintType[] values = DecodeHintType.values();
        for (DecodeHintType hintType : values) {
            if (!(hintType == DecodeHintType.CHARACTER_SET || hintType == DecodeHintType.NEED_RESULT_POINT_CALLBACK || hintType == DecodeHintType.POSSIBLE_FORMATS)) {
                String hintName = hintType.name();
                if (extras.containsKey(hintName)) {
                    if (hintType.getValueType().equals(Void.class)) {
                        hints.put(hintType, Boolean.TRUE);
                    } else {
                        Object hintData = extras.get(hintName);
                        if (hintType.getValueType().isInstance(hintData)) {
                            hints.put(hintType, hintData);
                        } else {
                            Log.w(TAG, "Ignoring hint " + hintType + " because it is not assignable from " + hintData);
                        }
                    }
                }
            }
        }
        Log.i(TAG, "Hints from the Intent: " + hints);
        return hints;
    }
}
