package com.liujun.micro.autocode.generator.database.constant;

import com.liujun.micro.autocode.constant.Symbol;

/**
 * 数据库类型信息
 *
 * @author liujun
 * @version 1.0.0
 */
public enum DatabaseTypeEnum {
  /** 数据库类型为mysql */
  MYSQL("mysql"),

  /** 数据库类型为oracle */
  ORACLE("oracle"),
  ;

  /** 数据库类型 */
  private String databaseType;

  DatabaseTypeEnum(String databaseType) {
    this.databaseType = databaseType;
  }

  public String getDatabaseType() {
    return databaseType;
  }

  /**
   * get proerties the type
   *
   * @param propertisName proties name
   * @return type info
   */
  public static DatabaseTypeEnum getPropertiesDbType(String propertisName) {
    if (propertisName != null) {
      String name =
          propertisName.substring(
              propertisName.indexOf(Symbol.UNDER_LINE) + 1,
              propertisName.lastIndexOf(Symbol.POINT));

      for (DatabaseTypeEnum types : values()) {
        if (types.databaseType.equalsIgnoreCase(name)) {
          return types;
        }
      }
    }
    return null;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("DatabaseTypeEnum{");
    sb.append("databaseType='").append(databaseType).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
