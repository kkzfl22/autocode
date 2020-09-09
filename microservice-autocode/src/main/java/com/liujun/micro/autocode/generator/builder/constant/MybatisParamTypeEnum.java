package com.liujun.micro.autocode.generator.builder.constant;

import com.liujun.micro.autocode.generator.database.constant.StandardTypeEnum;

/**
 * java的数据类型枚举信息
 *
 * @author liujun
 * @version 0.0.1
 * @date 2019/10/29
 */
public enum MybatisParamTypeEnum {

  /** TINYINT 1 字节 (-128，127) */
  TINYINT(StandardTypeEnum.TINYINT, "java.lang.Short"),

  /** SMALLINT 2 字节 (-32 768，32 767) */
  SMALLINT(StandardTypeEnum.SMALLINT, "java.lang.Integer"),

  /** MEDIUMINT 3 字节 (-8 388 608，8 388 607) */
  MEDIUMINT(StandardTypeEnum.MEDIUMINT, "java.lang.Integer"),

  /** INT或INTEGER 4 字节 (-2 147 483 648，2 147 483 647) */
  INT(StandardTypeEnum.INTEGER, "java.lang.Integer"),

  /** INT或INTEGER 4 字节 (-2 147 483 648，2 147 483 647) */
  INTEGER(StandardTypeEnum.INTEGER, "java.lang.Integer"),

  /** BIGINT 8 字节 (-9,223,372,036,854,775,808，9 223 372 036 854 775 807) */
  BIGINT(StandardTypeEnum.BIGINT, "java.lang.Long"),

  /**
   * FLOAT 4 字节 (-3.402 823 466 E+38，-1.175 494 351 E-38)，0，(1.175 494 351 E-38，3.402 823 466 351
   * E+38)
   */
  FLOAT(StandardTypeEnum.FLOAT, "java.lang.Float"),

  /**
   * DOUBLE 8 字节 (-1.797 693 134 862 315 7 E+308，-2.225 073 858 507 201 4 E-308)，0，(2.225 073 858
   * 507 201 4 E-308，1.797 693 134 862 315 7 E+308)
   */
  DOUBLE(StandardTypeEnum.DOUBLE, "java.lang.Double"),

  /** DECIMAL 对DECIMAL(M,D) ，如果M>D，为M+2否则为D+2 依赖于M和D的值 */
  DECIMAL(StandardTypeEnum.DECIMAL, "java.math.BigDecimal"),

  /** DATE 3 1000-01-01/9999-12-31 YYYY-MM-DD 日期值 */
  DATE(StandardTypeEnum.DATE, "java.lang.String"),

  /** TIME 3 '-838:59:59'/'838:59:59' HH:MM:SS 时间值或持续时间 */
  TIME(StandardTypeEnum.TIME, "java.lang.String"),

  /** YEAR 1 1901/2155 YYYY 年份值 */
  YEAR(StandardTypeEnum.YEAR, "java.lang.String"),

  /** DATETIME 8 1000-01-01 00:00:00/9999-12-31 23:59:59 YYYY-MM-DD HH:MM:SS 混合日期和时间值 */
  DATETIME(StandardTypeEnum.DATETIME, "java.lang.String"),

  /**
   * TIMESTAMP 4 1970-01-01 00:00:00/2038 结束时间是第 2147483647 秒，北京时间 2038-1-19 11:14:07，格林尼治时间
   * 2038年1月19日 凌晨 03:14:07 YYYYMMDD HHMMSS 混合日期和时间值，时间戳
   */
  TIMESTAMP(StandardTypeEnum.TIMESTAMP, "java.lang.String"),

  /** CHAR 0-255字节 定长字符串 */
  CHAR(StandardTypeEnum.CHAR, "java.lang.String"),

  /** VARCHAR 0-65535 字节 变长字符串 */
  VARCHAR(StandardTypeEnum.VARCHAR, "java.lang.String"),

  /** TINYBLOB 0-255字节 不超过 255 个字符的二进制字符串 */
  TINYBLOB(StandardTypeEnum.TINYBLOB, "java.lang.String"),

  /** TINYTEXT 0-255字节 短文本字符串 */
  TINYTEXT(StandardTypeEnum.TINYTEXT, "java.lang.String"),

  /** BLOB 0-65 535字节 二进制形式的长文本数据 */
  BLOB(StandardTypeEnum.BLOB, "java.lang.String"),

  /** TEXT 0-65 535字节 长文本数据 */
  TEXT(StandardTypeEnum.TEXT, "java.lang.String"),

  /** MEDIUMBLOB 0-16 777 215字节 二进制形式的中等长度文本数据 */
  MEDIUMBLOB(StandardTypeEnum.MEDIUMBLOB, "java.lang.String"),

  /** MEDIUMTEXT 0-16 777 215字节 中等长度文本数据 */
  MEDIUMTEXT(StandardTypeEnum.MEDIUMTEXT, "java.lang.String"),

  /** LONGBLOB 0-4 294 967 295字节 二进制形式的极大文本数据 */
  LONGBLOB(StandardTypeEnum.LONGBLOB, "java.lang.String"),

  /** LONGTEXT 0-4 294 967 295字节 极大文本数据 */
  LONGTEXT(StandardTypeEnum.LONGTEXT, "java.lang.String"),
  ;

  /** 标准的key */
  private final StandardTypeEnum standKey;

  /** mysql中的类型名称 */
  private final String mybatisParamType;

  /**
   * 指明标准的key，与mysql的类型
   *
   * @param standKey
   * @param mybatisType
   */
  MybatisParamTypeEnum(StandardTypeEnum standKey, String mybatisType) {
    this.standKey = standKey;
    this.mybatisParamType = mybatisType;
  }

  public StandardTypeEnum getStandKey() {
    return standKey;
  }

  public String getMybatisParamType() {
    return mybatisParamType;
  }

  /**
   * 获取mybais的类型信息
   *
   * @param standardType 标准的类型信息
   * @return mybatis的类型信息
   */
  public static String getMybatisParamType(StandardTypeEnum standardType) {
    for (MybatisParamTypeEnum dataType : values()) {
      if (dataType.getStandKey().equals(standardType)) {
        return dataType.getMybatisParamType();
      }
    }

    return MybatisParamTypeEnum.VARCHAR.getMybatisParamType();
  }
}
