package com.msjc.realworld.infra.dmr.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;

@Value
@Data
@AllArgsConstructor
public class ArticleFavoriteCount {
  private Long id;
  private Integer count;
}
