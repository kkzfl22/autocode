package com.liujun.micro.autocode.generator.builder.operator.code;

import com.liujun.micro.autocode.generator.builder.entity.GenerateCodeContext;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeRepositoryObjectCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeRepositoryDaoInfCreate;
import org.junit.Test;

/**
 * 进行文件输出测试
 *
 * @author liujun
 * @version 0.0.1
 */
public class JavaCodeRepositoryDaoInfCreateTest {

  @Test
  public void testOutPersistObject() {

    GenerateCodeContext context = CodeBaseUtils.getBase();

    JavaCodeRepositoryObjectCreate poInstance = new JavaCodeRepositoryObjectCreate();
    JavaCodeRepositoryDaoInfCreate daoInstance = new JavaCodeRepositoryDaoInfCreate();

    // po优先生成
    poInstance.generateCode(context);
    // 再生成dao
    daoInstance.generateCode(context);
  }
}
