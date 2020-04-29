package dji.component.map;

import com.dji.mapkit.lbs.configuration.LocationConfiguration;

public interface IMapService {
    public static final String COMPONENT_NAME = "MapService";

    void amapAvailability(boolean z);

    boolean canUseAMap();

    boolean canUseMapbox();

    LocationConfiguration dynamicHomeConfiguration(boolean z);

    LocationConfiguration getDefaultConfiguration(boolean z, boolean z2);

    boolean isAnyProviderEnabled();

    boolean isSupportingAMap();
}
