package com.liujun.auto.generator.builder.constant;

/**
 * 固定方法枚举
 *
 * @author liujun
 * @version 0.0.1
 */
public enum MethodFixEnum {

  /** 单个插入方法 */
  INSERT("insert", "单个数据插入"),

  /** 批量插入方法 */
  INSERT_LIST("insertList", "批量数据插入"),

  /** 数据修改方法 */
  UPDATE("update", "单个数据修改"),

  /** 单个删除方法 */
  DELETE("delete", "单个数据删除"),

  /** 批量数据根据主键删除方法 */
  DELETE_BY_IDS("deleteByIds", "批量数据删除"),

  /** 分页查询 */
  QUERY_PAGE("queryPage", "分页查询"),

  /** 单个数据按主键查询 */
  QUERY_BY_ID("queryById", "按主键id进行查询"),

  /** 按id的集合进行查询 */
  QUERY_BY_IDS("queryByIds", "按主键id进行批量查询"),
  ;

  /** 方法的操作定义的key */
  private final String key;

  /** 方法描述 */
  private final String comment;

  MethodFixEnum(String key, String comment) {
    this.key = key;
    this.comment = comment;
  }

  public String getKey() {
    return key;
  }

  public String getComment() {
    return comment;
  }

  /**
   * 获取固定的方法信息
   *
   * @param methodKey 方法标识的key
   * @return 方法的枚举信息
   */
  public static MethodFixEnum getFixMethod(String methodKey) {
    for (MethodFixEnum fixEnum : values()) {
      if (fixEnum.getKey().equals(methodKey)) {
        return fixEnum;
      }
    }

    return null;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("MethodFixEnum{");
    sb.append("key='").append(key).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
