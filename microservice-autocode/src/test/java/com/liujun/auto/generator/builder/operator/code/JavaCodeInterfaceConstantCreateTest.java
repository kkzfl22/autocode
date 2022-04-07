package com.liujun.auto.generator.builder.operator.code;

import com.liujun.auto.generator.builder.ddd.full.facade.JavaCodeFacadeConstantCreate;
import org.junit.Test;

/**
 * @author liujun
 * @version 0.0.1
 */
public class JavaCodeInterfaceConstantCreateTest {

  @Test
  public void testGenerate() {
    JavaCodeFacadeConstantCreate instance = new JavaCodeFacadeConstantCreate();

    // 进行数据生成
    instance.generateCode(CodeBaseUtils.getBase());
  }
}
