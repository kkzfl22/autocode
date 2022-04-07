package com.liujun.auto.generator.builder.operator.code;

import com.liujun.auto.generator.builder.entity.GenerateCodeContext;
import com.liujun.auto.generator.builder.operator.ddd.full.domain.JavaCodeDomainObjectCreate;
import com.liujun.auto.generator.builder.operator.ddd.full.facade.JavaCodeFacadeAssemblerCreate;
import com.liujun.auto.generator.builder.operator.ddd.full.facade.JavaCodeFacadeObjectCreate;
import org.junit.Test;

/**
 * 进行persistObject生成的测试
 *
 * @author liujun
 * @version 0.0.1
 */
public class JavaCodeInterfaceObjectCreateTest {

  @Test
  public void testGenerate() {
    JavaCodeFacadeObjectCreate dtoObjInstance = new JavaCodeFacadeObjectCreate();
    JavaCodeDomainObjectCreate domainObjInstance = new JavaCodeDomainObjectCreate();
    JavaCodeFacadeAssemblerCreate assemblerCreate = new JavaCodeFacadeAssemblerCreate();

    GenerateCodeContext context = CodeBaseUtils.getBase();

    // 进行数据生成
    dtoObjInstance.generateCode(context);
    //领域对象生成
    domainObjInstance.generateCode(context);
    //转换对象
    assemblerCreate.generateCode(context);
  }
}
