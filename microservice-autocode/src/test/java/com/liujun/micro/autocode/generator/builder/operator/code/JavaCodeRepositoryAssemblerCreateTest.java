package com.liujun.micro.autocode.generator.builder.operator.code;

import com.liujun.micro.autocode.generator.builder.entity.GenerateCodeContext;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeDomainObjectCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeRepositoryAssemblerCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeRepositoryObjectCreate;
import org.junit.Test;

/**
 * @author liujun
 * @version 0.0.1
 */
public class JavaCodeRepositoryAssemblerCreateTest {

  @Test
  public void testGenerate() {
    JavaCodeDomainObjectCreate instance = new JavaCodeDomainObjectCreate();
    JavaCodeRepositoryObjectCreate repositoryInstance = new JavaCodeRepositoryObjectCreate();
    JavaCodeRepositoryAssemblerCreate assembler = new JavaCodeRepositoryAssemblerCreate();

    GenerateCodeContext context = CodeBaseUtils.getBase();

    // 进行数据生成
    instance.generateCode(context);
    // 进行数据生成
    repositoryInstance.generateCode(context);
    // 转换类
    assembler.generateCode(context);
  }
}
