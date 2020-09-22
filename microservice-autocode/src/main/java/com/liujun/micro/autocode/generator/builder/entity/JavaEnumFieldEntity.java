package com.liujun.micro.autocode.generator.builder.entity;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * java的枚举属性定义
 *
 * @author liujun
 * @version 0.0.1
 */
@Data
@Builder
@ToString
public class JavaEnumFieldEntity {

  /** 方法的注释 */
  protected String comment;

  /** 名称 */
  protected String name;

  /** 值信息 */
  private String value;
}
