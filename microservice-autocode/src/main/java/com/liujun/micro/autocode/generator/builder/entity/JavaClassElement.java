package com.liujun.micro.autocode.generator.builder.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 类公共元素信息
 *
 * @author liujun
 * @version 0.0.1
 */
@Getter
@Setter
@ToString
public class JavaClassElement {

  /** 方法的注释 */
  protected String comment;

  /** 方法注解 */
  protected List<String> annotationList;

  /** 方法访问修饰符 */
  protected String visit;

  /** 静态标识 */
  protected String staticFlag;

  /** final标识 */
  protected String finalFlag;

  /** 类型信息 */
  protected String type;

  /** 名称 */
  protected String name;
}
