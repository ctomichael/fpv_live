package dji.common.remotecontroller;

import android.support.annotation.NonNull;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.P3.DataRcSetCustomFuction;
import dji.midware.util.BytesUtil;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.bouncycastle.crypto.tls.CipherSuite;

@EXClassNullAway
public class ProfessionalRC {

    public interface Callback {
        void onEvent(@NonNull Event event);
    }

    public enum ProfessionalRCEventType {
        CLICK,
        CLICK_DOWN,
        CLICK_UP,
        LONG_CLICK,
        ROTATE
    }

    public static class Event {
        public static final int MAX_VALUE_OF_DIAL = 660;
        public static final int MAX_VALUE_OF_WHEEL = 660;
        public static final int MIN_VALUE_OF_DIAL = -660;
        public static final int MIN_VALUE_OF_WHEEL = -660;
        private int currentValue;
        private CustomizableButton customizableButton = CustomizableButton.UNKNOWN;
        private ButtonAction functionID = ButtonAction.NOT_DEFINED;
        private int maxValue;
        private int minValue;
        private ProfessionalRCEventType professionalRCEventType = ProfessionalRCEventType.CLICK_DOWN;

        public Event() {
        }

        public Event(CustomizableButton customizableButton2, ProfessionalRCEventType professionalRCEventType2, int currentValue2, int minValue2, int maxValue2) {
            this.customizableButton = customizableButton2;
            this.professionalRCEventType = professionalRCEventType2;
            this.currentValue = currentValue2;
            this.minValue = minValue2;
            this.maxValue = maxValue2;
        }

        public boolean equals(Object o) {
            boolean z = true;
            if (this == o) {
                return true;
            }
            if (!(o instanceof Event)) {
                return false;
            }
            Event event = (Event) o;
            if (this.currentValue != event.currentValue || this.minValue != event.minValue || this.maxValue != event.maxValue || this.functionID != event.functionID || this.customizableButton != event.customizableButton) {
                return false;
            }
            if (this.professionalRCEventType != event.professionalRCEventType) {
                z = false;
            }
            return z;
        }

        public Event(ButtonAction functionID2) {
            this.functionID = functionID2;
        }

        public int hashCode() {
            return (((((((((this.functionID.hashCode() * 31) + this.customizableButton.hashCode()) * 31) + this.professionalRCEventType.hashCode()) * 31) + this.currentValue) * 31) + this.minValue) * 31) + this.maxValue;
        }

        public ButtonAction getFunctionID() {
            return this.functionID;
        }

        public CustomizableButton getCustomizableButton() {
            return this.customizableButton;
        }

        public ProfessionalRCEventType getProfessionalRCEventType() {
            return this.professionalRCEventType;
        }

        public int getCurrentValue() {
            return this.currentValue;
        }

        public int getMinValue() {
            return this.minValue;
        }

        public int getMaxValue() {
            return this.maxValue;
        }

        public String toString() {
            return "Event{functionID=" + this.functionID + ", customizableButton=" + this.customizableButton + ", professionalRCEventType=" + this.professionalRCEventType + ", currentValue=" + this.currentValue + ", minValue=" + this.minValue + ", maxValue=" + this.maxValue + '}';
        }
    }

    public enum ButtonAction {
        CameraSetting(0),
        GimbalCenter(1),
        SwitchGimbalMode(2),
        MapSwitch(3),
        ClearRoute(4),
        Battery(5),
        GimbalDirec(6),
        CenterMetering(7),
        AeLock(8),
        ForeArm(9),
        Vision1(10),
        Vision2(11),
        RESET_FPV_GIMBAL(12),
        LiveViewExpand(13),
        QUICK_SPIN(14),
        Navigation(15),
        PlayBack(16),
        CenterFocus(17),
        Navigation1(18),
        Navigation2(19),
        IndexShutterISOSwitch(20),
        FixWing(21),
        OneKeyTakeOffLanding(22),
        GetGimbalControl(23),
        GimbalMode(24),
        QuickSetting(25),
        AFMF(26),
        CloseTips(27),
        SmartGear(28),
        ReviewWarning(29),
        SWITCH_FREQUENCY(30),
        UpdateHomeDrone(31),
        UpdateHomeRC(32),
        UpdateHomeStartFly(33),
        FlyExp(34),
        GimbalExp(35),
        GimbalRoll(36),
        Focus(37),
        Apeture(38),
        CameraZoom(39),
        FpvPitch(40),
        CompositionMode(41),
        Spotlight(42),
        FocusPeaking(43),
        GridLine(44),
        Histogram(45),
        AEMFSwitch(46),
        OverExposure(47),
        ISO(48),
        SHUT(49),
        TOGGLE_LANDING_GEAR(54),
        CloseRadarMap(59),
        CloseDownVPS(60),
        CloseFrontAss(61),
        SwitchGimbalFpv(62),
        FullScreen(63),
        EnterGSMode(64),
        ExitGSMode(65),
        ForceMapSwitch(66),
        ExitFixWing(67),
        MenuSetting(70),
        GimbalPitch(71),
        GimbalYaw(73),
        TorsGyro(77),
        VertVelocity(78),
        GimbalPitchYawCenter(79),
        ColorOscilloScope(80),
        GimbalDownCenter(81),
        MasterSlaveGroup(82),
        OnBoardInterfaceZero(86),
        OnBoardInterfaceOne(87),
        OnBoardInterfaceTwo(88),
        OnBoardInterfaceThree(89),
        OnBoardInterfaceFour(90),
        CUSTOM100(100),
        CUSTOM101(101),
        CUSTOM102(102),
        CUSTOM103(103),
        CUSTOM104(104),
        CUSTOM105(105),
        CUSTOM106(106),
        CUSTOM107(107),
        CUSTOM108(108),
        CUSTOM109(109),
        OTHER(110),
        AutoHighLight(CipherSuite.TLS_DH_anon_WITH_CAMELLIA_256_CBC_SHA256),
        ClearTipsForConsume(198),
        DownVisionLight(199),
        VisionSwitch(200),
        NOT_DEFINED(255);
        
        private int data;

        private ButtonAction(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        private boolean _equals(int b) {
            return this.data == b;
        }

        public static ButtonAction find(int b) {
            ButtonAction result = NOT_DEFINED;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public enum CustomizableButton {
        C1,
        C2,
        C3,
        C4,
        BA,
        BB,
        BC,
        BD,
        BE,
        BF,
        BG,
        BH,
        SET,
        SHUT,
        APER,
        ISO,
        Menu,
        TD,
        LW,
        RW,
        LD,
        RD,
        UNKNOWN;

        public static int findIndex(CustomizableButton btn) {
            CustomizableButton[] list = values();
            for (int i = 0; i < list.length; i++) {
                if (list[i] == btn) {
                    return i;
                }
            }
            return -1;
        }

        public static CustomizableButton find(String str) {
            CustomizableButton[] list = values();
            for (int i = 0; i < list.length; i++) {
                if (list[i].toString().equals(str)) {
                    return list[i];
                }
            }
            return null;
        }
    }

    public static class ProRCUserBean {
        private boolean auto_mode;
        private int configIndex_now = 0;
        private List<ProRCConfigBean> configs = new LinkedList();
        private int master;
        private int single;
        private int slave;
        private String username;

        public byte[] getUserInfoHeader() {
            int i = 0;
            byte[] result = new byte[14];
            byte[] tmp = BytesUtil.getBytes(this.username);
            System.arraycopy(tmp, 0, result, 0, tmp.length);
            for (int i2 = tmp.length; i2 < 12; i2++) {
                result[i2] = 0;
            }
            result[12] = (byte) this.configIndex_now;
            if (this.auto_mode) {
                i = 1;
            }
            result[13] = (byte) ((i << 6) + (this.single << 4) + (this.master << 2) + this.slave);
            return result;
        }

        public ProRCUserBean(String _username) {
            this.username = _username;
            this.configs.add(new ProRCConfigBean());
            this.configs.add(new ProRCConfigBean());
            this.configs.add(new ProRCConfigBean());
        }

        public List<ProRCConfigBean> getConfigs() {
            return this.configs;
        }

        public void setConfigs(List<ProRCConfigBean> configs2) {
            this.configs = configs2;
        }

        public void setUsername(String username2) {
            this.username = username2;
        }

        public String getUsername() {
            return this.username;
        }

        public int getConfigIndex() {
            return this.configIndex_now;
        }

        public void setConfigIndex(int configIndex_now2) {
            this.configIndex_now = configIndex_now2;
        }

        public boolean isAuto_mode() {
            return this.auto_mode;
        }

        public void setAuto_mode(boolean auto_mode2) {
            this.auto_mode = auto_mode2;
        }

        public int getSingle() {
            return this.single;
        }

        public void setSingle(int single2) {
            this.single = single2;
        }

        public int getMaster() {
            return this.master;
        }

        public void setMaster(int master2) {
            this.master = master2;
        }

        public int getSlave() {
            return this.slave;
        }

        public void setSlave(int slave2) {
            this.slave = slave2;
        }

        public ProRCConfigBean getConfigNow() {
            return this.configs.get(this.configIndex_now);
        }

        public void setConfigNow(ProRCConfigBean currentConfig) {
            this.configs.set(this.configIndex_now, currentConfig);
        }

        public boolean equals(Object o) {
            boolean z = true;
            if (this == o) {
                return true;
            }
            if (!(o instanceof ProRCUserBean)) {
                return false;
            }
            ProRCUserBean that = (ProRCUserBean) o;
            if (this.configIndex_now != that.configIndex_now || this.auto_mode != that.auto_mode || this.single != that.single || this.master != that.master || this.slave != that.slave) {
                return false;
            }
            if (this.username != null) {
                if (!this.username.equals(that.username)) {
                    return false;
                }
            } else if (that.username != null) {
                return false;
            }
            if (this.configs != null) {
                z = this.configs.equals(that.configs);
            } else if (that.configs != null) {
                z = false;
            }
            return z;
        }

        public int hashCode() {
            int result;
            int i;
            int i2 = 0;
            if (this.username != null) {
                result = this.username.hashCode();
            } else {
                result = 0;
            }
            int i3 = ((result * 31) + this.configIndex_now) * 31;
            if (this.auto_mode) {
                i = 1;
            } else {
                i = 0;
            }
            int i4 = (((((((i3 + i) * 31) + this.single) * 31) + this.master) * 31) + this.slave) * 31;
            if (this.configs != null) {
                i2 = this.configs.hashCode();
            }
            return i4 + i2;
        }

        public String toString() {
            return "ProRCUserBean{username='" + this.username + '\'' + ", configIndex_now=" + this.configIndex_now + ", auto_mode=" + this.auto_mode + ", single=" + this.single + ", master=" + this.master + ", slave=" + this.slave + ", configs=" + this.configs + '}';
        }
    }

    public static class ProRCConfigBean {
        private List<ProRCFuncMapBean> funcMaps = new LinkedList();

        public byte[] toBytes() {
            byte[] result = new byte[22];
            for (int i = 0; i < this.funcMaps.size(); i++) {
                result[i] = (byte) this.funcMaps.get(i).getFuncValue();
            }
            for (int i2 = this.funcMaps.size(); i2 < 22; i2++) {
                result[i2] = -1;
            }
            return result;
        }

        ProRCConfigBean() {
            DataRcSetCustomFuction.ProCustomButton[] btns;
            for (DataRcSetCustomFuction.ProCustomButton proCustomButton : DataRcSetCustomFuction.ProCustomButton.values()) {
                this.funcMaps.add(new ProRCFuncMapBean(proCustomButton.ordinal(), 0));
            }
        }

        public ProRCConfigBean(int[] values) {
            DataRcSetCustomFuction.ProCustomButton[] btns = DataRcSetCustomFuction.ProCustomButton.values();
            for (int i = 0; i < btns.length; i++) {
                this.funcMaps.add(new ProRCFuncMapBean(btns[i].ordinal(), values[i]));
            }
        }

        public ProRCConfigBean(ButtonConfiguration configuration) {
            DataRcSetCustomFuction.ProCustomButton[] btns = DataRcSetCustomFuction.ProCustomButton.values();
            CustomizableButton[] values = CustomizableButton.values();
            for (int i = 0; i < btns.length; i++) {
                this.funcMaps.add(new ProRCFuncMapBean(btns[i].ordinal(), configuration.getButtonActions().get(values[i]).value()));
            }
        }

        public ProRCConfigBean(byte[] values) {
            DataRcSetCustomFuction.ProCustomButton[] btns = DataRcSetCustomFuction.ProCustomButton.values();
            for (int i = 0; i < btns.length; i++) {
                this.funcMaps.add(new ProRCFuncMapBean(btns[i].ordinal(), values[i]));
            }
        }

        public List<ProRCFuncMapBean> getFuncMaps() {
            return this.funcMaps;
        }

        public void setFuncMaps(List<ProRCFuncMapBean> funcMaps2) {
            this.funcMaps = funcMaps2;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof ProRCConfigBean)) {
                return false;
            }
            ProRCConfigBean that = (ProRCConfigBean) o;
            if (this.funcMaps != null) {
                return this.funcMaps.equals(that.funcMaps);
            }
            if (that.funcMaps != null) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            if (this.funcMaps != null) {
                return this.funcMaps.hashCode();
            }
            return 0;
        }
    }

    public static class ProRCFuncMapBean {
        /* access modifiers changed from: private */
        public int btn_id;
        /* access modifiers changed from: private */
        public int func_value;

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof ProRCFuncMapBean)) {
                return false;
            }
            ProRCFuncMapBean that = (ProRCFuncMapBean) o;
            if (this.btn_id != that.btn_id) {
                return false;
            }
            if (this.func_value != that.func_value) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return (this.btn_id * 31) + this.func_value;
        }

        public ProRCFuncMapBean(int id, int value) {
            this.btn_id = id;
            this.func_value = value;
        }

        public int getBtnID() {
            return this.btn_id;
        }

        public void setBtnID(int btn_id2) {
            this.btn_id = btn_id2;
        }

        public int getFuncValue() {
            return this.func_value;
        }

        public void setFuncValue(int func_value2) {
            this.func_value = func_value2;
        }
    }

    public static class ButtonConfiguration {
        private Map<CustomizableButton, ButtonAction> buttonActions = new HashMap(CustomizableButton.values().length);

        public ButtonConfiguration(ProRCConfigBean proRCConfigBean) {
            for (ProRCFuncMapBean bean : proRCConfigBean.getFuncMaps()) {
                this.buttonActions.put(CustomizableButton.values()[bean.btn_id], ButtonAction.find(bean.func_value));
            }
        }

        public ButtonConfiguration(Map<CustomizableButton, ButtonAction> buttonActions2) {
            this.buttonActions = buttonActions2;
        }

        public Map<CustomizableButton, ButtonAction> getButtonActions() {
            return new HashMap<>(this.buttonActions);
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof ButtonConfiguration)) {
                return false;
            }
            ButtonConfiguration that = (ButtonConfiguration) o;
            if (this.buttonActions != null) {
                return this.buttonActions.equals(that.buttonActions);
            }
            if (that.buttonActions != null) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            if (this.buttonActions != null) {
                return this.buttonActions.hashCode();
            }
            return 0;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            for (Map.Entry<CustomizableButton, ButtonAction> entry : this.buttonActions.entrySet()) {
                stringBuilder.append(entry.getKey()).append(":").append(entry.getValue()).append("\n");
            }
            return stringBuilder.toString();
        }
    }

    public static class ButtonConfigurationBuilder {
        private static volatile ProRCConfigBean defaultConfig;
        static final ButtonAction[] settableActions = {ButtonAction.RESET_FPV_GIMBAL, ButtonAction.QUICK_SPIN, ButtonAction.SWITCH_FREQUENCY, ButtonAction.Focus, ButtonAction.TOGGLE_LANDING_GEAR, ButtonAction.CUSTOM100, ButtonAction.CUSTOM101, ButtonAction.CUSTOM102, ButtonAction.CUSTOM103, ButtonAction.CUSTOM104, ButtonAction.CUSTOM105, ButtonAction.CUSTOM106, ButtonAction.CUSTOM107, ButtonAction.CUSTOM108, ButtonAction.CUSTOM109};
        private ButtonConfiguration buttonConfiguration;

        public ButtonConfiguration build() {
            return this.buttonConfiguration;
        }

        public ButtonConfigurationBuilder buildDefaultButtonConfiguration() {
            if (defaultConfig == null) {
                synchronized (ButtonConfigurationBuilder.class) {
                    if (defaultConfig == null) {
                        DataRcSetCustomFuction.ProCustomButton[] btnList = DataRcSetCustomFuction.ProCustomButton.values();
                        int len = btnList.length;
                        int[] resetValues = new int[len];
                        for (int i = 0; i < len; i++) {
                            resetValues[i] = ButtonAction.NOT_DEFINED.value();
                            String func = RcProDefaultConfig.getInstance().getFunc(btnList[i].toString());
                            if (func != null) {
                                resetValues[i] = RcProConfigInfo.getInstance().getFunctionValueByName(func);
                            }
                        }
                        defaultConfig = new ProRCConfigBean(resetValues);
                    }
                }
            }
            this.buttonConfiguration = new ButtonConfiguration(defaultConfig);
            return this;
        }

        public ButtonConfigurationBuilder buildButtonConfigurationWithMap(Map<CustomizableButton, ButtonAction> map) {
            this.buttonConfiguration = new ButtonConfiguration(map);
            return this;
        }

        public ButtonConfigurationBuilder configButton(CustomizableButton button, ButtonAction buttonAction) {
            if (this.buttonConfiguration == null) {
                buildDefaultButtonConfiguration();
            }
            if (!this.buttonConfiguration.getButtonActions().values().contains(buttonAction) || buttonAction.equals(ButtonAction.NOT_DEFINED)) {
                this.buttonConfiguration.getButtonActions().put(button, buttonAction);
                return this;
            }
            throw new RuntimeException("some button already has the action:" + buttonAction.name());
        }

        public ButtonAction[] getValidActionsForButton(CustomizableButton button) {
            return settableActions;
        }
    }
}
