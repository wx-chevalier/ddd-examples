package com.udma.core.type.pageable;

import lombok.Data;

@Data
public class SortOrder {

  private final String property;
  private final Direction direction;

  public SortOrder(String property) {
    this(property, Direction.ASC);
  }

  public SortOrder(String property, Direction direction) {
    this.property = property;
    this.direction = direction;
  }

  public SortOrder(String property, boolean asc) {
    this(property, asc ? Direction.ASC : Direction.DESC);
  }

  public enum Direction {
    ASC,
    DESC
  }
}
