package com.mapbox.mapboxsdk.attribution;

import android.support.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class Attribution {
    static final List<String> IMPROVE_MAP_URLS = new ArrayList();
    static final String MAPBOX_URL = "https://www.mapbox.com/about/maps/";
    private static final String OPENSTREETMAP = "OpenStreetMap";
    private static final String OPENSTREETMAP_ABBR = "OSM";
    static final String TELEMETRY = "Telemetry Settings";
    static final String TELEMETRY_URL = "https://www.mapbox.com/telemetry/";
    private String title;
    private String url;

    static {
        IMPROVE_MAP_URLS.add("https://www.mapbox.com/feedback/");
        IMPROVE_MAP_URLS.add("https://www.mapbox.com/map-feedback/");
        IMPROVE_MAP_URLS.add("https://apps.mapbox.com/feedback/");
    }

    Attribution(String title2, String url2) {
        this.title = title2;
        this.url = url2;
    }

    public String getTitle() {
        return this.title;
    }

    public String getTitleAbbreviated() {
        if (this.title.equals(OPENSTREETMAP)) {
            return OPENSTREETMAP_ABBR;
        }
        return this.title;
    }

    public String getUrl() {
        return this.url;
    }

    public boolean equals(@Nullable Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Attribution that = (Attribution) o;
        if (this.title == null ? that.title != null : !this.title.equals(that.title)) {
            return false;
        }
        if (this.url != null) {
            return this.url.equals(that.url);
        }
        if (that.url != null) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result;
        int i = 0;
        if (this.title != null) {
            result = this.title.hashCode();
        } else {
            result = 0;
        }
        int i2 = result * 31;
        if (this.url != null) {
            i = this.url.hashCode();
        }
        return i2 + i;
    }
}
