package com.liujun.auto.generator.builder.operator.code;

import com.liujun.auto.generator.builder.ddd.full.facade.JavaCodeFacadeErrorCodeCreate;
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
    JavaCodeFacadeErrorCodeCreate instance = new JavaCodeFacadeErrorCodeCreate();

    // 进行数据生成
    instance.generateCode(CodeBaseUtils.getBase());
  }
}
