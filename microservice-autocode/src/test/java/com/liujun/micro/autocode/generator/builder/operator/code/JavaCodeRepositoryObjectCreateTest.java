package com.liujun.micro.autocode.generator.builder.operator.code;

import com.liujun.micro.autocode.generator.builder.operator.ddd.JavaCodeRepositoryDaoInfCreate;
import org.junit.Test;

/**
 * 进行persistObject生成的测试
 *
 * @author liujun
 * @version 0.0.1
 */
public class JavaCodeRepositoryObjectCreateTest {

  @Test
  public void testOutPersistObject() {
    JavaCodeRepositoryDaoInfCreate instance = new JavaCodeRepositoryDaoInfCreate();

    // 进行数据生成
    instance.generateCode(CodeBaseUtils.getBase());
  }
}
