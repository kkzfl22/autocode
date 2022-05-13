package com.liujun.auto.constant;

/**
 * 方法的类型
 *
 * @author liujun
 * @version 0.0.1
 */
public enum MethodOperatorEnum {
  /** 添加操作 */
  INSERT("insert"),

  /** 数据库的修改操作 */
  UPDATE("update"),

  /** 删除操作 */
  DELETE("delete"),

  /** 数据库查询操作 */
  QUERY("query"),

  /** 数据库分页查询 */
  QUERY_PAGE("queryPage"),

  /** 按id执行详细的查询操作 */
  DETAIL("detail"),
  ;

  /** 类型信息 */
  private final String type;

  MethodOperatorEnum(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }

  public static MethodOperatorEnum getType(String typeInfo) {
    for (MethodOperatorEnum typeItem : values()) {
      if (typeItem.getType().equals(typeInfo)) {
        return typeItem;
      }
    }

    return null;
  }

  /**
   * 检查当前是否为更新类操作，包括，添加、修改、删除
   *
   * @return 当前为插入，false 为查询
   */
  public boolean isUpdate() {

    if (MethodOperatorEnum.INSERT.equals(this)
        || MethodOperatorEnum.UPDATE.equals(this)
        || MethodOperatorEnum.DELETE.equals(this)) {
      return true;
    }

    return false;
  }

  /**
   * 当前是否为查询操作
   *
   * @return
   */
  public boolean isQuery() {
    if (MethodOperatorEnum.QUERY.equals(this)
        || MethodOperatorEnum.QUERY_PAGE.equals(this)
        || MethodOperatorEnum.DETAIL.equals(this)) {
      return true;
    }

    return false;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("OperatorTypeEnum{");
    sb.append("type='").append(type).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
