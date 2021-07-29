package com.liujun.micro.autocode.generator.javalanguage.constant;

import com.liujun.micro.autocode.generator.database.constant.StandardTypeEnum;



/**
 * java的数据类型枚举信息
 *
 * @author liujun
 * @version 0.0.1
 * @date 2019/10/29
 */
public enum JavaDataTypeEnum {

  /** byte类型 */
  TINYINT(StandardTypeEnum.TINYINT, JavaDataType.BYTE),

  /** 对应java中的short类型 */
  SMALLINT(StandardTypeEnum.SMALLINT, JavaDataType.SHORT),

  /** MEDIUMINT 3 字节 (-8 388 608，8 388 607) (0，16 777 215) 大整数值 */
  MEDIUMINT(StandardTypeEnum.MEDIUMINT, JavaDataType.INTEGER),

  /** java中的Integer类型 */
  INTEGER(StandardTypeEnum.INTEGER, JavaDataType.INTEGER),

  /** 对应java中的long类型 */
  BIGINT(StandardTypeEnum.BIGINT, JavaDataType.LONG),

  /**
   * FLOAT 4 字节 (-3.402 823 466 E+38，-1.175 494 351 E-38)，0，(1.175 494 351 E-38，3.402 823 466 351
   * E+38) 0，(1.175 494 351 E-38，3.402 823 466 E+38) 单精度 浮点数值
   */
  FLOAT(StandardTypeEnum.FLOAT, JavaDataType.FLOAT),

  /**
   * DOUBLE 8 字节 (-1.797 693 134 862 315 7 E+308，-2.225 073 858 507 201 4 E-308)，0，(2.225 073 858
   * 507 201 4 E-308，1.797 693 134 862 315 7 E+308) 0，(2.225 073 858 507 201 4 E-308，1.797 693 134
   * 862 315 7 E+308) 双精度 浮点数值
   */
  DOUBLE(StandardTypeEnum.DOUBLE, JavaDataType.DOUBLE),

  /** 货币类型 */
  DECIMAL(StandardTypeEnum.DECIMAL, JavaDataType.BIG_DECIMAL),

  /** 字符串类型 */
  CHAR(StandardTypeEnum.CHAR, JavaDataType.STRING),

  /** 字符串类型 */
  VARCHAR(StandardTypeEnum.VARCHAR, JavaDataType.STRING),

  /** 日期对象 */
  DATE(StandardTypeEnum.DATE, JavaDataType.DATE),

  /** 时间年对象 */
  YEAR(StandardTypeEnum.YEAR, JavaDataType.STRING),

  /** 时间对象 */
  TIME(StandardTypeEnum.TIME, JavaDataType.TIME),

  /** 时间对象 */
  DATETIME(StandardTypeEnum.DATETIME, JavaDataType.DATETIME),

  /** 时间搓 */
  TIMESTAMP(StandardTypeEnum.TIMESTAMP, JavaDataType.STRING),

  /** TINYBLOB 0-255字节 不超过 255 个字符的二进制字符串 */
  TINYBLOB(StandardTypeEnum.TINYBLOB, JavaDataType.STRING),

  /** tinytext 0-255字节 短文本字符串 */
  TINYTEXT(StandardTypeEnum.TINYTEXT, JavaDataType.STRING),

  /** BLOB 0-65 535字节 二进制形式的长文本数据 */
  BLOB(StandardTypeEnum.BLOB, JavaDataType.STRING),

  /** TEXT 0-65 535字节 长文本数据 */
  TEXT(StandardTypeEnum.TEXT, JavaDataType.STRING),

  /** MEDIUMBLOB 0-16 777 215字节 二进制形式的中等长度文本数据 */
  MEDIUMBLOB(StandardTypeEnum.MEDIUMBLOB, JavaDataType.STRING),

  /** MEDIUMTEXT 0-16 777 215字节 中等长度文本数据 */
  MEDIUMTEXT(StandardTypeEnum.MEDIUMTEXT, JavaDataType.STRING),

  /** longblob 0-4 294 967 295字节 二进制形式的极大文本数据 */
  LONGBLOB(StandardTypeEnum.LONGBLOB, JavaDataType.STRING),

  /** longtext 0-4 294 967 295字节 极大文本数据 */
  LONGTEXT(StandardTypeEnum.LONGTEXT, JavaDataType.STRING),
  ;

  /** 类型的key */
  private final StandardTypeEnum key;

  /** java的类型名称 */
  private final JavaDataType javaType;

  /** java的类型的类 */
  private final Class javaTypeClass;

  JavaDataTypeEnum(StandardTypeEnum key, JavaDataType typeEnum) {
    this.key = key;
    this.javaType = typeEnum;
    this.javaTypeClass = typeEnum.getDeclaringClass();
  }

  public StandardTypeEnum getKey() {
    return key;
  }

  public String getJavaTypeKey() {
    return javaType.getType();
  }

  public JavaDataType getJavaType() {
    return javaType;
  }

  public static String getJavaTypeKey(StandardTypeEnum standardType) {
    for (JavaDataTypeEnum dataType : values()) {
      if (dataType.getKey().equals(standardType)) {
        return dataType.getJavaTypeKey();
      }
    }

    // 默认返回String类型
    return JavaDataTypeEnum.VARCHAR.getJavaTypeKey();
  }

  public static StandardTypeEnum getStandardType(String javaType) {
    for (JavaDataTypeEnum dataType : values()) {
      if (dataType.getJavaTypeKey().equals(javaType)) {
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
