/*
 * Copyright (C), 2008-2021, Paraview All Rights Reserved.
 */
package com.liujun.auto.generator.javalanguage.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 方法的参数注释信息
 *
 * @author liujun
 * @since 2022/4/6
 */
@Getter
@Setter
@ToString
@Builder
public class ContextMethodParamDocument {

  /** 注解的符号,目前包括@Param,@return @Exception */
  private String annotation;

  /** 参数的名字,当为@return 可能不存在参数名称 */
  private String paramName;

  /** 参数的描述 */
  private String comment;
}
