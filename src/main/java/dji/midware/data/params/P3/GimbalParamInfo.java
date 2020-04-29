package dji.midware.data.params.P3;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class GimbalParamInfo extends ParamInfo {
    public String strValue = "";

    public String toString() {
        return "name=" + this.name + " hash=" + this.hash + " index=" + this.index + " value=" + this.value + " strValue=" + this.strValue + " typeid=" + this.typeId.toString() + " type=" + this.type.getSimpleName() + " size=" + this.size + " attr=" + this.attribute.toString() + " range=" + this.range.toString();
    }

    public static GimbalParamInfo copyOf(ParamInfo _info) {
        GimbalParamInfo gimbalInfo = new GimbalParamInfo();
        gimbalInfo.index = _info.index;
        gimbalInfo.typeId = _info.typeId;
        gimbalInfo.type = _info.type;
        gimbalInfo.size = _info.size;
        gimbalInfo.attribute = _info.attribute;
        gimbalInfo.range = _info.range;
        gimbalInfo.value = _info.value;
        gimbalInfo.setvalue = _info.setvalue;
        gimbalInfo.name = _info.name;
        return gimbalInfo;
    }
}
