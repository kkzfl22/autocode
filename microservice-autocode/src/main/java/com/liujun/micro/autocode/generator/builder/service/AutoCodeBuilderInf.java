package com.liujun.micro.autocode.generator.builder.service;

import com.liujun.micro.autocode.generator.builder.entity.GenerateCodeContext;

/** 生成代码的接口 */
public interface AutoCodeBuilderInf {

  /** 代码构建之前的数据准备工作 */
  void generateInit();

  /**
   * 用来生成代码的操作 方法描述
   *
   * @param param @创建日期 2016年10月12日
   */
  void generate(GenerateCodeContext param);
}
