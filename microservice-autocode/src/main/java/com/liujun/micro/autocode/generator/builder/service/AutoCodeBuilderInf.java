package com.liujun.micro.autocode.generator.builder.service;

import com.liujun.micro.autocode.generator.builder.entity.GenerateCodeContext;

/**
 * 生成代码的接口
 *
 * @version 0.0.1
 * @author liujun
 */
public interface AutoCodeBuilderInf {

  /** 代码构建之前的数据准备工作 */
  void generateInit();

  /**
   * 用来生成代码的操作 方法描述
   *
   * @param param 代码生成器的上下文信息
   */
  void generate(GenerateCodeContext param);
}
