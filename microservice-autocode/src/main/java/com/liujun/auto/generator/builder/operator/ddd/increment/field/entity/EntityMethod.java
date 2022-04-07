package com.liujun.auto.generator.builder.operator.ddd.increment.field.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * 方法的属性信息
 *
 * @author liujun
 * @version 0.0.1
 */
@Getter
@Setter
public class EntityMethod extends EntityInfo {

  /** 方法的参数信息 */
  private String methodParam;

  /** 方法的内容信息 */
  private String methodContext;

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("EntityMethod{");
    sb.append("methodParam='").append(methodParam).append('\'');
    sb.append(", methodContext='").append(methodContext).append('\'');
    sb.append('}');
    sb.append(super.toString());
    return sb.toString();
  }
}
