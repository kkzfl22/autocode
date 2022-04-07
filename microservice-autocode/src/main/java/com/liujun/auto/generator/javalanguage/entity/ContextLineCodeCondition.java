/*
 * Copyright (C), 2008-2021, Paraview All Rights Reserved.
 */
package com.liujun.auto.generator.javalanguage.entity;

import com.liujun.micro.autocode.generator.javalanguage.constant.CodeLineKeyWordEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 带条件的代码行信息，包括if switch
 *
 * @author liujun
 * @since 2022/4/6
 */
@Getter
@Setter
@ToString
public class ContextLineCodeCondition extends com.liujun.micro.autocode.generator.javalanguage.entity.ContextLineCodeBase {

  /** 关键字信息 */
  private CodeLineKeyWordEnum keyword;

  /** 条件的信息 */
  private String condition;

  /** 条件满足后需要执行的SQL */
  private List<com.liujun.micro.autocode.generator.javalanguage.entity.ContextLineCodeBase> code;
}
