package com.liujun.micro.autocode.generator.database.service.table;

import com.liujun.micro.autocode.generator.database.constant.StandardTypeEnum;

/**
 * 数据库类型的获取
 *
 * @author liujun
 * @version 0.0.1
 */
public interface DataBaseTypeInf {

  /**
   * 通过
   *
   * @param mysqlDataType 数据库类型
   * @return 标识的类型枚举
   */
  StandardTypeEnum getDataType(String mysqlDataType);

  /**
   * 进行标识转换与长度检查
   *
   * @param mysqlType 数据库类型
   * @param length 长度
   * @return 标识的类型的key
   */
  StandardTypeEnum standardAndLengthCheck(String mysqlType, Integer length);
}
