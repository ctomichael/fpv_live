package dji.common.payload;

import android.text.TextUtils;
import dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction;
import java.util.List;

public class PayloadWidget {
    private String hintMsg = DJIRCAbstraction.DEFAULT_FIRMWARE_VERSION;
    private String name;
    private List<String> subItems;
    private int widgetIndex;
    private PayloadWidgetType widgetType;
    private int widgetValue;

    public enum PayloadWidgetType {
        UNKNOWN(0),
        BUTTON(1),
        SWITCH(2),
        RANGE(3),
        LIST(4),
        INPUT(5);
        
        private final int data;

        private PayloadWidgetType(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        private boolean _equals(int b) {
            return this.data == b;
        }

        public static PayloadWidgetType find(int b) {
            PayloadWidgetType result = UNKNOWN;
            PayloadWidgetType[] values = values();
            for (PayloadWidgetType tmp : values) {
                if (tmp._equals(b)) {
                    return tmp;
                }
            }
            return result;
        }
    }

    public PayloadWidget(PayloadWidgetType widgetType2, int widgetIndex2, int widgetValue2, String name2) {
        this.widgetType = widgetType2;
        this.widgetIndex = widgetIndex2;
        this.widgetValue = widgetValue2;
        this.name = name2;
    }

    public PayloadWidget(PayloadWidgetType widgetType2, int widgetIndex2, int widgetValue2, String name2, String hintMsg2) {
        this.widgetType = widgetType2;
        this.widgetIndex = widgetIndex2;
        this.widgetValue = widgetValue2;
        this.name = name2;
        this.hintMsg = hintMsg2;
    }

    public PayloadWidget(PayloadWidgetType widgetType2, int widgetIndex2, int widgetValue2, String name2, List<String> subItems2) {
        this.widgetType = widgetType2;
        this.widgetIndex = widgetIndex2;
        this.widgetValue = widgetValue2;
        this.name = name2;
        this.subItems = subItems2;
    }

    public PayloadWidget(PayloadWidgetType widgetType2, int widgetIndex2, int widgetValue2, String name2, String hintMsg2, List<String> subItems2) {
        this.widgetType = widgetType2;
        this.widgetIndex = widgetIndex2;
        this.widgetValue = widgetValue2;
        this.name = name2;
        if (!TextUtils.isEmpty(hintMsg2)) {
            this.hintMsg = hintMsg2;
        }
        this.subItems = subItems2;
    }

    public PayloadWidgetType getWidgetType() {
        return this.widgetType;
    }

    public int getWidgetIndex() {
        return this.widgetIndex;
    }

    public int getWidgetValue() {
        return this.widgetValue;
    }

    public String getName() {
        return this.name;
    }

    public String getHintMsg() {
        return this.hintMsg;
    }

    public List<String> getSubItems() {
        return this.subItems;
    }

    public String toString() {
        return "PayloadWidget{widgetType=" + this.widgetType.name() + ", widgetIndex=" + this.widgetIndex + ", widgetValue=" + this.widgetValue + ", name='" + this.name + '\'' + ", hintMsg='" + this.hintMsg + '\'' + ", subItems=" + (this.subItems != null ? TextUtils.join(", ", this.subItems) : "null") + '}';
    }
}
