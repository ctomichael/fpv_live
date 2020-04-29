package dji.component.flysafe;

import com.dji.mapkit.core.models.DJILatLng;
import com.dji.mapkit.core.utils.DJIMapLocationUtils;
import dji.component.flysafe.FlyForbidBasePainter;
import dji.component.flysafe.model.FlyForbidDrawParam;
import java.util.List;

public class FlyfrbAreaClickUtil {
    public static boolean isInFlyforbidArea(DJILatLng latLng, FlyForbidDrawParam drawParam) {
        float distance = DJIMapLocationUtils.getDistance(latLng, drawParam.getCenterPoint());
        if (drawParam.getDrawShapeType() == FlyForbidBasePainter.DrawShape.CIRCLE && distance <= ((float) drawParam.getRadius())) {
            return true;
        }
        if (drawParam.getDrawShapeType() != FlyForbidBasePainter.DrawShape.POLYGON || !ptInPolygon(latLng, drawParam.getConvertedPoints())) {
            return false;
        }
        return true;
    }

    private static boolean ptInPolygon(DJILatLng point, List<DJILatLng> polygonPoints) {
        if (point == null || polygonPoints == null || polygonPoints.isEmpty()) {
            return false;
        }
        int nCross = 0;
        int pSize = polygonPoints.size();
        for (int i = 0; i < pSize; i++) {
            DJILatLng p1 = polygonPoints.get(i);
            DJILatLng p2 = polygonPoints.get((i + 1) % polygonPoints.size());
            if (p1.longitude != p2.longitude && point.longitude >= Math.min(p1.longitude, p2.longitude) && point.longitude < Math.max(p1.longitude, p2.longitude) && (((point.longitude - p1.longitude) * (p2.latitude - p1.latitude)) / (p2.longitude - p1.longitude)) + p1.latitude > point.latitude) {
                nCross++;
            }
        }
        return nCross % 2 == 1;
    }
}
