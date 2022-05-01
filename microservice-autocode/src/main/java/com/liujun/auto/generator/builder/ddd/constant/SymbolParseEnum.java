package com.liujun.auto.generator.builder.ddd.constant;

import lombok.Getter;
import lombok.ToString;

/**
 * 字符转化的枚举
 *
 * @author liujun
 * @since 2022/4/30
 */
@Getter
@ToString
public enum SymbolParseEnum {

  /** 单引号的转义 */
  SINGLE_QUOTE("'", "\\\\''"),

  /** 双引号转义 */
  QUOTE("\"", "\\\\\"");

  /** 原始字符 */
  private String src;

  /** 目标字符 */
  private String target;

  SymbolParseEnum(String src, String target) {
    this.src = src;
    this.target = target;
  }

  /**
   * 进行数据的处理
   *
   * @param src
   * @return
   */
  public static String symbolParse(String src) {
    String target = src;

    // 执行数据的转义操作
    for (SymbolParseEnum item : values()) {
      if (target.indexOf(item.getSrc()) != -1) {
        target = target.replaceAll(item.getSrc(), item.getTarget());
      }
    }

    return target;
  }
}
