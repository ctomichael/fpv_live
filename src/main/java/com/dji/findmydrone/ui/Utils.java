package com.dji.findmydrone.ui;

public class Utils {
    public static boolean isGPSValid(double lat, double lng) {
        return Math.abs(lat) > 9.999999747378752E-5d && Math.abs(lng) > 9.999999747378752E-5d;
    }
}
