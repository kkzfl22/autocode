package com.liujun.auto.generator.builder.ddd.entity;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * java的枚举属性定义
 *
 * @author liujun
 * @version 0.0.1
 */
@Builder
public class JavaEnumFieldEntity {

  /** 方法的注释 */
  protected String comment;

  /** 名称 */
  protected String name;

  /** 值信息 */
  private String value;

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("JavaEnumFieldEntity{");
    sb.append("comment='").append(comment).append('\'');
    sb.append(", name='").append(name).append('\'');
    sb.append(", value='").append(value).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
