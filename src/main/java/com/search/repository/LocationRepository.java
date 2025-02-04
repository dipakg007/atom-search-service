package com.search.repository;

import com.search.entities.Location;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface LocationRepository extends CrudRepository<Location, Long> {

  @Query(
      value =
          "SELECT * FROM location WHERE ST_DWithin(coordinates, ST_SetSRID(ST_MakePoint(:lng, :lat), 4326), :radius)",
      nativeQuery = true)
  List<Location> findNearby(
      @Param("lat") double latitude,
      @Param("lng") double longitude,
      @Param("radius") double radius);
}
