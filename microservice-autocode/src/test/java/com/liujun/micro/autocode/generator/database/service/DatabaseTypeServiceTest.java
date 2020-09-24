package com.liujun.micro.autocode.generator.database.service;

import com.liujun.micro.autocode.generator.database.constant.DatabaseTypeEnum;
import com.liujun.micro.autocode.generator.database.constant.StandardTypeEnum;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author liujun
 * @version 0.0.1
 */
public class DatabaseTypeServiceTest {

  /** 测试获取数据 */
  @Test
  public void testGetDatabaseType() {
    StandardTypeEnum dataType =
        DatabaseTypeService.INSTANCE.getDatabaseType(DatabaseTypeEnum.MYSQL, "VARCAHR");
    Assert.assertEquals(StandardTypeEnum.VARCHAR, dataType);
  }
}
