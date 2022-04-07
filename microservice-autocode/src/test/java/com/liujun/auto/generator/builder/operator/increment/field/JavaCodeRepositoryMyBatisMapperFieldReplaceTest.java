package com.liujun.auto.generator.builder.operator.increment.field;

import com.liujun.auto.generator.builder.operator.code.CodeBaseUtils;
import com.liujun.auto.generator.builder.ddd.increment.field.JavaCodeRepositoryMyBatisMapperFieldReplace;
import com.liujun.auto.generator.builder.ddd.entity.GenerateCodeContext;
import org.junit.Test;

/**
 * 进行字段生成的单元测试
 *
 * @author liujun
 * @version 0.0.1
 */
public class JavaCodeRepositoryMyBatisMapperFieldReplaceTest {

  @Test
  public void testGenerate() {

    GenerateCodeContext context = CodeBaseUtils.getBase();

    JavaCodeRepositoryMyBatisMapperFieldReplace mapperFieldReplace =
        JavaCodeRepositoryMyBatisMapperFieldReplace.INSTANCE;

    // mapper的字段替换
    mapperFieldReplace.generateCode(context);
  }


}
