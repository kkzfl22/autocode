package com.liujun.auto.generator.builder.operator.code;

import com.liujun.auto.generator.builder.ddd.full.repositorymybatis.JavaCodeRepositoryMyBatisObjectCreate;
import com.liujun.auto.generator.builder.ddd.full.repositorymybatis.JavaCodeRepositoryMybatisConverterCreate;
import com.liujun.auto.generator.builder.ddd.entity.GenerateCodeContext;
import com.liujun.auto.generator.builder.ddd.full.domain.JavaCodeDomainObjectCreate;
import org.junit.Test;

/**
 * @author liujun
 * @version 0.0.1
 */
public class JavaCodeRepositoryAssemblerCreateTest {

  @Test
  public void testGenerate() {
    JavaCodeDomainObjectCreate instance = new JavaCodeDomainObjectCreate();
    JavaCodeRepositoryMyBatisObjectCreate repositoryInstance = new JavaCodeRepositoryMyBatisObjectCreate();
    JavaCodeRepositoryMybatisConverterCreate assembler = new JavaCodeRepositoryMybatisConverterCreate();

    GenerateCodeContext context = CodeBaseUtils.getBase();

    // 进行数据生成
    instance.generateCode(context);
    // 进行数据生成
    repositoryInstance.generateCode(context);
    // 转换类
    assembler.generateCode(context);
  }
}
