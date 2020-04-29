package dji.midware.media.colors;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.media.colors.ColorOscilloscopeDisplayView;
import dji.midware.util.DjiSharedPreferencesManager;
import org.greenrobot.eventbus.EventBus;

@EXClassNullAway
public class ColorOscilloscopeUtils {
    public static final String OSCILLOSCOPE_IS_EXP_KEY = "oscilloscope_switch_exp_color_key";
    public static final String OSCILLOSCOPE_SWITCH_KEY = "oscilloscope_switch_key";

    public static void setOscilloscopeSwitchToSp(boolean isOpen) {
        DjiSharedPreferencesManager.putBoolean(ServiceManager.getContext(), OSCILLOSCOPE_SWITCH_KEY, isOpen);
    }

    public static boolean getOscilloscopeSwitchFromSp() {
        return DjiSharedPreferencesManager.getBoolean(ServiceManager.getContext(), OSCILLOSCOPE_SWITCH_KEY, false);
    }

    public static void setOscilloscopeIsExpToSp(boolean isExp) {
        DjiSharedPreferencesManager.putBoolean(ServiceManager.getContext(), OSCILLOSCOPE_IS_EXP_KEY, isExp);
    }

    public static boolean getOscilloscopeIsExpFromSp() {
        return DjiSharedPreferencesManager.getBoolean(ServiceManager.getContext(), OSCILLOSCOPE_IS_EXP_KEY, true);
    }

    public static void switchColorOscilloscope(boolean isOpen) {
        setOscilloscopeSwitchToSp(isOpen);
        EventBus.getDefault().post(isOpen ? ColorOscilloscopeDisplayView.OscilloscopeDisplayEvent.Open : ColorOscilloscopeDisplayView.OscilloscopeDisplayEvent.Close);
    }
}
