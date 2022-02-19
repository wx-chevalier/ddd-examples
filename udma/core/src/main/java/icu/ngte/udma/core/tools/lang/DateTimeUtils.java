package icu.ngte.udma.core.tools.lang;

import static java.time.DayOfWeek.SUNDAY;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class DateTimeUtils {

  /** 获取一天中最小的时间，也就是当天的00:00:00.0000 */
  public static LocalDateTime getStartTimeOfDate(LocalDateTime dateTime) {
    return LocalDateTime.of(dateTime.toLocalDate(), LocalTime.MIN);
  }

  /** 获取一天中最大的时间，也就是当天的23:59:59.9999 */
  public static LocalDateTime getDayEndTime(LocalDateTime dateTime) {
    return LocalDateTime.of(dateTime.toLocalDate(), LocalTime.MAX);
  }

  /** 根据时间单位分割时间 */
  public static List<LocalDate> split(LocalDate start, LocalDate end, StatisticTimeUnit timeUnit) {
    switch (timeUnit) {
      case DAY:
        return splitWithDays(start, end);
      case WEEK:
        return splitWithWeek(start, end);
      case MONTH:
        return splitWithMonth(start, end);
      default:
        return null;
    }
  }

  /** 按周分割时间 */
  private static List<LocalDate> splitWithWeek(LocalDate start, LocalDate end) {
    List<LocalDate> dateList = new ArrayList<>();
    dateList.add(start);
    while ((start = start.with(TemporalAdjusters.next(SUNDAY)).plusDays(1)).isBefore(end)) {
      dateList.add(start);
      start = start.plusDays(1);
    }
    dateList.add(end);
    return dateList;
  }

  /** 按月分割时间 */
  private static List<LocalDate> splitWithMonth(LocalDate start, LocalDate end) {
    List<LocalDate> dateList = new ArrayList<>();
    dateList.add(start);
    while ((start = start.with(TemporalAdjusters.lastDayOfMonth()).plusDays(1)).isBefore(end)) {
      dateList.add(start);
      start = start.plusDays(1);
    }
    dateList.add(end);
    return dateList;
  }

  /** 按天分割时间 */
  private static List<LocalDate> splitWithDays(LocalDate start, LocalDate end) {
    List<LocalDate> dateList = new ArrayList<>();
    LocalDate date = start;
    while (date.isBefore(end)) {
      dateList.add(date);
      date = date.plusDays(1);
    }
    dateList.add(end);
    return dateList;
  }

  /** 获取当前时间格式 "2019-12-12 12:00:00" */
  public static String currentDatetime() {
    return currentDatetime("yyyy-MM-dd HH:mm:ss");
  }

  /** 执行时间格式获取当前时间 */
  public static String currentDatetime(String pattern) {
    return LocalDateTime.now()
        .atZone(ZoneOffset.systemDefault())
        .format(DateTimeFormatter.ofPattern(pattern));
  }

  public static long toTimestamp(LocalDateTime dateTime) {
    return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
  }

  /** 根据天数获取距离1900-01-01 的日期 */
  public static LocalDate of(long daysOfCount) {
    Calendar c = new GregorianCalendar(1900, Calendar.JANUARY, -1);
    return c.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(daysOfCount);
  }

  /** 格式化日期 */
  public static String format(LocalDate localDate) {
    if (localDate == null) {
      return null;
    }
    return localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
  }

  /** 获取当前时间之后若干秒的时间 */
  public static Date currentAfter(int second) {
    return Date.from(ZonedDateTime.now().plusSeconds(second).toInstant());
  }

  public static Date current() {
    return Date.from(ZonedDateTime.now().toInstant());
  }

  /** 将字符串转换为LocalDate */
  public static LocalDate converter(String localDateStr) {
    return LocalDate.parse(localDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
  }

  /** 获取给定年/月 的第一天,例如: 2020-02 => 2020-02-01 ; 2020-01 => 2020-01-01 */
  public static LocalDate getFirstDayOnMonth(Integer year, Integer monthOfYear) {
    return LocalDate.of(year, monthOfYear, 1);
  }

  /** 获取给定年/月 的最后一天,例如: 2020-02 => 2020-02-29 ; 2020-01 => 2020-01-31 */
  public static LocalDate getLastDayOnMonth(Integer year, Integer monthOfYear) {
    return LocalDate.of(year, monthOfYear, 1).with(TemporalAdjusters.lastDayOfMonth());
  }

  /** 获取给定日期的最小时间，例如： 2020-02-01 => 2020-02-01 00:00:00 */
  public static LocalDateTime getStartTimeOfDate(LocalDate date) {
    if (date != null) {
      return LocalDateTime.of(date, LocalTime.MIN);
    }
    return null;
  }

  /** 获取给定日期的最小时间，例如： 2020-02-01 => 2020-02-01 23:59:59 */
  public static LocalDateTime getEndTimeOfDate(LocalDate date) {
    if (date != null) {
      return LocalDateTime.of(date, LocalTime.MAX);
    }
    return null;
  }

  /** 获取给定月份的第一秒时间，例如： 2020-05 => 2020-05-01 00:00:00 */
  public static LocalDateTime getStartTimeOfDate(Integer year, Integer month) {
    // 获取指定月份的第一天日期
    LocalDate startDate = DateTimeUtils.getFirstDayOnMonth(year, month);

    // 获取指定天的起始时间
    return DateTimeUtils.getStartTimeOfDate(startDate);
  }

  public static LocalDateTime getStartTimeOfDate(YearMonth month) {
    return getStartTimeOfDate(month.getYear(), month.getMonthValue());
  }

  /** 获取给定月份的最后一秒的时间，例如： 2020-05 => 2020-05-31 23:59:59 */
  public static LocalDateTime getEndTimeOfDate(Integer year, Integer month) {
    // 获取指定月份的最后一天
    LocalDate lastDay = DateTimeUtils.getLastDayOnMonth(year, month);

    // 获取指定天的结束时间
    return DateTimeUtils.getEndTimeOfDate(lastDay);
  }

  /** 如果给定的日期不为空，则直接返回，否则返回当前日期 */
  public static LocalDate getOrCurrent(LocalDate date) {
    return date == null ? LocalDate.now() : date;
  }
}
