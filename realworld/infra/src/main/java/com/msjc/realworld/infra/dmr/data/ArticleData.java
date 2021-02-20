package com.msjc.realworld.infra.dmr.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleData {
  private Long id;
  private String slug;
  private String title;
  private String description;
  private String body;
  private boolean favorited;
  private int favoritesCount;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private List<String> tagList;

  @JsonProperty("author")
  private ProfileData profileData;
}
