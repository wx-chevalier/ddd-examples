package com.udma.core.type.dto;

import io.vavr.API;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParamDateRange {

  private String dateProperty;

  // 起始时间
  private LocalDate startDate;

  // 结束时间 (不包含当天)
  private LocalDate endDate;

  private LocalDateTime startDateTime;

  private LocalDateTime endDateTime;

  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
    this.startDateTime =
        API.Option(startDate).map(d -> LocalDateTime.of(d, LocalTime.MIN)).getOrNull();
  }

  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
    this.endDateTime = API.Option(endDate).map(d -> LocalDateTime.of(d, LocalTime.MIN)).getOrNull();
  }

  public ParamDateRange(LocalDate startDate, LocalDate endDate) {
    setStartDate(startDate);
    setEndDate(endDate);
  }

  public ParamDateRange(String dateProperty, LocalDate startDate, LocalDate endDate) {
    this(startDate, endDate);
    this.dateProperty = dateProperty;
  }
}
