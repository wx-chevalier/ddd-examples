package icu.ngte.realworld.infra.dmr.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentData {
  private Long id;
  private String body;
  @JsonIgnore private String articleId;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  @JsonProperty("author")
  private ProfileData profileData;
}
