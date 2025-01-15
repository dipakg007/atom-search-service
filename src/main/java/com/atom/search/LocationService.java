package com.atom.search;

import static com.atom.utils.LocationUtils.calculateDistance;

import com.atom.dto.LocationDTO;
import com.atom.entities.Location;
import com.atom.repository.LocationRepository;
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
}
