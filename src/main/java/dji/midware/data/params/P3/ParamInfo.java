package dji.midware.data.params.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.P3.DataFlycGetParamInfo;

@Keep
@EXClassNullAway
public class ParamInfo {
    public DataFlycGetParamInfo.Attribute attribute;
    public long hash;
    public int index;
    public String name;
    public RangeModel range;
    public Number setvalue;
    public int size;
    public Class<? extends Number> type;
    public DataFlycGetParamInfo.TypeId typeId;
    public Number value;

    public String toString() {
        return "name=" + this.name + " hash=" + this.hash + " index=" + this.index + " value=" + this.value + " typeid=" + this.typeId.toString() + " type=" + this.type.getSimpleName() + " size=" + this.size + " attr=" + this.attribute.toString() + " range=" + this.range.toString();
    }

    public ParamInfoBean getBean() {
        ParamInfoBean bean = new ParamInfoBean();
        bean.index = this.index;
        bean.typeID = this.typeId.value();
        bean.size = this.size;
        bean.attribute = this.attribute.value();
        bean.maxValue = this.range.maxValue + "";
        bean.minValue = this.range.minValue + "";
        bean.defaultValue = this.range.defaultValue + "";
        bean.name = this.name;
        return bean;
    }

    public boolean isCorrect(Number value2) {
        switch (this.typeId) {
            case INT08S:
            case INT16S:
            case INT32S:
            case INT08U:
            case INT16U:
                if (this.range.maxValue.intValue() < value2.intValue() || this.range.minValue.intValue() > value2.intValue()) {
                    return false;
                }
                return true;
            case INT64S:
            case INT32U:
            case INT64U:
                if (this.range.maxValue.longValue() < value2.longValue() || this.range.minValue.longValue() > value2.longValue()) {
                    return false;
                }
                return true;
            case BYTE:
                if (this.range.maxValue.byteValue() < value2.byteValue() || this.range.minValue.byteValue() > value2.byteValue()) {
                    return false;
                }
                return true;
            case DOUBLE:
                if (this.range.maxValue.doubleValue() < value2.doubleValue() || this.range.minValue.doubleValue() > value2.doubleValue()) {
                    return false;
                }
                return true;
            default:
                if (this.range.maxValue.floatValue() < value2.floatValue() || this.range.minValue.floatValue() > value2.floatValue()) {
                    return false;
                }
                return true;
        }
    }
}
