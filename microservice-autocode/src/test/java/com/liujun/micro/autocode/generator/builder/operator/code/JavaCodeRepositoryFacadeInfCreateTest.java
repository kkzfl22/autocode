package com.liujun.micro.autocode.generator.builder.operator.code;

import com.liujun.micro.autocode.generator.builder.entity.GenerateCodeContext;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeDomainObjectCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeRepositoryFacadeInfCreate;
import org.junit.Test;

/**
 * 领域方法的生成
 *
 * @author liujun
 * @version 0.0.1
 */
public class JavaCodeRepositoryFacadeInfCreateTest {

  @Test
  public void testGenerate() {
    JavaCodeDomainObjectCreate instance = new JavaCodeDomainObjectCreate();
    JavaCodeRepositoryFacadeInfCreate domainService = new JavaCodeRepositoryFacadeInfCreate();

    GenerateCodeContext context = CodeBaseUtils.getBase();
    // 进行数据生成
    instance.generateCode(context);
    domainService.generateCode(context);
  }
}
