package com.liujun.micro.autocode.generator.builder.operator.code;

import com.liujun.micro.autocode.generator.builder.entity.GenerateCodeContext;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeInterfaceCheckCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeInterfaceErrorCodeCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeInterfaceObjectCreate;
import org.junit.Test;

/**
 * 错误验证的代码
 *
 * @author liujun
 * @version 0.0.1
 */
public class JavaCodeInterfaceCheckCreateTest {

  @Test
  public void testGenerate() {
    JavaCodeInterfaceErrorCodeCreate instance = new JavaCodeInterfaceErrorCodeCreate();
    JavaCodeInterfaceObjectCreate dtoObjInstance = new JavaCodeInterfaceObjectCreate();
    JavaCodeInterfaceCheckCreate checkInstance = new JavaCodeInterfaceCheckCreate();

    GenerateCodeContext context = CodeBaseUtils.getBase();

    // 进行数据生成
    instance.generateCode(context);
    dtoObjInstance.generateCode(context);
    checkInstance.generateCode(context);
  }
}
