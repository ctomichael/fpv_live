package com.dji.mapkit.core;

import com.dji.mapkit.core.Mapkit;
import com.dji.mapkit.core.providers.MapProvider;
import java.util.ArrayList;
import java.util.List;

public class MapkitOptions {
    private MapProvider mapProvider;
    private int mapType = 1;
    @Mapkit.MapProviderConstant
    private int provider;
    private List<Integer> providerList;

    public MapProvider getMapProvider() {
        return this.mapProvider;
    }

    private MapkitOptions(@Mapkit.MapProviderConstant int provider2) {
        this.provider = provider2;
    }

    private MapkitOptions() {
    }

    public MapkitOptions(int mapType2, MapProvider mapProvider2) {
        this.mapType = mapType2;
        this.mapProvider = mapProvider2;
    }

    public MapkitOptions(int mapType2, @Mapkit.MapProviderConstant List<Integer> providerList2) {
        this.mapType = mapType2;
        this.providerList = providerList2;
    }

    @Mapkit.MapProviderConstant
    public int getProvider() {
        return this.provider;
    }

    public int getMapType() {
        return this.mapType;
    }

    @Mapkit.MapProviderConstant
    public List<Integer> getProviderList() {
        return this.providerList;
    }

    public static final class Builder {
        private int mapType = 1;
        @Mapkit.MapProviderConstant
        private List<Integer> providerList = new ArrayList();

        public Builder() {
            this.providerList.clear();
        }

        public Builder mapType(int mapType2) {
            this.mapType = mapType2;
            return this;
        }

        public Builder addMapProvider(@Mapkit.MapProviderConstant int provider) {
            this.providerList.add(Integer.valueOf(provider));
            return this;
        }

        public MapkitOptions build() {
            return new MapkitOptions(this.mapType, this.providerList);
        }
    }
}
