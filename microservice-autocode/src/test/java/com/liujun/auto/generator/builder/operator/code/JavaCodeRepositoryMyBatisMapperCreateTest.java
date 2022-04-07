package com.liujun.auto.generator.builder.operator.code;

import com.liujun.auto.generator.builder.operator.ddd.full.repositorymybatis.JavaCodeRepositoryMyBatisMapperInfCreate;
import com.liujun.auto.generator.builder.operator.ddd.full.repositorymybatis.JavaCodeRepositoryMyBatisMapperXmlCreate;
import com.liujun.auto.generator.builder.operator.ddd.full.repositorymybatis.JavaCodeRepositoryMyBatisObjectCreate;
import com.liujun.auto.generator.builder.entity.GenerateCodeContext;
import org.junit.Test;

/**
 * 测试生成mapper文件
 *
 * @author liujun
 * @version 0.0.1
 */
public class JavaCodeRepositoryMyBatisMapperCreateTest {

  @Test
  public void testGenerate() {

    GenerateCodeContext context = CodeBaseUtils.getBase();

    JavaCodeRepositoryMyBatisObjectCreate poInstance = new JavaCodeRepositoryMyBatisObjectCreate();
    JavaCodeRepositoryMyBatisMapperInfCreate daoInstance = new JavaCodeRepositoryMyBatisMapperInfCreate();
    JavaCodeRepositoryMyBatisMapperXmlCreate mapperInstance = new JavaCodeRepositoryMyBatisMapperXmlCreate();

    // po优先生成
    poInstance.generateCode(context);
    // 再生成dao
    daoInstance.generateCode(context);
    // 生成mapper文件
    mapperInstance.generateCode(context);
  }
}
