package com.liujun.micro.autocode.generator.builder.constant;

/**
 * 上下文配制枚举
 *
 * @author liujun
 * @version 0.0.1
 */
public enum ContextCfgEnum {

  /** 单元测试中需要公共导入的包 */
  CONTEXT_JUNIT_IMPORT("junit_import_list");

  /** 配制的key信息 */
  private String key;

  ContextCfgEnum(String key) {
    this.key = key;
  }

  public String getKey() {
    return key;
  }
}
