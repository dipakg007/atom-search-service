package com.search.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class LocationUtils {
  public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
    final int R = 6371;

    double latDistance = Math.toRadians(lat2 - lat1);
    double lonDistance = Math.toRadians(lon2 - lon1);

    double a =
        Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
            + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2)
                * Math.sin(lonDistance / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    double distance = R * c;

    BigDecimal bd = new BigDecimal(distance).setScale(4, RoundingMode.HALF_UP);

    return bd.doubleValue();
  }
}
