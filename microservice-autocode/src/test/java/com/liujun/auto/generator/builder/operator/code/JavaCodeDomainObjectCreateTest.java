package com.liujun.auto.generator.builder.operator.code;

import com.liujun.auto.generator.builder.ddd.full.domain.JavaCodeDomainObjectCreate;
import org.junit.Test;

/**
 * 进行persistObject生成的测试
 *
 * @author liujun
 * @version 0.0.1
 */
public class JavaCodeDomainObjectCreateTest {

  @Test
  public void testGenerate() {
    JavaCodeDomainObjectCreate instance = new JavaCodeDomainObjectCreate();

    // 进行数据生成
    instance.generateCode(CodeBaseUtils.getBase());
  }
}
