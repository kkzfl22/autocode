package com.liujun.micro.autocode.generator.database.service;

import com.liujun.micro.autocode.config.generate.GenerateConfig;
import com.liujun.micro.autocode.generator.database.constant.DatabaseTypeEnum;
import com.liujun.micro.autocode.generator.database.entity.TableColumnDTO;
import com.liujun.micro.autocode.generator.database.entity.TableInfoDTO;
import com.liujun.micro.autocode.generator.database.service.tableInfo.DataParseInf;
import com.liujun.micro.autocode.generator.database.service.tableInfo.DatabaseProcessInf;
import com.liujun.micro.autocode.generator.database.service.tableInfo.mysql.DataParseMysqlParseImpl;
import com.liujun.micro.autocode.generator.database.service.tableInfo.mysql.DatabaseMysqlProcessImpl;

import java.util.HashMap;
import java.util.List;
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
  private static final Map<String, DataParseInf> DATABASE_PROC_MAP = new HashMap<>();

  static {
    // 与数据操作的值类型信息
    DATABASE_PROC_MAP.put(
        DatabaseTypeEnum.MYSQL.getDatabaseType(), DataParseMysqlParseImpl.INSTANCE);
  }

  /**
   * 进行数据的填充
   *
   * @param typeEnum 数据库类型信息
   * @param columnDTO 数据库列信息
   * @return 数据值
   */
  public String createValue(DatabaseTypeEnum typeEnum, TableColumnDTO columnDTO) {
    DataParseInf dataInstance = DATABASE_PROC_MAP.get(typeEnum.getDatabaseType());
    return dataInstance.createValue(columnDTO);
  }

  /**
   * 通过数据库的类型转换为java的类型
   *
   * @param bean 数据列信息
   * @return 类型
   */
  public String parseJavaType(DatabaseTypeEnum typeEnum, TableColumnDTO bean) {
    DataParseInf dataInstance = DATABASE_PROC_MAP.get(typeEnum.getDatabaseType());
    return dataInstance.parseJavaType(bean);
  }
}
