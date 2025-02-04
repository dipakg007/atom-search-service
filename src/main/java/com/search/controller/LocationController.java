package com.search.controller;

import com.search.dto.LocationDTO;
import com.search.service.LocationService;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
public class LocationController {

  private final LocationService locationService;

  public LocationController(LocationService locationService) {
    this.locationService = locationService;
  }

  @GetMapping("/nearby")
  public List<LocationDTO> getNearbyLocations(
      @RequestParam double latitude, @RequestParam double longitude, @RequestParam double radius) {
    return locationService.findNearby(latitude, longitude, radius);
  }
}
