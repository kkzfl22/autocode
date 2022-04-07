package com.liujun.auto.generator.builder.operator.code;

import com.liujun.auto.generator.builder.entity.GenerateCodeContext;
import com.liujun.auto.generator.builder.operator.ddd.full.facade.JavaCodeFacadeErrorCodeCreate;
import com.liujun.auto.generator.builder.operator.ddd.full.other.JavaCodeResourceI18nEnUsCreate;
import com.liujun.auto.generator.builder.operator.ddd.full.other.JavaCodeResourceI18nZhCnCreate;
import org.junit.Test;

/**
 * 错误码资源
 *
 * @author liujun
 * @version 0.0.1
 */
public class JavaCodeResourceI18nCreateTest {

  @Test
  public void testGenerate() {
    JavaCodeFacadeErrorCodeCreate instance = new JavaCodeFacadeErrorCodeCreate();
    JavaCodeResourceI18nZhCnCreate codeResource = new JavaCodeResourceI18nZhCnCreate();
    JavaCodeResourceI18nEnUsCreate resourceData = new JavaCodeResourceI18nEnUsCreate();

    GenerateCodeContext context = CodeBaseUtils.getBase();

    // 进行数据生成
    instance.generateCode(context);
    codeResource.generateCode(context);
    resourceData.generateCode(context);
  }
}
