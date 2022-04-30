package com.liujun.auto.generator.database.service.table;

import com.liujun.auto.generator.database.constant.StandardTypeEnum;

/**
 * 数据库类型的获取
 *
 * @author liujun
 * @version 0.0.1
 */
public interface DataBaseTypeInf {

  /**
   * 数据库的类型获取到标准的类型信息
   *
   * @param dbType 数据库类型
   * @return 标识的类型枚举
   */
  StandardTypeEnum getDataType(String dbType);

  /**
   * 进行标识转换与长度检查
   *
   * @param dbType 数据库类型
   * @param length 长度
   * @return 标识的类型的key
   */
  StandardTypeEnum standardAndLengthCheck(String dbType, Long length);

  /**
   * 通过标准的key获取据库的类型
   *
   * @param standard 标准的类型
   * @return
   */
  String getDbType(StandardTypeEnum standard);

  /**
   * 根据指定的类型获取最大值
   *
   * @param dbType 数据库的类型
   * @return 返回最大值信息
   */
  Long getDataTypeMax(String dbType);
}
