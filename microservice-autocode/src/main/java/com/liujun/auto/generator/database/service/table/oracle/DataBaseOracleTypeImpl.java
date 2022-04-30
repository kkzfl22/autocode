package com.liujun.auto.generator.database.service.table.oracle;

import com.liujun.auto.generator.database.constant.OracleDataTypeEnum;
import com.liujun.auto.generator.database.constant.StandardTypeEnum;
import com.liujun.auto.generator.database.service.table.DataBaseTypeInf;

/**
 * mysql的类型转换实现
 *
 * @author liujun
 * @version 0.0.1
 */
public class DataBaseOracleTypeImpl implements DataBaseTypeInf {

  public static final DataBaseOracleTypeImpl INSTANCE = new DataBaseOracleTypeImpl();

  @Override
  public StandardTypeEnum getDataType(String dbType) {
    StandardTypeEnum standardType = OracleDataTypeEnum.databaseToStandKey(dbType);

    return standardType;
  }

  @Override
  public StandardTypeEnum standardAndLengthCheck(String mysqlType, Long length) {
    // 将类型转换为标准的类型
    StandardTypeEnum standardType = OracleDataTypeEnum.standardAndLengthCheck(mysqlType, length);

    return standardType;
  }

  @Override
  public Long getDataTypeMax(String dbType) {

    // 获取类型信息
    OracleDataTypeEnum typeEnum = OracleDataTypeEnum.oracleDataType(dbType);

    if (typeEnum == null) {
      return null;
    }

    return typeEnum.getLengthEnd();
  }

  @Override
  public String getDbType(StandardTypeEnum standard) {
    return OracleDataTypeEnum.getOracleDbType(standard);
  }
}
