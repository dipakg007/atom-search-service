package com.atom.search;

import com.atom.dto.LocationDTO;
import com.atom.entities.Location;
import com.atom.repository.LocationRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.WKTWriter;
import org.springframework.stereotype.Service;

@Service
public class LocationService {

  private final LocationRepository repository;

  public LocationService(LocationRepository repository) {
    this.repository = repository;
  }

  public List<LocationDTO> findNearby(Double latitude, Double longitude, Double radius) {
    return repository.findNearby(latitude, longitude, radius).stream()
        .map(location -> convertToDTO(location, latitude, longitude))
        .collect(Collectors.toList());
  }

  private LocationDTO convertToDTO(Location location, double latitude, double longitude) {
    LocationDTO dto = new LocationDTO();
    dto.setId(location.getId());
    dto.setName(location.getName());

    if (location.getCoordinates() != null) {
      Point destinationPoint = location.getCoordinates();
      WKTWriter writer = new WKTWriter();
      dto.setWktCoordinates(writer.write(destinationPoint));
      double destinationLatitude = destinationPoint.getY(); // y = Latitude
      double destinationLongitude = destinationPoint.getX(); // x = Longitude
      double distance =
          calculateDistance(latitude, longitude, destinationLatitude, destinationLongitude);
      dto.setDistance(distance);
    }

    return dto;
  }

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
