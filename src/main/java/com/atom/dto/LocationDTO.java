package com.atom.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class LocationDTO implements Serializable {
  private Long id;
  private String name;
  private String wktCoordinates;
  private Double distance;
}
