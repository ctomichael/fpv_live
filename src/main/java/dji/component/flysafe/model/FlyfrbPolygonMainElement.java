package dji.component.flysafe.model;

import android.support.annotation.Keep;
import com.dji.mapkit.core.models.DJILatLng;
import dji.component.flysafe.model.FlyfrbPolygonItem;
import dji.component.flysafe.util.NFZLogUtil;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.util.BytesUtil;
import dji.thirdparty.afinal.annotation.sqlite.Table;
import dji.thirdparty.afinal.annotation.sqlite.Transient;
import java.util.ArrayList;
import java.util.List;

@Keep
@EXClassNullAway
@Table(name = "airmap_geofence_infos")
public class FlyfrbPolygonMainElement extends FlyForbidElement implements IPreciseStaticDbTable {
    @Transient
    protected List<FlyfrbPolygonItem> mFlyfrbPolygonItems = new ArrayList();
    public String phone = "";
    public byte[] polygon_shape;
    public int search_radius;
    public boolean tower = false;

    public List<FlyfrbPolygonItem> getFlyfrbPolygonItems() {
        if (this.mFlyfrbPolygonItems == null || this.mFlyfrbPolygonItems.size() == 0) {
            try {
                convertItems();
            } catch (Exception e) {
                NFZLogUtil.savedLOGE("nfz convert polygon item error, area_id: " + this.area_id);
            }
        }
        return this.mFlyfrbPolygonItems;
    }

    private void convertItems() {
        if (this.polygon_shape != null && this.polygon_shape.length != 0) {
            byte b = this.polygon_shape[0];
            int pointIndex = 0;
            int arrayIndex = 1;
            while (pointIndex != b && arrayIndex < this.polygon_shape.length) {
                FlyfrbPolygonItem item = new FlyfrbPolygonItem();
                int arrayIndex2 = arrayIndex + 1;
                item.type = FlyfrbPolygonItem.SubAreaShape.find(this.polygon_shape[arrayIndex]);
                item.length = BytesUtil.getShort(this.polygon_shape, arrayIndex2);
                int arrayIndex3 = arrayIndex2 + 2;
                int arrayIndex4 = arrayIndex3 + 1;
                item.sub_area_id = this.polygon_shape[arrayIndex3];
                item.height = BytesUtil.getShort(this.polygon_shape, arrayIndex4);
                int arrayIndex5 = arrayIndex4 + 2;
                item.radius = BytesUtil.getInt(this.polygon_shape, arrayIndex5);
                int arrayIndex6 = arrayIndex5 + 4;
                item.point_num = this.polygon_shape[arrayIndex6];
                int arrayIndex7 = arrayIndex6 + 1;
                for (int i = 0; i != item.point_num; i++) {
                    item.mConvertedPoints.add(new DJILatLng(((double) BytesUtil.getInt(this.polygon_shape, arrayIndex7 + 4)) / 1000000.0d, ((double) BytesUtil.getInt(this.polygon_shape, arrayIndex7)) / 1000000.0d));
                    arrayIndex7 += 8;
                }
                this.mFlyfrbPolygonItems.add(item);
                pointIndex++;
                arrayIndex = arrayIndex7;
            }
        }
    }

    public int getSearchRadius() {
        if (this.search_radius != 0) {
            return this.search_radius;
        }
        return this.radius;
    }
}
