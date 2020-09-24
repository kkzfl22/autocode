package com.liujun.micro.autocode.generator.database.service;

import com.liujun.micro.autocode.generator.database.constant.DatabaseTypeEnum;
import com.liujun.micro.autocode.generator.database.entity.TableColumnDTO;
import com.liujun.micro.autocode.generator.database.service.table.DataValueInf;
import com.liujun.micro.autocode.generator.database.service.table.mysql.DataParseMysqlValueImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据库相关的值操作
 *
 * @author liujun
 * @version 0.0.1
 */
public class DatabaseValue {

  public static final DatabaseValue INSTANCE = new DatabaseValue();

  /** 数据库类型处理 */
  private static final Map<String, DataValueInf> DATABASE_PROC_MAP = new HashMap<>();

  static {
    // 与数据操作的值类型信息
    DATABASE_PROC_MAP.put(
        DatabaseTypeEnum.MYSQL.getDatabaseType(), DataParseMysqlValueImpl.INSTANCE);
  }

  /**
   * 进行数据的填充
   *
   * @param typeEnum 数据库类型信息
   * @param columnDTO 数据库列信息
   * @return 数据值
   */
  public String createValue(DatabaseTypeEnum typeEnum, TableColumnDTO columnDTO) {
    DataValueInf dataInstance = DATABASE_PROC_MAP.get(typeEnum.getDatabaseType());
    return dataInstance.createValue(columnDTO);
  }

  
}
