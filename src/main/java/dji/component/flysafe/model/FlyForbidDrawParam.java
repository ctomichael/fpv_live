package dji.component.flysafe.model;

import com.dji.mapkit.core.models.DJILatLng;
import dji.component.flysafe.FlyForbidBasePainter;
import dji.component.flysafe.FlyForbidProtocol;
import dji.component.flysafe.model.FlyfrbPolygonItem;
import java.util.ArrayList;
import java.util.List;

public class FlyForbidDrawParam {
    private boolean isSpecialUnlock;
    private boolean isUnlocked;
    private Object mAreaMapObject = null;
    private DJILatLng mCenterPoint = new DJILatLng(0.0d, 0.0d);
    private List<DJILatLng> mConvertedPoints = new ArrayList();
    private FlyForbidBasePainter.DrawShape mDrawShapeType = FlyForbidBasePainter.DrawShape.CIRCLE;
    private long mEndAt;
    private int mHeight;
    private FlyForbidProtocol.LevelType mLevel;
    private String mPoints;
    private int mRadius;
    private int mSearchRadius;
    private long mStartAt;
    private int mType;

    public static FlyForbidDrawParam copyFromFlyForbidElement(FlyForbidElement element) {
        FlyForbidDrawParam drawParam = new FlyForbidDrawParam();
        drawParam.mCenterPoint = new DJILatLng(element.lat, element.lng);
        drawParam.mRadius = element.radius;
        drawParam.mSearchRadius = element.getSearchRadius();
        drawParam.mLevel = FlyForbidProtocol.LevelType.ofValue(element.level);
        drawParam.mHeight = 0;
        drawParam.mType = element.type;
        if (drawParam.mType == 14 || drawParam.mType == 19) {
            drawParam.mDrawShapeType = FlyForbidBasePainter.DrawShape.SIMPLE_MARKER;
        } else {
            drawParam.mDrawShapeType = FlyForbidBasePainter.DrawShape.CIRCLE;
        }
        drawParam.isUnlocked = element.is_unlock;
        drawParam.mStartAt = element.begin_at;
        drawParam.mEndAt = element.end_at;
        drawParam.mPoints = element.points;
        return drawParam;
    }

    public static FlyForbidDrawParam copyFromFlyfrbPolygonItem(FlyfrbPolygonItem item, int itemLevel, int searchRadius, boolean isUnlocked2, long startAt, long endAt) {
        FlyForbidDrawParam drawParam = new FlyForbidDrawParam();
        List<DJILatLng> polygonPoints = item.getConvertedPoints();
        if (polygonPoints != null && !polygonPoints.isEmpty()) {
            drawParam.mCenterPoint = polygonPoints.get(0);
            drawParam.mConvertedPoints = new ArrayList(polygonPoints);
        }
        drawParam.mRadius = item.radius;
        drawParam.mSearchRadius = searchRadius;
        drawParam.mLevel = FlyForbidProtocol.LevelType.ofValue(itemLevel);
        drawParam.mHeight = item.height;
        if (drawParam.mHeight > 0) {
            drawParam.mHeight -= 5;
        }
        drawParam.mType = -1;
        if (item.type == FlyfrbPolygonItem.SubAreaShape.SUB_POLYGON) {
            drawParam.mDrawShapeType = FlyForbidBasePainter.DrawShape.POLYGON;
        } else {
            drawParam.mDrawShapeType = FlyForbidBasePainter.DrawShape.CIRCLE;
        }
        drawParam.isUnlocked = isUnlocked2;
        drawParam.mStartAt = startAt;
        drawParam.mEndAt = endAt;
        return drawParam;
    }

    public static FlyForbidDrawParam copyFromFlyfrbCircleSpecialUnlock(DJILatLng latLng, double radius) {
        FlyForbidDrawParam drawParam = new FlyForbidDrawParam();
        drawParam.isSpecialUnlock = true;
        drawParam.mRadius = (int) radius;
        drawParam.mSearchRadius = (int) radius;
        drawParam.mCenterPoint = new DJILatLng(latLng.getLatitude(), latLng.getLongitude());
        drawParam.mDrawShapeType = FlyForbidBasePainter.DrawShape.CIRCLE;
        drawParam.mLevel = FlyForbidProtocol.LevelType.STRONG_WARNING;
        return drawParam;
    }

    public static FlyForbidDrawParam copyFromFlyfrbPolygonSpecialUnlock(List<DJILatLng> points) {
        FlyForbidDrawParam drawParam = new FlyForbidDrawParam();
        drawParam.isSpecialUnlock = true;
        drawParam.mConvertedPoints = new ArrayList(points);
        drawParam.mDrawShapeType = FlyForbidBasePainter.DrawShape.POLYGON;
        drawParam.mLevel = FlyForbidProtocol.LevelType.STRONG_WARNING;
        return drawParam;
    }

    public DJILatLng getCenterPoint() {
        return this.mCenterPoint;
    }

    public int getRadius() {
        return this.mRadius;
    }

    public int getSearchRadius() {
        return this.mSearchRadius;
    }

    public FlyForbidProtocol.LevelType getLevel() {
        return this.mLevel;
    }

    public int getHeight() {
        return this.mHeight;
    }

    public int getType() {
        return this.mType;
    }

    public Object getAreaMapObject() {
        return this.mAreaMapObject;
    }

    public void setAreaMapObject(Object areaMapObject) {
        this.mAreaMapObject = areaMapObject;
    }

    public FlyForbidBasePainter.DrawShape getDrawShapeType() {
        return this.mDrawShapeType;
    }

    public boolean isUnlocked() {
        return this.isUnlocked;
    }

    public long getStartAt() {
        return this.mStartAt;
    }

    public long getEndAt() {
        return this.mEndAt;
    }

    public String getPoints() {
        return this.mPoints;
    }

    public List<DJILatLng> getConvertedPoints() {
        return this.mConvertedPoints;
    }
}
