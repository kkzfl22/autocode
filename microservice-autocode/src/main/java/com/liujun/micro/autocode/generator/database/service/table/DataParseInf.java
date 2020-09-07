package com.liujun.micro.autocode.generator.database.service.table;

import com.liujun.micro.autocode.generator.database.entity.TableColumnDTO;

/**
 * 数据转换操作接口
 *
 * @author liujun
 * @version 0.0.1
 */
public interface DataParseInf {

  /**
   * 进行数据的填充
   *
   * @param bean 数据库列信息
   * @return 数据值
   */
  String createValue(TableColumnDTO bean);

  /**
   * 通过数据库的类型转换为java的类型
   *
   * @param bean 数据列信息
   * @return 类型
   */
  String parseJavaType(TableColumnDTO bean);
}
