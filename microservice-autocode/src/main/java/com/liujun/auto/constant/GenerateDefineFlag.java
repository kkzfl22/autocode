package com.liujun.auto.constant;

/**
 * 代码中的占位符信息
 *
 * @author liujun
 * @version 0.0.1
 */
public enum GenerateDefineFlag {

  /** 表名的定义符,表示使用以表名转换为对象信息 */
  TABLE_NAME("table_name"),
  ;

  /** 定义的占位符信息 */
  private final String defineFlag;

  GenerateDefineFlag(String defineFlag) {
    this.defineFlag = defineFlag;
  }

  public String getDefineFlag() {
    return defineFlag;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("GenerateDefineFlag{");
    sb.append("defineFlag='").append(defineFlag).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
