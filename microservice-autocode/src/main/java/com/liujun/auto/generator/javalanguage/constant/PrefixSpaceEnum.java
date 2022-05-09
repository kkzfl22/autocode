/*
 * Copyright (C), 2008-2021, Paraview All Rights Reserved.
 */
package com.liujun.auto.generator.javalanguage.constant;

import lombok.Getter;
import lombok.ToString;

/**
 * 行前面的空格
 *
 * @author liujun
 * @since 2022/5/8
 */
@Getter
@ToString
public enum PrefixSpaceEnum {

  /** 空，不输出空格 */
  NONE(0),

  /** 一级，一个级别代码两个空格 */
  ONE(1),

  /** 二级 代表4个空格 */
  TWO(2),

  /** 三级，代表6个空格 */
  THIRD(3);
  ;

  /** 级别信息 */
  private int level;

  PrefixSpaceEnum(int level) {
    this.level = level;
  }
}
