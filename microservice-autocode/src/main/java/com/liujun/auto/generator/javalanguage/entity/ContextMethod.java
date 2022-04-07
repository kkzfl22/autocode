/*
 * Copyright (C), 2008-2021, Paraview All Rights Reserved.
 */
package com.liujun.auto.generator.javalanguage.entity;

import com.liujun.auto.generator.javalanguage.constant.VisitEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 类的方法信息
 *
 * @author liujun
 * @since 2022/4/6
 */
@Getter
@Setter
@ToString
public class ContextMethod extends ContextBase {

  /** 空格数 */
  private int spaceLine;

  /** 方法的注释信息 */
  private ContextMethodDocument document;

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

  /** 方法的参数信息 */
  private List<ContextMethodParam> param;
}
