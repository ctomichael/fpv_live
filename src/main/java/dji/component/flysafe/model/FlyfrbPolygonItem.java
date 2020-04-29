package dji.component.flysafe.model;

import android.support.annotation.Keep;
import com.dji.mapkit.core.models.DJILatLng;
import dji.fieldAnnotation.EXClassNullAway;
import dji.flysafe.LimitAreaShape;
import java.util.ArrayList;
import java.util.List;

@Keep
@EXClassNullAway
public class FlyfrbPolygonItem {
    public int height;
    public int length;
    public List<DJILatLng> mConvertedPoints = new ArrayList();
    public int point_num;
    public int radius;
    public int sub_area_id;
    public SubAreaShape type = SubAreaShape.SUB_POLYGON;

    public List<DJILatLng> getConvertedPoints() {
        return this.mConvertedPoints;
    }

    @Keep
    public enum SubAreaShape {
        SUB_POLYGON(0),
        CIRCLE(1),
        OTHER(100);
        
        private int data;

        private SubAreaShape(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static SubAreaShape find(int b) {
            SubAreaShape result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }

        public static SubAreaShape findByProtobufType(LimitAreaShape limitAreaShape) {
            if (limitAreaShape == LimitAreaShape.Circle) {
                return CIRCLE;
            }
            if (limitAreaShape == LimitAreaShape.Polygon) {
                return SUB_POLYGON;
            }
            return OTHER;
        }
    }
}
