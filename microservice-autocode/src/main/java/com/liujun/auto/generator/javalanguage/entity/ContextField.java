package com.liujun.auto.generator.javalanguage.entity;

import com.liujun.auto.generator.javalanguage.constant.VisitEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 类的属性信息
 *
 * @author liujun
 * @since 2022/4/6
 */
@Getter
@Setter
@ToString
public class ContextField extends ContextBase {

  /** 空格数 */
  private int topLine;

  /** 左边空格数 */
  private int leftSpace;

  /** 属性的注释信息 */
  private ContextFieldDocument document;

  /** 属性的的注解信息 */
  private List<ContextAnnotationList> annotation;

  /** 属性的访问修饰符 */
  private VisitEnum visit;

  /** 静态标识 */
  private Boolean staticFlag;

  /** 不可修改的标识 final标识的 */
  private Boolean finalFlag;

  /** 返回的类型 */
  private String returnClass;

  /** 属性名称 */
  private String name;

  /** 属性值信息 */
  private String value;
}
