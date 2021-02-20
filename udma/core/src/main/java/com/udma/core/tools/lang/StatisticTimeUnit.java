package com.udma.core.tools.lang;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.function.Function;
import lombok.Getter;

/**
 * 统计时间单位
 * */
public enum StatisticTimeUnit implements Serializable {
  DAY("天", "yyyy年MM月dd日"),
  WEEK("周", "yyyy年第w周"),
  MONTH("月", "yyyy年MM月"),
  YEAR("年", "yyyy年");

  @Getter private String description;

  @Getter private String timeFormatter;

  private transient Function<LocalDate, LocalDate> timeIncFunc;

  StatisticTimeUnit(String description, String timeFormatter) {
    this.description = description;
    this.timeFormatter = timeFormatter;
  }

  public DateTimeFormatter formatter() {
    return DateTimeFormatter.ofPattern(this.timeFormatter);
  }

  public Function<LocalDate, LocalDate> getTimeIncFunc() {
    if (this.timeIncFunc == null) {
      switch (this) {
        case DAY:
          this.timeIncFunc = localDateTime -> localDateTime.plusDays(1);
          break;
        case WEEK:
          this.timeIncFunc = localDateTime -> localDateTime.plusWeeks(1);
          break;
        case MONTH:
          this.timeIncFunc = localDateTime -> localDateTime.plusMonths(1);
          break;
        case YEAR:
          this.timeIncFunc = localDateTime -> localDateTime.plusYears(1);
          break;
        default:
          throw new RuntimeException("Unknown " + getClass().getSimpleName());
      }
    }
    return timeIncFunc;
  }

  /** 校验时间范围是否正确 */
  public boolean validate(LocalDate startDate, LocalDate endDate) {
    long between = ChronoUnit.DAYS.between(startDate, endDate);
    if (between <= 0) {
      return false;
    }
    int maxDaysInterval = Integer.MAX_VALUE;
    switch (this) {
      case DAY:
        maxDaysInterval = 30;
        break;
      case WEEK:
        maxDaysInterval = 60;
        break;
      case MONTH:
        maxDaysInterval = 366;
        break;
      default:
    }
    return between <= maxDaysInterval;
  }
}
