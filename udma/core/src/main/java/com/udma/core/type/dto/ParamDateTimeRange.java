package com.udma.core.type.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParamDateTimeRange {

  private String dateTimeProperty;

  // 起始时间
  private LocalDateTime startDateTime;

  // 结束时间 (不包含该值)
  private LocalDateTime endDateTime;
}
