package com.liujun.micro.autocode.generator.database.service.tableInfo.mysql;

import com.liujun.micro.autocode.generator.database.entity.TableColumnDTO;
import com.liujun.micro.autocode.generator.database.entity.TableInfoDTO;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * 测试mysql的数据库连接操作
 *
 * @author liujun
 * @version 0.0.1
 */
public class DatabaseMysqlProcessImplTest {

  @Test
  public void testGetTableColumn() {
    Map<String, List<TableColumnDTO>> columnMap =
        DatabaseMysqlProcessImpl.INSTANCE.getTableColumn("autocode");

    System.out.println(columnMap);
    Assert.assertNotNull(columnMap);
  }

  @Test
  public void testGetAllTable() {
    Map<String, TableInfoDTO> tableMap =
        DatabaseMysqlProcessImpl.INSTANCE.getTableInfo("autocode");

    System.out.println(tableMap);
    Assert.assertNotNull(tableMap);
  }
}
