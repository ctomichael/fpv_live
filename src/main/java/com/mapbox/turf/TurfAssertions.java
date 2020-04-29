package com.mapbox.turf;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.GeoJson;
import com.mapbox.geojson.Point;

public final class TurfAssertions {
    private TurfAssertions() {
    }

    @Deprecated
    public static Point getCoord(Feature obj) {
        return TurfMeta.getCoord(obj);
    }

    public static void geojsonType(GeoJson value, String type, String name) {
        if (type == null || type.length() == 0 || name == null || name.length() == 0) {
            throw new TurfException("Type and name required");
        } else if (value == null || !value.type().equals(type)) {
            throw new TurfException("Invalid input to " + name + ": must be a " + type + ", given " + (value != null ? value.type() : " null"));
        }
    }

    public static void featureOf(Feature feature, String type, String name) {
        if (name == null || name.length() == 0) {
            throw new TurfException(".featureOf() requires a name");
        } else if (feature == null || !feature.type().equals("Feature") || feature.geometry() == null) {
            throw new TurfException(String.format("Invalid input to %s, Feature with geometry required", name));
        } else if (feature.geometry() == null || !feature.geometry().type().equals(type)) {
            throw new TurfException(String.format("Invalid input to %s: must be a %s, given %s", name, type, feature.geometry().type()));
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x0047  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void collectionOf(com.mapbox.geojson.FeatureCollection r6, java.lang.String r7, java.lang.String r8) {
        /*
            r5 = 1
            r4 = 0
            if (r8 == 0) goto L_0x000a
            int r1 = r8.length()
            if (r1 != 0) goto L_0x0013
        L_0x000a:
            com.mapbox.turf.TurfException r1 = new com.mapbox.turf.TurfException
            java.lang.String r2 = "collectionOf() requires a name"
            r1.<init>(r2)
            throw r1
        L_0x0013:
            if (r6 == 0) goto L_0x0028
            java.lang.String r1 = r6.type()
            java.lang.String r2 = "FeatureCollection"
            boolean r1 = r1.equals(r2)
            if (r1 == 0) goto L_0x0028
            java.util.List r1 = r6.features()
            if (r1 != 0) goto L_0x0039
        L_0x0028:
            com.mapbox.turf.TurfException r1 = new com.mapbox.turf.TurfException
            java.lang.String r2 = "Invalid input to %s, FeatureCollection required"
            java.lang.Object[] r3 = new java.lang.Object[r5]
            r3[r4] = r8
            java.lang.String r2 = java.lang.String.format(r2, r3)
            r1.<init>(r2)
            throw r1
        L_0x0039:
            java.util.List r1 = r6.features()
            java.util.Iterator r1 = r1.iterator()
        L_0x0041:
            boolean r2 = r1.hasNext()
            if (r2 == 0) goto L_0x00a6
            java.lang.Object r0 = r1.next()
            com.mapbox.geojson.Feature r0 = (com.mapbox.geojson.Feature) r0
            if (r0 == 0) goto L_0x0062
            java.lang.String r2 = r0.type()
            java.lang.String r3 = "Feature"
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0062
            com.mapbox.geojson.Geometry r2 = r0.geometry()
            if (r2 != 0) goto L_0x0073
        L_0x0062:
            com.mapbox.turf.TurfException r1 = new com.mapbox.turf.TurfException
            java.lang.String r2 = "Invalid input to %s, Feature with geometry required"
            java.lang.Object[] r3 = new java.lang.Object[r5]
            r3[r4] = r8
            java.lang.String r2 = java.lang.String.format(r2, r3)
            r1.<init>(r2)
            throw r1
        L_0x0073:
            com.mapbox.geojson.Geometry r2 = r0.geometry()
            if (r2 == 0) goto L_0x0087
            com.mapbox.geojson.Geometry r2 = r0.geometry()
            java.lang.String r2 = r2.type()
            boolean r2 = r2.equals(r7)
            if (r2 != 0) goto L_0x0041
        L_0x0087:
            com.mapbox.turf.TurfException r1 = new com.mapbox.turf.TurfException
            java.lang.String r2 = "Invalid input to %s: must be a %s, given %s"
            r3 = 3
            java.lang.Object[] r3 = new java.lang.Object[r3]
            r3[r4] = r8
            r3[r5] = r7
            r4 = 2
            com.mapbox.geojson.Geometry r5 = r0.geometry()
            java.lang.String r5 = r5.type()
            r3[r4] = r5
            java.lang.String r2 = java.lang.String.format(r2, r3)
            r1.<init>(r2)
            throw r1
        L_0x00a6:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mapbox.turf.TurfAssertions.collectionOf(com.mapbox.geojson.FeatureCollection, java.lang.String, java.lang.String):void");
    }
}
