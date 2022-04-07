package com.liujun.auto.generator.builder.operator.code;

import com.liujun.auto.generator.builder.ddd.full.repositorymybatis.JavaCodeRepositoryMyBatisObjectCreate;
import org.junit.Test;

/**
 * 进行persistObject生成的测试
 *
 * @author liujun
 * @version 0.0.1
 */
public class JavaCodeRepositoryObjectCreateTest {

  @Test
  public void testGenerate() {
    JavaCodeRepositoryMyBatisObjectCreate instance = new JavaCodeRepositoryMyBatisObjectCreate();

    // 进行数据生成
    instance.generateCode(CodeBaseUtils.getBase());
  }
}
