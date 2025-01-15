package com.atom.dto;

import lombok.Data;

@Data
public class LocationDTO {
  private Long id;
  private String name;
  private String wktCoordinates;
  private Double distance;
}
