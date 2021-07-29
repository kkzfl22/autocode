package com.liujun.micro.autocode.generator.builder.operator.code;

import com.liujun.micro.autocode.generator.builder.entity.GenerateCodeContext;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeRepositoryObjectCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeRepositoryMapperInfCreate;
import org.junit.Test;

/**
 * 进行文件输出测试
 *
 * @author liujun
 * @version 0.0.1
 */
public class JavaCodeRepositoryMapperInfCreateTest {

  @Test
  public void testGenerate() {

    GenerateCodeContext context = CodeBaseUtils.getBase();

    JavaCodeRepositoryObjectCreate poInstance = new JavaCodeRepositoryObjectCreate();
    JavaCodeRepositoryMapperInfCreate daoInstance = new JavaCodeRepositoryMapperInfCreate();

    // po优先生成
    poInstance.generateCode(context);
    // 再生成dao
    daoInstance.generateCode(context);
  }
}
