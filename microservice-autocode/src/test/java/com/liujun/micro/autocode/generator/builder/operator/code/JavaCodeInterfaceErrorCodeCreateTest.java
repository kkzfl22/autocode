package com.liujun.micro.autocode.generator.builder.operator.code;

import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeInterfaceErrorCodeCreate;
import org.junit.Test;

/**
 * 错误码的创建
 *
 * @author liujun
 * @version 0.0.1
 */
public class JavaCodeInterfaceErrorCodeCreateTest {

  @Test
  public void testGenerate() {
    JavaCodeInterfaceErrorCodeCreate instance = new JavaCodeInterfaceErrorCodeCreate();

    // 进行数据生成
    instance.generateCode(CodeBaseUtils.getBase());
  }
}
