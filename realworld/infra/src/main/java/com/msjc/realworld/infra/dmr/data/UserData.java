package com.msjc.realworld.infra.dmr.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserData {
  private Long id;
  private String email;
  private String username;
  private String bio;
  private String image;
}
