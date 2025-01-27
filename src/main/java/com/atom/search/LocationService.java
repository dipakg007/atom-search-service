package com.atom.search;

import static com.atom.utils.LocationUtils.calculateDistance;

import com.atom.dto.LocationDTO;
import com.atom.entities.Location;
import com.atom.repository.LocationRepository;
import java.util.List;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.WKTWriter;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class LocationService {

  private final LocationRepository repository;
  private final RedisService redisService;

  public LocationService(LocationRepository repository, RedisService redisService) {
    this.repository = repository;
    this.redisService = redisService;
  }

  public List<LocationDTO> findNearby(Double latitude, Double longitude, Double radius) {
    String key = latitude + ":" + longitude + ":" + radius;
    List<LocationDTO> cacheData = redisService.getData(key);
    if (!CollectionUtils.isEmpty(cacheData)) return cacheData;
    List<LocationDTO> dbData =
        repository.findNearby(latitude, longitude, radius).stream()
            .map(location -> convertToDTO(location, latitude, longitude))
            .toList();
    redisService.saveData(key, dbData);
    return dbData;
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
