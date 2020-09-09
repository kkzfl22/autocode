package com.liujun.micro.autocode.generator.builder.operator.code;

import com.liujun.micro.autocode.generator.builder.entity.GenerateCodeContext;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeDomainObjectCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeInterfaceAssemblerCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeInterfaceObjectCreate;
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
    JavaCodeInterfaceObjectCreate dtoObjInstance = new JavaCodeInterfaceObjectCreate();
    JavaCodeDomainObjectCreate domainObjInstance = new JavaCodeDomainObjectCreate();
    JavaCodeInterfaceAssemblerCreate assemblerCreate = new JavaCodeInterfaceAssemblerCreate();

    GenerateCodeContext context = CodeBaseUtils.getBase();

    // 进行数据生成
    dtoObjInstance.generateCode(context);
    //领域对象生成
    domainObjInstance.generateCode(context);
    //转换对象
    assemblerCreate.generateCode(context);
  }
}
