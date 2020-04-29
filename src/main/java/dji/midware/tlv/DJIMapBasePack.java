package dji.midware.tlv;

import android.util.SparseArray;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class DJIMapBasePack {
    public short crc;
    public int mapItemCount = 0;
    protected final SparseArray<MapBaseItem> mapItems = new SparseArray<>();
    public int mapStartIndex = 0;
    public DJISTLVBasePack tlvHeader;

    public static DJIMapBasePack testData() {
        DJIMapBasePack mapPack = new DJIMapBasePack();
        mapPack.tlvHeader = DJISTLVBasePack.testData();
        for (int i = 0; i < 5; i++) {
            mapPack.addMapItem(MapBaseItem.testData());
        }
        mapPack.mapStartIndex = 0;
        mapPack.mapItemCount = mapPack.mapItemSize();
        mapPack.crc = 3;
        return mapPack;
    }

    public synchronized void addMapItem(MapBaseItem mapItem) {
        this.mapItems.put(mapItemSize(), mapItem);
    }

    public int mapItemSize() {
        return this.mapItems.size();
    }

    public MapBaseItem getMapItem(int key) {
        return this.mapItems.get(key);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{ ").append("tlv_header:").append(this.tlvHeader == null ? "null" : this.tlvHeader.toString()).append(" ").append("mapStartIndex:").append(this.mapStartIndex).append(" ").append("mapItemCount:").append(this.mapItemCount).append(" ");
        sb.append("mapItems:");
        if (mapItemSize() > 0) {
            for (int i = 0; i < mapItemSize(); i++) {
                sb.append("\n\t{ ").append(getMapItem(i).toString()).append(" }");
            }
        } else {
            sb.append("null");
        }
        sb.append(" ");
        sb.append("\n\tcrc:").append((int) this.crc).append(" ").append(" }");
        return sb.toString();
    }

    public static class MapBaseItem {
        public long offset;
        public int type;
        public float version;

        public MapBaseItem() {
        }

        public MapBaseItem(int type2, float version2, long offset2) {
            this.type = type2;
            this.version = version2;
            this.offset = offset2;
        }

        public int length() {
            return 16;
        }

        public static MapBaseItem testData() {
            MapBaseItem mapItem = new MapBaseItem();
            mapItem.type = 1;
            mapItem.version = 1.0f;
            mapItem.offset = 16;
            return mapItem;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("{ ").append("type:").append(this.type).append(" ").append("version:").append(this.version).append(" ").append("offset:").append(this.offset).append(" ").append(" }");
            return sb.toString();
        }
    }
}
