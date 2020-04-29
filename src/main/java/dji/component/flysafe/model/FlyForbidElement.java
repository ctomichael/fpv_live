package dji.component.flysafe.model;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.forbid.DJISetFlyForbidAreaModel;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import java.util.ArrayList;
import java.util.List;

@Keep
@EXClassNullAway
public class FlyForbidElement implements IFlyfrbBaseDb, IFlyfrbAreaTable {
    public int area_id;
    public long begin_at;
    public String city = "";
    public int country;
    public int disable;
    public long end_at;
    public int id;
    public boolean is_unlock;
    public double lat;
    public int level;
    public long limit_height = 0;
    public double lng;
    public String name = "";
    public String points = "";
    public int radius;
    public int shape;
    public int type;
    public long updated_at;
    public int warning;

    public FlyForbidElement copyOf() {
        FlyForbidElement tmp = new FlyForbidElement();
        tmp.area_id = this.area_id;
        tmp.type = this.type;
        tmp.shape = this.shape;
        tmp.lat = this.lat;
        tmp.lng = this.lng;
        tmp.radius = this.radius;
        tmp.warning = this.warning;
        tmp.level = this.level;
        tmp.disable = this.disable;
        tmp.updated_at = this.updated_at;
        tmp.begin_at = this.begin_at;
        tmp.end_at = this.end_at;
        tmp.name = this.name;
        tmp.country = this.country;
        tmp.city = this.city;
        tmp.points = this.points;
        return tmp;
    }

    public DJISetFlyForbidAreaModel copyToSentModel() {
        DJISetFlyForbidAreaModel sentAreaModel = new DJISetFlyForbidAreaModel();
        sentAreaModel.latitude = (int) (this.lat * 1000000.0d);
        sentAreaModel.longitude = (int) (this.lng * 1000000.0d);
        sentAreaModel.radius = this.radius;
        if (sentAreaModel.radius > 65530) {
            sentAreaModel.radius = 65530;
        }
        sentAreaModel.contryCode = this.country;
        sentAreaModel.type = this.type;
        if (((DataOsdGetPushCommon.getInstance().getFlycVersion() & 255) < 9) && sentAreaModel.type > 2) {
            sentAreaModel.type = 2;
        }
        sentAreaModel.id = this.area_id;
        return sentAreaModel;
    }

    public void handleNullField() {
        if (this.name == null) {
            this.name = "";
        }
        if (this.city == null) {
            this.city = "";
        }
        if (this.points == null) {
            this.points = "";
        }
    }

    public int getSearchRadius() {
        return this.radius;
    }

    public List<FlyfrbPolygonItem> getFlyfrbPolygonItems() {
        return new ArrayList();
    }

    public double getLat() {
        return this.lat;
    }

    public double getLng() {
        return this.lng;
    }

    public enum AreaShape {
        CIRCLE(0),
        SINGLE_POLYGON(1),
        MULTI_POLYGON(12),
        OTHER(100);
        
        private int data;

        private AreaShape(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static AreaShape find(int b) {
            AreaShape result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
