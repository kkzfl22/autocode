package com.liujun.auto.generator.database.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 表名信息
 *
 * @author liujun
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class TableInfoDTO {

  /** 表名 */
  private String tableName;

  /** 表描述信息 */
  private String tableComment;

  /** 表的类型信息 */
  private String tableType;

  /** 列信息 */
  private List<TableColumnDTO> columnList;

  /** 以表名为key，再以列名为key */
  private Map<String, TableColumnDTO> columnMap;

  /** 索引名为key */
  private Map<String, TableIndexDTO> tableIndexMap;

  public TableInfoDTO() {}

  public TableInfoDTO(String tableName, String tableComment) {
    super();
    this.tableName = tableName;
    this.tableComment = tableComment;
  }

  /**
   * 进行列的设置操作
   *
   * @param tableMap
   * @param indexMap 索引信息
   */
  public static void setIndex(
      Map<String, TableInfoDTO> tableMap, Map<String, Map<String, TableIndexDTO>> indexMap) {

    for (Map.Entry<String, TableInfoDTO> itemTable : tableMap.entrySet()) {
      Map<String, TableIndexDTO> tableIndex = indexMap.get(itemTable.getKey());
      // 设置索引
      itemTable.getValue().setTableIndexMap(tableIndex);
    }
  }

  /**
   * 进行列的设置操作
   *
   * @param tableMap
   * @param columnMap
   */
  public static void setColumn(
      Map<String, TableInfoDTO> tableMap, Map<String, List<TableColumnDTO>> columnMap) {

    for (Map.Entry<String, TableInfoDTO> itemTable : tableMap.entrySet()) {
      List<TableColumnDTO> columnList = columnMap.get(itemTable.getKey());
      // 设置列集合
      itemTable.getValue().setColumnList(columnList);
      // 同是转换一个列的Map用于快速取数据
      itemTable.getValue().setColumnMap(parseColumnToMap(columnList));
    }
  }

  /**
   * 将列转换为Map 以列名为key，以列信息为值
   *
   * @param columnList 列集合
   * @return 列信息
   */
  private static Map<String, TableColumnDTO> parseColumnToMap(List<TableColumnDTO> columnList) {
    Map<String, TableColumnDTO> resultMap = new HashMap<>(columnList.size(), 1);

    for (TableColumnDTO columnInfo : columnList) {
      resultMap.put(columnInfo.getColumnName(), columnInfo);
    }

    return resultMap;
  }
}
