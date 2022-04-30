/*
 * Copyright (C), 2008-2021, Paraview All Rights Reserved.
 */
package com.liujun.auto.generator.builder.utils;

import com.liujun.auto.constant.Symbol;
import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * 时间工具类
 *
 * @author liujun
 * @since 2021/5/28
 */
public class DateTimeUtils {

  private DateTimeUtils() {}

  /** 默认返回时间戳 */
  public static final String DEFAULT_STAMP = "0";

  /** 东八区时间 */
  public static final String CHINA = "+8";

  /** 中国的时区 */
  public static final String TIME_ZONE_CHINA = "Asia/Shanghai";

  /** 日期 */
  private static final DateTimeFormatter Y_M_D = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  /** 日期 */
  private static final DateTimeFormatter H_M_S = DateTimeFormatter.ofPattern("HH:mm:ss");

  /** 到秒的时间 */
  private static final DateTimeFormatter Y_M_S = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  /** 到秒的时间 */
  public static final DateTimeFormatter Y_M_S_T_Z =
      DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

  /**
   * 获取当前的时间
   *
   * @return 返回当前的时间
   */
  public static LocalDateTime getCurrDateTime() {
    return LocalDateTime.now();
  }

  /**
   * 将当前时间转换为秒数
   *
   * @return
   */
  public static Long getCurrTimeEpochSecond() {
    return getCurrDateTime().toEpochSecond(DateTimeUtils.getDefaultZone());
  }

  /**
   * 将当前时间转换为毫秒数
   *
   * @return
   */
  public static Long getCurrTimeMillSecond() {
    return getCurrDateTime().toInstant(DateTimeUtils.getDefaultZone()).toEpochMilli();
  }

  public static Long getCurrDateTimeLong() {
    return getCurrDateTime().toInstant(getDefaultZone()).getEpochSecond();
  }

  /**
   * 给时间增上指定年，返回标准的时间格式
   *
   * @param incrementValue
   * @return
   */
  public static String defaultPlusYear(int incrementValue) {
    LocalDateTime currTime = getCurrDateTime();
    // 增加时间
    currTime = currTime.plus(incrementValue, ChronoUnit.YEARS);
    return currTime.format(Y_M_S);
  }

  /**
   * 将时间转换为Date
   *
   * @param time
   * @return
   */
  public static LocalDateTime toLocalDateTimeByYms(String time) {

    if (StringUtils.isEmpty(time)) {
      return null;
    }

    return LocalDateTime.parse(time, Y_M_S);
  }

  /**
   * 将时间转换为Date
   *
   * @param time
   * @return
   */
  public static LocalDateTime toLocalDateTimeByLongTimeStr(String time) {

    if (StringUtils.isEmpty(time)) {
      return null;
    }
    return toLocalDateTimeByLongTime(Long.parseLong(time));
  }

  /**
   * 将时间转换为Date
   *
   * @param time
   * @return
   */
  public static LocalDateTime toLocalDateTimeByLongTime(Long time) {

    if (null == time || time < 0) {
      return null;
    }
    Instant instant = Instant.ofEpochMilli(time);
    return LocalDateTime.ofInstant(instant, DateTimeUtils.getDefaultZone());
  }

  /**
   * 默认的截止时间检查
   *
   * @param time 当前的时间
   * @return 在截止时间内，则返回true，其他返回false
   */
  public static boolean defaultCheckScope(String time) {
    LocalDateTime currTime = getCurrDateTime();
    LocalDateTime endTime = LocalDateTime.parse(time, Y_M_S);

    return currTime.isBefore(endTime);
  }

  /**
   * 默认的截止时间检查
   *
   * @param dateTime 当前的时间
   * @return 格式化后的赶时间
   */
  public static String localDataTimeOut(LocalDateTime dateTime) {
    if (dateTime != null) {
      return dateTime.format(Y_M_S);
    }

    return Symbol.EMPTY;
  }

  /**
   * 输出年月日
   *
   * @param dateTime 当前的时间
   * @return 格式化后的赶时间
   */
  public static String localDataTimeOutYMD(LocalDateTime dateTime) {
    if (dateTime != null) {
      return dateTime.format(Y_M_D);
    }

    return Symbol.EMPTY;
  }

  /**
   * 输至时分秒
   *
   * @param dateTime 当前的时间
   * @return 格式化后的赶时间
   */
  public static String localDataTimeOutHMS(LocalDateTime dateTime) {
    if (dateTime != null) {
      return dateTime.format(H_M_S);
    }

    return Symbol.EMPTY;
  }

  /**
   * 以前当前进行加法操作
   *
   * @param second 时间操作的秒
   * @return
   */
  public static LocalDateTime getLocalDateTimePlus(Integer second) {
    LocalDateTime time1 = getCurrDateTime();
    return time1.plus(second, ChronoUnit.SECONDS);
  }

  /**
   * 以前当前进行加法操作
   *
   * @param second 时间操作的秒
   * @return 返回时间到秒
   */
  public static Long getLocalDateTimeLongPlus(Integer second) {
    LocalDateTime time1 = getCurrDateTime();
    time1 = time1.plus(second, ChronoUnit.SECONDS);
    return time1.toEpochSecond(DateTimeUtils.getDefaultZone());
  }

  /**
   * 获取默认时间
   *
   * @return
   */
  public static ZoneOffset getDefaultZone() {
    return ZoneOffset.of(CHINA);
  }

  /**
   * 计算过期时间
   *
   * @param dataTime
   * @param expires
   * @return
   */
  public static long countExpires(LocalDateTime dataTime, int expires) {
    long epochSecond = dataTime.toEpochSecond(DateTimeUtils.getDefaultZone());
    return epochSecond + expires;
  }

  /**
   * 返回时间的秒数
   *
   * <p>为自1970-01-01T00：00：00Z纪元以来的秒数
   *
   * @param dataTime
   * @return
   */
  public static Long getEpochSecond(LocalDateTime dataTime) {
    if (null == dataTime) {
      return 0L;
    }

    return dataTime.toInstant(getDefaultZone()).getEpochSecond();
  }

  /**
   * 返回时间毫秒秒数
   *
   * <p>为自1970-01-01T00：00：00Z纪元以来的毫秒数
   *
   * @param dataTime
   * @return
   */
  public static Long getMillSecond(LocalDateTime dataTime) {
    if (null == dataTime) {
      return 0L;
    }

    return dataTime.toInstant(getDefaultZone()).toEpochMilli();
  }

  /**
   * 返回时间毫秒秒数的时间戳
   *
   * <p>为自1970-01-01T00：00：00Z纪元以来的毫秒数
   *
   * @param dataTime
   * @return
   */
  public static String getMillSecondStr(LocalDateTime dataTime) {
    if (null == dataTime) {
      return DEFAULT_STAMP;
    }

    return String.valueOf(dataTime.toInstant(getDefaultZone()).toEpochMilli());
  }
}
