package com.liujun.auto.generator.builder.operator.code;

import com.liujun.auto.generator.builder.operator.ddd.full.repositorymybatis.JavaCodeRepositoryMyBatisMapperInfCreate;
import com.liujun.auto.generator.builder.operator.ddd.full.repositorymybatis.JavaCodeRepositoryMyBatisObjectCreate;
import com.liujun.auto.generator.builder.entity.GenerateCodeContext;
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

    JavaCodeRepositoryMyBatisObjectCreate poInstance = new JavaCodeRepositoryMyBatisObjectCreate();
    JavaCodeRepositoryMyBatisMapperInfCreate daoInstance = new JavaCodeRepositoryMyBatisMapperInfCreate();

    // po优先生成
    poInstance.generateCode(context);
    // 再生成dao
    daoInstance.generateCode(context);
  }
}
