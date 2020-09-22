package com.liujun.micro.autocode.generator.builder.operator.code;

import com.liujun.micro.autocode.generator.builder.entity.GenerateCodeContext;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeInterfaceErrorCodeCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeResourceI18nEnUsCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeResourceI18nZhCnCreate;
import org.junit.Test;

/**
 * 错误码资源
 *
 * @author liujun
 * @version 0.0.1
 */
public class JavaCodeResourceI18NZhCnCreateTest {

  @Test
  public void testGenerate() {
    JavaCodeInterfaceErrorCodeCreate instance = new JavaCodeInterfaceErrorCodeCreate();
    JavaCodeResourceI18nZhCnCreate codeResource = new JavaCodeResourceI18nZhCnCreate();
    JavaCodeResourceI18nEnUsCreate resourceData = new JavaCodeResourceI18nEnUsCreate();

    GenerateCodeContext context = CodeBaseUtils.getBase();

    // 进行数据生成
    instance.generateCode(context);
    codeResource.generateCode(context);
    resourceData.generateCode(context);
  }
}
