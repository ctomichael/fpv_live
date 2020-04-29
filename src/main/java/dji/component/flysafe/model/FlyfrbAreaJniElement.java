package dji.component.flysafe.model;

import android.support.annotation.Keep;
import com.dji.mapkit.core.models.DJILatLng;
import dji.component.flysafe.model.FlyfrbPolygonItem;
import dji.component.flysafe.util.ProtobufHelper;
import dji.flysafe.LimitArea;
import dji.flysafe.LocationCoordinate;

@Keep
public class FlyfrbAreaJniElement extends FlyfrbPolygonMainElement {
    public int source;

    public static FlyfrbAreaJniElement convertFromProtobuf(LimitArea areaDataProtobuf) {
        int i;
        int i2 = 1;
        FlyfrbAreaJniElement element = new FlyfrbAreaJniElement();
        element.area_id = (int) ProtobufHelper.toLong(areaDataProtobuf.area_id);
        if (areaDataProtobuf.type != null) {
            element.type = areaDataProtobuf.type.getValue();
        }
        if (areaDataProtobuf.shape != null) {
            element.shape = areaDataProtobuf.shape.getValue();
        }
        element.lat = ProtobufHelper.toDouble(areaDataProtobuf.latitude);
        element.lng = ProtobufHelper.toDouble(areaDataProtobuf.longitude);
        element.radius = ProtobufHelper.toInt(areaDataProtobuf.radius);
        element.limit_height = ProtobufHelper.toLong(areaDataProtobuf.limit_height);
        if (ProtobufHelper.toBool(areaDataProtobuf.is_warning)) {
            i = 1;
        } else {
            i = 0;
        }
        element.warning = i;
        if (areaDataProtobuf.level != null) {
            element.level = areaDataProtobuf.level.getValue();
        }
        if (!ProtobufHelper.toBool(areaDataProtobuf.is_disable)) {
            i2 = 0;
        }
        element.disable = i2;
        element.updated_at = ProtobufHelper.toLong(areaDataProtobuf.updated_time);
        element.begin_at = ProtobufHelper.toLong(areaDataProtobuf.begin_time);
        element.end_at = ProtobufHelper.toLong(areaDataProtobuf.end_time);
        if (areaDataProtobuf.name != null) {
            element.name = areaDataProtobuf.name;
        }
        element.country = ProtobufHelper.toInt(areaDataProtobuf.country);
        if (areaDataProtobuf.city != null) {
            element.city = areaDataProtobuf.city;
        }
        if (areaDataProtobuf.point_desc != null) {
            element.points = areaDataProtobuf.point_desc;
        }
        element.search_radius = (int) ProtobufHelper.toFloat(areaDataProtobuf.search_radius);
        element.is_unlock = ProtobufHelper.toBool(areaDataProtobuf.is_unlocked);
        if (areaDataProtobuf.phone_number != null) {
            element.phone = areaDataProtobuf.phone_number;
        }
        if (areaDataProtobuf.source != null) {
            element.source = areaDataProtobuf.source.getValue();
        }
        if (areaDataProtobuf.sub_shapes != null) {
            for (LimitArea subAreaProto : areaDataProtobuf.sub_shapes) {
                FlyfrbPolygonItem subItem = new FlyfrbPolygonItem();
                subItem.sub_area_id = (int) ProtobufHelper.toLong(subAreaProto.sub_area_id);
                if (subAreaProto.shape != null) {
                    subItem.type = FlyfrbPolygonItem.SubAreaShape.findByProtobufType(subAreaProto.shape);
                }
                subItem.radius = ProtobufHelper.toInt(subAreaProto.radius);
                if (subAreaProto.polygon_points != null) {
                    for (LocationCoordinate coordinateProto : subAreaProto.polygon_points) {
                        DJILatLng latLng = new DJILatLng(ProtobufHelper.toDouble(coordinateProto.latitude), ProtobufHelper.toDouble(coordinateProto.longitude));
                        if (latLng.isAvailable()) {
                            subItem.mConvertedPoints.add(latLng);
                        }
                    }
                }
                subItem.height = (int) ProtobufHelper.toLong(subAreaProto.limit_height);
                element.mFlyfrbPolygonItems.add(subItem);
            }
        }
        return element;
    }
}
