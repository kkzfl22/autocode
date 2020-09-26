package com.liujun.micro.autocode.generator.builder.operator.code;

import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeInterfaceConstantCreate;
import org.junit.Test;

/**
 * @author liujun
 * @version 0.0.1
 */
public class JavaCodeInterfaceConstantCreateTest {

  @Test
  public void testGenerate() {
    JavaCodeInterfaceConstantCreate instance = new JavaCodeInterfaceConstantCreate();

    // 进行数据生成
    instance.generateCode(CodeBaseUtils.getBase());
  }
}
