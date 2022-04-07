package com.liujun.auto.constant;

/**
 * 方法的类型
 *
 * @author liujun
 * @version 0.0.1
 */
public enum MethodTypeEnum {
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

  MethodTypeEnum(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }

  public static MethodTypeEnum getType(String typeInfo) {
    for (MethodTypeEnum typeItem : values()) {
      if (typeItem.getType().equals(typeInfo)) {
        return typeItem;
      }
    }

    return null;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("OperatorTypeEnum{");
    sb.append("type='").append(type).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
