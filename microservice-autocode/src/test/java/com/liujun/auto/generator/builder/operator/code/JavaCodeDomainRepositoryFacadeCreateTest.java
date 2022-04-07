package com.liujun.auto.generator.builder.operator.code;

import com.liujun.auto.generator.builder.ddd.entity.GenerateCodeContext;
import com.liujun.auto.generator.builder.ddd.full.domain.JavaCodeDomainObjectCreate;
import com.liujun.auto.generator.builder.ddd.full.domain.JavaCodeDomainRepositoryFacadeCreate;
import org.junit.Test;

/**
 * 领域方法的生成
 *
 * @author liujun
 * @version 0.0.1
 */
public class JavaCodeDomainRepositoryFacadeCreateTest {

  @Test
  public void testGenerate() {
    JavaCodeDomainObjectCreate instance = new JavaCodeDomainObjectCreate();
    JavaCodeDomainRepositoryFacadeCreate domainService = new JavaCodeDomainRepositoryFacadeCreate();

    GenerateCodeContext context = CodeBaseUtils.getBase();
    // 进行数据生成
    instance.generateCode(context);
    domainService.generateCode(context);
  }
}
