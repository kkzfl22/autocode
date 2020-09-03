package com.liujun.micro.autocode.generator.builder.operator.code;

import com.liujun.micro.autocode.generator.builder.entity.GenerateCodeContext;
import com.liujun.micro.autocode.generator.builder.operator.ddd.JavaCodeRepositoryDaoInfCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.JavaCodeRepositoryJunitDaoCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.JavaCodeRepositoryMyBatisMapperCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.JavaCodeRepositoryObjectCreate;
import org.junit.Test;

/**
 * @author liujun
 * @version 0.0.1
 */
public class JavaCodeRepositoryJunitDaoCreateTest {

  @Test
  public void testOutPersistObject() {

    GenerateCodeContext context = CodeBaseUtils.getBase();

    JavaCodeRepositoryObjectCreate poInstance = new JavaCodeRepositoryObjectCreate();
    JavaCodeRepositoryDaoInfCreate daoInstance = new JavaCodeRepositoryDaoInfCreate();
    JavaCodeRepositoryJunitDaoCreate junitDaoInstance = new JavaCodeRepositoryJunitDaoCreate();
    JavaCodeRepositoryMyBatisMapperCreate mapperInstance =
            new JavaCodeRepositoryMyBatisMapperCreate();

    // po优先生成
    poInstance.generateCode(context);
    // 再生成dao
    daoInstance.generateCode(context);
    // 生成数据库的单元测试
    junitDaoInstance.generateCode(context);
    // 生成mapper文件
    mapperInstance.generateCode(context);
  }
}
