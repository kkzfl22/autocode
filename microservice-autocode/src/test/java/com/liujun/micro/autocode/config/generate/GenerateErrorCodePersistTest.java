package com.liujun.micro.autocode.config.generate;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * 进行错误码保存的测试
 *
 * @author liujun
 * @version 0.0.1
 */
public class GenerateErrorCodePersistTest {

  /** 执行错误码的操作 */
  @Test
  public void testErrorOperator() throws IOException {
    Integer value = -11110;
    System.out.println(new File(".").getAbsolutePath());
    System.out.println(new File(".").getPath());
    System.out.println(new File(".").toURI());
    System.out.println("======================");
    GenerateErrorCodePersist.INSTANCE.save(value);
    Integer loadValue = GenerateErrorCodePersist.INSTANCE.load();
    Assert.assertEquals(value, loadValue);
  }
}
