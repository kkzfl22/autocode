package com.liujun.auto.generator.database.service.table.mysql;

import com.liujun.auto.generator.database.constant.MysqlDataTypeEnum;
import com.liujun.auto.generator.database.constant.StandardTypeEnum;
import com.liujun.auto.generator.database.service.table.DataBaseTypeInf;

/**
 * mysql的类型转换实现
 *
 * @author liujun
 * @version 0.0.1
 */
public class DataBaseMysqlTypeImpl implements DataBaseTypeInf {

  public static final DataBaseMysqlTypeImpl INSTANCE = new DataBaseMysqlTypeImpl();

  @Override
  public StandardTypeEnum getDataType(String mysqlDataType) {
    StandardTypeEnum standardType = MysqlDataTypeEnum.databaseToStandKey(mysqlDataType);

    return standardType;
  }

  @Override
  public StandardTypeEnum standardAndLengthCheck(String mysqlType, Long length) {
    // 将类型转换为标准的类型
    StandardTypeEnum standardType = MysqlDataTypeEnum.standardAndLengthCheck(mysqlType, length);

    return standardType;
  }

  @Override
  public Long getDataTypeMax(String dbType) {

    // 获取类型信息
    MysqlDataTypeEnum typeEnum = MysqlDataTypeEnum.mysqlDataType(dbType);

    if (typeEnum == null) {
      return null;
    }

    return typeEnum.getLengthEnd();
  }
}
