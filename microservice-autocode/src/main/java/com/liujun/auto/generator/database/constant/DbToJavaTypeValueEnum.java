package com.liujun.auto.generator.database.constant;

import com.liujun.auto.generator.convergence.TypeConvergence;
import lombok.Getter;
import lombok.ToString;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * java的数据类型枚举信息
 *
 * @author liujun
 * @version 0.0.1
 * @date 2019/10/29
 */
@Getter
@ToString
public enum DbToJavaTypeValueEnum {

  /** byte类型 */
  TINYINT(
      StandardTypeEnum.TINYINT,
      (rs, t) -> {
        return rs.getByte(t);
      }),

  /** 对应java中的short类型 */
  SMALLINT(
      StandardTypeEnum.SMALLINT,
      (rs, t) -> {
        return rs.getShort(t);
      }),

  /** MEDIUMINT 3 字节 (-8 388 608，8 388 607) (0，16 777 215) 大整数值 */
  MEDIUMINT(
      StandardTypeEnum.MEDIUMINT,
      (rs, t) -> {
        return rs.getInt(t);
      }),

  /** java中的Integer类型 */
  INTEGER(
      StandardTypeEnum.INTEGER,
      (rs, t) -> {
        return rs.getInt(t);
      }),

  /** 对应java中的long类型 */
  BIGINT(
      StandardTypeEnum.BIGINT,
      (rs, t) -> {
        return rs.getLong(t);
      }),

  /**
   * FLOAT 4 字节 (-3.402 823 466 E+38，-1.175 494 351 E-38)，0，(1.175 494 351 E-38，3.402 823 466 351
   * E+38) 0，(1.175 494 351 E-38，3.402 823 466 E+38) 单精度 浮点数值
   */
  FLOAT(
      StandardTypeEnum.FLOAT,
      (rs, t) -> {
        return rs.getFloat(t);
      }),

  /**
   * DOUBLE 8 字节 (-1.797 693 134 862 315 7 E+308，-2.225 073 858 507 201 4 E-308)，0，(2.225 073 858
   * 507 201 4 E-308，1.797 693 134 862 315 7 E+308) 0，(2.225 073 858 507 201 4 E-308，1.797 693 134
   * 862 315 7 E+308) 双精度 浮点数值
   */
  DOUBLE(
      StandardTypeEnum.DOUBLE,
      (rs, t) -> {
        return rs.getDouble(t);
      }),

  /** 货币类型 */
  DECIMAL(
      StandardTypeEnum.DECIMAL,
      (rs, t) -> {
        return rs.getBigDecimal(t);
      }),

  /** 字符串类型 */
  CHAR(
      StandardTypeEnum.CHAR,
      (rs, t) -> {
        return rs.getString(t);
      }),

  /** 字符串类型 */
  VARCHAR(
      StandardTypeEnum.VARCHAR,
      (rs, t) -> {
        return rs.getString(t);
      }),

  /** 日期对象 */
  DATE(
      StandardTypeEnum.DATE,
      (rs, t) -> {
        return rs.getString(t);
      }),

  /** 时间对象 */
  TIME(
      StandardTypeEnum.TIME,
      (rs, t) -> {
        return rs.getString(t);
      }),

  /** 时间年对象 */
  YEAR(
      StandardTypeEnum.YEAR,
      (rs, t) -> {
        return rs.getString(t);
      }),

  /** 时间对象 */
  DATETIME(
      StandardTypeEnum.DATETIME,
      (rs, t) -> {
        return rs.getString(t);
      }),

  /** 时间搓 */
  TIMESTAMP(
      StandardTypeEnum.TIMESTAMP,
      (rs, t) -> {
        return rs.getString(t);
      }),

  /** TINYBLOB 0-255字节 不超过 255 个字符的二进制字符串 */
  TINYBLOB(
      StandardTypeEnum.TINYBLOB,
      (rs, t) -> {
        return rs.getString(t);
      }),

  /** tinytext 0-255字节 短文本字符串 */
  TINYTEXT(
      StandardTypeEnum.TINYTEXT,
      (rs, t) -> {
        return rs.getString(t);
      }),

  /** BLOB 0-65 535字节 二进制形式的长文本数据 */
  BLOB(
      StandardTypeEnum.BLOB,
      (rs, t) -> {
        return rs.getBlob(t);
      }),

  /** TEXT 0-65 535字节 长文本数据 */
  TEXT(
      StandardTypeEnum.TEXT,
      (rs, t) -> {
        return rs.getString(t);
      }),

  /** MEDIUMBLOB 0-16 777 215字节 二进制形式的中等长度文本数据 */
  MEDIUMBLOB(
      StandardTypeEnum.MEDIUMBLOB,
      (rs, t) -> {
        return rs.getString(t);
      }),

  /** MEDIUMTEXT 0-16 777 215字节 中等长度文本数据 */
  MEDIUMTEXT(
      StandardTypeEnum.MEDIUMTEXT,
      (rs, t) -> {
        return rs.getString(t);
      }),

  /** longblob 0-4 294 967 295字节 二进制形式的极大文本数据 */
  LONGBLOB(
      StandardTypeEnum.LONGBLOB,
      (rs, t) -> {
        return rs.getString(t);
      }),

  /** longtext 0-4 294 967 295字节 极大文本数据 */
  LONGTEXT(
      StandardTypeEnum.LONGTEXT,
      (rs, t) -> {
        return rs.getString(t);
      }),
  ;

  /** 类型的key */
  private final StandardTypeEnum key;

  /** 运行的方法 */
  private final DbValueFunction<ResultSet, String, Object> runFunction;

  DbToJavaTypeValueEnum(
      StandardTypeEnum key, DbValueFunction<ResultSet, String, Object> runFunction) {
    this.key = key;
    this.runFunction = runFunction;
  }

  /**
   * 进行值的转换操作
   *
   * @param srcType 原值类型
   * @param dbType 数据库类型
   * @param rs 结果集
   * @param name 名称
   * @return
   * @throws SQLException
   */
  public static Object getValue(DatabaseTypeEnum srcType, String dbType, ResultSet rs, String name)
      throws SQLException {
    StandardTypeEnum standardTypeEnum = TypeConvergence.getDbStandard(srcType, dbType);

    for (DbToJavaTypeValueEnum typeEnum : values()) {
      if (typeEnum.getKey().equals(standardTypeEnum)) {
        return typeEnum.getRunFunction().apply(rs, name);
      }
    }
    return null;
  }
}
