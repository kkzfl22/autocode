package com.liujun.micro.autocode.generator.database.service;

import com.liujun.micro.autocode.config.generate.GenerateConfig;
import com.liujun.micro.autocode.generator.database.constant.DatabaseTypeEnum;
import com.liujun.micro.autocode.generator.database.entity.TableColumnDTO;
import com.liujun.micro.autocode.generator.database.entity.TableInfoDTO;
import com.liujun.micro.autocode.generator.database.service.tableInfo.DatabaseProcessInf;
import com.liujun.micro.autocode.generator.database.service.tableInfo.mysql.DatabaseMysqlProcessImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据库对外统一服务类
 *
 * @author liujun
 * @version 0.0.1
 */
public class DatabaseOperator {

  public static final DatabaseOperator INSTANCE = new DatabaseOperator();

  /** 数据库类型处理 */
  private static final Map<String, DatabaseProcessInf> DATABASE_PROC_MAP = new HashMap<>();

  static {
    // 加载mysql的数据库查询实现
    DATABASE_PROC_MAP.put(
        DatabaseTypeEnum.MYSQL.getDatabaseType(), DatabaseMysqlProcessImpl.INSTANCE);
  }

  /**
   * 获取数据库表结构信息
   *
   * @param tableSpace
   * @return
   */
  public Map<String, TableInfoDTO> getTableInfo(String tableSpace) {
    String dbType = GenerateConfig.INSTANCE.getCfgEntity().getGenerate().getDatabaseType();
    DatabaseProcessInf tableInfo = DATABASE_PROC_MAP.get(dbType);
    if (null == tableInfo) {
      throw new IllegalArgumentException(dbType + " type not exists");
    }
    return tableInfo.getTableInfo(tableSpace);
  }

  /**
   * 获取列结构信息
   *
   * @param tableSpace
   * @return
   */
  public Map<String, List<TableColumnDTO>> getColumnInfo(String tableSpace) {
    String dbType = GenerateConfig.INSTANCE.getCfgEntity().getGenerate().getDatabaseType();
    DatabaseProcessInf tableInfo = DATABASE_PROC_MAP.get(dbType);
    if (null == tableInfo) {
      throw new IllegalArgumentException(dbType + " type not exists");
    }
    return tableInfo.getTableColumn(tableSpace);
  }

  /**
   * 将列信息转化为以列名为key,的map
   *
   * @param tableColumnList 表集合数据
   * @return 二层map，一层以表名为key,二层以列名为key
   */
  public Map<String, Map<String, TableColumnDTO>> parseColumnMap(
      Map<String, List<TableColumnDTO>> tableColumnList) {

    Map<String, Map<String, TableColumnDTO>> result = new HashMap<>(tableColumnList.size());

    Map<String, TableColumnDTO> tableColumnMap = null;

    for (Map.Entry<String, List<TableColumnDTO>> entryItem : tableColumnList.entrySet()) {
      tableColumnMap = result.get(entryItem.getKey());

      if (null == tableColumnMap) {
        tableColumnMap = new HashMap<>(entryItem.getValue().size());
        result.put(entryItem.getKey(), tableColumnMap);
      }

      // 再以列名为key
      for (TableColumnDTO tableColumn : entryItem.getValue()) {
        tableColumnMap.put(tableColumn.getColumnName(), tableColumn);
      }
    }

    return result;
  }
}
