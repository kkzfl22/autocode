package com.liujun.micro.autocode.generator.builder.operator.code;

import com.liujun.micro.autocode.config.generate.GenerateConfigProcess;
import com.liujun.micro.autocode.config.generate.entity.GenerateConfigEntity;
import com.liujun.micro.autocode.generator.builder.entity.GenerateCodeContext;
import com.liujun.micro.autocode.generator.run.builder.GenerateCodeBuilder;

/**
 * 基础的代码路径
 *
 * @author liujun
 * @version 0.0.1
 */
public class CodeBaseUtils {

  /**
   * 基础的代码
   *
   * @return 构建查询参数信息
   */
  public static GenerateCodeContext getBase() {

    // 生成构建器
    GenerateCodeBuilder builder =
        new GenerateCodeBuilder(GenerateConfigProcess.INSTANCE.getCfgEntity());

    // 数据库的初始化
    builder.databaseLoader();

    return builder.getContext();
  }

  /**
   * 构建字段替换器的上下文信息
   *
   * @return 构建查询参数信息
   */
  public static GenerateCodeContext getFieldReplaceBase() {

    GenerateConfigEntity entity = GenerateConfigProcess.INSTANCE.getCfgEntity();

    entity.getGenerate().setModel(2);

    // 生成构建器
    GenerateCodeBuilder builder = new GenerateCodeBuilder(entity);

    // 数据库的初始化
    builder.databaseLoader();

    return builder.getContext();
  }
}
