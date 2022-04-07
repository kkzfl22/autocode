package com.liujun.auto.constant;

/**
 * mybatis的操作符
 *
 * @author liujun
 * @version 0.0.1
 */
public enum MyBatisOperatorFlag {

  /** 使用范围内匹配 */
  IN("in"),

  /** 判断是否相等 */
  EQUAL("="),
  ;

  /** 操作符 */
  private final String operator;

  MyBatisOperatorFlag(String operator) {
    this.operator = operator;
  }

  public String getOperator() {
    return operator;
  }

  /**
   * 获取操作符的枚举
   *
   * @param typeInfo 操作符信息
   * @return
   */
  public static MyBatisOperatorFlag getOperator(String typeInfo) {
    for (MyBatisOperatorFlag item : values()) {
      if (item.getOperator().equals(typeInfo)) {
        return item;
      }
    }

    // 默认使用等于操作符
    return MyBatisOperatorFlag.EQUAL;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("MyBatisOperatorFlag{");
    sb.append("operator='").append(operator).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
