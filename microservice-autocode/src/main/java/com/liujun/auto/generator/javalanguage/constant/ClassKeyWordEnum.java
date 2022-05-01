package com.liujun.auto.generator.javalanguage.constant;

import lombok.Getter;
import lombok.ToString;

/**
 * 类的关键字定义
 *
 * @author liujun
 * @since 2022/4/6
 */
@Getter
@ToString
public enum ClassKeyWordEnum {

  /** 类的关键字 */
  CLASS("class"),

  /** 接口关键字 */
  INTERFACE("interface"),

  /** 枚举的关键字 */
  ENUM("enum");

  private String keyWord;

  private ClassKeyWordEnum(String keyWord) {
    this.keyWord = keyWord;
  }
}
