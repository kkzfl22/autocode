package com.liujun.micro.autocode.generator.builder.operator.code;

import com.liujun.micro.autocode.config.generate.GenerateConfig;
import com.liujun.micro.autocode.generator.builder.entity.GenerateCodeContext;
import com.liujun.micro.autocode.generator.builder.service.AutoCodeBeanBuilder;
import com.liujun.micro.autocode.generator.database.constant.DatabaseTypeEnum;

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
    // 代码基础路径
    String basePkg = "com.paraview.security.pap.microservice.";
    // 表空间
    String tableSpace = "autocode";
    // 模块名称
    String modelName = "attribute";
    // 文件输出代码路径
    String filePath = "D:/java/workspace/selfwork/autocode/generateCode/";

    GenerateCodeContext param =
        new GenerateCodeContext(
            filePath,
            basePkg,
            modelName,
            DatabaseTypeEnum.MYSQL,
            tableSpace,
            GenerateConfig.INSTANCE.getCfgEntity());

    AutoCodeBeanBuilder builder = new AutoCodeBeanBuilder(param);

    // 1,数据准备,从数据库捞取数据
    builder.builderInit();

    return param;
  }
}
