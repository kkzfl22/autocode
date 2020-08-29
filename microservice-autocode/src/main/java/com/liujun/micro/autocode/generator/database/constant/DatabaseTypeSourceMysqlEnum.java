package com.liujun.micro.autocode.generator.database.constant;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.Function;

/**
 * database message key
 *
 * @author liujun
 * @version 0.0.1
 * @date 2018/09/13
 */
public enum DatabaseTypeSourceMysqlEnum {
  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type <code>BIT</code>.
   */
  DATABASE_TYPE_BIT(DatabaseTypeSourceEnum.DATABASE_TYPE_BIT, s -> Boolean.parseBoolean(s)),

  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type <code>TINYINT</code>.
   */
  DATABASE_TYPE_TINYINT(DatabaseTypeSourceEnum.DATABASE_TYPE_TINYINT, s -> Integer.parseInt(s)),

  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type <code>SMALLINT</code>.
   */
  DATABASE_TYPE_SMALLINT(DatabaseTypeSourceEnum.DATABASE_TYPE_SMALLINT, s -> Integer.parseInt(s)),

  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type <code>INTEGER</code>.
   */
  DATABASE_TYPE_INTEGER(DatabaseTypeSourceEnum.DATABASE_TYPE_INTEGER, s -> Integer.parseInt(s)),

  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type <code>BIGINT</code>.
   */
  DATABASE_TYPE_BIGINT(
      DatabaseTypeSourceEnum.DATABASE_TYPE_BIGINT,
      s -> {
        if (s.length() > ParseJavaTypeLength.MAX_LONG) {
          return new BigInteger(s);
        } else {
          return Long.parseLong(s);
        }
      }),

  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type <code>FLOAT</code>.
   */
  DATABASE_TYPE_FLOAT(DatabaseTypeSourceEnum.DATABASE_TYPE_FLOAT, s -> Float.parseFloat(s)),

  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type <code>REAL</code>.
   */
  DATABASE_TYPE_REAL(DatabaseTypeSourceEnum.DATABASE_TYPE_REAL, s -> Double.parseDouble(s)),

  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type <code>DOUBLE</code>.
   */
  DATABASE_TYPE_DOUBLE(DatabaseTypeSourceEnum.DATABASE_TYPE_DOUBLE, s -> Double.parseDouble(s)),

  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type <code>NUMERIC</code>.
   */
  DATABASE_TYPE_NUMERIC(DatabaseTypeSourceEnum.DATABASE_TYPE_NUMERIC, s -> new BigDecimal(s)),

  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type <code>DECIMAL</code>.
   */
  DATABASE_TYPE_DECIMAL(DatabaseTypeSourceEnum.DATABASE_TYPE_DECIMAL, s -> new BigDecimal(s)),

  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type <code>CHAR</code>.
   */
  DATABASE_TYPE_CHAR(DatabaseTypeSourceEnum.DATABASE_TYPE_CHAR, s -> s),

  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type <code>VARCHAR</code>.
   */
  DATABASE_TYPE_VARCHAR(DatabaseTypeSourceEnum.DATABASE_TYPE_VARCHAR, s -> s),

  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type <code>LONGVARCHAR</code>.
   */
  DATABASE_TYPE_LONGVARCHAR(DatabaseTypeSourceEnum.DATABASE_TYPE_LONGVARCHAR, s -> s),

  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type <code>DATE</code>.
   */
  DATABASE_TYPE_DATE(DatabaseTypeSourceEnum.DATABASE_TYPE_DATE, s -> s),

  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type <code>TIME</code>.
   */
  DATABASE_TYPE_TIME(DatabaseTypeSourceEnum.DATABASE_TYPE_TIME, s -> s),

  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type <code>TIMESTAMP</code>.
   */
  DATABASE_TYPE_TIMESTAMP(DatabaseTypeSourceEnum.DATABASE_TYPE_TIMESTAMP, s -> s),

  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type <code>BINARY</code>.
   */
  DATABASE_TYPE_BINARY(DatabaseTypeSourceEnum.DATABASE_TYPE_BINARY, s -> s.getBytes()),

  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type <code>VARBINARY</code>.
   */
  DATABASE_TYPE_VARBINARY(DatabaseTypeSourceEnum.DATABASE_TYPE_VARBINARY, s -> s.getBytes()),

  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type <code>LONGVARBINARY</code>.
   */
  DATABASE_TYPE_LONGVARBINARY(
      DatabaseTypeSourceEnum.DATABASE_TYPE_LONGVARBINARY, s -> s.getBytes()),

  /**
   * The constant in the Java programming language that identifies the generic SQL value <code>NULL
   * </code>.
   */
  DATABASE_TYPE_NULL(DatabaseTypeSourceEnum.DATABASE_TYPE_NULL, s -> null),

  /**
   * The constant in the Java programming language that indicates that the SQL type is
   * database-specific and gets mapped to a Java object that can be accessed via the methods <code>
   * getObject</code> and <code>setObject</code>.
   */
  DATABASE_TYPE_OTHER(DatabaseTypeSourceEnum.DATABASE_TYPE_OTHER, s -> s),

  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type <code>JAVA_OBJECT</code>.
   *
   * @since 1.2
   */
  DATABASE_TYPE_JAVA_OBJECT(DatabaseTypeSourceEnum.DATABASE_TYPE_JAVA_OBJECT, s -> s),

  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type <code>DISTINCT</code>.
   *
   * @since 1.2
   */
  DATABASE_TYPE_DISTINCT(DatabaseTypeSourceEnum.DATABASE_TYPE_DISTINCT, s -> s),

  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type <code>STRUCT</code>.
   *
   * @since 1.2
   */
  DATABASE_TYPE_STRUCT(DatabaseTypeSourceEnum.DATABASE_TYPE_STRUCT, s -> s),

  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type <code>ARRAY</code>.
   *
   * @since 1.2
   */
  DATABASE_TYPE_ARRAY(DatabaseTypeSourceEnum.DATABASE_TYPE_ARRAY, s -> s),

  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type <code>BLOB</code>.
   *
   * @since 1.2
   */
  DATABASE_TYPE_BLOB(DatabaseTypeSourceEnum.DATABASE_TYPE_BLOB, s -> s),

  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type <code>CLOB</code>.
   *
   * @since 1.2
   */
  DATABASE_TYPE_CLOB(DatabaseTypeSourceEnum.DATABASE_TYPE_CLOB, s -> s),

  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type <code>REF</code>.
   *
   * @since 1.2
   */
  DATABASE_TYPE_REF(DatabaseTypeSourceEnum.DATABASE_TYPE_REF, s -> s),

  /**
   * The constant in the Java programming language, somtimes referred to as a type code, that
   * identifies the generic SQL type <code>DATALINK</code>.
   *
   * @since 1.4
   */
  DATABASE_TYPE_DATALINK(DatabaseTypeSourceEnum.DATABASE_TYPE_DATALINK, s -> s),

  /**
   * The constant in the Java programming language, somtimes referred to as a type code, that
   * identifies the generic SQL type <code>BOOLEAN</code>.
   *
   * @since 1.4
   */
  DATABASE_TYPE_BOOLEAN(DatabaseTypeSourceEnum.DATABASE_TYPE_BOOLEAN, s -> Boolean.parseBoolean(s)),

  // ------------------------- JDBC 4.0 -----------------------------------
  //// ------------------------- JDBC 4.0 -----------------------------------

  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type <code>ROWID</code>
   *
   * @since 1.6
   */
  DATABASE_TYPE_ROWID(DatabaseTypeSourceEnum.DATABASE_TYPE_ROWID, s -> s),

  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type <code>NCHAR</code>
   *
   * @since 1.6
   */
  DATABASE_TYPE_NCHAR(DatabaseTypeSourceEnum.DATABASE_TYPE_NCHAR, s -> s),

  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type <code>NVARCHAR</code>.
   *
   * @since 1.6
   */
  DATABASE_TYPE_NVARCHAR(DatabaseTypeSourceEnum.DATABASE_TYPE_NVARCHAR, s -> s),

  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type <code>LONGNVARCHAR</code>.
   *
   * @since 1.6
   */
  DATABASE_TYPE_LONGNVARCHAR(DatabaseTypeSourceEnum.DATABASE_TYPE_LONGNVARCHAR, s -> s),

  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type <code>NCLOB</code>.
   *
   * @since 1.6
   */
  DATABASE_TYPE_NCLOB(DatabaseTypeSourceEnum.DATABASE_TYPE_NCLOB, s -> s),

  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type <code>XML</code>.
   *
   * @since 1.6
   */
  DATABASE_TYPE_SQLXML(DatabaseTypeSourceEnum.DATABASE_TYPE_SQLXML, s -> s),

  // --------------------------JDBC 4.2 -----------------------------

  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type {@code REF CURSOR}.
   *
   * @since 1.8
   */
  DATABASE_TYPE_REF_CURSOR(DatabaseTypeSourceEnum.DATABASE_TYPE_REF_CURSOR, s -> s),

  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type {@code TIME WITH TIMEZONE}.
   *
   * @since 1.8
   */
  DATABASE_TYPE_TIME_WITH_TIMEZONE(DatabaseTypeSourceEnum.DATABASE_TYPE_TIME_WITH_TIMEZONE, s -> s),

  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type {@code TIMESTAMP WITH TIMEZONE}.
   *
   * @since 1.8
   */
  DATABASE_TYPE_TIMESTAMP_WITH_TIMEZONE(
      DatabaseTypeSourceEnum.DATABASE_TYPE_TIMESTAMP_WITH_TIMEZONE, s -> s);

  private DatabaseTypeSourceEnum dbtype;

  private Function<String, Object> parseFunction;

  DatabaseTypeSourceMysqlEnum(
      DatabaseTypeSourceEnum dbtype, Function<String, Object> parseFunction) {
    this.dbtype = dbtype;
    this.parseFunction = parseFunction;
  }
}
