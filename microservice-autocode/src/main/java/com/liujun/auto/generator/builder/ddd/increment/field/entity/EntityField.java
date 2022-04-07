package com.liujun.auto.generator.builder.ddd.increment.field.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * 属性字段
 *
 * @author liujun
 * @version 0.0.1
 */
@Getter
@Setter
public class EntityField extends EntityInfo {

  /** 值信息 */
  private String value;

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("EntityField{");
    sb.append("value='").append(value).append('\'');
    sb.append('}');
    sb.append(super.toString());
    return sb.toString();
  }
}
