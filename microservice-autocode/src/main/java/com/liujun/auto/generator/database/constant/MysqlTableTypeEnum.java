package com.liujun.auto.generator.database.constant;

/**
 * @author liujun
 * @since 2022/4/29
 */
public enum MysqlTableTypeEnum {

  /** 普通的表信息 */
  TABLE("BASE TABLE"),

  /** 视图类型 */
  VIEW("VIEW"),
  ;

  /** 数据库的类型 */
  private String dbType;

  MysqlTableTypeEnum(String dbType) {
    this.dbType = dbType;
  }

  public String getDbType() {
    return dbType;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("MysqlTableTypeEnum{");
    sb.append("dbType='").append(dbType).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
