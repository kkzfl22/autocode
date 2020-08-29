package com.liujun.micro.autocode.generator.run;

import com.liujun.micro.autocode.config.generate.GenerateConfig;
import com.liujun.micro.autocode.generator.builder.entity.GenerateCodeContext;
import com.liujun.micro.autocode.generator.builder.service.AutoCodeBeanBuilder;

/**
 * 微服务相当的类的代码生成
 *
 * @since 2018年4月15日 下午9:39:49
 * @version 0.0.1
 * @author liujun
 */
public class CodeCreateMicroServiceRun {

  public static void main(String[] args) {
    // 代码基础路径
    String basePkg = "com.paraview.security.pap.microservice.";
    // 表空间
    String tableSpace = "autocode";

    /** 模块名称 */
    String modelName = "attribute";
    // 文件输出代码路径
    String filePath = "D:/java/encode/javacode/";

    GenerateCodeContext param =
        new GenerateCodeContext(
            filePath, basePkg, modelName, tableSpace, GenerateConfig.INSTANCE.getCfgEntity());
    AutoCodeBeanBuilder builder = new AutoCodeBeanBuilder(param);

    // 1,数据准备,从数据库捞取数据
    builder.builderInit();

    // 使用javaBean代码的生成操作
    // 生成bean
    builder.addPersistObject();

    // 生成代码
    builder.generateCode();

    System.out.println("生成结束");
  }
}
