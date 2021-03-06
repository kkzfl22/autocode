package com.liujun.micro.autocode.generator.database.service.table.mysql;

import com.liujun.micro.autocode.generator.database.constant.MysqlDataTypeEnum;
import com.liujun.micro.autocode.generator.database.constant.StandardTypeEnum;
import com.liujun.micro.autocode.generator.database.service.table.DataBaseTypeInf;

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
  public StandardTypeEnum standardAndLengthCheck(String mysqlType, Integer length) {
    // 将类型转换为标准的类型
    StandardTypeEnum standardType = MysqlDataTypeEnum.standardAndLengthCheck(mysqlType, length);

    return standardType;
  }

  @Override
  public Integer getDataTypeMax(String dbType) {

    // 获取类型信息
    MysqlDataTypeEnum typeEnum = MysqlDataTypeEnum.mysqlDataType(dbType);

    if (typeEnum == null) {
      return null;
    }

    return (int) ((long) typeEnum.getLengthEnd());
  }
}
