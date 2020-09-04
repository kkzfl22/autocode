package com.liujun.micro.autocode.generator.builder.operator.code;

import com.liujun.micro.autocode.generator.builder.entity.GenerateCodeContext;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeRepositoryMyBatisMapperCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeRepositoryObjectCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeRepositoryDaoInfCreate;
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

    JavaCodeRepositoryObjectCreate poInstance = new JavaCodeRepositoryObjectCreate();
    JavaCodeRepositoryDaoInfCreate daoInstance = new JavaCodeRepositoryDaoInfCreate();
    JavaCodeRepositoryMyBatisMapperCreate mapperInstance = new JavaCodeRepositoryMyBatisMapperCreate();

    // po优先生成
    poInstance.generateCode(context);
    // 再生成dao
    daoInstance.generateCode(context);
    // 生成mapper文件
    mapperInstance.generateCode(context);
  }
}
