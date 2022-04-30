package com.liujun.auto.generator.database.constant;

/**
 * java的数据类型枚举信息
 *
 * @author liujun
 * @version 0.0.1
 * @date 2019/10/29
 */
public enum OracleDataTypeEnum {

  /** TINYINT 1 字节 (-128，127) */
  TINYINT(StandardTypeEnum.TINYINT, "NUMBER", 1, 4),

  /** SMALLINT 2 字节 (-32 768，32 767) */
  SMALLINT(StandardTypeEnum.SMALLINT, "NUMBER", 1, 6),

  /** MEDIUMINT 3 字节 (-8 388 608，8 388 607) */
  MEDIUMINT(StandardTypeEnum.MEDIUMINT, "NUMBER", 1, 9),

  /** INT或INTEGER 4 字节 (-2 147 483 648，2 147 483 647) */
  INT(StandardTypeEnum.INTEGER, "NUMBER", 1, 10),

  /** INT或INTEGER 4 字节 (-2 147 483 648，2 147 483 647) */
  INTEGER(StandardTypeEnum.INTEGER, "NUMBER", 1, 10),

  /** BIGINT 8 字节 (0,18 446 744 073 709 551 615) */
  BIGINT2(StandardTypeEnum.BIGINT, "NUMBER", 1, 21),

  /**
   * FLOAT 4 字节 (-3.402 823 466 E+38，-1.175 494 351 E-38)，0，(1.175 494 351 E-38，3.402 823 466 351
   * E+38)
   */
  FLOAT(StandardTypeEnum.FLOAT, "NUMBER", 1, 12),

  /**
   * DOUBLE 8 字节 (-1.797 693 134 862 315 7 E+308，-2.225 073 858 507 201 4 E-308)，0，(2.225 073 858
   * 507 201 4 E-308，1.797 693 134 862 315 7 E+308)
   */
  DOUBLE(StandardTypeEnum.DOUBLE, "NUMBER", 1, 22),

  /** DECIMAL 对DECIMAL(M,D) ，如果M>D，为M+2否则为D+2 依赖于M和D的值 M最大65，D最大30 */
  DECIMAL(StandardTypeEnum.DECIMAL, "NUMBER", 1, 95),

  /** DATE 3 1000-01-01/9999-12-31 YYYY-MM-DD 日期值 */
  DATE(StandardTypeEnum.DATE, "DATE", 1, 10),

  /** TIME 3 '-838:59:59'/'838:59:59' HH:MM:SS 时间值或持续时间 */
  TIME(StandardTypeEnum.TIME, "DATE", 1, 8),

  /** YEAR 1 1901/2155 YYYY 年份值 */
  YEAR(StandardTypeEnum.YEAR, "YEAR", 1, 4),

  /** DATETIME 8 1000-01-01 00:00:00/9999-12-31 23:59:59 YYYY-MM-DD HH:MM:SS 混合日期和时间值 */
  DATETIME(StandardTypeEnum.DATETIME, "TIMESTAMP", 0, 20),

  /**
   * TIMESTAMP 4 1970-01-01 00:00:00/2038 结束时间是第 2147483647 秒，北京时间 2038-1-19 11:14:07，格林尼治时间
   * 2038年1月19日 凌晨 03:14:07 YYYYMMDD HHMMSS 混合日期和时间值，时间戳
   */
  TIMESTAMP(StandardTypeEnum.TIMESTAMP, "TIMESTAMP", 0, 25),

  /** CHAR 0-255字节 定长字符串 */
  CHAR(StandardTypeEnum.CHAR, "CHAR", 0, 255),

  /** VARCHAR 0-65535 字节 变长字符串 */
  VARCHAR(StandardTypeEnum.VARCHAR, "VARCHAR2", 0, 4000),

  /** TINYBLOB 0-255字节 不超过 255 个字符的二进制字符串 */
  TINYBLOB(StandardTypeEnum.TINYBLOB, "VARCHAR2", 0, 255),

  /** TINYTEXT 0-255字节 短文本字符串 */
  TINYTEXT(StandardTypeEnum.TINYTEXT, "VARCHAR2", 0, 255),

  /** BLOB 0-65 535字节 二进制形式的长文本数据 */
  BLOB(StandardTypeEnum.BLOB, "BLOB", 0, 65536),

  /** TEXT 0-65 535字节 长文本数据 */
  TEXT(StandardTypeEnum.TEXT, "TEXT", 0, 65536),

  /** MEDIUMBLOB 0-16 777 215字节 二进制形式的中等长度文本数据 */
  MEDIUMBLOB(StandardTypeEnum.MEDIUMBLOB, "TEXT", 0, 16777215),

  /** MEDIUMTEXT 0-16 777 215字节 中等长度文本数据 */
  MEDIUMTEXT(StandardTypeEnum.MEDIUMTEXT, "TEXT", 0, 16777215),

  /** LONGBLOB 0-4 294 967 295字节 二进制形式的极大文本数据 */
  LONGBLOB(StandardTypeEnum.LONGBLOB, "TEXT", 0, 4294967295L),

  /** LONGTEXT 0-4 294 967 295字节 极大文本数据 */
  LONGTEXT(StandardTypeEnum.LONGTEXT, "BLOB", 0, 4294967295L),
  ;

  /** 标准的key */
  private final StandardTypeEnum standKey;

  /** oracle中的类型名称 */
  private final String oracleType;

  /** 长度定义范围的开始 */
  private Long lengthStart;

  /** 长度定义的范围的结束 */
  private Long lengthEnd;

  /**
   * 指明标准的key，与oracle的类型
   *
   * @param standKey
   * @param oracleType
   */
  OracleDataTypeEnum(StandardTypeEnum standKey, String oracleType) {
    this.standKey = standKey;
    this.oracleType = oracleType;
  }

  /**
   * 标准的key与oracle类型，并指定长度范围
   *
   * @param standKey
   * @param oracleType
   * @param lengthStart
   * @param lengthEnd
   */
  OracleDataTypeEnum(
      StandardTypeEnum standKey, String oracleType, long lengthStart, long lengthEnd) {
    this.standKey = standKey;
    this.oracleType = oracleType;
    this.lengthStart = lengthStart;
    this.lengthEnd = lengthEnd;
  }

  /**
   * 根据数据库的类型，转换为标准的key
   *
   * <p>当转换的类不存在时，则会抛出IllegalArgumentException异常
   *
   * @param oracleType oracle的类型
   * @return 标准的key
   */
  public static StandardTypeEnum databaseToStandKey(String oracleType) {
    OracleDataTypeEnum oracleDataType = oracleDataType(oracleType);
    if (oracleDataType != null) {
      return oracleDataType.getStandKey();
    }

    return null;
  }

  /**
   * 按类型获取oracle的类型
   *
   * <p>当转换的类不存在时，则会抛出IllegalArgumentException异常
   *
   * @param dataTypeParam oracle的类型
   * @return 标准的key
   */
  public static OracleDataTypeEnum oracleDataType(String dataTypeParam) {
    for (OracleDataTypeEnum dataType : values()) {
      // 在数据库的类型判断中不区分大小写
      if (dataType.oracleType.equalsIgnoreCase(dataTypeParam)) {
        return dataType;
      }
    }
    return null;
  }

  /**
   * 长度的检查
   *
   * @param oracleType oracle的类型
   * @param length 类型长度
   * @return 标准的key
   */
  public static StandardTypeEnum standardAndLengthCheck(String oracleType, Long length) {
    {
      OracleDataTypeEnum dataType = oracleDataType(oracleType);

      // 1,如果在标准的设置中未定义长度，则与java中直接对应，返回标准的类型
      if (null == length) {
        return dataType.standKey;
      }

      // 检查类型是否在java的范围中
      if (dataType.lengthStart <= length && length <= dataType.lengthEnd) {
        return dataType.standKey;
      } else {
        throw new IllegalArgumentException(
            "oracle type :"
                + oracleType
                + ",curr length :"
                + length
                + " is not in scope {"
                + dataType.lengthStart
                + " - "
                + dataType.lengthEnd
                + "}");
      }
    }
  }

  /**
   * 获取数据类型的的枚举
   *
   * @param dataTypeFlag
   * @return
   */
  public static OracleDataTypeEnum getOracleType(String dataTypeFlag) {
    for (OracleDataTypeEnum dataType : values()) {
      // 在数据库的类型判断中不区分大小写
      if (dataType.oracleType.equalsIgnoreCase(dataTypeFlag)) {
        return dataType;
      }
    }
    return null;
  }

  /**
   * 获取数据类型的的枚举
   *
   * @param standard 标准的的key
   * @return 数据库的类型信息
   */
  public static String getOracleDbType(StandardTypeEnum standard) {
    for (OracleDataTypeEnum dataType : values()) {
      if (dataType.getStandKey().equals(standard)) {
        return dataType.getOracleType();
      }
    }
    return null;
  }

  public StandardTypeEnum getStandKey() {
    return standKey;
  }

  public String getOracleType() {
    return oracleType;
  }

  public Long getLengthStart() {
    return lengthStart;
  }

  public Long getLengthEnd() {
    return lengthEnd;
  }
}
