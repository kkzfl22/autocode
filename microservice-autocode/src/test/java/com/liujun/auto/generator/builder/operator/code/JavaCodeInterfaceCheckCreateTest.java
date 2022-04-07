package com.liujun.auto.generator.builder.operator.code;

import com.liujun.auto.generator.builder.entity.GenerateCodeContext;
import com.liujun.auto.generator.builder.operator.ddd.full.facade.JavaCodeFacadeCheckCreate;
import com.liujun.auto.generator.builder.operator.ddd.full.facade.JavaCodeFacadeConstantCreate;
import com.liujun.auto.generator.builder.operator.ddd.full.facade.JavaCodeFacadeErrorCodeCreate;
import com.liujun.auto.generator.builder.operator.ddd.full.facade.JavaCodeFacadeObjectCreate;
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
    JavaCodeFacadeErrorCodeCreate instance = new JavaCodeFacadeErrorCodeCreate();
    JavaCodeFacadeObjectCreate dtoObjInstance = new JavaCodeFacadeObjectCreate();
    JavaCodeFacadeConstantCreate constantInstance = new JavaCodeFacadeConstantCreate();
    JavaCodeFacadeCheckCreate checkInstance = new JavaCodeFacadeCheckCreate();

    GenerateCodeContext context = CodeBaseUtils.getBase();

    // 进行数据生成
    instance.generateCode(context);
    dtoObjInstance.generateCode(context);
    constantInstance.generateCode(context);
    checkInstance.generateCode(context);
  }
}
