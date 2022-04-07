package com.liujun.auto.generator.database.service.table;

import com.liujun.auto.generator.database.entity.TableColumnDTO;
import com.liujun.auto.generator.database.entity.TableInfoDTO;

import java.util.List;
import java.util.Map;

/**
 * 数据库操作的接口
 *
 * @author liujun
 * @version 1.0.0
 */
public interface DatabaseProcessInf {

  /**
   * 获得表信息
   *
   * @param tableFlag 标识信息，当为数据库时，为表空间。doc文件时，为文件名
   * @return 表信息
   */
  Map<String, TableInfoDTO> getTableInfo(String tableFlag);

  /**
   * 获得表列的信息
   *
   * @param tableFlag 标识信息，当为数据库时，为表空间。doc文件时，为文件名
   * @return 表的所有表信息
   */
  Map<String, List<TableColumnDTO>> getTableColumn(String tableFlag);
}
