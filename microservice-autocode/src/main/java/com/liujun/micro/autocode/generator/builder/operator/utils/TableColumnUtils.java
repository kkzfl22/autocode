package com.liujun.micro.autocode.generator.builder.operator.utils;

import com.liujun.micro.autocode.constant.Symbol;
import com.liujun.micro.autocode.generator.database.entity.TableColumnDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * 表列信息的公共处理
 *
 * @author liujun
 * @version 0.0.1
 */
public class TableColumnUtils {

  /**
   * 获取主键的key的信息 方法描述
   *
   * <p>支持单主键与联合主键
   *
   * @param list 表的列信息
   * @return 主键列信息
   */
  public static List<TableColumnDTO> getPrimaryKey(List<TableColumnDTO> list) {

    List<TableColumnDTO> primaryKey = new ArrayList<>();

    if (list == null || list.isEmpty()) {
      return primaryKey;
    }

    for (TableColumnDTO tableBean : list) {
      if (tableBean.getPrimaryKey()) {
        primaryKey.add(tableBean);
      }
    }

    // 如果未定义主键，就使用第一个列
    if (primaryKey.isEmpty()) {
      primaryKey.add(list.get(0));
    }

    return primaryKey;
  }

  /**
   * 进行特殊字符的处理操作
   *
   * @param comment 特殊字符
   * @return
   */
  public static String specialChar(String comment) {
    String result = comment;

    // 替换掉-号替换成*号
    result = result.replaceAll(Symbol.MINUS, Symbol.STAR);

    return result;
  }

  /**
   * 获取指定的列
   *
   * @param columnList 列集合
   * @param inConditionItem 作为in条件的列
   * @return 列信息
   */
  public static TableColumnDTO getColumn(List<TableColumnDTO> columnList, String inConditionItem) {
    for (TableColumnDTO tableColumnItem : columnList) {
      // 如果能列名能匹配上
      if (tableColumnItem.getColumnName().equalsIgnoreCase(inConditionItem)) {
        return tableColumnItem;
      }
    }

    throw new IllegalArgumentException("condition :" + inConditionItem + "not exists!");
  }
}
