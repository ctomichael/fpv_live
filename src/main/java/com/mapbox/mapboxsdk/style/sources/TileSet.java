package com.mapbox.mapboxsdk.style.sources;

import android.support.annotation.NonNull;
import android.support.annotation.Size;
import com.mapbox.mapboxsdk.geometry.LatLng;
import dji.publics.protocol.ResponseBase;
import java.util.HashMap;
import java.util.Map;

public class TileSet {
    private String attribution;
    private Float[] bounds;
    private Float[] center;
    private String[] data;
    private String description;
    private String encoding;
    private String[] grids;
    private String legend;
    private Float maxZoom;
    private Float minZoom;
    private String name;
    private String scheme;
    private String template;
    private final String tilejson;
    private final String[] tiles;
    private String version;

    public TileSet(String tilejson2, String... tiles2) {
        this.tilejson = tilejson2;
        this.tiles = tiles2;
    }

    public String getTilejson() {
        return this.tilejson;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description2) {
        this.description = description2;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version2) {
        this.version = version2;
    }

    public String getAttribution() {
        return this.attribution;
    }

    public void setAttribution(String attribution2) {
        this.attribution = attribution2;
    }

    public String getTemplate() {
        return this.template;
    }

    public void setTemplate(String template2) {
        this.template = template2;
    }

    public String getLegend() {
        return this.legend;
    }

    public void setLegend(String legend2) {
        this.legend = legend2;
    }

    public String getScheme() {
        return this.scheme;
    }

    public void setScheme(String scheme2) {
        this.scheme = scheme2;
    }

    public String[] getTiles() {
        return this.tiles;
    }

    public String[] getGrids() {
        return this.grids;
    }

    public void setGrids(String... grids2) {
        this.grids = grids2;
    }

    public String[] getData() {
        return this.data;
    }

    public void setData(String... data2) {
        this.data = data2;
    }

    public float getMinZoom() {
        return this.minZoom.floatValue();
    }

    public void setMinZoom(float minZoom2) {
        this.minZoom = Float.valueOf(minZoom2);
    }

    public float getMaxZoom() {
        return this.maxZoom.floatValue();
    }

    public void setMaxZoom(float maxZoom2) {
        this.maxZoom = Float.valueOf(maxZoom2);
    }

    public Float[] getBounds() {
        return this.bounds;
    }

    public void setBounds(@Size(4) Float... bounds2) {
        this.bounds = bounds2;
    }

    public String getEncoding() {
        return this.encoding;
    }

    public void setEncoding(String encoding2) {
        this.encoding = encoding2;
    }

    public Float[] getCenter() {
        return this.center;
    }

    public void setCenter(@Size(2) Float... center2) {
        this.center = center2;
    }

    public void setCenter(@NonNull LatLng center2) {
        this.center = new Float[]{Float.valueOf((float) center2.getLongitude()), Float.valueOf((float) center2.getLatitude())};
    }

    /* access modifiers changed from: package-private */
    @NonNull
    public Map<String, Object> toValueObject() {
        Map<String, Object> result = new HashMap<>();
        result.put("tilejson", this.tilejson);
        result.put("tiles", this.tiles);
        if (this.name != null) {
            result.put("name", this.name);
        }
        if (this.description != null) {
            result.put(ResponseBase.STRING_DESCRIPTION, this.description);
        }
        if (this.version != null) {
            result.put("version", this.version);
        }
        if (this.attribution != null) {
            result.put("attribution", this.attribution);
        }
        if (this.template != null) {
            result.put("template", this.template);
        }
        if (this.legend != null) {
            result.put("legend", this.legend);
        }
        if (this.scheme != null) {
            result.put("scheme", this.scheme);
        }
        if (this.grids != null) {
            result.put("grids", this.grids);
        }
        if (this.data != null) {
            result.put("data", this.data);
        }
        if (this.minZoom != null) {
            result.put("minzoom", this.minZoom);
        }
        if (this.maxZoom != null) {
            result.put("maxzoom", this.maxZoom);
        }
        if (this.bounds != null) {
            result.put("bounds", this.bounds);
        }
        if (this.center != null) {
            result.put("center", this.center);
        }
        if (this.encoding != null) {
            result.put("encoding", this.encoding);
        }
        return result;
    }
}
