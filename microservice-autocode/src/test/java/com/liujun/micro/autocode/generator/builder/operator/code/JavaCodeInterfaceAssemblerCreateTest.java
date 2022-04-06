package com.liujun.micro.autocode.generator.builder.operator.code;

import com.liujun.micro.autocode.generator.builder.operator.ddd.full.facade.JavaCodeFacadeObjectCreate;
import org.junit.Test;

/**
 * 进行persistObject生成的测试
 *
 * @author liujun
 * @version 0.0.1
 */
public class JavaCodeInterfaceAssemblerCreateTest {

  @Test
  public void testGenerate() {
    JavaCodeFacadeObjectCreate instance = new JavaCodeFacadeObjectCreate();

    // 进行数据生成
    instance.generateCode(CodeBaseUtils.getBase());
  }
}
