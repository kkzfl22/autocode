package com.liujun.auto.generator.database.constant;

/**
 * database message key
 *
 * @author liujun
 * @version 0.0.1
 * @date 2018/09/13
 */
public enum DatabaseTypeSourceEnum {
  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type <code>BIT</code>.
   */
  DATABASE_TYPE_BIT("DATABASE.TYPE.BIT"),

  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type <code>TINYINT</code>.
   */
  DATABASE_TYPE_TINYINT("DATABASE.TYPE.TINYINT"),

  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type <code>SMALLINT</code>.
   */
  DATABASE_TYPE_SMALLINT("DATABASE.TYPE.SMALLINT"),

  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type <code>INTEGER</code>.
   */
  DATABASE_TYPE_INTEGER("DATABASE.TYPE.INTEGER"),

  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type <code>BIGINT</code>.
   */
  DATABASE_TYPE_BIGINT("DATABASE.TYPE.BIGINT"),

  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type <code>FLOAT</code>.
   */
  DATABASE_TYPE_FLOAT("DATABASE.TYPE.FLOAT"),

  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type <code>REAL</code>.
   */
  DATABASE_TYPE_REAL("DATABASE.TYPE.REAL"),

  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type <code>DOUBLE</code>.
   */
  DATABASE_TYPE_DOUBLE("DATABASE.TYPE.DOUBLE"),

  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type <code>NUMERIC</code>.
   */
  DATABASE_TYPE_NUMERIC("DATABASE.TYPE.NUMERIC"),

  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type <code>DECIMAL</code>.
   */
  DATABASE_TYPE_DECIMAL("DATABASE.TYPE.DECIMAL"),

  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type <code>CHAR</code>.
   */
  DATABASE_TYPE_CHAR("DATABASE.TYPE.CHAR"),

  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type <code>VARCHAR</code>.
   */
  DATABASE_TYPE_VARCHAR("DATABASE.TYPE.VARCHAR"),

  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type <code>LONGVARCHAR</code>.
   */
  DATABASE_TYPE_LONGVARCHAR("DATABASE.TYPE.LONGVARCHAR"),

  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type <code>DATE</code>.
   */
  DATABASE_TYPE_DATE("DATABASE.TYPE.DATE"),

  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type <code>TIME</code>.
   */
  DATABASE_TYPE_TIME("DATABASE.TYPE.TIME"),

  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type <code>TIMESTAMP</code>.
   */
  DATABASE_TYPE_TIMESTAMP("DATABASE.TYPE.TIMESTAMP"),

  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type <code>BINARY</code>.
   */
  DATABASE_TYPE_BINARY("DATABASE.TYPE.BINARY"),

  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type <code>VARBINARY</code>.
   */
  DATABASE_TYPE_VARBINARY("DATABASE.TYPE.VARBINARY"),

  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type <code>LONGVARBINARY</code>.
   */
  DATABASE_TYPE_LONGVARBINARY("DATABASE.TYPE.LONGVARBINARY"),

  /**
   * The constant in the Java programming language that identifies the generic SQL value <code>NULL
   * </code>.
   */
  DATABASE_TYPE_NULL("DATABASE.TYPE.NULL"),

  /**
   * The constant in the Java programming language that indicates that the SQL type is
   * database-specific and gets mapped to a Java object that can be accessed via the methods <code>
   * getObject</code> and <code>setObject</code>.
   */
  DATABASE_TYPE_OTHER("DATABASE.TYPE.OTHER"),

  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type <code>JAVA_OBJECT</code>.
   *
   * @since 1.2
   */
  DATABASE_TYPE_JAVA_OBJECT("DATABASE.TYPE.JAVA_OBJECT"),

  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type <code>DISTINCT</code>.
   *
   * @since 1.2
   */
  DATABASE_TYPE_DISTINCT("DATABASE.TYPE.DISTINCT"),

  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type <code>STRUCT</code>.
   *
   * @since 1.2
   */
  DATABASE_TYPE_STRUCT("DATABASE.TYPE.STRUCT"),

  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type <code>ARRAY</code>.
   *
   * @since 1.2
   */
  DATABASE_TYPE_ARRAY("DATABASE.TYPE.ARRAY"),

  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type <code>BLOB</code>.
   *
   * @since 1.2
   */
  DATABASE_TYPE_BLOB("DATABASE.TYPE.BLOB"),

  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type <code>CLOB</code>.
   *
   * @since 1.2
   */
  DATABASE_TYPE_CLOB("DATABASE.TYPE.CLOB"),

  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type <code>REF</code>.
   *
   * @since 1.2
   */
  DATABASE_TYPE_REF("DATABASE.TYPE.REF"),

  /**
   * The constant in the Java programming language, somtimes referred to as a type code, that
   * identifies the generic SQL type <code>DATALINK</code>.
   *
   * @since 1.4
   */
  DATABASE_TYPE_DATALINK("DATABASE.TYPE.DATALINK"),

  /**
   * The constant in the Java programming language, somtimes referred to as a type code, that
   * identifies the generic SQL type <code>BOOLEAN</code>.
   *
   * @since 1.4
   */
  DATABASE_TYPE_BOOLEAN("DATABASE.TYPE.BOOLEAN"),

  // ------------------------- JDBC 4.0 -----------------------------------
  //// ------------------------- JDBC 4.0 -----------------------------------

  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type <code>ROWID</code>
   *
   * @since 1.6
   */
  DATABASE_TYPE_ROWID("DATABASE.TYPE.ROWID"),

  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type <code>NCHAR</code>
   *
   * @since 1.6
   */
  DATABASE_TYPE_NCHAR("DATABASE.TYPE.NCHAR"),

  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type <code>NVARCHAR</code>.
   *
   * @since 1.6
   */
  DATABASE_TYPE_NVARCHAR("DATABASE.TYPE.NVARCHAR"),

  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type <code>LONGNVARCHAR</code>.
   *
   * @since 1.6
   */
  DATABASE_TYPE_LONGNVARCHAR("DATABASE.TYPE.LONGNVARCHAR"),

  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type <code>NCLOB</code>.
   *
   * @since 1.6
   */
  DATABASE_TYPE_NCLOB("DATABASE.TYPE.NCLOB"),

  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type <code>XML</code>.
   *
   * @since 1.6
   */
  DATABASE_TYPE_SQLXML("DATABASE.TYPE.SQLXML"),

  // --------------------------JDBC 4.2 -----------------------------

  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type {@code REF CURSOR}.
   *
   * @since 1.8
   */
  DATABASE_TYPE_REF_CURSOR("DATABASE.TYPE.REF_CURSOR"),

  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type {@code TIME WITH TIMEZONE}.
   *
   * @since 1.8
   */
  DATABASE_TYPE_TIME_WITH_TIMEZONE("DATABASE.TYPE.TIME_WITH_TIMEZONE"),

  /**
   * The constant in the Java programming language, sometimes referred to as a type code, that
   * identifies the generic SQL type {@code TIMESTAMP WITH TIMEZONE}.
   *
   * @since 1.8
   */
  DATABASE_TYPE_TIMESTAMP_WITH_TIMEZONE("DATABASE.TYPE.TIMESTAMP_WITH_TIMEZONE"),

  /** oracle time local timezone */
  DATABASE_TYPE_TIMESTAMP_WITH_LOCAL_TIMEZONE("DATABASE.TYPE.TIMESTAMP_WITH_LOCAL_TIMEZONE"),
  ;

  private String proKey;

  DatabaseTypeSourceEnum(String proKey) {
    this.proKey = proKey;
  }

  public String getProKey() {
    return proKey;
  }



  public static DatabaseTypeSourceEnum getDataBaseType(String key) {
    for (DatabaseTypeSourceEnum dbtype : values()) {
      if (dbtype.proKey.equals(key)) {
        return dbtype;
      }
    }
    return null;
  }
}
