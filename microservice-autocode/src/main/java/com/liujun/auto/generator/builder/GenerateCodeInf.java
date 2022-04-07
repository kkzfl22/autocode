package com.liujun.auto.generator.builder;

import com.liujun.auto.generator.builder.ddd.entity.GenerateCodeContext;

/**
 * 生成代码的接口
 *
 * @author liujun
 * @version 1.0.0
 */
public interface GenerateCodeInf {

  /**
   * 代码生成接口
   *
   * @param param 用于生成代码的相关参数信息
   */
  void generateCode(GenerateCodeContext param);
}
