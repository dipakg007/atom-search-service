package com.atom.controller;

import com.atom.dto.LocationDTO;
import com.atom.search.LocationService;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/locations")
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
