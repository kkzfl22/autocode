/*
 *
 */
package com.liujun.auto.generator.builder.ddd.constant;

import lombok.ToString;

/**
 * 路径的名称枚举，用于标识出特殊标识的路径信息
 *
 * @author liujun
 * @since 2022/4/7
 */
@ToString
public enum DirNameEnum {

  /** 工程的基础路径 */
  BASE("base"),

  /** 模块的名称 */
  MODEL_NAME("modelName"),

  /** 基础层中，infrastructure与moduleName之间的路径 */
  PREFIX("prefix");

  /** 方法的操作定义的key */
  private final String name;

  DirNameEnum(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
