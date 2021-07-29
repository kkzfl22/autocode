package com.liujun.micro.autocode.generator.database.service;

import com.liujun.micro.autocode.generator.database.constant.DatabaseTypeEnum;
import com.liujun.micro.autocode.generator.database.constant.StandardTypeEnum;
import com.liujun.micro.autocode.generator.database.service.table.DataBaseTypeInf;
import com.liujun.micro.autocode.generator.database.service.table.mysql.DataBaseMysqlTypeImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liujun
 * @version 0.0.1
 */
public class DatabaseTypeService {

  public static final DatabaseTypeService INSTANCE = new DatabaseTypeService();

  /** 数据库类型处理 */
  private static final Map<String, DataBaseTypeInf> DATABASE_TYPE_MAP = new HashMap<>();

  static {
    // 加载mysql的数据库查询实现
    DATABASE_TYPE_MAP.put(DatabaseTypeEnum.MYSQL.getDatabaseType(), DataBaseMysqlTypeImpl.INSTANCE);
  }

  /**
   * 获取数据库类型的枚举相关信息
   *
   * @param typeEnum
   * @return
   */
  public StandardTypeEnum getDatabaseType(DatabaseTypeEnum typeEnum, String dataType) {
    DataBaseTypeInf typeInfo = DATABASE_TYPE_MAP.get(typeEnum.getDatabaseType());

    if (typeInfo != null) {
      return typeInfo.getDataType(dataType);
    }

    return null;
  }

  /**
   * 标准的长度检查操作
   *
   * @param typeEnum 类型枚举
   * @param mysqlType 数据库的类型
   * @param length 长度
   * @return 标识的key
   */
  public StandardTypeEnum standardAndLengthCheck(
      DatabaseTypeEnum typeEnum, String mysqlType, Long length) {
    DataBaseTypeInf typeInfo = DATABASE_TYPE_MAP.get(typeEnum.getDatabaseType());

    if (typeInfo != null) {
      return typeInfo.standardAndLengthCheck(mysqlType, length);
    }

    return null;
  }

  /**
   * 获取数据类型的最大长度
   *
   * @param typeEnum 类型枚举
   * @param databaseType 数据库的类型
   * @return 标识的key
   */
  public Long getDatabaseTypeLength(DatabaseTypeEnum typeEnum, String databaseType) {
    DataBaseTypeInf typeInfo = DATABASE_TYPE_MAP.get(typeEnum.getDatabaseType());

    if (typeInfo != null) {
      return typeInfo.getDataTypeMax(databaseType);
    }

    return null;
  }
}
