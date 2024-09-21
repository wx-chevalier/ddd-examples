package icu.ngte.realworld.infra.dmr.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileData {
  @JsonIgnore private Long id;
  private String username;
  private String bio;
  private String image;
  private boolean following;
}
