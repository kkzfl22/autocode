package com.liujun.auto.config.properties;

import com.liujun.auto.constant.ConfigEnum;
import org.junit.Assert;
import org.junit.Test;

/**
 * 测试配制文件加载
 *
 * @author liujun
 * @version 0.0.1
 */
public class ConfigPropertiesTest {

  @Test
  public void testLoad() {
    String value = ConfigProperties.getInstance().getValue(ConfigEnum.DATABASE_TYPE);
    System.out.println(value);
    Assert.assertNotNull(value);
  }
}
