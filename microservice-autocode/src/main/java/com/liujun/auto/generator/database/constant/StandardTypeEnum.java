package com.liujun.auto.generator.database.constant;

/**
 * 标准的类型定义
 *
 * @author liujun
 * @version 0.0.1
 * @date 2019/10/29
 */
public enum StandardTypeEnum {
  /** TINYINT 1 字节 (-128，127) */
  TINYINT,

  /** SMALLINT 2 字节 (-32 768，32 767) */
  SMALLINT,

  /** MEDIUMINT 3 字节 (-8 388 608，8 388 607) */
  MEDIUMINT,

  /** INT或INTEGER 4 字节 (-2 147 483 648，2 147 483 647) */
  INTEGER,

  /** BIGINT 8 字节 (-9,223,372,036,854,775,808，9 223 372 036 854 775 807) */
  BIGINT,

  /**
   * FLOAT 4 字节 (-3.402 823 466 E+38，-1.175 494 351 E-38)，0，(1.175 494 351 E-38，3.402 823 466 351
   * E+38)
   */
  FLOAT,

  /**
   * DOUBLE 8 字节 (-1.797 693 134 862 315 7 E+308，-2.225 073 858 507 201 4 E-308)，0，(2.225 073 858
   * 507 201 4 E-308，1.797 693 134 862 315 7 E+308)
   */
  DOUBLE,

  /** DECIMAL 对DECIMAL(M,D) ，如果M>D，为M+2否则为D+2 依赖于M和D的值 */
  DECIMAL,

  /** DATE 3 1000-01-01/9999-12-31 YYYY-MM-DD 日期值 */
  DATE,

  /** TIME 3 '-838:59:59'/'838:59:59' HH:MM:SS 时间值或持续时间 */
  TIME,

  /** YEAR 1 1901/2155 YYYY 年份值 */
  YEAR,

  /** DATETIME 8 1000-01-01 00:00:00/9999-12-31 23:59:59 YYYY-MM-DD HH:MM:SS 混合日期和时间值 */
  DATETIME,

  /**
   * TIMESTAMP 4 1970-01-01 00:00:00/2038 结束时间是第 2147483647 秒，北京时间 2038-1-19 11:14:07，格林尼治时间
   * 2038年1月19日 凌晨 03:14:07 YYYYMMDD HHMMSS 混合日期和时间值，时间戳
   */
  TIMESTAMP,

  /** CHAR 0-255字节 定长字符串 */
  CHAR,

  /** VARCHAR 0-65535 字节 变长字符串 */
  VARCHAR,

  /** TINYBLOB 0-255字节 不超过 255 个字符的二进制字符串 */
  TINYBLOB,

  /** TINYTEXT 0-255字节 短文本字符串 */
  TINYTEXT,

  /** BLOB 0-65 535字节 二进制形式的长文本数据 */
  BLOB,

  /** TEXT 0-65 535字节 长文本数据 */
  TEXT,

  /** MEDIUMBLOB 0-16 777 215字节 二进制形式的中等长度文本数据 */
  MEDIUMBLOB,

  /** MEDIUMTEXT 0-16 777 215字节 中等长度文本数据 */
  MEDIUMTEXT,

  /** LONGBLOB 0-4 294 967 295字节 二进制形式的极大文本数据 */
  LONGBLOB,

  /** LONGTEXT 0-4 294 967 295字节 极大文本数据 */
  LONGTEXT,
  ;
}
