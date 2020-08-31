package com.liujun.micro.autocode.generator.database.constant;

import org.apache.commons.lang3.RandomUtils;

/**
 * java的数据类型枚举信息
 *
 * @author liujun
 * @version 0.0.1
 * @date 2019/10/29
 */
public enum JavaDataTypeGenerateValueEnum {

  /** byte类型 */
  TINYINT(StandardTypeEnum.TINYINT, "RandomUtils.nextInt(0,1<<#length#)"),

  /** 对应java中的short类型 */
  SMALLINT(StandardTypeEnum.SMALLINT, "RandomUtils.nextInt(0,1<<#length#)"),

  /** MEDIUMINT 3 字节 (-8 388 608，8 388 607) (0，16 777 215) 大整数值 */
  MEDIUMINT(StandardTypeEnum.MEDIUMINT, "RandomUtils.nextInt(0,1<<#length#)"),

  /** java中的Integer类型 */
  INTEGER(StandardTypeEnum.INTEGER, "RandomUtils.nextInt(0,1<<#length#)"),

  /** 对应java中的long类型 */
  BIGINT(StandardTypeEnum.BIGINT, "RandomUtils.nextLong(0,1<<#length#)"),

  /**
   * FLOAT 4 字节 (-3.402 823 466 E+38，-1.175 494 351 E-38)，0，(1.175 494 351 E-38，3.402 823 466 351
   * E+38) 0，(1.175 494 351 E-38，3.402 823 466 E+38) 单精度 浮点数值
   */
  FLOAT(StandardTypeEnum.FLOAT, "RandomUtils.nextFloat(0f,1<<#length#)"),

  /**
   * DOUBLE 8 字节 (-1.797 693 134 862 315 7 E+308，-2.225 073 858 507 201 4 E-308)，0，(2.225 073 858
   * 507 201 4 E-308，1.797 693 134 862 315 7 E+308) 0，(2.225 073 858 507 201 4 E-308，1.797 693 134
   * 862 315 7 E+308) 双精度 浮点数值
   */
  DOUBLE(StandardTypeEnum.DOUBLE, "RandomUtils.nextDouble(0d,1<<#length#)"),

  /** 货币类型 */
  DECIMAL(StandardTypeEnum.DECIMAL, "BigDecimal.valueOf(RandomUtils.nextDouble(0d,1<<#length#))"),

  /** 字符串类型 */
  CHAR(StandardTypeEnum.CHAR, "RandomStringUtils.randomAlphabetic(#length#)"),

  /** 字符串类型 */
  VARCHAR(StandardTypeEnum.VARCHAR, "RandomStringUtils.randomAlphabetic(#length#)"),

  /** 日期对象 */
  DATE(StandardTypeEnum.DATE, "LocalDate.now().toString()"),

  /** 时间对象 */
  TIME(StandardTypeEnum.TIME, "LocalTime.now().format(DateTimeFormatter.ofPattern(\"hh:mm:ss\"))"),

  /** 时间年对象 */
  YEAR(StandardTypeEnum.YEAR, "String.valueOf(LocalDate.now().getYear())"),

  /** 时间对象 */
  DATETIME(
      StandardTypeEnum.DATETIME,
      "LocalDateTime.now().format(DateTimeFormatter.ofPattern(\"yyyy-MM-dd hh:mm:ss\"))"),

  /** 时间搓 */
  TIMESTAMP(
      StandardTypeEnum.TIMESTAMP,
      "LocalDateTime.now().format(DateTimeFormatter.ofPattern(\"yyyy-MM-dd hh:mm:ss.0\"))"),

  /** TINYBLOB 0-255字节 不超过 255 个字符的二进制字符串 */
  TINYBLOB(StandardTypeEnum.TINYBLOB, "RandomStringUtils.randomAlphabetic(#length#)"),

  /** tinytext 0-255字节 短文本字符串 */
  TINYTEXT(StandardTypeEnum.TINYTEXT, "RandomStringUtils.randomAlphabetic(#length#)"),

  /** BLOB 0-65 535字节 二进制形式的长文本数据 */
  BLOB(StandardTypeEnum.BLOB, "RandomStringUtils.randomAlphabetic(#length#)"),

  /** TEXT 0-65 535字节 长文本数据 */
  TEXT(StandardTypeEnum.TEXT, "RandomStringUtils.randomAlphabetic(#length#)"),

  /** MEDIUMBLOB 0-16 777 215字节 二进制形式的中等长度文本数据 */
  MEDIUMBLOB(StandardTypeEnum.MEDIUMBLOB, "RandomStringUtils.randomAlphabetic(#length#)"),

  /** MEDIUMTEXT 0-16 777 215字节 中等长度文本数据 */
  MEDIUMTEXT(StandardTypeEnum.MEDIUMTEXT, "RandomStringUtils.randomAlphabetic(#length#)"),

  /** longblob 0-4 294 967 295字节 二进制形式的极大文本数据 */
  LONGBLOB(StandardTypeEnum.LONGBLOB, "RandomStringUtils.randomAlphabetic(#length#)"),

  /** longtext 0-4 294 967 295字节 极大文本数据 */
  LONGTEXT(StandardTypeEnum.LONGTEXT, "RandomStringUtils.randomAlphabetic(#length#)"),
  ;

  /** 类型的key */
  private StandardTypeEnum key;

  /** 运行的方法 */
  private String runFunction;

  /** 长度信息 */
  private static final String LENGTH_KEY = "#length#";

  private static final int DEFINE_MAX_LENGTH = 20;

  JavaDataTypeGenerateValueEnum(StandardTypeEnum key, String runFunction) {
    this.key = key;
    this.runFunction = runFunction;
  }

  public StandardTypeEnum getKey() {
    return key;
  }

  public String getRunFunction() {
    return runFunction;
  }

  public static String getGenerateFun(StandardTypeEnum key, int length) {

    String outLengthStr;

    int outLength;
    if (length > DEFINE_MAX_LENGTH) {
      outLength = DEFINE_MAX_LENGTH;
    } else if (length == 0) {
      outLength = 1;
    } else {
      outLength = length;
    }

    outLengthStr = String.valueOf(outLength);

    for (JavaDataTypeGenerateValueEnum item : values()) {
      if (item.getKey().equals(key)) {
        String itemValue = item.getRunFunction();
        if (itemValue.indexOf(LENGTH_KEY) != -1) {
          return itemValue.replaceAll(LENGTH_KEY, outLengthStr);
        } else {
          return itemValue;
        }
      }
    }

    return "RandomStringUtils.randomNumeric(" + outLengthStr + ")";
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("JavaDataTypeGenerateValueEnum{");
    sb.append("key=").append(key);
    sb.append(", runFuncation='").append(runFunction).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
