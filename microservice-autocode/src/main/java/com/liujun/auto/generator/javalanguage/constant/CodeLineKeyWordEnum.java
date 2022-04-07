/*
 * Copyright (C), 2008-2021, Paraview All Rights Reserved.
 */
package com.liujun.auto.generator.javalanguage.constant;

import lombok.Getter;
import lombok.ToString;

/**
 * 代码行中的关键字信息
 *
 * @author liujun
 * @since 2022/4/6
 */
@Getter
@ToString
public enum CodeLineKeyWordEnum {

  /** 判断语名 */
  IF("if"),

  /** else if */
  ELSEIF("else if"),

  /** else */
  ELSE("else"),

  /** */
  SWITCH("switch"),

  /** case */
  CASE("case"),

  /** 中断 */
  BREAK("break"),

  /** 默认 */
  DEFAULT("default"),

  /** 返回 */
  RETURN("return");

  private String keyWord;

  private CodeLineKeyWordEnum(String keyWord) {
    this.keyWord = keyWord;
  }
}
