package dji.midware.data.params.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.P3.DataFlycGetParamInfo;
import dji.midware.natives.GroudStation;
import dji.midware.util.BytesUtil;

@Keep
@EXClassNullAway
public class ParamInfoBean {
    public int attribute;
    public String defaultValue;
    public int index;
    public String maxValue;
    public String minValue;
    public String name;
    public int size;
    public int typeID;

    public ParamInfo getParamInfo() {
        DataFlycGetParamInfo.TypeId mtypeId = DataFlycGetParamInfo.TypeId.find(this.typeID);
        if (mtypeId.value() == DataFlycGetParamInfo.TypeId.OTHER.value()) {
            return null;
        }
        ParamInfo paramInfo = new ParamInfo();
        RangeModel range = new RangeModel();
        switch (mtypeId) {
            case INT08S:
            case INT16S:
            case INT32S:
            case INT08U:
            case INT16U:
                paramInfo.type = Integer.class;
                range.minValue = Integer.valueOf(this.minValue);
                range.maxValue = Integer.valueOf(this.maxValue);
                range.defaultValue = Integer.valueOf(this.defaultValue);
                paramInfo.range = range;
                break;
            case INT64S:
            case INT32U:
            case INT64U:
                paramInfo.type = Long.class;
                range.minValue = Long.valueOf(this.minValue);
                range.maxValue = Long.valueOf(this.maxValue);
                range.defaultValue = Long.valueOf(this.defaultValue);
                paramInfo.range = range;
                break;
            case BYTE:
                paramInfo.type = Byte.class;
                range.minValue = Byte.valueOf(this.minValue);
                range.maxValue = Byte.valueOf(this.maxValue);
                range.defaultValue = Byte.valueOf(this.defaultValue);
                paramInfo.range = range;
                break;
            case DOUBLE:
                paramInfo.type = Double.class;
                range.minValue = Double.valueOf(this.minValue);
                range.maxValue = Double.valueOf(this.maxValue);
                range.defaultValue = Double.valueOf(this.defaultValue);
                paramInfo.range = range;
                break;
            default:
                paramInfo.type = Float.class;
                range.minValue = Float.valueOf(this.minValue);
                range.maxValue = Float.valueOf(this.maxValue);
                range.defaultValue = Float.valueOf(this.defaultValue);
                paramInfo.range = range;
                break;
        }
        paramInfo.index = this.index;
        paramInfo.typeId = mtypeId;
        paramInfo.size = this.size;
        paramInfo.attribute = DataFlycGetParamInfo.Attribute.find(this.attribute);
        paramInfo.name = this.name;
        paramInfo.value = 0;
        paramInfo.hash = GroudStation.native_hashFromString(BytesUtil.getBytes(this.name));
        return paramInfo;
    }
}
