package com.liujun.micro.autocode.generator.builder.operator.utils;

import com.liujun.micro.autocode.constant.Symbol;
import com.liujun.micro.autocode.generator.convergence.TypeConvergence;
import com.liujun.micro.autocode.generator.database.constant.DatabaseTypeEnum;
import com.liujun.micro.autocode.generator.database.entity.TableColumnDTO;
import com.liujun.micro.autocode.generator.javalanguage.constant.JavaDataType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 表列信息的公共处理
 *
 * @author liujun
 * @version 0.0.1
 */
public class TableColumnUtils {

  private TableColumnUtils() {}

  /** 主键 */
  private static final String ID = "id";

  /**
   * 获取主键的key的信息 方法描述
   *
   * <p>支持单主键与联合主键
   *
   * @param list 表的列信息
   * @return 主键列信息
   */
  public static List<TableColumnDTO> getPrimaryKey(List<TableColumnDTO> list) {

    if (list == null || list.isEmpty()) {
      return Collections.emptyList();
    }

    List<TableColumnDTO> primaryKey = new ArrayList<>();

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
   * 将数据集转换为map
   *
   * @param list 数据集信息
   * @return
   */
  public static Map<String, TableColumnDTO> parseToMap(List<TableColumnDTO> list) {

    if (null == list || list.isEmpty()) {
      return Collections.emptyMap();
    }

    Map<String, TableColumnDTO> parseMap = new HashMap<>(list.size());

    for (TableColumnDTO columnInfo : list) {
      parseMap.put(columnInfo.getColumnName(), columnInfo);
    }

    return parseMap;
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

    throw new IllegalArgumentException("condition :" + inConditionItem + " not exists!");
  }

  /**
   * 检查当前是否使用自增长的uid
   *
   * @param columnList 列信息
   * @return true 顾在使用long的主键 , false 不存在
   */
  public static boolean primaryKeyUid(List<TableColumnDTO> columnList, DatabaseTypeEnum typeEnum) {
    if (null == columnList || columnList.isEmpty()) {
      return Boolean.FALSE;
    }

    for (TableColumnDTO column : columnList) {
      String javaType = TypeConvergence.getJavaType(typeEnum, column.getDataType());

      if (ID.equals(column.getColumnName()) && JavaDataType.LONG.getType().equals(javaType)) {
        return true;
      }
    }

    return false;
  }
}
