/*
 * Copyright (C), 2008-2021, Paraview All Rights Reserved.
 */
package com.liujun.auto.generator.javalanguage.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 最基础的代码行信息
 *
 * @author liujun
 * @since 2022/4/6
 */
@Getter
@Setter
@ToString
public class ContextLineCodeBase {

  /** 空行数 */
  private int topLine;

  /** 左边空格数 */
  private int leftSpace;

  /** 行注释信息 */
  private ContextFieldDocument document;

  /** 代码行之前的空格 */
  private int spaceFrontNum;

  /** 返回的标识 */
  private Boolean returnFlag;
}
