package com.liujun.auto.generator.run;

import com.liujun.auto.config.generate.GenerateConfigProcess;
import com.liujun.auto.generator.run.builder.GenerateCodeBuilder;

/**
 * 相当的类的代码生成
 *
 * @author liujun
 * @version 0.0.1
 * @since 2018年4月15日 下午9:39:49
 */
public class CodeCreateMicroServiceRun {

  public static final CodeCreateMicroServiceRun INSTANCE = new CodeCreateMicroServiceRun();

  public void generate() {

    // 生成构建器
    GenerateCodeBuilder builder =
        new GenerateCodeBuilder(GenerateConfigProcess.INSTANCE.getCfgEntity());

    // 进行代码的生成操作
    builder.generate();

    System.out.println("代码生成结束");
  }
}
