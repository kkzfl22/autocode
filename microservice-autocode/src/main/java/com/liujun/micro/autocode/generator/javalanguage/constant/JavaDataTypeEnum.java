package com.liujun.micro.autocode.generator.javalanguage.constant;

import com.liujun.micro.autocode.generator.database.constant.StandardTypeEnum;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * java的数据类型枚举信息
 *
 * @author liujun
 * @version 0.0.1
 * @date 2019/10/29
 */
public enum JavaDataTypeEnum {

  /** byte类型 */
  TINYINT(StandardTypeEnum.TINYINT, "Byte", Byte.class),

  /** 对应java中的short类型 */
  SMALLINT(StandardTypeEnum.SMALLINT, "Short", Short.class),

  /** MEDIUMINT 3 字节 (-8 388 608，8 388 607) (0，16 777 215) 大整数值 */
  MEDIUMINT(StandardTypeEnum.MEDIUMINT, "Integer", Integer.class),

  /** java中的Integer类型 */
  INTEGER(StandardTypeEnum.INTEGER, "Integer", Integer.class),

  /** 对应java中的long类型 */
  BIGINT(StandardTypeEnum.BIGINT, "Long", Long.class),

  /**
   * FLOAT 4 字节 (-3.402 823 466 E+38，-1.175 494 351 E-38)，0，(1.175 494 351 E-38，3.402 823 466 351
   * E+38) 0，(1.175 494 351 E-38，3.402 823 466 E+38) 单精度 浮点数值
   */
  FLOAT(StandardTypeEnum.FLOAT, "Float", Float.class),

  /**
   * DOUBLE 8 字节 (-1.797 693 134 862 315 7 E+308，-2.225 073 858 507 201 4 E-308)，0，(2.225 073 858
   * 507 201 4 E-308，1.797 693 134 862 315 7 E+308) 0，(2.225 073 858 507 201 4 E-308，1.797 693 134
   * 862 315 7 E+308) 双精度 浮点数值
   */
  DOUBLE(StandardTypeEnum.DOUBLE, "Double", Double.class),

  /** 货币类型 */
  DECIMAL(StandardTypeEnum.DECIMAL, "BigDecimal", BigDecimal.class),

  /** 字符串类型 */
  CHAR(StandardTypeEnum.CHAR, "String", String.class),

  /** 字符串类型 */
  VARCHAR(StandardTypeEnum.VARCHAR, "String", String.class),

  /** 日期对象 */
  DATE(StandardTypeEnum.DATE, "LocalDate", LocalDate.class),

  /** 时间年对象 */
  YEAR(StandardTypeEnum.YEAR, "String", String.class),

  /** 时间对象 */
  TIME(StandardTypeEnum.TIME, "LocalTime", LocalTime.class),

  /** 时间对象 */
  DATETIME(StandardTypeEnum.DATETIME, "LocalDateTime", LocalDateTime.class),

  /** 时间搓 */
  TIMESTAMP(StandardTypeEnum.TIMESTAMP, "String", String.class),

  /** TINYBLOB 0-255字节 不超过 255 个字符的二进制字符串 */
  TINYBLOB(StandardTypeEnum.TINYBLOB, "String", String.class),

  /** tinytext 0-255字节 短文本字符串 */
  TINYTEXT(StandardTypeEnum.TINYTEXT, "String", String.class),

  /** BLOB 0-65 535字节 二进制形式的长文本数据 */
  BLOB(StandardTypeEnum.BLOB, "String", String.class),

  /** TEXT 0-65 535字节 长文本数据 */
  TEXT(StandardTypeEnum.TEXT, "String", String.class),

  /** MEDIUMBLOB 0-16 777 215字节 二进制形式的中等长度文本数据 */
  MEDIUMBLOB(StandardTypeEnum.MEDIUMBLOB, "String", String.class),

  /** MEDIUMTEXT 0-16 777 215字节 中等长度文本数据 */
  MEDIUMTEXT(StandardTypeEnum.MEDIUMTEXT, "String", String.class),

  /** longblob 0-4 294 967 295字节 二进制形式的极大文本数据 */
  LONGBLOB(StandardTypeEnum.LONGBLOB, "String", String.class),

  /** longtext 0-4 294 967 295字节 极大文本数据 */
  LONGTEXT(StandardTypeEnum.LONGTEXT, "String", String.class),
  ;

  /** 类型的key */
  private StandardTypeEnum key;

  /** java的类型名称 */
  private String javaType;

  /** java的类型的类 */
  private Class javaTypeClass;

  JavaDataTypeEnum(StandardTypeEnum key, String javaType, Class javaTypeClass) {
    this.key = key;
    this.javaType = javaType;
    this.javaTypeClass = javaTypeClass;
  }

  public StandardTypeEnum getKey() {
    return key;
  }

  public String getJavaType() {
    return javaType;
  }

  public Class getJavaTypeClass() {
    return javaTypeClass;
  }

  public static String getJavaType(StandardTypeEnum standardType) {
    for (JavaDataTypeEnum dataType : values()) {
      if (dataType.getKey().equals(standardType)) {
        return dataType.getJavaType();
      }
    }

    // 默认返回String类型
    return JavaDataTypeEnum.VARCHAR.getJavaType();
  }

  public static StandardTypeEnum getStandardType(String javaType) {
    for (JavaDataTypeEnum dataType : values()) {
      if (dataType.getJavaType().equals(javaType)) {
        return dataType.getKey();
      }
    }

    // 默认返回String类型
    return JavaDataTypeEnum.VARCHAR.key;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("JavaDataTypeEnum{");
    sb.append("key='").append(key).append('\'');
    sb.append(", javaType='").append(javaType).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
