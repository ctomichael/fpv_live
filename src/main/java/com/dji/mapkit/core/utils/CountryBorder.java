package com.dji.mapkit.core.utils;

import java.util.ArrayList;

public class CountryBorder {

    public static class CountryItem {
        public Geometry geometry;
        public String id;
        public String name;
    }

    public static class Geometry {
        public ArrayList<ArrayList<ArrayList<ArrayList<Double>>>> coordinates;
        public String type;
    }
}
