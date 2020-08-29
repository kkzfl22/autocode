package com.liujun.micro.autocode.generator.builder.operator.code;

import com.liujun.micro.autocode.generator.builder.entity.GenerateCodeContext;
import com.liujun.micro.autocode.generator.builder.operator.ddd.JavaCodePersistMyBatisMapperCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.JavaCodePersistObjectCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.JavaCodeRepositoryDaoInfCreate;
import org.junit.Test;

/**
 * 测试生成mapper文件
 *
 * @author liujun
 * @version 0.0.1
 */
public class JavaCodePersistMyBatisMapperCreateTest {

  @Test
  public void testOutPersistObject() {

    GenerateCodeContext context = CodeBaseUtils.getBase();

    JavaCodePersistObjectCreate poInstance = new JavaCodePersistObjectCreate();
    JavaCodeRepositoryDaoInfCreate daoInstance = new JavaCodeRepositoryDaoInfCreate();
    JavaCodePersistMyBatisMapperCreate mapperInstance = new JavaCodePersistMyBatisMapperCreate();

    // po优先生成
    poInstance.generateCode(context);
    // 再生成dao
    daoInstance.generateCode(context);
    // 生成mapper文件
    mapperInstance.generateCode(context);
  }
}
