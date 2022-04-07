package com.liujun.auto.config.generate.entity;

/**
 * 代码生成器的配制文件
 *
 * @author liujun
 * @version 0.0.1
 * @date 2019/09/10
 */
public class GenerateConfigEntity {

  /** 生成的代码相关的配制信息 */
  private Generate generate;

  public Generate getGenerate() {
    return generate;
  }

  public void setGenerate(Generate generate) {
    this.generate = generate;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("GenerateConfigEntity{");
    sb.append("generate=").append(generate);
    sb.append('}');
    return sb.toString();
  }
}
