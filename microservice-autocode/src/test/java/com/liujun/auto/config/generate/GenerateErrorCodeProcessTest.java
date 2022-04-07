package com.liujun.auto.config.generate;

import org.junit.Assert;
import org.junit.Test;

/**
 * 文件操作
 *
 * @author liujun
 * @version 0.0.1
 */
public class GenerateErrorCodeProcessTest {

  @Test
  public void testLoader() {
    for (int i = 0; i < 10; i++) {
      int start = GenerateErrorCodeProcess.INSTANCE.getStartCode();
      int assertValue = start;
      for (int j = 0; j < 5; j++) {
        assertValue = assertValue + GenerateErrorCodeProcess.INCREMENT_VALUE;
        int value = GenerateErrorCodeProcess.INSTANCE.increment();
        Assert.assertEquals(assertValue, value);
        System.out.println(value);
      }
    }
  }
}
